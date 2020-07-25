package com.gochiusa.wanandroid.tasks.search;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BaseRecyclerViewPresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Article;

import java.util.List;

public interface ResultContract {
    interface ResultView extends BaseView {
        void showLoading();
        void hideLoading();
        void showRefreshing();
        void hideRefreshing();
        void addArticlesToList(List<Article> articles);
        void removeAllArticle();
    }
    interface ResultPresenter extends BaseRecyclerViewPresenter {
        void firstRequest(String keyword);
    }
    interface ResultModel {
        void searchArticle(RequestCallback<List<Article>, String> callback,
                           String keyword);
        void showMoreArticle(RequestCallback<List<Article>, String> callback,
                           String keyword);
    }
}
