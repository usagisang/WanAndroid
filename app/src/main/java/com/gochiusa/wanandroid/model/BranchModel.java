package com.gochiusa.wanandroid.model;

import android.os.Handler;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.HttpModel;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.tasks.main.sort.branch.BranchContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.OffsetCalculator;

import org.json.JSONException;

import java.util.List;

public class BranchModel extends HttpModel<Article> implements BranchContract.Model {

    /**
     *  偏移量计算工具类
     */
    private OffsetCalculator mOffsetCalculator;


    @Override
    protected List<Article> pauseJSON(String jsonData) throws JSONException {
        return JSONPause.getArticles(jsonData, mOffsetCalculator);
    }

    @Override
    protected Handler.Callback createCallback() {
        return (message) -> {
            // 尝试获取回调接口，进行强制类型转换
            RequestCallback<List<Article>, String> callback =
                    (RequestCallback<List<Article>, String>) message.obj;
            switch (message.what) {
                case REQUEST_SUCCESS: {
                    callback.onResponse(mCacheList);
                    break;
                }
                case REQUEST_ERROR: {
                    callback.onFailure(ERROR_MESSAGE);
                    break;
                }
            }
            return true;
        };
    }

    @Override
    public void loadMoreArticle(RequestCallback<List<Article>, String> callback, int typeId) {
        if (mOffsetCalculator.increaseOffset()) {
            // 如果成功递增偏移量
            getThreadPool().submit(createTask(callback,
                    CreateURLToRequest.createTypeArticle(mOffsetCalculator.getPage(), typeId)));
        } else {
            // 如果递增偏移量失败
            callback.onFailure(NOT_MORE_TIP);
        }
    }

    @Override
    public void loadNewArticle(RequestCallback<List<Article>, String> callback, int typeId) {
        // 开启线程执行任务
        getThreadPool().submit(createTask(callback,
                CreateURLToRequest.createTypeArticle(0, typeId)));
    }

    /**
     *  单例模式
     */
    private BranchModel() {
        super();
        mOffsetCalculator = new OffsetCalculator(0, 1, 0);
    }
    public static BranchModel newInstance() {
        return SingleModel.MODEL;
    }
    private static final class SingleModel {
        private static final BranchModel MODEL = new BranchModel();
    }
}
