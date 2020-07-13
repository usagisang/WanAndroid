package com.gochiusa.wanandroid.base.view;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.gochiusa.wanandroid.base.presenter.BasePresenter;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
        implements BaseView {
    private P mPresenter;

    /**
     *   交由子类实现如何获得Presenter的方法
     * @return 对应的Presenter
     */
    protected abstract P onBindPresenter();


    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }

    @Override
    public Activity getSelfView() {
        return this;
    }

    /**
     * 活动销毁后取消掉与Presenter的绑定
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.removeAttach();
        }
    }
}
