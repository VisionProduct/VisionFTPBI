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
import com.vision.vb.ErrorCodesVb;
@Component
public class ErrorCodesDao extends AbstractDao<ErrorCodesVb> {
	
	
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				ErrorCodesVb errorCodesVb = new ErrorCodesVb();
				errorCodesVb.setErrorCode(rs.getString("ERROR_CODE"));
				errorCodesVb.setErrorDescription(rs.getString("ERROR_DESCRIPTION"));
				errorCodesVb.setErrorTypeNt(rs.getInt("ERROR_TYPE_NT"));
				errorCodesVb.setErrorType(rs.getInt("ERROR_TYPE"));
				errorCodesVb.setErrorStatusNt(rs.getInt("ERROR_STATUS_NT"));
				errorCodesVb.setErrorStatus(rs.getInt("ERROR_STATUS"));
				errorCodesVb.setDbStatus(rs.getInt("ERROR_STATUS"));
				errorCodesVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				errorCodesVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				errorCodesVb.setMaker(rs.getLong("MAKER"));
				errorCodesVb.setVerifier(rs.getLong("VERIFIER"));
				errorCodesVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				errorCodesVb.setDateCreation(rs.getString("DATE_CREATION"));
				errorCodesVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				if(rs.getString("MAKER_NAME")!= null){ 
					errorCodesVb.setMakerName(rs.getString("MAKER_NAME"));
				}
				if(rs.getString("VERIFIER_NAME")!= null){ 
					errorCodesVb.setVerifierName(rs.getString("VERIFIER_NAME"));
				}
				return errorCodesVb;
			}
		};
		return mapper;
	}
	
	public List<ErrorCodesVb> getQueryPopupResults(ErrorCodesVb dObj){
		
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.ERROR_CODE," +
				"TAppr.ERROR_DESCRIPTION, TAppr.ERROR_TYPE_NT," +
				"TAppr.ERROR_TYPE, TAppr.ERROR_STATUS_NT, TAppr.ERROR_STATUS," +
				"TAppr.MAKER, "+makerApprDesc
				+ ", TAppr.VERIFIER, "+verifierApprDesc
				+ ", TAppr.RECORD_INDICATOR_NT," +
				"TAppr.RECORD_INDICATOR, "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, TAppr.INTERNAL_STATUS" +
				" From ERROR_CODES TAppr  ");
		String strWhereNotExists = new String( " Not Exists (Select 'X' From ERROR_CODES_PEND TPend Where TPend.ERROR_CODE = TAppr.ERROR_CODE)");
		StringBuffer strBufPending = new StringBuffer("Select TPend.ERROR_CODE," +
				"TPend.ERROR_DESCRIPTION, TPend.ERROR_TYPE_NT," +
				"TPend.ERROR_TYPE, TPend.ERROR_STATUS_NT, TPend.ERROR_STATUS," +
				"TPend.MAKER, "+makerPendDesc
				+ ", TPend.VERIFIER, "+verifierPendDesc
				+ ", TPend.RECORD_INDICATOR_NT," +
				"TPend.RECORD_INDICATOR, "
				+dateFormat+ "(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, TPend.INTERNAL_STATUS" +
				" From ERROR_CODES_PEND TPend ");
		try
		{
			
			//check if the column [VARIABLE_STATUS] should be included in the query
			if (dObj.getErrorStatus() != -1){
				params.addElement(new Integer(dObj.getErrorStatus()));
				CommonUtils.addToQuery("TAppr.ERROR_STATUS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ERROR_STATUS = ?", strBufPending);
			}

			//check if the column [VARIABLE] should be included in the query
			if (ValidationUtil.isValid(dObj.getErrorCode())){
				params.addElement("%" + dObj.getErrorCode().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.ERROR_CODE) LIKE ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.ERROR_CODE) LIKE ?", strBufPending);
			}

			//check if the column [VALUE] should be included in the query
			if (ValidationUtil.isValid(dObj.getErrorDescription())){
				params.addElement("%" + dObj.getErrorDescription().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.ERROR_DESCRIPTION) LIKE ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.ERROR_DESCRIPTION) LIKE ?", strBufPending);
			}
			if(dObj.getErrorType() != -1){
				params.addElement(new Integer(dObj.getErrorType()));
				CommonUtils.addToQuery("TAppr.ERROR_TYPE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.ERROR_TYPE = ?", strBufPending);
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
			String orderBy=" Order By ERROR_CODE ";
			return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params);
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is Null":strBufApprove.toString()));
			logger.error("UNION");
			logger.error(((strBufPending==null)? "strBufPending is Null":strBufPending.toString()));

			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;

		}
	}
	public List<ErrorCodesVb> getQueryResults(ErrorCodesVb dObj, int intStatus){

		List<ErrorCodesVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		String strQueryAppr = new String("Select TAppr.ERROR_CODE," +
				"TAppr.ERROR_DESCRIPTION, TAppr.ERROR_TYPE_NT," +
				"TAppr.ERROR_TYPE, TAppr.ERROR_STATUS_NT, TAppr.ERROR_STATUS," +
				"TAppr.MAKER, "+makerApprDesc
				+ ", TAppr.VERIFIER, "+verifierApprDesc
				+ ", TAppr.RECORD_INDICATOR_NT," +
				"TAppr.RECORD_INDICATOR, "+
				dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, TAppr.INTERNAL_STATUS" +
				" From ERROR_CODES TAppr " + 
				"Where TAppr.ERROR_CODE = ?");
			String strQueryPend = new String("Select TPend.ERROR_CODE," +
				"TPend.ERROR_DESCRIPTION, TPend.ERROR_TYPE_NT," +
				"TPend.ERROR_TYPE, TPend.ERROR_STATUS_NT, TPend.ERROR_STATUS," +
				"TPend.MAKER, "+makerPendDesc
				+ ", TPend.VERIFIER, "+verifierPendDesc
				+ ", TPend.RECORD_INDICATOR_NT," +
				"TPend.RECORD_INDICATOR, "+
				dateFormat+ "(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION, TPend.INTERNAL_STATUS" +
				" From ERROR_CODES_PEND TPend " + 
				"Where TPend.ERROR_CODE = ?");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getErrorCode().toUpperCase());//[ERROR_CODE]

		try
		{if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0)
			{
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
	protected List<ErrorCodesVb> selectApprovedRecord(ErrorCodesVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}
	@Override
	protected List<ErrorCodesVb> doSelectPendingRecord(ErrorCodesVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}
	@Override
	protected int getStatus(ErrorCodesVb records){return records.getErrorStatus();}
	@Override
	protected void setStatus(ErrorCodesVb vObject,int status){vObject.setErrorStatus(status);}
	@Override
	protected int doInsertionAppr(ErrorCodesVb vObject){
		String query = "Insert Into ERROR_CODES (ERROR_CODE, ERROR_DESCRIPTION, ERROR_TYPE_NT, ERROR_TYPE, "+
			"ERROR_STATUS_NT, ERROR_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, "+
			"DATE_LAST_MODIFIED, DATE_CREATION) "+
			"Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = {vObject.getErrorCode(),vObject.getErrorDescription(), vObject.getErrorTypeNt(), vObject.getErrorType(), vObject.getErrorStatusNt(),vObject.getErrorStatus(), 
			vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(),vObject.getMaker(),
			vObject.getVerifier(), vObject.getInternalStatus()};  
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doInsertionPend(ErrorCodesVb vObject){
		String query = "Insert Into ERROR_CODES_PEND (ERROR_CODE, ERROR_DESCRIPTION, ERROR_TYPE_NT, ERROR_TYPE, "+
			"ERROR_STATUS_NT, ERROR_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, "+
			"DATE_LAST_MODIFIED, DATE_CREATION) "+
			"Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object[] args = {vObject.getErrorCode(),vObject.getErrorDescription(), vObject.getErrorTypeNt(), vObject.getErrorType(), vObject.getErrorStatusNt(),vObject.getErrorStatus(), 
			vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(),vObject.getMaker(),
			vObject.getVerifier(), vObject.getInternalStatus()};  
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doInsertionPendWithDc(ErrorCodesVb vObject){
		String query = "Insert Into ERROR_CODES_PEND (ERROR_CODE, ERROR_DESCRIPTION, ERROR_TYPE_NT, ERROR_TYPE, "+
			"ERROR_STATUS_NT, ERROR_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, "+
			"DATE_LAST_MODIFIED, DATE_CREATION) "+
			"Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";
		Object[] args = {vObject.getErrorCode(),vObject.getErrorDescription(), vObject.getErrorTypeNt(), vObject.getErrorType(), vObject.getErrorStatusNt(),vObject.getErrorStatus(), 
			vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(),vObject.getMaker(),
			vObject.getVerifier(), vObject.getInternalStatus(),vObject.getDateCreation()};  
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doUpdateAppr(ErrorCodesVb vObject){
		String query = "Update ERROR_CODES Set ERROR_DESCRIPTION = ?, ERROR_TYPE_NT = ?, ERROR_TYPE = ?, "+
			"ERROR_STATUS_NT = ?, ERROR_STATUS = ?, RECORD_INDICATOR_NT = ?,"+
			"RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, DATE_LAST_MODIFIED = "+systemDate+", INTERNAL_STATUS = ? Where ERROR_CODE = ?";
		Object[] args = {vObject.getErrorDescription(), vObject.getErrorTypeNt(), vObject.getErrorType(),vObject.getErrorStatusNt(),vObject.getErrorStatus(),
			vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(),vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), vObject.getErrorCode()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doUpdatePend(ErrorCodesVb vObject){
		String query = "Update ERROR_CODES_PEND Set ERROR_DESCRIPTION = ?, ERROR_TYPE_NT = ?, ERROR_TYPE = ?, "+
			"ERROR_STATUS_NT = ?, ERROR_STATUS = ?, RECORD_INDICATOR_NT = ?,"+
			"RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, DATE_LAST_MODIFIED = "+systemDate+", INTERNAL_STATUS = ? Where ERROR_CODE = ?";
		Object[] args = {vObject.getErrorDescription(), vObject.getErrorTypeNt(), vObject.getErrorType(),vObject.getErrorStatusNt(),vObject.getErrorStatus(),
				vObject.getRecordIndicatorNt(),vObject.getRecordIndicator(),vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), vObject.getErrorCode()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doDeleteAppr(ErrorCodesVb vObject){
		String query = "Delete From ERROR_CODES Where ERROR_CODE = ?";
		Object[] args = {vObject.getErrorCode()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int deletePendingRecord(ErrorCodesVb vObject){
		String query = "Delete From ERROR_CODES_PEND Where ERROR_CODE = ?";
		Object[] args = {vObject.getErrorCode()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected String frameErrorMessage(ErrorCodesVb vObject, String strOperation){
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try{
			strErrMsg =  strErrMsg + "ERROR_CODE:" + vObject.getErrorCode();
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
	protected String getAuditString(ErrorCodesVb vObject){
		final String auditDelimiter = vObject.getAuditDelimiter();
		final String auditDelimiterColVal = vObject.getAuditDelimiterColVal();
		StringBuffer strAudit = new StringBuffer("");
		if(ValidationUtil.isValid(vObject.getErrorCode()))
			strAudit.append("ERROR_CODE"+auditDelimiterColVal+vObject.getErrorCode().trim());
		else
			strAudit.append("ERROR_CODE"+auditDelimiterColVal+"NULL");
		strAudit.append(auditDelimiter);

		if(ValidationUtil.isValid(vObject.getErrorDescription()))
			strAudit.append("ERROR_DESCRIPTION"+auditDelimiterColVal+vObject.getErrorDescription().trim());
		else
			strAudit.append("ERROR_DESCRIPTION"+auditDelimiterColVal+"NULL");
		strAudit.append(auditDelimiter);

		strAudit.append("ERROR_TYPE_NT"+auditDelimiterColVal+vObject.getErrorTypeNt());
		strAudit.append(auditDelimiter);
		strAudit.append("ERROR_TYPE"+auditDelimiterColVal+vObject.getErrorType());
		strAudit.append(auditDelimiter);
		strAudit.append("ERROR_STATUS_NT"+auditDelimiterColVal+vObject.getErrorStatusNt());
		strAudit.append(auditDelimiter);
		strAudit.append("ERROR_STATUS"+auditDelimiterColVal+vObject.getErrorStatus());
		strAudit.append(auditDelimiter);
		strAudit.append("RECORD_INDICATOR_NT"+auditDelimiterColVal+vObject.getRecordIndicatorNt());
		strAudit.append(auditDelimiter);
		if(vObject.getRecordIndicator() == -1)
			vObject.setRecordIndicator(0);
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
		serviceName = "ErrorCodes";
		serviceDesc = CommonUtils.getResourceManger().getString("errorCodesMain");
		tableName = "ERROR_CODES";
		childTableName = "ERROR_CODES";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
}