package com.dev.reef.readonesia.profile;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String name;
    private String username;
    private String email;
    private int poin;
    private String imageUrl;

    public User(String name, String username, String email, int poin, String imageUrl) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.poin = poin;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public int getPoin() {
        return poin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
