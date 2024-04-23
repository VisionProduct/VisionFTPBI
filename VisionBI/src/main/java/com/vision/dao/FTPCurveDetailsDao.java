/*package com.vision.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.FTPCurveDetailsVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.TenorBucketsVb;


public class FTPCurveDetailsDao extends AbstractDao<FTPCurveDetailsVb> {
	protected RowMapper getCurveMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveDetailsVb ftpCurveDetailsVb = new FTPCurveDetailsVb();
				ftpCurveDetailsVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpCurveDetailsVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpCurveDetailsVb.setTenorBucketApplicationCode(rs.getInt("TENOR_APPLICATION_CODE"));
				ftpCurveDetailsVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				ftpCurveDetailsVb.setVisionSbu(rs.getString("VISION_SBU_ATTRIBUTE"));
				ftpCurveDetailsVb.setCurrency(rs.getString("CURRENCY"));
				ftpCurveDetailsVb.setIntStart(rs.getString("INT_RATE_START"));
				ftpCurveDetailsVb.setIntEnd(rs.getString("INT_RATE_END"));
				ftpCurveDetailsVb.setAmountStart(rs.getString("AMOUNT_START"));
				ftpCurveDetailsVb.setAmountEnd(rs.getString("AMOUNT_END"));
				ftpCurveDetailsVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
				ftpCurveDetailsVb.setSubRate(rs.getString("FTP_CURVE"));
				ftpCurveDetailsVb.setFtpCurveStatus(rs.getInt("FTP_CURVE_STATUS"));
				ftpCurveDetailsVb.setDbStatus(rs.getInt("FTP_CURVE_STATUS"));
				ftpCurveDetailsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpCurveDetailsVb.setMaker(rs.getInt("MAKER"));
				ftpCurveDetailsVb.setVerifier(rs.getInt("VERIFIER"));
				ftpCurveDetailsVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpCurveDetailsVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpCurveDetailsVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpCurveDetailsVb.setMaker(rs.getInt("MAKER"));
				ftpCurveDetailsVb.setVerifier(rs.getInt("VERIFIER"));
				return ftpCurveDetailsVb;
			}
		};
		return mapper;
	}
	protected RowMapper getFtpRatesMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
				ftpSourceConfigVb.setSubRate(rs.getString("FTP_CURVE"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setFtpCurveStatus(rs.getInt("FTP_RATE_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_RATE_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR_NT"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getSbuMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb vObjVb = new AlphaSubTabVb();
				vObjVb.setAlphaSubTab(rs.getString("VISION_SBU"));
				vObjVb.setDescription(rs.getString("SBU_DESCRIPTION"));
				return vObjVb;
			}
		};
		return mapper;
	}
	
	public ExceptionCode addModifyFtpRates(ExceptionCode exceptionCode,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		collTemp  = getCheckResultFtpRates(vObject);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpRates(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setMaker(getIntCurrentUserId());
		vObject.setVerifier(getIntCurrentUserId());
		retVal = doInsertionFtpRates(vObject);
		if(retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			return exceptionCode;
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public List<FTPGroupsVb> getCheckResultFtpRates(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("SELECT FTP_RATE_ID,TO_CHAR(FTP_CURVE,'9,990.999999990') FTP_CURVE,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,FTP_RATE_STATUS,"
				 +" RECORD_INDICATOR_NT,RECORD_INDICATOR from FTP_TERM_RATES t1 WHERE FTP_RATE_STATUS = 0 AND FTP_RATE_ID = ? "
				 +" Effective_date = TO_DATE(?,'DD-MM-RRRR') ");
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpRateId();
		objParams[1] = dObj.getEffectiveDate();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getFtpRatesMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	protected int deleteFtpRates(FTPCurveVb vObject){
		String query = "Delete From FTP_TERM_RATES Where FTP_RATE_ID = ? AND EFFECTIVE_DATE =TO_DATE(?,'DD-MM-RRRR') "; 
		Object args[] = {vObject.getFtpRateId(),vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	protected int doInsertionFtpRates(FTPCurveVb vObject){
		String query = "Insert Into FTP_TERM_RATES( " + 
				" FTP_RATE_ID, FTP_CURVE, EFFECTIVE_DATE, FTP_RATE_STATUS_NT, FTP_RATE_STATUS,"+
				" RECORD_INDICATOR_NT,RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
				" Values (?, ?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, SysDate,SysDate)";

			Object args[] = {vObject.getFtpRateId() ,vObject.getSubRate().replaceAll(",", ""),
				 vObject.getEffectiveDate(), vObject.getFtpCurveStatusNt(), vObject.getFtpCurveStatus(),
				 vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
			return getJdbcTemplate().update(query,args);
	}
	public List<FTPCurveDetailsVb> getQueryResultCurves(FTPCurveDetailsVb dObj){
		List<FTPCurveDetailsVb> collTemp = null;
		final int intKeyFieldsCount = 5;
		String strBufApprove = new String("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,TENOR_APPLICATION_CODE,TENOR_CODE,VISION_SBU_ATTRIBUTE,"
				+ " CURRENCY,TO_CHAR(INT_RATE_START,'9,990.999999990') INT_RATE_START,TO_CHAR(INT_RATE_END,'9,990.999999990') INT_RATE_END,TO_CHAR(AMOUNT_START,'99,999,999,999,999,999,990.99990') AMOUNT_START ,TO_CHAR(AMOUNT_END,'99,999,999,999,999,999,990.99990') AMOUNT_END,FTP_CURVE_STATUS,"
				+ " (SELECT TO_CHAR(FTP_CURVE,'9,990.999999990') FTP_CURVE FROM FTP_TERM_RATES WHERE " 
	            + " FTP_RATE_ID = T1.FTP_RATE_ID AND EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_TERM_RATES WHERE FTP_RATE_ID = T1.FTP_RATE_ID ) AND EFFECTIVE_DATE >= T1.EFFECTIVE_DATE) FTP_CURVE,"
				+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION,FTP_RATE_ID FROM FTP_CURVES T1"
				+ " WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE= TO_DATE(?,'DD-MM-RRRR') AND TENOR_APPLICATION_CODE = ? AND TENOR_CODE = ? "
				+ " AND CURRENCY = ? ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		objParams[2] = dObj.getTenorBucketApplicationCode();
		objParams[3] = dObj.getTenorBucketCode();
		objParams[4] = dObj.getVisionSbu();
		objParams[4] = dObj.getCurrency();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getCurveMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultFtpRates(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("SELECT FTP_RATE_ID,TO_CHAR(FTP_CURVE,'9,990.999999990') FTP_CURVE,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,FTP_RATE_STATUS,"
				 +" RECORD_INDICATOR_NT,RECORD_INDICATOR from FTP_TERM_RATES t1 WHERE FTP_RATE_STATUS = 0 AND FTP_RATE_ID = ? and Effective_Date = "
				 +" (Select Max(Effective_date) from FTP_TERM_RATES where Effective_date <= TO_DATE(?,'DD-MM-RRRR') and FTP_RATE_ID = t1.FTP_RATE_ID) ");
				 
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpRateId();
		objParams[1] = dObj.getEffectiveDate();

		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getFtpRatesMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	
	public List<FTPCurveDetailsVb> getQueryResultsForReview(String ftpCurve,String effectiveDate,FTPCurveDetailsVb dObj,Boolean flag){

		List<FTPCurveDetailsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		StringBuffer strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR (t1.EFFECTIVE_DATE, 'DD-MM-RRRR') EFFECTIVE_DATE,"+
				" TENOR_APPLICATION_CODE,TENOR_CODE,NVL (VISION_SBU_ATTRIBUTE, '000') VISION_SBU_ATTRIBUTE,"+
				" CURRENCY,TO_CHAR (INT_RATE_START, '9,990.999999990') INT_RATE_START,"+
				" TO_CHAR (INT_RATE_END, '9,990.999999990') INT_RATE_END,TO_CHAR (AMOUNT_START, '99,999,999,999,999,999,990.99990')"+
				" AMOUNT_START,TO_CHAR (AMOUNT_END, '99,999,999,999,999,999,990.99990') AMOUNT_END,"+
				" FTP_CURVE_STATUS,FTP_RATE_ID,(SELECT TO_CHAR (t3.FTP_CURVE, '9,990.999999990') FTP_CURVE"+
				" FROM FTP_TERM_RATES t3 WHERE     t3.FTP_RATE_ID = T1.FTP_RATE_ID "+
				" AND t3.EFFECTIVE_DATE = (SELECT MAX (EFFECTIVE_DATE) FROM FTP_TERM_RATES t2"+
				" WHERE t2.FTP_RATE_ID = T1.FTP_RATE_ID and  t2.EFFECTIVE_DATE <= t1.Effective_Date)) FTP_CURVE,"+
				" RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,"+
				" DATE_CREATION FROM FTP_CURVES T1 WHERE"+
				" EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_CURVES t2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID"+
				" AND t2.EFFECTIVE_DATE <= TO_DATE ('"+effectiveDate+"', 'DD-MM-RRRR')) ");

		strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR (t1.EFFECTIVE_DATE, 'DD-MM-RRRR') EFFECTIVE_DATE,"+
				" TENOR_APPLICATION_CODE,TENOR_CODE,NVL (VISION_SBU_ATTRIBUTE, '000') VISION_SBU_ATTRIBUTE,"+
				" CURRENCY,TO_CHAR (INT_RATE_START, '9,990.999999990') INT_RATE_START,"+
				" TO_CHAR (INT_RATE_END, '9,990.999999990') INT_RATE_END,TO_CHAR (AMOUNT_START, '99,999,999,999,999,999,990.99990')"+
				" AMOUNT_START,TO_CHAR (AMOUNT_END, '99,999,999,999,999,999,990.99990') AMOUNT_END,"+
				" FTP_CURVE_STATUS,FTP_RATE_ID,(SELECT TO_CHAR (t3.FTP_CURVE, '9,990.999999990') FTP_CURVE"+
				" FROM FTP_TERM_RATES t3 WHERE     t3.FTP_RATE_ID = T1.FTP_RATE_ID "+
				" AND t3.EFFECTIVE_DATE = (SELECT MAX (EFFECTIVE_DATE) FROM FTP_TERM_RATES t2"+
				" WHERE t2.FTP_RATE_ID = T1.FTP_RATE_ID and  t2.EFFECTIVE_DATE <= t1.Effective_Date)) FTP_CURVE,"+
				" RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,"+
				" DATE_CREATION FROM FTP_CURVES T1 WHERE"+
				" EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_CURVES t2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID"+
				" AND t2.EFFECTIVE_DATE = TO_DATE ('"+effectiveDate+"', 'DD-MM-RRRR')) ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new Integer(dObj.getTenorBucketApplicationCode());	//[TENOR_APPLICATION_CODE]
		objParams[1] = new String(dObj.getTenorBucketCode());	//[TENOR_CODE]
		try
		{
			if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0)
			{
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strQueryAppr.toString(),objParams,getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),objParams,getMapper());
			}
			return collTemp;
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			if(intStatus == 0)
				logger.error(((strQueryAppr == null) ? "strQueryAppr is null" : strQueryAppr.toString()));
			else
				logger.error(((strQueryPend == null) ? "strQueryPend is null" : strQueryPend.toString()));

			return null;
		}
	}
	@Override
	protected List<FTPCurveDetailsVb> selectApprovedRecord(FTPCurveDetailsVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<FTPCurveDetailsVb> doSelectPendingRecord(FTPCurveDetailsVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}
	public List<FTPCurveDetailsVb> getQueryPopupResultsCurves(String ftpCurve,String effectiveDate,FTPCurveDetailsVb dObj,Boolean flag){
		dObj.setRecordIndicator(0);
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer();
		if(flag){
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR (t1.EFFECTIVE_DATE, 'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TENOR_APPLICATION_CODE,TENOR_CODE,NVL (VISION_SBU_ATTRIBUTE, '000') VISION_SBU_ATTRIBUTE,"+
					" CURRENCY,TO_CHAR (INT_RATE_START, '9,990.999999990') INT_RATE_START,"+
					" TO_CHAR (INT_RATE_END, '9,990.999999990') INT_RATE_END,TO_CHAR (AMOUNT_START, '99,999,999,999,999,999,990.99990')"+
					" AMOUNT_START,TO_CHAR (AMOUNT_END, '99,999,999,999,999,999,990.99990') AMOUNT_END,"+
					" FTP_CURVE_STATUS,FTP_RATE_ID,(SELECT TO_CHAR (t3.FTP_CURVE, '9,990.999999990') FTP_CURVE"+
					" FROM FTP_TERM_RATES t3 WHERE     t3.FTP_RATE_ID = T1.FTP_RATE_ID "+
					" AND t3.EFFECTIVE_DATE = (SELECT MAX (EFFECTIVE_DATE) FROM FTP_TERM_RATES t2"+
					" WHERE t2.FTP_RATE_ID = T1.FTP_RATE_ID and  t2.EFFECTIVE_DATE <= t1.Effective_Date)) FTP_CURVE,"+
					" RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,"+
					" DATE_CREATION FROM FTP_CURVES T1 WHERE"+
					" EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_CURVES t2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID"+
					" AND t2.EFFECTIVE_DATE <= TO_DATE ('"+effectiveDate+"', 'DD-MM-RRRR')) ");
		}else{
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR (t1.EFFECTIVE_DATE, 'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TENOR_APPLICATION_CODE,TENOR_CODE,NVL (VISION_SBU_ATTRIBUTE, '000') VISION_SBU_ATTRIBUTE,"+
					" CURRENCY,TO_CHAR (INT_RATE_START, '9,990.999999990') INT_RATE_START,"+
					" TO_CHAR (INT_RATE_END, '9,990.999999990') INT_RATE_END,TO_CHAR (AMOUNT_START, '99,999,999,999,999,999,990.99990')"+
					" AMOUNT_START,TO_CHAR (AMOUNT_END, '99,999,999,999,999,999,990.99990') AMOUNT_END,"+
					" FTP_CURVE_STATUS,FTP_RATE_ID,(SELECT TO_CHAR (t3.FTP_CURVE, '9,990.999999990') FTP_CURVE"+
					" FROM FTP_TERM_RATES t3 WHERE     t3.FTP_RATE_ID = T1.FTP_RATE_ID "+
					" AND t3.EFFECTIVE_DATE = (SELECT MAX (EFFECTIVE_DATE) FROM FTP_TERM_RATES t2"+
					" WHERE t2.FTP_RATE_ID = T1.FTP_RATE_ID and  t2.EFFECTIVE_DATE <= t1.Effective_Date)) FTP_CURVE,"+
					" RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,"+
					" DATE_CREATION FROM FTP_CURVES T1 WHERE"+
					" EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_CURVES t2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID"+
					" AND t2.EFFECTIVE_DATE = TO_DATE ('"+effectiveDate+"', 'DD-MM-RRRR')) ");
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
			String orderBy = " Order By TENOR_APPLICATION_CODE";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getCurveMapper());
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
		serviceName = "FTP DETAILS";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpGroup");
		tableName = "FTP DETAILS";
		childTableName = "FTP DETAILS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
	
	public List callProcToPopulateVisionSBUData(){
		setServiceDefaults();
		strErrorDesc = "";
		Connection con = null;
		CallableStatement cs =  null;
		ArrayList<FTPGroupsVb> sbuList = new ArrayList<FTPGroupsVb>();
		try{
			String sessionId = "";
			sessionId = String.valueOf(System.currentTimeMillis()); 
			String promptId = "PR088";
			con = getConnection();
			cs = con.prepareCall("{call PR_RS_PROMPT_SBU_PARENT(?,?,?,?,?)}");
			cs.setString(1, String.valueOf(intCurrentUserId));//VisionId
	        cs.setString(2, sessionId);//Session Id
	        cs.setString(3, promptId);//Prompt Id
	        cs.registerOutParameter(4, java.sql.Types.VARCHAR); //Status
	        cs.registerOutParameter(5, java.sql.Types.VARCHAR); //Error Message
			ResultSet rs = cs.executeQuery();
		    rs.close();
		    if("-1".equalsIgnoreCase(cs.getString(4))){
		    	strErrorDesc = cs.getString(5);
				throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
		    }else{
		    	sbuList = (ArrayList<FTPGroupsVb>) getVisionSbu(String.valueOf(intCurrentUserId),sessionId,promptId);
		    }
		}catch(Exception ex){
			ex.printStackTrace();
			strErrorDesc = ex.getMessage().trim();
			throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
		}finally{
			JdbcUtils.closeStatement(cs);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	return sbuList;
	}
	
	public List<FTPGroupsVb> getVisionSbu(String visionId,String sessionId,String promptId){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 3;
		String strBufApprove = new String("SELECT FIELD_1 AS VISION_SBU,FIELD_2 AS SBU_DESCRIPTION FROM PROMPTS_STG WHERE PROMPT_ID = ? AND VISION_ID = ? AND SESSION_ID = ? "+
						                  " ORDER BY SORT_FIELD ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = promptId;
		objParams[1] = visionId;
		objParams[2] = sessionId;
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getSbuMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	
	public int doInsertionFtpCurves(FTPCurveDetailsVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_CURVES( " + 
			" FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE, TENOR_CODE, VISION_SBU_ATTRIBUTE, CURRENCY,INT_RATE_START, INT_RATE_END, AMOUNT_START, " + 
			" AMOUNT_END, FTP_RATE_ID,FTP_CURVE_STATUS, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,SysDate, SysDate)";

		Object args[] = {vObject.getFtpCurveId(), vObject.getEffectiveDate(), vObject.getTenorBucketApplicationCode(),
			 vObject.getTenorBucketCode(), vObject.getVisionSbu(), vObject.getCurrency(), vObject.getIntStart().replaceAll(",", ""),
			 vObject.getIntEnd().replaceAll(",", ""), vObject.getAmountStart().replaceAll(",", ""),vObject.getAmountEnd().replaceAll(",", ""),
			 vObject.getFtpRateId(), vObject.getFtpCurveStatus(),vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	
	public ExceptionCode addModifyFtpCurve(ExceptionCode exceptionCode,List<FTPCurveDetailsVb> ftpCurveList,FTPCurveDetailsVb vObject){
		List<FTPCurveDetailsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		for(FTPCurveDetailsVb vObjVb : ftpCurveList){
			if(vObjVb.isChecked()){
				collTemp  = getQueryResultCurves(vObjVb);
				if(!vObjVb.isNewRecord()){
					if (collTemp!=null && collTemp.size() > 0){
						retVal = deleteFtpCurves(vObject);
						if(retVal != Constants.SUCCESSFUL_OPERATION){
							exceptionCode = getResultObject(retVal);
							return exceptionCode;
						}
					}
				}
				vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
				vObjVb.setMaker(getIntCurrentUserId());
				vObjVb.setVerifier(getIntCurrentUserId());
				retVal = doInsertionFtpCurves(vObjVb);
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
	protected int deleteFtpCurves(FTPCurveDetailsVb vObject){
		String query = "DELETE FROM FTP_CURVES WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')";
		Object args[] = {vObject.getFtpCurveId(),vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
}
}
*/