package com.jeecms.lawyer.action.admin;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.entity.LawyerType;
import com.jeecms.lawyer.manager.LawyerMng;
import com.jeecms.lawyer.manager.LawyerTypeMng;

import net.sf.json.JSONArray;

@Controller
public class LawyerAct {
	private static final Logger log = LoggerFactory.getLogger(LawyerAct.class);

	@RequiresPermissions({ "lawyer:v_list" })
	@RequestMapping({ "/lawyer/v_list.do" })
	public String list(String queryUsername, String queryEmail, Integer queryGroupId, Boolean queryDisabled, Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(queryUsername, queryEmail, null, 3, queryDisabled, false, null, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);

		model.addAttribute("queryUsername", queryUsername);
		model.addAttribute("queryEmail", queryEmail);
		model.addAttribute("queryGroupId", queryGroupId);
		model.addAttribute("queryDisabled", queryDisabled);

		return "lawyer/list";
	}

	@RequiresPermissions({ "lawyer:v_add" })
	@RequestMapping({ "/lawyer/v_add.do" })
	public String add(ModelMap model, HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		List<CmsGroup> groupList = cmsGroupMng.getList();
		List<CmsConfigItem> registerItems = cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<Area> areaList = areaMng.getList(0);
		List<LawyerType> list = lawyerTypeManager.getList();

		String json = JSONArray.fromObject(list).toString();
		model.addAttribute("registerItems", registerItems);
		model.addAttribute("groupList", groupList);
		model.addAttribute("areaList", areaList);
		model.addAttribute("allLawyerType", json);
		return "lawyer/add";
	}

	@RequiresPermissions({ "lawyer:v_edit" })
	@RequestMapping({ "/lawyer/v_edit.do" })
	public String edit(Integer id, Integer queryGroupId, Boolean queryDisabled, HttpServletRequest request, ModelMap model) {
		String queryUsername = RequestUtils.getQueryParam(request, "queryUsername");
		String queryEmail = RequestUtils.getQueryParam(request, "queryEmail");
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsUser user = cmsUserMng.findById(id);
		List<CmsGroup> groupList = cmsGroupMng.getList();
		List<CmsConfigItem> registerItems = cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<Area> areaList = areaMng.getList(0);
		List<LawyerType> list = lawyerTypeManager.getList();

		Lawyer lawyer = manager.findById(id);
		String goodAtField = "";
		String professionalField = "";

		if (null != lawyer && StringUtils.isNotBlank(lawyer.getGoodAtField())) {
			goodAtField = lawyer.getGoodAtField().substring(1, lawyer.getGoodAtField().length() - 1);
		}

		if (null != lawyer && StringUtils.isNotBlank(lawyer.getProfessionalField())) {
			professionalField = lawyer.getProfessionalField().substring(1, lawyer.getProfessionalField().length() - 1);
		}
		String[] goodAtFieldArray = goodAtField.split(",");
		String[] professionalFieldArray = professionalField.split(",");
		List gList = new ArrayList();
		List pList = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			Map map = new HashMap();
			map.put("id", list.get(i).getId());
			map.put("pId", list.get(i).getpId());
			map.put("name", list.get(i).getName());
			map.put("level", list.get(i).getLevel());
			for (int j = 0; j < goodAtFieldArray.length; j++) {

				if (goodAtFieldArray[j].equals(list.get(i).getId().toString())) {
					map.put("checked", true);
				}
			}

			gList.add(map);

		}

		for (int i = 0; i < list.size(); i++) {
			Map map = new HashMap();
			map.put("id", list.get(i).getId());
			map.put("pId", list.get(i).getpId());
			map.put("name", list.get(i).getName());
			map.put("level", list.get(i).getLevel());
			for (int j = 0; j < professionalFieldArray.length; j++) {

				if (professionalFieldArray[j].equals(list.get(i).getId().toString())) {
					map.put("checked", true);
				}
			}
			pList.add(map);

		}

		String gJson = JSONArray.fromObject(gList).toString();
		String pJson = JSONArray.fromObject(pList).toString();

		List<String> userAttrValues = new ArrayList<String>();
		for (CmsConfigItem item : registerItems) {
			userAttrValues.add(user.getAttr().get(item.getField()));
		}

