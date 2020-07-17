package com.gochiusa.wanandroid.tasks.main.sort;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Tree;

import java.util.List;

public interface SortPageContract {
    interface SortView extends BaseView {
        void showToast(String message);
        void showRefreshing();
        void hideRefreshing();
        void replaceAll(List<Tree> trees);
    }
    interface SortPresenter extends BasePresenter {
        void refresh();
    }

    interface SortModel {
        void loadAllTrees(RequestCallback<List<Tree>, String> callback);
    }

}
