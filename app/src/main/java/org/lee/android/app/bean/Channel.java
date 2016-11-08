package org.lee.android.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订阅频道对象。
 * 
 * @author ruiyuLee
 * 
 */
public class Channel implements Parcelable {
	public String id;
	public String position;
	public String parent;
	public String name;
	public String desc;
	public String imageUrl;

	public static final Parcelable.Creator<Channel> CREATOR = new Creator<Channel>() {

		@Override
		public Channel[] newArray(int size) {
			return new Channel[size];
		}

		@Override
		public Channel createFromParcel(Parcel source) {
			return new Channel(source);
		}
	};

	public Channel() {
	}

	public Channel(Parcel source) {
		id = source.readString();
		position = source.readString();
		parent = source.readString();
		name = source.readString();
		desc = source.readString();
		imageUrl = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(id);
		dest.writeString(position);
		dest.writeString(parent);
		dest.writeString(name);
		dest.writeString(desc);
		dest.writeString(imageUrl);
	}

	@Override
	public boolean equals(Object channel) {
		if (this.name.equals(((Channel) channel).name)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "id:" + id + "position:" + position + "parent:" + parent
				+ "title:" + name;
	}
}