package com.gochiusa.wanandroid.entity;

public class Article {
    private String author;
    private String title;
    private String superChapterName;
    private String chapterName;
    private String link;
    private String niceDate;

    public String getAuthor() {
        return author;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getLink() {
        return link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
