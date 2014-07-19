package org.lee.android.json.network;

import android.content.Context;

import com.abooc.android.baidupicture.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class APIServices {

    private static String api_get_latest_noti;
    private static String api_add_noti;

    public static void init(Context context) {
        api_get_latest_noti = context.getString(R.string.get_noti_url).replace(
                "*", "&");
    }

    public static String api_get_latest_noti(String packageName,
                                             int currVersionCode) {
        return api_get_latest_noti.replace("{package}", packageName);
    }

    public static String api_add_noti(String packageName, int currVersionCode) {
        return api_add_noti.replace("{package}", packageName).replace(
                "{versioncode}", "" + currVersionCode);
    }

    /**
     * 组装url请求参数
     *
     * @param tag1 父类目
     * @param tag2 子类目
     * @param pn   页码
     * @param rn   一页加载数
     * @return 返回组装好的url地址，可能返回NULL。
     */
    public static String fixUrl(String tag1, String tag2, int pn, int rn) {
        try {
            tag1 = URLEncoder.encode(tag1, "UTF-8");
            tag2 = URLEncoder.encode(tag2, "UTF-8");
            String url = "http://images.baidu.com/channel/listjson?fr=channel"
                    + "&tag1=" + tag1
                    + "&tag2=" + tag2
                    + "&sorttype=1"
                    + "&pn=" + pn
                    + "&rn=" + rn
                    + "&qq-pf-to=pcqq.c2c&ie=utf-8&oe=utf-8";
            return url;
        } catch (UnsupportedEncodingException e1) {
        }
        return null;
    }

    public static String searchUrl(String tag1, int pn, int rn) {
        try {
            tag1 = URLEncoder.encode(tag1, "UTF-8");
            String url = "http://image.baidu.com/i?tn=resultjson&ie=utf-8&word={tag}&pn={start}&rn={count}&width={w}&height={h}";
            url = url.replace("{tag}", tag1).replace("{start}", "" + pn)
                    .replace("{count}", "" + rn);
            return url;
        } catch (UnsupportedEncodingException e1) {
        }
        return null;
    }

    public static String searchImageWAPUrl(String tag) {
        try {
            tag = URLEncoder.encode(tag, "UTF-8");
            String url = "http://m.baidu.com/img?tn=bdidxiphone#!/search/{tag}";
            // String url = "http://m.image.so.com/i?src=m_www&q={tag}";
            return url.replace("{tag}", tag);
        } catch (UnsupportedEncodingException e1) {
        }
        return null;
    }

    public static String suggestionUrl(String tag) {
        try {
            tag = URLEncoder.encode(tag, "UTF-8");
            String url = "http://suggestion.baidu.com/su?wd={tag}&zxmode=2&json=1&p=2";
            return url.replace("{tag}", tag);
        } catch (UnsupportedEncodingException e1) {
        }
        return null;
    }
}
