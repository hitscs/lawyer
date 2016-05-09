package com.jeecms.lawyer.entity.base;

import java.io.Serializable;

public abstract class BaseLawyerType implements Serializable {

	public static String REF = "LawyerType";
	//public static String PROP_SITE = "site";
	public static String PROP_PID = "pid";
	public static String PROP_LEVEL = "level";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";

	// constructors
	public BaseLawyerType() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseLawyerType(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseLawyerType(java.lang.Integer id, java.lang.String name, java.lang.Integer pid, java.lang.Integer level) {

		this.setId(id);
		this.setName(name);
		this.setPid(pid);
		this.setLevel(level);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.Integer pid;
	private java.lang.Integer level;
	//private java.lang.Boolean all;

	// many to one
//	private com.jeecms.core.entity.CmsSite site;

	private java.util.Set<com.jeecms.lawyer.entity.Lawyer> lawyer;

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
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

/*	public com.jeecms.core.entity.CmsSite getSite() {
		return site;
	}

	public void setSite(com.jeecms.core.entity.CmsSite site) {
		this.site = site;
	}*/

	public java.util.Set<com.jeecms.lawyer.entity.Lawyer> getLawyer() {
		return lawyer;
	}

	public void setLawyer(java.util.Set<com.jeecms.lawyer.entity.Lawyer> lawyer) {
		this.lawyer = lawyer;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.jeecms.lawyer.entity.LawyerType))
			return false;
		else {
			com.jeecms.lawyer.entity.LawyerType lawyerType = (com.jeecms.lawyer.entity.LawyerType) obj;
			if (null == this.getId() || null == lawyerType.getId())
				return false;
			else
				return (this.getId().equals(lawyerType.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}