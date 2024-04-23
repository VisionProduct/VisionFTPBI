package com.vision.vb;

/**
 * @author Prabu.CJ
 *
 */
import java.util.ArrayList;

public class MappingsVb extends CommonVb {
	private int parentSequence = 0;
	private String separator = "-1";
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private String country = "";
	private String leBook = "";
	private String projectId = "";
	private String mappingId = "";
	private String mappingName = "";
	private String mappingDesc = "";
	private String mappingScript = "";
	private int mappingValidStatusNt = 313;
	private int mappingValidStatus = 0;
	private int mappingStatusNt = 1;
	private int mappingStatus = -1;
	
	private ArrayList<MappingsVb> children = null;
	
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
	public String getMappingName() {
		return mappingName;
	}
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}
	public String getMappingDesc() {
		return mappingDesc;
	}
	public void setMappingDesc(String mappingDesc) {
		this.mappingDesc = mappingDesc;
	}
	public String getMappingScript() {
		return mappingScript;
	}
	public void setMappingScript(String mappingScript) {
		this.mappingScript = mappingScript;
	}
	public int getMappingStatusNt() {
		return mappingStatusNt;
	}
	public void setMappingStatusNt(int mappingStatusNt) {
		this.mappingStatusNt = mappingStatusNt;
	}
	public int getMappingStatus() {
		return mappingStatus;
	}
	public void setMappingStatus(int mappingStatus) {
		this.mappingStatus = mappingStatus;
	}
	public ArrayList<MappingsVb> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<MappingsVb> children) {
		this.children = children;
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
	public int getMappingValidStatusNt() {
		return mappingValidStatusNt;
	}
	public void setMappingValidStatusNt(int mappingValidStatusNt) {
		this.mappingValidStatusNt = mappingValidStatusNt;
	}
	public int getMappingValidStatus() {
		return mappingValidStatus;
	}
	public void setMappingValidStatus(int mappingValidStatus) {
		this.mappingValidStatus = mappingValidStatus;
	}
}