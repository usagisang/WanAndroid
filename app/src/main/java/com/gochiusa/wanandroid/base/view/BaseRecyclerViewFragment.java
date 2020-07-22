package com.gochiusa.wanandroid.base.view;

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
import com.gochiusa.wanandroid.base.presenter.BaseRecyclerViewPresenter;
import com.gochiusa.wanandroid.util.MyApplication;


/**
 *  仅含有SwipeRefreshLayout与RecyclerView的布局的碎片可以使用的公共基类
 *  基于这两个控件而经常需要实现的一些方法，这个基类将提供标准实现
 *  设置这个类是为了减少重复代码
 * @param <P> 这个碎片对应的Presenter
 */
public abstract class BaseRecyclerViewFragment<P extends BaseRecyclerViewPresenter>
        extends BaseFragment<P> {

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container,false);
        initChildView(view);
        requestFirstData();
        return view;
    }


    /**
     *  初始化子项控件
     */
    protected void initChildView(View parentView) {
        mRecyclerView = parentView.findViewById(R.id.rv_show_content);
        mSwipeRefreshLayout = parentView.findViewById(R.id.swipe_refresh);
        // 对RecyclerView进行配置
        initRecyclerView(mRecyclerView);
        // 设置SwipeRefreshLayout刷新时触发的操作
        mSwipeRefreshLayout.setOnRefreshListener(this::refreshData);
    }

    /**
     *  初始化RecyclerView
     */
    protected void initRecyclerView(RecyclerView recyclerView) {
        // 添加分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                MyApplication.getContext(), DividerItemDecoration.VERTICAL));
        // 设置RecyclerView为线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(
                MyApplication.getContext()));
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

    /**
     *  显示刷新进度条
     */
    public void showRefreshing() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    /**
     * 隐藏刷新进度条
     */
    public void hideRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     *  指定碎片在onCreateView阶段如何向Presenter请求初始数据。
     *  默认使用{@code getPresenter().refresh()}，可以重写以支持更多形式
     */
    protected void requestFirstData() {
        // 默认的请求数据方式（刷新操作）
        getPresenter().refresh();
    }

    /**
     *  指定SwipeRefreshLayout刷新如何向Presenter请求数据。
     *  默认使用{@code getPresenter().refresh()}，可以重写以支持更多形式
     */
    protected void refreshData() {
        getPresenter().refresh();
    }


}
