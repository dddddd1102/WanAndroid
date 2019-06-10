package com.dd.wanandroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.HotKey;
import com.dd.wanandroid.help.RetrofitHelper;
import com.dd.wanandroid.ui.fragment.HomeFragment;
import com.dd.wanandroid.ui.fragment.MeFragment;
import com.dd.wanandroid.ui.fragment.TreeFragment;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "WanAndroid#HomeActivity";

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private HomeFragment homeFragment;

    private TreeFragment treeFragment;

    private MeFragment meFragment;

    private List<HotKey> hotKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void initData() {
        homeFragment = HomeFragment.newInstance();
        treeFragment = TreeFragment.newInstance();
        meFragment = MeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_content, homeFragment).commit();

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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        toolbar.setTitle(R.string.menu_home);
                        transaction.replace(R.id.layout_content, homeFragment);
                        break;
                    case R.id.action_tag:
                        toolbar.setTitle(R.string.menu_tag);
                        transaction.replace(R.id.layout_content, treeFragment);
                        break;
                    case R.id.action_me:
                        toolbar.setTitle(R.string.menu_me);
                        transaction.replace(R.id.layout_content, meFragment);
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
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
