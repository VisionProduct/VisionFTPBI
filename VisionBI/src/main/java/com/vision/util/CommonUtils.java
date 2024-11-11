package com.vision.util;

import java.awt.Color;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.vb.DCManualQueryVb;
import com.vision.vb.ReportVb;

import io.jsonwebtoken.Claims;
import jcifs.util.Base64;

public class CommonUtils {

	private static String strErrMsgs[] = new String[50];
	private static String strSeverityFlags[] = new String[50];
	private static String SPACE = " ";
	public static String makerApprDesc = "(SELECT NVL(USER_NAME,'') USER_NAME FROM vision_users WHERE VISION_ID = TAppr.MAKER) MAKER_NAME";
	public static String verifierApprDesc = "(SELECT NVL(USER_NAME,'') USER_NAME FROM vision_users WHERE VISION_ID = TAppr.VERIFIER) VERIFIER_NAME";
	
	public static String makerPendDesc = "(SELECT NVL(USER_NAME,'') USER_NAME FROM vision_users WHERE VISION_ID = TPend.MAKER) MAKER_NAME";
	public static String verifierPendDesc = "(SELECT NVL(USER_NAME,'') USER_NAME FROM vision_users WHERE VISION_ID = TPend.VERIFIER) VERIFIER_NAME";
	
	public static int CELL_STYLE_HEADER_CAP_COL = 0;
	
	static {
		strErrMsgs[0] = new String("System error.  Contact System Admin");
		strErrMsgs[1] = new String("Successful !!");
		strErrMsgs[2] = new String("Invalid Status flag in database table");
		strErrMsgs[3] = new String("Duplicate key insertion attempted");
		strErrMsgs[4] = new String("Record pending for deletion. Can't update now");
		strErrMsgs[5] = new String("Attempt to modify a non-existent record");
		strErrMsgs[6] = new String("Attempt to delete a non-existent record");
		strErrMsgs[7] = new String("Attempt to delete record waiting for approval");
		strErrMsgs[8] = new String("No such pending record exists !!");
		strErrMsgs[9] = new String("Record is already pending for add approve.  Can't add again.");
		strErrMsgs[10] = new String("Record already present in approved but in Inactive/Delete  state");
		strErrMsgs[11] = new String("Cannot delete an inactive/delete record");
		strErrMsgs[12] = new String("No valid records to approve");
		strErrMsgs[13] = new String("No valid records to reject");
		strErrMsgs[14] = new String("Maker cannot approve pending records");
		strErrMsgs[15] = new String("Maker cannot reject pending records");
		strErrMsgs[16] = new String("Record might be Approved / Deleted");
		strErrMsgs[17] = new String("Cannot delete an unauthorised records");
		strErrMsgs[18] = new String("Cannot modify an record to delete state");
		strErrMsgs[19] = new String("FX Rate is not Maintained for the Month/Year");
		strErrMsgs[21] = new String("Cannot delete while balances are maintained");
		strErrMsgs[22] = new String("Cannot authorise without balance records");
		strErrMsgs[23] = new String("No Operation Performed");
		strErrMsgs[24] = new String("Maker cannot approve pending child records");
		strErrMsgs[25] = new String("Validation Finished - No Errors ");
//		strErrMsgs[26] = new String("Validation Finished - Errors Found ");
		strErrMsgs[26] = new String("Errors Found ");
		strErrMsgs[27] = new String("Trying to Modify an Approved Record which has been Modified by another User");
		strErrMsgs[28] = new String("Cannot Authorise. Balance Amounts Exceed Tolerance Amount");
		strErrMsgs[29] = new String("Cannot Authorise. Balance exceeds Tolerance(0)");
		strErrMsgs[30] = new String("Warning : Audit Trail Operation is not completed");
		strErrMsgs[31] = new String("Cannot Authorise. EOP Balance Amounts Exceed Tolerance Amount");
		strErrMsgs[32] = new String("Cannot Authorise. Average Balance Amounts Exceed Tolerance Amount");
		strErrMsgs[33] = new String("Cannot Authorise. P&L Balance Amounts Exceed Tolerance Amount");
		strErrMsgs[34] = new String("Cannot Authorise. Pool Balance Amounts Exceed Tolerance Amount");
		strErrMsgs[35] = new String("Allocations Data for Current Year cannot be deleted");
		strErrMsgs[37] = new String("No data found in Build Controls data table ");
		strErrMsgs[38] = new String("Cannot Add, Modify or Delete the record. Build Module is Running");
		strErrMsgs[39] = new String("Cannot Approve the record. Build Module is Running");
		strErrMsgs[40] = new String(".txt file is not found");
		strErrMsgs[41] = new String("Sorry - No Records Found!");
		strErrMsgs[42] = new String("No such Unauthorized record exists !!");
		strErrMsgs[43] = new String("Attempt to delete record waiting for authorize");
		strErrMsgs[44] = new String("Posted record can not be deleted");
		strErrMsgs[45] = new String("No data found in Approved table");
		strErrMsgs[46] = new String("Record does not exist or might be deleted");
		strErrMsgs[47] = new String("Unable to read/write file from/to the server.\n OR \nRemote service un-available.");
		strErrMsgs[48] = new String("Invalid Error Code or Error Code not mapped in the system.");
		strErrMsgs[49] = new String("Year is not maintain in Period controls table.");

		strSeverityFlags[0] = new String("F");
		strSeverityFlags[1] = new String("");
		strSeverityFlags[2] = new String("E");
		strSeverityFlags[3] = new String("E");
		strSeverityFlags[4] = new String("E");
		strSeverityFlags[5] = new String("E");
		strSeverityFlags[6] = new String("E");
		strSeverityFlags[7] = new String("E");
		strSeverityFlags[8] = new String("E");
		strSeverityFlags[9] = new String("E");
		strSeverityFlags[10] = new String("E");
		strSeverityFlags[11] = new String("E");
		strSeverityFlags[12] = new String("E");
		strSeverityFlags[13] = new String("E");
		strSeverityFlags[14] = new String("E");
		strSeverityFlags[15] = new String("E");
		strSeverityFlags[16] = new String("F");
		strSeverityFlags[17] = new String("F");
		strSeverityFlags[18] = new String("F");
		strSeverityFlags[19] = new String("E");
		strSeverityFlags[20] = new String("E");
		strSeverityFlags[21] = new String("E");
		strSeverityFlags[22] = new String("E");
		strSeverityFlags[23] = new String("E");
		strSeverityFlags[24] = new String("E");
		strSeverityFlags[25] = new String("");
		strSeverityFlags[26] = new String("");
		strSeverityFlags[27] = new String("F");
		strSeverityFlags[28] = new String("F");
		strSeverityFlags[29] = new String("F");
		strSeverityFlags[30] = new String("W");
		strSeverityFlags[31] = new String("F");
		strSeverityFlags[32] = new String("F");
		strSeverityFlags[33] = new String("F");
		strSeverityFlags[34] = new String("F");
		strSeverityFlags[35] = new String("F");
		strSeverityFlags[36] = new String("W");
		strSeverityFlags[38] = new String("E");
		strSeverityFlags[39] = new String("E");
		strSeverityFlags[40] = new String("E");
		strSeverityFlags[41] = new String("W");
		strSeverityFlags[42] = new String("E");
		strSeverityFlags[43] = new String("E");
		strSeverityFlags[44] = new String("E");
		strSeverityFlags[45] = new String("F");
		strSeverityFlags[46] = new String("F");
		strSeverityFlags[48] = new String("F");
		strSeverityFlags[49] = new String("F");
	}

	// Function used by the getQueryResults function of the Query Object class for
	// each service
	// public static void addToQuery(String strFld, StringBuffer strBuf, String
	// strType, String strStrBufType)
	public static void addToQuery(String strFld, StringBuffer strBuf) {
		if (strBuf.toString().endsWith(") ") == true || strBuf.toString().endsWith("?") == true || strBuf.toString().endsWith("IS NULL") == true || strBuf.toString().endsWith(")  ") == true)
			strBuf.append(" AND " + strFld);
		else
			strBuf.append(" Where " + strFld);
	}

