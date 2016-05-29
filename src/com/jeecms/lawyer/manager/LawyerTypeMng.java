package com.jeecms.lawyer.manager;

import java.util.List;

import com.jeecms.lawyer.entity.LawyerType;

public interface LawyerTypeMng {

	public List<LawyerType> getList();

	public List<LawyerType> getList(Integer pid);

	public LawyerType findById(Integer id);

}