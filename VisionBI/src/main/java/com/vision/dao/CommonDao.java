package com.vision.dao;

import java.io.IOException;
import java.net.SocketException;
import java.sql.Connection;
/**
 * @author Prabu.CJ
 *
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.CommonVb;
import com.vision.vb.LevelOfDisplayVb;
import com.vision.vb.MenuVb;
import com.vision.vb.ProfileData;
import com.vision.vb.VisionUsersVb;

import org.apache.commons.lang.StringUtils;
@Component
public class CommonDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Value("${app.productName}")
	private String productName;

	@Value("${app.databaseType}")
	private String databaseType;
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	CommonApiDao commonApiDao;

	public List<CommonVb> findVerificationRequiredAndStaticDelete(String pTableName) throws DataAccessException {

		String sql = "select DELETE_TYPE,VERIFICATION_REQD FROM VISION_TABLES where UPPER(TABLE_NAME) = UPPER(?)";
		Object[] lParams = new Object[1];
		lParams[0] = pTableName;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				CommonVb commonVb = new CommonVb();
				commonVb.setStaticDelete(rs.getString("DELETE_TYPE") == null || rs.getString("DELETE_TYPE").equalsIgnoreCase("S") ? true : false);
				commonVb.setVerificationRequired(rs.getString("VERIFICATION_REQD") == null || rs.getString("VERIFICATION_REQD").equalsIgnoreCase("Y") ? true : false);
				return commonVb;
			}
		};
		List<CommonVb> commonVbs = getJdbcTemplate().query(sql, lParams, mapper);
		if (commonVbs == null || commonVbs.isEmpty()) {
			commonVbs = new ArrayList<CommonVb>();
			CommonVb commonVb = new CommonVb();
			commonVb.setStaticDelete(true);
			commonVb.setVerificationRequired(true);
			commonVbs.add(commonVb);
		}
		return commonVbs;
	}

	public List<ProfileData> getTopLevelMenu(int visionId) throws DataAccessException {
		String sql = "SELECT distinct NST.NUM_SUBTAB_DESCRIPTION, PP.MENU_GROUP,PP.MENU_ICON ,"
				+ "PP.P_ADD, PP.P_MODIFY, PP.P_DELETE, PP.P_INQUIRY, PP.P_VERIFICATION , PP.P_EXCEL_UPLOAD "
				+ "FROM PROFILE_PRIVILEGES PP, VISION_USERS MU, NUM_SUB_TAB NST where PP.USER_GROUP = MU.USER_GROUP and PP.USER_PROFILE = MU.USER_PROFILE "
				+ "and  NST.NUM_SUB_TAB = PP.MENU_GROUP and MU.VISION_ID = ? AND PP.PROFILE_STATUS = 0 AND NST.NUM_SUBTAB_STATUS=0 "
				+ "and num_tab = 176 and pp.Application_Access Like '%" + productName + "%' order by PP.MENU_GROUP";
		Object[] lParams = new Object[1];
		lParams[0] = visionId;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfileData profileData = new ProfileData();
				profileData.setMenuItem(rs.getString("NUM_SUBTAB_DESCRIPTION"));
				profileData.setMenuGroup(rs.getInt("MENU_GROUP"));
				profileData.setProfileAdd(rs.getString("P_ADD"));
				profileData.setProfileModify(rs.getString("P_MODIFY"));
				profileData.setProfileDelete(rs.getString("P_DELETE"));
				profileData.setProfileInquiry(rs.getString("P_INQUIRY"));
				profileData.setProfileVerification(rs.getString("P_VERIFICATION"));
				profileData.setProfileUpload(rs.getString("P_EXCEL_UPLOAD"));
				profileData.setMenuIcon(rs.getString("MENU_ICON"));
				return profileData;
			}
		};
		List<ProfileData> profileData = getJdbcTemplate().query(sql, lParams, mapper);
		return profileData;
	}

	/*
	 * public ArrayList<MenuVb> getSubMenuItemsForMenuGroup(int menuGroup) throws
	 * DataAccessException {
	 * 
	 * String sql =
	 * "SELECT * FROM VISION_MENU WHERE MENU_GROUP = ? AND MENU_STATUS = 0 AND UPPER(MENU_NAME) != 'SEPERATOR' and Application_Access Like '%"
	 * + productName + "%' ORDER BY PARENT_SEQUENCE, MENU_SEQUENCE"; Object[]
	 * lParams = new Object[1]; lParams[0] = menuGroup; RowMapper mapper = new
	 * RowMapper() { public Object mapRow(ResultSet rs, int rowNum) throws
	 * SQLException { MenuVb menuVb = new MenuVb();
	 * menuVb.setMenuProgram(rs.getString("MENU_PROGRAM"));
	 * menuVb.setMenuName(rs.getString("MENU_NAME"));
	 * menuVb.setMenuSequence(rs.getInt("MENU_SEQUENCE"));
	 * menuVb.setParentSequence(rs.getInt("PARENT_SEQUENCE"));
	 * menuVb.setSeparator(rs.getString("SEPARATOR"));
	 * menuVb.setMenuGroup(rs.getInt("MENU_GROUP"));
	 * menuVb.setMenuStatus(rs.getInt("MENU_STATUS")); //
	 * menuVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR")); return menuVb; } };
	 * ArrayList<MenuVb> menuList = (ArrayList<MenuVb>) getJdbcTemplate().query(sql,
	 * lParams, mapper); return menuList; }
	 */

	public String findVisionVariableValue(String pVariableName) throws DataAccessException {
		if (!ValidationUtil.isValid(pVariableName)) {
			return null;
		}
		String sql = "select VALUE FROM VISION_VARIABLES where UPPER(VARIABLE) = UPPER(?)";
		Object[] lParams = new Object[1];
		lParams[0] = pVariableName;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				CommonVb commonVb = new CommonVb();
				commonVb.setMakerName(rs.getString("VALUE"));
				return commonVb;
			}
		};
		List<CommonVb> commonVbs = getJdbcTemplate().query(sql, lParams, mapper);
		if (commonVbs != null && !commonVbs.isEmpty()) {
			return commonVbs.get(0).getMakerName();
		}
		return null;
	}

	public void findDefaultHomeScreen(VisionUsersVb vObject) throws DataAccessException {
		int count = 0;
		String sql = "SELECT COUNT(1) FROM PWT_REPORT_SUITE WHERE VISION_ID = " + vObject.getVisionId();
		count = getJdbcTemplate().queryForObject(sql, Integer.class);
		/*
		 * if(count>0){ vObject.setDefaultHomeScreen(true); }
		 */
	}

	public int getMaxOfId() {
		String sql = "select max(vision_id) from (Select max(vision_id) vision_id from vision_users UNION ALL select Max(vision_id) from vision_users_pend)";
		int i = getJdbcTemplate().queryForObject(sql, Integer.class);
		return i;
	}

	public String getVisionBusinessDayForExpAnalysis(String countryLeBook) {
		Object args[] = { countryLeBook };
		return getJdbcTemplate().queryForObject("select TO_CHAR(BUSINESS_DATE,'Mon-RRRR') BUSINESS_DATE  from Vision_Business_Day  WHERE COUNTRY ||'-'|| LE_BOOK=?", args,
				String.class);
	}

	public String getyearMonthForTop10Deals(String countryLeBook) {
		Object args[] = { countryLeBook };
		return getJdbcTemplate().queryForObject("select TO_CHAR(BUSINESS_DATE,'RRRRMM') BUSINESS_DATE  from Vision_Business_Day  WHERE COUNTRY ||'-'|| LE_BOOK=?", args,
				String.class);
	}

	public String getVisionBusinessDate(String countryLeBook) {
		try {
			return getJdbcTemplate().queryForObject(
					"select TO_CHAR(BUSINESS_DATE,'DD-Mon-RRRR') BUSINESS_DATE  from Vision_Business_Day  WHERE COUNTRY ||'-'|| LE_BOOK='" + countryLeBook +"'", String.class);
		} catch (Exception e) {
			return "";
		}
	}

	public String getVisionCurrentYearMonth() {
		try {
			return getJdbcTemplate().queryForObject("select to_char(to_date(CURRENT_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') CURRENT_YEAR_MONTH  from V_Curr_Year_Month",
					String.class);
		} catch (Exception e) {
			return "";
		}
	}

	public int getUploadCount() {
		String sql = "Select count(1) from Vision_Upload where Upload_Status = 1 AND FILE_NAME LIKE '%XLSX'";
		int i = getJdbcTemplate().queryForObject(sql, Integer.class);
		return i;
	}

	public int doPasswordResetInsertion(VisionUsersVb vObject) {
		Date oldDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String resetValidity = df.format(oldDate);
		if (ValidationUtil.isValid(vObject.getPwdResetTime())) {
			Date newDate = DateUtils.addHours(oldDate, Integer.parseInt(vObject.getPwdResetTime()));
			resetValidity = df.format(newDate);
		}
		String query = "Insert Into FORGOT_PASSWORD ( VISION_ID, RESET_DATE, RESET_VALIDITY, RS_STATUS_NT, RS_STATUS)"
				+ "Values (?, SysDate, To_Date(?, 'DD-MM-YYYY HH24:MI:SS'), ?, ?)";
		Object[] args = { vObject.getVisionId(), resetValidity, vObject.getUserStatusNt(), vObject.getUserStatus() };
		return getJdbcTemplate().update(query, args);
	}

	public List<LevelOfDisplayVb> getQueryUserGroupProfile() throws DataAccessException {
		String sql = "SELECT USER_GROUP, USER_PROFILE FROM PROFILE_PRIVILEGES  GROUP BY USER_GROUP, USER_PROFILE";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				LevelOfDisplayVb lodVb = new LevelOfDisplayVb();
				lodVb.setUserGroup(rs.getString("USER_GROUP"));
				lodVb.setUserProfile(rs.getString("USER_PROFILE"));
				return lodVb;
			}
		};
		List<LevelOfDisplayVb> lodVbList = getJdbcTemplate().query(sql, mapper);
		return lodVbList;
	}

	public List<ProfileData> getQueryUserGroup() throws DataAccessException {
		String sql = "SELECT DISTINCT USER_GROUP FROM PROFILE_PRIVILEGES ORDER BY USER_GROUP";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfileData profileData = new ProfileData();
				profileData.setUserGroup(rs.getString("USER_GROUP"));
				return profileData;
			}
		};
		List<ProfileData> profileData = getJdbcTemplate().query(sql, mapper);
		return profileData;
	}

	public List<ProfileData> getQueryUserGroupBasedProfile(String userGroup) throws DataAccessException {
		String sql = "SELECT DISTINCT USER_PROFILE,USER_GROUP FROM PROFILE_PRIVILEGES where USER_GROUP ='" + userGroup + "' ORDER BY USER_GROUP";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfileData profileData = new ProfileData();
				profileData.setUserGroup(rs.getString("USER_GROUP"));
				profileData.setUserProfile(rs.getString("USER_PROFILE"));
				return profileData;
			}
		};
		List<ProfileData> profileData = getJdbcTemplate().query(sql, mapper);
		return profileData;
	}

	public String getSystemDate() {
		String sql = "SELECT To_Char(SysDate, 'DD-MM-YYYY HH24:MI:SS') AS SYSDATE1 FROM DUAL";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (rs.getString("SYSDATE1"));
			}
		};
		return (String) getJdbcTemplate().queryForObject(sql, null, mapper);
	}
	
	public String getSystemDateAlone() {
		String sql = "SELECT To_Char(SysDate, 'DD-MM-YYYY') AS SYSDATE1 FROM DUAL";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (rs.getString("SYSDATE1"));
			}
		};
		return (String) getJdbcTemplate().queryForObject(sql, null, mapper);
	}

	public String getSystemDate12Hr() {
		String sql = "SELECT To_Char(SysDate, 'DD-MM-YYYY HH:MI:SS') AS SYSDATE1 FROM DUAL";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (rs.getString("SYSDATE1"));
			}
		};
		return (String) getJdbcTemplate().queryForObject(sql, null, mapper);
	}

	public String getScriptValue(String pVariableName) throws DataAccessException, Exception {
		Object params[] = { pVariableName };
		String sql = new String("select VARIABLE_SCRIPT from VISION_DYNAMIC_HASH_VAR WHERE VARIABLE_TYPE = 2  AND UPPER(VARIABLE_NAME)=UPPER(?)");
		return getJdbcTemplate().queryForObject(sql, params, String.class);
	}

	public List<AlphaSubTabVb> getAvailableNodesLst() throws DataAccessException {
		String sql = "SELECT NODE_NAME,NODE_DESCRIPTION FROM VISION_NODE_CREDENTIALS WHERE NODE_STATUS = 0 ORDER BY NODE_DESCRIPTION";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb vObj = new AlphaSubTabVb();
				vObj.setAlphaSubTab(rs.getString("NODE_NAME"));
				vObj.setDescription(rs.getString("NODE_DESCRIPTION"));
				return vObj;
			}
		};
		List<AlphaSubTabVb> nodeAvailablelst = getJdbcTemplate().query(sql, mapper);
		return nodeAvailablelst;
	}

	public String getCurrentDateInfo(String option, String ctryLeBook) {
		String val = "";
		String sql = "";
		try {
			switch (option) {
			case "CYM":
				if ("ORACLE".equalsIgnoreCase(databaseType)) {
					sql = "SELECT TO_CHAR(To_Date(BUSINESS_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') FROM VISION_BUSINESS_DAY  WHERE COUNTRY||'-'||LE_BOOK = ? ";
				} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
					sql = "SELECT BUSINESS_YEAR_MONTH   FROM VISION_BUSINESS_DAY  WHERE COUNTRY+'-'+LE_BOOK = ? ";
				}
				break;
			case "CY":
				if ("ORACLE".equalsIgnoreCase(databaseType)) {
					sql = "SELECT TO_CHAR(To_Date(BUSINESS_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') FROM VISION_BUSINESS_DAY  WHERE COUNTRY||'-'||LE_BOOK = ? ";
				} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
					sql = "SELECT BUSINESS_YEAR_MONTH   FROM VISION_BUSINESS_DAY  WHERE COUNTRY+'-'+LE_BOOK = ? ";
				}
				break;
			default:
				if ("ORACLE".equalsIgnoreCase(databaseType)) {
					sql = "SELECT TO_CHAR(To_Date(BUSINESS_DATE),'DD-Mon-RRRR') FROM VISION_BUSINESS_DAY  WHERE COUNTRY||'-'||LE_BOOK = ? ";
				} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
					sql = "SELECT FORMAT(BUSINESS_DATE,'dd-MMM-yyyy')  BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE COUNTRY+'-'+LE_BOOK = ?";
				}
				break;
			}
			String args[] = { ctryLeBook };

			val = getJdbcTemplate().queryForObject(sql, args, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public List getDateFormatforCaption() {
		try {
			String query = "";
			VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				query = " SELECT COUNTRY+'-'+LE_BOOK as COUNTRY,  FORMAT(BUSINESS_DATE,'dd-MMM-yyyy') VBD, "
						+ " FORMAT(CONVERT(DATE,CONCAT(BUSINESS_YEAR_MONTH,'01')),'MMM-yyyy') VBM ,  FORMAT(BUSINESS_WEEK_DATE,'dd-MMM-yyyy') VBW, "
						+ " FORMAT(CONVERT(DATE,CONCAT(BUSINESS_QTR_YEAR_MONTH,'01')),'MMM-yyyy') VBQ  BUSINESS_QTR_YEAR_MONTH VBQ, "
						+ " FORMAT(BUSINESS_DATE,'yyyy') CY,  (select value from vision_variables where variable = 'CURRENT_MONTH') CM "
						+ " FROM VISION_BUSINESS_DAY ";
			} else if ("ORACLE".equalsIgnoreCase(databaseType)) {
				query = " SELECT COUNTRY||'-'||LE_BOOK COUNTRY,   TO_CHAR(BUSINESS_DATE,'DD-Mon-RRRR') VBD, "
						+ "  TO_CHAR(TO_DATE(BUSINESS_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') VBM,   TO_CHAR(BUSINESS_WEEK_DATE,'DD-Mon-RRRR') VBW, "
						+ "  TO_CHAR(TO_DATE(BUSINESS_QTR_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') VBQ,   TO_CHAR(BUSINESS_DATE,'RRRR') CY, "
						+ "  (select value from vision_variables where variable = 'CURRENT_MONTH') CM, "
						+ "  (SELECT TO_CHAR(CURRENT_TIMESTAMP, 'HH24:MI:SS') FROM dual) SYSTIME,   (SELECT SYSDATE FROM DUAL) SYSTEMDATE, "
						+ "  TO_CHAR(PREV_BUSINESS_DATE,'DD-Mon-RRRR') PVBD, " + // #VBD-1#
						"   TO_CHAR(ADD_MONTHS(BUSINESS_DATE,-1),'DD-Mon-RRRR') PMVBD ,    TO_CHAR(ADD_MONTHS(BUSINESS_DATE,-12),'DD-Mon-RRRR') PYVBD, "
						+ "   TO_CHAR(ADD_MONTHS(BUSINESS_DATE,-1)-1,'DD-Mon-RRRR') PMPVBD " + // #PMVBD-1#
						"  FROM VISION_BUSINESS_DAY   WHERE COUNTRY = '" + visionUsersVb.getCountry() + "' AND LE_BOOK = '" + visionUsersVb.getLeBook() + "'";
			}
			ExceptionCode exceptionCode = commonApiDao.getCommonResultDataQuery(query);
			List resultlst = (List) exceptionCode.getResponse();
			return resultlst;
		} catch (Exception e) {
			return null;
		}
	}

	public String getDbFunction(String reqFunction) {
		String functionName = "";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			switch (reqFunction) {
			case "DATEFUNC":
				functionName = "FORMAT";
				break;
			case "SYSDATE":
				functionName = "GetDate()";
				break;
			case "NVL":
				functionName = "ISNULL";
				break;
			case "TIME":
				functionName = "HH:mm:ss";
				break;
			case "DATEFORMAT":
				functionName = "dd-MMM-yyyy";
				break;
			case "CONVERT":
				functionName = "CONVERT";
				break;
			case "TYPE":
				functionName = "varchar,";
				break;
			case "TIMEFORMAT":
				functionName = "108";
				break;
			}
		} else if ("ORACLE".equalsIgnoreCase(databaseType)) {
			switch (reqFunction) {
			case "DATEFUNC":
				functionName = "TO_CHAR";
				break;
			case "SYSDATE":
				functionName = "SYSDATE";
				break;
			case "NVL":
				functionName = "NVL";
				break;
			case "TIME":
				functionName = "HH24:MI:SS";
				break;
			case "DATEFORMAT":
				functionName = "DD-Mon-RRRR";
				break;
			case "CONVERT":
				functionName = "TO_CHAR";
				break;
			case "TYPE":
				functionName = "";
				break;
			case "TIMEFORMAT":
				functionName = "'HH:MM:SS'";
				break;
			}
		}

		return functionName;
	}

	public ExceptionCode getReqdConnection(Connection conExt, String connectionName) {
		ExceptionCode exceptionCodeCon = new ExceptionCode();
		try {
			if (!ValidationUtil.isValid(connectionName) || "DEFAULT".equalsIgnoreCase(connectionName)) {
				conExt = commonApiDao.getConnection();
				exceptionCodeCon.setResponse(conExt);
			} else {
				String dbScript = getScriptValue(connectionName);
				if (!ValidationUtil.isValid(dbScript)) {
					exceptionCodeCon.setErrorCode(Constants.ERRONEOUS_OPERATION);
					exceptionCodeCon.setErrorMsg("DB Connection Name is Invalid");
					return exceptionCodeCon;
				}
				exceptionCodeCon = CommonUtils.getConnection(dbScript, false);
			}
			exceptionCodeCon.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		} catch (Exception e) {
			exceptionCodeCon.setErrorMsg(e.getMessage());
			exceptionCodeCon.setErrorCode(Constants.ERRONEOUS_OPERATION);
		}
		return exceptionCodeCon;
	}

	public List<ProfileData> getTopLevelMenu() throws DataAccessException {
		String sql = " Select MENU_GROUP_SEQ,MENU_GROUP_NAME,MENU_GROUP_ICON from PRD_MENU_GROUP where MENU_GROUP_Status = 0 "
				+ "and Application_Access = ? ORDER BY MENU_GROUP_SEQ ";
		Object[] lParams = new Object[1];
		lParams[0] = productName;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfileData profileData = new ProfileData();
				profileData.setMenuItem(rs.getString("MENU_GROUP_NAME"));
				profileData.setMenuGroup(rs.getInt("MENU_GROUP_SEQ"));
				profileData.setMenuIcon(rs.getString("MENU_GROUP_ICON"));
				return profileData;
			}
		};
		List<ProfileData> profileData = getJdbcTemplate().query(sql, lParams, mapper);
		return profileData;
	}

	public ArrayList<MenuVb> getSubMenuItemsForSubMenuGroup(int menuGroup, int parentSequence, int visionId) throws DataAccessException {
		String sql = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			sql = " SELECT *                                                               FROM PRD_VISION_MENU t1, PRD_PROFILE_PRIVILEGES t2, VISION_USERS t3   "
					+ "    WHERE     MENU_GROUP = ?                                               "
					+ "          AND MENU_STATUS = 0                                              "
					+ "          AND UPPER (MENU_NAME) != 'SEPERATOR'                                       AND t1.Application_Access = '" + productName
					+ "'                   		 AND t1.Application_Access = t2.Application_Access "
					+ "          AND Menu_Sequence <> Parent_Sequence                                       AND Parent_Sequence = " + parentSequence
					+ "                                   AND T1.MENU_PROGRAM = t2.screen_Name                                          AND t3.vision_ID = '"
					+ visionId + "'                                          AND t2.User_Group||'-'||T2.USER_PROFILE = t3.user_group || '-' || t3.USER_PROFILE    "
					+ " ORDER BY PARENT_SEQUENCE, MENU_SEQUENCE                                   ";
		} else {
			sql = " SELECT *                                                               FROM PRD_VISION_MENU t1, PRD_PROFILE_PRIVILEGES t2, VISION_USERS t3   "
					+ "    WHERE     MENU_GROUP = ?                                               "
					+ "          AND MENU_STATUS = 0                                              "
					+ "          AND UPPER (MENU_NAME) != 'SEPERATOR'                                       AND t1.Application_Access = '" + productName
					+ "'                   		 AND t1.Application_Access = t2.Application_Access "
					+ "          AND Menu_Sequence <> Parent_Sequence                                       AND Parent_Sequence = " + parentSequence
					+ "                                   AND T1.MENU_PROGRAM = t2.screen_Name                                          AND t3.vision_ID = '"
					+ visionId + "'                                          AND t2.User_Group+'-'+T2.USER_PROFILE = t3.user_group || '-' || t3.USER_PROFILE    "
					+ " ORDER BY PARENT_SEQUENCE, MENU_SEQUENCE ";

		}

		Object[] lParams = new Object[1];
		lParams[0] = menuGroup;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MenuVb menuVb = new MenuVb();
				menuVb.setMenuProgram(rs.getString("MENU_PROGRAM"));
				menuVb.setMenuName(rs.getString("MENU_NAME"));
				menuVb.setMenuSequence(rs.getInt("MENU_SEQUENCE"));
				menuVb.setParentSequence(rs.getInt("PARENT_SEQUENCE"));
				menuVb.setSeparator(rs.getString("SEPARATOR"));
				menuVb.setMenuGroup(rs.getInt("MENU_GROUP"));
				menuVb.setMenuStatus(rs.getInt("MENU_STATUS"));
				menuVb.setProfileAdd(rs.getString("P_ADD"));
				menuVb.setProfileModify(rs.getString("P_MODIFY"));
				menuVb.setProfileDelete(rs.getString("P_DELETE"));
				menuVb.setProfileView(rs.getString("P_INQUIRY"));
				menuVb.setProfileVerification(rs.getString("P_VERIFICATION"));
				menuVb.setProfileUpload(rs.getString("P_EXCEL_UPLOAD"));
				menuVb.setProfileDownload(rs.getString("P_DOWNLOAD"));
				return menuVb;
			}
		};
		ArrayList<MenuVb> menuList = (ArrayList<MenuVb>) getJdbcTemplate().query(sql, lParams, mapper);
		return menuList;
	}

	public List<ProfileData> getTopLevelMenu(VisionUsersVb visionUsersVb) throws DataAccessException {
		String sql = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			sql = "Select MENU_GROUP_SEQ,MENU_GROUP_NAME,MENU_GROUP_ICON,MENU_PROGRAM, "
					+ " T2.P_ADD,T2.P_MODIFY,P_DELETE,P_INQUIRY,P_VERIFICATION,P_EXCEL_UPLOAD,P_DOWNLOAD  from PRD_MENU_GROUP T1,PRD_PROFILE_PRIVILEGES_NEW T2 "
					+ " where T1.MENU_GROUP_Status = 0  and T1.Application_Access = ?  AND T2.USER_GROUP||'-'||T2.USER_PROFILE = ? "
					+ " AND T1.Application_Access = T2.Application_Access  AND T1.MENU_GROUP_SEQ = T2.MENU_GROUP  ORDER BY Menu_Display_Order ";
		} else {
			sql = "Select MENU_GROUP_SEQ,MENU_GROUP_NAME,MENU_GROUP_ICON,MENU_PROGRAM, "
					+ " T2.P_ADD,T2.P_MODIFY,P_DELETE,P_INQUIRY,P_VERIFICATION,P_EXCEL_UPLOAD,P_DOWNLOAD  from PRD_MENU_GROUP T1,PRD_PROFILE_PRIVILEGES_NEW T2 "
					+ " where T1.MENU_GROUP_Status = 0  and T1.Application_Access = ?  AND T2.USER_GROUP+'-'+T2.USER_PROFILE = ? "
					+ " AND T1.Application_Access = T2.Application_Access  AND T1.MENU_GROUP_SEQ = T2.MENU_GROUP  ORDER BY Menu_Display_Order ";
		}
		String grpProfile =  "";
		if(ValidationUtil.isValid(visionUsersVb.getUserGroup()) && ValidationUtil.isValid(visionUsersVb.getUserProfile()))
			grpProfile = visionUsersVb.getUserGroup() + "-" + visionUsersVb.getUserProfile();
		else
			grpProfile = visionUsersVb.getUserGrpProfile();
		Object[] lParams = new Object[2];
		lParams[0] = productName;
		lParams[1] = grpProfile;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProfileData profileData = new ProfileData();
				profileData.setMenuItem(rs.getString("MENU_GROUP_NAME"));
				profileData.setMenuGroup(rs.getInt("MENU_GROUP_SEQ"));
				profileData.setMenuIcon(rs.getString("MENU_GROUP_ICON"));
				profileData.setMenuProgram(rs.getString("MENU_PROGRAM"));
				profileData.setProfileAdd(rs.getString("P_ADD"));
				profileData.setProfileModify(rs.getString("P_MODIFY"));
				profileData.setProfileDelete(rs.getString("P_DELETE"));
				profileData.setProfileView(rs.getString("P_INQUIRY"));
				profileData.setProfileVerification(rs.getString("P_VERIFICATION"));
				profileData.setProfileUpload(rs.getString("P_EXCEL_UPLOAD"));
				profileData.setProfileDownload(rs.getString("P_DOWNLOAD"));
				return profileData;
			}
		};
		List<ProfileData> profileData = getJdbcTemplate().query(sql, lParams, mapper);
		return profileData;
	}

	public ArrayList<MenuVb> getSubMenuItemsForMenuGroup(int menuGroup) throws DataAccessException {

		String sql = "SELECT * FROM PRD_VISION_MENU WHERE MENU_GROUP = ? AND MENU_STATUS = 0 "
				+ " AND APPLICATION_ACCESS = ? AND MENU_SEQUENCE = PARENT_SEQUENCE ORDER BY PARENT_SEQUENCE, MENU_SEQUENCE";
		Object[] lParams = new Object[2];
		lParams[0] = menuGroup;
		lParams[1] = productName;
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MenuVb menuVb = new MenuVb();
				menuVb.setMenuProgram(rs.getString("MENU_PROGRAM"));
				menuVb.setMenuName(rs.getString("MENU_NAME"));
				menuVb.setMenuSequence(rs.getInt("MENU_SEQUENCE"));
				menuVb.setParentSequence(rs.getInt("PARENT_SEQUENCE"));
				menuVb.setSeparator(rs.getString("SEPARATOR"));
				menuVb.setMenuGroup(rs.getInt("MENU_GROUP"));
				menuVb.setMenuStatus(rs.getInt("MENU_STATUS"));
				return menuVb;
			}
		};
		ArrayList<MenuVb> menuList = (ArrayList<MenuVb>) getJdbcTemplate().query(sql, lParams, mapper);
		return menuList;
	}

	public ArrayList<MenuVb> getSubMenuItemsForSubMenuGroup(int menuGroup, int parentSequence, VisionUsersVb visionUsersVb) throws DataAccessException {
		String sql = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			sql = " SELECT * FROM PRD_VISION_MENU t1,PRD_PROFILE_PRIVILEGES_NEW T2  WHERE  T1.MENU_GROUP = ?  AND PARENT_SEQUENCE = ? "
					+ " AND MENU_STATUS = 0   AND SEPARATOR = 'N'  AND T1.APPLICATION_ACCESS = ?  AND T1.MENU_GROUP = T2.MENU_GROUP "
					+ " AND T1.APPLICATION_ACCESS = T2.APPLICATION_ACCESS  AND T2.USER_GROUP||'-'||T2.USER_PROFILE = ?  ORDER BY MENU_SEQUENCE ";
		} else {
			sql = " SELECT * FROM PRD_VISION_MENU t1,PRD_PROFILE_PRIVILEGES_NEW T2  WHERE  T1.MENU_GROUP = ?  AND PARENT_SEQUENCE = ? "
					+ " AND MENU_STATUS = 0   AND SEPARATOR = 'N'  AND T1.APPLICATION_ACCESS = ?  AND T1.MENU_GROUP = T2.MENU_GROUP "
					+ " AND T1.APPLICATION_ACCESS = T2.APPLICATION_ACCESS  AND T2.USER_GROUP+'-'+T2.USER_PROFILE = ?  ORDER BY MENU_SEQUENCE ";
		}

		String grpProfile = visionUsersVb.getUserGroup() + "-" + visionUsersVb.getUserProfile();
		Object[] lParams = new Object[4];
		lParams[0] = menuGroup;
		lParams[1] = parentSequence;
		lParams[2] = productName;
		lParams[3] = grpProfile;

		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MenuVb menuVb = new MenuVb();
				menuVb.setMenuProgram(rs.getString("MENU_PROGRAM"));
				menuVb.setMenuName(rs.getString("MENU_NAME"));
				menuVb.setMenuSequence(rs.getInt("MENU_SEQUENCE"));
				menuVb.setParentSequence(rs.getInt("PARENT_SEQUENCE"));
				menuVb.setSeparator(rs.getString("SEPARATOR"));
				menuVb.setMenuGroup(rs.getInt("MENU_GROUP"));
				menuVb.setMenuStatus(rs.getInt("MENU_STATUS"));
				menuVb.setProfileAdd(rs.getString("P_ADD"));
				menuVb.setProfileModify(rs.getString("P_MODIFY"));
				menuVb.setProfileDelete(rs.getString("P_DELETE"));
				menuVb.setProfileView(rs.getString("P_INQUIRY"));
				menuVb.setProfileVerification(rs.getString("P_VERIFICATION"));
				menuVb.setProfileUpload(rs.getString("P_EXCEL_UPLOAD"));
				menuVb.setProfileDownload(rs.getString("P_DOWNLOAD"));
				return menuVb;
			}
		};
		ArrayList<MenuVb> menuList = (ArrayList<MenuVb>) getJdbcTemplate().query(sql, lParams, mapper);
		return menuList;
	}
	
	public static String getMacAddress(String ip) throws IOException {
        String address = null;
        String str = "";
        String macAddress = "";
        try {
        	
        	 String cmd = "arp -a " + ip;
        	    Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream());
        	    Pattern pattern = Pattern.compile("(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}\\.){2}[0-9A-Fa-f]{4})");
        	    try {
        	        while (s.hasNext()) {
        	            str = s.next();
        	            Matcher matcher = pattern.matcher(str);
        	            if (matcher.matches()){
        	                break;
        	            }
        	            else{
        	                str = null;
        	            }
        	        }
        	    }
        	    finally {
        	        s.close();
        	    }
        	    if(!ValidationUtil.isValid(str)){
        	    	return ip;
        	    }
        	    return (str != null) ? str.toUpperCase(): null;
        	
        } catch (SocketException ex) {
            ex.printStackTrace();
            return ip;
        }
    }
	public String getConnectorScripts(String connectorId) throws Exception {
		String sql = "SELECT CONNECTOR_SCRIPTS FROM DQ_CONNECTOR_V2 WHERE UPPER(CONNECTOR_ID) = ? ";
		Object[] args = { connectorId.toUpperCase() };
		try {
			return getJdbcTemplate().queryForObject(sql, args, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public String getDbLink(String connectorId) throws Exception {
		String sql = "SELECT DB_LINK_NAME FROM DQ_CONNECTOR_V2 WHERE UPPER(CONNECTOR_ID) = ? ";
		Object[] args = { connectorId.toUpperCase() };
		try {
			return getJdbcTemplate().queryForObject(sql, args, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getTableType(String connectorId, String dqTableName) throws Exception {
		String sql = "SELECT distinct TABLE_TYPE FROM DQ_CONNECTED_TABLES_V2 WHERE UPPER(CONNECTOR_ID) = ? and UPPER(DQ_TABLE_NAME) = ? ";
		Object[] args = { connectorId.toUpperCase(), dqTableName.toUpperCase() };
		try {
			return getJdbcTemplate().queryForObject(sql, args, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String[][] PromptSplit(String promptValue,String query,String twoD_arr[][],int ctr1){
		String promptArrComma[] = promptValue.split(",");
		HashMap<String,String> prompt1Map = new HashMap<>();
		int ctr = 1;
		for(String str : promptArrComma) {
			prompt1Map.put(""+ctr, str);
			ctr++;
		}
		if(prompt1Map.size() > 0) {
			for (Map.Entry<String, String> set :prompt1Map.entrySet()) {
				String totalSplit[] = set.getValue().split("-");
				int ctr2 = 0;
				for(String str : totalSplit) {
					str = str.replaceAll("'", "");
					if(ValidationUtil.isValid(twoD_arr[ctr1][ctr2])) {
						twoD_arr[ctr1][ctr2] = twoD_arr[ctr1][ctr2]+","+"'"+str+"'";	
					}else {
						twoD_arr[ctr1][ctr2] = "'"+str+"'";
					}
					ctr2++;
				}
			}
		}
		return twoD_arr;
	}

	public String applyUserRestriction(String sqlQuery) {
		VisionUsersVb visionUserVb = CustomContextHolder.getContext();
		//VU_CLEB,VU_CLEB_AO,VU_CLEB_LV,VU_SBU,VU_PRODUCT,VU_OUC
		if(sqlQuery.contains("#VU_CLEB")) 
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_CLEB(", visionUserVb.getCountry(),visionUserVb.getUpdateRestriction());
		if(sqlQuery.contains("#VU_CLEB_AO")) 
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_CLEB_AO(", visionUserVb.getAccountOfficer(),visionUserVb.getUpdateRestriction());
		if(sqlQuery.contains("#VU_CLEB_LV")) 
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_CLEB_LV(", visionUserVb.getLegalVehicle(),visionUserVb.getUpdateRestriction());
		if(sqlQuery.contains("#VU_LV_CLEB"))
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_LV_CLEB(", visionUserVb.getLegalVehicleCleb(),visionUserVb.getUpdateRestriction());
		if(sqlQuery.contains("#VU_SBU")) 
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_SBU(", visionUserVb.getSbuCode(),visionUserVb.getUpdateRestriction());
		if(sqlQuery.contains("#VU_PRODUCT")) 
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_PRODUCT(", visionUserVb.getProductAttribute(),visionUserVb.getUpdateRestriction());
		if(sqlQuery.contains("#VU_OUC")) 
			sqlQuery = replacehashPrompt(sqlQuery, "#VU_OUC(", visionUserVb.getOucAttribute(),visionUserVb.getUpdateRestriction());	
		return sqlQuery;
	}
	public String replacehashPrompt(String query,String restrictStr,String restrictVal,String updateRestriction) {
		try {
			String replaceStr = "";
			String orgSbuStr = StringUtils.substringBetween(query, restrictStr, ")#");
			if(ValidationUtil.isValid(orgSbuStr)) {
				if ("Y".equalsIgnoreCase(updateRestriction) && ValidationUtil.isValid(restrictVal))
					replaceStr = " AND " + orgSbuStr + " IN (" + restrictVal + ")";
				
				restrictStr = restrictStr.replace("(", "\\(");
				orgSbuStr = orgSbuStr.replace("|", "\\|");
				query = query.replaceAll(restrictStr + orgSbuStr + "\\)#", replaceStr);	
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return query;
	}
	public List getAllBusinessDate(){
		try
		{	
			String query = "";
			if ("MSSQL".equalsIgnoreCase(databaseType)) {
				query = " SELECT COUNTRY+'-'+LE_BOOK as COUNTRY, "+  
					    " FORMAT(BUSINESS_DATE,'dd-MMM-yyyy') VBD, "+ 
						" FORMAT(CONVERT(DATE,CONCAT(BUSINESS_YEAR_MONTH,'01')),'MMM-yyyy') VBM , "+
						" FORMAT(BUSINESS_WEEK_DATE,'dd-MMM-yyyy') VBW, "+  
						" FORMAT(CONVERT(DATE,CONCAT(BUSINESS_QTR_YEAR_MONTH,'01')),'MMM-yyyy') VBQ "+
						" FROM VISION_BUSINESS_DAY ";
			}else if ("ORACLE".equalsIgnoreCase(databaseType)) {
				query = " SELECT COUNTRY||'-'||LE_BOOK COUNTRY, "+
					    " TO_CHAR(BUSINESS_DATE,'DD-Mon-RRRR') VBD, "+
						" TO_CHAR(TO_DATE(BUSINESS_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') VBM, "+
						" TO_CHAR(BUSINESS_WEEK_DATE,'DD-Mon-RRRR') VBW, "+
						" TO_CHAR(TO_DATE(BUSINESS_QTR_YEAR_MONTH,'RRRRMM'),'Mon-RRRR') VBQ "+
						" FROM VISION_BUSINESS_DAY";
			}
			ExceptionCode exceptionCode = commonApiDao.getCommonResultDataQuery(query);
			List resultlst = (List)exceptionCode.getResponse();
			return resultlst;
		}catch(Exception e) {
			return null;
		}
	}	
	public static String getReferenceNumforAudit(){
		try
		{
			String strDay   = "";
			String strMonth = "";
			String strYear  = "";
			String strHour  = "";
			String strMin   = "";
			String strSec   = "";
			String strMSec  = "";
			String strAMPM  = "";

			Calendar c = Calendar.getInstance();
			strMonth = c.get(Calendar.MONTH) + 1 + "";
			strDay   = c.get(Calendar.DATE) + "";
			strYear  = c.get(Calendar.YEAR) + "";
			strAMPM  = c.get(Calendar.AM_PM) + "";
			strMin  = c.get(Calendar.MINUTE) + "";
			strSec  = c.get(Calendar.SECOND) + "";
			strMSec  = c.get(Calendar.MILLISECOND) + "";

			if (strAMPM.equals("1"))
				strHour  = (c.get(Calendar.HOUR) + 12 )+ "";
			else
				strHour  = c.get(Calendar.HOUR) + "";

			if (strHour.length() == 1)
				strHour = "0" + strHour;

			if (strMin.length() == 1)
				strMin = "0" + strMin;

			if (strSec.length() == 1)
				strSec = "0" + strSec;

			if (strMSec.length() == 1)
				strMSec = "00" + strMSec;
			else if (strMSec.length() == 2)
				strMSec = "0" + strMSec;

			if (strMonth.length() == 1)
				strMonth = "0" + strMonth;

			if (strDay.length() == 1)
				strDay = "0" + strDay;

			return strYear + strMonth + strDay + strHour + strMin + strSec  + strMSec;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}	
	public String restrictedAoPermission(String module) {
		VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
		String allowedAoAccess = "";
		if(ValidationUtil.isValid(visionUsersVb.getRestrictedAo())) {
			String aoBackuplst[] = 	visionUsersVb.getRestrictedAo().split(",");
			for(int ctr = 0;ctr < aoBackuplst.length;ctr++) {
				String accessXml = getAoAccessRights(visionUsersVb.getAccountOfficer(),aoBackuplst[ctr]);
				String permission = CommonUtils.getValueForXmlTag(accessXml, module);
				if(ValidationUtil.isValid(permission) && "Y".equalsIgnoreCase(permission)) {
					if(ValidationUtil.isValid(allowedAoAccess)) {
						allowedAoAccess = allowedAoAccess+"','"+aoBackuplst[ctr];
					}else {
						allowedAoAccess = aoBackuplst[ctr];
					}
				}
			}
			allowedAoAccess = ",'"+allowedAoAccess+"'";
		}
		return allowedAoAccess;
	}	
	public String getAoAccessRights(String accountOfficer,String backupAo) {
		try {
			String query = " SELECT "+
					"  Access_Rights_Xml "+
					"  FROM MDM_AO_LEAVE_MGMT Where account_Officer = '"+backupAo+"' "+
				    "  and AO_back_up = '"+accountOfficer+"' and To_date(sysdate,'DD-MM-RRRR') >= To_date(leave_from,'DD-MM-RRRR') "+
				    "  and To_date(sysdate,'DD-MM-RRRR') <= To_date(leave_to,'DD-MM-RRRR') ";
			return getJdbcTemplate().queryForObject(query,null,String.class);
		}catch(Exception e) {
			return null;
		}
	}
	public String getVisionSbu(String parentVal) {
		String sbu="";
		try {
			sbu = getJdbcTemplate().queryForObject(" SELECT DISTINCT "+
					" ''''|| LISTAGG (VISION_SBU, ''',''') WITHIN GROUP (ORDER BY VISION_SBU)|| '''' "+
					" Sbu FROM (SELECT DISTINCT VISION_SBU FROM VISION_SBU_MDM "+
					" WHERE    VISION_SBU IN ("+parentVal+") OR PARENT_SBU IN ("+parentVal+") OR DIVISON IN ("+parentVal+") "+
					" OR BANK_GROUP IN ("+parentVal+") ) ",null,String.class);
			
			System.out.println("Sbu Query:"+sbu);
		}catch(Exception e) {
			System.out.println("Error while getting SBU:"+e.getMessage());
			e.printStackTrace();
			sbu = "";
		}
		return sbu;
	}	
	public String getUserHomeDashboard(String userGrpProfile) {
		String homeDashboard = "NA";
		String query = "";
		try {
			query = " Select "+getDbFunction("NVL")+"(Home_dashboard,'NA') from PRD_PROFILE_DASHBOARDS where "
					+ "User_group "+getDbFunction("PIPELINE")+"'-'"+getDbFunction("PIPELINE")+"User_Profile = ? and Application_access = ? ";

			Object[] lParams = new Object[2];
			lParams[0] = userGrpProfile;
			lParams[1] = productName;
			homeDashboard = getJdbcTemplate().queryForObject(query, lParams, String.class);
			if (!ValidationUtil.isValid(homeDashboard)) {
				homeDashboard = "NA";
			}
		} catch (Exception e) {
		}
		return homeDashboard;
	}
	
	public String getRestrictionsByUsersByMenuGroup(String screenName, String operation) throws DataAccessException {
		if (!ValidationUtil.isValid(screenName)) {
			return null;
		}
		VisionUsersVb usersVb = CustomContextHolder.getContext();
		String column = "P_ADD";
		if("MODIFY".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_MODIFY";
		if("DELETE".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_DELETE";		
		if("APPROVE".equalsIgnoreCase(operation.toUpperCase()) || "REJECT".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_VERIFICATION";	
		if("DOWNLOAD".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_DOWNLOAD";	
		if("UPLOAD".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_EXCEL_UPLOAD";
		String sql = "Select T2."+column+" USER_RESTRINCTION  from PRD_MENU_GROUP T1,PRD_PROFILE_PRIVILEGES_NEW T2 \r\n" + 
				"					where T1.MENU_GROUP_Status = 0  and T1.Application_Access = ? AND MENU_GROUP = ? AND T2.USER_GROUP = ? AND T2.USER_PROFILE = ? \r\n" + 
				"					AND T1.Application_Access = T2.Application_Access  AND T1.MENU_GROUP_SEQ = T2.MENU_GROUP ";
		Object[] lParams = new Object[4];
		lParams[0] = productName;
		lParams[1] = screenName;
		lParams[2] = usersVb.getUserGroup();
		lParams[3] = usersVb.getUserProfile();
		RowMapper mapper = new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				CommonVb commonVb = new CommonVb();
				commonVb.setMakerName(rs.getString("USER_RESTRINCTION"));
				return commonVb;
			}
		};
		List<CommonVb> commonVbs = getJdbcTemplate().query(sql, lParams, mapper);
		if (commonVbs != null && !commonVbs.isEmpty()) {
			return commonVbs.get(0).getMakerName();
		}
		return null;
	}
	public String getRestrictionsByUsersByScreenName(String screenName, String operation) throws DataAccessException {
		if (!ValidationUtil.isValid(screenName)) {
			return null;
		}
		VisionUsersVb usersVb = CustomContextHolder.getContext();
		String column = "P_ADD";
		if("MODIFY".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_MODIFY";
		if("DELETE".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_DELETE";		
		if("APPROVE".equalsIgnoreCase(operation.toUpperCase()) || "REJECT".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_VERIFICATION";	
		if("DOWNLOAD".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_DOWNLOAD";	
		if("UPLOAD".equalsIgnoreCase(operation.toUpperCase()))
			column = "P_EXCEL_UPLOAD";
		/*String sql = "SELECT "+column+" USER_RESTRINCTION FROM PRD_PROFILE_PRIVILEGES_NEW "
				+ "WHERE APPLICATION_ACCESS = ? "
				+ " AND MENU_GROUP = ? "
				+ " AND USER_GROUP = ? "
				+ " AND USER_PROFILE = ? ";*/
		String sql = "Select T2."+column+" USER_RESTRINCTION  from PRD_MENU_GROUP T1,PRD_PROFILE_PRIVILEGES_NEW T2 \r\n" + 
		"					where T1.MENU_GROUP_Status = 0  and T1.Application_Access = ? AND upper(MENU_PROGRAM) = upper(?) AND T2.USER_GROUP = ? AND T2.USER_PROFILE = ? \r\n" + 
		"					AND T1.Application_Access = T2.Application_Access  AND T1.MENU_GROUP_SEQ = T2.MENU_GROUP ";
		Object[] lParams = new Object[4];
		lParams[0] = productName;
		lParams[1] = screenName.toUpperCase();
		lParams[2] = usersVb.getUserGroup();
		lParams[3] = usersVb.getUserProfile();
		RowMapper mapper = new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				CommonVb commonVb = new CommonVb();
				commonVb.setMakerName(rs.getString("USER_RESTRINCTION"));
				return commonVb;
			}
		};
		List<CommonVb> commonVbs = getJdbcTemplate().query(sql, lParams, mapper);
		if (commonVbs != null && !commonVbs.isEmpty()) {
			return commonVbs.get(0).getMakerName();
		}
		return null;
	}
	
	public String findMassingColumns(String client, String connectorId, String tableName) throws DataAccessException {
		if (!ValidationUtil.isValid(client)) {
			return null;
		}
		if (!ValidationUtil.isValid(connectorId)) {
			return null;
		}
		if (!ValidationUtil.isValid(tableName)) {
			return null;
		}
		tableName = tableName.replaceAll(" ", "_");
		String sql = "SELECT LISTAGG(COLUMN_NAME,',') WITHIN GROUP (ORDER BY 1) COLUMN_NAME FROM ( " + 
				"select distinct COLUMN_NAME FROM DQ_TAB_COLS_V2 WHERE UPPER(CLIENT_ID) = ? AND UPPER(CONNECTOR_ID) = ?  " + 
				"and UPPER(DQ_TABLE_NAME) = ?  " + 
				"and MASK_FLAG = 'Y' " + 
				")";
		Object[] lParams = new Object[3];
		lParams[0] = client.toUpperCase();
		lParams[1] = connectorId.toUpperCase();
		lParams[2] = tableName.toUpperCase();
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				CommonVb commonVb = new CommonVb();
				commonVb.setMakerName(rs.getString("COLUMN_NAME"));
				return commonVb;
			}
		};
		List<CommonVb> commonVbs = getJdbcTemplate().query(sql, lParams, mapper);
		if (commonVbs != null && !commonVbs.isEmpty()) {
			return commonVbs.get(0).getMakerName();
		}
		return null;
	}
}