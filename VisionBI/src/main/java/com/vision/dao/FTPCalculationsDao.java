package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

//import org.eclipse.jdt.internal.compiler.lookup.VoidTypeBinding;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.CommonVb;
import com.vision.vb.FTPCalculationsVb;
import com.vision.vb.FTPRefCodeVb;

@Component

public class FTPCalculationsDao extends AbstractDao<FTPCalculationsVb> {
	CommonDao commonDao;
	public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCalculationsVb ftpCalculationsVb = new FTPCalculationsVb();
				ftpCalculationsVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpCalculationsVb.setCountry(rs.getString("COUNTRY"));
				ftpCalculationsVb.setLeBook(rs.getString("LE_BOOK"));
				ftpCalculationsVb.setVersionNo(rs.getString("VERSION_NO"));
				ftpCalculationsVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				
				return ftpCalculationsVb;
			}
		};
		return mapper;
	}
	public RowMapper getDesciptionMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCalculationsVb ftpCalculationsVb = new FTPCalculationsVb();
				ftpCalculationsVb.setPoolCodeDesc(rs.getString("POOL_CODE_DESCRIPTION"));
				return ftpCalculationsVb;
			}
		};
		return mapper;
	}
	public RowMapper getProductDescriptionMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCalculationsVb ftpCalculationsVb = new FTPCalculationsVb();
				ftpCalculationsVb.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
				return ftpCalculationsVb;
			}
		};
		return mapper;
	}
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCalculationsVb ftpCalculationsVb = new FTPCalculationsVb();
				ftpCalculationsVb.setDataSourceAt(rs.getInt("DATA_SOURCE_AT"));
				ftpCalculationsVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpCalculationsVb.setCountry(rs.getString("COUNTRY"));
				ftpCalculationsVb.setLeBook(rs.getString("LE_BOOK"));
				ftpCalculationsVb.setCurrency(rs.getString("CURRENCY"));
				ftpCalculationsVb.setVersionNo(rs.getString("VERSION_NO"));
				ftpCalculationsVb.setPoolCode(rs.getString("POOL_CODE"));
				ftpCalculationsVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpCalculationsVb.setProduct(rs.getString("PRODUCT"));
				ftpCalculationsVb.setVisionSbuAt(rs.getInt("VISION_SBU_AT"));
				ftpCalculationsVb.setVisionSbu(rs.getString("VISION_SBU"));
				ftpCalculationsVb.setTenorRateStart(rs.getString("TENOR_START"));
				ftpCalculationsVb.setTenorRateEnd(rs.getString("TENOR_END"));
				ftpCalculationsVb.setInterestRateStart(rs.getString("INT_RATE_START"));
				ftpCalculationsVb.setInterestRateEnd(rs.getString("INT_RATE_END"));
				ftpCalculationsVb.setAvgStart(rs.getString("AVG_START"));
				ftpCalculationsVb.setAvgEnd(rs.getString("AVG_END"));
				ftpCalculationsVb.setOucAttribute(rs.getString("OUC_ATTRIBUTE"));
				ftpCalculationsVb.setOucAttributeLevel(rs.getString("OUC_ATTRIBUTE_LEVEL"));
				ftpCalculationsVb.setRiskAssetClass(rs.getString("RISK_ASSET_CLASS"));
				ftpCalculationsVb.setFtpMethodAt(rs.getInt("FTP_CALC_METHOD_AT"));
				ftpCalculationsVb.setFtpMethod(rs.getString("FTP_CALC_METHOD"));
				ftpCalculationsVb.setFtpRefCodeDr(rs.getString("FTP_REF_CODE_DR"));
				ftpCalculationsVb.setFtpRefCodeCr(rs.getString("FTP_REF_CODE_CR"));
				ftpCalculationsVb.setFtpRateDr(rs.getString("FTP_RATE_DR"));
				ftpCalculationsVb.setFtpRateCr(rs.getString("FTP_RATE_CR"));
				ftpCalculationsVb.setFtpEngineStatusNt(rs.getInt("FTP_ENGINE_STATUS_NT"));
				ftpCalculationsVb.setFtpEngineStatus(rs.getInt("FTP_ENGINE_STATUS"));
				ftpCalculationsVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpCalculationsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpCalculationsVb.setMaker(rs.getLong("MAKER"));
				ftpCalculationsVb.setVerifier(rs.getLong("VERIFIER"));
				ftpCalculationsVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpCalculationsVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpCalculationsVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				return ftpCalculationsVb;
			}
		};
		return mapper;
	}
	public RowMapper getOucLevelMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCalculationsVb ftpCalculationsVb = new FTPCalculationsVb();
				ftpCalculationsVb.setOucAttributeLevel(rs.getString("OUC_ATTRIBUTE_LEVEL"));
				ftpCalculationsVb.setOucAttributeLevelDesc(rs.getString("OUC_ATTRIBUTE_LEVEL_DESC"));
				return ftpCalculationsVb;
			}
		};
		return mapper;
	}
	public List<AlphaSubTabVb> getOucAttributeLevel(){
		StringBuffer strQueryAppr = new StringBuffer("select OUC_ATTRIBUTE_LEVEL,OUC_ATTRIBUTE_LEVEL_DESC from ouc_attribute_levels");
		try
		{ 
			return getJdbcTemplate().query(strQueryAppr.toString(),getOucLevelMapper());
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults OUC Attribute Level Exception :   ");
			logger.error(((strQueryAppr == null) ? "strQueryAppr is null" : strQueryAppr.toString()));
			return null;
		}
	}
	public List<FTPCalculationsVb> getQueryPopupResults(FTPCalculationsVb dObj){
		dObj.setVerificationRequired(false);
		Vector<Object> params = new Vector<Object>();
		if(ValidationUtil.isValid(dObj.getAvgStart())){
			dObj.setAvgStart(dObj.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd())){
			dObj.setAvgEnd(dObj.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getInterestRateStart())){
			dObj.setInterestRateStart(dObj.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
			dObj.setInterestRateEnd(dObj.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getFtpRateCr())){
			dObj.setFtpRateCr(dObj.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getFtpRateDr())){ 
			dObj.setFtpRateDr(dObj.getFtpRateDr().replaceAll(",", ""));
		}
		StringBuffer strBufApprove = new StringBuffer("Select Distinct TAppr.DATA_SOURCE, " + 
			"  To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE, TAppr.COUNTRY, TAppr.LE_BOOK,TAppr.VERSION_NO " +
			" From FTP_ENGINE TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From FTP_ENGINE_PEND TPend Where " + 
			"TAppr.DATA_SOURCE = TPend.DATA_SOURCE " + 
			"And TAppr.COUNTRY = TPend.COUNTRY " + 
			"And TAppr.LE_BOOK = TPend.LE_BOOK " + 
			"And TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE " +
			"And TAppr.VERSION_NO = TPend.VERSION_NO )");

		StringBuffer strBufPending = new StringBuffer("Select Distinct TPend.DATA_SOURCE, " + 
			" To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,  TPend.COUNTRY, TPend.LE_BOOK,TPend.VERSION_NO " +
			" From FTP_ENGINE_PEND TPend ");


		try
		{
			if (ValidationUtil.isValid(dObj.getDataSource()) && !"-1".equalsIgnoreCase(dObj.getDataSource()))
			{
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DATA_SOURCE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getVisionSbu()) && !"-1".equalsIgnoreCase(dObj.getVisionSbu()))
			{
				params.addElement(dObj.getVisionSbu());
				CommonUtils.addToQuery("TAppr.VISION_SBU = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.VISION_SBU = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getEffectiveDate())){
				params.addElement(dObj.getEffectiveDate().toUpperCase());
				CommonUtils.addToQuery("To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') = ?", strBufApprove);
				CommonUtils.addToQuery("To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement(dObj.getCountry().toUpperCase());
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY)  = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COUNTRY)  = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement(dObj.getLeBook().toUpperCase());
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK)  = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.LE_BOOK)  = ?", strBufPending);
			}
			/*if (ValidationUtil.isValid(dObj.getCurrency()))
			{
				params.addElement("%" + dObj.getCurrency().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.CURRENCY) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.CURRENCY) like ?", strBufPending);
			}*/
			if (ValidationUtil.isValid(dObj.getPoolCode()))
			{
				params.addElement("%" + dObj.getPoolCode().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.POOL_CODE) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.POOL_CODE) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getProduct()))
			{
				params.addElement("%" + dObj.getProduct().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.PRODUCT) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.PRODUCT) like ?", strBufPending);
			}
			if (dObj.getFtpEngineStatus() != -1)
			{
				params.addElement(dObj.getFtpEngineStatus());
				CommonUtils.addToQuery("TAppr.FTP_ENGINE_STATUS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_ENGINE_STATUS = ?", strBufPending);
			}
			//check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1){
				if (dObj.getRecordIndicator() > 3){
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
					CommonUtils.addToQuery("TPend.RECORD_INDICATOR > ?", strBufPending);
				}else{
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
					CommonUtils.addToQuery("TPend.RECORD_INDICATOR = ?", strBufPending);
				}
			}
			//Update Restriction - UBA
			/*VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
			if(("Y".equalsIgnoreCase(visionUsersVb.getUpdateRestriction()) || "Y".equalsIgnoreCase(visionUsersVb.getAutoUpdateRestriction()))){
				if(ValidationUtil.isValid(visionUsersVb.getCountry())){
					CommonUtils.addToQuery("TAppr.COUNTRY||'-'||TAppr.LE_BOOK IN ("+visionUsersVb.getCountry()+") ", strBufApprove);
					CommonUtils.addToQuery("TPend.COUNTRY||'-'||TPend.LE_BOOK IN ("+visionUsersVb.getCountry()+") ", strBufPending);
				}
				if(ValidationUtil.isValid(visionUsersVb.getSbuCode())){
					CommonUtils.addToQuery("TAppr.VISION_SBU IN ("+visionUsersVb.getSbuCode()+") ", strBufApprove);
					CommonUtils.addToQuery("TPend.VISION_SBU IN ("+visionUsersVb.getSbuCode()+") ", strBufPending);
				}
			}*/
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			logger.error("UNION");
			logger.error(((strBufPending==null)? "strBufPending is Null":strBufPending.toString()));

			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}

		String orderBy = " Order By  EFFECTIVE_DATE, DATA_SOURCE, COUNTRY, LE_BOOK, VERSION_NO ";
		return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params, getQueryPopupMapper());
	}

	public List<FTPCalculationsVb> getQueryResults(FTPCalculationsVb dObj, int intStatus){
		List<FTPCalculationsVb> collTemp = null;
		Vector<Object> params= new Vector<Object>();
		if(ValidationUtil.isValid(dObj.getAvgStart())){
			dObj.setAvgStart(dObj.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd())){
			dObj.setAvgEnd(dObj.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getInterestRateStart())){
			dObj.setInterestRateStart(dObj.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
			dObj.setInterestRateEnd(dObj.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getFtpRateCr())){
			dObj.setFtpRateCr(dObj.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getFtpRateDr())){ 
			dObj.setFtpRateDr(dObj.getFtpRateDr().replaceAll(",", ""));
		}
		
		StringBuffer strBufApprove = new StringBuffer(" SELECT DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,TO_CHAR(FTP_RATE_DR,'999,990.90') FTP_RATE_DR,TO_CHAR(FTP_RATE_CR,'999,990.90') FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ENGINE Where COUNTRY = ?  " + 
				"		 And LE_BOOK = ? And VERSION_NO = ? " +
				" 		 And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') and Data_Source = ? ");
		StringBuffer strQueryPend = new StringBuffer(" SELECT DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ENGINE Where COUNTRY = ?  " + 
				"		 And LE_BOOK = ? And VERSION_NO = ? And VERSION_NO = ? " +
				" 		 And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') and Data_Source = ? ");
		
		params.add(dObj.getCountry());	//[COUNTRY]
		params.add(dObj.getLeBook());	//[LE_BOOK]
		params.add(dObj.getVersionNo());	//[CURRENCY]
		params.add(dObj.getEffectiveDate());	//[EFFECTIVE_DATE]
		params.add(dObj.getDataSource());	//[DATA_SOURCE]
		
		if(ValidationUtil.isValid(dObj.getVisionSbu()) && !"-1".equalsIgnoreCase(dObj.getVisionSbu())){
			strBufApprove.append("and Vision_Sbu = '"+dObj.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getProduct())){
			strBufApprove.append("and Product = '"+dObj.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getTenorRateStart())){
			strBufApprove.append("and TENOR_START = '"+dObj.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getTenorRateEnd())){
			strBufApprove.append("and TENOR_END = '"+dObj.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getInterestRateStart())){
			strBufApprove.append("and INT_RATE_START = '"+dObj.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
			strBufApprove.append("and INT_RATE_END = '"+dObj.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getAvgStart() )){
			strBufApprove.append("and AVG_START = '"+dObj.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd() )){
			strBufApprove.append("and AVG_END = '"+dObj.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getOucAttribute())){
			strBufApprove.append("and OUC_ATTRIBUTE = '"+dObj.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(dObj.getOucAttributeLevel())){
			strBufApprove.append("and OUC_ATTRIBUTE_LEVEL = '"+dObj.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getRiskAssetClass())){
			strBufApprove.append("and RISK_ASSET_CLASS = '"+dObj.getRiskAssetClass()+"' ");
		}
		if (dObj.getFtpEngineStatus() != -1)
		{
			strBufApprove.append(" AND FTP_ENGINE_STATUS = '"+dObj.getFtpEngineStatus()+"'");
		}
		try
		{
			if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0)
			{
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strBufApprove.toString(),params.toArray(),getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),params.toArray(),getMapper());
			}
			return collTemp;
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			if(intStatus == 0)
				logger.error(((strBufApprove == null) ? "strQueryAppr is null" : strBufApprove.toString()));
			else
				logger.error(((strQueryPend == null) ? "strQueryPend is null" : strQueryPend.toString()));

			return null;
		}
	}

	public List<FTPCalculationsVb> getQueryResultsForReview(FTPCalculationsVb dObj, int intStatus){
		List<FTPCalculationsVb> collTemp = null;
		Vector<Object> params= new Vector<Object>();
		if(ValidationUtil.isValid(dObj.getAvgStart())){
			dObj.setAvgStart(dObj.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd())){
			dObj.setAvgEnd(dObj.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getInterestRateStart())){
			dObj.setInterestRateStart(dObj.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
			dObj.setInterestRateEnd(dObj.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getFtpRateCr())){
			dObj.setFtpRateCr(dObj.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getFtpRateDr())){ 
			dObj.setFtpRateDr(dObj.getFtpRateDr().replaceAll(",", ""));
		}
		StringBuffer strBufApprove = new StringBuffer(" SELECT DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ENGINE Where COUNTRY = ?  " + 
				"		 And LE_BOOK = ? And CURRENCY = ? And POOL_CODE = ? " +
				" 		 And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') and Data_Source = ? And Version_No =? ");
		StringBuffer strQueryPend = new StringBuffer(" SELECT DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ENGINE Where COUNTRY = ?  " + 
				"		 And LE_BOOK = ? And CURRENCY = ? And POOL_CODE = ? " +
				" 		 And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') and Data_Source = ? And Version_No =? ");
		
		params.add(dObj.getCountry());	//[COUNTRY]
		params.add(dObj.getLeBook());	//[LE_BOOK]
		params.add(dObj.getCurrency());	//[CURRENCY]
		params.add(dObj.getPoolCode());	//[POOL_CODE]
		params.add(dObj.getEffectiveDate());	//[EFFECTIVE_DATE]
		params.add(dObj.getDataSource());	//[DATA_SOURCE]
		params.add(dObj.getVersionNo());
		
		if(ValidationUtil.isValid(dObj.getVisionSbu()) && !"-1".equalsIgnoreCase(dObj.getVisionSbu())){
			strBufApprove.append("and Vision_Sbu = '"+dObj.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getProduct())){
			strBufApprove.append("and Product = '"+dObj.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getTenorRateStart())){
			strBufApprove.append("and TENOR_START = '"+dObj.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getTenorRateEnd())){
			strBufApprove.append("and TENOR_END = '"+dObj.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getInterestRateStart())){
			strBufApprove.append("and INT_RATE_START = '"+dObj.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
			strBufApprove.append("and INT_RATE_END = '"+dObj.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getAvgStart() )){
			strBufApprove.append("and AVG_START = '"+dObj.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd() )){
			strBufApprove.append("and AVG_END = '"+dObj.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getOucAttribute())){
			strBufApprove.append("and OUC_ATTRIBUTE = '"+dObj.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(dObj.getOucAttributeLevel())){
			strBufApprove.append("and OUC_ATTRIBUTE_LEVEL = '"+dObj.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getRiskAssetClass()) && !"-1".equalsIgnoreCase(dObj.getRiskAssetClass())){
			strBufApprove.append("and RISK_ASSET_CLASS = '"+dObj.getRiskAssetClass()+"' ");
		}
				
		try
		{
			if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0)
			{
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strBufApprove.toString(),params.toArray(),getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),params.toArray(),getMapper());
			}
			return collTemp;
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			if(intStatus == 0)
				logger.error(((strBufApprove == null) ? "strQueryAppr is null" : strBufApprove.toString()));
			else
				logger.error(((strQueryPend == null) ? "strQueryPend is null" : strQueryPend.toString()));

			return null;
		}
	}
	public int getCountOfFtpRate(FTPCalculationsVb dObj, int intStatus){
		int dataCnt = 0;
		try{
			String sqlApprove = "Select count(1) from FTP_RATES Where COUNTRY = '"+dObj.getCountry()+"'  " + 
					" And LE_BOOK = '"+dObj.getLeBook()+"' And CURRENCY = '"+dObj.getCurrency()+"' And POOL_CODE = '"+dObj.getPoolCode()+"' " +
					" And EFFECTIVE_DATE = To_Date('"+dObj.getEffectiveDate()+"', 'DD-MM-YYYY') and Data_Source = '"+dObj.getDataSource()+"' ";
				  
			String sqlPend = "Select count(1) from FTP_RATES_PEND Where COUNTRY = '"+dObj.getCountry()+"'  " + 
					" And LE_BOOK = '"+dObj.getLeBook()+"' And CURRENCY = '"+dObj.getCurrency()+"' And POOL_CODE = '"+dObj.getPoolCode()+"' " +
					" And EFFECTIVE_DATE = To_Date('"+dObj.getEffectiveDate()+"', 'DD-MM-YYYY') and Data_Source = '"+dObj.getDataSource()+"' ";
			
			if(ValidationUtil.isValid(dObj.getVisionSbu()) && !"-1".equalsIgnoreCase(dObj.getVisionSbu())){
				sqlApprove = sqlApprove + " and Vision_Sbu = '"+dObj.getVisionSbu()+"' ";
				sqlPend = sqlPend + " and Vision_Sbu = '"+dObj.getVisionSbu()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getProduct())){
				sqlApprove= sqlApprove+" and Product = '"+dObj.getProduct()+"' ";
				sqlPend= sqlPend+" and Product = '"+dObj.getProduct()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getTenorRateStart())){
				sqlApprove= sqlApprove+" and TENOR_START = '"+dObj.getTenorRateStart()+"' ";
				sqlPend= sqlPend+"and TENOR_START = '"+dObj.getTenorRateStart()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getTenorRateEnd())){
				sqlApprove= sqlApprove+" and TENOR_END = '"+dObj.getTenorRateEnd()+"' ";
				sqlPend= sqlPend+" and TENOR_END = '"+dObj.getTenorRateEnd()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getInterestRateStart())){
				sqlApprove= sqlApprove+" and INT_RATE_START = '"+dObj.getInterestRateStart()+"' ";
				sqlPend= sqlPend+" and INT_RATE_START = '"+dObj.getInterestRateStart()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
				sqlApprove= sqlApprove+" and INT_RATE_END = '"+dObj.getInterestRateEnd()+"' ";
				sqlPend= sqlPend+" and INT_RATE_END = '"+dObj.getInterestRateEnd()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getAvgStart() )){
				sqlApprove= sqlApprove+" and AVG_START = '"+dObj.getAvgStart()+"' ";
				sqlPend= sqlPend+" and AVG_START = '"+dObj.getAvgStart()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getAvgEnd() )){
				sqlApprove= sqlApprove+" and AVG_END = '"+dObj.getAvgEnd()+"' ";
				sqlPend= sqlPend+" and AVG_END = '"+dObj.getAvgEnd()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getOucAttribute())){
				sqlApprove= sqlApprove+" and OUC_ATTRIBUTE = '"+dObj.getOucAttribute()+"' ";
				sqlPend= sqlPend+" and OUC_ATTRIBUTE = '"+dObj.getOucAttribute()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(dObj.getOucAttributeLevel())){
				sqlApprove= sqlApprove+" and OUC_ATTRIBUTE_LEVEL = '"+dObj.getOucAttributeLevel()+"' ";
				sqlPend= sqlPend+" and OUC_ATTRIBUTE_LEVEL = '"+dObj.getOucAttributeLevel()+"' ";
			}
			if(ValidationUtil.isValid(dObj.getRiskAssetClass()) && !"-1".equalsIgnoreCase(dObj.getRiskAssetClass())){
				sqlApprove= sqlApprove+" and RISK_ASSET_CLASS = '"+dObj.getRiskAssetClass()+"' ";
				sqlPend= sqlPend+" and RISK_ASSET_CLASS = '"+dObj.getRiskAssetClass()+"' ";
			}
			if(intStatus == 0){
				dataCnt = getJdbcTemplate().queryForObject(sqlApprove, Integer.class);
			}else{
				dataCnt = getJdbcTemplate().queryForObject(sqlPend, Integer.class);
			}
			return dataCnt;
		}
		catch(Exception e){
			e.getStackTrace();
			return 0;
		}
	}
	
	public List<FTPCalculationsVb> getQueryResultsForFtpRate(FTPCalculationsVb dObj, int intStatus){
		List<FTPCalculationsVb> collTemp = null;
		Vector<Object> params= new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer(" SELECT DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_RATES Where COUNTRY = ?  " + 
				"		 And LE_BOOK = ? And CURRENCY = ? And POOL_CODE = ? " +
				" 		 And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') and Data_Source = ? ");
		StringBuffer strQueryPend = new StringBuffer(" SELECT DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ENGINE Where COUNTRY = ?  " + 
				"		 And LE_BOOK = ? And CURRENCY = ? And POOL_CODE = ? " +
				" 		 And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') and Data_Source = ? ");
		
		params.add(dObj.getCountry());	//[COUNTRY]
		params.add(dObj.getLeBook());	//[LE_BOOK]
		params.add(dObj.getCurrency());	//[CURRENCY]
		params.add(dObj.getPoolCode());	//[POOL_CODE]
		params.add(dObj.getEffectiveDate());	//[EFFECTIVE_DATE]
		params.add(dObj.getDataSource());	//[DATA_SOURCE]
		params.add(dObj.getVersionNo());
		
		if(ValidationUtil.isValid(dObj.getVisionSbu()) && !"-1".equalsIgnoreCase(dObj.getVisionSbu())){
			strBufApprove.append("and Vision_Sbu = '"+dObj.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getProduct())){
			strBufApprove.append("and Product = '"+dObj.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getTenorRateStart())){
			strBufApprove.append("and TENOR_START = '"+dObj.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getTenorRateEnd())){
			strBufApprove.append("and TENOR_END = '"+dObj.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getInterestRateStart())){
			strBufApprove.append("and INT_RATE_START = '"+dObj.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getInterestRateEnd())){
			strBufApprove.append("and INT_RATE_END = '"+dObj.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getAvgStart() )){
			strBufApprove.append("and AVG_START = '"+dObj.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd() )){
			strBufApprove.append("and AVG_END = '"+dObj.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getOucAttribute())){
			strBufApprove.append("and OUC_ATTRIBUTE = '"+dObj.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(dObj.getOucAttributeLevel())){
			strBufApprove.append("and OUC_ATTRIBUTE_LEVEL = '"+dObj.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(dObj.getRiskAssetClass()) && !"-1".equalsIgnoreCase(dObj.getRiskAssetClass())){
			strBufApprove.append("and RISK_ASSET_CLASS = '"+dObj.getRiskAssetClass()+"' ");
		}
				
		try
		{
			if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0)
			{
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strBufApprove.toString(),params.toArray(),getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),params.toArray(),getMapper());
			}
			return collTemp;
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			if(intStatus == 0)
				logger.error(((strBufApprove == null) ? "strQueryAppr is null" : strBufApprove.toString()));
			else
				logger.error(((strQueryPend == null) ? "strQueryPend is null" : strQueryPend.toString()));

			return null;
		}
	}
	@Override
	protected List<FTPCalculationsVb> selectApprovedRecord(FTPCalculationsVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<FTPCalculationsVb> doSelectPendingRecord(FTPCalculationsVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}

	@Override
	protected void setServiceDefaults(){
		serviceName = "FTPRates";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpRates");
		tableName = "FTP_ENGINE";
		childTableName = "FTPL_RATES";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}

	@Override
	protected int getStatus(FTPCalculationsVb records){return records.getFtpEngineStatus();}

	@Override
	protected void setStatus(FTPCalculationsVb vObject,int status){vObject.setFtpEngineStatus(status);}

	@Override
	protected int doInsertionAppr(FTPCalculationsVb ftpCalculationsVb){
		if(ValidationUtil.isValid(ftpCalculationsVb.getAvgStart())){
			ftpCalculationsVb.setAvgStart(ftpCalculationsVb.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getAvgEnd())){
			ftpCalculationsVb.setAvgEnd(ftpCalculationsVb.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getInterestRateStart())){
			ftpCalculationsVb.setInterestRateStart(ftpCalculationsVb.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getInterestRateEnd())){
			ftpCalculationsVb.setInterestRateEnd(ftpCalculationsVb.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getFtpRateCr())){
			ftpCalculationsVb.setFtpRateCr(ftpCalculationsVb.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getFtpRateDr())){
			ftpCalculationsVb.setFtpRateDr(ftpCalculationsVb.getFtpRateDr().replaceAll(",", ""));
		}
		
		String query = "Insert Into FTP_ENGINE(DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION) " + 
				" Values (?,?,?,?,?,?,?,To_date(?,'DD-MM-YYYY') ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate)";

		Object args[] = {ftpCalculationsVb.getDataSourceAt(),ftpCalculationsVb.getDataSource(),ftpCalculationsVb.getCountry(),              
				ftpCalculationsVb.getLeBook(),ftpCalculationsVb.getCurrency(),ftpCalculationsVb.getVersionNo(),            
				ftpCalculationsVb.getPoolCode(),ftpCalculationsVb.getEffectiveDate(),ftpCalculationsVb.getProduct(),              
				ftpCalculationsVb.getVisionSbuAt(), ftpCalculationsVb.getVisionSbu(),ftpCalculationsVb.getTenorRateStart(),       
				ftpCalculationsVb.getTenorRateEnd(), ftpCalculationsVb.getInterestRateStart(),ftpCalculationsVb.getInterestRateEnd(),      
				ftpCalculationsVb.getAvgStart(),ftpCalculationsVb.getAvgEnd(),ftpCalculationsVb.getOucAttribute(),ftpCalculationsVb.getOucAttributeLevel(),    
				ftpCalculationsVb.getRiskAssetClass(),ftpCalculationsVb.getFtpMethodAt(),ftpCalculationsVb.getFtpMethod(),            
				ftpCalculationsVb.getFtpRefCodeDr(),ftpCalculationsVb.getFtpRefCodeCr(),ftpCalculationsVb.getFtpRateDr(),            
				ftpCalculationsVb.getFtpRateCr(),ftpCalculationsVb.getFtpEngineStatusNt(),ftpCalculationsVb.getFtpEngineStatus(),      
				ftpCalculationsVb.getRecordIndicatorNt(),ftpCalculationsVb.getRecordIndicator(),ftpCalculationsVb.getMaker(),                
				ftpCalculationsVb.getVerifier(),ftpCalculationsVb.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public int doInsertionFtpRates(FTPCalculationsVb vObject,int status){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateCr())){
			vObject.setFtpRateCr(vObject.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateDr())){
			vObject.setFtpRateDr(vObject.getFtpRateDr().replaceAll(",", ""));
		}
		String query = "";
		if(status == 0){
			query = "Insert Into FTP_RATES( " + 
				" DATA_SOURCE_AT, DATA_SOURCE, VISION_SBU, COUNTRY, LE_BOOK, CURRENCY, POOL_CODE, PRODUCT,EFFECTIVE_DATE, TENOR_START,TENOR_END, INT_RATE_START, INT_RATE_END, AVG_START, AVG_END, " + 
				" DEBIT_POOL_RATE, CREDIT_POOL_RATE, POOL_RATE_STATUS_NT, POOL_RATE_STATUS, RECORD_INDICATOR_NT, " + 
				" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, OUC_ATTRIBUTE, OUC_ATTRIBUTE_LEVEL, RISK_ASSET_CLASS) " + 
				" Values (?, ?, ?, ?, ?, ?, ?, ?, To_Date(?, 'DD-MM-YYYY'), ?, ?,?, ?, ?, ?, TO_NUMBER(?,'9,990.9999999'), TO_NUMBER(?,'9,990.9999999'), ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate, ?, ?, ?)";		
		}else{
			query = "Insert Into FTP_RATES_PEND( " + 
					" DATA_SOURCE_AT, DATA_SOURCE, VISION_SBU, COUNTRY, LE_BOOK, CURRENCY, POOL_CODE, PRODUCT,EFFECTIVE_DATE, TENOR_START,TENOR_END, INT_RATE_START, INT_RATE_END, AVG_START, AVG_END, " + 
					" DEBIT_POOL_RATE, CREDIT_POOL_RATE, POOL_RATE_STATUS_NT, POOL_RATE_STATUS, RECORD_INDICATOR_NT, " + 
					" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, OUC_ATTRIBUTE, OUC_ATTRIBUTE_LEVEL, RISK_ASSET_CLASS) " + 
					" Values (?, ?, ?, ?, ?, ?, ?, ?, To_Date(?, 'DD-MM-YYYY'), ?, ?,?, ?, ?, ?, TO_NUMBER(?,'9,990.9999999'), TO_NUMBER(?,'9,990.9999999'), ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate, ?, ?, ?)";
		}
		Object args[] = {10, vObject.getDataSource(),
			 vObject.getVisionSbu(), vObject.getCountry(), vObject.getLeBook(), vObject.getCurrency(),
			 vObject.getPoolCode(), vObject.getProduct(),vObject.getEffectiveDate(), vObject.getTenorRateStart(), vObject.getTenorRateEnd(), vObject.getInterestRateStart(), vObject.getInterestRateEnd(),
			 vObject.getAvgStart(), vObject.getAvgEnd(), vObject.getFtpRateDr(), vObject.getFtpRateCr(),
			 1, 0, vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),vObject.getOucAttribute(),
			 vObject.getOucAttributeLevel(), vObject.getRiskAssetClass()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPend(FTPCalculationsVb ftpCalculationsVb){
		if(ValidationUtil.isValid(ftpCalculationsVb.getAvgStart())){
			ftpCalculationsVb.setAvgStart(ftpCalculationsVb.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getAvgEnd())){
			ftpCalculationsVb.setAvgEnd(ftpCalculationsVb.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getInterestRateStart())){
			ftpCalculationsVb.setInterestRateStart(ftpCalculationsVb.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getInterestRateEnd())){
			ftpCalculationsVb.setInterestRateEnd(ftpCalculationsVb.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getFtpRateCr())){
			ftpCalculationsVb.setFtpRateCr(ftpCalculationsVb.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getFtpRateDr())){
			ftpCalculationsVb.setFtpRateDr(ftpCalculationsVb.getFtpRateDr().replaceAll(",", ""));
		}
		String query = "Insert Into FTP_ENGINE_pend (DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION) " + 
				" Values (?,?,?,?,?,?,?,To_date(?,'DD-MM-YYYY') ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate)";

		Object args[] = {ftpCalculationsVb.getDataSourceAt(),ftpCalculationsVb.getDataSource(),ftpCalculationsVb.getCountry(),              
				ftpCalculationsVb.getLeBook(),ftpCalculationsVb.getCurrency(),ftpCalculationsVb.getVersionNo(),            
				ftpCalculationsVb.getPoolCode(),ftpCalculationsVb.getEffectiveDate(),ftpCalculationsVb.getProduct(),              
				ftpCalculationsVb.getVisionSbuAt(), ftpCalculationsVb.getVisionSbu(),ftpCalculationsVb.getTenorRateStart(),       
				ftpCalculationsVb.getTenorRateEnd(), ftpCalculationsVb.getInterestRateStart(),ftpCalculationsVb.getInterestRateEnd(),      
				ftpCalculationsVb.getAvgStart(),ftpCalculationsVb.getAvgEnd(),ftpCalculationsVb.getOucAttribute(),ftpCalculationsVb.getOucAttributeLevel(),    
				ftpCalculationsVb.getRiskAssetClass(),ftpCalculationsVb.getFtpMethodAt(),ftpCalculationsVb.getFtpMethod(),            
				ftpCalculationsVb.getFtpRefCodeDr(),ftpCalculationsVb.getFtpRefCodeCr(),ftpCalculationsVb.getFtpRateDr(),            
				ftpCalculationsVb.getFtpRateCr(),ftpCalculationsVb.getFtpEngineStatusNt(),ftpCalculationsVb.getFtpEngineStatus(),      
				ftpCalculationsVb.getRecordIndicatorNt(),ftpCalculationsVb.getRecordIndicator(),ftpCalculationsVb.getMaker(),                
				ftpCalculationsVb.getVerifier(),ftpCalculationsVb.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPendWithDc(FTPCalculationsVb ftpCalculationsVb){
		if(ValidationUtil.isValid(ftpCalculationsVb.getAvgStart())){
			ftpCalculationsVb.setAvgStart(ftpCalculationsVb.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getAvgEnd())){
			ftpCalculationsVb.setAvgEnd(ftpCalculationsVb.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getInterestRateStart())){
			ftpCalculationsVb.setInterestRateStart(ftpCalculationsVb.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getInterestRateEnd())){
			ftpCalculationsVb.setInterestRateEnd(ftpCalculationsVb.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getFtpRateCr())){
			ftpCalculationsVb.setFtpRateCr(ftpCalculationsVb.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(ftpCalculationsVb.getFtpRateDr())){
			ftpCalculationsVb.setFtpRateDr(ftpCalculationsVb.getFtpRateDr().replaceAll(",", ""));
		}
		String query = "Insert Into FTP_ENGINE_pend (DATA_SOURCE_AT,       "+
				"        DATA_SOURCE,COUNTRY,LE_BOOK,CURRENCY,VERSION_NO,POOL_CODE,            "+
				"        EFFECTIVE_DATE,PRODUCT,VISION_SBU_AT,VISION_SBU,TENOR_START,          "+
				"        TENOR_END,INT_RATE_START,INT_RATE_END,AVG_START,AVG_END,OUC_ATTRIBUTE,        "+
				"        OUC_ATTRIBUTE_LEVEL,RISK_ASSET_CLASS,FTP_CALC_METHOD_AT,FTP_CALC_METHOD,      "+
				"        FTP_REF_CODE_DR,FTP_REF_CODE_CR,FTP_RATE_DR,FTP_RATE_CR,FTP_ENGINE_STATUS_NT,FTP_ENGINE_STATUS,    "+
				"        RECORD_INDICATOR_NT,RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,      "+
				"        DATE_LAST_MODIFIED,DATE_CREATION) " + 
				" Values (?,?,?,?,?,?,?,To_date(?,'DD-MM-YYYY') ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate)";

		Object args[] = {ftpCalculationsVb.getDataSourceAt(),ftpCalculationsVb.getDataSource(),ftpCalculationsVb.getCountry(),              
				ftpCalculationsVb.getLeBook(),ftpCalculationsVb.getCurrency(),ftpCalculationsVb.getVersionNo(),            
				ftpCalculationsVb.getPoolCode(),ftpCalculationsVb.getEffectiveDate(),ftpCalculationsVb.getProduct(),              
				ftpCalculationsVb.getVisionSbuAt(), ftpCalculationsVb.getVisionSbu(),ftpCalculationsVb.getTenorRateStart(),       
				ftpCalculationsVb.getTenorRateEnd(), ftpCalculationsVb.getInterestRateStart(),ftpCalculationsVb.getInterestRateEnd(),      
				ftpCalculationsVb.getAvgStart(),ftpCalculationsVb.getAvgEnd(),ftpCalculationsVb.getOucAttribute(),ftpCalculationsVb.getOucAttributeLevel(),    
				ftpCalculationsVb.getRiskAssetClass(),ftpCalculationsVb.getFtpMethodAt(),ftpCalculationsVb.getFtpMethod(),            
				ftpCalculationsVb.getFtpRefCodeDr(),ftpCalculationsVb.getFtpRefCodeCr(),ftpCalculationsVb.getFtpRateDr(),            
				ftpCalculationsVb.getFtpRateCr(),ftpCalculationsVb.getFtpEngineStatusNt(),ftpCalculationsVb.getFtpEngineStatus(),      
				ftpCalculationsVb.getRecordIndicatorNt(),ftpCalculationsVb.getRecordIndicator(),ftpCalculationsVb.getMaker(),                
				ftpCalculationsVb.getVerifier(),ftpCalculationsVb.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdateAppr(FTPCalculationsVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateCr())){
			vObject.setFtpRateCr(vObject.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateDr())){ 
			vObject.setFtpRateDr(vObject.getFtpRateDr().replaceAll(",", ""));
		}
		StringBuilder query = new StringBuilder("Update FTP_ENGINE Set " +
			" FTP_CALC_METHOD = ?,FTP_REF_CODE_DR = ?,FTP_REF_CODE_CR = ?,FTP_RATE_DR = ?,FTP_RATE_CR=?, " +	                                 
			" FTP_ENGINE_STATUS = ?,RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?,"+
			" DATE_LAST_MODIFIED = sysdate WHERE COUNTRY = ? AND LE_BOOK = ? AND DATA_SOURCE = ? AND VERSION_NO = ? "+
			" AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-YYYY') AND CURRENCY = ? AND POOL_CODE = ? ");
		
		Object args[] = {vObject.getFtpMethod(),vObject.getFtpRefCodeDr(),vObject.getFtpRefCodeCr(),vObject.getFtpRateDr(),vObject.getFtpRateCr(),
				vObject.getFtpEngineStatus(),vObject.getRecordIndicator(),vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(),vObject.getVersionNo(),
				vObject.getEffectiveDate(),vObject.getCurrency(),vObject.getPoolCode()};
		
		if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
			query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getProduct())){
			query.append("and Product = '"+vObject.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateStart())){
			query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
			query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgStart() )){
			query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd() )){
			query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
			query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
			query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
		}
		return getJdbcTemplate().update(query.toString(),args);
	}

	@Override
	protected int doUpdatePend(FTPCalculationsVb vObject){
		StringBuilder query = new StringBuilder("Update FTP_ENGINE_PEND  Set " +
				" FTP_CALC_METHOD = ?,FTP_REF_CODE_DR = ?,FTP_REF_CODE_CR = ?,FTP_RATE_DR = ?,FTP_RATE_CR=? " +	                                 
				" FTP_ENGINE_STATUS = ?,RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?,"+
				" DATE_LAST_MODIFIED = sysdate WHERE COUNTRY = ? AND LE_BOOK = ? AND DATA_SOURCE = ? AND VERSION_NO = ? "+
				" AND EFFECTIVE_DATE = ? AND CURRENCY = ? AND POOL_CODE = ? ");
			
			Object args[] = {vObject.getFtpMethod(),vObject.getFtpRefCodeDr(),vObject.getFtpRefCodeCr(),vObject.getFtpRateDr(),vObject.getFtpRateCr(),
					vObject.getFtpEngineStatus(),vObject.getRecordIndicator(),vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
					vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(),vObject.getVersionNo(),
					vObject.getEffectiveDate(),vObject.getCurrency(),vObject.getPoolCode()};
			
			if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
				query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getProduct())){
				query.append("and Product = '"+vObject.getProduct()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getTenorRateStart())){
				query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
				query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getInterestRateStart())){
				query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
				query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getAvgStart() )){
				query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getAvgEnd() )){
				query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getOucAttribute())){
				query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
				query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
			}
			if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
				query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
			}
			return getJdbcTemplate().update(query.toString(),args);
	}

	@Override
	protected int doDeleteAppr(FTPCalculationsVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateCr())){
			vObject.setFtpRateCr(vObject.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateDr())){ 
			vObject.setFtpRateDr(vObject.getFtpRateDr().replaceAll(",", ""));
		}
		Vector<Object> params = new Vector<Object>();
		StringBuffer query =new StringBuffer("Delete From FTP_ENGINE Where " + 
		" DATA_SOURCE = ? " + 
		" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
		" And COUNTRY = ? " + 
		" And LE_BOOK = ? " + 
		" And VERSION_NO = ? " +
		" And CURRENCY = ? " +
		" And POOL_CODE = ? ") ;
		
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getVersionNo());
		params.add(vObject.getCurrency());
		params.add(vObject.getPoolCode());
		
		
		if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
			query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getProduct())){
			query.append("and Product = '"+vObject.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateStart())){
			query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
			query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgStart() )){
			query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd() )){
			query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel())  && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
			query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
			query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
		}
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}
	public int doDeleteFtpRate(FTPCalculationsVb vObject,int status){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateCr())){
			vObject.setFtpRateCr(vObject.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateDr())){ 
			vObject.setFtpRateDr(vObject.getFtpRateDr().replaceAll(",", ""));
		}
		Vector<Object> params = new Vector<Object>();
		StringBuffer query = null;
		if(status == 0){
			query =new StringBuffer("Delete From FTP_RATES Where " + 
					" DATA_SOURCE = ? " + 
					" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
					" And COUNTRY = ? " + 
					" And LE_BOOK = ? " + 
					" And CURRENCY = ? " +
					" And POOL_CODE = ? ") ;	
		}else{
			query =new StringBuffer("Delete From FTP_RATES_PEND Where " + 
					" DATA_SOURCE = ? " + 
					" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
					" And COUNTRY = ? " + 
					" And LE_BOOK = ? " + 
					" And CURRENCY = ? " +
					" And POOL_CODE = ? ") ;	
		}
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getCurrency());
		params.add(vObject.getPoolCode());
		
		
		if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
			query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getProduct())){
			query.append("and Product = '"+vObject.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateStart())){
			query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
			query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgStart() )){
			query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd() )){
			query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel())  && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
			query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
			query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
		}
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}

	@Override
	protected int deletePendingRecord(FTPCalculationsVb vObject){
		Vector<Object> params = new Vector<Object>();
		StringBuffer query =new StringBuffer("Delete From FTP_ENGINE Where " + 
				" DATA_SOURCE = ? " + 
				" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
				" And COUNTRY = ? " + 
				" And LE_BOOK = ? " + 
				" And VERSION_NO = ? " +
				" And CURRENCY = ? " +
				" And POOL_CODE = ? ") ;
		
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getVersionNo());
		params.add(vObject.getCurrency());
		params.add(vObject.getPoolCode());
		
		
		if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
			query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getProduct())){
			query.append("and Product = '"+vObject.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateStart())){
			query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
			query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgStart() )){
			query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd() )){
			query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel())  && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
			query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
			query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
		}
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}

	@Override
	protected String frameErrorMessage(FTPCalculationsVb vObject, String strOperation)
	{
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try {
			strErrMsg =  strErrMsg + " DATA_SOURCE:" + vObject.getDataSource();
			strErrMsg =  strErrMsg + " EFFECTIVE_DATE:" + vObject.getEffectiveDate();
			strErrMsg =  strErrMsg + " COUNTRY:" + vObject.getCountry();
			strErrMsg =  strErrMsg + " LE_BOOK:" + vObject.getLeBook();
			strErrMsg =  strErrMsg + " CURRENCY:" + vObject.getCurrency();
			strErrMsg =  strErrMsg + " POOL_CODE:" + vObject.getPoolCode();
			strErrMsg =  strErrMsg + " PRODUCT:" + vObject.getProduct();
			strErrMsg =  strErrMsg + " VISION_SBU:" + vObject.getVisionSbu();
			// Now concatenate the error message that has been sent
			if ("Approve".equalsIgnoreCase(strOperation))
				strErrMsg = strErrMsg + " failed during approve Operation. Bulk Approval aborted !!";
			else
				strErrMsg = strErrMsg + " failed during reject Operation. Bulk Rejection aborted !!";
		}catch(Exception ex){
			strErrorDesc = ex.getMessage();
			strErrMsg = strErrMsg + strErrorDesc;
			logger.error(strErrMsg, ex);
		}
		// Return back the error message string
		return strErrMsg;
	}


	@Override
	protected String getAuditString(FTPCalculationsVb vObject)
	{
		StringBuffer strAudit = new StringBuffer("");
		try
		{
			if(vObject.getCountry() != null)
				strAudit.append(vObject.getCountry().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if(vObject.getLeBook() != null)
				strAudit.append(vObject.getLeBook().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(vObject.getPoolCode() != null)
				strAudit.append(vObject.getPoolCode().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(vObject.getPoolCodeDesc() != null)
				strAudit.append(vObject.getPoolCodeDesc().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getEffectiveDate());
			strAudit.append("!|#");
			
			strAudit.append(vObject.getDataSourceAt());
			strAudit.append("!|#");
			if(vObject.getDataSource() != null)
				strAudit.append(vObject.getDataSource().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(vObject.getCurrency() != null)
				strAudit.append(vObject.getCurrency().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getVisionSbuAt());
			strAudit.append("!|#");
			if(vObject.getVisionSbu() != null)
				strAudit.append(vObject.getVisionSbu().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if(vObject.getProduct() != null)
				strAudit.append(vObject.getProduct().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getFtpEngineStatusNt());
			strAudit.append("!|#");
			strAudit.append(vObject.getFtpEngineStatus());
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
			if(vObject.getDateLastModified() != null)
				strAudit.append(vObject.getDateLastModified().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if(vObject.getDateCreation() != null)
				strAudit.append(vObject.getDateCreation().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

		}
		catch(Exception ex)
		{
			strErrorDesc = ex.getMessage();
			strAudit = strAudit.append(strErrorDesc);
			ex.printStackTrace();
		}
		return strAudit.toString();
	}
	public List<FTPCalculationsVb> getPoolCodeDescription(FTPCalculationsVb dObj)
	{
		List<FTPCalculationsVb> collTemp = null;
		String query = "select POOL_CODE_DESCRIPTION from POOL_CODES where COUNTRY = '"+dObj.getCountry()+"' AND LE_BOOK = '"+dObj.getLeBook()+"' AND POOL_CODE = '"+dObj.getPoolCode()+"'";			
		try
		{
			collTemp = getJdbcTemplate().query(query, getDesciptionMapper());
			return collTemp;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public List<FTPCalculationsVb> getProductDescription(FTPCalculationsVb dObj)
	{
		List<FTPCalculationsVb> collTemp = null;
		String query = "select PRODUCT_DESCRIPTION from PRODUCT_CODES where PRODUCT_STATUS =0 ORDER BY PRODUCT";			
		try
		{
			collTemp = getJdbcTemplate().query(query, getProductDescriptionMapper());
			return collTemp;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public int validatePoolCode(FTPCalculationsVb vObDlyVb){
		final int intKeyFieldsCount = 3;
		int count = 0;
		String strQuery = new String("Select  count(1) From Pool_Codes Where COUNTRY = ? AND LE_BOOK=? AND POOL_CODE =? AND Pool_Code_Status=0");
		Object objParams[] = new Object[intKeyFieldsCount];
		if(vObDlyVb.getCountry() == null || vObDlyVb.getLeBook() == null || vObDlyVb.getPoolCode() == null){
			return 1;
		}
		objParams[0] =vObDlyVb.getCountry();
		objParams[1] =vObDlyVb.getLeBook();
		objParams[2] =vObDlyVb.getPoolCode();
		try
		{
			count = getJdbcTemplate().queryForObject(strQuery.toString(),objParams, Integer.class);
			return  count;
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}
	}
	public List<FTPRefCodeVb> getFtpRefCodeResults(FTPCalculationsVb dObj){
		List<FTPRefCodeVb> collTemp = null;
		final int intKeyFieldsCount = 4;
		StringBuffer strQueryAppr = new StringBuffer("Select t1.Ftp_Ref_Code,t1.Ftp_Ref_description,t1.Ftp_Ref_Rate from Ftp_Ref_Codes t1 where t1.Ftp_Ref_Code_Status = 0 and t1.Country = ? "+
				" and t1.LE_Book = ? and t1.FTP_CALC_METHOD = ? and t1.Effective_Date = (select max(Effective_Date) from Ftp_Ref_Codes t2 where T2.Ftp_Ref_Code_Status = 0 and t2.Country = t1.Country "+ 
                " and t2.LE_Book =t1.LE_Book and t2.Ftp_Ref_Code = t1.Ftp_Ref_Code and t2.FTP_CALC_METHOD = t1.FTP_CALC_METHOD and t2.Effective_Date <= To_date(?,'DD-MM-RRRR') ) ");
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] =dObj.getCountry();
		objParams[1] =dObj.getLeBook();
		objParams[2] =dObj.getFtpMethod();
		objParams[3] =dObj.getEffectiveDate();
		try
		{
			logger.info("Executing approved query");
			collTemp = getJdbcTemplate().query(strQueryAppr.toString(),objParams,getFtpRefCodeMapper());
			return collTemp;
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			logger.error(((strQueryAppr == null) ? "strQueryAppr is null" : strQueryAppr.toString()));
			return null;
		}
	}
	public RowMapper getFtpRefCodeMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPRefCodeVb vObject = new FTPRefCodeVb();
				vObject.setFtpRefCode(rs.getString("Ftp_Ref_Code"));
				vObject.setFtpRefCodeDesc(rs.getString("Ftp_Ref_description"));
				vObject.setFtpRefCodeRate(rs.getString("Ftp_Ref_Rate"));
				return vObject;
			}
		};
		return mapper;
	}
	public String getVisionPrevYearBusinessDate(String effectiveDate){
		Object args[] = {effectiveDate};
		return getJdbcTemplate().queryForObject("Select '01-'||To_CHAR(ADD_MONTHS(TO_DATE(?,'DD-MM-RRRR'),-11),'MM-RRRR') FROM DUAL ",
				args,String.class);
	}
	public List<FTPCalculationsVb> getFtpProductBalances(FTPCalculationsVb dObj){
		List<FTPCalculationsVb> collTemp = null;
		StringBuffer strQueryAppr = new StringBuffer("select To_Char(t1.Base_balance,'999,999,999,999,990.90') Base_balance,"
				+ "To_Char(t2.Current_Bal,'999,999,999,999,990.90') Current_Bal "+
                /*" --round(((t2.Current_Bal/t1.Base_Balance)-1) * 100,2) CAGR ,   "+
                " --round(((t2.Current_Bal/t1.Base_Balance)-1) * 100,2) * (:a) p50_CAGR, "+
                " --t1.Base_Balance*(1+(((t2.Current_Bal/t1.Base_Balance)-1) * (:a))) Core_Balance, "+ 
                " --round(((t1.Base_Balance*(1+(((t2.Current_Bal/t1.Base_Balance)-1) * (:a))))/t2.current_Bal*(:b))*100,2) Core, "+ 
                " --100-(round(((t1.Base_Balance*(1+(((t2.Current_Bal/t1.Base_Balance)-1) * (:a))))/t2.current_Bal*(:b))*100,2)) Non_Core "+*/ 
                " from  "+
                " (select min(amount)  Base_Balance "+       
                " from ftp_product_balances where country = '"+dObj.getCountry()+"' and le_book = '"+dObj.getLeBook()+"' and ftp_product = '"+dObj.getFtpProduct()+"' "+ 
                " and business_date >= TO_DATE('"+dObj.getEarliestDate()+"','DD-MM-YYYY') "+
                " and business_date <= TO_DATE('"+dObj.getEffectiveDate()+"','DD-MM-YYYY') ) t1, "+ 
                " (select sum(amount) Current_Bal  from ftp_product_balances where country = '"+dObj.getCountry()+"' and le_book = '"+dObj.getLeBook()+"' and ftp_product = '"+dObj.getFtpProduct()+"' "+ 
                " and business_date = (select max(business_date) from ftp_product_balances where country = '"+dObj.getCountry()+"' and le_book = '"+dObj.getLeBook()+"' and ftp_product = '"+dObj.getFtpProduct()+"' "+ 
                " and business_date >= TO_DATE('"+dObj.getEarliestDate()+"','DD-MM-YYYY') "+
                " and business_date <= TO_DATE('"+dObj.getLatestDate()+"','DD-MM-YYYY') )) t2 ");
		Object objParams[] = new Object[0];
		try
		{
			logger.info("Executing approved query");
			collTemp = getJdbcTemplate().query(strQueryAppr.toString(),objParams,getFtpProductBalancesMapper());
			return collTemp;
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			logger.error(((strQueryAppr == null) ? "strQueryAppr is null" : strQueryAppr.toString()));
			return null;
		}
	}
	public RowMapper getFtpProductBalancesMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCalculationsVb vObject = new FTPCalculationsVb();
				vObject.setBaseBalance(rs.getString("Base_balance"));
				vObject.setCurrentBalance(rs.getString("Current_Bal"));
				/*vObject.setCagr(rs.getString("CAGR"));
				vObject.setPerCagr(rs.getString("p50_CAGR"));
				vObject.setCoreBalance(rs.getString("Core_Balance"));
				vObject.setFtpCore(rs.getString("Core"));
				vObject.setFtpNonCore(rs.getString("Non_Core"));*/
				return vObject;
			}
		};
		return mapper;
	}
	public int doUpdateEngineStatusFlag(FTPCalculationsVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateCr())){
			vObject.setFtpRateCr(vObject.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateDr())){ 
			vObject.setFtpRateDr(vObject.getFtpRateDr().replaceAll(",", ""));
		}
		StringBuilder query = new StringBuilder("Update FTP_ENGINE  Set " +
				" FTP_ENGINE_STATUS = ? WHERE COUNTRY = ? AND LE_BOOK = ? AND DATA_SOURCE = ? AND VERSION_NO = ? "+
				" AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR') AND CURRENCY = ? AND POOL_CODE = ? ");
			
		Object args[] = {vObject.getFtpEngineStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(),vObject.getVersionNo(),
				vObject.getEffectiveDate(),vObject.getCurrency(),vObject.getPoolCode()};
		
		if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
			query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getProduct())){
			query.append("and Product = '"+vObject.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateStart())){
			query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
			query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgStart() )){
			query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd() )){
			query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
			query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
			query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
		}
		return getJdbcTemplate().update(query.toString(),args);
	}
	public int doUpdateFtpRates(FTPCalculationsVb vObject,int status){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateCr())){
			vObject.setFtpRateCr(vObject.getFtpRateCr().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getFtpRateDr())){ 
			vObject.setFtpRateDr(vObject.getFtpRateDr().replaceAll(",", ""));
		}
		StringBuilder query = null;
		if(status == 0){
			query = new StringBuilder("Update FTP_RATES Set " +
					" DEBIT_POOL_RATE = ?,CREDIT_POOL_RATE = ?, DATE_LAST_MODIFIED = sysdate WHERE COUNTRY = ? AND LE_BOOK = ? AND DATA_SOURCE = ? "+
					" AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-YYYY') AND CURRENCY = ? AND POOL_CODE = ? ");	
		}else{
			query = new StringBuilder("Update FTP_RATES_PEND Set " +
					" DEBIT_POOL_RATE = ?,CREDIT_POOL_RATE = ?, DATE_LAST_MODIFIED = sysdate WHERE COUNTRY = ? AND LE_BOOK = ? AND DATA_SOURCE = ? "+
					" AND EFFECTIVE_DATE = TO_DATE(?,'DD-MM-YYYY') AND CURRENCY = ? AND POOL_CODE = ? ");
		}
		
		Object args[] = {vObject.getFtpRateDr(),vObject.getFtpRateCr(),vObject.getCountry(), vObject.getLeBook(), vObject.getDataSource(),
				vObject.getEffectiveDate(),vObject.getCurrency(),vObject.getPoolCode()};
		
		if(ValidationUtil.isValid(vObject.getVisionSbu()) && !"-1".equalsIgnoreCase(vObject.getVisionSbu())){
			query.append("and Vision_Sbu = '"+vObject.getVisionSbu()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getProduct())){
			query.append("and Product = '"+vObject.getProduct()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateStart())){
			query.append("and TENOR_START = '"+vObject.getTenorRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getTenorRateEnd())){
			query.append("and TENOR_END = '"+vObject.getTenorRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			query.append("and INT_RATE_START = '"+vObject.getInterestRateStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			query.append("and INT_RATE_END = '"+vObject.getInterestRateEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgStart() )){
			query.append("and AVG_START = '"+vObject.getAvgStart()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd() )){
			query.append("and AVG_END = '"+vObject.getAvgEnd()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			query.append("and OUC_ATTRIBUTE = '"+vObject.getOucAttribute()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel()) && !"-1".equalsIgnoreCase(vObject.getOucAttributeLevel())){
			query.append("and OUC_ATTRIBUTE_LEVEL = '"+vObject.getOucAttributeLevel()+"' ");
		}
		if(ValidationUtil.isValid(vObject.getRiskAssetClass()) && !"-1".equalsIgnoreCase(vObject.getRiskAssetClass())){
			query.append("and RISK_ASSET_CLASS = '"+vObject.getRiskAssetClass()+"' ");
		}
		return getJdbcTemplate().update(query.toString(),args);
	}
	public ExceptionCode doInsertApprRecordForNonTransFtpRates(FTPCalculationsVb vObject) throws RuntimeCustomException {
		List<FTPCalculationsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strCurrentOperation = Constants.ADD;
		strApproveOperation =Constants.ADD;		
		setServiceDefaults();
		int retVal = 0;
		if("RUNNING".equalsIgnoreCase(getBuildStatus(vObject))){
			exceptionCode = getResultObject(Constants.BUILD_IS_RUNNING);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObject.setMaker(getIntCurrentUserId());
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_ENGINE");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
		int status = 0;
		if(vObject.isVerificationRequired()){
			vObject.setRecordIndicator(2);
			vObject.setVerifier(0);
			status = 1;
		}else{
			vObject.setRecordIndicator(0);
			vObject.setVerifier(getIntCurrentUserId());
			status = 0;
		}
		
		int recCnt = getCountOfFtpRate(vObject,status);
		if(recCnt > 0){
			retVal = doUpdateFtpRates(vObject,status);
		}else{
			retVal = doInsertionFtpRates(vObject,status);	
		}
		if (retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		}else{
			vObject.setFtpEngineStatus(1);
			retVal = doUpdateEngineStatusFlag(vObject);
		}
		String systemDate = getSystemDate();
		vObject.setDateLastModified(systemDate);
		vObject.setDateCreation(systemDate);
		exceptionCode = writeAuditLog(vObject, null);
		if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR);
			throw buildRuntimeCustomException(exceptionCode);
		}
		return exceptionCode;
	}
	public CommonDao getCommonDao() {
		return commonDao;
	}
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
}
