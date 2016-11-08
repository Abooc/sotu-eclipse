package org.lee.android.activity;
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
//package org.lee.android.main;
//
//import java.util.LinkedList;
//
//import org.lee.android.analytics.AnalyticsActivity;
//import org.lee.android.simples.baidupicture.R;
//import org.lee.framework.print.Lg;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import cn.domob.android.ads.DomobAdEventListener;
//import cn.domob.android.ads.DomobAdManager.ErrorCode;
//import cn.domob.android.ads.DomobAdView;
//import cn.domob.android.ads.DomobFeedsAdListener;
//import cn.domob.android.ads.DomobFeedsAdView;
//
//public class AdvDomobActivity extends AnalyticsActivity {
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
//		setContentView(R.layout.adv_activity_domob);
//		mListView = (android.widget.ListView) findViewById(R.id.listView);
//
//		addAdvList();
//
//		addBanner();
//	}
//
//	private DomobFeedsAdView mFeedsAdView;
//
//	private void addAdvList() {
//		mFeedsAdView = new DomobFeedsAdView(this, PUBLISHER_ID, FeedsPPID);
//		mFeedsAdView.loadFeedsAd();
//		final LinearLayout parentLinearLayout = (LinearLayout) findViewById(R.id.adcontainer);
//		parentLinearLayout.addView(mFeedsAdView, 0);
//		mFeedsAdView.setFeedsAdListener(new DomobFeedsAdListener() {
//
//			@Override
//			public void onLandingPageOpen() {
//				Lg.anchor("onLandingPageOpen");
//			}
//
//			@Override
//			public void onLandingPageClose() {
//				Lg.anchor("onLandingPageClose");
//			}
//
//			@Override
//			public void onFeedsAdReady() {
//				Lg.anchor("onFeedsAdReady");
//			}
//
//			@Override
//			public void onFeedsAdPresent() {
//				Lg.anchor("onFeedsAdPresent");
//			}
//
//			@Override
//			public void onFeedsAdLeaveApplication() {
//				Lg.anchor("onFeedsAdLeaveApplication");
//			}
//
//			@Override
//			public void onFeedsAdFailed(ErrorCode code) {
//				Lg.anchor("onFeedsAdFailed");
//			}
//
//			@Override
//			public void onFeedsAdDismiss() {
//				Lg.anchor("onFeedsAdDismiss");
//				parentLinearLayout.invalidate();
//				mFeedsAdView.loadFeedsAd();
//			}
//
//			@Override
//			public void onFeedsAdClicked(DomobFeedsAdView feedsAdView) {
//				Lg.anchor("onFeedsAdClicked");
//			}
//		});
//		final LinkedList<String> mDataLinkedList = new LinkedList<String>();
//		for (int i = 0; i < 10; i++) {
//			mDataLinkedList.add(String.valueOf(i));
//		}
//		BaseAdapter mBaseAdapter = new BaseAdapter() {
//			public View getView(int position, View convertView, ViewGroup parent) {
//				convertView = LayoutInflater.from(getApplicationContext())
//						.inflate(R.layout.sliding_menu_list_child_item, null);
//				TextView textView = (TextView) convertView
//						.findViewById(android.R.id.text1);
//				textView.setText(mDataLinkedList.get(position));
//				return convertView;
//			}
//
//			public long getItemId(int position) {
//				return position;
//			}
//
//			public Object getItem(int position) {
//				return mDataLinkedList.get(position);
//			}
//
//			public int getCount() {
//				return mDataLinkedList.size();
//			}
//		};
//		mListView.setAdapter(mBaseAdapter);
//	}
//
//	private DomobAdView mAdviewFlexibleAdView;
//
//	public static final String PUBLISHER_ID = "56OJyM1ouMGoaSnvCK";// Test
//	// private String PUBLISHER_ID = "56OJwkDouNLBgIO7Fx";
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
//				Lg.anchor("onDomobAdReturned");
//			}
//
//			@Override
//			public void onDomobAdOverlayPresented(DomobAdView adView) {
//				Lg.anchor("overlayPresented");
//			}
//
//			@Override
//			public void onDomobAdOverlayDismissed(DomobAdView adView) {
//				Lg.anchor("Overrided be dismissed");
//			}
//
//			@Override
//			public void onDomobAdClicked(DomobAdView arg0) {
//				Lg.anchor("onDomobAdClicked");
//			}
//
//			@Override
//			public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
//				Lg.anchor("onDomobAdFailed");
//			}
//
//			@Override
//			public void onDomobLeaveApplication(DomobAdView arg0) {
//				Lg.anchor("onDomobLeaveApplication");
//			}
//
//			@Override
//			public Context onDomobAdRequiresCurrentContext() {
//				return AdvDomobActivity.this;
//			}
//		});
//		bannerLayout.addView(mAdviewFlexibleAdView);
//	}
//}