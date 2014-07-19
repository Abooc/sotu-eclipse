package org.lee.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lee.android.activity.UIController;
import org.lee.android.app.bean.Channel;
import org.lee.android.json.network.APIServices;
import org.lee.android.json.network.NetworkTasker;
import com.abooc.android.baidupicture.R;
import org.lee.framework.print.Lg;
import org.lee.java.util.Empty;

public class SuggestionFragment extends Fragment {

	public SuggestionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_suggestion, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
	}

	public void show(String tag) {
		mBanner.setVisibility(View.VISIBLE);
		if (Empty.isEmpty(tag)) {
			return;
		}
		tag = tag.replace("\"", "");
		if (tag != null && !tag.equals(mCurrTag)) {
			mCurrTag = tag;
			requestNetwork(tag);
		}
	}

	private String mCurrTag;
	private TextView mMessage;
	private TextView mAdsTag;
	private GridView mGridView;
	private LinearLayout mBanner;
	private ArrayAdapter<String> mAdapter;

	private void installView(View view) {
		Channel channel = getArguments().getParcelable(
				GalleryFragment.MESSAGE_KEY);
		if (channel != null) {
			Lg.anchor(channel.name + ", mCurrChannel:" + channel.name);
		}

		mMessage = (TextView) view.findViewById(R.id.message);
		mAdsTag = (TextView) view.findViewById(R.id.adsTag);
		mGridView = (GridView) view.findViewById(R.id.GridView);
		mAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.activity_home_hotwords_item, new String[] {});
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(mOnItemClickListener);
		mBanner = (LinearLayout) view.findViewById(R.id.banner);
		view.findViewById(R.id.close).setOnClickListener(mOnClickListener);

	}

	private void requestNetwork(String tag) {
		String url;
		url = APIServices.suggestionUrl(tag);
		Request<String> re = new StringRequest(url, OnLoadFinished,
				mErrorListener);
		NetworkTasker.add(re);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_search_search:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String url = APIServices.searchImageWAPUrl(((TextView) view)
					.getText().toString());
			UIController.openBrowser(getActivity(), url);
		}
	};

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.adsTag:
				mBanner.setVisibility(View.VISIBLE);
				break;
			case R.id.close:
				mBanner.setVisibility(View.GONE);
				mAdsTag.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	private Listener<String> OnLoadFinished = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			Lg.anchor(response);
			response = (String) response.subSequence(response.indexOf("{"),
					response.indexOf(")"));
			try {
				JSONObject jo = new JSONObject(response);
				JSONArray ja = jo.getJSONArray("s");
				int length = ja.length();
				if (length == 0) {
					mMessage.setText("唉，其他人都在看隐私...");
					return;
				}
				mMessage.setVisibility(View.GONE);

				String[] array = new String[length];
				for (int i = 0; i < length; i++) {
					array[i] = (String) ja.get(i);
				}
				mAdapter = new ArrayAdapter<String>(getActivity(),
						R.layout.activity_home_hotwords_item, array);
				mGridView.setAdapter(mAdapter);
			} catch (JSONException e) {
				if (mAdapter.getCount() == 0) {
					mMessage.setText("唉，其他人都在看隐私...");
				}
				Lg.e(e.getMessage());
			}
		}
	};

	private ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			Lg.anchor(error.toString());
			mCurrTag = "";
			if (mAdapter.getCount() == 0) {
				mMessage.setText("网络出错了！");
			}
		}
	};

}