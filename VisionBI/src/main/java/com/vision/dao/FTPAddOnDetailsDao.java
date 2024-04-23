/*package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.jdbc.core.RowMapper;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPAddOnDetailsVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;

public class FTPAddOnDetailsDao extends AbstractDao<FTPGroupsVb> {
	
	
	protected RowMapper getAddOnMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setCustomerId(rs.getString("CUSTOMER_ID"));
				ftpSourceConfigVb.setContractId(rs.getString("CONTRACT_ID"));
				ftpSourceConfigVb.setAddOnRate(rs.getString("ADDON_RATE"));
				ftpSourceConfigVb.setSubRate(rs.getString("SUBSIDY_RATE"));
				ftpSourceConfigVb.setFtpCurveStatus(rs.getInt("FTP_ADDON_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_ADDON_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				ftpSourceConfigVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpSourceConfigVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpSourceConfigVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;  
	}
	public List<FTPGroupsVb> getQueryResults(FTPGroupsVb dObj,int status){
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("SELECT T1.FTP_CURVE_ID,T1.EFFECTIVE_DATE,T1.CUSTOMER_ID,T1.CONTRACT_ID,T1.ADDON_RATE,T1.SUBSIDY_RATE,"+
				"T2.FTP_ADDON_STATUS,T2.SOURCE_REFERENCE,T2.METHOD_REFERENCE,"+
			    "T1.RECORD_INDICATOR,T1.MAKER,T1.VERIFIER,T1.INTERNAL_STATUS,"+
				"   FROM FTP_AddOn ");
		try{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(T1.COUNTRY) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(T1.LE_BOOK) like ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())){
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("T1.DATA_SOURCE = ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())){
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("T1.FTP_GROUP = ?", strBufApprove);
			}
			CommonUtils.addToQuery(" T1.COUNTRY = T2.COUNTRY"+
					"	AND T1.LE_BOOK = T2.LE_BOOK"+
					"	AND T1.FTP_REFERENCE = T2.FTP_REFERENCE ",strBufApprove);
			String orderBy = " Order By FTP_GROUP_SEQ";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	
	@Override
	protected List<FTPAddOnDetailsVb> selectApprovedRecord(FTPAddOnDetailsVb vObject){
		return getQueryResultAddOn(vObject);
	}
	public ExceptionCode addModifyFtpAddOn(ExceptionCode exceptionCode,List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		for(FTPCurveVb vObjVb : ftpCurveList){
			if(vObjVb.isChecked()){
				collTemp  = getQueryResultAddOn(vObjVb);
				if (collTemp!=null && collTemp.size() > 0){
					retVal = deleteFtpAddOn(vObject);
					if(retVal != Constants.SUCCESSFUL_OPERATION){
						exceptionCode = getResultObject(retVal);
						return exceptionCode;
					}
				}
				vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
				vObjVb.setMaker(getIntCurrentUserId());
				vObjVb.setVerifier(getIntCurrentUserId());
				retVal = doInsertionFtpAddOn(vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
			}
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public String businessDay(){
		String sql ="SELECT TO_CHAR(BUSINESS_DATE,'DD-MM-RRRR') BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE "+ 
				" COUNTRY = (SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_COUNTRY') "+
				" AND LE_BOOK =(SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_LE_BOOK')";
		return getJdbcTemplate().queryForObject(sql,null,String.class);
	}
	protected int deleteFtpAddOn(FTPCurveVb vObject){
		String query = "DELETE FROM FTP_ADDON WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')";
		Object args[] = {vObject.getFtpCurveId(),vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	public int doInsertionFtpAddOn(FTPCurveVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_ADDON( " + 
			" FTP_CURVE_ID, EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,ADDON_RATE,SUBSIDY_RATE,FTP_ADDON_STATUS, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, ?,SysDate, SysDate)";

		Object args[] = {vObject.getFtpCurveId(), vObject.getEffectiveDate(), vObject.getCustomerId(),
			 vObject.getContractId(), vObject.getAddOnRate(), vObject.getSubRate(),
			 vObject.getFtpCurveStatus(),vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public List<FTPAddOnDetailsVb> getQueryResultAddOn(FTPAddOnDetailsVb dObj){
		List<FTPAddOnDetailsVb> collTemp = null;
		final int intKeyFieldsCount = 4;
		String strBufApprove = new String("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,TO_CHAR(ADDON_RATE,'9,990.999999990')ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'9,990.999999990') SUBSIDY_RATE,FTP_ADDON_STATUS,"
				+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON"
				+ " WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE= TO_DATE(?,'DD-MM-RRRR') AND CUSTOMER_ID = ? AND CONTRACT_ID =? ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		objParams[2] = dObj.getCustomerId();
		objParams[3] = dObj.getContractId();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getAddOnMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResults(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, " + 
			" TAppr.DATA_SOURCE,TAPPR.FTP_GROUP " + 
			" From FTP_GROUPS TAppr");
		try{
			if (ValidationUtil.isValid(ftpCurve))
			{
				params.addElement("%" + ftpCurve.toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(FTP_CURVE_ID) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(effectiveDate) && flag)
			{
				params.addElement(effectiveDate);
				CommonUtils.addToQuery("EFFECTIVE_DATE >= TO_DATE(?,'DD-MM-RRRR')", strBufApprove);
			}else{
				params.addElement(effectiveDate);
				CommonUtils.addToQuery("EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')", strBufApprove);
			}
			//check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1){
				if (dObj.getRecordIndicator() > 3){
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
				}else{
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
				}
			}
			String orderBy = " Order By COUNTRY, LE_BOOK, DATA_SOURCE, FTP_GROUP ";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getQueryPopupMapper());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	protected void setServiceDefaults(){
		serviceName = "FTP ADDON";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpAddOnDetails");
		tableName = "FTP ADDON";
		childTableName = "FTP AddOn";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
	
	
	
	
	
	
	
	public List<FTPGroupsVb> getQueryPopupResultsAddOn(String ftpCurve,String effectiveDate,FTPGroupsVb dObj,Boolean flag){
		dObj.setRecordIndicator(0);
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer();
		if(flag){
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,TO_CHAR(ADDON_RATE,'9,990.999999990') ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'9,990.999999990') SUBSIDY_RATE,FTP_ADDON_STATUS,"
					+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON"
					+ " WHERE EFFECTIVE_DATE = (Select max(Effective_date) from FTP_ADDON) ");	
		}else{
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,TO_CHAR(ADDON_RATE,'9,990.999999990') ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'9,990.999999990') SUBSIDY_RATE,FTP_ADDON_STATUS,"
					+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON");
		}
		try{
			if (ValidationUtil.isValid(ftpCurve))
			{
				params.addElement("%" + ftpCurve.toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(FTP_CURVE_ID) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(effectiveDate) && flag)
			{
				params.addElement(effectiveDate);
				CommonUtils.addToQuery("EFFECTIVE_DATE >= TO_DATE(?,'DD-MM-RRRR')", strBufApprove);
			}else{
				params.addElement(effectiveDate);
				CommonUtils.addToQuery("EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')", strBufApprove);
			}
			String orderBy = " Order By CUSTOMER_ID";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getAddOnMapper());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
}*/