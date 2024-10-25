package com.vision.dao;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.SmartSearchVb;
@Component
public class FTPSourceConfigDao extends AbstractDao<FTPSourceConfigVb> {
	
	@Value("${spring.datasource.username}")
	private String owner;
	

/*******Mapper Start**********/
	String CountryApprDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TAppr.COUNTRY) COUNTRY_DESC";
	String CountryPendDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TPend.COUNTRY) COUNTRY_DESC";
	String LeBookApprDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TAppr.LE_BOOK AND COUNTRY = TAppr.COUNTRY ) LE_BOOK_DESC";
	String LeBookTPendDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TPend.LE_BOOK AND COUNTRY = TPend.COUNTRY ) LE_BOOK_DESC";

	String OperandNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1303, "TAppr.OPERAND", "OPERAND_DESC");
	String OperandNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1303, "TPend.OPERAND", "OPERAND_DESC");

	String FtpScStatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_SC_STATUS", "FTP_SC_STATUS_DESC");
	String FtpScStatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_SC_STATUS", "FTP_SC_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPSourceConfigVb vObject = new FTPSourceConfigVb();
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
				if(rs.getString("FTP_SUB_GROUP_ID")!= null){ 
					vObject.setFtpSubGroupId(rs.getString("FTP_SUB_GROUP_ID"));
				}else{
					vObject.setFtpSubGroupId("");
				}
				vObject.setFilterSequence(rs.getInt("FILTER_SEQUENCE"));
				if(rs.getString("QUERY_TYPE")!= null){ 
					vObject.setQueryType(rs.getString("QUERY_TYPE"));
				}else{
					vObject.setQueryType("");
				}
				if(ValidationUtil.isValid(vObject.getQueryType())) {
					if("D".equalsIgnoreCase(vObject.getQueryType())) {
						vObject.setQueryTypeDesc("Dynamic");
					}else {
						vObject.setQueryTypeDesc("Manual");
					}
				}
				if(rs.getString("TABLE_NAME")!= null){ 
					vObject.setTableName(rs.getString("TABLE_NAME"));
				}else{
					vObject.setTableName("");
				}
				if(rs.getString("COLUMN_NAME")!= null){ 
					vObject.setColName(rs.getString("COLUMN_NAME"));
				}else{
					vObject.setColName("");
				}
				vObject.setOperandNt(rs.getInt("OPERAND_NT"));
				vObject.setOperand(rs.getString("OPERAND"));
				if(rs.getString("CONDITION_VALUE1")!= null){ 
					vObject.setConditionValue1(rs.getString("CONDITION_VALUE1"));
				}else{
					vObject.setConditionValue1("");
				}
				if(rs.getString("CONDITION_VALUE2")!= null){ 
					vObject.setConditionValue2(rs.getString("CONDITION_VALUE2"));
				}else{
					vObject.setConditionValue2("");
				}
				vObject.setFtpSourceConfigStatusNt(rs.getInt("FTP_SC_STATUS_NT"));
				vObject.setFtpSourceConfigStatus(rs.getInt("FTP_SC_STATUS"));
				vObject.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				vObject.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				vObject.setMaker(rs.getInt("MAKER"));
				vObject.setVerifier(rs.getInt("VERIFIER"));
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
				
				
				vObject.setStatusDesc(rs.getString("FTP_SC_STATUS_DESC"));
				
				vObject.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				
				if(ValidationUtil.isValid(rs.getString("MAKER_NAME")))
					vObject.setMakerName(rs.getString("MAKER_NAME"));
				
				if(ValidationUtil.isValid(rs.getString("VERIFIER_NAME")))
					vObject.setVerifierName(rs.getString("VERIFIER_NAME"));
				
				
				return vObject;
			}
		};
		return mapper;
	}

