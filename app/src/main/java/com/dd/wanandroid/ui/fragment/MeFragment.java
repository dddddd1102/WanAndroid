package com.dd.wanandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.User;
import com.dd.wanandroid.ui.LoginActivity;
import com.dd.wanandroid.ui.view.ItemView;

import io.realm.Realm;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE = 1000;

    private View rootView;

    private ConstraintLayout clHeader;

    private TextView tvUsername;

    private ItemView itemCollection;

    private ItemView itemNightMode;

    private ItemView itemSetting;

    private ItemView itemAbout;

    private ItemView itemLogout;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert inflater != null;
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        initData();
        initEvent();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView() {
        clHeader = rootView.findViewById(R.id.cl_header);
        tvUsername = rootView.findViewById(R.id.tv_user_name);
        itemCollection = rootView.findViewById(R.id.item_collect);
        itemNightMode = rootView.findViewById(R.id.item_night_mode);
        itemSetting = rootView.findViewById(R.id.item_setting);
        itemAbout = rootView.findViewById(R.id.item_about);
        // TODO 需要添加登陆判断，如果未登陆不显示此选项
        itemLogout = rootView.findViewById(R.id.item_logout);
    }

    private void initData() {}

    private void initEvent() {
        clHeader.setOnClickListener(this);
        itemCollection.setOnClickListener(this);
        itemNightMode.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        itemAbout.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user == null) {
            tvUsername.setText(R.string.me_login_or_register);
            clHeader.setClickable(true);
        } else {
            tvUsername.setText(user.getUsername());
            clHeader.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_header:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.item_collect:
                Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_night_mode:
                Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_setting:
                Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_about:
                Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_logout:
                Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // TODO 处理数据
        }
    }
}
