package com.dd.wanandroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dd.wanandroid.R;
import com.gyf.immersionbar.ImmersionBar;


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
}
