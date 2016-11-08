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
//import org.lee.android.analytics.AnalyticsActivity;
//import org.lee.android.simples.baidupicture.R;
//
//import android.content.Intent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//
//public class AdvActionBarActivity extends AnalyticsActivity {
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.update_menu, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;
//		switch (item.getItemId()) {
//		case R.id.actionbar_main_adv:
//			intent = new Intent(AdvActionBarActivity.this,
//					AdvUmengActivity.class);
//			break;
//		default:
//			intent = new Intent(AdvActionBarActivity.this,
//					AdvDomobActivity.class);
//			break;
//		}
//		startActivity(intent);
//		return super.onOptionsItemSelected(item);
//	}
//
//}