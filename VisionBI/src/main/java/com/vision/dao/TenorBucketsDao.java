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
import com.vision.vb.SmartSearchVb;
import com.vision.vb.TenorBucketsVb;

@Component
public class TenorBucketsDao extends AbstractDao<TenorBucketsVb> {
	
	String TenorAppCodeNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.TENOR_APPLICATION_CODE", "TENOR_APPLICATION_CODE_DESC");
	String TenorAppCodeNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.TENOR_APPLICATION_CODE", "TENOR_APPLICATION_CODE_DESC");
	
	String StatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.TENOR_STATUS", "TENOR_STATUS_DESC");
	String StatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.TENOR_STATUS", "TENOR_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	
	public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				TenorBucketsVb tenorBucketsVb = new TenorBucketsVb();
				tenorBucketsVb.setDateCreation(rs.getString("DATE_CREATION"));
				tenorBucketsVb.setTenorBucketApplicationCodeNt(rs.getInt("TENOR_APPLICATION_CODE_NT"));
				tenorBucketsVb.setTenorBucketApplicationCode(rs.getInt("TENOR_APPLICATION_CODE"));
				tenorBucketsVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				tenorBucketsVb.setTenorBucketDescription(rs.getString("TENOR_DESCRIPTION"));
				tenorBucketsVb.setDayStart(rs.getString("DAY_START"));
				tenorBucketsVb.setDayEnd(rs.getString("DAY_END"));
				tenorBucketsVb.setTenorBucketStatusNt(rs.getInt("TENOR_STATUS_NT"));
				tenorBucketsVb.setTenorBucketStatus(rs.getInt("TENOR_STATUS"));
				tenorBucketsVb.setDbStatus(rs.getInt("TENOR_STATUS"));
				tenorBucketsVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				tenorBucketsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				tenorBucketsVb.setMaker(rs.getInt("MAKER"));
				tenorBucketsVb.setVerifier(rs.getInt("VERIFIER"));
				tenorBucketsVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				tenorBucketsVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				
				if(rs.getString("MAKER_NAME")!= null){ 
					tenorBucketsVb.setMakerName(rs.getString("MAKER_NAME"));
				}
				if(rs.getString("MAKER_NAME")!= null){ 
					tenorBucketsVb.setMakerName(rs.getString("MAKER_NAME"));
				}
				tenorBucketsVb.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				tenorBucketsVb.setTenorBucketStatusDesc(rs.getString("TENOR_STATUS_DESC"));
				
				tenorBucketsVb.setTenorBucketApplicationCodeDesc(rs.getString("TENOR_APPLICATION_CODE_DESC"));
				
				if(rs.getString("VERIFIER_NAME")!= null){ 
					tenorBucketsVb.setVerifierName(rs.getString("VERIFIER_NAME"));
				}
				
