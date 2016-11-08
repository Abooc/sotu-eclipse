package org.lee.android.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.lee.framework.print.Lg;

import android.content.Context;

public class FileManager {

	/**
	 * 缓存文件
	 * 
	 * @param context
	 *            Context Object
	 * @param file
	 *            本地文件名
	 * @param data
	 *            要保存的数据
	 * @param mode
	 *            打开文件的方式
	 * @return 是否保存成功
	 */
	public static boolean cache(Context context, String file, byte[] data,
			int mode) {
		boolean bResult = false;
		if (null != data && data.length > 0) {
			FileOutputStream fos = null;
			try {
				fos = context.openFileOutput(file, mode);
				fos.write(data);
				fos.flush();
				bResult = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != fos) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return bResult;
	}

	public static boolean save(Context context, File file, String toPath) {
		boolean bResult = false;

		Lg.anchor("file.length():" + file.length());
		if (null != file && file.length() > 0) {
			FileOutputStream fos = null;
			FileInputStream input = null;
			try {
				input = new FileInputStream(file);
				fos = context.openFileOutput(toPath, Context.MODE_APPEND);
				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
					fos.write(buffer, 0, bytesRead);
					Lg.anchor();
				}
				fos.write(buffer);
				fos.flush();
				bResult = true;
			} catch (IOException e) {
				Lg.e(e.getMessage());
				Lg.anchor();
			} finally {
				try {
					if (null != fos) {
						fos.close();
					}
					if (null != input) {
						input.close();
					}
				} catch (IOException e) {
					Lg.e(e.getMessage());
					Lg.anchor();
				}
			}
		}

		return bResult;
	}
}
