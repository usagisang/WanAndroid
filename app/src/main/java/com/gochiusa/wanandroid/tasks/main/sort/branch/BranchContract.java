package com.gochiusa.wanandroid.tasks.main.sort.branch;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BaseRecyclerViewPresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Article;

import java.util.List;

public interface BranchContract {
    interface View extends BaseView {
        void addArticlesToList(List<Article> articles);
        void showLoading();
        void hideLoading();
        void showRefreshing();
        void hideRefreshing();
        void removeAllArticle();
    }
    interface Presenter extends BaseRecyclerViewPresenter {
        void firstRequest(int typeId);
    }

    interface Model {
        void loadMoreArticle(RequestCallback<List<Article>, String> callback, int typeId);
        void loadNewArticle(RequestCallback<List<Article>, String> callback, int typeId);
    }

}
