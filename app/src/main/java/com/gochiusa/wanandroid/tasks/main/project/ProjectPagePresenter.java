package com.gochiusa.wanandroid.tasks.main.project;


import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.entity.Tree;
import com.gochiusa.wanandroid.model.LoadTreeModel;

import java.util.List;

public class ProjectPagePresenter extends BasePresenterImpl<ProjectContract.ProjectView>
        implements ProjectContract.ProjectPresenter, RequestCallback<List<Tree>, String> {

    private ProjectContract.ProjectModel mProjectModel;

    public ProjectPagePresenter(ProjectContract.ProjectView view) {
        super(view);
        mProjectModel = LoadTreeModel.newInstance();
    }

    @Override
    public void requestProjectTree() {
        mProjectModel.loadProjectTree(this);
    }

    @Override
    public void onResponse(List<Tree> response) {
        if (isViewAttach()) {
            getView().treeLoadSuccess(response.get(0));
        }
    }

    @Override
    public void onFailure(String failure) {
        if (isViewAttach()) {
            getView().showToast(failure);
        }
    }
}
