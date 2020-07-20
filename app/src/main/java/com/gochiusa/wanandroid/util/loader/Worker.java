package com.gochiusa.wanandroid.util.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.Callable;


class Worker implements Callable<Worker> {

    private final Action action;
    private final Downloader downloader;
    private final Dispatcher dispatcher;
    private Bitmap result;

    Worker(Action action, Downloader downloader, Dispatcher dispatcher) {
        this.action = action;
        this.downloader = downloader;
        this.dispatcher = dispatcher;
    }

    @Override
    public Worker call() {
        try {
            result = loadBitmap();
            dispatcher.performComplete(action);
        } catch (Exception ignored) {
            dispatcher.performError(this);
        }
        return this;
    }

    private @Nullable Bitmap loadBitmap() throws IOException {
        Downloader.Response result = downloader.load(action.key);
        if (result.getInputStream() != null) {
            // 优先解析输入流获取位图
           return BitmapFactory.decodeStream(result.getInputStream());
        }
        return result.getBitmap();
    }

    Bitmap getResult() {
        return result;
    }

    Action getAction() {
        return action;
    }
}
