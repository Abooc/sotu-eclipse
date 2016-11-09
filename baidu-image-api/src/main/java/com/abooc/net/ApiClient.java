package com.abooc.net;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dayu on 2016/11/8.
 */
public class ApiClient {

    String baseUrl = "http://images.baidu.com/";
    File cacheDir;
    public static final String CACHE_DIR = "com.abooc.suto.cache";

    private static ApiClient ourInstance = new ApiClient();

    public static ApiClient getInstance() {
        return ourInstance;
    }

    private ApiClient() {
    }

    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    public Retrofit getRetrofit() {
        OkHttpClient client =
                new OkHttpClient.Builder()
//                        .cache(new Cache(cacheDir, 1024 * 1024 * 50))
//                        .addNetworkInterceptor(interceptor)
                        .build();

        return new Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}
