package org.lee.android.app.bean;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Entity {
    /**
     * 数据包
     * <p/>
     * 包含类目、信息总条数、数据的json串。
     *
     * @author ruiyuLee
     */
    public static class DataPakage {

        /**
         * 父类目
         */
        public String tag1;
        /**
         * 子类目
         */
        public String tag2;
        /**
         * 信息总条数
         */
        public int totalNum;
        public int start_index;
        public int return_number;
        public JsonElement data;

        public ArrayList<Channel> toArray() {
            Gson gson = new Gson();
            return gson.fromJson(data,
                    new TypeToken<ArrayList<Channel>>() {
                    }.getType()
            );
        }

        public String name() {
            return tag1 + "/" + tag2;
        }

        public String currentDesc() {
            return "当前查看位置: " + start_index + "-"
                    + (start_index + return_number) + "张";
        }

        public String total() {
            return "共:" + totalNum + "张图";
        }
    }

    public static class ImageEntity {
        public String id;
        public int pn;
        public String abs;
        public JsonElement tags;
        public String tag;
        public JsonElement desc;
        public String date;
        public String image_url;
        public String thumbnail_url;
        public String thumb_large_url;
        public String download_url;
        public int image_width;
        public int image_height;
        public int thumbnail_width;
        public int thumbnail_height;
        public String from_url;
        public String colum;
        public String desc_info;
        public int download_num;
        public int collect_num;

        public String desc() {
            return desc.toString();
        }
    }
}
