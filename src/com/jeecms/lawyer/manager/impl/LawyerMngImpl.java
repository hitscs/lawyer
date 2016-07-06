package com.jeecms.lawyer.manager.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsUserExtMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.lawyer.dao.LawyerDao;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.manager.LawyerMng;

@Service
@Transactional
public class LawyerMngImpl implements LawyerMng {
	private LawyerDao lawyerDao;
	


	@Transactional(readOnly = true)
	public Pagination getPage(String username, String email, Integer siteId, Integer groupId, Boolean disabled, Boolean admin, Integer rank, int pageNo, int pageSize) {
		Pagination page = lawyerDao.getPage(username, email, siteId, groupId, disabled, admin, rank, pageNo, pageSize);
		return page;
	}
	  public Pagination getPageByCondition(Integer siteId,
				Integer provinceId,Integer cityId,Integer regionId, String realname,String professionalField,String goodAtField, Integer groupId, Boolean disabled, Boolean admin, Integer rank,
				int pageNo, int pageSize){
			Pagination page = lawyerDao.getPageByCondition(siteId, provinceId, cityId, regionId, realname, professionalField, goodAtField, groupId, disabled, admin, rank, pageNo, pageSize);
			return page;
		  
		  
	  }

	public CmsUser registerMember(String username, String email, String password, String ip, Integer groupId, Integer grain,Integer provinceId,Integer cityId,Integer regionId, boolean disabled, CmsUserExt userExt, Lawyer lawyer,Map<String, String> attr) {
		CmsUser user = new CmsUser();
		if(provinceId!=null) userExt.setProvince(areaMng.findById(provinceId));
		if(cityId!=null) userExt.setCity(areaMng.findById(cityId));
		if(regionId!=null) userExt.setRegion(areaMng.findById(regionId));
		user=cmsUserMng.registerMember(username, email, password, ip, groupId, grain, disabled, userExt, attr);
		lawyerDao.save(lawyer,user);
		
		return user;
	}
	
	public CmsUser updateMember(Integer id, String email, String password,
			Boolean isDisabled,Integer provinceId,Integer cityId,Integer regionId, CmsUserExt ext,Lawyer lawyer, Integer groupId,Integer grain,Map<String,String>attr) {
		CmsUser entity = cmsUserMng.findById(id);
		if (!StringUtils.isBlank(email)) {
			entity.setEmail(email);
		}
		if (isDisabled != null) {
			entity.setDisabled(isDisabled);
		}
		if (groupId != null) {
			entity.setGroup(cmsGroupMng.findById(groupId));
		}		
		// 更新属性表
		if (attr != null) {
			Map<String, String> attrOrig = entity.getAttr();
			attrOrig.clear();
			attrOrig.putAll(attr);
		}
		if(provinceId!=null){
			ext.setProvince(areaMng.findById(provinceId));
		}
		if(cityId!=null){
			ext.setCity(areaMng.findById(cityId));
		}
		if(regionId!=null){
			ext.setRegion(areaMng.findById(regionId));
		}
		ext=cmsUserExtMng.update(ext, entity);
		unifiedUserMng.update(id, password, email);
		
		Lawyer l=lawyerDao.findById(id);
		
		if (l == null) {
			l = lawyerDao.save(lawyer,entity);

		} else {
			Updater<Lawyer> updater = new Updater<Lawyer>(lawyer);
			

			l = lawyerDao.updateByUpdater(updater);

		}
		entity.getUserExtSet().add(ext);
		entity.getLawyerSet().add(l);
		
		return entity;
	}

/*	public CmsUser registerMember(String username, String email, String password, String ip, Integer groupId, boolean disabled, CmsUserExt userExt, Map<String, String> attr, Boolean activation,
			EmailSender sender, MessageTemplate msgTpl) throws UnsupportedEncodingException, MessagingException {
		UnifiedUser unifiedUser = unifiedUserMng.save(username, email, password, ip, activation, sender, msgTpl);
		CmsUser user = new CmsUser();
		user.forMember(unifiedUser);
		user.setAttr(attr);
		user.setDisabled(disabled);
		CmsGroup group = null;
		if (groupId != null) {
			group = cmsGroupMng.findById(groupId);
		} else {
			group = cmsGroupMng.getRegDef();
		}
		if (group == null) {
			throw new RuntimeException("register default member group not found!");
		}
		user.setGroup(group);
		user.init();
		cmsUserDao.save(user);
		cmsUserExtMng.save(userExt, user);
		return user;
	}*/

	@Transactional(readOnly = true)
	public java.util.List<Lawyer> getList(int count, boolean cache) {
		return this.lawyerDao.getList(count, cache);
	}

	@Transactional(readOnly = true)
	public Lawyer findById(Integer id) {
		Lawyer entity = this.lawyerDao.findById(id);
		return entity;
	}

/*	public Lawyer save(Lawyer bean) {
		this.lawyerDao.save(bean);
		return bean;
	}*/

	public Lawyer update(Lawyer bean) {
		Updater<Lawyer> updater = new Updater(bean);
		bean = this.lawyerDao.updateByUpdater(updater);
		return bean;
	}

	public Lawyer deleteById(Integer id) {
		Lawyer bean = this.lawyerDao.deleteById(id);
		return bean;
	}

	public Lawyer[] deleteByIds(Integer[] ids) {
		Lawyer[] beans = new Lawyer[ids.length];
		int i = 0;
		for (int len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private CmsUserMng cmsUserMng;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private CmsUserExtMng cmsUserExtMng;
	@Autowired
	private AreaMng areaMng;
	@Autowired
	private CmsGroupMng cmsGroupMng;

	@Autowired
	public void setCmsUserMng(CmsUserMng cmsUserMng) {
		this.cmsUserMng = cmsUserMng;
	}



	@Autowired
	public void setDao(LawyerDao lawyerDao) {
		this.lawyerDao = lawyerDao;
	}
	
}
