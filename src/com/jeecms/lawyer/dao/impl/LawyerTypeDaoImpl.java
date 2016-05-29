package com.jeecms.lawyer.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.lawyer.dao.LawyerTypeDao;
import com.jeecms.lawyer.entity.LawyerType;

@Repository
public class LawyerTypeDaoImpl extends HibernateBaseDao<LawyerType, Integer>implements LawyerTypeDao {
	@SuppressWarnings("unchecked")
	public List<LawyerType> getList() {
		String hql = "from LawyerType bean ";
		hql += "where 1=1 order by bean.id asc";
		return find(hql);
	}

	/**
	 * 根据父ID获取子ID
	 */
	@SuppressWarnings("unchecked")
	public List<LawyerType> getList(Integer pid) {
		Finder f = Finder.create("select bean from LawyerType bean ");
		f.append(" where pid=:pid ");
		f.setParam("pid", pid);
		f.append(" order by bean.id asc");
		return find(f);

	}

	public LawyerType findById(Integer id) {
		LawyerType entity = get(id);
		return entity;
	}

	@Override
	protected Class<LawyerType> getEntityClass() {
		return LawyerType.class;
	}
}