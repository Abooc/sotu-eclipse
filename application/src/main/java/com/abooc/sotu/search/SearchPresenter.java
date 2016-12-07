package com.abooc.sotu.search;

import com.abooc.baidu_image_api.BaiduClient;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.SearchResult;
import com.abooc.util.Debug;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dayu on 2016/11/10.
 */

public class SearchPresenter implements SearchContract.Presenter {

    SearchContract.View mView;
    BaiduClient mClient = BaiduClient.getInstance();

    public SearchPresenter(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void load(String word) {
        Call<SearchResult> call = BaiduClient.getInstance().search(word, 1, 50);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                SearchResult body = response.body();

                mView.showInfo(body);
                List<Image> images = Arrays.asList(body.data);
                mView.showImages(images);
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Debug.error("TAG" + " onFailure:" + t);
            }
        });
    }

    @Override
    public void start() {

    }
}
