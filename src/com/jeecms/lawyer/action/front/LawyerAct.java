package com.jeecms.lawyer.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.manager.LawyerMng;

@Controller
public class LawyerAct {
	public static final String INDEX = "tpl.lawyerIndex";
	public static final String DETAIL = "tpl.lawyerDetail";
	@Autowired
	private LawyerMng lawyerMng;

	@RequestMapping(value = { "/lawyer/index.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", "tpl.lawyerIndex");
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
}
