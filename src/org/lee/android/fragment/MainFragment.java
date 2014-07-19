package org.lee.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import org.lee.a.b.F;
import org.lee.android.activity.MainActivity;
import org.lee.android.activity.UIController;
import com.abooc.android.baidupicture.R;

import java.util.ArrayList;

public class MainFragment extends F {

	private UIController mUI;

	public MainFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mUI = ((MainActivity) activity).getUIController();
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

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().setTitle(R.string.home);
	}

	private RadioGroup mRadioGroup;
	private ViewPager mViewPager;
	private WebViewFragment mWebViewFragment;

	private void installView(View view) {
		mRadioGroup = (RadioGroup) getActivity().findViewById(R.id.RadioGroup);
		mRadioGroup.setVisibility(View.GONE);

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		// {
		// Bundle bundle = new Bundle();
		// bundle.putString("name", getString(R.string.leftPageTitle));
		// bundle.putString("url", getString(R.string.leftPageUrl));
		// UserdefinedFragment fragment = new UserdefinedFragment();
		// fragment.setArguments(bundle);
		// fragment.setUIController(mUI);
		// fragments.add(fragment);
		// }
		{
			Fragment fragment = new HomeFragment();
			fragments.add(fragment);
		}
		{
			Bundle bundle = new Bundle();
			bundle.putString("name", getString(R.string.rightPageTitle));
			bundle.putString("url", getString(R.string.rightPageUrl));
			mWebViewFragment = new WebViewFragment();
			mWebViewFragment.setArguments(bundle);
			mWebViewFragment.setUIController(mUI);
			fragments.add(mWebViewFragment);
		}
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(3);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity()
				.getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.actionbar_main_switchShow).setVisible(false);
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.actionbar_main_refresh).setVisible(false);
	}

	private void addListener() {
		mViewPager.setOnPageChangeListener(on);
	}

	private OnPageChangeListener on = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int index) {
			// mRadioGroup.getChildAt(index).performClick();
			if (!mWebViewFragment.isRunning()) {
				mWebViewFragment.start();
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