package com.jeecms.lawyer.entity.base;

import java.io.Serializable;

import com.jeecms.core.entity.CmsUser;
import com.jeecms.lawyer.entity.Lawyer;

public abstract class BaseLawyer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String REF = "Lawyer";
	public static String PROP_USER = "user";
	public static String PROP_ID = "id";
	public static String PROP_COMPANY = "company";
	public static String PROP_CERTIFICATE_NUMBER = "certificateNumber";
	public static String PROP_GOOD_AT_FIELD = "goodAtField";
	public static String PROP_PROFESSIONAL_FIELD = "professionalField";
	public static String PROP_TRUST_YEARS = "trustYears";

	public static String PROP_LAWYER_LEVEL = "lawyerLevel";
	public static String PROP_LAWYER_RESUME = "lawyerResume";

	public BaseLawyer() {
		initialize();
	}

	public BaseLawyer(Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	private Integer id;

	private String company;

	private String certificateNumber;
	private String goodAtField;
	private String professionalField;
	private Integer trustYears;
	private String lawyerLevel;
	private String lawyerResume;

	// one to one
	private com.jeecms.core.entity.CmsUser user;
	
	private java.util.Set<com.jeecms.lawyer.entity.LawyerType> type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getGoodAtField() {
		return goodAtField;
	}

	public void setGoodAtField(String goodAtField) {
		this.goodAtField = goodAtField;
	}

	public String getProfessionalField() {
		return professionalField;
	}

	public void setProfessionalField(String professionalField) {
		this.professionalField = professionalField;
	}

	public Integer getTrustYears() {
		return trustYears;
	}

	public void setTrustYears(Integer trustYears) {
		this.trustYears = trustYears;
	}

	public String getLawyerLevel() {
		return lawyerLevel;
	}

	public void setLawyerLevel(String lawyerLevel) {
		this.lawyerLevel = lawyerLevel;
	}

	public String getLawyerResume() {
		return lawyerResume;
	}

	public void setLawyerResume(String lawyerResume) {
		this.lawyerResume = lawyerResume;
	}

	public com.jeecms.core.entity.CmsUser getUser() {
		return user;
	}

	public void setUser(com.jeecms.core.entity.CmsUser user) {
		this.user = user;
	}

	public java.util.Set<com.jeecms.lawyer.entity.LawyerType> getType() {
		return type;
	}

	public void setType(java.util.Set<com.jeecms.lawyer.entity.LawyerType> type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.jeecms.lawyer.entity.Lawyer))
			return false;
		else {
			com.jeecms.lawyer.entity.Lawyer lawyer = (com.jeecms.lawyer.entity.Lawyer) obj;
			if (null == this.getId() || null == lawyer.getId())
				return false;
			else
				return (this.getId().equals(lawyer.getId()));
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
