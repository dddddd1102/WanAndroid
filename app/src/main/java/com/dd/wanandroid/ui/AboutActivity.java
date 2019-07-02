package com.dd.wanandroid.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dd.wanandroid.R;
import com.dd.wanandroid.ui.view.ItemView;
import com.dd.wanandroid.util.SpUtils;
import com.gyf.immersionbar.ImmersionBar;

import cn.like.nightmodel.NightModelManager;

public class AboutActivity extends AppCompatActivity {

    private TextView tvIcon;

    private ItemView itemVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NightModelManager.getInstance().attach(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.me_about);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        NightModelManager.getInstance().detach(this);
        super.onDestroy();
    }

    private void initView() {
        tvIcon = findViewById(R.id.tv_icon);
        itemVersion = findViewById(R.id.item_version);
    }

    private void initData() {
        tvIcon.setText(getVersion());
        itemVersion.setContent(getVersion());
        if (SpUtils.queryNightMode(this)) {
            NightModelManager.getInstance().applyNightModel(this);
        } else {
            NightModelManager.getInstance().applyDayModel(this);
        }
    }

    private String getVersion() {
        String versionName = "";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
