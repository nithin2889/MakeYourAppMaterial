package com.learnwithme.xyzreader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.learnwithme.xyzreader.XYZReaderApp;
import com.learnwithme.xyzreader.injection.components.ActivityComponent;
import com.learnwithme.xyzreader.injection.components.DaggerActivityComponent;
import com.learnwithme.xyzreader.injection.modules.ActivityModule;

public class BaseActivity extends AppCompatActivity {

  private ActivityComponent activityComponent;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (activityComponent == null) {
      activityComponent = DaggerActivityComponent.builder()
          .activityModule(new ActivityModule(this))
          .applicationComponent(XYZReaderApp.get(this).getComponent())
          .build();
    }
  }

  public ActivityComponent getActivityComponent() {
    return activityComponent;
  }
}
