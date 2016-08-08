package com.jeecms.lawyer.action.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentExt;
import com.jeecms.cms.entity.main.ContentTxt;
import com.jeecms.cms.entity.main.ContentType;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.cms.manager.main.ContentTypeMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.Area;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.lawyer.entity.LawyerType;
import com.jeecms.lawyer.manager.LawyerMng;
import com.jeecms.lawyer.manager.LawyerTypeMng;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

import net.sf.json.JSONArray;

@Controller
public class AskAct {
	public static final String ASK = "tpl.askIndex";
	@Autowired
	private LawyerMng lawyerMng;
	@Autowired
	private AreaMng areaManager;
	@Autowired
	private LawyerTypeMng lawyerTypeManager;

	@RequestMapping(value = { "/lawyer/ask.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String index(HttpServletRequest request, HttpServletResponse response,String message, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);

		List<Area> areaList = areaManager.getList();
		List<LawyerType> lawyerTypeList = lawyerTypeManager.getList();
		List lawyerCommentList =lawyerMng.getListByComment(null, null, 1, 10);
		List lawyerContentList =lawyerMng.getListByComment();

		String areaListJson = JSONArray.fromObject(areaList).toString();
		model.addAttribute("lawyerCommentList", lawyerCommentList);//回复数量排行（一篇文章可以多条回复）
		model.addAttribute("lawyerContentList", lawyerContentList);//文章数量排行
		model.addAttribute("areaListJson", areaListJson);
		model.addAttribute("areaList", areaList);
		model.addAttribute("lawyerTypeList", lawyerTypeList);
		model.addAttribute("currentMenu", "askIndex");
		model.addAttribute("message", message);
		
		FrontUtils.frontData(request, model, site);
		//FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", ASK);
	}
	
	
	@RequestMapping(value = "/lawyer/ask_save.jspx")
	public String save(String title, String author, String description,Integer toUserId,
			String txt, String tagStr, Integer channelId, Integer modelId,
			String captcha,String mediaPath,String mediaType,
			String[] attachmentPaths, String[] attachmentNames,
			String[] attachmentFilenames, String[] picPaths, String[] picDescs,
			String nextUrl, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		WebErrors errors = validateSave(title, author, description, txt,
				tagStr, channelId, site, user, captcha, request, response);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}

		Content c = new Content();
		c.setSite(site);
		if(null!=toUserId){
			CmsUser toUser =cmsUserMng.findById(toUserId);
			c.setToUser(toUser);
		}
		CmsModel defaultModel=cmsModelMng.getDefModel();
		if(modelId!=null){
			CmsModel m=cmsModelMng.findById(modelId);
			if(m!=null){
				c.setModel(m);
			}else{
				c.setModel(defaultModel);
			}
		}else{
			c.setModel(defaultModel);
		}
		
		ContentExt ext = new ContentExt();
		ext.setTitle(title);
		ext.setAuthor(author);
		ext.setDescription(description);
		ext.setMediaPath(mediaPath);
		ext.setMediaType(mediaType);
		
		ContentTxt t = new ContentTxt();
		t.setTxt(txt);
		ContentType type = contentTypeMng.getDef();
		if (type == null) {
			throw new RuntimeException("Default ContentType not found.");
		}
		Integer typeId = type.getId();
		String[] tagArr = StrUtils.splitAndTrim(tagStr, ",", null);
		c.setAttr(RequestUtils.getRequestMap(request, "attr_"));
		c = contentMng.save(c, ext, t, null, null, null, tagArr, attachmentPaths,attachmentNames, attachmentFilenames
				,picPaths,picDescs,channelId, typeId, null,true, user, true);
		//return FrontUtils.showSuccess(request, model, nextUrl);
		
		model.addAttribute("currentMenu", "askIndex");
		//model.addAttribute("messageSend", "ok");
		request.removeAttribute("txt");
		FrontUtils.frontData(request, model, site);
		//FrontUtils.frontPageData(request, model);
		
        if(null!=toUserId){
	    return "redirect:detail.jspx?message=ok&id="+toUserId;
        }else return "redirect:ask.jspx?message=ok";
		//return "redirect:/comment.jspx?contentId="+c.getId();
		//return FrontUtils.getTplPath(request, site.getSolutionPath(), "lawyer", ASK);
	}	
	
	
	
	
	
	
	
/*
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
	}*/
/*
	@RequestMapping(value = { "/lawyer/lawyerList.jspx" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String lawyerList(String realname, String professionalField, String goodAtField, Integer groupId, Integer provinceId, Integer cityId, Integer regionId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		if (null != professionalField && !professionalField.trim().equals(""))
			professionalField = "," + professionalField + ",";
		if (null != goodAtField && !goodAtField.trim().equals(""))
			goodAtField = "," + goodAtField + ",";
		Pagination pagination = lawyerMng.getPageByCondition(site.getId(), provinceId, cityId, regionId, realname, professionalField, goodAtField, groupId, false, false, null, 1, 10);
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
	}*/
	private WebErrors validateSave(String title, String author,
			String description, String txt, String tagStr, Integer channelId,
			CmsSite site, CmsUser user, String captcha,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		//先不验证验证码
/*		try {
			if (!imageCaptchaService.validateResponseForID(session
					.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");
				return errors;
			}
		} catch (CaptchaServiceException e) {
			errors.addErrorCode("error.exceptionCaptcha");
			return errors;
		}*/
		if (errors.ifBlank(title, "title", 150)) {
			return errors;
		}
		if (errors.ifMaxLength(author, "author", 100)) {
			return errors;
		}
		if (errors.ifMaxLength(description, "description", 255)) {
			return errors;
		}
		
		if (errors.ifMaxLength(tagStr, "tagStr", 255)) {
			return errors;
		}
		if (errors.ifNull(channelId, "channelId")) {
			return errors;
		}
		if (vldChannel(errors, site, user, channelId)) {
			return errors;
		}
		return errors;
	}
	private boolean vldChannel(WebErrors errors, CmsSite site, CmsUser user,
			Integer channelId) {
		Channel channel = channelMng.findById(channelId);
		if (errors.ifNotExist(channel, Channel.class, channelId)) {
			return true;
		}
		if (!channel.getSite().getId().equals(site.getId())) {
			errors.notInSite(Channel.class, channelId);
			return true;
		}
		if (!channel.getContriGroups().contains(user.getGroup())) {
			errors.noPermission(Channel.class, channelId);
			return true;
		}
		return false;
	}
	
	@Autowired		
	private CmsUserMng cmsUserMng;	
	@Autowired
	protected ContentMng contentMng;
	@Autowired
	protected ContentTypeMng contentTypeMng;
	@Autowired
	protected ChannelMng channelMng;
	@Autowired
	protected CmsModelMng cmsModelMng;
	@Autowired
	protected SessionProvider session;
	@Autowired
	protected FileRepository fileRepository;
	@Autowired
	protected ImageCaptchaService imageCaptchaService;
}
