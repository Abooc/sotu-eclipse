package org.lee.android.activity;

/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.lee.android.analytics.AlA;
import org.lee.android.sdk.util.AppFunction;
import org.lee.android.simples.baidupicture.R;
import org.lee.java.util.Empty;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class UserConfigActivity extends AlA {

	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		installView();
		attachData();
	}

	private EditText nameEdit;
	private EditText urlEdit;

	private void installView() {
		setContentView(R.layout.activity_userconfig);
		webView = (WebView) findViewById(R.id.WebView);
		webView.setWebChromeClient(iWebChromeClient);
		webView.setWebViewClient(iWebViewClient);
		UIController.buildWebView(webView);
		nameEdit = (EditText) findViewById(R.id.nameEdit);
		urlEdit = (EditText) findViewById(R.id.urlEdit);
		findViewById(R.id.preview).setOnClickListener(mOnClickListener);
	}

	private void attachData() {
		String from_url = "http://gongyi.in.sohu.com/yaan/";
		webView.loadUrl(from_url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.preview:
				// String name = nameEdit.getText().toString().trim();
				String url = urlEdit.getText().toString().trim();
				if (Empty.isEmpty(url)) {
					urlEdit.setHint("网址不能为空");
					urlEdit.setHintTextColor(Color.parseColor("#80FF0000"));
					return;
				}
				AppFunction.hideInputMethod(UserConfigActivity.this, v);
				if (!url.startsWith("http://") && !url.startsWith("https://")) {
					url = "http://" + url;
				}
				webView.loadUrl(url);
				break;
			default:
				break;
			}
		}
	};

	private WebChromeClient iWebChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			super.onProgressChanged(view, newProgress);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					setProgress(newProgress * 100);
				}
			});
		}

	};

	private WebViewClient iWebViewClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Intent intent = new Intent(UserConfigActivity.this,
					BrowserActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			ComponentName componentName = new ComponentName(
					UserConfigActivity.this, BrowserActivity.class);
			intent.setComponent(componentName);
			intent.setData(Uri.parse(url));
			startActivity(intent);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// view.getSettings().setBlockNetworkImage(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// view.getSettings().setBlockNetworkImage(false);
			nameEdit.setText(view.getTitle());
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			webView.loadUrl("file:///android_asset/htmls/404.html");
		}

	};

}