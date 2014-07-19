package org.lee.android.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.lee.android.activity.UIController;
import org.lee.android.app.bean.Channel;
import com.abooc.android.baidupicture.R;

public class ErrorFragment extends Fragment {
	public static final String ARG_URL = "url";

	private Channel mCurrChannel;

	public ErrorFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_error, container,
				false);
		Bundle bunle = getArguments();
		if (bunle != null) {
			Message msg = bunle.getParcelable(GalleryFragment.MESSAGE_KEY);
			mCurrChannel = (Channel) msg.obj;
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_main_refresh:
			if (uiController != null) {
				uiController.onGalleryPage(mCurrChannel);
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private UIController uiController;

	public void setUIController(UIController uiController) {
		this.uiController = uiController;
	}
}