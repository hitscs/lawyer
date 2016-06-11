package com.jeecms.lawyer.manager;

import java.util.List;

import com.jeecms.lawyer.entity.LawyerType;

public abstract interface LawyerTypeMng {

	public List<LawyerType> getList();

	public List<LawyerType> getList(Integer pId);

	public LawyerType findById(Integer id);

}