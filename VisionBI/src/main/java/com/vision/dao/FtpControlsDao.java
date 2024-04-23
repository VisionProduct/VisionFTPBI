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
import com.vision.vb.FtpControlsVb;
import com.vision.vb.FtpMethodsVb;

@Component
public class FtpControlsDao extends AbstractDao<FtpControlsVb> {
	
	
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpControlsVb ftpControlsVb = new FtpControlsVb();
				ftpControlsVb = new FtpControlsVb();
				ftpControlsVb.setCountry(rs.getString("COUNTRY"));
				ftpControlsVb.setLeBook(rs.getString("LE_BOOK"));
				ftpControlsVb.setMethodReference(rs.getString("METHOD_REFERENCE"));
				ftpControlsVb.setFtpDescription(rs.getString("FTP_DESCRIPTION"));
				ftpControlsVb.setFtpReference(rs.getString("FTP_REFERENCE"));
				ftpControlsVb.setStatusNt(rs.getInt("FTP_CONTROLS_STATUS_NT"));
				ftpControlsVb.setStatus(rs.getInt("FTP_CONTROLS_STATUS"));
				ftpControlsVb.setSourceReferenceAt(rs.getInt("SOURCE_REFERENCE_AT"));
				ftpControlsVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				ftpControlsVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpControlsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpControlsVb.setMaker(rs.getLong("MAKER"));
				ftpControlsVb.setVerifier(rs.getLong("VERIFIER"));
				ftpControlsVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpControlsVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpControlsVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				return ftpControlsVb;
			}
		};
		return mapper;
	}
	
	public List<FtpControlsVb> getQueryPopupResults(FtpControlsVb dObj){
		
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.METHOD_REFERENCE," +
				"TAppr.FTP_DESCRIPTION,TAppr.FTP_REFERENCE,TAppr.FTP_CONTROLS_STATUS_NT,TAppr.FTP_CONTROLS_STATUS," +
				"TAppr.SOURCE_REFERENCE_AT, TAppr.SOURCE_REFERENCE," +
				"TAppr.RECORD_INDICATOR_NT," +
				"TAppr.RECORD_INDICATOR, TAppr.MAKER, TAppr.VERIFIER," +
				"TAppr.INTERNAL_STATUS, To_Char(TAppr.DATE_LAST_MODIFIED, 'DD-MM-RRRR HH24:MI:SS') DATE_LAST_MODIFIED," +
				"To_Char(TAppr.DATE_CREATION, 'DD-MM-RRRR HH24:MI:SS') DATE_CREATION" +
				" From FTP_CONTROLS TAppr ");
			String strWhereNotExists = new String( " Not Exists (Select * From FTP_CONTROLS_PEND TPend" +
				" Where TPend.COUNTRY = TAppr.COUNTRY and TPend.LE_BOOK = TAppr.LE_BOOK and TPend.FTP_REFERENCE = TAppr.FTP_REFERENCE) ");
			StringBuffer strBufPending = new StringBuffer("Select TPend.COUNTRY,TPend.LE_BOOK,TPend.METHOD_REFERENCE," +
					"TPend.FTP_DESCRIPTION,TPend.FTP_REFERENCE,TPend.FTP_CONTROLS_STATUS_NT,TPend.FTP_CONTROLS_STATUS," +
					"TPend.SOURCE_REFERENCE_AT, TPend.SOURCE_REFERENCE,"+
					"TPend.RECORD_INDICATOR_NT," +
				"TPend.RECORD_INDICATOR, TPend.MAKER, TPend.VERIFIER," +
				"TPend.INTERNAL_STATUS, To_Char(TPend.DATE_LAST_MODIFIED, 'DD-MM-RRRR HH24:MI:SS') DATE_LAST_MODIFIED," +
				"To_Char(TPend.DATE_CREATION, 'DD-MM-RRRR HH24:MI:SS') DATE_CREATION" +
				" From FTP_CONTROLS_PEND TPend ");
		try
		{
			
			//check if the column [GL_ENRICH_STATUS] should be included in the query
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement( dObj.getCountry().toUpperCase() );
				CommonUtils.addToQuery("TAppr.COUNTRY = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.COUNTRY = ?", strBufPending);
			}
			//check if the column [LE_BOOK] should be included in the query
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement( dObj.getLeBook().toUpperCase() );
				CommonUtils.addToQuery("TAppr.LE_BOOK = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.LE_BOOK = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getMethodReference())){
				params.addElement("%" + dObj.getMethodReference().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.METHOD_REFERENCE) LIKE ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.METHOD_REFERENCE) LIKE ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getFtpDescription())){
				params.addElement("%" + dObj.getFtpDescription().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.FTP_DESCRIPTION) LIKE ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.FTP_DESCRIPTION) LIKE ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getFtpReference())){
				params.addElement("%" + dObj.getFtpReference().toUpperCase() + "%");
				CommonUtils.addToQuery("UPPER(TAppr.FTP_REFERENCE) LIKE ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.FTP_REFERENCE) LIKE ?", strBufPending);
			}
			if (dObj.getStatus() != -1){
				params.addElement(new Integer(dObj.getStatus()));
				CommonUtils.addToQuery("TAppr.FTP_CONTROLS_STATUS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.FTP_CONTROLS_STATUS = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getSourceReference()) && !"-1".equalsIgnoreCase(dObj.getSourceReference()))
			{
				params.addElement(dObj.getSourceReference().toUpperCase());
				CommonUtils.addToQuery("TAppr.SOURCE_REFERENCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.SOURCE_REFERENCE = ?", strBufPending);
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
			String orderBy=" Order By COUNTRY, LE_BOOK, FTP_REFERENCE";
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
	public List<FtpControlsVb> getQueryResults(FtpControlsVb dObj, int intStatus){

		List<FtpControlsVb> collTemp = null;
		final int intKeyFieldsCount = 3;
		setServiceDefaults();
		String strQueryAppr = new String("Select TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.METHOD_REFERENCE," +
				"TAppr.FTP_DESCRIPTION,TAppr.FTP_REFERENCE,TAppr.FTP_CONTROLS_STATUS_NT,TAppr.FTP_CONTROLS_STATUS," +
				"TAppr.SOURCE_REFERENCE_AT, TAppr.SOURCE_REFERENCE," +
				"TAppr.RECORD_INDICATOR_NT," +
				"TAppr.RECORD_INDICATOR, TAppr.MAKER, TAppr.VERIFIER," +
				"TAppr.INTERNAL_STATUS, To_Char(TAppr.DATE_LAST_MODIFIED, 'DD-MM-RRRR HH24:MI:SS') DATE_LAST_MODIFIED," +
				"To_Char(TAppr.DATE_CREATION, 'DD-MM-RRRR HH24:MI:SS') DATE_CREATION" +
				" From FTP_CONTROLS TAppr "+
				"Where TAppr.COUNTRY = ? AND TAppr.LE_BOOK = ? AND TAppr.FTP_REFERENCE = ?");
			String strQueryPend = new String("Select TPend.COUNTRY,TPend.LE_BOOK,TPend.METHOD_REFERENCE," +
					"TPend.FTP_DESCRIPTION,TPend.FTP_REFERENCE,TPend.FTP_CONTROLS_STATUS_NT,TPend.FTP_CONTROLS_STATUS," +
					"TPend.SOURCE_REFERENCE_AT, TPend.SOURCE_REFERENCE,"+
					"TPend.RECORD_INDICATOR_NT," +
					"TPend.RECORD_INDICATOR, TPend.MAKER, TPend.VERIFIER," +
					"TPend.INTERNAL_STATUS, To_Char(TPend.DATE_LAST_MODIFIED, 'DD-MM-RRRR HH24:MI:SS') DATE_LAST_MODIFIED," +
					"To_Char(TPend.DATE_CREATION, 'DD-MM-RRRR HH24:MI:SS') DATE_CREATION" +
					" From FTP_CONTROLS_PEND TPend "+
					"Where TPend.COUNTRY = ? AND TPend.LE_BOOK = ? AND TPend.FTP_REFERENCE = ?");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getCountry());
		objParams[1] = new String(dObj.getLeBook());
		objParams[2] = new String(dObj.getFtpReference());
	/*	objParams[1] = new String(dObj.getMethodType());*/
		/*objParams[2] = new String(dObj.getRepricingFlag());*///[VARIABLE]

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
	protected List<FtpControlsVb> selectApprovedRecord(FtpControlsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}
	@Override
	protected List<FtpControlsVb> doSelectPendingRecord(FtpControlsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}
	@Override
	protected int getStatus(FtpControlsVb records){return records.getStatus();}
	@Override
	protected void setStatus(FtpControlsVb vObject,int status){vObject.setStatus(status);}
	protected int doInsertionAppr(FtpControlsVb vObject){
		String query = "Insert Into FTP_CONTROLS ( COUNTRY,LE_BOOK,METHOD_REFERENCE, FTP_DESCRIPTION,FTP_REFERENCE,FTP_CONTROLS_STATUS_NT,FTP_CONTROLS_STATUS,"+
			"SOURCE_REFERENCE_AT, SOURCE_REFERENCE,RECORD_INDICATOR_NT,RECORD_INDICATOR,"+
			"MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,SysDate,SysDate)";
		Object[] args = {vObject.getCountry(),vObject.getLeBook(),vObject.getMethodReference(), vObject.getFtpDescription(),
				vObject.getFtpReference(), vObject.getStatusNt(),vObject.getStatus(),vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus()};  
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doInsertionPend(FtpControlsVb vObject){
		String query = "Insert Into FTP_CONTROLS_PEND ( COUNTRY,LE_BOOK,METHOD_REFERENCE, FTP_DESCRIPTION,FTP_REFERENCE,FTP_CONTROLS_STATUS_NT,FTP_CONTROLS_STATUS,"+
			"SOURCE_REFERENCE_AT, SOURCE_REFERENCE,RECORD_INDICATOR_NT,RECORD_INDICATOR,"+
			"MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,SysDate,SysDate)";
		Object[] args = { vObject.getCountry(),vObject.getLeBook(),vObject.getMethodReference(), vObject.getFtpDescription(),
				vObject.getFtpReference(), vObject.getStatusNt(),vObject.getStatus(),vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus()}; 
		return getJdbcTemplate().update(query,args);	
	}
	@Override
	protected int doInsertionPendWithDc(FtpControlsVb vObject){
		String query = "Insert Into FTP_CONTROLS_PEND ( COUNTRY,LE_BOOK,METHOD_REFERENCE, FTP_DESCRIPTION,FTP_REFERENCE,FTP_CONTROLS_STATUS_NT,FTP_CONTROLS_STATUS,"+
			"SOURCE_REFERENCE_AT, SOURCE_REFERENCE,RECORD_INDICATOR_NT,RECORD_INDICATOR,"+
			"MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,SysDate,SysDate)";
		Object[] args ={vObject.getCountry(),vObject.getLeBook(),vObject.getMethodReference(), vObject.getFtpDescription(),
				vObject.getFtpReference(), vObject.getStatusNt(),vObject.getStatus(),vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus()}; 
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doUpdateAppr(FtpControlsVb vObject){
		String query = "Update FTP_CONTROLS Set METHOD_REFERENCE = ?, FTP_DESCRIPTION = ?,FTP_REFERENCE = ?, " +
				"FTP_CONTROLS_STATUS_NT = ?,FTP_CONTROLS_STATUS = ?,SOURCE_REFERENCE_AT = ?,SOURCE_REFERENCE = ?,RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, " +
				"INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = SysDate Where COUNTRY = ? AND LE_BOOK = ? AND FTP_REFERENCE = ?";
		Object[] args = {vObject.getMethodReference(), vObject.getFtpDescription(),
				vObject.getFtpReference(), vObject.getStatusNt(),vObject.getStatus(),vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getCountry(),vObject.getLeBook(),vObject.getFtpReference()}; 
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doUpdatePend(FtpControlsVb vObject){
		String query = "Update FTP_CONTROLS_PEND Set METHOD_REFERENCE = ?, FTP_DESCRIPTION = ?,FTP_REFERENCE = ?, " +
				"FTP_CONTROLS_STATUS_NT = ?,FTP_CONTROLS_STATUS = ?,SOURCE_REFERENCE_AT = ?,SOURCE_REFERENCE = ?,RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, " +
				"INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = SysDate Where COUNTRY = ? AND LE_BOOK = ? AND FTP_REFERENCE = ?";
		Object[] args = {vObject.getMethodReference(), vObject.getFtpDescription(),
				vObject.getFtpReference(), vObject.getStatusNt(),vObject.getStatus(),vObject.getSourceReferenceAt(),vObject.getSourceReference(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getCountry(),vObject.getLeBook(),vObject.getFtpReference()}; 
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doDeleteAppr(FtpControlsVb vObject){
		String query = "Delete From FTP_CONTROLS Where COUNTRY= ? AND LE_BOOK= ? AND FTP_REFERENCE = ?";
		Object[] args = {vObject.getCountry(),vObject.getLeBook(),vObject.getFtpReference()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int deletePendingRecord(FtpControlsVb vObject){
		String query = "Delete From FTP_CONTROLS_PEND Where COUNTRY= ? AND LE_BOOK= ? AND FTP_REFERENCE = ?";
		Object[] args = {vObject.getCountry(),vObject.getLeBook(),vObject.getFtpReference()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected String frameErrorMessage(FtpControlsVb vObject, String strOperation){
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try{
			
			strErrMsg =  strErrMsg + "COUNTRY:" + vObject.getCountry();
			strErrMsg =  strErrMsg + ",LE_BOOK:" + vObject.getLeBook();
			strErrMsg =  strErrMsg + ",FTP_REFERENCE:" + vObject.getFtpReference();
			// Now concatenate the error message that has been sent
			if ("Approve".equalsIgnoreCase(strOperation))
				strErrMsg = strErrMsg + " failed during approve Operation. Bulk Approval aborted !!";
			else
				strErrMsg = strErrMsg + " failed during reject Operation. Bulk Rejection aborted !!";
			// Return back the error message string
			return strErrMsg;
		}catch(Exception ex){
			strErrorDesc = ex.getMessage();
			strErrMsg = strErrMsg + strErrorDesc;
			logger.error(strErrMsg, ex);
		}
		// Return back the error message string
		return strErrMsg;
	}
	@Override
	protected String getAuditString(FtpControlsVb vObject){
		StringBuffer strAudit = new StringBuffer("");
		if(vObject.getCountry() != null && !vObject.getCountry().equalsIgnoreCase(""))
			strAudit.append(vObject.getCountry().trim());
		else
			strAudit.append("NULL");
		strAudit.append("!|#");

		if(vObject.getLeBook() != null && !vObject.getLeBook().equalsIgnoreCase(""))
			strAudit.append(vObject.getLeBook().trim());
		else
			strAudit.append("NULL");
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodReference().trim());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpDescription());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpReference());
		strAudit.append("!|#");
		strAudit.append(vObject.getStatusNt());
		strAudit.append("!|#");
		strAudit.append(vObject.getStatus());
		strAudit.append("!|#");
		strAudit.append(vObject.getSourceReferenceAt());
		strAudit.append("!|#");
		strAudit.append(vObject.getSourceReference());
		strAudit.append("!|#");
		strAudit.append(vObject.getRecordIndicatorNt());
		strAudit.append("!|#");
		if(vObject.getRecordIndicator()== -1)
			vObject.setRecordIndicator(0);
		strAudit.append(vObject.getRecordIndicator());
		strAudit.append("!|#");
		strAudit.append(vObject.getMaker());
		strAudit.append("!|#");
		strAudit.append(vObject.getVerifier());
		strAudit.append("!|#");
		strAudit.append(vObject.getInternalStatus());
		strAudit.append("!|#");
		if(vObject.getDateLastModified() != null  && !vObject.getDateLastModified().equalsIgnoreCase(""))
			strAudit.append(vObject.getDateLastModified().trim());
		else
			strAudit.append("NULL");
		strAudit.append("!|#");
		if(vObject.getDateCreation() != null && !vObject.getDateCreation().equalsIgnoreCase(""))
			strAudit.append(vObject.getDateCreation().trim());
		else
			strAudit.append("NULL");
		strAudit.append("!|#");
		return strAudit.toString();
	}
	@Override
	protected void setServiceDefaults(){
		serviceName = "FtpControls";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpControls");//"GL Enrich Ids";
		tableName = "Ftp_controls";
		childTableName = "Ftp_controls";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
}