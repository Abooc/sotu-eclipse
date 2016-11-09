package com.abooc.baidu_image_api;

import com.abooc.net.ApiClient;
import com.abooc.sotu.modle.ImageListResult;

import retrofit2.Call;

/**
 * Created by dayu on 2016/11/8.
 */
public class BaiduImageClient implements BaiduImageApi {

    private BaiduImageApiService observable;

    private static BaiduImageClient ourInstance = new BaiduImageClient();

    public static BaiduImageClient getInstance() {
        return ourInstance;
    }

    private BaiduImageClient() {
        observable = ApiClient.getInstance().getRetrofit().create(BaiduImageApiService.class);
    }

    @Override
    public Call<ImageListResult> getImageList() {
        return observable.listImagesCall("美女", "全部", 1, 2);
    }
}
