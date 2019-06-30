package com.dd.wanandroid.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.Tree;
import com.dd.wanandroid.ui.adapter.TitleFragmentPagerAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class TagActivity extends AppCompatActivity {

    private static final String TAG = "TagActivity";

    private int treeId;

    private Tree tree;

    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .init();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initData();
        initEvent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
    }

    private void initData() {
        if (getIntent() != null) {
            treeId = getIntent().getIntExtra("tree_id", -1);
        }
        Log.d(TAG, "initData: treeId=" + treeId);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Tree> trees = realm.where(Tree.class).equalTo("id", treeId).findAll();
        Log.d(TAG, "initData: trees.size=" + trees.size());
        if (trees.size() > 0) {
            tree = trees.get(0);
        }
        if (tree == null) {
            return;
        }
        toolbar.setTitle(tree.getName());
        int size = tree.getChildren().size();
        String[] titles = new String[size];
        for (int i = 0; i < size; i++) {
            Tree t = tree.getChildren().get(i);
            fragments.add(new Fragment());
            if (t != null) {
                titles[i] = t.getName();
            }
        }
        if (size < 5) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initEvent() {
    }

}
