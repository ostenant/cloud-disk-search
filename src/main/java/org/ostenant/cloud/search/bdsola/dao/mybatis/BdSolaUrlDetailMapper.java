package org.ostenant.cloud.search.bdsola.dao.mybatis;

import org.ostenant.cloud.search.bdsola.pojo.mybatis.BdSolaUrlDetail;

public interface BdSolaUrlDetailMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(BdSolaUrlDetail record);

    int insertSelective(BdSolaUrlDetail record);

    BdSolaUrlDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BdSolaUrlDetail record);

    int updateByPrimaryKey(BdSolaUrlDetail record);
}