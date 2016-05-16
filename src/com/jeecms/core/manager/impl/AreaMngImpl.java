package com.jeecms.core.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.core.dao.AreaDao;
import com.jeecms.core.entity.Area;
import com.jeecms.core.manager.AreaMng;

@Service
@Transactional
public class AreaMngImpl implements AreaMng {
	@Transactional(readOnly = true)
	public List<Area> getList() {
		return dao.getList();
	}
	@Transactional(readOnly = true)
	public List<Area> getList(Integer pid) {
		return dao.getList();
	}
	@Transactional(readOnly = true)
	public Area findById(Integer id) {
		Area entity = dao.findById(id);
		return entity;
	}



	private AreaDao dao;

	@Autowired
	public void setDao(AreaDao dao) {
		this.dao = dao;
	}
}