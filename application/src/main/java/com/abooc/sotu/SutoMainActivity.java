package com.abooc.sotu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.abooc.plugin.tab.TabManager;
import com.abooc.util.Debug;

import butterknife.ButterKnife;

import static com.abooc.sotu.R.string.app_name;
import static com.abooc.sotu.SutoMainActivity.NAMES.HOME;

/**
 * Created by dayu on 2016/11/8.
 */

public class SutoMainActivity extends AppCompatActivity {

    TabManager iTabManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.on();
        Debug.anchor("sdfadafsd");

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        iTabManager = new TabManager(this, getSupportFragmentManager(), R.id.TabContent);
        iTabManager.setOnSwitchListener(onSwitchListener);
        iTabManager.add(iTabManager.build(HOME.name(), HomeFragment.class))
                .add(iTabManager.build(NAMES.SEARCH.name(), SearchFragment.class))
                .add(iTabManager.build(NAMES.CLOUD.name(), CloudFragment.class))
                .add(iTabManager.build(NAMES.ACCOUNT.name(), AccountFragment.class));


        Fragment fragment = iTabManager.instance(iTabManager.getTabs().get(0));
        iTabManager.switchTo(null, fragment);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    private TabManager.OnSwitchListener onSwitchListener = new TabManager.OnSwitchListener() {
        @Override
        public void onSwitched(Fragment from, Fragment to) {
            NAMES name = NAMES.valueOf(to.getTag());
            switch (name) {
                case HOME:
                    setTitle(getString(app_name));
                    break;
                case SEARCH:
                    setTitle("搜索");
                    break;
                case CLOUD:
                    setTitle("CLOUD");
                    break;
                case ACCOUNT:
                    setTitle("我");
                    break;
            }
            Log.d("Debug", to.getTag());
        }
    };

    enum NAMES {
        HOME,
        SEARCH,
        CLOUD,
        ACCOUNT
    }

    public void onClickTab(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.menu_home:
                fragment = iTabManager.instance(iTabManager.getTabs().get(0));
                iTabManager.switchTo(iTabManager.content, fragment);
                break;
            case R.id.menu_search:
                fragment = iTabManager.instance(iTabManager.getTabs().get(1));
                iTabManager.switchTo(iTabManager.content, fragment);
                break;
            case R.id.menu_cloud:
                fragment = iTabManager.instance(iTabManager.getTabs().get(2));
                iTabManager.switchTo(iTabManager.content, fragment);
                break;
            case R.id.menu_account:
                fragment = iTabManager.instance(iTabManager.getTabs().get(3));
                iTabManager.switchTo(iTabManager.content, fragment);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!iTabManager.goBack()) {
            super.onBackPressed();
        }
    }
}
