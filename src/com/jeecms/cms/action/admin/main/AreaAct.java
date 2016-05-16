package com.jeecms.cms.action.admin.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.Area;
import com.jeecms.core.manager.AreaMng;

import net.sf.json.JSONArray;

@Controller
public class AreaAct {
	private static final Logger log = LoggerFactory.getLogger(AreaAct.class);
/**
 * 获取地区json数据
 * @param request
 * @param response
 * @param pid
 */

	@RequiresPermissions("area:v_list")
	@RequestMapping("/area/v_list.do")
	public void list(HttpServletRequest request,HttpServletResponse response, Integer pid) {
		
		List<Area> list = manager.getList(pid);
		
		String json = JSONArray.fromObject(list).toString();
		ResponseUtils.renderJson(response, json);
		
		
	}


	@Autowired
	private AreaMng manager;

}