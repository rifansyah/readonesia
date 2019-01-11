package com.dev.reef.readonesia.profile;

public class Riwayat {
    private String oppositeName;
    private String status;
    private int point;

    public Riwayat(String oppositeName, String status, int point) {
        this.oppositeName = oppositeName;
        this.status = status;
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getOppositeName() {
        return oppositeName;
    }

    public String getStatus() {
        return status;
    }
}
