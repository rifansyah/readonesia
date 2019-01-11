package com.dev.reef.readonesia.beranda;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/v2/top-headlines?country=id&apiKey=0f0398c0a8794c6f9c7159958fd04b45")
    Call<ApiModel> getAllBerita();
}
