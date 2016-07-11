package com.jeecms.cms.action.member;

import static com.jeecms.cms.Constants.TPLDIR_MEMBER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.Area;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.core.manager.CmsUserExtMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.entity.LawyerType;
import com.jeecms.lawyer.manager.LawyerMng;
import com.jeecms.lawyer.manager.LawyerTypeMng;

import net.sf.json.JSONArray;

/**
 * 会员中心Action
 */
@Controller
public class MemberAct {
	private static final Logger log = LoggerFactory.getLogger(MemberAct.class);

	public static final String MEMBER_CENTER = "tpl.memberCenter";
	public static final String LAWYER_CENTER = "tpl.lawyerCenter";
	public static final String LVSUO_CENTER = "tpl.lvsuoCenter";
	public static final String MEMBER_PROFILE = "tpl.memberProfile";
	public static final String LAWYER_PROFILE = "tpl.lawyerProfile";
	public static final String LVSUO_PROFILE = "tpl.lvsuoProfile";
	public static final String MEMBER_PORTRAIT = "tpl.memberPortrait";
	public static final String MEMBER_PASSWORD = "tpl.memberPassword";

	/**
	 * 会员中心页
	 * 
	 * 如果没有登录则跳转到登陆页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/index.jspx", method = RequestMethod.GET)
	public String index(HttpServletRequest request,
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
		CmsUserExt ext=cmsUserExtMng.findById(user.getId());
		Lawyer lawyer=lawyerMng.findById(user.getId());
		String goodAtField = "";
		String professionalField = "";

		if (null != lawyer && StringUtils.isNotBlank(lawyer.getGoodAtField())&&lawyer.getGoodAtField().length()>1) {
			goodAtField = lawyer.getGoodAtField().substring(1, lawyer.getGoodAtField().length() - 1);
		}

		if (null != lawyer && StringUtils.isNotBlank(lawyer.getProfessionalField())&&lawyer.getProfessionalField().length()>1) {
			professionalField = lawyer.getProfessionalField().substring(1, lawyer.getProfessionalField().length() - 1);
		}
		if(professionalField!=null&&!professionalField.equals("")){
			String[] pArray =professionalField.split(",");
			String pString="";
			for(int j=0;j<pArray.length;j++){
				LawyerType lawyerType = lawyerTypeManager.findById(Integer.parseInt(pArray[j]));
				pString=pString+lawyerType.getName()+",";
				
			}
			professionalField=pString.substring(0, pString.length()-1);
		}		
		
		if(goodAtField!=null&&!goodAtField.equals("")){
			String[] gArray =goodAtField.split(",");
			String gString="";
			for(int j=0;j<gArray.length;j++){
				LawyerType lawyerType = lawyerTypeManager.findById(Integer.parseInt(gArray[j]));
				gString=gString+lawyerType.getName()+",";
				
			}
			goodAtField=gString.substring(0, gString.length()-1);
		}		
		
		
		model.addAttribute("goodAtField", goodAtField);
		model.addAttribute("professionalField", professionalField);
		model.addAttribute("ext", ext);
		model.addAttribute("lawyer", lawyer);
		if(user.getGroup().getId()==3){
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LAWYER_CENTER);
		}
		if(user.getGroup().getId()==4){
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LVSUO_CENTER);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_CENTER);
	}

	/**
	 * 个人资料输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/profile.jspx", method = RequestMethod.GET)
	public String profileInput(HttpServletRequest request,
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
		CmsUserExt ext=cmsUserExtMng.findById(user.getId());
		Lawyer lawyer=lawyerMng.findById(user.getId());
		List<Area> areaList = areaManager.getList();
		List<Area> cityList =areaManager.getList(110000);
		if(null!=ext&&ext.getProvince()!=null){
			cityList=areaManager.getList(ext.getProvince().getId());
		}
		List<LawyerType> list = lawyerTypeManager.getList();
		String goodAtField = "";
		String professionalField = "";

		if (null != lawyer && StringUtils.isNotBlank(lawyer.getGoodAtField())&&lawyer.getGoodAtField().length()>1) {
			goodAtField = lawyer.getGoodAtField().substring(1, lawyer.getGoodAtField().length() - 1);
		}

		if (null != lawyer && StringUtils.isNotBlank(lawyer.getProfessionalField())&&lawyer.getProfessionalField().length()>1) {
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
		
		List areaListJson= new ArrayList();
		
		for(int i=0;i<areaList.size();i++){
			if( areaList.get(i).getPid()!=0){
			Map map=new HashMap();
			map.put("id", areaList.get(i).getId());
			map.put("pid", areaList.get(i).getPid());
			map.put("name", areaList.get(i).getName());
			areaListJson.add(map);
			}
			
			
		}
		
		String cityListJson = JSONArray.fromObject(areaListJson).toString();
		

		
		model.addAttribute("cityListJson", cityListJson);
		model.addAttribute("ext", ext);
		model.addAttribute("lawyer", lawyer);
		model.addAttribute("areaList", areaList);
		model.addAttribute("cityList", cityList);
		model.addAttribute("gLawyerType", gJson);
		model.addAttribute("pLawyerType", pJson);
		if(user.getGroup().getId()==3){
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LAWYER_PROFILE);
		}
		if(user.getGroup().getId()==4){
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_MEMBER, LVSUO_PROFILE);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_PROFILE);
	}
	/**
	 * 更换头像
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/portrait.jspx", method = RequestMethod.GET)
	public String portrait(HttpServletRequest request,
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
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_PORTRAIT);
	}

	/**
	 * 个人资料提交页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/profile.jspx", method = RequestMethod.POST)
	public String profileSubmit(CmsUserExt ext, String nextUrl,Integer provinceId, Integer cityId, Integer regionId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
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
		if(provinceId!=null) ext.setProvince(areaManager.findById(provinceId));
		if(cityId!=null) ext.setCity(areaManager.findById(cityId));
		if(regionId!=null) ext.setRegion(areaManager.findById(regionId));
		ext.setId(user.getId());
		cmsUserExtMng.update(ext, user);
		log.info("update CmsUserExt success. id={}", user.getId());
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	/**
	 * 律师个人资料提交
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/lawyerProfile.jspx", method = RequestMethod.POST)
	public String lawyerProfileSubmit(CmsUserExt ext,  Lawyer lawyer,String nextUrl,Integer provinceId, Integer cityId, Integer regionId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
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
		if(provinceId!=null) ext.setProvince(areaManager.findById(provinceId));
		if(cityId!=null) ext.setCity(areaManager.findById(cityId));
		if(regionId!=null) ext.setRegion(areaManager.findById(regionId));
		ext.setId(user.getId());
		cmsUserExtMng.update(ext, user);
		lawyer.setId(user.getId());
		lawyerMng.update(lawyer, user);
		log.info("update Lawyer success. id={}", user.getId());
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	/**
	 * 密码修改输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/pwd.jspx", method = RequestMethod.GET)
	public String passwordInput(HttpServletRequest request,
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
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_PASSWORD);
	}

	/**
	 * 密码修改提交页
	 * 
	 * @param origPwd
	 *            原始密码
	 * @param newPwd
	 *            新密码
	 * @param email
	 *            邮箱
	 * @param nextUrl
	 *            下一个页面地址
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/pwd.jspx", method = RequestMethod.POST)
	public String passwordSubmit(String origPwd, String newPwd, String email,
			String nextUrl, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
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
		WebErrors errors = validatePasswordSubmit(user.getId(), origPwd,
				newPwd, email, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		cmsUserMng.updatePwdEmail(user.getId(), newPwd, email);
		return FrontUtils.showSuccess(request, model, nextUrl);
	}

	/**
	 * 验证密码是否正确
	 * 
	 * @param origPwd
	 *            原密码
	 * @param request
	 * @param response
	 */
	@RequestMapping("/member/checkPwd.jspx")
	public void checkPwd(String origPwd, HttpServletRequest request,
			HttpServletResponse response) {
		CmsUser user = CmsUtils.getUser(request);
		boolean pass = cmsUserMng.isPasswordValid(user.getId(), origPwd);
		ResponseUtils.renderJson(response, pass ? "true" : "false");
	}

	private WebErrors validatePasswordSubmit(Integer id, String origPwd,
			String newPwd, String email, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifBlank(origPwd, "origPwd", 100)) {
			return errors;
		}
		if (errors.ifMaxLength(newPwd, "newPwd", 100)) {
			return errors;
		}
		if (errors.ifNotEmail(email, "email", 100)) {
			return errors;
		}
		if (!cmsUserMng.isPasswordValid(id, origPwd)) {
			errors.addErrorCode("member.origPwdInvalid");
			return errors;
		}
		return errors;
	}

	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsUserExtMng cmsUserExtMng;
	@Autowired
	private LawyerMng lawyerMng;
	@Autowired
	private AreaMng areaManager;
	@Autowired
	private LawyerTypeMng lawyerTypeManager;
}
