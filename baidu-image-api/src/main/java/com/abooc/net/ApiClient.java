package com.abooc.net;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                return null;
                            }
                        })
//                        .addNetworkInterceptor()
//                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build();

        Retrofit.Builder builder = new Retrofit.Builder();


        return builder.baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

}
