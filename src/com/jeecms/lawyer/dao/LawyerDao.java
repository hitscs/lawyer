package com.jeecms.lawyer.dao;

import java.util.List;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.lawyer.entity.Lawyer;

public abstract interface LawyerDao
{
  public abstract Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize);
  
  public abstract List<Lawyer> getList(int paramInt, boolean paramBoolean);
  
  public abstract Lawyer findById(Integer paramInteger);
  
  public abstract Lawyer save(Lawyer lawyer, CmsUser user);
  
  public abstract Lawyer updateByUpdater(Updater<Lawyer> paramUpdater);
  
  public abstract Lawyer deleteById(Integer paramInteger);
}
