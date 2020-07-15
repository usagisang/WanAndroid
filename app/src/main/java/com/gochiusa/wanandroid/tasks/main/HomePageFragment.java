package com.gochiusa.wanandroid.tasks.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.base.view.BaseFragment;

public class HomePageFragment extends BaseFragment<HomePageContract.HomePresenter>
        implements HomePageContract.HomeView {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected HomePageContract.HomePresenter onBindPresenter() {
        return new HomePagePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
