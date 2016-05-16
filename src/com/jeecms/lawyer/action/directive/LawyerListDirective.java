package com.jeecms.lawyer.action.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.lawyer.entity.Lawyer;
import com.jeecms.lawyer.manager.LawyerMng;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class LawyerListDirective implements TemplateDirectiveModel {
	public static final String PARAM_COUNT = "count";
	@Autowired
	private LawyerMng lawyerMng;

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		Integer count = DirectiveUtils.getInt("count", params);
		List<Lawyer> list = this.lawyerMng.getList(count.intValue(), true);
		Map<String, TemplateModel> paramWrap = new HashMap(params);
		paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
}
