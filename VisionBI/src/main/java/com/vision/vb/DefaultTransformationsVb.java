package com.vision.vb;

import java.util.ArrayList;

public class DefaultTransformationsVb extends CommonVb {

	private static final long serialVersionUID = -2010835009684844752L;
	private int parentSequence = 0;
	private String separator = "-1";
	private ArrayList<DefaultTransformationsVb> children = null;
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	
	private String country = "";
	private String leBook = "";
	private String transformationId = "";
	private String transformationName = "";
	private String transformationSeq = "";
	private String transformationIcon = "";
	private String transformationScript = "";
	private String transformationAutolink = "";
	private String transformationAddColumn = "";
	private String transformationEditColumn = "";
	private String transformationDeleteColumn = "";
	private String transformationSelectAllColumn = "";
	private String transformationMinimize = "";
	private String transformationCopyColumn = "";
	private String transformationPasteColumn = "";
	private String transformationMoveUp = "";
	private String transformationMoveDown = "";
	private String transformationFilter = "";
	private String transformationSort = "";
	private int transformationStatusNt = 1;
	private int transformationStatus = -1;
	
	private String functionId = "";
	private String functionDisplayName = "";
	private String funtionName = "";
	private String syntax = "";
	private String parameters = "";
	private String dataType = "";
	private String definition = "";
	private String example = "";
	private String functionRestriction = "";
	private String functionTypeAt = "";
	private String functionType = "";
	private String functionStatusNt = "";
	private String functionStatus = "";
	private String grpFlag = "";
	
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
	public ArrayList<DefaultTransformationsVb> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<DefaultTransformationsVb> children) {
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
	public String getTransformationId() {
		return transformationId;
	}
	public void setTransformationId(String transformationId) {
		this.transformationId = transformationId;
	}
	public String getTransformationName() {
		return transformationName;
	}
	public void setTransformationName(String transformationName) {
		this.transformationName = transformationName;
	}
	public String getTransformationSeq() {
		return transformationSeq;
	}
	public void setTransformationSeq(String transformationSeq) {
		this.transformationSeq = transformationSeq;
	}
	public String getTransformationIcon() {
		return transformationIcon;
	}
	public void setTransformationIcon(String transformationIcon) {
		this.transformationIcon = transformationIcon;
	}
	public String getTransformationScript() {
		return transformationScript;
	}
	public void setTransformationScript(String transformationScript) {
		this.transformationScript = transformationScript;
	}
	public String getTransformationAutolink() {
		return transformationAutolink;
	}
	public void setTransformationAutolink(String transformationAutolink) {
		this.transformationAutolink = transformationAutolink;
	}
	public String getTransformationAddColumn() {
		return transformationAddColumn;
	}
	public void setTransformationAddColumn(String transformationAddColumn) {
		this.transformationAddColumn = transformationAddColumn;
	}
	public String getTransformationEditColumn() {
		return transformationEditColumn;
	}
	public void setTransformationEditColumn(String transformationEditColumn) {
		this.transformationEditColumn = transformationEditColumn;
	}
	public String getTransformationDeleteColumn() {
		return transformationDeleteColumn;
	}
	public void setTransformationDeleteColumn(String transformationDeleteColumn) {
		this.transformationDeleteColumn = transformationDeleteColumn;
	}
	public String getTransformationSelectAllColumn() {
		return transformationSelectAllColumn;
	}
	public void setTransformationSelectAllColumn(String transformationSelectAllColumn) {
		this.transformationSelectAllColumn = transformationSelectAllColumn;
	}
	public String getTransformationMinimize() {
		return transformationMinimize;
	}
	public void setTransformationMinimize(String transformationMinimize) {
		this.transformationMinimize = transformationMinimize;
	}
	public String getTransformationCopyColumn() {
		return transformationCopyColumn;
	}
	public void setTransformationCopyColumn(String transformationCopyColumn) {
		this.transformationCopyColumn = transformationCopyColumn;
	}
	public String getTransformationPasteColumn() {
		return transformationPasteColumn;
	}
	public void setTransformationPasteColumn(String transformationPasteColumn) {
		this.transformationPasteColumn = transformationPasteColumn;
	}
	public String getTransformationMoveUp() {
		return transformationMoveUp;
	}
	public void setTransformationMoveUp(String transformationMoveUp) {
		this.transformationMoveUp = transformationMoveUp;
	}
	public String getTransformationMoveDown() {
		return transformationMoveDown;
	}
	public void setTransformationMoveDown(String transformationMoveDown) {
		this.transformationMoveDown = transformationMoveDown;
	}
	public String getTransformationFilter() {
		return transformationFilter;
	}
	public void setTransformationFilter(String transformationFilter) {
		this.transformationFilter = transformationFilter;
	}
	public String getTransformationSort() {
		return transformationSort;
	}
	public void setTransformationSort(String transformationSort) {
		this.transformationSort = transformationSort;
	}
	public int getTransformationStatusNt() {
		return transformationStatusNt;
	}
	public void setTransformationStatusNt(int transformationStatusNt) {
		this.transformationStatusNt = transformationStatusNt;
	}
	public int getTransformationStatus() {
		return transformationStatus;
	}
	public void setTransformationStatus(int transformationStatus) {
		this.transformationStatus = transformationStatus;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getFunctionDisplayName() {
		return functionDisplayName;
	}
	public void setFunctionDisplayName(String functionDisplayName) {
		this.functionDisplayName = functionDisplayName;
	}
	public String getFuntionName() {
		return funtionName;
	}
	public void setFuntionName(String funtionName) {
		this.funtionName = funtionName;
	}
	public String getSyntax() {
		return syntax;
	}
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getFunctionRestriction() {
		return functionRestriction;
	}
	public void setFunctionRestriction(String functionRestriction) {
		this.functionRestriction = functionRestriction;
	}
	public String getFunctionTypeAt() {
		return functionTypeAt;
	}
	public void setFunctionTypeAt(String functionTypeAt) {
		this.functionTypeAt = functionTypeAt;
	}
	public String getFunctionType() {
		return functionType;
	}
	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}
	public String getFunctionStatusNt() {
		return functionStatusNt;
	}
	public void setFunctionStatusNt(String functionStatusNt) {
		this.functionStatusNt = functionStatusNt;
	}
	public String getFunctionStatus() {
		return functionStatus;
	}
	public void setFunctionStatus(String functionStatus) {
		this.functionStatus = functionStatus;
	}
	public String getGrpFlag() {
		return grpFlag;
	}
	public void setGrpFlag(String grpFlag) {
		this.grpFlag = grpFlag;
	}
}