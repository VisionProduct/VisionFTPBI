package com.vision.vb;

public class FTPCalculationsVb extends CommonVb{
	
	private int	dataSourceAt =  0;
	private String	dataSource = "-1";
	private String  year =  "";
	private String  month =  "";
	private String	country = "";
	private String	leBook = "";
	private String	currency = "";
	private String	poolCode = "";
	private String	poolCodeDesc = "";
	private int	ftpEngineStatusNt =  0;
	private int	ftpEngineStatus =  -1;
	private int	visionSbuAt =  3;
	private String	visionSbu = "-1";	
	private String	product = "";
	private String	productDesc = "";
	private String effectiveDate="";
	private String oucAttribute= "";
	private String	oucAttributeLevel="-1";
	private String riskAssetClass = "";
	private String	oucAttributeLevelDesc="";
	private String	versionNo ="";
	private String tenorRateStart = "";
	private String tenorRateEnd = "";
	private String interestRateStart = "";
	private String interestRateEnd = "";
	private String avgStart = "";
	private String avgEnd = "";
	private int	ftpMethodAt =  4508;
	private String	ftpMethod = "-1";
	private String ftpRefCodeDr = "";
	private String ftpRefCodeCr = "";
	private String ftpRateDr = "";
	private String ftpRateCr = "";
	private String ftpProduct = "";
	private String baseBalance = "";
	private String currentBalance = "";
	private String cagr = "";
	private String perCagr = "";
	private String coreBalance = "";
	private String ftpCore = "";
	private String ftpNonCore = "";
	private String cagrGrowth = "";
	private String adjustmentFactor = "";
	private String crr = "";
	private String earliestDate = "";
	private String latestDate = "";
	private String noOfyear = "";
	private String noOfactualYear = "";
	
