package com.dd.wanandroid.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.User;
import com.dd.wanandroid.help.RetrofitHelper;
import com.dd.wanandroid.ui.AboutActivity;
import com.dd.wanandroid.ui.CollectActivity;
import com.dd.wanandroid.ui.LoginActivity;
import com.dd.wanandroid.ui.view.ItemView;
import com.dd.wanandroid.util.SpUtils;

import cn.like.nightmodel.NightModelManager;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";

    private View rootView;

    private ConstraintLayout clHeader;

    private TextView tvUsername;

    private ItemView itemCollection;

    private ItemView itemNightMode;

    private ItemView itemSetting;

    private ItemView itemAbout;

    private ItemView itemLogout;

    private AlertDialog alertDialog;

    private User user;

    private OnNightModeChangedListener onNightModeChangedListener;

    private boolean nightMode = false;

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
        onNightModeChangedListener = (OnNightModeChangedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNightModeChangedListener = null;
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

    private void initData() {
        nightMode = SpUtils.queryNightMode(getActivity());
    }

    private void initEvent() {
        clHeader.setOnClickListener(this);
        itemCollection.setOnClickListener(this);
        itemNightMode.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        itemAbout.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
        itemNightMode.setOnSwitchChangedListener(new ItemView.OnSwitchChangedListener() {
            @Override
            public void onSwitchChanged(boolean isChecked) {
                SpUtils.saveNightMode(getContext(), isChecked);
                if (onNightModeChangedListener != null) {
                    onNightModeChangedListener.onNightModeChanged(isChecked);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    private void refreshUI() {
        itemNightMode.setChecked(nightMode);
        Realm realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        if (user == null) {
            tvUsername.setText(R.string.me_login_or_register);
            clHeader.setClickable(true);
            itemLogout.setVisibility(View.GONE);
        } else {
            tvUsername.setText(user.getUsername());
            clHeader.setClickable(false);
            itemLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_header:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.item_collect:
                if (user != null) {
                    startActivity(new Intent(getActivity(), CollectActivity.class));
                } else {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.item_night_mode:
                if (itemNightMode.isChecked()) {
                    itemNightMode.setChecked(false);
                    SpUtils.saveNightMode(getContext(), false);
                    if (onNightModeChangedListener != null) {
                        onNightModeChangedListener.onNightModeChanged(false);
                    }
                } else {
                    itemNightMode.setChecked(true);
                    SpUtils.saveNightMode(getContext(), true);
                    if (onNightModeChangedListener != null) {
                        onNightModeChangedListener.onNightModeChanged(true);
                    }
                }
                break;
            case R.id.item_setting:
                Toast.makeText(getActivity(), "功能开发中...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.item_logout:
                showLogout();
                break;
            default:
                break;
        }
    }

    private void showLogout() {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                    dialog.dismiss();
                }
            });
            builder.setTitle("确认退出？");
            alertDialog = builder.create();
        }
        alertDialog.show();
    }

    private void logout() {
        RetrofitHelper.getInstance().logout().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<String> stringBasicData) {
                        if (stringBasicData.getErrorCode() == 0) {
                            removeLoginInfo();
                            refreshUI();
                        } else {
                            Toast.makeText(getActivity(), "退出失败！", Toast.LENGTH_SHORT).show();
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

    private void removeLoginInfo() {
        RetrofitHelper.getInstance().clearCookies();
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<User> realmResults = realm.where(User.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
            }
        });
    }

    public interface OnNightModeChangedListener {
        void onNightModeChanged(boolean nightMode);
    }

}
