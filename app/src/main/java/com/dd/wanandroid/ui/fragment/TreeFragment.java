package com.dd.wanandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.Tree;
import com.dd.wanandroid.help.RetrofitHelper;
import com.dd.wanandroid.ui.adapter.TreeAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class TreeFragment extends Fragment {

    private static final String TAG = "TreeFragment";

    private RecyclerView rvTree;

    private TreeAdapter treeAdapter;

    private List<Tree> trees;

    public TreeFragment() {
    }

    public static TreeFragment newInstance() {
        return new TreeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    private void initView(View view) {
        rvTree = view.findViewById(R.id.rv_tree);
        trees = new ArrayList<>();
        treeAdapter = new TreeAdapter(trees, getContext());
        rvTree.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTree.setAdapter(treeAdapter);
    }

    private void initData() {
        trees = queryTrees();
        if (trees != null && trees.size() > 0) {
            Log.d(TAG, "query trees from realm");
            treeAdapter.setTrees(trees);
            return;
        }
        RetrofitHelper.getInstance().getTrees().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<List<Tree>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<List<Tree>> listBasicData) {
                        if (listBasicData.getErrorCode() == 0) {
                            trees = listBasicData.getData();
                            treeAdapter.setTrees(trees);
                            saveTrees();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initEvent() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private List<Tree> queryTrees() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Tree> trees = realm.where(Tree.class).equalTo("parentChapterId", 0).findAll();
        return realm.copyFromRealm(trees);
    }

    private void saveTrees() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Tree tree : trees) {
            realm.copyToRealmOrUpdate(tree);
        }
        realm.commitTransaction();
    }
}
