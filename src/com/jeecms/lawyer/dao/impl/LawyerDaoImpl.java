package com.jeecms.lawyer.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.lawyer.entity.Lawyer;

@Repository
public class LawyerDaoImpl extends HibernateBaseDao<Lawyer, Integer>implements com.jeecms.lawyer.dao.LawyerDao {
	public Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from CmsUser bean");
		if (siteId != null) {
			f.append(" join bean.userSites userSite");
			f.append(" where userSite.site.id=:siteId");
			f.setParam("siteId", siteId);
		} else {
			f.append(" where 1=1");
		}
		if (!StringUtils.isBlank(username)) {
			f.append(" and bean.username like :username");
			f.setParam("username", "%" + username + "%");
		}
		if (!StringUtils.isBlank(email)) {
			f.append(" and bean.email like :email");
			f.setParam("email", "%" + email + "%");
		}
		if (groupId != null) {
			f.append(" and bean.group.id=:groupId");
			f.setParam("groupId", groupId);
		}
		if (disabled != null) {
			f.append(" and bean.disabled=:disabled");
			f.setParam("disabled", disabled);
		}
		if (admin != null) {
			f.append(" and bean.admin=:admin");
			f.setParam("admin", admin);
		}
		if (rank != null) {
			f.append(" and bean.rank<=:rank");
			f.setParam("rank", rank);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}
	public Pagination getPageByCondition(Integer siteId,
			Integer provinceId,Integer cityId,Integer regionId, String realname,String professionalField,String goodAtField, Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize) {
		Finder f = Finder.create("select * from Lawyer bean left join CmsUserExt userExtSet with bean.id=userExtSet.id ");

			f.append(" where 1=1");

		if (!StringUtils.isBlank(realname)) {
			f.append(" and userExtSet.realname like :realname");
			f.setParam("realname", "%" + realname + "%");
		}
		if (!StringUtils.isBlank(professionalField)) {
			f.append(" and bean.professionalField like :professionalField");
			f.setParam("professionalField", "%" + professionalField + "%");
		}
		if (!StringUtils.isBlank(goodAtField)) {
			f.append(" and bean.goodAtField like :goodAtField");
			f.setParam("goodAtField", "%" + goodAtField + "%");
		}
		if (null!=provinceId) {
			f.append(" and userExtSet.province like :provinceId");
			f.setParam("provinceId", "%" + provinceId + "%");
		}
		if (null!=cityId) {
			f.append(" and userExtSet.city like :cityId");
			f.setParam("cityId", "%" + cityId + "%");
		}
		if (null!=regionId) {
			f.append(" and userExtSet.region like :regionId");
			f.setParam("regionId", "%" + regionId + "%");
		}
		if (groupId != null) {
			f.append(" and bean.user.group.id=:groupId");
			f.setParam("groupId", groupId);
		}
		if (disabled != null) {
			f.append(" and bean.user.disabled=:disabled");
			f.setParam("disabled", disabled);
		}
		if (admin != null) {
			f.append(" and bean.user.admin=:admin");
			f.setParam("admin", admin);
		}
		if (rank != null) {
			f.append(" and bean.user.rank<=:rank");
			f.setParam("rank", rank);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}
	public List<Lawyer> getList(int count, boolean cache) {
		Finder finder = Finder.create("from Lawyer");
		finder.setCacheable(cache);
		finder.setMaxResults(count);
		return find(finder);
	}

	public Lawyer findById(Integer id) {
		Lawyer entity = (Lawyer) get(id);
		return entity;
	}

	public Lawyer save(Lawyer lawyer, CmsUser user) {

		lawyer.setUser(user);
		getSession().save(lawyer);
		return lawyer;
	}
	public Lawyer update(Lawyer lawyer, CmsUser user) {

		lawyer.setUser(user);
		getSession().update(lawyer);
		return lawyer;
	}
	public Lawyer deleteById(Integer id) {
		Lawyer entity = (Lawyer) super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	protected Class<Lawyer> getEntityClass() {
		return Lawyer.class;
	}
}
