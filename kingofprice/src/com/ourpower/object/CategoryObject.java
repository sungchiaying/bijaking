package com.ourpower.object;

import android.util.Log;

public class CategoryObject {
	int class_id;
	String name;
	Channels[] channels;

	/**
	 * public CategoryObject (int class_id, String name, int Channel[]){
	 * this.class_id = class_id; this.name = name; }
	 */

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Channels[] getChannels() {
		return channels;
	}

	public void setChannels(Channels[] channels) {
		this.channels = channels;
	}
}
