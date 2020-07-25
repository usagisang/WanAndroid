package com.gochiusa.wanandroid.tasks.search;

import android.util.Log;

import com.gochiusa.wanandroid.base.presenter.BasePresenterImpl;
import com.gochiusa.wanandroid.model.SearchModel;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SearchPresenter extends BasePresenterImpl<SearchContract.View>
        implements SearchContract.Presenter {

    private SearchContract.Model mSearchModel;

    /**
     *  当Future为空时的提示信息
     */
    private String mErrorTag = "Error";
    private String mErrorMessage = "Future is null";

    private Future<List<String>> mHotWordFuture = null;
    private Future<List<String>> mHistoryFuture = null;


    public SearchPresenter(SearchContract.View view) {
        super(view);
        mSearchModel = new SearchModel();
    }

    @Override
    public void requestData() {
        // 请求热词数据
        mHotWordFuture = mSearchModel.loadHotWord(refreshHotWord());
        // 请求历史数据
        mHistoryFuture = mSearchModel.loadHistory(refreshHistory());
    }

    @Override
    public void clearHistory() {
        mSearchModel.clearHistoryFromDisk();
    }

    @Override
    public void addHistory(String history, boolean oldData) {
        mSearchModel.addHistoryToDisk(history, oldData);
    }

    private Runnable refreshHotWord() {
        return () -> {
            if (mHotWordFuture != null) {
                try {
                    // 尝试获取请求结果
                    List<String> list = mHotWordFuture.get();
                    // 回调View的方法
                    if (isViewAttach()) {
                        getView().showHotWord(list);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(mErrorTag, mErrorMessage);
            }
        };
    }

    private Runnable refreshHistory() {
        return () -> {
            if (mHistoryFuture != null) {
                try {
                    // 尝试获取请求结果
                    List<String> list = mHistoryFuture.get();
                    // 回调View的方法以显示历史记录
                    if (isViewAttach()) {
                        getView().showHistory(list);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(mErrorTag, mErrorMessage);
            }
        };
    }
}
