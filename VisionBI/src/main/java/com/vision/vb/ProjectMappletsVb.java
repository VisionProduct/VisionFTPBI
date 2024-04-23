package com.vision.vb;

/**
 * @author Prabu.CJ
 *
 */
import java.util.ArrayList;

public class ProjectMappletsVb extends CommonVb {
	private int parentSequence = 0;
	private String separator = "-1";
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private String country = "";
	private String leBook = "";
	private String projectId = "";
	private String mappletId = "";
	private String mappletName = "";
	private String mappletDesc = "";
	private String sourceMappletId = "";
	private String mappletIdTypeAt = "";
	private String mappletIdType = "";
	private int mappletStatusNt = 1;
	private int mappletStatus = -1;
	private String mappletScript = "";
	
	private ArrayList<ProjectMappletsVb> children = null;
	
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
	public ArrayList<ProjectMappletsVb> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<ProjectMappletsVb> children) {
		this.children = children;
	}
	public String getMappletId() {
		return mappletId;
	}
	public void setMappletId(String mappletId) {
		this.mappletId = mappletId;
	}
	public String getMappletName() {
		return mappletName;
	}
	public void setMappletName(String mappletName) {
		this.mappletName = mappletName;
	}
	public String getMappletDesc() {
		return mappletDesc;
	}
	public void setMappletDesc(String mappletDesc) {
		this.mappletDesc = mappletDesc;
	}
	public String getSourceMappletId() {
		return sourceMappletId;
	}
	public void setSourceMappletId(String sourceMappletId) {
		this.sourceMappletId = sourceMappletId;
	}
	public String getMappletIdTypeAt() {
		return mappletIdTypeAt;
	}
	public void setMappletIdTypeAt(String mappletIdTypeAt) {
		this.mappletIdTypeAt = mappletIdTypeAt;
	}
	public String getMappletIdType() {
		return mappletIdType;
	}
	public void setMappletIdType(String mappletIdType) {
		this.mappletIdType = mappletIdType;
	}
	public int getMappletStatusNt() {
		return mappletStatusNt;
	}
	public void setMappletStatusNt(int mappletStatusNt) {
		this.mappletStatusNt = mappletStatusNt;
	}
	public int getMappletStatus() {
		return mappletStatus;
	}
	public void setMappletStatus(int mappletStatus) {
		this.mappletStatus = mappletStatus;
	}
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
	public String getMappletScript() {
		return mappletScript;
	}
	public void setMappletScript(String mappletScript) {
		this.mappletScript = mappletScript;
	}
}