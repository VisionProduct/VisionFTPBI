package com.vision.vb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DashboardTilesVb extends CommonVb implements Serializable{

	private static final long serialVersionUID = 1L;
	private String dashboardId = "";
	private String tabId = "";
	private String tileId= "";
	private String tileCaption= "";
	private int tileSequence= 0;
	private int detailTileSequence= 0;
	private String tileType= "";
	private String columns= "";
	private Boolean drillDownFlag = false;
	private Boolean exportFlag = false;
	private String queryId= "";
	private String drillDownRef= "";
	private String propId = "";
	private String propValue = "";
	private String chartType = "";
	private String propertyAttr = "";
	private int placeHolderCnt = 0;
	private String doubleWidthFlag ="N";
	private String subTiles = "N";
	private String subSequence = "1";
	
	private String csvDelimiter = "\t";
	String tileDataSet = "";
	String chartDataSet = "";
	List gridColumnSet = null;
	List gridDataSet = null;
	List gridColumnFormats = null;
	
	private String promptValue1 ="";
	private String promptValue2 ="";
	private String promptValue3 ="";
	private String promptValue4 ="";
	private String promptValue5 ="";
	private String promptValue6 ="";
	private String promptValue7 ="";
	private String promptValue8 ="";
	private String promptValue9 ="";
	private String promptValue10 ="";
	private List<DashboardTilesVb> childTileslst = new ArrayList<DashboardTilesVb>();
	private List<DashboardTilesVb> drillDownlst = new ArrayList<DashboardTilesVb>();
	private List<ReportFilterVb> reportFilterlst = new ArrayList<ReportFilterVb>();
	
	private String subTileId= "";
	private List<DashboardTilesVb> subTileslst = new ArrayList<DashboardTilesVb>();
	private int	transitionEffectAT = 0;
	private String transitionEffect = "";
	private int	themeAT = 0;
	private String theme = "";
	
	private String drillDownKey1 = "";
	private String drillDownKey2 = "";
	private String drillDownKey3 = "";
	private String drillDownKey4 = "";
	private String drillDownKey5 = "";
	private String drillDownKey6 = "";
	private String drillDownKey7 = "";
	private String drillDownKey8 = "";
	private String drillDownKey9 = "";
	private String drillDownKey10 = "";
	private String drillDownKey0 = "";
	
	private String dataFilter1 = "";
	private String dataFilter2 = ""; 
	private String dataFilter3 =  "";
	private String dataFilter4 =  "";
	private String dataFilter5 =  "";
	private String dataFilter6 =  "";
	private String dataFilter7 =  "";
	private String dataFilter8 =  "";
	private String dataFilter9 = "";
	private String dataFilter10 = "";
	private String scalingFactor = "0";
	private String filterPosition = "";
	private PromptTreeVb promptTree = null;
	private String dbConnection= "";
	private Boolean isDrillDown = false;
	private int parentSequence= 0;
	private String promptLabel = "";
	private String applicationTheme = "";
	private List chartList  = new ArrayList<>();
	private String savedChart  = "";
	private List<SingletonTileVb> placeHolderLst = new ArrayList<SingletonTileVb>(); 
	
	
	private String tileTable1 = "";
	private String tileTable2 = "";
	private String tileTable3 = "";
	private String tileTable4 = "";
	private String tileTable5 = "";
	private String tileTable6 = "";
	private String tileTable7 = "";
	private String tileTable8 = "";
	private String tileTable9 = "";
	private String tileTable10 = "";
	
	private String filterRefCode = "";
	private String localFilter1 = "";
	private String localFilter2 = ""; 
	private String localFilter3 =  "";
	private String localFilter4 =  "";
	private String localFilter5 =  "";
	private String localFilter6 =  "";
	private String localFilter7 =  "";
	private String localFilter8 =  "";
	private String localFilter9 = "";
	private String localFilter10 = "";
	
	private String clientId = "";
	private String projectId = "";
	private String profilerId = "";
	private String connectorId = "";
	private String dqTableName = "";
	
	
	public String getTileType() {
		return tileType;
	}
	public void setTileType(String tileType) {
		this.tileType = tileType;
	}
	public String getTileId() {
		return tileId;
	}
	public void setTileId(String tileId) {
		this.tileId = tileId;
	}
	public String getTileCaption() {
		return tileCaption;
	}
	public void setTileCaption(String tileCaption) {
		this.tileCaption = tileCaption;
	}
	public int getTileSequence() {
		return tileSequence;
	}
	public void setTileSequence(int tileSequence) {
		this.tileSequence = tileSequence;
	}
	public Boolean getDrillDownFlag() {
		return drillDownFlag;
	}
	public void setDrillDownFlag(Boolean drillDownFlag) {
		this.drillDownFlag = drillDownFlag;
	}
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getPropId() {
		return propId;
	}
	public void setPropId(String propId) {
		this.propId = propId;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public String getDrillDownRef() {
		return drillDownRef;
	}
	public void setDrillDownRef(String drillDownRef) {
		this.drillDownRef = drillDownRef;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public List<DashboardTilesVb> getDrillDownlst() {
		return drillDownlst;
	}
	public void setDrillDownlst(List<DashboardTilesVb> drillDownlst) {
		this.drillDownlst = drillDownlst;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getPropertyAttr() {
		return propertyAttr;
	}
	public void setPropertyAttr(String propertyAttr) {
		this.propertyAttr = propertyAttr;
	}
	public int getDetailTileSequence() {
		return detailTileSequence;
	}
	public void setDetailTileSequence(int detailTileSequence) {
		this.detailTileSequence = detailTileSequence;
	}
	public int getPlaceHolderCnt() {
		return placeHolderCnt;
	}
	public void setPlaceHolderCnt(int placeHolderCnt) {
		this.placeHolderCnt = placeHolderCnt;
	}
	public String getTileDataSet() {
		return tileDataSet;
	}
	public void setTileDataSet(String tileDataSet) {
		this.tileDataSet = tileDataSet;
	}
	public String getChartDataSet() {
		return chartDataSet;
	}
	public void setChartDataSet(String chartDataSet) {
		this.chartDataSet = chartDataSet;
	}
	public String getDoubleWidthFlag() {
		return doubleWidthFlag;
	}
	public void setDoubleWidthFlag(String doubleWidthFlag) {
		this.doubleWidthFlag = doubleWidthFlag;
	}
	public List getGridDataSet() {
		return gridDataSet;
	}
	public void setGridDataSet(List gridDataSet) {
		this.gridDataSet = gridDataSet;
	}
	public List getGridColumnSet() {
		return gridColumnSet;
	}
	public void setGridColumnSet(List gridColumnSet) {
		this.gridColumnSet = gridColumnSet;
	}
	public List getGridColumnFormats() {
		return gridColumnFormats;
	}
	public void setGridColumnFormats(List gridColumnFormats) {
		this.gridColumnFormats = gridColumnFormats;
	}
	public String getPromptValue1() {
		return promptValue1;
	}
	public void setPromptValue1(String promptValue1) {
		this.promptValue1 = promptValue1;
	}
	public String getPromptValue2() {
		return promptValue2;
	}
	public void setPromptValue2(String promptValue2) {
		this.promptValue2 = promptValue2;
	}
	public String getPromptValue3() {
		return promptValue3;
	}
	public void setPromptValue3(String promptValue3) {
		this.promptValue3 = promptValue3;
	}
	public String getPromptValue4() {
		return promptValue4;
	}
	public void setPromptValue4(String promptValue4) {
		this.promptValue4 = promptValue4;
	}
	public String getPromptValue5() {
		return promptValue5;
	}
	public void setPromptValue5(String promptValue5) {
		this.promptValue5 = promptValue5;
	}
	public String getPromptValue6() {
		return promptValue6;
	}
	public void setPromptValue6(String promptValue6) {
		this.promptValue6 = promptValue6;
	}
	public String getPromptValue7() {
		return promptValue7;
	}
	public void setPromptValue7(String promptValue7) {
		this.promptValue7 = promptValue7;
	}
	public String getPromptValue8() {
		return promptValue8;
	}
	public void setPromptValue8(String promptValue8) {
		this.promptValue8 = promptValue8;
	}
	public String getPromptValue9() {
		return promptValue9;
	}
	public void setPromptValue9(String promptValue9) {
		this.promptValue9 = promptValue9;
	}
	public String getPromptValue10() {
		return promptValue10;
	}
	public void setPromptValue10(String promptValue10) {
		this.promptValue10 = promptValue10;
	}
	public List<DashboardTilesVb> getChildTileslst() {
		return childTileslst;
	}
	public void setChildTileslst(List<DashboardTilesVb> childTileslst) {
		this.childTileslst = childTileslst;
	}
	public String getDashboardId() {
		return dashboardId;
	}
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getSubTiles() {
		return subTiles;
	}
	public void setSubTiles(String subTiles) {
		this.subTiles = subTiles;
	}
	public String getSubSequence() {
		return subSequence;
	}
	public void setSubSequence(String subSequence) {
		this.subSequence = subSequence;
	}
	public String getSubTileId() {
		return subTileId;
	}
	public void setSubTileId(String subTileId) {
		this.subTileId = subTileId;
	}
	public List<DashboardTilesVb> getSubTileslst() {
		return subTileslst;
	}
	public void setSubTileslst(List<DashboardTilesVb> subTileslst) {
		this.subTileslst = subTileslst;
	}
	public int getTransitionEffectAT() {
		return transitionEffectAT;
	}
	public void setTransitionEffectAT(int transitionEffectAT) {
		this.transitionEffectAT = transitionEffectAT;
	}
	public String getTransitionEffect() {
		return transitionEffect;
	}
	public void setTransitionEffect(String transitionEffect) {
		this.transitionEffect = transitionEffect;
	}
	public int getThemeAT() {
		return themeAT;
	}
	public void setThemeAT(int themeAT) {
		this.themeAT = themeAT;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getDrillDownKey1() {
		return drillDownKey1;
	}
	public void setDrillDownKey1(String drillDownKey1) {
		this.drillDownKey1 = drillDownKey1;
	}
	public String getDrillDownKey2() {
		return drillDownKey2;
	}
	public void setDrillDownKey2(String drillDownKey2) {
		this.drillDownKey2 = drillDownKey2;
	}
	public String getDrillDownKey3() {
		return drillDownKey3;
	}
	public void setDrillDownKey3(String drillDownKey3) {
		this.drillDownKey3 = drillDownKey3;
	}
	public String getDrillDownKey4() {
		return drillDownKey4;
	}
	public void setDrillDownKey4(String drillDownKey4) {
		this.drillDownKey4 = drillDownKey4;
	}
	public String getDrillDownKey5() {
		return drillDownKey5;
	}
	public void setDrillDownKey5(String drillDownKey5) {
		this.drillDownKey5 = drillDownKey5;
	}
	public String getDrillDownKey6() {
		return drillDownKey6;
	}
	public void setDrillDownKey6(String drillDownKey6) {
		this.drillDownKey6 = drillDownKey6;
	}
	public String getDrillDownKey7() {
		return drillDownKey7;
	}
	public void setDrillDownKey7(String drillDownKey7) {
		this.drillDownKey7 = drillDownKey7;
	}
	public String getDrillDownKey8() {
		return drillDownKey8;
	}
	public void setDrillDownKey8(String drillDownKey8) {
		this.drillDownKey8 = drillDownKey8;
	}
	public String getDrillDownKey9() {
		return drillDownKey9;
	}
	public void setDrillDownKey9(String drillDownKey9) {
		this.drillDownKey9 = drillDownKey9;
	}
	public String getDrillDownKey10() {
		return drillDownKey10;
	}
	public void setDrillDownKey10(String drillDownKey10) {
		this.drillDownKey10 = drillDownKey10;
	}
	public String getDrillDownKey0() {
		return drillDownKey0;
	}
	public void setDrillDownKey0(String drillDownKey0) {
		this.drillDownKey0 = drillDownKey0;
	}
	public String getDataFilter1() {
		return dataFilter1;
	}
	public void setDataFilter1(String dataFilter1) {
		this.dataFilter1 = dataFilter1;
	}
	public String getDataFilter2() {
		return dataFilter2;
	}
	public void setDataFilter2(String dataFilter2) {
		this.dataFilter2 = dataFilter2;
	}
	public String getDataFilter3() {
		return dataFilter3;
	}
	public void setDataFilter3(String dataFilter3) {
		this.dataFilter3 = dataFilter3;
	}
	public String getDataFilter4() {
		return dataFilter4;
	}
	public void setDataFilter4(String dataFilter4) {
		this.dataFilter4 = dataFilter4;
	}
	public String getDataFilter5() {
		return dataFilter5;
	}
	public void setDataFilter5(String dataFilter5) {
		this.dataFilter5 = dataFilter5;
	}
	public String getDataFilter6() {
		return dataFilter6;
	}
	public void setDataFilter6(String dataFilter6) {
		this.dataFilter6 = dataFilter6;
	}
	public String getDataFilter7() {
		return dataFilter7;
	}
	public void setDataFilter7(String dataFilter7) {
		this.dataFilter7 = dataFilter7;
	}
	public String getDataFilter8() {
		return dataFilter8;
	}
	public void setDataFilter8(String dataFilter8) {
		this.dataFilter8 = dataFilter8;
	}
	public String getDataFilter9() {
		return dataFilter9;
	}
	public void setDataFilter9(String dataFilter9) {
		this.dataFilter9 = dataFilter9;
	}
	public String getDataFilter10() {
		return dataFilter10;
	}
	public void setDataFilter10(String dataFilter10) {
		this.dataFilter10 = dataFilter10;
	}
	public String getScalingFactor() {
		return scalingFactor;
	}
	public void setScalingFactor(String scalingFactor) {
		this.scalingFactor = scalingFactor;
	}
	public String getFilterPosition() {
		return filterPosition;
	}
	public void setFilterPosition(String filterPosition) {
		this.filterPosition = filterPosition;
	}
	public PromptTreeVb getPromptTree() {
		return promptTree;
	}
	public void setPromptTree(PromptTreeVb promptTree) {
		this.promptTree = promptTree;
	}
	public String getDbConnection() {
		return dbConnection;
	}
	public void setDbConnection(String dbConnection) {
		this.dbConnection = dbConnection;
	}
	public Boolean getIsDrillDown() {
		return isDrillDown;
	}
	public void setIsDrillDown(Boolean isDrillDown) {
		this.isDrillDown = isDrillDown;
	}
	public int getParentSequence() {
		return parentSequence;
	}
	public void setParentSequence(int parentSequence) {
		this.parentSequence = parentSequence;
	}
	public String getPromptLabel() {
		return promptLabel;
	}
	public void setPromptLabel(String promptLabel) {
		this.promptLabel = promptLabel;
	}
	public String getApplicationTheme() {
		return applicationTheme;
	}
	public void setApplicationTheme(String applicationTheme) {
		this.applicationTheme = applicationTheme;
	}
	public List getChartList() {
		return chartList;
	}
	public void setChartList(List chartList) {
		this.chartList = chartList;
	}
	public String getSavedChart() {
		return savedChart;
	}
	public void setSavedChart(String savedChart) {
		this.savedChart = savedChart;
	}
	public List<SingletonTileVb> getPlaceHolderLst() {
		return placeHolderLst;
	}
	public void setPlaceHolderLst(List<SingletonTileVb> placeHolderLst) {
		this.placeHolderLst = placeHolderLst;
	}
	public String getTileTable1() {
		return tileTable1;
	}
	public void setTileTable1(String tileTable1) {
		this.tileTable1 = tileTable1;
	}
	public String getTileTable2() {
		return tileTable2;
	}
	public void setTileTable2(String tileTable2) {
		this.tileTable2 = tileTable2;
	}
	public String getTileTable3() {
		return tileTable3;
	}
	public void setTileTable3(String tileTable3) {
		this.tileTable3 = tileTable3;
	}
	public String getTileTable4() {
		return tileTable4;
	}
	public void setTileTable4(String tileTable4) {
		this.tileTable4 = tileTable4;
	}
	public String getTileTable5() {
		return tileTable5;
	}
	public void setTileTable5(String tileTable5) {
		this.tileTable5 = tileTable5;
	}
	public String getTileTable6() {
		return tileTable6;
	}
	public void setTileTable6(String tileTable6) {
		this.tileTable6 = tileTable6;
	}
	public String getTileTable7() {
		return tileTable7;
	}
	public void setTileTable7(String tileTable7) {
		this.tileTable7 = tileTable7;
	}
	public String getTileTable8() {
		return tileTable8;
	}
	public void setTileTable8(String tileTable8) {
		this.tileTable8 = tileTable8;
	}
	public String getTileTable9() {
		return tileTable9;
	}
	public void setTileTable9(String tileTable9) {
		this.tileTable9 = tileTable9;
	}
	public String getTileTable10() {
		return tileTable10;
	}
	public void setTileTable10(String tileTable10) {
		this.tileTable10 = tileTable10;
	}
	public String getFilterRefCode() {
		return filterRefCode;
	}
	public void setFilterRefCode(String filterRefCode) {
		this.filterRefCode = filterRefCode;
	}
	public String getLocalFilter1() {
		return localFilter1;
	}
	public void setLocalFilter1(String localFilter1) {
		this.localFilter1 = localFilter1;
	}
	public String getLocalFilter2() {
		return localFilter2;
	}
	public void setLocalFilter2(String localFilter2) {
		this.localFilter2 = localFilter2;
	}
	public String getLocalFilter3() {
		return localFilter3;
	}
	public void setLocalFilter3(String localFilter3) {
		this.localFilter3 = localFilter3;
	}
	public String getLocalFilter4() {
		return localFilter4;
	}
	public void setLocalFilter4(String localFilter4) {
		this.localFilter4 = localFilter4;
	}
	public String getLocalFilter5() {
		return localFilter5;
	}
	public void setLocalFilter5(String localFilter5) {
		this.localFilter5 = localFilter5;
	}
	public String getLocalFilter6() {
		return localFilter6;
	}
	public void setLocalFilter6(String localFilter6) {
		this.localFilter6 = localFilter6;
	}
	public String getLocalFilter7() {
		return localFilter7;
	}
	public void setLocalFilter7(String localFilter7) {
		this.localFilter7 = localFilter7;
	}
	public String getLocalFilter8() {
		return localFilter8;
	}
	public void setLocalFilter8(String localFilter8) {
		this.localFilter8 = localFilter8;
	}
	public String getLocalFilter9() {
		return localFilter9;
	}
	public void setLocalFilter9(String localFilter9) {
		this.localFilter9 = localFilter9;
	}
	public String getLocalFilter10() {
		return localFilter10;
	}
	public void setLocalFilter10(String localFilter10) {
		this.localFilter10 = localFilter10;
	}
	public List<ReportFilterVb> getReportFilterlst() {
		return reportFilterlst;
	}
	public void setReportFilterlst(List<ReportFilterVb> reportFilterlst) {
		this.reportFilterlst = reportFilterlst;
	}
	public Boolean getExportFlag() {
		return exportFlag;
	}
	public void setExportFlag(Boolean exportFlag) {
		this.exportFlag = exportFlag;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProfilerId() {
		return profilerId;
	}
	public void setProfilerId(String profilerId) {
		this.profilerId = profilerId;
	}
	public String getConnectorId() {
		return connectorId;
	}
	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}
	public String getDqTableName() {
		return dqTableName;
	}
	public void setDqTableName(String dqTableName) {
		this.dqTableName = dqTableName;
	}
	public String getCsvDelimiter() {
		return csvDelimiter;
	}
	public void setCsvDelimiter(String csvDelimiter) {
		this.csvDelimiter = csvDelimiter;
	}
}
