package com.gochiusa.wanandroid.tasks.main;

import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;

class HomePagePresenter extends BasePresenterImpl<HomePageContract.HomeView>
        implements HomePageContract.HomePresenter {


    public HomePagePresenter(HomePageContract.HomeView view) {
        super(view);
    }
}
