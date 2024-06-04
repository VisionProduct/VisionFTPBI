package com.vision.vb;

import java.util.List;

public class TenorBucketsVb extends CommonVb{
	private int	tenorBucketApplicationCodeNt  =  0;
	private int	tenorBucketApplicationCode = -1;
	private String	tenorBucketApplicationCodeDesc = "";
	private String	tenorBucketCode = "";
	private String	tenorBucketDescription = "";
	private String	dayStart =  "";
	private String	dayEnd =  "";
	private int	tenorBucketStatusNt =  1;
	private int	tenorBucketStatus =  -1;
	private String	tenorBucketStatusDesc = "";
	
	List<SmartSearchVb> smartSearchOpt = null;
	
	public int getTenorBucketApplicationCodeNt() {
		return tenorBucketApplicationCodeNt;
	}
	public void setTenorBucketApplicationCodeNt(int tenorBucketApplicationCodeNt) {
		this.tenorBucketApplicationCodeNt = tenorBucketApplicationCodeNt;
	}
	public int getTenorBucketApplicationCode() {
		return tenorBucketApplicationCode;
	}
	public void setTenorBucketApplicationCode(int tenorBucketApplicationCode) {
		this.tenorBucketApplicationCode = tenorBucketApplicationCode;
	}
	public String getTenorBucketCode() {
		return tenorBucketCode;
	}
	public void setTenorBucketCode(String tenorBucketCode) {
		this.tenorBucketCode = tenorBucketCode;
	}
	public String getTenorBucketDescription() {
		return tenorBucketDescription;
	}
	public void setTenorBucketDescription(String tenorBucketDescription) {
		this.tenorBucketDescription = tenorBucketDescription;
	}
	public String getDayStart() {
		return dayStart;
	}
	public void setDayStart(String dayStart) {
		this.dayStart = dayStart;
	}
	public String getDayEnd() {
		return dayEnd;
	}
	public void setDayEnd(String dayEnd) {
		this.dayEnd = dayEnd;
	}
	public int getTenorBucketStatusNt() {
		return tenorBucketStatusNt;
	}
	public void setTenorBucketStatusNt(int tenorBucketStatusNt) {
		this.tenorBucketStatusNt = tenorBucketStatusNt;
	}
	public int getTenorBucketStatus() {
		return tenorBucketStatus;
	}
	public void setTenorBucketStatus(int tenorBucketStatus) {
		this.tenorBucketStatus = tenorBucketStatus;
	}
	public List<SmartSearchVb> getSmartSearchOpt() {
		return smartSearchOpt;
	}
	public void setSmartSearchOpt(List<SmartSearchVb> smartSearchOpt) {
		this.smartSearchOpt = smartSearchOpt;
	}
	public String getTenorBucketStatusDesc() {
		return tenorBucketStatusDesc;
	}
	public void setTenorBucketStatusDesc(String tenorBucketStatusDesc) {
		this.tenorBucketStatusDesc = tenorBucketStatusDesc;
	}
	public String getTenorBucketApplicationCodeDesc() {
		return tenorBucketApplicationCodeDesc;
	}
	public void setTenorBucketApplicationCodeDesc(String tenorBucketApplicationCodeDesc) {
		this.tenorBucketApplicationCodeDesc = tenorBucketApplicationCodeDesc;
	}

}
