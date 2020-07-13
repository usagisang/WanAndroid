package com.gochiusa.wanandroid.base.presenter;

public interface BasePresenter {

    /**
     *  View是否与Presenter保持连接
     * @return 如果View保持连接返回true,否则返回false
     */
    boolean isViewAttach();

    /**
     *   移除View和Presenter的连接
     */
    void removeAttach();
}
