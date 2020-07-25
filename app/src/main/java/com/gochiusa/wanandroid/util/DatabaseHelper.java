package com.gochiusa.wanandroid.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public final class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context,
                          @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstant.CREATE_ARTICLE_TABLE);
        db.execSQL(DatabaseConstant.CREATE_SEARCH_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     *  默认创建、打开的数据库
     */
    public static DatabaseHelper openDefaultDatabase() {
        return new DatabaseHelper(MyApplication.getContext(), DatabaseConstant.DATABASE_NAME,
                null, 1);
    }
}
