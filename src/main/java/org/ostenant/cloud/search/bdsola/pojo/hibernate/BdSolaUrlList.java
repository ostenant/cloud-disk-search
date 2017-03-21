package org.ostenant.cloud.search.bdsola.pojo.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BdSolaUrlList implements Serializable {

	private static final long serialVersionUID = 5481005061672046918L;

	private String id; // -- kw+searchType+sort+currentPage

	private String topUrl;

	private String kw;
	private String searchType;
	private Integer sort;
	private Integer currentPage;

	private Boolean isCached;

	private String cachedDir;
	private Boolean ifExists;
	private Integer pages;

	private Date lastCrawlTime;
	private Integer avgCrawlTimes; // 平均抓取次数

	private Set<BdSolaUrlDetail> bdSolaUrlDetails = new HashSet<BdSolaUrlDetail>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopUrl() {
		return topUrl;
	}

	public void setTopUrl(String topUrl) {
		this.topUrl = topUrl;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Boolean getIsCached() {
		return isCached;
	}

	public void setIsCached(Boolean isCached) {
		this.isCached = isCached;
	}

	public String getCachedDir() {
		return cachedDir;
	}

	public void setCachedDir(String cachedDir) {
		this.cachedDir = cachedDir;
	}

	public Boolean getIfExists() {
		return ifExists;
	}

	public void setIfExists(Boolean ifExists) {
		this.ifExists = ifExists;
	}

	public Set<BdSolaUrlDetail> getBdSolaUrlDetails() {
		return bdSolaUrlDetails;
	}

	public void setBdSolaUrlDetails(Set<BdSolaUrlDetail> bdSolaUrlDetails) {
		this.bdSolaUrlDetails = bdSolaUrlDetails;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Date getLastCrawlTime() {
		return lastCrawlTime;
	}

	public void setLastCrawlTime(Date lastCrawlTime) {
		this.lastCrawlTime = lastCrawlTime;
	}

	public Integer getAvgCrawlTimes() {
		return avgCrawlTimes;
	}

	public void setAvgCrawlTimes(Integer avgCrawlTimes) {
		this.avgCrawlTimes = avgCrawlTimes;
	}

	@Override
	public String toString() {
		return "BdSolaUrlList [id=" + id + ", topUrl=" + topUrl + ", kw=" + kw
				+ ", searchType=" + searchType + ", sort=" + sort
				+ ", currentPage=" + currentPage + ", isCached=" + isCached
				+ ", cachedDir=" + cachedDir + ", ifExists=" + ifExists
				+ ", pages=" + pages + ", lastCrawlTime=" + lastCrawlTime
				+ ", avgCrawlTimes=" + avgCrawlTimes + ", bdSolaUrlDetails="
				+ bdSolaUrlDetails + "]";
	}

}