package com.gochiusa.wanandroid.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gochiusa.wanandroid.base.model.CachedThreadModel;
import com.gochiusa.wanandroid.tasks.search.SearchContract;
import com.gochiusa.wanandroid.util.CreateURLToRequest;
import static com.gochiusa.wanandroid.util.DatabaseConstant.*;

import com.gochiusa.wanandroid.util.CursorPause;
import com.gochiusa.wanandroid.util.DatabaseHelper;
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
        Callable<List<String>> callable = () -> {
            DatabaseHelper helper = DatabaseHelper.openDefaultDatabase();
            SQLiteDatabase readableDatabase = helper.getReadableDatabase();
            // 查询所有历史搜索，并按照时间来排序
            Cursor cursor = readableDatabase.rawQuery(SELECT_ALL_HISTORY, null);
            // 解析数据
            List<String> list = CursorPause.getHistory(cursor);
            // 关闭资源
            readableDatabase.close();
            helper.close();
            // 将需要在主线程执行的Runnable推出去
            getMainHandler().post(runInMainThread);
            return list;
        };
        return getThreadPool().submit(callable);
    }

    @Override
    public void clearHistoryFromDisk() {
        Runnable runnable = () -> {
            DatabaseHelper helper = DatabaseHelper.openDefaultDatabase();
            SQLiteDatabase writeDatabase = helper.getWritableDatabase();
            // 清除所有数据
            writeDatabase.delete(HISTORY_TABLE_NAME, null, null);
            // 关闭资源
            writeDatabase.close();
            helper.close();
        };
        getThreadPool().submit(runnable);
    }

    @Override
    public void addHistoryToDisk(String history, boolean oldData) {
        Runnable runnable = () -> {
            // 打开相关资源
            DatabaseHelper helper = DatabaseHelper.openDefaultDatabase();
            SQLiteDatabase writeDatabase = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            // 填充毫秒数的字符串
            values.put(DATE_COLUMN_NAME, Long.toString(System.currentTimeMillis()));
            if (oldData) {
                // 如果是旧记录，更新旧的数据
                writeDatabase.update(HISTORY_TABLE_NAME, values,
                        QUERY_COLUMN_NAME + "=?", new String[] {history});
            } else {
                // 如果是新纪录，插入新的数据
                // 填充历史记录
                values.put(QUERY_COLUMN_NAME, history);
                writeDatabase.insert(HISTORY_TABLE_NAME, null, values);
            }
            // 关闭资源
            writeDatabase.close();
            helper.close();
        };
        getThreadPool().submit(runnable);
    }
}
