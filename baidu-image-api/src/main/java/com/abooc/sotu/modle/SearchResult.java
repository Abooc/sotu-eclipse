package com.abooc.sotu.modle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dayu on 2016/11/8.
 */

public class SearchResult implements Serializable {

    @SerializedName("gsm")
    public String gsm;

    @SerializedName("data")
    public Image[] data;


    @Override
    public String toString() {
        return "gsm:" + gsm ;
//        return "gsm:" + gsm + ", data:" + data;
    }
}
