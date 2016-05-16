package com.jeecms.core.entity.base;

import java.io.Serializable;




public abstract class BaseArea  implements Serializable {

	public static String REF = "Area";
	public static String PROP_SITE = "site";
	public static String PROP_PID = "pid";
	public static String PROP_LEVEL = "level";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseArea () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseArea (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseArea (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer pid,
		java.lang.Integer level,
		java.lang.Boolean all) {

		this.setId(id);
		this.setName(name);
		this.setPid(pid);
		this.setLevel(level);
		this.setAll(all);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.Integer pid;
	private java.lang.Integer level;
	private java.lang.Boolean all;

	// many to one
	private com.jeecms.core.entity.CmsSite site;




	public java.lang.Integer getId () {
		return id;
	}


	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}






	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Integer getPid() {
		return pid;
	}

	public void setPid(java.lang.Integer pid) {
		this.pid = pid;
	}



	public java.lang.Integer getLevel() {
		return level;
	}

	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}

	public com.jeecms.core.entity.CmsSite getSite() {
		return site;
	}

	public void setSite(com.jeecms.core.entity.CmsSite site) {
		this.site = site;
	}

	public java.lang.Boolean getAll() {
		return all;
	}

	public void setAll(java.lang.Boolean all) {
		this.all = all;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.core.entity.Area)) return false;
		else {
			com.jeecms.core.entity.Area area = (com.jeecms.core.entity.Area) obj;
			if (null == this.getId() || null == area.getId()) return false;
			else return (this.getId().equals(area.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


/*	public String toString () {
		return super.toString();
	}*/

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", pid=" + pid
				+ ", level=" + level + "]";
	}
}