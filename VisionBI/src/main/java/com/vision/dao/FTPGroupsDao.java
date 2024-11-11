package com.vision.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.Paginationhelper;
import com.vision.util.PaginationhelperMsSql;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.DCManualQueryVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpAddonVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.FtpPremiumsVb;
import com.vision.vb.SmartSearchVb;
import com.vision.vb.VcConfigMainColumnsVb;
import com.vision.vb.VcConfigMainTreeVb;

@Component
public class FTPGroupsDao extends AbstractDao<FTPGroupsVb> {
	@Value("${app.databaseType}")
	 private String databaseType;
	
	@Autowired
	FTPSourceConfigDao ftpSourceConfigDao;

	@Autowired
	FtpMethodsDao ftpMethodsDao;
	
	@Autowired
	FtpCurvesDao ftpCurvesDao;
	
	@Autowired
	FtpAddonDao ftpAddonDao;
	
	@Autowired
	FtpPremiumsDao ftpPremiumsDao;

	public FtpMethodsDao getFtpMethodsDao() {
		return ftpMethodsDao;
	}

	public void setFtpMethodsDao(FtpMethodsDao ftpMethodsDao) {
		this.ftpMethodsDao = ftpMethodsDao;
	}

	public FTPSourceConfigDao getFtpSourceConfigDao() {
		return ftpSourceConfigDao;
	}

	public void setFtpSourceConfigDao(FTPSourceConfigDao ftpSourceConfigDao) {
		this.ftpSourceConfigDao = ftpSourceConfigDao;
	}

	String DataSourceAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 10, "TAppr.DATA_SOURCE", "DATA_SOURCE_DESC");
	String DataSourceAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 10, "TPend.DATA_SOURCE", "DATA_SOURCE_DESC");

	String FtpGroupAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 1301, "TAppr.FTP_GROUP", "FTP_GROUP_DESC");
	String FtpGroupAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 1301, "TPend.FTP_GROUP", "FTP_GROUP_DESC");
	
	String StatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_GRP_STATUS",
			"FTP_GRP_STATUS_DESC");
	String StatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_GRP_STATUS",
			"FTP_GRP_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR",
			"RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR",
			"RECORD_INDICATOR_DESC");
	
//	New Changes Start
	
	public List<FTPGroupsVb> getQueryPopupResultsByGroup(FTPGroupsVb dObj) {
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, "
				+ " TAppr.DATA_SOURCE,TAPPR.FTP_GROUP"
				+ ",(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC  From FTP_GROUPS TAppr ");
		StringBuffer strBufPending = new StringBuffer("Select Distinct TPend.COUNTRY, TPend.LE_BOOK, "
				+ " TPend.DATA_SOURCE,TPend.FTP_GROUP"
				+ ",(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC From FTP_GROUPS_PEND TPend");
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_GROUPS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK"
				+ " AND TAppr.DATA_SOURCE = TPend.DATA_SOURCE AND TAppr.FTP_GROUP = TPend.FTP_GROUP"
//				+ " AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID "
				+ ")");
		try {
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data : dObj.getSmartSearchOpt()) {
					if(!data.getObject().equalsIgnoreCase("ftpGroupDesc")) {
						if (count == dObj.getSmartSearchOpt().size()) {
							data.setJoinType("");
						} else {
							if (!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType())
									|| "OR".equalsIgnoreCase(data.getJoinType()))) {
								data.setJoinType("AND");
							}
						}
						String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
						switch (data.getObject()) {
						case "country":
//							CommonUtils.addToQuery("TPend.RECORD_INDICATOR = ?", strBufPending);
							CommonUtils.addToQuery("TAppr.COUNTRY = ?", strBufApprove);
							CommonUtils.addToQuery("TPend.COUNTRY = ?", strBufPending);
							params.addElement(data.getValue());
//							CommonUtils.addToQuerySearch(" upper(TAppr.COUNTRY) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.COUNTRY) " + val, strBufPending, data.getJoinType());
							break;
							
						case "leBook":
							CommonUtils.addToQuery("TAppr.LE_BOOK = ?", strBufApprove);
							CommonUtils.addToQuery("TPend.LE_BOOK = ?", strBufPending);
							params.addElement(data.getValue());
//							CommonUtils.addToQuerySearch(" upper(TAppr.LE_BOOK) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.LE_BOOK) " + val, strBufPending, data.getJoinType());
							break;
							
						case "dataSource":
							CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
							CommonUtils.addToQuery("TPend.DATA_SOURCE = ?", strBufPending);
							params.addElement(data.getValue());
//							CommonUtils.addToQuerySearch(" upper(TAppr.DATA_SOURCE) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.DATA_SOURCE) " + val, strBufPending, data.getJoinType());
							break;								
							
						case "ftpSubGroupPriority":
							CommonUtils.addToQuery("UPPER(TAppr.FTP_SUB_GROUP_PRIORITY) "+val, strBufApprove);
							CommonUtils.addToQuery("UPPER(TPend.FTP_SUB_GROUP_PRIORITY) "+val, strBufPending);
//							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_PRIORITY) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_PRIORITY) " + val, strBufPending, data.getJoinType());
							break;
							
							
						case "ftpSubGroupDesc":
							CommonUtils.addToQuery("UPPER(TAppr.FTP_SUB_GROUP_DESC) "+val, strBufApprove);
							CommonUtils.addToQuery("UPPER(TPend.FTP_SUB_GROUP_DESC) "+val, strBufPending);
//							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_DESC) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_DESC) " + val, strBufPending, data.getJoinType());
							break;
							
						case "recordIndicatorDesc":
							CommonUtils.addToQuerySearch(" upper(TAppr.RECORD_INDICATOR) " + val, strBufApprove, data.getJoinType());
							CommonUtils.addToQuerySearch(" upper(TPend.RECORD_INDICATOR) " + val, strBufPending, data.getJoinType());
							break;						
							
						case "statusDesc":
							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_GRP_STATUS) " + val, strBufApprove, data.getJoinType());
							CommonUtils.addToQuerySearch(" upper(TPend.FTP_GRP_STATUS) " + val, strBufPending, data.getJoinType());
							break;
							
						case "defaultGroup":
							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_DEFAULT_FLAG) " + val, strBufApprove, data.getJoinType());
							CommonUtils.addToQuerySearch(" upper(TPend.FTP_DEFAULT_FLAG) " + val, strBufPending, data.getJoinType());
							break;
							
						default:
						}
						count++;
					}
				}
			}
			StringBuffer strBufApproveFinal = new StringBuffer();
			StringBuffer strBufPendingFinal = new StringBuffer();
			strBufApproveFinal.append("SELECT * FROM ("+strBufApprove+") TAppr");
			strBufPendingFinal.append("SELECT * FROM ("+strBufPending+") TPend");
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data : dObj.getSmartSearchOpt()) {
					if(data.getObject().equalsIgnoreCase("ftpGroupDesc")) {
						if (count == dObj.getSmartSearchOpt().size()) {
							data.setJoinType("");
						} else {
							if (!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType())
									|| "OR".equalsIgnoreCase(data.getJoinType()))) {
								data.setJoinType("AND");
							}
						}
						String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
						switch (data.getObject()) {
						case "ftpGroupDesc":
							CommonUtils.addToQuery("UPPER(TAppr.FTP_GROUP_DESC) "+val, strBufApproveFinal);
							CommonUtils.addToQuery("UPPER(TPend.FTP_GROUP_DESC) "+val, strBufPendingFinal);
//							CommonUtils.addToQuerySearch(" upper(TAppr.) " + val, strBufApproveFinal, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.FTP_GROUP_DESC) " + val, strBufPendingFinal, data.getJoinType());
							break;				
							
						default:
						}
						count++;
					}
				}
			}
			
			String orderBy = " Order By COUNTRY, LE_BOOK, DATA_SOURCE, FTP_GROUP ";
			return getQueryPopupResults(dObj, strBufPendingFinal, strBufApproveFinal, strWhereNotExists, orderBy, params,
					getQueryPopupMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			if (params != null)
				for (int i = 0; i < params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}

	public List<FTPGroupsVb> getQueryPopupResultsDetails(FTPGroupsVb dObj) {
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		/*StringBuffer strBufApprove = new StringBuffer("SELECT * FROM (SELECT TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,\n"
				+ "(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC\n"
				+ ",TAppr.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC"
				+ ",TAppr.FTP_SUB_GROUP_PRIORITY,\n" + "	 TAppr.SOURCE_REFERENCE,\n"
				+ "(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB T1 WHERE ALPHA_TAB = SOURCE_REFERENCE_AT AND ALPHA_sub_TAB= SOURCE_REFERENCE ) SOURCE_REFERENCE_DESC,\n"
				+ "				TAppr.METHOD_REFERENCE,\n"
				+ "				(SELECT MIN(METHOD_DESCRIPTION) FROM FTP_METHODS WHERE METHOD_REFERENCE = TAppr.METHOD_REFERENCE) METHOD_DESCRIPTION,\n"
				+ "				TAppr.FTP_DEFAULT_FLAG,\n" + "				TAppr.FTP_GRP_STATUS, "
				+ VariableStatusNtApprDesc + ",TAppr.RECORD_INDICATOR, " + RecordIndicatorNtApprDesc
				+ ",TAppr.MAKER,"+makerApprDesc+" ,TAppr.VERIFIER,"+verifierApprDesc+",TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED \n"
				+ "				FROM FTP_GROUPS TAppr) TAppr");*/
		
		/*StringBuffer strBufApprove = new StringBuffer("SELECT * FROM (SELECT TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,  \n" + 
				"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
				"				   ,TAppr.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
				"				   ,TAppr.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				"				   				TAppr.FTP_SUB_GROUP_ID,  \n" + 
				"				   				FTP_SUB_GROUP_DESC,  \n" + 
				"				   				METHOD_TYPE,\n" + 
				"				   				(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_sub_TAB= METHOD_TYPE ) METHOD_TYPE_DESC ,  \n" + 
				"				   				TAppr.FTP_DEFAULT_FLAG,      				TAppr.FTP_GRP_STATUS,     \n" + StatusNtApprDesc+
				"				   ,TAppr.RECORD_INDICATOR,\n" +RecordIndicatorNtApprDesc+ 
				"				   ,TAppr.MAKER, "+makerApprDesc+",  TAppr.VERIFIER, "+verifierApprDesc+",   TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED   \n" + 
				"				   				FROM FTP_GROUPS TAppr, FTP_METHODS FTP_METHOD\n" + 
				"				   				WHERE  TAppr.COUNTRY = FTP_METHOD.COUNTRY\n" + 
				"				   				AND TAppr.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
				"				   				AND TAppr.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID) TAppr");*/

		String strApprGroupBy = " GROUP BY TAppr.COUNTRY, TAppr.LE_BOOK, TAppr.DATA_SOURCE, TAppr.FTP_GROUP,\n" + 
				"         TAppr.FTP_SUB_GROUP_PRIORITY, TAppr.FTP_SUB_GROUP_ID, FTP_SUB_GROUP_DESC,\n" + 
				"         TAppr.FTP_DEFAULT_FLAG, TAppr.FTP_GRP_STATUS, TAppr.RECORD_INDICATOR,\n" + 
				"         TAppr.MAKER, TAppr.VERIFIER, TAppr.INTERNAL_STATUS, TAppr.DATE_CREATION,\n" + 
				"         TAppr.DATE_LAST_MODIFIEd, TAppr.DATA_SOURCE_AT, TAppr.FTP_GROUP_AT ";
		
		String strPendGroupBy =  "GROUP BY TPend.COUNTRY, TPend.LE_BOOK, TPend.DATA_SOURCE, TPend.FTP_GROUP,\n" + 
				"         TPend.FTP_SUB_GROUP_PRIORITY, TPend.FTP_SUB_GROUP_ID, FTP_SUB_GROUP_DESC,\n" + 
				"         TPend.FTP_DEFAULT_FLAG, TPend.FTP_GRP_STATUS, TPend.RECORD_INDICATOR,\n" + 
				"         TPend.MAKER, TPend.VERIFIER, TPend.INTERNAL_STATUS, TPend.DATE_CREATION,\n" + 
				"         TPend.DATE_LAST_MODIFIEd, TPend.DATA_SOURCE_AT, TPend.FTP_GROUP_AT ";
		
		StringBuffer strBufApprove = new StringBuffer("SELECT * FROM (SELECT TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,  \n" + 
				"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
				"				   ,TAppr.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
				"				   ,TAppr.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				"				   				TAppr.FTP_SUB_GROUP_ID,  \n" + 
				"				   				FTP_SUB_GROUP_DESC,  \n" + 
				"				   				LISTAGG(METHOD_TYPE, ', ') WITHIN GROUP (ORDER BY METHOD_TYPE) AS METHOD_TYPE_DESC ,\n" + 
//				"				   				(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_sub_TAB= METHOD_TYPE ) METHOD_TYPE_DESC ,  \n" + 
				"				   				TAppr.FTP_DEFAULT_FLAG,      				TAppr.FTP_GRP_STATUS,     \n" + StatusNtApprDesc+
				"				   ,TAppr.RECORD_INDICATOR,\n" +RecordIndicatorNtApprDesc+ 
				"				   ,TAppr.MAKER, "+makerApprDesc+",  TAppr.VERIFIER, "+verifierApprDesc+",   TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED   \n" + 
				"				   				FROM FTP_GROUPS TAppr LEFT JOIN  FTP_METHODS FTP_METHOD\n" + 
				"				   				ON  TAppr.COUNTRY = FTP_METHOD.COUNTRY\n" + 
				"				   				AND TAppr.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
				"				   				AND TAppr.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID"
				+ " "+strApprGroupBy+" "
				+ ") TAppr");
		
		StringBuffer strBufPending = new StringBuffer("SELECT * FROM (SELECT TPend.COUNTRY,TPend.LE_BOOK,TPend.DATA_SOURCE,  \n" + 
				"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
				"				   ,TPend.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
				"				   ,TPend.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				"				   				TPend.FTP_SUB_GROUP_ID,  \n" + 
				"				   				FTP_SUB_GROUP_DESC,  \n" + 
				"				   				LISTAGG(METHOD_TYPE, ', ') WITHIN GROUP (ORDER BY METHOD_TYPE) AS METHOD_TYPE_DESC ,\n" + 
//				"				   				(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_sub_TAB= METHOD_TYPE ) METHOD_TYPE_DESC ,  \n" + 
				"				   				TPend.FTP_DEFAULT_FLAG,      				TPend.FTP_GRP_STATUS,     \n" + StatusNtPendDesc+
				"				   ,TPend.RECORD_INDICATOR,\n" +RecordIndicatorNtPendDesc+ 
				"				   ,TPend.MAKER, "+makerPendDesc+",  TPend.VERIFIER, "+verifierPendDesc+",   TPend.INTERNAL_STATUS,TPend.DATE_CREATION,TPend.DATE_LAST_MODIFIED   \n" + 
				"				   				FROM FTP_GROUPS_PEND TPend LEFT JOIN  FTP_METHODS_PEND FTP_METHOD\n" + 
				"				   				ON  TPend.COUNTRY = FTP_METHOD.COUNTRY\n" + 
				"				   				AND TPend.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
				"				   				AND TPend.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID"
				+ " "+strPendGroupBy+" "
				+ ") TPend");
		try {
			if (ValidationUtil.isValid(dObj.getCountry())) {
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COUNTRY) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getLeBook())) {
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.LE_BOOK) like ?", strBufPending);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())) {
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DATA_SOURCE = ?", strBufPending);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())) {
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("TAppr.FTP_GROUP = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_GROUP = ?", strBufPending);
			}
			// check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1) {
				if (dObj.getRecordIndicator() > 3) {
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
					CommonUtils.addToQuery("TPend.RECORD_INDICATOR > ?", strBufPending);
				} else {
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
					CommonUtils.addToQuery("TPend.RECORD_INDICATOR = ?", strBufPending);
				}
			}

			
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data : dObj.getSmartSearchOpt()) {
					if (count == dObj.getSmartSearchOpt().size()) {
						data.setJoinType("");
					} else {
						if (!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType())
								|| "OR".equalsIgnoreCase(data.getJoinType()))) {
							data.setJoinType("AND");
						}
					}
					String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
					switch (data.getObject()) {
					case "ftpSubGroupPriority":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_PRIORITY) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_PRIORITY) " + val, strBufPending, data.getJoinType());
						break;
						
					case "dataSource":
						CommonUtils.addToQuerySearch(" upper(TAppr.DATA_SOURCE) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.DATA_SOURCE) " + val, strBufPending, data.getJoinType());
						break;	
						
					case "ftpSubGroupDesc":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_DESC) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_DESC) " + val, strBufPending, data.getJoinType());
						break;
						
					case "recordIndicatorDesc":
						CommonUtils.addToQuerySearch(" upper(TAppr.RECORD_INDICATOR) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.RECORD_INDICATOR) " + val, strBufPending, data.getJoinType());
						break;						
						
					case "ftpGroupStatus":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_GRP_STATUS) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_GRP_STATUS) " + val, strBufPending, data.getJoinType());
						break;
						
					case "defaultGroup":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_DEFAULT_FLAG) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_DEFAULT_FLAG) " + val, strBufPending, data.getJoinType());
						break;
						
					default:
					}
					count++;
				}
			} 

			String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_GROUPS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.DATA_SOURCE = TPend.DATA_SOURCE AND TAppr.FTP_GROUP = TPend.FTP_GROUP AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID )");
			
			String orderBy = " ORDER BY COUNTRY, LE_BOOK, DATA_SOURCE, FTP_GROUP, FTP_SUB_GROUP_PRIORITY,FTP_SUB_GROUP_ID ";
