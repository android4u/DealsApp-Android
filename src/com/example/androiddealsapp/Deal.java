package com.example.androiddealsapp;

import android.net.Uri;



public class Deal {
	
	private String id;
	private String smallImageUrl;
	private String shortAnnouncementTitle;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getShortAnnouncementTitle() {
		return shortAnnouncementTitle;
	}
	public void setShortAnnouncementTitle(String shortAnnouncementTitle) {
		this.shortAnnouncementTitle = shortAnnouncementTitle;
	}
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	

}
