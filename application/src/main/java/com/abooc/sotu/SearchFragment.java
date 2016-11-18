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
import android.widget.EditText;

import com.abooc.android.widget.ViewHolder;
import com.abooc.sotu.group.GroupActivity;
import com.abooc.sotu.home.HomeAdapter;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.SearchResult;
import com.abooc.sotu.search.SearchContract;
import com.abooc.sotu.search.SearchPresenter;
import com.abooc.util.Debug;

import java.util.List;


public class SearchFragment extends Fragment implements SearchContract.View, View.OnClickListener {


    EditText mSearchContent;
    RecyclerView mRecyclerView;
    HomeAdapter mHomeAdapter;
    SearchPresenter mPresenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Debug.anchor();
        mSearchContent = (EditText) view.findViewById(R.id.SearchEdit);
        view.findViewById(R.id.Search).setOnClickListener(this);

        mHomeAdapter = new HomeAdapter();
        mHomeAdapter.setListener(new ViewHolder.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                GroupActivity.launch(getContext(), "风景");
            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mHomeAdapter);

        mPresenter = new SearchPresenter(this);

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



        String searchWord = mSearchContent.getText().toString().trim();
        mPresenter.load(searchWord);
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

    @Override
    public void showInfo(SearchResult category) {

    }

    @Override
    public void showImages(List<Image> list) {
        mHomeAdapter.getCollection().update(list);
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {

    }
}