//			strBufApprove.append(strApprGroupBy);
			
			return getQueryPopupResults(dObj, strBufPending, strBufApprove, strWhereNotExists, orderBy, params,
					getQueryPopupMapperDetails());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			if (params != null)
				for (int i = 0; i < params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResultsDetailsNew(FTPGroupsVb dObj, List<SmartSearchVb> groupSmartSearchOpt) {
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		String listStag = "LISTAGG";
		String seperator = "||";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			seperator = "+";
			listStag = "STRING_AGG";
		}
		StringBuffer strBufApprove = new StringBuffer("SELECT * FROM (SELECT TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,  \n" + 
				"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
				"				   ,TAppr.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
				"				   ,TAppr.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				"				   				TAppr.FTP_SUB_GROUP_ID, "+
				"				   				FTP_SUB_GROUP_DESC, \n"
				+ " "+listStag+"(CASE WHEN (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE) IS NOT NULL \n" + 
				"				   				THEN (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE)\n" + 
				"				   				"+seperator+"' ('"+seperator+"  CASE WHEN REPRICING_FLAG = 'NOMINAL' THEN 'N' ELSE 'R' END "+seperator+"')' \n" + 
				"				   				ELSE '' END,',') METHOD_LIST, \n"+
				"				   				TAppr.FTP_DEFAULT_FLAG,      				TAppr.FTP_GRP_STATUS,     \n" + StatusNtApprDesc+
				"				   ,TAppr.RECORD_INDICATOR,\n" +RecordIndicatorNtApprDesc+ 
				"				   ,TAppr.MAKER, "+makerApprDesc+",  TAppr.VERIFIER, "+verifierApprDesc+",   TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED   \n" + 
				"				   				FROM FTP_GROUPS TAppr LEFT JOIN  FTP_METHODS FTP_METHOD\n" + 
				"				   				ON  TAppr.COUNTRY = FTP_METHOD.COUNTRY\n" + 
				"				   				AND TAppr.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
				"				   				AND TAppr.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID "
				+ " GROUP BY\n" + 
				" TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,  \n" + 
				" TAppr.DATA_SOURCE_AT ,\n" + 
				" TAppr.FTP_GROUP,TAppr.FTP_GROUP_AT,\n" + 
				" TAppr.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				" TAppr.FTP_SUB_GROUP_ID, \n" + 
				" FTP_SUB_GROUP_DESC,  \n" + 
				" TAppr.FTP_DEFAULT_FLAG,      				TAppr.FTP_GRP_STATUS,TAppr.FTP_GRP_STATUS,TAppr.RECORD_INDICATOR,\n" + 
				" TAppr.MAKER,\n" + 
				" TAppr.VERIFIER,TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED) TAppr ");
		
		StringBuffer strBufPending = new StringBuffer("SELECT * FROM (SELECT TPend.COUNTRY,TPend.LE_BOOK,TPend.DATA_SOURCE,  \n" + 
				"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
				"				   ,TPend.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
				"				   ,TPend.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				"				   				TPend.FTP_SUB_GROUP_ID, "+
				"				   				FTP_SUB_GROUP_DESC,  \n" +
				" "+listStag+"(CASE WHEN (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE) IS NOT NULL \n" + 
				"				   				THEN (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE)\n" + 
				"				   				"+seperator+"' ('"+seperator+"  CASE WHEN REPRICING_FLAG = 'NOMINAL' THEN 'N' ELSE 'R' END "+seperator+"')' \n" + 
				"				   				ELSE '' END,',') METHOD_LIST, \n"+
				"				   				TPend.FTP_DEFAULT_FLAG,      				TPend.FTP_GRP_STATUS,     \n" + StatusNtPendDesc+
				"				   ,TPend.RECORD_INDICATOR,\n" +RecordIndicatorNtPendDesc+ 
				"				   ,TPend.MAKER, "+makerPendDesc+",  TPend.VERIFIER, "+verifierPendDesc+",   TPend.INTERNAL_STATUS,TPend.DATE_CREATION,TPend.DATE_LAST_MODIFIED   \n" + 
				"				   				FROM FTP_GROUPS_PEND TPend LEFT JOIN  FTP_METHODS_PEND FTP_METHOD\n" + 
				"				   				ON  TPend.COUNTRY = FTP_METHOD.COUNTRY\n" + 
				"				   				AND TPend.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
				"				   				AND TPend.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID "
				+ "  GROUP BY\n" + 
				"    TPend.COUNTRY,TPend.LE_BOOK,TPend.DATA_SOURCE,  \n" + 
				"    TPend.DATA_SOURCE_AT ,\n" + 
				"    TPend.FTP_GROUP,TPend.FTP_GROUP_AT,\n" + 
				"    TPend.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				"    TPend.FTP_SUB_GROUP_ID, \n" + 
				"    FTP_SUB_GROUP_DESC,  \n" + 
				"    TPend.FTP_DEFAULT_FLAG,      				TPend.FTP_GRP_STATUS,TPend.FTP_GRP_STATUS,TPend.RECORD_INDICATOR,\n" + 
				"    TPend.MAKER,\n" + 
				"    TPend.VERIFIER,TPend.INTERNAL_STATUS,TPend.DATE_CREATION,TPend.DATE_LAST_MODIFIED) TPend ");
		String groupBy = " GROUP BY\n" + 
				" COUNTRY,LE_BOOK,DATA_SOURCE, DATA_SOURCE_DESC, \n" + 
				" FTP_GROUP, FTP_GROUP_DESC,\n" + 
				" FTP_SUB_GROUP_PRIORITY,      	  \n" + 
				" FTP_SUB_GROUP_ID, \n" + 
				" FTP_SUB_GROUP_DESC,  \n" + 
				" FTP_DEFAULT_FLAG,      				\n" + 
				" FTP_GRP_STATUS, FTP_GRP_STATUS_DESC,\n" + 
				" RECORD_INDICATOR,\n" + 
				" RECORD_INDICATOR_DESC,\n" + 
				" MAKER, MAKER_NAME,\n" + 
				" VERIFIER, VERIFIER_NAME,\n" + 
				" INTERNAL_STATUS,\n" + 
				" DATE_CREATION,DATE_LAST_MODIFIED ";
		
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new StringBuffer("SELECT \n" + 
					" COUNTRY,LE_BOOK,DATA_SOURCE, DATA_SOURCE_DESC,  \n" + 
					" FTP_GROUP, FTP_GROUP_DESC,\n" + 
					" FTP_SUB_GROUP_PRIORITY,      	  \n" + 
					" FTP_SUB_GROUP_ID, \n" + 
					" FTP_SUB_GROUP_DESC,  \n" + 
					" FTP_DEFAULT_FLAG,      				\n" + 
					" FTP_GRP_STATUS, FTP_GRP_STATUS_DESC,\n" + 
					" RECORD_INDICATOR,\n" + 
					" RECORD_INDICATOR_DESC,\n" + 
					" MAKER, MAKER_NAME,\n" + 
					" VERIFIER, VERIFIER_NAME,\n" + 
					" INTERNAL_STATUS,\n" + 
					" DATE_CREATION,DATE_LAST_MODIFIED,"
					+ "STRING_AGG(METHOD_LIST+' ('+CASE WHEN REPRICING_FLAG = 'NOMINAL' THEN 'N' ELSE 'R' END+')', ',') AS METHOD_LIST\n" + 
					"FROM (\n" + 
					"SELECT REPRICING_FLAG, TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,  \n" + 
					"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
					"				   ,TAppr.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
					"				   ,TAppr.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
					"				   				TAppr.FTP_SUB_GROUP_ID, 				   				FTP_SUB_GROUP_DESC, \n" + 
					" (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE) METHOD_LIST, \n" + 
					"				   				TAppr.FTP_DEFAULT_FLAG,      				TAppr.FTP_GRP_STATUS,     \n" + 
					"(SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB = 1 AND NUM_SUB_TAB =  TAppr.FTP_GRP_STATUS)  FTP_GRP_STATUS_DESC				   ,TAppr.RECORD_INDICATOR,\n" + 
					"(SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB = 7 AND NUM_SUB_TAB =  TAppr.RECORD_INDICATOR)  RECORD_INDICATOR_DESC				   ,TAppr.MAKER, (SELECT MIN(USER_NAME) FROM VISION_USERS WHERE VISION_ID = CASE  WHEN TAppr.MAKER IS NULL THEN 0  ELSE TAppr.MAKER  END ) MAKER_NAME,  TAppr.VERIFIER, (SELECT MIN(USER_NAME) FROM VISION_USERS WHERE VISION_ID = CASE  WHEN TAppr.VERIFIER IS NULL THEN 0  ELSE TAppr.VERIFIER  END ) VERIFIER_NAME,   TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED   \n" + 
					"				   				FROM FTP_GROUPS TAppr LEFT JOIN  FTP_METHODS FTP_METHOD\n" + 
					"				   				ON  TAppr.COUNTRY = FTP_METHOD.COUNTRY\n" + 
					"				   				AND TAppr.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
					"				   				AND TAppr.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID \n" + 
					") TAppr ");
			
			
			strBufPending = new StringBuffer("SELECT \n" + 
					" COUNTRY,LE_BOOK,DATA_SOURCE,DATA_SOURCE_DESC,  \n" + 
					" FTP_GROUP,FTP_GROUP_DESC,\n" + 
					" FTP_SUB_GROUP_PRIORITY,      	  \n" + 
					" FTP_SUB_GROUP_ID, \n" + 
					" FTP_SUB_GROUP_DESC,  \n" + 
					" FTP_DEFAULT_FLAG,      				\n" + 
					" FTP_GRP_STATUS, FTP_GRP_STATUS_DESC,\n" + 
					" RECORD_INDICATOR,\n" + 
					" RECORD_INDICATOR_DESC,\n" + 
					" MAKER, MAKER_NAME,\n" + 
					" VERIFIER, VERIFIER_NAME,\n" + 
					" INTERNAL_STATUS,\n" + 
					" DATE_CREATION,DATE_LAST_MODIFIED,"
					+ "STRING_AGG(METHOD_LIST+' ('+CASE WHEN REPRICING_FLAG = 'NOMINAL' THEN 'N' ELSE 'R' END+')', ',') AS METHOD_LIST\n" + 
					"FROM (\n" + 
					"SELECT REPRICING_FLAG, TPend.COUNTRY,TPend.LE_BOOK,TPend.DATA_SOURCE,  \n" + 
					"				   (SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC  \n" + 
					"				   ,TPend.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TPend WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC \n" + 
					"				   ,TPend.FTP_SUB_GROUP_PRIORITY,      	  \n" + 
					"				   				TPend.FTP_SUB_GROUP_ID, 				   				FTP_SUB_GROUP_DESC, \n" + 
					" (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE) METHOD_LIST, \n" + 
					"				   				TPend.FTP_DEFAULT_FLAG,      				TPend.FTP_GRP_STATUS,     \n" + 
					"(SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB = 1 AND NUM_SUB_TAB =  TPend.FTP_GRP_STATUS)  FTP_GRP_STATUS_DESC				   ,TPend.RECORD_INDICATOR,\n" + 
					"(SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB = 7 AND NUM_SUB_TAB =  TPend.RECORD_INDICATOR)  RECORD_INDICATOR_DESC				   ,TPend.MAKER, (SELECT MIN(USER_NAME) FROM VISION_USERS WHERE VISION_ID = CASE  WHEN TPend.MAKER IS NULL THEN 0  ELSE TPend.MAKER  END ) MAKER_NAME,  TPend.VERIFIER, (SELECT MIN(USER_NAME) FROM VISION_USERS WHERE VISION_ID = CASE  WHEN TPend.VERIFIER IS NULL THEN 0  ELSE TPend.VERIFIER  END ) VERIFIER_NAME,   TPend.INTERNAL_STATUS,TPend.DATE_CREATION,TPend.DATE_LAST_MODIFIED   \n" + 
					"				   				FROM FTP_GROUPS_PEND TPend LEFT JOIN  FTP_METHODS_PEND FTP_METHOD\n" + 
					"				   				ON  TPend.COUNTRY = FTP_METHOD.COUNTRY\n" + 
					"				   				AND TPend.LE_BOOK = FTP_METHOD.LE_BOOK\n" + 
					"				   				AND TPend.FTP_SUB_GROUP_ID = FTP_METHOD.FTP_SUB_GROUP_ID \n" + 
					") TPend ");
		}
		
		try {
			if (ValidationUtil.isValid(dObj.getCountry())) {
				params.addElement(dObj.getCountry().toUpperCase());
				CommonUtils.addToQuery("TAppr.COUNTRY = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.COUNTRY = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getLeBook())) {
				params.addElement(dObj.getLeBook().toUpperCase());
				CommonUtils.addToQuery("TAppr.LE_BOOK = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.LE_BOOK = ?", strBufPending);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())) {
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DATA_SOURCE = ?", strBufPending);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())) {
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("TAppr.FTP_GROUP = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_GROUP = ?", strBufPending);
			}
			// check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1) {
				if (dObj.getRecordIndicator() > 3) {
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
					CommonUtils.addToQuery("TPend.RECORD_INDICATOR > ?", strBufPending);
				} else {
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
					CommonUtils.addToQuery("TPend.RECORD_INDICATOR = ?", strBufPending);
				}
			}

			
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data : dObj.getSmartSearchOpt()) {
					if (count == dObj.getSmartSearchOpt().size()) {
						data.setJoinType("");
					} else {
						if (!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType())
								|| "OR".equalsIgnoreCase(data.getJoinType()))) {
							data.setJoinType("AND");
						}
					}
					String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
					switch (data.getObject()) {
					case "ftpSubGroupPriority":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_PRIORITY) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_PRIORITY) " + val, strBufPending, data.getJoinType());
						break;
						
					case "methodTypeDesc":
						CommonUtils.addToQuerySearch(" upper(TAppr.METHOD_LIST) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.METHOD_LIST) " + val, strBufPending, data.getJoinType());
						break;	
						
					case "ftpSubGroupDesc":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_DESC) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_DESC) " + val, strBufPending, data.getJoinType());
						break;
						
					case "recordIndicatorDesc":
						CommonUtils.addToQuerySearch(" upper(TAppr.RECORD_INDICATOR_DESC) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.RECORD_INDICATOR_DESC) " + val, strBufPending, data.getJoinType());
						break;						
						
					case "statusDesc":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_GRP_STATUS_DESC) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_GRP_STATUS_DESC) " + val, strBufPending, data.getJoinType());
						break;
						
					case "defaultGroup":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_DEFAULT_FLAG) " + val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_DEFAULT_FLAG) " + val, strBufPending, data.getJoinType());
						break;
						
					default:
					}
					count++;
				}
			} 
			
			if (groupSmartSearchOpt != null && groupSmartSearchOpt.size() > 0) {
				int count = 1;
				for (SmartSearchVb data : groupSmartSearchOpt) {
					if(data.getObject().equalsIgnoreCase("ftpSubGroupPriority")
						|| data.getObject().equalsIgnoreCase("ftpSubGroupDesc")) {
						if (count == dObj.getSmartSearchOpt().size()) {
							data.setJoinType("");
						} else {
							if (!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType())
									|| "OR".equalsIgnoreCase(data.getJoinType()))) {
								data.setJoinType("AND");
							}
						}
						String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
						switch (data.getObject()) {
						case "ftpSubGroupPriority":
							CommonUtils.addToQuery("UPPER(TAppr.FTP_SUB_GROUP_PRIORITY) "+val, strBufApprove);
							CommonUtils.addToQuery("UPPER(TPend.FTP_SUB_GROUP_PRIORITY) "+val, strBufPending);
//							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_PRIORITY) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_PRIORITY) " + val, strBufPending, data.getJoinType());
							break;
							
						case "ftpSubGroupDesc":
							CommonUtils.addToQuery("UPPER(TAppr.FTP_SUB_GROUP_DESC) "+val, strBufApprove);
							CommonUtils.addToQuery("UPPER(TPend.FTP_SUB_GROUP_DESC) "+val, strBufPending);
//							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_DESC) " + val, strBufApprove, data.getJoinType());
//							CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_DESC) " + val, strBufPending, data.getJoinType());
							break;
						case "defaultGroup":
							CommonUtils.addToQuerySearch(" upper(TAppr.FTP_DEFAULT_FLAG) " + val, strBufApprove, data.getJoinType());
							CommonUtils.addToQuerySearch(" upper(TPend.FTP_DEFAULT_FLAG) " + val, strBufPending, data.getJoinType());
							break;
							
						default:
						}
						count++;
					}
				}
			}
			
			String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_GROUPS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.DATA_SOURCE = TPend.DATA_SOURCE AND TAppr.FTP_GROUP = TPend.FTP_GROUP AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID )");
			
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				
				CommonUtils.addToQuery(strWhereNotExists, strBufApprove);
				
				strBufApprove.append(groupBy);
				strBufPending.append(groupBy);
				
				
				strWhereNotExists = "";
			}
			
			
