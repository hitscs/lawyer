package com.jeecms.core.manager;

import java.util.List;

import com.jeecms.core.entity.Area;

public interface AreaMng {
	
	public List<Area> getList();
	
	public List<Area> getList(Integer pid);
	
	public Area findById(Integer id);

}