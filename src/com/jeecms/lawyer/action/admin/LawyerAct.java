 package com.jeecms.lawyer.action.admin;
 
 import static com.jeecms.common.page.SimplePage.cpn;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.entity.Area;
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.core.manager.CmsConfigItemMng;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.manager.LawyerMng;
 
 
 
 @Controller
 public class LawyerAct
 {
   private static final Logger log = LoggerFactory.getLogger(LawyerAct.class);
   
   @RequiresPermissions({"lawyer:v_list"})
   @RequestMapping({"/lawyer/v_list.do"})
	public String list(String queryUsername, String queryEmail,
			Integer queryGroupId, Boolean queryDisabled, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(queryUsername, queryEmail,
				null, 3, queryDisabled, false, null, cpn(pageNo),
				CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);

		model.addAttribute("queryUsername", queryUsername);
		model.addAttribute("queryEmail", queryEmail);
		model.addAttribute("queryGroupId", queryGroupId);
		model.addAttribute("queryDisabled", queryDisabled);

     return "lawyer/list"; }
   

   @RequiresPermissions({"lawyer:v_add"})
   @RequestMapping({"/lawyer/v_add.do"})
   public String add(ModelMap model,HttpServletRequest request) {
		CmsSite site=CmsUtils.getSite(request);
		List<CmsGroup> groupList = cmsGroupMng.getList();
		List<CmsConfigItem>registerItems=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<Area> areaList = areaMng.getList(0);
		model.addAttribute("registerItems", registerItems);
		model.addAttribute("groupList", groupList);	   
		model.addAttribute("areaList", areaList);
	   return "lawyer/add"; }
   
 
   @RequiresPermissions({"test:v_edit"})
   @RequestMapping({"/test/v_edit.do"})
   public String edit(Integer id, Integer pageNo, HttpServletRequest request, ModelMap model) {
     WebErrors errors = validateEdit(id, request);
     if (errors.hasErrors()) {
       return errors.showErrorPage(model);
     }
     model.addAttribute("test", this.manager.findById(id));
     model.addAttribute("pageNo", pageNo);
     return "test/edit";
   }
   
   @RequiresPermissions({"lawyer:o_save"})
   @RequestMapping({"/lawyer/o_save.do"})
   public String save(CmsUser bean, CmsUserExt ext,Lawyer lawyer, String username,
			String email, String password, Integer groupId,Integer grain,Integer provinceId,Integer cityId,Integer regionId, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		//groupId=3;
		String ip = RequestUtils.getIpAddr(request);
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		bean = manager.registerMember(username, email, password, ip, groupId, grain, provinceId, cityId, regionId, false, ext, lawyer, attrs);
		log.info("save CmsMember id={}", bean.getId());
		cmsLogMng.operating(request, "cmsMember.log.save", "id=" + bean.getId()
				+ ";username=" + bean.getUsername());
		return "redirect:v_list.do";
   }
   
/*   @RequiresPermissions({"test:o_update"})
   @RequestMapping({"/test/o_update.do"})
   public String update(Lawyer bean, Integer pageNo, HttpServletRequest request, ModelMap model)
   {
     WebErrors errors = validateUpdate(bean.getId(), request);
     if (errors.hasErrors()) {
       return errors.showErrorPage(model);
     }
     bean = this.manager.update(bean);
     log.info("update CmsTest id={}.", bean.getId());
     return list(pageNo, request, model);
   }*/
   
/*   @RequiresPermissions({"test:o_delete"})
   @RequestMapping({"/test/o_delete.do"})
   public String delete(Integer[] ids, Integer pageNo, HttpServletRequest request, ModelMap model)
   {
     WebErrors errors = validateDelete(ids, request);
     if (errors.hasErrors()) {
       return errors.showErrorPage(model);
     }
     Lawyer[] beans = this.manager.deleteByIds(ids);
     Lawyer[] arrayOfCmsTest1; int j = (arrayOfCmsTest1 = beans).length; for (int i = 0; i < j; i++) { Lawyer bean = arrayOfCmsTest1[i];
       log.info("delete CmsTest id={}", bean.getId());
     }
     return list(pageNo, request, model);
   }*/
   
	private WebErrors validateSave(CmsUser bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}
   
   private WebErrors validateEdit(Integer id, HttpServletRequest request) {
     WebErrors errors = WebErrors.create(request);
     CmsSite site = CmsUtils.getSite(request);
     if (vldExist(id, site.getId(), errors)) {
       return errors;
     }
     return errors;
   }
   
   private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
     WebErrors errors = WebErrors.create(request);
     CmsSite site = CmsUtils.getSite(request);
     if (vldExist(id, site.getId(), errors)) {
       return errors;
     }
     return errors;
   }
   
   private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
     WebErrors errors = WebErrors.create(request);
     CmsSite site = CmsUtils.getSite(request);
     if (errors.ifEmpty(ids, "ids"))
       return errors;
     Integer[] arrayOfInteger;
     int j = (arrayOfInteger = ids).length; for (int i = 0; i < j; i++) { Integer id = arrayOfInteger[i];
       vldExist(id, site.getId(), errors);
     }
     return errors;
   }
   
   private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
     if (errors.ifNull(id, "id")) {
       return true;
     }
     Lawyer entity = this.manager.findById(id);
     if (errors.ifNotExist(entity, Lawyer.class, id)) {
       return true;
     }
     return false;
   }
	@Autowired
	private CmsGroupMng cmsGroupMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private AreaMng areaMng;
	@Autowired
	 private LawyerMng manager;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
 }
