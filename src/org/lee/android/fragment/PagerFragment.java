package org.lee.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

import org.lee.android.activity.UI;
import org.lee.android.activity.UIController;
import com.abooc.android.baidupicture.R;

import java.util.ArrayList;

public class PagerFragment extends Fragment {

	public PagerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
		addListener();
	}

	private UI mUI;

	public void setUIController(UIController ui) {
		mUI = ui;
	}

	private ViewPager mViewPager;
	private GalleryFragment mGalleryFragment;
	private SuggestionFragment mSuggestionFragment;

	private void installView(View view) {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>(2);
		mGalleryFragment = new GalleryFragment();
		mGalleryFragment.setArguments(getArguments());
		fragments.add(mGalleryFragment);
		mSuggestionFragment = new SuggestionFragment();
		mSuggestionFragment.setArguments(getArguments());
		fragments.add(mSuggestionFragment);
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(fragments.size());
		ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity()
				.getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onResume() {
		if (mUI != null) {
			mUI.setOnKeyListener(getView(), mOnKeyListener);
		}
		super.onResume();
	}

	private OnKeyListener mOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (mViewPager.getCurrentItem() != 0) {
				mViewPager.setCurrentItem(0);
				return true;
			}
			return false;
		}
	};

	@Override
	public void onStop() {
		if (mUI != null) {
			mUI.setOnKeyListener(getView(), null);
		}
		super.onStop();
	}

	private void addListener() {
		mViewPager.setOnPageChangeListener(on);
	}

	private OnPageChangeListener on = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			if (position == 1) {
				String tag = mGalleryFragment.currentVisibleTag();
				mSuggestionFragment.show(tag);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		private ArrayList<Fragment> mFragments;

		public ViewPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			mFragments = fragments;
		}

		@Override
		public Fragment getItem(int index) {
			return mFragments.get(index);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

	}

}