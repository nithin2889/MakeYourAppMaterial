package com.learnwithme.xyzreader.injection.modules;

import android.app.Application;
import android.content.Context;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.learnwithme.xyzreader.data.RequestMode;
import com.learnwithme.xyzreader.data.remote.RemoteArticlesService;
import com.learnwithme.xyzreader.data.remote.RemoteServiceFactory;
import com.learnwithme.xyzreader.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    RemoteArticlesService provideArticlesService() {
        return RemoteServiceFactory.createFrom(RemoteArticlesService.class, RemoteArticlesService.ENDPOINT);
    }

    @Provides
    @Singleton
    BehaviorRelay<Integer> provideBehaviorRelay() {
        return BehaviorRelay.createDefault(RequestMode.IDLE);
    }
}