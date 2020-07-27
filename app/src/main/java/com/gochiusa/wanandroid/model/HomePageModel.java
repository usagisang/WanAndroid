package com.gochiusa.wanandroid.model;

import android.os.Handler;
import android.os.Message;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.HttpModel;
import com.gochiusa.wanandroid.dao.ArticleDao;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.tasks.main.home.HomePageContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.OffsetCalculator;

import org.json.JSONException;

import java.util.List;

public class HomePageModel extends HttpModel<Article> implements HomePageContract.HomeModel {

    /**
     *  偏移量计算工具类
     */
    private OffsetCalculator mOffsetCalculator;


    public HomePageModel() {
        super();
        mOffsetCalculator = new OffsetCalculator(0, 1, Integer.MAX_VALUE);
    }

    @Override
    protected List<Article> pauseJSON(String jsonData) throws JSONException {
        return JSONPause.getArticles(jsonData, mOffsetCalculator);
    }


    @Override
    public void loadNewArticle(RequestCallback<List<Article>, String> callback) {
        // 开启线程执行任务
        getThreadPool().submit(createTask(callback,
                CreateURLToRequest.createHomeArticleURL(0)));
    }

    @Override
    public void loadArticleFromDatabase(RequestCallback<List<Article>, String> callback) {
        Runnable runnable = () -> {
            Message message = Message.obtain();
            // 将回调接口存进message
            message.obj = callback;
            // 使用数据库接口直接请求数据
            mCacheList = ArticleDao.newInstance().selectAll();
            message.what = REQUEST_SUCCESS;
            // 发送消息，回到主线程进行后续处理
            getMainHandler().sendMessage(message);
        };
        getThreadPool().submit(runnable);
    }

    @Override
    public void saveToDatabase(List<Article> articleList) {
        Runnable runnable = () -> {
            ArticleDao articleDao = ArticleDao.newInstance();
            // 删除全部数据
            articleDao.deleteAll();
            // 插入列表中的文章的数据
            articleDao.insert(articleList);
        };
        getThreadPool().submit(runnable);
    }

    @Override
    public void loadMoreArticle(RequestCallback<List<Article>, String> callback) {
        if (mOffsetCalculator.increaseOffset()) {
            // 如果成功递增偏移量
            getThreadPool().submit(createTask(callback,
                    CreateURLToRequest.createHomeArticleURL(mOffsetCalculator.getPage())));
        } else {
            // 如果递增偏移量失败
            callback.onFailure(NOT_MORE_TIP);
        }
    }

    protected Handler.Callback createCallback() {
        return (message) -> {
            // 尝试获取回调接口
            if (message.obj instanceof RequestCallback) {
                // 强制类型转换
                RequestCallback<List<Article>, String> callback =
                        (RequestCallback<List<Article>, String>) message.obj;
                switch (message.what) {
                    case REQUEST_SUCCESS: {
                        callback.onResponse(mCacheList);
                        break;
                    }
                    case REQUEST_ERROR : {
                        callback.onFailure(ERROR_MESSAGE);
                        break;
                    }
                }
            }
            return true;
        };
    }

}
