package com.gochiusa.wanandroid.util;

import android.database.Cursor;

import com.gochiusa.wanandroid.entity.Article;
import static com.gochiusa.wanandroid.util.DatabaseConstant.*;

import java.util.ArrayList;
import java.util.List;

public final class CursorPause {
    /**
     *  从数据库表中读取数据，并转化为实体类集合
     * @param cursor 数据集
     */
    public static List<Article> getArticles(Cursor cursor) {
        List<Article> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // 初始化实体类并设置相关信息
                Article article = new Article();
                article.setAuthor(cursor.getString(cursor.getColumnIndex(
                        AUTHOR_COLUMN_NAME)));
                article.setChapterName(cursor.getString(cursor.getColumnIndex(
                        CHAPTER_COLUMN_NAME)));
                article.setLink(cursor.getString(cursor.getColumnIndex(
                        LINK_COLUMN_NAME)));
                article.setNiceDate(cursor.getString(cursor.getColumnIndex(
                        DATE_COLUMN_NAME)));
                article.setSuperChapterName(cursor.getString(cursor.getColumnIndex(
                        SUPER_COLUMN_NAME)));
                article.setTitle(cursor.getString(cursor.getColumnIndex(
                        TITLE_COLUMN_NAME)));
                // 添加实体类至列表
                list.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     *  解析包含历史搜索记录的结果集
     * @return 历史搜索记录的字符串
     */
    public static List<String> getHistory(Cursor cursor) {
        List<String> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(QUERY_COLUMN_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
