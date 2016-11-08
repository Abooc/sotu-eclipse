package org.lee.android.preview;

import org.lee.android.activity.UIController;
import org.lee.android.app.bean.Entity.ImageEntity;
import org.lee.android.simples.baidupicture.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhotoViewHolder {
	@SuppressLint("HandlerLeak")
	private Handler UiThread = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			iProcessText.setText(msg.what + "%");
			iActivity.setProgress(msg.what * 100);
		}

	};
	private ProgressBar iProgressBar;
	private TextView iProcessText;
	private WebView iPhotoView;
	private LinearLayout iDescLayout;
	private TextView iDescText;
	private TextView iZoomText;
	private Activity iActivity;
	private Animation iEnterAnimation;
	private Animation iExitAnimation;

	public PhotoViewHolder(Activity ac) {
		iActivity = ac;
		iEnterAnimation = AnimationUtils.loadAnimation(ac,
				android.R.anim.fade_in);
		iExitAnimation = AnimationUtils.loadAnimation(ac,
				android.R.anim.fade_out);
	}

	public void attachView(View view) {
		iProgressBar = (ProgressBar) view.findViewById(R.id.loading);
		iProcessText = (TextView) view.findViewById(R.id.process);
		iPhotoView = (WebView) view.findViewById(R.id.image);
		iPhotoView.setBackgroundColor(Color.BLACK);
		iDescLayout = (LinearLayout) view.findViewById(R.id.descLayout);
		iDescLayout.setTag(1);// 递变view背景色使用
		iDescText = (TextView) view.findViewById(R.id.desc);
		iZoomText = (TextView) view.findViewById(R.id.zoom);

		UIController.buildWebView(iPhotoView);
	}

	private boolean ACTION_MOVE_CANCLE = false;
	private float x;
	private float y;
	private OnTouchListener onTouchEvent = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ACTION_MOVE_CANCLE = false;
				x = event.getX();
				y = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (Math.abs(event.getX() - x) > 10
						|| Math.abs(event.getY() - y) > 10) {
					ACTION_MOVE_CANCLE = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (!ACTION_MOVE_CANCLE) {
					if (iDescLayout.getVisibility() == View.INVISIBLE) {
						iDescLayout.setVisibility(View.VISIBLE);
						iDescLayout.startAnimation(iEnterAnimation);
						iActivity.getActionBar().show();
					} else {
						iDescLayout.startAnimation(iExitAnimation);
						iDescLayout.setVisibility(View.INVISIBLE);
						iActivity.getActionBar().hide();
					}
				}
				break;
			}
			return false;
		}
	};

	private OnClickListener iOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.descLayout:
				int i = (Integer) iDescLayout.getTag();
				String c1 = "#" + ((i = (i + 3)) > 7 ? (i = 1) : i) + "0000000";
				iDescLayout.setBackgroundColor(Color.parseColor(c1));
				iDescLayout.setTag(i);
				break;
			}
		}
	};

	private WebChromeClient iWebChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			UiThread.sendEmptyMessage(newProgress);
		}

	};

	private WebViewClient iWebViewClient = new WebViewClient() {

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			iProgressBar.setVisibility(View.GONE);
			iProcessText.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			iPhotoView.loadUrl("file:///android_asset/htmls/404.html");
		}

	};

	public void attachData(final ImageEntity imageEntity) {
		iPhotoView.setOnTouchListener(onTouchEvent);
		iZoomText.setOnClickListener(iOnClickListener);
		iDescLayout.setOnClickListener(iOnClickListener);
		iDescText.setText(imageEntity.desc);
		iPhotoView.setWebChromeClient(iWebChromeClient);
		iPhotoView.setWebViewClient(iWebViewClient);

		String html = fixScreen(imageEntity.image_url);
		iPhotoView.loadData(html, "text/html", "utf-8");
		// iPhotoView.loadUrl(imageEntity.image_url);
	}

	// Google
	// String link =
	// "<link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" />";

	@SuppressWarnings("unused")
	private static String mFixHtml = "<!DOCTYPE html>"//
			+ "<html lang=\"zh-cn\">"//
			+ "<head>"//
			+ "<title>image</title>"//
			+ "<meta charset=\"utf-8\" />"//
			+ "<style type=\"text/css\">"//
			+ "img{display:block;vertical-align:middle;max-width:100%;min-width:100%;margin:0px;padding:0px;}"//
			+ "</style>"//
			+ "</head>"//
			+ "<body style=\"padding:0;margin:0;\">"//
			// + "<td align=\"center\" valign=\"middle\">"//
			+ "<img src=\"%image-url%\" />"//
			// + "</td>" //
			+ "</body>" + "</html>";//

	public static String fixScreen(String imgUrl) {
		return newHtml.replace("%image-url%", imgUrl);
	}

	public void reload() {
		iPhotoView.reload();
	}

	private static final String script =
	//
	"<script type=\"text/javascript\">"//
			+ "window.onload = function (){"//
			// + "alert(1);"
			+ "var clientHeight=document.documentElement.clientHeight;"//
			+ "var imgHeight=document.getElementById('image_show').height;"//
			+ "if(clientHeight > imgHeight){"//
			+ "	var margin = (clientHeight - imgHeight) / 2;"//
			+ "	document.getElementById('container').style.height = clientHeight+\"px\";"//
			+ "	document.getElementById('image_show').style['margin-top'] = margin+\"px\";"//
			+ "}"//
			+ "};"//
			+ "</script>"; //

	private static final String newHtml =
	//
	"<!DOCTYPE html>"//
			+ "<html lang=\"zh-cn\">"//
			+ "<head>"//
			+ "<title>image</title>"//
			+ "<meta charset=\"utf-8\" />"//
			+ "<style type=\"text/css\">"//
			+ "body{margin:0;padding:0;}"//
			+ "#container{width:100%;height:100%;overflow:scroll-y;vertical-align:middle;margin:0px;padding:0px;}"//
			+ "#container img{display:block;max-width:100%;min-width:100%;margin:0px;padding:0px;}"//
			+ "</style>"//
			+ "</head>"//
			+ "<body>"//
			+ "<div id=\"container\">"//
			+ "<img id=\"image_show\" src=\"%image-url%\" />"//
			+ script//
			+ "</div>"//
			+ "</body>"//
			+ "</html>";
}