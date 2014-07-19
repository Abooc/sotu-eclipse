package org.lee.android.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lee.a.b.S_;
import org.lee.android.activity.BackgroundServices.Notification.Type;
import org.lee.android.json.network.APIServices;
import org.lee.android.json.network.NetworkTasker;
import org.lee.android.json.network.NetworkTasker.Task;
import com.abooc.android.baidupicture.R;
import org.lee.framework.print.Lg;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundServices extends S_ {

	public class LocalBinder extends Binder {
		public BackgroundServices getService() {
			return BackgroundServices.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();

	private final Handler UIThread = new Handler(Looper.myLooper()) {
		@Override
		public void handleMessage(Message msg) {
			mUIController.notify((Notification) msg.obj);
		}
	};

	/** 设置程序主Activity暂停/运行切换是否后台通知任务响应 */
	private boolean mPauseable = false;
	/** 设置后台通知任务是否循环 */
	private boolean mNotifyTaskLoop = false;
	private Timer mTimer;
	private long mTimerDelay = 1 * 1000;
	private long mTimerPeriod = 10 * 60 * 1000;
	// private long mTimerPeriod = 10 * 1000;

	private TimerTask mTimerTask;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("unused")
	private int versionCode;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// String packageName = getPackageName();
		versionCode = getResources().getInteger(R.integer.versionCode);
		String packageName = getString(R.string.notify_package);
		int versionCode = getResources().getInteger(R.integer.versionCode);
		createCheckUpdateTask(packageName, versionCode);
		runTimer();
		return super.onStartCommand(intent, flags, startId);
	}

	private UIController mUIController;

	public void setController(UIController controller) {
		mUIController = controller;
		mUIController.setPauseable(mPauseable);
	}

	@Override
	public ComponentName startService(Intent service) {
		return super.startService(service);
	}

	@Override
	public boolean stopService(Intent name) {
		Lg.anchor();
		return super.stopService(name);
	}

	@Override
	public void onDestroy() {
		Lg.anchor();
		super.onDestroy();
	}

	private Task mCheckUpdateTask;

	private void createCheckUpdateTask(String packageName, int currVersionCode) {
		mCheckUpdateTask = new Task();
		mCheckUpdateTask.url = APIServices.api_get_latest_noti(packageName,
				currVersionCode);
		mCheckUpdateTask.mErrorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Lg.e("VolleyError:" + error.getMessage());
			}
		};
		mCheckUpdateTask.mArrayListener = new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				try {
					int length = response.length();
					if (length == 0) {
						Lg.e("response:" + response);
						return;
					}
					int bigIndex = 0;
					int tempid = 0;
					for (int i = 0; i < length; i++) {
						int id = response.getJSONObject(i).getInt("id");
						if (tempid < id) {
							tempid = id;
							bigIndex = i;
						}
					}
					Notification not = parse(response.getJSONObject(bigIndex));
					if (mUIController != null) {
						mUIController.notify(not);
					}
				} catch (JSONException e) {
					Lg.e(e);
				}
			}
		};
	}

	public static Notification parse(JSONObject response) throws JSONException {
		Notification noti = new Notification();
		noti.enable = Boolean.valueOf(getJSONObjectValue("enable", response));
		noti.id = Integer.valueOf(getJSONObjectValue("id", response));
		noti.messageType = toType(getJSONObjectValue("messageType", response));
		noti.versionCode = Integer.valueOf(getJSONObjectValue("versionCode",
				response));
		noti.name = getJSONObjectValue("name", response);
		noti.content = getJSONObjectValue("content", response);
		return noti;
	}

	public static int toType(String typeValue) {
		if ("update".equals(typeValue)) {
			return Type.NOTIFY_TYPE_UPDATE;
		} else {
			return Type.NOTIFY_TYPE_MESSAGE;
		}
	}

	public static String getJSONObjectValue(String key, JSONObject response)
			throws JSONException {
		if (response.has(key)) {
			Object o = response.get(key);
			return String.valueOf(o);
		}
		return "0";
	}

	static class Notification implements android.os.Parcelable {



		static class Type {
			/** 更新 */
			static int NOTIFY_TYPE_UPDATE = 0;
			/** 消息 */
			static int NOTIFY_TYPE_MESSAGE = 1;
		}

		boolean enable;
		int id;
		int messageType;
		int versionCode;
		String name;
		String content;

		public static final Creator<Notification> CREATOR = new Creator<Notification>() {

			@Override
			public Notification[] newArray(int size) {
				return new Notification[size];
			}

			@Override
			public Notification createFromParcel(Parcel source) {
				return new Notification(source);
			}
		};

		public Notification() {
		}

		public Notification(Parcel source) {
			id = source.readInt();
			messageType = source.readInt();
			versionCode = source.readInt();
			name = source.readString();
			content = source.readString();
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(id);
			dest.writeInt(messageType);
			dest.writeInt(versionCode);
			dest.writeString(name);
			dest.writeString(content);

		}

		public String toString() {
			// Gson gson = new Gson();
			// return gson.toJson(this);
			return null;
		}

	}

	public void runTimer() {
		if (mTimer == null) {
			mTimer = new Timer(this.getClass().getSimpleName(), true);
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					NetworkTasker.addArray(mCheckUpdateTask);
				}
			};
			if (mNotifyTaskLoop) {
				mTimer.schedule(mTimerTask, mTimerDelay, mTimerPeriod);
			} else {
				mTimer.schedule(mTimerTask, mTimerDelay);
			}
		}
	}

	public void cancelTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}

	/**
	 * For test
	 * 
	 * @return
	 */
	public Notification create() {
		// final String content = getString(R.string.notify_content);
		// final String versionCode = getString(R.integer.versionCode);
		Notification noti = new Notification();
		noti.enable = true;
		noti.messageType = Type.NOTIFY_TYPE_MESSAGE;
		String ver = String.valueOf(System.currentTimeMillis());
		ver = ver.substring(ver.length() - 6);
		noti.versionCode = Integer.valueOf(ver);
		noti.name = "测试通知";
		noti.content = "本地测试通知内容，这只是个测试通知！";
		Message msg = new Message();
		msg.obj = noti;
		UIThread.sendMessage(msg);
		return noti;
	}
}
