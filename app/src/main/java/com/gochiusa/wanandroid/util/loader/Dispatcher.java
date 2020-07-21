package com.gochiusa.wanandroid.util.loader;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class Dispatcher {

    private Map<String, Future<Worker>> mFutureMap;
    private Downloader mDownloader;
    private ExecutorService mExecutorService;
    private Handler mHandler;
    public static final int REQUEST_COMPLETE = 1;
    public static final int REQUEST_ERROR = 2;

    Dispatcher(Downloader downloader, ExecutorService executorService) {
        mFutureMap = new HashMap<>();
        this.mDownloader = downloader;
        this.mExecutorService = executorService;
        mHandler = new Handler(Looper.getMainLooper(), this::handleMessage);
    }

    void performCancel(Action action) {
        // 相当于将Future对象弹出
        Future<Worker> future = mFutureMap.remove(action.key);
        if (future != null) {
            // 获取Future对象并中断线程
            future.cancel(true);
        }
    }

    /**
     *  提交Action到子线程执行加载任务
     */
    void performSubmit(Action action) {
        Worker worker = new Worker(action, mDownloader, this);
        Future<Worker> future = mExecutorService.submit(worker);
        mFutureMap.put(action.key, future);
    }

    void performComplete(Action action) {
        // 相当于将Future对象弹出
        Future<Worker> future = mFutureMap.remove(action.key);
        if (future != null && future.isDone()) {
            try {
                action.imageLoader.complete(future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void performError(Worker worker) {
        // 取消请求
        performCancel(worker.getAction());
        // 回调方法
        worker.getAction().imageLoader.complete(worker);
    }

    void dispatchComplete(Action action) {
        mHandler.sendMessage(mHandler.obtainMessage(REQUEST_COMPLETE, action));
    }


    void dispatchError(Worker worker) {
        mHandler.sendMessage(mHandler.obtainMessage(REQUEST_ERROR, worker));
    }


    private boolean handleMessage(Message message) {
        switch (message.what) {
            case REQUEST_COMPLETE : {
                Action action = (Action) message.obj;
                performComplete(action);
                break;
            }
            case REQUEST_ERROR : {
                Worker worker = (Worker) message.obj;
                performError(worker);
                break;
            }
        }
        return true;
    }
}
