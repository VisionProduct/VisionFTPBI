package com.vision.vb;

import java.util.ArrayList;
import java.util.List;

public class MappletTransformationsVb extends CommonVb {

	private static final long serialVersionUID = -2010835009684844752L;
	private int parentSequence = 0;
	private String separator = "-1";
	private ArrayList<MappletTransformationsVb> children = null;
	
	private ArrayList<MappletTransformationsVb> transformationsLinkerChildren = null;

	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private String country = "";
	private String leBook = "";
	private String projectId = "";
	private String transTableName = "";
	private String transHierarchyId = "";
	private String transTypeAt = "803";
	private String transType = "";
	private String transMappletId = "";
	private String transName = "";
	private String transDescription = "";
	private String mappingId = "";
	private String mappletHierarchyId = "";
	private String transformationId = "";
	private int mappingStatusNt = 1;
	private int mappingStatus = -1;
	private String transScript = "";
	private String inputCount = "";
	private String outCount = "";


	// Insert Values for DQ_Pj_Ws_Lk
	private String ParentHierarchyId = "";
	private int ParentHierarchyStatusNt = 302;
	private int ParentHierarchyStatus = 0;

	// Filter Query validations
	private String filterQuery = "";
	private String queryField = "";

	//
	
	
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
	private String status = "";
	private String errorMessage = "";
	private String  columnIdMT ="0";
	private String sortCondition = "";

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

	public ArrayList<MappletTransformationsVb> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<MappletTransformationsVb> children) {
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

	public String getTransTableName() {
		return transTableName;
	}

	public void setTransTableName(String transTableName) {
		this.transTableName = transTableName;
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

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getTransDescription() {
		return transDescription;
	}

	public void setTransDescription(String transDescription) {
		this.transDescription = transDescription;
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

	public String getParentHierarchyId() {
		return ParentHierarchyId;
	}

	public void setParentHierarchyId(String parentHierarchyId) {
		ParentHierarchyId = parentHierarchyId;
	}

	public int getParentHierarchyStatusNt() {
		return ParentHierarchyStatusNt;
	}

	public void setParentHierarchyStatusNt(int parentHierarchyStatusNt) {
		ParentHierarchyStatusNt = parentHierarchyStatusNt;
	}

	public int getParentHierarchyStatus() {
		return ParentHierarchyStatus;
	}

	public void setParentHierarchyStatus(int parentHierarchyStatus) {
		ParentHierarchyStatus = parentHierarchyStatus;
	}

	public String getFilterQuery() {
		return filterQuery;
	}

	public void setFilterQuery(String filterQuery) {
		this.filterQuery = filterQuery;
	}

	public String getQueryField() {
		return queryField;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}


	public String getColumnSeq() {
		return columnSeq;
	}

	public String getColumnId() {
		return columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getDrivedColumn() {
		return drivedColumn;
	}

	public String getColumnIOTypeAt() {
		return columnIOTypeAt;
	}

	public String getColumnIOType() {
		return columnIOType;
	}

	public String getColumnQuery() {
		return columnQuery;
	}

	public String getLookupColumn() {
		return lookupColumn;
	}

	public String getLookupTable() {
		return lookupTable;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public String getStrategyDesc() {
		return strategyDesc;
	}

	public String getCondition() {
		return condition;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getRegexpPattern() {
		return regexpPattern;
	}

	public String getNullFlag() {
		return nullFlag;
	}

	public String getRefCorrectionColumn() {
		return refCorrectionColumn;
	}

	public String getRefCorrectColumn() {
		return refCorrectColumn;
	}

	public String getCustomeCurrectionText() {
		return customeCurrectionText;
	}

	public String getCustomeCurrectText() {
		return customeCurrectText;
	}

	public String getTrailLeadSpace() {
		return trailLeadSpace;
	}

	public String getMultipleSpace() {
		return multipleSpace;
	}

	public String getScope() {
		return Scope;
	}

	public String getCaseType() {
		return caseType;
	}

	public int getColumnStatusNt() {
		return columnStatusNt;
	}

	public int getColumnStatus() {
		return columnStatus;
	}

	public void setColumnSeq(String columnSeq) {
		this.columnSeq = columnSeq;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setDrivedColumn(String drivedColumn) {
		this.drivedColumn = drivedColumn;
	}

	public void setColumnIOTypeAt(String columnIOTypeAt) {
		this.columnIOTypeAt = columnIOTypeAt;
	}

	public void setColumnIOType(String columnIOType) {
		this.columnIOType = columnIOType;
	}

	public void setColumnQuery(String columnQuery) {
		this.columnQuery = columnQuery;
	}

	public void setLookupColumn(String lookupColumn) {
		this.lookupColumn = lookupColumn;
	}

	public void setLookupTable(String lookupTable) {
		this.lookupTable = lookupTable;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public void setStrategyDesc(String strategyDesc) {
		this.strategyDesc = strategyDesc;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setRegexpPattern(String regexpPattern) {
		this.regexpPattern = regexpPattern;
	}

	public void setNullFlag(String nullFlag) {
		this.nullFlag = nullFlag;
	}

	public void setRefCorrectionColumn(String refCorrectionColumn) {
		this.refCorrectionColumn = refCorrectionColumn;
	}

	public void setRefCorrectColumn(String refCorrectColumn) {
		this.refCorrectColumn = refCorrectColumn;
	}

	public void setCustomeCurrectionText(String customeCurrectionText) {
		this.customeCurrectionText = customeCurrectionText;
	}

	public void setCustomeCurrectText(String customeCurrectText) {
		this.customeCurrectText = customeCurrectText;
	}

	public void setTrailLeadSpace(String trailLeadSpace) {
		this.trailLeadSpace = trailLeadSpace;
	}

	public void setMultipleSpace(String multipleSpace) {
		this.multipleSpace = multipleSpace;
	}

	public void setScope(String scope) {
		Scope = scope;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public void setColumnStatusNt(int columnStatusNt) {
		this.columnStatusNt = columnStatusNt;
	}

	public void setColumnStatus(int columnStatus) {
		this.columnStatus = columnStatus;
	}

	public ArrayList<MappletTransformationsVb> getTransformationsLinkerChildren() {
		return transformationsLinkerChildren;
	}

	public void setTransformationsLinkerChildren(ArrayList<MappletTransformationsVb> transformationsLinkerChildren) {
		this.transformationsLinkerChildren = transformationsLinkerChildren;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getColumnIdMT() {
		return columnIdMT;
	}

	public void setColumnIdMT(String columnIdMT) {
		this.columnIdMT = columnIdMT;
	}

	public String getTransScript() {
		return transScript;
	}

	public void setTransScript(String transScript) {
		this.transScript = transScript;
	}

	public String getInputCount() {
		return inputCount;
	}

	public void setInputCount(String inputCount) {
		this.inputCount = inputCount;
	}
	public String getOutCount() {
		return outCount;
	}

	public void setOutCount(String outCount) {
		this.outCount = outCount;
	}

	public String getSortCondition() {
		return sortCondition;
	}

	public void setSortCondition(String sortCondition) {
		this.sortCondition = sortCondition;
	}

}