package com.abooc.sotu.modle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dayu on 2016/11/8.
 */

public class Image implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("abs")
    String abs;

    @SerializedName("download_url")
    String download_url;
}
