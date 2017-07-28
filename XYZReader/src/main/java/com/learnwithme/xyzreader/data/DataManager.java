package com.learnwithme.xyzreader.data;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.learnwithme.xyzreader.data.local.DatabaseHelper;
import com.learnwithme.xyzreader.data.model.Article;
import com.learnwithme.xyzreader.data.remote.RemoteArticlesService;
import com.learnwithme.xyzreader.utils.RxUtils;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    private final RemoteArticlesService articlesService;
    private final DatabaseHelper databaseHelper;
    private final BehaviorRelay<Integer> requestState;

    @Inject
    public DataManager(RemoteArticlesService articlesService, DatabaseHelper databaseHelper,
                       BehaviorRelay<Integer> requestState) {

        this.articlesService = articlesService;
        this.databaseHelper = databaseHelper;
        this.requestState = requestState;
    }

    private void publishRequestMode(@RequestMode.State int state) {
        Observable.just(state)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestState);
    }

    public Completable refreshArticles() {
        return articlesService
                .loadArticlesFromServer()
                .compose(RxUtils.applySchedulers())
                .doOnSubscribe(disposable -> publishRequestMode(RequestMode.LOADING))
                .doOnError(throwable -> publishRequestMode(RequestMode.ERROR))
                .doOnComplete(() -> publishRequestMode(RequestMode.COMPLETED))
                .flatMapCompletable(databaseHelper::saveArticlesToDatabase);
    }

    public Observable<Article> getArticlesDataFromObservable() {
        return databaseHelper
                .getArticlesFromDatabase()
                .compose(RxUtils.applySchedulers())
                .flatMap(Observable::fromIterable)
                .distinct();
    }

    public Single<Article> getSingleArticle(int id) {
        return databaseHelper
                .getSingleArticleFromDatabase(id)
                .compose(RxUtils.applySchedulers())
                .firstOrError();
    }

    public BehaviorRelay<Integer> getRequestState() {
        return requestState;
    }
}