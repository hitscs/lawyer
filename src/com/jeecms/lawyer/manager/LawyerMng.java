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
  public abstract Pagination getPageByCondition(Integer siteId,
			Integer provinceId,Integer cityId,Integer regionId, String realname,String professionalField,String goodAtField, Integer groupId, Boolean disabled, Boolean admin, Integer rank,
			int pageNo, int pageSize); 
  /**
   * 通过回复数量获取律师
   * @param siteId
   * @param disabled
   * @param pageNo
   * @param pageSize
   * @return
   */
  public abstract List getListByComment(Integer siteId, Boolean disabled,int pageNo, int pageSize);
  /**
   * 通过回复过的文章数量获取律师
   * @param siteId
   * @param disabled
   * @param pageNo
   * @param pageSize
   * @return
   */
  public abstract List getListByComment();
	public CmsUser registerMember(String username, String email,
			String password, String ip, Integer groupId,Integer grain,Integer provinceId,Integer cityId,Integer regionId, boolean disabled,CmsUserExt userExt,Lawyer lawyer,Map<String,String>attr);
	public CmsUser registerMember(String username, String email, String password, String ip, Integer groupId, Integer grain,Integer provinceId,Integer cityId,Integer regionId, boolean disabled, CmsUserExt userExt, Lawyer lawyer,Map<String, String> attr,
			Boolean activation, EmailSender sender, MessageTemplate msgTpl)throws UnsupportedEncodingException, MessagingException ;
	public CmsUser updateMember(Integer id, String email, String password,
			Boolean isDisabled,Integer provinceId,Integer cityId,Integer regionId, CmsUserExt ext,Lawyer lawyer, Integer groupId,Integer grain,Map<String,String>attr) ;

	public Lawyer update(Lawyer lawyer, CmsUser user);
/*	public CmsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, boolean disabled,CmsUserExt userExt,Map<String,String>attr, Boolean activation , EmailSender sender, MessageTemplate msgTpl)throws UnsupportedEncodingException, MessagingException ;
*/ 
  public abstract List<Lawyer> getList(int paramInt, boolean paramBoolean);
  
  public abstract Lawyer findById(Integer paramInteger);
  
/*  public abstract Lawyer save(Lawyer paramCmsTest);*/
  
  public abstract Lawyer update(Lawyer paramCmsTest);
  
  public abstract Lawyer deleteById(Integer paramInteger);
  
  public abstract Lawyer[] deleteByIds(Integer[] paramArrayOfInteger);
}

