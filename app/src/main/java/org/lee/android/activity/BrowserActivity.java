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
import org.lee.android.app.AppApplication;
import org.lee.android.sdk.util.Downloader;
import org.lee.android.sdk.util.ShareManager;
import org.lee.android.simples.baidupicture.R;
import org.lee.android.util.Toast;
import org.lee.java.util.Empty;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BrowserActivity extends AlA {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		setContentView(R.layout.ads_load_layout);
		installView();
		addBootomListener();

		Intent intent = getIntent();
		Uri uri = intent.getData();
		String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action) && uri != null) {
			String url = uri.toString().trim();
			if (Empty.isEmpty(url)) {
				Toast.show("链接失效，已被自动转向默认页！");
				url = getString(R.string.rightPageUrl);
			}
			webView.loadUrl(url);
			addWebViewListener();
		}
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		HitTestResult result = webView.getHitTestResult();
		if (result.getType() == HitTestResult.IMAGE_TYPE
				|| result.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.webview_menu, menu);
			menu.setHeaderTitle(result.getExtra());
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final HitTestResult result = webView.getHitTestResult();
		switch (item.getItemId()) {
		case R.id.actionbar_preview_save:
			Downloader d = Downloader.getInstance(getApplicationContext());
			long id = d.download(webView.getTitle(), result.getExtra());
			AppApplication app = (AppApplication) getApplication();
			app.setDownloadId(id);
			return true;
		case R.id.actionbar_preview_share_image:
			String footer = getString(R.string.menu_share_footer)
					+ ShareManager
							.get_google_play_app_url(getApplicationContext());
			ShareManager.getInstance().share(BrowserActivity.this,
					getString(R.string.menu_share_title), "",
					result.getExtra(), footer);
			return true;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private WebView webView;
	private ProgressBar iProgressBar;
	private TextView iProcessText;

	private void installView() {
		iProgressBar = (ProgressBar) findViewById(R.id.loading);
		iProcessText = (TextView) findViewById(R.id.process);
		webView = (WebView) findViewById(R.id.image);
		registerForContextMenu(webView);
		UIController.buildWebView(webView);

	}

	private void addWebViewListener() {
		webView.setWebViewClient(iWebViewClient);
		webView.setWebChromeClient(iWebChromeClient);
	}

	private void addBootomListener() {
		findViewById(R.id.back).setOnClickListener(mOnClickListener);
//		findViewById(R.id.back).setOnLongClickListener(mOnLongClickListener);
		findViewById(R.id.forward).setOnClickListener(mOnClickListener);
		findViewById(R.id.close).setOnClickListener(mOnClickListener);
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}
				break;
			case R.id.forward:
				if (webView.canGoForward()) {
					webView.goForward();
				}
				break;
			case R.id.close:
				finish();
				break;
			default:
				finish();
				break;
			}
		}
	};

	private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			WebBackForwardList wbfl = webView.copyBackForwardList();
			
			return false;
		}
	};
	

	private WebChromeClient iWebChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			super.onProgressChanged(view, newProgress);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					iProcessText.setText(newProgress + "%");
					setProgress(newProgress * 100);
				}
			});
		}

	};

	private WebViewClient iWebViewClient = new WebViewClient() {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			iProgressBar.setVisibility(View.VISIBLE);
			iProcessText.setVisibility(View.VISIBLE);
			view.getSettings().setBlockNetworkImage(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			iProgressBar.setVisibility(View.GONE);
			iProcessText.setVisibility(View.GONE);
			view.getSettings().setBlockNetworkImage(false);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			webView.loadUrl("file:///android_asset/htmls/404.html");
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}