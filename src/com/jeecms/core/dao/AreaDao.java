package com.jeecms.core.dao;

import java.util.List;

import com.jeecms.core.entity.Area;

public interface AreaDao {
	public List<Area> getList();

	public List<Area> getList(Integer pid);
	
	public Area findById(Integer id);

}