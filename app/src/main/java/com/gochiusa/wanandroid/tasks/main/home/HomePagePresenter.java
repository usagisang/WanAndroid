package com.gochiusa.wanandroid.tasks.main.home;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Article;

import java.util.List;

class HomePagePresenter extends BasePresenterImpl<HomePageContract.HomeView>
        implements HomePageContract.HomePresenter {

    private HomePageContract.HomeModel mHomeModel;
    // 是否已经获取所有数据
    private boolean mHasLoadedAll = false;

    public HomePagePresenter(HomePageContract.HomeView view) {
        super(view);
        mHomeModel = new HomePageModel();
    }

    @Override
    public void refresh() {
        if (isViewAttach()) {
            // 显示正在刷新
            getView().showRefreshing();
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
                }
            }
            @Override
            public void onFailure(String failure) {
                // 隐藏刷新的控件
                getView().hideRefreshing();
                // 弹出提示
                getView().showToast(failure);
            }
        };
        mHomeModel.loadNewArticle(callback);
    }

    /**
     *   在页面下拉列表，触发加载更多事件
     */
    @Override
    public void showMore() {
        // 如果已经加载完毕则直接退出
        if (mHasLoadedAll) {
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

        mHomeModel.loadMoreArticle(callback);
    }
}
