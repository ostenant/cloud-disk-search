package org.ostenant.cloud.search.bdsola.service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.ostenant.cloud.search.bdsola.common.BdSolaUtils;
import org.ostenant.cloud.search.bdsola.crawler.BdSolaCrawler;
import org.ostenant.cloud.search.bdsola.dao.hibernate.BdSolaUrlDetailDao;
import org.ostenant.cloud.search.bdsola.dao.hibernate.BdSolaUrlListDao;
import org.ostenant.cloud.search.bdsola.dao.mybatis.BdSolaUrlDetailMapper;
import org.ostenant.cloud.search.bdsola.dao.mybatis.BdSolaUrlListMapper;
import org.ostenant.cloud.search.bdsola.pojo.entity.BdSolaUrlItem;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlDetail;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;
import org.ostenant.cloud.search.bdsola.trigger.BdSolaRequestTask;
import org.ostenant.cloud.search.bdsola.web.WebInitialListener;
import org.ostenant.cloud.search.common.BdsolaConfig;
import org.ostenant.cloud.search.common.utils.CommonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;

import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.util.JsoupUtils;

@Transactional
@SuppressWarnings("all")
public class BdSolaServiceImpl implements BdSolaService, ServletContextAware {

	private BdSolaUrlDetailDao bdSolaUrlDetailDao;
	private BdSolaUrlListDao bdSolaUrlListDao;
	private BdSolaUrlListMapper bdSolaUrlListMapper;
	private BdSolaUrlDetailMapper bdSolaUrlDetailMapper;

	private HashMap<String, Object> bdsolaConfigMap = null;
	private JSONObject bdsolaConfigJSON = null;
	private ServletContext servletContext = null;

	private long interval;
	private boolean crawlUseCached;
	private int depth;

	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static Logger logger = Logger.getLogger(BdSolaServiceImpl.class);

	private Set<BdSolaUrlList> cachedMatchBeans = new LinkedHashSet<BdSolaUrlList>(
			1024);

	public Set<BdSolaUrlList> getCachedMatchBeans() {
		return cachedMatchBeans;
	}

	public BdSolaUrlDetailDao getBdSolaUrlDetailDao() {
		return bdSolaUrlDetailDao;
	}

	public void setBdSolaUrlDetailDao(BdSolaUrlDetailDao bdSolaUrlDetailDao) {
		this.bdSolaUrlDetailDao = bdSolaUrlDetailDao;
	}

	public BdSolaUrlListDao getBdSolaUrlListDao() {
		return bdSolaUrlListDao;
	}

	public void setBdSolaUrlListDao(BdSolaUrlListDao bdSolaUrlListDao) {
		this.bdSolaUrlListDao = bdSolaUrlListDao;
	}

	public BdSolaUrlListMapper getBdSolaUrlListMapper() {
		return bdSolaUrlListMapper;
	}

	public void setBdSolaUrlListMapper(BdSolaUrlListMapper bdSolaUrlListMapper) {
		this.bdSolaUrlListMapper = bdSolaUrlListMapper;
	}

	public BdSolaUrlDetailMapper getBdSolaUrlDetailMapper() {
		return bdSolaUrlDetailMapper;
	}

	public void setBdSolaUrlDetailMapper(
			BdSolaUrlDetailMapper bdSolaUrlDetailMapper) {
		this.bdSolaUrlDetailMapper = bdSolaUrlDetailMapper;
	}

	@Transactional(readOnly = true)
	public List<String> urlListIds() {
		List<String> urlListIds = bdSolaUrlListMapper.queryUrlListIds();
		return urlListIds;
	}

