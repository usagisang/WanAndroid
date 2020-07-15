package com.gochiusa.wanandroid.tasks.main.home;

import android.content.Context;
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
import com.gochiusa.wanandroid.adapter.HomeArticleAdapter;
import com.gochiusa.wanandroid.base.view.BaseFragment;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends BaseFragment<HomePageContract.HomePresenter>
        implements HomePageContract.HomeView {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeArticleAdapter mArticleAdapter;

    @Override
    protected HomePageContract.HomePresenter onBindPresenter() {
        return new HomePagePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container,false);
        initChildView(view);
        return view;
    }


    /**
     *  初始化子项控件
     */
    private void initChildView(View parentView) {
        mRecyclerView = parentView.findViewById(R.id.rv_main);
        mSwipeRefreshLayout = parentView.findViewById(R.id.swipe_refresh);
        initRecyclerView(mRecyclerView);
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
        mArticleAdapter = new HomeArticleAdapter(new ArrayList<Article>());
        recyclerView.setAdapter(mArticleAdapter);
    }

    @Override
    public void addArticlesToList(List<Article> articleList) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void hideRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * 单例模式
     */
    private static final class GetHomePageFragment {
        private static final HomePageFragment HOME_PAGE_FRAGMENT = new HomePageFragment();
    }
    private HomePageFragment() {}

    public static HomePageFragment newInstance() {
        return GetHomePageFragment.HOME_PAGE_FRAGMENT;
    }

}
