package com.mrdeveloper.newsapps;

public class NewsModel {

    private String title;
    private String description;
    private String imageUrl;
    private String publishTime;

    public NewsModel(String title, String description, String imageUrl, String publishTime) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishTime = publishTime;
    }

    public NewsModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishTime() {
        return publishTime;
    }
}
