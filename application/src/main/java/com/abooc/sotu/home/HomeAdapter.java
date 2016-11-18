package com.abooc.sotu.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abooc.android.widget.RecyclerViewAdapter;
import com.abooc.android.widget.ViewHolder;
import com.abooc.sotu.R;
import com.abooc.sotu.modle.Image;
import com.squareup.picasso.Picasso;

/**
 * Created by dayu on 2016/11/10.
 */

public class HomeAdapter extends RecyclerViewAdapter<Image> {


    ViewHolder.OnRecyclerItemClickListener onRecyclerItemClickListener;

    public void setListener(ViewHolder.OnRecyclerItemClickListener listener) {
        onRecyclerItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list_item, parent, false);
        return new HomeViewHolder(v, onRecyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Image image = getItem(position);
        HomeViewHolder holder = (HomeViewHolder) h;

        Picasso.with(h.getContext()).load(image.thumbnail_url).into(holder.imageView);
    }

    class HomeViewHolder extends ViewHolder {

        ImageView imageView;

        public HomeViewHolder(View itemLayoutView, OnRecyclerItemClickListener listener) {
            super(itemLayoutView, listener);
        }

        @Override
        public void onBindedView(View itemLayoutView) {
            imageView = (ImageView) itemLayoutView.findViewById(R.id.ImageView);

        }
    }


}
