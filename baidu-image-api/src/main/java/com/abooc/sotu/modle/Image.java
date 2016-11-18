package com.abooc.sotu.modle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dayu on 2016/11/8.
 */

public class Image implements Serializable {

    @SerializedName("id")
    public String id;

//    @SerializedName("abs")
//    public String abs;

    @SerializedName("thumbnail_url")
    public String thumbnail_url;
}
