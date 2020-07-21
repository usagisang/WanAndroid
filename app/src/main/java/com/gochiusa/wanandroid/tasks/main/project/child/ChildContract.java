package com.gochiusa.wanandroid.tasks.main.project.child;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BaseRecyclerViewPresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Project;

import java.util.List;

public interface ChildContract {
    interface ChildView extends BaseView {
        void showLoading();
        void hideLoading();
        void showRefreshing();
        void hideRefreshing();
        void addProjectToList(List<Project> projectList);
        void removeAllProjects();
    }
    interface ChildPresenter extends BaseRecyclerViewPresenter {
        void firstRequest(int typeId);
    }
    interface ChildModel {
        void loadMoreProject(RequestCallback<List<Project>, String> callback, int typeId);
        void loadNewProject(RequestCallback<List<Project>, String> callback, int typeId);
    }
}
