package com.jeecms.lawyer.action.front;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
	public String lawyerList(String realname, String professionalField, String goodAtField, String professionalFieldF, String goodAtFieldF,Integer groupId, Integer provinceId, Integer cityId, Integer regionId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String professionalFieldS="";//用来查询的变量
		String goodAtFieldS="";//用来查询的变量
		String professionalFieldF1="";//过渡变量
		String goodAtFieldF1="";//过渡变量
		//一级为空二级不会空的话，通过二级查找到一级
		if(StringUtils.isBlank(professionalFieldF)&&!StringUtils.isBlank(professionalField)){
			professionalFieldF=lawyerTypeManager.findById(Integer.parseInt(professionalField)).getpId().toString();
			
		}
		if(StringUtils.isBlank(goodAtFieldF)&&!StringUtils.isBlank(goodAtField)){
			goodAtFieldF=lawyerTypeManager.findById(Integer.parseInt(goodAtField)).getpId().toString();
			
		}		
		
		
		//goodAtFieldF与professionalFieldF是在筛选的时候作为一级分类的参数。查询还是靠二级分类
		if (null != professionalFieldF && !professionalFieldF.trim().equals(""))
			professionalFieldF1 = "," + professionalFieldF + ",";
		if (null != goodAtFieldF && !goodAtFieldF.trim().equals(""))
			goodAtFieldF1 = "," + goodAtFieldF + ",";
		if (null != professionalField && !professionalField.trim().equals(""))
			professionalFieldS = "," + professionalField + ",";
		else professionalFieldS=professionalFieldF1;
		if (null != goodAtField && !goodAtField.trim().equals(""))
			goodAtFieldS = "," + goodAtField + ",";
		else goodAtFieldS=goodAtFieldF1;
		
		Pagination pagination = lawyerMng.getPageByCondition(site.getId(), provinceId, cityId, regionId, realname, professionalFieldS, goodAtFieldS, 3, false, false, null, 1, 10);
		//把取得的律师专业和特长由id转换为名称
		if(pagination.getTotalCount()>0){
			List<Object[]> list=new ArrayList<Object[]>();
			for(int i=0;i<pagination.getTotalCount();i++){
				Object[] obj=(Object[]) pagination.getList().get(i);
				Lawyer lawyer =	(Lawyer) obj[2];
				String professionalFieldString=lawyer.getProfessionalField();
				//String goodAtFieldString=lawyer.getGoodAtField();
				if(professionalFieldString!=null&&!professionalFieldString.equals("")){
					professionalFieldString=professionalFieldString.substring(1, professionalFieldString.length()-1);
					String[] pArray =professionalFieldString.split(",");
					String pString="";
					for(int j=0;j<pArray.length;j++){
						LawyerType lawyerType = lawyerTypeManager.findById(Integer.parseInt(pArray[j]));
						pString=pString+lawyerType.getName()+",";
						
					}
					professionalFieldString=pString.substring(0, pString.length()-1);
				}
				lawyer.setProfessionalField(professionalFieldString);
				obj[2]=lawyer;
				list.add(obj);
				
			}
			
			pagination.setList(list);
		}
		
		
		List<Area> provinceList = areaManager.getList(0);
		List<Area> cityList = null;
		if(null!=provinceId){
			cityList = areaManager.getList(provinceId);
		}
		
		List<LawyerType> lawyerTypeList = lawyerTypeManager.getList();

		model.addAttribute("provinceList", provinceList);
		model.addAttribute("cityList", cityList);
		model.addAttribute("lawyerTypeList", lawyerTypeList);
		model.addAttribute("provinceId", provinceId);
		model.addAttribute("cityId", cityId);
		model.addAttribute("regionId", regionId);
		model.addAttribute("professionalField", professionalField);
		model.addAttribute("goodAtField", goodAtField);
		model.addAttribute("professionalFieldF", professionalFieldF);
		model.addAttribute("goodAtFieldF", goodAtFieldF);
		model.addAttribute("pagination", pagination);
		model.addAttribute("currentMenu", "lawyerIndex");
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", LAWYERLIST);
	}

	@RequestMapping(value = { "/lawyer/lvsuoList.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String lvsuoList(String realname, String professionalField, String goodAtField, Integer groupId, Integer provinceId, Integer cityId, Integer regionId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);

		Pagination pagination = lawyerMng.getPageByCondition(site.getId(), provinceId, cityId, regionId, realname, professionalField, goodAtField, groupId, false, false, null, 1, 10);
		model.addAttribute("pagination", pagination);
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
