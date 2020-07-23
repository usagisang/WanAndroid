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
    }

    @Override
    public void clearHistory() {

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
}
