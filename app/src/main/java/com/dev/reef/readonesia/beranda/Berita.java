package com.dev.reef.readonesia.beranda;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Berita {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("author")
    private String author;
    @SerializedName("publishedAt")
    private String publishedAt;
    @SerializedName("urlToImage")
    private String urlToImage;
    @SerializedName("url")
    private String url;

    public Berita(){

    }

    public Berita(String title, String description, String author, String publishedAt, String urlToImage, String url) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.url = url;
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

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl() {
        return url;
    }
}

class ApiModel{
    @SerializedName("status")
    public String status;
    @SerializedName("totalResults")
    public int totalResults;
    @SerializedName("articles")
    public List<Berita> articles;
}