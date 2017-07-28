package com.learnwithme.xyzreader.data.remote;

import com.learnwithme.xyzreader.data.model.Article;

import io.reactivex.Observable;

import java.util.List;

import retrofit2.http.GET;

public interface RemoteArticlesService {
    String ENDPOINT = "https://go.udacity.com";

    @GET("/xyz-reader-json")
    Observable<List<Article>> loadArticlesFromServer();
}