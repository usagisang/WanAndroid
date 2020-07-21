package com.gochiusa.wanandroid.tasks.main.sort;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Tree;
import com.gochiusa.wanandroid.model.LoadTreeModel;

import java.util.List;

public class SortPagePresenter extends BasePresenterImpl<SortPageContract.SortView>
        implements SortPageContract.SortPresenter, RequestCallback<List<Tree>, String> {

    private SortPageContract.SortModel mSortModel;
    public SortPagePresenter(SortPageContract.SortView view) {
        super(view);
        mSortModel = LoadTreeModel.newInstance();
    }
    @Override
    public void refresh() {
        if (isViewAttach()) {
            // 显示刷新进度条
            getView().showRefreshing();
        }
        mSortModel.loadTypeTrees(this);
    }

    /**
     *  什么都不做
     */
    @Override
    public void showMore() {}

    @Override
    public void onResponse(List<Tree> response) {
        if (isViewAttach()) {
            getView().hideRefreshing();
            // 刷新列表显示的体系
            getView().replaceAll(response);
        }
    }

    @Override
    public void onFailure(String failure) {
        if (isViewAttach()) {
            getView().hideRefreshing();
            // 弹出Toast
            getView().showToast(failure);
        }
    }
}
