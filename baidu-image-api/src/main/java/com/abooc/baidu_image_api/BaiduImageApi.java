package com.abooc.baidu_image_api;

import com.abooc.sotu.modle.ImageListResult;

import retrofit2.Call;

/**
 * Created by dayu on 2016/11/8.
 */

public interface BaiduImageApi {

    Call<ImageListResult> getImageList();
}
