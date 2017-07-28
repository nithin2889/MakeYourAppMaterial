package com.learnwithme.xyzreader.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.learnwithme.xyzreader.R;
import com.learnwithme.xyzreader.data.DataManager;
import com.learnwithme.xyzreader.data.RequestMode;
import com.learnwithme.xyzreader.data.model.Article;
import com.learnwithme.xyzreader.ui.ArticlesAdapter.OnArticleClickListener;
import com.learnwithme.xyzreader.utils.NetworkUtils;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import javax.inject.Inject;

import timber.log.Timber;

@SuppressWarnings("ConstantConditions")
public class ArticleListActivity extends BaseActivity implements
        OnArticleClickListener, OnRefreshListener {

    @Inject
    DataManager dataManager;

    @BindInt(R.integer.list_column_count)
    int columnsCount;

    @BindString(R.string.error_msg)
    String errorMessage;

    @BindString(R.string.error_msg_unknown)
    String errorMessageUnknown;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArticlesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnsCount, StaggeredGridLayoutManager.VERTICAL);

        adapter = new ArticlesAdapter(this);
        adapter.setHasStableIds(true);

        recyclerView.setLayoutManager(sglm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        subscribeToDbStream();
        handleLoadingStatus();

        if (savedInstanceState == null) {
            syncData();
        }
    }

    @Override
    public void onRefresh() {
        syncData();
    }

    @Override
    public void articleClicked(int id) {
        Intent intent = ArticleDetailActivity.prepareIntent(this, id);

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void handleLoadingStatus() {
        dataManager.getRequestState().subscribe(state -> {
            switch (state) {
                case RequestMode.IDLE:
                    break;
                case RequestMode.LOADING:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                case RequestMode.COMPLETED:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case RequestMode.ERROR:
                    showErrorMessage();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        });
    }

    private void showErrorMessage() {
        String message = NetworkUtils.isNetworkConnected(this) ?
                errorMessageUnknown : errorMessage;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void syncData() {
        dataManager
            .refreshArticles()
            .subscribe(getSyncObserver());
    }

    private void subscribeToDbStream() {
        dataManager
                .getArticlesDataFromObservable()
                .subscribe(getDbStreamObserver());
    }

    private Observer<Article> getDbStreamObserver() {
        return new Observer<Article>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                adapter.clearList();
            }

            @Override
            public void onNext(@NonNull Article article) {
                Timber.d("Fetch article from db: " + article.title());
                adapter.addArticle(article);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Articles loading - error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Error loading articles",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Toast.makeText(getApplicationContext(), "Articles loaded successfully..",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private CompletableObserver getSyncObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("Sync started...");
            }

            @Override
            public void onComplete() {
                Timber.d("Sync finished...");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Sync failed! Error: " + e.getMessage());
            }
        };
    }
}
