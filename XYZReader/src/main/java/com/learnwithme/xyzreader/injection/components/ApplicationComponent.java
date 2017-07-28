package com.learnwithme.xyzreader.injection.components;

import android.app.Application;
import android.content.Context;

import com.learnwithme.xyzreader.XYZReaderApp;
import com.learnwithme.xyzreader.data.DataManager;
import com.learnwithme.xyzreader.data.local.DatabaseHelper;
import com.learnwithme.xyzreader.injection.ApplicationContext;
import com.learnwithme.xyzreader.injection.modules.ApplicationModule;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(XYZReaderApp xyzReaderApp);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    DatabaseHelper getDatabaseHelper();
}