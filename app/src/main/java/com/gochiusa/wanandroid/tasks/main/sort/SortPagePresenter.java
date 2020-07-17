package com.gochiusa.wanandroid.tasks.main.sort;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Tree;

import java.util.List;

public class SortPagePresenter extends BasePresenterImpl<SortPageContract.SortView>
        implements SortPageContract.SortPresenter, RequestCallback<List<Tree>, String> {

    private SortPageContract.SortModel mSortModel;
    public SortPagePresenter(SortPageContract.SortView view) {
        super(view);
        mSortModel = new SortPageModel();
    }
    @Override
    public void refresh() {
        if (isViewAttach()) {
            // 显示刷新进度条
            getView().showRefreshing();
        }
        mSortModel.loadAllTrees(this);
    }

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
