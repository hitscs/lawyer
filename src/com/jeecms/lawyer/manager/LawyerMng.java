package com.jeecms.lawyer.manager;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.lawyer.entity.Lawyer;

public abstract interface LawyerMng
{
  public abstract Pagination getPage(String username, String email, Integer siteId,
			Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize);
	public CmsUser registerMember(String username, String email,
			String password, String ip, Integer groupId,Integer grain, boolean disabled,CmsUserExt userExt,Lawyer lawyer,Map<String,String>attr);
	
	public CmsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, boolean disabled,CmsUserExt userExt,Map<String,String>attr, Boolean activation , EmailSender sender, MessageTemplate msgTpl)throws UnsupportedEncodingException, MessagingException ;
 
  public abstract List<Lawyer> getList(int paramInt, boolean paramBoolean);
  
  public abstract Lawyer findById(Integer paramInteger);
  
/*  public abstract Lawyer save(Lawyer paramCmsTest);*/
  
  public abstract Lawyer update(Lawyer paramCmsTest);
  
  public abstract Lawyer deleteById(Integer paramInteger);
  
  public abstract Lawyer[] deleteByIds(Integer[] paramArrayOfInteger);
}

