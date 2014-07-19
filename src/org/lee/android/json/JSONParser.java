package org.lee.android.json;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.bean.Entity.ImageEntity;

import java.util.ArrayList;

/**
 * Json Parser for JO or Bean.
 *
 * @author ruiyuLee
 */
public class JSONParser {

    public static DataPakage toParsePakage(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DataPakage.class);
    }

    public static class SearchPackage {
        String queryEnc = "";
        public String queryExt = "";
        public String listNum = "1937";
        String displayNum = "2737";
        String bdFmtDispNum = "约2,730";
        String bdSearchTime = "0.000";
        String bdIsClustered = "1";
        public String data = "data";
    }

    public static class SearchImage {
        public String objURL = "";
        public String tag;
        public int width;
        public int height;
        public String fromPageTitleEnc = "";
        public String di = "";
        public String fromURL = "";
    }

    public static SearchPackage toSearchPackage(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SearchPackage.class);
    }

    public static ArrayList<SearchImage> toSearchImage(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return null;
        }
        int size = jsonArray.length();
        ArrayList<SearchImage> array = new ArrayList<SearchImage>();
        for (int i = 0; i < size; i++) {
            try {
                JSONObject jo = jsonArray.getJSONObject(i);
                SearchImage entity;

                Gson gson = new Gson();
                entity = gson.fromJson(jo.toString(), SearchImage.class);
                if (entity != null) {
                    array.add(entity);
                }
            } catch (JSONException e) {
                // e.printStackTrace();
                continue;
            }
        }
        return array;

    }



    public static ArrayList<ImageEntity> parseSearchImage(
            ArrayList<SearchImage> search) {
        if (search == null || search.size() == 0) {
            return null;
        }
        int length = search.size();
        ImageEntity im;
        ArrayList<ImageEntity> array = new ArrayList<ImageEntity>(length);
        for (int i = 0; i < length; i++) {
            im = parseSearchImage(search.get(i));
            array.add(im);
        }
        return array;
    }

    public static ImageEntity parseSearchImage(SearchImage search) {
        ImageEntity image = new ImageEntity();
        image.id = search.di;
//        image.desc = search.fromPageTitleEnc;
        image.image_width = search.width;
        image.image_height = search.height;
        image.thumbnail_width = search.width;
        image.thumbnail_height = search.height;
        image.download_url = search.objURL;
        image.image_url = search.objURL;
        image.thumb_large_url = search.objURL;
        image.tag = search.tag;
        image.from_url = search.fromURL;
        return image;
    }

}