	public static String getFixedLength(String strValue, String strFillValue, int intLength) {
		try {
			if (strValue == null)
				strValue = "";

			while (strValue.length() < intLength) {
				if (strFillValue.equals("0"))
					strValue = strFillValue + strValue;
				else
					strValue += strFillValue;
			}
			return strValue;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Converts the Screen Format Date to DB Format
	 * 
	 * @param String (Screen Format Date)
	 * @return
	 */
	public static String getTableFormatDate(String strDate) {
		if (strDate == null)
			strDate = "";
		else
			strDate = strDate.trim();

		if (strDate.length() > 8 && strDate.indexOf("/") != -1) {
			String strRetDt = strDate.substring(3, 5) + "/" + strDate.substring(0, 2) + "/" + strDate.substring(6);
			return strRetDt;
		} else
			return strDate;

	}

	/**
	 * 
	 * @return String (Unique Reference number for the Audit Entry)
	 */
	public static String getReferenceNo() {

		try {
			String strDay = "";
			String strMonth = "";
			String strYear = "";
			String strHour = "";
			String strMin = "";
			String strSec = "";
			String strMSec = "";
			String strAMPM = "";

			Calendar c = Calendar.getInstance();
			strMonth = c.get(Calendar.MONTH) + 1 + "";
			strDay = c.get(Calendar.DATE) + "";
			strYear = c.get(Calendar.YEAR) + "";
			strAMPM = c.get(Calendar.AM_PM) + "";
			strMin = c.get(Calendar.MINUTE) + "";
			strSec = c.get(Calendar.SECOND) + "";
			strMSec = c.get(Calendar.MILLISECOND) + "";

			if (strAMPM.equals("1"))
				strHour = (c.get(Calendar.HOUR) + 12) + "";
			else
				strHour = c.get(Calendar.HOUR) + "";

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

			return strYear + strMonth + strDay + strHour + strMin + strSec + strMSec;
		} catch (Exception e) {
			return "";
		}
	}

	// Function used by all business objects to generate the result object for an
	// operation
	public static ExceptionCode getResultObject(String strServiceName, int intErrorId, String strOperation, String strErrMsg) {
		String strTemp = new String("");
//		ResourceBundle rsb = getResourceManger();
		ExceptionCode rObj = new ExceptionCode();
		if (!ValidationUtil.isValid(strOperation)) {
			strTemp = strServiceName + " - " + (ValidationUtil.isValid(strErrMsg) ? strErrMsg : strErrMsgs[intErrorId]);
		} else {
			strTemp = strServiceName + " - " + getErrorMsg(strOperation + " ", null) + " - ";
			if (intErrorId == Constants.SUCCESSFUL_OPERATION) {
				strTemp = strTemp + getErrorMsg("Successful ", null);
			} else if (intErrorId == Constants.VALIDATION_ERRORS_FOUND) {
				strTemp = strTemp + getErrorMsg("Successful", null) + " - " + strErrMsgs[intErrorId];
			} else if (intErrorId == Constants.VALIDATION_NO_ERRORS) {
				strTemp = strTemp + getErrorMsg("Successful", null) + " - " + strErrMsgs[intErrorId];
			} else if (intErrorId == Constants.AUDIT_TRAIL_ERROR) {
				strTemp = strTemp + getErrorMsg("Successful", null) + " - " + strErrMsgs[intErrorId];
			} else if (intErrorId == Constants.NO_RECORDS_FOUND) {
				strTemp = getErrorMsg(strErrMsgs[intErrorId], null);
			} else if (intErrorId == Constants.WE_HAVE_WARNING_DESCRIPTION) {
				if (strErrMsg == null)
					strTemp = strTemp + getErrorMsg("Successful", null);
				else
					strTemp = strTemp + getErrorMsg("Successful", null) + " - " + strErrMsg;

			} else {
				strTemp = strTemp + getErrorMsg("Failed", null) + " - ";
				if (intErrorId != Constants.WE_HAVE_ERROR_DESCRIPTION) {
					if (strErrMsg != null && strErrMsg.length() > 0) {
						strTemp = strTemp + getErrorMsg(strErrMsgs[intErrorId], null) + " - " + strErrMsg;

					} else {
						strTemp = strTemp + getErrorMsg(strErrMsgs[intErrorId], null);
					}
				} else {
//					strTemp = strTemp + strErrMsg + " - " + strServiceId;
					if (strErrMsg != null && strErrMsg.startsWith(strTemp)) {
						strTemp = strErrMsg;
					} else {
						strTemp = strTemp + strErrMsg;
					}
				}
			}

		}
		// Set the error code and display message
		rObj.setErrorCode(intErrorId);
		rObj.setErrorMsg(strTemp);
		return rObj;
	}

	public static String Round(String returnValue, int roundToPlaces) {
		if (returnValue == null || returnValue.isEmpty()) {
			returnValue = "0";
		}
		StringBuffer format = new StringBuffer("##,##0.0");
		if (roundToPlaces > 1) {
			for (int i = 1; i < roundToPlaces; i++) {
				format.append("0");
			}
		}
		DecimalFormat decimalFormat = new DecimalFormat(format.toString());
		return decimalFormat.format(Double.valueOf(returnValue));
	}

	public static ResourceBundle getResourceManger() {
		ResourceBundle rsb;
		if (CustomContextHolder.getContext() != null) {
			String language = CustomContextHolder.getContext().getLocale();
			if (language == null || language == "")
				language = "en_US";
			String country = language.substring(0, language.indexOf('_'));
			String lang = language.substring(language.indexOf('_') + 1, language.length());
			rsb = ResourceBundle.getBundle("locale/message", new Locale(country, lang));
		} else {
			String language = "en_US";
			rsb = ResourceBundle.getBundle("message",
					new Locale(language.substring(0, language.indexOf('_')), language.substring(language.indexOf('_') + 1, language.length())));
		}
		return rsb;
	}

	public static String getErrorMsg(String msg, ResourceBundle rsb) {
		String errorMsg = "";
		if (msg.indexOf(" ") > 1 && rsb != null) {
			String fristStr = msg.substring(0, msg.indexOf(" ")).toLowerCase();
			fristStr = fristStr.replaceAll(",", "");
			String remaingStr = msg.substring(msg.indexOf(" "), msg.length());
			remaingStr = remaingStr.replace(" ", "");
			remaingStr = remaingStr.replace("-", "");
			remaingStr = remaingStr.replace("_", "");
			remaingStr = remaingStr.replace("/", "");
			remaingStr = remaingStr.replace("!", "");
			remaingStr = remaingStr.replace(":", "");
			remaingStr = remaingStr.replace("\n", "");
			remaingStr = remaingStr.replace(".", "");
			remaingStr = remaingStr.replace("(0)", "");
			remaingStr = remaingStr.replace(",", "");
			remaingStr = remaingStr.replace("'", "");
			String name = fristStr + remaingStr;
			errorMsg = rsb.getString(name);
		} else {
			errorMsg = msg;
		}
		return errorMsg;
	}

	public static String getDBErrorMsg(String msg, ResourceBundle rsb) {
		String errorMsg = "";
		if (msg.indexOf(" ") > 1) {
			String fristStr = msg.substring(0, msg.indexOf(" ")).toLowerCase();
			fristStr = fristStr.replaceAll(",", "");
			String remaingStr = msg.substring(msg.indexOf(" "), msg.length());
			remaingStr = remaingStr.replace(" ", "");
			remaingStr = remaingStr.replace("-", "");
			remaingStr = remaingStr.replace("_", "");
			remaingStr = remaingStr.replace("/", "");
			remaingStr = remaingStr.replace("!", "");
			remaingStr = remaingStr.replace(":", "");
			remaingStr = remaingStr.replace("\n", "");
			remaingStr = remaingStr.replace(".", "");
			remaingStr = remaingStr.replace("(0)", "");
			remaingStr = remaingStr.replace(",", "");
			String name = fristStr.toLowerCase() + remaingStr.toLowerCase();
			errorMsg = rsb.getString(name);
		} else {
			errorMsg = msg;
		}
		return errorMsg;
	}

	public static void addToQuerySpecialCase(String strFld, StringBuffer strBuf) {
		if (strBuf.toString().endsWith(") ") == true || strBuf.toString().endsWith("?") == true || strBuf.toString().endsWith("IS NULL") == true
				|| strBuf.toString().endsWith("LE_BOOK") == true || strBuf.toString().endsWith("PRODUCT") == true) {
			strBuf.append(" AND " + strFld);
		} else {
			strBuf.append(" Where " + strFld);
		}
	}

	public static String replaceNewLineChar(String str) {
		try {
			if (!str.isEmpty()) {
				return str.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll("$@!", "").replaceAll("null", "").replaceAll("NULL", "")
						.replaceAll(System.lineSeparator(), "");
			}
			return str;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return str;
		}
	}

	public static Connection getDbConnection(String jdbcUrl, String username, String password, String type, String version)
			throws ClassNotFoundException, SQLException, Exception {
		Connection connection = null;
		if ("ORACLE".equalsIgnoreCase(type))
			Class.forName("oracle.jdbc.driver.OracleDriver");
		else if ("MSSQL".equalsIgnoreCase(type) || "SQLSERVER".equalsIgnoreCase(type))
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		else if ("MYSQL".equalsIgnoreCase(type))
			Class.forName("com.mysql.jdbc.Driver");
		else if ("POSTGRESQL".equalsIgnoreCase(type))
			Class.forName("org.postgresql.Driver");
		else if ("SYBASE".equalsIgnoreCase(type))
			Class.forName("com.sybase.jdbc4.jdbc.SybDataSource");
		else if ("INFORMIX".equalsIgnoreCase(type))
			Class.forName("com.informix.jdbc.IfxDriver");

		connection = DriverManager.getConnection(jdbcUrl, username, password);
		return connection;
	}

	public static ExceptionCode getConnection(String dbScript, boolean isDBConnectionClosed) {
		ExceptionCode exceptionCode = new ExceptionCode();
		Connection con = null;
		Statement stmt = null;
		String dbIP = "";
		String jdbcUrl = "";
		String dbServiceName = getValue(dbScript, "SERVICE_NAME");
		String dbOracleSid = getValue(dbScript, "SID");
		String dbUserName = getValue(dbScript, "USER");
		if (!ValidationUtil.isValid(dbUserName))
			dbUserName = getValue(dbScript, "DB_USER");

		String dbPassWord = getValue(dbScript, "PWD");
		if (!ValidationUtil.isValid(dbPassWord))
			dbPassWord = getValue(dbScript, "DB_PWD");

		String dbPortNumber = getValue(dbScript, "DB_PORT");

		String dataBaseName = getValue(dbScript, "DB_NAME");
		String dataBaseType = getValue(dbScript, "DATABASE_TYPE");
		String dbInstance = getValue(dbScript, "DB_INSTANCE");
		String dbIp = getValue(dbScript, "DB_IP");

		String serverName = getValue(dbScript, "SERVER_NAME");
		String version = getValue(dbScript, "JAR_VERSION");
		String paramLevel = "";
		String hostName = dbServiceName;
		Boolean sidFlag = false;
		if (!ValidationUtil.isValid(hostName)) {
			hostName = dbOracleSid;
			sidFlag = true;
		}
		if (ValidationUtil.isValid(dbIp))
			dbIP = dbIp;
		try {
			if ("ORACLE".equalsIgnoreCase(dataBaseType)) {
				if(sidFlag)
					jdbcUrl = "jdbc:oracle:thin:@//" + dbIP + ":" + dbPortNumber + "/" + hostName;
				else
					jdbcUrl = "jdbc:oracle:thin:@" + dbIP + ":" + dbPortNumber + ":" + hostName;
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "ORACLE", version);
			} else if ("MSSQL".equalsIgnoreCase(dataBaseType) || "SQLSERVER".equalsIgnoreCase(dataBaseType)) {
				if (ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)) {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";instanceName=" + dbInstance + ";databaseName=" + hostName;
				} else if (ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(hostName)) {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";instanceName=" + dbInstance;
				} else if (!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)) {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";databaseName=" + hostName;
				} else {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";databaseName=" + hostName;
				}

				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MSSQL", version);
			} else if ("MYSQL".equalsIgnoreCase(dataBaseType)) {

				if (ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)) {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "//instanceName=" + dbInstance + "//databaseName=" + dataBaseName;
				} else if (ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(dataBaseName)) {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "//instanceName=" + dbInstance;
				} else if (!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)) {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "/" + dataBaseName;
				} else {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "/" + dataBaseName;
				}

				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MYSQL", version);
			} else if ("POSTGRESQL".equalsIgnoreCase(dataBaseType)) {
				jdbcUrl = "jdbc:postgresql://" + dbIP + ":" + dbPortNumber + "/" + dataBaseName;
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "POSTGRESQL", version);
			} else if ("SYBASE".equalsIgnoreCase(dataBaseType)) {
				/* jdbc:sybase:Tds:194.170.154.136:7834?ServiceName=PROD_RCB */
				// jdbcUrl="jdbc:jtds:sybase://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
				jdbcUrl = "jdbc:sybase:Tds:" + dbIP + ":" + dbPortNumber + "?ServiceName=" + hostName;
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "SYBASE", version);
			} else if ("INFORMIX".equalsIgnoreCase(dataBaseType)) {
				jdbcUrl = "jdbc:informix-sqli://" + dbIP + ":" + dbPortNumber + "/" + dataBaseName + ":informixserver=" + serverName;
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "INFORMIX", version);
			}
			if (con == null) {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Problem in gaining connection with datasource");
				return exceptionCode;
			}
			String dbSetParam1 = CommonUtils.getValue(dbScript, "DB_SET_PARAM1");
			String dbSetParam2 = CommonUtils.getValue(dbScript, "DB_SET_PARAM2");
			String dbSetParam3 = CommonUtils.getValue(dbScript, "DB_SET_PARAM3");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (ValidationUtil.isValid(dbSetParam1)) {
				paramLevel = "Error when executing Parameter1 - ";
				stmt.executeUpdate(dbSetParam1);
			}
			if (ValidationUtil.isValid(dbSetParam2)) {
				paramLevel = "Error when executing Parameter2 - ";
				stmt.executeUpdate(dbSetParam2);
			}

			if (ValidationUtil.isValid(dbSetParam3)) {
				paramLevel = "Error when executing Parameter3 - ";
				stmt.executeUpdate(dbSetParam3);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:" + (ValidationUtil.isValid(paramLevel) ? paramLevel : "") + e.getMessage());
			return exceptionCode;
		} catch (SQLException e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:" + (ValidationUtil.isValid(paramLevel) ? paramLevel : "") + e.getMessage());
			return exceptionCode;
		} catch (Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:" + (ValidationUtil.isValid(paramLevel) ? paramLevel : "") + e.getMessage());
			return exceptionCode;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if(isDBConnectionClosed) {
					if(con !=null){
						con.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		exceptionCode.setErrorMsg("Connector Test Successful");
		exceptionCode.setResponse(con);
		return exceptionCode;
	}

	public static String getValue(String source, String key) {
		try {
			Matcher regexMatcher = Pattern.compile("\\{" + key + ":#(.*?)\\$@!(.*?)\\#}", Pattern.DOTALL).matcher(source);
			if (regexMatcher.find()) {
				if (ValidationUtil.isValid(regexMatcher.group(2)) && "ENCRYPT".equalsIgnoreCase(regexMatcher.group(1))) {
					return new String(Base64.decode(regexMatcher.group(2)));
				} else {
					return regexMatcher.group(2);
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String escapeMetaCharacters(String inputString) {
		final String[] metaCharacters = { "\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&" };
		String outputString = "";
		for (int i = 0; i < metaCharacters.length; i++) {
			if (inputString.contains(metaCharacters[i])) {
				outputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
				inputString = outputString;
			}
		}
		return ValidationUtil.isValid(outputString) ? outputString : inputString;
	}

	public static String getValueForXmlTag(String source, String tagName) {
		try {
			Matcher regexMatcher = Pattern.compile("\\<" + tagName + "\\>(.*?)\\<\\/" + tagName + "\\>", Pattern.DOTALL).matcher(source);
			return regexMatcher.find() ? regexMatcher.group(1) : null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String randomColorToCountSpecified(int count) {
		StringBuffer returnColorStrBuffer = new StringBuffer("");
		for (int i = 0; i < count; i++) {
			returnColorStrBuffer.append(ValidationUtil.isValid(String.valueOf(returnColorStrBuffer)) ? "," : "");
			long k = i;
			Random rand = new Random(k);
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			Color randomColor = new Color(r, g, b);
			String hex = Integer.toHexString(randomColor.getRGB() & 0xffffff);
			if (hex.length() < 6) {
				if (hex.length() == 5)
					hex = "0" + hex;
				if (hex.length() == 4)
					hex = "00" + hex;
				if (hex.length() == 3)
					hex = "000" + hex;
			}
			hex = "#" + hex;
			returnColorStrBuffer.append(hex);
		}
		return String.valueOf(returnColorStrBuffer);
	}

	public static Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public static String rgb2Hex(Color color) {
		return Integer.toHexString(color.getRGB() & 0xffffff);
	}

	public static boolean checkValidHash(String val) {
		if ("TVC_SESSIONID_STG_1".equalsIgnoreCase(val) || "TVC_SESSIONID_STG_2".equalsIgnoreCase(val) || "TVC_SESSIONID_STG_3".equalsIgnoreCase(val)) {
			return false;
		} else {
			return true;
		}
	}

	public static String replaceHashTag(String str, String[] hashArr, String[] hashValArr) {
		if (hashArr != null && hashValArr != null && hashArr.length == hashValArr.length) {
			Pattern pattern = Pattern.compile("#(.*?)#", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				int hashIndex = 0;
				String matchedStr = matcher.group(1);
				matchedStr = matchedStr.toUpperCase().replaceAll("\\s", "\\_");
				String useStr = matcher.group(1);
				useStr = escapeMetaCharacters(useStr);
				for (String hashVar : hashArr) {
					if (matchedStr.equalsIgnoreCase(hashVar))
						str = str.replaceAll("#" + useStr + "#", hashValArr[hashIndex]);
					hashIndex++;
				}
			}
		}
		return str;
	}

	public static boolean isAggrigateFunction(String criteria) {
		if (("SUM".equalsIgnoreCase(criteria)) || ("AVG".equalsIgnoreCase(criteria)) || ("MIN".equalsIgnoreCase(criteria)) || ("MAX".equalsIgnoreCase(criteria))
				|| ("COUNT".equalsIgnoreCase(criteria))) {
			return true;
		}
		return false;
	}

	public static boolean isBetweenCondition(String criteria) {
		if (("between".equalsIgnoreCase(criteria))) {
			return true;
		}
		return false;
	}

	public static boolean isMultiValueCondition(String criteria) {
		if (("not in".equalsIgnoreCase(criteria)) || ("in".equalsIgnoreCase(criteria))) {
			return true;
		}
		return false;
	}

	public static boolean isNoValueCondition(String criteria) {
		if (("is null".equalsIgnoreCase(criteria)) || ("is not null".equalsIgnoreCase(criteria))) {
			return true;
		}
		return false;
	}

	public static String appendQuots(String value) {
		if (ValidationUtil.isValid(value)) {
			value = value.trim();
			if (value.charAt(0) != '\'') {
				value = "'" + value;
			}
			if (value.charAt(value.length() - 1) != '\'') {
				value = value + "'";
			}
		}
		return value;
	}

	public static String transformXmlDocToString(Document doc) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Object[]> parseScript(String script) {
		if (ValidationUtil.isValid(script)) {
			List<Object[]> returnLst = new ArrayList<Object[]>(3);
			List<String> hashVar = new ArrayList<String>();
			List<String> hashVarDisplayVal = new ArrayList<String>();
			List<String> hashVarActualVal = new ArrayList<String>();
			Matcher matchObj = Pattern.compile("\\{(.*?)\\:\\#(.*?)\\$\\@\\!(.*?)\\#\\}", Pattern.DOTALL).matcher(script);
			while (matchObj.find()) {
				hashVar.add(matchObj.group(1));
				hashVarDisplayVal.add(matchObj.group(2));
				hashVarActualVal.add(matchObj.group(3));
			}
			Object[] hashVarArr = hashVar.toArray();
			Object[] hashVarDisplayValArr = hashVarDisplayVal.toArray();
			Object[] hashVarActualValArr = hashVarActualVal.toArray();
			returnLst.add(hashVarArr);
			returnLst.add(hashVarDisplayValArr);
			returnLst.add(hashVarActualValArr);
			return returnLst;
		} else {
			return null;
		}
	}

	public static String toJavascriptArray(Object[] arr) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < arr.length; i++) {
			sb.append("\"").append(arr[i]).append("\"");
			if (i + 1 < arr.length) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static void callSetter(Object obj, String fieldName, Object value) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(fieldName, obj.getClass());
			pd.getWriteMethod().invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ExceptionCode XmlToJson(String data) {
		String value = "";
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			data = data.replaceAll("\n", "").replaceAll("\r", "");
			data = data.replaceAll(">\\s*<", "><");
			JSONObject asJson = XML.toJSONObject(data);
			value = asJson.toString();
		} catch (Exception e) {
			exceptionCode = CommonUtils.getResultObject("xmlToJson", Constants.WE_HAVE_ERROR_DESCRIPTION, "Xml To Json Convertion", e.getMessage());
			exceptionCode.setResponse(value);
			throw new RuntimeCustomException(exceptionCode);
		}
		exceptionCode = CommonUtils.getResultObject("xmlToJson", Constants.SUCCESSFUL_OPERATION, "Sucess", "");
		exceptionCode.setResponse(value);
		return exceptionCode;
	}

	public static ExceptionCode jsonToXml(String data) {
		String value = "";
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			JSONObject asJson = new JSONObject(data);
			value = XML.toString(asJson);
		} catch (Exception e) {
			exceptionCode = CommonUtils.getResultObject("jsonToXml", Constants.WE_HAVE_ERROR_DESCRIPTION, "JSON To XML Convertion", e.getMessage());
			exceptionCode.setResponse(value);
			throw new RuntimeCustomException(exceptionCode);
		}
		exceptionCode = CommonUtils.getResultObject("jsonToXml", Constants.SUCCESSFUL_OPERATION, "Sucess", "");
		exceptionCode.setResponse(value);
		return exceptionCode;
	}

	public static void addToOrderByQuery(String strFld, StringBuffer strBuf) {
		if (strBuf.toString().endsWith(" ") == true) {
			strBuf.append(strFld);
		} else {
			strBuf.append(", " + strFld);
		}
	}

	public static Connection getDBConnection(String dbScript) throws ClassNotFoundException, SQLException, Exception {
		Connection con = null;
		String dbIP = "";
		String jdbcUrl = "";
		String dbServiceName = getValue(dbScript, "SERVICE_NAME");
		String dbOracleSid = getValue(dbScript, "SID");

		String dbUserName = getValue(dbScript, "USER");
		if (!ValidationUtil.isValid(dbUserName))
			dbUserName = getValue(dbScript, "DB_USER");
		String dbPassWord = getValue(dbScript, "PWD");
		if (!ValidationUtil.isValid(dbPassWord))
			dbPassWord = getValue(dbScript, "DB_PWD");
		String dbPortNumber = getValue(dbScript, "DB_PORT");
		String dataBaseName = getValue(dbScript, "DB_NAME");
		String dataBaseType = getValue(dbScript, "DATABASE_TYPE");
		String dbInstance = getValue(dbScript, "DB_INSTANCE");
		String dbIp = getValue(dbScript, "DB_IP");
		String serverName = getValue(dbScript, "SERVER_NAME");
		String version = getValue(dbScript, "JAR_VERSION");

		String hostName = dbServiceName;
		if (!ValidationUtil.isValid(hostName)) {
			hostName = dbOracleSid;
		}
		if (ValidationUtil.isValid(dbIp))
			dbIP = dbIp;
		if ("ORACLE".equalsIgnoreCase(dataBaseType)) {
			jdbcUrl = "jdbc:oracle:thin:@" + dbIP + ":" + dbPortNumber + ":" + hostName;
			con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "ORACLE", version);
		} else if ("MSSQL".equalsIgnoreCase(dataBaseType) || "SQLSERVER".equalsIgnoreCase(dataBaseType)) {
			if (ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)) {
				jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";instanceName=" + dbInstance + ";databaseName=" + hostName;
			} else if (ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(hostName)) {
				jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";instanceName=" + dbInstance;
			} else if (!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)) {
				jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";databaseName=" + hostName;
			} else {
				jdbcUrl = "jdbc:sqlserver://" + dbIP + ":" + dbPortNumber + ";databaseName=" + hostName;
			}

			con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MSSQL", version);
		} else if ("MYSQL".equalsIgnoreCase(dataBaseType)) {

			if (ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)) {
				jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "//instanceName=" + dbInstance + "//databaseName=" + dataBaseName;
			} else if (ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(dataBaseName)) {
				jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "//instanceName=" + dbInstance;
			} else if (!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)) {
				jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "/" + dataBaseName;
			} else {
				jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "/" + dataBaseName;
			}

			con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MYSQL", version);
		} else if ("POSTGRESQL".equalsIgnoreCase(dataBaseType)) {
			jdbcUrl = "jdbc:postgresql://" + dbIP + ":" + dbPortNumber + "/" + dataBaseName;
			con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "POSTGRESQL", version);
		} else if ("SYBASE".equalsIgnoreCase(dataBaseType)) {
			/* jdbc:sybase:Tds:194.170.154.136:7834?ServiceName=PROD_RCB */
			// jdbcUrl="jdbc:jtds:sybase://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
			jdbcUrl = "jdbc:sybase:Tds:" + dbIP + ":" + dbPortNumber + "?ServiceName=" + hostName;
			con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "SYBASE", version);
		} else if ("INFORMIX".equalsIgnoreCase(dataBaseType)) {
			jdbcUrl = "jdbc:informix-sqli://" + dbIP + ":" + dbPortNumber + "/" + dataBaseName + ":informixserver=" + serverName;
			con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "INFORMIX", version);
		}
		return con;
	}

	public static void addToQuerySearch(String strFld, StringBuffer strBuf, String condition) {
		if(strBuf.toString().endsWith(")  ") == true  || strBuf.toString().endsWith("?") == true) {
			strBuf.append(" AND " + strFld + condition + SPACE);
		}
		else if (strBuf.toString().endsWith(") ") == true || strBuf.toString().endsWith("?") == true
				|| strBuf.toString().endsWith("IS NULL") == true || strBuf.toString().endsWith("AND ") == true
				|| strBuf.toString().endsWith("OR ") == true)
			strBuf.append(strFld + condition + SPACE);
		else
			strBuf.append(" Where " + strFld + condition + SPACE);
	}

	public static String criteriaBasedVal(String criteria, String value) {
		String output = "";
		try {
			switch (criteria) {
			case "LIKE":
				output = " LIKE UPPER('%" + value + "%') ";
				break;

			case "STARTSWITH":
				output = " LIKE UPPER('" + value + "%') ";
				break;

			case "ENDSWITH":
				output = " LIKE UPPER('%" + value + "') ";
				break;

			case "EQUALS":
				output = " = UPPER('" + value + "') ";
				break;

			default:

			}
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ByteArrayInputStream ReportToExcelerror(List<Map<String, Object>> list_excelerror) {
		try {
			List<String> headers = list_excelerror.stream().flatMap(map -> map.keySet().stream()).distinct().collect(Collectors.toList());
			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < headers.size(); i++) {
				sb.append(headers.get(i));
				sb.append(i == headers.size() - 1 ? "\n" : ",");
			}
			for (Map<String, Object> map : list_excelerror) {
				for (int i = 0; i < headers.size(); i++) {
					sb.append(map.get(headers.get(i)));
					sb.append(i == headers.size() - 1 ? "\n" : ",");
				}
			}
			byte[] data = sb.toString().getBytes();
			return new ByteArrayInputStream(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatNumber(String input) {
		try {
			if (ValidationUtil.isNumericDecimal(input)) {
				Double numParsed = Double.parseDouble(input);
				input = String.format("%,.2f", numParsed);
			}
			return input;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}

	}

	public static String criteriaBasedVal(String criteria, String valueStart, String valueEnd) {
		String output = "";
		try {
			switch (criteria) {

			case "=":
				output = " = UPPER('" + valueStart + "') ";
				break;

			case "!=":
				output = " != UPPER('" + valueStart + "') ";
				break;

			case ">":
				output = " > UPPER('" + valueStart + "') ";
				break;

			case ">=":
				output = " >= UPPER('" + valueStart + "') ";
				break;

			case "BETWEEN":
				output = " BETWEEN UPPER('" + valueStart + "') AND UPPER('" + valueEnd + "') ";
				break;

			case "<":
				output = " < UPPER('" + valueStart + "') ";
				break;

			case "<=":
				output = " <= UPPER('" + valueStart + "') ";
				break;

			case "LIKE":
				output = " LIKE UPPER('%" + valueStart + "%') ";
				break;

			case "NOT LIKE":
				output = " NOT LIKE UPPER('%" + valueStart + "%') ";
				break;

			case "IN":
				String[] values = valueStart.split(",");
				output = " IN (";
				for (int i = 0; i < values.length; i++) {
					output = output + "'" + values[i] + "'";
					if (i < values.length - 1) {
						output = output + ",";
					}
				}
				output = output + ") ";
				break;

			case "NOT IN":
				values = valueStart.split(",");
				output = " NOT IN (";
				for (int i = 0; i < values.length; i++) {
					output = output + "'" + values[i] + "'";
					if (i < values.length - 1) {
						output = output + ",";
					}
				}
				output = output + ") ";
				break;

			case "IS NULL":
				output = " IS NULL  ";
				break;

			case "IS NOT NULL":
				output = " IS NOT NULL ";
				break;

			default:

			}
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String numAlphaTabDescritpionQuery(String numAlpha, int numAlphaTab, String columnName, String mappingColmn) {
		String finalQuery = "";
		if (numAlpha.equalsIgnoreCase("AT")) {
			finalQuery = "(SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = " + numAlphaTab + " AND ALPHA_SUB_TAB =  " + columnName + ") "
					+ mappingColmn;
		} else if (numAlpha.equalsIgnoreCase("NT")) {
			finalQuery = "(SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB = " + numAlphaTab + " AND NUM_SUB_TAB =  " + columnName + ")  " + mappingColmn;
		}
		return finalQuery;
	}
	
	public static byte[] getRGB(final String rgb) {
		byte[] sunoidaClr = null;
		if (!ValidationUtil.isValid(rgb)) {
		//	sunoidaClr = new byte[] { (byte) 177, (byte) 24, (byte) 124 }; //pink
			sunoidaClr = new byte[] { (byte) 66, (byte) 133, (byte) 244 };
		} else {
			final int[] ret = new int[3];
			for (int i = 0; i < 3; i++) {
				ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
			}
			sunoidaClr = new byte[] { (byte) ret[0], (byte) ret[1], (byte) ret[2] };
		}
		return sunoidaClr;

	}
	public static ByteArrayInputStream ReportToExcel(List<ReportVb> reportVbs) {
		try {
			// COMPLETENESS
			String[] columnsNew = { "Rule Universe Count", "Passed Id Count", "Passed (%)", "Failed Id Count", "Failed (%)", "Dpmo", "Six Sigma Rating",

					// ACCURACY
					"Dimension Universe", "Passed Id Count", "Passed (%)", "Failed Id Count", "Failed (%)", "Dpmo", "Six Sigma Rating",

					// CONFORMITY
					"Dimension Universe", "Passed Id Count", "Passed (%)", "Failed Id Count", "Failed (%)", "Dpmo", "Six Sigma Rating",

					// UNIQUENESS
					"Rule Universe", "Passed Id Count", "Passed (%)", "Failed Id Count", "Failed (%)", "Dpmo", "Six Sigma Rating",

					// CONSISTENCY
					"Dimension Universe", "Passed Id Count", "Passed (%)", "Failed Id Count", "Failed (%)", "Dpmo", "Six Sigma Rating",

					// OVERALL
					"Passed Id Count", "Passed (%)", "Failed Id Count", "Failed (%)", "Six Sigma Rating" };

			String[] headerArray = { "Completeness", "Accuracy", "Conformity", "Uniqueness", "Consistency", "Overall (Excluding Uniqueness)" };

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Dimension Report");
			Map<Integer, XSSFCellStyle>  styls = ExcelExportUtil.createStyles(workbook, "");
			int rowNum = 0;

			byte[] sunoidaPinkClr = getRGB("");
			XSSFColor sunoidaPinkXClr = new XSSFColor(sunoidaPinkClr);
			Font fontHeader = ExcelExportUtil.createFont(workbook, IndexedColors.WHITE.index, Font.BOLDWEIGHT_BOLD, "Calibri", 10);
			Font fontData = ExcelExportUtil.createFont(workbook, IndexedColors.BLACK.index, Font.BOLDWEIGHT_NORMAL, "Calibri", 10);

			XSSFColor whiteClr = new XSSFColor();
			whiteClr.setIndexed(IndexedColors.WHITE.index);
			XSSFCellStyle csHeaderCaptionColTop = ExcelExportUtil.createStyle(workbook, sunoidaPinkXClr, CellStyle.ALIGN_CENTER_SELECTION, fontHeader, null,
					CellStyle.BORDER_THIN, whiteClr);

			XSSFCellStyle csHeaderCaptionColTopNew = ExcelExportUtil.createStyleHeader(workbook, sunoidaPinkXClr, CellStyle.ALIGN_CENTER_SELECTION, fontHeader, null,
					CellStyle.BORDER_THIN, whiteClr, CellStyle.BORDER_THIN, whiteClr);

			csHeaderCaptionColTopNew.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			csHeaderCaptionColTop.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			csHeaderCaptionColTop.setBorderTop(HSSFCellStyle.BORDER_THIN);
			csHeaderCaptionColTop.setBorderRight(HSSFCellStyle.BORDER_THIN);
			csHeaderCaptionColTop.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			csHeaderCaptionColTop.setTopBorderColor(IndexedColors.WHITE.index);
			csHeaderCaptionColTop.setBottomBorderColor(IndexedColors.WHITE.index);
			csHeaderCaptionColTop.setRightBorderColor(IndexedColors.WHITE.index);
			csHeaderCaptionColTop.setLeftBorderColor(IndexedColors.WHITE.index);

			Font headerFont = workbook.createFont();
			headerFont.setFontHeightInPoints((short) 10);
			headerFont.setFontName(HSSFFont.FONT_ARIAL);
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			Font childHeaderFont = workbook.createFont();
			childHeaderFont.setFontHeightInPoints((short) 10);
			childHeaderFont.setColor(IndexedColors.WHITE.getIndex());
			childHeaderFont.setFontName(HSSFFont.FONT_ARIAL);
			childHeaderFont.setBoldweight(HSSFFont.COLOR_NORMAL);
			childHeaderFont.setColor(HSSFColor.WHITE.index);
			childHeaderFont.setBoldweight((short) 10);

			Row row = sheet.createRow(rowNum);
			
			

			Cell childHeader1 = row.createCell(0);
			childHeader1.setCellValue("S.No");
			csHeaderCaptionColTopNew.setRightBorderColor(IndexedColors.WHITE.index);
			childHeader1.setCellStyle(csHeaderCaptionColTopNew);
		//	sheet.autoSizeColumn(0);
			
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

			Cell childHeader2 = row.createCell(1);
			childHeader2.setCellValue("Column Name");
			childHeader2.setCellStyle(csHeaderCaptionColTopNew);
		//	sheet.autoSizeColumn(1);
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));

			Cell childHeader3 = row.createCell(2);
			childHeader3.setCellValue("Total Records");
			childHeader3.setCellStyle(csHeaderCaptionColTopNew);
		//	sheet.autoSizeColumn(2);
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

			// COMPLETENESS
			Cell cell = row.createCell(3);

			cell.setCellValue(headerArray[0]);
			cell.setCellStyle(csHeaderCaptionColTop);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 9));

			// ACCURACY
			Cell cell1 = row.createCell(10);

			cell1.setCellValue(headerArray[1]);
			cell1.setCellStyle(csHeaderCaptionColTop);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 10, 16));

			// CONFORMITY
			Cell cell2 = row.createCell(17);

			cell2.setCellValue(headerArray[2]);
			cell2.setCellStyle(csHeaderCaptionColTop);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 17, 23));

			// UNIQUENESS
			Cell cell3 = row.createCell(24);

			cell3.setCellValue(headerArray[3]);
			cell3.setCellStyle(csHeaderCaptionColTop);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 24, 30));

			// CONSISTENCY
			Cell cell4 = row.createCell(31);
			cell4.setCellValue(headerArray[4]);
			cell4.setCellStyle(csHeaderCaptionColTop);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 31, 37));

			// OVERALL
			Cell cell5 = row.createCell(38);
			cell5.setCellValue(headerArray[5]);
			cell5.setCellStyle(csHeaderCaptionColTop);
			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 38, 42));

			rowNum++;

			row = sheet.createRow(rowNum);
			row.createCell(0);
			row.createCell(1);
			row.createCell(2);

			row.getCell(0).setCellStyle(csHeaderCaptionColTopNew);
			row.getCell(1).setCellStyle(csHeaderCaptionColTopNew);
			row.getCell(2).setCellStyle(csHeaderCaptionColTopNew);

			/*Map<Integer,Integer> columnWidths = new HashMap<Integer,Integer>(42);
			for(int i =0;i < columnsNew.length;i++) {
				columnWidths.put(i, 40);
			}*/
			
			for (int i = 0; i < columnsNew.length; i++) {
				Cell childHeader = row.createCell(i + 3);
				childHeader.setCellValue(columnsNew[i]);
				childHeader.setCellStyle(csHeaderCaptionColTop);
				//columnWidths.put(i,(int)getColumnWidth(cell, csHeaderCaptionColTop.getFont(), true, columnWidths.get(i)));
				//sheet.autoSizeColumn(i);
			}
			byte[] pinkXclr = { (byte) 230, (byte) 184, (byte) 183 };
			XSSFColor pinkClr = new XSSFColor(pinkXclr);
			XSSFCellStyle styl = ExcelExportUtil.createStyleNew(workbook, whiteClr, CellStyle.ALIGN_RIGHT, fontData, null, CellStyle.BORDER_THIN, pinkClr);
			XSSFCellStyle stylLEft = ExcelExportUtil.createStyleNew(workbook, whiteClr, CellStyle.ALIGN_LEFT, fontData, null, CellStyle.BORDER_THIN, pinkClr);

			if (reportVbs != null && reportVbs.size() > 0) {
				for (ReportVb reportVb : reportVbs) {
					rowNum++;
					row = sheet.createRow(rowNum);
					System.out.println("rowNum : "+rowNum);
					row.createCell(0);
					row.getCell(0).setCellStyle(stylLEft);
					row.getCell(0).setCellValue(reportVb.getPosSeq());

					row.createCell(1);
					row.getCell(1).setCellValue(reportVb.getSourceColumnName());
					row.getCell(1).setCellStyle(stylLEft);

					// row.createCell(2).setCellValue(reportVb.getTotalRecord()));
					Cell cellrow2 = row.createCell(2);
					cellrow2.setCellStyle(styl);
					cellrow2.setCellValue(reportVb.getTotalRecord());

					// COMPLETENESS
					row.createCell(3);
					row.getCell(3).setCellStyle(styl);
					row.getCell(3).setCellValue(reportVb.getcDimensionUniverse());

					row.createCell(4).setCellValue(reportVb.getcPassedIdCount());
					row.getCell(4).setCellStyle(styl);

					// row.createCell(5).setCellValue(reportVbs.getC_passed_percent()); //style
					row.createCell(6).setCellValue(reportVb.getcFailedIdCount());
					row.getCell(6).setCellStyle(styl);

					row.createCell(7).setCellValue(reportVb.getcFailedPercent());
					row.getCell(7).setCellStyle(styl);

					row.createCell(8).setCellValue(reportVb.getcDpmo());
					row.getCell(8).setCellStyle(styl);

					row.createCell(9).setCellValue(reportVb.getcSixSigmaRating());
					row.getCell(9).setCellStyle(styl);

					// ACCURACY
					row.createCell(10).setCellValue(reportVb.getaDimensionUniverse());
					row.getCell(10).setCellStyle(styl);

					row.createCell(11).setCellValue(reportVb.getaPassedIdCount());
					row.getCell(11).setCellStyle(styl);

					// row.createCell(12).setCellValue(reportVb.getA_passed_percent());
					row.createCell(13).setCellValue(reportVb.getaFailedIdCount());
					row.getCell(13).setCellStyle(styl);

					row.createCell(14).setCellValue(reportVb.getaFailedPercent());
					row.getCell(14).setCellStyle(styl);

					row.createCell(15).setCellValue(reportVb.getaDpmo());
					row.getCell(15).setCellStyle(styl);

					row.createCell(16).setCellValue(reportVb.getaSixSigmaRating());
					row.getCell(16).setCellStyle(styl);
					// CONFORMITY
					row.createCell(17).setCellValue(reportVb.getCnDimensionUniverse());
					row.getCell(17).setCellStyle(styl);

					row.createCell(18).setCellValue(reportVb.getCnPassedIdCount());
					row.getCell(18).setCellStyle(styl);

					// row.createCell(19).setCellValue(reportVb.getCn_passed_percent());
					row.createCell(20).setCellValue(reportVb.getCnFailedIdCount());
					row.getCell(20).setCellStyle(styl);

					row.createCell(21).setCellValue(reportVb.getCnFailedPercent());
					row.getCell(21).setCellStyle(styl);

					row.createCell(22).setCellValue(reportVb.getCnDpmo());
					row.getCell(22).setCellStyle(styl);

					row.createCell(23).setCellValue(reportVb.getCnSixSigmaRating());
					row.getCell(23).setCellStyle(styl);

					// UNIQUENESS
					row.createCell(24).setCellValue(reportVb.getuDimensionUniverse());
					row.getCell(24).setCellStyle(styl);

					row.createCell(25).setCellValue(reportVb.getuPassedIdCount());
					row.getCell(25).setCellStyle(styl);

					// row.createCell(26).setCellValue(reportVb.getU_passed_percent());
					row.createCell(27).setCellValue(reportVb.getuFailedIdCount());
					row.getCell(27).setCellStyle(styl);

					row.createCell(28).setCellValue(reportVb.getuFailedPercent());
					row.getCell(28).setCellStyle(styl);

					row.createCell(29).setCellValue(reportVb.getuDpmo());
					row.getCell(29).setCellStyle(styl);

					row.createCell(30).setCellValue(reportVb.getuSixSigmaRating());
					row.getCell(30).setCellStyle(styl);

					// CONSISTENCY

					row.createCell(31).setCellValue(reportVb.getCsDimensionUniverse());
					row.getCell(31).setCellStyle(styl);

					row.createCell(32).setCellValue(reportVb.getCsPassedIdCount());
					row.getCell(32).setCellStyle(styl);

					// row.createCell(33).setCellValue(reportVb.getCs_passed_percent());
					row.createCell(34).setCellValue(reportVb.getCsFailedIdCount());
					row.getCell(34).setCellStyle(styl);

					row.createCell(35).setCellValue(reportVb.getCsFailedPercent());
					row.getCell(35).setCellStyle(styl);

					row.createCell(36).setCellValue(reportVb.getCsDpmo());
					row.getCell(36).setCellStyle(styl);

					row.createCell(37).setCellValue(reportVb.getCsSixSigmaRating());
					row.getCell(37).setCellStyle(styl);

					// OVERALL
					
					/*int OPassedIdCount = 0;
					int OFailedIdCount = 0;
					int OPassedPercent = 0;
					int OFailedPercent = 0;
					int OSixSigmaRating = 0;*/
					int RowNumber = rowNum+1;
					System.out.println("RowNumber "+RowNumber);
					
					String OPassedIdCount = "D"+RowNumber+"-AO"+RowNumber;
					row.createCell(38);
					row.getCell(38).setCellStyle(styl);
					row.getCell(38).setCellType(HSSFCell.CELL_TYPE_FORMULA);
					row.getCell(38).setCellFormula(OPassedIdCount);
					
					row.createCell(40);
					row.getCell(40).setCellStyle(styl);
					String OFailedIdCount = "SUM(G"+RowNumber+",N"+RowNumber+",U"+RowNumber+")";
					row.getCell(40).setCellType(HSSFCell.CELL_TYPE_FORMULA);
					row.getCell(40).setCellFormula(OFailedIdCount);
					
					
					String OFailedPercent = "AO"+RowNumber+"/D"+RowNumber;
					row.createCell(41);
					row.getCell(41).setCellStyle(styl);
					row.getCell(41).setCellType(HSSFCell.CELL_TYPE_FORMULA);
					row.getCell(41).setCellFormula(OFailedPercent);

				//	String OSixSigmaRating = "AVERAGE(J"+RowNumber+",X"+RowNumber+")";
					String  OSixSigmaRating = "IF(VALUE(J"+RowNumber+")<>0,AVERAGE(VALUE(J"+RowNumber+"),VALUE(X"+RowNumber+")),0)";
					System.out.println("OSixSigmaRating "+OSixSigmaRating);
					row.createCell(42);
					row.getCell(42).setCellStyle(styl);
					row.getCell(42).setCellType(HSSFCell.CELL_TYPE_FORMULA);
					row.getCell(42).setCellFormula(OSixSigmaRating);

					Font fontGYR = workbook.createFont();
					fontGYR.setColor(IndexedColors.BLACK.getIndex());

					// Green percentage Style

					byte[] greenClr = { (byte) 146, (byte) 208, (byte) 80 };
					XSSFColor GreenClr = new XSSFColor(greenClr);
					XSSFCellStyle styleGreen = (XSSFCellStyle) workbook.createCellStyle();
					styleGreen.setFillForegroundColor(GreenClr);
					styleGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleGreen.setFont(fontGYR);
					styleGreen.setAlignment(CellStyle.ALIGN_CENTER);

					// YELLOW percentage Style

					byte[] yellowClr = { (byte) 255, (byte) 231, (byte) 153 };
					XSSFColor yelClr = new XSSFColor(yellowClr);
					XSSFCellStyle styleYellow = (XSSFCellStyle) workbook.createCellStyle();
					styleYellow.setFillForegroundColor(yelClr);
					styleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleYellow.setFont(fontGYR);
					styleYellow.setAlignment(CellStyle.ALIGN_CENTER);

					// Red percentage Style

					byte[] redClr = { (byte) 255, (byte) 0, (byte) 0 };
					XSSFColor RedClr = new XSSFColor(redClr);
					XSSFCellStyle styleRed = (XSSFCellStyle) workbook.createCellStyle();
					styleRed.setFillForegroundColor(RedClr);
					styleRed.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRed.setFont(fontGYR);
					styleRed.setAlignment(CellStyle.ALIGN_CENTER);

					double C_passed_percent = Double.parseDouble(reportVb.getcPassedPercent());

					if ((C_passed_percent >= 90) && C_passed_percent <= 100) {
						row.createCell(5).setCellValue(reportVb.getcPassedPercent() + "%");
						row.getCell(5).setCellStyle(styleGreen);
					} else if (C_passed_percent >= 80 && C_passed_percent <= 89) {
						row.createCell(5).setCellValue(reportVb.getcPassedPercent() + "%");
						row.getCell(5).setCellStyle(styleYellow);
					} else if (C_passed_percent <= 79) {
						row.createCell(5).setCellValue(reportVb.getcPassedPercent() + "%");
						row.getCell(5).setCellStyle(styleRed);
					}

					double a_passed_percent = Double.parseDouble(reportVb.getaPassedPercent());

					if ((a_passed_percent >= 90) && a_passed_percent <= 100) {
						row.createCell(12).setCellValue(reportVb.getaPassedPercent() + "%");
						row.getCell(12).setCellStyle(styleGreen);
					} else if (a_passed_percent >= 80 && a_passed_percent <= 89) {
						row.createCell(12).setCellValue(reportVb.getaPassedPercent() + "%");
						row.getCell(12).setCellStyle(styleYellow);
					} else if (a_passed_percent <= 79) {
						row.createCell(12).setCellValue(reportVb.getaPassedPercent() + "%");
						row.getCell(12).setCellStyle(styleRed);
					}

					double Cn_passed_percent = Double.parseDouble(reportVb.getCnPassedPercent());

					if ((Cn_passed_percent >= 90) && Cn_passed_percent <= 100) {
						row.createCell(19).setCellValue(reportVb.getCnPassedPercent() + "%");
						row.getCell(19).setCellStyle(styleGreen);
					} else if (Cn_passed_percent >= 80 && Cn_passed_percent <= 89) {
						row.createCell(19).setCellValue(reportVb.getCnPassedPercent() + "%");
						row.getCell(19).setCellStyle(styleYellow);
					} else if (Cn_passed_percent <= 79) {
						row.createCell(19).setCellValue(reportVb.getCnPassedPercent() + "%");
						row.getCell(19).setCellStyle(styleRed);
					}

					double U_passed_percent = Double.parseDouble(reportVb.getuPassedPercent());

					if ((U_passed_percent >= 90) && U_passed_percent <= 100) {
						row.createCell(26).setCellValue(reportVb.getuPassedPercent() + "%");
						row.getCell(26).setCellStyle(styleGreen);
					} else if (U_passed_percent >= 80 && U_passed_percent <= 89) {
						row.createCell(26).setCellValue(reportVb.getuPassedPercent() + "%");
						row.getCell(26).setCellStyle(styleYellow);
					} else if (U_passed_percent <= 79) {
						row.createCell(26).setCellValue(reportVb.getuPassedPercent() + "%");
						row.getCell(26).setCellStyle(styleRed);
					}

					double Cs_passed_percent = Double.parseDouble(reportVb.getCsPassedPercent());

					if ((Cs_passed_percent >= 90) && Cs_passed_percent <= 100) {
						row.createCell(33).setCellValue(reportVb.getCsPassedPercent() + "%");
						row.getCell(33).setCellStyle(styleGreen);
					} else if (Cs_passed_percent >= 80 && Cs_passed_percent <= 89) {
						row.createCell(33).setCellValue(reportVb.getCsPassedPercent() + "%");
						row.getCell(33).setCellStyle(styleYellow);
					} else if (Cs_passed_percent <= 79) {
						row.createCell(33).setCellValue(reportVb.getCsPassedPercent() + "%");
						row.getCell(33).setCellStyle(styleRed);
					}
					String OPassedPercent =  "AM"+RowNumber+"/D"+RowNumber;
					int O_passed_percent = Integer.parseInt(reportVb.getoPassedIdCount().replaceAll(",", ""))/ Integer.parseInt(reportVb.getcDimensionUniverse().replaceAll(",", ""));
					
					if ((O_passed_percent >= 90) && O_passed_percent <= 100) {
						row.createCell(39);
						//setCellValue(OPassedPercent + "%");
						row.getCell(39).setCellStyle(styleGreen);
						row.getCell(39).setCellType(HSSFCell.CELL_TYPE_FORMULA);
						row.getCell(39).setCellFormula(OPassedPercent);
					} else if (O_passed_percent >= 80 && O_passed_percent <= 89) {
						row.createCell(39);//setCellValue(OPassedPercent + "%");
						row.getCell(39).setCellStyle(styleYellow);
						row.getCell(39).setCellType(HSSFCell.CELL_TYPE_FORMULA);
						row.getCell(39).setCellFormula(OPassedPercent);
					} else if (O_passed_percent <= 79) {
						row.createCell(39);//setCellValue(OPassedPercent + "%");
						row.getCell(39).setCellStyle(styleRed);
						row.getCell(39).setCellType(HSSFCell.CELL_TYPE_FORMULA);
						row.getCell(39).setCellFormula(OPassedPercent);
					}
				}
			}
			
			/*for(int loopCount=0; loopCount<columnsNew.length;loopCount++){
				sheet.setColumnWidth(loopCount, columnWidths.get(loopCount));
			}*/
			sheet.createFreezePane(3, 2);
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while downloading dimensional report : " + e.getMessage());
			return null;
		}

	}
	
	public static String convertQuery(String query, String orderBy){
		String finalQuery = "";
		finalQuery = "select rowtemp.*, ROW_NUMBER() OVER ( "+orderBy+" ) num from("+query+") rowtemp ";
		return finalQuery;
	}
	public static ExceptionCode getConnection(String dbScript){
		ExceptionCode exceptionCode = new ExceptionCode();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String dbIP = "";
		String jdbcUrl = "";
		String dbServiceName = getValue(dbScript, "SERVICE_NAME");
		String dbOracleSid = getValue(dbScript, "SID");
		String dbUserName = getValue(dbScript, "USER");
		if(!ValidationUtil.isValid(dbUserName))
			dbUserName = getValue(dbScript, "DB_USER");
		String dbPassWord = getValue(dbScript, "PWD");
		if(!ValidationUtil.isValid(dbPassWord))
			dbPassWord = getValue(dbScript, "DB_PWD");
		String dbPortNumber = getValue(dbScript, "DB_PORT");
		String dbPostFix = getValue(dbScript, "SUFFIX");
		String dbPreFix = getValue(dbScript, "PREFIX");
		String dataBaseName = getValue(dbScript, "DB_NAME");
		String dataBaseType = getValue(dbScript, "DATABASE_TYPE");
		String dbInstance = getValue(dbScript, "DB_INSTANCE");
		String dbIp = getValue(dbScript, "DB_IP");		
		String serverName = getValue(dbScript, "SERVER_NAME");
		String dbSetParam1 =getValue(dbScript, "DB_SET_PARAM1");
		String dbSetParam2 =getValue(dbScript, "DB_SET_PARAM2");
		String dbSetParam3 =getValue(dbScript, "DB_SET_PARAM3");
		String version = getValue(dbScript, "JAR_VERSION");
		String dbURL = CommonUtils.getValue(dbScript, "DB_URL");

		String hostName = dbServiceName;
		if(!ValidationUtil.isValid(hostName)){
			hostName = dbOracleSid;
		}
		if(ValidationUtil.isValid(dbIp))
			dbIP = dbIp;
		try{
			if("ORACLE".equalsIgnoreCase(dataBaseType)){
				if(ValidationUtil.isValid(dbURL)){
					jdbcUrl =dbURL;
				}else{
					jdbcUrl = "jdbc:oracle:thin:@"+dbIP+":"+dbPortNumber+":"+hostName;
				}
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "ORACLE", version);
			}else if("MSSQL".equalsIgnoreCase(dataBaseType)){
				if(ValidationUtil.isValid(dbURL)){
					jdbcUrl =dbURL;
				}else if(ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)){
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";instanceName="+dbInstance+";databaseName="+hostName;
				}else if(ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(hostName)){
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";instanceName="+dbInstance;
				}else if(!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)){
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";databaseName="+hostName;
				}else{
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";databaseName="+hostName;
				}
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MSSQL", version);
			}else if("MYSQL".equalsIgnoreCase(dataBaseType)){
				if(ValidationUtil.isValid(dbURL)){
					jdbcUrl =dbURL;
				}else if(ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)){
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"//instanceName="+dbInstance+"//databaseName="+dataBaseName;
				}else if(ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(dataBaseName)){
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"//instanceName="+dbInstance;
				}else if(!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)){
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"/"+dataBaseName;
				}else{
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"/"+dataBaseName;
				}	
				
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MYSQL", version);
			}else if("POSTGRESQL".equalsIgnoreCase(dataBaseType)){
				if(ValidationUtil.isValid(dbURL)){
					jdbcUrl =dbURL;
				}else {
					jdbcUrl = "jdbc:postgresql://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
				}
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "POSTGRESQL", version);
			}else if("SYBASE".equalsIgnoreCase(dataBaseType)){
				/* jdbc:sybase:Tds:194.170.154.136:7834?ServiceName=PROD_RCB */
	//			jdbcUrl="jdbc:jtds:sybase://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
				if(ValidationUtil.isValid(dbURL)){
					jdbcUrl =dbURL;
				}else{ 
					jdbcUrl="jdbc:sybase:Tds:"+dbIP+":"+dbPortNumber+"?ServiceName="+hostName;
				}
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "SYBASE", version);
			}else if("INFORMIX".equalsIgnoreCase(dataBaseType)){
				if(ValidationUtil.isValid(dbURL)){
					jdbcUrl =dbURL;
				}else{ 
					jdbcUrl = "jdbc:informix-sqli://"+dbIP+":"+dbPortNumber+"/"+dataBaseName+":informixserver="+serverName;
				}
				con = getDbConnection(jdbcUrl, dbUserName, dbPassWord, "INFORMIX", version);
			}
			if(con==null){
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Problem in gaining connection with datasource");
				return exceptionCode;
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:"+e.getMessage());
			return exceptionCode;
		} catch (SQLException e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:"+e.getMessage());
			return exceptionCode;
		}catch(Exception e){
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:"+e.getMessage());
			return exceptionCode;
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		exceptionCode.setResponse(con);
		return exceptionCode;
	}
	
	public static <T> String replaceCharacterRanges(T input) {
        String stringInput = String.valueOf(input);
        // Replace A-Z with 'A'
        String replaced = stringInput.replaceAll("[A-Z]", "A");

        // Replace a-z with 'a'
        replaced = replaced.replaceAll("[a-z]", "a");

        // Replace 0-9 with '#'
        replaced = replaced.replaceAll("[0-9]", "#");

        return replaced;
    }
	
	public static boolean isFileExist(ChannelSftp channelSftp, String fileName) throws SftpException {
		try {
		    channelSftp.lstat(fileName);
		    return true;
		} catch (SftpException e) {
		    if(e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE){
		    	return false;
		    } else {
		        throw e;
		    }
		}
	}
	
	public static ExceptionCode getDBConnectionDeatils(String dbScript) {
		ExceptionCode exceptionCode = new ExceptionCode();
		Map<String, String> dbConnectionsDetails = new HashMap<>();
		String dbIP = "";
		String jdbcUrl = "";
		String dbServiceName = getValue(dbScript, "SERVICE_NAME");
		String dbOracleSid = getValue(dbScript, "SID");
		String dbUserName = getValue(dbScript, "USER");
		if (!ValidationUtil.isValid(dbUserName))
			dbUserName = getValue(dbScript, "DB_USER");

		String dbPassWord = getValue(dbScript, "PWD");
		if (!ValidationUtil.isValid(dbPassWord))
			dbPassWord = getValue(dbScript, "DB_PWD");

		String dbPortNumber = getValue(dbScript, "DB_PORT");

		String dataBaseName = getValue(dbScript, "DB_NAME");
		String dataBaseType = getValue(dbScript, "DATABASE_TYPE");
		String dbInstance = getValue(dbScript, "DB_INSTANCE");
		String dbIp = getValue(dbScript, "DB_IP");

		String serverName = getValue(dbScript, "SERVER_NAME");
		String version = getValue(dbScript, "JAR_VERSION");
		String paramLevel = "";
		String hostName = dbServiceName;
		Boolean sidFlag = false;
		
		String driver = null;
		
		if (!ValidationUtil.isValid(hostName)) {
			hostName = dbOracleSid;
			sidFlag = true;
		}
		if (ValidationUtil.isValid(dbIp))
			dbIP = dbIp;
		try {
			if ("ORACLE".equalsIgnoreCase(dataBaseType)) {
				if(sidFlag)
					jdbcUrl = "jdbc:oracle:thin:@//" + dbIP + ":" + dbPortNumber + "/" + hostName;
				else
					jdbcUrl = "jdbc:oracle:thin:@" + dbIP + ":" + dbPortNumber + ":" + hostName;
				driver = "oracle.jdbc.driver.OracleDriver";
			} else if ("MSSQL".equalsIgnoreCase(dataBaseType) || "SQLSERVER".equalsIgnoreCase(dataBaseType)) {
				if (ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)) {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ";port=" + dbPortNumber + ";instanceName=" + dbInstance + ";databaseName=" + hostName;
				} else if (ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(hostName)) {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ";port=" + dbPortNumber + ";instanceName=" + dbInstance;
				} else if (!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)) {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ";port=" + dbPortNumber + ";databaseName=" + hostName;
				} else {
					jdbcUrl = "jdbc:sqlserver://" + dbIP + ";port=" + dbPortNumber + ";databaseName=" + hostName;
				}
				driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			} else if ("MYSQL".equalsIgnoreCase(dataBaseType)) {

				if (ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)) {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "//instanceName=" + dbInstance + "//databaseName=" + dataBaseName;
				} else if (ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(dataBaseName)) {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "//instanceName=" + dbInstance;
				} else if (!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)) {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "/" + dataBaseName;
				} else {
					jdbcUrl = "jdbc:mysql://" + dbIp + ":" + dbPortNumber + "/" + dataBaseName;
				}
				driver = "com.mysql.cj.jdbc.Driver";
			} else if ("POSTGRESQL".equalsIgnoreCase(dataBaseType)) {
				jdbcUrl = "jdbc:postgresql://" + dbIP + ":" + dbPortNumber + "/" + dataBaseName;
				driver = "org.postgresql.Driver";
			} else if ("SYBASE".equalsIgnoreCase(dataBaseType)) {
				/* jdbc:sybase:Tds:194.170.154.136:7834?ServiceName=PROD_RCB */
				// jdbcUrl="jdbc:jtds:sybase://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
				jdbcUrl = "jdbc:sybase:Tds:" + dbIP + ":" + dbPortNumber + "?ServiceName=" + hostName;
				driver = "com.sybase.jdbc4.jdbc.SybDataSource";
			} else if ("INFORMIX".equalsIgnoreCase(dataBaseType)) {
				jdbcUrl = "jdbc:informix-sqli://" + dbIP + ":" + dbPortNumber + "/" + dataBaseName + ":informixserver=" + serverName;
				driver = "com.informix.jdbc.IfxDriver";
			}
			dbConnectionsDetails.put("dbUrl", jdbcUrl);
			dbConnectionsDetails.put("dbUserName", dbUserName);
			dbConnectionsDetails.put("dbPassword", dbPassWord);
			dbConnectionsDetails.put("dbType", dataBaseType);
			dbConnectionsDetails.put("driver", driver);
		}catch (Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:" + (ValidationUtil.isValid(paramLevel) ? paramLevel : "") + e.getMessage());
			return exceptionCode;
		} finally {
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		exceptionCode.setResponse(dbConnectionsDetails);
		return exceptionCode;
	}
	public static Map<String, String> extractCreationExpireDate(String token) {
		Map<String, String> returnMap = new HashMap<>();
		Claims claims = JwtTokenUtil.parseToken(token);

		//date format for token issuedDate & expireDate
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		//date format for issuedDate from token
		Date tokenCreatedDate = claims.getIssuedAt();
        String strTokenCreatedDate = dateFormat.format(tokenCreatedDate);
        //date format for expireDate from token
		Date  expiration= claims.getExpiration();
        String strTokenExpirationDate = dateFormat.format(expiration); 
        
        returnMap.put("dateCreation", strTokenCreatedDate);
        returnMap.put("validTill", strTokenExpirationDate);
		return returnMap;
	}	
	public static ExceptionCode formHashList(DCManualQueryVb vObj) {
		ExceptionCode exceptionCode = new ExceptionCode();
		/* Use HashSet to avoid duplicates */
		HashSet<String> hashLst = new HashSet<String>();
		Pattern pattern = Pattern.compile("#(.*?)#", Pattern.DOTALL);
		Matcher matcher = null;
		String curretSql = "";
		try {
			if (ValidationUtil.isValid(vObj.getSqlQuery())) {
				curretSql = vObj.getSqlQuery().trim();
				matcher = pattern.matcher(curretSql);
				while (matcher.find()) {
					if (CommonUtils.checkValidHash(matcher.group(1)) && ValidationUtil.isValid(matcher.group(1)))
						hashLst.add(matcher.group(1).toUpperCase().replaceAll("\\s", "\\_"));
				}
			}
			if (ValidationUtil.isValid(vObj.getStgQuery1())) {
				curretSql = vObj.getStgQuery1().trim();
				matcher = pattern.matcher(curretSql);
				while (matcher.find()) {
					if (CommonUtils.checkValidHash(matcher.group(1)) && ValidationUtil.isValid(matcher.group(1)))
						hashLst.add(matcher.group(1).toUpperCase().replaceAll("\\s", "\\_"));
				}
			}
			if (ValidationUtil.isValid(vObj.getStgQuery2())) {
				curretSql = vObj.getStgQuery2().trim();
				matcher = pattern.matcher(curretSql);
				while (matcher.find()) {
					if (CommonUtils.checkValidHash(matcher.group(1)) && ValidationUtil.isValid(matcher.group(1)))
						hashLst.add(matcher.group(1).toUpperCase().replaceAll("\\s", "\\_"));
				}
			}
			if (ValidationUtil.isValid(vObj.getStgQuery3())) {
				curretSql = vObj.getStgQuery3().trim();
				matcher = pattern.matcher(curretSql);
				while (matcher.find()) {
					if (CommonUtils.checkValidHash(matcher.group(1)) && ValidationUtil.isValid(matcher.group(1)))
						hashLst.add(matcher.group(1).toUpperCase().replaceAll("\\s", "\\_"));
				}
			}
			if (ValidationUtil.isValid(vObj.getPostQuery())) {
				curretSql = vObj.getPostQuery().trim();
				matcher = pattern.matcher(curretSql);
				while (matcher.find()) {
					if (CommonUtils.checkValidHash(matcher.group(1)) && ValidationUtil.isValid(matcher.group(1)))
						hashLst.add(matcher.group(1).toUpperCase().replaceAll("\\s", "\\_"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Error forming hash list");
			exceptionCode.setOtherInfo(null);
			exceptionCode.setResponse(0);
			return exceptionCode;
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		String[] returnArr = hashLst.toArray(new String[hashLst.size()]);
		String[] returnValArr = new String[(returnArr != null) ? returnArr.length : 0];

		if (ValidationUtil.isValid(vObj.getHashVariableScript())) {
			String hashVarValue = vObj.getHashVariableScript();
			hashVarValue = hashVarValue.replaceAll("@HASH@", "#");
			int index = 0;
			for (String varName : returnArr) {
				String value = CommonUtils.getValue(hashVarValue, varName);
				returnValArr[index] = ValidationUtil.isValid(value) ? value : "";
				index++;
			}
		}
		exceptionCode.setOtherInfo(returnArr);
		exceptionCode.setResponse((returnArr != null) ? returnArr.length : 0);
		exceptionCode.setRequest(returnValArr);
		return exceptionCode;
	}


	
}