package org.ostenant.cloud.search.bdsola.dao.hibernate.impl;


import java.util.ArrayList;
import java.util.List;

import org.ostenant.cloud.search.bdsola.dao.hibernate.BdSolaUrlListDao;
import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;
import org.ostenant.cloud.search.common.base.BaseDaoSupport;

public class BdSolaUrlListDaoImpl extends BaseDaoSupport<BdSolaUrlList> implements
		BdSolaUrlListDao {

	@Override
	public List<BdSolaUrlList> getTopNListByTime(int topN) {
		String hql = null;//"SELECT TOP ? FROM BdSolaUrlList ul WHERE ORDER BY ul.lastCrawlTime DESC";
		List<Object> params = new ArrayList<Object>();
		List<BdSolaUrlList> bdSolaUrlLists = queryByHQL(hql, params);
		return bdSolaUrlLists;
	}
	
}
