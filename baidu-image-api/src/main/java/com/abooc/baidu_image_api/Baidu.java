package com.abooc.baidu_image_api;

import com.abooc.sotu.modle.ImageCategory;
import com.abooc.sotu.modle.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dayu on 2016/11/8.
 */

public interface Baidu {

//    http://images.baidu.com/channel/listjson?tag1=摄影&tag2=全部&pn=1&rn=3

    // http://image.baidu.com/search/wisejsonala?tn=wisejsonala&ie=utf8&word=风景&fr=&pn=0&rn=3;

    @GET("search/wisejsonala")
    Call<SearchResult> search(
            @Query("tn") String tn,
            @Query("ie") String ie,
            @Query("word") String word,
            @Query("width") String width,
            @Query("height") String height,
            @Query("pn") int pn,
            @Query("rn") int rn
    );

    @GET("channel/listjson")
    Call<ImageCategory> listImagesCall(
            @Query("tag1") String tag1,
            @Query("tag2") String tag2,
            @Query("pn") int pn,
            @Query("rn") int rn
    );

}
