package org.lee.android.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lee.a.b.F;
import org.lee.android.activity.SearchActivity;
import org.lee.android.activity.StaggeredAdapter.ViewHolder;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.json.JSONParser;
import org.lee.android.json.JSONParser.SearchImage;
import org.lee.android.json.JSONParser.SearchPackage;
import org.lee.android.json.network.APIServices;
import org.lee.android.json.network.NetworkTasker;
import org.lee.android.json.network.NetworkTasker.Task;
import org.lee.android.sdk.util.AppFunction;
import org.lee.android.simples.baidupicture.R;
import org.lee.android.util.Toast;
import org.lee.android.volley.VolleyLoader;
import org.lee.java.util.Empty;
import org.lee.pulltorefresh.library.View.StaggeredGridView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

@SuppressWarnings("deprecation")
public class SearchFragment extends F {

	private EditText mEditText;

	public SearchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
		setHasOptionsMenu(true);

		String words = getArguments().getString("words");
		if (!Empty.isEmpty(words)) {
			mChannel = new Channel();
			mChannel.parent = getString(R.string.search);
			mChannel.name = words;
			requestNetwork();
			getActivity().invalidateOptionsMenu();
			mEditText.setText(words);
		} else {
			mEditText.requestFocus();
			AppFunction.showInputMethod(getActivity(), mEditText);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onStop() {
		AppFunction.hideInputMethod(getActivity(), getView());
		super.onStop();
	}

	private ListAdapter mListAdapter;
	private StaggeredGridView mListView;
	/** For temp */
	private float y;
	private PageGalleryHolder mPageIndexView;

	private void installView(final View view) {
		mListView = (StaggeredGridView) view.findViewById(android.R.id.list);
		mListView.setSelector(R.drawable.translucence_background);
		mListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					y = event.getY();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					float newY = event.getY();
					if ((newY - y) > 60) {
						getActivity().getActionBar().show();
						return false;
					} else if ((newY - y) < -60) {
						getActivity().getActionBar().hide();
						return false;
					}
				}
				return false;
			}
		});
		mListAdapter = new ListAdapter(getActivity());
		mListView.setAdapter(mListAdapter);
		mListView
				.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
					@Override
					public void onItemClick(StaggeredGridView parent,
							View view, int position, long id) {
						Intent intent = new Intent(getActivity(),
								org.lee.android.preview.PreviewActivity.class);
						intent.putExtra("data", mSearchPackage.data);
						intent.putExtra("position", position);
						intent.putExtra("startIndex", mStart);
						intent.putExtra(Channel.class.getSimpleName(), mChannel);
						startActivity(intent);
					}
				});

		mEditText = (EditText) view.findViewById(android.R.id.edit);
		view.findViewById(R.id.search).setOnClickListener(ClickListener);
		mEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					view.findViewById(R.id.search).performClick();
					return true;
				}
				return false;
			}
		});

	}

	private OnClickListener ClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String words = mEditText.getText().toString();
			if (Empty.isEmpty(words)) {
				Toast.show(R.string.input_not_null);
				return;
			}
			mChannel = new Channel();
			mChannel.parent = getString(R.string.search);
			mChannel.name = words;
			requestNetwork();
			getActivity().invalidateOptionsMenu();
			AppFunction.hideInputMethod(getActivity(), v);
		}
	};

	/** For temp */
	private ProgressDialog mPD;

	private void showWatting() {
		if (mPD == null) {
			mPD = new ProgressDialog(getActivity());
			mPD.setMessage(getString(R.string.watting));
		}
		mPD.show();
	}

	private void dissmisWatting() {
		if (mPD != null && mPD.isShowing()) {
			mPD.dismiss();
		}
	}

	private Channel mChannel;
	private int mStart = 0;

	private void requestNetwork() {
		showWatting();

		Channel channel = mChannel;
		String url;
		url = APIServices.searchUrl(channel.name, mStart,
				PresetReader.PAGE_SIZE);
		Task task = new Task();
		task.url = url;
		task.mListener = OnLoadFinished;
		task.mErrorListener = OnError;
		NetworkTasker.add(task);

		EasyTracker easyTracker = EasyTracker.getInstance(getActivity());
		easyTracker.send(MapBuilder.createEvent("8001",
				"pageNum:" + (mStart / PresetReader.PAGE_SIZE), channel.name,
				null).build());
	}

	private SearchPackage mSearchPackage;
	private Listener<JSONObject> OnLoadFinished = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			mSearchPackage = JSONParser.toSearchPackage(response);
			try {
				JSONArray jsonArray = new JSONArray(mSearchPackage.data);
				ArrayList<SearchImage> a = JSONParser.toSearchImage(jsonArray);
				mListAdapter.clear();
				mListAdapter.notifyDataSetChanged();
				((SearchActivity) getActivity()).invalidateOptionsMenu();

				int totalNum = Integer.valueOf(mSearchPackage.listNum);
				if (mPageIndexView == null) {
					View view = getActivity().findViewById(R.id.pageindexer1);
					mPageIndexView = new PageGalleryHolder();
					mPageIndexView.init(totalNum, view);
					View woolom = LayoutInflater.from(getActivity()).inflate(
							R.layout.woolom, null);
					mListView.setFooterView(woolom);
				}

				mPageIndexView.invaliadAdapter(totalNum);
				mPageIndexView.setSelection(mStart / PresetReader.PAGE_SIZE);
				mListView.setAdapter(mListAdapter);
				mListAdapter.addArray(a);
				mListAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dissmisWatting();
		}
	};

	private ErrorListener OnError = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			dissmisWatting();
			((SearchActivity) getActivity()).invalidateOptionsMenu();
			Toast.show(R.string.toast_error);
		}
	};

	private class PageGalleryHolder {
		private View iRootView;
		private Gallery iGallery;
		private TextView iTagText;

		@SuppressWarnings("unused")
		public View getView() {
			return iRootView;
		}

		public void init(int totalNum, View view) {
			if (iRootView == null) {
				iRootView = view;
				iTagText = (TextView) iRootView.findViewById(R.id.tag);
				iGallery = (Gallery) iRootView.findViewById(R.id.Gallery);
				invaliadAdapter(totalNum);
				iGallery.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						mStart = (position) * PresetReader.PAGE_SIZE;
						requestNetwork();
					}
				});
			}
		}

		private void invaliadAdapter(int totalNum) {
			setTotalNum(totalNum);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), R.layout.activity_pageindexer_item,
					new String[totalPageNum]) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					if (convertView == null) {
						convertView = LayoutInflater.from(getContext())
								.inflate(R.layout.activity_pageindexer_item,
										null);
					}
					((TextView) convertView).setText(position + 1 + "");
					return convertView;
				}

			};
			iGallery.setAdapter(adapter);
		}

		private void setSelection(int position) {
			iGallery.setSelection(position);
		}

		int totalPageNum;

		private void setTotalNum(int totalNum) {
			totalPageNum = totalNum / PresetReader.PAGE_SIZE;
			iTagText.setText("共" + (totalPageNum) + "页");
		}
	}

	class ListAdapter extends BaseAdapter {

		private ArrayList<SearchImage> array;
		private Activity mActivity;

		public ListAdapter(Activity a) {
			mActivity = a;
		}

		public void addArray(ArrayList<SearchImage> a) {
			array = a;
		}

		public void clear() {
			if (getCount() > 0)
				array.clear();
		}

		@Override
		public int getCount() {
			return array == null ? 0 : array.size();
		}

		@Override
		public SearchImage getItem(int position) {
			return array.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(mActivity);
				convertView = layoutInflator.inflate(
						R.layout.activity_gallery_item, null);
				holder = new ViewHolder(mActivity);
				holder.attachView(convertView, false);
				holder.bannerLayout.setVisibility(View.GONE);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			SearchImage image = getItem(position);
			ImageEntity item = JSONParser.parseSearchImage(image);
			holder.attachData(item);
			holder.setWH(item.thumbnail_width,
					item.thumbnail_height > 2000 ? 2000 : item.thumbnail_height);
			holder.imageView.setImageUrl(item.image_url, VolleyLoader
					.getInstance().getImageLoader());
			return convertView;
		}
	}

}