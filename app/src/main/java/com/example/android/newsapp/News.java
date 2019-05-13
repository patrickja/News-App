package com.example.android.newsapp;

public class News {
    private String mTitle;
    private String mDate;
    private String mUrl;
    private String mType;
    private String mSection;
    private String mAuthor;

    public News(String title, String date, String url, String type, String section, String author) {
        mTitle = title;
        mDate = date;
        mUrl = url;
        mType = type;
        mSection = section;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getType() {
        return mType;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public String toString() {
        return " title " + getTitle();
    }
}