package com.gochiusa.wanandroid.tasks.main.home;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Article;

import java.util.Collection;
import java.util.List;

public interface HomePageContract {
    interface HomeView extends BaseView {
        void addArticlesToList(Collection<? extends Article> collection);
        void showLoading();
        void hideLoading();
        void showToast(String message);
        void showRefreshing();
        void hideRefreshing();
        void removeAllArticle();
    }

    interface HomePresenter extends BasePresenter {
        void refresh();
        void showMore();
    }

    interface HomeModel {
        void loadMoreArticle(RequestCallback<List<Article>, String> callback);
        void loadNewArticle(RequestCallback<List<Article>, String> callback);
    }
}
