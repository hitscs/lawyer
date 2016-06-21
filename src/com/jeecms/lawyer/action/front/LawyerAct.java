package com.jeecms.lawyer.action.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.Area;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.entity.LawyerType;
import com.jeecms.lawyer.manager.LawyerMng;
import com.jeecms.lawyer.manager.LawyerTypeMng;

import net.sf.json.JSONArray;

@Controller
public class LawyerAct {
	public static final String INDEX = "tpl.lawyerIndex";
	public static final String DETAIL = "tpl.lawyerDetail";
	public static final String LAWYERLIST = "tpl.lawyerList";
	public static final String LVSUOLIST = "tpl.lvsuoList";
	@Autowired
	private LawyerMng lawyerMng;
	@Autowired
	private AreaMng areaManager;
	@Autowired
	private LawyerTypeMng lawyerTypeManager;
	@RequestMapping(value = { "/lawyer/index.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);

		List<Area> areaList = areaManager.getList();
		List<LawyerType> lawyerTypeList = lawyerTypeManager.getList();
		
		String areaListJson = JSONArray.fromObject(areaList).toString();
		model.addAttribute("areaListJson", areaListJson);
		model.addAttribute("areaList", areaList);
		model.addAttribute("lawyerTypeList", lawyerTypeList);
		model.addAttribute("currentMenu", "lawyerIndex");
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", INDEX);
	}

	@RequestMapping(value = { "/lawyer/detail.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String detail(Integer id, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Lawyer lawyer = null;
		if (id != null) {
			lawyer = this.lawyerMng.findById(id);
		}
		if ((id == null) || (lawyer == null)) {
			WebErrors errors = WebErrors.create(request);
			errors.addErrorCode("not fount test");
			FrontUtils.showError(request, response, model, errors);
		}
		model.addAttribute("lawyer", lawyer);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", "tpl.lawyerDetail");
	}
	@RequestMapping(value = { "/lawyer/lawyerList.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String lawyerList(String realname,String professionalField,String goodAtField, Integer groupId,Integer provinceId,Integer cityId,Integer regionId,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);

		Pagination pagination = lawyerMng.getPageByCondition(site.getId(), provinceId, cityId, regionId, realname, professionalField, goodAtField, groupId, false, false, null, 1, 10);
		model.addAttribute("pagination", pagination);
		model.addAttribute("currentMenu", "lawyerIndex");
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", LAWYERLIST);
	}
	@RequestMapping(value = { "/lawyer/lvsuoList.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String lvsuoList(CmsUser bean, CmsUserExt ext,Lawyer lawyer, Integer groupId,Integer provinceId_lvsuo,Integer cityId_lvsuo,Integer regionId_lvsuo, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);

		model.addAttribute("currentMenu", "lawyerIndex");
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", LVSUOLIST);
	}	
	@RequestMapping(value = "/lawyer/area.jspx", method = RequestMethod.POST)
	public String getArea(Integer pid, HttpServletRequest request, HttpServletResponse response) {
		if (null == pid)
			pid = 0;
		List<Area> list = areaManager.getList(pid);

		String json = JSONArray.fromObject(list).toString();
		ResponseUtils.renderJson(response, json);

		return null;
	}	
}
