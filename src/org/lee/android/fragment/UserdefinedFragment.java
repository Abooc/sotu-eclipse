package org.lee.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.lee.android.activity.UserConfigActivity;
import com.abooc.android.baidupicture.R;

public class UserdefinedFragment extends WebViewFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
	}

	private void installView(View view) {
		view.findViewById(R.id.WOOLOM_LAYOUT).setVisibility(View.GONE);
		view.findViewById(R.id.shadow).setBackgroundResource(
				R.drawable.ic_drawer_shadow_left);
		view.findViewById(R.id.settings).setOnClickListener(mOnClickListener);
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), UserConfigActivity.class);
			startActivity(intent);
		}
	};

}
