package com.gochiusa.wanandroid.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.HttpModel;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.tasks.main.home.HomePageContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import com.gochiusa.wanandroid.util.CursorPause;
import com.gochiusa.wanandroid.util.DatabaseConstant;
import com.gochiusa.wanandroid.util.DatabaseHelper;
import com.gochiusa.wanandroid.util.JSONPause;
import com.gochiusa.wanandroid.util.MyApplication;
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
            // 打开数据库管理类
            DatabaseHelper helper = DatabaseHelper.openDefaultDatabase();
            SQLiteDatabase readableDatabase = helper.getReadableDatabase();
            // 查询所有文章的信息
            Cursor articleCursor = readableDatabase.query(DatabaseConstant.ARTICLE_TABLE_NAME,
                    null, null, null,
                    null, null, null);
            // 解析歌曲信息并添加到缓存列表中
            mCacheList = CursorPause.getArticles(articleCursor);
            message.what = REQUEST_SUCCESS;
            // 发送消息，回到主线程进行后续处理
            getMainHandler().sendMessage(message);
            // 关闭相关资源
            readableDatabase.close();
            helper.close();
        };
        getThreadPool().submit(runnable);
    }

    @Override
    public void saveToDatabase(List<Article> articleList) {
        Runnable runnable = () -> {
            // 打开数据库管理类
            DatabaseHelper helper = DatabaseHelper.openDefaultDatabase();
            SQLiteDatabase writeableDatabase = helper.getWritableDatabase();
            // 删除数据库的全部数据
            writeableDatabase.delete(DatabaseConstant.ARTICLE_TABLE_NAME,
                    null, null);
            ContentValues contentValues = new ContentValues();
            // 迭代组装、插入新的数据
            for (Article article : articleList) {
                initContentValue(article, contentValues);
                writeableDatabase.insert(DatabaseConstant.ARTICLE_TABLE_NAME,
                        null, contentValues);
            }
            // 关闭相关资源
            writeableDatabase.close();
            helper.close();
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

    /**
     *  使用实体类组装{@code ContentValues}填充入数据库的信息
     */
    private void initContentValue(Article article, ContentValues values) {
        values.clear();
        values.put(DatabaseConstant.AUTHOR_COLUMN_NAME, article.getAuthor());
        values.put(DatabaseConstant.TITLE_COLUMN_NAME, article.getTitle());
        values.put(DatabaseConstant.CHAPTER_COLUMN_NAME, article.getChapterName());
        values.put(DatabaseConstant.DATE_COLUMN_NAME, article.getNiceDate());
        values.put(DatabaseConstant.SUPER_COLUMN_NAME, article.getSuperChapterName());
        values.put(DatabaseConstant.LINK_COLUMN_NAME, article.getLink());
    }
}
