//package org.lee.android.activity;
//
///*
// * Copyright 2013 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import org.lee.android.analytics.AlA;
//import org.lee.android.simples.baidupicture.R;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.umeng.newxp.common.ExchangeConstants;
//import com.umeng.newxp.controller.ExchangeDataService;
//import com.umeng.newxp.controller.XpListenersCenter.NTipsChangedListener;
//
//public class AdvUmengActivity extends AlA {
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		installView();
//
//	}
//
//	private ListView mListView;
//
//	private void installView() {
//		setContentView(R.layout.adv_activity_umeng);
//		mListView = (android.widget.ListView) findViewById(R.id.listView);
//
//		addAdvList();
//
//		addBanner();
//	}
//
//	private ExchangeDataService iExchangeDataService;
//
//	private void addAdvList() {
//		iExchangeDataService = new ExchangeDataService("40251");
//		iExchangeDataService.preloadData(this, new NTipsChangedListener() {
//			@Override
//			public void onChanged(int flag) {
//				TextView view = (TextView) findViewById(R.id.tips);
//				view.setText("" + flag);
//			};
//		}, ExchangeConstants.type_container);
//	}
//
//	private DomobAdView mAdviewFlexibleAdView;
//
//	private String PUBLISHER_ID = "56OJyM1ouMGoaSnvCK";
//	public static final String FeedsPPID = "16TLwebvAchksNUGSZjJXz_k";
//	public static final String InlinePPID = "16TLwebvAchksY6iO_8oSb-i";
//	public static final String FlexibleInlinePPID = "16TLwebvAchksNUH_fumgl0k";
//	public static final String InterstitialPPID = "16TLwebvAchksY6iOa7F4DXs";
//	public static final String SplashPPID = "16TLwebvAchksY6iOGe3xcik";
//
//	private void addBanner() {
//		LinearLayout bannerLayout = (LinearLayout) findViewById(R.id.banner);
//
//		mAdviewFlexibleAdView = new DomobAdView(this, PUBLISHER_ID,
//				FlexibleInlinePPID, DomobAdView.INLINE_SIZE_FLEXIBLE);
//		mAdviewFlexibleAdView.setKeyword("game");
//		mAdviewFlexibleAdView.setUserGender("male");
//		mAdviewFlexibleAdView.setUserBirthdayStr("2000-08-08");
//		mAdviewFlexibleAdView.setUserPostcode("123456");
//
//		mAdviewFlexibleAdView.setAdEventListener(new DomobAdEventListener() {
//
//			@Override
//			public void onDomobAdReturned(DomobAdView adView) {
//				Log.i("DomobSDKDemo", "onDomobAdReturned");
//			}
//
//			@Override
//			public void onDomobAdOverlayPresented(DomobAdView adView) {
//				Log.i("DomobSDKDemo", "overlayPresented");
//			}
//
//			@Override
//			public void onDomobAdOverlayDismissed(DomobAdView adView) {
//				Log.i("DomobSDKDemo", "Overrided be dismissed");
//			}
//
//			@Override
//			public void onDomobAdClicked(DomobAdView arg0) {
//				Log.i("DomobSDKDemo", "onDomobAdClicked");
//			}
//
//			@Override
//			public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
//				Log.i("DomobSDKDemo", "onDomobAdFailed");
//			}
//
//			@Override
//			public void onDomobLeaveApplication(DomobAdView arg0) {
//				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
//			}
//
//			@Override
//			public Context onDomobAdRequiresCurrentContext() {
//				return AdvUmengActivity.this;
//			}
//		});
//		bannerLayout.addView(mAdviewFlexibleAdView);
//	}
//}