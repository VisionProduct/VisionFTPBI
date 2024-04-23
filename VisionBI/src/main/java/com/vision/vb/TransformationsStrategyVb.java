package com.vision.vb;

import java.util.ArrayList;

public class TransformationsStrategyVb extends CommonVb {

	private static final long serialVersionUID = -2010835009684844752L;
	private int parentSequence = 0;
	private String separator = "-1";
	private ArrayList<TransformationsStrategyVb> children = null;
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private String country = "";
	private String leBook = "";
	private String projectId = "";
	private String mappingId = "";
	private String transHierarchyId = "";
	private String transTypeAt = "803";
	private String transType = "";
	private String transMappletId = "";
	private String mappletHierarchyId = "";
	private String transformationId = "";
	
	private String columnSeq = "";
	private String columnId = "";
	private String columnName = "";
	private String tableName = "";
	private String drivedColumn = "";
	private String columnIOTypeAt = "801";
	private String columnIOType = "";
	private String columnQuery = "";
	private String lookupColumn = "";
	private String lookupTable = "";
	private String strategyId = "";
	private String strategyName = "";
	private String strategyDesc = "";
	private String condition = "";
	private String sortColumn = "";
	private String delimiter = "";
	private String groupId = "";
	private String regexpPattern = "";
	private String nullFlag = "";
	private String refCorrectionColumn = "";
	private String refCorrectColumn = "";
	private String customeCurrectionText = "";
	private String customeCurrectText = "";
	private String trailLeadSpace = "";
	private String multipleSpace = "";
	private String Scope = "";
	private String caseType = "";
	private int columnStatusNt = 308;
	private int columnStatus = 0;
	
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
	public ArrayList<TransformationsStrategyVb> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<TransformationsStrategyVb> children) {
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
	public String getMappletHierarchyId() {
		return mappletHierarchyId;
	}
	public void setMappletHierarchyId(String mappletHierarchyId) {
		this.mappletHierarchyId = mappletHierarchyId;
	}
	public String getTransformationId() {
		return transformationId;
	}
	public void setTransformationId(String transformationId) {
		this.transformationId = transformationId;
	}
	public String getTransTypeAt() {
		return transTypeAt;
	}
	public void setTransTypeAt(String transTypeAt) {
		this.transTypeAt = transTypeAt;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransMappletId() {
		return transMappletId;
	}
	public void setTransMappletId(String transMappletId) {
		this.transMappletId = transMappletId;
	}
	public String getColumnSeq() {
		return columnSeq;
	}
	public void setColumnSeq(String columnSeq) {
		this.columnSeq = columnSeq;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDrivedColumn() {
		return drivedColumn;
	}
	public void setDrivedColumn(String drivedColumn) {
		this.drivedColumn = drivedColumn;
	}
	public String getColumnIOTypeAt() {
		return columnIOTypeAt;
	}
	public void setColumnIOTypeAt(String columnIOTypeAt) {
		this.columnIOTypeAt = columnIOTypeAt;
	}
	public String getColumnIOType() {
		return columnIOType;
	}
	public void setColumnIOType(String columnIOType) {
		this.columnIOType = columnIOType;
	}
	public String getColumnQuery() {
		return columnQuery;
	}
	public void setColumnQuery(String columnQuery) {
		this.columnQuery = columnQuery;
	}
	public String getLookupColumn() {
		return lookupColumn;
	}
	public void setLookupColumn(String lookupColumn) {
		this.lookupColumn = lookupColumn;
	}
	public String getLookupTable() {
		return lookupTable;
	}
	public void setLookupTable(String lookupTable) {
		this.lookupTable = lookupTable;
	}
	public String getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getStrategyDesc() {
		return strategyDesc;
	}
	public void setStrategyDesc(String strategyDesc) {
		this.strategyDesc = strategyDesc;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getRegexpPattern() {
		return regexpPattern;
	}
	public void setRegexpPattern(String regexpPattern) {
		this.regexpPattern = regexpPattern;
	}
	public String getNullFlag() {
		return nullFlag;
	}
	public void setNullFlag(String nullFlag) {
		this.nullFlag = nullFlag;
	}
	public String getRefCorrectionColumn() {
		return refCorrectionColumn;
	}
	public void setRefCorrectionColumn(String refCorrectionColumn) {
		this.refCorrectionColumn = refCorrectionColumn;
	}
	public String getRefCorrectColumn() {
		return refCorrectColumn;
	}
	public void setRefCorrectColumn(String refCorrectColumn) {
		this.refCorrectColumn = refCorrectColumn;
	}
	public String getCustomeCurrectionText() {
		return customeCurrectionText;
	}
	public void setCustomeCurrectionText(String customeCurrectionText) {
		this.customeCurrectionText = customeCurrectionText;
	}
	public String getCustomeCurrectText() {
		return customeCurrectText;
	}
	public void setCustomeCurrectText(String customeCurrectText) {
		this.customeCurrectText = customeCurrectText;
	}
	public String getTrailLeadSpace() {
		return trailLeadSpace;
	}
	public void setTrailLeadSpace(String trailLeadSpace) {
		this.trailLeadSpace = trailLeadSpace;
	}
	public String getMultipleSpace() {
		return multipleSpace;
	}
	public void setMultipleSpace(String multipleSpace) {
		this.multipleSpace = multipleSpace;
	}
	public String getScope() {
		return Scope;
	}
	public void setScope(String scope) {
		Scope = scope;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public int getColumnStatusNt() {
		return columnStatusNt;
	}
	public void setColumnStatusNt(int columnStatusNt) {
		this.columnStatusNt = columnStatusNt;
	}
	public int getColumnStatus() {
		return columnStatus;
	}
	public void setColumnStatus(int columnStatus) {
		this.columnStatus = columnStatus;
	}
}