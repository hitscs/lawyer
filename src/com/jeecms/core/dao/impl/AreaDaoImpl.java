package com.jeecms.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.core.dao.AreaDao;
import com.jeecms.core.entity.Area;

@Repository
public class AreaDaoImpl extends HibernateBaseDao<Area, Integer>
		implements AreaDao {
	@SuppressWarnings("unchecked")
	public List<Area> getList() {
		String hql = "from Area bean ";
		hql+="where 1=1 order by bean.id asc";
		return find(hql);
	}
/**
 * 根据父ID获取子ID
 */
	@SuppressWarnings("unchecked")
	public List<Area> getList(Integer pid) {
		Finder f = Finder.create("select bean from Area bean ");
		f.append(" where pid=:pid ");
		f.setParam("pid", pid);
		f.append(" order by bean.id asc");
		return find(f);		
		
		
	}
	
	public Area findById(Integer id) {
		Area entity = get(id);
		return entity;
	}


	@Override
	protected Class<Area> getEntityClass() {
		return Area.class;
	}
}