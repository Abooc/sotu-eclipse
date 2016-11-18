package com.abooc.sotu.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.abooc.android.widget.ViewHolder;
import com.abooc.sotu.R;
import com.abooc.sotu.home.HomeAdapter;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.SearchResult;

import java.util.List;

public class GroupActivity extends AppCompatActivity implements GroupContract.View {

    RecyclerView mRecyclerView;

    HomeAdapter mHomeAdapter;


    GroupContract.Presenter mPresenter;

    public static void launch(Context context, String title) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra("title");
        setTitle(title);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_group);


        mHomeAdapter = new HomeAdapter();
        mHomeAdapter.setListener(new ViewHolder.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                GroupActivity.launch(getBaseContext(), "风景");
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        GridLayoutManager manager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mHomeAdapter);

        mPresenter = new GroupPresenter(this);
        mPresenter.load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showInfo(SearchResult category) {

    }

    @Override
    public void showImages(List<Image> list) {

    }

    @Override
    public void setPresenter(GroupContract.Presenter presenter) {

    }
}
