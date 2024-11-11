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
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.FtpPremiumsVb;

@Component
public class FtpPremiumsDao extends AbstractDao<FtpPremiumsVb> {

	@Value("${app.databaseType}")
	private String databaseType;
	
/*******Mapper Start**********/
	String CountryApprDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TAppr.COUNTRY) COUNTRY_DESC";
	String CountryPendDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TPend.COUNTRY) COUNTRY_DESC";
	String LeBookApprDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TAppr.LE_BOOK AND COUNTRY = TAppr.COUNTRY ) LE_BOOK_DESC";
	String LeBookTPendDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TPend.LE_BOOK AND COUNTRY = TPend.COUNTRY ) LE_BOOK_DESC";

	String TenorApplicationCodeNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 8, "TAppr.LP_TENOR_APPLICATION_CODE", "LP_TENOR_APPLICATION_CODE_DESC");
	String TenorApplicationCodeNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 8, "TPend.LP_TENOR_APPLICATION_CODE", "LP_TENOR_APPLICATION_CODE_DESC");

	String VisionSectorAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 59, "TAppr.VISION_SECTOR", "VISION_SECTOR_DESC");
	String VisionSectorAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 59, "TPend.VISION_SECTOR", "VISION_SECTOR_DESC");

	String VisionSbuAttributeAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 3, "TAppr.VISION_SBU_ATTRIBUTE", "VISION_SBU_ATTRIBUTE_DESC");
	String VisionSbuAttributeAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 3, "TPend.VISION_SBU_ATTRIBUTE", "VISION_SBU_ATTRIBUTE_DESC");

	String FtpPremiumStatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_PREMIUM_STATUS", "FTP_PREMIUM_STATUS_DESC");
	String FtpPremiumStatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_PREMIUM_STATUS", "FTP_PREMIUM_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpPremiumsVb vObject = new FtpPremiumsVb();
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
				vObject.setLpTenorApplicationCode(rs.getInt("LP_TENOR_APPLICATION_CODE"));
				vObject.setLpTenorApplicationCodeDesc(rs.getString("LP_TENOR_APPLICATION_CODE"));
				if(rs.getString("LP_TENOR_CODE")!= null){ 
					vObject.setLpTenorCode(rs.getString("LP_TENOR_CODE"));
				}else{
					vObject.setLpTenorCode("");
				}
				if(rs.getString("LP_TENOR_CODE_DESC")!= null){ 
					vObject.setLpTenorCodeDesc(rs.getString("LP_TENOR_CODE_DESC"));
				}
				vObject.setVisionSectorAt(rs.getInt("VISION_SECTOR_AT"));
				if(rs.getString("VISION_SECTOR")!= null){ 
					vObject.setVisionSector(rs.getString("VISION_SECTOR"));
				}else{
					vObject.setVisionSector("");
				}
				if(rs.getString("VISION_SECTOR_DESC")!= null){ 
					vObject.setVisionSectorDesc(rs.getString("VISION_SECTOR_DESC"));
				}
				vObject.setVisionSbuAttributeAt(rs.getInt("VISION_SBU_ATTRIBUTE_AT"));
				if(rs.getString("VISION_SBU_ATTRIBUTE")!= null){ 
					vObject.setVisionSbuAttribute(rs.getString("VISION_SBU_ATTRIBUTE"));
				}else{
					vObject.setVisionSbuAttribute("");
				}
				
				if(rs.getString("VISION_SBU_ATTRIBUTE_DESC")!= null){ 
					vObject.setVisionSbuAttributeDesc(rs.getString("VISION_SBU_ATTRIBUTE_DESC"));
				}
				if(vObject.getVisionSbuAttribute().equalsIgnoreCase("ALL")) {
					vObject.setVisionSbuAttributeDesc("All");
				}
				if(rs.getString("PRODUCT_ATTRIBUTE")!= null){ 
					vObject.setProductAttribute(rs.getString("PRODUCT_ATTRIBUTE"));
				}else{
					vObject.setProductAttribute("");
				}
				if(rs.getString("PRODUCT_ATTRIBUTE_DESC")!= null){ 
					vObject.setProductAttributeDesc(rs.getString("PRODUCT_ATTRIBUTE_DESC"));
				}
				if(rs.getString("CUSTOMER_ID")!= null){ 
					vObject.setCustomerId(rs.getString("CUSTOMER_ID"));
				}else{
					vObject.setCustomerId("");
				}
				if(rs.getString("CUSTOMER_ID_DESC")!= null){ 
					vObject.setCustomerIdDesc(rs.getString("CUSTOMER_ID_DESC"));
				}
				if(rs.getString("CONTRACT_ID")!= null){ 
					vObject.setContractId(rs.getString("CONTRACT_ID"));
				}else{
					vObject.setContractId("");
				}
				if(rs.getString("CONTRACT_ID_DESC")!= null){ 
					vObject.setContractIdDesc(rs.getString("CONTRACT_ID_DESC"));
				}
				if(rs.getString("CURRENCY")!= null){ 
					vObject.setCurrency(rs.getString("CURRENCY"));
				}else{
					vObject.setCurrency("");
				}
				vObject.setPremiumRate(rs.getString("PREMIUM_RATE"));
				if(rs.getString("END_DATE")!= null){ 
					vObject.setEndDate(rs.getString("END_DATE"));
				}else{
					vObject.setEndDate("");
				}
				vObject.setFtpPremiumStatusNt(rs.getInt("FTP_PREMIUM_STATUS_NT"));
				vObject.setFtpPremiumStatus(rs.getInt("FTP_PREMIUM_STATUS"));
				vObject.setStatusDesc(rs.getString("FTP_PREMIUM_STATUS_DESC"));
				vObject.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				
				vObject.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				
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
				if(ValidationUtil.isValid(rs.getString("MAKER_NAME")))
					vObject.setMakerName(rs.getString("MAKER_NAME"));
				
				if(ValidationUtil.isValid(rs.getString("VERIFIER_NAME")))
					vObject.setVerifierName(rs.getString("VERIFIER_NAME"));
				return vObject;
			}
		};
		return mapper;
	}

	public List<FtpPremiumsVb> getQueryResults(FtpPremiumsVb dObj, int intStatus){

		setServiceDefaults();

		List<FtpPremiumsVb> collTemp = null;

		final int intKeyFieldsCount = 12;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_CURVE_ID"
			+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TAppr.TENOR_APPLICATION_CODE_NT"
			+ ",TAppr.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
			+ ",TAppr.LP_TENOR_CODE"
//			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE LP_TENOR_APPLICATION_CODE = TAppr.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.LP_TENOR_CODE) LP_TENOR_CODE_DESC "
			+" , (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT =  TAppr.TENOR_APPLICATION_CODE_NT and TENOR_APPLICATION_CODE = TAppr.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.LP_TENOR_CODE) LP_TENOR_CODE_DESC  "
			+ ",TAppr.VISION_SECTOR_AT"
			+ ",TAppr.VISION_SECTOR, "+VisionSectorAtApprDesc
			+ ",TAppr.VISION_SBU_ATTRIBUTE_AT"
			+ ",TAppr.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtApprDesc
			+ ",TAppr.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TAppr.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
			+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"			
			+ ",TAppr.CURRENCY"
			+ ",TRIM("+dateFormat+" (TAppr.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
			+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TAppr.FTP_PREMIUM_STATUS_NT"
			+ ",TAppr.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_PREMIUMS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = ?  AND LP_TENOR_APPLICATION_CODE = ?  AND LP_TENOR_CODE = ?  "
			+ "AND (VISION_SECTOR IS NULL OR VISION_SECTOR = ?) AND "
			+ " (VISION_SBU_ATTRIBUTE = ? OR VISION_SBU_ATTRIBUTE IS NULL)  AND "
			+ " (PRODUCT_ATTRIBUTE = ? OR PRODUCT_ATTRIBUTE IS NULL) AND "
			+ " (CUSTOMER_ID = ? OR CUSTOMER_ID IS NULL)  AND "
			+ " (CONTRACT_ID = ? OR CONTRACT_ID IS NULL)  AND "
			+ " (CURRENCY = ? OR CURRENCY IS NULL)  ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_CURVE_ID"
			+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TPend.TENOR_APPLICATION_CODE_NT"
			+ ",TPend.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
			+ ",TPend.LP_TENOR_CODE"
//			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE LP_TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.LP_TENOR_CODE) LP_TENOR_CODE_DESC "
			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT =  TPend.TENOR_APPLICATION_CODE_NT and TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.LP_TENOR_CODE) LP_TENOR_CODE_DESC  "
			+ ",TPend.VISION_SECTOR_AT"
			+ ",TPend.VISION_SECTOR, "+VisionSectorAtPendDesc
			+ ",TPend.VISION_SBU_ATTRIBUTE_AT"
			+ ",TPend.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtPendDesc
			+ ",TPend.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TPend.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
			+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"
			+ ",TPend.CURRENCY"
			+ ",TRIM("+dateFormat+" (TPend.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
			+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TPend.FTP_PREMIUM_STATUS_NT"
			+ ",TPend.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_PREMIUMS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = ?  AND LP_TENOR_APPLICATION_CODE = ?  AND LP_TENOR_CODE = ?  "
			+ "AND (VISION_SECTOR IS NULL OR VISION_SECTOR = ?) AND "
			+ " (VISION_SBU_ATTRIBUTE = ? OR VISION_SBU_ATTRIBUTE IS NULL)  AND "
			+ " (PRODUCT_ATTRIBUTE = ? OR PRODUCT_ATTRIBUTE IS NULL) AND "
			+ " (CUSTOMER_ID = ? OR CUSTOMER_ID IS NULL)  AND "
			+ " (CONTRACT_ID = ? OR CONTRACT_ID IS NULL)  AND "
			+ " (CURRENCY = ? OR CURRENCY IS NULL)  ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpCurveId();
		objParams[3] = dObj.getEffectiveDate();
		objParams[4] = dObj.getLpTenorApplicationCode();
		objParams[5] = dObj.getLpTenorCode();
		objParams[6] = dObj.getVisionSector();
		objParams[7] = dObj.getVisionSbuAttribute();
		objParams[8] = dObj.getProductAttribute();
		objParams[9] = dObj.getCustomerId();
		objParams[10] = dObj.getContractId();
		objParams[11] = dObj.getCurrency();

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
	public List<FtpPremiumsVb> getQueryResultsByParent(FtpMethodsVb dObj, int intStatus, boolean onlyActive){

		setServiceDefaults();

		List<FtpPremiumsVb> collTemp = null;

		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_CURVE_ID"
			+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TAppr.TENOR_APPLICATION_CODE_NT"
			+ ",TAppr.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
			+ ",TAppr.LP_TENOR_CODE"
			+" , (SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT =  TAppr.TENOR_APPLICATION_CODE_NT and TENOR_APPLICATION_CODE = TAppr.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.LP_TENOR_CODE) LP_TENOR_CODE_DESC  "
			+ ",TAppr.VISION_SECTOR_AT"
			+ ",TAppr.VISION_SECTOR, "+VisionSectorAtApprDesc
			+ ",TAppr.VISION_SBU_ATTRIBUTE_AT"
			+ ",TAppr.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtApprDesc
			+ ",TAppr.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TAppr.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
			+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"			
			+ ",TAppr.CURRENCY"
			+ ",TRIM("+dateFormat+" (TAppr.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
			+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TAppr.FTP_PREMIUM_STATUS_NT"
			+ ",TAppr.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_PREMIUMS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_CURVE_ID"
			+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TPend.TENOR_APPLICATION_CODE_NT"
			+ ",TPend.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
			+ ",TPend.LP_TENOR_CODE"
			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT =  TPend.TENOR_APPLICATION_CODE_NT and TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.LP_TENOR_CODE) LP_TENOR_CODE_DESC  "
			+ ",TPend.VISION_SECTOR_AT"
			+ ",TPend.VISION_SECTOR, "+VisionSectorAtPendDesc
			+ ",TPend.VISION_SBU_ATTRIBUTE_AT"
			+ ",TPend.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtPendDesc
			+ ",TPend.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TPend.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
			+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"
			+ ",TPend.CURRENCY"
			+ ",TRIM("+dateFormat+" (TPend.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
			+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TPend.FTP_PREMIUM_STATUS_NT"
			+ ",TPend.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_PREMIUMS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ");
		final int intKeyFieldsCount = 3;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpCurveId();
		if(onlyActive) {
			strQueryAppr = strQueryAppr  + "AND FTP_PREMIUM_STATUS != 9";
			strQueryPend = strQueryPend  + "AND FTP_PREMIUM_STATUS != 9";
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
	protected List<FtpPremiumsVb> selectApprovedRecord(FtpPremiumsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}


	@Override
	protected List<FtpPremiumsVb> doSelectPendingRecord(FtpPremiumsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}


	@Override
	protected int getStatus(FtpPremiumsVb records){return records.getFtpPremiumStatus();}


	@Override
	protected void setStatus(FtpPremiumsVb vObject,int status){vObject.setFtpPremiumStatus(status);}


	public void removeComma(FtpPremiumsVb vObject) {
		vObject.setPremiumRate(vObject.getPremiumRate().replaceAll(",", ""));
	}
	
	@Override
	protected int doInsertionAppr(FtpPremiumsVb vObject) {
		removeComma(vObject);
		String query = "Insert Into FTP_PREMIUMS (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, "
				+ "LP_TENOR_APPLICATION_CODE, LP_TENOR_CODE, VISION_SECTOR_AT, VISION_SECTOR, VISION_SBU_ATTRIBUTE_AT, VISION_SBU_ATTRIBUTE, "
				+ "PRODUCT_ATTRIBUTE, CUSTOMER_ID, CONTRACT_ID, CURRENCY, PREMIUM_RATE, END_DATE, FTP_PREMIUM_STATUS_NT, FTP_PREMIUM_STATUS, "
				+ "RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getLpTenorApplicationCode(),
				vObject.getLpTenorCode(), vObject.getVisionSectorAt(), vObject.getVisionSector(),
				vObject.getVisionSbuAttributeAt(), vObject.getVisionSbuAttribute(), vObject.getProductAttribute(),
				vObject.getCustomerId(), vObject.getContractId(), vObject.getCurrency(), vObject.getPremiumRate(),
				vObject.getEndDate(), vObject.getFtpPremiumStatusNt(), vObject.getFtpPremiumStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doInsertionPend(FtpPremiumsVb vObject){
		removeComma(vObject);
		String query = "Insert Into FTP_PREMIUMS_PEND (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, "
				+ "LP_TENOR_APPLICATION_CODE, LP_TENOR_CODE, VISION_SECTOR_AT, VISION_SECTOR, VISION_SBU_ATTRIBUTE_AT, VISION_SBU_ATTRIBUTE, "
				+ "PRODUCT_ATTRIBUTE, CUSTOMER_ID, CONTRACT_ID, CURRENCY, PREMIUM_RATE, END_DATE, FTP_PREMIUM_STATUS_NT, FTP_PREMIUM_STATUS, "
				+ "RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getLpTenorApplicationCode(),
				vObject.getLpTenorCode(), vObject.getVisionSectorAt(), vObject.getVisionSector(),
				vObject.getVisionSbuAttributeAt(), vObject.getVisionSbuAttribute(), vObject.getProductAttribute(),
				vObject.getCustomerId(), vObject.getContractId(), vObject.getCurrency(), vObject.getPremiumRate(),
				vObject.getEndDate(), vObject.getFtpPremiumStatusNt(), vObject.getFtpPremiumStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doInsertionPendWithDc(FtpPremiumsVb vObject){
		removeComma(vObject);
		String query = "Insert Into FTP_PREMIUMS_PEND (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, "
				+ "LP_TENOR_APPLICATION_CODE, LP_TENOR_CODE, VISION_SECTOR_AT, VISION_SECTOR, VISION_SBU_ATTRIBUTE_AT, VISION_SBU_ATTRIBUTE, "
				+ "PRODUCT_ATTRIBUTE, CUSTOMER_ID, CONTRACT_ID, CURRENCY, PREMIUM_RATE, END_DATE, FTP_PREMIUM_STATUS_NT, FTP_PREMIUM_STATUS, "
				+ "RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getLpTenorApplicationCode(),
				vObject.getLpTenorCode(), vObject.getVisionSectorAt(), vObject.getVisionSector(),
				vObject.getVisionSbuAttributeAt(), vObject.getVisionSbuAttribute(), vObject.getProductAttribute(),
				vObject.getCustomerId(), vObject.getContractId(), vObject.getCurrency(), vObject.getPremiumRate(),
				vObject.getEndDate(), vObject.getFtpPremiumStatusNt(), vObject.getFtpPremiumStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus(), vObject.getDateCreation() };

		return getJdbcTemplate().update(query, args);
	
	}


	@Override
	protected int doUpdateAppr(FtpPremiumsVb vObject) {
		removeComma(vObject);
		String query = "Update FTP_PREMIUMS Set TENOR_APPLICATION_CODE_NT = ?, VISION_SECTOR_AT = ?, VISION_SBU_ATTRIBUTE_AT = ?, "
				+ "PREMIUM_RATE = ?, END_DATE = " + dateConvert
				+ ", FTP_PREMIUM_STATUS_NT = ?, FTP_PREMIUM_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?,"
				+ " MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = " + systemDate + "  "
				+ "WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = " + dateConvert + "  "
				+ "AND LP_TENOR_APPLICATION_CODE = ?  AND LP_TENOR_CODE = ?  AND (VISION_SECTOR = ? OR VISION_SECTOR IS NULL) AND (VISION_SBU_ATTRIBUTE = ? OR VISION_SBU_ATTRIBUTE IS NULL) "
				+ "AND (PRODUCT_ATTRIBUTE = ? OR PRODUCT_ATTRIBUTE IS NULL)  AND (CUSTOMER_ID = ? OR CUSTOMER_ID IS NULL)  AND (CONTRACT_ID = ? OR CONTRACT_ID IS NULL ) AND CURRENCY = ? ";
		
		Object[] args = { vObject.getTenorApplicationCodeNt(), vObject.getVisionSectorAt(),
				vObject.getVisionSbuAttributeAt(), vObject.getPremiumRate(), vObject.getEndDate(),
				vObject.getFtpPremiumStatusNt(), vObject.getFtpPremiumStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(), vObject.getEffectiveDate(),
				vObject.getLpTenorApplicationCode(), vObject.getLpTenorCode(), vObject.getVisionSector(),
				vObject.getVisionSbuAttribute(), vObject.getProductAttribute(), vObject.getCustomerId(),
				vObject.getContractId(), vObject.getCurrency() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doUpdatePend(FtpPremiumsVb vObject){
		removeComma(vObject);
		String query = "Update FTP_PREMIUMS_PEND Set TENOR_APPLICATION_CODE_NT = ?, VISION_SECTOR_AT = ?, VISION_SBU_ATTRIBUTE_AT = ?, "
				+ "PREMIUM_RATE = ?, END_DATE = " + dateConvert
				+ ", FTP_PREMIUM_STATUS_NT = ?, FTP_PREMIUM_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?,"
				+ " MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = " + systemDate + "  "
				+ "WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = " + dateConvert + "  "
				+ "AND LP_TENOR_APPLICATION_CODE = ?  AND LP_TENOR_CODE = ?  AND (VISION_SECTOR = ? OR VISION_SECTOR IS NULL) AND (VISION_SBU_ATTRIBUTE = ? OR VISION_SBU_ATTRIBUTE IS NULL) "
				+ "AND (PRODUCT_ATTRIBUTE = ? OR PRODUCT_ATTRIBUTE IS NULL)  AND (CUSTOMER_ID = ? OR CUSTOMER_ID IS NULL)  AND (CONTRACT_ID = ? OR CONTRACT_ID IS NULL ) AND CURRENCY = ? ";
		
		Object[] args = { vObject.getTenorApplicationCodeNt(), vObject.getVisionSectorAt(),
				vObject.getVisionSbuAttributeAt(), vObject.getPremiumRate(), vObject.getEndDate(),
				vObject.getFtpPremiumStatusNt(), vObject.getFtpPremiumStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(), vObject.getEffectiveDate(),
				vObject.getLpTenorApplicationCode(), vObject.getLpTenorCode(), vObject.getVisionSector(),
				vObject.getVisionSbuAttribute(), vObject.getProductAttribute(), vObject.getCustomerId(),
				vObject.getContractId(), vObject.getCurrency() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doDeleteAppr(FtpPremiumsVb vObject) {
		removeComma(vObject);
		String query = "Delete From FTP_PREMIUMS Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE ="
				+ dateConvert + "  "
				+ "AND LP_TENOR_APPLICATION_CODE = ?  AND LP_TENOR_CODE = ?   AND (VISION_SECTOR = ? OR VISION_SECTOR IS NULL) AND (VISION_SBU_ATTRIBUTE = ? OR VISION_SBU_ATTRIBUTE IS NULL) " + 
				" AND (PRODUCT_ATTRIBUTE = ? OR PRODUCT_ATTRIBUTE IS NULL)  AND (CUSTOMER_ID = ? OR CUSTOMER_ID IS NULL)  AND (CONTRACT_ID = ? OR CONTRACT_ID IS NULL ) AND CURRENCY = ? "; 
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getLpTenorApplicationCode(), vObject.getLpTenorCode(),
				vObject.getVisionSector(), vObject.getVisionSbuAttribute(), vObject.getProductAttribute(),
				vObject.getCustomerId(), vObject.getContractId(), vObject.getCurrency() };
		return getJdbcTemplate().update(query, args);
	}
	protected int doDeleteApprByParent(FtpMethodsVb vObject) {
		String query = "Delete From FTP_PREMIUMS Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId()};
		return getJdbcTemplate().update(query, args);
	}
	protected int doDeletePendingByParent(FtpMethodsVb vObject) {
		String query = "Delete From FTP_PREMIUMS_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId()};
		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int deletePendingRecord(FtpPremiumsVb vObject){
		removeComma(vObject);
		String query = "Delete From FTP_PREMIUMS_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE ="
				+ dateConvert + "  "
				+ "AND LP_TENOR_APPLICATION_CODE = ?  AND LP_TENOR_CODE = ?   AND (VISION_SECTOR = ? OR VISION_SECTOR IS NULL) AND (VISION_SBU_ATTRIBUTE = ? OR VISION_SBU_ATTRIBUTE IS NULL) " + 
				" AND (PRODUCT_ATTRIBUTE = ? OR PRODUCT_ATTRIBUTE IS NULL)  AND (CUSTOMER_ID = ? OR CUSTOMER_ID IS NULL)  AND (CONTRACT_ID = ? OR CONTRACT_ID IS NULL ) AND CURRENCY = ? "; 
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getLpTenorApplicationCode(), vObject.getLpTenorCode(),
				vObject.getVisionSector(), vObject.getVisionSbuAttribute(), vObject.getProductAttribute(),
				vObject.getCustomerId(), vObject.getContractId(), vObject.getCurrency() };
		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected String getAuditString(FtpPremiumsVb vObject){
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

				strAudit.append("LP_TENOR_APPLICATION_CODE"+auditDelimiterColVal+vObject.getLpTenorApplicationCode());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getLpTenorCode()))
				strAudit.append("LP_TENOR_CODE"+auditDelimiterColVal+vObject.getLpTenorCode().trim());
			else
				strAudit.append("LP_TENOR_CODE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("VISION_SECTOR_AT"+auditDelimiterColVal+vObject.getVisionSectorAt());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getVisionSector()))
				strAudit.append("VISION_SECTOR"+auditDelimiterColVal+vObject.getVisionSector().trim());
			else
				strAudit.append("VISION_SECTOR"+auditDelimiterColVal+"NULL");
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

			if(ValidationUtil.isValid(vObject.getCustomerId()))
				strAudit.append("CUSTOMER_ID"+auditDelimiterColVal+vObject.getCustomerId().trim());
			else
				strAudit.append("CUSTOMER_ID"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getContractId()))
				strAudit.append("CONTRACT_ID"+auditDelimiterColVal+vObject.getContractId().trim());
			else
				strAudit.append("CONTRACT_ID"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getCurrency()))
				strAudit.append("CURRENCY"+auditDelimiterColVal+vObject.getCurrency().trim());
			else
				strAudit.append("CURRENCY"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("PREMIUM_RATE"+auditDelimiterColVal+vObject.getPremiumRate());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getEndDate()))
				strAudit.append("END_DATE"+auditDelimiterColVal+vObject.getEndDate().trim());
			else
				strAudit.append("END_DATE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_PREMIUM_STATUS_NT"+auditDelimiterColVal+vObject.getFtpPremiumStatusNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_PREMIUM_STATUS"+auditDelimiterColVal+vObject.getFtpPremiumStatus());
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
		serviceName = "FtpPremiums";
		serviceDesc = "FTP Premiums";
		tableName = "FTP_PREMIUMS";
		childTableName = "FTP_PREMIUMS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
		
	}
	public List<FtpPremiumsVb> getQueryPopupResultPremium(FtpPremiumsVb dObj) {
		List<FtpPremiumsVb> collTemp = null;
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY"
						+ ",TAppr.LE_BOOK"
						+ ",TAppr.FTP_CURVE_ID"
						+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
						+ ",TAppr.TENOR_APPLICATION_CODE_NT"
						+ ",TAppr.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
						+ ",TAppr.LP_TENOR_CODE"
						+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TENOR_APPLICATION_CODE_NT AND  TENOR_APPLICATION_CODE = TAppr.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.LP_TENOR_CODE) LP_TENOR_CODE_DESC "
						+ ",TAppr.VISION_SECTOR_AT"
						+ ",TAppr.VISION_SECTOR, "+VisionSectorAtApprDesc
						+ ",TAppr.VISION_SBU_ATTRIBUTE_AT"
						+ ",TAppr.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtApprDesc
						+ ",TAppr.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TAppr.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
						+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
						+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"			
						+ ",TAppr.CURRENCY"
						+ ",TRIM("+dateFormat+" (TAppr.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
						+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
						+ ",TAppr.FTP_PREMIUM_STATUS_NT"
						+ ",TAppr.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtApprDesc
						+ ",TAppr.RECORD_INDICATOR_NT"
						+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
						+ ",TAppr.MAKER, "+makerApprDesc
						+ ",TAppr.VERIFIER, "+verifierApprDesc
						+ ",TAppr.INTERNAL_STATUS"
						+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED "
						+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
				" FROM FTP_PREMIUMS TAppr WHERE  \r\n" + 
				"					   TAppr.Country = ? And TAppr.LE_Book = ? And TAppr.FTP_CURVE_ID = ? ");
		StringBuffer strBufPend = new StringBuffer("Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_CURVE_ID"
				+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
				+ ",TPend.TENOR_APPLICATION_CODE_NT"
				+ ",TPend.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
				+ ",TPend.LP_TENOR_CODE"
				+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TENOR_APPLICATION_CODE_NT AND  TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.LP_TENOR_CODE) LP_TENOR_CODE_DESC "
				+ ",TPend.VISION_SECTOR_AT"
				+ ",TPend.VISION_SECTOR, "+VisionSectorAtPendDesc
				+ ",TPend.VISION_SBU_ATTRIBUTE_AT"
				+ ",TPend.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtPendDesc
				+ ",TPend.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TPend.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
				+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
				+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"			
				+ ",TPend.CURRENCY"
				+ ",TRIM("+dateFormat+" (TPend.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
				+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
				+ ",TPend.FTP_PREMIUM_STATUS_NT"
				+ ",TPend.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED "
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
		" FROM FTP_PREMIUMS_PEND TPend WHERE  \r\n" + 
		"					   TPend.Country = ? And TPend.LE_Book = ? And TPend.FTP_CURVE_ID = ? ");
		Object objParams[]=null;
		
		String strBufApproveAnd = " And TAppr.EFFECTIVE_DATE =(  \r\n" + 
				"					                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_PREMIUMS t2  \r\n" + 
				"					                WHERE t2.Country   = TAppr.Country  \r\n" + 
				"									AND t2.LE_Book   = TAppr.LE_Book  \r\n" + 
				"					                And t2.FTP_CURVE_ID   = TAppr.FTP_CURVE_ID  \r\n" + 
				"					                And T2.LP_TENOR_APPLICATION_CODE   = TAppr.LP_TENOR_APPLICATION_CODE     \r\n" + 
				"					                And T2.LP_TENOR_CODE = TAppr.LP_TENOR_CODE     \r\n" + 
				"									And NVL(T2.Vision_SBU_Attribute, 'NA')   = NVL(T2.Vision_SBU_Attribute, 'NA')									  \r\n" + 
				"									And NVL(T2.Product_Attribute, 'NA')      = NVL(TAppr.Product_Attribute, 'NA')  \r\n" + 
				"									And T2.Currency               = TAppr.Currency  \r\n" + 
				"					                AND t2.EFFECTIVE_DATE <= NVL(TO_DATE (?, 'DD-MM-RRRR') , trunc(sysdate))  \r\n" + 
				"									And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) )";
		
		String strBufPendAnd = " And TPend.EFFECTIVE_DATE =(  \r\n" + 
				"					                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_PREMIUMS_PEND t2  \r\n" + 
				"					                WHERE t2.Country   = TPend.Country  \r\n" + 
				"									AND t2.LE_Book   = TPend.LE_Book  \r\n" + 
				"					                And t2.FTP_CURVE_ID   = TPend.FTP_CURVE_ID  \r\n" + 
				"					                And T2.LP_TENOR_APPLICATION_CODE   = TPend.LP_TENOR_APPLICATION_CODE     \r\n" + 
				"					                And T2.LP_TENOR_CODE = TPend.LP_TENOR_CODE     \r\n" + 
				"									And NVL(T2.Vision_SBU_Attribute, 'NA')   = NVL(T2.Vision_SBU_Attribute, 'NA')									  \r\n" + 
				"									And NVL(T2.Product_Attribute, 'NA')      = NVL(TPend.Product_Attribute, 'NA')  \r\n" + 
				"									And T2.Currency               = TPend.Currency  \r\n" + 
				"					                AND t2.EFFECTIVE_DATE <= NVL(TO_DATE (?, 'DD-MM-RRRR') , trunc(sysdate))  \r\n" + 
				"									And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) )";
		
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_PREMIUMS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_CURVE_ID = TPend.FTP_CURVE_ID \r\n" + 
				" AND TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE AND TAppr.LP_TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE \r\n" + 
				" AND TAppr.LP_TENOR_CODE = TPend.LP_TENOR_CODE \r\n" + 
				" AND "+nullFun+"(TAppr.VISION_SECTOR, 'NA') = "+nullFun+"(TPend.VISION_SECTOR, 'NA') \r\n" + 
				" AND "+nullFun+"(TAppr.VISION_SBU_ATTRIBUTE, 'NA') = "+nullFun+"(TPend.VISION_SBU_ATTRIBUTE, 'NA') \r\n" + 
				" AND "+nullFun+"(TAppr.PRODUCT_ATTRIBUTE, 'NA') = "+nullFun+"(TPend.PRODUCT_ATTRIBUTE, 'NA') \r\n" + 
				" AND "+nullFun+"(TAppr.CUSTOMER_ID, 'NA') = "+nullFun+"(TPend.CUSTOMER_ID, 'NA')\r\n" + 
				" AND "+nullFun+"(TAppr.CONTRACT_ID, 'NA') = "+nullFun+"(TPend.CONTRACT_ID, 'NA') \r\n" + 
				" AND TAppr.CURRENCY = TPend.CURRENCY)");
		String orderBy = " Order By COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, LP_TENOR_APPLICATION_CODE, LP_TENOR_CODE, VISION_SECTOR, VISION_SBU_ATTRIBUTE, PRODUCT_ATTRIBUTE, CUSTOMER_ID, CONTRACT_ID, CURRENCY ";
		
		
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApproveAnd = " And TAppr.EFFECTIVE_DATE =(  \r\n" + 
					"					                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_PREMIUMS t2  \r\n" + 
					"					                WHERE t2.Country   = TAppr.Country  \r\n" + 
					"									AND t2.LE_Book   = TAppr.LE_Book  \r\n" + 
					"					                And t2.FTP_CURVE_ID   = TAppr.FTP_CURVE_ID  \r\n" + 
					"					                And T2.LP_TENOR_APPLICATION_CODE   = TAppr.LP_TENOR_APPLICATION_CODE     \r\n" + 
					"					                And T2.LP_TENOR_CODE = TAppr.LP_TENOR_CODE     \r\n" + 
					"									And "+nullFun+"(T2.Vision_SBU_Attribute, 'NA')   = "+nullFun+"(T2.Vision_SBU_Attribute, 'NA')									  \r\n" + 
					"									And "+nullFun+"(T2.Product_Attribute, 'NA')      = "+nullFun+"(TAppr.Product_Attribute, 'NA')  \r\n" + 
					"									And T2.Currency               = TAppr.Currency  \r\n" + 
