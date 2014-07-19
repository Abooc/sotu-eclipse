package org.lee.android.sdk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.lee.android.app.database.ImagesDbHelper;
import org.lee.framework.print.Lg;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.util.concurrent.Executor;

public class DatabaseHelper implements Closeable {
	protected static final String DB_NAME = "images.db";
	protected static final int DB_VERSION = 4;
	protected static Context mContext;
	/** Used to perform history write operations asynchronously. */
	protected final Executor mExecutor;

	/** SQLiteOpenHelper. */
	protected final SQLiteOpenHelper mOpenHelper;

	/**
	 * 构造函数.
	 * 
	 * @param context
	 *            Context
	 * @param executor
	 *            Executor
	 * @param openHelper
	 */
	protected DatabaseHelper(Context context, Executor executor,
			SQLiteOpenHelper openHelper) {
		mContext = context;
		mExecutor = executor;
		mOpenHelper = openHelper;
	}

	/**
	 * 异步执行数据库操作。
	 * 
	 * @param transaction
	 *            Refer to {@link SQLiteTransaction}
	 */
	protected void runTransactionAsync(final SQLiteTransaction transaction) {
		mExecutor.execute(new Runnable() {
			public void run() {
				transaction.run(mOpenHelper.getWritableDatabase());
			}
		});
	}

	@Override
	public void close() {
		mOpenHelper.close();
	}

	public static byte[] toBytes(Drawable d) {
		if (d == null) {
			return null;
		}
		Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();
		return bitmapdata;
	}

	public static final class DbOpenHelper extends SQLiteOpenHelper {
		/** Database path. */
		private String mPath;
		/** 单例. */
		private static DbOpenHelper mDbOpenHelper = null;

		/**
		 * DbOpenHelper constructor.
		 * 
		 * @param context
		 *            Context
		 * @param name
		 *            Database name
		 * @param version
		 *            Database version
		 * @param executor
		 *            Executor
		 * */
		private DbOpenHelper(Context context, String name, int version) {
			super(context, name, null, version);
		}

		/**
		 * 获得单例.
		 * 
		 * @param context
		 *            Context
		 * @param name
		 *            Database name
		 * @param version
		 *            Database version
		 * @param executor
		 *            Executor
		 * @return {@link org.lee.android.sdk.database.DatabaseHelper.DbOpenHelper}
		 */
		public static DbOpenHelper getInstance(Context context, String name,
				int version) {
			if (mDbOpenHelper == null) {
				mDbOpenHelper = new DbOpenHelper(context, name, version);
			}
			return mDbOpenHelper;
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			mPath = db.getPath();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(ImagesDbHelper.SQL_CREATE_TABLE_NEWS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion <= newVersion){
				db.execSQL(ImagesDbHelper.SQL_DROP_TABLE);
				db.execSQL(ImagesDbHelper.SQL_CREATE_TABLE_NEWS);
			}
//			int version = oldVersion;
//			while (version <= newVersion) {
//				switch (version) {
//				case 3:
//					db.execSQL(ImagesDbHelper.SQL_DROP_TABLE);
//					db.execSQL(ImagesDbHelper.SQL_CREATE_TABLE_NEWS);
//					break;
//				}
//				version++;
//			}
		}

		/**
		 * Deletes the database file.
		 */
		public void deleteDatabase() {
			// close();
			if (mPath == null) {
				return;
			}
			try {
				boolean isOk = new File(mPath).delete();
				if (isOk) {
					Lg.anchor("deleted " + mPath);
				}
			} catch (Exception e) {
				Lg.e("couldn't delete " + mPath + ", Exception:" + e);
			}
		}

	}

}
