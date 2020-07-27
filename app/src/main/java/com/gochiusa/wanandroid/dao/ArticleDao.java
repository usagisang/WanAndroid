package com.gochiusa.wanandroid.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gochiusa.wanandroid.entity.Article;

import java.util.List;

public class ArticleDao implements DatabaseDao<Article> {

    private DatabaseHelper mDatabaseHelper;

    /**
     *  线程锁
     */
    private final Object mLock = new Object();

    @Override
    public List<Article> selectAll() {
        return select((readableDatabase) ->
                readableDatabase.query(DatabaseConstant.ARTICLE_TABLE_NAME,
                null, null, null,
                null, null, null));
    }

    @Override
    public List<Article> select(ReadData readData) {
        synchronized (mLock) {
            SQLiteDatabase readableDatabase = mDatabaseHelper.getReadableDatabase();
            // 查询所有文章的信息
            Cursor articleCursor = readData.readFrom(readableDatabase);
            // 解析文章信息并返回
            List<Article> list = CursorPause.getArticles(articleCursor);
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
        delete(writableDatabase ->
                writableDatabase.delete(DatabaseConstant.ARTICLE_TABLE_NAME,
                        null, null));

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
    public void insert(List<Article> values) {
        synchronized (mLock) {
            SQLiteDatabase writableDatabase = mDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            // 迭代组装、插入新的数据
            for (Article article : values) {
                initContentValue(article, contentValues);
                writableDatabase.insert(DatabaseConstant.ARTICLE_TABLE_NAME,
                        null, contentValues);
            }
            writableDatabase.close();
        }
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


    /**
     *  单例模式
     */
    public static ArticleDao newInstance() {
        return GetDao.DAO;
    }

    private ArticleDao() {
        mDatabaseHelper = DatabaseHelper.openDefaultDatabase();
    }

    private static final class GetDao {
        private static final ArticleDao DAO = new ArticleDao();
    }
}
