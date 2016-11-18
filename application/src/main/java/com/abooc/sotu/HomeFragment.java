package com.abooc.sotu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abooc.android.widget.ViewHolder;
import com.abooc.sotu.group.GroupActivity;
import com.abooc.sotu.home.HomeAdapter;
import com.abooc.sotu.home.HomeContract;
import com.abooc.sotu.home.HomePresenter;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.ImageCategory;
import com.abooc.util.Debug;

import java.util.List;


public class HomeFragment extends Fragment implements HomeContract.View {


    HomeContract.Presenter mPresenter;

    TextView mHomeText;
    RecyclerView mRecyclerView;

    HomeAdapter mHomeAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        mPresenter = new HomePresenter(this);

        mHomeAdapter = new HomeAdapter();
        mHomeAdapter.setListener(new ViewHolder.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                GroupActivity.launch(getContext(), "风景");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Debug.anchor();
        mHomeText = (TextView) view.findViewById(R.id.HomeText);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mHomeAdapter);

//        mPresenter.load();
    }


    @Override
    public void showInfo(ImageCategory category) {
        mHomeText.setText(category.toString());
    }

    @Override
    public void showImages(List<Image> list) {
        mHomeAdapter.getCollection().update(list);
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {

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
