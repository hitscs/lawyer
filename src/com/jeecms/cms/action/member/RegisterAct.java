package com.jeecms.cms.action.member;

import static com.jeecms.cms.Constants.TPLDIR_MEMBER;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.Area;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.core.manager.AuthenticationMng;
import com.jeecms.core.manager.CmsConfigItemMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.manager.LawyerMng;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

import net.sf.json.JSONArray;

/**
 * 前台会员注册Action
 */
@Controller
public class RegisterAct {
	private static final Logger log = LoggerFactory
			.getLogger(RegisterAct.class);

	public static final String REGISTER = "tpl.register";
	public static final String REGISTER_RESULT = "tpl.registerResult";
	public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final String REGISTERLAWYER= "tpl.registerLawyer";
	public static final String REGISTERLVSUO= "tpl.registerLvsuo";

	@RequestMapping(value = "/register.jspx", method = RequestMethod.GET)
	public String input(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		// 没有开启会员注册
		if (!mcfg.isRegisterOn()) {
			return FrontUtils.showMessage(request, model,
					"member.registerClose");
		}
		
		List<CmsConfigItem>items=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<Area> areaList = areaManager.getList();

		String areaListJson = JSONArray.fromObject(areaList).toString();
		model.addAttribute("areaListJson", areaListJson);
		model.addAttribute("areaList", areaList);
		FrontUtils.frontData(request, model, site);
		model.addAttribute("mcfg", mcfg);
		model.addAttribute("items", items);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, REGISTER);
	}
	@RequestMapping(value = "/register.jspx", method = RequestMethod.POST)
	public String submit(String username, String email, String password,int groupId,Integer provinceId,Integer cityId,Integer regionId,
			CmsUserExt userExt, String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		CmsConfig config = site.getConfig();
		WebErrors errors = validateSubmit(username, email, password, captcha,
				site, request, response);
		boolean disabled=false;
		if(config.getMemberConfig().isCheckOn()){
			disabled=true;
		}
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		String ip = RequestUtils.getIpAddr(request);
		if(provinceId!=null) userExt.setProvince(areaManager.findById(provinceId));
		if(cityId!=null) userExt.setCity(areaManager.findById(cityId));
		if(regionId!=null) userExt.setRegion(areaManager.findById(regionId));
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		if (config.getEmailValidate()) {
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				model.addAttribute("status", 5);
			} else {
				try {
					cmsUserMng.registerMember(username, email, password, ip,
							groupId,disabled,userExt,attrs, false, sender, msgTpl);
					model.addAttribute("status", 0);
				} catch (UnsupportedEncodingException e) {
					// 发送邮件异常
					model.addAttribute("status", 100);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				} catch (MessagingException e) {
					// 发送邮件异常
					model.addAttribute("status", 101);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				}
			}
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);
				return null;
			} else {
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_MEMBER, REGISTER_RESULT);
			}
		} else {
			cmsUserMng.registerMember(username, email, password, ip, groupId,null,disabled,userExt,attrs);
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			FrontUtils.frontPageData(request, model);
			model.addAttribute("success", true);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LOGIN_INPUT);
		}

	}	
	@RequestMapping(value = "/registerLvsuo.jspx", method = RequestMethod.GET)
	public String inputLvsuo(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		// 没有开启会员注册
		if (!mcfg.isRegisterOn()) {
			return FrontUtils.showMessage(request, model,
					"member.registerClose");
		}
		
		List<CmsConfigItem>items=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<Area> areaList = areaManager.getList();

		String areaListJson = JSONArray.fromObject(areaList).toString();
		model.addAttribute("areaListJson", areaListJson);
		model.addAttribute("areaList", areaList);
		FrontUtils.frontData(request, model, site);
		model.addAttribute("mcfg", mcfg);
		model.addAttribute("items", items);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, REGISTERLVSUO);
	}
	@RequestMapping(value = "/registerLvsuo.jspx", method = RequestMethod.POST)
	public String submitLvsuo(String username, String email, String password,int groupId,Integer provinceId,Integer cityId,Integer regionId,
			CmsUserExt userExt, String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		CmsConfig config = site.getConfig();
		WebErrors errors = validateSubmit(username, email, password, captcha,
				site, request, response);
		boolean disabled=false;
		if(config.getMemberConfig().isCheckOn()){
			disabled=true;
		}
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		String ip = RequestUtils.getIpAddr(request);
		if(provinceId!=null) userExt.setProvince(areaManager.findById(provinceId));
		if(cityId!=null) userExt.setCity(areaManager.findById(cityId));
		if(regionId!=null) userExt.setRegion(areaManager.findById(regionId));
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		if (config.getEmailValidate()) {
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				model.addAttribute("status", 5);
			} else {
				try {
					cmsUserMng.registerMember(username, email, password, ip,
							groupId,disabled,userExt,attrs, false, sender, msgTpl);
					model.addAttribute("status", 0);
				} catch (UnsupportedEncodingException e) {
					// 发送邮件异常
					model.addAttribute("status", 100);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				} catch (MessagingException e) {
					// 发送邮件异常
					model.addAttribute("status", 101);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				}
			}
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);
				return null;
			} else {
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_MEMBER, REGISTER_RESULT);
			}
		} else {
			cmsUserMng.registerMember(username, email, password, ip, groupId,null,disabled,userExt,attrs);
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			FrontUtils.frontPageData(request, model);
			model.addAttribute("success", true);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LOGIN_INPUT);
		}

	}
	@RequestMapping(value = "/registerLawyer.jspx", method = RequestMethod.GET)
	public String inputLawyer(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		// 没有开启会员注册
		if (!mcfg.isRegisterOn()) {
			return FrontUtils.showMessage(request, model,
					"member.registerClose");
		}
		List<CmsConfigItem>items=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<Area> areaList = areaManager.getList();

		String areaListJson = JSONArray.fromObject(areaList).toString();
		model.addAttribute("areaListJson", areaListJson);
		model.addAttribute("areaList", areaList);
		FrontUtils.frontData(request, model, site);
		model.addAttribute("mcfg", mcfg);
		model.addAttribute("items", items);
		
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, REGISTERLAWYER);
	}
