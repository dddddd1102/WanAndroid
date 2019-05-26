package com.dd.wanandroid.service;

import com.dd.wanandroid.entity.ArticleData;
import com.dd.wanandroid.entity.BannerInfo;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.Tree;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * WanAndroid
 *
 * @author daidong
 */
public interface ApiService {

    @GET("banner/json")
    Observable<BasicData<List<BannerInfo>>> getBanners();

    @GET("article/list/{page}/json")
    Observable<BasicData<ArticleData>> getArticles(@Path("page") int page);

    @GET("project/list/{page}/json?cid=294")
    Observable<BasicData<ArticleData>> getProjects(@Path("page") int page);

    @GET("tree/json")
    Observable<BasicData<List<Tree>>> getTrees();

}
