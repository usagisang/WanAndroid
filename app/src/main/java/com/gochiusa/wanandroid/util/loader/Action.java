package com.gochiusa.wanandroid.util.loader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

class Action {

    final ImageLoader imageLoader;
    final WeakReference<ImageView> target;
    final int errorResId;
    final Uri uri;
    final String key;

    /**
     *  该Action是否被取消
     */
    boolean cancelled;

    Action(ImageLoader imageLoader, ImageView target, int errorResId, String key, Uri uri) {
        this.imageLoader = imageLoader;
        this.target = new WeakReference<>(target);
        this.errorResId = errorResId;
        this.key = key;
        this.uri = uri;
    }

    void cancel() {
        cancelled = true;
    }

    @Nullable ImageView getTarget() {
        return target.get();
    }

    String getKey() {
        return key;
    }

    boolean isCancelled() {
        return cancelled;
    }

    Uri getUri() {
        return uri;
    }

    ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     *  加载成功完成后回调这个方法
     */
    void complete(Bitmap result) {
        ImageView target = this.target.get();
        if (target != null) {
            target.setImageBitmap(result);
        }
    }

    /**
     *  加载失败后回调这个方法
     */
    void error() {
        ImageView target = this.target.get();
        if (target != null) {
            target.setImageResource(errorResId);
        }
    }

}
