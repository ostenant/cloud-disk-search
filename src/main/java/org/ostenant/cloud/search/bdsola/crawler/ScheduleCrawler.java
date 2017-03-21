package org.ostenant.cloud.search.bdsola.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.ostenant.cloud.search.bdsola.common.BdSolaUtils;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlDetail;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;
import org.ostenant.cloud.search.bdsola.service.BdSolaService;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

@SuppressWarnings("all")
public class ScheduleCrawler extends BreadthCrawler {

	private static Logger logger = Logger.getLogger(ScheduleCrawler.class);

	private BdSolaService bdSolaService;

	public void setBdSolaService(BdSolaService bdSolaService) {
		this.bdSolaService = bdSolaService;
	}

	public BdSolaService getBdSolaService() {
		return bdSolaService;

	}

	public ScheduleCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		@SuppressWarnings("deprecation")
		Document doc = page.getDoc();
		String title = doc.title();
		String url = page.getUrl();
		logger.debug(">> " + "Current title is  >> " + title);
		logger.debug(">> " + "Current url is  >> " + url);

		// --文件路径
		// String filePath = BdSolaUtils.saveFirstLevelSource(page, url,
		// firstLevelPlaceHolder);
		HashMap<String, String> urlDescriptionMap = BdSolaUtils
				.generateByURL(url);

		String id = urlDescriptionMap.get("id");
		String kw = urlDescriptionMap.get("kw");
		String searchType = urlDescriptionMap.get("searchType");
		String sort = urlDescriptionMap.get("sort");
		String currentPage = urlDescriptionMap.get("currentPage");

		BdSolaUrlList bdSolaUrlList = bdSolaService.getBdSolaUrlListById(id);
		String cachedDir = bdSolaUrlList.getCachedDir();
		int pages = bdSolaUrlList.getPages();

		File cacheDir = new File(cachedDir);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		} else {
			// 删除缓存
			cacheDir.delete();
			cacheDir.mkdirs();
		}

		String parentDir = cachedDir.endsWith("/") ? cachedDir.substring(0,
				cachedDir.length() - 1) : cachedDir;
		if (StringUtils.isNotBlank(currentPage)
				&& Integer.parseInt(currentPage) > 0) {
			String fileName = kw + "_" + currentPage + ".html";
			File cachedFile = new File(fileName);
			String html = page.getHtml();
			try {
				BdSolaUtils.writeToFile(cachedFile, html);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 返回持久化对象pojo
			List<BdSolaUrlDetail> bdSolaUrlDetails = bdSolaService
					.crawlAndsaveUrlDetail(page, fileName, id);

		}
	}
	
	@Override
	protected void afterParse(Page page, CrawlDatums next) {
		super.afterParse(page, next);
	}

}
