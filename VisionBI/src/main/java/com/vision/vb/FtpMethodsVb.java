package com.vision.vb;

import java.util.List;

public class FtpMethodsVb extends CommonVb {

	
	private static final long serialVersionUID = 2073522091526813534L;
	
	private String methodReference = "";
	private String methodDescription = "";
	private int methodTypeAt = 1303;
	private String methodType = "";
	private String methodTypeDesc = "";
	private int ftpTenorTypeNt = 1302;
	private int ftpTenorType = -1;
	private String ftpTenorTypeDesc = "";
	private int methodBalTypeNt = 1301;
	private int methodBalType = -1;
	private String methodBalTypeDesc = "";
	private int StatusNt = 1;
	private int Status = -1;
	private int repricingFlagAt = 1304;
	private String repricingFlag = "";
	private String repricingFlagDesc = "";
	private int lpTenorTypeNt =1302;
	private int lpTenorType = -1;
	private String lpTenorTypeDesc = "";
	private int ftpCurveIdAt =1310;
	private String ftpCurveId = "";
	private int interestBasisNt = 1301;
	private int interestBasis = -1;
	private String interestBasisDesc = "";
	private int ftpApplyRateNt = 1304;
	private int ftpApplyRate = -1;
	private String ftpApplyRateDesc = "";
	private int addonApplyRate = -1;
	private String addonApplyRateDesc = "";
	private int lpApplyRate = -1;
	private String lpApplyRateDesc = "";
	private int methodSubTypeAt = 1360;
	private String methodSubType = "NA";
	private String methodSubTypeDesc = "NA";
	
	List<SmartSearchVb> smartSearchOpt = null;
	
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
	public String getMethodReference() {
		return methodReference;
	}
	public void setMethodReference(String methodReference) {
		this.methodReference = methodReference;
	}
	public String getMethodDescription() {
		return methodDescription;
	}
	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}
	public int getFtpTenorTypeNt() {
		return ftpTenorTypeNt;
	}
	public void setFtpTenorTypeNt(int ftpTenorTypeNt) {
		this.ftpTenorTypeNt = ftpTenorTypeNt;
	}
	public int getFtpTenorType() {
		return ftpTenorType;
	}
	public void setFtpTenorType(int ftpTenorType) {
		this.ftpTenorType = ftpTenorType;
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
	public int getStatusNt() {
		return StatusNt;
	}
	public void setStatusNt(int statusNt) {
		StatusNt = statusNt;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getLpTenorTypeNt() {
		return lpTenorTypeNt;
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
	public void setLpTenorTypeNt(int lpTenorTypeNt) {
		this.lpTenorTypeNt = lpTenorTypeNt;
	}
	public int getLpTenorType() {
		return lpTenorType;
	}
	public void setLpTenorType(int lpTenorType) {
		this.lpTenorType = lpTenorType;
	}
	public int getFtpCurveIdAt() {
		return ftpCurveIdAt;
	}
	public void setFtpCurveIdAt(int ftpCurveIdAt) {
		this.ftpCurveIdAt = ftpCurveIdAt;
	}
	public String getFtpCurveId() {
		return ftpCurveId;
	}
	public void setFtpCurveId(String ftpCurveId) {
		this.ftpCurveId = ftpCurveId;
	}
	public int getFtpApplyRateNt() {
		return ftpApplyRateNt;
	}
	public void setFtpApplyRateNt(int ftpApplyRateNt) {
		this.ftpApplyRateNt = ftpApplyRateNt;
	}
	public int getFtpApplyRate() {
		return ftpApplyRate;
	}
	public void setFtpApplyRate(int ftpApplyRate) {
		this.ftpApplyRate = ftpApplyRate;
	}
	public int getAddonApplyRate() {
		return addonApplyRate;
	}
	public void setAddonApplyRate(int addonApplyRate) {
		this.addonApplyRate = addonApplyRate;
	}
	public int getLpApplyRate() {
		return lpApplyRate;
	}
	public void setLpApplyRate(int lpApplyRate) {
		this.lpApplyRate = lpApplyRate;
	}
	public int getMethodSubTypeAt() {
		return methodSubTypeAt;
	}
	public void setMethodSubTypeAt(int methodSubTypeAt) {
		this.methodSubTypeAt = methodSubTypeAt;
	}
	public String getMethodSubType() {
		return methodSubType;
	}
	public void setMethodSubType(String methodSubType) {
		this.methodSubType = methodSubType;
	}
	public List<SmartSearchVb> getSmartSearchOpt() {
		return smartSearchOpt;
	}
	public void setSmartSearchOpt(List<SmartSearchVb> smartSearchOpt) {
		this.smartSearchOpt = smartSearchOpt;
	}
	public String getMethodTypeDesc() {
		return methodTypeDesc;
	}
	public void setMethodTypeDesc(String methodTypeDesc) {
		this.methodTypeDesc = methodTypeDesc;
	}
	public String getFtpTenorTypeDesc() {
		return ftpTenorTypeDesc;
	}
	public void setFtpTenorTypeDesc(String ftpTenorTypeDesc) {
		this.ftpTenorTypeDesc = ftpTenorTypeDesc;
	}
	public String getMethodBalTypeDesc() {
		return methodBalTypeDesc;
	}
	public void setMethodBalTypeDesc(String methodBalTypeDesc) {
		this.methodBalTypeDesc = methodBalTypeDesc;
	}
	public String getRepricingFlagDesc() {
		return repricingFlagDesc;
	}
	public void setRepricingFlagDesc(String repricingFlagDesc) {
		this.repricingFlagDesc = repricingFlagDesc;
	}
	public String getLpTenorTypeDesc() {
		return lpTenorTypeDesc;
	}
	public void setLpTenorTypeDesc(String lpTenorTypeDesc) {
		this.lpTenorTypeDesc = lpTenorTypeDesc;
	}
	public String getInterestBasisDesc() {
		return interestBasisDesc;
	}
	public void setInterestBasisDesc(String interestBasisDesc) {
		this.interestBasisDesc = interestBasisDesc;
	}
	public String getFtpApplyRateDesc() {
		return ftpApplyRateDesc;
	}
	public void setFtpApplyRateDesc(String ftpApplyRateDesc) {
		this.ftpApplyRateDesc = ftpApplyRateDesc;
	}
	public String getLpApplyRateDesc() {
		return lpApplyRateDesc;
	}
	public void setLpApplyRateDesc(String lpApplyRateDesc) {
		this.lpApplyRateDesc = lpApplyRateDesc;
	}
	public String getMethodSubTypeDesc() {
		return methodSubTypeDesc;
	}
	public void setMethodSubTypeDesc(String methodSubTypeDesc) {
		this.methodSubTypeDesc = methodSubTypeDesc;
	}
	public String getAddonApplyRateDesc() {
		return addonApplyRateDesc;
	}
	public void setAddonApplyRateDesc(String addonApplyRateDesc) {
		this.addonApplyRateDesc = addonApplyRateDesc;
	}
	
	
	}