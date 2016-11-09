package com.abooc.baidu_image_api;

import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.ImageListResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dayu on 2016/11/8.
 */

public interface BaiduImageApiService {

//    http://images.baidu.com/channel/listjson?tag1=美女&tag2=美臀&pn=1&rn=3

    //    @GET("users/{user}/repos")
    @GET("channel/listjson")
    rx.Observable<ImageListResult> listImages(
            @Query("tag1") String tag1,
            @Query("tag2") String tag2,
            @Query("pn") int pn,
            @Query("rn") int rn
    );

    @GET("channel/listjson")
    Call<ImageListResult> listImagesCall(
            @Query("tag1") String tag1,
            @Query("tag2") String tag2,
            @Query("pn") int pn,
            @Query("rn") int rn
    );

}
