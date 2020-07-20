package com.gochiusa.wanandroid.base.view;

import android.app.Activity;

public interface BaseView {
    /**
     *   使Presenter能够获得Context但是不保留引用，避免内存泄漏
     * @param <T> T为继承了Activity的View类
     * @return View界面本身
     */
    <T extends Activity> T getSelfView();

    /**
     *  利用Toast弹出提示
     * @param message 需要弹出的提示
     */
    void showToast(String message);
}
