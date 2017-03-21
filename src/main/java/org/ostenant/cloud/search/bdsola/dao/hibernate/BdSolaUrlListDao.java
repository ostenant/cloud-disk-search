package org.ostenant.cloud.search.bdsola.dao.hibernate;

import java.util.List;

import org.ostenant.cloud.search.bdsola.pojo.hibernate.BdSolaUrlList;
import org.ostenant.cloud.search.common.base.BaseDao;

public interface BdSolaUrlListDao extends BaseDao<BdSolaUrlList> {

	public List<BdSolaUrlList> getTopNListByTime(int topN);

}
