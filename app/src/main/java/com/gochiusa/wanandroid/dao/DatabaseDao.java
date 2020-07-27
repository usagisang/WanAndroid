package com.gochiusa.wanandroid.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface DatabaseDao<T> {
    List<T> selectAll();
    List<T> select(ReadData readData);
    void delete(WriteData writeData);
    void deleteAll();
    void update(WriteData writeData);
    void insert(List<T> values);

    /**
     *  指定具体的读取数据的行为的接口
     */
    interface ReadData {
        /**
         *  传入可读的数据库对象，指定读取Cursor的具体操作
         * @param readableDatabase 已经被打开的对应表的可读数据库
         * @return 读取到的数据库行集
         */
        Cursor readFrom(SQLiteDatabase readableDatabase);
    }

    /**
     *  指定具体的对数据库修改、删除等行为的接口
     */
    interface WriteData {
        /**
         *  传入可写的数据库对象，指定修改数据库数据的具体操作
         * @param writableDatabase 已经被打开的的对应表的可写数据库
         */
        void writeTo(SQLiteDatabase writableDatabase);
    }
}
