package com.dd.wanandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.User;
import com.dd.wanandroid.help.RetrofitHelper;
import com.gyf.immersionbar.ImmersionBar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;


public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;

    private EditText edtPassword;

    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimaryDark)
                .init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
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
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
    }

    private void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    // TODO 校验用户名
                } else if (TextUtils.isEmpty(password)) {
                    // TODO 校验密码
                } else {
                    login(username, password);
                }
            }
        });
    }

    private void login(final String username, String password) {
        RetrofitHelper.getInstance().login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<User> userBasicData) {
                        if (userBasicData.getErrorCode() == 0) {
                            User user = userBasicData.getData();
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            saveUser(user);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, userBasicData.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LoginActivity", "onError#" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("LoginActivity", "onComplete");
                    }
                });
    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }
}
