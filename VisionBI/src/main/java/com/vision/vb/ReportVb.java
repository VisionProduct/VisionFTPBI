package com.vision.vb;

import java.util.ArrayList;

public class ReportVb extends CommonVb {
	private static final long serialVersionUID = 897039568057874596L;
	private String country;
	private String leBook;
	private String projectId;
	private String mappingId;
	private String reportId;
	private String reportName;
	private int posSeq = 0;
	private String sourceTable = "";
	private String sourceColumnName;
	private String dqTableName = "";
	private String totalRecord;
	private String cDimensionUniverse;
	private String cPassedIdCount;
	private String cPassedPercent;
	private String cFailedIdCount;
	private String cFailedPercent;
	private String cDpmo;
	private String cSixSigmaRating;
	private String aDimensionUniverse;
	private String aPassedIdCount;
	private String aPassedPercent;
	private String aFailedIdCount;
	private String aFailedPercent;
	private String aDpmo;
	private String aSixSigmaRating;
	private String cnDimensionUniverse;
	private String cnPassedIdCount;
	private String cnPassedPercent;
	private String cnFailedIdCount;
	private String cnFailedPercent;
	private String cnDpmo;
	private String cnSixSigmaRating;
	private String uDimensionUniverse;
	private String uPassedIdCount;
	private String uPassedPercent;
	private String uFailedIdCount;
	private String uFailedPercent;
	private String uDpmo;
	private String uSixSigmaRating;
	private String csDimensionUniverse;
	private String csPassedIdCount;
	private String csPassedPercent;
	private String csFailedIdCount;
	private String csFailedPercent;
	private String csDpmo;
	private String csSixSigmaRating;
	private String oPassedIdCount;
	private String oPassedPercent;
	private String oFailedIdCount;
	private int oFailedPercent = 0;
	private int oSixSigmaRating = 0;
	private int numSubTab = 320;
	private String errormessage;
	private int dimensionNt = 320;
	private int dimension;
	private int dependDimension;
	private String primaryKeyColumns ;
	private String errorTableColList;

	private int reportTypeNt = 322;
	private int reportType = 1;
	private int reportStatusNt = 321;
	private int reportStatus;
	private String dqLogicalTableName;

	private String column_names;
	private String criteria;
	private String inputvalue;
	private String condition;
	private ArrayList<SmartSearchVb> searchTableschild = null;
	private String columnName = "";

