package org.ostenant.cloud.search.bdsola.seed;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class BdSolaSeed implements Serializable {

	private static final long serialVersionUID = -7118550405373380498L;

	// http://www.bdsola.com/search.php?kw=Dubbo&searchType=ALL&sort=0&cur_page=2
	private String baseUrl = DEFAULT_URL;

	private static final String DEFAULT_URL = "http://www.bdsola.com/search.php";

	private String kw;
	private String searchType;
	private int sort = 0;
	private int currentPage = 1;
	private Boolean ifDownLoad;

	public static final int SORT_ASC = 0;
	public static final int SORT_DESC = 1;

	public static final int DEFAULT_PAGE = 1;

	public String getSeedUrl() {
		boolean first = true;
		String url = DEFAULT_URL + "?";
		if (StringUtils.isNotBlank(kw)) {
			if (kw.trim().indexOf(" ") > -1) {
				String[] multiKw = kw.trim().split(" ");

				for (int k = 0; k < multiKw.length; k++) {
					if (k == 0) {
						kw += multiKw[k];
					} else {
						kw += "+" + multiKw[k];
					}
				}
			}
			url += "kw=" + kw;
			first = false;
		}

		if (StringUtils.isNotBlank(searchType)) {
			if (first) {
				url += "searchType=" + searchType;
				first = false;
			} else {
				url += "&searchType=" + searchType;
			}
		}

		if (sort != SORT_ASC || sort != SORT_DESC) {
			sort = SORT_ASC;
		}

		if (first) {
			url += "sort=" + sort;
			first = false;
		} else {
			url += "&sort=" + sort;
		}

		if (currentPage < 0) {
			currentPage = DEFAULT_PAGE;
		}

		if (first) {
			url += "cur_page=" + currentPage;
			first = false;
		} else {
			url += "&cur_page=" + currentPage;
		}

		return url;

	}

	public static void main(String[] args) {
		BdSolaSeed seed = new BdSolaSeed("Dubbo", 1);
		String seedUrl = seed.getSeedUrl();
		System.out.println(seedUrl);
	}

	public BdSolaSeed(String kw) {
		this(kw, 0);
	}

	public BdSolaSeed(String kw, int sort) {
		this(kw, BdSolaTypes.ALL, SORT_ASC, DEFAULT_PAGE);
	}

	public BdSolaSeed(int currentPage, String kw) {
		this(kw, SORT_ASC, currentPage);
	}

	public BdSolaSeed(String kw, String searchType) {
		this(kw, searchType, SORT_ASC, DEFAULT_PAGE);
	}

	public BdSolaSeed(String kw, int sort, int currentPage) {
		this(kw, BdSolaTypes.ALL, sort, currentPage);
	}

	public BdSolaSeed(String kw, String searchType, int sort, int currentPage) {
		this(DEFAULT_URL, kw, searchType, sort, currentPage);
	}

	public BdSolaSeed(String baseUrl, String kw, String searchType, int sort,
			int currentPage) {
		super();
		this.baseUrl = baseUrl;
		this.kw = kw;
		this.searchType = searchType;
		this.sort = sort;
		this.currentPage = currentPage;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Boolean getIfDownLoad() {
		return ifDownLoad;
	}

	public void setIfDownLoad(Boolean ifDownLoad) {
		this.ifDownLoad = ifDownLoad;
	}

	@Override
	public String toString() {
		return "BdSolaSeed [baseUrl=" + baseUrl + ", kw=" + kw
				+ ", searchType=" + searchType + ", sort=" + sort
				+ ", currentPage=" + currentPage + ", ifDownLoad=" + ifDownLoad
				+ "]";
	}

}
