package com.vision.vb;

public class FTPCurveVb extends CommonVb{
	private int	tenorBucketApplicationCodeNt  =  8;
	private int	tenorBucketApplicationCode = -1;
	private String	tenorBucketCode = "";
	private String	intStart =  "";
	private String	intEnd =  "";
	private String	amountStart =  "";
	private String	amountEnd =  "";
	private String	ftpRateId =  "";
	private int	ftpCurveStatusNt =  0;
	private int	ftpCurveStatus =  -1;
	private String	effectiveDate =  "";
	private int ftpCurveIdAt =1310;
	private String ftpCurveId = "";
	private int	visionSbuAt =  3;
	private String	visionSbu =  "";
	private String	currency =  "";
	private String	customerId =  "";
	private String	contractId =  "";
	private String	addOnRate =  "";
	private String	subRate =  "";
	private int	lpTenorBucketApplicationCodeNt  =  8;
	private int	lpTenorBucketApplicationCode = -1;
	private String	lpTenorBucketCode = "";
	private String	rateEffectiveDate = "";
	private String tenorDecription ="";
	private String actualEffectiveDate = "";
	private String	addOnDepositRate = "";
	private String	subsidy = "";
	private String	addOnLendingRate = "";
	private String	requiredReserveRate = "";
	private String	glAllocationRate = "";
	private String	insuranceRate = "";
	private String	addOnAttRate1 = "";
	private String	addOnAttRate2 = "";
	private String	addOnAttRate3 = "";
	private String	addOnAttRate4 = "";
	
	private String productAttribute = "";
	
	public String getProductAttribute() {
		return productAttribute;
	}
	public void setProductAttribute(String productAttribute) {
		this.productAttribute = productAttribute;
	}
	
	public String getActualEffectiveDate() {
		return actualEffectiveDate;
	}
	public void setActualEffectiveDate(String actualEffectiveDate) {
		this.actualEffectiveDate = actualEffectiveDate;
	}
	public String getTenorDecription() {
		return tenorDecription;
	}
	public void setTenorDecription(String tenorDecription) {
		this.tenorDecription = tenorDecription;
	}
	public int getLpTenorBucketApplicationCodeNt() {
		return lpTenorBucketApplicationCodeNt;
	}
	public void setLpTenorBucketApplicationCodeNt(int lpTenorBucketApplicationCodeNt) {
		this.lpTenorBucketApplicationCodeNt = lpTenorBucketApplicationCodeNt;
	}
	public int getLpTenorBucketApplicationCode() {
		return lpTenorBucketApplicationCode;
	}
	public void setLpTenorBucketApplicationCode(int lpTenorBucketApplicationCode) {
		this.lpTenorBucketApplicationCode = lpTenorBucketApplicationCode;
	}
	public String getLpTenorBucketCode() {
		return lpTenorBucketCode;
	}
	public void setLpTenorBucketCode(String lpTenorBucketCode) {
		this.lpTenorBucketCode = lpTenorBucketCode;
	}
	public String getRateEffectiveDate() {
		return rateEffectiveDate;
	}
	public void setRateEffectiveDate(String rateEffectiveDate) {
		this.rateEffectiveDate = rateEffectiveDate;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getAddOnRate() {
		return addOnRate;
	}
	public void setAddOnRate(String addOnRate) {
		this.addOnRate = addOnRate;
	}
	public String getSubRate() {
		return subRate;
	}
	public void setSubRate(String subRate) {
		this.subRate = subRate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getVisionSbuAt() {
		return visionSbuAt;
	}
	public void setVisionSbuAt(int visionSbuAt) {
		this.visionSbuAt = visionSbuAt;
	}
	public String getVisionSbu() {
		return visionSbu;
	}
	public void setVisionSbu(String visionSbu) {
		this.visionSbu = visionSbu;
	}
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
	public String getIntStart() {
		return intStart;
	}
	public void setIntStart(String intStart) {
		this.intStart = intStart;
	}
	public String getIntEnd() {
		return intEnd;
	}
	public void setIntEnd(String intEnd) {
		this.intEnd = intEnd;
	}
	public String getAmountStart() {
		return amountStart;
	}
	public void setAmountStart(String amountStart) {
		this.amountStart = amountStart;
	}
	public String getAmountEnd() {
		return amountEnd;
	}
	public void setAmountEnd(String amountEnd) {
		this.amountEnd = amountEnd;
	}
	public String getFtpRateId() {
		return ftpRateId;
	}
	public void setFtpRateId(String ftpRateId) {
		this.ftpRateId = ftpRateId;
	}
	public int getFtpCurveStatusNt() {
		return ftpCurveStatusNt;
	}
	public void setFtpCurveStatusNt(int ftpCurveStatusNt) {
		this.ftpCurveStatusNt = ftpCurveStatusNt;
	}
	public int getFtpCurveStatus() {
		return ftpCurveStatus;
	}
	public void setFtpCurveStatus(int ftpCurveStatus) {
		this.ftpCurveStatus = ftpCurveStatus;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
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
	public String getAddOnDepositRate() {
		return addOnDepositRate;
	}
	public void setAddOnDepositRate(String addOnDepositRate) {
		this.addOnDepositRate = addOnDepositRate;
	}
	public String getSubsidy() {
		return subsidy;
	}
	public void setSubsidy(String subsidy) {
		this.subsidy = subsidy;
	}
	public String getAddOnLendingRate() {
		return addOnLendingRate;
	}
	public void setAddOnLendingRate(String addOnLendingRate) {
		this.addOnLendingRate = addOnLendingRate;
	}
	public String getRequiredReserveRate() {
		return requiredReserveRate;
	}
	public void setRequiredReserveRate(String requiredReserveRate) {
		this.requiredReserveRate = requiredReserveRate;
	}
	public String getGlAllocationRate() {
		return glAllocationRate;
	}
	public void setGlAllocationRate(String glAllocationRate) {
		this.glAllocationRate = glAllocationRate;
	}
	public String getInsuranceRate() {
		return insuranceRate;
	}
	public void setInsuranceRate(String insuranceRate) {
		this.insuranceRate = insuranceRate;
	}
	public String getAddOnAttRate1() {
		return addOnAttRate1;
	}
	public void setAddOnAttRate1(String addOnAttRate1) {
		this.addOnAttRate1 = addOnAttRate1;
	}
	public String getAddOnAttRate2() {
		return addOnAttRate2;
	}
	public void setAddOnAttRate2(String addOnAttRate2) {
		this.addOnAttRate2 = addOnAttRate2;
	}
	public String getAddOnAttRate3() {
		return addOnAttRate3;
	}
	public void setAddOnAttRate3(String addOnAttRate3) {
		this.addOnAttRate3 = addOnAttRate3;
	}
	public String getAddOnAttRate4() {
		return addOnAttRate4;
	}
	public void setAddOnAttRate4(String addOnAttRate4) {
		this.addOnAttRate4 = addOnAttRate4;
	}
	
}
