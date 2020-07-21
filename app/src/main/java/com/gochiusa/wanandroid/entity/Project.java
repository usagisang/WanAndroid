package com.gochiusa.wanandroid.entity;

public class Project {
    private String title;
    private String niceDate;
    private String description;
    private String link;
    private String envelopePictureLink;
    private String author;

    public String getAuthor() {
        return author;
    }


    public String getDescription() {
        return description;
    }

    public String getEnvelopePictureLink() {
        return envelopePictureLink;
    }

    public String getLink() {
        return link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnvelopePictureLink(String envelopePictureLink) {
        this.envelopePictureLink = envelopePictureLink;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
