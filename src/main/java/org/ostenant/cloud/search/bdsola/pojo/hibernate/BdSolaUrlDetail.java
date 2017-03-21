package org.ostenant.cloud.search.bdsola.pojo.hibernate;

import java.io.Serializable;
import java.util.Date;

public class BdSolaUrlDetail implements Serializable {

	private static final long serialVersionUID = -4309644832119949765L;

	private String id; // -- firstLevelUrl的后缀

	private String shareTitle;
	private String sharer;
	private Date shareTime;
	private String fileType;
	private String firstLevelUrl; // -- 一级页面当前Item的url

	private String secondLevelUrl; // -- 二级页面的url
	private String yunShareId; // -- 百度云后缀
	private String filePath; // -- 源文件存放路径

	private Boolean useCache; // -- 是否使用缓存
	private Date lastCrawlTime;
	private Integer crawlTimes; // -- 爬取次数

	private Boolean isCrawled; // -- 是否爬取过

	private BdSolaUrlList bdSolaUrlList; // -- 多对一

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public Date getShareTime() {
		return shareTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFirstLevelUrl() {
		return firstLevelUrl;
	}

	public void setFirstLevelUrl(String firstLevelUrl) {
		this.firstLevelUrl = firstLevelUrl;
	}

	public String getSecondLevelUrl() {
		return secondLevelUrl;
	}

	public void setSecondLevelUrl(String secondLevelUrl) {
		this.secondLevelUrl = secondLevelUrl;
	}

	public String getYunShareId() {
		return yunShareId;
	}

	public void setYunShareId(String yunShareId) {
		this.yunShareId = yunShareId;
	}

	public Boolean getUseCache() {
		return useCache;
	}

	public void setUseCache(Boolean useCache) {
		this.useCache = useCache;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getLastCrawlTime() {
		return lastCrawlTime;
	}

	public void setLastCrawlTime(Date lastCrawlTime) {
		this.lastCrawlTime = lastCrawlTime;
	}

	public Integer getCrawlTimes() {
		return crawlTimes;
	}

	public void setCrawlTimes(Integer crawlTimes) {
		this.crawlTimes = crawlTimes;
	}

	public Boolean getIsCrawled() {
		return isCrawled;
	}

	public void setIsCrawled(Boolean isCrawled) {
		this.isCrawled = isCrawled;
	}

	public BdSolaUrlList getBdSolaUrlList() {
		return bdSolaUrlList;
	}

	public void setBdSolaUrlList(BdSolaUrlList bdSolaUrlList) {
		this.bdSolaUrlList = bdSolaUrlList;
	}

	@Override
	public String toString() {
		return "BdSolaUrlDetail [id=" + id + ", shareTitle=" + shareTitle
				+ ", sharer=" + sharer + ", shareTime=" + shareTime
				+ ", fileType=" + fileType + ", firstLevelUrl=" + firstLevelUrl
				+ ", secondLevelUrl=" + secondLevelUrl + ", yunShareId="
				+ yunShareId + ", useCache=" + useCache + ", filePath="
				+ filePath + ", lastCrawlTime=" + lastCrawlTime
				+ ", crawlTimes=" + crawlTimes + ", isCrawled=" + isCrawled
				+ ", bdSolaUrlList=" + bdSolaUrlList + "]";
	}

}