//					"									And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) )";
					" AND t2.EFFECTIVE_DATE <= ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) "
					+ " AND ISNULL(t2.End_Date, CONVERT(DATE, '31-12-2099', 105)) > ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) )";			
			
			
			strBufPendAnd = " And TPend.EFFECTIVE_DATE =(  \r\n" + 
					"					                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_PREMIUMS_PEND t2  \r\n" + 
					"					                WHERE t2.Country   = TPend.Country  \r\n" + 
					"									AND t2.LE_Book   = TPend.LE_Book  \r\n" + 
					"					                And t2.FTP_CURVE_ID   = TPend.FTP_CURVE_ID  \r\n" + 
					"					                And T2.LP_TENOR_APPLICATION_CODE   = TPend.LP_TENOR_APPLICATION_CODE     \r\n" + 
					"					                And T2.LP_TENOR_CODE = TPend.LP_TENOR_CODE     \r\n" + 
					"									And "+nullFun+"(T2.Vision_SBU_Attribute, 'NA')   = "+nullFun+"(T2.Vision_SBU_Attribute, 'NA')									  \r\n" + 
					"									And "+nullFun+"(T2.Product_Attribute, 'NA')      = "+nullFun+"(TPend.Product_Attribute, 'NA')  \r\n" + 
					"									And T2.Currency               = TPend.Currency  \r\n" + 
					" AND t2.EFFECTIVE_DATE <= ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) "
					+ " AND ISNULL(t2.End_Date, CONVERT(DATE, '31-12-2099', 105)) > ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) )";
		}
		strBufApprove.append(" "+strBufApproveAnd+" ");
		
		strBufPend.append(" "+strBufPendAnd+" ");
		try {
			Vector<Object> params = new Vector<Object>();
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpCurveId());
			params.addElement(dObj.getEffectiveDate());
			params.addElement(dObj.getEffectiveDate());
			if(ValidationUtil.isValid(dObj.getLpTenorApplicationCode()) && dObj.getLpTenorApplicationCode() != -1 ) {
				strBufApprove.append(" AND TAppr.LP_TENOR_APPLICATION_CODE = ? ");
				strBufPend.append(" AND TPend.LP_TENOR_APPLICATION_CODE = ? ");
				params.addElement(dObj.getLpTenorApplicationCode());
			}
			if(ValidationUtil.isValid(dObj.getLpTenorCode())) {
				strBufApprove.append(" AND TAppr.LP_TENOR_CODE = ? ");
				strBufPend.append(" AND TPend.LP_TENOR_CODE = ? ");
				params.addElement(dObj.getLpTenorCode());
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
				
			return getJdbcTemplate().query(query, objParams, getMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FtpPremiumsVb> getQueryPopupResultsPremiumsOverAllReview(FtpPremiumsVb dObj) {
		List<FtpPremiumsVb> collTemp = null;
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY"
						+ ",TAppr.LE_BOOK"
						+ ",TAppr.FTP_CURVE_ID"
						+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
						+ ",TAppr.TENOR_APPLICATION_CODE_NT"
						+ ",TAppr.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
						+ ",TAppr.LP_TENOR_CODE"
						+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TENOR_APPLICATION_CODE_NT AND  TENOR_APPLICATION_CODE = TAppr.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.LP_TENOR_CODE) LP_TENOR_CODE_DESC "
						+ ",TAppr.VISION_SECTOR_AT"
						+ ",TAppr.VISION_SECTOR, "+VisionSectorAtApprDesc
						+ ",TAppr.VISION_SBU_ATTRIBUTE_AT"
						+ ",TAppr.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtApprDesc
						+ ",TAppr.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TAppr.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
						+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
						+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"			
						+ ",TAppr.CURRENCY"
						+ ",TRIM("+dateFormat+" (TAppr.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
						+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
						+ ",TAppr.FTP_PREMIUM_STATUS_NT"
						+ ",TAppr.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtApprDesc
						+ ",TAppr.RECORD_INDICATOR_NT"
						+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
						+ ",TAppr.MAKER, "+makerApprDesc
						+ ",TAppr.VERIFIER, "+verifierApprDesc
						+ ",TAppr.INTERNAL_STATUS"
						+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED "
						+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
				" FROM FTP_PREMIUMS TAppr WHERE  \r\n" + 
				"					   TAppr.Country = ? And TAppr.LE_Book = ? And TAppr.FTP_CURVE_ID = ? ");
		StringBuffer strBufPend = new StringBuffer("Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_CURVE_ID"
				+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
				+ ",TPend.TENOR_APPLICATION_CODE_NT"
				+ ",TPend.LP_TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
				+ ",TPend.LP_TENOR_CODE"
				+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE_NT = TENOR_APPLICATION_CODE_NT AND  TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.LP_TENOR_CODE) LP_TENOR_CODE_DESC "
				+ ",TPend.VISION_SECTOR_AT"
				+ ",TPend.VISION_SECTOR, "+VisionSectorAtPendDesc
				+ ",TPend.VISION_SBU_ATTRIBUTE_AT"
				+ ",TPend.VISION_SBU_ATTRIBUTE, "+VisionSbuAttributeAtPendDesc
				+ ",TPend.PRODUCT_ATTRIBUTE, (SELECT PRODUCT_ATT_DESCRIPTION FROM PRODUCT_NARROW WHERE TPend.PRODUCT_ATTRIBUTE = PRODUCT_ATTRIBUTE) PRODUCT_ATTRIBUTE_DESC "
				+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
				+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"			
				+ ",TPend.CURRENCY"
				+ ",TRIM("+dateFormat+" (TPend.PREMIUM_RATE, "+numberFormat+"))PREMIUM_RATE"
				+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
				+ ",TPend.FTP_PREMIUM_STATUS_NT"
				+ ",TPend.FTP_PREMIUM_STATUS, "+FtpPremiumStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED "
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
		" FROM FTP_PREMIUMS_PEND TPend WHERE  \r\n" + 
		"					   TPend.Country = ? And TPend.LE_Book = ? And TPend.FTP_CURVE_ID = ? ");
		Object objParams[]=null;
		
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_PREMIUMS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_CURVE_ID = TPend.FTP_CURVE_ID \r\n" + 
				" AND TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE AND TAppr.LP_TENOR_APPLICATION_CODE = TPend.LP_TENOR_APPLICATION_CODE \r\n" + 
				" AND TAppr.LP_TENOR_CODE = TPend.LP_TENOR_CODE \r\n" + 
				" AND "+nullFun+"(TAppr.VISION_SECTOR, 'NA') = "+nullFun+"(TPend.VISION_SECTOR, 'NA') \r\n" + 
				" AND "+nullFun+"(TAppr.VISION_SBU_ATTRIBUTE, 'NA') = "+nullFun+"(TPend.VISION_SBU_ATTRIBUTE, 'NA') \r\n" + 
				" AND "+nullFun+"(TAppr.PRODUCT_ATTRIBUTE, 'NA') = "+nullFun+"(TPend.PRODUCT_ATTRIBUTE, 'NA') \r\n" + 
				" AND "+nullFun+"(TAppr.CUSTOMER_ID, 'NA') = "+nullFun+"(TPend.CUSTOMER_ID, 'NA')\r\n" + 
				" AND "+nullFun+"(TAppr.CONTRACT_ID, 'NA') = "+nullFun+"(TPend.CONTRACT_ID, 'NA') \r\n" + 
				" AND TAppr.CURRENCY = TPend.CURRENCY)");
		
		
		String orderBy = " Order By EFFECTIVE_DATE";
		try {
			Vector<Object> params = new Vector<Object>();
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpCurveId());
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
			return getJdbcTemplate().query(query, objParams, getMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doDeleteApprRecord(List<FtpPremiumsVb> vObjects, FtpPremiumsVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FtpPremiumsVb vObject : vObjects) {
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
	public ExceptionCode doDeleteRecord(List<FtpPremiumsVb> vObjects, FtpPremiumsVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FtpPremiumsVb vObject : vObjects) {
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
