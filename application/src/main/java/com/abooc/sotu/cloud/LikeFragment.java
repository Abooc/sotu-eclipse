package com.abooc.sotu.cloud;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abooc.sotu.R;
import com.abooc.util.Debug;


public class LikeFragment extends Fragment {


    public LikeFragment() {
        // Required empty public constructor
    }

    public static LikeFragment newInstance(String param1, String param2) {
        LikeFragment fragment = new LikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Debug.anchor();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Debug.anchor();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        Debug.anchor();
        super.onResume();
    }

    @Override
    public void onPause() {
        Debug.anchor();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Debug.anchor();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Debug.anchor();
        super.onDestroy();
    }
}
