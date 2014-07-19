package org.lee.android.preview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abooc.sdk.update.app.AppApplication;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.lee.android.activity.StaggeredAdapter.ViewHolder;
import org.lee.android.analytics.AlA;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.app.database.ImagesDbHelper;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.json.JSONParser;
import org.lee.android.json.JSONParser.SearchImage;
import org.lee.android.json.network.NetworkTasker;
import org.lee.android.json.network.NetworkTasker.Task;
import org.lee.android.sdk.util.Downloader;
import org.lee.android.sdk.util.ShareManager;
import com.abooc.android.baidupicture.R;
import org.lee.android.util.Toast;
import org.lee.android.volley.VolleyLoader;
import org.lee.java.util.ArrayUtils;
import org.lee.java.util.Empty;

import java.util.ArrayList;

/**
 * 带侧滑菜单图片预览
 * 
 * @author ruiyuLee
 * 
 */
public class PreviewActivity extends AlA {

	private int mCurrPosition;
	private int mStartIndex;
	private ViewPager mViewPager;
	private ViewPagerAdapter mPagerAdapter;
	private Channel mCurrChannel;

	private DrawerLayout mDrawerLayout;
	private LinearLayout mMenuLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		parseIntent();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		installView();
		// installMenu();

		addViewPagerListener();
	}

	private void installView() {
		setContentView(R.layout.activity_preview_drawer);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		ArrayList<ImageEntity> array;
		if (Empty.isEmpty(data)) {
			array = ImagesDbHelper.getInstance().select(mCurrChannel.parent,
					mCurrChannel.name);
		} else {
			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ArrayList<SearchImage> a = JSONParser.toSearchImage(jsonArray);
			array = JSONParser.parseSearchImage(a);
		}
		mPagerAdapter = new ViewPagerAdapter(this);
		mPagerAdapter.addArray(array);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(mCurrPosition);
		mStartIndex = ArrayUtils.count(array);
	}

	private boolean isCancel;

	private MenuViewAdapter mMenuAdapter;

	@SuppressWarnings("unused")
	private void installMenu() {
		TextView titleText = (TextView) findViewById(R.id.Title);
		titleText.setText(mCurrChannel.parent + "/" + mCurrChannel.name);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuLayout = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.ic_drawer_shadow_right,
				GravityCompat.START);
		TextView cancleText = (TextView) findViewById(R.id.exitText);
		cancleText.setText(R.string.back);

		final ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setSelector(R.drawable.translucence_background);
		mMenuAdapter = new MenuViewAdapter(this);
		mMenuAdapter.addArray(mPagerAdapter.getArray());
		mListView.setAdapter(mMenuAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mDrawerLayout.closeDrawer(mMenuLayout);
				mCurrPosition = position;

				mPagerAdapter.notifyDataSetChanged();
				mViewPager.setCurrentItem(position, false);

				EasyTracker easyTracker = EasyTracker
						.getInstance(PreviewActivity.this);
				easyTracker.send(MapBuilder.createEvent("3001", "3010", "3011",
						null).build());
			}
		});
		ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				if (isCancel)
					finish();
			}

			public void onDrawerOpened(View drawerView) {
				mListView.setSelection(mCurrPosition > 10 ? mCurrPosition - 4
						: mCurrPosition);
				mListView.smoothScrollToPosition(mCurrPosition);
				mDrawerLayout.requestFocus();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		cancleText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isCancel = true;
				mDrawerLayout.closeDrawer(mMenuLayout);
			}
		});

	}

	private String data;

	private void parseIntent() {
		Intent intent = getIntent();
		Channel channel = intent.getParcelableExtra(Channel.class
				.getSimpleName());
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(channel.name.equals("全部") ? channel.parent
				: channel.name);
		mCurrPosition = intent.getIntExtra("position", 0);
		mStartIndex = intent.getIntExtra("startIndex", 0);
		mCurrChannel = channel;
		data = intent.getStringExtra("data");
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
			EasyTracker easyTracker = EasyTracker
					.getInstance(PreviewActivity.this);
			easyTracker.send(MapBuilder.createEvent("3001", "3012",
					mCurrChannel.parent + "-" + mCurrChannel.name, null)
					.build());

			ImageEntity entity = mPagerAdapter.getItem(mCurrPosition);
			String image_url = entity.image_url;
			Downloader d = Downloader.getInstance(this);
			long id = d.download(entity.tag + "-" + entity.desc, image_url);
			AppApplication app = (AppApplication) getApplication();
			app.setDownloadId(id);
			return true;
		case R.id.actionbar_preview_refresh:
			mViewPager.setCurrentItem(mCurrPosition);
			return true;
		case R.id.actionbar_preview_copyAddress:
			ClipboardManager m = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("image_url",
					mPagerAdapter.getItem(mCurrPosition).download_url);
			m.setPrimaryClip(clip);
			Toast.show(R.string.toast_copy_already);
			return true;
		case R.id.actionbar_preview_share_image:
			String url = mPagerAdapter.getItem(mCurrPosition).image_url;
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

	@SuppressWarnings("unused")
	private long exitTime;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// if (keyCode == KeyEvent.KEYCODE_BACK) {
		// mDrawerLayout.openDrawer(mMenuLayout);
		// // if ((System.currentTimeMillis() - exitTime) > 2000) {
		// // Toast.makeText(getApplicationContext(),
		// // getString(R.string.back), Toast.LENGTH_SHORT).show();
		// // exitTime = System.currentTimeMillis();
		// // } else {
		// // finish();
		// // }
		// return true;
		// }
		return super.onKeyUp(keyCode, event);
	}

	private boolean mCurrLoading;

	private void addViewPagerListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCurrPosition = position;
				int count = mStartIndex;
				if (!mCurrLoading && position > mPagerAdapter.getCount() - 3) {
					onLoadNext(count);
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
			mPagerAdapter.addArray(array);
			mPagerAdapter.notifyDataSetChanged();
			// mMenuAdapter.addArray(array);
			// mMenuAdapter.notifyDataSetChanged();
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
		}
	};

	private void onLoadNext(int start) {
		if (!Empty.isEmpty(data)) {
			return;
		}
		mCurrLoading = true;
		Channel channel = mCurrChannel;
		String url = PresetReader.fixUrl(channel.parent, channel.name, start,
				PresetReader.PAGE_SIZE);
		Task task = new Task();
		task.url = url;
		task.mListener = OnLoadFinished;
		task.mErrorListener = OnError;
		NetworkTasker.add(task);
		// PhotoRequest.loadFromWeb(url, OnLoadFinished, OnError);
	}

	private void onLoadPosition(int count, int position) {
		if (!mCurrLoading && position > count - 4) {
			onLoadNext(mStartIndex + 1);
		}
	}

	private class MenuViewAdapter extends BaseAdapter {
		private ArrayList<ImageEntity> iArray;
		private Activity iActivity;

		public MenuViewAdapter(Activity a) {
			iActivity = a;
		}

		public void addArray(ArrayList<ImageEntity> array) {
			if (iArray == null) {
				iArray = array;
			} else {
				iArray.addAll(array);
			}
		}

		@Override
		public int getCount() {
			return iArray == null ? 0 : iArray.size();
		}

		@Override
		public ImageEntity getItem(int position) {
			onLoadPosition(getCount(), position);
			return iArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(iActivity).inflate(
						R.layout.activity_gallery_item, null);
				holder = new ViewHolder(iActivity);
				holder.attachView(convertView, true);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.setBanner(position);
			ImageEntity item = getItem(position);
			holder.tagText.setText(item.desc());
			holder.setWH(item.thumbnail_width, item.thumbnail_height);
			holder.imageView.setImageUrl(item.thumb_large_url, VolleyLoader
					.getInstance().getImageLoader());
			return convertView;
		}

	}
}
