package org.lee.android.activity;

import java.util.ArrayList;

import org.lee.android.app.bean.Category;
import org.lee.android.app.bean.Channel;

import org.lee.android.simples.baidupicture.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ParentAdapter extends BaseAdapter {

	private Context iCtx;
	private ArrayList<Category> array;

	public ParentAdapter(Context c) {
		iCtx = c;
	}

	public void setArray(ArrayList<Category> array) {
		this.array = array;
	}

	@Override
	public int getCount() {
		return array == null ? 0 : array.size();
	}

	@Override
	public Category getItem(int position) {
		return array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return array.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(iCtx).inflate(
					R.layout.sliding_menu_list_parent_item, null);
		}
		Category rss = getItem(position);
		((TextView) convertView).setText(rss.name);
		return convertView;
	}

}

class ChildAdapter extends BaseAdapter {

	private ArrayList<Channel> array = new ArrayList<Channel>();
	private Context iCtx;

	public ChildAdapter(Context ctx, ArrayList<Channel> array) {
		this.iCtx = ctx;
		this.array = array;
	}

	public void setArray(ArrayList<Channel> array) {
		this.array = array;
	}

	@Override
	public int getCount() {
		return array == null ? 0 : array.size();
	}

	@Override
	public Channel getItem(int position) {
		return array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(iCtx).inflate(
					R.layout.sliding_menu_list_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView;
			convertView.setTag(viewHolder);
		}
		Channel item = getItem(position);
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.title.setText(item.name);
		return convertView;
	}

	private class ViewHolder {
		TextView title;
	}

}