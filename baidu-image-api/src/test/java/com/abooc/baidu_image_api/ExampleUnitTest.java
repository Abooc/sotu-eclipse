package com.abooc.baidu_image_api;

import com.abooc.sotu.modle.ImageCategory;
import com.abooc.sotu.modle.SearchResult;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        BaiduClient.getInstance().getImageList("风景", "全部", 1, 2).enqueue(new Callback<ImageCategory>() {
            @Override
            public void onResponse(Call<ImageCategory> call, Response<ImageCategory> response) {

            }

            @Override
            public void onFailure(Call<ImageCategory> call, Throwable t) {

            }
        });
    }

    @Test
    public void search() {
        System.out.println("TAG" + " onResponse:");
        Call<SearchResult> call = BaiduClient.getInstance().search("风景", 1, 3);
//        try {
//            Response<SearchResult> response = call.execute();
//            System.out.println("TAG" + " onResponse:" + response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                System.out.println("TAG" + " onResponse:");
                SearchResult body = response.body();

//                List<Image> images = Arrays.asList(body.data);
//                System.out.println("TAG" + " onResponse:" + images);
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                System.out.println("TAG" + " onFailure:" + t);

            }
        });
        System.out.println("TAG" + " :" + call.isExecuted());
    }
}