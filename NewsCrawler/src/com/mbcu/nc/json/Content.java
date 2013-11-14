package com.mbcu.nc.json;

public class Content {
	
	private String title;
	private String author;
	private String place;
	private String html;
	private long timestamp;
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setHtml(String html) {
		this.html = html;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getHtml() {
		return html;
	}
	
	public String getPlace() {
		return place;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public String getTitle() {
		return title;
	}

}
