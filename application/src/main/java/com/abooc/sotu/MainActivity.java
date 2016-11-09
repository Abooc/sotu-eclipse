package com.abooc.sotu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.abooc.baidu_image_api.BaiduImageClient;
import com.abooc.sotu.modle.ImageListResult;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dayu on 2016/11/8.
 */

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.MainText)
    TextView json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        BaiduImageClient.getInstance().getImageList()
                .enqueue(new Callback<ImageListResult>() {
                    @Override
                    public void onResponse(Call<ImageListResult> call, Response<ImageListResult> response) {
                        Log.d("TAG", "onCompleted call:" + call + ", response:" + response);

                        System.out.println("TAG, call:" + call + ", response:" + response);

                        json.setText(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ImageListResult> call, Throwable t) {

                        System.out.println("TAG" + "" + t);
                    }
                });
    }

}
