package com.gochiusa.wanandroid.model;

import android.os.Handler;
import android.os.Message;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.SingleThreadModel;
import com.gochiusa.wanandroid.entity.Tree;
import com.gochiusa.wanandroid.tasks.main.project.ProjectContract;
import com.gochiusa.wanandroid.tasks.main.sort.SortPageContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.http.Call;
import com.gochiusa.wanandroid.util.http.HttpClient;
import com.gochiusa.wanandroid.util.http.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class LoadTreeModel extends SingleThreadModel
        implements SortPageContract.SortModel, ProjectContract.ProjectModel {

    /**
     * 缓存请求结果的变量
     */
    private List<Tree> mCacheList;

    /**
     *   加载中出现错误的提示信息
     */
    private static final String ERROR_MESSAGE = "刷新失败！";


    @Override
    public void loadTypeTrees(RequestCallback<List<Tree>, String> callback) {
        Runnable runnable = () -> {
            Message message = Message.obtain();
            // 缓存接口
            message.obj = callback;
            // 创建Call
            Call call = HttpClient.createDefaultCall(CreateURLToRequest.createTypeTreeURL());
            try {
                // 尝试同步获取响应
                Response response = call.execute();
                // 解析JSON数据
                mCacheList = JSONPause.getTypeTrees(response.getResponseBodyString());
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
        // 开始任务
        getThreadPool().submit(runnable);
    }

    /**
     * 指定Handler收到Message后如何处理
     */
    private Handler.Callback createCallback() {
        return (message) -> {
            if (message.obj instanceof RequestCallback) {
                RequestCallback<List<Tree>, String> callback =
                        (RequestCallback<List<Tree>, String>) message.obj;
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
            }
          return true;
        };
    }

    @Override
    public void loadProjectTree(RequestCallback<List<Tree>, String> callback) {
        Runnable runnable = () -> {
            Message message = Message.obtain();
            // 缓存接口
            message.obj = callback;
            // 创建Call
            Call call = HttpClient.createDefaultCall(CreateURLToRequest.createProjectTreeURL());
            try {
                // 尝试同步获取响应
                Response response = call.execute();
                // 解析JSON数据
                mCacheList = JSONPause.getProjectTree(response.getResponseBodyString());
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
        // 开始任务
        getThreadPool().submit(runnable);
    }


    /**
     *  单例模式
     */
    private LoadTreeModel() {
        // 初始化Handler
        initMainHandler(createCallback());
    }
    public static LoadTreeModel newInstance() {
        return SingleModel.MODEL;
    }

    private static final class SingleModel {
        private static final LoadTreeModel MODEL = new LoadTreeModel();
    }
}
