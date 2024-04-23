package com.vision.vb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RulesLibraryVb extends CommonVb implements Serializable {

	private static final long serialVersionUID = -2010835009684844752L;
	private ArrayList<RulesLibraryVb> children = null;
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private List<RulesLibraryVb> subRulelst = null;
	private String country = "";
	private String leBook = "";
	private String ruleId = "";
	private String ruleName = "";
	private String ruleDescription = "";
	private String glossaryDomain = "";
	private String subDomain = "";
	private String dataSetName = "";
	private String dataSetNameDesc = "";
	private String glossaryTerm = "";
	private String critical = "";
	private String technicalDescription = "";
	private String measuredInSystem = "";
	private String ruleType = "";
	private String frequency = "";
	private String attributeName = "";
	private String attributeDescription = "";
	private String green = "";
	private String amber = "";
	private int ruleStatusNt = 325;
	private int ruleStatus = 0;
	private String column_names;
	private String criteria;
	private String inputvalue;
	private String condition;
	private ArrayList<SmartSearchVb> searchTableschild = null;

	private int glossaryDomainAt = 817;
	private int glossaryTermAt = 818;
	private int subDomainAt = 819;
	private int dataSetNameAt = 820;
	private int criticalAt = 811;
	private int measuredInSystemAt = 821;
	private int ruleTypeAt = 812;
	private int frequencyAt = 813;
	private String validationMappletList = "";
	private String cleanserMappletList = "";

	public ArrayList<RulesLibraryVb> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<RulesLibraryVb> children) {
		this.children = children;
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

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getGlossaryDomain() {
		return glossaryDomain;
	}

	public void setGlossaryDomain(String glossaryDomain) {
		this.glossaryDomain = glossaryDomain;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	public String getGlossaryTerm() {
		return glossaryTerm;
	}

	public void setGlossaryTerm(String glossaryTerm) {
		this.glossaryTerm = glossaryTerm;
	}

	public String getCritical() {
		return critical;
	}

	public void setCritical(String critical) {
		this.critical = critical;
	}

	public String getTechnicalDescription() {
		return technicalDescription;
	}

	public void setTechnicalDescription(String technicalDescription) {
		this.technicalDescription = technicalDescription;
	}

	public String getMeasuredInSystem() {
		return measuredInSystem;
	}

	public void setMeasuredInSystem(String measuredInSystem) {
		this.measuredInSystem = measuredInSystem;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeDescription() {
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}

	public String getGreen() {
		return green;
	}

	public void setGreen(String green) {
		this.green = green;
	}

	public String getAmber() {
		return amber;
	}

	public void setAmber(String amber) {
		this.amber = amber;
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

	public List<RulesLibraryVb> getSubRulelst() {
		return subRulelst;
	}

	public void setSubRulelst(List<RulesLibraryVb> subRulelst) {
		this.subRulelst = subRulelst;
	}

	public int getRuleStatusNt() {
		return ruleStatusNt;
	}

	public void setRuleStatusNt(int ruleStatusNt) {
		this.ruleStatusNt = ruleStatusNt;
	}

	public int getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(int ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public String getColumn_names() {
		return column_names;
	}

	public void setColumn_names(String column_names) {
		this.column_names = column_names;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getInputvalue() {
		return inputvalue;
	}

	public void setInputvalue(String inputvalue) {
		this.inputvalue = inputvalue;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public ArrayList<SmartSearchVb> getSearchTableschild() {
		return searchTableschild;
	}

	public void setSearchTableschild(ArrayList<SmartSearchVb> searchTableschild) {
		this.searchTableschild = searchTableschild;
	}

	public int getGlossaryDomainAt() {
		return glossaryDomainAt;
	}

	public void setGlossaryDomainAt(int glossaryDomainAt) {
		this.glossaryDomainAt = glossaryDomainAt;
	}

	public int getGlossaryTermAt() {
		return glossaryTermAt;
	}

	public void setGlossaryTermAt(int glossaryTermAt) {
		this.glossaryTermAt = glossaryTermAt;
	}

	public int getSubDomainAt() {
		return subDomainAt;
	}

	public void setSubDomainAt(int subDomainAt) {
		this.subDomainAt = subDomainAt;
	}

	public int getDataSetNameAt() {
		return dataSetNameAt;
	}

	public void setDataSetNameAt(int dataSetNameAt) {
		this.dataSetNameAt = dataSetNameAt;
	}

	public int getCriticalAt() {
		return criticalAt;
	}

	public void setCriticalAt(int criticalAt) {
		this.criticalAt = criticalAt;
	}

	public int getMeasuredInSystemAt() {
		return measuredInSystemAt;
	}

	public void setMeasuredInSystemAt(int measuredInSystemAt) {
		this.measuredInSystemAt = measuredInSystemAt;
	}

	public int getRuleTypeAt() {
		return ruleTypeAt;
	}

	public void setRuleTypeAt(int ruleTypeAt) {
		this.ruleTypeAt = ruleTypeAt;
	}

	public int getFrequencyAt() {
		return frequencyAt;
	}

	public void setFrequencyAt(int frequencyAt) {
		this.frequencyAt = frequencyAt;
	}

	public String getValidationMappletList() {
		return validationMappletList;
	}

	public void setValidationMappletList(String validationMappletList) {
		this.validationMappletList = validationMappletList;
	}

	public String getCleanserMappletList() {
		return cleanserMappletList;
	}

	public void setCleanserMappletList(String cleanserMappletList) {
		this.cleanserMappletList = cleanserMappletList;
	}

	public String getDataSetNameDesc() {
		return dataSetNameDesc;
	}

	public void setDataSetNameDesc(String dataSetNameDesc) {
		this.dataSetNameDesc = dataSetNameDesc;
	}

}
