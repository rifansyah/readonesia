package com.dev.reef.readonesia;

public class Comment {
    private String idCommenter;
    private String comment;

    public Comment(String idCommenter, String comment) {
        this.idCommenter = idCommenter;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getIdCommenter() {
        return idCommenter;
    }
}
