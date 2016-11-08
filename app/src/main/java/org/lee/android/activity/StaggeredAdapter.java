package org.lee.android.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.lee.android.app.AppApplication;
import org.lee.android.app.bean.Entity.DataPakage;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.json.JSONParser;
import org.lee.android.sdk.util.Downloader;
import org.lee.android.simples.baidupicture.R;
import org.lee.android.volley.INetworkImageView;
import org.lee.android.volley.VolleyLoader;
import org.lee.framework.print.Lg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class StaggeredAdapter extends BaseAdapter {

	public static interface OnLoadPositionListener {
		public void onLoadPosition(int count, int position);
	}

	private Activity mActivity;
	private ArrayList<ImageEntity> mArray = new ArrayList<ImageEntity>();
	private static DataPakage mChannelEntity;
	private OnLoadPositionListener mOnLoadPositionListener;

	public StaggeredAdapter(Activity context) {
		this.mActivity = context;
	}

	public ArrayList<ImageEntity> getArray() {
		return mArray;
	}

	public DataPakage getChannelEntity() {
		return mChannelEntity;
	}

	public static ArrayList<ImageEntity> toArray(DataPakage arrayEntity) {
		if (arrayEntity == null) {
			return null;
		}
		mChannelEntity = arrayEntity;
		try {
			JSONArray jsonArray = new JSONArray(arrayEntity.data);
			ArrayList<ImageEntity> array = JSONParser
					.toImageEntityArray(jsonArray);
			return array;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addArray(ArrayList<ImageEntity> arrayEntity) {
		mArray.addAll(arrayEntity);
		// mArray = arrayEntity;
	}

	public void clear() {
		if (mArray == null)
			return;
		mArray.clear();
		notifyDataSetChanged();
	}

	public void setOnLoadPositionListener(
			OnLoadPositionListener onLoadPositionListener) {
		mOnLoadPositionListener = onLoadPositionListener;
	}

	@Override
	public int getCount() {
		return mArray == null ? 0 : mArray.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public ImageEntity getItem(int position) {
		if (mOnLoadPositionListener != null) {
			mOnLoadPositionListener.onLoadPosition(getCount(), position);
		}
		return mArray.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(mActivity);
			convertView = layoutInflator.inflate(
					R.layout.activity_gallery_item, null);
			holder = new ViewHolder(mActivity);
			holder.attachView(convertView, true);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		holder.setBanner(position, convertView);
		ImageEntity item = getItem(position);
		holder.attachData(item);
		holder.setWH(item.thumbnail_width, item.thumbnail_height > 2000 ? 2000
				: item.thumbnail_height);
		holder.imageView.setImageUrl(item.image_url, VolleyLoader.getInstance()
				.getImageLoader());
		return convertView;
	}

	public static class ViewHolder {
		public TextView tagText;
		public TextView from_urlText;
		public TextView downloadText;
		public TextView shoucangText;
		public AdView adView;
		public INetworkImageView imageView;
		public LinearLayout bannerLayout;
		private static String AD_UNIT_ID;
		private static Activity iActivity;
		private ImageEntity item;

		private static AdListener iAdListener;

		public ViewHolder(Activity a) {
			iActivity = a;
			AD_UNIT_ID = a.getString(R.string.google_AD_UNIT_ID);
			iAdListener = new AdListener() {

				@Override
				public void onReceiveAd(Ad arg0) {

				}

				@Override
				public void onPresentScreen(Ad arg0) {

				}

				@Override
				public void onLeaveApplication(Ad arg0) {

				}

				@Override
				public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
					bannerLayout.setVisibility(View.GONE);
				}

				@Override
				public void onDismissScreen(Ad arg0) {

				}
			};
		}

		public void attachView(View view, boolean download) {
			tagText = (TextView) view.findViewById(R.id.tag);
			from_urlText = (TextView) view.findViewById(R.id.from_url);
			shoucangText = (TextView) view.findViewById(R.id.shoucang);
			downloadText = (TextView) view.findViewById(R.id.download);
			downloadText.setVisibility(download ? View.VISIBLE : View.GONE);
			imageView = (INetworkImageView) view.findViewById(R.id.imageView1);
			bannerLayout = (LinearLayout) view.findViewById(R.id.banner);
		}

		public void setWH(int w, int h) {
			imageView.setImageWidth(w);
			imageView.setImageHeight(h);
		}

		public void attachData(ImageEntity item) {
			this.item = item;
			from_urlText.setOnClickListener(l);
			downloadText.setOnClickListener(l);
			tagText.setText(item.desc);
			shoucangText.setText("收藏(" + item.collect_num + ")");
			downloadText.setText("下载(" + item.download_num + ")");
		}

		public void loadBanner(int position, AdSize adSize) {
			adView = new AdView(iActivity, AdSize.IAB_MRECT, AD_UNIT_ID);
			adView.setAdListener(iAdListener);
			bannerLayout.addView(adView);
			AdRequest adRequest = new AdRequest();
			adRequest.setGender(AdRequest.Gender.MALE);
			adView.loadAd(adRequest);
		}

		public void setBanner(int position, View view) {
			if (position % 5 == 4) {
				bannerLayout.setVisibility(View.VISIBLE);
				if (adView == null) {
					AdSize customAdSize = new AdSize(300, 250);
					loadBanner(position, customAdSize);
					Lg.anchor(position);
				}
			} else {
				bannerLayout.setVisibility(View.GONE);
			}
		}

		private View.OnClickListener l = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.download:
					Downloader d = Downloader.getInstance(iActivity);
					long id = d.download(item.tag + "-" + item.desc,
							item.download_url);
					AppApplication app = (AppApplication) iActivity
							.getApplication();
					app.setDownloadId(id);
					break;
				case R.id.from_url:
					Intent intent = new Intent(iActivity, BrowserActivity.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(item.from_url));
					iActivity.startActivity(intent);
					break;
				}
			}
		};
	}

}
