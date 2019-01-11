package com.dev.reef.readonesia;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post {
    private String category;
    private String description;
    private String publishedAt;
    private String title;
    private String urlToImage;

    public Post() {

    }

    public Post(String title, String description, String category, String publishedAt, String urlToImage) {
        this.category = category;
        this.description = description;
        this.publishedAt = publishedAt;
        this.title = title;
        this.urlToImage = urlToImage;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
