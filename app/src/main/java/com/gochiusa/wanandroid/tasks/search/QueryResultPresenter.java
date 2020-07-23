package com.gochiusa.wanandroid.tasks.search;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.model.HomePageModel;
import com.gochiusa.wanandroid.model.QueryResultModel;

import java.util.List;

class QueryResultPresenter extends BasePresenterImpl<ResultContract.ResultView>
        implements ResultContract.ResultPresenter {

    /**
     *  搜索关键词
     */
    private String mSearchKey;
    /**
     * 标志变量，是否已经获取所有数据
     */
    private boolean mHasLoadedAll = false;
    /**
     *  是否处于刷新状态
     */
    private boolean mIsRefreshing = false;

    private ResultContract.ResultModel mResultModel;

    public QueryResultPresenter(ResultContract.ResultView view) {
        super(view);
        mResultModel = new QueryResultModel();
    }


    @Override
    public void refresh() {
        if (isViewAttach()) {
            // 显示正在刷新
            getView().showRefreshing();
            // 进入刷新状态
            mIsRefreshing = true;
        }
        RequestCallback<List<Article>, String> callback =
                new RequestCallback<List<Article>, String>() {
                    @Override
                    public void onResponse(List<Article> response) {
                        if (isViewAttach()) {
                            // 隐藏刷新的控件
                            getView().hideRefreshing();
                            // 移除所有已存在的数据，添加新的数据
                            getView().removeAllArticle();
                            getView().addArticlesToList(response);
                            // 退出刷新状态
                            mIsRefreshing = false;
                        }
                    }
                    @Override
                    public void onFailure(String failure) {
                        // 隐藏刷新的控件
                        getView().hideRefreshing();
                        // 弹出提示
                        getView().showToast(failure);
                        // 退出刷新状态
                        mIsRefreshing = false;
                    }
                };
        mResultModel.searchArticle(callback, mSearchKey);
    }


    @Override
    public void showMore() {
        // 如果正在刷新或者网络数据全部加载到列表，则直接退出
        if (mIsRefreshing || mHasLoadedAll) {
            return;
        }
        // 显示正在加载的尾布局
        if (isViewAttach()) {
            getView().showLoading();
        }
        RequestCallback<List<Article>, String> callback =
                new RequestCallback<List<Article>, String>() {
                    @Override
                    public void onResponse(List<Article> response) {
                        if (isViewAttach()) {
                            // 隐藏尾布局
                            getView().hideLoading();
                            // 添加新的数据
                            getView().addArticlesToList(response);
                        }
                    }
                    @Override
                    public void onFailure(String failure) {
                        // 隐藏尾布局
                        getView().hideLoading();
                        // 弹出提示
                        getView().showToast(failure);
                        // 如果是到达尽头的错误提示
                        if (HomePageModel.NOT_MORE_TIP.equals(failure)) {
                            mHasLoadedAll = true;
                        }
                    }
                };
        mResultModel.showMoreArticle(callback, mSearchKey);
    }

    /**
     *  重置搜索词并进行一次刷新请求
     * @param keyword 新的搜索词
     */
    @Override
    public void firstRequest(String keyword) {
        mSearchKey = keyword;
        refresh();
    }
}
