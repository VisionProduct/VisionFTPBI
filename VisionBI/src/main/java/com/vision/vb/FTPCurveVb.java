package com.vision.vb;

import java.util.ArrayList;
import java.util.List;

public class FTPCurveVb extends CommonVb{
	private String country = "";
	private String leBook = "";
	private String ftpCurveId = "";
	private String effectiveDate = "";
	private int tenorApplicationCodeNt = 8 ;
	private int tenorApplicationCode = -1 ;
	private String tenorApplicationCodeDesc = "";
	private String tenorCode = "";
	private String tenorCodeDesc = "";
	private int visionSbuAttributeAt = 3 ;
	private String visionSbuAttribute = "";
	private String visionSbuAttributeDesc = "";
	private String productAttribute = "";
	private String productAttributeDesc = "";
	
	private String currency = "";
	private String currencyDesc = "";
	private String intRateStart = "0";
	private String intRateEnd = "0";
	private String amountStart = "0";
	private String amountEnd = "0";
	private String endDate = "";
	private String ftpRate = "0";
	private String crrRate = "0" ;
	private int ftpCurveStatusNt = 1 ;
	private int ftpCurveStatus = 0 ;
	
	
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
	public String getFtpCurveId() {
		return ftpCurveId;
	}
	public void setFtpCurveId(String ftpCurveId) {
		this.ftpCurveId = ftpCurveId;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getTenorApplicationCodeNt() {
		return tenorApplicationCodeNt;
	}
	public void setTenorApplicationCodeNt(int tenorApplicationCodeNt) {
		this.tenorApplicationCodeNt = tenorApplicationCodeNt;
	}
	public int getTenorApplicationCode() {
		return tenorApplicationCode;
	}
	public void setTenorApplicationCode(int tenorApplicationCode) {
		this.tenorApplicationCode = tenorApplicationCode;
	}
	public String getTenorApplicationCodeDesc() {
		return tenorApplicationCodeDesc;
	}
	public void setTenorApplicationCodeDesc(String tenorApplicationCodeDesc) {
		this.tenorApplicationCodeDesc = tenorApplicationCodeDesc;
	}
	public String getTenorCode() {
		return tenorCode;
	}
	public void setTenorCode(String tenorCode) {
		this.tenorCode = tenorCode;
	}
	public int getVisionSbuAttributeAt() {
		return visionSbuAttributeAt;
	}
	public void setVisionSbuAttributeAt(int visionSbuAttributeAt) {
		this.visionSbuAttributeAt = visionSbuAttributeAt;
	}
	public String getVisionSbuAttribute() {
		return visionSbuAttribute;
	}
	public void setVisionSbuAttribute(String visionSbuAttribute) {
		this.visionSbuAttribute = visionSbuAttribute;
	}
	public String getVisionSbuAttributeDesc() {
		return visionSbuAttributeDesc;
	}
	public void setVisionSbuAttributeDesc(String visionSbuAttributeDesc) {
		this.visionSbuAttributeDesc = visionSbuAttributeDesc;
	}
	public String getProductAttribute() {
		return productAttribute;
	}
	public void setProductAttribute(String productAttribute) {
		this.productAttribute = productAttribute;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIntRateStart() {
		return intRateStart;
	}
	public void setIntRateStart(String intRateStart) {
		this.intRateStart = intRateStart;
	}
	public String getIntRateEnd() {
		return intRateEnd;
	}
	public void setIntRateEnd(String intRateEnd) {
		this.intRateEnd = intRateEnd;
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFtpRate() {
		return ftpRate;
	}
	public void setFtpRate(String ftpRate) {
		this.ftpRate = ftpRate;
	}
	public String getCrrRate() {
		return crrRate;
	}
	public void setCrrRate(String crrRate) {
		this.crrRate = crrRate;
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
	public String getProductAttributeDesc() {
		return productAttributeDesc;
	}
	public void setProductAttributeDesc(String productAttributeDesc) {
		this.productAttributeDesc = productAttributeDesc;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public String getTenorCodeDesc() {
		return tenorCodeDesc;
	}
	public void setTenorCodeDesc(String tenorCodeDesc) {
		this.tenorCodeDesc = tenorCodeDesc;
	}	
	
}
