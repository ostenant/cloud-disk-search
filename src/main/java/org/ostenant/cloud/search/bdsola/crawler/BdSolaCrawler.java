package org.ostenant.cloud.search.bdsola.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.ostenant.cloud.search.bdsola.common.BdSolaRetryHandler;
import org.ostenant.cloud.search.bdsola.common.BdSolaUtils;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlDetail;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;
import org.ostenant.cloud.search.bdsola.seed.BdSolaSeed;
import org.ostenant.cloud.search.bdsola.service.BdSolaService;
import org.ostenant.cloud.search.bdsola.service.BdSolaServiceImpl;
import org.ostenant.cloud.search.common.BdsolaConfig;
import org.ostenant.cloud.search.common.utils.PathParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BerkeleyDBManager;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

@SuppressWarnings("all")
public class BdSolaCrawler extends BreadthCrawler implements
		ServletContextAware {

	private static LinkedList<BdSolaSeed> solaSeeds = new LinkedList<BdSolaSeed>();
	private static LinkedList<BdSolaSeed> newSolaSeeds = new LinkedList<BdSolaSeed>();
	private static String dicPlaceHolder = BdsolaConfig.KEY_WORD_DIC_CONFIG;
	private static String crawlDataPlaceHolder = BdsolaConfig.CRAWL_DATA_PLACE_HOLDER;
	private static String firstLevelPlaceHolder = BdsolaConfig.FIRST_LEVEL_PLACE_HOLDER;
	private static final Logger log = Logger.getLogger(BdSolaCrawler.class);

	private ServletContext servletContext = null;
	private static HashMap<String, Object> bdsolaConfigMap = null;
	private static JSONObject bdsolaConfigJSON = null;
	private static boolean useCached = false;
	private static int crawlDepth = 0;
	private static long crawlInterval = 0;
	private List<String> matchUrlListIds = new ArrayList<String>();

	private BdSolaService bdSolaService;

	public void setBdSolaService(BdSolaService bdSolaService) {
		this.bdSolaService = bdSolaService;
	}

	public BdSolaService getBdSolaService() {
		return bdSolaService;

	}

	static {
		doPathFormat();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(new File(
					dicPlaceHolder)), BdsolaConfig.CHARSET);
			br = new BufferedReader(isr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String encodeResult = URLEncoder.encode(line.trim(),
						BdsolaConfig.CHARSET);
				BdSolaSeed bdSolaSeed = new BdSolaSeed(encodeResult);
				solaSeeds.add(bdSolaSeed);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					br = null;
				}
			}
		}
	}

	public static void doPathFormat() {
		dicPlaceHolder = PathParser.parser(BdSolaCrawler.class, dicPlaceHolder);
		crawlDataPlaceHolder = PathParser.parser(BdSolaCrawler.class,
				crawlDataPlaceHolder);
		firstLevelPlaceHolder = PathParser.parser(BdSolaCrawler.class,
				firstLevelPlaceHolder);
	}

	public BdSolaCrawler doConfig(HashMap<String, Object> bdsolaConfigMap) {

		boolean crawlResumable = bdsolaConfigMap.containsKey("crawlResumable") ? (Boolean) bdsolaConfigMap
				.get("crawlResumable") : false;
		log.info(" >> crawlResumable: " + crawlResumable);
		boolean autoParse = bdsolaConfigMap.containsKey("autoParse") ? (Boolean) bdsolaConfigMap
				.get("autoParse") : true;
		log.info(" >> autoParse: " + autoParse);
		boolean crawlUseCached = bdsolaConfigMap.containsKey("crawlUseCached") ? (Boolean) bdsolaConfigMap
				.get("crawlUseCached") : true;
		log.info(" >> crawlUseCached: " + crawlUseCached);
		int crawlThread = bdsolaConfigMap.containsKey("crawlThread") ? (Integer) bdsolaConfigMap
				.get("crawlThread") : 20;
		log.info(" >> crawlThread: " + crawlThread);
		int depth = bdsolaConfigMap.containsKey("crawlDepth") ? (Integer) bdsolaConfigMap
				.get("crawlDepth") : 1;
		log.info(" >> crawlDepth: " + crawlDepth);
		int crawlTopN = bdsolaConfigMap.containsKey("crawlTopN") ? (Integer) bdsolaConfigMap
				.get("crawlTopN") : 1000;
		log.info(" >> crawlTopN: " + crawlTopN);
		long interval = bdsolaConfigMap.containsKey("crawlInterval") ? Long
				.parseLong((String) bdsolaConfigMap.get("crawlInterval"))
				: 3600000L;
		log.info(" >> crawlInterval: " + crawlInterval);
		String dicEncoding = bdsolaConfigMap.containsKey("dicEncoding") ? (String) bdsolaConfigMap
				.get("dicEncoding") : "UTF-8";
		log.info(" >> dicEncoding: " + dicEncoding);

		useCached = crawlUseCached;
		crawlDepth = depth;
		crawlInterval = interval;

		setAutoParse(autoParse);
		setResumable(crawlResumable);
		setThreads(crawlThread);
		setTopN(crawlTopN);

		return this;

	}

	public BdSolaCrawler(String crawlPath, boolean autoParse,
			BdSolaService bdSolaService) {
		super(crawlPath, autoParse);

		this.bdSolaService = bdSolaService;
		// 正规则，待爬取网页至少符合一条正规则，才可以爬取
		for (BdSolaSeed baseSeed : solaSeeds) {
			try {
				int pageBound = BdSolaUtils.pageBound(baseSeed.getSeedUrl());
				if (pageBound > 0) {
					for (int pageNum = 1; pageNum <= pageBound; pageNum++) {
						newSolaSeeds.add(new BdSolaSeed(pageNum, baseSeed
								.getKw()));
					}
				} else if (pageBound == 0) {
					newSolaSeeds.add(baseSeed);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		for (BdSolaSeed seed : newSolaSeeds) {
			addSeed(seed.getSeedUrl());
			log.info(" >> Add Seed: " + seed.getSeedUrl());
		}

	}

	public void visit(Page page, CrawlDatums next) {
		Document doc = page.getDoc();
		String title = doc.title();
		String url = page.getUrl();
		log.debug(">> " + "Current title is  >> " + title);
		log.debug(">> " + "Current url is  >> " + url);

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

	/**
	 * 保存一级目录元素据 返回下一步处理动作
	 * 
	 * @param newSolaSeeds
	 * @param urlListIds
	 * @param firstLevelPlaceHolder
	 * @param bdsolaConfigMap
	 */
	private void waitForMetaData(LinkedList<BdSolaSeed> solaSeeds,
			List<String> matchUrlListIds, String firstLevelPlaceHolder,
			HashMap<String, Object> bdsolaConfigMap) {
		try {
			for (BdSolaSeed seed : solaSeeds) {
				String seedUrl = seed.getSeedUrl();
				BdSolaUrlList bdSolaUrlList = bdSolaService.queryDBForCrawler(
						seedUrl, bdsolaConfigMap, firstLevelPlaceHolder,
						matchUrlListIds);

				if (bdSolaUrlList == null)
					bdSolaUrlList = new BdSolaUrlList();
				int pages = BdSolaUtils.pageBound(seed.getSeedUrl());
				if (pages > 0) {
					bdSolaUrlList.setPages(pages);
				} else {
					BdSolaRetryHandler retryHandler = new BdSolaRetryHandler();
					Object pageNum = retryHandler.executeHttpRetry(seedUrl, 3);
					if (pageNum == null) {
						bdSolaUrlList.setPages(0);
						continue;
					} else if (pageNum instanceof Integer) {
						pages = (Integer) pageNum;
						bdSolaUrlList.setPages(pages);
					}
				}

				bdSolaService.saveOrUpdate(bdSolaUrlList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		BdSolaCrawler crawler = new BdSolaCrawler(PathParser.parser(
				BdSolaCrawler.class, crawlDataPlaceHolder), true,
				new BdSolaServiceImpl());
		crawler.setThreads(BdsolaConfig.CRAWL_THRAD_NUM);
		crawler.setExecuteInterval(1000);
		crawler.setResumable(false);
		crawler.start(1);
	}

	public void start(int depth, BdSolaUrlDetail bdSolaUrlDetail)
			throws Exception {
		start(depth);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		bdsolaConfigMap = (HashMap<String, Object>) servletContext
				.getAttribute(BdsolaConfig.BDSOLA_CONFIG_MAP);
		bdsolaConfigJSON = (JSONObject) servletContext
				.getAttribute(BdsolaConfig.BDSOLA_CONFIG_JSON);
		log.info(" >> bdsolaConfigJSON: " + this.bdsolaConfigJSON.toString());

		waitForMetaData(solaSeeds, matchUrlListIds, firstLevelPlaceHolder,
				bdsolaConfigMap);

	}

}