/*******Mapper End**********/
	public List<FTPSourceConfigVb> getQueryPopupResults(FTPSourceConfigVb dObj){

		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_SUB_GROUP_ID"
			+ ",TAppr.FILTER_SEQUENCE"
			+ ",TAppr.QUERY_TYPE"
			+ ",TAppr.TABLE_NAME"
			+ ",TAppr.COLUMN_NAME"
			+ ",TAppr.OPERAND_NT"
			+ ",TAppr.OPERAND, "+OperandNtApprDesc
			+ ",TAppr.CONDITION_VALUE1"
			+ ",TAppr.CONDITION_VALUE2"
			+ ",TAppr.FTP_SC_STATUS_NT"
			+ ",TAppr.FTP_SC_STATUS, "+FtpScStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" from FTP_SOURCE_CONFIG TAppr ");
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_SOURCE_CONFIG_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID AND TAppr.FILTER_SEQUENCE = TPend.FILTER_SEQUENCE )");
		StringBuffer strBufPending = new StringBuffer("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_SUB_GROUP_ID"
			+ ",TPend.FILTER_SEQUENCE"
			+ ",TPend.QUERY_TYPE"
			+ ",TPend.TABLE_NAME"
			+ ",TPend.COLUMN_NAME"
			+ ",TPend.OPERAND_NT"
			+ ",TPend.OPERAND, "+OperandNtPendDesc
			+ ",TPend.CONDITION_VALUE1"
			+ ",TPend.CONDITION_VALUE2"
			+ ",TPend.FTP_SC_STATUS_NT"
			+ ",TPend.FTP_SC_STATUS, "+FtpScStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" from FTP_SOURCE_CONFIG_PEND TPend ");
		try
			{
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data: dObj.getSmartSearchOpt()){
					if(count == dObj.getSmartSearchOpt().size()) {
						data.setJoinType("");
					} else {
						if(!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType()) || "OR".equalsIgnoreCase(data.getJoinType()))) {
							data.setJoinType("AND");
						}
					}
					String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
					switch (data.getObject()) {
					case "country":
						CommonUtils.addToQuerySearch(" upper(TAppr.COUNTRY) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.COUNTRY) "+ val, strBufPending, data.getJoinType());
						break;

					case "leBook":
						CommonUtils.addToQuerySearch(" upper(TAppr.LE_BOOK) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.LE_BOOK) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpSubGroupId":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_ID) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_ID) "+ val, strBufPending, data.getJoinType());
						break;

					case "filterSequence":
						CommonUtils.addToQuerySearch(" upper(TAppr.FILTER_SEQUENCE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FILTER_SEQUENCE) "+ val, strBufPending, data.getJoinType());
						break;

					case "queryType":
						CommonUtils.addToQuerySearch(" upper(TAppr.QUERY_TYPE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.QUERY_TYPE) "+ val, strBufPending, data.getJoinType());
						break;

					case "tableName":
						CommonUtils.addToQuerySearch(" upper(TAppr.TABLE_NAME) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.TABLE_NAME) "+ val, strBufPending, data.getJoinType());
						break;

					case "columnName":
						CommonUtils.addToQuerySearch(" upper(TAppr.COLUMN_NAME) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.COLUMN_NAME) "+ val, strBufPending, data.getJoinType());
						break;

					case "operand":
						CommonUtils.addToQuerySearch(" upper(TAppr.OPERAND) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.OPERAND) "+ val, strBufPending, data.getJoinType());
						break;

					case "conditionValue1":
						CommonUtils.addToQuerySearch(" upper(TAppr.CONDITION_VALUE1) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.CONDITION_VALUE1) "+ val, strBufPending, data.getJoinType());
						break;

					case "conditionValue2":
						CommonUtils.addToQuerySearch(" upper(TAppr.CONDITION_VALUE2) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.CONDITION_VALUE2) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpScStatus":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SC_STATUS) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SC_STATUS) "+ val, strBufPending, data.getJoinType());
						break;

					case "recordIndicator":
						CommonUtils.addToQuerySearch(" upper(TAppr.RECORD_INDICATOR) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.RECORD_INDICATOR) "+ val, strBufPending, data.getJoinType());
						break;

						default:
					}
					count++;
				}
			}
			String orderBy=" Order By COUNTRY AND LE_BOOK AND FTP_SUB_GROUP_ID AND FILTER_SEQUENCE ";
			return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params);

			}catch(Exception ex){
				ex.printStackTrace();
				logger.error(((strBufApprove==null)? "strBufApprove is Null":strBufApprove.toString()));
				logger.error("UNION");
				logger.error(((strBufPending==null)? "strBufPending is Null":strBufPending.toString()));

				if (params != null)
					for(int i=0 ; i< params.size(); i++)
						logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;

			}
	}



	public List<FTPSourceConfigVb> getQueryResultsByParent(FTPGroupsVb dObj, int intStatus){

		setServiceDefaults();
		List<FTPSourceConfigVb> collTemp = null;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
				+ ",TAppr.LE_BOOK"
				+ ",TAppr.FTP_SUB_GROUP_ID"
				+ ",TAppr.FILTER_SEQUENCE"
				+ ",TAppr.QUERY_TYPE"
				+ ",TAppr.TABLE_NAME"
				+ ",TAppr.COLUMN_NAME"
				+ ",TAppr.OPERAND_NT"
				+ ",TAppr.OPERAND, "+OperandNtApprDesc
				+ ",TAppr.CONDITION_VALUE1"
				+ ",TAppr.CONDITION_VALUE2"
				+ ",TAppr.FTP_SC_STATUS_NT"
				+ ",TAppr.FTP_SC_STATUS, "+FtpScStatusNtApprDesc
				+ ",TAppr.RECORD_INDICATOR_NT"
				+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
				+ ",TAppr.MAKER, "+makerApprDesc
				+ ",TAppr.VERIFIER, "+verifierApprDesc
				+ ",TAppr.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_SOURCE_CONFIG TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ? ");
		String strQueryPend = new String("Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_SUB_GROUP_ID"
				+ ",TPend.FILTER_SEQUENCE"
				+ ",TPend.QUERY_TYPE"
				+ ",TPend.TABLE_NAME"
				+ ",TPend.COLUMN_NAME"
				+ ",TPend.OPERAND_NT"
				+ ",TPend.OPERAND, "+OperandNtPendDesc
				+ ",TPend.CONDITION_VALUE1"
				+ ",TPend.CONDITION_VALUE2"
				+ ",TPend.FTP_SC_STATUS_NT"
				+ ",TPend.FTP_SC_STATUS, "+FtpScStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_SOURCE_CONFIG_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  ");

		final int intKeyFieldsCount = 3;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpSubGroupId();

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
	
	public List<FTPSourceConfigVb> getQueryResultsAllByParent(FTPGroupsVb dObj){

		setServiceDefaults();
		List<FTPSourceConfigVb> collTemp = null;
		StringBuffer strQueryAppr = new StringBuffer("Select TAppr.COUNTRY"
				+ ",TAppr.LE_BOOK"
				+ ",TAppr.FTP_SUB_GROUP_ID"
				+ ",TAppr.FILTER_SEQUENCE"
				+ ",TAppr.QUERY_TYPE"
				+ ",TAppr.TABLE_NAME"
				+ ",TAppr.COLUMN_NAME"
				+ ",TAppr.OPERAND_NT"
				+ ",TAppr.OPERAND, "+OperandNtApprDesc
				+ ","+toChar+"(TAppr.CONDITION_VALUE1) CONDITION_VALUE1"
				+ ","+toChar+"(TAppr.CONDITION_VALUE2) CONDITION_VALUE2"
				+ ",TAppr.FTP_SC_STATUS_NT"
				+ ",TAppr.FTP_SC_STATUS, "+FtpScStatusNtApprDesc
				+ ",TAppr.RECORD_INDICATOR_NT"
				+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
				+ ",TAppr.MAKER, "+makerApprDesc
				+ ",TAppr.VERIFIER, "+verifierApprDesc
				+ ",TAppr.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_SOURCE_CONFIG TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ? ");
		StringBuffer strQueryPend = new StringBuffer("Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_SUB_GROUP_ID"
				+ ",TPend.FILTER_SEQUENCE"
				+ ",TPend.QUERY_TYPE"
				+ ",TPend.TABLE_NAME"
				+ ",TPend.COLUMN_NAME"
				+ ",TPend.OPERAND_NT"
				+ ",TPend.OPERAND, "+OperandNtPendDesc
				+ ","+toChar+"(TPend.CONDITION_VALUE1) CONDITION_VALUE1"
				+ ","+toChar+"(TPend.CONDITION_VALUE2) CONDITION_VALUE2"
				+ ",TPend.FTP_SC_STATUS_NT"
				+ ",TPend.FTP_SC_STATUS, "+FtpScStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_SOURCE_CONFIG_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  ");
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_SOURCE_CONFIG_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID AND TAppr.FILTER_SEQUENCE = TPend.FILTER_SEQUENCE )");
		String orderBy=" Order By COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, FILTER_SEQUENCE ";
		Object objParams[]=null;
		try{
			/*			if(intStatus == 0){
			logger.info("Executing approved query");
			collTemp = getJdbcTemplate().query(strQueryAppr.toString(),objParams,getMapper());
		}else{
			logger.info("Executing pending query");
			collTemp = getJdbcTemplate().query(strQueryPend.toString(),objParams,getMapper());
		}*/
		
		Vector<Object> params = new Vector<Object>();
		params.addElement(dObj.getCountry());
		params.addElement(dObj.getLeBook());
		params.addElement(dObj.getFtpSubGroupId());
		int Ctr = 0;
		int Ctr2 = 0;
		String query = "";
		if(dObj.isVerificationRequired()){
			objParams = new Object[params.size()*2];
			for(Ctr=0; Ctr < params.size(); Ctr++)
				objParams[Ctr] = (Object) params.elementAt(Ctr);
			for(Ctr2=0 ; Ctr2 < params.size(); Ctr2++, Ctr++)
				objParams[Ctr] = (Object) params.elementAt(Ctr2);
			
			strQueryAppr.append(" AND "+strWhereNotExists);
			strQueryPend.append(orderBy);
			
			query = strQueryAppr.toString() + " Union " + strQueryPend.toString();
			
		}else{
			objParams = new Object[params.size()];
			for(Ctr=0; Ctr < params.size(); Ctr++)
				objParams[Ctr] = (Object) params.elementAt(Ctr);
			strQueryAppr.append(orderBy);
			query = strQueryAppr.toString();
		}
		collTemp = getJdbcTemplate().query(query,objParams,getMapper());
		return collTemp;
	}catch(Exception ex){
		ex.printStackTrace();
		logger.error("Error: getQueryResults Exception :   ");
		if (objParams != null)
		for(int i=0 ; i< objParams.length; i++)
			logger.error("objParams[" + i + "]" + objParams[i].toString());
		return null;
		}
	}
	public List<FTPSourceConfigVb> getQueryResults(FTPSourceConfigVb dObj, int intStatus){

		setServiceDefaults();

		List<FTPSourceConfigVb> collTemp = null;

		final int intKeyFieldsCount = 4;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
				+ ",TAppr.LE_BOOK"
				+ ",TAppr.FTP_SUB_GROUP_ID"
				+ ",TAppr.FILTER_SEQUENCE"
				+ ",TAppr.QUERY_TYPE"
				+ ",TAppr.TABLE_NAME"
				+ ",TAppr.COLUMN_NAME"
				+ ",TAppr.OPERAND_NT"
				+ ",TAppr.OPERAND, "+OperandNtApprDesc
				+ ",TAppr.CONDITION_VALUE1"
				+ ",TAppr.CONDITION_VALUE2"
				+ ",TAppr.FTP_SC_STATUS_NT"
				+ ",TAppr.FTP_SC_STATUS, "+FtpScStatusNtApprDesc
				+ ",TAppr.RECORD_INDICATOR_NT"
				+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
				+ ",TAppr.MAKER, "+makerApprDesc
				+ ",TAppr.VERIFIER, "+verifierApprDesc
				+ ",TAppr.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_SOURCE_CONFIG TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND FILTER_SEQUENCE = ?  ");
		String strQueryPend = new String("Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_SUB_GROUP_ID"
				+ ",TPend.FILTER_SEQUENCE"
				+ ",TPend.QUERY_TYPE"
				+ ",TPend.TABLE_NAME"
				+ ",TPend.COLUMN_NAME"
				+ ",TPend.OPERAND_NT"
				+ ",TPend.OPERAND, "+OperandNtPendDesc
				+ ",TPend.CONDITION_VALUE1"
				+ ",TPend.CONDITION_VALUE2"
				+ ",TPend.FTP_SC_STATUS_NT"
				+ ",TPend.FTP_SC_STATUS, "+FtpScStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_SOURCE_CONFIG_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND FILTER_SEQUENCE = ?   ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpSubGroupId();
		objParams[3] = dObj.getFilterSequence();

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
	@Override
	protected List<FTPSourceConfigVb> selectApprovedRecord(FTPSourceConfigVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}


	@Override
	protected List<FTPSourceConfigVb> doSelectPendingRecord(FTPSourceConfigVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}


	@Override
	protected int getStatus(FTPSourceConfigVb records){return records.getFtpSourceConfigStatus();}


	@Override
	protected void setStatus(FTPSourceConfigVb vObject,int status){vObject.setFtpSourceConfigStatus(status);}


	@Override
	protected int doInsertionAppr(FTPSourceConfigVb vObject){
		
		String query =	"Insert Into FTP_SOURCE_CONFIG (COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, FILTER_SEQUENCE, QUERY_TYPE, "
				+ "TABLE_NAME, COLUMN_NAME, OPERAND_NT, OPERAND, FTP_SC_STATUS_NT, FTP_SC_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, "
				+ "VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, CONDITION_VALUE1, CONDITION_VALUE2 )"+ 
				 "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+", ?, ?)";
		
		Object[] args = {vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getFilterSequence(), vObject.getQueryType(), 
				vObject.getTableName(), vObject.getColName(), vObject.getOperandNt(), vObject.getOperand(),  
				vObject.getFtpSourceConfigStatusNt(), vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), 
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };
			int result=0;
			try{
				return getJdbcTemplate().update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						int argumentLength = args.length;
						PreparedStatement ps = connection.prepareStatement(query);
						for(int i=1;i<=argumentLength;i++){
							ps.setObject(i,args[i-1]);
						}
						String clobData = ValidationUtil.isValid(vObject.getConditionValue1())?vObject.getConditionValue1():"";
						ps.setCharacterStream(++argumentLength, new StringReader(clobData), clobData.length());
						
						clobData = ValidationUtil.isValid(vObject.getConditionValue2())?vObject.getConditionValue2():"";
						ps.setCharacterStream(++argumentLength, new StringReader(clobData), clobData.length());
						return ps;
					}
				});
				
			}catch (Exception e) {
				e.printStackTrace();
				strErrorDesc = e.getMessage();
				logger.error("inset Error in EDW Object: "+e.getMessage());
			}
			return result;
	}


	@Override
	protected int doInsertionPend(FTPSourceConfigVb vObject){
		
		String query =	"Insert Into FTP_SOURCE_CONFIG_PEND (COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, FILTER_SEQUENCE, QUERY_TYPE, "
				+ "TABLE_NAME, COLUMN_NAME, OPERAND_NT, OPERAND, FTP_SC_STATUS_NT, FTP_SC_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, "
				+ "VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, CONDITION_VALUE1, CONDITION_VALUE2 )"+ 
				 "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate, ?, ?)";
		
		Object[] args = {vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getFilterSequence(), vObject.getQueryType(), 
				vObject.getTableName(), vObject.getColName(), vObject.getOperandNt(), vObject.getOperand(),  
				vObject.getFtpSourceConfigStatusNt(), vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), 
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };
			int result=0;
			try{
				return getJdbcTemplate().update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						int argumentLength = args.length;
						PreparedStatement ps = connection.prepareStatement(query);
						for(int i=1;i<=argumentLength;i++){
							ps.setObject(i,args[i-1]);
						}
						String clobData = ValidationUtil.isValid(vObject.getConditionValue1())?vObject.getConditionValue1():"";
						ps.setCharacterStream(++argumentLength, new StringReader(clobData), clobData.length());
						
						clobData = ValidationUtil.isValid(vObject.getConditionValue2())?vObject.getConditionValue2():"";
						ps.setCharacterStream(++argumentLength, new StringReader(clobData), clobData.length());
						return ps;
					}
				});
				
			}catch (Exception e) {
				e.printStackTrace();
				strErrorDesc = e.getMessage();
				logger.error("inset Error in EDW Object: "+e.getMessage());
			}
			return result;
	}


	@Override
	protected int doInsertionPendWithDc(FTPSourceConfigVb vObject){
		
		String query =	"Insert Into FTP_SOURCE_CONFIG_PEND (COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, FILTER_SEQUENCE, QUERY_TYPE, "
				+ "TABLE_NAME, COLUMN_NAME, OPERAND_NT, OPERAND, FTP_SC_STATUS_NT, FTP_SC_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, "
				+ "VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, CONDITION_VALUE1, CONDITION_VALUE2 )"+ 
				 "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+", ?, ?)";
		
		Object[] args = {vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getFilterSequence(), vObject.getQueryType(), 
				vObject.getTableName(), vObject.getColName(), vObject.getOperandNt(), vObject.getOperand(),  
				vObject.getFtpSourceConfigStatusNt(), vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), 
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), vObject.getDateCreation() };
			int result=0;
			try{
				return getJdbcTemplate().update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						int argumentLength = args.length;
						PreparedStatement ps = connection.prepareStatement(query);
						for(int i=1;i<=argumentLength;i++){
							ps.setObject(i,args[i-1]);
						}
						String clobData = ValidationUtil.isValid(vObject.getConditionValue1())?vObject.getConditionValue1():"";
						ps.setCharacterStream(++argumentLength, new StringReader(clobData), clobData.length());
						
						clobData = ValidationUtil.isValid(vObject.getConditionValue2())?vObject.getConditionValue2():"";
						ps.setCharacterStream(++argumentLength, new StringReader(clobData), clobData.length());
						return ps;
					}
				});
				
			}catch (Exception e) {
				e.printStackTrace();
				strErrorDesc = e.getMessage();
				logger.error("inset Error in EDW Object: "+e.getMessage());
			}
			return result;
	}


	@Override
	protected int doUpdateAppr(FTPSourceConfigVb vObject){
	String query = "Update FTP_SOURCE_CONFIG Set CONDITION_VALUE1 = ?, CONDITION_VALUE2 = ?, QUERY_TYPE = ?, TABLE_NAME = ?, COLUMN_NAME = ?, OPERAND_NT = ?, OPERAND = ?, "
			+ "FTP_SC_STATUS_NT = ?, FTP_SC_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, "
			+ "DATE_LAST_MODIFIED = "+systemDate+"  WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND FILTER_SEQUENCE = ? ";
		Object[] args = {vObject.getQueryType() , vObject.getTableName() , vObject.getColName() , vObject.getOperandNt() , vObject.getOperand() ,  vObject.getFtpSourceConfigStatusNt() , vObject.getFtpSourceConfigStatus() , vObject.getRecordIndicatorNt() , vObject.getRecordIndicator() , vObject.getMaker() , vObject.getVerifier() , vObject.getInternalStatus() 
				, vObject.getCountry() , vObject.getLeBook() , vObject.getFtpSubGroupId() , vObject.getFilterSequence()  };

		int result=0;
		try{
			return getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(query);
					int psIndex = 0;
					String clobData = ValidationUtil.isValid(vObject.getConditionValue1())?vObject.getConditionValue1():"";
					ps.setCharacterStream(++psIndex, new StringReader(clobData), clobData.length());
					
					clobData = ValidationUtil.isValid(vObject.getConditionValue2())?vObject.getConditionValue2():"";
					ps.setCharacterStream(++psIndex, new StringReader(clobData), clobData.length());
					for(int i=1;i<=args.length;i++){
						ps.setObject(++psIndex,args[i-1]);	
					}
					return ps;
				}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
			strErrorDesc = e.getMessage();
			logger.error("inset Error in EDW Object: "+e.getMessage());
		}
		return result;
	}


	@Override
	protected int doUpdatePend(FTPSourceConfigVb vObject){
		String query = "Update FTP_SOURCE_CONFIG_PEND Set CONDITION_VALUE1 = ?, CONDITION_VALUE2 = ?, QUERY_TYPE = ?, TABLE_NAME = ?, COLUMN_NAME = ?, OPERAND_NT = ?, OPERAND = ?, "
				+ "FTP_SC_STATUS_NT = ?, FTP_SC_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, "
				+ "DATE_LAST_MODIFIED = SysDate  WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND FILTER_SEQUENCE = ? ";
			Object[] args = {vObject.getQueryType() , vObject.getTableName() , vObject.getColName() , vObject.getOperandNt() , vObject.getOperand() , 
					vObject.getFtpSourceConfigStatusNt() , vObject.getFtpSourceConfigStatus() , vObject.getRecordIndicatorNt() , vObject.getRecordIndicator() , vObject.getMaker() , vObject.getVerifier() , vObject.getInternalStatus() 
					, vObject.getCountry() , vObject.getLeBook() , vObject.getFtpSubGroupId() , vObject.getFilterSequence()  };

			int result=0;
			try{
				return getJdbcTemplate().update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(query);
						int psIndex = 0;
						String clobData = ValidationUtil.isValid(vObject.getConditionValue1())?vObject.getConditionValue1():"";
						ps.setCharacterStream(++psIndex, new StringReader(clobData), clobData.length());
						
						clobData = ValidationUtil.isValid(vObject.getConditionValue2())?vObject.getConditionValue2():"";
						ps.setCharacterStream(++psIndex, new StringReader(clobData), clobData.length());
						for(int i=1;i<=args.length;i++){
							ps.setObject(++psIndex,args[i-1]);	
						}
						return ps;
					}
				});
				
			}catch (Exception e) {
				e.printStackTrace();
				strErrorDesc = e.getMessage();
				logger.error("inset Error in EDW Object: "+e.getMessage());
			}
			return result;
		}


	@Override
	protected int doDeleteAppr(FTPSourceConfigVb vObject){
		String query = "Delete From FTP_SOURCE_CONFIG Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND FILTER_SEQUENCE = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getFilterSequence() };
		return getJdbcTemplate().update(query,args);
	}


	@Override
	protected int deletePendingRecord(FTPSourceConfigVb vObject){
		String query = "Delete From FTP_SOURCE_CONFIG_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND FILTER_SEQUENCE = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getFilterSequence() };
		return getJdbcTemplate().update(query,args);
	}


	@Override
	protected String getAuditString(FTPSourceConfigVb vObject){
		final String auditDelimiter = vObject.getAuditDelimiter();
		final String auditDelimiterColVal = vObject.getAuditDelimiterColVal();
		StringBuffer strAudit = new StringBuffer("");
			if(ValidationUtil.isValid(vObject.getCountry()))
				strAudit.append("COUNTRY"+auditDelimiterColVal+vObject.getCountry().trim());
			else
				strAudit.append("COUNTRY"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getLeBook()))
				strAudit.append("LE_BOOK"+auditDelimiterColVal+vObject.getLeBook().trim());
			else
				strAudit.append("LE_BOOK"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getFtpSubGroupId()))
				strAudit.append("FTP_SUB_GROUP_ID"+auditDelimiterColVal+vObject.getFtpSubGroupId().trim());
			else
				strAudit.append("FTP_SUB_GROUP_ID"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("FILTER_SEQUENCE"+auditDelimiterColVal+vObject.getFilterSequence());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getQueryType()))
				strAudit.append("QUERY_TYPE"+auditDelimiterColVal+vObject.getQueryType().trim());
			else
				strAudit.append("QUERY_TYPE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getTableName()))
				strAudit.append("TABLE_NAME"+auditDelimiterColVal+vObject.getTableName().trim());
			else
				strAudit.append("TABLE_NAME"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getColName()))
				strAudit.append("COLUMN_NAME"+auditDelimiterColVal+vObject.getColName().trim());
			else
				strAudit.append("COLUMN_NAME"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("OPERAND_NT"+auditDelimiterColVal+vObject.getOperandNt());
			strAudit.append(auditDelimiter);

				strAudit.append("OPERAND"+auditDelimiterColVal+vObject.getOperand());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getConditionValue1()))
				strAudit.append("CONDITION_VALUE1"+auditDelimiterColVal+vObject.getConditionValue1().trim());
			else
				strAudit.append("CONDITION_VALUE1"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getConditionValue2()))
				strAudit.append("CONDITION_VALUE2"+auditDelimiterColVal+vObject.getConditionValue2().trim());
			else
				strAudit.append("CONDITION_VALUE2"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_SC_STATUS_NT"+auditDelimiterColVal+vObject.getFtpSourceConfigStatusNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_SC_STATUS"+auditDelimiterColVal+vObject.getFtpSourceConfigStatus());
			strAudit.append(auditDelimiter);

				strAudit.append("RECORD_INDICATOR_NT"+auditDelimiterColVal+vObject.getRecordIndicatorNt());
			strAudit.append(auditDelimiter);

				strAudit.append("RECORD_INDICATOR"+auditDelimiterColVal+vObject.getRecordIndicator());
			strAudit.append(auditDelimiter);

				strAudit.append("MAKER"+auditDelimiterColVal+vObject.getMaker());
			strAudit.append(auditDelimiter);

				strAudit.append("VERIFIER"+auditDelimiterColVal+vObject.getVerifier());
			strAudit.append(auditDelimiter);

				strAudit.append("INTERNAL_STATUS"+auditDelimiterColVal+vObject.getInternalStatus());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getDateLastModified()))
				strAudit.append("DATE_LAST_MODIFIED"+auditDelimiterColVal+vObject.getDateLastModified().trim());
			else
				strAudit.append("DATE_LAST_MODIFIED"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getDateCreation()))
				strAudit.append("DATE_CREATION"+auditDelimiterColVal+vObject.getDateCreation().trim());
			else
				strAudit.append("DATE_CREATION"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

		return strAudit.toString();
		}

	@Override
	protected void setServiceDefaults(){
		serviceName = "FtpSourceConfig";
		serviceDesc = "Ftpsourceconfig";
		tableName = "FTP_SOURCE_CONFIG";
		childTableName = "FTP_SOURCE_CONFIG";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
		
	}

	public List<FTPSourceConfigVb> getQueryResultTuning(FTPSourceConfigVb dObj){
		List<FTPSourceConfigVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		String strBufApprove = new String("SELECT SOURCE_REFERENCE,DAILY_ORACLE_HINT,MONTHLY_ORACLE_HINT,HISTORY_ORACLE_HINT,"
				+ " ORACLE_HINT_STATUS_NT,ORACLE_HINT_STATUS,"
				 +" RECORD_INDICATOR_NT,RECORD_INDICATOR,TUNING_FREQUENCY from FTP_SOURCE_TUNING WHERE SOURCE_REFERENCE = ? ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
//		objParams[0] = dObj.getSourceReference();

		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getFtpTuningMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	protected RowMapper getFtpTuningMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPSourceConfigVb FTPSourceConfigVb = new FTPSourceConfigVb();
//				FTPSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				FTPSourceConfigVb.setDailyHint (rs.getString("DAILY_ORACLE_HINT"));
				FTPSourceConfigVb.setMonthlyHint(rs.getString("MONTHLY_ORACLE_HINT"));
				FTPSourceConfigVb.setYearlyHint(rs.getString("HISTORY_ORACLE_HINT"));
				FTPSourceConfigVb.setFtpSourceConfigStatus(rs.getInt("ORACLE_HINT_STATUS_NT"));
				FTPSourceConfigVb.setFtpSourceConfigStatus(rs.getInt("ORACLE_HINT_STATUS"));
				FTPSourceConfigVb.setDbStatus(rs.getInt("ORACLE_HINT_STATUS"));
				FTPSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR_NT"));
				FTPSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				FTPSourceConfigVb.setFrquencyTuning(rs.getInt("TUNING_FREQUENCY"));
				return FTPSourceConfigVb;
			}
		};
		return mapper;
	}
	protected int doInsertionFtpTuning(FTPSourceConfigVb vObject){
		String query = "Insert Into FTP_SOURCE_TUNING( " + 
				" SOURCE_REFERENCE_AT, SOURCE_REFERENCE, DAILY_ORACLE_HINT, MONTHLY_ORACLE_HINT, HISTORY_ORACLE_HINT, ORACLE_HINT_STATUS_NT, " + 
				" ORACLE_HINT_STATUS,RECORD_INDICATOR_NT,RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION,TUNING_FREQUENCY) " + 
				" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+","+systemDate+",?)";

			Object args[] = {
//					vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				 vObject.getDailyHint(), vObject.getMonthlyHint(), vObject.getYearlyHint(), vObject.getFtpSourceConfigStatusNt(),
				 vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),vObject.getFrquencyTuning()};
			return getJdbcTemplate().update(query,args);
	}
	public ExceptionCode addModifyFtpTuning(FTPSourceConfigVb vObject){
		List<FTPSourceConfigVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		collTemp  = getQueryResultTuning(vObject);
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setMaker(getIntCurrentUserId());
		vObject.setVerifier(getIntCurrentUserId());
		retVal = doInsertionFtpTuning(vObject);
		if(retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			return exceptionCode;
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	
	protected ExceptionCode doUpdateApprRecordForNonTrans(FTPSourceConfigVb vObject) throws RuntimeCustomException {
		List<FTPSourceConfigVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strCurrentOperation = Constants.ADD;
		strApproveOperation = Constants.ADD;
		setServiceDefaults();
		FTPSourceConfigVb vObjectlocal = null;
		vObject.setMaker(getIntCurrentUserId());
		collTemp = selectApprovedRecord(vObject);
		if (collTemp == null) {
			logger.error("Collection is null for Select Approved Record");
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		if (collTemp.size() > 0) {
			vObjectlocal = ((ArrayList<FTPSourceConfigVb>) collTemp).get(0);
			int intStaticDeletionFlag = getStatus(((ArrayList<FTPSourceConfigVb>) collTemp).get(0));
			if (intStaticDeletionFlag == Constants.PASSIVATE) {
				logger.error("Collection size is greater than zero - Duplicate record found, but inactive");
				exceptionCode = getResultObject(Constants.RECORD_ALREADY_PRESENT_BUT_INACTIVE);
				throw buildRuntimeCustomException(exceptionCode);
			}
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
			vObject.setVerifier(getIntCurrentUserId());
			vObject.setDateCreation(vObjectlocal.getDateCreation());
			retVal = doUpdateAppr(vObject);
			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			} else {
				exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
			}
		}else {
			vObject.setRecordIndicator(Constants.STATUS_ZERO);
			vObject.setVerifier(getIntCurrentUserId());
			retVal = doInsertionAppr(vObject);
			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			} else {
				exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
			}
		}
		String systemDate = getSystemDate();
		vObject.setDateLastModified(systemDate);
		vObject.setDateCreation(systemDate);
		exceptionCode = writeAuditLog(vObject, null);
		if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR);
			throw buildRuntimeCustomException(exceptionCode);
		}
		return exceptionCode;
	}
	
	public List<FTPSourceConfigVb> getTableColumns(FTPSourceConfigVb vObject) {
		setServiceDefaults();
		List<FTPSourceConfigVb> collTemp = null;
		StringBuffer strQueryAppr = new StringBuffer("SELECT OWNER, TABLE_NAME, COLUMN_NAME,       DATA_TYPE, "
				+ "CASE                                            WHEN DATA_TYPE IN                               "
				+ "('NUMBER',                                      'NUMERIC',                                      "
				+ "'DEC',                                          'DECIMAL',                                      "
				+ "'INTEGER',                                      'INT',                                          "
				+ "'SMALLINT',                                     'REAL',                                         "
				+ "'FLOAT')                                        THEN                                            "
				+ "DATA_PRECISION                                  ELSE                                            "
				+ "DATA_LENGTH                                     END                                             "
				+ "DATA_LENGTH,                                    "
				+ "CASE                                            "
				+ "WHEN DATA_TYPE IN                               ('NUMBER',                                      "
				+ "'NUMERIC',                                      'DEC',                                          "
				+ "'DECIMAL',                                      'INTEGER',                                      "
				+ "'INT',                                          'SMALLINT',                                     "
				+ "'REAL',                                         'FLOAT')                                        "
				+ "THEN                                            DATA_SCALE                                      "
				+ "ELSE                                            DATA_PRECISION                                  "
				+ "END                                             DATA_PRECISION,"
				+ "  NULLABLE, nvl(COLUMN_ID, 0)  COLUMN_ID "
				+ " FROM ALL_TAB_COLUMNS                            WHERE UPPER(TABLE_NAME) = upper(?) AND UPPER(OWNER)= UPPER(?)");
		Object objParams[] = new Object[2];
		objParams[0] = vObject.getTableName().toUpperCase();
		objParams[1] = owner.toUpperCase();
		try {
			RowMapper mapper = new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					FTPSourceConfigVb vObject = new FTPSourceConfigVb();
//					vObject.setColName(rs.getString("COLUMN_ID"));
					vObject.setColName(rs.getString("COLUMN_NAME"));
/*					vObject.setColumnLength(rs.getInt("DATA_LENGTH"));
					vObject.setColumnPrecision(rs.getInt("DATA_PRECISION"));
					vObject.setColumnType(rs.getString("DATA_TYPE"));
					vObject.setNullable(rs.getString("NULLABLE"));*/
					return vObject;
				}
			};
			collTemp = getJdbcTemplate().query(strQueryAppr.toString(), objParams, mapper);
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	protected ExceptionCode doUpdateRecordForNonTrans(FTPSourceConfigVb vObject) throws RuntimeCustomException {
		List<FTPSourceConfigVb> collTemp = null;
		FTPSourceConfigVb vObjectlocal = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.MODIFY;
		strErrorDesc = "";
		strCurrentOperation = Constants.MODIFY;
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
			vObjectlocal = ((ArrayList<FTPSourceConfigVb>) collTemp).get(0);

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
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
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
					vObjectlocal = ((ArrayList<FTPSourceConfigVb>) collTemp).get(0);
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
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		}
	}
	
}