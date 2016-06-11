package com.jeecms.lawyer.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.lawyer.dao.LawyerTypeDao;
import com.jeecms.lawyer.entity.LawyerType;
import com.jeecms.lawyer.manager.LawyerTypeMng;

@Service
@Transactional
public class LawyerTypeMngImpl implements LawyerTypeMng {
	@Transactional(readOnly = true)
	public List<LawyerType> getList() {
		return dao.getList();
	}

	@Transactional(readOnly = true)
	public List<LawyerType> getList(Integer pId) {
		return dao.getList(pId);
	}

	@Transactional(readOnly = true)
	public LawyerType findById(Integer id) {
		LawyerType entity = dao.findById(id);
		return entity;
	}

	private LawyerTypeDao dao;

	@Autowired
	public void setDao(LawyerTypeDao dao) {
		this.dao = dao;
	}
}