package com.dd.wanandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.ArticleData;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.help.RetrofitHelper;
import com.gyf.immersionbar.ImmersionBar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectActivity extends AppCompatActivity {

    private static final String TAG = "CollectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    private void initData() {
        RetrofitHelper.getInstance().getCollectArticles(0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<ArticleData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<ArticleData> articleDataBasicData) {
                        Log.d(TAG, "onNext: " + articleDataBasicData.toString());
                        if(articleDataBasicData.getErrorCode() != 0) {
                            Toast.makeText(CollectActivity.this, articleDataBasicData.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        } else {

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
}