//			String orderBy = " ORDER BY  COUNTRY, LE_BOOK, DATA_SOURCE, FTP_GROUP, FTP_SUB_GROUP_PRIORITY,FTP_SUB_GROUP_ID ";
			String orderBy = " ORDER BY  case when FTP_DEFAULT_FLAG = 'Y' THEN 0 ELSE 1 END,  FTP_SUB_GROUP_PRIORITY";
//			strBufApprove.append(strApprGroupBy);
			
			return getQueryPopupResults(dObj, strBufPending, strBufApprove, strWhereNotExists, orderBy, params, getQueryPopupMapperDetails());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			if (params != null)
				for (int i = 0; i < params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	protected List<FTPGroupsVb> getQueryPopupResults(FTPGroupsVb dObj,StringBuffer pendingQuery, StringBuffer approveQuery, 
			String whereNotExistsQuery,	String orderBy, Vector<Object> params, RowMapper rowMapper){
		Object objParams[]=null;
		int Ctr = 0;
		int Ctr2 = 0;
		List<FTPGroupsVb> result;
		// Now the params are ready with the values.Create an object array and insert the values into it
		if(dObj.isVerificationRequired() && dObj.getRecordIndicator() != 0){
			objParams = new Object[params.size()*2];
		}else{
			objParams = new Object[params.size()];
		}

		for(Ctr=0; Ctr < params.size(); Ctr++)
			objParams[Ctr] = (Object) params.elementAt(Ctr);
		
//		pendingQuery.append(orderBy);
		
		Paginationhelper<FTPGroupsVb> paginationhelper = new Paginationhelper<FTPGroupsVb>(); 
		PaginationhelperMsSql<FTPGroupsVb> paginationhelperMsSql = new PaginationhelperMsSql<FTPGroupsVb>();
		if(dObj.isVerificationRequired() && dObj.getRecordIndicator() != 0){
			//Same set of parameters are needed for the Pending table query too
			//So add another set of similar values to the objects array
			for(Ctr2=0 ; Ctr2 < params.size(); Ctr2++, Ctr++)
				objParams[Ctr] = (Object) params.elementAt(Ctr2);
			if(whereNotExistsQuery != null && !whereNotExistsQuery.isEmpty() && approveQuery != null)
				CommonUtils.addToQuery(whereNotExistsQuery, approveQuery);
			String query = "";
			if(approveQuery == null || pendingQuery == null){
				if(approveQuery == null){
					query = pendingQuery.toString();
				}else{
					query = approveQuery.toString();
				}
			}else{
				query = approveQuery.toString() + " Union " + pendingQuery.toString();
			}
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				query = ValidationUtil.convertQuery(query, orderBy);
			}else {
				query = "SELECT * FROM ("+query+") A "+ orderBy;
			}
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				if(dObj.getTotalRows()  <= 0){
					result = paginationhelperMsSql.fetchPage(getJdbcTemplate(), query, 
							objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), rowMapper == null ? getMapper(): rowMapper);
					dObj.setTotalRows(paginationhelper.getTotalRows());
				}else{
					result = paginationhelperMsSql.fetchPage(getJdbcTemplate(), query, 
							objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), dObj.getTotalRows(), rowMapper == null ? getMapper(): rowMapper); 
				}
			}else {
				if(dObj.getTotalRows()  <= 0){
					result = paginationhelper.fetchPage(getJdbcTemplate(), query, 
							objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), rowMapper == null ? getMapper(): rowMapper);
					dObj.setTotalRows(paginationhelper.getTotalRows());
				}else{
					result = paginationhelper.fetchPage(getJdbcTemplate(), query, 
							objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), dObj.getTotalRows(), rowMapper == null ? getMapper(): rowMapper); 
				}				
			}
		}else{
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				if(dObj.getTotalRows()  <= 0){
					result = paginationhelperMsSql.fetchPage(getJdbcTemplate(), ValidationUtil.convertQuery(approveQuery.toString(), orderBy), 
							objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), rowMapper == null ? getMapper(): rowMapper);
					dObj.setTotalRows(paginationhelper.getTotalRows());
				}else{
					result = paginationhelperMsSql.fetchPage(getJdbcTemplate(), ValidationUtil.convertQuery(approveQuery.toString(), orderBy), objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), dObj.getTotalRows(), rowMapper == null ? getMapper(): rowMapper);
				}				
			}else {
				if(dObj.getTotalRows()  <= 0){
					result = paginationhelper.fetchPage(getJdbcTemplate(), approveQuery.toString()+orderBy, 
							objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), rowMapper == null ? getMapper(): rowMapper);
					dObj.setTotalRows(paginationhelper.getTotalRows());
				}else{
					result = paginationhelper.fetchPage(getJdbcTemplate(), approveQuery.toString()+orderBy, objParams, dObj.getCurrentPage(), dObj.getMaxRecords(), dObj.getTotalRows(), rowMapper == null ? getMapper(): rowMapper);
				}				
			}
		}
		return result;
	}
	
	public List<FtpMethodsVb> getQueryResultMethods(FTPGroupsVb dObj) {
		List<FtpMethodsVb> collTemp = null;

		String strBufApprove = new String("SELECT\n" + 
				"COUNTRY,\n" + 
				"LE_BOOK,\n" + 
				"FTP_SUB_GROUP_ID,\n" + 
				"REPRICING_FLAG_AT,\n" + 
				"REPRICING_FLAG,(SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = REPRICING_FLAG_AT AND ALPHA_SUB_TAB = REPRICING_FLAG ) REPRICING_FLAG_DESC,\n" + 
				"METHOD_TYPE_AT,\n" + 
				"METHOD_TYPE,(SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB = METHOD_TYPE )METHOD_TYPE_DESC,\n" + 
				"TENOR_TYPE_NT,\n" + 
				"FTP_TENOR_TYPE,(SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = FTP_TENOR_TYPE) FTP_TENOR_TYPE_DESC,\n" + 
				"METHOD_BAL_TYPE_NT,\n" + 
				"METHOD_BAL_TYPE,(SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = METHOD_BAL_TYPE_NT AND NUM_SUB_TAB = METHOD_BAL_TYPE ) METHOD_BAL_TYPE_DESC,\n" + 
				"INTEREST_BASIS_NT,\n" + 
				"INTEREST_BASIS,(SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = INTEREST_BASIS_NT AND NUM_SUB_TAB = INTEREST_BASIS) INTEREST_BASIS_DESC,\n" + 
				"APPLY_RATE_NT,\n" + 
				"FTP_APPLY_RATE,(SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = FTP_APPLY_RATE ) FTP_APPLY_RATE_DESC ,\n" + 
				"FTP_CURVE_ID,\n" + 
				"FTP_MT_STATUS_NT,\n" + 
				"FTP_MT_STATUS,\n" + 
				"RECORD_INDICATOR_NT,\n" + 
				"RECORD_INDICATOR,\n" + 
				"MAKER,\n" + 
				"VERIFIER,\n" + 
				"INTERNAL_STATUS,\n" + 
				"DATE_LAST_MODIFIED,\n" + 
				"DATE_CREATION \n" + 
				" FROM FTP_METHODS WHERE FTP_MT_STATUS = 0 and COUNTRY = ? and LE_BOOK = ? and FTP_SUB_GROUP_ID = ? ");

//		if (ValidationUtil.isValid(dObj.getRepricingFlag())) {
//			strBufApprove = new String(
//					"SELECT METHOD_TYPE, METHOD_REFERENCE, METHOD_DESCRIPTION,(SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB = METHOD_TYPE )METHOD_TYPE_DESC "
//							+ ", METHOD_SUB_TYPE, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_SUB_TYPE_AT AND ALPHA_SUB_TAB = METHOD_SUB_TYPE ) METHOD_SUB_TYPE_DESC , "
//							+ "REPRICING_FLAG, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = REPRICING_FLAG_AT AND ALPHA_SUB_TAB = REPRICING_FLAG ) REPRICING_FLAG_DESC, "
//							+ "FTP_TENOR_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = FTP_TENOR_TYPE) FTP_TENOR_TYPE_DESC,"
//							+ "LP_TENOR_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = LP_TENOR_TYPE) LP_TENOR_TYPE_DESC,"
//							+ "METHOD_BAL_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = METHOD_BAL_TYPE_NT AND NUM_SUB_TAB = METHOD_BAL_TYPE ) METHOD_BAL_TYPE_DESC,"
//							+ "INTEREST_BASIS, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = INTEREST_BASIS_NT AND NUM_SUB_TAB = INTEREST_BASIS) INTEREST_BASIS_DESC,"
//							+ "FTP_APPLY_RATE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = FTP_APPLY_RATE ) FTP_APPLY_RATE_DESC ,"
//							+ "ADDON_APPLY_RATE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = ADDON_APPLY_RATE ) ADDON_APPLY_RATE_DESC ,"
//							+ "LP_APPLY_RATE,  (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB =  APPLY_RATE_NT AND NUM_SUB_TAB = LP_APPLY_RATE )LP_APPLY_RATE_DESC ,"
//							+ "FTP_CURVE_ID,FTP_MT_STATUS,RECORD_INDICATOR FROM FTP_METHODS WHERE METHOD_REFERENCE = ? AND FTP_MT_STATUS = 0 and REPRICING_FLAG='"
//							+ dObj.getRepricingFlag() + "'");
//		}
		final int intKeyFieldsCount = 3;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getMethodReference();

		try {
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getMapperMethods());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	
	public List<FTPGroupsVb> getQueryResults(FTPGroupsVb dObj, int intStatus){
		setServiceDefaults();
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 5;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.DATA_SOURCE_AT"
			+ ",TAppr.DATA_SOURCE,"+DataSourceAtApprDesc
			+ ",TAppr.FTP_GROUP_AT"
			+ ",TAppr.FTP_GROUP, "+FtpGroupAtApprDesc
			+ ",TAppr.FTP_SUB_GROUP_ID"
			+ ",TAppr.FTP_SUB_GROUP_PRIORITY"
			+ ",TAppr.FTP_SUB_GROUP_DESC"
			+ ",TAppr.FTP_DEFAULT_FLAG"
			+ ",TAppr.FTP_GRP_STATUS_NT"
			+ ",TAppr.FTP_GRP_STATUS,"+StatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_GROUPS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?  AND FTP_SUB_GROUP_ID = ?  ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.DATA_SOURCE_AT"
			+ ",TPend.DATA_SOURCE, "+DataSourceAtPendDesc
			+ ",TPend.FTP_GROUP_AT"
			+ ",TPend.FTP_GROUP, "+FtpGroupAtPendDesc
			+ ",TPend.FTP_SUB_GROUP_ID"
			+ ",TPend.FTP_SUB_GROUP_PRIORITY"
			+ ",TPend.FTP_SUB_GROUP_DESC"
			+ ",TPend.FTP_DEFAULT_FLAG"
			+ ",TPend.FTP_GRP_STATUS_NT"
			+ ",TPend.FTP_GRP_STATUS,"+StatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_GROUPS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?  AND FTP_SUB_GROUP_ID = ?   ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getDataSource();
		objParams[3] = dObj.getFtpGroup();
		objParams[4] = dObj.getFtpSubGroupId();

		try{
			if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0){
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strQueryAppr.toString(),objParams,getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),objParams,getMapper());
			}
			return collTemp;
		}catch(Exception ex){
				ex.printStackTrace();
				logger.error("Error: getQueryResults Exception :   ");
				if(intStatus == 0)
					logger.error(((strQueryAppr == null) ? "strQueryAppr is Null" : strQueryAppr.toString()));
				else
					logger.error(((strQueryPend == null) ? "strQueryPend is Null" : strQueryPend.toString()));

				if (objParams != null)
				for(int i=0 ; i< objParams.length; i++)
					logger.error("objParams[" + i + "]" + objParams[i].toString());
				return null;
				}
	}
	
	public List<FTPGroupsVb> getQueryAllSequences(FTPGroupsVb dObj, int intStatus){
		setServiceDefaults();
		StringBuffer strQueryAppr = new StringBuffer("Select DISTINCT "
			+ "TAppr.FTP_SUB_GROUP_ID, "
			+ "TAppr.FTP_SUB_GROUP_PRIORITY"+
			" From FTP_GROUPS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?");
		StringBuffer strQueryPend = new StringBuffer("Select DISTINCT "
			+ "TPend.FTP_SUB_GROUP_ID, "
			+ " TPend.FTP_SUB_GROUP_PRIORITY"+
			" From FTP_GROUPS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?");
		
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_GROUPS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.DATA_SOURCE = TPend.DATA_SOURCE AND TAppr.FTP_GROUP = TPend.FTP_GROUP AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID )");
		Vector<Object> params = new Vector<Object>();
		final int intKeyFieldsCount = 4;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getDataSource();
		objParams[3] = dObj.getFtpGroup();
		
		params.addElement(dObj.getCountry());
		params.addElement(dObj.getLeBook());
		params.addElement(dObj.getDataSource());
		params.addElement(dObj.getFtpGroup());
		try{
			
			RowMapper mapper = new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					FTPGroupsVb vObject = new FTPGroupsVb();
					vObject.setFtpSubGroupId(rs.getString("FTP_SUB_GROUP_ID"));
					vObject.setFtpSubGroupPriority(rs.getInt("FTP_SUB_GROUP_PRIORITY"));
					return vObject;
				}
			};
			String orderBy=" Order By FTP_SUB_GROUP_PRIORITY ";
			return getQueryPopupResults(dObj, strQueryPend, strQueryAppr, strWhereNotExists, orderBy, params, mapper);
		}catch(Exception ex){
				ex.printStackTrace();
				logger.error("Error: getQueryResults Exception :   ");
				if(intStatus == 0)
					logger.error(((strQueryAppr == null) ? "strQueryAppr is Null" : strQueryAppr.toString()));
				else
					logger.error(((strQueryPend == null) ? "strQueryPend is Null" : strQueryPend.toString()));

				if (objParams != null)
				for(int i=0 ; i< objParams.length; i++)
					logger.error("objParams[" + i + "]" + objParams[i].toString());
				return null;
				}
	}
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb vObject = new FTPGroupsVb();
				if(rs.getString("COUNTRY")!= null){ 
					vObject.setCountry(rs.getString("COUNTRY"));
				}else{
					vObject.setCountry("");
				}
				if(rs.getString("LE_BOOK")!= null){ 
					vObject.setLeBook(rs.getString("LE_BOOK"));
				}else{
					vObject.setLeBook("");
				}
				vObject.setDataSourceAt(rs.getInt("DATA_SOURCE_AT"));
				if(rs.getString("DATA_SOURCE")!= null){ 
					vObject.setDataSource(rs.getString("DATA_SOURCE"));
				}else{
					vObject.setDataSource("");
				}
				if(rs.getString("DATA_SOURCE_DESC")!= null){ 
					vObject.setDataSourceDescription(rs.getString("DATA_SOURCE_DESC"));
				}
				vObject.setFtpGroupAt(rs.getInt("FTP_GROUP_AT"));
				if(rs.getString("FTP_GROUP")!= null){ 
					vObject.setFtpGroup(rs.getString("FTP_GROUP"));
				}else{
					vObject.setFtpGroup("");
				}
				if(rs.getString("FTP_GROUP_DESC")!= null){ 
					vObject.setFtpGroupDesc(rs.getString("FTP_GROUP_DESC"));
				}
				if(rs.getString("FTP_SUB_GROUP_ID")!= null){ 
					vObject.setFtpSubGroupId(rs.getString("FTP_SUB_GROUP_ID"));
				}else{
					vObject.setFtpSubGroupId("");
				}
				vObject.setFtpSubGroupPriority(rs.getInt("FTP_SUB_GROUP_PRIORITY"));
				if(rs.getString("FTP_SUB_GROUP_DESC")!= null){ 
					vObject.setFtpSubGroupDesc(rs.getString("FTP_SUB_GROUP_DESC"));
				}else{
					vObject.setFtpSubGroupDesc("");
				}
				if(rs.getString("FTP_DEFAULT_FLAG")!= null){ 
					vObject.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
				}else{
					vObject.setDefaultGroup("");
				}
				vObject.setFtpGroupStatusNt(rs.getInt("FTP_GRP_STATUS_NT"));
				vObject.setFtpGroupStatus(rs.getInt("FTP_GRP_STATUS"));
				
				vObject.setStatusDesc(rs.getString("FTP_GRP_STATUS_DESC"));
				
				vObject.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				vObject.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				vObject.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				vObject.setMaker(rs.getInt("MAKER"));
				if(ValidationUtil.isValid(rs.getString("MAKER_NAME")))
					vObject.setMakerName(rs.getString("MAKER_NAME"));
				vObject.setVerifier(rs.getInt("VERIFIER"));
				if(ValidationUtil.isValid(rs.getString("VERIFIER_NAME")))
					vObject.setVerifierName(rs.getString("VERIFIER_NAME"));
				vObject.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				if(rs.getString("DATE_LAST_MODIFIED")!= null){ 
					vObject.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				}else{
					vObject.setDateLastModified("");
				}
				if(rs.getString("DATE_CREATION")!= null){ 
					vObject.setDateCreation(rs.getString("DATE_CREATION"));
				}else{
					vObject.setDateCreation("");
				}
				return vObject;
			}
		};
		return mapper;
	}
	protected RowMapper getMapperMethods() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpMethodsVb ftpMethodsVb = new FtpMethodsVb();
				ftpMethodsVb = new FtpMethodsVb();
				ftpMethodsVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpMethodsVb.setMethodType(rs.getString("METHOD_TYPE"));
				ftpMethodsVb.setMethodTypeDesc(rs.getString("METHOD_TYPE_DESC"));
				ftpMethodsVb.setFtpTenorType(rs.getInt("FTP_TENOR_TYPE"));
				ftpMethodsVb.setFtpTenorTypeDesc(rs.getString("FTP_TENOR_TYPE_DESC"));
				ftpMethodsVb.setMethodBalType(rs.getInt("METHOD_BAL_TYPE"));
				ftpMethodsVb.setMethodBalTypeDesc(rs.getString("METHOD_BAL_TYPE_DESC"));
				ftpMethodsVb.setRepricingFlag(rs.getString("REPRICING_FLAG"));
				ftpMethodsVb.setRepricingFlagDesc(rs.getString("REPRICING_FLAG_DESC"));
				ftpMethodsVb.setFtpMtStatus(rs.getInt("FTP_MT_STATUS"));
				ftpMethodsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));

				ftpMethodsVb.setInterestBasis(rs.getInt("INTEREST_BASIS"));
				ftpMethodsVb.setFtpApplyRate(rs.getInt("FTP_APPLY_RATE"));

				ftpMethodsVb.setInterestBasisDesc(rs.getString("INTEREST_BASIS_DESC"));
				ftpMethodsVb.setFtpApplyRateDesc(rs.getString("FTP_APPLY_RATE_DESC"));
				return ftpMethodsVb;
			}
		};
		return mapper;
	}

	@Override
	protected List<FTPGroupsVb> doSelectPendingRecord(FTPGroupsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}
	@Override
	protected ExceptionCode doInsertrOrUpdateApprRecordForNonTrans(FTPGroupsVb vObject) throws RuntimeCustomException {
		List<FTPGroupsVb> collTemp = null;
		FTPGroupsVb vObjectlocal = null;
		ExceptionCode exceptionCode = null;
		strErrorDesc = "";
		strApproveOperation = Constants.MODIFY;
		strCurrentOperation = Constants.MODIFY;
		
		if(ValidationUtil.isValid(vObject.getActionType())) {
			strApproveOperation = vObject.getActionType();
			strCurrentOperation = vObject.getActionType();
		}
		
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		if ("RUNNING".equalsIgnoreCase(getBuildStatus(vObject))) {
			exceptionCode = getResultObject(Constants.BUILD_IS_RUNNING);
			throw buildRuntimeCustomException(exceptionCode);
		}
		/*
		 * if (getQueryResultsGroup(vObject) == 0){ exceptionCode =
		 * getResultObject(Constants.ERRONEOUS_OPERATION); throw
		 * buildRuntimeCustomException(exceptionCode); }
		 */
		// Even if record is not there in Appr. table reject the record
		collTemp = getQueryResults(vObject, 0);
		if (collTemp == null || collTemp.size() == 0) {
			/*
			 * exceptionCode =
			 * getResultObject(Constants.ATTEMPT_TO_MODIFY_UNEXISTING_RECORD); throw
			 * buildRuntimeCustomException(exceptionCode);
			 */
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
			vObject.setVerifier(getIntCurrentUserId());
			retVal = doInsertionGroups(vObject);
			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			}
			
		}else {
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
			vObject.setVerifier(getIntCurrentUserId());
//			retVal = doUpdateDefaultFlag(vObject);
			retVal = doUpdateAppr(vObject);
			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			}
			String systemDate = getSystemDate();
			vObject.setDateLastModified(systemDate);
		}
		// exceptionCode = writeAuditLog(vObject, vObjectlocal);
		/*
		 * if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION){
		 * exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR); throw
		 * buildRuntimeCustomException(exceptionCode); }
		 */
		
		// Source Configuration Add / Modify 
		for (FTPSourceConfigVb sourceConfigVb  : vObject.getFtpSourceConfigList() ) {
			sourceConfigVb.setVerificationRequired(vObject.isVerificationRequired());
			sourceConfigVb.setStaticDelete(vObject.isStaticDelete());
			exceptionCode = ftpSourceConfigDao.doInsertrOrUpdateApprRecordForNonTrans(sourceConfigVb);
			if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				return exceptionCode;
			}
		}

		// Method Configuration Add / Modify
		for (FtpMethodsVb methodsVb  : vObject.getFtpMethodsList() ) {
			methodsVb.setVerificationRequired(vObject.isVerificationRequired());
			methodsVb.setStaticDelete(vObject.isStaticDelete());
			exceptionCode = ftpMethodsDao.doInsertrOrUpdateApprRecordForNonTrans(methodsVb);
			if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				return exceptionCode;
			}
		}
		
		exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
		return exceptionCode;
	}
	
	public ExceptionCode doUpdateRecordForNonTrans(FTPGroupsVb vObject) throws RuntimeCustomException {
		List<FTPGroupsVb> collTemp = null;
		FTPGroupsVb vObjectlocal = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.MODIFY;
		strErrorDesc = "";
		strCurrentOperation = Constants.MODIFY;
		
		if(ValidationUtil.isValid(vObject.getActionType())) {
			strApproveOperation = vObject.getActionType();
			strCurrentOperation = vObject.getActionType();
		}
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		// Search if record already exists in pending. If it already exists, check for
		// status
		collTemp = doSelectPendingRecord(vObject);
		if (collTemp == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		if (collTemp.size() > 0) {
			vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);

			// Check if the record is pending for deletion. If so return the error
			if (vObjectlocal.getRecordIndicator() == Constants.STATUS_DELETE) {
				exceptionCode = getResultObject(Constants.RECORD_PENDING_FOR_DELETION);
				throw buildRuntimeCustomException(exceptionCode);
			}
			vObject.setDateCreation(vObjectlocal.getDateCreation());
			if (vObjectlocal.getRecordIndicator() == Constants.STATUS_INSERT) {
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_INSERT);
				retVal = doUpdatePend(vObject);
			} else {
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_UPDATE);
				retVal = doUpdatePend(vObject);
			}
			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			}
		} else {
			collTemp = null;
			collTemp = selectApprovedRecord(vObject);

			if (collTemp == null) {
				exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
				throw buildRuntimeCustomException(exceptionCode);
			}

			// Even if record is not there in Appr. table reject the record
			if (collTemp.size() == 0) {
//				exceptionCode = getResultObject(Constants.ATTEMPT_TO_MODIFY_UNEXISTING_RECORD);
//				throw buildRuntimeCustomException(exceptionCode);
				// Try inserting the record
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_INSERT);
				String systemDate = getSystemDate();
				vObject.setDateLastModified(systemDate);
				vObject.setDateCreation(systemDate);
				retVal = doInsertionPend(vObject);
				if (retVal != Constants.SUCCESSFUL_OPERATION) {
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}else {
				// This is required for Audit Trail.
				if (collTemp.size() > 0) {
					vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);
					vObject.setDateCreation(vObjectlocal.getDateCreation());
				}
				vObject.setDateCreation(vObjectlocal.getDateCreation());
				// Record is there in approved, but not in pending. So add it to pending
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_UPDATE);
				retVal = doInsertionPendWithDc(vObject);
				if (retVal != Constants.SUCCESSFUL_OPERATION) {
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
				}				
			}
			
		}
