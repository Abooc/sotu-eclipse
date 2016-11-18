package com.abooc.sotu.group;

import com.abooc.baidu_image_api.BaiduClient;
import com.abooc.sotu.modle.SearchResult;
import com.abooc.util.Debug;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dayu on 2016/11/10.
 */

public class GroupPresenter implements GroupContract.Presenter {

    GroupContract.View mView;
    BaiduClient mClient = BaiduClient.getInstance();

    public GroupPresenter(GroupContract.View view) {
        mView = view;
    }

    @Override
    public void load() {
        mClient.search("风景", 1, 50)
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        SearchResult body = response.body();

                        mView.showInfo(body);
//                        List<Image> images = Arrays.asList(body.data);
//                        mView.showImages(images);
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        Debug.anchor("TAG" + "" + t);
                    }
                });
    }

    @Override
    public void start() {

    }
}
