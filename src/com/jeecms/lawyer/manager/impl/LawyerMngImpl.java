package com.jeecms.lawyer.manager.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.CmsUserDao;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsRoleMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.CmsUserExtMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.CmsUserSiteMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.lawyer.dao.LawyerDao;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.manager.LawyerMng;

@Service
@Transactional
public class LawyerMngImpl implements LawyerMng {
	private LawyerDao lawyerDao;
	
	private CmsUserDao cmsUserDao;

	@Transactional(readOnly = true)
	public Pagination getPage(String username, String email, Integer siteId, Integer groupId, Boolean disabled, Boolean admin, Integer rank, int pageNo, int pageSize) {
		Pagination page = lawyerDao.getPage(username, email, siteId, groupId, disabled, admin, rank, pageNo, pageSize);
		return page;
	}

	public CmsUser registerMember(String username, String email, String password, String ip, Integer groupId, Integer grain, boolean disabled, CmsUserExt userExt, Lawyer lawyer,Map<String, String> attr) {
		CmsUser user = new CmsUser();

		user=cmsUserMng.registerMember(username, email, password, ip, groupId, grain, disabled, userExt, attr);
		lawyerDao.save(lawyer,user);
		
		return user;
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
	private CmsUserSiteMng cmsUserSiteMng;
	private CmsSiteMng cmsSiteMng;
	private ChannelMng channelMng;
	private CmsRoleMng cmsRoleMng;
	private CmsGroupMng cmsGroupMng;
	private UnifiedUserMng unifiedUserMng;
	private CmsUserMng cmsUserMng;
	private CmsUserExtMng cmsUserExtMng;
	@Autowired
	private ContentMng contentMng;
	@Autowired
	public void setCmsUserSiteMng(CmsUserSiteMng cmsUserSiteMng) {
		this.cmsUserSiteMng = cmsUserSiteMng;
	}

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}

	@Autowired
	public void setChannelMng(ChannelMng channelMng) {
		this.channelMng = channelMng;
	}

	@Autowired
	public void setCmsRoleMng(CmsRoleMng cmsRoleMng) {
		this.cmsRoleMng = cmsRoleMng;
	}

	@Autowired
	public void setUnifiedUserMng(UnifiedUserMng unifiedUserMng) {
		this.unifiedUserMng = unifiedUserMng;
	}


	@Autowired
	public void setCmsUserMng(CmsUserMng cmsUserMng) {
		this.cmsUserMng = cmsUserMng;
	}

	@Autowired
	public void setCmsUserExtMng(CmsUserExtMng cmsUserExtMng) {
		this.cmsUserExtMng = cmsUserExtMng;
	}

	@Autowired
	public void setCmsGroupMng(CmsGroupMng cmsGroupMng) {
		this.cmsGroupMng = cmsGroupMng;
	}


	@Autowired
	public void setDao(LawyerDao lawyerDao) {
		this.lawyerDao = lawyerDao;
	}
	
}