	private String dimColumnName = "";
	private String errorDesc = "";
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
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


	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}


	public ArrayList<SmartSearchVb> getSearchTableschild() {
		return searchTableschild;
	}

	public void setSearchTableschild(ArrayList<SmartSearchVb> searchTableschild) {
		this.searchTableschild = searchTableschild;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getPosSeq() {
		return posSeq;
	}

	public void setPosSeq(int posSeq) {
		this.posSeq = posSeq;
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

	public String getSourceColumnName() {
		return sourceColumnName;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public String getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}

	public String getcDimensionUniverse() {
		return cDimensionUniverse;
	}

	public void setcDimensionUniverse(String cDimensionUniverse) {
		this.cDimensionUniverse = cDimensionUniverse;
	}

	public String getcPassedIdCount() {
		return cPassedIdCount;
	}

	public void setcPassedIdCount(String cPassedIdCount) {
		this.cPassedIdCount = cPassedIdCount;
	}

	public String getcPassedPercent() {
		return cPassedPercent;
	}

	public void setcPassedPercent(String cPassedPercent) {
		this.cPassedPercent = cPassedPercent;
	}

	public String getcFailedIdCount() {
		return cFailedIdCount;
	}

	public void setcFailedIdCount(String cFailedIdCount) {
		this.cFailedIdCount = cFailedIdCount;
	}

	public String getcFailedPercent() {
		return cFailedPercent;
	}

	public void setcFailedPercent(String cFailedPercent) {
		this.cFailedPercent = cFailedPercent;
	}

	public String getcDpmo() {
		return cDpmo;
	}

	public void setcDpmo(String cDpmo) {
		this.cDpmo = cDpmo;
	}

	public String getcSixSigmaRating() {
		return cSixSigmaRating;
	}

	public void setcSixSigmaRating(String cSixSigmaRating) {
		this.cSixSigmaRating = cSixSigmaRating;
	}

	public String getaDimensionUniverse() {
		return aDimensionUniverse;
	}

	public void setaDimensionUniverse(String aDimensionUniverse) {
		this.aDimensionUniverse = aDimensionUniverse;
	}

	public String getaPassedIdCount() {
		return aPassedIdCount;
	}

	public void setaPassedIdCount(String aPassedIdCount) {
		this.aPassedIdCount = aPassedIdCount;
	}

	public String getaPassedPercent() {
		return aPassedPercent;
	}

	public void setaPassedPercent(String aPassedPercent) {
		this.aPassedPercent = aPassedPercent;
	}

	public String getaFailedIdCount() {
		return aFailedIdCount;
	}

	public void setaFailedIdCount(String aFailedIdCount) {
		this.aFailedIdCount = aFailedIdCount;
	}

	public String getaFailedPercent() {
		return aFailedPercent;
	}

	public void setaFailedPercent(String aFailedPercent) {
		this.aFailedPercent = aFailedPercent;
	}

	public String getaDpmo() {
		return aDpmo;
	}

	public void setaDpmo(String aDpmo) {
		this.aDpmo = aDpmo;
	}

	public String getaSixSigmaRating() {
		return aSixSigmaRating;
	}

	public void setaSixSigmaRating(String aSixSigmaRating) {
		this.aSixSigmaRating = aSixSigmaRating;
	}

	public String getCnDimensionUniverse() {
		return cnDimensionUniverse;
	}

	public void setCnDimensionUniverse(String cnDimensionUniverse) {
		this.cnDimensionUniverse = cnDimensionUniverse;
	}

	public String getCnPassedIdCount() {
		return cnPassedIdCount;
	}

	public void setCnPassedIdCount(String cnPassedIdCount) {
		this.cnPassedIdCount = cnPassedIdCount;
	}

	public String getCnPassedPercent() {
		return cnPassedPercent;
	}

	public void setCnPassedPercent(String cnPassedPercent) {
		this.cnPassedPercent = cnPassedPercent;
	}

	public String getCnFailedIdCount() {
		return cnFailedIdCount;
	}

	public void setCnFailedIdCount(String cnFailedIdCount) {
		this.cnFailedIdCount = cnFailedIdCount;
	}

	public String getCnFailedPercent() {
		return cnFailedPercent;
	}

	public void setCnFailedPercent(String cnFailedPercent) {
		this.cnFailedPercent = cnFailedPercent;
	}

	public String getCnDpmo() {
		return cnDpmo;
	}

	public void setCnDpmo(String cnDpmo) {
		this.cnDpmo = cnDpmo;
	}

	public String getCnSixSigmaRating() {
		return cnSixSigmaRating;
	}

	public void setCnSixSigmaRating(String cnSixSigmaRating) {
		this.cnSixSigmaRating = cnSixSigmaRating;
	}

	public String getuDimensionUniverse() {
		return uDimensionUniverse;
	}

	public void setuDimensionUniverse(String uDimensionUniverse) {
		this.uDimensionUniverse = uDimensionUniverse;
	}

	public String getuPassedIdCount() {
		return uPassedIdCount;
	}

	public void setuPassedIdCount(String uPassedIdCount) {
		this.uPassedIdCount = uPassedIdCount;
	}

	public String getuPassedPercent() {
		return uPassedPercent;
	}

	public void setuPassedPercent(String uPassedPercent) {
		this.uPassedPercent = uPassedPercent;
	}

	public String getuFailedIdCount() {
		return uFailedIdCount;
	}

	public void setuFailedIdCount(String uFailedIdCount) {
		this.uFailedIdCount = uFailedIdCount;
	}

	public String getuFailedPercent() {
		return uFailedPercent;
	}

	public void setuFailedPercent(String uFailedPercent) {
		this.uFailedPercent = uFailedPercent;
	}

	public String getuDpmo() {
		return uDpmo;
	}

	public void setuDpmo(String uDpmo) {
		this.uDpmo = uDpmo;
	}

	public String getuSixSigmaRating() {
		return uSixSigmaRating;
	}

	public void setuSixSigmaRating(String uSixSigmaRating) {
		this.uSixSigmaRating = uSixSigmaRating;
	}

	public String getCsDimensionUniverse() {
		return csDimensionUniverse;
	}

	public void setCsDimensionUniverse(String csDimensionUniverse) {
		this.csDimensionUniverse = csDimensionUniverse;
	}

	public String getCsPassedIdCount() {
		return csPassedIdCount;
	}

	public void setCsPassedIdCount(String csPassedIdCount) {
		this.csPassedIdCount = csPassedIdCount;
	}

	public String getCsPassedPercent() {
		return csPassedPercent;
	}

	public void setCsPassedPercent(String csPassedPercent) {
		this.csPassedPercent = csPassedPercent;
	}

	public String getCsFailedIdCount() {
		return csFailedIdCount;
	}

	public void setCsFailedIdCount(String csFailedIdCount) {
		this.csFailedIdCount = csFailedIdCount;
	}

	public String getCsFailedPercent() {
		return csFailedPercent;
	}

	public void setCsFailedPercent(String csFailedPercent) {
		this.csFailedPercent = csFailedPercent;
	}

	public String getCsDpmo() {
		return csDpmo;
	}

	public void setCsDpmo(String csDpmo) {
		this.csDpmo = csDpmo;
	}

	public String getCsSixSigmaRating() {
		return csSixSigmaRating;
	}

	public void setCsSixSigmaRating(String csSixSigmaRating) {
		this.csSixSigmaRating = csSixSigmaRating;
	}

	public String getoPassedIdCount() {
		return oPassedIdCount;
	}

	public void setoPassedIdCount(String oPassedIdCount) {
		this.oPassedIdCount = oPassedIdCount;
	}

	public String getoPassedPercent() {
		return oPassedPercent;
	}

	public void setoPassedPercent(String oPassedPercent) {
		this.oPassedPercent = oPassedPercent;
	}

	public String getoFailedIdCount() {
		return oFailedIdCount;
	}

	public void setoFailedIdCount(String oFailedIdCount) {
		this.oFailedIdCount = oFailedIdCount;
	}

	public int getoFailedPercent() {
		return oFailedPercent;
	}

	public void setoFailedPercent(int oFailedPercent) {
		this.oFailedPercent = oFailedPercent;
	}

	public int getoSixSigmaRating() {
		return oSixSigmaRating;
	}

	public void setoSixSigmaRating(int oSixSigmaRating) {
		this.oSixSigmaRating = oSixSigmaRating;
	}

	public String getDqTableName() {
		return dqTableName;
	}

	public void setDqTableName(String dqTableName) {
		this.dqTableName = dqTableName;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public int getNumSubTab() {
		return numSubTab;
	}

	public void setNumSubTab(int numSubTab) {
		this.numSubTab = numSubTab;
	}

	public int getDimensionNt() {
		return dimensionNt;
	}

	public void setDimensionNt(int dimensionNt) {
		this.dimensionNt = dimensionNt;
	}

	public int getDependDimension() {
		return dependDimension;
	}

	public void setDependDimension(int dependDimension) {
		this.dependDimension = dependDimension;
	}

	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getErrorTableColList() {
		return errorTableColList;
	}

	public void setErrorTableColList(String errorTableColList) {
		this.errorTableColList = errorTableColList;
	}

	public int getReportTypeNt() {
		return reportTypeNt;
	}

	public void setReportTypeNt(int reportTypeNt) {
		this.reportTypeNt = reportTypeNt;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public int getReportStatusNt() {
		return reportStatusNt;
	}

	public void setReportStatusNt(int reportStatusNt) {
		this.reportStatusNt = reportStatusNt;
	}

	public String getDqLogicalTableName() {
		return dqLogicalTableName;
	}

	public void setDqLogicalTableName(String dqLogicalTableName) {
		this.dqLogicalTableName = dqLogicalTableName;
	}

	public int getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(int reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public void setPrimaryKeyColumns(String primaryKeyColumns) {
		this.primaryKeyColumns = primaryKeyColumns;
	}

	public String getDimColumnName() {
		return dimColumnName;
	}

	public void setDimColumnName(String dimColumnName) {
		this.dimColumnName = dimColumnName;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	

}
