package com.abooc.plugin.tab;

import android.support.v4.app.Fragment;

public class Tab {
    public String name;
    public Class<? extends Fragment> fragment;

    public Tab(Class<? extends Fragment> fragment) {
        this.name = fragment.getSimpleName() + fragment.hashCode();
        this.fragment = fragment;
    }

    public Tab(String name, Class<? extends Fragment> fragment) {
        this.name = name;
        this.fragment = fragment;
    }
}
