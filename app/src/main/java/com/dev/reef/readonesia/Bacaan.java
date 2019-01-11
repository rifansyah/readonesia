package com.dev.reef.readonesia;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Bacaan {
    private String title;
    private String description;
    private String category;
    private String author;
    private String publishedAt;
    private String urlToImage;
    private String id;
    private String authorId;

    public Bacaan(){

    }

    public Bacaan(String title, String description, String category, String author, String publishedAt, String urlToImage, String id, String authorId) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.author = author;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.id = id;
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }
}
