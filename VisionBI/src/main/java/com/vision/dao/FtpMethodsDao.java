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
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.SmartSearchVb;

@Component
public class FtpMethodsDao extends AbstractDao<FtpMethodsVb> {
	
	
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpMethodsVb ftpMethodsVb = new FtpMethodsVb();
				ftpMethodsVb = new FtpMethodsVb();
				ftpMethodsVb.setMethodReference(rs.getString("METHOD_REFERENCE"));
				ftpMethodsVb.setMethodDescription(rs.getString("METHOD_DESCRIPTION"));
				ftpMethodsVb.setFtpCurveIdAt(rs.getInt("FTP_CURVE_ID_AT"));
				ftpMethodsVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpMethodsVb.setMethodTypeAt(rs.getInt("METHOD_TYPE_AT"));
				ftpMethodsVb.setMethodType(rs.getString("METHOD_TYPE"));
				ftpMethodsVb.setMethodSubTypeAt(rs.getInt("METHOD_SUB_TYPE_AT"));
				ftpMethodsVb.setMethodSubType(rs.getString("METHOD_SUB_TYPE"));
				ftpMethodsVb.setFtpTenorTypeNt(rs.getInt("TENOR_TYPE_NT"));
				ftpMethodsVb.setFtpTenorType(rs.getInt("FTP_TENOR_TYPE"));
				ftpMethodsVb.setMethodBalTypeNt(rs.getInt("METHOD_BAL_TYPE_NT"));
				ftpMethodsVb.setMethodBalType(rs.getInt("METHOD_BAL_TYPE"));
				ftpMethodsVb.setStatusNt(rs.getInt("FTP_MT_STATUS_NT"));
				ftpMethodsVb.setStatus(rs.getInt("FTP_MT_STATUS"));
				ftpMethodsVb.setRepricingFlagAt(rs.getInt("REPRICING_FLAG_AT"));
				ftpMethodsVb.setRepricingFlag(rs.getString("REPRICING_FLAG"));
				ftpMethodsVb.setLpTenorType(rs.getInt("LP_TENOR_TYPE"));
				ftpMethodsVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpMethodsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpMethodsVb.setMaker(rs.getLong("MAKER"));
				ftpMethodsVb.setVerifier(rs.getLong("VERIFIER"));
				ftpMethodsVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpMethodsVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpMethodsVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpMethodsVb.setInterestBasisNt(rs.getInt("INTEREST_BASIS_NT"));
				ftpMethodsVb.setInterestBasis(rs.getInt("INTEREST_BASIS"));
				ftpMethodsVb.setFtpApplyRateNt(rs.getInt("APPLY_RATE_NT"));
				ftpMethodsVb.setFtpApplyRate(rs.getInt("FTP_APPLY_RATE"));
				ftpMethodsVb.setAddonApplyRate(rs.getInt("ADDON_APPLY_RATE"));
				ftpMethodsVb.setLpApplyRate(rs.getInt("LP_APPLY_RATE"));
				return ftpMethodsVb;
			}
		};
		return mapper;
	}
	
	public List<FtpMethodsVb> getQueryPopupResults(FtpMethodsVb dObj){
		
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.METHOD_REFERENCE," +
				"TAppr.METHOD_DESCRIPTION,TAppr.FTP_CURVE_ID_AT,TAppr.FTP_CURVE_ID," +
				"TAppr.METHOD_TYPE_AT, TAppr.METHOD_TYPE," +
				"TAppr.METHOD_SUB_TYPE_AT, TAppr.METHOD_SUB_TYPE," +
				"TAppr.TENOR_TYPE_NT,TAppr.FTP_TENOR_TYPE,TAppr.METHOD_BAL_TYPE_NT,TAppr.METHOD_BAL_TYPE,TAppr.APPLY_RATE_NT,TAppr.FTP_APPLY_RATE,TAppr.ADDON_APPLY_RATE,TAppr.LP_APPLY_RATE,TAppr.FTP_MT_STATUS_NT,TAppr.FTP_MT_STATUS,TAppr.REPRICING_FLAG_AT,TAppr.REPRICING_FLAG,TAppr.LP_TENOR_TYPE,TAppr.RECORD_INDICATOR_NT," +
				"TAppr.RECORD_INDICATOR, TAppr.MAKER, TAppr.VERIFIER," +
				"TAppr.INTERNAL_STATUS, "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION,TAppr.INTEREST_BASIS_NT,TAppr.INTEREST_BASIS " +
				" From FTP_METHODS TAppr ");
			String strWhereNotExists = new String( " Not Exists (Select * From FTP_METHODS_PEND TPend" +
				" Where TPend.METHOD_REFERENCE = TAppr.METHOD_REFERENCE AND TPEND.REPRICING_FLAG = TAPPR.REPRICING_FLAG) ");
			StringBuffer strBufPending = new StringBuffer("Select TPend.METHOD_REFERENCE," +
				"TPend.METHOD_DESCRIPTION,TPend.FTP_CURVE_ID_AT,TPend.FTP_CURVE_ID," +
				"TPend.METHOD_TYPE_AT, TPend.METHOD_TYPE," +
				"TPend.METHOD_SUB_TYPE_AT, TPend.METHOD_SUB_TYPE," +
				"TPend.TENOR_TYPE_NT,TPend.FTP_TENOR_TYPE,TPend.METHOD_BAL_TYPE_NT,TPend.METHOD_BAL_TYPE,TPend.APPLY_RATE_NT,TPend.FTP_APPLY_RATE,TPend.ADDON_APPLY_RATE,TPend.LP_APPLY_RATE,TPend.FTP_MT_STATUS_NT,TPend.FTP_MT_STATUS,TPend.REPRICING_FLAG_AT,TPend.REPRICING_FLAG,TPend.LP_TENOR_TYPE,TPend.RECORD_INDICATOR_NT," +
				"TPend.RECORD_INDICATOR, TPend.MAKER, TPend.VERIFIER," +
				"TPend.INTERNAL_STATUS, "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION,TPend.INTEREST_BASIS_NT,TPend.INTEREST_BASIS " +
				" From FTP_METHODS_PEND TPend ");
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
					case "methodReference":
						CommonUtils.addToQuerySearch(" upper(TAppr.METHOD_REFERENCE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.METHOD_REFERENCE) "+ val, strBufPending, data.getJoinType());
						break;

					case "methodDescription":
						CommonUtils.addToQuerySearch(" upper(TAppr.METHOD_DESCRIPTION) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.METHOD_DESCRIPTION) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpCurveId":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_CURVE_ID) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_CURVE_ID) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpGroup":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_GROUP) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_GROUP) "+ val, strBufPending, data.getJoinType());
						break;

					case "methodType":
						CommonUtils.addToQuerySearch(" upper(TAppr.METHOD_TYPE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.METHOD_TYPE) "+ val, strBufPending, data.getJoinType());
						break;
					case "ftpTenorType":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_TENOR_TYPE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_TENOR_TYPE) "+ val, strBufPending, data.getJoinType());
						break;

					case "methodBalType":
						CommonUtils.addToQuerySearch(" upper(TAppr.METHOD_BAL_TYPE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.METHOD_BAL_TYPE) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpApplyRate":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_APPLY_RATE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_APPLY_RATE) "+ val, strBufPending, data.getJoinType());
						break;

					case "addonApplyRate":
						CommonUtils.addToQuerySearch(" upper(TAppr.ADDON_APPLY_RATE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.ADDON_APPLY_RATE) "+ val, strBufPending, data.getJoinType());
						break;

					case "lpApplyRate":
						CommonUtils.addToQuerySearch(" upper(TAppr.LP_APPLY_RATE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.LP_APPLY_RATE) "+ val, strBufPending, data.getJoinType());
						break;
						
					case "status":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_MT_STATUS) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_MT_STATUS) "+ val, strBufPending, data.getJoinType());
						break;
						
					case "repricingFlag":
						CommonUtils.addToQuerySearch(" upper(TAppr.REPRICING_FLAG) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.REPRICING_FLAG) "+ val, strBufPending, data.getJoinType());
						break;
						
					case "lpTenorType":
						CommonUtils.addToQuerySearch(" upper(TAppr.LP_TENOR_TYPE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.LP_TENOR_TYPE) "+ val, strBufPending, data.getJoinType());
						break;
						
					case "interestBasis":
						CommonUtils.addToQuerySearch(" upper(TAppr.INTEREST_BASIS) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.INTEREST_BASIS) "+ val, strBufPending, data.getJoinType());
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
			String orderBy=" Order By METHOD_REFERENCE";
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
	public List<FtpMethodsVb> getQueryResults(FtpMethodsVb dObj, int intStatus){

		List<FtpMethodsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		setServiceDefaults();
		String strQueryAppr = new String("Select TAppr.METHOD_REFERENCE," +
				"TAppr.METHOD_DESCRIPTION, TAppr.FTP_CURVE_ID_AT,TAppr.FTP_CURVE_ID," +
				"TAppr.METHOD_TYPE_AT, TAppr.METHOD_TYPE," +
				"TAppr.METHOD_SUB_TYPE_AT, TAppr.METHOD_SUB_TYPE," +
				"TAppr.TENOR_TYPE_NT,TAppr.FTP_TENOR_TYPE,TAppr.METHOD_BAL_TYPE_NT,TAppr.METHOD_BAL_TYPE,TAppr.APPLY_RATE_NT,TAppr.FTP_APPLY_RATE,TAppr.ADDON_APPLY_RATE,TAppr.LP_APPLY_RATE,TAppr.FTP_MT_STATUS_NT,TAppr.FTP_MT_STATUS,TAppr.REPRICING_FLAG_AT,TAppr.REPRICING_FLAG,TAppr.LP_TENOR_TYPE,TAppr.RECORD_INDICATOR_NT," +
				"TAppr.RECORD_INDICATOR, TAppr.MAKER, TAppr.VERIFIER," +
				"TAppr.INTERNAL_STATUS, "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
				dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION,TAppr.INTEREST_BASIS_NT,TAppr.INTEREST_BASIS " +
				" From FTP_METHODS TAppr "+
				"Where TAppr.METHOD_REFERENCE = ? AND TAppr.REPRICING_FLAG = ? ");
			String strQueryPend = new String("Select TPend.METHOD_REFERENCE," +
					"TPend.METHOD_DESCRIPTION,TPend.FTP_CURVE_ID_AT,TPend.FTP_CURVE_ID," +
					"TPend.METHOD_TYPE_AT, TPend.METHOD_TYPE," +
					"TPend.METHOD_SUB_TYPE_AT, TPend.METHOD_SUB_TYPE," +
					"TPend.TENOR_TYPE_NT,TPend.FTP_TENOR_TYPE,TPend.METHOD_BAL_TYPE_NT,TPend.METHOD_BAL_TYPE,TPend.APPLY_RATE_NT,TPend.FTP_APPLY_RATE,TPend.ADDON_APPLY_RATE,TPend.LP_APPLY_RATE,TPend.FTP_MT_STATUS_NT,TPend.FTP_MT_STATUS,TPend.REPRICING_FLAG_AT,TPend.REPRICING_FLAG,TPend.LP_TENOR_TYPE,TPend.RECORD_INDICATOR_NT," +
					"TPend.RECORD_INDICATOR, TPend.MAKER, TPend.VERIFIER," +
					"TPend.INTERNAL_STATUS, "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED," +
					dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION,TPend.INTEREST_BASIS_NT,TPend.INTEREST_BASIS" +
					" From FTP_METHODS_PEND TPend "+
			"Where TPend.METHOD_REFERENCE = ? AND TPend.REPRICING_FLAG = ? ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getMethodReference());
		objParams[1] = new String(dObj.getRepricingFlag());

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
	protected List<FtpMethodsVb> selectApprovedRecord(FtpMethodsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}
	@Override
	protected List<FtpMethodsVb> doSelectPendingRecord(FtpMethodsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}
	@Override
	protected int getStatus(FtpMethodsVb records){return records.getStatus();}
	@Override
	protected void setStatus(FtpMethodsVb vObject,int status){vObject.setStatus(status);}
	@Override
	protected int doInsertionAppr(FtpMethodsVb vObject){
		String query = "Insert Into FTP_METHODS ( METHOD_REFERENCE, METHOD_DESCRIPTION,FTP_CURVE_ID_AT,FTP_CURVE_ID,"+
			"METHOD_TYPE_AT, METHOD_TYPE,METHOD_SUB_TYPE_AT, METHOD_SUB_TYPE, TENOR_TYPE_NT, FTP_TENOR_TYPE,METHOD_BAL_TYPE_NT,METHOD_BAL_TYPE,APPLY_RATE_NT,FTP_APPLY_RATE,ADDON_APPLY_RATE,LP_APPLY_RATE,FTP_MT_STATUS_NT,FTP_MT_STATUS,REPRICING_FLAG_AT,REPRICING_FLAG,LP_TENOR_TYPE,RECORD_INDICATOR_NT,RECORD_INDICATOR,"+
			"MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION,INTEREST_BASIS_NT,INTEREST_BASIS) Values (?, ?, ?, ?, ?, ?,?,?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ "?, ?, ?,"+systemDate+","+systemDate+",?,?)";
		Object[] args = {vObject.getMethodReference(), vObject.getMethodDescription(),vObject.getFtpCurveIdAt(),vObject.getFtpCurveId(), vObject.getMethodTypeAt(),
				vObject.getMethodType(), vObject.getMethodSubTypeAt(),
				vObject.getMethodSubType(), vObject.getFtpTenorTypeNt(),vObject.getFtpTenorType(),vObject.getMethodBalTypeNt(),vObject.getMethodBalType(),vObject.getFtpApplyRateNt(),vObject.getFtpApplyRate(),
				vObject.getAddonApplyRate(),vObject.getLpApplyRate(),
				vObject.getStatusNt(),vObject.getStatus(),vObject.getRepricingFlagAt(),vObject.getRepricingFlag(),
				vObject.getLpTenorType(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getInterestBasisNt(),vObject.getInterestBasis()};  
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doInsertionPend(FtpMethodsVb vObject){
		String query = "Insert Into FTP_METHODS_PEND ( METHOD_REFERENCE, METHOD_DESCRIPTION,FTP_CURVE_ID_AT,FTP_CURVE_ID,"+
			" METHOD_TYPE_AT, METHOD_TYPE, METHOD_SUB_TYPE_AT, METHOD_SUB_TYPE, TENOR_TYPE_NT, FTP_TENOR_TYPE,METHOD_BAL_TYPE_NT,METHOD_BAL_TYPE,APPLY_RATE_NT,FTP_APPLY_RATE,ADDON_APPLY_RATE,LP_APPLY_RATE,FTP_MT_STATUS_NT,FTP_MT_STATUS,REPRICING_FLAG_AT,REPRICING_FLAG,LP_TENOR_TYPE,RECORD_INDICATOR_NT,RECORD_INDICATOR,"+
			" MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION,INTEREST_BASIS_NT,INTEREST_BASIS) "+
			" Values (?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"+systemDate+","+systemDate+",?,?)";
		Object[] args ={vObject.getMethodReference(), vObject.getMethodDescription(),vObject.getFtpCurveIdAt(),vObject.getFtpCurveId(), vObject.getMethodTypeAt(),
				vObject.getMethodType(), vObject.getMethodSubTypeAt(),
				vObject.getMethodSubType(),vObject.getFtpTenorTypeNt(),vObject.getFtpTenorType(),vObject.getMethodBalTypeNt(),vObject.getMethodBalType(),vObject.getFtpApplyRateNt(),vObject.getFtpApplyRate(),
				vObject.getAddonApplyRate(),vObject.getLpApplyRate(),
				vObject.getStatusNt(),vObject.getStatus(),vObject.getRepricingFlagAt(),vObject.getRepricingFlag(),
				vObject.getLpTenorType(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getInterestBasisNt(),vObject.getInterestBasis()}; 
		return getJdbcTemplate().update(query,args);	
	}
	@Override
	protected int doInsertionPendWithDc(FtpMethodsVb vObject){
		String query = "Insert Into FTP_METHODS_PEND ( METHOD_REFERENCE, METHOD_DESCRIPTION,FTP_CURVE_ID_AT,FTP_CURVE_ID,"+
			" METHOD_TYPE_AT, METHOD_TYPE,  METHOD_SUB_TYPE_AT, METHOD_SUB_TYPE, TENOR_TYPE_NT,FTP_TENOR_TYPE,METHOD_BAL_TYPE_NT,METHOD_BAL_TYPE,APPLY_RATE_NT,FTP_APPLY_RATE,ADDON_APPLY_RATE,LP_APPLY_RATE,FTP_MT_STATUS_NT,FTP_MT_STATUS,REPRICING_FLAG_AT,REPRICING_FLAG,LP_TENOR_TYPE,RECORD_INDICATOR_NT, RECORD_INDICATOR,"+
			" MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION,INTEREST_BASIS_NT,INTEREST_BASIS) "+
			" Values (?, ?, ?, ?, ?,?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+","+dateTimeConvert+",?,?)";
		Object[] args = {vObject.getMethodReference(), vObject.getMethodDescription(),vObject.getFtpCurveIdAt(),vObject.getFtpCurveId(), vObject.getMethodTypeAt(),
				vObject.getMethodType(),vObject.getMethodSubTypeAt(),
				vObject.getMethodSubType(), vObject.getFtpTenorTypeNt(),vObject.getFtpTenorType(),vObject.getMethodBalTypeNt(),vObject.getMethodBalType(),vObject.getFtpApplyRateNt(),vObject.getFtpApplyRate(),
				vObject.getAddonApplyRate(),vObject.getLpApplyRate(),
				vObject.getStatusNt(),vObject.getStatus(),vObject.getRepricingFlagAt(),vObject.getRepricingFlag(),
				vObject.getLpTenorType(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(), vObject.getDateCreation(),vObject.getInterestBasisNt(),vObject.getInterestBasis()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doUpdateAppr(FtpMethodsVb vObject){
		String query = "Update FTP_METHODS Set METHOD_DESCRIPTION = ?,FTP_CURVE_ID_AT = ?,FTP_CURVE_ID = ?, " +
				"METHOD_TYPE_AT = ?, METHOD_TYPE = ?, METHOD_SUB_TYPE_AT=?, METHOD_SUB_TYPE=?, TENOR_TYPE_NT = ?,FTP_TENOR_TYPE = ?,METHOD_BAL_TYPE_NT = ?,METHOD_BAL_TYPE = ?,APPLY_RATE_NT = ?,FTP_APPLY_RATE = ?,ADDON_APPLY_RATE = ?,LP_APPLY_RATE= ?,FTP_MT_STATUS_NT = ?,FTP_MT_STATUS = ?,LP_TENOR_TYPE = ?,RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, " +
				"INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+", INTEREST_BASIS = ? Where METHOD_REFERENCE = ? AND REPRICING_FLAG = ?";
		Object[] args = {vObject.getMethodDescription(),vObject.getFtpCurveIdAt(),vObject.getFtpCurveId(),vObject.getMethodTypeAt(),
				vObject.getMethodType(), vObject.getMethodSubTypeAt(),
				vObject.getMethodSubType(),vObject.getFtpTenorTypeNt(),vObject.getFtpTenorType(),vObject.getMethodBalTypeNt(),vObject.getMethodBalType(),vObject.getFtpApplyRateNt(),vObject.getFtpApplyRate(),
				vObject.getAddonApplyRate(),vObject.getLpApplyRate(),
				vObject.getStatusNt(),vObject.getStatus(),
				vObject.getLpTenorType(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getInterestBasis(),vObject.getMethodReference(),vObject.getRepricingFlag()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doUpdatePend(FtpMethodsVb vObject){
		String query = "Update FTP_METHODS_PEND Set  METHOD_DESCRIPTION = ?,FTP_CURVE_ID_AT = ?,FTP_CURVE_ID = ?, " +
				"METHOD_TYPE_AT = ?, METHOD_TYPE = ?,  METHOD_SUB_TYPE_AT=?, METHOD_SUB_TYPE=?,TENOR_TYPE_NT = ?,FTP_TENOR_TYPE = ?,METHOD_BAL_TYPE_NT = ?,METHOD_BAL_TYPE = ?,APPLY_RATE_NT = ?,FTP_APPLY_RATE = ?,ADDON_APPLY_RATE_NT = ?,ADDON_APPLY_RATE = ?,LP_APPLY_RATE_NT = ?,LP_APPLY_RATE= ?,FTP_MT_STATUS_NT = ?,FTP_MT_STATUS = ?,LP_TENOR_TYPE = ?,RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, " +
				"INTERNAL_STATUS = ?, DATE_LAST_MODIFIED = "+systemDate+", INTEREST_BASIS = ? Where METHOD_REFERENCE = ? AND REPRICING_FLAG = ?";
		Object[] args = {vObject.getMethodDescription(),vObject.getFtpCurveIdAt(),vObject.getFtpCurveId(),vObject.getMethodTypeAt(),
				vObject.getMethodType(), vObject.getMethodSubTypeAt(),
				vObject.getMethodSubType(),vObject.getFtpTenorTypeNt(),vObject.getFtpTenorType(),vObject.getMethodBalTypeNt(),vObject.getMethodBalType(),vObject.getFtpApplyRateNt(),vObject.getFtpApplyRate(),
				vObject.getAddonApplyRate(),vObject.getLpApplyRate(),
				vObject.getStatusNt(),vObject.getStatus(),
				vObject.getLpTenorType(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getInterestBasis(),vObject.getMethodReference(),vObject.getRepricingFlag()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doDeleteAppr(FtpMethodsVb vObject){
		String query = "Delete From FTP_METHODS Where METHOD_REFERENCE= ? AND REPRICING_FLAG = ?";
		Object[] args = {vObject.getMethodReference(),vObject.getRepricingFlag()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int deletePendingRecord(FtpMethodsVb vObject){
		String query = "Delete From FTP_METHODS_PEND Where METHOD_REFERENCE = ? AND REPRICING_FLAG = ?";
		Object[] args = {vObject.getMethodReference(),vObject.getRepricingFlag()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected String frameErrorMessage(FtpMethodsVb vObject, String strOperation){
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try{
			
			strErrMsg =  strErrMsg + "FTP_METHODS:" + vObject.getMethodReference();
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
	protected String getAuditString(FtpMethodsVb vObject){
		StringBuffer strAudit = new StringBuffer("");
		strAudit.append(vObject.getMethodReference().trim());
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodDescription());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpCurveIdAt());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpCurveId());
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodTypeAt());
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodType());
		strAudit.append("!|#");
	
		strAudit.append(vObject.getFtpTenorTypeNt());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpTenorType());
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodBalTypeNt());
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodBalType());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpApplyRateNt());
		strAudit.append("!|#");
		strAudit.append(vObject.getFtpApplyRate());
		strAudit.append("!|#");
		strAudit.append(vObject.getAddonApplyRate());
		strAudit.append("!|#");
		strAudit.append(vObject.getLpApplyRate());
		strAudit.append("!|#");
		
		strAudit.append(vObject.getStatusNt());
		strAudit.append("!|#");
		strAudit.append(vObject.getStatus());
		strAudit.append("!|#");
		strAudit.append(vObject.getRepricingFlagAt());
		strAudit.append("!|#");
		strAudit.append(vObject.getRepricingFlag());
		strAudit.append("!|#");
		strAudit.append(vObject.getLpTenorType());
		strAudit.append("!|#");
		strAudit.append(vObject.getInterestBasisNt());
		strAudit.append("!|#");
		strAudit.append(vObject.getInterestBasis());
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
		strAudit.append(vObject.getMethodSubTypeAt());
		strAudit.append("!|#");
		strAudit.append(vObject.getMethodSubType());
		strAudit.append("!|#");
		return strAudit.toString();
	}
	@Override
	protected void setServiceDefaults(){
		serviceName = "FtpMethods";
		serviceDesc = "FTP Methods";//CommonUtils.getResourceManger().getString("ftpMethods");//"GL Enrich Ids";
		tableName = "FTP_METHODS";
		childTableName = "FTP_METHODS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
}