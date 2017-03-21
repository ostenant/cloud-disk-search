package org.ostenant.cloud.search.bdsola.dao.mybatis;

import java.util.List;

import org.ostenant.cloud.search.bdsola.pojo.mybatis.BdSolaUrlList;

public interface BdSolaUrlListMapper {

	int deleteByPrimaryKey(String id);

	int insert(BdSolaUrlList record);

	int insertSelective(BdSolaUrlList record);

	BdSolaUrlList selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(BdSolaUrlList record);

	int updateByPrimaryKey(BdSolaUrlList record);

	List<String> queryUrlListIds();
}