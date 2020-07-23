package com.gochiusa.wanandroid.tasks.search;

import com.gochiusa.wanandroid.base.presenter.BasePresenter;
import com.gochiusa.wanandroid.base.view.BaseView;

import java.util.List;
import java.util.concurrent.Future;

public interface SearchContract {
    interface View extends BaseView {
        void showHistory(List<String> historyList);
        void showHotWord(List<String> hotWordList);
    }
    interface Presenter extends BasePresenter {
        void requestData();
        void clearHistory();
    }
    interface Model {
        Future<List<String>> loadHotWord(Runnable runInMainThread);
        Future<List<String>> loadHistory(Runnable runInMainThread);
    }
}
