package com.abooc.baidu_image_api;

import com.abooc.net.ApiClient;
import com.abooc.sotu.modle.ImageCategory;
import com.abooc.sotu.modle.SearchResult;

import retrofit2.Call;

/**
 * Created by dayu on 2016/11/8.
 */
public class BaiduClient extends ApiClient {


    private Baidu mBaidu;

    private static BaiduClient ourInstance = new BaiduClient();

    public static BaiduClient getInstance() {
        return ourInstance;
    }

    private BaiduClient() {
        mBaidu = getRetrofit().create(Baidu.class);
    }

    /**
     * @param tag1 大类别
     * @param tag2 小类别
     * @param pn   第几页
     * @param rn   每页数量
     * @return 返回结果集对象
     */
    public Call<ImageCategory> getImageList(String tag1, String tag2, int pn, int rn) {
        return mBaidu.listImagesCall(tag1, tag2, pn, rn);
    }

    public Call<SearchResult> search(String word, String width, String height, int pn, int rn) {
        return mBaidu.search("wisejsonala", "utf8", word, width, height, pn, rn);
    }

    public Call<SearchResult> search(String word, int pn, int rn) {
        return search(word, "", "", pn, rn);
    }
}
