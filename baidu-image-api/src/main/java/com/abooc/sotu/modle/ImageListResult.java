package com.abooc.sotu.modle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dayu on 2016/11/8.
 */

public class ImageListResult implements Serializable {

    @SerializedName("tag1")
    String tag1;

    @SerializedName("tag2")
    String tag2;

    @SerializedName("totalNum")
    String totalNum;
    @SerializedName("start_index")
    String start_index;
    @SerializedName("return_number")
    String return_number;

    @SerializedName("data")
    Image[] data;


    @Override
    public String toString() {
        return "tag1:" + tag1 + ", tag2:" + tag2;
    }
}
