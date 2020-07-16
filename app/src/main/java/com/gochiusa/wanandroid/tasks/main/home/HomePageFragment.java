package com.gochiusa.wanandroid.tasks.main.home;

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
import com.gochiusa.wanandroid.util.ActivityUtil;
import com.gochiusa.wanandroid.util.MyApplication;

import java.util.ArrayList;
import java.util.Collection;

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
        // 向Presenter请求数据（刷新操作）
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
        mArticleAdapter = new HomeArticleAdapter(new ArrayList<>());
        recyclerView.setAdapter(mArticleAdapter);
        // 为recyclerView添加滚动的监听器
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (! recyclerView.canScrollVertically(1)) {
                    // 如果滑动到底部
                    getPresenter().showMore();
                }
            }
        });
    }

    @Override
    public void addArticlesToList(Collection<? extends Article> collection) {
        mArticleAdapter.addAll(collection);
    }

    /**
     *  显示尾布局
     */
    @Override
    public void showLoading() {
        mArticleAdapter.getFootView().setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏尾布局
     */
    @Override
    public void hideLoading() {
        mArticleAdapter.getFootView().setVisibility(View.INVISIBLE);
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
    public void removeAllArticle() {
        mArticleAdapter.removeAll();
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
