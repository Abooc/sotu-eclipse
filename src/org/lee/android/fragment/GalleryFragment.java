package org.lee.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.lee.a.b.F;
import org.lee.android.activity.MainActivity;
import org.lee.android.activity.StaggeredAdapter;
import org.lee.android.activity.StaggeredAdapter.OnLoadPositionListener;
import org.lee.android.activity.UIController;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.app.database.ImagesDbHelper;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.json.JSONParser;
import org.lee.android.json.network.APIServices;
import org.lee.android.json.network.NetworkTasker;
import org.lee.android.json.network.NetworkTasker.Task;
import com.abooc.android.baidupicture.R;
import org.lee.android.util.Log;
import org.lee.framework.print.Lg;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class GalleryFragment extends F {
	public static final String MESSAGE_KEY = "msg";
	private TextView mTotalText;

	private int mStartIndex = 0;
	private Channel mCurrChannel;

	private ListView mGridView;
	private StaggeredAdapter mGridViewAdapter;
	private int mTotalNum = 0;
	private boolean mRefresh = false;

	private UIController mUI;

	public GalleryFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setHasOptionsMenu(true);
		mUI = ((MainActivity) activity).getUIController();
		mSharedPreferences = getActivity().getSharedPreferences(
				GalleryFragment.this.getClass().getSimpleName(),
				Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_gallery, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTotalText = (TextView) view.findViewById(R.id.total);

		installView(view);
		addListener();

		Channel channel = getArguments().getParcelable(MESSAGE_KEY);
		if (mCurrChannel != null && !mCurrChannel.equals(channel)) {
			Lg.anchor(channel.name + ", mCurrChannel:" + mCurrChannel.name);
			mGridViewAdapter.clear();
		}
		mStartIndex = readPrefrences(channel.parent + channel.name + "start");
		mCurrChannel = channel;
		ImagesDbHelper db = ImagesDbHelper.getInstance();
		int count = db.getCount(channel.parent, channel.name);
		if (count > 0) {
			mTotalNum = readPrefrences(channel.parent + channel.name);
			ArrayList<ImageEntity> array = db.select(channel.parent,
					channel.name);
			mGridViewAdapter.addArray(array);
			mGridViewAdapter.notifyDataSetChanged();
		} else {
			requestNetwork();
			mUI.invalidateOptionsMenu();
		}

	}

	private void installView(View view) {
		// mGridView = (StaggeredGridView) view
		// .findViewById(R.id.staggeredGridView1);

		mGridView = (ListView) view.findViewById(R.id.staggeredGridView1);
		// mGridView.setItemMargin(4);
		mGridView.setPadding(4, 0, 4, 0);
		mGridView.setSelector(R.drawable.translucence_background);
		mGridViewAdapter = new StaggeredAdapter(getActivity());
		mGridView.setAdapter(mGridViewAdapter);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.actionbar_main_switchShow).setVisible(true);
		MenuItem refreshItem = menu.findItem(R.id.actionbar_main_refresh);
		if (mUI.isDrawerOpen(null) || mUI.isHomePage()) {
			refreshItem.setVisible(false);
		} else if (NetworkTasker.isRunning()) {
			refreshItem.setVisible(false);
			refreshItem.setActionView(R.layout.loading);
		} else {
			refreshItem.setVisible(true);
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onResume() {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().setTitle(mCurrChannel.name);
		super.onResume();
	}

	private void addListener() {
		OnItemClickListener l = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						org.lee.android.preview.PreviewActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("startIndex", mStartIndex);
				intent.putExtra(Channel.class.getSimpleName(), mCurrChannel);
				startActivity(intent);
			}
		};
		mGridView.setOnItemClickListener(l);

		OnLoadPositionListener onLoadPositionListener = new OnLoadPositionListener() {

			@Override
			public void onLoadPosition(int count, int position) {
				mTotalText.setText("" + (mTotalNum - position - mStartIndex));
				if (!isLoading && position == count - 6 && count > 20) {
					mRefresh = false;
					requestNetwork();
				}
			}
		};
		mGridViewAdapter.setOnLoadPositionListener(onLoadPositionListener);
	}

	public int currentVisiblePosition() {
		int first = mGridView.getFirstVisiblePosition();
		int last = mGridView.getLastVisiblePosition();
		return (first + last) / 2;
	}

	public String currentVisibleTag() {
		return mGridViewAdapter.getItem(currentVisiblePosition()).tags.toString();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_main_refresh:
			mRefresh = true;
			requestNetwork();
			item.setActionView(R.layout.loading);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected boolean isLoading = false;

	protected void requestNetwork() {
		isLoading = true;
		Channel channel = mCurrChannel;
		String url;
		url = APIServices.fixUrl(channel.parent, channel.name, mStartIndex,
				PresetReader.PAGE_SIZE);
		Task task = new Task();
		task.url = url;
		task.mListener = OnLoadFinished;
		task.mErrorListener = OnError;
		NetworkTasker.add(task);
	}

	private Listener<String> OnLoadFinished = new Listener<String>() {

		@Override
		public void onResponse(String response) {
            Log.anchor(response);

            try {
                String s = new String(response.getBytes(), "UTF-8");
                Log.anchor(s);
                response = URLDecoder.decode(response, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.anchor(e);
            }
            Log.anchor(response);
            DataPakage dataPakage = JSONParser.toParsePakage(response);
			mTotalNum = dataPakage.totalNum;
			savePrefrences(mCurrChannel.parent + mCurrChannel.name, mTotalNum);

            Gson gson = new Gson();
            ArrayList<ImageEntity> array = gson.fromJson(dataPakage.data, new TypeToken<ArrayList<ImageEntity>>() {
            }.getType());
			ImagesDbHelper db = ImagesDbHelper.getInstance();
            if (mRefresh) {
                db.clear(mCurrChannel.parent, mCurrChannel.name);
                mGridViewAdapter.clear();
            }
            db.insert(array);
            mGridViewAdapter.addArray(array);
            mGridViewAdapter.notifyDataSetChanged();
            mStartIndex += dataPakage.return_number;
            savePrefrences(mCurrChannel.parent + mCurrChannel.name + "start",
                    mStartIndex);

            if (mRefresh) {
                // mGridView.setSelectionToTop();
                mGridView.setSelection(0);
            }

            isLoading = false;
            mUI.invalidateOptionsMenu();
        }
	};

	private ErrorListener OnError = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			mUI.errorPage(mCurrChannel);
			isLoading = false;
			mUI.invalidateOptionsMenu();
		}
	};

	private SharedPreferences mSharedPreferences;

	private void savePrefrences(String key, int totalNum) {
		mSharedPreferences.edit().putInt(key, totalNum).commit();
	}

	private int readPrefrences(String key) {
		if (mTotalNum == 0) {
			return mSharedPreferences.getInt(key, 0);
		}
		return mTotalNum;
	}
}