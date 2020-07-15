package com.gochiusa.wanandroid.tasks.main.home;

import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Article;

import java.util.List;

public interface HomePageContract {
    interface HomeView extends BaseView {
        void addArticlesToList(List<Article> articleList);
        void showLoading();
        void hideLoading();
        void showToast(String message);
        void hideRefreshing();
    }

    interface HomePresenter extends BasePresenter {
        void refresh();
        void showMore();
    }

    interface HomeModel {
        void loadMoreArticle();
        void loadNewArticle();
    }
}
