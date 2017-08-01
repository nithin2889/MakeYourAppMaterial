package com.learnwithme.xyzreader.data.remote;

import com.learnwithme.xyzreader.data.model.Article;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RemoteArticlesService {
    String ENDPOINT = "https://raw.githubusercontent.com/TNTest/";

    @GET("xyzreader/master/data.json")
    Observable<List<Article>> loadArticlesFromServer();
}