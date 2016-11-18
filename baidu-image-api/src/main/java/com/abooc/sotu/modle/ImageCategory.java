package com.abooc.sotu.modle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dayu on 2016/11/8.
 */

public class ImageCategory implements Serializable {

    @SerializedName("tag1")
    public String tag1;

    @SerializedName("tag2")
    public String tag2;

    @SerializedName("totalNum")
    public String totalNum;
    @SerializedName("start_index")
    public String start_index;
    @SerializedName("return_number")
    public String return_number;

    @SerializedName("data")
    public Image[] data;


    @Override
    public String toString() {

        int size = data == null ? -1 : data.length;
        return "tag1:" + tag1 + ", tag2:" + tag2 + ", size:" + size;
    }
}