	public String getOucAttributeLevelDesc() {
		return oucAttributeLevelDesc;
	}
	public void setOucAttributeLevelDesc(String oucAttributeLevelDesc) {
		this.oucAttributeLevelDesc = oucAttributeLevelDesc;
	}
	public String getOucAttribute() {
		return oucAttribute;
	}
	public void setOucAttribute(String oucAttribute) {
		this.oucAttribute = oucAttribute;
	}
	public String getOucAttributeLevel() {
		return oucAttributeLevel;
	}
	public void setOucAttributeLevel(String oucAttributeLevel) {
		this.oucAttributeLevel = oucAttributeLevel;
	}
	public String getRiskAssetClass() {
		return riskAssetClass;
	}
	public void setRiskAssetClass(String riskAssetClass) {
		this.riskAssetClass = riskAssetClass;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public int getDataSourceAt() {
		return dataSourceAt;
	}
	public void setDataSourceAt(int dataSourceAt) {
		this.dataSourceAt = dataSourceAt;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPoolCode() {
		return poolCode;
	}
	public void setPoolCode(String poolCode) {
		this.poolCode = poolCode;
	}
	public String getPoolCodeDesc() {
		return poolCodeDesc;
	}
	public void setPoolCodeDesc(String poolCodeDesc) {
		this.poolCodeDesc = poolCodeDesc;
	}
	public int getFtpEngineStatusNt() {
		return ftpEngineStatusNt;
	}
	public void setFtpEngineStatusNt(int ftpEngineStatusNt) {
		this.ftpEngineStatusNt = ftpEngineStatusNt;
	}
	public int getFtpEngineStatus() {
		return ftpEngineStatus;
	}
	public void setFtpEngineStatus(int ftpEngineStatus) {
		this.ftpEngineStatus = ftpEngineStatus;
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
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getTenorRateStart() {
		return tenorRateStart;
	}
	public void setTenorRateStart(String tenorRateStart) {
		this.tenorRateStart = tenorRateStart;
	}
	public String getTenorRateEnd() {
		return tenorRateEnd;
	}
	public void setTenorRateEnd(String tenorRateEnd) {
		this.tenorRateEnd = tenorRateEnd;
	}
	public String getInterestRateStart() {
		return interestRateStart;
	}
	public void setInterestRateStart(String interestRateStart) {
		this.interestRateStart = interestRateStart;
	}
	public String getInterestRateEnd() {
		return interestRateEnd;
	}
	public void setInterestRateEnd(String interestRateEnd) {
		this.interestRateEnd = interestRateEnd;
	}
	public String getAvgStart() {
		return avgStart;
	}
	public void setAvgStart(String avgStart) {
		this.avgStart = avgStart;
	}
	public String getAvgEnd() {
		return avgEnd;
	}
	public void setAvgEnd(String avgEnd) {
		this.avgEnd = avgEnd;
	}
	public int getFtpMethodAt() {
		return ftpMethodAt;
	}
	public void setFtpMethodAt(int ftpMethodAt) {
		this.ftpMethodAt = ftpMethodAt;
	}
	public String getFtpMethod() {
		return ftpMethod;
	}
	public void setFtpMethod(String ftpMethod) {
		this.ftpMethod = ftpMethod;
	}
	public String getFtpRefCodeDr() {
		return ftpRefCodeDr;
	}
	public void setFtpRefCodeDr(String ftpRefCodeDr) {
		this.ftpRefCodeDr = ftpRefCodeDr;
	}
	public String getFtpRefCodeCr() {
		return ftpRefCodeCr;
	}
	public void setFtpRefCodeCr(String ftpRefCodeCr) {
		this.ftpRefCodeCr = ftpRefCodeCr;
	}
	public String getFtpRateDr() {
		return ftpRateDr;
	}
	public void setFtpRateDr(String ftpRateDr) {
		this.ftpRateDr = ftpRateDr;
	}
	public String getFtpRateCr() {
		return ftpRateCr;
	}
	public void setFtpRateCr(String ftpRateCr) {
		this.ftpRateCr = ftpRateCr;
	}
	public String getFtpProduct() {
		return ftpProduct;
	}
	public void setFtpProduct(String ftpProduct) {
		this.ftpProduct = ftpProduct;
	}
	public String getBaseBalance() {
		return baseBalance;
	}
	public void setBaseBalance(String baseBalance) {
		this.baseBalance = baseBalance;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getCagr() {
		return cagr;
	}
	public void setCagr(String cagr) {
		this.cagr = cagr;
	}
	public String getCoreBalance() {
		return coreBalance;
	}
	public void setCoreBalance(String coreBalance) {
		this.coreBalance = coreBalance;
	}
	public String getPerCagr() {
		return perCagr;
	}
	public void setPerCagr(String perCagr) {
		this.perCagr = perCagr;
	}
	public String getFtpCore() {
		return ftpCore;
	}
	public void setFtpCore(String ftpCore) {
		this.ftpCore = ftpCore;
	}
	public String getFtpNonCore() {
		return ftpNonCore;
	}
	public void setFtpNonCore(String ftpNonCore) {
		this.ftpNonCore = ftpNonCore;
	}
	public String getCagrGrowth() {
		return cagrGrowth;
	}
	public void setCagrGrowth(String cagrGrowth) {
		this.cagrGrowth = cagrGrowth;
	}
	public String getAdjustmentFactor() {
		return adjustmentFactor;
	}
	public void setAdjustmentFactor(String adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}
	public String getCrr() {
		return crr;
	}
	public void setCrr(String crr) {
		this.crr = crr;
	}
	public String getEarliestDate() {
		return earliestDate;
	}
	public void setEarliestDate(String earliestDate) {
		this.earliestDate = earliestDate;
	}
	public String getLatestDate() {
		return latestDate;
	}
	public void setLatestDate(String latestDate) {
		this.latestDate = latestDate;
	}
	public String getNoOfyear() {
		return noOfyear;
	}
	public void setNoOfyear(String noOfyear) {
		this.noOfyear = noOfyear;
	}
	public String getNoOfactualYear() {
		return noOfactualYear;
	}
	public void setNoOfactualYear(String noOfactualYear) {
		this.noOfactualYear = noOfactualYear;
	}
}
