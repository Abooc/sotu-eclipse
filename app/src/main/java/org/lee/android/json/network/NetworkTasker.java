package org.lee.android.json.network;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lee.java.util.Empty;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class NetworkTasker {

	private static RequestQueue mQueue;
	private static boolean isRunning;
	private static NetworkTasker mInstance;

	public static class Task {
		public Listener<JSONObject> mListener;
		public Listener<JSONArray> mArrayListener;
		public ErrorListener mErrorListener;
		public String url;

		@Override
		public boolean equals(Object obj) {
			if (obj != null && !Empty.isEmpty(url)
					&& url.equals(((Task) obj).url)) {
				return true;
			}
			return false;
		}
	}

	private NetworkTasker(Context context) {
		mQueue = Volley.newRequestQueue(context);
	}

	public static void initialize(Context context) {
		if (mInstance == null) {
			mInstance = new NetworkTasker(context);
		}
	}

	public static void start() {
		mQueue.start();
		isRunning = true;
	}

	public static void stop() {
		mQueue.stop();
		isRunning = false;
	}

	public static void add(Task task) {
		JsonObjectRequest request = convertObjectTask(task);
		request.setRetryPolicy(defaultRetryPolicy());
		mQueue.add(request);
	}

	public static JsonObjectRequest convertObjectTask(Task task) {
		return new JsonObjectRequest(Method.GET, task.url, null,
				task.mListener, task.mErrorListener);
	}

	public static void addArray(Task task) {
		JsonArrayRequest request = convertArrayTask(task);
		request.setRetryPolicy(defaultRetryPolicy());
		mQueue.add(request);
	}

	public static JsonArrayRequest convertArrayTask(Task task) {
		return new JsonArrayRequest(task.url, task.mArrayListener,
				task.mErrorListener);
	}

	public static void add(Request<?> request) {
		mQueue.add(request);
	}

	public static boolean isRunning() {
		return isRunning;
	}

	private static DefaultRetryPolicy defaultRetryPolicy() {
		return new DefaultRetryPolicy(12 * 1000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}
}
