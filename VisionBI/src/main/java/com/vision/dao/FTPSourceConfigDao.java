package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
@Component
public class FTPSourceConfigDao extends AbstractDao<FTPSourceConfigVb> {
	public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPSourceConfigVb ftpSourceConfigVb = new FTPSourceConfigVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPSourceConfigVb ftpSourceConfigVb = new FTPSourceConfigVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setSourceReferenceAt(rs.getInt("SOURCE_REFERENCE_AT"));
				ftpSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				ftpSourceConfigVb.setSequence(rs.getInt("SOURCE_SEQUENCE"));
				ftpSourceConfigVb.setTableName(rs.getString("TABLE_NAME"));
				ftpSourceConfigVb.setColName(rs.getString("COLUMN_NAME"));
				ftpSourceConfigVb.setConditionValue1(rs.getString("CONDITION_VALUE1"));
				ftpSourceConfigVb.setConditionValue2(rs.getString("CONDITION_VALUE2"));
				ftpSourceConfigVb.setOperand(rs.getString("OPERAND"));
				ftpSourceConfigVb.setFtpSourceConfigStatusNt(rs.getInt("FTP_SC_STATUS_NT"));
				ftpSourceConfigVb.setFtpSourceConfigStatus(rs.getInt("FTP_SC_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_SC_STATUS"));
				ftpSourceConfigVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				ftpSourceConfigVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpSourceConfigVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpSourceConfigVb.setDateCreation(rs.getString("DATE_CREATION"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}

	public List<FTPSourceConfigVb> getQueryPopupResults(FTPSourceConfigVb dObj){
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, " + 
			" TAppr.SOURCE_REFERENCE" + 
			" From FTP_SOURCE_CONFIG TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From FTP_SOURCE_CONFIG_PEND TPend Where " + 
			"TAppr.COUNTRY = TPend.COUNTRY " + 
			"And TAppr.LE_BOOK = TPend.LE_BOOK " + 
			"And TAppr.SOURCE_REFERENCE = TPend.SOURCE_REFERENCE " + 
			"And TAppr.SOURCE_SEQUENCE = TPend.SOURCE_SEQUENCE)");

		StringBuffer strBufPending = new StringBuffer("Select Distinct TPend.COUNTRY, TPend.LE_BOOK, " + 
			" TPend.SOURCE_REFERENCE" + 
			" From FTP_SOURCE_CONFIG_PEND TPend ");


		try
		{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COUNTRY) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.LE_BOOK) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getSequence()) &&  dObj.getSequence() != 0)
			{
				params.addElement(dObj.getSequence());
				CommonUtils.addToQuery("UPPER(TAppr.SOURCE_SEQUENCE) = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.SOURCE_SEQUENCE) = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getSourceReference()) && !"-1".equalsIgnoreCase(dObj.getSourceReference()))
			{
				params.addElement(dObj.getSourceReference());
				CommonUtils.addToQuery("TAppr.SOURCE_REFERENCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.SOURCE_REFERENCE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getTableName()))
			{
				params.addElement("%" + dObj.getTableName().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.TABLE_NAME) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.TABLE_NAME) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getColName()))
			{
				params.addElement("%" + dObj.getColName().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COLUMN_NAME) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COLUMN_NAME) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getColName()))
			{
				params.addElement("%" + dObj.getColName().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COLUMN_NAME) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COLUMN_NAME) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getOperand()) && !"-1".equalsIgnoreCase(dObj.getOperand()))
			{
				params.addElement(dObj.getOperand());
				CommonUtils.addToQuery("UPPER(TAppr.OPERAND) = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.OPERAND) = ?", strBufPending);
			}
			if (dObj.getFtpSourceConfigStatus() != -1)
			{
				params.addElement(dObj.getFtpSourceConfigStatus());
				CommonUtils.addToQuery("TAppr.FTP_SC_STATUS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_SC_STATUS = ?", strBufPending);
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

		String orderBy = " Order By COUNTRY, LE_BOOK, SOURCE_REFERENCE";
		return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params, getQueryPopupMapper());
	}

	public List<FTPSourceConfigVb> getQueryResults(FTPSourceConfigVb dObj, int intStatus){
		Vector<Object> params = new Vector<Object>();
		setServiceDefaults();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY,TAppr.LE_BOOK, " + 
			" TAppr.SOURCE_REFERENCE_AT,TAppr.SOURCE_REFERENCE,TAppr.SOURCE_SEQUENCE,TAppr.TABLE_NAME,TAppr.COLUMN_NAME, " + 
			" TAppr.OPERAND,TAppr.CONDITION_VALUE1,TAppr.CONDITION_VALUE2,TAppr.FTP_SC_STATUS_NT,TAppr.FTP_SC_STATUS, " + 
			" TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR,TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS, " + 
			dateFormat+" (TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED,"+dateFormat+"(TAppr.DATE_CREATION, " + 
			dateFormatStr+" ) DATE_CREATION From FTP_SOURCE_CONFIG TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From FTP_SOURCE_CONFIG_PEND TPend Where " + 
			"TAppr.COUNTRY = TPend.COUNTRY " + 
			"And TAppr.LE_BOOK = TPend.LE_BOOK " + 
			"And TAppr.SOURCE_REFERENCE = TPend.SOURCE_REFERENCE " + 
			"And TAppr.SOURCE_SEQUENCE = TPend.SOURCE_SEQUENCE) ");

		StringBuffer strBufPending = new StringBuffer("Select TPend.COUNTRY,TPend.LE_BOOK, " + 
				" TPend.SOURCE_REFERENCE_AT,TPend.SOURCE_REFERENCE,TPend.SOURCE_SEQUENCE,TPend.TABLE_NAME,TPend.COLUMN_NAME, " + 
				" TPend.OPERAND,TPend.CONDITION_VALUE1,TPend.CONDITION_VALUE2,TPend.FTP_SC_STATUS_NT,TPend.FTP_SC_STATUS, " + 
				" TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR,TPend.MAKER,TPend.VERIFIER,TPend.INTERNAL_STATUS, " + 
				dateFormat+" (TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED,"+dateFormat+"(TPend.DATE_CREATION, " + 
				dateFormatStr+" ) DATE_CREATION From FTP_SOURCE_CONFIG_PEND TPend ");

		try
		{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COUNTRY) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.LE_BOOK) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getSequence()) &&  dObj.getSequence() != 0)
			{
				params.addElement(dObj.getSequence());
				CommonUtils.addToQuery("UPPER(TAppr.SOURCE_SEQUENCE) = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.SOURCE_SEQUENCE) = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getSourceReference()) && !"-1".equalsIgnoreCase(dObj.getSourceReference()))
			{
				params.addElement(dObj.getSourceReference());
				CommonUtils.addToQuery("TAppr.SOURCE_REFERENCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.SOURCE_REFERENCE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getTableName()))
			{
				params.addElement("%" + dObj.getTableName().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.TABLE_NAME) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.TABLE_NAME) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getColName()))
			{
				params.addElement("%" + dObj.getColName().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COLUMN_NAME) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COLUMN_NAME) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getColName()))
			{
				params.addElement("%" + dObj.getColName().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COLUMN_NAME) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COLUMN_NAME) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getOperand()) && !"-1".equalsIgnoreCase(dObj.getOperand()))
			{
				params.addElement(dObj.getOperand());
				CommonUtils.addToQuery("UPPER(TAppr.OPERAND) = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.OPERAND) = ?", strBufPending);
			}
			if (dObj.getFtpSourceConfigStatus() != -1)
			{
				params.addElement(dObj.getFtpSourceConfigStatus());
				CommonUtils.addToQuery("TAppr.FTP_SC_STATUS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_SC_STATUS = ?", strBufPending);
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
			String orderBy = " Order By COUNTRY, LE_BOOK, SOURCE_REFERENCE, SOURCE_SEQUENCE ";
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

	public List<FTPSourceConfigVb> getQueryResultsForReview(FTPSourceConfigVb dObj, int intStatus){

		List<FTPSourceConfigVb> collTemp = null;
		final int intKeyFieldsCount = 4;
		StringBuffer strQueryAppr = new StringBuffer("Select TAppr.COUNTRY,TAppr.LE_BOOK, " + 
			" TAppr.SOURCE_REFERENCE_AT,TAppr.SOURCE_REFERENCE,TAppr.SOURCE_SEQUENCE,TAppr.TABLE_NAME,TAppr.COLUMN_NAME, " + 
			" TAppr.OPERAND,TAppr.CONDITION_VALUE1,TAppr.CONDITION_VALUE2,TAppr.FTP_SC_STATUS_NT,TAppr.FTP_SC_STATUS, " + 
			" TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR,TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS, " + 
			dateFormat+" (TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED,"+dateFormat+"(TAppr.DATE_CREATION, " + 
			dateFormatStr+" ) DATE_CREATION From FTP_SOURCE_CONFIG TAppr " + 
			" Where TAppr.COUNTRY = ?  And TAppr.LE_BOOK = ?  And TAppr.SOURCE_REFERENCE = ? And TAppr.SOURCE_SEQUENCE = ? ");

		StringBuffer strQueryPend = new StringBuffer("Select TPend.COUNTRY,TPend.LE_BOOK, " + 
				" TPend.SOURCE_REFERENCE_AT,TPend.SOURCE_REFERENCE,TPend.SOURCE_SEQUENCE,TPend.TABLE_NAME,TPend.COLUMN_NAME, " + 
				" TPend.OPERAND,TPend.CONDITION_VALUE1,TPend.CONDITION_VALUE2,TPend.FTP_SC_STATUS_NT,TPend.FTP_SC_STATUS, " + 
				" TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR,TPend.MAKER,TPend.VERIFIER,TPend.INTERNAL_STATUS, " + 
				dateFormat+" (TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED,"+dateFormat+"(TPend.DATE_CREATION, " + 
				dateFormatStr+" ) DATE_CREATION From FTP_SOURCE_CONFIG_PEND TPend  " + 
			" Where TPend.COUNTRY = ?  And TPend.LE_BOOK = ?  And TPend.SOURCE_REFERENCE = ?  And TPend.SOURCE_SEQUENCE = ? ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getCountry());	//[COUNTRY]
		objParams[1] = new String(dObj.getLeBook());	//[LE_BOOK]
		objParams[2] = new String(dObj.getSourceReference());	//[SOURCE_REFERENCE]
		objParams[3] = new Integer(dObj.getSequence());	//[SOURCE_SEQUENCE]
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
	protected List<FTPSourceConfigVb> selectApprovedRecord(FTPSourceConfigVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<FTPSourceConfigVb> doSelectPendingRecord(FTPSourceConfigVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}

	@Override
	protected void setServiceDefaults(){
		serviceName = "FTPSourceConfig";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpSourceConfig");
		tableName = "FTP_SOURCE_CONFIG";
		childTableName = "FTP_SOURCE_CONFIG";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}

	@Override
	protected int getStatus(FTPSourceConfigVb records){return records.getFtpSourceConfigStatus();}

	@Override
	protected void setStatus(FTPSourceConfigVb vObject,int status){vObject.setFtpSourceConfigStatus(status);}

	@Override
	protected int doInsertionAppr(FTPSourceConfigVb vObject){
		String query = "Insert Into FTP_SOURCE_CONFIG( " + 
			" COUNTRY, LE_BOOK, SOURCE_REFERENCE_AT, SOURCE_REFERENCE, SOURCE_SEQUENCE, TABLE_NAME, COLUMN_NAME, CONDITION_VALUE1, " + 
			" CONDITION_VALUE2, OPERAND, FTP_SC_STATUS_NT, FTP_SC_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReferenceAt(),vObject.getSourceReference(),
			 vObject.getSequence(), vObject.getTableName(), vObject.getColName(), vObject.getConditionValue1(),
			 vObject.getConditionValue2(), vObject.getOperand(),vObject.getFtpSourceConfigStatusNt(),
			 vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPend(FTPSourceConfigVb vObject){
		String query = "Insert Into FTP_SOURCE_CONFIG_PEND( " + 
				" COUNTRY, LE_BOOK, SOURCE_REFERENCE_AT, SOURCE_REFERENCE, SOURCE_SEQUENCE, TABLE_NAME, COLUMN_NAME, CONDITION_VALUE1, " + 
				" CONDITION_VALUE2, OPERAND, FTP_SC_STATUS_NT, FTP_SC_STATUS, RECORD_INDICATOR_NT, " + 
				" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
				" Values (?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";

			Object args[] = {vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				 vObject.getSequence(), vObject.getTableName(), vObject.getColName(), vObject.getConditionValue1(),
				 vObject.getConditionValue2(), vObject.getOperand(),vObject.getFtpSourceConfigStatusNt(),
				 vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
			return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPendWithDc(FTPSourceConfigVb vObject){
		String query = "Insert Into FTP_SOURCE_CONFIG_PEND( " + 
				" COUNTRY, LE_BOOK, SOURCE_REFERENCE_AT, SOURCE_REFERENCE, SOURCE_SEQUENCE, TABLE_NAME, COLUMN_NAME, CONDITION_VALUE1, " + 
				" CONDITION_VALUE2, OPERAND, FTP_SC_STATUS_NT, FTP_SC_STATUS, RECORD_INDICATOR_NT, " + 
				" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
				" Values (?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";

			Object args[] = {vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				 vObject.getSequence(), vObject.getTableName(), vObject.getColName(), vObject.getConditionValue1(),
				 vObject.getConditionValue2(), vObject.getOperand(),vObject.getFtpSourceConfigStatusNt(),
				 vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),vObject.getDateCreation()};
			return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdateAppr(FTPSourceConfigVb vObject){
		String query = "Update FTP_SOURCE_CONFIG Set " + 
		" TABLE_NAME = ?, COLUMN_NAME = ?, CONDITION_VALUE1 = ?,CONDITION_VALUE2 = ?, "+
		" OPERAND = ?, FTP_SC_STATUS_NT = ?, FTP_SC_STATUS = ?, " + 
		" RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, " + 
		" DATE_LAST_MODIFIED = "+systemDate+" " + 
		" Where COUNTRY = ? " +
		" And LE_BOOK = ? " +
		" And SOURCE_REFERENCE = ? " +
		" And SOURCE_SEQUENCE = ? ";

		Object args[] = {vObject.getTableName(), vObject.getColName(), vObject.getConditionValue1(),vObject.getConditionValue2(),
				vObject.getOperand(),vObject.getFtpSourceConfigStatusNt(), vObject.getFtpSourceConfigStatus(), 
				vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(), 
				vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReference(),vObject.getSequence()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdatePend(FTPSourceConfigVb vObject){
		String query = "Update FTP_SOURCE_CONFIG_PEND Set " + 
				" TABLE_NAME = ?, COLUMN_NAME = ?, CONDITION_VALUE1 = ?,CONDITION_VALUE2 = ?, "+
				" OPERAND = ?, FTP_SC_STATUS_NT = ?, FTP_SC_STATUS = ?, " + 
				" RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, " + 
				" DATE_LAST_MODIFIED = "+systemDate+" " + 
				" Where COUNTRY = ? " +
				" And LE_BOOK = ? " +
				" And SOURCE_REFERENCE = ? " +
				" And SOURCE_SEQUENCE = ? ";

				Object args[] = {vObject.getTableName(), vObject.getColName(), vObject.getConditionValue1(),vObject.getConditionValue2(),
						vObject.getOperand(),vObject.getFtpSourceConfigStatusNt(), vObject.getFtpSourceConfigStatus(), 
						vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(), 
						vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
						vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReference(),vObject.getSequence()};

				return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doDeleteAppr(FTPSourceConfigVb vObject){
		String query = "Delete From FTP_SOURCE_CONFIG Where " + 
		"COUNTRY = ? AND " + 
		"LE_BOOK = ? AND " + 
		"SOURCE_REFERENCE = ? AND " + 
		"SOURCE_SEQUENCE = ? " ;

		Object args[] = {vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReference(),vObject.getSequence()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int deletePendingRecord(FTPSourceConfigVb vObject){
		String query = "Delete From FTP_SOURCE_CONFIG_PEND Where " + 
				"COUNTRY = ? AND " + 
				"LE_BOOK = ? AND " + 
				"SOURCE_REFERENCE = ? AND " + 
				"SOURCE_SEQUENCE = ? " ;

				Object args[] = {vObject.getCountry(), vObject.getLeBook(), vObject.getSourceReference(),vObject.getSequence()};

				return getJdbcTemplate().update(query,args);
	}

	@Override
	protected String frameErrorMessage(FTPSourceConfigVb vObject, String strOperation)
	{
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try {
			strErrMsg =  strErrMsg + " COUNTRY:" + vObject.getCountry();
			strErrMsg =  strErrMsg + " LE_BOOK:" + vObject.getLeBook();
			strErrMsg =  strErrMsg + " SOURCE_REFERENCE:" + vObject.getSourceReference();
			strErrMsg =  strErrMsg + " SOURCE_SEQUENCE:" + vObject.getSequence();
			
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
	protected String getAuditString(FTPSourceConfigVb vObject)
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

			strAudit.append(vObject.getSourceReferenceAt());
			strAudit.append("!|#");
			strAudit.append(vObject.getSourceReference());
			strAudit.append("!|#");
			strAudit.append(vObject.getSequence());
			strAudit.append("!|#");

			if(ValidationUtil.isValid(vObject.getTableName()))
				strAudit.append(vObject.getTableName().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(ValidationUtil.isValid(vObject.getColName()))
				strAudit.append(vObject.getColName().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getOperand());
			strAudit.append("!|#");
			
			strAudit.append("!|#");
			if(ValidationUtil.isValid(vObject.getConditionValue1()))
				strAudit.append(vObject.getConditionValue1().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append("!|#");
			if(ValidationUtil.isValid(vObject.getConditionValue2()))
				strAudit.append(vObject.getConditionValue2().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			strAudit.append("!|#");
			strAudit.append(vObject.getFtpSourceConfigStatusNt());
			strAudit.append("!|#");
			strAudit.append(vObject.getFtpSourceConfigStatus());
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
	protected int deleteFtpTuning(FTPSourceConfigVb vObject){
		String query = "Delete From FTP_SOURCE_TUNING Where SOURCE_REFERENCE = ? "; 
		Object args[] = {vObject.getSourceReference()};
		return getJdbcTemplate().update(query,args);
	}
	public List<FTPSourceConfigVb> getQueryResultTuning(FTPSourceConfigVb dObj){
		List<FTPSourceConfigVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		String strBufApprove = new String("SELECT SOURCE_REFERENCE,DAILY_ORACLE_HINT,MONTHLY_ORACLE_HINT,HISTORY_ORACLE_HINT,"
				+ " ORACLE_HINT_STATUS_NT,ORACLE_HINT_STATUS,"
				 +" RECORD_INDICATOR_NT,RECORD_INDICATOR,TUNING_FREQUENCY from FTP_SOURCE_TUNING WHERE SOURCE_REFERENCE = ? ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getSourceReference();

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
				FTPSourceConfigVb ftpSourceConfigVb = new FTPSourceConfigVb();
				ftpSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				ftpSourceConfigVb.setDailyHint (rs.getString("DAILY_ORACLE_HINT"));
				ftpSourceConfigVb.setMonthlyHint(rs.getString("MONTHLY_ORACLE_HINT"));
				ftpSourceConfigVb.setYearlyHint(rs.getString("HISTORY_ORACLE_HINT"));
				ftpSourceConfigVb.setFtpSourceConfigStatus(rs.getInt("ORACLE_HINT_STATUS_NT"));
				ftpSourceConfigVb.setFtpSourceConfigStatus(rs.getInt("ORACLE_HINT_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("ORACLE_HINT_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR_NT"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setFrquencyTuning(rs.getInt("TUNING_FREQUENCY"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected int doInsertionFtpTuning(FTPSourceConfigVb vObject){
		String query = "Insert Into FTP_SOURCE_TUNING( " + 
				" SOURCE_REFERENCE_AT, SOURCE_REFERENCE, DAILY_ORACLE_HINT, MONTHLY_ORACLE_HINT, HISTORY_ORACLE_HINT, ORACLE_HINT_STATUS_NT, " + 
				" ORACLE_HINT_STATUS,RECORD_INDICATOR_NT,RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION,TUNING_FREQUENCY) " + 
				" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+","+systemDate+",?)";

			Object args[] = {vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				 vObject.getDailyHint(), vObject.getMonthlyHint(), vObject.getYearlyHint(), vObject.getFtpSourceConfigStatusNt(),
				 vObject.getFtpSourceConfigStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),vObject.getFrquencyTuning()};
			return getJdbcTemplate().update(query,args);
	}
	public ExceptionCode addModifyFtpTuning(ExceptionCode exceptionCode,FTPSourceConfigVb vObject){
		List<FTPSourceConfigVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		collTemp  = getQueryResultTuning(vObject);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpTuning(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
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
}