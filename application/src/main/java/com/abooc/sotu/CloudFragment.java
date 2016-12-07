package com.abooc.sotu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abooc.joker.tab.TabManager;
import com.abooc.sotu.cloud.DownloadFragment;
import com.abooc.sotu.cloud.LikeFragment;
import com.abooc.util.Debug;

import static com.abooc.sotu.CloudFragment.NAMES.DOWNLOAD;
import static com.abooc.sotu.CloudFragment.NAMES.LIKE;


public class CloudFragment extends Fragment implements View.OnClickListener {


    public CloudFragment() {
        // Required empty public constructor
    }

    public static CloudFragment newInstance(String param1, String param2) {
        CloudFragment fragment = new CloudFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    TabManager iTabManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Debug.anchor();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


        iTabManager = new TabManager(getContext(), getChildFragmentManager(), R.id.Cloud_TabContent);
        iTabManager.setOnSwitchListener(onSwitchListener);
        iTabManager.add(iTabManager.build(LIKE.name(), LikeFragment.class))
                .add(iTabManager.build(DOWNLOAD.name(), DownloadFragment.class));

    }


    private TabManager.OnSwitchListener onSwitchListener = new TabManager.OnSwitchListener() {
        @Override
        public void onSwitched(Fragment from, Fragment to) {
            NAMES name = NAMES.valueOf(to.getTag());
            switch (name) {
                case LIKE:
                    break;
                case DOWNLOAD:
                    break;
            }
            Log.d("Debug", to.getTag());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_like:
                Fragment like = iTabManager.instance(iTabManager.getTabs().get(0));
                iTabManager.switchTo(null, like);
                break;
            case R.id.tab_download:
                Fragment download = iTabManager.instance(iTabManager.getTabs().get(1));
                iTabManager.switchTo(iTabManager.content, download);
                break;
        }
    }

    enum NAMES {
        LIKE,
        DOWNLOAD
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cloud, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Debug.anchor();
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tab_like).setOnClickListener(this);
        view.findViewById(R.id.tab_download).setOnClickListener(this);
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
