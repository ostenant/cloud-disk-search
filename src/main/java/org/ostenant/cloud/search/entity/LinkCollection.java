package org.ostenant.cloud.search.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkCollection implements Iterable<LinkBean> {
	
	private String pageUrl;

	private List<LinkBean> linkBeans = new ArrayList<LinkBean>();
	
	public LinkCollection() {
	}
	
	public LinkCollection(String pageUrl) {
		this.pageUrl = pageUrl;
	}


	public void add(LinkBean linkBean) {
		linkBeans.add(linkBean);
	}
	

	public String getPageUrl() {
		return pageUrl;
	}
	

	public LinkCollection setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
		return this;
	}

	public Iterator<LinkBean> iterator() {
		return linkBeans.iterator();
	}

	public LinkBean remove(int index) {
		return linkBeans.remove(index);
	}

	public boolean remove(LinkBean linkBean) {
		return linkBeans.remove(linkBean);
	}

	public void clear() {
		linkBeans.clear();
	}

	public boolean isEmpty() {

		return linkBeans.isEmpty();
	}

	public int indexOf(LinkBean linkBean) {
		return linkBeans.indexOf(linkBean);
	}

	public String toString() {
		return linkBeans.toString();
	}

	public LinkBean get(int index) {
		return linkBeans.get(index);
	}

	public int size() {
		return linkBeans.size();
	}

}
