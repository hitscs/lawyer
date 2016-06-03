package com.jeecms.lawyer.action.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.Area;
import com.jeecms.core.manager.AreaMng;
import com.jeecms.lawyer.entity.LawyerType;
import com.jeecms.lawyer.manager.LawyerTypeMng;

import net.sf.json.JSONArray;

@Controller
public class IndexAct {
	@Autowired
	private AreaMng areaManager;
	@Autowired
	private LawyerTypeMng lawyerTypeManager;
	// @RequiresPermissions("unified_user:v_check_username")
	@RequestMapping("/index/area.jspx")
	public String getArea(Integer pid, HttpServletRequest request, HttpServletResponse response) {
		if (null == pid)
			pid = 0;
		List<Area> list = areaManager.getList(pid);

		String json = JSONArray.fromObject(list).toString();
		ResponseUtils.renderJson(response, json);

		return null;
	}
	@RequestMapping("/index/lawyerType.jspx")
	public String getLawyerType(Integer pid, HttpServletRequest request, HttpServletResponse response) {
		if (null == pid)
			pid = 0;
		List<LawyerType> list = lawyerTypeManager.getList(pid);

		String json = JSONArray.fromObject(list).toString();
		ResponseUtils.renderJson(response, json);

		return null;
	}
	@RequestMapping("/index/allLawyerType.jspx")
	public String getAllLawyerType(HttpServletRequest request, HttpServletResponse response) {

		List<LawyerType> list = lawyerTypeManager.getList();

		String json = JSONArray.fromObject(list).toString();
		ResponseUtils.renderJson(response, json);

		return null;
	}
}
