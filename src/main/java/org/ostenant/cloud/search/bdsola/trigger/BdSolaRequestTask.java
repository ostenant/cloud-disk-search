package org.ostenant.cloud.search.bdsola.trigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.json.JSONObject;
import org.ostenant.cloud.search.bdsola.crawler.BdSolaCrawler;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlDetail;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;
import org.ostenant.cloud.search.bdsola.service.BdSolaService;
import org.ostenant.cloud.search.bdsola.service.BdSolaServiceImpl;
import org.ostenant.cloud.search.common.BdsolaConfig;
import org.ostenant.cloud.search.common.trigger.RequestTask;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
@SuppressWarnings("all")
public class BdSolaRequestTask extends RequestTask implements
		ServletContextAware {

	private HashMap<String, Object> bdsolaConfigMap;
	private JSONObject bdsolaConfigJSON;
	private ServletContext servletContext;
	private long interval;
	private boolean crawlUseCached;
	private int depth;

	@Resource
	private BdSolaCrawler bdSolaCrawler;

	private BdSolaServiceImpl bdSolaServiceImpl;

	/**
	 * 设置定时fetcher策略
	 * 
	 * 1).定时器定时执行handleTrigger方法 <br>
	 * 从bodolaserviceimpl中获取LinkhashSet中获取
	 * 抓取过程中根据数据库记录找到并删除符合（关键字）对应的缓存文件目录 <br>
	 * 2).设置优先级一： 从数据库中查询抓取日期最久的100条 重新抓取 <br>
	 * 3).设置优先级二： 从数据库中查询抓取次数最少的100条 重新抓取
	 * 
	 * 
	 */
	@Override
	public void handleTrigger() {
		bdSolaCrawler.doConfig(bdsolaConfigMap);
		try {
			int topN = 100;
			List<BdSolaUrlList> bdSolaUrlLists = bdSolaServiceImpl
					.fetchTopNListByTime(topN);
			//bdSolaServiceImpl.crawlAndsaveUrlDetail(page, fileName, id)
		} catch (Exception e) {
			e.printStackTrace();
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

}
