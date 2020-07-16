package com.gochiusa.wanandroid.base.model;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.ExecutorService;

/**
 *  Model基类，供需要开启线程处理复杂任务的Model继承实现
 */
public abstract class BaseThreadModel implements BaseModel {

    /**
     * 线程成功执行任务或失败的Message返回码
     */
    protected static final int REQUEST_SUCCESS = 10086;
    protected static final int REQUEST_ERROR = 10087;
    /**
     *  跟主线程绑定的Handler
     */
    private Handler mMainHandler;


    /**
     *  抽象方法，开启一个线程池并返回。
     * @return 开启的线程池
     */
    protected abstract ExecutorService openExecutorService();


    /**
     *  抽象方法，关闭被打开的线程池
     */
    protected abstract void closeThreadPool();

    /**
     *  初始化主线程的Handler
     */
    public void initMainHandler(Handler.Callback callback) {
        mMainHandler = new Handler(Looper.getMainLooper(), callback);
    }


    public Handler getMainHandler() {
        return mMainHandler;
    }
}
