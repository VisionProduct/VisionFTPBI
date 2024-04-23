/**
 * Author : Deepak.s
 */
package com.vision.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import com.sun.rowset.CachedRowSetImpl;
import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.ChartUtils;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.ColumnHeadersVb;
import com.vision.vb.DashboarUserDefVb;
import com.vision.vb.DashboardFilterVb;
import com.vision.vb.DashboardTabVb;
import com.vision.vb.DashboardTilesVb;
import com.vision.vb.DashboardVb;
import com.vision.vb.ExportVb;
import com.vision.vb.PrdQueryConfig;
import com.vision.vb.PromptTreeVb;
import com.vision.vb.ReportsVb;
import com.vision.vb.SingletonTileVb;
import com.vision.vb.VisionUsersVb;
import com.vision.wb.ReportsWb;

@Component
public class DashboardsDao extends AbstractDao<DashboardVb> {
	@Autowired
	CommonDao commonDao;
	@Value("${app.clientName}")
	private String clientName;
	@Value("${app.productName}")
	private String productName;
	@Value("${app.databaseType}")
	private String databaseType;
	@Autowired
	ChartUtils chartUtils;
	@Autowired
	ReportsDao reportsDao;
	@Autowired
	CommonApiDao commonApiDao;

	public DashboardVb getDashboardDetail(DashboardVb dObj) {
		setServiceDefaults();
		DashboardVb dashboardVb = new DashboardVb();
		String query = "";
		try {
			if ("ORACLE".equalsIgnoreCase(databaseType)) {
				query = " SELECT t1.Dashboard_Id,Dashboard_Name,Total_Tabs,Data_Source_Ref,Filter,Filter_Ref_Code,"
						+ "  NVL((Select Min(t2.DS_Theme) from PRD_USER_Dashboard_Theme t2 where t2.dashboard_ID=t1.dashboard_ID and t2.vision_ID= ?),t1.DS_THEME) DS_THEME,"
						+ "  t1.DS_THEME_AT " + "  from PRD_DASHBOARDS t1 where Dashboard_ID = ? ";
			} else if ("MSSQL".equalsIgnoreCase(databaseType)) {
				query = " Select t1.Dashboard_Id,Dashboard_Name,Total_Tabs,Data_Source_Ref,Filter,Filter_Ref_Code,"
						+ "  isnull((Select Min(t2.DS_Theme) from PRD_USER_Dashboard_Theme t2 where t2.dashboard_ID=t1.dashboard_ID and t2.vision_ID= ?),t1.DS_THEME) DS_THEME,"
						+ "  t1.DS_THEME_AT " + "  from PRD_DASHBOARDS t1 where Dashboard_ID = ? ";
			}
			Object args[] = {CustomContextHolder.getContext().getVisionId(),dObj.getDashboardId()};
			List<DashboardVb> list = getJdbcTemplate().query(query, args,getDashboardDetMapper());
			return (list != null && list.size() > 0) ? list.get(0) : dashboardVb;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getting Dashboard Detail...!!");
			return dashboardVb;
		}
	}