/**
 * 律师注册
 * @param username
 * @param email
 * @param password
 * @param groupId
 * @param grain
 * @param provinceId
 * @param cityId
 * @param regionId
 * @param userExt
 * @param lawyer
 * @param captcha
 * @param nextUrl
 * @param request
 * @param response
 * @param model
 * @return
 * @throws IOException
 */

	@RequestMapping(value = "/registerLawyer.jspx", method = RequestMethod.POST)
	public String submitLawyer(String username, String email, String password,Integer groupId,Integer grain,Integer provinceId,Integer cityId,Integer regionId,
			CmsUserExt userExt, Lawyer lawyer,String captcha, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		CmsConfig config = site.getConfig();
		WebErrors errors = validateSubmit(username, email, password, captcha,
				site, request, response);
		boolean disabled=false;
		if(config.getMemberConfig().isCheckOn()){
			disabled=true;
		}
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		String ip = RequestUtils.getIpAddr(request);
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		if (config.getEmailValidate()) {
			EmailSender sender = configMng.getEmailSender();
			MessageTemplate msgTpl = configMng.getRegisterMessageTemplate();
			if (sender == null) {
				// 邮件服务器没有设置好
				model.addAttribute("status", 4);
			} else if (msgTpl == null) {
				// 邮件模板没有设置好
				model.addAttribute("status", 5);
			} else {
				try {
					lawyerMng.registerMember(username, email, password, ip, groupId, grain, provinceId, cityId, regionId, disabled, userExt, lawyer, attrs, false, sender, msgTpl);

					model.addAttribute("status", 0);
				} catch (UnsupportedEncodingException e) {
					// 发送邮件异常
					model.addAttribute("status", 100);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				} catch (MessagingException e) {
					// 发送邮件异常
					model.addAttribute("status", 101);
					model.addAttribute("message", e.getMessage());
					log.error("send email exception.", e);
				}
			}
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			if (!StringUtils.isBlank(nextUrl)) {
				response.sendRedirect(nextUrl);
				return null;
			} else {
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_MEMBER, REGISTER_RESULT);
			}
		} else {
			lawyerMng.registerMember(username, email, password, ip, groupId, grain, provinceId, cityId, regionId, disabled, userExt, lawyer, attrs);
			log.info("member register success. username={}", username);
			FrontUtils.frontData(request, model, site);
			FrontUtils.frontPageData(request, model);
			model.addAttribute("success", true);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LOGIN_INPUT);
		}

	}
	@RequestMapping(value = "/active.jspx", method = RequestMethod.GET)
	public String active(String username, String key,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateActive(username, key, request, response);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		UnifiedUser user = unifiedUserMng.active(username, key);
		String ip = RequestUtils.getIpAddr(request);
		authMng.activeLogin(user, ip, request, response, session);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, REGISTER_ACTIVE_SUCCESS);
	}

	@RequestMapping(value = "/username_unique.jspx")
	public void usernameUnique(HttpServletRequest request,
			HttpServletResponse response) {
		String username = RequestUtils.getQueryParam(request, "username");
		// 用户名为空，返回false。
		if (StringUtils.isBlank(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		CmsSite site = CmsUtils.getSite(request);
		CmsConfig config = site.getConfig();
		// 保留字检查不通过，返回false。
		if (!config.getMemberConfig().checkUsernameReserved(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		ResponseUtils.renderJson(response, "true");
	}

	@RequestMapping(value = "/email_unique.jspx")
	public void emailUnique(HttpServletRequest request,
			HttpServletResponse response) {
		String email = RequestUtils.getQueryParam(request, "email");
		// email为空，返回false。
		if (StringUtils.isBlank(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		// email存在，返回false。
		if (unifiedUserMng.emailExist(email)) {
			ResponseUtils.renderJson(response, "false");
			return;
		}
		ResponseUtils.renderJson(response, "true");
	}

	private WebErrors validateSubmit(String username, String email,
			String password, String captcha, CmsSite site,
			HttpServletRequest request, HttpServletResponse response) {
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		WebErrors errors = WebErrors.create(request);
		try {
			if (!imageCaptchaService.validateResponseForID(session
					.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");
				return errors;
			}
		} catch (CaptchaServiceException e) {
			errors.addErrorCode("error.exceptionCaptcha");
			log.warn("", e);
			return errors;
		}
		if (errors.ifOutOfLength(username, "username",
				mcfg.getUsernameMinLen(), 100)) {
			return errors;
		}
		if (errors.ifNotUsername(username, "username",
				mcfg.getUsernameMinLen(), 100)) {
			return errors;
		}
		if (errors.ifOutOfLength(password, "password",
				mcfg.getPasswordMinLen(), 100)) {
			return errors;
		}
		if (errors.ifNotEmail(email, "email", 100)) {
			return errors;
		}
		// 保留字检查不通过，返回false。
		if (!mcfg.checkUsernameReserved(username)) {
			errors.addErrorCode("error.usernameReserved");
			return errors;
		}
		// 用户名存在，返回false。
		if (unifiedUserMng.usernameExist(username)) {
			errors.addErrorCode("error.usernameExist");
			return errors;
		}
		return errors;
	}

	private WebErrors validateActive(String username, String activationCode,
			HttpServletRequest request, HttpServletResponse response) {
		WebErrors errors = WebErrors.create(request);
		if (StringUtils.isBlank(username)
				|| StringUtils.isBlank(activationCode)) {
			errors.addErrorCode("error.exceptionParams");
			return errors;
		}
		UnifiedUser user = unifiedUserMng.getByUsername(username);
		if (user == null) {
			errors.addErrorCode("error.usernameNotExist");
			return errors;
		}
		if (user.getActivation()
				|| StringUtils.isBlank(user.getActivationCode())) {
			errors.addErrorCode("error.usernameActivated");
			return errors;
		}
		if (!user.getActivationCode().equals(activationCode)) {
			errors.addErrorCode("error.exceptionActivationCode");
			return errors;
		}
		return errors;
	}

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private AuthenticationMng authMng;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
	@Autowired
	private AreaMng areaManager;
	@Autowired
	private LawyerMng lawyerMng;
}
