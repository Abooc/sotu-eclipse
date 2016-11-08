package org.lee.android.app.database;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.sdk.database.DatabaseHelper;
import org.lee.framework.print.Lg;
import org.lee.java.util.Empty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class ImagesDbHelper extends DatabaseHelper {

	/** images表结构 */
	private static class Column {
		static final String TB_NAME = "images";
		static final String index = "_index";
		static final String ID = "_id";
		static final String colum = "colum";
		static final String tag = "tag";
		static final String tags = "tags";
		static final String abs = "abs";
		static final String desc = "desc";
		static final String date = "date";
		static final String thumbnail_width = "thumbnail_width";
		static final String thumbnail_height = "thumbnail_height";
		static final String DRAWABLE = "drawable";
		static final String thumbnail_url = "thumbnail_url";
		static final String thumb_large_url = "thumb_large_url";
		static final String image_url = "image_url";
		static final String from_url = "from_url";
		static final String download_url = "download_url";
		static final String download_num = "download_num";
		static final String collect_num = "collect_num";

		static final String[] columns = { ID, index, colum, tag, tags, abs,
				desc, date, thumbnail_width, thumbnail_height, DRAWABLE,
				thumbnail_url, thumb_large_url, image_url, from_url,
				download_url, download_num, collect_num

		};
	}

	/** 表创建语句 */
	public static final String SQL_CREATE_TABLE_NEWS = "CREATE TABLE  IF NOT EXISTS "
			+ Column.TB_NAME
			+ "("
			+ Column.index
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " // INTEGER PRIMARY KEY
														// AUTOINCREMENT
			+ Column.ID
			+ " INTEGER UNIQUE, "
			+ Column.colum
			+ " TEXT, "
			+ Column.tag
			+ " TEXT, "
			+ Column.tags
			+ " TEXT, "
			+ Column.abs
			+ " TEXT, "
			+ Column.desc
			+ " TEXT, "
			+ Column.date
			+ " TEXT, "
			+ Column.thumbnail_width
			+ " TEXT, "
			+ Column.thumbnail_height
			+ " TEXT, "
			+ Column.DRAWABLE
			+ " BLOB, "
			+ Column.thumbnail_url
			+ " TEXT,"
			+ Column.thumb_large_url
			+ " TEXT, "
			+ Column.image_url
			+ " TEXT, "
			+ Column.download_num
			+ " TEXT, "
			+ Column.collect_num
			+ " TEXT, "
			+ Column.from_url
			+ " TEXT, "
			+ Column.download_url
			+ " TEXT) ";

	public static final String SQL_DROP_TABLE = "DROP TABLE " + Column.TB_NAME
			+ ";";

	private static ImagesDbHelper mInstance;

	/**
	 * 构造器
	 * 
	 * @param context
	 *            上下文
	 * @param executor
	 *            executor
	 * @param openHelper
	 *            openHelper
	 */
	private ImagesDbHelper(Context context, Executor executor,
			SQLiteOpenHelper openHelper) {
		super(context, executor, openHelper);
	}

	public static void initialize(Context context) {
		if (mInstance == null) {
			context = context.getApplicationContext();
			ThreadFactory logThreadFactory = Executors.defaultThreadFactory();
			Executor logExecutor = Executors
					.newSingleThreadExecutor(logThreadFactory);
			SQLiteOpenHelper openHelper = DbOpenHelper.getInstance(context,
					DatabaseHelper.DB_NAME, DatabaseHelper.DB_VERSION);
			mInstance = new ImagesDbHelper(context, logExecutor, openHelper);
			openHelper.getReadableDatabase();
		}
	}

	public static ImagesDbHelper getInstance() {
		return mInstance;
	}

	public int insert(ArrayList<ImageEntity> array) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int i = 0;
		String imageId = "";
		try {
			for (ImageEntity item : array) {
				imageId = item.id;
				if (exist(db, imageId)) {
					Lg.e("Already exist :" + imageId);
					continue;
				}
				db.insert(Column.TB_NAME, null, parser(item));
				i++;
			}
		} catch (Exception e) {
			// Lg.e(e);
			Lg.e("Exception :" + e);
		}
		return i;
	}

	public int clear(String parent, String title) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.delete(Column.TB_NAME, Column.colum + "=? AND " + Column.tag
				+ "=?", new String[] { parent, title });
	}

	// public News select(String channel, News news, int limitCount) {
	// SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	// String selection = null;
	// String[] selectionArgs = null;
	// Cursor cursor = null;
	// if (!TextUtils.isEmpty(channel)) {
	// selection = NewsColumn.COLUMN_image_url + "=?";
	// selectionArgs = new String[] { channel };
	// if (news != null) {
	// selection += " AND " + NewsColumn.COLUMN_date + "<? ";
	// selectionArgs = new String[] { channel, news.datetime };
	// }
	// }
	// cursor = db.query(NewsColumn.TB_NAME, NewsColumn.columns, selection,
	// selectionArgs, null, null, "datetime DESC", limitCount + ", 1");
	// if (cursor == null || cursor.getCount() == 0)
	// return null;
	// cursor.moveToFirst();
	// News breakPrev = parserCursor(cursor);
	// return breakPrev;
	// }

	public boolean exist(SQLiteDatabase db, String id) {
		if (Empty.isEmpty(id)) {
			return false;
		}
		String selection = null;
		String[] selectionArgs = null;
		selection = Column.ID + "=?";
		selectionArgs = new String[] { id };
		Cursor cursor = db.query(Column.TB_NAME, Column.columns, selection,
				selectionArgs, null, null, null);
		if (cursor == null)
			return false;
		return cursor.getCount() > 0;
	}

	// public int update(News news) {
	// SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	// return db.update(NewsColumn.TB_NAME, parser(news), NewsColumn.COLUMN_ID
	// + "=?", new String[] { news.id + "" });
	// }
	//
	// public int update(News targetNews, News news) {
	// SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	// return db.update(NewsColumn.TB_NAME, parser(news), NewsColumn.COLUMN_ID
	// + "=? AND " + NewsColumn.COLUMN_image_url, new String[] {
	// targetNews.id + "", targetNews.channel });
	// }

	// public int deleteUser(String user) {
	// SQLiteDatabase db = mHelper.getWritableDatabase();
	// return db.delete(T_User.TB_NAME, T_User.COLUMN_USER + "=? ",
	// new String[] { user });
	// }

	public int delete(String tableName) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.delete(tableName, null, null);
	}

	public int delete(int newsId, String channel) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		return db.delete(Column.TB_NAME, Column.ID + "=? AND "
				+ Column.image_url + "=?",
				new String[] { newsId + "", channel });
	}

	public int getCount(String parent, String channel) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select " + Column.ID + " from "
					+ Column.TB_NAME + " WHERE " + Column.colum + "=? AND "
					+ Column.tag + "=?", new String[] { parent, channel });
			if (cursor == null)
				return 0;
			return cursor.getCount();
		} catch (Exception e) {
			return -1;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	public ArrayList<ImageEntity> select(String parent, String channel) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		String selection = null;
		String[] selectionArgs = null;
		Cursor cursor = null;
		if (!TextUtils.isEmpty(channel)) {
			selection = Column.colum + "=? AND " + Column.tag + "=?";
			selectionArgs = new String[] { parent, channel };
		}
		try {
			cursor = db.query(Column.TB_NAME, Column.columns, selection,
					selectionArgs, null, null, Column.index + " asc");
			if (cursor == null)
				return null;
			ArrayList<ImageEntity> newsArray = new ArrayList<ImageEntity>();
			while (cursor.moveToNext()) {
				ImageEntity item = parserCursor(cursor);
				newsArray.add(item);
			}
			return newsArray;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	private ContentValues parser(ImageEntity item) {
		ContentValues values = new ContentValues();
		values.put(Column.ID, item.id);
		values.put(Column.colum, item.colum);
		values.put(Column.tag, item.tag);
		values.put(Column.tags, item.tags);
		values.put(Column.abs, item.abs);
		values.put(Column.desc, item.desc);
		values.put(Column.date, item.date);
		values.put(Column.thumbnail_width, item.thumbnail_width);
		values.put(Column.thumbnail_height, item.thumbnail_height);
		values.put(Column.thumbnail_url, item.thumbnail_url);
		values.put(Column.thumb_large_url, item.thumb_large_url);
		values.put(Column.image_url, item.image_url);
		values.put(Column.from_url, item.from_url);
		values.put(Column.download_num, item.download_num);
		values.put(Column.collect_num, item.collect_num);
		values.put(Column.download_url, item.download_url);
		// values.put(Column.COLUMN_DRAWABLE, item.imageData);
		return values;
	}

	private ImageEntity parserCursor(Cursor cursor) {
		ImageEntity item = new ImageEntity();
		item.id = cursor.getString(cursor.getColumnIndex(Column.ID));
		item.colum = cursor.getString(cursor.getColumnIndex(Column.colum));
		item.tag = cursor.getString(cursor.getColumnIndex(Column.tag));
		item.tags = cursor.getString(cursor.getColumnIndex(Column.tags));
		item.abs = cursor.getString(cursor.getColumnIndex(Column.abs));
		item.desc = cursor.getString(cursor.getColumnIndex(Column.desc));
		item.date = cursor.getString(cursor.getColumnIndex(Column.date));
		item.thumbnail_width = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(Column.thumbnail_width)));
		item.thumbnail_height = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(Column.thumbnail_height)));
		item.thumbnail_url = cursor.getString(cursor
				.getColumnIndex(Column.thumbnail_url));
		item.thumb_large_url = cursor.getString(cursor
				.getColumnIndex(Column.thumb_large_url));
		item.image_url = cursor.getString(cursor
				.getColumnIndex(Column.image_url));
		item.from_url = cursor
				.getString(cursor.getColumnIndex(Column.from_url));
		item.download_num = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(Column.download_num)));
		item.collect_num = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(Column.collect_num)));
		item.download_url = cursor.getString(cursor
				.getColumnIndex(Column.download_url));
		// item.imageData = cursor.getBlob(cursor
		// .getColumnIndex(Column.COLUMN_DRAWABLE));
		return item;
	}

}
