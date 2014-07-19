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

import android.os.Bundle;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

import org.lee.android.analytics.AlA;
import com.abooc.android.baidupicture.R;
import org.lee.framework.print.Lg;

public class AdvActivity extends AlA {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		installView();
		addBanner();
	}

	private void installView() {
		setContentView(R.layout.adv_activity);

		// ViewGroup fatherLayout = (ViewGroup) this.findViewById(R.id.ad);
		// ListView listView = (ListView) this.findViewById(R.id.list);
		// ExchangeViewManager exchangeViewManager = new
		// ExchangeViewManager(this,new ExchangeDataService());
		// exchangeViewManager.addView(fatherLayout, listView);
	}

	@Override
	public void finish() {
		if (interstitial.isReady()) {
			interstitial.show();
		} else {
			Lg.anchor("Interstitial ad was not ready to be shown.");
		}
		super.finish();
	}

	private InterstitialAd interstitial;

	private void addBanner() {
		interstitial = new InterstitialAd(this,
				getString(R.string.google_AD_UNIT_ID));
		interstitial.setAdListener(new BannerListener());
		AdRequest adRequest = new AdRequest();
		interstitial.loadAd(adRequest);

	}

	private class BannerListener implements AdListener {
		// 当用户关闭通过onPresentScreen展示的全屏Activity且控制权将交还给应用时调用。
		@Override
		public void onDismissScreen(Ad arg0) {
		}

		// 当loadAd失败时发送，常见失败原因：网络故障、应用配置错误，或广告资源不足。您可以记下这些事件，以便进行调试
		@Override
		public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {

		}

		// 当Ad触摸将启动新应用时调用。
		@Override
		public void onLeaveApplication(Ad arg0) {

		}

		// 当系统响应用户触摸广告的操作，在您的应用前创建了Activity并向用户展示全屏广告界面时调用。
		@Override
		public void onPresentScreen(Ad arg0) {
		}

		// 当AdView.loadAd成功时发送。
		@Override
		public void onReceiveAd(Ad arg0) {
		}
	}
}