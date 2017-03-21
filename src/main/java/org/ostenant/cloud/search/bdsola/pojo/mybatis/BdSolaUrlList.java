package org.ostenant.cloud.search.bdsola.pojo.mybatis;

import java.io.Serializable;
import java.util.Date;

public class BdSolaUrlList implements Serializable {

	private static final long serialVersionUID = -5030473534448313375L;

	private String id;

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

	@Override
	public String toString() {
		return "BdSolaUrlList [id=" + id + ", topUrl=" + topUrl + ", kw=" + kw
				+ ", searchType=" + searchType + ", sort=" + sort
				+ ", currentPage=" + currentPage + ", isCached=" + isCached
				+ ", cachedDir=" + cachedDir + ", ifExists=" + ifExists
				+ ", pages=" + pages + ", lastCrawlTime=" + lastCrawlTime + "]";
	}

}