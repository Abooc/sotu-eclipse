package org.lee.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.abooc.android.baidupicture.R;

public class UpdateActivity extends AdvActionBarActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		installView();

	}

	private void installView() {
		setContentView(R.layout.activity_update);
		findViewById(R.id.update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(""));
				startActivity(intent);
			}
		});
	}


}