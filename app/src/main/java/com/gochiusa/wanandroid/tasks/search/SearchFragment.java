package com.gochiusa.wanandroid.tasks.search;

import com.gochiusa.wanandroid.base.view.BaseFragment;

public class SearchFragment extends BaseFragment<SearchContract.Presenter>
        implements SearchContract.View {


    @Override
    protected SearchContract.Presenter onBindPresenter() {
        return new SearchPresenter(this);
    }
}