		model.addAttribute("queryUsername", queryUsername);
		model.addAttribute("queryEmail", queryEmail);
		model.addAttribute("queryGroupId", queryGroupId);
		model.addAttribute("queryDisabled", queryDisabled);
		model.addAttribute("groupList", groupList);
		model.addAttribute("cmsMember", user);
		model.addAttribute("registerItems", registerItems);
		model.addAttribute("userAttrValues", userAttrValues);
		model.addAttribute("areaList", areaList);
		model.addAttribute("gLawyerType", gJson);
		model.addAttribute("pLawyerType", pJson);
		model.addAttribute("lawyer", lawyer);
		return "lawyer/edit";
	}

	@RequiresPermissions({ "lawyer:o_save" })
	@RequestMapping({ "/lawyer/o_save.do" })
	public String save(CmsUser bean, CmsUserExt ext, Lawyer lawyer, String username, String email, String password, Integer groupId, Integer grain, Integer provinceId, Integer cityId,
			Integer regionId, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		// groupId=3;
		String ip = RequestUtils.getIpAddr(request);
		Map<String, String> attrs = RequestUtils.getRequestMap(request, "attr_");
		bean = manager.registerMember(username, email, password, ip, groupId, grain, provinceId, cityId, regionId, false, ext, lawyer, attrs);
		log.info("save CmsMember id={}", bean.getId());
		cmsLogMng.operating(request, "cmsMember.log.save", "id=" + bean.getId() + ";username=" + bean.getUsername());
		return "redirect:v_list.do";
	}

	@RequiresPermissions("lawyer:o_update")
	@RequestMapping("/lawyer/o_update.do")
	public String update(Integer id, String email, String password, Boolean disabled, CmsUserExt ext, Lawyer lawyer, Integer groupId, Integer grain, Integer provinceId, Integer cityId,
			Integer regionId, String queryUsername, String queryEmail, Integer queryGroupId, Boolean queryDisabled, Integer pageNo, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Map<String, String> attrs = RequestUtils.getRequestMap(request, "attr_");
		CmsUser bean = manager.updateMember(id, email, password, disabled, provinceId, cityId, regionId, ext, lawyer, groupId, grain, attrs);
		log.info("update CmsMember id={}.", bean.getId());
		cmsLogMng.operating(request, "cmsMember.log.update", "id=" + bean.getId() + ";username=" + bean.getUsername());

		return list(queryUsername, queryEmail, queryGroupId, queryDisabled, pageNo, request, model);
	}

	@RequiresPermissions({ "lawyer:o_delete" })
	@RequestMapping({ "/lawyer/o_delete.do" })
	public String delete(Integer[] ids, Integer queryGroupId, Boolean queryDisabled, Integer pageNo, HttpServletRequest request, ModelMap model) {
		String queryUsername = RequestUtils.getQueryParam(request, "queryUsername");
		String queryEmail = RequestUtils.getQueryParam(request, "queryEmail");
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsUser[] beans = cmsUserMng.deleteByIds(ids);
		for (CmsUser bean : beans) {
			log.info("delete CmsMember id={}", bean.getId());
			cmsLogMng.operating(request, "cmsMember.log.delete", "id=" + bean.getId() + ";username=" + bean.getUsername());
		}
		return list(queryUsername, queryEmail, queryGroupId, queryDisabled, pageNo, request, model);
	}

	@RequiresPermissions("lawyer:o_check")
	@RequestMapping("/lawyer/o_check.do")
	public String check(Integer[] ids, Integer queryGroupId, Boolean queryDisabled, Integer pageNo, HttpServletRequest request, ModelMap model) {
		String queryUsername = RequestUtils.getQueryParam(request, "queryUsername");
		String queryEmail = RequestUtils.getQueryParam(request, "queryEmail");
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		for (Integer id : ids) {
			CmsUser user = cmsUserMng.findById(id);
			user.setDisabled(false);
			cmsUserMng.updateUser(user);
			log.info("check CmsMember id={}", user.getId());
			cmsLogMng.operating(request, "cmsMember.log.delete", "id=" + user.getId() + ";username=" + user.getUsername());
		}
		return list(queryUsername, queryEmail, queryGroupId, queryDisabled, pageNo, request, model);
	}

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
		int j = (arrayOfInteger = ids).length;
		for (int i = 0; i < j; i++) {
			Integer id = arrayOfInteger[i];
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
	@Autowired
	private LawyerTypeMng lawyerTypeManager;
	@Autowired
	private CmsUserMng cmsUserMng;
}
