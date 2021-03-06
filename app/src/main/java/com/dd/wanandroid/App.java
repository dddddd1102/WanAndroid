package com.dd.wanandroid;

import android.app.Application;
import android.content.Context;

import cn.like.nightmodel.NightModelManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name("wanandroid.realm")
                .deleteRealmIfMigrationNeeded()
                .build());
        NightModelManager.getInstance().init(this);
    }

    public static Context getContext() {
        return context;
    }
}
