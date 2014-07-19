package org.lee.android.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

import org.lee.a.b.F;
import org.lee.android.activity.SearchActivity;
import com.abooc.android.baidupicture.R;

public class SearchResultFragment extends F {

	public SearchResultFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_suggestion, container,
				false);
		setHasOptionsMenu(true);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
	}

	@Override
	public void onResume() {
		super.onResume();
		SearchActivity a = (SearchActivity) getActivity();
		a.invalidateOptionsMenu();
	}

	private void installView(View view) {
		getView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					SearchActivity a = (SearchActivity) getActivity();
					SearchFragment search = new SearchFragment();
					a.swichFragment(search, false);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.actionbar_search_search).setVisible(true);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_search_search:
			SearchActivity a = (SearchActivity) getActivity();
			SearchFragment search = new SearchFragment();
			a.swichFragment(search, true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}