package com.gochiusa.wanandroid.base.view;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.util.ActivityUtil;

/**
 *  只有需要自行控制查询数据行为的碎片才需要继承这个基类
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    @Override
    public Activity getSelfView() {
        return getActivity();
    }
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
        ActivityUtil.showToast(message, getContext());
    }
}
