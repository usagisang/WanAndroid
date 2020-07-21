package com.gochiusa.wanandroid.tasks.main.sort;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BaseRecyclerViewPresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Tree;

import java.util.List;

public interface SortPageContract {
    interface SortView extends BaseView {
        void showRefreshing();
        void hideRefreshing();
        void replaceAll(List<Tree> trees);
    }
    interface SortPresenter extends BaseRecyclerViewPresenter {

    }

    interface SortModel {
        void loadTypeTrees(RequestCallback<List<Tree>, String> callback);
    }

}
