package com.gochiusa.wanandroid.util.loader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

class ActionCreator {

    private ImageLoader mImageLoader;
     /**
     *  需要显示的图片的Uri地址
     */
    private Uri mUri;

    private String mKey;
    /**
     *  在请求到具体的图片前，是否使用预加载图
     */
    private boolean setPlaceHolder;
    /**
     *  预加载图的图片资源id
     */
    private int mPlaceHolderId;
    /**
     *  加载失败后显示的图片的资源id
     */
    private int mErrorResourceId;

    ActionCreator(ImageLoader imageLoader, String key) {
        this.mImageLoader = imageLoader;
        this.mUri = Uri.parse(key);
        this.mKey = key;
    }

    public void into(ImageView target) {
        // 如果设置了占位图，则显示
        if (setPlaceHolder) {
            target.setImageResource(mPlaceHolderId);
        }
        // 从内存缓存中尝试检索位图
        Bitmap cacheBitmap = mImageLoader.quickMemoryCacheCheck(mKey);
        if (cacheBitmap != null) {
            // 尝试取消请求
            mImageLoader.cancelRequest(target);
            // 设置位图后结束调用
            target.setImageBitmap(cacheBitmap);
            return;
        }
        Action action = new Action(mImageLoader, target, mErrorResourceId, mKey, mUri);
        // 提交请求
        mImageLoader.enqueueAndSubmit(action);
    }

    public ActionCreator placeHolder(int resId) {
        this.mPlaceHolderId = resId;
        this.setPlaceHolder = true;
        return this;
    }

    public ActionCreator error(int resId) {
        this.mErrorResourceId = resId;
        return this;
    }


}
