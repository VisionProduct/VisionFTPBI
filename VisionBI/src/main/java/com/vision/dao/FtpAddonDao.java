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
import com.vision.vb.FtpAddonVb;
import com.vision.vb.FtpMethodsVb;

@Component
public class FtpAddonDao extends AbstractDao<FtpAddonVb> {
	@Value("${app.databaseType}")
	 private String databaseType;
/*******Mapper Start**********/
	String CountryApprDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TAppr.COUNTRY) COUNTRY_DESC";
	String CountryPendDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TPend.COUNTRY) COUNTRY_DESC";
	String LeBookApprDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TAppr.LE_BOOK AND COUNTRY = TAppr.COUNTRY ) LE_BOOK_DESC";
	String LeBookTPendDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TPend.LE_BOOK AND COUNTRY = TPend.COUNTRY ) LE_BOOK_DESC";

	String TenorApplicationCodeNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 8, "TAppr.TENOR_APPLICATION_CODE", "TENOR_APPLICATION_CODE_DESC");
	String TenorApplicationCodeNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 8, "TPend.TENOR_APPLICATION_CODE", "TENOR_APPLICATION_CODE_DESC");

	String FtpAddonStatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_ADDON_STATUS", "FTP_ADDON_STATUS_DESC");
	String FtpAddonStatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_ADDON_STATUS", "FTP_ADDON_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpAddonVb vObject = new FtpAddonVb();
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
				if(rs.getString("TENOR_DESC")!= null){ 
					vObject.setTenorCodeDesc(rs.getString("TENOR_DESC"));
				}
				if(rs.getString("CUSTOMER_ID")!= null){ 
					vObject.setCustomerId(rs.getString("CUSTOMER_ID"));
				}else{
					vObject.setCustomerId("");
				}
				vObject.setCustomerIdDesc(rs.getString("CUSTOMER_ID_DESC"));
				if(rs.getString("CONTRACT_ID")!= null){ 
					vObject.setContractId(rs.getString("CONTRACT_ID"));
				}else{
					vObject.setContractId("");
				}
				vObject.setContractIdDesc(rs.getString("CONTRACT_ID_DESC"));
				vObject.setSubsidyRate(rs.getString("SUBSIDY_RATE"));
				if(rs.getString("END_DATE")!= null){ 
					vObject.setEndDate(rs.getString("END_DATE"));
				}else{
					vObject.setEndDate("");
				}
				vObject.setFtpAddonStatusNt(rs.getInt("FTP_ADDON_STATUS_NT"));
				vObject.setFtpAddonStatus(rs.getInt("FTP_ADDON_STATUS"));
				vObject.setStatusDesc(rs.getString("FTP_ADDON_STATUS_DESC"));
				vObject.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				vObject.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				vObject.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				vObject.setMaker(rs.getInt("MAKER"));
				vObject.setVerifier(rs.getInt("VERIFIER"));
				
				if(ValidationUtil.isValid(rs.getString("MAKER_NAME")))
					vObject.setMakerName(rs.getString("MAKER_NAME"));
				
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

	public List<FtpAddonVb> getQueryResults(FtpAddonVb dObj, int intStatus){

		setServiceDefaults();

		List<FtpAddonVb> collTemp = null;

		final int intKeyFieldsCount = 8;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_CURVE_ID"
			+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TAppr.TENOR_APPLICATION_CODE_NT"
			+ ",TAppr.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
			+ ",TAppr.TENOR_CODE"
			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.TENOR_CODE)TENOR_DESC "
			+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"
			+ ",TRIM("+dateFormat+" (TAppr.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"
			+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TAppr.FTP_ADDON_STATUS_NT"
			+ ",TAppr.FTP_ADDON_STATUS, "+FtpAddonStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_ADDON TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND CUSTOMER_ID = ?  AND CONTRACT_ID = ?  ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_CURVE_ID"
			+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TPend.TENOR_APPLICATION_CODE_NT"
			+ ",TPend.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
			+ ",TPend.TENOR_CODE"
			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.TENOR_CODE)TENOR_DESC "
			+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"
			+ ",TRIM("+dateFormat+" (TPend.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"
			+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TPend.FTP_ADDON_STATUS_NT"
			+ ",TPend.FTP_ADDON_STATUS, "+FtpAddonStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_ADDON_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND CUSTOMER_ID = ?  AND CONTRACT_ID = ?   ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpCurveId();
		objParams[3] = dObj.getEffectiveDate();
		objParams[4] = dObj.getTenorApplicationCode();
		objParams[5] = dObj.getTenorCode();
		objParams[6] = dObj.getCustomerId();
		objParams[7] = dObj.getContractId();

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
	
	public List<FtpAddonVb> getQueryResultsByParent(FtpMethodsVb dObj, int intStatus, boolean onlyActive){

		setServiceDefaults();

		List<FtpAddonVb> collTemp = null;

		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_CURVE_ID"
			+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TAppr.TENOR_APPLICATION_CODE_NT"
			+ ",TAppr.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
			+ ",TAppr.TENOR_CODE"
			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.TENOR_CODE)TENOR_DESC "
			+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"
			+ ",TRIM("+dateFormat+" (TAppr.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"
			+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TAppr.FTP_ADDON_STATUS_NT"
			+ ",TAppr.FTP_ADDON_STATUS, "+FtpAddonStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_ADDON TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_CURVE_ID"
			+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
			+ ",TPend.TENOR_APPLICATION_CODE_NT"
			+ ",TPend.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
			+ ",TPend.TENOR_CODE"
			+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.TENOR_CODE)TENOR_DESC "
			+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
			+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"
			+ ",TRIM("+dateFormat+" (TPend.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"
			+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
			+ ",TPend.FTP_ADDON_STATUS_NT"
			+ ",TPend.FTP_ADDON_STATUS, "+FtpAddonStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_ADDON_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ");
		final int intKeyFieldsCount = 3;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpCurveId();
		if(onlyActive) {
			strQueryAppr = strQueryAppr  + "AND FTP_ADDON_STATUS != 9";
			strQueryPend = strQueryPend  + "AND FTP_ADDON_STATUS != 9";
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
	protected List<FtpAddonVb> selectApprovedRecord(FtpAddonVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}


	@Override
	protected List<FtpAddonVb> doSelectPendingRecord(FtpAddonVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}


	@Override
	protected int getStatus(FtpAddonVb records){return records.getFtpAddonStatus();}


	@Override
	protected void setStatus(FtpAddonVb vObject,int status){vObject.setFtpAddonStatus(status);}

	public void removeComma(FtpAddonVb vObject) {
		vObject.setSubsidyRate(vObject.getSubsidyRate().replaceAll(",", ""));
	}
	@Override
	protected int doInsertionAppr(FtpAddonVb vObject) {
		removeComma(vObject);
		String query = "Insert Into FTP_ADDON (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, TENOR_APPLICATION_CODE, TENOR_CODE, "
				+ "CUSTOMER_ID, CONTRACT_ID, SUBSIDY_RATE, END_DATE, FTP_ADDON_STATUS_NT, FTP_ADDON_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, "
				+ "INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getTenorApplicationCode(),
				vObject.getTenorCode(), vObject.getCustomerId(), vObject.getContractId(), vObject.getSubsidyRate(),
				vObject.getEndDate(), vObject.getFtpAddonStatusNt(), vObject.getFtpAddonStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doInsertionPend(FtpAddonVb vObject){
		removeComma(vObject);
		String query = "Insert Into FTP_ADDON_PEND (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, TENOR_APPLICATION_CODE, TENOR_CODE, "
				+ "CUSTOMER_ID, CONTRACT_ID, SUBSIDY_RATE, END_DATE, FTP_ADDON_STATUS_NT, FTP_ADDON_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, "
				+ "INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getTenorApplicationCode(),
				vObject.getTenorCode(), vObject.getCustomerId(), vObject.getContractId(), vObject.getSubsidyRate(),
				vObject.getEndDate(), vObject.getFtpAddonStatusNt(), vObject.getFtpAddonStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doInsertionPendWithDc(FtpAddonVb vObject) {
		removeComma(vObject);
		String query = "Insert Into FTP_ADDON_PEND (COUNTRY, LE_BOOK, FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE_NT, TENOR_APPLICATION_CODE, TENOR_CODE, "
				+ "CUSTOMER_ID, CONTRACT_ID, SUBSIDY_RATE, END_DATE, FTP_ADDON_STATUS_NT, FTP_ADDON_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, "
				+ "INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCodeNt(), vObject.getTenorApplicationCode(),
				vObject.getTenorCode(), vObject.getCustomerId(), vObject.getContractId(), vObject.getSubsidyRate(),
				vObject.getEndDate(), vObject.getFtpAddonStatusNt(), vObject.getFtpAddonStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus(), vObject.getDateCreation() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doUpdateAppr(FtpAddonVb vObject){
		removeComma(vObject);
	String query = "Update FTP_ADDON Set TENOR_APPLICATION_CODE_NT = ?, SUBSIDY_RATE = ?, END_DATE = "+dateConvert+","
			+ " FTP_ADDON_STATUS_NT = ?, FTP_ADDON_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, "
			+ "VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+"  WHERE COUNTRY = ?  AND LE_BOOK = ?  "
			+ "AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND CUSTOMER_ID = ?  AND CONTRACT_ID = ? ";
		Object[] args = {vObject.getTenorApplicationCodeNt() , vObject.getSubsidyRate() , vObject.getEndDate() , vObject.getFtpAddonStatusNt() , 
				vObject.getFtpAddonStatus() , vObject.getRecordIndicatorNt() , vObject.getRecordIndicator() , vObject.getMaker() , vObject.getVerifier() , vObject.getInternalStatus() , 
				vObject.getCountry() , vObject.getLeBook() , vObject.getFtpCurveId() , vObject.getEffectiveDate() , vObject.getTenorApplicationCode() , 
				vObject.getTenorCode() , vObject.getCustomerId() , vObject.getContractId() };

		return getJdbcTemplate().update(query,args);
	}


	@Override
	protected int doUpdatePend(FtpAddonVb vObject){
		removeComma(vObject);
		String query = "Update FTP_ADDON_PEND Set TENOR_APPLICATION_CODE_NT = ?, SUBSIDY_RATE = ?, END_DATE = "+dateConvert+","
				+ " FTP_ADDON_STATUS_NT = ?, FTP_ADDON_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, "
				+ "VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+"  WHERE COUNTRY = ?  AND LE_BOOK = ?  "
				+ "AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert+"  AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND CUSTOMER_ID = ?  AND CONTRACT_ID = ? ";
			Object[] args = {vObject.getTenorApplicationCodeNt() , vObject.getSubsidyRate() , vObject.getEndDate() , vObject.getFtpAddonStatusNt() , 
					vObject.getFtpAddonStatus() , vObject.getRecordIndicatorNt() , vObject.getRecordIndicator() , vObject.getMaker() , vObject.getVerifier() , vObject.getInternalStatus() , 
					vObject.getCountry() , vObject.getLeBook() , vObject.getFtpCurveId() , vObject.getEffectiveDate() , vObject.getTenorApplicationCode() , 
					vObject.getTenorCode() , vObject.getCustomerId() , vObject.getContractId() };

			return getJdbcTemplate().update(query,args);
		}


	@Override
	protected int doDeleteAppr(FtpAddonVb vObject) {
		String query = "Delete From FTP_ADDON Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert
				+ " AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND CUSTOMER_ID = ?  AND CONTRACT_ID = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCode(), vObject.getTenorCode(),
				vObject.getCustomerId(), vObject.getContractId() };
		return getJdbcTemplate().update(query, args);
	}
	
	protected int doDeleteApprByParent(FtpMethodsVb vObject) {
		String query = "Delete From FTP_ADDON Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId()};
		return getJdbcTemplate().update(query, args);
	}
	protected int doDeletePendingByParent(FtpMethodsVb vObject) {
		String query = "Delete From FTP_ADDON_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ? ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId()};
		return getJdbcTemplate().update(query, args);
	
	}

	@Override
	protected int deletePendingRecord(FtpAddonVb vObject){
		String query = "Delete From FTP_ADDON_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_CURVE_ID = ?  AND EFFECTIVE_DATE = "+dateConvert
				+ " AND TENOR_APPLICATION_CODE = ?  AND TENOR_CODE = ?  AND CUSTOMER_ID = ?  AND CONTRACT_ID = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpCurveId(),
				vObject.getEffectiveDate(), vObject.getTenorApplicationCode(), vObject.getTenorCode(),
				vObject.getCustomerId(), vObject.getContractId() };
		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected String getAuditString(FtpAddonVb vObject){
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

				strAudit.append("SUBSIDY_RATE"+auditDelimiterColVal+vObject.getSubsidyRate());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getEndDate()))
				strAudit.append("END_DATE"+auditDelimiterColVal+vObject.getEndDate().trim());
			else
				strAudit.append("END_DATE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_ADDON_STATUS_NT"+auditDelimiterColVal+vObject.getFtpAddonStatusNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_ADDON_STATUS"+auditDelimiterColVal+vObject.getFtpAddonStatus());
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
		serviceName = "FtpAddon";
		serviceDesc = "FTP Addon";
		tableName = "FTP_ADDON";
		childTableName = "FTP_ADDON";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
		
	}
	public List<FtpAddonVb> getQueryPopupResultsAddOn(FtpAddonVb dObj) {
		dObj.setRecordIndicator(0);
		StringBuffer strBufApprove = new StringBuffer(" Select TAppr.COUNTRY"
							+ ",TAppr.LE_BOOK"
							+ ",TAppr.FTP_CURVE_ID"
							+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
							+ ",TAppr.TENOR_APPLICATION_CODE_NT"
							+ ",TAppr.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
							+ ",TAppr.TENOR_CODE"
							+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.TENOR_CODE)TENOR_DESC " 
							+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
							+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"
							+", TRIM("+dateFormat+" (TAppr.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"  
							+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
							+ ",TAppr.FTP_ADDON_STATUS_NT"
							+ ",TAppr.FTP_ADDON_STATUS, "+FtpAddonStatusNtApprDesc
							+ ",TAppr.RECORD_INDICATOR_NT"
							+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
							+ ",TAppr.MAKER, "+makerApprDesc
							+ ",TAppr.VERIFIER, "+verifierApprDesc
							+ ",TAppr.INTERNAL_STATUS"
							+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
							+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
					"FROM FTP_ADDON TAppr WHERE\n" + 
					"   TAppr.Country = ? And TAppr.LE_Book = ? And TAppr.FTP_CURVE_ID = ? ");
		
		StringBuffer strBufPend = new StringBuffer(" Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_CURVE_ID"
				+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
				+ ",TPend.TENOR_APPLICATION_CODE_NT"
				+ ",TPend.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
				+ ",TPend.TENOR_CODE"
				+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.TENOR_CODE)TENOR_DESC " 
				+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
				+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"
				+", TRIM("+dateFormat+" (TPend.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"  
				+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
				+ ",TPend.FTP_ADDON_STATUS_NT"
				+ ",TPend.FTP_ADDON_STATUS, "+FtpAddonStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
		"FROM FTP_ADDON_PEND TPend WHERE\n" + 
		"   TPend.Country = ? And TPend.LE_Book = ? And TPend.FTP_CURVE_ID = ? ");
		
		String strBufApproveAnd = " And TAppr.EFFECTIVE_DATE =(\n" + 
				"                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_ADDON t2\n" + 
				"                WHERE t2.Country   = TAppr.Country\n" + 
				"				AND t2.LE_Book   = TAppr.LE_Book\n" + 
				"                And t2.FTP_CURVE_ID   = TAppr.FTP_CURVE_ID\n" + 
				"                And T2.TENOR_APPLICATION_CODE   = TAppr.TENOR_APPLICATION_CODE   \n" + 
				"                And T2.TENOR_CODE = TAppr.TENOR_CODE   \n" + 
				"				And T2.CUSTOMER_ID   = TAppr.CUSTOMER_ID\n" + 
				"				And T2.CONTRACT_ID      = TAppr.CONTRACT_ID\n" + 
				"                AND t2.EFFECTIVE_DATE <= NVL(TO_DATE (?, 'DD-MM-RRRR') , trunc(sysdate))\n" + 
				"				And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) ) ";
		
		String strBufPendAnd = " And TPend.EFFECTIVE_DATE =(\n" + 
				"                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_ADDON_PEND t2\n" + 
				"                WHERE t2.Country   = TPend.Country\n" + 
				"				AND t2.LE_Book   = TPend.LE_Book\n" + 
				"                And t2.FTP_CURVE_ID   = TPend.FTP_CURVE_ID\n" + 
				"                And T2.TENOR_APPLICATION_CODE   = TPend.TENOR_APPLICATION_CODE   \n" + 
				"                And T2.TENOR_CODE = TPend.TENOR_CODE   \n" + 
				"				And T2.CUSTOMER_ID   = TPend.CUSTOMER_ID\n" + 
				"				And T2.CONTRACT_ID      = TPend.CONTRACT_ID\n" + 
				"                AND t2.EFFECTIVE_DATE <= NVL(TO_DATE (?, 'DD-MM-RRRR') , trunc(sysdate))\n" + 
				"				And NVL(T2.End_Date,TO_DATE ('31-12-2099', 'DD-MM-RRRR')) > NVL(TO_DATE (?, 'DD-MM-RRRR'),trunc(sysdate)) ) ";
		Object objParams[]=null;
		
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApproveAnd = " And TAppr.EFFECTIVE_DATE =(\n" + 
					"                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_ADDON t2\n" + 
					"                WHERE t2.Country   = TAppr.Country\n" + 
					"				AND t2.LE_Book   = TAppr.LE_Book\n" + 
					"                And t2.FTP_CURVE_ID   = TAppr.FTP_CURVE_ID\n" + 
					"                And T2.TENOR_APPLICATION_CODE   = TAppr.TENOR_APPLICATION_CODE   \n" + 
					"                And T2.TENOR_CODE = TAppr.TENOR_CODE   \n" + 
					"				And T2.CUSTOMER_ID   = TAppr.CUSTOMER_ID\n" + 
					"				And T2.CONTRACT_ID      = TAppr.CONTRACT_ID\n" + 
					" AND t2.EFFECTIVE_DATE <= ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) "
					+ " AND ISNULL(t2.End_Date, CONVERT(DATE, '31-12-2099', 105)) > ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) ";
			
			
			strBufPendAnd = " And TPend.EFFECTIVE_DATE =(\n" + 
					"                SELECT MAX(T2.EFFECTIVE_DATE) FROM FTP_ADDON_PEND t2\n" + 
					"                WHERE t2.Country   = TPend.Country\n" + 
					"				AND t2.LE_Book   = TPend.LE_Book\n" + 
					"                And t2.FTP_CURVE_ID   = TPend.FTP_CURVE_ID\n" + 
					"                And T2.TENOR_APPLICATION_CODE   = TPend.TENOR_APPLICATION_CODE   \n" + 
					"                And T2.TENOR_CODE = TPend.TENOR_CODE   \n" + 
					"				And T2.CUSTOMER_ID   = TPend.CUSTOMER_ID\n" + 
					"				And T2.CONTRACT_ID      = TPend.CONTRACT_ID\n" + 
					" AND t2.EFFECTIVE_DATE <= ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) "
					+ " AND ISNULL(t2.End_Date, CONVERT(DATE, '31-12-2099', 105)) > ISNULL(CONVERT(DATE, ?, 105), CAST(GETDATE() AS DATE)) ";
		}
		
		strBufApprove.append(" "+strBufApproveAnd+" ");
		
		strBufPend.append(" "+strBufPendAnd+" ");
		try {
			String orderBy = " Order By TENOR_APPLICATION_CODE";

			Vector<Object> params = new Vector<Object>();
			
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpCurveId());
			params.addElement(dObj.getEffectiveDate());
			params.addElement(dObj.getEffectiveDate());
				if(ValidationUtil.isValid(dObj.getTenorApplicationCode()) && dObj.getTenorApplicationCode() != -1 ) {
					strBufApprove.append(" AND TAppr.TENOR_APPLICATION_CODE = ? ");
					strBufApprove.append(" AND TPend.TENOR_APPLICATION_CODE = ? ");
					params.addElement(dObj.getTenorApplicationCode());
				}
				if(ValidationUtil.isValid(dObj.getTenorCode())) {
					strBufApprove.append(" AND TAppr.TENOR_CODE = ? ");
					strBufApprove.append(" AND TPend.TENOR_CODE = ? ");
					params.addElement(dObj.getTenorCode());
				}
				
				if(ValidationUtil.isValid(dObj.getCustomerId())) {
					strBufApprove.append(" AND TAppr.CUSTOMER_ID = ? ");
					strBufApprove.append(" AND TPend.CUSTOMER_ID = ? ");
					params.addElement(dObj.getCustomerId());
				}
				
				if(ValidationUtil.isValid(dObj.getContractId())) {
					strBufApprove.append(" AND TAppr.CONTRACT_ID = ? ");
					strBufApprove.append(" AND TPend.CONTRACT_ID = ? ");
					params.addElement(dObj.getContractId());
				}
			String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_ADDON_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_CURVE_ID = TPend.FTP_CURVE_ID AND TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE AND TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TAppr.TENOR_CODE = TPend.TENOR_CODE AND TAppr.CUSTOMER_ID = TPend.CUSTOMER_ID AND TAppr.CONTRACT_ID = TPend.CONTRACT_ID )");

