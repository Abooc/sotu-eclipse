package org.lee.android.preview;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.abooc.sdk.update.app.AppApplication;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lee.android.analytics.AlA;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.app.database.ImagesDbHelper;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.json.JSONParser;
import org.lee.android.json.network.NetworkTasker;
import org.lee.android.json.network.NetworkTasker.Task;
import org.lee.android.sdk.util.Downloader;
import org.lee.android.sdk.util.ShareManager;
import com.abooc.android.baidupicture.R;
import org.lee.android.util.Toast;
import org.lee.framework.print.Lg;
import org.lee.java.util.Empty;

import java.util.ArrayList;

/**
 * 图片预览
 * 
 * @author ruiyuLee
 * 
 */
public class P extends AlA {

	private ViewPager mViewPager;
	private int mCurrPosition;
	private int mStartIndex;
	private ViewPagerAdapter mPhotosAdapter;
	private Channel mCurrentChannel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_PROGRESS);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		installView();
		addViewPagerListener();

		parseIntent();
	}

	private void installView() {
		setContentView(R.layout.activity_photo_preview);
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}

	private void parseIntent() {
		Intent intent = getIntent();
		Channel channel = intent.getParcelableExtra("msg");
		if (channel == null)
			return;
		mCurrentChannel = channel;
		String title = channel.name;
		title = title == null || title.equals("全部") ? channel.parent : title;
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(title);

		ArrayList<ImageEntity> array;
		array = ImagesDbHelper.getInstance().select(channel.parent,
				title.equals(channel.parent) ? "全部" : title);
		if (Empty.isEmpty(array)) {
			Toast.show(R.string.error_message);
			return;
		}

		mCurrPosition = intent.getIntExtra("position", 0);
		mStartIndex = intent.getIntExtra("startIndex", 0);
		mStartIndex = mStartIndex == 0 ? array.size() : mStartIndex;

		mPhotosAdapter = new ViewPagerAdapter(this);
		mPhotosAdapter.addArray(array);
		mViewPager.setAdapter(mPhotosAdapter);
		mViewPager.setCurrentItem(mCurrPosition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.preview_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.actionbar_preview_save:
			ImageEntity entity = mPhotosAdapter.getItem(mCurrPosition);
			String image_url = entity.image_url;
			Downloader d = Downloader.getInstance(this);
			long id = d.download(entity.tag, image_url);
			AppApplication app = (AppApplication) getApplication();
			app.setDownloadId(id);
			return true;
		case R.id.actionbar_preview_refresh:
			mViewPager.setCurrentItem(mCurrPosition);
			return true;
		case R.id.actionbar_preview_copyAddress:
			ClipboardManager m = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("image_url",
					mPhotosAdapter.getItem(mCurrPosition).download_url);
			m.setPrimaryClip(clip);
			Toast.show(R.string.toast_copy_already);
			return true;
		case R.id.actionbar_preview_share_image:
			String url = mPhotosAdapter.getItem(mCurrPosition).image_url;
			ShareManager.getInstance().share(
					this,
					getString(R.string.menu_share_title),
					"",
					url,
					getString(R.string.menu_share_footer)
							+ ShareManager.get_google_play_app_url(this));

			return true;
		case R.id.actionbar_preview_shareApk:
			ShareManager.getInstance().share(this,
					getString(R.string.menu_share_title), "",
					ShareManager.get_google_play_app_url(this),
					getString(R.string.menu_share_footer));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean mCurrLoading;

	private void addViewPagerListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCurrPosition = position;
				int count = mStartIndex;
				if (!mCurrLoading && position > mPhotosAdapter.getCount() - 3) {
					mCurrLoading = true;
					Channel channel = mCurrentChannel;
					String url = PresetReader.fixUrl(channel.parent,
							channel.name, count, PresetReader.PAGE_SIZE);
					Task task = new Task();
					task.url = url;
					task.mListener = OnLoadFinished;
					task.mErrorListener = OnError;
					NetworkTasker.add(task);

					// PhotoRequest.loadFromWeb(url, OnLoadFinished, OnError);

					Lg.anchor(channel.parent + " - " + channel.name
							+ ", start:" + count);

					EasyTracker easyTracker = EasyTracker.getInstance(P.this);
					easyTracker.send(MapBuilder
							.createEvent(
									"预览图片",
									"onPageSelected",
									mCurrentChannel.parent + "-"
											+ mCurrentChannel.name, null)
							.build());
				}
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});
	}

	private DataPakage mDataPakage;
	private Listener<String> OnLoadFinished = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			mDataPakage = JSONParser.toParsePakage(response);
            Gson gson = new Gson();
            ArrayList<ImageEntity> array = gson.fromJson(mDataPakage.data, new TypeToken<ArrayList<ImageEntity>>() {
            }.getType());
			mPhotosAdapter.addArray(array);
			mPhotosAdapter.notifyDataSetChanged();
			mStartIndex = mStartIndex + mDataPakage.return_number;

			ImagesDbHelper db = ImagesDbHelper.getInstance();
			db.insert(array);
			mCurrLoading = false;
		}
	};

	private ErrorListener OnError = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			mCurrLoading = false;
			// Activity ac = getActivity();
			// if (ac != null) {
			// ((MainActivity) ac).errorPage(null);
			// }
		}
	};

}
