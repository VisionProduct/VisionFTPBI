package com.vision.vb;

public class FtpAddonVb extends CommonVb{

	private String country = "";
	private String leBook = "";
	private String ftpCurveId = "";
	private String effectiveDate = "";
	private int tenorApplicationCodeNt = 8 ;
	private int tenorApplicationCode = -1 ;
	private String tenorApplicationCodeDesc = "" ;
	private String tenorCode = "";
	private String tenorCodeDesc = "";
	private String customerId = "";
	private String customerIdDesc = "";
	private String contractId = "";
	private String contractIdDesc = "";
	private String subsidyRate = "" ;
	private String endDate = "";
	private int ftpAddonStatusNt = 1 ;
	private int ftpAddonStatus = 0 ;
	public void setCountry(String country) {
		this.country = country; 
	}

	public String getCountry() {
		return country; 
	}
	public void setLeBook(String leBook) {
		this.leBook = leBook; 
	}

	public String getLeBook() {
		return leBook; 
	}
	public void setFtpCurveId(String ftpCurveId) {
		this.ftpCurveId = ftpCurveId; 
	}

	public String getFtpCurveId() {
		return ftpCurveId; 
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate; 
	}

	public String getEffectiveDate() {
		return effectiveDate; 
	}
	public void setTenorApplicationCodeNt(int tenorApplicationCodeNt) {
		this.tenorApplicationCodeNt = tenorApplicationCodeNt; 
	}

	public int getTenorApplicationCodeNt() {
		return tenorApplicationCodeNt; 
	}
	public void setTenorApplicationCode(int tenorApplicationCode) {
		this.tenorApplicationCode = tenorApplicationCode; 
	}

	public int getTenorApplicationCode() {
		return tenorApplicationCode; 
	}
	public void setTenorCode(String tenorCode) {
		this.tenorCode = tenorCode; 
	}

	public String getTenorCode() {
		return tenorCode; 
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId; 
	}

	public String getCustomerId() {
		return customerId; 
	}
	public void setContractId(String contractId) {
		this.contractId = contractId; 
	}

	public String getContractId() {
		return contractId; 
	}
	public void setSubsidyRate(String subsidyRate) {
		this.subsidyRate = subsidyRate; 
	}

	public String getSubsidyRate() {
		return subsidyRate; 
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate; 
	}

	public String getEndDate() {
		return endDate; 
	}
	public void setFtpAddonStatusNt(int ftpAddonStatusNt) {
		this.ftpAddonStatusNt = ftpAddonStatusNt; 
	}

	public int getFtpAddonStatusNt() {
		return ftpAddonStatusNt; 
	}
	public void setFtpAddonStatus(int ftpAddonStatus) {
		this.ftpAddonStatus = ftpAddonStatus; 
	}

	public int getFtpAddonStatus() {
		return ftpAddonStatus; 
	}

	public String getTenorApplicationCodeDesc() {
		return tenorApplicationCodeDesc;
	}

	public void setTenorApplicationCodeDesc(String tenorApplicationCodeDesc) {
		this.tenorApplicationCodeDesc = tenorApplicationCodeDesc;
	}

	public String getCustomerIdDesc() {
		return customerIdDesc;
	}

	public void setCustomerIdDesc(String customerIdDesc) {
		this.customerIdDesc = customerIdDesc;
	}

	public String getContractIdDesc() {
		return contractIdDesc;
	}

	public void setContractIdDesc(String contractIdDesc) {
		this.contractIdDesc = contractIdDesc;
	}

	public String getTenorCodeDesc() {
		return tenorCodeDesc;
	}

	public void setTenorCodeDesc(String tenorCodeDesc) {
		this.tenorCodeDesc = tenorCodeDesc;
	}


}