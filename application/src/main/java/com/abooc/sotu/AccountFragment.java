package com.abooc.sotu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abooc.util.Debug;


public class AccountFragment extends Fragment implements View.OnClickListener {


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Debug.anchor();
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Settings).setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Settings:

                break;
        }
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
