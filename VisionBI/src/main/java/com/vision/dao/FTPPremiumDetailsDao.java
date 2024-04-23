/*package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPPremiumDetailsVb;
import com.vision.vb.FTPCurveVb;


public class FTPPremiumDetailsDao extends AbstractDao<FTPCurveVb> {
	
	protected RowMapper getTenorBucketsMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				ftpSourceConfigVb.setTenorDecription(rs.getString("TENOR_DESCRIPTION"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	
	protected RowMapper getPremiumMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setTenorBucketApplicationCode(rs.getInt("FTP_TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setTenorBucketCode(rs.getString("FTP_TENOR_CODE"));
				ftpSourceConfigVb.setLpTenorBucketApplicationCode(rs.getInt("LP_TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setLpTenorBucketCode(rs.getString("LP_TENOR_CODE"));
				ftpSourceConfigVb.setVisionSbu(rs.getString("VISION_SBU_ATTRIBUTE"));
				ftpSourceConfigVb.setCurrency(rs.getString("CURRENCY"));
				ftpSourceConfigVb.setFtpRateId(rs.getString("LP_RATE_ID"));
				ftpSourceConfigVb.setSubRate(rs.getString("LP_RATE"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getlpRateMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpRateId(rs.getString("LP_RATE_ID"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setSubRate(rs.getString("LP_RATE"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	@Transactional(rollbackForClassName={"com.vision.exception.RuntimeCustomException"})
	public ExceptionCode addModifyFtpPremium(ExceptionCode exceptionCode,List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		for(FTPCurveVb vObjVb : ftpCurveList){
			if(vObjVb.isChecked()){
				collTemp  = getQueryResultPremium(vObjVb);
				if (collTemp!=null && collTemp.size() > 0){
					retVal = deleteFtpPremium(vObjVb);
					if(retVal != Constants.SUCCESSFUL_OPERATION){
						exceptionCode = getResultObject(retVal);
						return exceptionCode;
					}
				}
				vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
				vObjVb.setFtpCurveStatus(Constants.STATUS_ZERO);
				vObjVb.setMaker(getIntCurrentUserId());
				vObjVb.setVerifier(getIntCurrentUserId());
				retVal = doInsertionFtpPremium(vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
				exceptionCode =  addLpRates(exceptionCode,vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
			}
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public ExceptionCode addLpRates(ExceptionCode exceptionCode,FTPCurveVb vObjVb){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObjVb.setMaker(getIntCurrentUserId());
		int retVal = 0;
		String effectiveDate = "";
		if(ValidationUtil.isValid(vObjVb.getRateEffectiveDate()))
			effectiveDate = vObjVb.getRateEffectiveDate();
		else
			effectiveDate = vObjVb.getEffectiveDate();
		collTemp  = getQueryResultLPRates(vObjVb.getFtpRateId(),effectiveDate);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpLPRates(vObjVb);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
		vObjVb.setFtpCurveStatus(Constants.STATUS_ZERO);
		vObjVb.setMaker(getIntCurrentUserId());
		vObjVb.setVerifier(getIntCurrentUserId());
		retVal = doInsertionLPRates(vObjVb);
		if(retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			return exceptionCode;
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	protected int deleteFtpLPRates(FTPCurveVb vObject){
		String query = "DELETE FROM LP_TERM_RATES WHERE LP_RATE_ID = ? AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')";
		Object args[] = {vObject.getFtpRateId(),ValidationUtil.isValid(vObject.getRateEffectiveDate())?vObject.getRateEffectiveDate():vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	protected int doInsertionLPRates(FTPCurveVb vObject){
		String query = "Insert Into LP_TERM_RATES( " + 
		"LP_RATE_ID, EFFECTIVE_DATE, LP_RATE, LP_RATE_STATUS_NT, LP_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate)";

		Object args[] = {vObject.getFtpRateId(),ValidationUtil.isValid(vObject.getRateEffectiveDate())?vObject.getRateEffectiveDate():vObject.getEffectiveDate(), vObject.getSubRate().replaceAll(",", ""),
			 vObject.getFtpCurveStatusNt(), vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}
	public List<FTPGroupsVb> getQueryResultLPRates(String lpRateId,String effectiveDate){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("Select TAppr.LP_RATE_ID,TO_CHAR(TAppr.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TO_CHAR(TAppr.LP_RATE,'9,999.999990') LP_RATE From LP_TERM_RATES TAppr Where TAppr.LP_RATE_ID = ?"+
					" AND TAppr.EFFECTIVE_DATE = (Select Max(Effective_date) from lp_term_rates where Effective_date <= TO_DATE(?,'DD-MM-RRRR') and lp_rate_id = Tappr.Lp_rate_ID)");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = lpRateId;
		objParams[1] = effectiveDate;
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getlpRateMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public int doInsertionFtpPremium(FTPCurveVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_PREMIUMS( " + 
			" FTP_CURVE_ID, EFFECTIVE_DATE,FTP_TENOR_APPLICATION_CODE,FTP_TENOR_CODE,LP_TENOR_APPLICATION_CODE,LP_TENOR_CODE, "+
			" VISION_SBU_ATTRIBUTE,CURRENCY,LP_RATE_ID,FTP_PREMIUM_STATUS, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,SysDate, SysDate)";

		Object args[] = {vObject.getFtpCurveId(), vObject.getEffectiveDate(), vObject.getTenorBucketApplicationCode(),vObject.getTenorBucketCode(),
				vObject.getLpTenorBucketApplicationCode(),vObject.getLpTenorBucketCode(),vObject.getVisionSbu(),vObject.getCurrency(),vObject.getFtpRateId(),
				vObject.getFtpCurveStatus(),vObject.getRecordIndicator(),
				vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	protected int deleteFtpPremium(FTPCurveVb vObject){
		String query = "DELETE FROM FTP_PREMIUMS WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = to_date(?,'DD-MM-RRRR') AND FTP_TENOR_APPLICATION_CODE  = ? "
				+" AND FTP_TENOR_CODE = ? AND LP_TENOR_APPLICATION_CODE = ? "
				+" AND LP_TENOR_CODE = ? AND VISION_SBU_ATTRIBUTE = ? AND CURRENCY =?";
		Object args[] = {vObject.getFtpCurveId(),vObject.getEffectiveDate(),vObject.getTenorBucketApplicationCode(),vObject.getTenorBucketCode(),
				vObject.getLpTenorBucketApplicationCode(),vObject.getLpTenorBucketCode(),vObject.getVisionSbu(),vObject.getCurrency()};
		return getJdbcTemplate().update(query,args);
	}
	public List<FTPGroupsVb> getQueryResultPremium(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 8;
		String strBufApprove = new String("SELECT T1.FTP_CURVE_ID,TO_CHAR(T1.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE ,T1.FTP_TENOR_APPLICATION_CODE,T1.FTP_TENOR_CODE,"+
								" T1.LP_TENOR_APPLICATION_CODE,T1.LP_TENOR_CODE,"+ 
								" T1.VISION_SBU_ATTRIBUTE,T1.CURRENCY,T1.LP_RATE_ID,(SELECT LP_RATE FROM LP_TERM_RATES WHERE "+
								" LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE = T1.EFFECTIVE_DATE) LP_RATE "+
								" FROM FTP_PREMIUMS T1 WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = to_date(?,'DD-MM-RRRR') AND FTP_TENOR_APPLICATION_CODE  = ? "+ 
								" AND FTP_TENOR_CODE = ? AND LP_TENOR_APPLICATION_CODE = ? "+
								" AND LP_TENOR_CODE = ? AND VISION_SBU_ATTRIBUTE = ? AND CURRENCY =?");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		objParams[2] = dObj.getTenorBucketApplicationCode();
		objParams[3] = dObj.getTenorBucketCode();
		objParams[4] = dObj.getLpTenorBucketApplicationCode();
		objParams[5] = dObj.getLpTenorBucketCode();
		objParams[6] = dObj.getVisionSbu();
		objParams[7] = dObj.getCurrency();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getPremiumMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public String businessDay(){
		String sql ="SELECT TO_CHAR(BUSINESS_DATE,'DD-MM-RRRR') BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE "+ 
				" COUNTRY = (SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_COUNTRY') "+
				" AND LE_BOOK =(SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_LE_BOOK')";
		return getJdbcTemplate().queryForObject(sql,null,String.class);
	}
	@Override
	public List<FTPGroupsVb> getTenorCode(int tenorAppCode){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		String strBufApprove = new String("SELECT TENOR_CODE,TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = ? AND TENOR_STATUS = 0 ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = tenorAppCode;
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getTenorBucketsMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResultPremium(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 6;
		String strBufApprove = new String("SELECT T1.FTP_CURVE_ID,TO_CHAR(T1.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,T1.FTP_TENOR_APPLICATION_CODE,T1.FTP_TENOR_CODE,"+
								" T1.LP_TENOR_APPLICATION_CODE,T1.LP_TENOR_CODE,"+ 
								" T1.VISION_SBU_ATTRIBUTE,T1.CURRENCY,T1.LP_RATE_ID,"+
								" (SELECT TO_CHAR(LP_RATE,'9,990.999999990') LP_RATE FROM LP_TERM_RATES WHERE "+
								" LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM LP_TERM_RATES "+
								" WHERE LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE <= T1.EFFECTIVE_DATE)) LP_RATE "+
								" FROM FTP_PREMIUMS T1 WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = to_date(?,'DD-MM-RRRR') AND FTP_TENOR_APPLICATION_CODE  = ? "+ 
								" AND LP_TENOR_APPLICATION_CODE = ? "+
								" AND NVL(VISION_SBU_ATTRIBUTE,'000') = ? AND CURRENCY =?");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		objParams[2] = dObj.getTenorBucketApplicationCode();
		objParams[3] = dObj.getLpTenorBucketApplicationCode();
		objParams[4] = dObj.getVisionSbu();
		objParams[5] = dObj.getCurrency();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getPremiumMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	@Override
	protected void setServiceDefaults(){
		serviceName = "FTP Premium Details";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpPremiumDetails");
		tableName = "FTP_PREMIUM_DETAILS";
		childTableName = "FTP_PREMIUM_DETAILS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
}*/