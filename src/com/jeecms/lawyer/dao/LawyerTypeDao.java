package com.jeecms.lawyer.dao;

import java.util.List;

import com.jeecms.lawyer.entity.LawyerType;

public interface LawyerTypeDao {
	public List<LawyerType> getList();

	public List<LawyerType> getList(Integer pId);
	
	public LawyerType findById(Integer id);

}