package com.gochiusa.wanandroid.util;

public final class DatabaseConstant {
    public static final String DATABASE_NAME = "LocalArticle.db";

    public static final String ARTICLE_TABLE_NAME = "article";
    public static final String AUTHOR_COLUMN_NAME = "author";
    public static final String TITLE_COLUMN_NAME = "title";
    public static final String SUPER_COLUMN_NAME = "super";
    public static final String CHAPTER_COLUMN_NAME = "chapter";
    public static final String LINK_COLUMN_NAME = "link";
    public static final String DATE_COLUMN_NAME = "date";

    public static final String HISTORY_TABLE_NAME = "history";
    public static final String QUERY_COLUMN_NAME = "query";

    private static final String TEXT_WITH_COMMA = " TEXT,";


    public static final String CREATE_ARTICLE_TABLE = "CREATE TABLE " + ARTICLE_TABLE_NAME
            +" (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            AUTHOR_COLUMN_NAME + TEXT_WITH_COMMA +
            TITLE_COLUMN_NAME + TEXT_WITH_COMMA +
            SUPER_COLUMN_NAME + TEXT_WITH_COMMA +
            CHAPTER_COLUMN_NAME + TEXT_WITH_COMMA +
            LINK_COLUMN_NAME + TEXT_WITH_COMMA +
            DATE_COLUMN_NAME + " TEXT)";

    public static final String CREATE_SEARCH_HISTORY_TABLE = "CREATE TABLE " + HISTORY_TABLE_NAME
            + " (" + QUERY_COLUMN_NAME + "TEXT NOT NULL)";
}
