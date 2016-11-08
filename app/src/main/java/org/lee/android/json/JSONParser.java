package org.lee.android.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.framework.print.Lg;
import org.lee.java.util.Empty;

/**
 * Json Parser for JO or Bean.
 * 
 * @author ruiyuLee
 * 
 */
public class JSONParser {

	public static ArrayList<ImageEntity> toImageEntityArray(JSONArray jsonArray) {
		if (jsonArray == null || jsonArray.length() == 0) {
			return null;
		}
		int size = jsonArray.length();
		ArrayList<ImageEntity> array = new ArrayList<ImageEntity>();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject jo = jsonArray.getJSONObject(i);
				ImageEntity entity;
				entity = toImageEntity(jo);
				if (entity != null) {
					array.add(entity);
				}
			} catch (JSONException e) {
				// e.printStackTrace();
				Lg.e(e);
				continue;
			}
		}
		return array;
	}

	public static DataPakage toParsePakage(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		try {
			DataPakage dkg = new DataPakage();
			jsonObject = new JSONObject(new String(jsonObject.toString()
					.getBytes("ISO-8859-1")));

			dkg.tag1 = jsonObject.getString("tag1");
			dkg.tag2 = jsonObject.getString("tag2");
			dkg.data = jsonObject.getString("data");

			dkg.totalNum = jsonObject.getInt("totalNum");
			dkg.start_index = jsonObject.getInt("start_index");
			dkg.return_number = jsonObject.getInt("return_number");
			return dkg;
		} catch (Exception e) {
			// e.printStackTrace();
			Lg.e(e);
		}
		return null;
	}

	public static ImageEntity toImageEntity(JSONObject jsonObject) {
		if (jsonObject == null || !jsonObject.has("id")) {
			return null;
		}
		try {
			ImageEntity image = new ImageEntity();
			image.id = jsonObject.getString("id");
			image.pn = jsonObject.getInt("pn");
			image.colum = jsonObject.getString("colum");
			image.tags = jsonObject.getString("tags");
			image.tag = jsonObject.getString("tag");
			image.desc = jsonObject.getString("desc");
			image.abs = jsonObject.getString("abs");
			image.date = jsonObject.getString("date");
			image.image_url = jsonObject.getString("image_url");
			image.from_url = jsonObject.getString("from_url");
			image.thumbnail_height = jsonObject.getInt("thumbnail_height");
			image.thumbnail_width = jsonObject.getInt("thumbnail_width");
			image.thumbnail_url = jsonObject.getString("thumbnail_url");
			image.thumb_large_url = jsonObject.getString("thumb_large_url");
			image.download_url = jsonObject.getString("download_url");
			image.download_num = jsonObject.getInt("download_num");
			image.collect_num = jsonObject.getInt("collect_num");
			return image;
		} catch (JSONException e) {
			// e.printStackTrace();
			Lg.e(e);
		}
		return null;
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

	public static SearchPackage toSearchPackage(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		try {
			SearchPackage dkg = new SearchPackage();
			// jsonObject = new JSONObject(new String(jsonObject.toString()
			// .getBytes("ISO-8859-1")));
			// jsonObject = new JSONObject(jsonObject.toString());
			dkg.queryEnc = jsonObject.getString("queryEnc");
			dkg.queryExt = jsonObject.getString("queryExt");
			dkg.listNum = jsonObject.getString("listNum");
			dkg.displayNum = jsonObject.getString("displayNum");
			dkg.bdFmtDispNum = jsonObject.getString("bdFmtDispNum");
			dkg.bdSearchTime = jsonObject.getString("bdSearchTime");
			dkg.bdIsClustered = jsonObject.getString("bdIsClustered");
			dkg.data = jsonObject.getString("data");
			return dkg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
				entity = toSearchImage(jo);
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

	public static SearchImage toSearchImage(JSONObject jsonObject) {
		try {
			SearchImage dkg = new SearchImage();
			// jsonObject = new JSONObject(new String(jsonObject.toString()
			// .getBytes("ISO-8859-1")));
			dkg.objURL = jsonObject.getString("objURL");
			dkg.width = jsonObject.getInt("width");
			dkg.height = jsonObject.getInt("height");
			dkg.fromPageTitleEnc = jsonObject.getString("fromPageTitleEnc");
			dkg.di = jsonObject.getString("di");
			dkg.fromURL = jsonObject.getString("fromURL");
			return dkg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ImageEntity parseSearchImage(SearchImage search) {
		ImageEntity image = new ImageEntity();
		image.id = search.di;
		image.desc = search.fromPageTitleEnc;
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

	public static ArrayList<ImageEntity> parseSearchImage(
			ArrayList<SearchImage> search) {
		if (search == null || search.size() == 0) {
			return null;
		}
		int length = search.size();
		ImageEntity im;
		ArrayList<ImageEntity> array = new ArrayList<Entity.ImageEntity>(length);
		for (int i = 0; i < length; i++) {
			im = parseSearchImage(search.get(i));
			array.add(im);
		}
		return array;
	}

	public static Channel toChannel(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		try {
			Channel channel = new Channel();
			// jsonObject = new JSONObject(new String(jsonObject.toString()
			// .getBytes("ISO-8859-1")));

			channel.id = jsonObject.getString("id");
			channel.position = jsonObject.getString("position");
			channel.parent = jsonObject.getString("parent");
			channel.name = jsonObject.getString("name");
			channel.desc = jsonObject.getString("desc");
			channel.imageUrl = jsonObject.getString("imageUrl");
			return channel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Channel> toChannel(String json) {
		if (Empty.isEmpty(json)) {
			return null;
		}
		try {
			JSONArray ja = new JSONArray(json);
			return toChannel(ja);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Channel> toChannel(JSONArray ja) {
		if (ja == null || ja.length() == 0) {
			return null;
		}
		int length = ja.length();
		Channel im;
		ArrayList<Channel> array = new ArrayList<Channel>(length);
		try {
			for (int i = 0; i < length; i++) {
				im = toChannel(ja.getJSONObject(i));
				array.add(im);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}
}
