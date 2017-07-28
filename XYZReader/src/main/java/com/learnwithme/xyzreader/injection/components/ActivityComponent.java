package com.learnwithme.xyzreader.injection.components;

import com.learnwithme.xyzreader.injection.PerActivity;
import com.learnwithme.xyzreader.injection.modules.ActivityModule;
import com.learnwithme.xyzreader.ui.ArticleDetailActivity;
import com.learnwithme.xyzreader.ui.ArticleListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(ArticleListActivity articleListActivity);
    void inject(ArticleDetailActivity articleListActivity);
}