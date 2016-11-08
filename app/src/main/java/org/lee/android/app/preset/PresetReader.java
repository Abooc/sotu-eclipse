package org.lee.android.app.preset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.lee.android.app.bean.Category;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.simples.baidupicture.R;
import org.lee.java.sdk.baidu.BaiduImagesService;
import org.lee.java.util.Empty;

import android.content.Context;

/**
 * 临时预置数据
 * 
 * @author ruiyuLee
 * 
 */
public class PresetReader {

	public static String[] channels = { "父类别" };
	public static String[] children1 = { "美女" };
	public static String[] children2 = { "搞笑" };
	public static String[] children3 = { "明星" };
	public static String[] children4 = { "壁纸" };
	public static String[] children5 = { "动漫" };
	public static String[] children6 = { "汽车" };
	public static String[] children7 = { "旅游" };
	public static String[] children8 = { "服饰" };
	public static String[] children9 = { "摄影" };

	public static void initialize(Context context) {
		channels = context.getResources().getStringArray(
				R.array.preset_array_all);

		children1 = context.getResources().getStringArray(
				R.array.preset_array_meinv);
		children2 = context.getResources().getStringArray(
				R.array.preset_array_gaoxiao);
		children3 = context.getResources().getStringArray(
				R.array.preset_array_mingxing);
		children4 = context.getResources().getStringArray(
				R.array.preset_array_bizhi);
		children5 = context.getResources().getStringArray(
				R.array.preset_array_dongman);
		children6 = context.getResources().getStringArray(
				R.array.preset_array_qiche);
		children7 = context.getResources().getStringArray(
				R.array.preset_array_lvyou);
		children8 = context.getResources().getStringArray(
				R.array.preset_array_fushi);
		children9 = context.getResources().getStringArray(
				R.array.preset_array_sheying);

	}

	/**
	 * 每一页加载数
	 */
	public static int PAGE_SIZE = 40;

	/**
	 * 组装url请求参数
	 * 
	 * @param tag1
	 *            父类目
	 * @param tag2
	 *            子类目
	 * @param pn
	 *            起始索引
	 * @param rn
	 *            一页加载数
	 * @return 返回组装好的url地址，可能返回NULL。
	 */
	public static String fixUrl(String tag1, String tag2, int pn, int rn) {
		return BaiduImagesService.fixUrl(tag1, tag2, pn, rn);
	}

	public static Channel defaultChannel() {
		Channel c = new Channel();
		c.parent = "美女";
		c.name = "全部";
		return c;
	}

	private final static int BUFFER_SIZE = 2048;

	public static String InputStreamTOString(InputStream in, String encoding)
			throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), encoding);// "ISO-8859-1"
	}

	/**
	 * 加载百度图片类目数据
	 * 
	 * @return 返回类目集合
	 */
	public static ArrayList<Category> loadCategory() {
		int length = channels.length;
		ArrayList<String[]> chr = new ArrayList<String[]>(length);
		chr.add(children1);
		chr.add(children2);
		chr.add(children3);
		chr.add(children4);
		chr.add(children5);
		chr.add(children6);
		chr.add(children7);
		chr.add(children8);
		chr.add(children9);
		ArrayList<Category> array = new ArrayList<Category>(length);
		for (int i = 0; i < length; i++) {
			Category c = new Category();
			c.name = channels[i];
			c.channels = new ArrayList<Channel>();
			int childCount = chr.get(i).length;
			for (int j = 0; j < childCount; j++) {
				Channel cl = new Channel();
				cl.parent = c.name;
				cl.name = chr.get(i)[j];
				c.channels.add(cl);
			}
			array.add(c);
		}
		return array;
	}

	/**
	 * 加载图片类目数据
	 * 
	 * @return 返回类目集合
	 */
	public static DataPakage loadCategory2(Context context, String filename) {
		String json = "";
		try {
			InputStream in = context.getAssets().open(filename);
			json = InputStreamTOString(in, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Empty.isEmpty(json)) {
				return null;
			}
		}
		DataPakage dp = new DataPakage();
		try {
			JSONObject jo = new JSONObject(json);
			dp.tag1 = jo.getString("tag1");
			dp.tag2 = jo.getString("tag2");
			dp.start_index = jo.getInt("start_index");
			dp.return_number = jo.getInt("return_number");
			dp.totalNum = jo.getInt("totalNum");
			dp.data = jo.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Gson gson = new Gson();
		// DataPakage dp = gson.fromJson(dp.data, new TypeToken<DataPakage>() {
		// }.getType());
		// ArrayList<Channel> array = gson.fromJson(json,
		// new TypeToken<ArrayList<Channel>>() {
		// }.getType());
		return dp;
	}
}
