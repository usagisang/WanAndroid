package com.gochiusa.wanandroid.tasks.main.project;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.base.view.BaseView;
import com.gochiusa.wanandroid.entity.Tree;

import java.util.List;

public interface ProjectContract {
    interface ProjectView extends BaseView {
        void treeLoadSuccess(Tree projectTree);
    }
    interface ProjectPresenter extends BasePresenter {
        void requestProjectTree();
    }
    interface ProjectModel {
        void loadProjectTree(RequestCallback<List<Tree>, String> callback);
    }
}
