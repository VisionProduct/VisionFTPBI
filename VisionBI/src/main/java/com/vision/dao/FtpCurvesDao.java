package com.vision.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FtpMethodsVb;

@Component
public class FtpCurvesDao extends AbstractDao<FTPCurveVb> {

	@Value("${app.databaseType}")
	 private String databaseType;
	
/*******Mapper Start**********/
	String CountryApprDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TAppr.COUNTRY) COUNTRY_DESC";
	String CountryPendDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TPend.COUNTRY) COUNTRY_DESC";
	String LeBookApprDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TAppr.LE_BOOK AND COUNTRY = TAppr.COUNTRY ) LE_BOOK_DESC";
	String LeBookTPendDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TPend.LE_BOOK AND COUNTRY = TPend.COUNTRY ) LE_BOOK_DESC";

	String TenorApplicationCodeNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 8, "TAppr.TENOR_APPLICATION_CODE", "TENOR_APPLICATION_CODE_DESC");
	String TenorApplicationCodeNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 8, "TPend.TENOR_APPLICATION_CODE", "TENOR_APPLICATION_CODE_DESC");

	String VisionSbuAttributeAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 3, "TAppr.VISION_SBU_ATTRIBUTE", "VISION_SBU_ATTRIBUTE_DESC");
	String VisionSbuAttributeAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 3, "TPend.VISION_SBU_ATTRIBUTE", "VISION_SBU_ATTRIBUTE_DESC");

	String ProductAttributeAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 0, "TAppr.PRODUCT_ATTRIBUTE", "PRODUCT_ATTRIBUTE_DESC");
	String ProductAttributeAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 0, "TPend.PRODUCT_ATTRIBUTE", "PRODUCT_ATTRIBUTE_DESC");

	String FtpCurveStatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_CURVE_STATUS", "FTP_CURVE_STATUS_DESC");
	String FtpCurveStatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_CURVE_STATUS", "FTP_CURVE_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb vObject = new FTPCurveVb();
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
				if(rs.getString("FTP_CURVE_ID")!= null){ 
					vObject.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				}else{
					vObject.setFtpCurveId("");
				}
				if(rs.getString("EFFECTIVE_DATE")!= null){ 
					vObject.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				}else{
					vObject.setEffectiveDate("");
				}
				vObject.setTenorApplicationCodeNt(rs.getInt("TENOR_APPLICATION_CODE_NT"));
				vObject.setTenorApplicationCode(rs.getInt("TENOR_APPLICATION_CODE"));
				if(ValidationUtil.isValid(rs.getString("TENOR_APPLICATION_CODE_DESC")))
					vObject.setTenorApplicationCodeDesc(rs.getString("TENOR_APPLICATION_CODE_DESC"));
				if(rs.getString("TENOR_CODE")!= null){ 
					vObject.setTenorCode(rs.getString("TENOR_CODE"));
				}else{
					vObject.setTenorCode("");
				}
				
				if(rs.getString("TENOR_DESCRIPTION")!= null){ 
					vObject.setTenorCodeDesc(rs.getString("TENOR_DESCRIPTION"));
				}
				
				vObject.setVisionSbuAttributeAt(rs.getInt("VISION_SBU_ATTRIBUTE_AT"));
				if(rs.getString("VISION_SBU_ATTRIBUTE")!= null){ 
					vObject.setVisionSbuAttribute(rs.getString("VISION_SBU_ATTRIBUTE"));
				}else{
					vObject.setVisionSbuAttribute("");
				}
				if(ValidationUtil.isValid(rs.getString("VISION_SBU_ATTRIBUTE_DESC")))
					vObject.setVisionSbuAttributeDesc(rs.getString("VISION_SBU_ATTRIBUTE_DESC"));
				else
					vObject.setVisionSbuAttributeDesc(vObject.getVisionSbuAttribute());
				if(rs.getString("PRODUCT_ATTRIBUTE")!= null){ 
					vObject.setProductAttribute(rs.getString("PRODUCT_ATTRIBUTE"));
				}else{
					vObject.setProductAttribute("");
				}
				if(ValidationUtil.isValid(rs.getString("PRODUCT_ATTRIBUTE_DESC")))
					vObject.setProductAttributeDesc(rs.getString("PRODUCT_ATTRIBUTE_DESC"));
				else
					vObject.setProductAttributeDesc(vObject.getProductAttribute());
				if(rs.getString("CURRENCY")!= null){ 
					vObject.setCurrency(rs.getString("CURRENCY"));
				}else{
					vObject.setCurrency("");
				}
				vObject.setIntRateStart(rs.getString("INT_RATE_START"));
				vObject.setIntRateEnd(rs.getString("INT_RATE_END"));
				vObject.setAmountStart(rs.getString("AMOUNT_START"));
				vObject.setAmountEnd(rs.getString("AMOUNT_END"));
				if(rs.getString("END_DATE")!= null){ 
					vObject.setEndDate(rs.getString("END_DATE"));
				}else{
					vObject.setEndDate("");
				}
				if(ValidationUtil.isValid(rs.getString("FTP_RATE")))
					vObject.setFtpRate(rs.getString("FTP_RATE"));
				if(ValidationUtil.isValid(rs.getString("CRR_RATE")))
					vObject.setCrrRate(rs.getString("CRR_RATE"));
				vObject.setFtpCurveStatusNt(rs.getInt("FTP_CURVE_STATUS_NT"));
				vObject.setFtpCurveStatus(rs.getInt("FTP_CURVE_STATUS"));
				if(ValidationUtil.isValid(rs.getString("FTP_CURVE_STATUS_DESC")))
					vObject.setStatusDesc(rs.getString("FTP_CURVE_STATUS_DESC"));
				
				vObject.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				vObject.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				if(ValidationUtil.isValid(rs.getString("RECORD_INDICATOR_DESC")))
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

