package com.gochiusa.wanandroid.base.model;

import android.os.Handler;
import android.os.Message;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.util.http.Call;
import com.gochiusa.wanandroid.util.http.HttpClient;
import com.gochiusa.wanandroid.util.http.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 *  使用util.Http包实现的网络加载Model，封装了相似的网络加载代码
 *  如果有不一样的需求，可以不继承这个类
 * @param <T> 缓存的请求得到的数据的类型
 */
public abstract class HttpModel<T> extends SingleThreadModel {

    /**
     * 到达尽头的提示
     */
    public static final String NOT_MORE_TIP = "到达了尽头惹";
    /**
     *   加载中出现错误的提示信息
     */
    public static final String ERROR_MESSAGE = "数据加载失败！";

    protected List<T> mCacheList;

    public HttpModel() {
        initMainHandler(createCallback());
    }

    /**
     *   生成可执行任务，用于开启线程加载数据
     * @param callback 加载完毕后需要回调的接口，会通过Handler发送到主线程
     * @param loadUrl 请求的url
     */
    public Runnable createTask(RequestCallback callback, String loadUrl) {
        return () -> {
            Message message = Message.obtain();
            // 缓存接口
            message.obj = callback;
            // 创建Call
            Call call = HttpClient.createDefaultCall(loadUrl);
            try {
                // 尝试同步获取响应
                Response response = call.execute();
                // 解析JSON数据
                mCacheList = pauseJSON(response.getResponseBodyString());
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


    protected abstract List<T> pauseJSON(String jsonData) throws JSONException;


    protected abstract Handler.Callback createCallback();
}