	private RowMapper getDashboardDetMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				DashboardVb vObject = new DashboardVb();
				vObject.setDashboardId(rs.getString("Dashboard_Id"));
				vObject.setDashboardName(rs.getString("Dashboard_Name"));
				vObject.setTotalTabs(rs.getString("Total_Tabs"));
				vObject.setDataSourceRef(rs.getString("Data_Source_Ref"));
				vObject.setFilterFlag(rs.getString("Filter"));
				vObject.setFilterRefCode(rs.getString("Filter_Ref_Code"));
				vObject.setDashboard_Theme(rs.getString("DS_THEME"));
				vObject.setDashboar_ThemeAT(rs.getInt("DS_THEME_AT"));
				return vObject;
			}
		};
		return mapper;
	}

	public List<DashboardTabVb> getDashboardTabDetails(DashboardVb dObj) {
		setServiceDefaults();
		List<DashboardTabVb> collTemp = null;
		try {
			String query = " Select Tab_ID,Tab_Name,Template_ID from PRD_DASHBOARD_TABS where Status = 0 and Dashboard_id= ?  order by Tab_Sequence ";
			Object args[] = {dObj.getDashboardId()};
			collTemp = getJdbcTemplate().query(query,args,getDashboardTabMapper());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getting Dashboard Detail...!!");
			return null;
		}
	}

	private RowMapper getDashboardTabMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				DashboardTabVb vObject = new DashboardTabVb();
				vObject.setTabId(rs.getString("Tab_ID"));
				vObject.setTabName(rs.getString("Tab_Name"));
				vObject.setTemplateId(rs.getString("Template_ID"));
				return vObject;
			}
		};
		return mapper;
	}

	public List<DashboardTilesVb> getDashboardTileDetails(DashboardTabVb dObj,Boolean exportFlag) {
		setServiceDefaults();
		List<DashboardTilesVb> collTemp = null;
		String tileType = "";
		if(exportFlag)
			tileType = " AND TILE_TYPE = 'G'";
		try {
			String query = "Select Dashboard_ID,Tab_ID,Tile_ID,Tile_Caption,Sequence,Query_ID,TILE_TYPE,DRILL_DOWN_FLAG,CHART_TYPE,"
					/*+ "(SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE "
					+ " ALPHA_TAB = 7005 AND ALPHA_SUB_TAB = T1.CHART_TYPE) CHART_TYPE,"*/
					+ "TILE_PROPERTY_XML,PLACE_HOLDER_COUNT,DOUBLE_WIDTH_FLAG,SUB_TILES,Transition_Effect,"
					+ "Theme,PARENT_SEQUENCE from PRD_DASHBOARD_TILES T1 where Dashboard_ID = ? "
				    + " and Tab_ID = ? "+tileType+" Order by Sequence ";
			Object args[] = { dObj.getDashboardId(),dObj.getTabId()};
			collTemp = getJdbcTemplate().query(query, args,getDashboardTileMapper());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getDashboardTileDetails...!!");
			return null;
		}
	}
	private RowMapper getDashboardTileMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				DashboardTilesVb vObject = new DashboardTilesVb();
				vObject.setDashboardId(rs.getString("Dashboard_ID"));
				vObject.setTabId(rs.getString("Tab_ID"));
				vObject.setTileId(rs.getString("Tile_ID"));
				vObject.setTileCaption(rs.getString("Tile_Caption"));
				vObject.setTileSequence(rs.getInt("Sequence"));
				vObject.setQueryId(rs.getString("Query_ID"));
				vObject.setTileType(rs.getString("TILE_TYPE"));
				vObject.setPropertyAttr(rs.getString("TILE_PROPERTY_XML"));
				vObject.setPlaceHolderCnt(rs.getInt("PLACE_HOLDER_COUNT"));
				vObject.setChartType(rs.getString("CHART_TYPE"));
				vObject.setDoubleWidthFlag(rs.getString("DOUBLE_WIDTH_FLAG"));
				if ("Y".equalsIgnoreCase(rs.getString("DRILL_DOWN_FLAG"))) {
					vObject.setDrillDownFlag(true);
				} else {
					vObject.setDrillDownFlag(false);
				}
				vObject.setSubTiles(rs.getString("SUB_TILES"));
				vObject.setTransitionEffect(rs.getString("Transition_Effect"));
				vObject.setTheme(rs.getString("Theme"));
				vObject.setParentSequence(rs.getInt("PARENT_SEQUENCE"));
				return vObject;
			}
		};
		return mapper;
	}
	public List<DashboardTilesVb> getTileDrillDownDetails(String dashboardId, String tabId, int parentSequence,
			String subSequence, Boolean subTileFlag) {
		setServiceDefaults();
		List<DashboardTilesVb> collTemp = null;
		try {
			String query = "Select Dashboard_ID,Tab_ID,Tile_ID,Tile_Caption,Sequence,Query_ID,TILE_TYPE,DRILL_DOWN_FLAG,CHART_TYPE,"
					/*+ " (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE "
					+ " ALPHA_TAB = 7005 AND ALPHA_SUB_TAB = T1.CHART_TYPE) CHART_TYPE,"*/
					+ "TILE_PROPERTY_XML,PLACE_HOLDER_COUNT,DOUBLE_WIDTH_FLAG,'N' SUB_TILES, Transition_Effect,Theme,PARENT_SEQUENCE from PRD_DASHBOARD_TILES_DD T1"
					+ " where Dashboard_ID = ? and Tab_ID = ? and Parent_Sequence = ? ";

			if (subTileFlag) {
				query = query + " and Sub_Sequence= ? ";
				Object args[] = {dashboardId,tabId,parentSequence,subSequence};
				collTemp = getJdbcTemplate().query(query,args, getDashboardTileMapper());
			}else {
				Object args[] = {dashboardId,tabId,parentSequence};
				collTemp = getJdbcTemplate().query(query,args, getDashboardTileMapper());
			}
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getDashboardTileDetails...!!");
			return null;
		}
	}
	public List<DashboardTilesVb> getTileDrillDownDetailsforExport(String dashboardId, String tabId, int parentSequence,
			String subSequence, Boolean subTileFlag) {
		setServiceDefaults();
		List<DashboardTilesVb> collTemp = null;
		try {
			String query = "Select Dashboard_ID,Tab_ID,Tile_ID,Tile_Caption,Sequence,Query_ID,TILE_TYPE,DRILL_DOWN_FLAG,CHART_TYPE,"
					/*+ " (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE "
					+ " ALPHA_TAB = 7005 AND ALPHA_SUB_TAB = T1.CHART_TYPE) CHART_TYPE,"*/
					+ "TILE_PROPERTY_XML,PLACE_HOLDER_COUNT,DOUBLE_WIDTH_FLAG,'N' SUB_TILES,Transition_Effect,Theme,PARENT_SEQUENCE from PRD_DASHBOARD_TILES_DD T1"
					+ " where Dashboard_ID = ? and Tab_ID = ? and Parent_Sequence = ?  and tile_type = ? ";
			if (subTileFlag) {
				query = query + " and Sub_Sequence= ? ";
				Object args[] = {dashboardId,tabId,parentSequence,"G",subSequence};
				collTemp = getJdbcTemplate().query(query,args, getDashboardTileMapper());
			}else {
				Object args[] = {dashboardId,tabId,parentSequence,"G"};
				collTemp = getJdbcTemplate().query(query,args, getDashboardTileMapper());
			}
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getDashboardTileDetails...!!");
			return null;
		}
	}

	public ExceptionCode getTilesReportData(DashboardTilesVb vObject) throws SQLException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		setServiceDefaults();
		Statement stmt = null;
		ResultSet rs = null;
		String resultData = "";
		DecimalFormat dfDec = new DecimalFormat("0.00");
		DecimalFormat dfNoDec = new DecimalFormat("0");
		List<PrdQueryConfig> sqlQueryList = new ArrayList<PrdQueryConfig>();
		PrdQueryConfig prdQueryConfig = new PrdQueryConfig();
		Connection conExt = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		Statement stmt1 = null;
		ExceptionCode exceptionCodeProc = new ExceptionCode();
		PromptTreeVb promptTreeVb = new PromptTreeVb();
		String orginalQuery = "";
		try {
			sqlQueryList = reportsDao.getSqlQuery(vObject.getQueryId());
			if (sqlQueryList != null && sqlQueryList.size() > 0) {
				prdQueryConfig = sqlQueryList.get(0);
			} else {
				exceptionCode.setOtherInfo(vObject);
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Query not maintained for the Data Ref Id[" + vObject.getQueryId() + "]");
				return exceptionCode;
			}
			
			ExceptionCode exConnection = commonDao.getReqdConnection(conExt, prdQueryConfig.getDbConnectionName());
			if (exConnection.getErrorCode() != Constants.ERRONEOUS_OPERATION && exConnection.getResponse() != null) {
				conExt = (Connection) exConnection.getResponse();
			} else {
				exceptionCode.setErrorCode(exConnection.getErrorCode());
				exceptionCode.setErrorMsg(exConnection.getErrorMsg());
				return exceptionCode;
			}
			
			if ("QUERY".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				orginalQuery = prdQueryConfig.getQueryProc();
				orginalQuery = replacePromptVariables(prdQueryConfig.getQueryProc(), vObject, false);
			}else if ("PROCEDURE".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));
				orginalQuery = prdQueryConfig.getQueryProc();
				orginalQuery = replacePromptVariables(orginalQuery, vObject, true);
				//logger.info("Procedure Execution Start for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Tile Seq["+vObject.getTileSequence()+"] ");
				exceptionCode = callProcforReportData(vObject, orginalQuery,true);
				//logger.info("Procedure Execution End for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Tile Seq["+vObject.getTileSequence()+"] ");
				if (exceptionCode.getErrorCode() == Constants.STATUS_ZERO) {
					promptTreeVb = (PromptTreeVb) exceptionCode.getResponse();
					if (ValidationUtil.isValid(promptTreeVb.getTableName())) {
						if ("REPORTS_STG".equalsIgnoreCase(promptTreeVb.getTableName().toUpperCase())) {
							orginalQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " WHERE SESSION_ID='"
									+ promptTreeVb.getSessionId() + "' AND REPORT_ID='" + promptTreeVb.getReportId()+ "' ";
						} else {
							orginalQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " ";
						}
					}
				}else {
					exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
					return exceptionCode;
				}
			}
			
			String fieldProp =getTilePropertyXml(vObject);
			stmt1 = conExt.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//logger.info("Query Execution Start for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Tile Seq["+vObject.getTileSequence()+"] ");
			ResultSet rsData = stmt1.executeQuery(orginalQuery);
			ResultSetMetaData metaData = rsData.getMetaData();
			int colCount = metaData.getColumnCount();
			//logger.info("Query Execution End for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Tile Seq["+vObject.getTileSequence()+"] ");
			HashMap<String, String> columns = new HashMap<String, String>();
			Boolean dataAvail = false;
			/*ArrayList datalst = new ArrayList();
			ArrayList<String> collst =new ArrayList<>();
			ArrayList<ColumnHeadersVb> colHeadersDetLst = new ArrayList<>();
			String reportUnusedColumns = commonDao.findVisionVariableValue("PRD_REPORT_UNUSED_COLS");
			String reportcolTypes = commonDao.findVisionVariableValue("PRD_REPORT_COL_TYPES");
			Boolean columnHeaderFetch = false;
			int rowNum = 1;
			int cnt = 0;
			while (rsData.next()) {
				for (int cn = 1; cn <= colCount; cn++) {
					ColumnHeadersVb colHeader = new ColumnHeadersVb();
					String columnName = metaData.getColumnName(cn);
					String colType = metaData.getColumnTypeName(cn);
					columns.put(columnName.toUpperCase(), rsData.getString(columnName));
					if (!columnHeaderFetch) {
						collst.add(columnName.toUpperCase());
						if (ValidationUtil.isValid(reportUnusedColumns)
								&& reportUnusedColumns.toUpperCase().contains(columnName.toUpperCase())) {
							cnt = cnt - 1;
							continue;
						}
						colHeader.setLabelColNum(cnt);
						colHeader.setLabelRowNum(1);
						// colHeader.setCaption(WordUtils.capitalizeFully(columnName.replaceAll("_", "
						// ")));
						colHeader.setCaption(columnName);
						colHeader.setDbColumnName(columnName.toUpperCase());
						if (ValidationUtil.isValid(reportcolTypes)
								&& reportcolTypes.toUpperCase().contains(colType.toUpperCase()))
							colHeader.setColType("T");
						else
							colHeader.setColType("N");
						colHeader.setRowspan(0);
						colHeader.setColspan(0);
						colHeader.setDrillDownLabel(false);
						colHeadersDetLst.add(colHeader);
					}
					datalst.add(columns);
				}
				dataAvail = true;
				columnHeaderFetch = true;
				rowNum++;
				break;
			}
			rsData.close();*/
			while (rsData.next()) {
				for (int cn = 1; cn <= colCount; cn++) {
					String columnName = metaData.getColumnName(cn);
					columns.put(columnName.toUpperCase(), rsData.getString(columnName));
				}
				dataAvail = true;
				break;
			}
			rsData.close();
			if (!dataAvail) {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
				exceptionCode.setErrorMsg("No Records Found");
				return exceptionCode;
			}
			List<SingletonTileVb> tilePropAttrLst = new ArrayList<>();
			//logger.info("Processing placeholders Start for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Tile Seq["+vObject.getTileSequence()+"] ");
			fieldProp = ValidationUtil.isValid(fieldProp) ? fieldProp.replaceAll("\n", "").replaceAll("\r", "") : "";
			for (int ctr = 1; ctr <= vObject.getPlaceHolderCnt(); ctr++) {
				String placeHolder = CommonUtils.getValueForXmlTag(fieldProp, "PLACEHOLDER" + ctr);
				String sourceCol = CommonUtils.getValueForXmlTag(placeHolder, "SOURCECOL");
				
				String dataType = CommonUtils.getValueForXmlTag(placeHolder, "DATA_TYPE");
				String numberFormat = CommonUtils.getValueForXmlTag(placeHolder, "NUMBERFORMAT");
				String scaling = CommonUtils.getValueForXmlTag(placeHolder, "SCALING");
				String decimalCount = CommonUtils.getValueForXmlTag(placeHolder, "DECIMAL_COUNT");
				String preCaption = CommonUtils.getValueForXmlTag(placeHolder, "PRECAPTION");
				String postCaption = CommonUtils.getValueForXmlTag(placeHolder, "POSTCAPTION");
				String prefixStr = CommonUtils.getValueForXmlTag(placeHolder, "PREFIX");
				String suffix = CommonUtils.getValueForXmlTag(placeHolder, "SUFFIX");
				String toolTip = CommonUtils.getValueForXmlTag(placeHolder, "TOOL_TIP");
				String ddKeyId = CommonUtils.getValueForXmlTag(placeHolder, "DDKEY");
				if (!ValidationUtil.isValid(placeHolder)) {
					continue;
				}
				String fieldValue = "";
				if (ValidationUtil.isValid(sourceCol)) {
					fieldValue = columns.get(sourceCol.toUpperCase());
					fieldValue = (ValidationUtil.isValid(fieldValue)) ? fieldValue : "";
					/* Double val = 0.00; */
					String prefix = "";
					if (ValidationUtil.isValid(scaling) && "Y".equalsIgnoreCase(scaling)
							&& ValidationUtil.isValid(fieldValue)) {
						Double dbValue = Math.abs(Double.parseDouble(fieldValue));
						if (dbValue > 1000000000) {
							dbValue = Double.parseDouble(fieldValue) / 1000000000;
							prefix = " B";
						} else if (dbValue > 1000000) {
							dbValue = Double.parseDouble(fieldValue) / 1000000;
							prefix = " M";
						} else if (dbValue > 10000) {
							dbValue = Double.parseDouble(fieldValue) / 1000;
							prefix = " K";
						}
						String afterDecimalVal = String.valueOf(dbValue);
						if (!afterDecimalVal.contains("E")) {
							afterDecimalVal = afterDecimalVal.substring(afterDecimalVal.indexOf(".") + 1);
							if (Double.parseDouble(afterDecimalVal) > 0)
								fieldValue = dfDec.format(dbValue) + " " + prefix;
							else
								fieldValue = dfNoDec.format(dbValue) + " " + prefix;
						} else {
							fieldValue = "0.00";
						}
					}
					if (ValidationUtil.isValid(fieldValue) && ValidationUtil.isValid(numberFormat)
							&& "Y".equalsIgnoreCase(numberFormat) && !ValidationUtil.isValid(prefix)) {
						DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
						double tmpVal = Double.parseDouble(fieldValue);
						fieldValue = decimalFormat.format(tmpVal);
					}
					/* if(ValidationUtil.isValid(fieldValue)) */
					fieldProp = fieldProp.replaceAll(sourceCol, fieldValue);
				}
				String ddKeyValue = "";
				if(ValidationUtil.isValid(ddKeyId)) {
					ddKeyValue = columns.get(ddKeyId.toUpperCase());
					fieldProp = fieldProp.replaceAll(ddKeyId, ddKeyValue);
				}
				SingletonTileVb singletonVb = new SingletonTileVb();
				singletonVb.setPlaceHolder(ctr);
				singletonVb.setDataType(dataType);
				singletonVb.setDecimalCount(decimalCount);
				singletonVb.setNumberFormat(numberFormat);
				singletonVb.setSourceCol(fieldValue);
				singletonVb.setPreCaption(preCaption);
				singletonVb.setPostCaption(postCaption);
				singletonVb.setPrefix(prefixStr);
				singletonVb.setSuffix(suffix);
				singletonVb.setToolTip(toolTip);
				singletonVb.setTileCaption(vObject.getTileCaption());
				singletonVb.setDdKeyValue(ddKeyValue);
				tilePropAttrLst.add(singletonVb);
			}
			int PRETTY_PRINT_INDENT_FACTOR = 4;
			JSONObject xmlJSONObj = XML.toJSONObject(fieldProp);
			resultData = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR).replaceAll("[\\n\\t ]", "");
			//logger.info("Processing placeholders End for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Tile Seq["+vObject.getTileSequence()+"] ");
			exceptionCode.setResponse(resultData);
			exceptionCode.setRequest(tilePropAttrLst);
			//exceptionCode.setRequest(datalst);
			//exceptionCode.setResponse1(columnHeaderFetch);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		} catch (Exception ex) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(ex.getMessage());
			exceptionCode.setRequest(orginalQuery);
		} finally {
			if (stmt1 != null)
				stmt1.close();
			if (conExt != null)
				conExt.close();
		}
		return exceptionCode;
	}

	public ExceptionCode getGridData(DashboardTilesVb vObject) throws SQLException{
		setServiceDefaults();
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList resultlst = new ArrayList();
		List<PrdQueryConfig> sqlQueryList = new ArrayList<PrdQueryConfig>();
		PrdQueryConfig prdQueryConfig = new PrdQueryConfig();
		ExceptionCode exceptionCode = new ExceptionCode();
		Connection conExt  = null;
		ArrayList<ColumnHeadersVb> colHeaders = new ArrayList<ColumnHeadersVb>();
		ExceptionCode exceptionCodeProc = new ExceptionCode();
		PromptTreeVb promptTreeVb = new PromptTreeVb();
		String orginalQuery = "";
		try
		{
			//String orginalQuery = getReportQuery(vObject.getQueryId());
			sqlQueryList = reportsDao.getSqlQuery(vObject.getQueryId());
			if (sqlQueryList != null && sqlQueryList.size() > 0) {
				prdQueryConfig = sqlQueryList.get(0);
			}else {
				exceptionCode.setOtherInfo(vObject);
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Query not maintained for the Data Ref Id[" + vObject.getQueryId() + "]");
				return exceptionCode;
			}
			ExceptionCode exConnection = commonDao.getReqdConnection(conExt,prdQueryConfig.getDbConnectionName());
			if(exConnection.getErrorCode() != Constants.ERRONEOUS_OPERATION && exConnection.getResponse() != null) {
				conExt = (Connection)exConnection.getResponse();
			}else {
				exceptionCode.setErrorCode(exConnection.getErrorCode());
				exceptionCode.setErrorMsg(exConnection.getErrorMsg());
				return exceptionCode;
			}
			orginalQuery = (String)prdQueryConfig.getQueryProc();
			ReportsVb reportsVb = new ReportsVb();
			reportsVb.setPromptValue1(vObject.getPromptValue1());
			reportsVb.setPromptValue2(vObject.getPromptValue2());
			reportsVb.setPromptValue3(vObject.getPromptValue3());
			reportsVb.setPromptValue4(vObject.getPromptValue4());
			reportsVb.setPromptValue5(vObject.getPromptValue5());
			reportsVb.setPromptValue6(vObject.getPromptValue6());
			reportsVb.setPromptValue7(vObject.getPromptValue7());
			reportsVb.setPromptValue8(vObject.getPromptValue8());
			reportsVb.setPromptValue9(vObject.getPromptValue9());
			reportsVb.setPromptValue10(vObject.getPromptValue10());
			boolean isDynamicXML = false;
			if(ValidationUtil.isValid(vObject.getTransitionEffect()) && "DYNAMIC_XML_SUMMARY".equalsIgnoreCase(vObject.getTransitionEffect())){
				String ddKey1Val = vObject.getDrillDownKey1().replaceAll("'", ""); 
				String ddTempTableName = ddKey1Val.substring(ddKey1Val.indexOf("#")+1, ddKey1Val.length());
				vObject.setTileTable10(ddTempTableName);
				isDynamicXML = true;
			}
			if(ValidationUtil.isValid(vObject.getTransitionEffect()) && "DYNAMIC_XML_SUMMARY_TABLE".equalsIgnoreCase(vObject.getTransitionEffect())){
				String ddKey1Val = vObject.getDrillDownKey10().replaceAll("'", ""); 
				String ddTempTableName = ddKey1Val.substring(ddKey1Val.indexOf("#")+1, ddKey1Val.length());
				vObject.setTileTable10(ddTempTableName);
				isDynamicXML = true;
			}
			if(ValidationUtil.isValid(vObject.getTransitionEffect()) && "DYNAMIC_XML_DETAILS".equalsIgnoreCase(vObject.getTransitionEffect())){
				String ddKey1Val = vObject.getDrillDownKey10().replaceAll("'", ""); 
				String ddTempTableName = ddKey1Val.substring(ddKey1Val.indexOf("#")+1, ddKey1Val.length());
				vObject.setTileTable10(ddTempTableName);
				isDynamicXML = true;
			}
			
			if ("QUERY".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				String queryTmp = orginalQuery.toUpperCase();
				String colHeaderXml = "";
				if (queryTmp.contains("ORDER BY")) {
					String orderBy = queryTmp.substring(queryTmp.lastIndexOf("ORDER BY"), queryTmp.length());
					prdQueryConfig.setQueryProc(prdQueryConfig.getQueryProc().substring(0, queryTmp.lastIndexOf("ORDER BY")));
					prdQueryConfig.setSortField(orderBy);
				}
				// prdQueryConfig.setQueryProc(searchQuery);
				if (!ValidationUtil.isValid(prdQueryConfig.getSortField())) {
					exceptionCode.setOtherInfo(vObject);
					exceptionCode.setErrorMsg("ORDER BY is mandatory in Query for [" + prdQueryConfig.getDataRefId() + "] !!");
					return exceptionCode;
				} else {
					colHeaderXml = getTilePropertyXml(vObject);

					if (!ValidationUtil.isValid(colHeaderXml)) {
						exceptionCode.setOtherInfo(vObject);
						exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
						exceptionCode.setErrorMsg("Column Headers/Tile Property XML Not Maintained for the Dashboard Id[" + vObject.getDashboardId()+ "] and Tab Id[" + vObject.getTabId() + vObject.getTileSequence() + "] !!");
						return exceptionCode;
					}
					if(isDynamicXML) {
						int colCount = Integer.parseInt(CommonUtils.getValueForXmlTag(colHeaderXml, "COLUMN_COUNT"));
						vObject.setPromptValue10("COLUMN_"+colCount);
					}
					orginalQuery = replacePromptVariables(prdQueryConfig.getQueryProc(), vObject, false);

				}
				
				if (!ValidationUtil.isValid(colHeaderXml)) {
					exceptionCode.setOtherInfo(vObject);
					exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
					exceptionCode.setErrorMsg("Column Headers/Tile Property XML Not Maintained for the Dashboard Id[" + vObject.getDashboardId()+ "] and Tab Id[" + vObject.getTabId() + vObject.getTileSequence() + "] !!");
					return exceptionCode;
				}
				colHeaders = reportsDao.getColumnHeaders(colHeaderXml,reportsVb);
			} else if ("PROCEDURE".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));
				orginalQuery = replacePromptVariables(orginalQuery, vObject, true);
				String procedureID = prdQueryConfig.getQueryProc();
				int parameterCnt = procedureID.replaceAll("[^,]", "").length() + 1;
				//logger.info("Procedure Execution Start for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Drill Down Tile Seq["+vObject.getTileSequence()+"] ");
				exceptionCodeProc = callProcforReportData(vObject, orginalQuery,false);
				//logger.info("Procedure Execution End for Dashboard["+vObject.getDashboardId()+"] Tab Id["+vObject.getTabId()+"] Drill Down Tile Seq["+vObject.getTileSequence()+"] ");
				if (exceptionCodeProc.getErrorCode() == Constants.STATUS_ZERO) {
					promptTreeVb = (PromptTreeVb) exceptionCodeProc.getResponse();
					promptTreeVb.setReportId(vObject.getDashboardId()+vObject.getTabId()+vObject.getTileSequence());
					if (ValidationUtil.isValid(promptTreeVb.getTableName())) {
						if ("REPORTS_STG".equalsIgnoreCase(promptTreeVb.getTableName().toUpperCase())) {
							orginalQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " WHERE SESSION_ID='"
									+ promptTreeVb.getSessionId() + "' AND REPORT_ID='" + promptTreeVb.getReportId()+ "' ";
						} else {
							orginalQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " ";
						}
						prdQueryConfig.setSortField("ORDER BY SORT_FIELD");
						vObject.setPromptTree(promptTreeVb);
						if (ValidationUtil.isValid(promptTreeVb.getColumnHeaderTable())) {
							colHeaders = (ArrayList<ColumnHeadersVb>)reportsDao.getColumnHeaderFromTable(promptTreeVb);
							if(colHeaders != null && !colHeaders.isEmpty()) {
								vObject.setGridColumnSet(colHeaders);
							}
						}
						Boolean headerPresent = false;
						
						if(vObject.getGridColumnSet() != null && vObject.getGridColumnSet().size() > 0) {
							headerPresent = true;
						}else {
							String colHeaderXml = getTilePropertyXml(vObject);
							colHeaders = reportsDao.getColumnHeaders(colHeaderXml,reportsVb);
							headerPresent = true;
						}
						if (!headerPresent) {
							exceptionCode.setOtherInfo(vObject);
							exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
							exceptionCode.setErrorMsg("Column Headers/Tile Property XML Not Maintained for the Dashboard Id[" + vObject.getDashboardId()+ "] and Tab Id[" + vObject.getTabId() + vObject.getTileSequence() + "] !!");
							return exceptionCode;
						}
						
					} else {
						exceptionCode.setOtherInfo(vObject);
						exceptionCode.setErrorMsg("Output Table not return from Procedure but the Procedure return Success Status");
						return exceptionCode;
					}
				} else {
					exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
					exceptionCode.setErrorMsg(exceptionCodeProc.getErrorMsg());
					exceptionCode.setOtherInfo(vObject);
					return exceptionCode;
				}
			}
			ArrayList datalst = new ArrayList();
			reportsVb.setFinalExeQuery(orginalQuery);
			reportsVb.setSortField(prdQueryConfig.getSortField());
			reportsVb.setTotalRows(vObject.getTotalRows());
			reportsVb.setMaxRecords(1000);
			reportsVb.setObjectType("G");
			reportsVb.setCurrentLevel(Integer.toString(vObject.getCurrentPage()));
			reportsVb.setColumnHeaderslst(colHeaders);
			//exceptionCode = reportsDao.extractReportData(reportsVb,conExt);
			
			String maskingColumns = commonDao.findMassingColumns(vObject.getClientId(), vObject.getConnectorId(), vObject.getDqTableName());
			reportsVb.setMaskingColumns(maskingColumns);
			
			exceptionCode = reportsDao.getResultData(reportsVb,vObject.getExportFlag());
			if(exceptionCode.getErrorCode() == Constants.ERRONEOUS_OPERATION) {
				return exceptionCode;
			}
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				reportsVb = (ReportsVb)exceptionCode.getResponse();
				datalst = (ArrayList)reportsVb.getGridDataSet();
				//datalst = (ArrayList) exceptionCode.getResponse();
			}		
			//ArrayList datalst = new ArrayList();
			/*stmt = conExt.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rsData = stmt.executeQuery(orginalQuery);
			ResultSetMetaData metaData = rsData.getMetaData();
			int colCount = metaData.getColumnCount();
			HashMap<String,String> columns = new HashMap<String,String>();
			while(rsData.next()){
				columns = new HashMap<String, String>();
				for (int cn = 1; cn <= colCount; cn++) {
					String columnName = metaData.getColumnName(cn);
					columns.put(columnName.toUpperCase(), rsData.getString(columnName));
				}
				datalst.add(columns);
			}
			rsData.close();*/
			if(datalst == null && datalst.size() == 0) {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
				exceptionCode.setErrorMsg("No Records Found");
				return exceptionCode;
			}
			//HashMap<String,String> columns = (HashMap<String,String>)getJdbcTemplate().query(orginalQuery, mapper);
			/*String fieldProp = vObject.getPropertyAttr();
			fieldProp = ValidationUtil.isValid(fieldProp)?fieldProp.replaceAll("\n", "").replaceAll("\r", ""):"";
			String columnValueDetails = CommonUtils.getValueForXmlTag(fieldProp, "COLUMNVALUE");
			String colValArray[] = {};
			if(ValidationUtil.isValid(columnValueDetails))
				colValArray = columnValueDetails.split(",");
			
			ArrayList finalDatalst = new ArrayList();
			ArrayList columnlst = new ArrayList();
			ArrayList columnFormatlst = new ArrayList();
			for(int ctr = 0;ctr < datalst.size();ctr++) {
				LinkedHashMap <String,String> finalDataMap = new LinkedHashMap <String,String>();
				HashMap<String, String> dataMap = (HashMap<String, String>)datalst.get(ctr);
				for(int ct = 0;ct < colValArray.length;ct++) {
					String colArr[] = colValArray[ct].split("!@#");
					String colName = colArr[0]; 
					String colSrc = colArr[1];
					String colType = colArr[2];
					finalDataMap.put(colName, dataMap.get(colSrc));
					if(ctr == 0) {
						columnlst.add(colName);
						columnFormatlst.add(colType);
					}
				}
				finalDatalst.add(finalDataMap);
			}
			resultlst.add(columnlst);
			resultlst.add(finalDatalst);
			resultlst.add(columnFormatlst);
			exceptionCode.setResponse(resultlst);*/
			resultlst.add(colHeaders);
			resultlst.add(datalst);
			exceptionCode.setResponse(resultlst);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("successful operation");
			return exceptionCode;
		}catch(

	Exception ex){
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(ex.getMessage());
		}finally
	{
		if (stmt != null)
			stmt.close();
		if (conExt != null)
			conExt.close();
	}return exceptionCode;
	}

	public ExceptionCode getChartReportData(DashboardTilesVb vObject) throws SQLException{
		ExceptionCode exceptionCode =new ExceptionCode();
		setServiceDefaults();
		Statement stmt = null;
		List collTemp = new ArrayList();
		HashMap chartDataMap = new HashMap();
		ResultSet rs = null;
		Connection conExt = null;
		List<PrdQueryConfig> sqlQueryList = new ArrayList<PrdQueryConfig>();
		PrdQueryConfig prdQueryConfig = new PrdQueryConfig();
		ExceptionCode exceptionCodeProc = new ExceptionCode();
		PromptTreeVb promptTreeVb = new PromptTreeVb();
		ArrayList<ColumnHeadersVb> colHeaders = new ArrayList<ColumnHeadersVb>();
		String orginalQuery = "";
		try
		{
			if(!ValidationUtil.isValid(vObject.getPropertyAttr())) {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Chart Config Xml missing is Tile_Property_xml in PRD_Dashboard_Tiles");
			}
			sqlQueryList = reportsDao.getSqlQuery(vObject.getQueryId());
			if (sqlQueryList != null && sqlQueryList.size() > 0) {
				prdQueryConfig = sqlQueryList.get(0);
			} else {
				exceptionCode.setOtherInfo(vObject);
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Query not maintained for the Data Ref Id[" + vObject.getQueryId() + "]");
				return exceptionCode;
			}
			ExceptionCode exConnection = commonDao.getReqdConnection(conExt, prdQueryConfig.getDbConnectionName());
			if (exConnection.getErrorCode() != Constants.ERRONEOUS_OPERATION && exConnection.getResponse() != null) {
				conExt = (Connection) exConnection.getResponse();
			} else {
				exceptionCode.setErrorCode(exConnection.getErrorCode());
				exceptionCode.setErrorMsg(exConnection.getErrorMsg());
				return exceptionCode;
			}
			if("QUERY".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				orginalQuery = replacePromptVariables(prdQueryConfig.getQueryProc(), vObject, false);
			}else if ("PROCEDURE".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));
				orginalQuery = replacePromptVariables(prdQueryConfig.getQueryProc(), vObject, true);
				exceptionCodeProc = callProcforReportData(vObject, orginalQuery,false);
				if (exceptionCodeProc.getErrorCode() == Constants.STATUS_ZERO) {
					promptTreeVb = (PromptTreeVb) exceptionCodeProc.getResponse();
					promptTreeVb.setReportId(vObject.getQueryId());
					if (ValidationUtil.isValid(promptTreeVb.getTableName())) {
						if ("REPORTS_STG".equalsIgnoreCase(promptTreeVb.getTableName().toUpperCase())) {
							orginalQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " WHERE SESSION_ID='"
									+ promptTreeVb.getSessionId() + "' AND REPORT_ID='" + promptTreeVb.getReportId()+ "' ";
						} else {
							orginalQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " ";
						}
						prdQueryConfig.setSortField("ORDER BY SORT_FIELD");
					} else {
						exceptionCode.setOtherInfo(vObject);
						exceptionCode.setErrorMsg("Output Table not return from Procedure but the Procedure return Success Status");
						return exceptionCode;
					}
				} else {
					exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
					exceptionCode.setErrorMsg(exceptionCodeProc.getErrorMsg());
					exceptionCode.setOtherInfo(vObject);
					return exceptionCode;
				}
			}

			stmt = conExt.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (ValidationUtil.isValid(orginalQuery)) {
				rs = stmt.executeQuery(orginalQuery);
			} else {
				return null;
			}
			CachedRowSet rsChild = RowSetProvider.newFactory().createCachedRowSet();
			rsChild.populate(rs);
			// String returnXml = chartUtils.getChartXML(vObject.getChartType(),
			// vObject.getPropertyAttr(),rs ,rsChild);
			vObject.setChartType(getChartType(vObject.getChartType()));
			exceptionCode = chartUtils.getChartXML(vObject.getChartType(), vObject.getPropertyAttr(), rs, rsChild,null);
			if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg(exceptionCode.getErrorMsg());
				return exceptionCode;
			}
			if(!ValidationUtil.isValid(exceptionCode.getResponse())){
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
				exceptionCode.setErrorMsg("No Data Found");
				return exceptionCode;
			}
			exceptionCode.setResponse(exceptionCode.getResponse());
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			return exceptionCode;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while getting Dashboard Data...!!");
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(ex.getMessage());
			exceptionCode.setRequest(orginalQuery);
			return exceptionCode;
		}finally{
			if(stmt != null)
				stmt.close();
			if(conExt != null)
				conExt.close();
			if(rs != null)
				rs.close();
		}
	}

	/*
	 * public String getChartXmlFormObjProperties(String chartType){ String sql =
	 * "select HTML_TAG_PROPERTY from PRD_CHART_PROPERTIES where " +
	 * "VRD_OBJECT_ID='Col3D' AND " + "UPPER(OBJ_TAG_ID)=UPPER('"+chartType+"')";
	 * return getJdbcTemplate().queryForObject(sql, String.class); }
	 */
	public String getReportQuery(String reportId) {
		setServiceDefaults();
		String strQueryAppr = new String("Select QUERY from PRD_QUERY_CONFIG where Report_ID = ? ");
		Object args[] = {reportId};
		try {
			String i = getJdbcTemplate().queryForObject(strQueryAppr, args, String.class);
			return i;
		} catch (Exception ex) {
			return "";
		}
	}

	public LinkedHashMap<String, String> getDashboardFilterValue(String sourceQuery) {
		ResultSetExtractor mapper = new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				ResultSetMetaData metaData = rs.getMetaData();
				LinkedHashMap<String, String> filterMap = new LinkedHashMap<String, String>();
				while (rs.next()) {
					filterMap.put(rs.getString(1), rs.getString(2));
				}
				return filterMap;
			}
		};
		return (LinkedHashMap<String, String>) getJdbcTemplate().query(sourceQuery, mapper);
	}

	public String getDashboardDefaultValue(String sourceQuery) {
		ResultSetExtractor mapper = new ResultSetExtractor() {
			String defaultValue = "";

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					defaultValue = rs.getString(1);
				}
				return defaultValue;
			}
		};
		return (String) getJdbcTemplate().query(sourceQuery, mapper);
	}

	public String replacePromptVariables(String reportQuery, DashboardTilesVb promptsVb, Boolean isProcedure) {
		try {
			if(ValidationUtil.isValid(promptsVb.getQueryId()) && "DQDD0032".equalsIgnoreCase(promptsVb.getQueryId())) {
				promptsVb.setDrillDownKey1(promptsVb.getDrillDownKey1().replaceAll("'", ""));
			}
			if (isProcedure)
				promptsVb = replaceProcedurePrompt(promptsVb);
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_1#",
					ValidationUtil.isValid(promptsVb.getPromptValue1()) ? promptsVb.getPromptValue1() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_2#",
					ValidationUtil.isValid(promptsVb.getPromptValue2()) ? promptsVb.getPromptValue2() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_3#",
					ValidationUtil.isValid(promptsVb.getPromptValue3()) ? promptsVb.getPromptValue3() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_4#",
					ValidationUtil.isValid(promptsVb.getPromptValue4()) ? promptsVb.getPromptValue4() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_5#",
					ValidationUtil.isValid(promptsVb.getPromptValue5()) ? promptsVb.getPromptValue5() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_6#",
					ValidationUtil.isValid(promptsVb.getPromptValue6()) ? promptsVb.getPromptValue6() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_7#",
					ValidationUtil.isValid(promptsVb.getPromptValue7()) ? promptsVb.getPromptValue7() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_8#",
					ValidationUtil.isValid(promptsVb.getPromptValue8()) ? promptsVb.getPromptValue8() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_9#",
					ValidationUtil.isValid(promptsVb.getPromptValue9()) ? promptsVb.getPromptValue9() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_10#",
					ValidationUtil.isValid(promptsVb.getPromptValue10()) ? promptsVb.getPromptValue10() : "''");
			
			
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_1#",
					ValidationUtil.isValid(promptsVb.getTileTable1()) ? promptsVb.getTileTable1() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_2#",
					ValidationUtil.isValid(promptsVb.getTileTable2()) ? promptsVb.getTileTable2() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_3#",
					ValidationUtil.isValid(promptsVb.getTileTable3()) ? promptsVb.getTileTable3() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_4#",
					ValidationUtil.isValid(promptsVb.getTileTable4()) ? promptsVb.getTileTable4() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_5#",
					ValidationUtil.isValid(promptsVb.getTileTable5()) ? promptsVb.getTileTable5() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_6#",
					ValidationUtil.isValid(promptsVb.getTileTable6()) ? promptsVb.getTileTable6() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_7#",
					ValidationUtil.isValid(promptsVb.getTileTable7()) ? promptsVb.getTileTable7() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_8#",
					ValidationUtil.isValid(promptsVb.getTileTable8()) ? promptsVb.getTileTable8() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_9#",
					ValidationUtil.isValid(promptsVb.getTileTable9()) ? promptsVb.getTileTable9() : "''");
			reportQuery = reportQuery.replaceAll("#TILE_TABLE_10#",
					ValidationUtil.isValid(promptsVb.getTileTable10()) ? promptsVb.getTileTable10() : "''");			
			
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_1#",
					promptsVb.getPromptValue1().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_2#",
					promptsVb.getPromptValue2().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_3#",
					promptsVb.getPromptValue3().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_4#",
					promptsVb.getPromptValue4().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_5#",
					promptsVb.getPromptValue5().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_6#",
					promptsVb.getPromptValue6().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_7#",
					promptsVb.getPromptValue7().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_8#",
					promptsVb.getPromptValue8().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_9#",
					promptsVb.getPromptValue9().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_10#",
					promptsVb.getPromptValue10().replaceAll("'", ""));

			reportQuery = reportQuery.replaceAll("#DDKEY1#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey1()) ? promptsVb.getDrillDownKey1() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY2#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey2()) ? promptsVb.getDrillDownKey2() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY3#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey3()) ? promptsVb.getDrillDownKey3() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY4#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey4()) ? promptsVb.getDrillDownKey4() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY5#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey5()) ? promptsVb.getDrillDownKey5() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY6#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey6()) ? promptsVb.getDrillDownKey6() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY7#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey7()) ? promptsVb.getDrillDownKey7() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY8#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey8()) ? promptsVb.getDrillDownKey8() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY9#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey9()) ? promptsVb.getDrillDownKey9() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY10#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey10()) ? promptsVb.getDrillDownKey10() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY0#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey0()) ? promptsVb.getDrillDownKey0() : "''");

			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_1#",
					ValidationUtil.isValid(promptsVb.getDataFilter1()) ? promptsVb.getDataFilter1() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_2#",
					ValidationUtil.isValid(promptsVb.getDataFilter2()) ? promptsVb.getDataFilter2() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_3#",
					ValidationUtil.isValid(promptsVb.getDataFilter3()) ? promptsVb.getDataFilter3() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_4#",
					ValidationUtil.isValid(promptsVb.getDataFilter4()) ? promptsVb.getDataFilter4() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_5#",
					ValidationUtil.isValid(promptsVb.getDataFilter5()) ? promptsVb.getDataFilter5() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_6#",
					ValidationUtil.isValid(promptsVb.getDataFilter6()) ? promptsVb.getDataFilter6() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_7#",
					ValidationUtil.isValid(promptsVb.getDataFilter7()) ? promptsVb.getDataFilter7() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_8#",
					ValidationUtil.isValid(promptsVb.getDataFilter8()) ? promptsVb.getDataFilter8() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_9#",
					ValidationUtil.isValid(promptsVb.getDataFilter9()) ? promptsVb.getDataFilter9() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_10#",
					ValidationUtil.isValid(promptsVb.getDataFilter10()) ? promptsVb.getDataFilter10() : "''");

			reportQuery = reportQuery.replaceAll("#FILTER_POSITION#",
					ValidationUtil.isValid(promptsVb.getFilterPosition()) ? "'" + promptsVb.getFilterPosition() + "'"
							: "''");

			reportQuery = reportQuery.replaceAll("#REPORT_ID#", "'"+promptsVb.getDashboardId()+promptsVb.getTabId()+promptsVb.getTileSequence()+ "'");
			reportQuery = reportQuery.replaceAll("#SUB_REPORT_ID#","'" + promptsVb.getQueryId()+ "'");
			reportQuery = reportQuery.replaceAll("#VISION_ID#","'" + CustomContextHolder.getContext().getVisionId() + "'");
			reportQuery = reportQuery.replaceAll("#SESSION_ID#", "'" + promptsVb.getDateCreation() + "'");
			if(!ValidationUtil.isValid(promptsVb.getScalingFactor()) || "0".equalsIgnoreCase(promptsVb.getScalingFactor())) {
				promptsVb.setScalingFactor("1");
			}
			reportQuery = reportQuery.replaceAll("#SCALING_FACTOR#", promptsVb.getScalingFactor());
			// Below is used only on MPR Report - MDM for Prime Bank - Deepak
			if (promptsVb.getDashboardId().contains("MPR") && ValidationUtil.isValid(promptsVb.getPromptValue6())) {
				reportQuery = reportQuery.replaceAll("#PYM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "PYM"));
				reportQuery = reportQuery.replaceAll("#NYM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "NYM"));
				reportQuery = reportQuery.replaceAll("#PM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "PM"));
				reportQuery = reportQuery.replaceAll("#NM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "NM"));
				reportQuery = reportQuery.replaceAll("#CM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "CM"));
				reportQuery = reportQuery.replaceAll("#CY#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "CY"));
				reportQuery = reportQuery.replaceAll("#PY#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "PY"));
			}
			
			reportQuery = commonDao.applyUserRestriction(reportQuery);
			reportQuery = applyPrPromptChange(reportQuery,promptsVb);
			reportQuery = applySpecialPrompts(reportQuery,promptsVb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportQuery;
	}

	public DashboardTilesVb replaceProcedurePrompt(DashboardTilesVb vObject) {
		vObject.setPromptValue1(
				ValidationUtil.isValid(vObject.getPromptValue1()) ? vObject.getPromptValue1().replaceAll("','", "'',''")
						: vObject.getPromptValue1());
		vObject.setPromptValue2(
				ValidationUtil.isValid(vObject.getPromptValue2()) ? vObject.getPromptValue2().replaceAll("','", "'',''")
						: vObject.getPromptValue2());
		vObject.setPromptValue3(
				ValidationUtil.isValid(vObject.getPromptValue3()) ? vObject.getPromptValue3().replaceAll("','", "'',''")
						: vObject.getPromptValue3());
		vObject.setPromptValue4(
				ValidationUtil.isValid(vObject.getPromptValue4()) ? vObject.getPromptValue4().replaceAll("','", "'',''")
						: vObject.getPromptValue4());
		vObject.setPromptValue5(
				ValidationUtil.isValid(vObject.getPromptValue5()) ? vObject.getPromptValue5().replaceAll("','", "'',''")
						: vObject.getPromptValue5());
		vObject.setPromptValue6(
				ValidationUtil.isValid(vObject.getPromptValue6()) ? vObject.getPromptValue6().replaceAll("','", "'',''")
						: vObject.getPromptValue6());
		vObject.setPromptValue7(
				ValidationUtil.isValid(vObject.getPromptValue7()) ? vObject.getPromptValue7().replaceAll("','", "'',''")
						: vObject.getPromptValue7());
		vObject.setPromptValue8(
				ValidationUtil.isValid(vObject.getPromptValue8()) ? vObject.getPromptValue8().replaceAll("','", "'',''")
						: vObject.getPromptValue8());
		vObject.setPromptValue9(
				ValidationUtil.isValid(vObject.getPromptValue9()) ? vObject.getPromptValue9().replaceAll("','", "'',''")
						: vObject.getPromptValue9());
		vObject.setPromptValue10(ValidationUtil.isValid(vObject.getPromptValue10())
				? vObject.getPromptValue10().replaceAll("','", "'',''")
				: vObject.getPromptValue10());
		vObject.setDataFilter1(
				ValidationUtil.isValid(vObject.getDataFilter1()) ? vObject.getDataFilter1().replaceAll("','", "'',''")
						: vObject.getDataFilter1());
		vObject.setDataFilter2(
				ValidationUtil.isValid(vObject.getDataFilter2()) ? vObject.getDataFilter2().replaceAll("','", "'',''")
						: vObject.getDataFilter2());
		vObject.setDataFilter3(
				ValidationUtil.isValid(vObject.getDataFilter3()) ? vObject.getDataFilter3().replaceAll("','", "'',''")
						: vObject.getDataFilter3());
		vObject.setDataFilter4(
				ValidationUtil.isValid(vObject.getDataFilter4()) ? vObject.getDataFilter4().replaceAll("','", "'',''")
						: vObject.getDataFilter4());
		vObject.setDataFilter5(
				ValidationUtil.isValid(vObject.getDataFilter5()) ? vObject.getDataFilter5().replaceAll("','", "'',''")
						: vObject.getDataFilter5());
		vObject.setDataFilter6(
				ValidationUtil.isValid(vObject.getDataFilter6()) ? vObject.getDataFilter6().replaceAll("','", "'',''")
						: vObject.getDataFilter6());
		vObject.setDataFilter7(
				ValidationUtil.isValid(vObject.getDataFilter7()) ? vObject.getDataFilter7().replaceAll("','", "'',''")
						: vObject.getDataFilter7());
		vObject.setDataFilter8(
				ValidationUtil.isValid(vObject.getDataFilter8()) ? vObject.getDataFilter8().replaceAll("','", "'',''")
						: vObject.getDataFilter8());
		vObject.setDataFilter9(
				ValidationUtil.isValid(vObject.getDataFilter9()) ? vObject.getDataFilter9().replaceAll("','", "'',''")
						: vObject.getDataFilter9());
		vObject.setDataFilter10(
				ValidationUtil.isValid(vObject.getDataFilter10()) ? vObject.getDataFilter10().replaceAll("','", "'',''")
						: vObject.getDataFilter10());
		return vObject;
	}

	public List<DashboardTilesVb> getDashboardSubTileDetails(DashboardTilesVb dObj) {
		setServiceDefaults();
		List<DashboardTilesVb> collTemp = null;
		try {
			String query = "Select Dashboard_ID,Tab_ID,PARENT_SEQUENCE,SUB_SEQUENCE,SUB_TILE_ID,Tile_Caption,Query_ID,"
					+ "TILE_TYPE,DRILL_DOWN_FLAG,CHART_TYPE,TILE_PROPERTY_XML,"
					+ "PLACE_HOLDER_COUNT,DOUBLE_WIDTH_FLAG,Theme_AT,Theme from PRD_DASHBOARD_SUB_TILES "
					+ "where Dashboard_ID = ? and Tab_ID = ? and PARENT_SEQUENCE = ? Order by SUB_SEQUENCE ";
			Object args[] = {dObj.getDashboardId(), dObj.getTabId(),dObj.getTileSequence()};
			collTemp = getJdbcTemplate().query(query,args, getDashboardSubTileMapper());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getDashboardSubTileDetails...!!");
			return null;
		}
	}

	private RowMapper getDashboardSubTileMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				DashboardTilesVb vObject = new DashboardTilesVb();
				vObject.setDashboardId(rs.getString("Dashboard_ID"));
				vObject.setTabId(rs.getString("Tab_ID"));
				vObject.setTileSequence(rs.getInt("PARENT_SEQUENCE"));
				vObject.setSubSequence(rs.getString("SUB_SEQUENCE"));
				vObject.setSubTileId(rs.getString("SUB_TILE_ID"));
				vObject.setTileCaption(rs.getString("Tile_Caption"));
				vObject.setQueryId(rs.getString("Query_ID"));
				vObject.setTileType(rs.getString("TILE_TYPE"));
				vObject.setPropertyAttr(rs.getString("TILE_PROPERTY_XML"));
				vObject.setPlaceHolderCnt(rs.getInt("PLACE_HOLDER_COUNT"));
				vObject.setChartType(rs.getString("CHART_TYPE"));
				vObject.setDoubleWidthFlag(rs.getString("DOUBLE_WIDTH_FLAG"));
				if ("Y".equalsIgnoreCase(rs.getString("DRILL_DOWN_FLAG"))) {
					vObject.setDrillDownFlag(true);
				} else {
					vObject.setDrillDownFlag(false);
				}
				vObject.setThemeAT(rs.getInt("Theme_AT"));
				vObject.setTheme(rs.getString("Theme"));
				return vObject;
			}
		};
		return mapper;
	}

	// V1.02 Version New Features
	public List<AlphaSubTabVb> getDashboadList(String dashboardGroup, String applicationId) throws DataAccessException {
		VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
		String sql = "";
		if("ORACLE".equalsIgnoreCase(databaseType)) {
			sql = " Select t2.DASHBOARD_ID,t2.DASHBOARD_NAME from PRD_DASHBOARD_ACCESS t1,PRD_DASHBOARDS t2 "
				+ " where t1.Dashboard_ID  = t2.DASHBOARD_ID and T1.USER_GROUP||'-'||T1.USER_PROFILE = ? "
				+ " AND T2.APPLICATION_ID  = ? AND  T2.Dashboard_Group = ? AND T1.PRODUCT_NAME = T2.APPLICATION_ID ";
		}else if("MSSQL".equalsIgnoreCase(databaseType)) {
			sql = " Select t2.DASHBOARD_ID,t2.DASHBOARD_NAME from PRD_DASHBOARD_ACCESS t1,PRD_DASHBOARDS t2 "
				+ " where t1.Dashboard_ID  = t2.DASHBOARD_ID and T1.USER_GROUP+'-'+T1.USER_PROFILE = ? "
				+ " AND T2.APPLICATION_ID  = ? AND  T2.Dashboard_Group = ? AND T1.PRODUCT_NAME = T2.APPLICATION_ID ";
		}
				
		Object[] lParams = new Object[3];
		lParams[0] = visionUsersVb.getUserGrpProfile();
		lParams[1] = applicationId;
		lParams[2] = dashboardGroup;
		return getJdbcTemplate().query(sql, lParams, getDashboardMapper());
	}

	protected RowMapper getDashboardMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb alphaSubTabVb = new AlphaSubTabVb();
				alphaSubTabVb.setAlphaSubTab(rs.getString("Dashboard_ID"));
				alphaSubTabVb.setDescription(rs.getString("Dashboard_Name"));
				return alphaSubTabVb;
			}
		};
		return mapper;
	}

	public ExceptionCode callProcforReportData(DashboardTilesVb vObject, String procedure,Boolean isSingleton) {
		setServiceDefaults();
		strCurrentOperation = "Query";
		strErrorDesc = "";
		Connection conExt = null;
		CallableStatement cs = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		PromptTreeVb promptTreeVb = new PromptTreeVb();
		try {
			if (!ValidationUtil.isValid(vObject.getDbConnection())
					|| "DEFAULT".equalsIgnoreCase(vObject.getDbConnection())) {
				conExt = getConnection();
			} else {
				String dbScript = commonDao.getScriptValue(vObject.getDbConnection());
				ExceptionCode exceptionCodeCon = CommonUtils.getConnection(dbScript);
				if (exceptionCodeCon != null && exceptionCodeCon.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
					conExt = (Connection) exceptionCodeCon.getResponse();
				} else {
					exceptionCode = exceptionCodeCon;
					return exceptionCode;
				}
			}
			if (!ValidationUtil.isValid(procedure)) {
				strErrorDesc = "Invalid Procedure in PRD_QUERY_CONFIG table for report Id[" + vObject.getDashboardId()
						+ "].";
				throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
			}
			cs = conExt.prepareCall("{call " + procedure + "}");

			cs.registerOutParameter(1, java.sql.Types.VARCHAR); // Status
			cs.registerOutParameter(2, java.sql.Types.VARCHAR); // Error Message
			cs.registerOutParameter(3, java.sql.Types.VARCHAR); // Table Name
			cs.registerOutParameter(4, java.sql.Types.VARCHAR); // Column Headers
			
			cs.execute();
			
			exceptionCode.setErrorCode(cs.getInt(1));
			exceptionCode.setErrorMsg(cs.getString(2));
			promptTreeVb.setTableName(cs.getString(3));
			promptTreeVb.setColumnHeaderTable(cs.getString(4));
			promptTreeVb.setSessionId(vObject.getDateCreation());
			promptTreeVb.setReportId(vObject.getTileId());
			exceptionCode.setResponse(promptTreeVb);
			cs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			exceptionCode.setErrorMsg(ex.getMessage());
			exceptionCode.setErrorCode(Constants.PASSIVATE);
			return exceptionCode;
		} finally {
			JdbcUtils.closeStatement(cs);
			try {
				conExt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
		return exceptionCode;
	}
	public String getTilePropertyXml(DashboardTilesVb dObj) {
		String query = "";
		String tilePropertyXml = "";
		try {
			if (!dObj.getIsDrillDown()) {
				query = "SELECT TILE_PROPERTY_XML FROM PRD_DASHBOARD_TILES WHERE DASHBOARD_ID  = ?"
						+ " AND TAB_ID  = ? AND SEQUENCE = ? ";
				Object[] lParams = new Object[3];
				lParams[0] = dObj.getDashboardId().toUpperCase();
				lParams[1] = dObj.getTabId();
				lParams[2] = dObj.getTileSequence();
				tilePropertyXml = getJdbcTemplate().queryForObject(query, lParams,String.class);
			}else if(ValidationUtil.isValid(dObj.getTransitionEffect()) && "DYNAMIC_XML_SUMMARY_TABLE".equalsIgnoreCase(dObj.getTransitionEffect())){
				String ddKey1Val = dObj.getDrillDownKey10().replaceAll("'", ""); 
				String ddKey1 = ddKey1Val.substring(0, ddKey1Val.indexOf("#"));
				query = "SELECT TABLE_ROWS_HEADERS FROM DQ_PJ_MON_OUT_SUMMARY T1 "
						+ " WHERE upper(T1.CLIENT_ID||'|'||T1.PROJECT_ID||'|'||T1.MONITOR_ID||'|'||T1.CONNECTOR_ID||'|'||T1.DQ_TABLE_NAME) = "+dObj.getDrillDownKey0()+" "
								+ " AND upper(T1.TABLE_VERSION_NO||'|'||T1.COLUMN_NAME||'|'||T1.RULE_GROUP_ID) = '"+ddKey1+"'";
				tilePropertyXml = getJdbcTemplate().queryForObject(query, String.class);
			}else if(ValidationUtil.isValid(dObj.getTransitionEffect()) && "DYNAMIC_XML_SUMMARY".equalsIgnoreCase(dObj.getTransitionEffect())){
				String ddKey1Val = dObj.getDrillDownKey1().replaceAll("'", ""); 
				String ddKey1 = ddKey1Val.substring(0, ddKey1Val.indexOf("#"));
				query = "SELECT DD_TABLE_COL_HEADERS FROM DQ_PJ_MON_OUT_SUMMARY T1 "
						+ " WHERE upper(T1.CLIENT_ID||'|'||T1.PROJECT_ID||'|'||T1.MONITOR_ID||'|'||T1.CONNECTOR_ID||'|'||T1.DQ_TABLE_NAME) = "+dObj.getDrillDownKey0()+" "
								+ " AND upper(T1.TABLE_VERSION_NO||'|'||T1.COLUMN_NAME||'|'||T1.RULE_GROUP_ID) = '"+ddKey1+"'";
				tilePropertyXml = getJdbcTemplate().queryForObject(query, String.class);
			}else if(ValidationUtil.isValid(dObj.getTransitionEffect()) && "DYNAMIC_XML_DETAILS".equalsIgnoreCase(dObj.getTransitionEffect())){
				String ddKey1Val = dObj.getDrillDownKey10().replaceAll("'", ""); 
				String ddKey1 = ddKey1Val.substring(0, ddKey1Val.indexOf("#"));
				query = "SELECT DD_TABLE_COL_HEADERS FROM DQ_PJ_MON_OUT_DETAILS T1 "
						+ " WHERE upper(T1.CLIENT_ID||'|'||T1.PROJECT_ID||'|'||T1.MONITOR_ID||'|'||T1.CONNECTOR_ID||'|'||T1.DQ_TABLE_NAME||'!@#'||T1.COLUMN_NAME) = "
						+ ""+dObj.getDrillDownKey0()+" "
								+ " AND upper(T1.TABLE_VERSION_NO||'|'||T1.RULE_ID||'|'||T1.COLUMN_NAME||'|'||T1.RULE_GROUP_ID) = '"+ddKey1+"'";
				tilePropertyXml = getJdbcTemplate().queryForObject(query, String.class);
			}else {
				query = "SELECT TILE_PROPERTY_XML FROM PRD_DASHBOARD_TILES_DD WHERE DASHBOARD_ID  = ?"
						+ " AND TAB_ID  = ? AND SEQUENCE = ? AND PARENT_SEQUENCE = ?";
				Object[] lParams = new Object[4];
				lParams[0] = dObj.getDashboardId().toUpperCase();
				lParams[1] = dObj.getTabId();
				lParams[2] = dObj.getTileSequence();
				lParams[3] = dObj.getParentSequence();
				tilePropertyXml = getJdbcTemplate().queryForObject(query, lParams,String.class);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return tilePropertyXml;
	}
	public String getChartType(String chartType) {
		String strQuery = new String("SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE "
					+ " ALPHA_TAB = 7005 AND ALPHA_SUB_TAB = ? ");
		Object args[] = {chartType};
		try {
			return getJdbcTemplate().queryForObject(strQuery, args, String.class);
		} catch (Exception ex) {
			return "";
		}
	}
	public int savedThemeExists(String dashboardId){
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			query = "SELECT COUNT(1) FROM PRD_DASHBOARD_USER_DEF WHERE DASHBOARD_ID = ? AND VISION_ID = ? ";	
			Object[] args = {dashboardId,intCurrentUserId};
			retVal= getJdbcTemplate().queryForObject(query,args,Integer.class);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while inserting into the PRD_USER_WIDGETS...!!");
			return 0;
		}
	}
	public int saveDashboardUserSefExists(DashboardTilesVb dObj,Boolean ddFlag){
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			if(!ddFlag)
				dObj.setParentSequence(0);
			query = "SELECT COUNT(1) FROM PRD_DASHBOARDS_USER_DET WHERE DASHBOARD_ID = ? AND VISION_ID = ? "
					+ " AND TAB_ID = ? AND SEQUENCE = ? AND PARENT_SEQUENCE = ? and TILE_ID = ?	";	
			Object[] args = {dObj.getDashboardId(),intCurrentUserId,dObj.getTabId(),dObj.getTileSequence(),dObj.getParentSequence(),dObj.getTileId()};
			retVal= getJdbcTemplate().queryForObject(query,args,Integer.class);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while inserting into the PRD_USER_WIDGETS...!!");
			return 0;
		}
	}
	public int deleteDashboardThemeExists(DashboarUserDefVb dObj){
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			query = "DELETE FROM PRD_DASHBOARD_USER_DEF WHERE DASHBOARD_ID = ? AND VISION_ID = ? ";	
			Object[] args = {dObj.getDashboardId(),intCurrentUserId};
			retVal= getJdbcTemplate().update(query,args);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while deleteing into the PRD_DASHBOARD_USER_DEF...!!");
			return 0;
		}
	}
	public int saveDashboardTheme(DashboarUserDefVb dObj) {
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			query = "Insert Into PRD_DASHBOARD_USER_DEF (VISION_ID, DASHBOARD_ID,  DS_THEME,  "+
					" PROMPT_VALUE_1,PROMPT_VALUE_2,PROMPT_VALUE_3,PROMPT_VALUE_4,PROMPT_VALUE_5, "+
					" PROMPT_VALUE_6,PROMPT_VALUE_7,PROMPT_VALUE_8,PROMPT_VALUE_9,PROMPT_VALUE_10, "+
					" STATUS,RECORD_INDICATOR,MAKER,VERIFIER,DATE_LAST_MODIFIED,DATE_CREATION) "+
					"Values (?,?,?,?,?,?,?,?,?,?,?,?,?,0,0,?,?,"+getDbFunction("SYSDATE")+","+getDbFunction("SYSDATE")+")";	

			Object[] args = {intCurrentUserId,dObj.getDashboardId(),dObj.getDsTheme(),
					dObj.getPromptValue1(),dObj.getPromptValue2(),dObj.getPromptValue3(),dObj.getPromptValue4(),dObj.getPromptValue5(),
					dObj.getPromptValue6(),dObj.getPromptValue7(),dObj.getPromptValue8(),dObj.getPromptValue9(),dObj.getPromptValue10(),
					intCurrentUserId,intCurrentUserId};
			retVal= getJdbcTemplate().update(query,args);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while inserting into the PRD_DASHBOARD_USER_DEF...!!");
			return 0;
		}
	}
	public int deleteDashboardUserSettingExists(DashboardTilesVb dObj){
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			query = "DELETE FROM PRD_DASHBOARDS_USER_DET WHERE DASHBOARD_ID = ? AND VISION_ID = ? "
					+ " AND TAB_ID = ? AND SEQUENCE = ? AND PARENT_SEQUENCE = ? AND TILE_ID = ?";	
			Object[] args = {dObj.getDashboardId(),intCurrentUserId,dObj.getTabId(),dObj.getTileSequence(),
					dObj.getParentSequence(),dObj.getTileId()};
			retVal= getJdbcTemplate().update(query,args);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while deleting from the PRD_DASHBOARDS_USER_DET...!!");
			return 0;
		}
	}
	public int deleteDashboardUserSettingExists(String dashboardId){
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			query = "DELETE FROM PRD_DASHBOARDS_USER_DET WHERE DASHBOARD_ID = ? AND VISION_ID = ? ";
			Object[] args = {dashboardId,intCurrentUserId};
			retVal= getJdbcTemplate().update(query,args);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while deleting from the PRD_DASHBOARDS_USER_DET...!!");
			return 0;
		}
	}
	public int saveDashboardUserSettings(DashboardTilesVb dObj) {
		setServiceDefaults();
		int retVal = 0;
		String query = "";
		try {
			query = "Insert Into PRD_DASHBOARDS_USER_DET (VISION_ID,DASHBOARD_ID,TAB_ID,SEQUENCE,PARENT_SEQUENCE,TILE_ID,CHART_TYPE,THEME, "+
					" STATUS,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION) "+
					"Values (?,?,?,?,?,?,?,?,0,0,?,?,0,"+getDbFunction("SYSDATE")+","+getDbFunction("SYSDATE")+")";	

			Object[] args = {intCurrentUserId,dObj.getDashboardId(),
					dObj.getTabId(),dObj.getTileSequence(),dObj.getParentSequence(),dObj.getTileId(),dObj.getChartType(),
					dObj.getTheme(),intCurrentUserId,intCurrentUserId};
			retVal= getJdbcTemplate().update(query,args);
			return retVal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception while inserting into the PRD_DASHBOARD_USER_DEF...!!");
			return 0;
		}
	}
	public String getSavedChart(DashboardTilesVb dObj,Boolean ddFlag) {
		setServiceDefaults();
		String savedChartType  = "";
		try {
			if(!ddFlag)
				dObj.setParentSequence(0);
			String sql = "SELECT CHART_TYPE FROM PRD_DASHBOARDS_USER_DET  WHERE DASHBOARD_ID = ? AND VISION_ID = ? "
					+ " AND TAB_ID = ? AND SEQUENCE = ? AND PARENT_SEQUENCE = ? AND TILE_ID = ?";
			Object[] args = {dObj.getDashboardId(),intCurrentUserId,dObj.getTabId(),dObj.getTileSequence(),
					dObj.getParentSequence(),dObj.getTileId()};
			savedChartType =  getJdbcTemplate().queryForObject(sql, args, String.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return savedChartType;
	}
	public String getSavedTheme(String dashboardId) {
		String savedChartType  = "";
		try {
			String sql = "SELECT DS_THEME FROM PRD_DASHBOARD_USER_DEF  WHERE DASHBOARD_ID = ? AND VISION_ID = ? ";
			Object[] args = {dashboardId,intCurrentUserId};
			return getJdbcTemplate().queryForObject(sql, args, String.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return savedChartType;
	}
	@Override
	protected void setServiceDefaults(){
		serviceName = "DashboardsDao";
		serviceDesc = "DashboardsDao";;
		tableName = "PRD_DASHBOARDS";
		childTableName = "PRD_DASHBOARDS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
	public String pfPromptHashReplace(String query,String restrictStr,String restrictVal) {
		try {
			String replaceStr = "";
			String orgSbuStr = StringUtils.substringBetween(query, restrictStr, ")#");
			if (ValidationUtil.isValid(restrictVal) && !"'ALL'".equalsIgnoreCase(restrictVal))
				replaceStr = " AND " + orgSbuStr + " IN (" + restrictVal + ")";
			restrictStr = restrictStr.replace("(", "\\(");
			orgSbuStr = orgSbuStr.replace("|", "\\|");
			query = query.replaceAll(restrictStr + orgSbuStr + "\\)#", replaceStr);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return query;
	}
	public String applyPrPromptChange(String sqlQuery,DashboardTilesVb promptVb) {
		VisionUsersVb visionUserVb = CustomContextHolder.getContext();
		//VU_CLEB,VU_CLEB_AO,VU_CLEB_LV,VU_SBU,VU_PRODUCT,VU_OUC
		if(sqlQuery.contains("#PF_PROMPT_VALUE_1")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_1(", promptVb.getPromptValue1());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_2")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_2(", promptVb.getPromptValue2());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_3")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_3(", promptVb.getPromptValue3());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_4")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_4(", promptVb.getPromptValue4());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_5")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_5(", promptVb.getPromptValue5());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_6")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_6(", promptVb.getPromptValue6());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_7")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_7(", promptVb.getPromptValue7());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_8")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_8(", promptVb.getPromptValue8());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_9")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_9(", promptVb.getPromptValue9());
		if(sqlQuery.contains("#PF_PROMPT_VALUE_10")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_10(", promptVb.getPromptValue10());
		
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_1")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_1(", promptVb.getPromptValue1().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_2")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_2(", promptVb.getPromptValue2().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_3")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_3(", promptVb.getPromptValue3().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_4")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_4(", promptVb.getPromptValue4().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_5")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_5(", promptVb.getPromptValue5().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_6")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_6(", promptVb.getPromptValue6().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_7")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_7(", promptVb.getPromptValue7().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_8")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_8(", promptVb.getPromptValue8().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_9")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_9(", promptVb.getPromptValue9().replaceAll("'", ""));
		if(sqlQuery.contains("#PF_NS_PROMPT_VALUE_10")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_10(", promptVb.getPromptValue10().replaceAll("'", ""));
		
		if(sqlQuery.contains("#PF_DDKEY1")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY1(", promptVb.getDrillDownKey1());
		if(sqlQuery.contains("#PF_DDKEY2")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY2(", promptVb.getDrillDownKey2());
		if(sqlQuery.contains("#PF_DDKEY3")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY3(", promptVb.getDrillDownKey3());
		if(sqlQuery.contains("#PF_DDKEY4")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY4(", promptVb.getDrillDownKey4());
		if(sqlQuery.contains("#PF_DDKEY5")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY5(", promptVb.getDrillDownKey5());
		if(sqlQuery.contains("#PF_DDKEY6")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY6(", promptVb.getDrillDownKey6());
		if(sqlQuery.contains("#PF_DDKEY7")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY7(", promptVb.getDrillDownKey7());
		if(sqlQuery.contains("#PF_DDKEY8")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY8(", promptVb.getDrillDownKey8());
		if(sqlQuery.contains("#PF_DDKEY9")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY9(", promptVb.getDrillDownKey9());
		if(sqlQuery.contains("#PF_DDKEY10")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY10(", promptVb.getDrillDownKey10());
		if(sqlQuery.contains("#PF_DDKEY0")) 
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY10(", promptVb.getDrillDownKey0());
		
		return sqlQuery;
	}
	public String applySpecialPrompts(String sqlQuery,DashboardTilesVb promptVb) {
		try {
			String promptArray[][] = new String[10][10];
			if(ValidationUtil.isValid(promptVb.getPromptValue1())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue1(),sqlQuery,promptArray,0);
			}if(ValidationUtil.isValid(promptVb.getPromptValue2())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue2(),sqlQuery,promptArray,1);
			}if(ValidationUtil.isValid(promptVb.getPromptValue3())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue3(),sqlQuery,promptArray,2);
			}if(ValidationUtil.isValid(promptVb.getPromptValue4())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue4(),sqlQuery,promptArray,3);
			}if(ValidationUtil.isValid(promptVb.getPromptValue5())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue5(),sqlQuery,promptArray,4);
			}if(ValidationUtil.isValid(promptVb.getPromptValue6())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue6(),sqlQuery,promptArray,5);
			}if(ValidationUtil.isValid(promptVb.getPromptValue7())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue7(),sqlQuery,promptArray,6);
			}if(ValidationUtil.isValid(promptVb.getPromptValue8())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue8(),sqlQuery,promptArray,7);
			}if(ValidationUtil.isValid(promptVb.getPromptValue9())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue9(),sqlQuery,promptArray,8);
			}if(ValidationUtil.isValid(promptVb.getPromptValue10())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue10(),sqlQuery,promptArray,9);
			}
			for(int i = 1;i < 11;i++) {
				for(int j = 1;j < 11; j++) {
					if(ValidationUtil.isValid(promptArray[i-1][j-1])) {
						if(sqlQuery.contains("#PROMPT_VALUE_"+i+"."+j+"#")) 
							sqlQuery =sqlQuery.replace("#PROMPT_VALUE_"+i+"."+j+"#", promptArray[i-1][j-1]);
						if(sqlQuery.contains("#NS_PROMPT_VALUE_"+i+"."+j+"#"))
							sqlQuery =sqlQuery.replace("#NS_PROMPT_VALUE_"+i+"."+j+"#", promptArray[i-1][j-1].replaceAll("'", ""));
						if(sqlQuery.contains("#PF_PROMPT_VALUE_"+i+"."+j+"#"))
							sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_"+i+"."+j+"(", promptArray[i-1][j-1]);
					}else {
						sqlQuery =sqlQuery.replace("#PROMPT_VALUE_"+i+"."+j+"#", "NULL");
						sqlQuery =sqlQuery.replace("#NS_PROMPT_VALUE_"+i+"."+j+"#", "NULL");
					}
				}	
			}
			
			String promptDrillDwnArray[][] = new String[10][10];
			if(ValidationUtil.isValid(promptVb.getDrillDownKey1())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey1(),sqlQuery,promptDrillDwnArray,0);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey2())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey2(),sqlQuery,promptDrillDwnArray,1);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey3())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey3(),sqlQuery,promptDrillDwnArray,2);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey4())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey4(),sqlQuery,promptDrillDwnArray,3);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey5())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey5(),sqlQuery,promptDrillDwnArray,4);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey6())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey6(),sqlQuery,promptDrillDwnArray,5);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey7())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey7(),sqlQuery,promptDrillDwnArray,6);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey8())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey8(),sqlQuery,promptDrillDwnArray,7);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey9())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey9(),sqlQuery,promptDrillDwnArray,8);
			}if(ValidationUtil.isValid(promptVb.getDrillDownKey10())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey10(),sqlQuery,promptDrillDwnArray,9);
			}
			for(int i = 1;i < 11;i++) {
				for(int j = 1;j < 11; j++) {
					if(ValidationUtil.isValid(promptDrillDwnArray[i-1][j-1])) {
						if(sqlQuery.contains("#DDKEY"+i+"."+j+"#"))
							sqlQuery =sqlQuery.replace("#DDKEY"+i+"."+j+"#", promptDrillDwnArray[i-1][j-1]);
						if(sqlQuery.contains("#NS_DDKEY"+i+"."+j+"#")) 
							sqlQuery =sqlQuery.replace("#NS_DDKEY"+i+"."+j+"#", promptDrillDwnArray[i-1][j-1].replaceAll(",", ""));
						if(sqlQuery.contains("#PF_DDKEY"+i+"."+j+"#")) 
							sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY"+i+"."+j+"(", promptDrillDwnArray[i-1][j-1]);
					}else {
						sqlQuery =sqlQuery.replace("#DDKEY"+i+"."+j+"#", "NULL");
						sqlQuery =sqlQuery.replace("#NS_DDKEY"+i+"."+j+"#", "NULL");
					}
				}	
			}
			return sqlQuery;
		}catch(Exception ex) {
			ex.printStackTrace();
			return sqlQuery;
		}
	}
	public List<ReportsVb> getDashboardFolderStructure() throws SQLException{
		setServiceDefaults();
		VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
		String query = "";
		String extraCondition = "";
		visionUsersVb.setApplicationAccess(visionUsersVb.getApplicationAccess().replaceAll(",", "','"));
		if("UBA".equalsIgnoreCase(clientName)) {
			
			extraCondition ="  UNION ALL                                                               		"+
						"          SELECT TRIM (vu.auto_grp_profile) vu_grp_profile                         "+
						"            FROM vision_users_vw vu                                                "+
						"           WHERE vu.vision_id = ? AND vu.allow_auto_profile_Flag = 'Y'   "+
						"          UNION ALL                                                                "+
						"          SELECT vulink.user_group || '-' || vulink.user_profile                   "+
						"                    vu_grp_profile                                                 "+
						"            FROM vision_ad_profile_link vulink                                     "+
						"           WHERE     (SELECT vu.allow_ad_profile_flag                              "+
						"                        FROM vision_users_vw vu                                    "+
						"                       WHERE vu.vision_id = ?) = 'Y'          "+
						"                 AND vulink.profile_linker_status = 0 " ;
		}
		try{
			if("ORACLE".equalsIgnoreCase(databaseType)) {
				query = " WITH vision_user_profile_v1                                                       "+
						"      AS (SELECT TRIM (vu.user_grp_profile) vu_grp_profile                         "+
						"            FROM vision_users_vw vu                                                "+
						"           WHERE vu.vision_id = ?                            "+
						" "+extraCondition+" 																"+
						"          ),                            											"+
						"      vision_user_profile_v2                                                       "+
						"      AS (SELECT LISTAGG (v1.vu_grp_profile, ',') WITHIN GROUP (ORDER BY NULL)     "+
						"                    vu_grp_profile                                                 "+
						"            FROM vision_user_profile_v1 v1),                                       "+
						"      vision_user_profile_v3                                                       "+
						"      AS (    SELECT fn_rs_parsestring (                                           "+
						"                        fn_rs_parsestring (v2.vu_grp_profile, LEVEL, ','),         "+
						"                        1,                                                         "+
						"                        '-')                                                       "+
						"                        vu_grp,                                                    "+
						"                     fn_rs_parsestring (                                           "+
						"                        fn_rs_parsestring (v2.vu_grp_profile, LEVEL, ','),         "+
						"                        2,                                                         "+
						"                        '-')                                                       "+
						"                        vu_profile                                                 "+
						"                FROM vision_user_profile_v2 v2                                     "+
						"          CONNECT BY LEVEL <=                                                      "+
						"                        (  LENGTH (v2.vu_grp_profile)                              "+
						"                         - LENGTH (REPLACE (v2.vu_grp_profile, ',', ''))           "+
						"                         + 1)),                                                    "+
						"      vision_user_profile_v4                                                       "+
						"      AS (SELECT DISTINCT v3.vu_grp, v3.vu_profile                                 "+
						"            FROM vision_user_profile_v3 v3),										"+
						"      rs_access_v1                                                                 "+
						"      AS (SELECT DISTINCT rs.DASHBOARD_ID, rs_rcl.rs_category                      "+
						"            FROM PRD_DASHBOARDS rs,                                                "+
						"                 PRD_DASHBOARD_CAT_LINKER rs_rcl,                                  "+
						"                 PRD_DASHBOARD_ACCESS rs_a,                                        "+
						"                 vision_user_profile_v4 v4,                                        "+
						"                 prd_dashboard_categories rs_c                                     "+
						"           WHERE     rs.dashboard_id = rs_rcl.dashboard_id                         "+
						"                 AND rs_rcl.rs_link_status = 0                                     "+
						"                 AND rs_rcl.rs_category = rs_c.rs_category                         "+
						"                 AND rs_c.rs_cat_status = 0                                        "+
						"                 AND rs.application_id IN ('"+visionUsersVb.getApplicationAccess()+"') "+
						"                 AND rs.status = 0                                                 "+
						"                 AND (   (    rs_a.rs_type = 'CATEGORY'                            "+
						"                          AND rs_rcl.rs_category IN                                "+
						"                                 (    SELECT rs_cat.rs_category                    "+
						"                                        FROM PRD_dashboard_CATEGORIES rs_cat       "+
						"                                  START WITH rs_cat.rs_category = rs_a.rs_id       "+
						"                                  CONNECT BY     PRIOR rs_cat.rs_category =        "+
						"                                                    rs_cat.Rs_Parent_Category      "+
						"                                             AND rs_cat.rs_category !=             "+
						"                                                    rs_cat.Rs_Parent_Category))    "+
						"                      OR (    rs_a.rs_type = 'REPORT'                              "+
						"                          AND rs_rcl.dashboard_id = rs_a.rs_id))                   "+
						"                 AND rs_a.user_group = v4.vu_grp                                   "+
						"                 AND rs_a.user_profile = v4.vu_profile                             "+
						"                 AND rs_a.profile_status = 0)                                      "+
						"   SELECT *                                                                        "+
						"     FROM (    SELECT rs_c.rs_parent_category rs_parent_category,                  "+
						"                      (SELECT x1.rs_category_desc                                  "+
						"                         FROM PRD_DASHBOARD_CATEGORIES x1                          "+
						"                        WHERE x1.rs_category = rs_c.rs_parent_category)            "+
						"                         rs_par_category_desc,                                     "+
						"                      rs_c.rs_category,                                            "+
						"                      rs_c.rs_category_desc,                                       "+
						"                      'F' FRONT_END_DISPLAY_TYPE,                                  "+
						"                      1 xx_display_order,                                          "+
						"                      '' REPORT_ID,                                                "+
						"                      '' REPORT_TITLE                                              "+
						"                 FROM PRD_dashboard_CATEGORIES rs_c                                "+
						"           START WITH rs_c.rs_category IN (SELECT DISTINCT v1.rs_category          "+
						"                                             FROM rs_access_v1 v1)                 "+
						"           CONNECT BY NOCYCLE     rs_c.rs_category =                               "+
						"                                     PRIOR rs_c.rs_parent_category                 "+
						"                              AND PRIOR rs_c.rs_category IS NOT NULL               "+
						"           UNION                                                                   "+
						"           SELECT rs_rcl.rs_category rs_parent_category,                           "+
						"                  rs_c.rs_category_desc rs_par_category_desc,                      "+
						"                  rs.dashboard_id rs_category,                                     "+
						"                  rs.dashboard_name rs_category_desc,                              "+
						"                  'R' FRONT_END_DISPLAY_TYPE,                                      "+
						"                  5000 + rs_rcl.DISPLAY_ORDER xx_display_order,                    "+
						"                  rs.dashboard_id REPORT_ID,                                       "+
						"                  rs.dashboard_name REPORT_TITLE                                   "+
						"             FROM PRD_dashboards rs,                                               "+
						"                  PRD_DASHBOARD_CAT_LINKER rs_rcl,                                 "+
						"                  PRD_DASHBOARD_CATEGORIES rs_c,                                   "+
						"                  rs_access_v1 v1                                                  "+
						"            WHERE     rs.dashboard_id = rs_rcl.dashboard_id                        "+
						"                  AND rs_rcl.rs_category = rs_c.rs_category                        "+
						"                  AND rs_rcl.dashboard_id = v1.dashboard_id                        "+
						"                  AND rs_rcl.rs_category = v1.rs_category)                         "+
						" ORDER BY rs_parent_category,                                                      "+
						"          CASE WHEN FRONT_END_DISPLAY_TYPE = 'F' THEN rs_category END,             "+
						"          xx_display_order                                                         ";
			} else if ("MSSQL".equalsIgnoreCase(databaseType)) {
				query = " WITH VISION_USER_PROFILE_V1																						" +																	
						"     AS (SELECT (vu.user_grp_profile) vu_grp_profile                                                          " +
						"           FROM vision_users_vw vu        "+                                                                         
						"           WHERE vu.vision_id = ? ),"
						+ "vision_user_profile_v2                                                                                        " +
						"     AS (SELECT distinct STUFF((                                                                                   " +
						"				SELECT ','+T1.vu_grp_profile                                                                        " +
						"				FROM vision_user_profile_v1 T1                                                                      " +
						"				FOR XML PATH('')), 1, LEN(','), '') AS vu_grp_profile                                               " +
						"				FROM vision_user_profile_v1 t0                                                                      " +
						"				GROUP BY t0.vu_grp_profile),                                                                        " +
						"     vision_user_profile_v3                                                                                        " +
						"     AS (SELECT DBO.fn_rs_parsestring (DBO.fn_rs_parsestring (v2.vu_grp_profile, 1, ','), 1, '-') vu_grp,          " +
						"                    DBO.fn_rs_parsestring (DBO.fn_rs_parsestring (v2.vu_grp_profile, 1, ','), 2, '-') vu_profile,  " +
						"					1 AS number                                                                                     " +
						"		FROM vision_user_profile_v2 V2                                                                              " +
						"     UNION ALL                                                                                                     " +
						"     SELECT DBO.fn_rs_parsestring (DBO.fn_rs_parsestring (v2.vu_grp_profile, CTE.number, ','), 1, '-') vu_grp,     " +
						"                    DBO.fn_rs_parsestring (DBO.fn_rs_parsestring (v2.vu_grp_profile, CTE.number, ','), 2, '-') vu_profile, " +
						"					number+1 AS number																				" +
						"     FROM vision_user_profile_v2 V2, vision_user_profile_v3 CTE                                                    " +
						"	 WHERE                                                                                                          " +
						"	 number<= (LEN (vu_grp_profile) - LEN (REPLACE (vu_grp_profile, ',', '')) + 1)),                                " +
						"					 vision_user_profile_v4 as                                                                      " +
						"					 (SELECT DISTINCT v3.vu_grp, v3.vu_profile FROM vision_user_profile_v3 v3 WHERE number!=1),     " 
						+ "rs_access_v0 as (                                                    "
						+ "select distinct rs.DASHBOARD_ID,rs_rcl.rs_category                   "
						+ "from PRD_DASHBOARDS rs,                                              "
						+ "PRD_DASHBOARD_CAT_LINKER rs_rcl,                                     "
						+ "PRD_DASHBOARD_ACCESS rs_a,                                           "
						+ "vision_user_profile_v4 v4,                                           "
						+ "prd_dashboard_categories rs_c                                        "
						+ "where rs.DASHBOARD_ID = rs_rcl.DASHBOARD_ID                          "
						+ "and rs_rcl.rs_link_status = 0                                        "
						+ "and rs_rcl.rs_category = rs_c.rs_category                            "
						+ "and rs_c.rs_cat_status = 0                                           "
						+ "and rs.application_id in ('"+visionUsersVb.getApplicationAccess()+"')  and rs.status = 0                   "
						+ "and rs_a.rs_type = 'REPORT'                                          "
						+ "and rs_rcl.DASHBOARD_ID = rs_a.DASHBOARD_ID                          "
						+ "and rs_a.user_group = v4.vu_grp                                      "
						+ "and rs_a.user_profile = v4.vu_profile                                "
						+ "and rs_a.profile_status = 0 ),                                       "
						+ "rs_access_v2 as(                                                     "
						+ "select distinct rsa_v1.DASHBOARD_ID,rsa_v1.rs_category               "
						+ "From rs_access_v0 rsa_v1 union                                       "
						+ "select rs_rcl.DASHBOARD_ID, rs_rcl.rs_category                       "
						+ "from PRD_DASHBOARD_CAT_LINKER rs_rcl                                 "
						+ "where rs_rcl.rs_category in (select distinct rsa_v0.rs_category      "
						+ "from rs_access_v0 rsa_v0)),                                          "
						+ "rs_final_category as( select  rs_c.rs_parent_category,               "
						+ "(select x1.rs_category_desc                                          "
						+ "from PRD_DASHBOARD_CATEGORIES x1                                     "
						+ "where x1.rs_category=rs_c.rs_parent_category)                        "
						+ " rs_par_category_desc,                                               "
						+ "rs_c.rs_category,  rs_c.rs_category_desc,                            "
						+ "'F' FRONT_END_DISPLAY_TYPE,                                          "
						+ "1 xx_display_order                                                   "
						+ "from PRD_DASHBOARD_CATEGORIES rs_c                                   "
						+ "where rs_c.rs_category in (select x.rs_category from rs_access_v2 x) "
						+ "union all select                                                     "
						+ "rs_c.rs_parent_category,                                             "
						+ "(select x1.rs_category_desc                                          "
						+ "from PRD_dashboard_CATEGORIES x1 where                               "
						+ "x1.rs_category=rs_c.rs_parent_category) rs_par_category_desc,        "
						+ "rs_c.rs_category,                                                    "
						+ "rs_c.rs_category_desc,                                               "
						+ "'F' FRONT_END_DISPLAY_TYPE,                                          "
						+ "1 xx_display_order                                                   "
						+ "from prd_dashboard_categories rs_c, rs_final_category priorr         "
						+ "where rs_c.rs_category = priorr.rs_parent_category                   "
						+ "and rs_c.rs_parent_category != priorr.rs_category )                  "
						+ "select distinct *,                                                   "
						+ " null REPORT_ID,'' REPORT_TITLE                                         "
						+ "from rs_final_category                                               "
						+ "union all select                                                     "
						+ "rs_rcl.rs_category rs_parent_category,                               "
						+ "rs_c.rs_category_desc rs_par_category_desc,                          "
						+ "rs.DASHBOARD_ID rs_category,                                         "
						+ "rs.DASHBOARD_NAME rs_category_desc,                                  "
						+ "'R' FRONT_END_DISPLAY_TYPE,                                          "
						+ "5000 + rs_rcl.DISPLAY_ORDER xx_display_order,                        "
						+ "RS.DASHBOARD_ID REPORT_ID,                                           "
						+ "RS.DASHBOARD_NAME REPORT_TITLE                                       "
						+ "from PRD_DASHBOARDS rs,                                              "
						+ "PRD_DASHBOARD_CAT_LINKER rs_rcl,                                     "
						+ "PRD_dashboard_CATEGORIES rs_c,                                       "
						+ "rs_access_v0 v1                                                      "
						+ "Where rs.DASHBOARD_ID = rs_rcl.DASHBOARD_ID                          "
						+ "and rs_rcl.rs_category = rs_c.rs_category                            "
						+ "and rs_rcl.DASHBOARD_ID = v1.DASHBOARD_ID                            "
						+ "and rs_rcl.rs_category = v1.rs_category                              ";

			}
			List<ReportsVb> tempPromptsList = null;
			if("UBA".equalsIgnoreCase(clientName)) {
				Object args[] = {intCurrentUserId,intCurrentUserId,intCurrentUserId};
				 tempPromptsList = getJdbcTemplate().query(query, args, getDashboardListMapper());
			}else {
				Object args[] = {intCurrentUserId};
				 tempPromptsList = getJdbcTemplate().query(query, args, getDashboardListMapper());
			}
			return tempPromptsList;
		}
		catch(Exception ex){
			ex.printStackTrace();
			//logger.error(((query==null)? "query is Null":query));
			strErrorDesc = ex.getMessage(); 
			throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
		}
	}
	protected RowMapper getDashboardListMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReportsVb vObject = new ReportsVb();
				vObject.setReportCategory(rs.getString("RS_PARENT_CATEGORY"));
				vObject.setCategoryDesc(rs.getString("RS_PAR_CATEGORY_DESC"));
				vObject.setSubCategory(rs.getString("RS_CATEGORY"));
				vObject.setSubCategoryDesc(rs.getString("RS_CATEGORY_DESC"));
				vObject.setDisplayType(rs.getString("FRONT_END_DISPLAY_TYPE"));
				vObject.setReportId(rs.getString("REPORT_ID"));
				if(vObject.getReportId() != null && vObject.getReportId() == "") {
					vObject.setReportId(null);
				}
				vObject.setReportTitle(rs.getString("REPORT_TITLE"));
				return vObject;
			}
		};
		return mapper;
	}
	public String getPdfGrwthWidth(ExportVb dObj) {
		String pdfWidth = "";
		try {
			String sql = "";
			sql = " SELECT PDF_WIDTH FROM PRD_DASHBOARD_TABS "+ 
				" WHERE DASHBOARD_ID = ? AND TAB_ID = ? ";
			Object[] lParams = new Object[2];
			lParams[0] = dObj.getDashboardId();
			lParams[1] = dObj.getTabId();
			pdfWidth = getJdbcTemplate().queryForObject(sql,lParams,String.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return pdfWidth;
	}
	public List<DashboardTilesVb> getDashboardTileDetailsPdf(DashboardTabVb dObj,Boolean exportFlag,String tileType) {
		setServiceDefaults();
		List<DashboardTilesVb> collTemp = null;
		String tileTypeQuery = "";
		/*if(!ValidationUtil.isValid(tileType))
			tileType = "'G','T'";
		else
			tileType = "'T'";*/
		
		if(exportFlag)
			tileTypeQuery  = " AND TILE_TYPE IN ("+tileType+")";
		try {
			String query = "Select Dashboard_ID,Tab_ID,Tile_ID,Tile_Caption,Sequence,Query_ID,TILE_TYPE,DRILL_DOWN_FLAG,CHART_TYPE,"
					/*+ "(SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE "
					+ " ALPHA_TAB = 7005 AND ALPHA_SUB_TAB = T1.CHART_TYPE) CHART_TYPE,"*/
					+ "TILE_PROPERTY_XML,PLACE_HOLDER_COUNT,DOUBLE_WIDTH_FLAG,SUB_TILES,Transition_Effect,Theme,PARENT_SEQUENCE from PRD_DASHBOARD_TILES T1 where Dashboard_ID = ?"
					+ " and Tab_ID = ? "+tileTypeQuery+" Order by Sequence ";
			Object args[] = {dObj.getDashboardId(),dObj.getTabId()};
			collTemp = getJdbcTemplate().query(query, args,getDashboardTileMapper());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while getDashboardTileDetails...!!");
			return null;
		}
	}
}