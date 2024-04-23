package com.vision.vb;

import java.util.ArrayList;

public class TransformationsLinkerVb extends CommonVb {

	private static final long serialVersionUID = -2010835009684844752L;
	private int parentSequence = 0;
	private String separator = "-1";
	private ArrayList<TransformationsLinkerVb> children = null; 
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private String country = "";
	private String leBook = "";
	private String projectId = "";
	private String mappingId = "";
	private String transHierarchyId = "";
	private String ParentHierarchyId = "";
	private int ParentHierarchyIdStatusNt = 803;
	private int ParentHierarchyIdStatus = 0;
	
	public int getParentSequence() {
		return parentSequence;
	}
	public void setParentSequence(int parentSequence) {
		this.parentSequence = parentSequence;
	}
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public ArrayList<TransformationsLinkerVb> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<TransformationsLinkerVb> children) {
		this.children = children;
	}
	public String getApplicationAccessAt() {
		return applicationAccessAt;
	}
	public void setApplicationAccessAt(String applicationAccessAt) {
		this.applicationAccessAt = applicationAccessAt;
	}
	public String getApplicationAccess() {
		return applicationAccess;
	}
	public void setApplicationAccess(String applicationAccess) {
		this.applicationAccess = applicationAccess;
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getMappingId() {
		return mappingId;
	}
	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}
	public String getTransHierarchyId() {
		return transHierarchyId;
	}
	public void setTransHierarchyId(String transHierarchyId) {
		this.transHierarchyId = transHierarchyId;
	}
	public String getParentHierarchyId() {
		return ParentHierarchyId;
	}
	public void setParentHierarchyId(String parentHierarchyId) {
		ParentHierarchyId = parentHierarchyId;
	}
	public int getParentHierarchyIdStatusNt() {
		return ParentHierarchyIdStatusNt;
	}
	public void setParentHierarchyIdStatusNt(int parentHierarchyIdStatusNt) {
		ParentHierarchyIdStatusNt = parentHierarchyIdStatusNt;
	}
	public int getParentHierarchyIdStatus() {
		return ParentHierarchyIdStatus;
	}
	public void setParentHierarchyIdStatus(int parentHierarchyIdStatus) {
		ParentHierarchyIdStatus = parentHierarchyIdStatus;
	}
	
}