	/**
	 * 保存文件并记入数据库
	 * 
	 * @param url
	 * @param bdsolaConfigMap
	 * @param firstLevelPlaceHolder
	 * @return
	 */
	public BdSolaUrlList queryDBForCrawler(String url,
			HashMap<String, Object> bdsolaConfigMap,
			String firstLevelPlaceHolder, List<String> matchUrlListIds) {
		/* 获取配置信息 */
		boolean crawlUseCached = bdsolaConfigMap.containsKey("crawlUseCached") ? (Boolean) bdsolaConfigMap
				.get("crawlUseCached") : true;

		List<String> urlListIds = urlListIds();

		if (StringUtils.isNotBlank(url)) {
			HashMap<String, String> urlDescriptionMap = BdSolaUtils
					.generateByURL(url);

			String id = urlDescriptionMap.get("id");
			String kw = urlDescriptionMap.get("kw");
			String searchType = urlDescriptionMap.get("searchType");
			String sort = urlDescriptionMap.get("sort");
			String currentPage = urlDescriptionMap.get("currentPage");

			BdSolaUrlList bdSolaUrlList = null;
			if (urlListIds != null && urlListIds.size() > 0
					&& urlListIds.contains(id)) {
				// 匹配的id列表
				logger.info(" >> matchUrlListIds size => "
						+ matchUrlListIds.size());
				matchUrlListIds.add(id);
				if (cachedMatchBeans.size() > 0) {
					for (BdSolaUrlList cachedFirst : cachedMatchBeans) {
						if (StringUtils.equals(id, cachedFirst.getId())) {
							bdSolaUrlList = cachedFirst;
						}
						// -- 移除旧的缓存
						boolean removeStatus = cachedMatchBeans
								.remove(cachedFirst);
						logger.debug(" >> Cached remove status => "
								+ removeStatus);
					}
				}

				if (bdSolaUrlList == null) {
					bdSolaUrlList = bdSolaUrlListDao.get(BdSolaUrlList.class,
							id);
				}

				String cachedDir = bdSolaUrlList.getCachedDir();
				if (StringUtils.isNotBlank(cachedDir)) {
					File dir = new File(cachedDir);
					if (!dir.exists()) {
						dir.mkdirs();
					}
				} else {
					cachedDir = BdSolaUtils.getCachedDir(url,
							firstLevelPlaceHolder);
					logger.warn("CachedDir not found, to be rebuilt!");
				}

				bdSolaUrlListDao.saveOrUpdate(bdSolaUrlList);
				// -- 更新新的缓存
				cachedMatchBeans.add(bdSolaUrlList);
				logger.debug(" >> Add cache ++ " + cachedMatchBeans.size());
				return bdSolaUrlList;

			} else {
				BdSolaUrlList newBdSolaUrlList = new BdSolaUrlList();
				newBdSolaUrlList.setId(id);
				newBdSolaUrlList.setKw(kw);
				newBdSolaUrlList.setSearchType(searchType);
				newBdSolaUrlList.setSort("0".equals(sort) ? 0 : 1);
				newBdSolaUrlList.setTopUrl(url);
				newBdSolaUrlList.setCurrentPage(Integer.parseInt(currentPage));
				newBdSolaUrlList.setIfExists(true);
				newBdSolaUrlList.setIsCached(crawlUseCached);
				String cachedDir = BdSolaUtils.getCachedDir(url,
						firstLevelPlaceHolder);
				newBdSolaUrlList.setCachedDir(cachedDir);

				bdSolaUrlListDao.save(newBdSolaUrlList);

				return bdSolaUrlList;
			}
		} else {
			logger.error(" >> Url is empty!");
			return null;
		}

	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		bdsolaConfigMap = (HashMap<String, Object>) servletContext
				.getAttribute(BdsolaConfig.BDSOLA_CONFIG_MAP);
		bdsolaConfigJSON = (JSONObject) servletContext
				.getAttribute(BdsolaConfig.BDSOLA_CONFIG_JSON);

		interval = bdsolaConfigMap.containsKey("crawlInterval") ? Long
				.parseLong((String) bdsolaConfigMap.get("crawlInterval"))
				: 3600000L;

		crawlUseCached = bdsolaConfigMap.containsKey("crawlUseCached") ? (Boolean) bdsolaConfigMap
				.get("crawlUseCached") : true;
		depth = bdsolaConfigMap.containsKey("crawlDepth") ? (Integer) bdsolaConfigMap
				.get("crawlDepth") : 1;

	}

