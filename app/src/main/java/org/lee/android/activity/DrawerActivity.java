package org.lee.android.activity;

import org.lee.android.analytics.AlA;
import org.lee.android.app.AppApplication;
import org.lee.android.app.bean.Category;
import org.lee.android.app.bean.Channel;
import org.lee.android.app.preset.PresetReader;
import org.lee.android.sdk.properties.AboutActivity;
import org.lee.android.sdk.util.ShareManager;
import org.lee.android.simples.baidupicture.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public abstract class DrawerActivity extends AlA {
	private DrawerLayout mDrawerLayout;
	private LinearLayout mMenuLayout;
	private ListView mParentListView;
	private ListView mChildListView;
	private ParentAdapter mParentAdapter;
	private ChildAdapter mChildAdapter;

	private ActionBarDrawerToggle mDrawerToggle;
	protected UIController mUIController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUIController = new UIController(this);
		installMenu();
		installActionBar();
		addListener();

		mParentListView.performItemClick(null, 0, 0);
	}

	private void installMenu() {
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuLayout = (LinearLayout) findViewById(R.id.left_drawer);

		mParentListView = (ListView) findViewById(android.R.id.list);
		mParentAdapter = new ParentAdapter(this);
		mParentAdapter.setArray(PresetReader.loadCategory());
		mParentListView.setAdapter(mParentAdapter);

		mChildListView = (ListView) findViewById(R.id.list2);
		mChildAdapter = new ChildAdapter(this, null);
		mChildListView.setAdapter(mChildAdapter);

		boolean mode = getResources().getBoolean(R.bool.menu_mode);
		mParentListView.setVisibility(mode ? View.VISIBLE : View.GONE);
	}

	private void installActionBar() {
		final CharSequence mTitle = getTitle();
		mDrawerLayout.setDrawerShadow(R.drawable.ic_drawer_shadow_right,
				GravityCompat.START);

		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);//TODO

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				mUIController.onDrawerClosed();
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				mUIController.onDrawerOpened();
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
				mDrawerLayout.requestFocus();
			}
		};
		mUIController.setDrawerObserver(new UIController.DrawerObserver() {

			@Override
			public boolean isDrawerOpen(View drawer) {
				return mDrawerLayout.isDrawerOpen(mMenuLayout);
			}

		});
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	protected boolean update = false;

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.actionbar_main_update).setVisible(update);
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mMenuLayout);
		menu.findItem(R.id.actionbar_main_refresh).setVisible(
				!drawerOpen && !mUIController.isHomePage());
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home
				&& !mUIController.isHomePage()) {
			mUIController.onHomePage();
			return true;
		}
		// if (mDrawerToggle.onOptionsItemSelected(item)) {
		// return true;
		// }
		switch (item.getItemId()) {
		// Google search
		// Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		// intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
		// if (intent.resolveActivity(getPackageManager()) != null) {
		// startActivity(intent);
		// } else {
		// Toast.makeText(this, R.string.app_not_available,
		// Toast.LENGTH_LONG).show();
		// }
		// return true;
		case R.id.actionbar_main_update:
			AppApplication.openGooglePlay(this);
			return super.onOptionsItemSelected(item);
		case R.id.actionbar_main_about_us:
			Intent intentAbout = new Intent(this, AboutActivity.class);
			if (intentAbout.resolveActivity(getPackageManager()) != null) {
				startActivity(intentAbout);
			}
			return super.onOptionsItemSelected(item);
		case R.id.actionbar_main_shareApk:
			ShareManager.getInstance().share(this,
					getString(R.string.menu_share_title), "",
					ShareManager.get_google_play_app_url(this),
					getString(R.string.menu_share_footer));
			return super.onOptionsItemSelected(item);
		case R.id.actionbar_main_pinglun:
			AppApplication.openGooglePlay(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mUIController.onKeyUp(keyCode, event)) {
				return true;
			}
			if (mUIController.isHomePage()) {
				mDrawerLayout.requestFocus();
				mDrawerLayout.openDrawer(mMenuLayout);
				return true;
			} else if (mDrawerLayout.isDrawerOpen(mMenuLayout)) {
				return super.onKeyUp(keyCode, event);
			} else {
				mUIController.onHomePage();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private void addListener() {
		findViewById(R.id.Title).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mUIController.isHomePage()) {
					mUIController.onHomePage();
					setTitle(R.string.home);
					mDrawerLayout.closeDrawer(mMenuLayout);
				}
			}
		});
		findViewById(R.id.exit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mParentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mParentListView.setItemChecked(position, true);
				mChildListView.scrollTo(0, 0);
				Category rss = mParentAdapter.getItem(position);
				mChildAdapter.setArray(rss.channels);
				mChildAdapter.notifyDataSetChanged();

				EasyTracker easyTracker = EasyTracker
						.getInstance(DrawerActivity.this);
				easyTracker.send(MapBuilder.createEvent("6601", "6611",
						rss.name, null).build());
			}
		});
		mChildListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Channel channel = mChildAdapter.getItem(position);
				setTitle(channel.name);
				mDrawerLayout.closeDrawer(mMenuLayout);

				mUIController.onGalleryPage(channel);

				EasyTracker easyTracker = EasyTracker
						.getInstance(DrawerActivity.this);
				easyTracker.send(MapBuilder.createEvent("6601", "6612",
						channel.name, null).build());
			}
		});
	}

}
