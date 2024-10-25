package com.vision.vb;

import java.util.List;

public class FtpMethodsVb extends CommonVb {

	
	private static final long serialVersionUID = 2073522091526813534L;
	
	
	private String country = "";
	private String leBook = "";
	private String ftpSubGroupId = "";
	
	private int methodBalTypeNt = 1301 ;
	private int methodBalType = -1 ;
	private String methodBalTypeDesc = "" ;
	private int interestBasisNt = 6 ;
	private int interestBasis = 0 ;
	private String interestBasisDesc = "";
	private int applyRateNt = 1304 ;
	private int ftpApplyRate = -1 ;
	private String ftpApplyRateDesc = "" ;
	private int ftpMtStatusNt = 1 ;
	private int ftpMtStatus = 0 ;
	
	
	private int repricingFlagAt = 1304 ;
	private String repricingFlag = "";
	private String repricingFlagDesc = "";
	private int methodTypeAt = 1303 ;
	private String methodType = "";
	private String methodTypeDesc = "";
	private int tenorTypeNt = 1302 ;
	private int ftpTenorType = 0 ;
	private String ftpTenorTypeDesc = "" ;
	private String ftpCurveId = "";
	
	
	List<SmartSearchVb> smartSearchOpt = null;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLeBook() {
		return leBook;
	}

	public void setLeBook(String leBook) {
		this.leBook = leBook;
	}

	public String getFtpSubGroupId() {
		return ftpSubGroupId;
	}

	public void setFtpSubGroupId(String ftpSubGroupId) {
		this.ftpSubGroupId = ftpSubGroupId;
	}

	public int getMethodBalTypeNt() {
		return methodBalTypeNt;
	}

	public void setMethodBalTypeNt(int methodBalTypeNt) {
		this.methodBalTypeNt = methodBalTypeNt;
	}

	public int getMethodBalType() {
		return methodBalType;
	}

	public void setMethodBalType(int methodBalType) {
		this.methodBalType = methodBalType;
	}

	public String getMethodBalTypeDesc() {
		return methodBalTypeDesc;
	}

	public void setMethodBalTypeDesc(String methodBalTypeDesc) {
		this.methodBalTypeDesc = methodBalTypeDesc;
	}

	public int getInterestBasisNt() {
		return interestBasisNt;
	}

	public void setInterestBasisNt(int interestBasisNt) {
		this.interestBasisNt = interestBasisNt;
	}

	public int getInterestBasis() {
		return interestBasis;
	}

	public void setInterestBasis(int interestBasis) {
		this.interestBasis = interestBasis;
	}

	public String getInterestBasisDesc() {
		return interestBasisDesc;
	}

	public void setInterestBasisDesc(String interestBasisDesc) {
		this.interestBasisDesc = interestBasisDesc;
	}

	public int getApplyRateNt() {
		return applyRateNt;
	}

	public void setApplyRateNt(int applyRateNt) {
		this.applyRateNt = applyRateNt;
	}

	public int getFtpApplyRate() {
		return ftpApplyRate;
	}

	public void setFtpApplyRate(int ftpApplyRate) {
		this.ftpApplyRate = ftpApplyRate;
	}

	public String getFtpApplyRateDesc() {
		return ftpApplyRateDesc;
	}

	public void setFtpApplyRateDesc(String ftpApplyRateDesc) {
		this.ftpApplyRateDesc = ftpApplyRateDesc;
	}

	public int getFtpMtStatusNt() {
		return ftpMtStatusNt;
	}

	public void setFtpMtStatusNt(int ftpMtStatusNt) {
		this.ftpMtStatusNt = ftpMtStatusNt;
	}

	public int getFtpMtStatus() {
		return ftpMtStatus;
	}

	public void setFtpMtStatus(int ftpMtStatus) {
		this.ftpMtStatus = ftpMtStatus;
	}

	public int getRepricingFlagAt() {
		return repricingFlagAt;
	}

	public void setRepricingFlagAt(int repricingFlagAt) {
		this.repricingFlagAt = repricingFlagAt;
	}

	public String getRepricingFlag() {
		return repricingFlag;
	}

	public void setRepricingFlag(String repricingFlag) {
		this.repricingFlag = repricingFlag;
	}

	public String getRepricingFlagDesc() {
		return repricingFlagDesc;
	}

	public void setRepricingFlagDesc(String repricingFlagDesc) {
		this.repricingFlagDesc = repricingFlagDesc;
	}

	public int getMethodTypeAt() {
		return methodTypeAt;
	}

	public void setMethodTypeAt(int methodTypeAt) {
		this.methodTypeAt = methodTypeAt;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getMethodTypeDesc() {
		return methodTypeDesc;
	}

	public void setMethodTypeDesc(String methodTypeDesc) {
		this.methodTypeDesc = methodTypeDesc;
	}

	public int getTenorTypeNt() {
		return tenorTypeNt;
	}

	public void setTenorTypeNt(int tenorTypeNt) {
		this.tenorTypeNt = tenorTypeNt;
	}

	public int getFtpTenorType() {
		return ftpTenorType;
	}

	public void setFtpTenorType(int ftpTenorType) {
		this.ftpTenorType = ftpTenorType;
	}

	public String getFtpTenorTypeDesc() {
		return ftpTenorTypeDesc;
	}

	public void setFtpTenorTypeDesc(String ftpTenorTypeDesc) {
		this.ftpTenorTypeDesc = ftpTenorTypeDesc;
	}

	public String getFtpCurveId() {
		return ftpCurveId;
	}

	public void setFtpCurveId(String ftpCurveId) {
		this.ftpCurveId = ftpCurveId;
	}

	public List<SmartSearchVb> getSmartSearchOpt() {
		return smartSearchOpt;
	}

	public void setSmartSearchOpt(List<SmartSearchVb> smartSearchOpt) {
		this.smartSearchOpt = smartSearchOpt;
	}
	
	
}