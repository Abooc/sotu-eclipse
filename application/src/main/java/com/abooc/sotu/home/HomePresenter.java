package com.abooc.sotu.home;

import android.util.Log;

import com.abooc.baidu_image_api.BaiduClient;
import com.abooc.baidu_image_api.test.data.TestTag1;
import com.abooc.baidu_image_api.test.data.TestTag2;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.ImageCategory;
import com.abooc.util.Debug;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dayu on 2016/11/10.
 */

public class HomePresenter implements HomeContract.Presenter {

    HomeContract.View mView;
    BaiduClient mClient = BaiduClient.getInstance();

    public HomePresenter(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void load() {
        Log.d("Debug", "--" + TestTag2.NULL.name());
        mClient.getImageList(TestTag1.宠物.name(), TestTag2.NULL.name(), 1, 30)
                .enqueue(new Callback<ImageCategory>() {
                    @Override
                    public void onResponse(Call<ImageCategory> call, Response<ImageCategory> response) {
                        Debug.anchor("onCompleted call:" + call + ", response:" + response);

                        ImageCategory body = response.body();

                        mView.showInfo(body);
                        List<Image> images = Arrays.asList(body.data);
                        mView.showImages(images);
                    }

                    @Override
                    public void onFailure(Call<ImageCategory> call, Throwable t) {

                        Debug.anchor("TAG" + "" + t);
                    }
                });

    }

    @Override
    public void start() {

    }
}
