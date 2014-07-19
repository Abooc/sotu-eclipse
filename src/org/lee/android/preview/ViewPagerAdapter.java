package org.lee.android.preview;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.app.bean.Entity.ImageEntity;
import com.abooc.android.baidupicture.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;
	private Activity mActivity;
	private ArrayList<ImageEntity> mArray;

	public ViewPagerAdapter(Activity activity) {
		inflater = LayoutInflater.from(activity);
		mActivity = activity;
	}

	public ArrayList<ImageEntity> getArray() {
		return mArray;
	}

	public void addArray(ArrayList<ImageEntity> array) {
		if (mArray == null) {
			mArray = array;
		} else {
			mArray.addAll(array);
		}
	}

	@Override
	public int getCount() {
		return mArray == null ? 0 : mArray.size();
	}

	public ImageEntity getItem(int position) {
		return mArray == null || mArray.size() <= position ? null : mArray
				.get(position);
	};

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View rootView;
		PhotoViewHolder viewHolder;
		rootView = (ViewGroup) inflater.inflate(
				R.layout.activity_photo_preview_item, container, false);
		((ViewPager) container).addView(rootView);
		viewHolder = new PhotoViewHolder(mActivity);
		viewHolder.attachView(rootView);
		ImageEntity imageEntity = mArray.get(position);
		viewHolder.attachData(imageEntity);
		return rootView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void startUpdate(ViewGroup container) {
	}

}