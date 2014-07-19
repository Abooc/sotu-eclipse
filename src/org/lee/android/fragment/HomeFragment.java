package org.lee.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.volley.toolbox.NetworkImageView;

import org.lee.a.b.F;
import org.lee.android.activity.MainActivity;
import org.lee.android.activity.SearchActivity;
import org.lee.android.activity.UIController;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.sdk.util.AppFunction;
import com.abooc.android.baidupicture.R;
import org.lee.android.volley.VolleyLoader;

import java.util.ArrayList;

public class HomeFragment extends F {
	public static final String ARG_URL = "url";

	private EditText mEditText;
	private ListView mListView;
	private ListAdapter mAdapter;

	private UIController mUI;

	public HomeFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mUI = ((MainActivity) activity).getUIController();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_home, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
		addListener(view);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().setTitle(R.string.home);
	}

	private void installView(View view) {
		mEditText = (EditText) view.findViewById(android.R.id.edit);
		{
			DataPakage dp = PresetReader.loadCategory2(getActivity(),
					getString(R.string.preset_banner));
			ArrayList<Channel> array = dp.toArray();
			mAdapter = new ListAdapter();
			mAdapter.addArray(array);
			mListView = (ListView) view.findViewById(android.R.id.list);
			mListView.setSelector(R.drawable.translucence_background);
			mListView.setAdapter(mAdapter);
			View footer = LayoutInflater.from(getActivity()).inflate(
					R.layout.woolom, null);
			mListView.addFooterView(footer);
		}

		{
			DataPakage dp = PresetReader.loadCategory2(getActivity(),
					getString(R.string.preset_hot));
			ArrayList<Channel> array = dp.toArray();
			TextView hotText = (TextView) view.findViewById(R.id.hot);
			hotText.setText(dp.tag2);
			LinearLayout hotLayout = (LinearLayout) view
					.findViewById(R.id.hotLayout);
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			int length = array.size();
			for (int i = 0; i < length; i++) {
				Channel channel = array.get(i);
				TextView itemText = (TextView) inflater.inflate(
						R.layout.activity_home_hotwords_item, null);
				itemText.setOnClickListener(hotListener);
				String name = channel.name;
				itemText.setText(name.equals("全部") ? channel.parent : name);
				itemText.setTag(channel);
				hotLayout.addView(itemText);
			}
		}
	}

	private OnClickListener hotListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Activity ac = getActivity();
			if (ac == null)
				return;
			Channel channel = (Channel) v.getTag();
			mUI.onGalleryPage(channel);
		}
	};

	private OnClickListener ClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String words = mEditText.getText().toString();
			AppFunction.hideInputMethod(getActivity(), v);
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			intent.putExtra("words", words);
			startActivity(intent);
		}
	};

	private void addListener(View view) {
		final View searchButton = view.findViewById(R.id.search);
		searchButton.setOnClickListener(ClickListener);
		mEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					searchButton.performClick();
					return true;
				}
				return false;
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Channel channel = (Channel) mAdapter.getItem(position);
				mUI.onGalleryPage(channel);
			}
		});
	}

	private class ListAdapter extends BaseAdapter {

		private ArrayList<Channel> array;

		public void addArray(ArrayList<Channel> array) {
			this.array = array;
		}

		@Override
		public int getCount() {
			return array == null ? 0 : array.size();
		}

		@Override
		public Channel getItem(int position) {
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
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.activity_home_banner_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			Channel channel = getItem(position);
			holder = (ViewHolder) convertView.getTag();
			holder.attachData(channel);
			return convertView;
		}

	}

	private static final class ViewHolder {
		TextView tagText;
		NetworkImageView imageView;

		public ViewHolder(View bannerLayout) {
			tagText = (TextView) bannerLayout
					.findViewById(R.id.activity_home_banner_title);
			imageView = (NetworkImageView) bannerLayout
					.findViewById(R.id.activity_home_banner_image);
		}

		void attachData(Channel channel) {
			tagText.setText(channel.desc);
			imageView.setImageUrl(channel.imageUrl,//
					VolleyLoader.getInstance().getImageLoader());
		}
	}
}
