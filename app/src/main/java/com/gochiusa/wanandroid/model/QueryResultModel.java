package com.gochiusa.wanandroid.model;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.HttpModel;
import com.gochiusa.wanandroid.base.model.SingleThreadModel;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.tasks.search.ResultContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.OffsetCalculator;
import com.gochiusa.wanandroid.util.http.Call;
import com.gochiusa.wanandroid.util.http.HttpClient;
import com.gochiusa.wanandroid.util.http.Request;
import com.gochiusa.wanandroid.util.http.RequestBody;
import com.gochiusa.wanandroid.util.http.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static com.gochiusa.wanandroid.util.DomainAPIConstant.KEY_PARAM;


public class QueryResultModel extends SingleThreadModel implements ResultContract.ResultModel {

    /**
     *  偏移量计算工具类
     */
    private OffsetCalculator mOffsetCalculator;

    /**
     *  缓存搜索结果的集合
     */
    protected List<Article> mCacheList;

    public QueryResultModel() {
        // 初始化主线程Handler
        initMainHandler(createCallback());
        mOffsetCalculator = new OffsetCalculator(0, 1, Integer.MAX_VALUE);
    }

    private Handler.Callback createCallback() {
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
                    callback.onFailure(HttpModel.ERROR_MESSAGE);
                    break;
                }
            }
            return true;
        };
    }

    /**
     *  使用给定的关键词进行文章的搜索
     * @param callback  回调接口，请求结束会被回调
     * @param keyword  搜索的关键词
     */
    @Override
    public void searchArticle(RequestCallback<List<Article>, String> callback, String keyword) {
        getThreadPool().submit(createTask(callback,
                CreateURLToRequest.createSearchURL(0), keyword));
    }

    /**
     *  加载同一个搜索词下更多的数据，注意，本方法不保证搜索词的一致性，
     *  一致性的保证应当由调用这个方法的类来保证，否则会出现显示的错误
     */
    @Override
    public void showMoreArticle(RequestCallback<List<Article>, String> callback, String keyword) {
        if (mOffsetCalculator.increaseOffset()) {
            // 如果成功递增偏移量
            getThreadPool().submit(createTask(callback,
                    CreateURLToRequest.createSearchURL(mOffsetCalculator.getPage()), keyword));
        } else {
            // 如果递增偏移量失败
            callback.onFailure(HttpModel.NOT_MORE_TIP);
        }
    }

    /**
     *   生成可执行任务，用于开启线程加载数据
     * @param callback 加载完毕后需要回调的接口，会通过Handler发送到主线程
     * @param loadUrl 请求的url
     */
    public Runnable createTask(RequestCallback<List<Article>, String> callback,
                               String loadUrl, String postParam) {
        return () -> {
            Message message = Message.obtain();
            // 缓存接口
            message.obj = callback;
            HttpClient client = new HttpClient();
            // 建造者生成Request，使用POST方法
            Request.Builder builder = new Request.Builder();
            Request request = builder.setUrl(loadUrl).post(
                    RequestBody.create(null, KEY_PARAM + postParam)).build();
            // 创建Call
            Call call = client.newCall(request);
            try {
                // 尝试同步获取响应
                Response response = call.execute();
                // 解析JSON数据
                mCacheList = JSONPause.getArticles(
                        response.getResponseBodyString(), mOffsetCalculator);
                // 将Message标记为请求成功
                message.what = REQUEST_SUCCESS;
            } catch (IOException | JSONException e) {
                // 将Message标记为请求失败
                message.what = REQUEST_ERROR;
                e.printStackTrace();
            } finally {
                // 无论请求成功或失败都会发送Message
                getMainHandler().sendMessage(message);
            }
        };
    }
}
