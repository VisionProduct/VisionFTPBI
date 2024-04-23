package com.vision.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.CommonApiModel;
import com.vision.vb.CommonVb;
import com.vision.vb.VisionUsersVb;

@Component
public class CommonApiDao extends AbstractDao<CommonVb>{
	
	@Value("${app.productName}")
	private String productName;
	
	public ExceptionCode getCommonResultDataFetch(CommonApiModel vObject){
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList result = new ArrayList(); 
		try
		{	
			String orginalQuery = getPrdQueryConfig(vObject.getQueryId());
			if(!ValidationUtil.isValid(orginalQuery)) {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("No Queries maintained for the Query Id["+vObject.getQueryId()+"]");
				return exceptionCode;
			}
			orginalQuery = replacePromptVariables(orginalQuery, vObject);
			ResultSetExtractor mapper = new ResultSetExtractor() {
				public Object extractData(ResultSet rs)  throws SQLException, DataAccessException {
					ResultSetMetaData metaData = rs.getMetaData();
					int colCount = metaData.getColumnCount();
					Boolean dataPresent = false;
					while(rs.next()){
						HashMap<String,String> resultData = new HashMap<String,String>();
						dataPresent = true;
						for(int cn = 1;cn <= colCount;cn++) {
							String columnName = metaData.getColumnName(cn);
							resultData.put(columnName.toUpperCase(), rs.getString(columnName));
						}
						result.add(resultData);
					}
					if(dataPresent) {
						exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
						exceptionCode.setResponse(result);
					}else {
						exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
						exceptionCode.setErrorMsg("No Records Found");
					}
					return exceptionCode;
				}
			};
			return (ExceptionCode)getJdbcTemplate().query(orginalQuery, mapper);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
			return exceptionCode;
		}
	}
	public String replacePromptVariables(String query, CommonApiModel vObj) {
		query = query.replaceAll("#PROMPT_VALUE_1#", vObj.getParam1());
		query = query.replaceAll("#PROMPT_VALUE_2#", vObj.getParam2());
		query = query.replaceAll("#PROMPT_VALUE_3#", vObj.getParam3());
		query = query.replaceAll("#PROMPT_VALUE_4#", vObj.getParam4());
		query = query.replaceAll("#PROMPT_VALUE_5#", vObj.getParam5());
		query = query.replaceAll("#PROMPT_VALUE_6#", vObj.getParam6());
		query = query.replaceAll("#PROMPT_VALUE_7#", vObj.getParam7());
		query = query.replaceAll("#PROMPT_VALUE_8#", vObj.getParam8());
		query = query.replaceAll("#PROMPT_VALUE_9#", vObj.getParam9());
		query = query.replaceAll("#PROMPT_VALUE_10#", vObj.getParam10());
		query = query.replaceAll("#VISION_ID#", "" + CustomContextHolder.getContext().getVisionId());
		return query;
	}
	public String getPrdQueryConfig(String queryId){
		String resultQuery = "";
		try
		{			
			String sql = "SELECT QUERY from PRD_QUERY_CONFIG WHERE DATA_REF_ID = '"+queryId+"' AND STATUS = 0 AND APPLICATION_ID = '"+productName+"V2' ";
			resultQuery = getJdbcTemplate().queryForObject(sql,String.class);
			return resultQuery;
		}catch(Exception ex){
			logger.error("Exception while getting the Query for the Query ID["+queryId+"]");
			return null;
		}
	}
	public int checkSaveTheme(VisionUsersVb vObj){
		int cnt = 0;
		try
		{			
			String sql = "SELECT  count(1) FROM PRD_APP_THEME where Vision_ID = "+vObj.getVisionId()+" AND APPLICATION_ID = '"+productName+"' ";
			cnt = getJdbcTemplate().queryForObject(sql,Integer.class);
			if(cnt > 0 ) {
				deleteSaveTheme(vObj);
			}
			return cnt;
		}catch(Exception ex){
			return 0;
		}
	}
	public int deleteSaveTheme(VisionUsersVb vObj){
		try
		{			
			String sql = "DELETE FROM PRD_APP_THEME where Vision_ID = "+vObj.getVisionId()+" AND APPLICATION_ID = '"+productName+"' ";
			int retVal = getJdbcTemplate().update(sql);
			return retVal;
		}catch(Exception ex){
			return 0;
		}
	}
	public ExceptionCode getCommonResultDataQuery(String query){
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList result = new ArrayList(); 
		try
		{	
			if(!ValidationUtil.isValid(query)) {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Query Invalid");
				return exceptionCode;
			}
			//orginalQuery = replacePromptVariables(query, vObject);
			ResultSetExtractor mapper = new ResultSetExtractor() {
				public Object extractData(ResultSet rs)  throws SQLException, DataAccessException {
					ResultSetMetaData metaData = rs.getMetaData();
					int colCount = metaData.getColumnCount();
					Boolean dataPresent = false;
					while(rs.next()){
						LinkedHashMap<String,String> resultData = new LinkedHashMap<String,String>();
						dataPresent = true;
						for(int cn = 1;cn <= colCount;cn++) {
							String columnName = metaData.getColumnName(cn);
							resultData.put(columnName.toUpperCase(), rs.getString(columnName));
						}
						result.add(resultData);
					}
					if(dataPresent) {
						exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
						exceptionCode.setResponse(result);
					}else {
						exceptionCode.setErrorMsg("No Records Found");
						exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					}
					return exceptionCode;
				}
			};
			return (ExceptionCode)getJdbcTemplate().query(query, mapper);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
			return exceptionCode;
		}
	}
	public ExceptionCode getCommonResultDataQuery(String query,Connection conExt){
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList result = new ArrayList(); 
		try
		{	
			if(!ValidationUtil.isValid(query)) {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Query Invalid");
				return exceptionCode;
			}
			//orginalQuery = replacePromptVariables(query, vObject);
			ResultSetExtractor mapper = new ResultSetExtractor() {
				public Object extractData(ResultSet rs)  throws SQLException, DataAccessException {
					ResultSetMetaData metaData = rs.getMetaData();
					int colCount = metaData.getColumnCount();
					Boolean dataPresent = false;
					while(rs.next()){
						LinkedHashMap<String,String> resultData = new LinkedHashMap<String,String>();
						dataPresent = true;
						for(int cn = 1;cn <= colCount;cn++) {
							String columnName = metaData.getColumnName(cn);
							resultData.put(columnName.toUpperCase(), rs.getString(columnName));
						}
						result.add(resultData);
					}
					if(dataPresent) {
						exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
						exceptionCode.setResponse(result);
					}else {
						exceptionCode.setErrorMsg("No Records Found");
						exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					}
					return exceptionCode;
				}
			};
			return (ExceptionCode)getJdbcTemplate().query(query, mapper);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
			return exceptionCode;
		}
	}	
	public List getChartList(String chartType) {
		String sql = "";
		List collTemp = new ArrayList<>();
		try {
			sql = "SELECT ALPHA_SUB_TAB, "
					+ " (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = 7005 AND ALPHA_SUB_TAB = S1.ALPHA_SUB_TAB ) DESCR "
					+ " FROM ALPHA_SUB_TAB S1 WHERE ALPHA_TAB = 7500 AND ALPHA_SUBTAB_DESCRIPTION IN (  "
					+ " SELECT T2.ALPHA_SUBTAB_DESCRIPTION TYPE  FROM ALPHA_SUB_TAB T1,  " + " ALPHA_SUB_TAB T2  "
					+ " WHERE T1.ALPHA_TAB = 7005  " + " AND T2.ALPHA_TAB = 7500  " + " AND T1.ALPHA_SUB_TAB = '"
					+ chartType + "'  " + " AND T1.ALPHA_SUB_TAB = T2.ALPHA_SUB_TAB)";
			ResultSetExtractor mapper = new ResultSetExtractor() {
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					while (rs.next()) {
						LinkedHashMap<String,String> resultData = new LinkedHashMap<String,String>();
						for(int cn = 1;cn < 2;cn++) {
							resultData.put(rs.getString("ALPHA_SUB_TAB"), rs.getString("DESCR"));
						}
						collTemp.add(resultData);
					}
					return collTemp;
				}
			};
			return (List) getJdbcTemplate().query(sql, mapper);
		} catch (Exception e) {
			logger.error("Error while getting chart List");
		}
		return collTemp;
	}
}
