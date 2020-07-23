package com.gochiusa.wanandroid.model;


import com.gochiusa.wanandroid.base.model.CachedThreadModel;
import com.gochiusa.wanandroid.tasks.search.SearchContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.http.Call;
import com.gochiusa.wanandroid.util.http.HttpClient;
import com.gochiusa.wanandroid.util.http.Response;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class SearchModel extends CachedThreadModel implements SearchContract.Model {

    public SearchModel() {
        // 不采用处理Message的方式，因此传入null
        initMainHandler(null);
    }


    @Override
    public Future<List<String>> loadHotWord(Runnable runInMainThread) {
        Callable<List<String>> callable = () -> {
            // 使用默认的请求方式即可
            Call call = HttpClient.createDefaultCall(CreateURLToRequest.createHotKeyURL());
            // 获取请求响应
            Response response = call.execute();
            // 解析数据
            List<String> hotWordList = JSONPause.getHotKey(response.getResponseBodyString());
            // 将需要在主线程执行的Runnable推出去
            getMainHandler().post(runInMainThread);
            return hotWordList;
        };
        // 提交任务
        return getThreadPool().submit(callable);
    }

    @Override
    public Future<List<String>> loadHistory(Runnable runInMainThread) {
        return null;
    }
}