				return tenorBucketsVb;
			}
		};
		return mapper;
	}
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				TenorBucketsVb tenorBucketsVb = new TenorBucketsVb();
				tenorBucketsVb.setDateCreation(rs.getString("DATE_CREATION"));
				tenorBucketsVb.setTenorBucketApplicationCodeNt(rs.getInt("TENOR_APPLICATION_CODE_NT"));
				tenorBucketsVb.setTenorBucketApplicationCode(rs.getInt("TENOR_APPLICATION_CODE"));
				tenorBucketsVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				tenorBucketsVb.setTenorBucketDescription(rs.getString("TENOR_DESCRIPTION"));
				tenorBucketsVb.setDayStart(rs.getString("DAY_START"));
				tenorBucketsVb.setDayEnd(rs.getString("DAY_END"));
				tenorBucketsVb.setTenorBucketStatusNt(rs.getInt("TENOR_STATUS_NT"));
				tenorBucketsVb.setTenorBucketStatus(rs.getInt("TENOR_STATUS"));
				tenorBucketsVb.setDbStatus(rs.getInt("TENOR_STATUS"));
				tenorBucketsVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				tenorBucketsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				tenorBucketsVb.setMaker(rs.getInt("MAKER"));
				tenorBucketsVb.setVerifier(rs.getInt("VERIFIER"));
				tenorBucketsVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				tenorBucketsVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				if(rs.getString("MAKER_NAME")!= null){ 
					tenorBucketsVb.setMakerName(rs.getString("MAKER_NAME"));
				}
				
				if(rs.getString("VERIFIER_NAME")!= null){ 
					tenorBucketsVb.setVerifierName(rs.getString("VERIFIER_NAME"));
				}
				return tenorBucketsVb;
			}
		};
		return mapper;
	}

	public List<TenorBucketsVb> getQueryPopupResults(TenorBucketsVb dObj){
		Vector<Object> params = new Vector<Object>();
		/*StringBuffer strBufApprove = new StringBuffer("Select Distinct TAppr.TENOR_APPLICATION_CODE"+
			" From TENOR_BUCKETS TAppr ");
		String strWhereNotExists = new String(" Not Exists (Select 'X' From TENOR_BUCKETS_PEND TPend Where " + 
			"TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE "+
			"And TAppr.TENOR_CODE = TPend.TENOR_CODE) ");
		StringBuffer strBufPending = new StringBuffer("Select Distinct TPend.TENOR_APPLICATION_CODE"+
			" From TENOR_BUCKETS_PEND TPend ");*/
		
		StringBuffer strBufApprove = new StringBuffer("Select "+dateFormat+"(TAppr.DATE_CREATION, " + 
				dateFormatStr+" ) DATE_CREATION,TAppr.TENOR_APPLICATION_CODE_NT"
						+ ",TAppr.TENOR_APPLICATION_CODE, " +TenorAppCodeNtApprDesc+ 
			", TAppr.TENOR_CODE,TAppr.TENOR_DESCRIPTION,TAppr.DAY_START,TAppr.DAY_END,TAppr.TENOR_STATUS_NT, " +StatusNtApprDesc+ 
			",TAppr.TENOR_STATUS,TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR,"+RecordIndicatorNtApprDesc
			+ ","
			+ "TAppr.MAKER, "+makerApprDesc
			+ ", TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS,"+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED From TENOR_BUCKETS TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From TENOR_BUCKETS_PEND TPend Where " + 
			"TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE " + 
			"And TAppr.TENOR_CODE = TPend.TENOR_CODE )");

		StringBuffer strBufPending = new StringBuffer("Select "+dateFormat+"(TPend.DATE_CREATION, " + 
			dateFormatStr+" ) DATE_CREATION,TPend.TENOR_APPLICATION_CODE_NT,TPend.TENOR_APPLICATION_CODE, "+TenorAppCodeNtPendDesc
					+ ", " + 
			"TPend.TENOR_CODE,TPend.TENOR_DESCRIPTION,TPend.DAY_START,TPend.DAY_END,TPend.TENOR_STATUS_NT, " +StatusNtPendDesc+ 
			",TPend.TENOR_STATUS"
			+ ",TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR,"+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, " +makerPendDesc 
			+", TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS,"+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED From TENOR_BUCKETS_PEND TPend ");
		try
		{
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data: dObj.getSmartSearchOpt()){
					if(count == dObj.getSmartSearchOpt().size()) {
						data.setJoinType("");
					} else {
						if(!ValidationUtil.isValid(data.getJoinType()) && !("AND".equalsIgnoreCase(data.getJoinType()) || "OR".equalsIgnoreCase(data.getJoinType()))) {
							data.setJoinType("AND");
						}
					}
					String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
					switch (data.getObject()) {
					case "tenorApplicationCode":
						CommonUtils.addToQuerySearch(" upper(TAppr.TENOR_APPLICATION_CODE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.TENOR_APPLICATION_CODE) "+ val, strBufPending, data.getJoinType());
						break;

					case "tenorCode":
						CommonUtils.addToQuerySearch(" upper(TAppr.TENOR_CODE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.TENOR_CODE) "+ val, strBufPending, data.getJoinType());
						break;

					case "tenorDescription":
						CommonUtils.addToQuerySearch(" upper(TAppr.TENOR_DESCRIPTION) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.TENOR_DESCRIPTION) "+ val, strBufPending, data.getJoinType());
						break;

					case "dayStart":
						CommonUtils.addToQuerySearch(" upper(TAppr.DAY_START) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.DAY_START) "+ val, strBufPending, data.getJoinType());
						break;

					case "dayEnd":
						CommonUtils.addToQuerySearch(" upper(TAppr.DAY_END) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.DAY_END) "+ val, strBufPending, data.getJoinType());
						break;

					case "tenorStatus":
						CommonUtils.addToQuerySearch(" upper(TAppr.TENOR_STATUS) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.TENOR_STATUS) "+ val, strBufPending, data.getJoinType());
						break;

					case "recordIndicator":
						CommonUtils.addToQuerySearch(" upper(TAppr.RECORD_INDICATOR) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.RECORD_INDICATOR) "+ val, strBufPending, data.getJoinType());
						break;

						default:
					}
					count++;
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

		String orderBy = " Order By TENOR_APPLICATION_CODE";
		return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params, getQueryPopupMapper());
	}

	public List<TenorBucketsVb> getQueryResults(TenorBucketsVb dObj, int intStatus){
		Vector<Object> params = new Vector<Object>();
		setServiceDefaults();
		StringBuffer strBufApprove = new StringBuffer("Select "+dateFormat+"(TAppr.DATE_CREATION, " + 
				dateFormatStr+" ) DATE_CREATION,TAppr.TENOR_APPLICATION_CODE_NT,TAppr.TENOR_APPLICATION_CODE, " +TenorAppCodeNtApprDesc+ 
			",TAppr.TENOR_CODE,TAppr.TENOR_DESCRIPTION,TAppr.DAY_START,TAppr.DAY_END,TAppr.TENOR_STATUS_NT, " + 
			"TAppr.TENOR_STATUS, "+StatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR,"+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, " +makerApprDesc 
			+",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS,"+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED From TENOR_BUCKETS TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From TENOR_BUCKETS_PEND TPend Where " + 
			"TAppr.TENOR_APPLICATION_CODE = TPend.TENOR_APPLICATION_CODE " + 
			"And TAppr.TENOR_CODE = TPend.TENOR_CODE )");

		StringBuffer strBufPending = new StringBuffer("Select "+dateFormat+"(TPend.DATE_CREATION, " + 
			dateFormatStr+" ) DATE_CREATION,TPend.TENOR_APPLICATION_CODE_NT,TPend.TENOR_APPLICATION_CODE, " +TenorAppCodeNtPendDesc+ 
			",TPend.TENOR_CODE,TPend.TENOR_DESCRIPTION,TPend.DAY_START,TPend.DAY_END,TPend.TENOR_STATUS_NT, " + 
			"TPend.TENOR_STATUS, "+StatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR,"+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, " +makerPendDesc 
			+", TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS,"+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED From TENOR_BUCKETS_PEND TPend ");

		try
		{
			if (dObj.getTenorBucketApplicationCode()!=-1)
			{
				params.addElement(dObj.getTenorBucketApplicationCode());
				CommonUtils.addToQuery("TAppr.TENOR_APPLICATION_CODE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.TENOR_APPLICATION_CODE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getTenorBucketCode()))
			{
				params.addElement("%" + dObj.getTenorBucketCode().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.TENOR_CODE) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.TENOR_CODE) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getTenorBucketDescription()))
			{
				params.addElement("%" + dObj.getTenorBucketDescription().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.TENOR_DESCRIPTION) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.TENOR_DESCRIPTION) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getDayStart()))
			{
				params.addElement(dObj.getDayStart());
				CommonUtils.addToQuery("TAppr.DAY_START = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DAY_START = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getDayEnd()))
			{
				params.addElement(dObj.getDayEnd());
				CommonUtils.addToQuery("TAppr.DAY_END = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DAY_END = ?", strBufPending);
			}
			if (dObj.getTenorBucketStatus() != -1)
			{
				params.addElement("%" + dObj.getTenorBucketStatus() + "%" );
				CommonUtils.addToQuery("TAppr.TENOR_STATUS like ?", strBufApprove);
				CommonUtils.addToQuery("TPend.TENOR_STATUS like ?", strBufPending);
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

			String orderBy = " Order By TENOR_APPLICATION_CODE, TENOR_CODE";
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

	public List<TenorBucketsVb> getQueryResultsForReview(TenorBucketsVb dObj, int intStatus){

		List<TenorBucketsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		StringBuffer strQueryAppr = new StringBuffer("Select "+dateFormat+"(TAppr.DATE_CREATION, " + 
				dateFormatStr+" ) DATE_CREATION," +
			"TAppr.TENOR_APPLICATION_CODE_NT, " + 
			"TAppr.TENOR_APPLICATION_CODE, "+TenorAppCodeNtApprDesc
			+ ",TAppr.TENOR_CODE,TAppr.TENOR_DESCRIPTION,TAppr.DAY_START, " + 
			"TAppr.DAY_END,TAppr.TENOR_STATUS_NT,TAppr.TENOR_STATUS, "+StatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT, " + 
			"TAppr.RECORD_INDICATOR,"+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER,"+makerApprDesc
			+ ", TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS,"+dateFormat+"(TAppr.DATE_LAST_MODIFIED, " + 
			dateFormatStr+" ) DATE_LAST_MODIFIED" +
			" From TENOR_BUCKETS TAppr " + 
			"Where TAppr.TENOR_APPLICATION_CODE = ?  And TAppr.TENOR_CODE = ? ");

		StringBuffer strQueryPend = new StringBuffer("Select "+dateFormat+"(TPend.DATE_CREATION, " + 
				dateFormatStr+" ) DATE_CREATION," +
			"TPend.TENOR_APPLICATION_CODE_NT, " + 
			"TPend.TENOR_APPLICATION_CODE, "+TenorAppCodeNtPendDesc
			+ ",TPend.TENOR_CODE,TPend.TENOR_DESCRIPTION,TPend.DAY_START, " + 
			"TPend.DAY_END,TPend.TENOR_STATUS_NT,TPend.TENOR_STATUS, "+StatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT, " + 
			"TPend.RECORD_INDICATOR,"+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS,"+dateFormat+"(TPend.DATE_LAST_MODIFIED, " + 
			dateFormatStr+" ) DATE_LAST_MODIFIED" +
			" From TENOR_BUCKETS_PEND TPend " + 
			"Where TPend.TENOR_APPLICATION_CODE = ?  And TPend.TENOR_CODE = ? ");

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
	protected List<TenorBucketsVb> selectApprovedRecord(TenorBucketsVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<TenorBucketsVb> doSelectPendingRecord(TenorBucketsVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}

	@Override
	protected void setServiceDefaults(){
		serviceName = "TenorBuckets";
		serviceDesc = CommonUtils.getResourceManger().getString("tenorBuckets");
		tableName = "TENOR_BUCKETS";
		childTableName = "TENOR_BUCKETS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}

	@Override
	protected int getStatus(TenorBucketsVb records){return records.getTenorBucketStatus();}

	@Override
	protected void setStatus(TenorBucketsVb vObject,int status){vObject.setTenorBucketStatus(status);}

	@Override
	protected int doInsertionAppr(TenorBucketsVb vObject){
		String query = "Insert Into TENOR_BUCKETS( " + 
		" TENOR_APPLICATION_CODE_NT, TENOR_APPLICATION_CODE, TENOR_CODE, TENOR_DESCRIPTION, " + 
			" DAY_START, DAY_END, TENOR_STATUS_NT, TENOR_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, " + 
			" MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";
		Object args[] = { vObject.getTenorBucketApplicationCodeNt(), vObject.getTenorBucketApplicationCode(),
			 vObject.getTenorBucketCode(), vObject.getTenorBucketDescription(), vObject.getDayStart(), vObject.getDayEnd(),
			 vObject.getTenorBucketStatusNt(), vObject.getTenorBucketStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPend(TenorBucketsVb vObject){
		String query = "Insert Into TENOR_BUCKETS_PEND( " + 
		"TENOR_APPLICATION_CODE_NT, TENOR_APPLICATION_CODE, TENOR_CODE, TENOR_DESCRIPTION, " + 
			" DAY_START, DAY_END, TENOR_STATUS_NT, TENOR_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, " + 
			" MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED,DATE_CREATION) " + 
			" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getTenorBucketApplicationCodeNt(), vObject.getTenorBucketApplicationCode(),
			 vObject.getTenorBucketCode(), vObject.getTenorBucketDescription(), vObject.getDayStart(), vObject.getDayEnd(),
			 vObject.getTenorBucketStatusNt(), vObject.getTenorBucketStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPendWithDc(TenorBucketsVb vObject){
		String query = "Insert Into TENOR_BUCKETS_PEND( " + 
		" TENOR_APPLICATION_CODE_NT, TENOR_APPLICATION_CODE, TENOR_CODE, TENOR_DESCRIPTION, " + 
			" DAY_START, DAY_END, TENOR_STATUS_NT, TENOR_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, " + 
			" MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			"Values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+dateTimeConvert+")";

		Object args[] = {vObject.getTenorBucketApplicationCodeNt(), vObject.getTenorBucketApplicationCode(), 
			 vObject.getTenorBucketCode(), vObject.getTenorBucketDescription(), vObject.getDayStart(), vObject.getDayEnd(), 
			 vObject.getTenorBucketStatusNt(), vObject.getTenorBucketStatus(), vObject.getRecordIndicatorNt(), 
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), vObject.getDateCreation()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdateAppr(TenorBucketsVb vObject){
		String query = "Update TENOR_BUCKETS Set " + 
		"TENOR_APPLICATION_CODE_NT = ?, TENOR_DESCRIPTION = ?, DAY_START = ?, DAY_END = ?, " + 
		" TENOR_STATUS_NT = ?, TENOR_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, " + 
		" MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+
		" Where TENOR_APPLICATION_CODE = ? " +
		" And TENOR_CODE = ? ";

		Object args[] = {vObject.getTenorBucketApplicationCodeNt(), vObject.getTenorBucketDescription(), vObject.getDayStart(), vObject.getDayEnd(),
		vObject.getTenorBucketStatusNt(), vObject.getTenorBucketStatus(), vObject.getRecordIndicatorNt(),
		vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
		vObject.getTenorBucketApplicationCode(), vObject.getTenorBucketCode()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdatePend(TenorBucketsVb vObject){
		String query = "Update TENOR_BUCKETS_PEND Set " + 
		"TENOR_APPLICATION_CODE_NT = ?, TENOR_DESCRIPTION = ?, DAY_START = ?, DAY_END = ?, " + 
		" TENOR_STATUS_NT = ?, TENOR_STATUS = ?, RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, " + 
		" MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+" " + 
		" Where TENOR_APPLICATION_CODE = ? " +
		" And TENOR_CODE = ? ";

		Object args[] = {vObject.getTenorBucketApplicationCodeNt(), vObject.getTenorBucketDescription(), vObject.getDayStart(), vObject.getDayEnd(),
		vObject.getTenorBucketStatusNt(), vObject.getTenorBucketStatus(), vObject.getRecordIndicatorNt(),
		vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
		vObject.getTenorBucketApplicationCode(), vObject.getTenorBucketCode()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doDeleteAppr(TenorBucketsVb vObject){
		String query = "Delete From TENOR_BUCKETS Where " + 
		"TENOR_APPLICATION_CODE = ? AND " + 
		"TENOR_CODE = ? " ;
		Object args[] = {vObject.getTenorBucketApplicationCode(), vObject.getTenorBucketCode()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int deletePendingRecord(TenorBucketsVb vObject){
		String query = "Delete From TENOR_BUCKETS_PEND Where " + 
		"TENOR_APPLICATION_CODE = ? AND " + 
		"TENOR_CODE = ? " ;

		Object args[] = {vObject.getTenorBucketApplicationCode(), vObject.getTenorBucketCode()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected String frameErrorMessage(TenorBucketsVb vObject, String strOperation)
	{
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try {
			strErrMsg =  strErrMsg + " TENOR_APPLICATION_CODE:" + vObject.getTenorBucketApplicationCode();
			strErrMsg =  strErrMsg + " TENOR_CODE:" + vObject.getTenorBucketCode();
			// Now concatenate the error message that has been sent
			if ("Approve".equalsIgnoreCase(strOperation))
				strErrMsg = strErrMsg + " failed during approve Operation.  Bulk Approval aborted !!";
			else
				strErrMsg = strErrMsg + " failed during reject Operation.  Bulk Rejection aborted !!";
		}catch(Exception ex){
			strErrorDesc = ex.getMessage();
			strErrMsg = strErrMsg + strErrorDesc;
			logger.error(strErrMsg, ex);
		}
		// Return back the error message string
		return strErrMsg;
	}


	@Override
	protected String getAuditString(TenorBucketsVb vObject)
	{
		final String auditDelimiter = vObject.getAuditDelimiter();
		final String auditDelimiterColVal = vObject.getAuditDelimiterColVal();
		StringBuffer strAudit = new StringBuffer("");
		try
		{
			strAudit.append("TENOR_APPLICATION_CODE_NT"+auditDelimiterColVal+vObject.getTenorBucketApplicationCodeNt());
			strAudit.append(auditDelimiter);
			strAudit.append("TENOR_APPLICATION_CODE"+auditDelimiterColVal+vObject.getTenorBucketApplicationCode());
			strAudit.append(auditDelimiter);
			if(vObject.getTenorBucketCode() != null)
				strAudit.append("TENOR_CODE"+auditDelimiterColVal+vObject.getTenorBucketCode().trim());
			else
				strAudit.append("TENOR_CODE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			if(vObject.getTenorBucketDescription() != null)
				strAudit.append("TENOR_DESCRIPTION"+auditDelimiterColVal+vObject.getTenorBucketDescription().trim());
			else
				strAudit.append("TENOR_DESCRIPTION"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

			strAudit.append("DAY_START"+auditDelimiterColVal+vObject.getDayStart());
			strAudit.append(auditDelimiter);
			strAudit.append("DAY_END"+auditDelimiterColVal+vObject.getDayEnd());
			strAudit.append(auditDelimiter);
			strAudit.append("TENOR_STATUS_NT"+auditDelimiterColVal+vObject.getTenorBucketStatusNt());
			strAudit.append(auditDelimiter);
			strAudit.append("TENOR_STATUS"+auditDelimiterColVal+vObject.getTenorBucketStatus());
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
			if(vObject.getDateLastModified() != null)
				strAudit.append("DATE_LAST_MODIFIED"+auditDelimiterColVal+vObject.getDateLastModified().trim());
			else
				strAudit.append("DATE_LAST_MODIFIED"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);
			
			if(vObject.getDateCreation() != null)
				strAudit.append("DATE_CREATION"+auditDelimiterColVal+vObject.getDateCreation().trim());
			else
				strAudit.append("DATE_CREATION"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

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
