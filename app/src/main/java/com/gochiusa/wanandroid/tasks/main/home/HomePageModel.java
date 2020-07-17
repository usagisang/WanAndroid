package com.gochiusa.wanandroid.tasks.main.home;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.SingleThreadModel;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.OffsetCalculator;
import com.gochiusa.wanandroid.util.http.Call;
import com.gochiusa.wanandroid.util.http.HttpClient;
import com.gochiusa.wanandroid.util.http.Request;
import com.gochiusa.wanandroid.util.http.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class HomePageModel extends SingleThreadModel implements HomePageContract.HomeModel {

    /**
     *  偏移量计算工具类
     */
    private OffsetCalculator mOffsetCalculator;
    /**
     * 到达尽头的提示
     */
    public static final String NOT_MORE_TIP = "到达了尽头惹";
    /**
     *   网络加载中出现错误的提示信息
     */
    public static final String ERROR_MESSAGE = "网络数据加载失败！";

    /**
     *  缓存文章请求结果的变量
     */
    private List<Article> mCacheList;

    public HomePageModel() {
        mOffsetCalculator = new OffsetCalculator(0, 1, 0);
        // 初始化Handler
        initMainHandler(getHandlerCallback());
    }

    @Override
    public void loadNewArticle(RequestCallback<List<Article>, String> callback) {
        // 开启线程执行任务
        getThreadPool().submit(createLoadArticleTask(callback, 0, mOffsetCalculator));
    }

    @Override
    public void loadMoreArticle(RequestCallback<List<Article>, String> callback) {
        if (mOffsetCalculator.increaseOffset()) {
            // 如果成功递增偏移量
            getThreadPool().submit(createLoadArticleTask(callback,
                    mOffsetCalculator.getPage(), null));
        } else {
            // 如果递增偏移量失败
            callback.onFailure(NOT_MORE_TIP);
        }
    }

    private Handler.Callback getHandlerCallback() {
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

    /**
     *   生成可执行任务，用于开启线程加载文章
     * @param callback 加载完毕后需要回调的接口
     * @param page 想要请求第几页的数据
     * @param calculator 偏移量计算器。一般而言只需要在刷新的时候刷新偏移量，其他时刻可以传入null
     */
    private Runnable createLoadArticleTask(RequestCallback<List<Article>, String> callback,
                                           int page, @Nullable OffsetCalculator calculator) {
        return () -> {
            Message message = Message.obtain();
            // 缓存接口
            message.obj = callback;
            // 不使用建造者，直接创建，使用默认的连接设置
            HttpClient httpClient = new HttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            // 生成请求URL，设置在请求中
            requestBuilder.setUrl(CreateURLToRequest.createHomeArticleURL(page));
            // 配置剩余的请求信息
            Request newArticleRequest = requestBuilder.get().build();
            // 创建Call
            Call call = httpClient.newCall(newArticleRequest);
            try {
                // 尝试同步获取响应
                Response response = call.execute();
                // 解析JSON数据
                mCacheList = JSONPause.getArticles(response.getResponseBody(), calculator);
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
