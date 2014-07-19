package org.lee.android.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.lee.android.activity.UI;
import org.lee.android.activity.UIController;
import com.abooc.android.baidupicture.R;

public class WebViewFragment extends Fragment {

	public WebViewFragment() {
	}

	private UI mUI;

	public void setUIController(UIController ui) {
		mUI = ui;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_no_ads, container, false);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		installView(view);
		UIController.buildWebView(webView);
	}

	@Override
	public void onResume() {
		if (mUI != null) {
			mUI.setOnKeyListener(getView(), mOnKeyListener);
		}
		super.onResume();
	}

	@Override
	public void onStop() {
		if (mUI != null) {
			mUI.setOnKeyListener(getView(), null);
		}
		super.onStop();
	}

	private OnKeyListener mOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (webView.canGoBack()) {
				webView.goBack();
				return true;
			}
			return false;
		}
	};

	private WebView webView;
	private TextView textView;

	private void installView(View view) {
		textView = (TextView) view.findViewById(R.id.WOOLOM_LAYOUT)
				.findViewById(R.id.WOOLOM_TEXT);
		webView = (WebView) view.findViewById(R.id.WebView);
	}

	private void attachData(Bundle bundle) {
		String name = bundle.getString("name", getString(R.string.app_name));
		String url = bundle.getString("url");
		textView.setText(name);
		webView.loadUrl(url);
	}

	private void addWebViewListener() {
		webView.setWebViewClient(iWebViewClient);
		webView.setWebChromeClient(iWebChromeClient);
	}

	private WebChromeClient iWebChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			super.onProgressChanged(view, newProgress);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getActivity().setProgress(newProgress * 100);
				}
			});
		}

	};

	private WebViewClient iWebViewClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("https://www.google.com")
					|| "https://www.google.com".equals(url)
					|| getString(R.string.leftPageUrl).equals(url)) {
				view.loadUrl(url);
				return false;
			}
			UIController.openBrowser(getActivity(), url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setBlockNetworkImage(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			view.getSettings().setBlockNetworkImage(false);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			webView.loadUrl("file:///android_asset/htmls/404.html");
		}

	};

	private boolean mRunning;

	public boolean isRunning() {
		return mRunning;
	}

	public void start() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			mRunning = true;
			attachData(bundle);
			addWebViewListener();
		}
	}
}
