package org.lee.android.app.bean;

public class Entity {
	/**
	 * 数据包
	 * <p>
	 * 包含类目、信息总条数、数据的json串。
	 * 
	 * @author ruiyuLee
	 * 
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
		public String data;

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
		public String tags;
		public String tag;
		public String desc;
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
		String obj_url;
		public String colum;
		String photo_id;
		String desc_info;
		String[] other_urls;
		public int download_num;
		public int collect_num;

		public String desc() {
			return "标签：" + tags + "\n" + abs + "\n" + desc;
		}
	}
}
