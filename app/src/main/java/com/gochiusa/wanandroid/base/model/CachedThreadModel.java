package com.gochiusa.wanandroid.base.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  内置CachedThreadPool的一个实现了BaseNetModel的基本Model类
 *  这个Model不包含实际的业务逻辑，主要是用于继承
 *  一个具体的Model对应一个线程池的消耗可能过于巨大，这个类和所有子类都将共享一个线程池
 */
public class CachedThreadModel extends BaseThreadModel {
    /**
     *  子类均共用的线程池
     */
    private static ExecutorService sCachedThreadPool;

    /**
     *  获取线程池，若线程池尚未创建则启动线程池
     * @return 线程池
     */
    protected ExecutorService getThreadPool() {
        if (sCachedThreadPool == null) {
            sCachedThreadPool = openExecutorService();
        }
        return sCachedThreadPool;
    }

    /**
     *  关闭线程池
     */
    @Override
    protected void closeThreadPool() {
        if (sCachedThreadPool != null) {
            sCachedThreadPool.shutdown();
        }
    }

    @Override
    protected ExecutorService openExecutorService() {
        return Executors.newCachedThreadPool();
    }
}
