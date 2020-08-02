package com.gochiusa.wanandroid.base.view;

import androidx.appcompat.app.AppCompatActivity;

import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.util.ActivityUtil;

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

    @Override
    public void showToast(String message) {
        ActivityUtil.showToast(message, this);
    }
}
