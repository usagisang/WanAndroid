package com.gochiusa.wanandroid.tasks.search;

import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;

public class SearchPresenter extends BasePresenterImpl<SearchContract.View>
        implements SearchContract.Presenter {

    public SearchPresenter(SearchContract.View view) {
        super(view);
    }
}
