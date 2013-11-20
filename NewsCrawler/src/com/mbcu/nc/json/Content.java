package com.mbcu.nc.json;

public class Content {

	private String url;
	private String title;
	private String author;
	private String place;
	private long timestamp;
	private String text;
	private String html;

	/**
	 * 
	 * @param url
	 * url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 
	 * @param author
	 *            Name of author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 
	 * @param text
	 *            parsed text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 
	 * @param html
	 *            The raw html
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * 
	 * @param place
	 *            Place where the news is reported
	 */

	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * 
	 * @param timestamp
	 *            Timestamp (second) when the news is published
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @param title
	 *            Title of the news
	 */
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

	public String getText() {
		return text;
	}
	
	public String getUrl() {
		return url;
	}
}
