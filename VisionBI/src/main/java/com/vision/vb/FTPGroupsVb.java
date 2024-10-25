package com.vision.vb;

import java.util.ArrayList;
import java.util.List;

public class FTPGroupsVb extends CommonVb{
	
	public FTPGroupsVb() {
		super();
	}
	
	
	private String	country = "";
	private String	leBook = "";
	private int	dataSourceAt =  10;
	private String	dataSource =  "";
	private String	dataSourceDescription =  "";
	private int	ftpGroupAt =  1301;
	private String	ftpGroup = "";
	private String	ftpGroupDesc = "";
	private String	ftpSubGroupId = "";
	private String	ftpSubGroupDesc = "";
	private int	ftpSubGroupPriority = 0;
	
	private String	methodType = "";
	private String	methodTypeDesc = "";

	private String	methodReference = "";
	private String	methodDescription = "";
	private String	defaultGroup = "N";
	private int	ftpGroupStatusNt =  1;
	private int	ftpGroupStatus =  0;
	private String repricingFlag = "";
	private int defaultFlagCount = -1;
	List<SmartSearchVb> smartSearchOpt = null;
	private String	effectiveDate =  "";
	
	
	private List<FTPSourceConfigVb> ftpSourceConfigList = new ArrayList<FTPSourceConfigVb>();
	
	private List<FtpMethodsVb> ftpMethodsList = new ArrayList<FtpMethodsVb>();
	
	private List<FTPCurveVb> ftpCurveList = new ArrayList<FTPCurveVb>();
	private List<FTPCurveVb> ftpAddOnList = new ArrayList<FTPCurveVb>();
	
	private List<FTPGroupsVb> childList = new ArrayList<FTPGroupsVb>();
	
	public FTPGroupsVb(String country, String leBook, String dataSource, String ftpGroup){
		this.country = country;
		this.leBook = leBook;
		this.dataSource = dataSource;
		this.ftpGroup = ftpGroup;
	}
	
	private ArrayList<FTPGroupsVb> children = null;
	
	public String getRepricingFlag() {
		return repricingFlag;
	}
	public void setRepricingFlag(String repricingFlag) {
		this.repricingFlag = repricingFlag;
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
	public int getFtpGroupAt() {
		return ftpGroupAt;
	}
	public void setFtpGroupAt(int ftpGroupAt) {
		this.ftpGroupAt = ftpGroupAt;
	}
	public String getFtpGroup() {
		return ftpGroup;
	}
	public void setFtpGroup(String ftpGroup) {
		this.ftpGroup = ftpGroup;
	}
	public String getMethodReference() {
		return methodReference;
	}
	public void setMethodReference(String methodReference) {
		this.methodReference = methodReference;
	}
	public String getDefaultGroup() {
		return defaultGroup;
	}
	public void setDefaultGroup(String defaultGroup) {
		this.defaultGroup = defaultGroup;
	}
	public int getFtpGroupStatusNt() {
		return ftpGroupStatusNt;
	}
	public void setFtpGroupStatusNt(int ftpGroupStatusNt) {
		this.ftpGroupStatusNt = ftpGroupStatusNt;
	}
	public int getFtpGroupStatus() {
		return ftpGroupStatus;
	}
	public void setFtpGroupStatus(int ftpGroupStatus) {
		this.ftpGroupStatus = ftpGroupStatus;
	}
	public String getMethodDescription() {
		return methodDescription;
	}
	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}
	public int getDefaultFlagCount() {
		return defaultFlagCount;
	}
	public void setDefaultFlagCount(int defaultFlagCount) {
		this.defaultFlagCount = defaultFlagCount;
	}
	public List<SmartSearchVb> getSmartSearchOpt() {
		return smartSearchOpt;
	}
	public void setSmartSearchOpt(List<SmartSearchVb> smartSearchOpt) {
		this.smartSearchOpt = smartSearchOpt;
	}
	public ArrayList<FTPGroupsVb> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<FTPGroupsVb> children) {
		this.children = children;
	}
	public String getDataSourceDescription() {
		return dataSourceDescription;
	}
	public void setDataSourceDescription(String dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}
	public List<FTPGroupsVb> getChildList() {
		return childList;
	}
	public void setChildList(List<FTPGroupsVb> childList) {
		this.childList = childList;
	}
	public String getFtpGroupDesc() {
		return ftpGroupDesc;
	}
	public void setFtpGroupDesc(String ftpGroupDesc) {
		this.ftpGroupDesc = ftpGroupDesc;
	}
	public String getFtpSubGroupId() {
		return ftpSubGroupId;
	}
	public void setFtpSubGroupId(String ftpSubGroupId) {
		this.ftpSubGroupId = ftpSubGroupId;
	}
	public String getFtpSubGroupDesc() {
		return ftpSubGroupDesc;
	}
	public void setFtpSubGroupDesc(String ftpSubGroupDesc) {
		this.ftpSubGroupDesc = ftpSubGroupDesc;
	}
	public int getFtpSubGroupPriority() {
		return ftpSubGroupPriority;
	}
	public void setFtpSubGroupPriority(int ftpSubGroupPriority) {
		this.ftpSubGroupPriority = ftpSubGroupPriority;
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
	public List<FTPSourceConfigVb> getFtpSourceConfigList() {
		return ftpSourceConfigList;
	}
	public void setFtpSourceConfigList(List<FTPSourceConfigVb> ftpSourceConfigList) {
		this.ftpSourceConfigList = ftpSourceConfigList;
	}
	public List<FtpMethodsVb> getFtpMethodsList() {
		return ftpMethodsList;
	}
	public void setFtpMethodsList(List<FtpMethodsVb> ftpMethodsList) {
		this.ftpMethodsList = ftpMethodsList;
	}
	public List<FTPCurveVb> getFtpCurveList() {
		return ftpCurveList;
	}
	public void setFtpCurveList(List<FTPCurveVb> ftpCurveList) {
		this.ftpCurveList = ftpCurveList;
	}
	public List<FTPCurveVb> getFtpAddOnList() {
		return ftpAddOnList;
	}
	public void setFtpAddOnList(List<FTPCurveVb> ftpAddOnList) {
		this.ftpAddOnList = ftpAddOnList;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
