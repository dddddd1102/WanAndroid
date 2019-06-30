package com.dd.wanandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.HotKey;
import com.dd.wanandroid.help.RetrofitHelper;
import com.gyf.immersionbar.ImmersionBar;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

//    private TagLayout tagLayout;

    private List<HotKey> hotKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .init();
        Toolbar toolbar = findViewById(R.id.toolbar);
//        tagLayout = findViewById(R.id.tag_layout);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        hotKeyList = queryHotKeys();
        if (hotKeyList.size() > 0) {
            return;
        }
        RetrofitHelper.getInstance().getHotKeys().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<List<HotKey>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<List<HotKey>> listBasicData) {
                        if (listBasicData.getErrorCode() == 0) {
                            hotKeyList = listBasicData.getData();
                            saveHotKeys();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initEvent() {
    }

    private List<HotKey> queryHotKeys() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HotKey> hotKeys = realm.where(HotKey.class).findAll();
        return realm.copyFromRealm(hotKeys);
    }

    private void saveHotKeys() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (HotKey hotKey : hotKeyList) {
            realm.copyToRealmOrUpdate(hotKey);
        }
        realm.commitTransaction();
    }
}
