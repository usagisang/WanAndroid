package com.gochiusa.wanandroid.tasks.main.home;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.model.HomePageModel;

import java.util.List;

class HomePagePresenter extends BasePresenterImpl<HomePageContract.HomeView>
        implements HomePageContract.HomePresenter {

    private HomePageContract.HomeModel mHomeModel;

    /**
     *  是否已经获取所有数据
     */
    private boolean mHasLoadedAll = false;

    /**
     *  是否处于刷新状态
     */
    private boolean mIsRefreshing = false;

    /**
     *  加载的数据是否来自数据库
     */
    private boolean mIsFromDatabase = false;

    public HomePagePresenter(HomePageContract.HomeView view) {
        super(view);
        mHomeModel = new HomePageModel();
    }

    @Override
    public void refresh() {
        if (isViewAttach()) {
            // 显示正在刷新
            getView().showRefreshing();
            // 进入刷新状态
            mIsRefreshing = true;
        }
        mHomeModel.loadNewArticle(createCallback(true));
    }

    /**
     *   在页面下拉列表，触发加载更多事件
     */
    @Override
    public void showMore() {
        // 如果正在刷新或者网络数据全部加载到列表，则直接退出
        if (mIsRefreshing || mHasLoadedAll) {
            return;
        }
        // 如果上一次的数据来源于数据库，先尝试刷新
        if (mIsFromDatabase) {
            mIsFromDatabase = false;
            refresh();
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

    @Override
    public void loadLocalArticle() {
        if (isViewAttach()) {
            // 显示正在刷新
            getView().showRefreshing();
            // 进入刷新状态
            mIsRefreshing = true;
        }
        // 标记数据来自数据库
        mIsFromDatabase = true;
        mHomeModel.loadArticleFromDatabase(createCallback(false));
    }

    /**
     *  生成刷新操作的回调接口
     * @param saveToDatabase 是否将刷新请求到的新数据存放到数据库中
     */
    private RequestCallback<List<Article>, String> createCallback(boolean saveToDatabase) {
        return new RequestCallback<List<Article>, String>() {
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
                    // 如果需要存入数据库
                    if (saveToDatabase) {
                        mHomeModel.saveToDatabase(response);
                    }
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
    }
}
