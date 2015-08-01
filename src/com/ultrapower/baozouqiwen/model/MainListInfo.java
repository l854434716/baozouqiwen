package com.ultrapower.baozouqiwen.model;


import android.graphics.Bitmap;

public class MainListInfo{
	private String iconUrl;
	private String title;
	private boolean isOpening;	//记录当前item�?展开状�?
	private String id;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isOpening() {
		return isOpening;
	}
	public void setOpening(boolean isOpening) {
		this.isOpening = isOpening;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	
	

}
