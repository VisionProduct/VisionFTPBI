package com.vision.vb;

import java.util.ArrayList;
import java.util.List;

public class FTPGroupsVb extends CommonVb{
	private String	country = "";
	private String	leBook = "";
	private int	dataSourceAt =  10;
	private String	dataSource =  "";
	private int	ftpGroupAt =  1301;
	private String	ftpGroup = "";
	private int	groupSeq = 0;
	private int	sourceReferenceAt =  1302;
	private String	sourceReference =  "";
	private String	ftpReference = "";
	private String	ftpDescription = "";
	private String	methodReference = "";
	private String	methodDescription = "";
	private String	defaultGroup = "";
	private int	ftpGroupStatusNt =  1;
	private int	ftpGroupStatus =  -1;
	private int	ftpControlStatusNt =  1;
	private int	ftpControlStatus =  -1;
	private String repricingFlag = "";
	private int defaultFlagCount = -1;
	List<SmartSearchVb> smartSearchOpt = null;
	
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
	public int getGroupSeq() {
		return groupSeq;
	}
	public void setGroupSeq(int groupSeq) {
		this.groupSeq = groupSeq;
	}
	public int getSourceReferenceAt() {
		return sourceReferenceAt;
	}
	public void setSourceReferenceAt(int sourceReferenceAt) {
		this.sourceReferenceAt = sourceReferenceAt;
	}
	public String getSourceReference() {
		return sourceReference;
	}
	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}
	public String getFtpReference() {
		return ftpReference;
	}
	public void setFtpReference(String ftpReference) {
		this.ftpReference = ftpReference;
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
	public int getFtpControlStatusNt() {
		return ftpControlStatusNt;
	}
	public void setFtpControlStatusNt(int ftpControlStatusNt) {
		this.ftpControlStatusNt = ftpControlStatusNt;
	}
	public int getFtpControlStatus() {
		return ftpControlStatus;
	}
	public void setFtpControlStatus(int ftpControlStatus) {
		this.ftpControlStatus = ftpControlStatus;
	}
	public String getMethodDescription() {
		return methodDescription;
	}
	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}
	public String getFtpDescription() {
		return ftpDescription;
	}
	public void setFtpDescription(String ftpDescription) {
		this.ftpDescription = ftpDescription;
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
}
