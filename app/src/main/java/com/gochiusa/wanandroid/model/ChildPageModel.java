package com.gochiusa.wanandroid.model;

import android.os.Handler;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.HttpModel;
import com.gochiusa.wanandroid.entity.Project;
import com.gochiusa.wanandroid.tasks.main.project.child.ChildContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.OffsetCalculator;

import org.json.JSONException;

import java.util.List;

public class ChildPageModel extends HttpModel<Project> implements ChildContract.ChildModel {

    /**
     *  偏移量计算工具类
     */
    private OffsetCalculator mOffsetCalculator;

    @Override
    public void loadMoreProject(RequestCallback<List<Project>, String> callback, int typeId) {
        if (mOffsetCalculator.increaseOffset()) {
            // 如果成功递增偏移量
            getThreadPool().submit(createTask(callback, CreateURLToRequest.createProjectDataURL(
                            mOffsetCalculator.getPage() + 1, typeId)));
        } else {
            // 如果递增偏移量失败
            callback.onFailure(HomePageModel.NOT_MORE_TIP);
        }
    }

    @Override
    public void loadNewProject(RequestCallback<List<Project>, String> callback, int typeId) {
        // 创建Runnable任务，提交线程池执行
        getThreadPool().submit(createTask(callback,
                CreateURLToRequest.createProjectDataURL(1, typeId)));
    }


    protected Handler.Callback createCallback() {
        return (message) -> {
            if (message.obj instanceof RequestCallback) {
                RequestCallback<List<Project>, String> callback =
                        (RequestCallback<List<Project>, String>) message.obj;
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
    protected List<Project> pauseJSON(String jsonData) throws JSONException {
        return JSONPause.getProjects(jsonData, mOffsetCalculator);
    }

    /**
     *  单例模式
     */
    private ChildPageModel() {
        super();
        mOffsetCalculator = new OffsetCalculator(0, 1, 0);
    }
    public static ChildPageModel newInstance() {
        return SingleModel.MODEL;
    }

    private static final class SingleModel {
        private static final ChildPageModel MODEL = new ChildPageModel();
    }
}