//		Source Configuration Add / Modify 
		for (FTPSourceConfigVb sourceConfigVb  : vObject.getFtpSourceConfigList() ) {
			sourceConfigVb.setVerificationRequired(vObject.isVerificationRequired());
			sourceConfigVb.setStaticDelete(vObject.isStaticDelete());			
			exceptionCode = ftpSourceConfigDao.doUpdateRecordForNonTrans(sourceConfigVb);
			if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				return exceptionCode;
			}
		}
		
		// Method Configuration Add / Modify
		for (FtpMethodsVb methodsVb  : vObject.getFtpMethodsList() ) {
			methodsVb.setVerificationRequired(vObject.isVerificationRequired());
			methodsVb.setStaticDelete(vObject.isStaticDelete());
			exceptionCode = ftpMethodsDao.doUpdateRecordForNonTrans(methodsVb);
			if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				return exceptionCode;
			}
		}
		
		return getResultObject(Constants.SUCCESSFUL_OPERATION);
	}
	
	@SuppressWarnings("unchecked")
	public List<VcConfigMainColumnsVb> getQueryTreeColumnsDetails(VcConfigMainTreeVb vcTreeVb) throws DataAccessException {

		setServiceDefaults();
		
		String sql = "SELECT VCC.TABLE_NAME, VCC.COLUMN_NAME, VCC.COLUMN_NAME COLUMN_ALIAS_NAME, VCC.SORT_COLUMN, VCC.COL_DISPLAY_TYPE,   \n" + 
				"				 VCC.COL_ATTRIBUTE_TYPE, VCC.COL_EXPERSSION_TYPE, VCC.FORMAT_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB = 2012 AND NUM_SUB_TAB = VCC.FORMAT_TYPE) FORMAT_TYPE_DESC, \n" + 
				"				 (SELECT MACROVAR_DESC FROM MACROVAR_TAGGING WHERE MACROVAR_NAME = 'JAVA' AND TAG_NAME = VCC.FORMAT_TYPE)JAVA_DATE_FORMAT, VCC.MAG_ENABLE_FLAG, VCC.MAG_TYPE,   \n" + 
				"				 VCC.MAG_SELECTION_TYPE, VCC.TAB_COLUMN_STATUS, VCC.MAG_DEFAULT, VCC.MAG_QUERY_ID, VCC.MAG_DISPLAY_COLUMN,   \n" + 
				"				 VCC.MAG_USE_COLUMN ,\n" + 
				"				 VCC.COL_TYPE,  \n" + 
				"				 (select DISPLAY_NAME from MACROVAR_TAGGING where MACROVAR_NAME = 'databaseType' AND MACROVAR_TYPE = 'DATE_FORMAT' AND TAG_NAME = VCC.FORMAT_TYPE) DATE_FORMATTING_SYNTAX,  \n" + 
				"				 (select DISPLAY_NAME from MACROVAR_TAGGING where MACROVAR_NAME = 'databaseType' AND MACROVAR_TYPE = 'DATE_CONVERT' AND TAG_NAME = VCC.FORMAT_TYPE) DATE_CONVERSION_SYNTAX  \n" + 
				"				 FROM  SOURCE_TAB_COLUMN_MAPPINGS VCC   \n" + 
				"				 WHERE TAB_COLUMN_STATUS = 0 \n" + 
				"				  ORDER BY SORT_COLUMN";
		
		Object args[] = {vcTreeVb.getCatalogId(), vcTreeVb.getTableId()};
		
		return getJdbcTemplate().query(sql, new RowMapper<VcConfigMainColumnsVb>() {
			@Override
			public VcConfigMainColumnsVb mapRow(ResultSet rs, int rowNum) throws SQLException {
				VcConfigMainColumnsVb columnVb = new VcConfigMainColumnsVb();
//				columnVb.setCatalogId(rs.getString("CATALOG_ID"));
				columnVb.setTableId(rs.getString("TABLE_NAME"));
//				columnVb.setColId(rs.getString("COL_ID"));
				columnVb.setColName(rs.getString("COLUMN_NAME"));
				columnVb.setAliasName(ValidationUtil.isValid(rs.getString("COLUMN_ALIAS_NAME"))?rs.getString("COLUMN_ALIAS_NAME"):rs.getString("COLUMN_NAME"));
				columnVb.setSortColumn(rs.getString("SORT_COLUMN"));
				columnVb.setColDisplayType(rs.getString("COL_DISPLAY_TYPE"));
				columnVb.setColAttributeType(rs.getString("COL_ATTRIBUTE_TYPE"));
				columnVb.setColExperssionType(rs.getString("COL_EXPERSSION_TYPE"));
				columnVb.setFormatType(rs.getString("FORMAT_TYPE"));
				columnVb.setFormatTypeDesc(rs.getString("FORMAT_TYPE_DESC"));
				columnVb.setMagEnableFlag(rs.getString("MAG_ENABLE_FLAG"));
				columnVb.setMagType(rs.getInt("MAG_TYPE"));
				columnVb.setMagSelectionType(rs.getString("MAG_SELECTION_TYPE"));
				columnVb.setVccStatus(rs.getInt("TAB_COLUMN_STATUS"));
				columnVb.setMagDefault(rs.getString("MAG_DEFAULT"));
				columnVb.setMagQueryId(rs.getString("MAG_QUERY_ID"));
				columnVb.setMagDisplayColumn(rs.getString("MAG_DISPLAY_COLUMN"));
				columnVb.setMagUseColumn(rs.getString("MAG_USE_COLUMN"));
//				columnVb.setFolderIds(rs.getString("FOLDER_IDS"));
				columnVb.setJavaFormatDesc(rs.getString("JAVA_DATE_FORMAT"));
/*				if(ValidationUtil.isValid(rs.getString("EXPERSSION_TEXT"))){
					columnVb.setExperssionText(rs.getString("EXPERSSION_TEXT").replaceAll("#TABLE_ALIAS#", ValidationUtil.isValid(rs.getString("TABLE_ALIAS_NAME"))?rs.getString("TABLE_ALIAS_NAME"):rs.getString("TABLE_NAME")));
				}*/
//				columnVb.setExperssionText(rs.getString("EXPERSSION_TEXT"));
				columnVb.setColType(rs.getString("COL_TYPE"));
//				columnVb.setMaskingFlag(rs.getString("MASKING_FLAG"));
//				columnVb.setMaskingScript(rs.getString("MASKING_SCRIPT"));
				columnVb.setDateFormattingSyntax(rs.getString("DATE_FORMATTING_SYNTAX"));
				columnVb.setDateConversionSyntax(rs.getString("DATE_CONVERSION_SYNTAX"));
//				if(ValidationUtil.isValid(rs.getString("INCLUDE_GROUP_COL")))
//					columnVb.setIncludeGroupCol(rs.getString("INCLUDE_GROUP_COL").replaceAll("#TABLE_ALIAS#", ValidationUtil.isValid(rs.getString("TABLE_ALIAS_NAME"))?rs.getString("TABLE_ALIAS_NAME"):rs.getString("TABLE_NAME")));
				return columnVb;
			}
		});
	
	}

	public List<DCManualQueryVb> getSpecificManualQueryDetails(DCManualQueryVb dObj) throws DataAccessException {
		String sql = "SELECT QUERY_ID, QUERY_DESCRIPTION, DATABASE_TYPE_AT, DATABASE_TYPE, "+
				" DATABASE_CONNECTIVITY_DETAILS,LOOKUP_DATA_LOADING_AT, LOOKUP_DATA_LOADING, QUERY_VALID_FLAG, SQL_QUERY, "+
				" STG_QUERY1, STG_QUERY2, STG_QUERY3, POST_QUERY, VCQ_STATUS_NT, VCQ_STATUS, "+
				" RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER,INTERNAL_STATUS, "+
				 getDbFunction("DATEFUNC")+"(DATE_LAST_MODIFIED, '"+getDbFunction("DD_Mon_RRRR")+" "+getDbFunction("TIME")+"') DATE_LAST_MODIFIED, "+
				 getDbFunction("DATEFUNC")+"(DATE_CREATION, '"+getDbFunction("DD_Mon_RRRR")+" "+getDbFunction("TIME")+"') DATE_CREATION, "+
				" COLUMNS_METADATA, HASH_VARIABLE_SCRIPT,QUERY_CATEGORY FROM VC_QUERIES TAPPR WHERE QUERY_ID= ? ";
		List<Object> argumentList = new ArrayList<Object>();
		argumentList.add(dObj.getQueryId());
		if(ValidationUtil.isValid(dObj.getDatabaseType())){
			if("M_QUERY".equalsIgnoreCase(dObj.getDatabaseType())) {
				dObj.setDatabaseType("MACROVAR");
			}
			sql = sql +" AND DATABASE_TYPE = ? ";
			argumentList.add(dObj.getDatabaseType());
		}
		Object[] args = new Object[argumentList.size()];
		argumentList.toArray(args);
		try {
			return  getJdbcTemplate().query(sql,args, getMapperManualuery());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}	
	protected RowMapper getMapperManualuery(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				DCManualQueryVb vObj = new DCManualQueryVb();
				vObj.setQueryId(rs.getString("QUERY_ID"));
				vObj.setQueryDescription(rs.getString("QUERY_DESCRIPTION"));
				vObj.setDatabaseType(rs.getString("DATABASE_TYPE"));
				vObj.setDatabaseConnectivityDetails(rs.getString("DATABASE_CONNECTIVITY_DETAILS"));
				vObj.setLookupDataLoading(rs.getString("LOOKUP_DATA_LOADING"));
				vObj.setSqlQuery(ValidationUtil.isValid(rs.getString("SQL_QUERY"))?rs.getString("SQL_QUERY"):"");
				vObj.setStgQuery1(ValidationUtil.isValid(rs.getString("STG_QUERY1"))?rs.getString("STG_QUERY1"):"");
				vObj.setStgQuery2(ValidationUtil.isValid(rs.getString("STG_QUERY2"))?rs.getString("STG_QUERY2"):"");
				vObj.setStgQuery3(ValidationUtil.isValid(rs.getString("STG_QUERY3"))?rs.getString("STG_QUERY3"):"");
				vObj.setPostQuery(ValidationUtil.isValid(rs.getString("POST_QUERY"))?rs.getString("POST_QUERY"):"");
				vObj.setVcqStatusNt(rs.getInt("VCQ_STATUS_NT"));
				vObj.setVcqStatus(rs.getInt("VCQ_STATUS"));
				vObj.setDbStatus(rs.getInt("VCQ_STATUS"));
				vObj.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				vObj.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				vObj.setMaker(rs.getLong("MAKER"));
				vObj.setVerifier(rs.getLong("VERIFIER"));
				vObj.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				vObj.setDateCreation(rs.getString("DATE_CREATION"));
				vObj.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				vObj.setQueryColumnXML(ValidationUtil.isValid(rs.getString("COLUMNS_METADATA"))?rs.getString("COLUMNS_METADATA"):"");
				vObj.setHashVariableScript(ValidationUtil.isValid(rs.getString("HASH_VARIABLE_SCRIPT"))?rs.getString("HASH_VARIABLE_SCRIPT"):"");
				vObj.setQueryValidFlag(ValidationUtil.isValid(rs.getString("QUERY_VALID_FLAG"))?"TRUE":"FALSE");
				vObj.setQueryType(rs.getInt("QUERY_CATEGORY"));
//				fetchMakerVerifierNames(vObj);
				return vObj;
			}
		};
		return mapper;
	}

	protected ExceptionCode doDeleteApprRecordForNonTrans(FTPGroupsVb vObject) throws RuntimeCustomException {
		List<FTPGroupsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		strCurrentOperation = Constants.DELETE;
		setServiceDefaults();
		FTPGroupsVb vObjectlocal = null;
		vObject.setMaker(getIntCurrentUserId());
		collTemp = selectApprovedRecord(vObject);
		if (collTemp == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		// If record already exists in the approved table, reject the addition
		if (collTemp.size() > 0) {
			int intStaticDeletionFlag = getStatus(((ArrayList<FTPGroupsVb>) collTemp).get(0));
			if (intStaticDeletionFlag == Constants.PASSIVATE) {
				exceptionCode = getResultObject(Constants.CANNOT_DELETE_AN_INACTIVE_RECORD);
				throw buildRuntimeCustomException(exceptionCode);
			}
		} else {
			exceptionCode = getResultObject(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);
		vObject.setDateCreation(vObjectlocal.getDateCreation());
		if (vObject.isStaticDelete()) {
			vObjectlocal.setMaker(getIntCurrentUserId());
			vObject.setVerifier(getIntCurrentUserId());
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
//			setStatus(vObject, Constants.PASSIVATE);
			setStatus(vObjectlocal, Constants.PASSIVATE);
			vObjectlocal.setVerifier(getIntCurrentUserId());
			vObjectlocal.setRecordIndicator(Constants.STATUS_ZERO);
			retVal = doUpdateAppr(vObjectlocal);
			String systemDate = getSystemDate();
			vObject.setDateLastModified(systemDate);
		} else {
			// delete the record from the Approve Table
			retVal = doDeleteAppr(vObject);
//			vObject.setRecordIndicator(-1);
			String systemDate = getSystemDate();
			vObject.setDateLastModified(systemDate);

		}
		if (retVal != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		}
		if (vObject.isStaticDelete()) {
			setStatus(vObjectlocal, Constants.STATUS_ZERO);
			setStatus(vObject, Constants.PASSIVATE);
			exceptionCode = writeAuditLog(vObject, vObjectlocal);
		} else {
			exceptionCode = writeAuditLog(null, vObject);
			vObject.setRecordIndicator(-1);
		}
		if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR);
			throw buildRuntimeCustomException(exceptionCode);
		}
		
		
		List<FTPSourceConfigVb> sourceConfigList = ftpSourceConfigDao.getQueryResultsByParent(vObject, 0);
		if(sourceConfigList!=null && sourceConfigList.size()>0) {
			for(FTPSourceConfigVb configVb : sourceConfigList) {
				configVb.setVerificationRequired(vObject.isVerificationRequired());
				configVb.setStaticDelete(vObject.isStaticDelete());
				exceptionCode = ftpSourceConfigDao.doDeleteApprRecordForNonTrans(configVb);
				if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
					strErrorDesc = exceptionCode.getErrorMsg();
					exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}
		}
		List<FtpMethodsVb> ftpMethodsList = ftpMethodsDao.getQueryResultsByParent(vObject, 0);
		if(ftpMethodsList!=null && ftpMethodsList.size() > 0) {
			for(FtpMethodsVb methodsVb : ftpMethodsList) {
				methodsVb.setVerificationRequired(vObject.isVerificationRequired());
				methodsVb.setStaticDelete(vObject.isStaticDelete());
				exceptionCode = ftpMethodsDao.doDeleteApprRecordForNonTrans(methodsVb);
				if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
					strErrorDesc = exceptionCode.getErrorMsg();
					exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}
			for(FtpMethodsVb methodsVb : ftpMethodsList) {
				List<FTPCurveVb> ftpCurveList = ftpCurvesDao.getQueryResultsByParent(methodsVb, 0, true);
				if(ftpCurveList!=null && ftpCurveList.size()>0) {
					for(FTPCurveVb ftpCurveVb : ftpCurveList) {
						ftpCurveVb.setVerificationRequired(vObject.isVerificationRequired());
						ftpCurveVb.setStaticDelete(vObject.isStaticDelete());
						exceptionCode = ftpCurvesDao.doDeleteApprRecordForNonTrans(ftpCurveVb);
						if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							strErrorDesc = exceptionCode.getErrorMsg();
							exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
							throw buildRuntimeCustomException(exceptionCode);
						}
					}
				}
				List<FtpAddonVb> ftpAddOnList = ftpAddonDao.getQueryResultsByParent(methodsVb, 0, true);
				
				if(ftpAddOnList != null && ftpAddOnList.size() > 0) {
					for(FtpAddonVb ftpAddonVb : ftpAddOnList) {
						ftpAddonVb.setVerificationRequired(vObject.isVerificationRequired());
						ftpAddonVb.setStaticDelete(vObject.isStaticDelete());
						exceptionCode = ftpAddonDao.doDeleteApprRecordForNonTrans(ftpAddonVb);
						if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							strErrorDesc = exceptionCode.getErrorMsg();
							exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
							throw buildRuntimeCustomException(exceptionCode);
						}
					}
				}
				
				List<FtpPremiumsVb> ftpPremiumsList = ftpPremiumsDao.getQueryResultsByParent(methodsVb, 0, true);
				
				if(ftpPremiumsList != null && ftpPremiumsList.size() > 0 ) {
					for(FtpPremiumsVb ftpPremiumsVb : ftpPremiumsList) {
						ftpPremiumsVb.setVerificationRequired(vObject.isVerificationRequired());
						ftpPremiumsVb.setStaticDelete(vObject.isStaticDelete());
						exceptionCode = ftpPremiumsDao.doDeleteApprRecordForNonTrans(ftpPremiumsVb);
						if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							strErrorDesc = exceptionCode.getErrorMsg();
							exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
							throw buildRuntimeCustomException(exceptionCode);
						}
					}
				}
			}
		}
		return getResultObject(Constants.SUCCESSFUL_OPERATION);
	}
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doDeleteApprRecord(List<FTPGroupsVb> vObjects, FTPGroupsVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FTPGroupsVb vObject : vObjects) {
//				if (vObject.isChecked()) {
					vObject.setStaticDelete(vObjectParam.isStaticDelete());
					vObject.setVerificationRequired(vObjectParam.isVerificationRequired());
					exceptionCode = doDeleteApprRecordForNonTrans(vObject);
					if (exceptionCode == null || exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						throw buildRuntimeCustomException(exceptionCode);
					}
//				}
			}
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		} catch (RuntimeCustomException rcException) {
			throw rcException;
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Delete.", ex);
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doDeleteRecord(List<FTPGroupsVb> vObjects, FTPGroupsVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FTPGroupsVb vObject : vObjects) {
//				if (vObject.isChecked()) {
					vObject.setStaticDelete(vObjectParam.isStaticDelete());
					vObject.setVerificationRequired(vObjectParam.isVerificationRequired());
					exceptionCode = doDeleteRecordForNonTrans(vObject);
					if (exceptionCode == null || exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						throw buildRuntimeCustomException(exceptionCode);
					}
//				}
			}
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		} catch (RuntimeCustomException rcException) {
			throw rcException;
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Delete.", ex);
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	protected ExceptionCode doDeleteRecordForNonTrans(FTPGroupsVb vObject) throws RuntimeCustomException {
		FTPGroupsVb vObjectlocal = null;
		List<FTPGroupsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		strCurrentOperation = Constants.DELETE;
		setServiceDefaults();
		collTemp = selectApprovedRecord(vObject);

		if (collTemp == null) {
			logger.error("Collection is null");
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		// If record already exists in the approved table, reject the addition
		if (collTemp.size() > 0) {
			vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);
			int intStaticDeletionFlag = getStatus(vObjectlocal);
			if (intStaticDeletionFlag == Constants.PASSIVATE) {
				exceptionCode = getResultObject(Constants.CANNOT_DELETE_AN_INACTIVE_RECORD);
				throw buildRuntimeCustomException(exceptionCode);
			}
		} else {
			exceptionCode = getResultObject(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
			throw buildRuntimeCustomException(exceptionCode);
		}

		// check to see if the record already exists in the pending table
		collTemp = doSelectPendingRecord(vObject);
		if (collTemp == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}

		// If records are there, check for the status and decide what error to return
		// back
		if (collTemp.size() > 0) {
			exceptionCode = getResultObject(Constants.TRYING_TO_DELETE_APPROVAL_PENDING_RECORD);
			throw buildRuntimeCustomException(exceptionCode);
		}

		// insert the record into pending table with status 3 - deletion
		if (vObjectlocal == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		if (collTemp.size() > 0) {
			vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);
			vObjectlocal.setDateCreation(vObject.getDateCreation());
		}
		// vObjectlocal.setDateCreation(vObject.getDateCreation());
		vObjectlocal.setMaker(getIntCurrentUserId());
		vObjectlocal.setRecordIndicator(Constants.STATUS_DELETE);
		vObjectlocal.setVerifier(0);
		retVal = doInsertionPendWithDc(vObjectlocal);
		if (retVal != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObject.setRecordIndicator(Constants.STATUS_DELETE);
		vObject.setVerifier(0);
		
		
		List<FTPSourceConfigVb> sourceConfigList = ftpSourceConfigDao.getQueryResultsByParent(vObject, 0);
		if(sourceConfigList!=null && sourceConfigList.size()>0) {
			for(FTPSourceConfigVb configVb : sourceConfigList) {
				configVb.setVerificationRequired(vObject.isVerificationRequired());
				configVb.setStaticDelete(vObject.isStaticDelete());
				exceptionCode = ftpSourceConfigDao.doDeleteRecordForNonTrans(configVb);
				if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
					strErrorDesc = exceptionCode.getErrorMsg();
					exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}
		}
		List<FtpMethodsVb> ftpMethodsList = ftpMethodsDao.getQueryResultsByParent(vObject, 0);
		if(ftpMethodsList!=null && ftpMethodsList.size() > 0) {
			for(FtpMethodsVb methodsVb : ftpMethodsList) {
				methodsVb.setVerificationRequired(vObject.isVerificationRequired());
				methodsVb.setStaticDelete(vObject.isStaticDelete());
				exceptionCode = ftpMethodsDao.doDeleteRecordForNonTrans(methodsVb);
				if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
					strErrorDesc = exceptionCode.getErrorMsg();
					exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}
			for(FtpMethodsVb methodsVb : ftpMethodsList) {
				List<FTPCurveVb> ftpCurveList = ftpCurvesDao.getQueryResultsByParent(methodsVb, 0, true);
				if(ftpCurveList!=null && ftpCurveList.size()>0) {
					for(FTPCurveVb ftpCurveVb : ftpCurveList) {
						ftpCurveVb.setVerificationRequired(vObject.isVerificationRequired());
						ftpCurveVb.setStaticDelete(vObject.isStaticDelete());
						exceptionCode = ftpCurvesDao.doDeleteRecordForNonTrans(ftpCurveVb);
						if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							strErrorDesc = exceptionCode.getErrorMsg();
							exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
							throw buildRuntimeCustomException(exceptionCode);
						}
					}
				}
				List<FtpAddonVb> ftpAddOnList = ftpAddonDao.getQueryResultsByParent(methodsVb, 0, true);
				
				if(ftpAddOnList != null && ftpAddOnList.size() > 0) {
					for(FtpAddonVb ftpAddonVb : ftpAddOnList) {
						ftpAddonVb.setVerificationRequired(vObject.isVerificationRequired());
						ftpAddonVb.setStaticDelete(vObject.isStaticDelete());
						exceptionCode = ftpAddonDao.doDeleteRecordForNonTrans(ftpAddonVb);
						if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							strErrorDesc = exceptionCode.getErrorMsg();
							exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
							throw buildRuntimeCustomException(exceptionCode);
						}
					}
				}
				
				List<FtpPremiumsVb> ftpPremiumsList = ftpPremiumsDao.getQueryResultsByParent(methodsVb, 0, true);
				
				if(ftpPremiumsList != null && ftpPremiumsList.size() > 0 ) {
					for(FtpPremiumsVb ftpPremiumsVb : ftpPremiumsList) {
						ftpPremiumsVb.setVerificationRequired(vObject.isVerificationRequired());
						ftpPremiumsVb.setStaticDelete(vObject.isStaticDelete());
						exceptionCode = ftpPremiumsDao.doDeleteRecordForNonTrans(ftpPremiumsVb);
						if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							strErrorDesc = exceptionCode.getErrorMsg();
							exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
							throw buildRuntimeCustomException(exceptionCode);
						}
					}
				}
			}
		}
		
		return getResultObject(Constants.SUCCESSFUL_OPERATION);
	}
	
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doBulkReject(List<FTPGroupsVb> vObjects, FTPGroupsVb vObject) throws RuntimeCustomException {

		strErrorDesc = "";
		strCurrentOperation = Constants.REJECT;
		setServiceDefaults();
		ExceptionCode exceptionCode = null;
		try {
			boolean foundFlag = false;
			for (FTPGroupsVb object : vObjects) {
//				if (object.getRecordIndicator() > 0 && object.isChecked()) {
				if (object.getRecordIndicator() > 0) {
					foundFlag = true;
					object.setVerificationRequired(vObject.isVerificationRequired());
					strErrorDesc = frameErrorMessage(object, Constants.REJECT);
					exceptionCode = doRejectRecord(object);
					if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						int index = exceptionCode.getErrorMsg().trim().indexOf(serviceDesc + " - Reject - Failed -");
						if (index >= 0) {
							strErrorDesc += " " + exceptionCode.getErrorMsg().replaceFirst(serviceDesc + " - Reject - Failed -", "-");
						} else {
							strErrorDesc += " " + exceptionCode.getErrorMsg();
						}
						break;
					}
				}
			}
			if (foundFlag == false) {
				logger.error("No Records To Reject");
				exceptionCode = getResultObject(Constants.NO_RECORDS_TO_REJECT);
				throw buildRuntimeCustomException(exceptionCode);
			}

			// When it has come out of the loop, check whether it has exited successfully or
			// with error
			if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				logger.error("Error in Bulk Reject. " + exceptionCode.getErrorMsg());
				exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
				throw buildRuntimeCustomException(exceptionCode);
			}
			// return code to indicate successful operation
			return exceptionCode;
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Bulk Reject.", ex);
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	
	public ExceptionCode doRejectRecord(FTPGroupsVb vObject) throws RuntimeCustomException {
		FTPGroupsVb vObjectlocal = null;
		List<FTPGroupsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		setServiceDefaults();
		strErrorDesc = "";
		strCurrentOperation = Constants.REJECT;
		vObject.setMaker(getIntCurrentUserId());
		try {
			if (vObject.getRecordIndicator() == 1 || vObject.getRecordIndicator() == 3)
				vObject.setRecordIndicator(0);
			else
				vObject.setRecordIndicator(-1);
			// See if such a pending request exists in the pending table
			collTemp = doSelectPendingRecord(vObject);
			if (collTemp == null) {
				exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
				throw buildRuntimeCustomException(exceptionCode);
			}
			if (collTemp.size() == 0) {
				exceptionCode = getResultObject(Constants.NO_SUCH_PENDING_RECORD);
				throw buildRuntimeCustomException(exceptionCode);
			}
			vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);
			// Delete the record from the Pending table
			if (deletePendingRecord(vObjectlocal) == 0) {
				exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
				throw buildRuntimeCustomException(exceptionCode);
			}
			
			List<FTPSourceConfigVb> sourceConfigList = ftpSourceConfigDao.getQueryResultsByParent(vObject, 1);
			if(sourceConfigList!=null && sourceConfigList.size()>0) {
				for(FTPSourceConfigVb configVb : sourceConfigList) {
					configVb.setVerificationRequired(vObject.isVerificationRequired());
					configVb.setStaticDelete(vObject.isStaticDelete());
					exceptionCode = ftpSourceConfigDao.doRejectForTransaction(configVb);
					if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						strErrorDesc = exceptionCode.getErrorMsg();
						exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
						throw buildRuntimeCustomException(exceptionCode);
					}
				}
			}
			List<FtpMethodsVb> ftpMethodsList = ftpMethodsDao.getQueryResultsByParent(vObject, 1);
			if(ftpMethodsList!=null && ftpMethodsList.size() > 0) {
				for(FtpMethodsVb methodsVb : ftpMethodsList) {
					methodsVb.setVerificationRequired(vObject.isVerificationRequired());
					methodsVb.setStaticDelete(vObject.isStaticDelete());
					exceptionCode = ftpMethodsDao.doRejectForTransaction(methodsVb);
					if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						strErrorDesc = exceptionCode.getErrorMsg();
						exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
						throw buildRuntimeCustomException(exceptionCode);
					}
				}
				for(FtpMethodsVb methodsVb : ftpMethodsList) {
					List<FTPCurveVb> ftpCurveList = ftpCurvesDao.getQueryResultsByParent(methodsVb, 1, false);
					if(ftpCurveList!=null && ftpCurveList.size()>0) {
						for(FTPCurveVb ftpCurveVb : ftpCurveList) {
							ftpCurveVb.setVerificationRequired(vObject.isVerificationRequired());
							ftpCurveVb.setStaticDelete(vObject.isStaticDelete());
							exceptionCode = ftpCurvesDao.doRejectForTransaction(ftpCurveVb);
							if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
								strErrorDesc = exceptionCode.getErrorMsg();
								exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
								throw buildRuntimeCustomException(exceptionCode);
							}
						}
					}
					List<FtpAddonVb> ftpAddOnList = ftpAddonDao.getQueryResultsByParent(methodsVb, 1, false);
					
					if(ftpAddOnList != null && ftpAddOnList.size() > 0) {
						for(FtpAddonVb ftpAddonVb : ftpAddOnList) {
							ftpAddonVb.setVerificationRequired(vObject.isVerificationRequired());
							ftpAddonVb.setStaticDelete(vObject.isStaticDelete());
							exceptionCode = ftpAddonDao.doRejectForTransaction(ftpAddonVb);
							if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
								strErrorDesc = exceptionCode.getErrorMsg();
								exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
								throw buildRuntimeCustomException(exceptionCode);
							}
						}
					}
					
					List<FtpPremiumsVb> ftpPremiumsList = ftpPremiumsDao.getQueryResultsByParent(methodsVb, 1, false);
					
					if(ftpPremiumsList != null && ftpPremiumsList.size() > 0 ) {
						for(FtpPremiumsVb ftpPremiumsVb : ftpPremiumsList) {
							ftpPremiumsVb.setVerificationRequired(vObject.isVerificationRequired());
							ftpPremiumsVb.setStaticDelete(vObject.isStaticDelete());
							exceptionCode = ftpPremiumsDao.doRejectForTransaction(ftpPremiumsVb);
							if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
								strErrorDesc = exceptionCode.getErrorMsg();
								exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
								throw buildRuntimeCustomException(exceptionCode);
							}
						}
					}
				}
			}
			
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Reject.", ex);
			logger.error(((vObject == null) ? "vObject is Null" : vObject.toString()));
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode bulkApprove(List<FTPGroupsVb> vObjects,  FTPGroupsVb vObject) throws RuntimeCustomException {
		strErrorDesc = "";
		strCurrentOperation = Constants.APPROVE;
		ExceptionCode exceptionCode = null;
		setServiceDefaults();
		try {
			boolean foundFlag = false;
			for (FTPGroupsVb object : vObjects) {
				if (object.getRecordIndicator() > 0) {
					foundFlag = true;
					object.setVerificationRequired(vObject.isVerificationRequired());
					strErrorDesc = frameErrorMessage(object, Constants.APPROVE);
					exceptionCode = doApproveRecord(object, vObject.isStaticDelete());
					if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						int index = exceptionCode.getErrorMsg().trim().indexOf(serviceDesc + " - Approve - Failed -");
						if (index >= 0) {
							strErrorDesc += " " + exceptionCode.getErrorMsg().replaceFirst(serviceDesc + " - Approve - Failed -", "-");
						} else {
							strErrorDesc += " " + exceptionCode.getErrorMsg();
						}
						break;
					}
				}
			}
			if (foundFlag == false) {
				logger.error("No Records To Approve");
				exceptionCode = getResultObject(Constants.NO_RECORDS_TO_APPROVE);
				throw buildRuntimeCustomException(exceptionCode);
			}
			// When it has come out of the loop, check whether it has exited successfully or
			// with error
			if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				logger.error("Error in Bulk Approve. " + exceptionCode.getErrorMsg());
				exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
				throw buildRuntimeCustomException(exceptionCode);
			}
			return exceptionCode;
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Bulk Approve.", ex);
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	public ExceptionCode doApproveRecord(FTPGroupsVb vObject, boolean staticDelete) throws RuntimeCustomException {
		FTPGroupsVb oldContents = null;
		FTPGroupsVb vObjectlocal = null;
		List<FTPGroupsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strCurrentOperation = Constants.APPROVE;
		setServiceDefaults();
		try {
			// See if such a pending request exists in the pending table
			vObject.setVerifier(getIntCurrentUserId());
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
			collTemp = doSelectPendingRecord(vObject);
			if (collTemp == null) {
				exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
				throw buildRuntimeCustomException(exceptionCode);
			}

			if (collTemp.size() == 0) {
				exceptionCode = getResultObject(Constants.NO_SUCH_PENDING_RECORD);
				throw buildRuntimeCustomException(exceptionCode);
			}

			vObjectlocal = ((ArrayList<FTPGroupsVb>) collTemp).get(0);

			if (vObjectlocal.getMaker() == getIntCurrentUserId()) {
				exceptionCode = getResultObject(Constants.MAKER_CANNOT_APPROVE);
				throw buildRuntimeCustomException(exceptionCode);
			}

			// If it's NOT addition, collect the existing record contents from the
			// Approved table and keep it aside, for writing audit information later.
			if (vObjectlocal.getRecordIndicator() != Constants.STATUS_INSERT) {
				collTemp = selectApprovedRecord(vObject);
				if (collTemp == null || collTemp.isEmpty()) {
					exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
					throw buildRuntimeCustomException(exceptionCode);
				}
				oldContents = ((ArrayList<FTPGroupsVb>) collTemp).get(0);
			}

			if (vObjectlocal.getRecordIndicator() == Constants.STATUS_INSERT) { // Add authorization
				// Write the contents of the Pending table record to the Approved table
				vObjectlocal.setRecordIndicator(Constants.STATUS_ZERO);
				vObjectlocal.setVerifier(getIntCurrentUserId());
				retVal = doInsertionGroups(vObjectlocal);
				if (retVal != Constants.SUCCESSFUL_OPERATION) {
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
				}

				String systemDate = getSystemDate();
				vObject.setDateLastModified(systemDate);
				vObject.setDateCreation(systemDate);
				strApproveOperation = Constants.ADD;
			} else if (vObjectlocal.getRecordIndicator() == Constants.STATUS_UPDATE) { // Modify authorization

				collTemp = selectApprovedRecord(vObject);
				if (collTemp == null || collTemp.isEmpty()) {
					exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
					throw buildRuntimeCustomException(exceptionCode);
				}

				// If record already exists in the approved table, reject the addition
				if (collTemp.size() > 0) {
					// retVal = doUpdateAppr(vObjectlocal, MISConstants.ACTIVATE);
					vObjectlocal.setVerifier(getIntCurrentUserId());
					vObjectlocal.setRecordIndicator(Constants.STATUS_ZERO);
					retVal = doUpdateAppr(vObjectlocal);
				}
				// Modify the existing contents of the record in Approved table
				if (retVal != Constants.SUCCESSFUL_OPERATION) {
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
				}
				String systemDate = getSystemDate();
				vObject.setDateLastModified(systemDate);
				// Set the current operation to write to audit log
				strApproveOperation = Constants.MODIFY;
			} else if (vObjectlocal.getRecordIndicator() == Constants.STATUS_DELETE) { // Delete authorization
				if (staticDelete) {
					// Update the existing record status in the Approved table to delete
					setStatus(vObjectlocal, Constants.PASSIVATE);
					vObjectlocal.setRecordIndicator(Constants.STATUS_ZERO);
					vObjectlocal.setVerifier(getIntCurrentUserId());
					retVal = doUpdateAppr(vObjectlocal);
					if (retVal != Constants.SUCCESSFUL_OPERATION) {
						exceptionCode = getResultObject(retVal);
						throw buildRuntimeCustomException(exceptionCode);
					}
					setStatus(vObject, Constants.PASSIVATE);
					String systemDate = getSystemDate();
					vObject.setDateLastModified(systemDate);

				} else {
					// Delete the existing record from the Approved table
					retVal = doDeleteAppr(vObjectlocal);
					if (retVal != Constants.SUCCESSFUL_OPERATION) {
						exceptionCode = getResultObject(retVal);
						throw buildRuntimeCustomException(exceptionCode);
					}
					String systemDate = getSystemDate();
					vObject.setDateLastModified(systemDate);
				}
				// Set the current operation to write to audit log
				strApproveOperation = Constants.DELETE;
			} else {
				exceptionCode = getResultObject(Constants.INVALID_STATUS_FLAG_IN_DATABASE);
				throw buildRuntimeCustomException(exceptionCode);
			}

			// Delete the record from the Pending table
			retVal = deletePendingRecord(vObjectlocal);

			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			}

			// Set the internal status to Approved
			vObject.setInternalStatus(0);
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
			if (vObjectlocal.getRecordIndicator() == Constants.STATUS_DELETE && !staticDelete) {
				exceptionCode = writeAuditLog(null, oldContents);
				vObject.setRecordIndicator(-1);
			} else
				exceptionCode = writeAuditLog(vObjectlocal, oldContents);

			if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR);
				throw buildRuntimeCustomException(exceptionCode);
			}
			
			List<FTPSourceConfigVb> sourceConfigList = ftpSourceConfigDao.getQueryResultsByParent(vObject, 1);
			if(sourceConfigList!=null && sourceConfigList.size()>0) {
				for(FTPSourceConfigVb configVb : sourceConfigList) {
					configVb.setVerificationRequired(vObject.isVerificationRequired());
					configVb.setStaticDelete(vObject.isStaticDelete());
					exceptionCode = ftpSourceConfigDao.doApproveForTransaction(configVb, staticDelete);
					if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						strErrorDesc = exceptionCode.getErrorMsg();
						exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
						throw buildRuntimeCustomException(exceptionCode);
					}
				}
			}
			List<FtpMethodsVb> ftpMethodsList = ftpMethodsDao.getQueryResultsByParent(vObject, 1);
			if(ftpMethodsList!=null && ftpMethodsList.size() > 0) {
				for(FtpMethodsVb methodsVb : ftpMethodsList) {
					methodsVb.setVerificationRequired(vObject.isVerificationRequired());
					methodsVb.setStaticDelete(vObject.isStaticDelete());
					exceptionCode = ftpMethodsDao.doApproveForTransaction(methodsVb, staticDelete);
					if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						strErrorDesc = exceptionCode.getErrorMsg();
						exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
						throw buildRuntimeCustomException(exceptionCode);
					}
				}
				for(FtpMethodsVb methodsVb : ftpMethodsList) {
					List<FTPCurveVb> ftpCurveList = ftpCurvesDao.getQueryResultsByParent(methodsVb, 1, false);
					if(ftpCurveList!=null && ftpCurveList.size()>0) {
						for(FTPCurveVb ftpCurveVb : ftpCurveList) {
							ftpCurveVb.setVerificationRequired(vObject.isVerificationRequired());
							ftpCurveVb.setStaticDelete(vObject.isStaticDelete());
							exceptionCode = ftpCurvesDao.doApproveForTransaction(ftpCurveVb, staticDelete);
							if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
								strErrorDesc = exceptionCode.getErrorMsg();
								exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
								throw buildRuntimeCustomException(exceptionCode);
							}
						}
					}
					List<FtpAddonVb> ftpAddOnList = ftpAddonDao.getQueryResultsByParent(methodsVb, 1, false);
					
					if(ftpAddOnList != null && ftpAddOnList.size() > 0) {
						for(FtpAddonVb ftpAddonVb : ftpAddOnList) {
							ftpAddonVb.setVerificationRequired(vObject.isVerificationRequired());
							ftpAddonVb.setStaticDelete(vObject.isStaticDelete());
							exceptionCode = ftpAddonDao.doApproveForTransaction(ftpAddonVb, staticDelete);
							if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
								strErrorDesc = exceptionCode.getErrorMsg();
								exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
								throw buildRuntimeCustomException(exceptionCode);
							}
						}
					}
					
					List<FtpPremiumsVb> ftpPremiumsList = ftpPremiumsDao.getQueryResultsByParent(methodsVb, 1, false);
					
					if(ftpPremiumsList != null && ftpPremiumsList.size() > 0 ) {
						for(FtpPremiumsVb ftpPremiumsVb : ftpPremiumsList) {
							ftpPremiumsVb.setVerificationRequired(vObject.isVerificationRequired());
							ftpPremiumsVb.setStaticDelete(vObject.isStaticDelete());
							exceptionCode = ftpPremiumsDao.doApproveForTransaction(ftpPremiumsVb, staticDelete);
							if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
								strErrorDesc = exceptionCode.getErrorMsg();
								exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
								throw buildRuntimeCustomException(exceptionCode);
							}
						}
					}
				}
			}
			
			
			
			
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Approve.", ex);
			logger.error(((vObject == null) ? "vObject is Null" : vObject.toString()));
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	
	
	
	
	
	
	
	
	public RowMapper getQueryPopupMapper() {
		RowMapper mapper = new RowMapper() {
			List<FTPGroupsVb> result = new ArrayList<FTPGroupsVb>();

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
				ftpSourceConfigVb.setFtpGroupDesc(rs.getString("FTP_GROUP_DESC"));
				return ftpSourceConfigVb;
			}

		};
		return mapper;
	}
	
	public RowMapper getQueryPopupMapperDetails() {
		RowMapper mapper = new RowMapper() {
			List<FTPGroupsVb> result = new ArrayList<FTPGroupsVb>();

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setDataSourceDescription(rs.getString("DATA_SOURCE_DESC"));
				ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
				ftpSourceConfigVb.setFtpGroupDesc(rs.getString("FTP_GROUP_DESC"));
				ftpSourceConfigVb.setFtpSubGroupPriority(rs.getInt("FTP_SUB_GROUP_PRIORITY"));
				ftpSourceConfigVb.setFtpSubGroupId(rs.getString("FTP_SUB_GROUP_ID"));
				ftpSourceConfigVb.setFtpSubGroupDesc(rs.getString("FTP_SUB_GROUP_DESC"));
//				ftpSourceConfigVb.setMethodType(rs.getString("METHOD_TYPE"));
//				ftpSourceConfigVb.setMethodTypeDesc(rs.getString("METHOD_TYPE_DESC"));
				ftpSourceConfigVb.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
				ftpSourceConfigVb.setFtpGroupStatus(rs.getInt("FTP_GRP_STATUS"));
				ftpSourceConfigVb.setStatusDesc(rs.getString("FTP_GRP_STATUS_DESC"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_GRP_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setMakerName(rs.getString("MAKER_NAME"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				ftpSourceConfigVb.setVerifierName(rs.getString("VERIFIER_NAME"));
				ftpSourceConfigVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpSourceConfigVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpSourceConfigVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpSourceConfigVb.setMethodTypeDesc(rs.getString("METHOD_LIST"));
				return ftpSourceConfigVb;
			}

		};
		return mapper;
	}

	public int doInsertionGroups(FTPGroupsVb vObject) {
		setServiceDefaults();
		if(!ValidationUtil.isValid(vObject.getDefaultGroup()))
			vObject.setDefaultGroup("N");
		vObject.setFtpGroupStatus(0);

		String query = "Insert Into FTP_GROUPS (COUNTRY, LE_BOOK, DATA_SOURCE_AT, DATA_SOURCE, FTP_GROUP_AT, FTP_GROUP, FTP_SUB_GROUP_ID, "
				+ "FTP_SUB_GROUP_PRIORITY, FTP_SUB_GROUP_DESC, FTP_DEFAULT_FLAG, FTP_GRP_STATUS_NT, FTP_GRP_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, "
				+ "MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSourceAt(), vObject.getDataSource(),
				vObject.getFtpGroupAt(), vObject.getFtpGroup(), vObject.getFtpSubGroupId(),
				vObject.getFtpSubGroupPriority(), vObject.getFtpSubGroupDesc(), vObject.getDefaultGroup(),
				vObject.getFtpGroupStatusNt(), vObject.getFtpGroupStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };

		 return getJdbcTemplate().update(query,args);
		 
	}
	public int doInsertionPend(FTPGroupsVb vObject) {
		setServiceDefaults();
		if(!ValidationUtil.isValid(vObject.getDefaultGroup()))
			vObject.setDefaultGroup("N");
		vObject.setFtpGroupStatus(0);

		String query = "Insert Into FTP_GROUPS_PEND (COUNTRY, LE_BOOK, DATA_SOURCE_AT, DATA_SOURCE, FTP_GROUP_AT, FTP_GROUP, FTP_SUB_GROUP_ID, "
				+ "FTP_SUB_GROUP_PRIORITY, FTP_SUB_GROUP_DESC, FTP_DEFAULT_FLAG, FTP_GRP_STATUS_NT, FTP_GRP_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, "
				+ "MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSourceAt(), vObject.getDataSource(),
				vObject.getFtpGroupAt(), vObject.getFtpGroup(), vObject.getFtpSubGroupId(),
				vObject.getFtpSubGroupPriority(), vObject.getFtpSubGroupDesc(), vObject.getDefaultGroup(),
				vObject.getFtpGroupStatusNt(), vObject.getFtpGroupStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };

		 return getJdbcTemplate().update(query,args);
		 
	}
	
	public int doInsertionPendWithDc(FTPGroupsVb vObject) {
		setServiceDefaults();
		if(!ValidationUtil.isValid(vObject.getDefaultGroup()))
			vObject.setDefaultGroup("N");
		vObject.setFtpGroupStatus(0);

		String query = "Insert Into FTP_GROUPS_PEND (COUNTRY, LE_BOOK, DATA_SOURCE_AT, DATA_SOURCE, FTP_GROUP_AT, FTP_GROUP, FTP_SUB_GROUP_ID, "
				+ "FTP_SUB_GROUP_PRIORITY, FTP_SUB_GROUP_DESC, FTP_DEFAULT_FLAG, FTP_GRP_STATUS_NT, FTP_GRP_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, "
				+ "MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSourceAt(), vObject.getDataSource(),
				vObject.getFtpGroupAt(), vObject.getFtpGroup(), vObject.getFtpSubGroupId(),
				vObject.getFtpSubGroupPriority(), vObject.getFtpSubGroupDesc(), vObject.getDefaultGroup(),
				vObject.getFtpGroupStatusNt(), vObject.getFtpGroupStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), vObject.getDateCreation() };

		 return getJdbcTemplate().update(query,args);
		 
	}
	@Override
	protected void setServiceDefaults() {
		serviceName = "FtpGroups";
		serviceDesc = "FTP Groups";// CommonUtils.getResourceManger().getString("ftpGroup");
		tableName = "FTP_GROUPS";
		childTableName = "FTP_GROUPS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
	
	@Override
	protected List<FTPGroupsVb> selectApprovedRecord(FTPGroupsVb vObject) {
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}
	
	

	
	// New Changes End  
	

	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doUpdateDefaultFlag(FTPGroupsVb vObject) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.MODIFY;
		strErrorDesc = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		try {
			int count = doUpdateDefaultFlagToN(vObject);
			int count1 = doUpdateDefaultFlagToY(vObject);
			exceptionCode = CommonUtils.getResultObject( serviceDesc, Constants.SUCCESSFUL_OPERATION, "Save", "");
			return exceptionCode;
		} catch (RuntimeCustomException rcException) {
			throw rcException;
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Modify.", ex);
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	public int doUpdateDefaultFlagToN(FTPGroupsVb vObject) {
		String query = "Update FTP_GROUPS Set FTP_DEFAULT_FLAG = 'N' "
				+ " Where COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? ";
		Object args[] = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(), vObject.getFtpGroup() };
		return getJdbcTemplate().update(query, args);
	}
	
	public int doUpdateDefaultFlagToY(FTPGroupsVb vObject) {
		String query = "Update FTP_GROUPS Set FTP_DEFAULT_FLAG = 'Y' "
				+ " Where COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? and FTP_SUB_GROUP_ID = ?  ";
		Object args[] = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(), vObject.getFtpGroup(), vObject.getFtpSubGroupId() };
		return getJdbcTemplate().update(query, args);
	}
	
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doUpdateDefaultFlagInPend(FTPGroupsVb vObject) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.MODIFY;
		strErrorDesc = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		try {
			int count = doUpdateDefaultFlagToNInPend(vObject);
			int count1 = doUpdateDefaultFlagToYInPend(vObject);
			exceptionCode = CommonUtils.getResultObject( serviceDesc, Constants.SUCCESSFUL_OPERATION, "Save", "");
			return exceptionCode;
		} catch (RuntimeCustomException rcException) {
			throw rcException;
		} catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = parseErrorMsg(uSQLEcxception);
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		} catch (Exception ex) {
			logger.error("Error in Modify.", ex);
			strErrorDesc = ex.getMessage();
			exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);
		}
	}
	public int doUpdateDefaultFlagToNInPend(FTPGroupsVb vObject) {
		String query = "Update FTP_GROUPS_PEND Set FTP_DEFAULT_FLAG = 'N' "
				+ " Where COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? ";
		Object args[] = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(), vObject.getFtpGroup() };
		return getJdbcTemplate().update(query, args);
	}
	
	public int doUpdateDefaultFlagToYInPend(FTPGroupsVb vObject) {
		String query = "Update FTP_GROUPS_PEND Set FTP_DEFAULT_FLAG = 'Y' "
				+ " Where COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? and FTP_SUB_GROUP_ID = ?  ";
		Object args[] = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(), vObject.getFtpGroup(), vObject.getFtpSubGroupId() };
		return getJdbcTemplate().update(query, args);
	}

	public int doUpdateAppr(FTPGroupsVb vObject) {
		String query = "Update FTP_GROUPS Set DATA_SOURCE_AT = ?, FTP_GROUP_AT = ?, FTP_SUB_GROUP_PRIORITY = ?, FTP_SUB_GROUP_DESC = ?, FTP_DEFAULT_FLAG = ?, "
				+ "FTP_GRP_STATUS_NT = ?, FTP_GRP_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate
				+ "  WHERE COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?  AND FTP_SUB_GROUP_ID = ? ";
		Object[] args = {vObject.getDataSourceAt() , vObject.getFtpGroupAt() , vObject.getFtpSubGroupPriority() , vObject.getFtpSubGroupDesc() , 
				vObject.getDefaultGroup() , vObject.getFtpGroupStatusNt(), vObject.getFtpGroupStatus() , vObject.getRecordIndicatorNt() 
				, vObject.getRecordIndicator() , vObject.getMaker() , vObject.getVerifier() , vObject.getInternalStatus() , vObject.getCountry() , vObject.getLeBook() , vObject.getDataSource() , vObject.getFtpGroup() , vObject.getFtpSubGroupId() , };

		return getJdbcTemplate().update(query, args);
	}
	
	public int doUpdatePend(FTPGroupsVb vObject) {
		String query = "Update FTP_GROUPS_PEND Set DATA_SOURCE_AT = ?, FTP_GROUP_AT = ?, FTP_SUB_GROUP_PRIORITY = ?, FTP_SUB_GROUP_DESC = ?, FTP_DEFAULT_FLAG = ?, "
				+ "FTP_GRP_STATUS_NT = ?, FTP_GRP_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate
				+ "  WHERE COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?  AND FTP_SUB_GROUP_ID = ? ";
		Object[] args = {vObject.getDataSourceAt() , vObject.getFtpGroupAt() , vObject.getFtpSubGroupPriority() , vObject.getFtpSubGroupDesc() , 
				vObject.getDefaultGroup() , vObject.getFtpGroupStatusNt(), vObject.getFtpGroupStatus() , vObject.getRecordIndicatorNt() 
				, vObject.getRecordIndicator() , vObject.getMaker() , vObject.getVerifier() , vObject.getInternalStatus() , vObject.getCountry() , vObject.getLeBook() , vObject.getDataSource() , vObject.getFtpGroup() , vObject.getFtpSubGroupId() , };

		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int doDeleteAppr(FTPGroupsVb vObject) {
		String query = "Delete From FTP_GROUPS Where COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?  AND FTP_SUB_GROUP_ID = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(), vObject.getFtpGroup(), vObject.getFtpSubGroupId() };
		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int deletePendingRecord(FTPGroupsVb vObject){
		String query = "Delete From FTP_GROUPS_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND DATA_SOURCE = ?  AND FTP_GROUP = ?  AND FTP_SUB_GROUP_ID = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(), vObject.getFtpGroup(), vObject.getFtpSubGroupId() };
		return getJdbcTemplate().update(query,args);
	}
	
	@Override
	protected String getAuditString(FTPGroupsVb vObject) {
		StringBuffer strAudit = new StringBuffer("");
		try {
			if (vObject.getCountry() != null)
				strAudit.append(vObject.getCountry().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if (vObject.getLeBook() != null)
				strAudit.append(vObject.getLeBook().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			strAudit.append(vObject.getFtpSubGroupId());
			strAudit.append("!|#");
			strAudit.append(vObject.getRecordIndicatorNt());
			strAudit.append("!|#");
			strAudit.append(vObject.getRecordIndicator());
			strAudit.append("!|#");
			strAudit.append(vObject.getMaker());
			strAudit.append("!|#");
			strAudit.append(vObject.getVerifier());
			strAudit.append("!|#");
			strAudit.append(vObject.getInternalStatus());
			strAudit.append("!|#");
			if (vObject.getDateLastModified() != null)
				strAudit.append(vObject.getDateLastModified().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if (vObject.getDateCreation() != null)
				strAudit.append(vObject.getDateCreation().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

		} catch (Exception ex) {
			strErrorDesc = ex.getMessage();
			strAudit = strAudit.append(strErrorDesc);
			ex.printStackTrace();
		}
		return strAudit.toString();
	}


	
	public String getSystemDateAlone() {
/*		String sql = "SELECT To_Char(SysDate, 'DD-MM-YYYY') AS SYSDATE1 FROM DUAL";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (rs.getString("SYSDATE1"));
			}
		};
		return (String) getJdbcTemplate().queryForObject(sql, null, mapper);*/
		
		

		String sql = "SELECT To_Char(SysDate, 'DD-MM-YYYY') AS SYSDATE1 FROM DUAL";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			sql = "SELECT Format(GetDate(), 'dd-MM-yyyy') AS SYSDATE1 ";
		}
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (rs.getString("SYSDATE1"));
			}
		};
		return (String) getJdbcTemplate().queryForObject(sql, null, mapper);
	
	}

	public String businessDay() {
		String sql = "SELECT TO_CHAR(BUSINESS_DATE,'DD-MM-RRRR') BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE "
				+ " COUNTRY = (SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_COUNTRY') "
				+ " AND LE_BOOK =(SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_LE_BOOK')";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			sql = "SELECT format(BUSINESS_DATE,'dd-MM-yyyy') BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE "
					+ " COUNTRY = (SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_COUNTRY') "
					+ " AND LE_BOOK =(SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_LE_BOOK')";
		}
		return getJdbcTemplate().queryForObject(sql, null, String.class);
	}

	@Override
	protected int getStatus(FTPGroupsVb records) {
		return records.getFtpGroupStatus();
	}

	@Override
	protected void setStatus(FTPGroupsVb vObject, int status) {
		vObject.setFtpGroupStatus(status);
	}





	public List<FTPGroupsVb> getVisionSbu(String visionId, String sessionId, String promptId) {
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 3;
		String strBufApprove = new String(
				"SELECT FIELD_1 AS VISION_SBU,FIELD_2 AS SBU_DESCRIPTION FROM PROMPTS_STG WHERE PROMPT_ID = ? AND VISION_ID = ? AND SESSION_ID = ? "
						+ " ORDER BY SORT_FIELD ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = promptId;
		objParams[1] = visionId;
		objParams[2] = sessionId;
		try {
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getSbuMapper());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}

	protected RowMapper getSbuMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb vObjVb = new AlphaSubTabVb();
				vObjVb.setAlphaSubTab(rs.getString("VISION_SBU"));
				vObjVb.setDescription(rs.getString("SBU_DESCRIPTION"));
				if(ValidationUtil.isValid(vObjVb.getDescription())) {
					vObjVb.setDescription(vObjVb.getDescription().replaceAll(";", ""));
					
				}
				return vObjVb;
			}
		};
		return mapper;
	}

	public List callProcToPopulateVisionSBUData() {
		setServiceDefaults();
		strErrorDesc = "";
		Connection con = null;
		CallableStatement cs = null;
		ArrayList<FTPGroupsVb> sbuList = new ArrayList<FTPGroupsVb>();
		try {
			String sessionId = "";
			sessionId = String.valueOf(System.currentTimeMillis());
			String promptId = "PR088";
			con = getConnection();
			cs = con.prepareCall("{call PR_RS_PROMPT_SBU_PARENT(?,?,?,?,?)}");
			cs.setString(1, String.valueOf(intCurrentUserId));// VisionId
			cs.setString(2, sessionId);// Session Id
			cs.setString(3, promptId);// Prompt Id
			cs.registerOutParameter(4, java.sql.Types.VARCHAR); // Status
			cs.registerOutParameter(5, java.sql.Types.VARCHAR); // Error Message
			ResultSet rs = cs.executeQuery();
			rs.close();
			/*
			 * if("-1".equalsIgnoreCase(cs.getString(4))){ strErrorDesc = cs.getString(5);
			 * throw buildRuntimeCustomException(getResultObject(Constants.
			 * WE_HAVE_ERROR_DESCRIPTION)); }else{
			 */
			sbuList = (ArrayList<FTPGroupsVb>) getVisionSbu(String.valueOf(intCurrentUserId), sessionId, promptId);
			/* } */
		} catch (Exception ex) {
			ex.printStackTrace();
			strErrorDesc = ex.getMessage().trim();
			throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
		} finally {
			JdbcUtils.closeStatement(cs);
//			DataSourceUtils.releaseConnection(con, getDataSource());
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
		return sbuList;
	}

}
