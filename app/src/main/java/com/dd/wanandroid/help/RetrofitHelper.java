package com.dd.wanandroid.help;

import com.dd.wanandroid.App;
import com.dd.wanandroid.entity.ArticleData;
import com.dd.wanandroid.entity.BannerInfo;
import com.dd.wanandroid.entity.BasicData;
import com.dd.wanandroid.entity.Tree;
import com.dd.wanandroid.service.ApiService;
import com.dd.wanandroid.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class RetrofitHelper {

    private volatile static RetrofitHelper sInstance;

    private ApiService apiService;

    private Gson gson;

    public static RetrofitHelper getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitHelper();
                }
            }
        }
        return sInstance;
    }

    private RetrofitHelper() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        gson = builder.create();
        File cacheFile = new File(App.getContext().getExternalCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        // 对request的设置用来指定有网/无网下所走的方式
        // 对response的设置用来指定有网/无网下的缓存时长
        // 无网络情况下强制读取缓存中的数据，此时该请求不会发送出去。
        // 无网络时直接读取缓存数据，该缓存数据保持1周
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 对request的设置用来指定有网/无网下所走的方式
                // 对response的设置用来指定有网/无网下的缓存时长
                Request request = chain.request();
                // 无网络情况下强制读取缓存中的数据，此时该请求不会发送出去。
                if (!NetworkUtils.isNetWorkAvailable(App.getContext())) {
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                Response response = chain.proceed(request);

                if (NetworkUtils.isNetWorkAvailable(App.getContext())) {
                    int maxAge = 60 * 60 * 24;
                    String cacheControl = "public,max-age=" + maxAge;
                    return response.newBuilder().header("Cache-Control", cacheControl)
                            .removeHeader("Pragma").build();
                } else {
                    // 无网络时直接读取缓存数据，该缓存数据保持1周
                    int maxStale = 60 * 60 * 24 * 7;
                    return response.newBuilder()
                            .header("Cache-Control", "public,only-if-cached,max-stale=" + maxStale)
                            .removeHeader("Pragma").build();
                }
            }
        };
        okHttpClientBuilder.addNetworkInterceptor(cacheInterceptor);
        okHttpClientBuilder.addInterceptor(cacheInterceptor);
        okHttpClientBuilder.cache(cache);
        OkHttpClient client = okHttpClientBuilder.build();

        apiService = createRetrofit(client).create(ApiService.class);
    }

    private Retrofit createRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("http://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 将时间戳转化成Date类型
     */
    public class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    public Observable<BasicData<List<BannerInfo>>> getBanners() {
        return apiService.getBanners();
    }

    public Observable<BasicData<ArticleData>> getArticles(int page) {
        return apiService.getArticles(page);
    }

    public Observable<BasicData<ArticleData>> getProjects(int page) {
        return apiService.getProjects(page);
    }

    public Observable<BasicData<List<Tree>>> getTrees() {
        return apiService.getTrees();
    }

}
