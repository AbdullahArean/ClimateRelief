package com.arean.ClimateRelief.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.arean.ClimateRelief.BuildConfig;
import com.arean.ClimateRelief.R;
import com.arean.ClimateRelief.model.db.MyObjectBox;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class MyApplication extends Application {
  private static BoxStore boxStore;
  public static LocaleManager localeManager;

  public static BoxStore getBoxStore() {
    return boxStore;
  }

  @Override
  public void onCreate() {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    super.onCreate();
    ViewPump.init(ViewPump.builder()
        .addInterceptor(new CalligraphyInterceptor(
            new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Vazir.ttf")
                //.setFontAttrId(R.attr.fontpath)
                    .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                .build()))
        .build());
    createBoxStore();
    if (SharedPreferencesUtil.getInstance(this).isDarkThemeEnabled())
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    else
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
  }

  private void createBoxStore() {
    boxStore = MyObjectBox.builder().androidContext(MyApplication.this).build();
    if (BuildConfig.DEBUG) {
      new AndroidObjectBrowser(boxStore).start(this);
    }
  }

  @Override
  protected void attachBaseContext(Context base) {
    localeManager = new LocaleManager(base);
    super.attachBaseContext(localeManager.setLocale(base));
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    localeManager.setLocale(this);
  }
}
