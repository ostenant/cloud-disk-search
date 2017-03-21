package org.ostenant.cloud.search.bdsola.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ostenant.cloud.search.common.BdsolaConfig;

import cn.edu.hfut.dmic.webcollector.model.Page;

public class BdSolaUtils {

	private static final Logger log = Logger.getLogger(BdSolaUtils.class);

	public static String getCachedDir(String url, String parentFolder) {
		try {
			if(parentFolder.endsWith("/")){
				parentFolder = parentFolder.substring(0, parentFolder.length() - 1);
			}
			String keyWord = "";
			String currentPage = "";
			if (url.lastIndexOf("?") > -1) {
				String params[] = url.substring(url.lastIndexOf("?"))
						.split("&");
				for (String param : params) {
					if (param.contains("kw")) {
						String kv[] = param.split("=");

						keyWord = URLDecoder.decode(kv[1], "UTF-8");

					} else if (param.contains("cur_page")) {
						String kv[] = param.split("=");
						currentPage = kv[1];
					}
				}
			}

			if (StringUtils.isNotBlank(keyWord)
					&& StringUtils.isNotBlank(currentPage)) {
				String location = parentFolder + "/" + keyWord;
				File dir = new File(location);
				if (!dir.exists()) {
					dir.mkdirs();
					log.info(" >> succeed to mkdir: " + location);
				} else {
					log.info(" >> dir: " + location + " >> has been exists");
				}

				return location;
			} else {
				throw new RuntimeException(" >> Url has error: " + url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return BdsolaConfig.STATUS_ERROR;
		}

	}

	/**
	 * 保存下载的html source
	 * 
	 * @param page
	 * @param url
	 * @param parentFolder
	 */
	public static String saveFirstLevelSource(Page page, String url,
			String parentFolder) {
		try {
			String keyWord = "";
			String currentPage = "";
			if (url.lastIndexOf("?") > -1) {
				String params[] = url.substring(url.lastIndexOf("?"))
						.split("&");
				for (String param : params) {
					if (param.contains("kw")) {
						String kv[] = param.split("=");

						keyWord = URLDecoder.decode(kv[1], "UTF-8");

					} else if (param.contains("cur_page")) {
						String kv[] = param.split("=");
						currentPage = kv[1];
					}
				}

			}

			if (StringUtils.isNotBlank(keyWord)
					&& StringUtils.isNotBlank(currentPage)) {
				String location = parentFolder + "/" + keyWord;
				File dir = new File(location);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				String fileName = keyWord + "_" + currentPage + ".html";
				String filePath = location + "/" + fileName;
				File source = new File(filePath);
				String html = page.getHtml();
				html.replaceAll("<<", "");

				Document doc = page.doc();
				Elements pageElements = doc.select("div.page");

				if (pageElements != null) {
					writeToFile(source, html);

					log.info(html);
				}

				return filePath;
			}

			return BdsolaConfig.STATUS_ERROR;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void writeToFile(File source, String html)
			throws IOException {
		FileWriter writer = new FileWriter(source);
		writer.write(html);
		writer.flush();
		writer.close();
	}

	public static int pageBound(String url) throws Exception {
		Document doc = Jsoup.connect(url).get();

		return pageBound(doc);

	}

	public static int pageBound(Document doc) throws Exception {
		// "http://www.bdsola.com/search.php?kw=springmvc+spring+mybatis&searchType=ALL&sort=0&cur_page=1";

		Elements pages = doc.select("div.page");
		// 检索结果是否存在
		if (StringUtils.isBlank(pages.html().trim()) || pages.size() <= 0) {
			log.debug(">> " + " No such search result!");
			return -1;
		}
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		log.info(pages.html());
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		// 当前页的page分页段
		Elements elements = doc.select("div.page .current");
		if (elements == null || elements.size() <= 0) {
			log.debug(">> " + " No current page!");
			return -1;
		} else {
			String html = elements.html();
			int currentPage = Integer.parseInt(html);
			log.info(">> currentPage: " + currentPage);

			// a标签最后一个children
			Elements aElements = doc.select(".page a");
			if (aElements.size() > 0) {
				Element aLast = aElements.last();
				String href = aLast.attr("href");
				String lastPage = href.substring(href.lastIndexOf("=") + 1);
				int lastPageNum = Integer.parseInt(lastPage);
				log.info(">> lastPageNum: " + lastPageNum);
				return Math.max(currentPage, lastPageNum);
			}

			return currentPage;
		}

	}

	public static void main(String[] args) {
		System.out
				.println(nextPageUrl(
						"http://www.bdsola.com/search.php?kw=spring&searchType=ALL&sort=0&cur_page=1",
						4));
	}

	public static String nextPageUrl(String originUrl, int page) {
		if (StringUtils.isNotBlank(originUrl)) {
			String template = originUrl.substring(0,
					originUrl.lastIndexOf("=") + 1);
			if (page > 0) {
				return template + page;
			}
		}
		return originUrl;
	}

	public static HashMap<String, String> generateByURL(String url) {
		HashMap<String, String> map = new HashMap<String, String>();
		StringBuffer buffer = new StringBuffer();
		String kw = "";
		String searchType = "";
		String sort = "";
		String cur_page = "";
		String[] parameters = url.substring(url.lastIndexOf("?") + 1)
				.split("&");
		if (parameters != null && parameters.length > 0) {
			for (String parameter : parameters) {
				String[] kv = parameter.split("=");
				if (kv != null && kv.length >= 2) {
					if ("kw".equals(kv[0].trim())) {
						kw = kv[1];
						map.put("kw", kw);
					} else if ("searchType".equals(kv[0].trim())) {
						searchType = kv[1];
						map.put("searchType", searchType);
					} else if ("sort".equals(kv[0].trim())) {
						sort = kv[1];
						map.put("sort", sort);
					} else if ("cur_page".equals(kv[0].trim())) {
						cur_page = kv[1];
						map.put("currentPage", cur_page);
					}
				}
			}
		}

		buffer.append(kw).append("_")//
				.append(searchType).append("_")//
				.append(sort).append("_")//
				.append(cur_page);

		map.put("id", buffer.toString().trim());

		return map;
	}

}
