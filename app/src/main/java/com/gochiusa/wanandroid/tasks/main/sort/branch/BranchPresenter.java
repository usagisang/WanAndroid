package com.gochiusa.wanandroid.tasks.main.sort.branch;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.model.BranchModel;
import com.gochiusa.wanandroid.model.HomePageModel;

import java.util.List;

public class BranchPresenter extends BasePresenterImpl<BranchContract.View>
        implements BranchContract.Presenter {

    /**
     *  进行请求所必须的文章类型id
     */
    private int mTypeId;

    /**
     *  状态变量，是否已经获取所有数据
     */
    private boolean mHasLoadedAll = false;

    private BranchContract.Model mBranchModel;
    public BranchPresenter(BranchContract.View view) {
        super(view);
        mBranchModel = BranchModel.newInstance();
    }


    /**
     *  第一次初始化列表的请求，需要明确传入文章的类型id
     * @param typeId 代表项目的一个类型的具体id
     */
    @Override
    public void firstRequest(int typeId) {
        // 缓存类型id
        mTypeId = typeId;
        // 按照刷新操作，进行请求
        refresh();
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
        // 进行刷新操作
        mBranchModel.loadNewArticle(callback, mTypeId);
    }

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
        // 执行加载更多操作
        mBranchModel.loadMoreArticle(callback, mTypeId);
    }
}
