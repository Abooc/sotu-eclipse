package com.abooc.plugin.tab;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.abooc.util.Debug;

import java.util.ArrayList;
import java.util.List;

public class TabManager {

    public Fragment prev;
    public Fragment content;
    @IdRes
    public int containerViewId;

    public List<Tab> tabs = new ArrayList<>();

    public Context mContext;
    public FragmentManager mFragmentManager;

    public TabManager(Context context, FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
    }

    private OnSwitchListener iOnSwitchListener;

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.iOnSwitchListener = onSwitchListener;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public Fragment instance(Tab tab) {
        Fragment byTag = mFragmentManager.findFragmentByTag(tab.name);

        if (byTag == null) {
            Debug.anchor(tab.name);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment fragment = Fragment.instantiate(mContext, tab.fragment.getName());
            transaction.add(containerViewId, fragment, tab.name)
                    .addToBackStack(tab.name)
                    .commit();
            return fragment;
        } else {
            return byTag;
        }
    }

    public Tab build(String name, Class<? extends Fragment> fragment) {
        return new Tab(name, fragment);
    }

    public TabManager add(Tab tab) {
        tabs.add(tab);
        return this;
    }

    public void switchTo(Fragment from, Fragment to) {
        if (from != to) {
            String message = (from == null ? "NULL" : from.getClass().getSimpleName())
                    + " --> " + to.getClass().getSimpleName();
            Log.e("Debug", message);
            Debug.anchor(message);
            prev = from;
            content = to;
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (from != null) {
                transaction.hide(from);
//              transaction.hide(from).detach(from);
            }

            transaction.show(to);
//          transaction.attach(to).show(to);
            transaction.commit();

            if (iOnSwitchListener != null) {
                iOnSwitchListener.onSwitched(from, to);
            }
        }
    }

    public boolean goBack() {
        boolean backStackImmediate = mFragmentManager.popBackStackImmediate();
        if (backStackImmediate) {
            List<Fragment> fragments = mFragmentManager.getFragments();
            int size = fragments.size();
            if (fragments != null && size > 0) {
                for (int i = 1; i <= size; i++) {
                    content = fragments.get(size - i);
                    if (content == null) {
                        fragments.remove(size - i);
                        continue;
                    }
                    mFragmentManager.beginTransaction()
                            .show(content).commit();
                    Log.d("Debug", content.getClass().getSimpleName());

                    if (iOnSwitchListener != null) {
                        iOnSwitchListener.onSwitched(null, content);
                    }
                    return true;
                }
                return false;
            } else {
                Log.e("Debug", "fragments.size = 0");
            }
        } else {
            Log.e("Debug", "backStackImmediate = " + backStackImmediate);
        }
        return backStackImmediate;
    }

    public interface OnSwitchListener {
        void onSwitched(Fragment from, Fragment to);
    }
}