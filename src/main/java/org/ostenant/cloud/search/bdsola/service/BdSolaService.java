package org.ostenant.cloud.search.bdsola.service;

import java.util.HashMap;
import java.util.List;

import org.ostenant.cloud.search.bdsola.crawler.BdSolaCrawler;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlDetail;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;

import cn.edu.hfut.dmic.webcollector.model.Page;

public interface BdSolaService {

	/**
	 * 查询BdSolaUrlList的id集合
	 * @return
	 */
	public List<String> urlListIds();

	/**
	 * 查询数据库完成BdsolaUrlList初始化和缓存设置
	 * @param url
	 * @param bdsolaConfigMap
	 * @param firstLevelPlaceHolder
	 * @param matchUrlListIds
	 * @return
	 */
	public BdSolaUrlList queryDBForCrawler(String url,
			HashMap<String, Object> bdsolaConfigMap,
			String firstLevelPlaceHolder, List<String> matchUrlListIds);
	
	/**
	 * 实现定时抓取爬虫数据
	 * @param bdSolaCrawler
	 */
	public void scheduleCrawler(BdSolaCrawler bdSolaCrawler);

	/**
	 * 保存或者更新bdSolaUrlList
	 * @param bdSolaUrlList
	 */
	public void saveOrUpdate(BdSolaUrlList bdSolaUrlList);

	/**
	 * 通过id查询BdSolaUrlList
	 * @param id
	 * @return
	 */
	public BdSolaUrlList getBdSolaUrlListById(String id);

	/**
	 * 爬取解析二级页面 保存BdSolaUrlDetail
	 * @param page
	 * @param fileName
	 * @param id
	 * @return
	 */
	public List<BdSolaUrlDetail> crawlAndsaveUrlDetail(Page page, String fileName,
			String id);
	
	/**
	 * 查询指定详情
	 * @param id
	 * @return
	 */
	public BdSolaUrlDetail queryDB2DetailById(String id);
	
	/**
	 * 根据时间查询TopN条BdSolaUrlList
	 * @param topN
	 * @return
	 */
	public List<BdSolaUrlList> fetchTopNListByTime(int topN);

}
