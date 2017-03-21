package org.ostenant.cloud.search.entity;

public class LinkBean {

	private String tagName;
	private String link;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public LinkBean(String tagName, String link) {
		this.tagName = tagName;
		this.link = link;
	}

}
