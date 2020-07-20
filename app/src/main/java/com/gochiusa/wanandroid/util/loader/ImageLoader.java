package com.gochiusa.wanandroid.util.loader;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    /**
     * 内存缓存
     */
    final Cache memoryCache;

    /**
     *  线程调度器
     */
    final Dispatcher dispatcher;

    /**
     * 线程池
     */
    final ExecutorService executorService;

    final Context context;

    /**
     *  缓存请求的映射
     */
    final Map<Object, Action> targetToAction;

    /**
     *  下载器
     */
    private Downloader downLoader;

    static ImageLoader singleton;

    ImageLoader(Context context, ExecutorService executorService,
                Downloader downLoader, Cache cache, Dispatcher dispatcher) {
        this.context = context;
        this.executorService = executorService;
        this.downLoader = downLoader;
        this.memoryCache = cache;
        targetToAction = new HashMap<>();
        this.dispatcher = dispatcher;
    }

    /**
     * 采用Double Check Lock方式获取单例
     */
    public static ImageLoader with(@NonNull Context context) {

        if (singleton == null) {
            synchronized (ImageLoader.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }

    /**
     *  创建{@code Action}的建造类{@code ActionCreator}
     * @param path 图片的加载地址，目前只支持Http或Https协议
     */
    public ActionCreator load(String path) {
        return new ActionCreator(this, path);
    }

    void enqueueAndSubmit(Action action) {
        Object target = action.getTarget();
        if (target != null && targetToAction.get(target) != action) {
            // 如果存在旧的Action，则取消之
            cancelRequest(target);
            targetToAction.put(target, action);
        }
        submit(action);
    }

    void submit(Action action) {
        dispatcher.performSubmit(action);
    }

    void cancelRequest(Object target) {
        // 获取对应的Action
        Action action = targetToAction.remove(target);
        if (action != null) {
            action.cancel();
            dispatcher.performCancel(action);
        }
    }

    /**
     *  加载结束后将回调这个方法
     * @param worker 加载结束后封装了结果的{@code Worker}
     */
    void complete(@NonNull Worker worker) {
        Action action = worker.getAction();
        if (action.isCancelled()) {
            // 如果Action已经被标记取消，则直接返回
            return;
        }
        // 清除Action对应的键值对
        targetToAction.remove(action.getTarget());
        Bitmap result = worker.getResult();
        if (result != null) {
            // 如果能够顺利获取位图
            memoryCache.set(action.key, result);
            action.complete(result);
        } else {
            // 不能则回调error()
            action.error();
        }
    }

    /**
     *  从内存缓存中尝试读取位图信息
     * @param key 能够唯一确定位图的键值
     * @return 如果内存缓存中没有找到键值对应的位图，则返回null
     */
    @Nullable
    Bitmap quickMemoryCacheCheck(String key) {
        return memoryCache.get(key);
    }


    /**
     *  建造者模式
     */
    public static class Builder {
        private Context context;
        private ExecutorService executorService;
        private Downloader downloader;
        private Cache cache;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder setDownloader(@NonNull Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        public Builder setExecutorService(@NonNull ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder setCache(@NonNull Cache cache) {
            this.cache = cache;
            return this;
        }

        public ImageLoader build() {
            if (this.cache == null) {
                this.cache = new MemoryCache(context);
            }
            if (this.downloader == null) {
                HttpDownloader httpDownloader = new HttpDownloader(context);
                // 设置内存缓存淘汰数据时的监听接口
                cache.setOnRemoveListener(httpDownloader.createMemoryCacheRemoveListener());
                this.downloader = new HttpDownloader(context);
            }
            if (this.executorService == null) {
                this.executorService = Executors.newCachedThreadPool();
            }
            Dispatcher dispatcher = new Dispatcher(downloader, executorService);
            return new ImageLoader(context, executorService, downloader, cache, dispatcher);
        }

    }
}
