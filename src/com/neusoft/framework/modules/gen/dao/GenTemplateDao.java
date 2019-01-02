/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.neusoft.framework.modules.gen.dao;

import com.neusoft.framework.common.persistence.CrudDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.framework.modules.gen.entity.GenTemplate;

/**
 * 代码模板DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTemplateDao extends CrudDao<GenTemplate> {
	
}
