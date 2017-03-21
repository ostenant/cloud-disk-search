package org.ostenant.cloud.search.bdsola.pojo.entity;

import java.io.Serializable;
import java.util.Date;

public class BdSolaUrlItem implements Serializable {

	private static final long serialVersionUID = -7211471803764651764L;

	public static String domain = "http://www.bdsola.com";

	private String fileType;
	private String href;
	private String title;
	private String uploadTime;
	private String author;

	private String id;
	private String secondary;
	private String yunShareId;

	private Date lastCrawlTime;

	public BdSolaUrlItem() {
	}

	public BdSolaUrlItem(String fileType, String href, String title,
			String uploadTime, String author) {
		super();
		this.fileType = fileType;
		this.href = href;
		this.title = title;
		this.uploadTime = uploadTime;
		this.author = author;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecondary() {
		return secondary;
	}

	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}

	public String getYunShareId() {
		return yunShareId;
	}

	public void setYunShareId(String yunShareId) {
		this.yunShareId = yunShareId;
	}

	public Date getLastCrawlTime() {
		return lastCrawlTime;
	}

	public void setLastCrawlTime(Date lastCrawlTime) {
		this.lastCrawlTime = lastCrawlTime;
	}

	public static String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return "BdSolaUrlItem [fileType=" + fileType + ", href=" + href
				+ ", title=" + title + ", uploadTime=" + uploadTime
				+ ", author=" + author + ", id=" + id + ", secondary="
				+ secondary + ", yunShareId=" + yunShareId + ", lastCrawlTime="
				+ lastCrawlTime + "]";
	}

}
