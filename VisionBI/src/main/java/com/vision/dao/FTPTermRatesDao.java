package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPTermRatesVb;

@Component
public class FTPTermRatesDao extends AbstractDao<FTPTermRatesVb> {
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPTermRatesVb ftpTermRatesVb = new FTPTermRatesVb();
				ftpTermRatesVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
				ftpTermRatesVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpTermRatesVb.setFtpCurve(rs.getString("FTP_CURVE"));
				ftpTermRatesVb.setAddOnDepositRate(rs.getString("ADDON_DEPOSIT_RATE"));
				ftpTermRatesVb.setSubsidy(rs.getString("SUBSIDY"));
				ftpTermRatesVb.setAddOnLendingRate(rs.getString("ADDON_LENDING_RATE"));
				ftpTermRatesVb.setRequiredReserveRate(rs.getString("REQUIRED_RESERVE_RATE"));
				ftpTermRatesVb.setGlAllocationRate(rs.getString("GL_ALLOCATION_RATE"));
				ftpTermRatesVb.setInsuranceRate(rs.getString("INSURANCE_RATE"));
				ftpTermRatesVb.setAddOnAttRate1(rs.getString("ADDON_ATT_RATE1"));
				ftpTermRatesVb.setAddOnAttRate2(rs.getString("ADDON_ATT_RATE2"));
				ftpTermRatesVb.setAddOnAttRate3(rs.getString("ADDON_ATT_RATE3"));
				ftpTermRatesVb.setAddOnAttRate4(rs.getString("ADDON_ATT_RATE4"));
				ftpTermRatesVb.setTermRateStatusNt(rs.getInt("FTP_RATE_STATUS_NT"));
				ftpTermRatesVb.setTermRateStatus(rs.getInt("FTP_RATE_STATUS"));
				ftpTermRatesVb.setDbStatus(rs.getInt("FTP_RATE_STATUS"));
				ftpTermRatesVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpTermRatesVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpTermRatesVb.setMaker(rs.getInt("MAKER"));
				ftpTermRatesVb.setVerifier(rs.getInt("VERIFIER"));
				ftpTermRatesVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpTermRatesVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpTermRatesVb.setDateCreation(rs.getString("DATE_CREATION"));
				return ftpTermRatesVb;
			}
		};
		return mapper;
	}

	public List<FTPTermRatesVb> getQueryResults(FTPTermRatesVb dObj, int intStatus){
		setServiceDefaults();
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.FTP_RATE_ID,TO_CHAR(TAppr.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE, " + 
			"TO_CHAR(TAppr.FTP_CURVE,'9,990.99999990') FTP_CURVE,TO_CHAR(TAppr.ADDON_DEPOSIT_RATE,'9,990.99999990') ADDON_DEPOSIT_RATE ,TO_CHAR(TAppr.SUBSIDY,'9,990.99999990') SUBSIDY,TO_CHAR(TAppr.ADDON_LENDING_RATE,'9,990.99999990')ADDON_LENDING_RATE,TO_CHAR(TAppr.REQUIRED_RESERVE_RATE,'9,990.99999990') REQUIRED_RESERVE_RATE,TO_CHAR(TAppr.GL_ALLOCATION_RATE,'9,990.99999990') GL_ALLOCATION_RATE,TO_CHAR(TAppr.INSURANCE_RATE,'9,990.99999990') INSURANCE_RATE,TO_CHAR(TAppr.ADDON_ATT_RATE1,'9,990.99999990') ADDON_ATT_RATE1,TO_CHAR(TAppr.ADDON_ATT_RATE2,'9,990.99999990') ADDON_ATT_RATE2,TO_CHAR(TAppr.ADDON_ATT_RATE3,'9,990.99999990') ADDON_ATT_RATE3,TO_CHAR(TAppr.ADDON_ATT_RATE4,'9,990.99999990') ADDON_ATT_RATE4,TAppr.FTP_RATE_STATUS_NT,TAppr.FTP_RATE_STATUS,TAppr.RECORD_INDICATOR_NT, " + 
			"TAppr.RECORD_INDICATOR,TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS,To_Char(TAppr.DATE_LAST_MODIFIED, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED,To_Char(TAppr.DATE_CREATION, 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION From FTP_TERM_RATES TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From FTP_TERM_RATES_PEND TPend Where " + 
			"TAppr.FTP_RATE_ID = TPend.FTP_RATE_ID AND TO_DATE(TAPPR.EFFECTIVE_DATE,'DD-MM-RRRR') = TO_DATE(TPEND.EFFECTIVE_DATE,'DD-MM-RRRR')) ");

		StringBuffer strBufPending = new StringBuffer("Select TPend.FTP_RATE_ID,TO_CHAR(TPend.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE, " + 
			"TO_CHAR(TPend.FTP_CURVE,'9,990.99999990') FTP_CURVE,TO_CHAR(TPend.ADDON_DEPOSIT_RATE,'9,990.99999990') ADDON_DEPOSIT_RATE ,TO_CHAR(TPend.SUBSIDY,'9,990.99999990') SUBSIDY,TO_CHAR(TPend.ADDON_LENDING_RATE,'9,990.99999990')ADDON_LENDING_RATE,TO_CHAR(TPend.REQUIRED_RESERVE_RATE,'9,990.99999990') REQUIRED_RESERVE_RATE,TO_CHAR(TPend.GL_ALLOCATION_RATE,'9,990.99999990') GL_ALLOCATION_RATE,TO_CHAR(TPend.INSURANCE_RATE,'9,990.99999990') INSURANCE_RATE,TO_CHAR(TPend.ADDON_ATT_RATE1,'9,990.99999990') ADDON_ATT_RATE1,TO_CHAR(TPend.ADDON_ATT_RATE2,'9,990.99999990') ADDON_ATT_RATE2,TO_CHAR(TPend.ADDON_ATT_RATE3,'9,990.99999990') ADDON_ATT_RATE3,TO_CHAR(TPend.ADDON_ATT_RATE4,'9,990.99999990') ADDON_ATT_RATE4,TPend.FTP_RATE_STATUS_NT,TPend.FTP_RATE_STATUS,TPend.RECORD_INDICATOR_NT, " + 
			"TPend.RECORD_INDICATOR,TPend.MAKER,TPend.VERIFIER,TPend.INTERNAL_STATUS,To_Char(TPend.DATE_LAST_MODIFIED, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED,To_Char(TPend.DATE_CREATION, 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION From FTP_TERM_RATES_PEND TPend");

		try
		{
			if (ValidationUtil.isValid(dObj.getFtpRateId()))
			{
				params.addElement("%" + dObj.getFtpRateId().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.FTP_RATE_ID) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.FTP_RATE_ID) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getEffectiveDate()))
			{
				params.addElement(dObj.getEffectiveDate());
				CommonUtils.addToQuery("TAppr.EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') ", strBufApprove);
				CommonUtils.addToQuery("TPend.EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') ", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getFtpCurve()))
			{
				params.addElement(dObj.getFtpCurve());
				CommonUtils.addToQuery("TAppr.FTP_CURVE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_CURVE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAddOnDepositRate()))
			{
				params.addElement(dObj.getAddOnDepositRate());
				CommonUtils.addToQuery("TAppr.ADDON_DEPOSIT_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ADDON_DEPOSIT_RATE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getSubsidy()))
			{
				params.addElement(dObj.getSubsidy());
				CommonUtils.addToQuery("TAppr.SUBSIDY = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.SUBSIDY = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAddOnLendingRate()))
			{
				params.addElement(dObj.getAddOnLendingRate());
				CommonUtils.addToQuery("TAppr.ADDON_LENDING_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ADDON_LENDING_RATE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getRequiredReserveRate()))
			{
				params.addElement(dObj.getRequiredReserveRate());
				CommonUtils.addToQuery("TAppr.REQUIRED_RESERVE_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.REQUIRED_RESERVE_RATE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getGlAllocationRate()))
			{
				params.addElement(dObj.getGlAllocationRate());
				CommonUtils.addToQuery("TAppr.GL_ALLOCATION_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.GL_ALLOCATION_RATE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getInsuranceRate()))
			{
				params.addElement(dObj.getInsuranceRate());
				CommonUtils.addToQuery("TAppr.INSURANCE_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.INSURANCE_RATE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAddOnAttRate1()))
			{
				params.addElement(dObj.getAddOnAttRate1());
				CommonUtils.addToQuery("TAppr.ADDON_ATT_RATE1 = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ADDON_ATT_RATE1 = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAddOnAttRate2()))
			{
				params.addElement(dObj.getAddOnAttRate2());
				CommonUtils.addToQuery("TAppr.ADDON_ATT_RATE2 = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ADDON_ATT_RATE2 = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAddOnAttRate3()))
			{
				params.addElement(dObj.getAddOnAttRate3());
				CommonUtils.addToQuery("TAppr.ADDON_ATT_RATE3 = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ADDON_ATT_RATE3 = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAddOnAttRate4()))
			{
				params.addElement(dObj.getAddOnAttRate4());
				CommonUtils.addToQuery("TAppr.ADDON_ATT_RATE4 = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ADDON_ATT_RATE4 = ?", strBufPending);
			}
			if (dObj.getTermRateStatus() != -1)
			{
				params.addElement(dObj.getTermRateStatus());
				CommonUtils.addToQuery("TAppr.FTP_RATE_STATUS like ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_RATE_STATUS like ?", strBufPending);
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

			String orderBy = "";
			return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params);
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
	}

	public List<FTPTermRatesVb> getQueryResultsForReview(FTPTermRatesVb dObj, int intStatus){

		List<FTPTermRatesVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		StringBuffer strQueryAppr = new StringBuffer("Select TAppr.FTP_RATE_ID,TO_CHAR(TAppr.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE, " + 
			"TO_CHAR(TAppr.FTP_CURVE,'9,990.99999990') FTP_CURVE,TO_CHAR(TAppr.ADDON_DEPOSIT_RATE,'9,990.99999990') ADDON_DEPOSIT_RATE ,TO_CHAR(TAppr.SUBSIDY,'9,990.99999990') SUBSIDY,TO_CHAR(TAppr.ADDON_LENDING_RATE,'9,990.99999990')ADDON_LENDING_RATE,TO_CHAR(TAppr.REQUIRED_RESERVE_RATE,'9,990.99999990') REQUIRED_RESERVE_RATE,TO_CHAR(TAppr.GL_ALLOCATION_RATE,'9,990.99999990') GL_ALLOCATION_RATE,TO_CHAR(TAppr.INSURANCE_RATE,'9,990.99999990') INSURANCE_RATE,TO_CHAR(TAppr.ADDON_ATT_RATE1,'9,990.99999990') ADDON_ATT_RATE1,TO_CHAR(TAppr.ADDON_ATT_RATE2,'9,990.99999990') ADDON_ATT_RATE2,TO_CHAR(TAppr.ADDON_ATT_RATE3,'9,990.99999990') ADDON_ATT_RATE3,TO_CHAR(TAppr.ADDON_ATT_RATE4,'9,990.99999990') ADDON_ATT_RATE4,TAppr.FTP_RATE_STATUS_NT,TAppr.FTP_RATE_STATUS,TAppr.RECORD_INDICATOR_NT, " + 
			"TAppr.RECORD_INDICATOR,TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS,To_Char(TAppr.DATE_LAST_MODIFIED, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED,To_Char(TAppr.DATE_CREATION, 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION From FTP_TERM_RATES TAppr " +
			" Where TAppr.FTP_RATE_ID = ? AND TAPPR.EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')");

		StringBuffer strQueryPend = new StringBuffer("Select TPend.FTP_RATE_ID,TO_CHAR(TPend.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE, " + 
			"TO_CHAR(TPend.FTP_CURVE,'9,990.99999990') FTP_CURVE,TO_CHAR(TPend.ADDON_DEPOSIT_RATE,'9,990.99999990') ADDON_DEPOSIT_RATE ,TO_CHAR(TPend.SUBSIDY,'9,990.99999990') SUBSIDY,TO_CHAR(TPend.ADDON_LENDING_RATE,'9,990.99999990')ADDON_LENDING_RATE,TO_CHAR(TPend.REQUIRED_RESERVE_RATE,'9,990.99999990') REQUIRED_RESERVE_RATE,TO_CHAR(TPend.GL_ALLOCATION_RATE,'9,990.99999990') GL_ALLOCATION_RATE,TO_CHAR(TPend.INSURANCE_RATE,'9,990.99999990') INSURANCE_RATE,TO_CHAR(TPend.ADDON_ATT_RATE1,'9,990.99999990') ADDON_ATT_RATE1,TO_CHAR(TPend.ADDON_ATT_RATE2,'9,990.99999990') ADDON_ATT_RATE2,TO_CHAR(TPend.ADDON_ATT_RATE3,'9,990.99999990') ADDON_ATT_RATE3,TO_CHAR(TPend.ADDON_ATT_RATE4,'9,990.99999990') ADDON_ATT_RATE4,TPend.FTP_RATE_STATUS_NT,TPend.FTP_RATE_STATUS,TPend.RECORD_INDICATOR_NT, " + 
			"TPend.RECORD_INDICATOR,TPend.MAKER,TPend.VERIFIER,TPend.INTERNAL_STATUS,To_Char(TPend.DATE_LAST_MODIFIED, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED,To_Char(TPend.DATE_CREATION, 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION From FTP_TERM_RATES_PEND TPend "+
			"Where TPend.FTP_RATE_ID = ? AND TPend.EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')  ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getFtpRateId());	//[FTP_RATE_ID]
		objParams[1] = new String(dObj.getEffectiveDate());	//[FTP_RATE_ID]
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
	protected List<FTPTermRatesVb> selectApprovedRecord(FTPTermRatesVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<FTPTermRatesVb> doSelectPendingRecord(FTPTermRatesVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}

	@Override
	protected void setServiceDefaults(){
		serviceName = "FTPTermRates";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpTermRates");//"Bank Grades";
		tableName = "FTP_TERM_RATES";
		childTableName = "FTP_TERM_RATES";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}

	@Override
	protected int getStatus(FTPTermRatesVb records){return records.getTermRateStatus();}

	@Override
	protected void setStatus(FTPTermRatesVb vObject,int status){vObject.setTermRateStatus(status);}

	@Override
	protected int doInsertionAppr(FTPTermRatesVb vObject){
		String query = "Insert Into FTP_TERM_RATES( " + 
		"FTP_RATE_ID, EFFECTIVE_DATE, FTP_CURVE, ADDON_DEPOSIT_RATE, SUBSIDY, ADDON_LENDING_RATE, REQUIRED_RESERVE_RATE, GL_ALLOCATION_RATE, INSURANCE_RATE, ADDON_ATT_RATE1, ADDON_ATT_RATE2, ADDON_ATT_RATE3, ADDON_ATT_RATE4, FTP_RATE_STATUS_NT, FTP_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate)";

		Object args[] = {vObject.getFtpRateId(), vObject.getEffectiveDate(), vObject.getFtpCurve().replaceAll(",", ""),vObject.getAddOnDepositRate(),vObject.getSubsidy(),vObject.getAddOnLendingRate(),vObject.getRequiredReserveRate(),vObject.getGlAllocationRate(),vObject.getInsuranceRate(),vObject.getAddOnAttRate1(),vObject.getAddOnAttRate2(),vObject.getAddOnAttRate3(),vObject.getAddOnAttRate4(),
			 vObject.getTermRateStatusNt(), vObject.getTermRateStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPend(FTPTermRatesVb vObject){
		String query = "Insert Into FTP_TERM_RATES_PEND( " + 
		"FTP_RATE_ID, EFFECTIVE_DATE, FTP_CURVE, ADDON_DEPOSIT_RATE, SUBSIDY, ADDON_LENDING_RATE, REQUIRED_RESERVE_RATE, GL_ALLOCATION_RATE, INSURANCE_RATE, ADDON_ATT_RATE1, ADDON_ATT_RATE2, ADDON_ATT_RATE3, ADDON_ATT_RATE4, FTP_RATE_STATUS_NT, FTP_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate)";

		Object args[] = {vObject.getFtpRateId(), vObject.getEffectiveDate(), vObject.getFtpCurve().replaceAll(",", ""),vObject.getAddOnDepositRate(), vObject.getSubsidy(), vObject.getAddOnLendingRate(), vObject.getRequiredReserveRate(), vObject.getGlAllocationRate(), vObject.getInsuranceRate(), vObject.getAddOnAttRate1(), vObject.getAddOnAttRate2(), vObject.getAddOnAttRate3(), vObject.getAddOnAttRate4(),
				 vObject.getTermRateStatusNt(), vObject.getTermRateStatus(), vObject.getRecordIndicatorNt(),
				 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPendWithDc(FTPTermRatesVb vObject){
		String query = "Insert Into FTP_TERM_RATES_PEND( " + 
		"FTP_RATE_ID, EFFECTIVE_DATE, FTP_CURVE, ADDON_DEPOSIT_RATE, SUBSIDY, ADDON_LENDING_RATE, REQUIRED_RESERVE_RATE, GL_ALLOCATION_RATE, INSURANCE_RATE, ADDON_ATT_RATE1, ADDON_ATT_RATE2, ADDON_ATT_RATE3, ADDON_ATT_RATE4, FTP_RATE_STATUS_NT, FTP_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			"Values (?, TO_DATE(?,'DD-MM-RRRR'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SysDate,  " + 
			"To_Date(?, 'DD-MM-YYYY HH24:MI:SS'))";

		Object args[] = {vObject.getFtpRateId(), vObject.getEffectiveDate(), vObject.getFtpCurve().replaceAll(",", ""),vObject.getAddOnDepositRate(),vObject.getSubsidy(),vObject.getAddOnLendingRate(),vObject.getRequiredReserveRate(),vObject.getGlAllocationRate(),vObject.getInsuranceRate(),vObject.getAddOnAttRate1(),vObject.getAddOnAttRate2(),vObject.getAddOnAttRate3(),vObject.getAddOnAttRate4(), 
			 vObject.getTermRateStatusNt(), vObject.getTermRateStatus(), vObject.getRecordIndicatorNt(), 
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), 
			 vObject.getDateCreation()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdateAppr(FTPTermRatesVb vObject){
		String query = "Update FTP_TERM_RATES Set " + 
		" FTP_CURVE = ?, ADDON_DEPOSIT_RATE = ?, SUBSIDY = ?, ADDON_LENDING_RATE = ?, REQUIRED_RESERVE_RATE = ?, GL_ALLOCATION_RATE = ?, INSURANCE_RATE = ?, ADDON_ATT_RATE1 = ?, ADDON_ATT_RATE2 = ?, ADDON_ATT_RATE3 = ?, ADDON_ATT_RATE4 = ?, FTP_RATE_STATUS_NT = ?, FTP_RATE_STATUS = ?, " + 
		" RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, " + 
		" DATE_LAST_MODIFIED = SysDate " + 
		" Where FTP_RATE_ID = ? and EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR') ";

		Object args[] = {vObject.getFtpCurve().replaceAll(",", "") ,vObject.getAddOnDepositRate().replaceAll(",", ""),vObject.getSubsidy().replaceAll(",", ""),vObject.getAddOnLendingRate().replaceAll(",", ""),vObject.getRequiredReserveRate().replaceAll(",", ""),vObject.getGlAllocationRate().replaceAll(",", ""),vObject.getInsuranceRate().replaceAll(",", ""),vObject.getAddOnAttRate1().replaceAll(",", ""),vObject.getAddOnAttRate2().replaceAll(",", ""),vObject.getAddOnAttRate3().replaceAll(",", ""),vObject.getAddOnAttRate4().replaceAll(",", ""), vObject.getTermRateStatusNt(),
		 vObject.getTermRateStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
		 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), 
		vObject.getFtpRateId(),vObject.getEffectiveDate()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdatePend(FTPTermRatesVb vObject){
		String query = "Update FTP_TERM_RATES_PEND Set " + 
		"FTP_CURVE = ?,ADDON_DEPOSIT_RATE = ?, SUBSIDY = ?, ADDON_LENDING_RATE = ?, REQUIRED_RESERVE_RATE = ?, GL_ALLOCATION_RATE = ?, INSURANCE_RATE = ?, ADDON_ATT_RATE1 = ?, ADDON_ATT_RATE2 = ?, ADDON_ATT_RATE3 = ?, ADDON_ATT_RATE4 = ?, FTP_RATE_STATUS_NT = ?, FTP_RATE_STATUS = ?, " +
		"RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?," +
		"DATE_LAST_MODIFIED = SysDate " + 
		"Where FTP_RATE_ID = ? and EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR') ";

		Object args[] = {vObject.getFtpCurve().replaceAll(",", ""),vObject.getAddOnDepositRate().replaceAll(",", ""),vObject.getSubsidy().replaceAll(",", ""),vObject.getAddOnLendingRate().replaceAll(",", ""),vObject.getRequiredReserveRate().replaceAll(",", ""),vObject.getGlAllocationRate().replaceAll(",", ""),vObject.getInsuranceRate().replaceAll(",", ""),vObject.getAddOnAttRate1().replaceAll(",", ""),vObject.getAddOnAttRate2().replaceAll(",", ""),vObject.getAddOnAttRate3().replaceAll(",", ""),vObject.getAddOnAttRate4().replaceAll(",", ""), vObject.getTermRateStatusNt(),  
		 vObject.getTermRateStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),  
		 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), 
		vObject.getFtpRateId(),vObject.getEffectiveDate()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doDeleteAppr(FTPTermRatesVb vObject){
		String query = "Delete From FTP_TERM_RATES Where " + 
		"FTP_RATE_ID = ? and EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR') " ;

		Object args[] = {vObject.getFtpRateId(),vObject.getEffectiveDate()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int deletePendingRecord(FTPTermRatesVb vObject){
		String query = "Delete From FTP_TERM_RATES_PEND Where " + 
		"FTP_RATE_ID = ? and EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR') " ;

		Object args[] = {vObject.getFtpRateId(),vObject.getEffectiveDate()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected String frameErrorMessage(FTPTermRatesVb vObject, String strOperation)
	{
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try {
			strErrMsg =  strErrMsg + " FTP_RATE_ID:" + vObject.getFtpRateId();
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
	protected String getAuditString(FTPTermRatesVb vObject)
	{
		StringBuffer strAudit = new StringBuffer("");
		try
		{
			if(vObject.getFtpRateId() != null)
				strAudit.append(vObject.getFtpRateId().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if(vObject.getEffectiveDate() != null)
				strAudit.append(vObject.getEffectiveDate().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			strAudit.append(vObject.getFtpCurve());
			strAudit.append("!|#");
			strAudit.append(vObject.getAddOnDepositRate());
			strAudit.append("!|#");
			strAudit.append(vObject.getSubsidy());
			strAudit.append("!|#");
			strAudit.append(vObject.getAddOnLendingRate());
			strAudit.append("!|#");
			strAudit.append(vObject.getRequiredReserveRate());
			strAudit.append("!|#");
			strAudit.append(vObject.getGlAllocationRate());
			strAudit.append("!|#");
			strAudit.append(vObject.getInsuranceRate());
			strAudit.append("!|#");
			strAudit.append(vObject.getAddOnAttRate1());
			strAudit.append("!|#");
			strAudit.append(vObject.getAddOnAttRate2());
			strAudit.append("!|#");
			strAudit.append(vObject.getAddOnAttRate3());
			strAudit.append("!|#");
			strAudit.append(vObject.getAddOnAttRate4());
			strAudit.append("!|#");
			strAudit.append(vObject.getTermRateStatusNt());
			strAudit.append("!|#");
			strAudit.append(vObject.getTermRateStatus());
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

}
