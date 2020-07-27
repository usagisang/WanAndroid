package com.gochiusa.wanandroid.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.List;

import static com.gochiusa.wanandroid.dao.DatabaseConstant.DATE_COLUMN_NAME;
import static com.gochiusa.wanandroid.dao.DatabaseConstant.HISTORY_TABLE_NAME;
import static com.gochiusa.wanandroid.dao.DatabaseConstant.QUERY_COLUMN_NAME;

public class HistoryDao implements DatabaseDao<String> {

    private DatabaseHelper mDatabaseHelper;

    /**
     *  线程锁
     */
    private final Object mLock = new Object();

    @Override
    public List<String> selectAll() {
        return select((readableDatabase) ->
                readableDatabase.query(DatabaseConstant.HISTORY_TABLE_NAME,
                        null, null, null,
                        null, null, null));
    }

    @Override
    public List<String> select(ReadData readData) {
        synchronized (mLock) {
            SQLiteDatabase readableDatabase = mDatabaseHelper.getReadableDatabase();
            // 查询所有历史搜索
            Cursor historyCursor = readData.readFrom(readableDatabase);
            // 解析历史搜索
            List<String> list = CursorPause.getHistory(historyCursor);
            // 关闭数据库
            readableDatabase.close();
            return list;
        }
    }

    @Override
    public void delete(WriteData writeData) {
        synchronized (mLock) {
            SQLiteDatabase writableDatabase = mDatabaseHelper.getWritableDatabase();
            writeData.writeTo(writableDatabase);
            writableDatabase.close();
        }
    }

    @Override
    public void deleteAll() {
        delete((writableDatabase) -> {
            // 清除所有数据
            writableDatabase.delete(HISTORY_TABLE_NAME, null, null);
        });
    }

    @Override
    public void update(WriteData writeData) {
        synchronized (mLock) {
            SQLiteDatabase writableDatabase = mDatabaseHelper.getWritableDatabase();
            writeData.writeTo(writableDatabase);
            writableDatabase.close();
        }
    }

    @Override
    @Deprecated
    public void insert(List<String> values) {
        synchronized (mLock) {
            SQLiteDatabase writableDatabase = mDatabaseHelper.getWritableDatabase();
            for (String value : values) {
                ContentValues contentValues = new ContentValues();
                // 填充毫秒数的字符串
                contentValues.put(DATE_COLUMN_NAME, Long.toString(System.currentTimeMillis()));
                // 填充历史记录
                contentValues.put(QUERY_COLUMN_NAME, value);
                writableDatabase.insert(HISTORY_TABLE_NAME, null, contentValues);
            }
            writableDatabase.close();
        }
    }

    public void insert(String history) {
        synchronized (mLock) {
            SQLiteDatabase writableDatabase = mDatabaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            // 填充毫秒数的字符串
            contentValues.put(DATE_COLUMN_NAME, Long.toString(System.currentTimeMillis()));
            // 填充历史记录
            contentValues.put(QUERY_COLUMN_NAME, history);

            writableDatabase.insert(HISTORY_TABLE_NAME, null, contentValues);
            writableDatabase.close();
        }
    }


    /**
     *  单例模式
     */
    public static HistoryDao newInstance() {
        return HistoryDao.GetDao.DAO;
    }

    private HistoryDao() {
        mDatabaseHelper = DatabaseHelper.openDefaultDatabase();
    }

    private static final class GetDao {
        private static final HistoryDao DAO = new HistoryDao();
    }
}