//			List<FtpAddonVb> collTemp = getJdbcTemplate().query(strBufApprove.toString() + " " + orderBy, objParams, getCurveMapper());
//			dObj.setTotalRows(collTemp.size() );
//			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getMapper());
//			return getJdbcTemplate().query(strBufApprove.toString() + " " + orderBy, objParams, getCurveMapper());
				
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
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			return null;
		}
	}
	
	public List<FtpAddonVb> getQueryPopupResultsAddOnOverAllReview(FtpAddonVb dObj) {
		dObj.setRecordIndicator(0);
		StringBuffer strBufApprove = new StringBuffer(" Select TAppr.COUNTRY"
							+ ",TAppr.LE_BOOK"
							+ ",TAppr.FTP_CURVE_ID"
							+ ","+dateFormat+"(TAppr.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
							+ ",TAppr.TENOR_APPLICATION_CODE_NT"
							+ ",TAppr.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtApprDesc
							+ ",TAppr.TENOR_CODE"
							+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TAppr.TENOR_APPLICATION_CODE AND TENOR_CODE = TAppr.TENOR_CODE)TENOR_DESC " 
							+ ",TAppr.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TAppr.CUSTOMER_ID) CUSTOMER_ID_DESC "
							+ ",TAppr.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TAppr.CONTRACT_ID) CONTRACT_ID_DESC"
							+", TRIM("+dateFormat+" (TAppr.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"  
							+ ","+dateFormat+"(TAppr.END_DATE, "+dateAloneFormatStr+") END_DATE"
							+ ",TAppr.FTP_ADDON_STATUS_NT"
							+ ",TAppr.FTP_ADDON_STATUS, "+FtpAddonStatusNtApprDesc
							+ ",TAppr.RECORD_INDICATOR_NT"
							+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
							+ ",TAppr.MAKER, "+makerApprDesc
							+ ",TAppr.VERIFIER, "+verifierApprDesc
							+ ",TAppr.INTERNAL_STATUS"
							+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
							+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
					"FROM FTP_ADDON TAppr WHERE\n" + 
					"   TAppr.Country = ? And TAppr.LE_Book = ? And TAppr.FTP_CURVE_ID = ? ");
		
		StringBuffer strBufPend = new StringBuffer(" Select TPend.COUNTRY"
				+ ",TPend.LE_BOOK"
				+ ",TPend.FTP_CURVE_ID"
				+ ","+dateFormat+"(TPend.EFFECTIVE_DATE, "+dateAloneFormatStr+") EFFECTIVE_DATE"
				+ ",TPend.TENOR_APPLICATION_CODE_NT"
				+ ",TPend.TENOR_APPLICATION_CODE, "+TenorApplicationCodeNtPendDesc
				+ ",TPend.TENOR_CODE"
				+" ,(SELECT TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TENOR_CODE = TPend.TENOR_CODE)TENOR_DESC " 
				+ ",TPend.CUSTOMER_ID,(SELECT DISTINCT CUSTOMER_NAME FROM CUSTOMERS WHERE CUSTOMER_STATUS = 0 AND CUSTOMER_ID = TPend.CUSTOMER_ID) CUSTOMER_ID_DESC "
				+ ",TPend.CONTRACT_ID,(SELECT ACCOUNT_NAME FROM ACCOUNTS WHERE ACCOUNT_STATUS =0 AND ACCOUNT_NO = TPend.CONTRACT_ID) CONTRACT_ID_DESC"
				+", TRIM("+dateFormat+" (TPend.SUBSIDY_RATE, "+numberFormat+"))SUBSIDY_RATE"  
				+ ","+dateFormat+"(TPend.END_DATE, "+dateAloneFormatStr+") END_DATE"
				+ ",TPend.FTP_ADDON_STATUS_NT"
				+ ",TPend.FTP_ADDON_STATUS, "+FtpAddonStatusNtPendDesc
				+ ",TPend.RECORD_INDICATOR_NT"
				+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
				+ ",TPend.MAKER, "+makerPendDesc
				+ ",TPend.VERIFIER, "+verifierPendDesc
				+ ",TPend.INTERNAL_STATUS"
				+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
				+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION "+
		"FROM FTP_ADDON_PEND TPend WHERE\n" + 
		"   TPend.Country = ? And TPend.LE_Book = ? And TPend.FTP_CURVE_ID = ? ");
		Object objParams[]=null;
		try {
			String orderBy = " Order By EFFECTIVE_DATE";

			Vector<Object> params = new Vector<Object>();
			
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpCurveId());
			String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_ADDON_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_CURVE_ID = TPend.FTP_CURVE_ID AND TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE AND TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE AND TAppr.TENOR_CODE = TPend.TENOR_CODE AND TAppr.CUSTOMER_ID = TPend.CUSTOMER_ID AND TAppr.CONTRACT_ID = TPend.CONTRACT_ID )");

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
			logger.error(((strBufApprove == null) ? "strBufApprove is null" : strBufApprove.toString()));
			return null;
		}
	}
	@Transactional(rollbackForClassName = { "com.vision.exception.RuntimeCustomException" })
	public ExceptionCode doDeleteApprRecord(List<FtpAddonVb> vObjects, FtpAddonVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FtpAddonVb vObject : vObjects) {
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
	public ExceptionCode doDeleteRecord(List<FtpAddonVb> vObjects, FtpAddonVb vObjectParam) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		setServiceDefaults();
		strCurrentOperation = Constants.DELETE;
		try {
			for (FtpAddonVb vObject : vObjects) {
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
