package com.gochiusa.wanandroid.base.presenter;

import com.gochiusa.wanandroid.base.view.BaseView;

import java.lang.ref.WeakReference;

public class BasePresenterImpl<V extends BaseView> implements BasePresenter {
    // 保存对View的弱引用
    private WeakReference<V> mViewReference;

    public BasePresenterImpl(V view) {
        attachView(view);
    }


    public void attachView(V view) {
        mViewReference = new WeakReference<>(view);
    }

    public V getView() {
        return mViewReference.get();
    }
    @Override
    public boolean isViewAttach() {
        return (mViewReference != null) && (mViewReference.get() != null);
    }

    @Override
    public void removeAttach() {
        if (mViewReference != null) {
            mViewReference.clear();
            mViewReference = null;
        }
    }
}
