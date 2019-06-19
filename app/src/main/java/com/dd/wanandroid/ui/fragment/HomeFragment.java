package com.dd.wanandroid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.Article;
import com.dd.wanandroid.entity.ArticleData;
import com.dd.wanandroid.entity.BannerInfo;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.help.RetrofitHelper;
import com.dd.wanandroid.listener.OnItemClickListener;
import com.dd.wanandroid.ui.WebActivity;
import com.dd.wanandroid.ui.adapter.ArticleAdapter;
import com.dd.wanandroid.ui.view.ListDecoration;
import com.dd.wanandroid.util.PicassoImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private Banner bannerView;

    private RecyclerView rvArticle;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<BannerInfo> banners;

    private List<String> urls;

    private List<String> titles;

    private List<Article> articles;

    private List<Article> topArticles;

    private ArticleAdapter articleAdapter;

    public HomeFragment() {
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bannerView.startAutoPlay();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView(View rootView) {
        bannerView = rootView.findViewById(R.id.banner);
        swipeRefreshLayout = rootView.findViewById(R.id.srl);
        rvArticle = rootView.findViewById(R.id.rv_article);
        rvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articles);
        rvArticle.setAdapter(new ArticleAdapter(articles));
        rvArticle.addItemDecoration(new ListDecoration(25));
    }


    private void initData() {
        urls = new ArrayList<>();
        titles = new ArrayList<>();

        articles = queryArticles();
        banners = queryBannerInfos();

        RetrofitHelper.getInstance().getTopArticles().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<List<Article>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<List<Article>> articleDataBasicData) {
                        Log.d(TAG, "onNext: ");
                        if (articleDataBasicData.getErrorCode() == 0) {
                            topArticles = articleDataBasicData.getData();
                            if (articles.size() > 0 && banners.size() > 0) {
                                Log.d(TAG, "query data from realm.");
                                initBanner();
                                articles.addAll(0, topArticles);

                                articleAdapter.setArticles(articles, topArticles.size());
                            } else {
                                queryData();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });


    }

    private void queryData() {
        Observable<BasicData<List<BannerInfo>>> bannerInfos = RetrofitHelper.getInstance().getBanners();
        final Observable<BasicData<ArticleData>> articlesObs = RetrofitHelper.getInstance().getArticles(0);
        Observable.merge(bannerInfos, articlesObs)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BasicData<? extends Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BasicData<?> basicData) {
                        if (basicData.getErrorCode() != 0) {
                            return;
                        }
                        if (basicData.getData() instanceof Collection) {
                            Log.d(TAG, "onNext#Banners#");
                            Collection<?> c = (Collection<?>) basicData.getData();
                            banners.clear();
                            for (Object o : c) {
                                if (o instanceof BannerInfo) {
                                    banners.add((BannerInfo) o);
                                }
                            }
                            initBanner();
                            saveBannerInfo();
                        } else if (basicData.getData() instanceof ArticleData) {
                            Log.d(TAG, "onNext#ArticleData#");
                            articles = ((ArticleData) basicData.getData()).getDatas();
                            articles.addAll(0, topArticles);
                            articleAdapter.setArticles(articles, topArticles.size());
                            saveArticle();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError#" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initEvent() {
        articleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("link", articles.get(position).getLink());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setClass(Objects.requireNonNull(getActivity()), WebActivity.class);
                } else if (getActivity() != null) {
                    intent.setClass(getActivity(), WebActivity.class);
                }
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "下拉刷新", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initBanner() {
        for (BannerInfo banner : banners) {
            urls.add(banner.getImagePath());
            titles.add(banner.getTitle());
        }
        bannerView.setImageLoader(new PicassoImageLoader());
        bannerView.setImages(urls);
        bannerView.setBannerTitles(titles);
        //设置banner动画效果
        bannerView.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        bannerView.isAutoPlay(true);
        //设置轮播时间
        bannerView.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        bannerView.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        bannerView.start();
    }

    private List<Article> queryArticles() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Article> articles = realm.where(Article.class).findAll();
        return realm.copyFromRealm(articles);
    }

    private List<BannerInfo> queryBannerInfos() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<BannerInfo> bannerInfos = realm.where(BannerInfo.class).findAll();
        return realm.copyFromRealm(bannerInfos);
    }

    private void saveArticle() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Article article : articles) {
            realm.copyToRealmOrUpdate(article);
        }
        realm.commitTransaction();
    }

    private void saveBannerInfo() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (BannerInfo bannerInfo : banners) {
            realm.copyToRealmOrUpdate(bannerInfo);
        }
        realm.commitTransaction();
    }

}
