package com.gochiusa.wanandroid.tasks.main.project.child;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Project;
import com.gochiusa.wanandroid.model.ChildPageModel;
import com.gochiusa.wanandroid.model.HomePageModel;

import java.util.List;

public class ChildPagePresenter extends BasePresenterImpl<ChildContract.ChildView>
        implements ChildContract.ChildPresenter {

    private ChildContract.ChildModel mChildPageModel;

    /**
     *  进行请求所必须的项目类型id
     */
    private int mTypeId;

    /**
     *  状态变量，是否已经获取所有数据
     */
    private boolean mHasLoadedAll = false;

    public ChildPagePresenter(ChildContract.ChildView view) {
        super(view);
        mChildPageModel = ChildPageModel.newInstance();
    }

    @Override
    public void refresh() {
        if (isViewAttach()) {
            // 显示正在刷新
            getView().showRefreshing();
        }
        mChildPageModel.loadNewProject(createRefreshCallback(), mTypeId);
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
        RequestCallback<List<Project>, String> callback =
                new RequestCallback<List<Project>, String>() {
                    @Override
                    public void onResponse(List<Project> response) {
                        if (isViewAttach()) {
                            // 隐藏尾布局
                            getView().hideLoading();
                            // 添加新的数据
                            getView().addProjectToList(response);
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

        mChildPageModel.loadMoreProject(callback, mTypeId);
    }

    /**
     *  第一次初始化列表的请求，需要明确传入项目的类型id
     * @param typeId 代表项目的一个类型的具体id
     */
    @Override
    public void firstRequest(int typeId) {
        // 缓存类型id
        mTypeId = typeId;
        // 按照刷新操作，进行请求
        refresh();
    }

    /**
     *  辅助方法，产生刷新操作需要回调的接口
     */
    private RequestCallback<List<Project>, String> createRefreshCallback() {
        return new RequestCallback<List<Project>, String>() {
            @Override
            public void onResponse(List<Project> response) {
                if (isViewAttach()) {
                    // 隐藏刷新的控件
                    getView().hideRefreshing();
                    // 移除所有已存在的数据，添加新的数据
                    getView().removeAllProjects();
                    getView().addProjectToList(response);
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
    }
}
