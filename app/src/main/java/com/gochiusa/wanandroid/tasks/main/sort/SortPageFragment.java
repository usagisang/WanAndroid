package com.gochiusa.wanandroid.tasks.main.sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.adapter.TreeAdapter;
import com.gochiusa.wanandroid.base.view.BaseFragment;
import com.gochiusa.wanandroid.entity.Tree;
import com.gochiusa.wanandroid.util.ActivityUtil;
import com.gochiusa.wanandroid.util.MyApplication;

import java.util.ArrayList;
import java.util.List;


public class SortPageFragment extends BaseFragment<SortPageContract.SortPresenter>
        implements SortPageContract.SortView {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TreeAdapter mTreeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container,false);
        initChildView(view);
        // 向Presenter请求数据
        getPresenter().refresh();
        return view;
    }

    /**
     *  初始化子项控件
     */
    private void initChildView(View parentView) {
        mRecyclerView = parentView.findViewById(R.id.rv_main);
        mSwipeRefreshLayout = parentView.findViewById(R.id.swipe_refresh);
        // 对RecyclerView进行配置
        initRecyclerView(mRecyclerView);
        // 设置SwipeRefreshLayout刷新时触发的操作
        mSwipeRefreshLayout.setOnRefreshListener(() -> getPresenter().refresh());
    }

    /**
     *  初始化RecyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        // 添加分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                MyApplication.getContext(), DividerItemDecoration.VERTICAL));
        // 设置RecyclerView为线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(
                MyApplication.getContext()));
        // 初始化适配器
        mTreeAdapter = new TreeAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(mTreeAdapter);
    }

    @Override
    public void showToast(String message) {
        ActivityUtil.showToast(message, MyApplication.getContext());
    }

    @Override
    public void showRefreshing() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void replaceAll(List<Tree> trees) {
        mTreeAdapter.clear();
        mTreeAdapter.addAll(trees);
    }

    @Override
    protected SortPageContract.SortPresenter onBindPresenter() {
        return new SortPagePresenter(this);
    }
}