/*******Mapper End**********/

	public List<FTPCurveVb> getQueryResults(FTPCurveVb dObj, int intStatus){

		setServiceDefaults();
		removeComma(dObj);
		List<FTPCurveVb> collTemp = null;

		final int intKeyFieldsCount = 13;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_CURVE_ID"
			+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TAppr.TENOR_APPLICATION_CODE_NT"
			+ ",TAppr.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
			+ ",TAppr.TENOR_CODE, (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TAppr.TENOR_APPLICATION_CODE_NT AND TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.TENOR_CODE) TENOR_DESCRIPTION "
			+ ",TAppr.VISION_SBU_ATTRIBUTE_AT"
			+ ",TAppr.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtApprDesc
			+ ",TAppr.PRODUCT_ATTRIBUTE, "+ProductAttributeAtApprDesc
			+ ",TAppr.CURRENCY, "+
			"TRIM("+dateFormat+" (TAppr.INT_RATE_START, "+numberFormat+"))INT_RATE_START  ," + 
			"TRIM("+dateFormat+" (TAppr.INT_RATE_END, "+numberFormat+")) INT_RATE_END    ," + 
			"TRIM("+dateFormat+" (TAppr.AMOUNT_START, "+numberFormat+")) AMOUNT_START    ," + 
			"TRIM("+dateFormat+" (TAppr.AMOUNT_END, "+numberFormat+"))   AMOUNT_END      ," +
			"TRIM("+dateFormat+" (TAppr.FTP_RATE, "+numberFormat+"))   FTP_RATE      ," +
			"TRIM("+dateFormat+" (TAppr.CRR_RATE, "+numberFormat+"))   CRR_RATE      ," +			
			""+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TAppr.FTP_CURVE_STATUS_NT"
			+ ",TAppr.FTP_CURVE_STATUS, "+FtpCurveStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_CURVES TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND VISION_SBU_ATTRIBUTE = ?  AND PRODUCT_ATTRIBUTE = ?  AND CURRENCY = ?  AND INT_RATE_START = ?  AND INT_RATE_END = ?  AND AMOUNT_START = ?  AND AMOUNT_END = ?  ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_CURVE_ID"
			+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TPend.TENOR_APPLICATION_CODE_NT"
			+ ",TPend.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
			+ ",TPend.TENOR_CODE, (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TPend.TENOR_APPLICATION_CODE_NT AND  TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.TENOR_CODE) TENOR_DESCRIPTION "
			+ ",TPend.VISION_SBU_ATTRIBUTE_AT"
			+ ",TPend.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtPendDesc
			+ ",TPend.PRODUCT_ATTRIBUTE, "+ProductAttributeAtPendDesc
			+ ",TPend.CURRENCY, "+
			"TRIM("+dateFormat+" (TPend.INT_RATE_START, "+numberFormat+"))INT_RATE_START  ," + 
			"TRIM("+dateFormat+" (TPend.INT_RATE_END, "+numberFormat+")) INT_RATE_END    ," + 
			"TRIM("+dateFormat+" (TPend.AMOUNT_START, "+numberFormat+")) AMOUNT_START    ," + 
			"TRIM("+dateFormat+" (TPend.AMOUNT_END, "+numberFormat+"))   AMOUNT_END      ," +
			"TRIM("+dateFormat+" (TPend.FTP_RATE, "+numberFormat+"))   FTP_RATE      ," +
			"TRIM("+dateFormat+" (TPend.CRR_RATE, "+numberFormat+"))   CRR_RATE      ," +
			""+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TPend.FTP_CURVE_STATUS_NT"
			+ ",TPend.FTP_CURVE_STATUS, "+FtpCurveStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_CURVES_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND VISION_SBU_ATTRIBUTE = ?  AND PRODUCT_ATTRIBUTE = ?  AND CURRENCY = ?  AND INT_RATE_START = ?  AND INT_RATE_END = ?  AND AMOUNT_START = ?  AND AMOUNT_END = ?   ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpCurveId();
		objParams[3] = dObj.getEffectiveDate();
		objParams[4] = dObj.getTenorApplicationCode();
		objParams[5] = dObj.getTenorCode();
		objParams[6] = dObj.getVisionSbuAttribute();
		objParams[7] = dObj.getProductAttribute();
		objParams[8] = dObj.getCurrency();
		objParams[9] = dObj.getIntRateStart();
		objParams[10] = dObj.getIntRateEnd();
		objParams[11] = dObj.getAmountStart();
		objParams[12] = dObj.getAmountEnd();

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
	
	public List<FTPCurveVb> getQueryResultsByParent(FtpMethodsVb dObj, int intStatus, boolean onlyActive){

		setServiceDefaults();

		List<FTPCurveVb> collTemp = null;

		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_CURVE_ID"
			+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TAppr.TENOR_APPLICATION_CODE_NT"
			+ ",TAppr.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
			+ ",TAppr.TENOR_CODE, (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TAppr.TENOR_APPLICATION_CODE_NT AND TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.TENOR_CODE) TENOR_DESCRIPTION "
			+ ",TAppr.VISION_SBU_ATTRIBUTE_AT"
			+ ",TAppr.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtApprDesc
			+ ",TAppr.PRODUCT_ATTRIBUTE, "+ProductAttributeAtApprDesc
			+ ",TAppr.CURRENCY"
			+ ",TAppr.INT_RATE_START"
			+ ",TAppr.INT_RATE_END"
			+ ",TAppr.AMOUNT_START"
			+ ",TAppr.AMOUNT_END"
			+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TAppr.FTP_RATE"
			+ ",TAppr.CRR_RATE"
			+ ",TAppr.FTP_CURVE_STATUS_NT"
			+ ",TAppr.FTP_CURVE_STATUS, "+FtpCurveStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_CURVES TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_CURVE_ID"
			+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TPend.TENOR_APPLICATION_CODE_NT"
			+ ",TPend.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
			+ ",TPend.TENOR_CODE, (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TPend.TENOR_APPLICATION_CODE_NT AND  TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.TENOR_CODE) TENOR_DESCRIPTION "
			+ ",TPend.VISION_SBU_ATTRIBUTE_AT"
			+ ",TPend.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtPendDesc
			+ ",TPend.PRODUCT_ATTRIBUTE, "+ProductAttributeAtPendDesc
			+ ",TPend.CURRENCY"
			+ ",TPend.INT_RATE_START"
			+ ",TPend.INT_RATE_END"
			+ ",TPend.AMOUNT_START"
			+ ",TPend.AMOUNT_END"
			+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TPend.FTP_RATE"
			+ ",TPend.CRR_RATE"
			+ ",TPend.FTP_CURVE_STATUS_NT"
			+ ",TPend.FTP_CURVE_STATUS, "+FtpCurveStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_CURVES_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  ");
		int intKeyFieldsCount = 3;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpCurveId();
		if(onlyActive) {
			strQueryAppr = strQueryAppr  + "AND FTP_CURVE_STATUS != 9";
			strQueryPend = strQueryPend  + "AND FTP_CURVE_STATUS != 9";
		}
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
	protected List<FTPCurveVb> selectApprovedRecord(FTPCurveVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}


	@Override
	protected List<FTPCurveVb> doSelectPendingRecord(FTPCurveVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}


	@Override
	protected int getStatus(FTPCurveVb records){return records.getFtpCurveStatus();}


	@Override
	protected void setStatus(FTPCurveVb vObject,int status){vObject.setFtpCurveStatus(status);}


	public void removeComma(FTPCurveVb vObject) {
		vObject.setFtpRate(vObject.getFtpRate().replaceAll(",", ""));
		vObject.setIntRateStart(vObject.getIntRateStart().replaceAll(",", ""));
		vObject.setIntRateEnd(vObject.getIntRateEnd().replaceAll(",", ""));
		vObject.setAmountStart(vObject.getAmountStart().replaceAll(",", ""));
		vObject.setAmountEnd(vObject.getAmountEnd().replaceAll(",", ""));
		vObject.setCrrRate(vObject.getCrrRate().replaceAll(",", ""));
	}
	
	@Override
	protected int doInsertionAppr(FTPCurveVb vObject) {
		removeComma(vObject);
		String query = "Insert Into FTP_CURVES (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, "
				+ "TENOR_APPLICATION_CODE, TENOR_CODE, VISION_SBU_ATTRIBUTE_AT, VISION_SBU_ATTRIBUTE, "
				+ "PRODUCT_ATTRIBUTE, CURRENCY, INT_RATE_START, INT_RATE_END, AMOUNT_START, AMOUNT_END, END_DATE, FTP_RATE, CRR_RATE, "
				+ "FTP_CURVE_STATUS_NT, FTP_CURVE_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getTenorApplicationCode(),
				vObject.getTenorCode(), vObject.getVisionSbuAttributeAt(), vObject.getVisionSbuAttribute(),
				vObject.getProductAttribute(), vObject.getCurrency(),
				vObject.getIntRateStart(), vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd(),
				vObject.getEndDate(), vObject.getFtpRate(), vObject.getCrrRate(), vObject.getFtpCurveStatusNt(),
				vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doInsertionPend(FTPCurveVb vObject) {
		removeComma(vObject);
		String query = "Insert Into FTP_CURVES_PEND (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, "
				+ "TENOR_APPLICATION_CODE, TENOR_CODE, VISION_SBU_ATTRIBUTE_AT, VISION_SBU_ATTRIBUTE, "
				+ "PRODUCT_ATTRIBUTE, CURRENCY, INT_RATE_START, INT_RATE_END, AMOUNT_START, AMOUNT_END, END_DATE, FTP_RATE, CRR_RATE, "
				+ "FTP_CURVE_STATUS_NT, FTP_CURVE_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getTenorApplicationCode(),
				vObject.getTenorCode(), vObject.getVisionSbuAttributeAt(), vObject.getVisionSbuAttribute(),
				vObject.getProductAttribute(), vObject.getCurrency(),
				vObject.getIntRateStart(), vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd(),
				vObject.getEndDate(), vObject.getFtpRate(), vObject.getCrrRate(), vObject.getFtpCurveStatusNt(),
				vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doInsertionPendWithDc(FTPCurveVb vObject) {
		removeComma(vObject);
		String query = "Insert Into FTP_CURVES_PEND (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, "
				+ "TENOR_APPLICATION_CODE, TENOR_CODE, VISION_SBU_ATTRIBUTE_AT, VISION_SBU_ATTRIBUTE, PRODUCT_ATTRIBUTE, CURRENCY, INT_RATE_START, INT_RATE_END, AMOUNT_START, AMOUNT_END, END_DATE, FTP_RATE, CRR_RATE, FTP_CURVE_STATUS_NT, FTP_CURVE_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getTenorApplicationCode(),
				vObject.getTenorCode(), vObject.getVisionSbuAttributeAt(), vObject.getVisionSbuAttribute(),
				 vObject.getProductAttribute(), vObject.getCurrency(),
				vObject.getIntRateStart(), vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd(),
				vObject.getEndDate(), vObject.getFtpRate(), vObject.getCrrRate(), vObject.getFtpCurveStatusNt(),
				vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), vObject.getDateCreation() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doUpdateAppr(FTPCurveVb vObject) {
		removeComma(vObject);
		String query = "Update FTP_CURVES Set TENOR_APPLICATION_CODE_NT = ?, VISION_SBU_ATTRIBUTE_AT = ?, END_DATE = "+dateConvert+""
				+ ", FTP_RATE = ?, CRR_RATE = ?, FTP_CURVE_STATUS_NT = ?, FTP_CURVE_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, "
				+ "VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+"  WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+" "
						+ " AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND VISION_SBU_ATTRIBUTE = ?  AND PRODUCT_ATTRIBUTE = ?  "
						+ "AND CURRENCY = ?  AND INT_RATE_START = ?  AND INT_RATE_END = ?  AND AMOUNT_START = ?  AND AMOUNT_END = ? ";
		Object[] args = { vObject.getTenorApplicationCodeNt(), vObject.getVisionSbuAttributeAt(),
				 vObject.getEndDate(), vObject.getFtpRate(), vObject.getCrrRate(),
				vObject.getFtpCurveStatusNt(), vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(), vObject.getEffectiveDate(),
				vObject.getTenorApplicationCode(), vObject.getTenorCode(), vObject.getVisionSbuAttribute(),
				vObject.getProductAttribute(), vObject.getCurrency(), vObject.getIntRateStart(),
				vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd(), };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doUpdatePend(FTPCurveVb vObject){
		removeComma(vObject);
		String query = "Update FTP_CURVES_PEND Set TENOR_APPLICATION_CODE_NT = ?, VISION_SBU_ATTRIBUTE_AT = ?, END_DATE = "+dateConvert+""
				+ ", FTP_RATE = ?, CRR_RATE = ?, FTP_CURVE_STATUS_NT = ?, FTP_CURVE_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, "
				+ "VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+"  WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+" "
						+ " AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND VISION_SBU_ATTRIBUTE = ?  AND PRODUCT_ATTRIBUTE = ?  "
						+ "AND CURRENCY = ?  AND INT_RATE_START = ?  AND INT_RATE_END = ?  AND AMOUNT_START = ?  AND AMOUNT_END = ? ";
		Object[] args = { vObject.getTenorApplicationCodeNt(), vObject.getVisionSbuAttributeAt(),
				 vObject.getEndDate(), vObject.getFtpRate(), vObject.getCrrRate(),
				vObject.getFtpCurveStatusNt(), vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(), vObject.getEffectiveDate(),
				vObject.getTenorApplicationCode(), vObject.getTenorCode(), vObject.getVisionSbuAttribute(),
				vObject.getProductAttribute(), vObject.getCurrency(), vObject.getIntRateStart(),
				vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd(), };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doDeleteAppr(FTPCurveVb vObject) {
		removeComma(vObject);
		String query = "Delete From FTP_CURVES Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  "
				+ "AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND VISION_SBU_ATTRIBUTE = ?  AND PRODUCT_ATTRIBUTE = ?  AND CURRENCY = ?  "
				+ "AND INT_RATE_START = ?  AND INT_RATE_END = ?  AND AMOUNT_START = ?  AND AMOUNT_END = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCode(), vObject.getTenorCode(),
				vObject.getVisionSbuAttribute(), vObject.getProductAttribute(), vObject.getCurrency(),
				vObject.getIntRateStart(), vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd() };
		return getJdbcTemplate().update(query, args);
	}
	protected int doDeleteApprByParent(FtpMethodsVb vObject) {
		String query = "Delete From FTP_CURVES Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId()};
		return getJdbcTemplate().update(query, args);
	}
	protected int doDeletePendingByParent(FtpMethodsVb vObject) {
		String query = "Delete From FTP_CURVES_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId()};
		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int deletePendingRecord(FTPCurveVb vObject){
		removeComma(vObject);
		String query = "Delete From FTP_CURVES_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  "
				+ "AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND VISION_SBU_ATTRIBUTE = ?  AND PRODUCT_ATTRIBUTE = ?  AND CURRENCY = ?  "
				+ "AND INT_RATE_START = ?  AND INT_RATE_END = ?  AND AMOUNT_START = ?  AND AMOUNT_END = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCode(), vObject.getTenorCode(),
				vObject.getVisionSbuAttribute(), vObject.getProductAttribute(), vObject.getCurrency(),
				vObject.getIntRateStart(), vObject.getIntRateEnd(), vObject.getAmountStart(), vObject.getAmountEnd() };
		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected String getAuditString(FTPCurveVb vObject){
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

			if(ValidationUtil.isValid(vObject.getFtpCurveId()))
				strAudit.append("FTP_CURVE_ID"+auditDelimiterColVal+vObject.getFtpCurveId().trim());
			else
				strAudit.append("FTP_CURVE_ID"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getEffectiveDate()))
				strAudit.append("EFFECTIVE_DATE"+auditDelimiterColVal+vObject.getEffectiveDate().trim());
			else
				strAudit.append("EFFECTIVE_DATE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("TENOR_APPLICATION_CODE_NT"+auditDelimiterColVal+vObject.getTenorApplicationCodeNt());
			strAudit.append(auditDelimiter);

				strAudit.append("TENOR_APPLICATION_CODE"+auditDelimiterColVal+vObject.getTenorApplicationCode());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getTenorCode()))
				strAudit.append("TENOR_CODE"+auditDelimiterColVal+vObject.getTenorCode().trim());
			else
				strAudit.append("TENOR_CODE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("VISION_SBU_ATTRIBUTE_AT"+auditDelimiterColVal+vObject.getVisionSbuAttributeAt());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getVisionSbuAttribute()))
				strAudit.append("VISION_SBU_ATTRIBUTE"+auditDelimiterColVal+vObject.getVisionSbuAttribute().trim());
			else
				strAudit.append("VISION_SBU_ATTRIBUTE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getProductAttribute()))
				strAudit.append("PRODUCT_ATTRIBUTE"+auditDelimiterColVal+vObject.getProductAttribute().trim());
			else
				strAudit.append("PRODUCT_ATTRIBUTE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getCurrency()))
				strAudit.append("CURRENCY"+auditDelimiterColVal+vObject.getCurrency().trim());
			else
				strAudit.append("CURRENCY"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("INT_RATE_START"+auditDelimiterColVal+vObject.getIntRateStart());
			strAudit.append(auditDelimiter);

				strAudit.append("INT_RATE_END"+auditDelimiterColVal+vObject.getIntRateEnd());
			strAudit.append(auditDelimiter);

				strAudit.append("AMOUNT_START"+auditDelimiterColVal+vObject.getAmountStart());
			strAudit.append(auditDelimiter);

				strAudit.append("AMOUNT_END"+auditDelimiterColVal+vObject.getAmountEnd());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getEndDate()))
				strAudit.append("END_DATE"+auditDelimiterColVal+vObject.getEndDate().trim());
			else
				strAudit.append("END_DATE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_RATE"+auditDelimiterColVal+vObject.getFtpRate());
			strAudit.append(auditDelimiter);

				strAudit.append("CRR_RATE"+auditDelimiterColVal+vObject.getCrrRate());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_CURVE_STATUS_NT"+auditDelimiterColVal+vObject.getFtpCurveStatusNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_CURVE_STATUS"+auditDelimiterColVal+vObject.getFtpCurveStatus());
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
		serviceName = "FtpCurves";
		serviceDesc = "FTP Curves";
		tableName = "FTP_CURVES";
		childTableName = "FTP_CURVES";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
		
	}
	public List<FTPCurveVb> getQueryPopupResultsCurves(FTPCurveVb dObj) {
		dObj.setRecordIndicator(0);
		StringBuffer strBufApprove = new StringBuffer("SELECT TAppr.COUNTRY," + 
					"        TAppr.LE_BOOK," + 
					"        TAppr.FTP_CURVE_ID                                            ," + 
					"        "+dateFormat+" (TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE," + 
					"		"+dateFormat+" (TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE," + 
					"        TAppr.TENOR_APPLICATION_CODE                                  ," + 
					"        TAppr.TENOR_CODE                                              ," + 
					"        (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND     TENOR_CODE             = TAppr.TENOR_CODE)TENOR_DESC," + 
					"        "+nullFun+" (TAppr.VISION_SBU_ATTRIBUTE, '000')                      VISION_SBU_ATTRIBUTE," + 
					"        (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB     = 300 AND     ALPHA_SUB_TAB = TAppr.VISION_SBU_ATTRIBUTE)VISIONSBU_DESC ," + 
					"        TAppr.CURRENCY                                                               ," + 
					"        ( SELECT CCY_DESCRIPTION DESCRIPTION FROM CURRENCIES WHERE CCY_STATUS = 0 AND     CURRENCY   = TAppr.CURRENCY)                  CURRENCY_DESC   ," + 
					"        TRIM("+dateFormat+" (TAppr.INT_RATE_START, "+numberFormat+"))INT_RATE_START  ," + 
					"        TRIM("+dateFormat+" (TAppr.INT_RATE_END, "+numberFormat+")) INT_RATE_END    ," + 
					"        TRIM("+dateFormat+" (TAppr.AMOUNT_START, "+numberFormat+")) AMOUNT_START    ," + 
					"        TRIM("+dateFormat+" (TAppr.AMOUNT_END, "+numberFormat+"))   AMOUNT_END      ," + 
					"        TRIM("+dateFormat+" (TAppr.FTP_RATE, "+numberFormat+"))   FTP_RATE      ," +
					"        TRIM("+dateFormat+" (TAppr.CRR_RATE, "+numberFormat+"))   CRR_RATE      ," +					"        TAppr.FTP_CURVE_STATUS, "
					+"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TAppr.FTP_CURVE_STATUS_NT AND NUM_SUB_TAB = TAppr.FTP_CURVE_STATUS )FTP_CURVE_STATUS_DESC ," 
					+ "      TAppr.RECORD_INDICATOR," + 
					"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TAppr.RECORD_INDICATOR_NT AND NUM_SUB_TAB = TAppr.RECORD_INDICATOR )RECORD_INDICATOR_DESC ," + 
					"        TAppr.MAKER ," +makerApprDesc+", "+ 
					"        TAppr.VERIFIER," +verifierApprDesc +", "+
					"        TAppr.INTERNAL_STATUS," + 
					dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
					+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, "+
					"        TAppr.PRODUCT_ATTRIBUTE," + 
					"        (SELECT DISTINCT PRODUCT_ATT_DESCRIPTION FROM product_narrow where PRODUCT_ATTRIBUTE = TAppr.PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC" + 
					" FROM FTP_CURVES TAppr WHERE" + 
					"   TAppr.Country = ? And TAppr.LE_Book = ? And TAppr.FTP_CURVE_ID = ? ");
		
		
		String strBufApproveAnd = " And TAppr.EFFECTIVE_DATE =(" + 
				" SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_CURVES t2" + 
				" WHERE t2.Country   = TAppr.Country" + 
				" AND t2.LE_Book   = TAppr.LE_Book" + 
				" And t2.FTP_CURVE_ID   = TAppr.FTP_CURVE_ID" + 
				" And T2.TENOR_APPLICATION_CODE   = TAppr.TENOR_APPLICATION_CODE   " + 
				" And T2.TENOR_CODE = TAppr.TENOR_CODE   " + 
				" And T2.Vision_SBU_Attribute   = TAppr.Vision_SBU_Attribute" + 
				" And T2.Product_Attribute      = TAppr.Product_Attribute" + 
				" And T2.Currency               = TAppr.Currency" + 
				" And T2.Int_Rate_Start         = TAppr.Int_Rate_Start" + 
				" And T2.Int_Rate_End           = TAppr.Int_Rate_End" + 
				" And T2.Amount_Start           = TAppr.Amount_Start" + 
				" And T2.Amount_End             = TAppr.Amount_End" + 
				" AND t2.EFFECTIVE_DATE <= NVL(TO_DATE (?, 'DD-MM-RRRR') , trunc(sysdate))" + 
				" And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) ) ";
		
		StringBuffer strBufPend = new StringBuffer("SELECT TPend.COUNTRY," + 
				"        TPend.LE_BOOK," + 
				"        TPend.FTP_CURVE_ID                                            ," + 
				"        "+dateFormat+" (TPend.EFFECTIVE_DATE, 'DD-Mon-RRRR') EFFECTIVE_DATE," + 
				"		"+dateFormat+" (TPend.END_DATE, "+dateAloneFormatStr+") END_DATE," + 
				"        TPend.TENOR_APPLICATION_CODE                                  ," + 
				"        TPend.TENOR_CODE                                              ," + 
				"        (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND     TENOR_CODE             = TPend.TENOR_CODE)TENOR_DESC," + 
				"        "+nullFun+" (TPend.VISION_SBU_ATTRIBUTE, '000')                      VISION_SBU_ATTRIBUTE," + 
				"        (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB     = 300 AND     ALPHA_SUB_TAB = TPend.VISION_SBU_ATTRIBUTE)VISIONSBU_DESC ," + 
				"        TPend.CURRENCY                                                               ," + 
				"        ( SELECT CCY_DESCRIPTION DESCRIPTION FROM CURRENCIES WHERE CCY_STATUS = 0 AND     CURRENCY   = TPend.CURRENCY)                  CURRENCY_DESC   ," + 
				"        TRIM("+dateFormat+" (TPend.INT_RATE_START, "+numberFormat+"))INT_RATE_START  ," + 
				"        TRIM("+dateFormat+" (TPend.INT_RATE_END, "+numberFormat+")) INT_RATE_END    ," + 
				"        TRIM("+dateFormat+" (TPend.AMOUNT_START, "+numberFormat+")) AMOUNT_START    ," + 
				"        TRIM("+dateFormat+" (TPend.AMOUNT_END, "+numberFormat+"))   AMOUNT_END      ," +
				"        TRIM("+dateFormat+" (TPend.FTP_RATE, "+numberFormat+"))   FTP_RATE      ," +
				"        TRIM("+dateFormat+" (TPend.CRR_RATE, "+numberFormat+"))   CRR_RATE      ," +
				"        TPend.FTP_CURVE_STATUS, "
				+"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TPend.FTP_CURVE_STATUS_NT AND NUM_SUB_TAB = TPend.FTP_CURVE_STATUS )FTP_CURVE_STATUS_DESC ," 
				+ "      TPend.RECORD_INDICATOR," + 
				"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TPend.RECORD_INDICATOR_NT AND NUM_SUB_TAB = TPend.RECORD_INDICATOR )RECORD_INDICATOR_DESC ," + 
				"        TPend.MAKER ," +makerPendDesc+", "+ 
				"        TPend.VERIFIER," +verifierPendDesc +", "+
				"        TPend.INTERNAL_STATUS," + 
				dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, "+
				"        TPend.PRODUCT_ATTRIBUTE," + 
				"        (SELECT DISTINCT PRODUCT_ATT_DESCRIPTION FROM product_narrow where PRODUCT_ATTRIBUTE = TPend.PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC" + 
				" FROM FTP_CURVES_PEND TPend WHERE" + 
				" TPend.Country = ? And TPend.LE_Book = ? And TPend.FTP_CURVE_ID = ? ");
		
		String strBufPendAnd = " And TPend.EFFECTIVE_DATE =(" + 
				" SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_CURVES_PEND t2" + 
				" WHERE t2.Country   = TPend.Country" + 
				" AND t2.LE_Book   = TPend.LE_Book" + 
				" And t2.FTP_CURVE_ID   = TPend.FTP_CURVE_ID" + 
				" And T2.TENOR_APPLICATION_CODE   = TPend.TENOR_APPLICATION_CODE   " + 
				" And T2.TENOR_CODE = TPend.TENOR_CODE   " + 
				" And T2.Vision_SBU_Attribute   = TPend.Vision_SBU_Attribute" + 
				" And T2.Product_Attribute      = TPend.Product_Attribute" + 
				" And T2.Currency               = TPend.Currency" + 
				" And T2.Int_Rate_Start         = TPend.Int_Rate_Start" + 
				" And T2.Int_Rate_End           = TPend.Int_Rate_End" + 
				" And T2.Amount_Start           = TPend.Amount_Start" + 
				" And T2.Amount_End             = TPend.Amount_End" + 
				" AND t2.EFFECTIVE_DATE <= NVL(TO_DATE (?, 'DD-MM-RRRR') , trunc(sysdate))" + 
				" And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) ) ";
		
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			
			strBufApproveAnd = " And TAppr.EFFECTIVE_DATE =(" + 
					" SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_CURVES t2" + 
					" WHERE t2.Country   = TAppr.Country" + 
					" AND t2.LE_Book   = TAppr.LE_Book" + 
					" And t2.FTP_CURVE_ID   = TAppr.FTP_CURVE_ID" + 
					" And T2.TENOR_APPLICATION_CODE   = TAppr.TENOR_APPLICATION_CODE   " + 
					" And T2.TENOR_CODE = TAppr.TENOR_CODE   " + 
					" And T2.Vision_SBU_Attribute   = TAppr.Vision_SBU_Attribute" + 
					" And T2.Product_Attribute      = TAppr.Product_Attribute" + 
					" And T2.Currency               = TAppr.Currency" + 
					" And T2.Int_Rate_Start         = TAppr.Int_Rate_Start" + 
					" And T2.Int_Rate_End           = TAppr.Int_Rate_End" + 
					" And T2.Amount_Start           = TAppr.Amount_Start" + 
					" And T2.Amount_End             = TAppr.Amount_End"
					+ " AND t2.EFFECTIVE_DATE <= ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) "
					+ " AND ISNULL(t2.End_Date, CONVERT(DATE, '31-12-2099', 105)) > ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) ";
			
			strBufPendAnd = " And TPend.EFFECTIVE_DATE =(" + 
					" SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_CURVES_PEND t2" + 
					" WHERE t2.Country   = TPend.Country" + 
					" AND t2.LE_Book   = TPend.LE_Book" + 
					" And t2.FTP_CURVE_ID   = TPend.FTP_CURVE_ID" + 
					" And T2.TENOR_APPLICATION_CODE   = TPend.TENOR_APPLICATION_CODE   " + 
					" And T2.TENOR_CODE = TPend.TENOR_CODE   " + 
					" And T2.Vision_SBU_Attribute   = TPend.Vision_SBU_Attribute" + 
					" And T2.Product_Attribute      = TPend.Product_Attribute" + 
					" And T2.Currency               = TPend.Currency" + 
					" And T2.Int_Rate_Start         = TPend.Int_Rate_Start" + 
					" And T2.Int_Rate_End           = TPend.Int_Rate_End" + 
					" And T2.Amount_Start           = TPend.Amount_Start" + 
					" And T2.Amount_End             = TPend.Amount_End" + 
					" AND t2.EFFECTIVE_DATE <= ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) "
					+ " AND ISNULL(t2.End_Date, CONVERT(DATE, '31-12-2099', 105)) > ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) ";
		}
		strBufApprove.append(" "+strBufApproveAnd+" ");
		
		strBufPend.append(" "+strBufPendAnd+" ");
		
		try {
			/*
			 * if (ValidationUtil.isValid(effectiveDate) && flag) {
			 * params.addElement(effectiveDate);
			 * CommonUtils.addToQuery("EFFECTIVE_DATE >= TO_DATE(?,'DD-MM-RRRR')",
			 * strBufApprove); }else{ params.addElement(effectiveDate);
			 * CommonUtils.addToQuery("EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')",
			 * strBufApprove); }
			 */
			String orderBy = " Order By TENOR_APPLICATION_CODE";

			Object objParams[] = objParams = new Object[5];
			objParams[0] = dObj.getCountry();
			objParams[1] = dObj.getLeBook();
			objParams[2] = dObj.getFtpCurveId();
			objParams[3] = dObj.getEffectiveDate();
			objParams[4] = dObj.getEffectiveDate();
			
			Vector<Object> params = new Vector<Object>();
			
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpCurveId());
			params.addElement(dObj.getEffectiveDate());
			params.addElement(dObj.getEffectiveDate());
				if(ValidationUtil.isValid(dObj.getTenorApplicationCode()) && dObj.getTenorApplicationCode() != -1 ) {
					strBufApprove.append(" AND TAppr.TENOR_APPLICATION_CODE = ? ");
					strBufPend.append(" AND TPend.TENOR_APPLICATION_CODE = ? ");
					params.addElement(dObj.getTenorApplicationCode());
				}
				if(ValidationUtil.isValid(dObj.getTenorCode())) {
					strBufApprove.append(" AND TAppr.TENOR_CODE = ? ");
					strBufPend.append(" AND TPend.TENOR_CODE = ? ");
					params.addElement(dObj.getTenorCode());
				}
				
				if(ValidationUtil.isValid(dObj.getCurrency())) {
					strBufApprove.append(" AND TAppr.CURRENCY = ? ");
					strBufPend.append(" AND TPend.CURRENCY = ? ");
					params.addElement(dObj.getCurrency());
				}
				
				if(ValidationUtil.isValid(dObj.getVisionSbuAttribute())) {
					strBufApprove.append(" AND TAppr.VISION_SBU_ATTRIBUTE = ? ");
					strBufPend.append(" AND TPend.VISION_SBU_ATTRIBUTE = ? ");
					params.addElement(dObj.getVisionSbuAttribute());
				}
				
				if(ValidationUtil.isValid(dObj.getProductAttribute())) {
					strBufApprove.append(" AND TAppr.PRODUCT_ATTRIBUTE = ? ");
					strBufPend.append(" AND TPend.PRODUCT_ATTRIBUTE = ? ");
					params.addElement(dObj.getProductAttribute());
					
				}
				String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_CURVES_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_CURVE_ID = TPend.FTP_CURVE_ID AND TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE AND TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TAppr.TENOR_CODE = TPend.TENOR_CODE AND TAppr.VISION_SBU_ATTRIBUTE = TPend.VISION_SBU_ATTRIBUTE AND TAppr.PRODUCT_ATTRIBUTE = TPend.PRODUCT_ATTRIBUTE AND TAppr.CURRENCY = TPend.CURRENCY AND TAppr.INT_RATE_START = TPend.INT_RATE_START AND TAppr.INT_RATE_END = TPend.INT_RATE_END AND TAppr.AMOUNT_START = TPend.AMOUNT_START AND TAppr.AMOUNT_END = TPend.AMOUNT_END )");
//			List<FTPCurveVb> collTemp = getJdbcTemplate().query(strBufApprove.toString() + " " + orderBy, objParams, getCurveMapper());
//			dObj.setTotalRows(collTemp.size() );
//			return getQueryPopupResults(dObj, strBufPend, strBufApprove, strWhereNotExists, orderBy, params,getCurveMapper());
			int Ctr = 0;
			int Ctr2 = 0;
			String query = "";
			if(dObj.isVerificationRequired()){
				objParams = new Object[params.size()*2];
				for(Ctr=0; Ctr < params.size(); Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr);
				for(Ctr2=0 ; Ctr2 < params.size(); Ctr2++, Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr2);
				
				strBufApprove.append(" AND "+ strWhereNotExists);
				strBufPend.append(orderBy);
				
				query = strBufApprove.toString() + " Union " + strBufPend.toString();
				
			}else{
				objParams = new Object[params.size()];
				for(Ctr=0; Ctr < params.size(); Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr);
				strBufApprove.append(orderBy);
				query = strBufApprove.toString();
			}
				
			return getJdbcTemplate().query(query, objParams, getCurveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			return null;
		}
	}
	
	public List<FTPCurveVb> getQueryPopupResultsCurvesOverAllReview(FTPCurveVb dObj) {
		dObj.setRecordIndicator(0);
		StringBuffer strBufApprove = new StringBuffer("SELECT TAppr.COUNTRY," + 
					"        TAppr.LE_BOOK," + 
					"        TAppr.FTP_CURVE_ID                                            ," + 
					"        "+dateFormat+" (TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE," + 
					"		"+dateFormat+" (TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE," + 
					"        TAppr.TENOR_APPLICATION_CODE                                  ," + 
					"        TAppr.TENOR_CODE                                              ," + 
					"        (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND     TENOR_CODE             = TAppr.TENOR_CODE)TENOR_DESC," + 
					"        "+nullFun+" (TAppr.VISION_SBU_ATTRIBUTE, '000')                      VISION_SBU_ATTRIBUTE," + 
					"        (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB     = 300 AND     ALPHA_SUB_TAB = TAppr.VISION_SBU_ATTRIBUTE)VISIONSBU_DESC ," + 
					"        TAppr.CURRENCY                                                               ," + 
					"        ( SELECT CCY_DESCRIPTION DESCRIPTION FROM CURRENCIES WHERE CCY_STATUS = 0 AND     CURRENCY   = TAppr.CURRENCY)                  CURRENCY_DESC   ," + 
					"        TRIM("+dateFormat+" (TAppr.INT_RATE_START, "+numberFormat+"))INT_RATE_START  ," + 
					"        TRIM("+dateFormat+" (TAppr.INT_RATE_END, "+numberFormat+")) INT_RATE_END    ," + 
					"        TRIM("+dateFormat+" (TAppr.AMOUNT_START, "+numberFormat+")) AMOUNT_START    ," + 
					"        TRIM("+dateFormat+" (TAppr.AMOUNT_END, "+numberFormat+"))   AMOUNT_END      ," + 
					"        TRIM("+dateFormat+" (TAppr.FTP_RATE, "+numberFormat+"))   FTP_RATE      ," +
					"        TRIM("+dateFormat+" (TAppr.CRR_RATE, "+numberFormat+"))   CRR_RATE      ," +					"        TAppr.FTP_CURVE_STATUS, "
					+"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TAppr.FTP_CURVE_STATUS_NT AND NUM_SUB_TAB = TAppr.FTP_CURVE_STATUS )FTP_CURVE_STATUS_DESC ," 
					+ "      TAppr.RECORD_INDICATOR," + 
					"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TAppr.RECORD_INDICATOR_NT AND NUM_SUB_TAB = TAppr.RECORD_INDICATOR )RECORD_INDICATOR_DESC ," + 
					"        TAppr.MAKER ," +makerApprDesc+", "+ 
					"        TAppr.VERIFIER," +verifierApprDesc +", "+
					"        TAppr.INTERNAL_STATUS," + 
					dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
					+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, "+
					"        TAppr.PRODUCT_ATTRIBUTE," + 
					"        (SELECT DISTINCT PRODUCT_ATT_DESCRIPTION FROM product_narrow where PRODUCT_ATTRIBUTE = TAppr.PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC" + 
					" FROM FTP_CURVES TAppr WHERE" + 
					"   TAppr.Country = ? And TAppr.LE_Book = ? And TAppr.FTP_CURVE_ID = ? ");
		StringBuffer strBufPend = new StringBuffer("SELECT TPend.COUNTRY," + 
				"        TPend.LE_BOOK," + 
				"        TPend.FTP_CURVE_ID                                            ," + 
				"        "+dateFormat+" (TPend.EFFECTIVE_DATE, 'DD-Mon-RRRR') EFFECTIVE_DATE," + 
				"		"+dateFormat+" (TPend.END_DATE, "+dateAloneFormatStr+") END_DATE," + 
				"        TPend.TENOR_APPLICATION_CODE                                  ," + 
				"        TPend.TENOR_CODE                                              ," + 
				"        (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND     TENOR_CODE             = TPend.TENOR_CODE)TENOR_DESC," + 
				"        "+nullFun+" (TPend.VISION_SBU_ATTRIBUTE, '000')                      VISION_SBU_ATTRIBUTE," + 
				"        (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB     = 300 AND     ALPHA_SUB_TAB = TPend.VISION_SBU_ATTRIBUTE)VISIONSBU_DESC ," + 
				"        TPend.CURRENCY                                                               ," + 
				"        ( SELECT CCY_DESCRIPTION DESCRIPTION FROM CURRENCIES WHERE CCY_STATUS = 0 AND     CURRENCY   = TPend.CURRENCY)                  CURRENCY_DESC   ," + 
				"        TRIM("+dateFormat+" (TPend.INT_RATE_START, "+numberFormat+"))INT_RATE_START  ," + 
				"        TRIM("+dateFormat+" (TPend.INT_RATE_END, "+numberFormat+")) INT_RATE_END    ," + 
				"        TRIM("+dateFormat+" (TPend.AMOUNT_START, "+numberFormat+")) AMOUNT_START    ," + 
				"        TRIM("+dateFormat+" (TPend.AMOUNT_END, "+numberFormat+"))   AMOUNT_END      ," +
				"        TRIM("+dateFormat+" (TPend.FTP_RATE, "+numberFormat+"))   FTP_RATE      ," +
				"        TRIM("+dateFormat+" (TPend.CRR_RATE, "+numberFormat+"))   CRR_RATE      ," +
				"        TPend.FTP_CURVE_STATUS, "
				+"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TPend.FTP_CURVE_STATUS_NT AND NUM_SUB_TAB = TPend.FTP_CURVE_STATUS )FTP_CURVE_STATUS_DESC ," 
				+ "      TPend.RECORD_INDICATOR," + 
				"        ( SELECT NUM_SUBTAB_DESCRIPTION FROM NUM_SUB_TAB WHERE NUM_TAB     = TPend.RECORD_INDICATOR_NT AND NUM_SUB_TAB = TPend.RECORD_INDICATOR )RECORD_INDICATOR_DESC ," + 
				"        TPend.MAKER ," +makerPendDesc+", "+ 
				"        TPend.VERIFIER," +verifierPendDesc +", "+
				"        TPend.INTERNAL_STATUS," + 
				dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, "+
				"        TPend.PRODUCT_ATTRIBUTE," + 
				"        (SELECT DISTINCT PRODUCT_ATT_DESCRIPTION FROM product_narrow where PRODUCT_ATTRIBUTE = TPend.PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC" + 
				" FROM FTP_CURVES_PEND TPend WHERE" + 
				" TPend.Country = ? And TPend.LE_Book = ? And TPend.FTP_CURVE_ID = ? ");
		try {
			String orderBy = " Order By EFFECTIVE_DATE ";

			Object objParams[] = objParams = new Object[5];
			
			Vector<Object> params = new Vector<Object>();
			
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpCurveId());
			String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_CURVES_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_CURVE_ID = TPend.FTP_CURVE_ID AND TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE AND TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TAppr.TENOR_CODE = TPend.TENOR_CODE AND TAppr.VISION_SBU_ATTRIBUTE = TPend.VISION_SBU_ATTRIBUTE AND TAppr.PRODUCT_ATTRIBUTE = TPend.PRODUCT_ATTRIBUTE AND TAppr.CURRENCY = TPend.CURRENCY AND TAppr.INT_RATE_START = TPend.INT_RATE_START AND TAppr.INT_RATE_END = TPend.INT_RATE_END AND TAppr.AMOUNT_START = TPend.AMOUNT_START AND TAppr.AMOUNT_END = TPend.AMOUNT_END )");
			int Ctr = 0;
			int Ctr2 = 0;
			String query = "";
			if(dObj.isVerificationRequired()){
				objParams = new Object[params.size()*2];
				for(Ctr=0; Ctr < params.size(); Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr);
				for(Ctr2=0 ; Ctr2 < params.size(); Ctr2++, Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr2);
				
				strBufApprove.append(" AND "+ strWhereNotExists);
				strBufPend.append(orderBy);
				
				query = strBufApprove.toString() + " Union " + strBufPend.toString();
				
			}else{
				objParams = new Object[params.size()];
				for(Ctr=0; Ctr < params.size(); Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr);
				strBufApprove.append(orderBy);
				query = strBufApprove.toString();
			}
				
			return getJdbcTemplate().query(query, objParams, getCurveMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			return null;
		}
	}
	protected RowMapper getCurveMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpCurveVb = new FTPCurveVb();
				if(rs.getString("COUNTRY")!= null){ 
					ftpCurveVb.setCountry(rs.getString("COUNTRY"));
				}
				if(rs.getString("LE_BOOK")!= null){ 
					ftpCurveVb.setLeBook(rs.getString("LE_BOOK"));
				}
				
				ftpCurveVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				/* ftpCurveVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE")); */
				ftpCurveVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpCurveVb.setTenorApplicationCode(rs.getInt("TENOR_APPLICATION_CODE"));
				ftpCurveVb.setTenorCode(rs.getString("TENOR_CODE"));
				ftpCurveVb.setVisionSbuAttribute(rs.getString("VISION_SBU_ATTRIBUTE"));
				ftpCurveVb.setCurrency(rs.getString("CURRENCY"));
				ftpCurveVb.setIntRateStart(rs.getString("INT_RATE_START"));
				ftpCurveVb.setIntRateEnd(rs.getString("INT_RATE_END"));
				ftpCurveVb.setAmountStart(rs.getString("AMOUNT_START"));
				ftpCurveVb.setAmountEnd(rs.getString("AMOUNT_END"));
				ftpCurveVb.setFtpRate(rs.getString("FTP_RATE"));
				ftpCurveVb.setCrrRate(rs.getString("CRR_RATE"));
//				ftpCurveVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
//				ftpCurveVb.setSubRate(rs.getString("FTP_CURVE"));
				ftpCurveVb.setFtpCurveStatus(rs.getInt("FTP_CURVE_STATUS"));
				ftpCurveVb.setDbStatus(rs.getInt("FTP_CURVE_STATUS"));
				ftpCurveVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpCurveVb.setMaker(rs.getInt("MAKER"));
				ftpCurveVb.setVerifier(rs.getInt("VERIFIER"));
				ftpCurveVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpCurveVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpCurveVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpCurveVb.setMaker(rs.getInt("MAKER"));
				ftpCurveVb.setVerifier(rs.getInt("VERIFIER"));
				ftpCurveVb.setStatusDesc(rs.getString("FTP_CURVE_STATUS_DESC"));
				ftpCurveVb.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				ftpCurveVb.setProductAttribute(rs.getString("PRODUCT_ATTRIBUTE"));
				ftpCurveVb.setProductAttributeDesc(rs.getString("PRODUCT_ATTRIBUTE_DESC"));
				if(ValidationUtil.isValid(ftpCurveVb.getProductAttribute()) && "ALL".equalsIgnoreCase(ftpCurveVb.getProductAttribute())) {
					ftpCurveVb.setProductAttributeDesc("All");
				}
				ftpCurveVb.setCurrencyDesc(rs.getString("CURRENCY_DESC"));
				ftpCurveVb.setTenorCodeDesc(rs.getString("TENOR_DESC"));
				ftpCurveVb.setVisionSbuAttributeDesc(rs.getString("VISIONSBU_DESC"));
				ftpCurveVb.setMakerName(rs.getString("MAKER_NAME"));
				ftpCurveVb.setVerifierName(rs.getString("VERIFIER_NAME"));
				return ftpCurveVb;
			}
		};
		return mapper;
	}
	
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doDeleteApprRecord(List<FTPCurveVb> vObjects, FTPCurveVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FTPCurveVb vObject : vObjects) {
				if (vObject.isChecked()) {
					vObject.setStaticDelete(vObjectParam.isStaticDelete());
					vObject.setVerificationRequired(vObjectParam.isVerificationRequired());
					exceptionCode = doDeleteApprRecordForNonTrans(vObject);
					if (exceptionCode == null || exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						throw buildRuntimeCustomException(exceptionCode);
					}
				}
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
	public ExceptionCode doDeleteRecord(List<FTPCurveVb> vObjects, FTPCurveVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FTPCurveVb vObject : vObjects) {
				if (vObject.isChecked()) {
					vObject.setStaticDelete(vObjectParam.isStaticDelete());
					vObject.setVerificationRequired(vObjectParam.isVerificationRequired());
					exceptionCode = doDeleteRecordForNonTrans(vObject);
					if (exceptionCode == null || exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
						throw buildRuntimeCustomException(exceptionCode);
					}
				}
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
}
