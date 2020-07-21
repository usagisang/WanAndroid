package com.gochiusa.wanandroid.tasks.main.project.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.adapter.ProjectAdapter;
import com.gochiusa.wanandroid.base.view.BaseRecyclerViewFragment;
import com.gochiusa.wanandroid.entity.Project;

import java.util.ArrayList;
import java.util.List;

public class ChildPageFragment extends BaseRecyclerViewFragment<ChildContract.ChildPresenter>
        implements ChildContract.ChildView {

    private ProjectAdapter mProjectAdapter;

    private int mProjectTypeId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container,false);
        initChildView(view);
        // 向Presenter请求最初的数据
        getPresenter().firstRequest(mProjectTypeId);
        return view;
    }

    /**
     *  重写初始化RecyclerView的方法，添加初始化适配器的部分
     */
    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        mProjectAdapter = new ProjectAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(mProjectAdapter);
    }


    @Override
    protected ChildContract.ChildPresenter onBindPresenter() {
        return new ChildPagePresenter(this);
    }

    public ChildPageFragment() {}

    /**
     *  传入项目类型来构造一个碎片
     * @param typeId 具体项目类型的id
     */
    public ChildPageFragment(int typeId) {
        mProjectTypeId = typeId;
    }

    @Override
    public void showLoading() {
        mProjectAdapter.showFootView();
    }

    @Override
    public void hideLoading() {
        mProjectAdapter.hideFootView();
    }


    @Override
    public void addProjectToList(List<Project> projectList) {
        mProjectAdapter.addAll(projectList);
    }

    @Override
    public void removeAllProjects() {
        mProjectAdapter.clear();
    }
}