	@Override
	public void scheduleCrawler(BdSolaCrawler bdSolaCrawler) {
		Set<BdSolaUrlList> cachedMatchBeans = getCachedMatchBeans();
		try {
			synchronized (this) {
				if (cachedMatchBeans.size() > 0) {
					for (BdSolaUrlList cachedListBean : cachedMatchBeans) {
						Set<BdSolaUrlDetail> cachedDetailBeans = cachedListBean
								.getBdSolaUrlDetails();
						if (cachedDetailBeans != null
								&& cachedDetailBeans.size() > 0) {
							for (BdSolaUrlDetail cachedDetailBean : cachedDetailBeans) {
								int crawlTimes = cachedDetailBean
										.getCrawlTimes();
								Date lastCrawlTime = cachedDetailBean
										.getLastCrawlTime();
								Boolean isCrawled = cachedDetailBean
										.getIsCrawled();
								if ((!isCrawled && crawlTimes <= 0)
										|| lastCrawlTime == null) {
									// 改装start爬虫
									cachedDetailBean.setIsCrawled(true);
									cachedDetailBean
											.setCrawlTimes(crawlTimes++);
									cachedDetailBean
											.setLastCrawlTime(new Date());
									cachedDetailBean.setUseCache(true);
									bdSolaCrawler.start(crawlTimes,
											cachedDetailBean);
								} else {
									long standardInterval = interval;
									if (lastCrawlTime != null) {
										Date currentTime = new Date();
										long nowlyInterval = currentTime
												.getTime()
												- lastCrawlTime.getTime();
										logger.debug(" >> Last crawl time: "
												+ format.format(lastCrawlTime));
										if (standardInterval > 0
												&& nowlyInterval >= standardInterval) {
											cachedDetailBean
													.setLastCrawlTime(currentTime);
											cachedDetailBean.setIsCrawled(true);
											cachedDetailBean
													.setCrawlTimes(++crawlTimes);
											bdSolaCrawler.start(depth,
													cachedDetailBean);
											logger.debug(" >> Crawl Time arrive: "
													+ format.format(currentTime));
										} else {
											logger.debug(" >> Next crawl time: "
													+ format.format(currentTime
															.getTime()
															+ standardInterval));
										}
									}
								}

							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void saveOrUpdate(BdSolaUrlList bdSolaUrlList) {
		bdSolaUrlListDao.saveOrUpdate(bdSolaUrlList);
	}

	@Override
	public BdSolaUrlList getBdSolaUrlListById(String id) {
		BdSolaUrlList bdSolaUrlList = bdSolaUrlListDao.get(BdSolaUrlList.class,
				id);
		return bdSolaUrlList;
	}

	/**
	 * @param page
	 *            JSoup页面解析元素
	 * @param fileName
	 *            当前BdSolaUrlDetail缓存文件存放路径
	 * @param id
	 *            BdSolaUrlDetail所属BdSolaUrlList的id
	 */
	@Override
	public List<BdSolaUrlDetail> crawlAndsaveUrlDetail(Page page,
			String fileName, String id) {
		Document doc = null;
		File cachedFile = new File(fileName);
		if (crawlUseCached) {
			if (cachedFile.exists() && cachedFile.isFile()) {
				doc = CommonParser.html2Document(cachedFile);
			} else {
				doc = page.getDoc();
			}
		} else {
			doc = page.getDoc();
		}

		Elements childrenDocs = doc.select("ul.flist");
		Element flistUlElem = null;
		ListIterator<Element> listIterator = childrenDocs.listIterator();
		while (listIterator.hasNext()) {
			flistUlElem = listIterator.next();
			int index = listIterator.nextIndex();
			logger.info("<<<<<<<<<<<<<<<<<<<<<  FlistUlElem "//
					+ index//
					+ " >>>>>>>>>>>>>>>>>>>>>>> ");
			logger.info(flistUlElem.toString());
		}

		Elements lisElem = flistUlElem.select("li");
		ListIterator<Element> liElemIterator = lisElem.listIterator();

		List<BdSolaUrlItem> itemList = new ArrayList<BdSolaUrlItem>();

		for (int in = 0; liElemIterator.hasNext(); in++) {
			Element nextLiElem = liElemIterator.next();
			BdSolaUrlItem item = new BdSolaUrlItem();

			List<Node> childNodes = nextLiElem.childNodes();

			for (Node node : childNodes) {
				String nodeName = node.nodeName();
				item.setLastCrawlTime(new Date());
				if ("p".equalsIgnoreCase(nodeName)) { // fileType
					String fileType = node.attr("class");
					item.setFileType(fileType);
				} else if ("a".equalsIgnoreCase(nodeName)) { // href +
																// title
					String title = node.attr("title");
					String href = node.attr("href");
					item.setHref(href); // /d/38006121.html
					item.setTitle(title);
					item.setId(href.substring(3, href.lastIndexOf("."))); // 38006121

				} else if ("span".equalsIgnoreCase(nodeName)) { // author
					List<Node> spanChildrenNodes = node.childNodes();
					Node spanANode = spanChildrenNodes.get(0);
					String author = spanANode.attr("title");
					item.setAuthor(author);
				} else if ("i".equalsIgnoreCase(nodeName)) { // time
					if (node.childNodeSize() > 0) {
						Node timeNode = node.childNode(0);
						item.setUploadTime(timeNode.toString());
					}
				}

			}

			itemList.add(item);

			// domain: http://www.bdsola.com
			// firstLevelUrl: /d/38006121.html
			// secondLevelUrl: /go.php?s_url=1kTu9Uq7
			try {
				for (BdSolaUrlItem it : itemList) {
					String firstLevelUrl = it.getDomain() + it.getHref();
					logger.debug(" >> firstLevelUrl is: " + firstLevelUrl);
					logger.debug(" >> Connect to firstLevelUrl...");
					Document secondaryDoc = Jsoup.connect(firstLevelUrl).get();
					if (secondaryDoc != null) {
						// css f-ext
						Elements divSpanElem = secondaryDoc
								.select(".f-ext span");
						int spanSize = divSpanElem.size();
						if (spanSize > 0) {
							Element lastSpanElem = divSpanElem.last();

							// lastSpanElem.getAllElements() -- 获取当前元素下的所有子孙元素
							Elements as = lastSpanElem.getElementsByTag("a");
							if (as.size() > 0) {
								Element targetElem = as.first();
								// ** 获取元素 ** //
								String sendcondaryHref = targetElem
										.attr("href");
								if (StringUtils.isNotBlank(sendcondaryHref)) {
									logger.info(" >> Current sencondary url: "
											+ sendcondaryHref);
									it.setSecondary(sendcondaryHref); // /go.php?s_url=1kTu9Uq7
									String yunId = sendcondaryHref
											.substring(sendcondaryHref
													.lastIndexOf("=" + 1));
									it.setYunShareId(yunId);
									item.setLastCrawlTime(new Date());
								} else {
									logger.warn(" >> Failed to parse sencondary url");
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// 实现BdSolaUrlItem到BdsolaUrlDetail的迁移转换
		List<BdSolaUrlDetail> bdSolaUrlDetails = new ArrayList<BdSolaUrlDetail>();
		int maxSize = itemList.size();
		int totalCrawlTimes = 0;
		for (int p = 0; p < maxSize; p++) {
			BdSolaUrlItem it = itemList.get(p);
			BdSolaUrlDetail detail = queryDB2DetailById(it.getId());
			if (detail == null) {
				detail = new BdSolaUrlDetail();
				detail.setId(it.getId());
				detail.setIsCrawled(true);
				detail.setUseCache(true);
				detail.setCrawlTimes(1);
				BdSolaUrlList urlList = getBdSolaUrlListById(id);
				detail.setBdSolaUrlList(urlList);
			} else {
				int crawlTimes = detail.getCrawlTimes();
				detail.setCrawlTimes(crawlTimes++);
			}

			totalCrawlTimes += detail.getCrawlTimes();

			detail.setFilePath(fileName);

			// -- 一次爬取直接获取
			detail.setFirstLevelUrl(it.getHref());
			detail.setFileType(it.getFileType());
			detail.setSharer(it.getAuthor());
			detail.setShareTitle(it.getTitle());
			Date shareTime = null;
			try {
				shareTime = format.parse(it.getUploadTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			detail.setShareTime(shareTime);

			// -- 设置从属的BdSolaUrlList的最近抓取时间
			if (p + 1 == maxSize) {
				detail.getBdSolaUrlList().setLastCrawlTime(new Date());
				int avgCrawlTimes = totalCrawlTimes / maxSize;
				detail.getBdSolaUrlList().setAvgCrawlTimes(avgCrawlTimes);
			}

			// -- 二次爬取解析
			detail.setSecondLevelUrl(it.getSecondary());
			detail.setYunShareId(it.getYunShareId());

			bdSolaUrlDetails.add(detail);
			// 保存记录
			bdSolaUrlDetailDao.saveOrUpdate(detail);
		}

		return bdSolaUrlDetails;
	}

	@Override
	public BdSolaUrlDetail queryDB2DetailById(String id) {
		BdSolaUrlDetail bdSolaUrlDetail = bdSolaUrlDetailDao.get(
				BdSolaUrlDetail.class, id);
		return bdSolaUrlDetail;
	}

	@Override
	public List<BdSolaUrlList> fetchTopNListByTime(int topN) {
		List<BdSolaUrlList> bdSolaUrlLists = bdSolaUrlListDao
				.getTopNListByTime(topN);
		return bdSolaUrlLists;
	}

}
