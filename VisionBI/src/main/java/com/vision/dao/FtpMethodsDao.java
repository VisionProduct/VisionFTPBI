package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.SmartSearchVb;

@Component
public class FtpMethodsDao extends AbstractDao<FtpMethodsVb> {

/*******Mapper Start**********/
	String CountryApprDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TAppr.COUNTRY) COUNTRY_DESC";
	String CountryPendDesc = "(SELECT COUNTRY_DESCRIPTION FROM COUNTRIES WHERE COUNTRY = TPend.COUNTRY) COUNTRY_DESC";
	String LeBookApprDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TAppr.LE_BOOK AND COUNTRY = TAppr.COUNTRY ) LE_BOOK_DESC";
	String LeBookTPendDesc = "(SELECT  LEB_DESCRIPTION FROM LE_BOOK WHERE  LE_BOOK = TPend.LE_BOOK AND COUNTRY = TPend.COUNTRY ) LE_BOOK_DESC";

	String RepricingFlagAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 1304, "TAppr.REPRICING_FLAG", "REPRICING_FLAG_DESC");
	String RepricingFlagAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 1304, "TPend.REPRICING_FLAG", "REPRICING_FLAG_DESC");

	String MethodTypeAtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 1303, "TAppr.METHOD_TYPE", "METHOD_TYPE_DESC");
	String MethodTypeAtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("AT", 1303, "TPend.METHOD_TYPE", "METHOD_TYPE_DESC");

	String TenorTypeNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1302, "TAppr.FTP_TENOR_TYPE", "FTP_TENOR_TYPE_DESC");
	String TenorTypeNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1302, "TPend.FTP_TENOR_TYPE", "FTP_TENOR_TYPE_DESC");

	String MethodBalTypeNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1301, "TAppr.METHOD_BAL_TYPE", "METHOD_BAL_TYPE_DESC");
	String MethodBalTypeNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1301, "TPend.METHOD_BAL_TYPE", "METHOD_BAL_TYPE_DESC");

	String InterestBasisNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 6, "TAppr.INTEREST_BASIS", "INTEREST_BASIS_DESC");
	String InterestBasisNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 6, "TPend.INTEREST_BASIS", "INTEREST_BASIS_DESC");

	String ApplyRateNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1304, "TAppr.FTP_APPLY_RATE", "FTP_APPLY_RATE_DESC");
	String ApplyRateNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1304, "TPend.FTP_APPLY_RATE", "FTP_APPLY_RATE_DESC");

	String FtpMtStatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_MT_STATUS", "FTP_MT_STATUS_DESC");
	String FtpMtStatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_MT_STATUS", "FTP_MT_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpMethodsVb vObject = new FtpMethodsVb();
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
				if(rs.getString("FTP_SUB_GROUP_ID")!= null){ 
					vObject.setFtpSubGroupId(rs.getString("FTP_SUB_GROUP_ID"));
				}else{
					vObject.setFtpSubGroupId("");
				}
				vObject.setRepricingFlagAt(rs.getInt("REPRICING_FLAG_AT"));
				if(rs.getString("REPRICING_FLAG")!= null){ 
					vObject.setRepricingFlag(rs.getString("REPRICING_FLAG"));
				}else{
					vObject.setRepricingFlag("");
				}
				if(rs.getString("REPRICING_FLAG_DESC")!= null){ 
					vObject.setRepricingFlagDesc(rs.getString("REPRICING_FLAG_DESC"));
				}
				vObject.setMethodTypeAt(rs.getInt("METHOD_TYPE_AT"));
				if(rs.getString("METHOD_TYPE")!= null){ 
					vObject.setMethodType(rs.getString("METHOD_TYPE"));
				}else{
					vObject.setMethodType("");
				}
				if(rs.getString("METHOD_TYPE_DESC")!= null){ 
					vObject.setMethodTypeDesc(rs.getString("METHOD_TYPE_DESC"));
				}
				vObject.setTenorTypeNt(rs.getInt("TENOR_TYPE_NT"));
				vObject.setFtpTenorType(rs.getInt("FTP_TENOR_TYPE"));
				if(rs.getString("FTP_TENOR_TYPE_DESC")!= null){ 
					vObject.setFtpTenorTypeDesc(rs.getString("FTP_TENOR_TYPE_DESC"));
				}
				vObject.setMethodBalTypeNt(rs.getInt("METHOD_BAL_TYPE_NT"));
				vObject.setMethodBalType(rs.getInt("METHOD_BAL_TYPE"));
				if(rs.getString("METHOD_BAL_TYPE_DESC")!= null){ 
					vObject.setMethodBalTypeDesc(rs.getString("METHOD_BAL_TYPE_DESC"));
				}
				vObject.setInterestBasisNt(rs.getInt("INTEREST_BASIS_NT"));
				vObject.setInterestBasis(rs.getInt("INTEREST_BASIS"));
				
				if(rs.getString("INTEREST_BASIS_DESC")!= null){ 
					vObject.setInterestBasisDesc(rs.getString("INTEREST_BASIS_DESC"));
				}
				
				vObject.setApplyRateNt(rs.getInt("APPLY_RATE_NT"));
				vObject.setFtpApplyRate(rs.getInt("FTP_APPLY_RATE"));
				
				if(rs.getString("FTP_APPLY_RATE_DESC")!= null){ 
					vObject.setFtpApplyRateDesc(rs.getString("FTP_APPLY_RATE_DESC"));
				}
				
				if(rs.getString("FTP_CURVE_ID")!= null){ 
					vObject.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				}else{
					vObject.setFtpCurveId("");
				}
				vObject.setFtpMtStatusNt(rs.getInt("FTP_MT_STATUS_NT"));
				vObject.setFtpMtStatus(rs.getInt("FTP_MT_STATUS"));
				
				if(rs.getString("FTP_MT_STATUS_DESC")!= null){ 
					vObject.setStatusDesc(rs.getString("FTP_MT_STATUS_DESC"));
				}
				
				vObject.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				vObject.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				
				if(rs.getString("RECORD_INDICATOR_DESC")!= null){ 
					vObject.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				}
				
				vObject.setMaker(rs.getInt("MAKER"));
				if(rs.getString("MAKER_NAME")!= null){ 
					vObject.setMakerName(rs.getString("MAKER_NAME"));
				}
				vObject.setVerifier(rs.getInt("VERIFIER"));
				if(rs.getString("VERIFIER_NAME")!= null){ 
					vObject.setVerifierName(rs.getString("VERIFIER_NAME"));
				}
				if(rs.getString("RECORD_INDICATOR_DESC")!= null){ 
					vObject.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				}
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
	public List<FtpMethodsVb> getQueryPopupResults(FtpMethodsVb dObj){
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_SUB_GROUP_ID"
			+ ",TAppr.REPRICING_FLAG_AT"
			+ ",TAppr.REPRICING_FLAG"
			+ ",TAppr.METHOD_TYPE_AT"
			+ ",TAppr.METHOD_TYPE"
			+ ",TAppr.TENOR_TYPE_NT"
			+ ",TAppr.FTP_TENOR_TYPE"
			+ ",TAppr.METHOD_BAL_TYPE_NT"
			+ ",TAppr.METHOD_BAL_TYPE"
			+ ",TAppr.INTEREST_BASIS_NT"
			+ ",TAppr.INTEREST_BASIS"
			+ ",TAppr.APPLY_RATE_NT"
			+ ",TAppr.FTP_APPLY_RATE"
			+ ",TAppr.FTP_CURVE_ID"
			+ ",TAppr.FTP_MT_STATUS_NT"
			+ ",TAppr.FTP_MT_STATUS"
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR"
			+ ",TAppr.MAKER"
			+ ",TAppr.VERIFIER"
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" from FTP_METHODS TAppr ");
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_METHODS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY "
				+ "AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID AND TAppr.REPRICING_FLAG = TPend.REPRICING_FLAG )");
		StringBuffer strBufPending = new StringBuffer("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_SUB_GROUP_ID"
			+ ",TPend.REPRICING_FLAG_AT"
			+ ",TPend.REPRICING_FLAG"
			+ ",TPend.METHOD_TYPE_AT"
			+ ",TPend.METHOD_TYPE"
			+ ",TPend.TENOR_TYPE_NT"
			+ ",TPend.FTP_TENOR_TYPE"
			+ ",TPend.METHOD_BAL_TYPE_NT"
			+ ",TPend.METHOD_BAL_TYPE"
			+ ",TPend.INTEREST_BASIS_NT"
			+ ",TPend.INTEREST_BASIS"
			+ ",TPend.APPLY_RATE_NT"
			+ ",TPend.FTP_APPLY_RATE"
			+ ",TPend.FTP_CURVE_ID"
			+ ",TPend.FTP_MT_STATUS_NT"
			+ ",TPend.FTP_MT_STATUS"
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR"
			+ ",TPend.MAKER"
			+ ",TPend.VERIFIER"
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" from FTP_METHODS_PEND TPend ");
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
					case "country":
						CommonUtils.addToQuerySearch(" upper(TAppr.COUNTRY) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.COUNTRY) "+ val, strBufPending, data.getJoinType());
						break;

					case "leBook":
						CommonUtils.addToQuerySearch(" upper(TAppr.LE_BOOK) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.LE_BOOK) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpSubGroupId":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_SUB_GROUP_ID) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_SUB_GROUP_ID) "+ val, strBufPending, data.getJoinType());
						break;

					case "repricingFlag":
						CommonUtils.addToQuerySearch(" upper(TAppr.REPRICING_FLAG) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.REPRICING_FLAG) "+ val, strBufPending, data.getJoinType());
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

					case "interestBasis":
						CommonUtils.addToQuerySearch(" upper(TAppr.INTEREST_BASIS) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.INTEREST_BASIS) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpApplyRate":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_APPLY_RATE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_APPLY_RATE) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpCurveId":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_CURVE_ID) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_CURVE_ID) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpMtStatus":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_MT_STATUS) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_MT_STATUS) "+ val, strBufPending, data.getJoinType());
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
			String orderBy=" Order By COUNTRY AND LE_BOOK AND FTP_SUB_GROUP_ID AND REPRICING_FLAG ";
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

	public List<FtpMethodsVb> getQueryResultsByParent(FTPGroupsVb dObj, int intStatus){
		setServiceDefaults();
		List<FtpMethodsVb> collTemp = null;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_SUB_GROUP_ID"
			+ ",TAppr.REPRICING_FLAG_AT"
			+ ",TAppr.REPRICING_FLAG, "+RepricingFlagAtApprDesc
			+ ",TAppr.METHOD_TYPE_AT"
			+ ",TAppr.METHOD_TYPE,"+MethodTypeAtApprDesc
			+ ",TAppr.TENOR_TYPE_NT"
			+ ",TAppr.FTP_TENOR_TYPE, "+TenorTypeNtApprDesc
			+ ",TAppr.METHOD_BAL_TYPE_NT"
			+ ",TAppr.METHOD_BAL_TYPE, "+MethodBalTypeNtApprDesc
			+ ",TAppr.INTEREST_BASIS_NT"
			+ ",TAppr.INTEREST_BASIS, "+InterestBasisNtApprDesc
			+ ",TAppr.APPLY_RATE_NT"
			+ ",TAppr.FTP_APPLY_RATE, "+ApplyRateNtApprDesc
			+ ",TAppr.FTP_CURVE_ID"
			+ ",TAppr.FTP_MT_STATUS_NT"
			+ ",TAppr.FTP_MT_STATUS, "+FtpMtStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_METHODS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ? ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_SUB_GROUP_ID"
			+ ",TPend.REPRICING_FLAG_AT"
			+ ",TPend.REPRICING_FLAG, "+RepricingFlagAtPendDesc
			+ ",TPend.METHOD_TYPE_AT"
			+ ",TPend.METHOD_TYPE, "+MethodTypeAtPendDesc
			+ ",TPend.TENOR_TYPE_NT"
			+ ",TPend.FTP_TENOR_TYPE, "+TenorTypeNtPendDesc
			+ ",TPend.METHOD_BAL_TYPE_NT"
			+ ",TPend.METHOD_BAL_TYPE, "+MethodBalTypeNtPendDesc
			+ ",TPend.INTEREST_BASIS_NT"
			+ ",TPend.INTEREST_BASIS, "+InterestBasisNtPendDesc
			+ ",TPend.APPLY_RATE_NT"
			+ ",TPend.FTP_APPLY_RATE, "+ApplyRateNtPendDesc
			+ ",TPend.FTP_CURVE_ID"
			+ ",TPend.FTP_MT_STATUS_NT"
			+ ",TPend.FTP_MT_STATUS, "+FtpMtStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_METHODS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?");
		final int intKeyFieldsCount = 3;
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpSubGroupId();
//		objParams[3] = dObj.getRepricingFlag();

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
	public List<FtpMethodsVb> getQueryResultsAllByParent(FTPGroupsVb dObj){
		setServiceDefaults();
		List<FtpMethodsVb> collTemp = null;
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_SUB_GROUP_ID"
			+ ",TAppr.REPRICING_FLAG_AT"
			+ ",TAppr.REPRICING_FLAG, "+RepricingFlagAtApprDesc
			+ ",TAppr.METHOD_TYPE_AT"
			+ ",TAppr.METHOD_TYPE,"+MethodTypeAtApprDesc
			+ ",TAppr.TENOR_TYPE_NT"
			+ ",TAppr.FTP_TENOR_TYPE, "+TenorTypeNtApprDesc
			+ ",TAppr.METHOD_BAL_TYPE_NT"
			+ ",TAppr.METHOD_BAL_TYPE, "+MethodBalTypeNtApprDesc
			+ ",TAppr.INTEREST_BASIS_NT"
			+ ",TAppr.INTEREST_BASIS, "+InterestBasisNtApprDesc
			+ ",TAppr.APPLY_RATE_NT"
			+ ",TAppr.FTP_APPLY_RATE, "+ApplyRateNtApprDesc
			+ ",TAppr.FTP_CURVE_ID"
			+ ",TAppr.FTP_MT_STATUS_NT"
			+ ",TAppr.FTP_MT_STATUS, "+FtpMtStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_METHODS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ? ");
		StringBuffer strQueryPend = new StringBuffer("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_SUB_GROUP_ID"
			+ ",TPend.REPRICING_FLAG_AT"
			+ ",TPend.REPRICING_FLAG, "+RepricingFlagAtPendDesc
			+ ",TPend.METHOD_TYPE_AT"
			+ ",TPend.METHOD_TYPE, "+MethodTypeAtPendDesc
			+ ",TPend.TENOR_TYPE_NT"
			+ ",TPend.FTP_TENOR_TYPE, "+TenorTypeNtPendDesc
			+ ",TPend.METHOD_BAL_TYPE_NT"
			+ ",TPend.METHOD_BAL_TYPE, "+MethodBalTypeNtPendDesc
			+ ",TPend.INTEREST_BASIS_NT"
			+ ",TPend.INTEREST_BASIS, "+InterestBasisNtPendDesc
			+ ",TPend.APPLY_RATE_NT"
			+ ",TPend.FTP_APPLY_RATE, "+ApplyRateNtPendDesc
			+ ",TPend.FTP_CURVE_ID"
			+ ",TPend.FTP_MT_STATUS_NT"
			+ ",TPend.FTP_MT_STATUS, "+FtpMtStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_METHODS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?");
		String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_METHODS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY "
				+ "AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID AND TAppr.REPRICING_FLAG = TPend.REPRICING_FLAG )");
		String orderBy=" Order By COUNTRY, LE_BOOK,FTP_SUB_GROUP_ID, REPRICING_FLAG ";
		Object objParams[]=null;
		try{
/*			if(intStatus == 0){
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strQueryAppr.toString(),objParams,getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),objParams,getMapper());
			}*/
			
			Vector<Object> params = new Vector<Object>();
			params.addElement(dObj.getCountry());
			params.addElement(dObj.getLeBook());
			params.addElement(dObj.getFtpSubGroupId());
			int Ctr = 0;
			int Ctr2 = 0;
			String query = "";
			if(dObj.isVerificationRequired()){
				objParams = new Object[params.size()*2];
				for(Ctr=0; Ctr < params.size(); Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr);
				for(Ctr2=0 ; Ctr2 < params.size(); Ctr2++, Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr2);
				
				strBufApprove.append(" AND "+strWhereNotExists);
				strQueryPend.append(orderBy);
				
				query = strBufApprove.toString() + " Union " + strQueryPend.toString();
				
			}else{
				objParams = new Object[params.size()];
				for(Ctr=0; Ctr < params.size(); Ctr++)
					objParams[Ctr] = (Object) params.elementAt(Ctr);
				strBufApprove.append(orderBy);
				query = strBufApprove.toString();
			}
			collTemp = getJdbcTemplate().query(query,objParams,getMapper());
			return collTemp;
		}catch(Exception ex){
				ex.printStackTrace();
				logger.error("Error: getQueryResults Exception :   ");
				if (objParams != null)
				for(int i=0 ; i< objParams.length; i++)
					logger.error("objParams[" + i + "]" + objParams[i].toString());
				return null;
				}
	}
	public List<FtpMethodsVb> getQueryResults(FtpMethodsVb dObj, int intStatus){
		setServiceDefaults();
		List<FtpMethodsVb> collTemp = null;

		final int intKeyFieldsCount = 4;
		String strQueryAppr = new String("Select TAppr.COUNTRY"
			+ ",TAppr.LE_BOOK"
			+ ",TAppr.FTP_SUB_GROUP_ID"
			+ ",TAppr.REPRICING_FLAG_AT"
			+ ",TAppr.REPRICING_FLAG,"+RepricingFlagAtApprDesc
			+ ",TAppr.METHOD_TYPE_AT"
			+ ",TAppr.METHOD_TYPE, "+MethodTypeAtApprDesc
			+ ",TAppr.TENOR_TYPE_NT"
			+ ",TAppr.FTP_TENOR_TYPE, "+TenorTypeNtApprDesc
			+ ",TAppr.METHOD_BAL_TYPE_NT"
			+ ",TAppr.METHOD_BAL_TYPE, "+MethodBalTypeNtApprDesc
			+ ",TAppr.INTEREST_BASIS_NT"
			+ ",TAppr.INTEREST_BASIS, "+InterestBasisNtApprDesc
			+ ",TAppr.APPLY_RATE_NT"
			+ ",TAppr.FTP_APPLY_RATE, "+ApplyRateNtApprDesc
			+ ",TAppr.FTP_CURVE_ID"
			+ ",TAppr.FTP_MT_STATUS_NT"
			+ ",TAppr.FTP_MT_STATUS, "+FtpMtStatusNtApprDesc
			+ ",TAppr.RECORD_INDICATOR_NT"
			+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
			+ ",TAppr.MAKER, "+makerApprDesc
			+ ",TAppr.VERIFIER, "+verifierApprDesc
			+ ",TAppr.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TAppr.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TAppr.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_METHODS TAppr WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND REPRICING_FLAG = ?  ");
		String strQueryPend = new String("Select TPend.COUNTRY"
			+ ",TPend.LE_BOOK"
			+ ",TPend.FTP_SUB_GROUP_ID"
			+ ",TPend.REPRICING_FLAG_AT"
			+ ",TPend.REPRICING_FLAG, "+RepricingFlagAtPendDesc
			+ ",TPend.METHOD_TYPE_AT"
			+ ",TPend.METHOD_TYPE, "+MethodTypeAtPendDesc
			+ ",TPend.TENOR_TYPE_NT"
			+ ",TPend.FTP_TENOR_TYPE, "+TenorTypeNtPendDesc
			+ ",TPend.METHOD_BAL_TYPE_NT"
			+ ",TPend.METHOD_BAL_TYPE, "+MethodBalTypeNtPendDesc
			+ ",TPend.INTEREST_BASIS_NT"
			+ ",TPend.INTEREST_BASIS, "+InterestBasisNtPendDesc
			+ ",TPend.APPLY_RATE_NT"
			+ ",TPend.FTP_APPLY_RATE, "+ApplyRateNtPendDesc
			+ ",TPend.FTP_CURVE_ID"
			+ ",TPend.FTP_MT_STATUS_NT"
			+ ",TPend.FTP_MT_STATUS, "+FtpMtStatusNtPendDesc
			+ ",TPend.RECORD_INDICATOR_NT"
			+ ",TPend.RECORD_INDICATOR, "+RecordIndicatorNtPendDesc
			+ ",TPend.MAKER, "+makerPendDesc
			+ ",TPend.VERIFIER, "+verifierPendDesc
			+ ",TPend.INTERNAL_STATUS"
			+ ", "+dateFormat+"(TPend.DATE_LAST_MODIFIED, "+dateFormatStr+") DATE_LAST_MODIFIED"
			+ ", "+dateFormat+"(TPend.DATE_CREATION, "+dateFormatStr+") DATE_CREATION"+
			" From FTP_METHODS_PEND TPend WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND REPRICING_FLAG = ?   ");

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getFtpSubGroupId();
		objParams[3] = dObj.getRepricingFlag();

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
	protected List<FtpMethodsVb> selectApprovedRecord(FtpMethodsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_ZERO);
	}


	@Override
	protected List<FtpMethodsVb> doSelectPendingRecord(FtpMethodsVb vObject){
		return getQueryResults(vObject, Constants.STATUS_PENDING);
	}


	@Override
	protected int getStatus(FtpMethodsVb records){return records.getFtpMtStatus();}


	@Override
	protected void setStatus(FtpMethodsVb vObject,int status){vObject.setFtpMtStatus(status);}


	@Override
	protected int doInsertionAppr(FtpMethodsVb vObject){
		String query =	"Insert Into FTP_METHODS (COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, REPRICING_FLAG_AT, "
				+ "REPRICING_FLAG, METHOD_TYPE_AT, METHOD_TYPE, TENOR_TYPE_NT, FTP_TENOR_TYPE, METHOD_BAL_TYPE_NT, "
				+ "METHOD_BAL_TYPE, INTEREST_BASIS_NT, INTEREST_BASIS, APPLY_RATE_NT, FTP_APPLY_RATE, FTP_CURVE_ID, "
				+ "FTP_MT_STATUS_NT, FTP_MT_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, "
				+ "INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"+ 
		 "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")"; 
		Object[] args = {vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getRepricingFlagAt(), vObject.getRepricingFlag(), vObject.getMethodTypeAt(), vObject.getMethodType(), vObject.getTenorTypeNt(), vObject.getFtpTenorType(), vObject.getMethodBalTypeNt(), vObject.getMethodBalType(), vObject.getInterestBasisNt(), vObject.getInterestBasis(), vObject.getApplyRateNt(), vObject.getFtpApplyRate(), vObject.getFtpCurveId(), vObject.getFtpMtStatusNt(), vObject.getFtpMtStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };

		 return getJdbcTemplate().update(query,args);
	}


	@Override
	protected int doInsertionPend(FtpMethodsVb vObject){
		String query =	"Insert Into FTP_METHODS_PEND (COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, REPRICING_FLAG_AT, "
				+ "REPRICING_FLAG, METHOD_TYPE_AT, METHOD_TYPE, TENOR_TYPE_NT, FTP_TENOR_TYPE, METHOD_BAL_TYPE_NT, "
				+ "METHOD_BAL_TYPE, INTEREST_BASIS_NT, INTEREST_BASIS, APPLY_RATE_NT, FTP_APPLY_RATE, FTP_CURVE_ID, "
				+ "FTP_MT_STATUS_NT, FTP_MT_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, "
				+ "INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"+ 
		 "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")"; 
		Object[] args = {vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getRepricingFlagAt(), vObject.getRepricingFlag(), vObject.getMethodTypeAt(), vObject.getMethodType(), vObject.getTenorTypeNt(), vObject.getFtpTenorType(), vObject.getMethodBalTypeNt(), vObject.getMethodBalType(), vObject.getInterestBasisNt(), vObject.getInterestBasis(), vObject.getApplyRateNt(), vObject.getFtpApplyRate(), vObject.getFtpCurveId(), vObject.getFtpMtStatusNt(), vObject.getFtpMtStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus() };

		 return getJdbcTemplate().update(query,args);
	}


	@Override
	protected int doInsertionPendWithDc(FtpMethodsVb vObject) {
		String query = "Insert Into FTP_METHODS_PEND (COUNTRY, LE_BOOK, FTP_SUB_GROUP_ID, "
				+ "REPRICING_FLAG_AT, REPRICING_FLAG, METHOD_TYPE_AT, METHOD_TYPE, TENOR_TYPE_NT, "
				+ "FTP_TENOR_TYPE, METHOD_BAL_TYPE_NT, METHOD_BAL_TYPE, INTEREST_BASIS_NT,"
				+ " INTEREST_BASIS, APPLY_RATE_NT, FTP_APPLY_RATE, FTP_CURVE_ID, FTP_MT_STATUS_NT, FTP_MT_STATUS, RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION)"
				+ "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "
				+ ""+dateTimeConvert+")";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(),
				vObject.getRepricingFlagAt(), vObject.getRepricingFlag(), vObject.getMethodTypeAt(),
				vObject.getMethodType(), vObject.getTenorTypeNt(), vObject.getFtpTenorType(),
				vObject.getMethodBalTypeNt(), vObject.getMethodBalType(), vObject.getInterestBasisNt(),
				vObject.getInterestBasis(), vObject.getApplyRateNt(), vObject.getFtpApplyRate(),
				vObject.getFtpCurveId(), vObject.getFtpMtStatusNt(), vObject.getFtpMtStatus(),
				vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),
				vObject.getInternalStatus(), vObject.getDateCreation() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doUpdateAppr(FtpMethodsVb vObject) {
		String query = "Update FTP_METHODS Set REPRICING_FLAG_AT = ?, METHOD_TYPE_AT = ?, METHOD_TYPE = ?, TENOR_TYPE_NT = ?, FTP_TENOR_TYPE = ?, METHOD_BAL_TYPE_NT = ?, "
				+ "METHOD_BAL_TYPE = ?, INTEREST_BASIS_NT = ?, INTEREST_BASIS = ?, APPLY_RATE_NT = ?, FTP_APPLY_RATE = ?, FTP_CURVE_ID = ?, FTP_MT_STATUS_NT = ?, FTP_MT_STATUS = ?"
				+ ", RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED =  "+systemDate
				+ " WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND REPRICING_FLAG = ? ";
		Object[] args = { vObject.getRepricingFlagAt(), vObject.getMethodTypeAt(), vObject.getMethodType(),
				vObject.getTenorTypeNt(), vObject.getFtpTenorType(), vObject.getMethodBalTypeNt(),
				vObject.getMethodBalType(), vObject.getInterestBasisNt(), vObject.getInterestBasis(),
				vObject.getApplyRateNt(), vObject.getFtpApplyRate(), vObject.getFtpCurveId(),
				vObject.getFtpMtStatusNt(), vObject.getFtpMtStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getRepricingFlag() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doUpdatePend(FtpMethodsVb vObject){
		String query = "Update FTP_METHODS_PEND Set REPRICING_FLAG_AT = ?, METHOD_TYPE_AT = ?, METHOD_TYPE = ?, TENOR_TYPE_NT = ?, FTP_TENOR_TYPE = ?, METHOD_BAL_TYPE_NT = ?, "
				+ "METHOD_BAL_TYPE = ?, INTEREST_BASIS_NT = ?, INTEREST_BASIS = ?, APPLY_RATE_NT = ?, FTP_APPLY_RATE = ?, FTP_CURVE_ID = ?, FTP_MT_STATUS_NT = ?, FTP_MT_STATUS = ?"
				+ ", RECORD_INDICATOR_NT = ?, RECORD_INDICATOR = ?, MAKER = ?, VERIFIER = ?, INTERNAL_STATUS = ?, DATE_LAST_MODIFIED =  "+systemDate
				+ " WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND REPRICING_FLAG = ? ";
		Object[] args = { vObject.getRepricingFlagAt(), vObject.getMethodTypeAt(), vObject.getMethodType(),
				vObject.getTenorTypeNt(), vObject.getFtpTenorType(), vObject.getMethodBalTypeNt(),
				vObject.getMethodBalType(), vObject.getInterestBasisNt(), vObject.getInterestBasis(),
				vObject.getApplyRateNt(), vObject.getFtpApplyRate(), vObject.getFtpCurveId(),
				vObject.getFtpMtStatusNt(), vObject.getFtpMtStatus(), vObject.getRecordIndicatorNt(),
				vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),
				vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(), vObject.getRepricingFlag() };

		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int doDeleteAppr(FtpMethodsVb vObject) {
		String query = "Delete From FTP_METHODS Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND REPRICING_FLAG = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(),
				vObject.getRepricingFlag() };
		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected int deletePendingRecord(FtpMethodsVb vObject) {
		String query = "Delete From FTP_METHODS_PEND Where COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ?  AND REPRICING_FLAG = ?  ";
		Object[] args = { vObject.getCountry(), vObject.getLeBook(), vObject.getFtpSubGroupId(),
				vObject.getRepricingFlag() };
		return getJdbcTemplate().update(query, args);
	}


	@Override
	protected String getAuditString(FtpMethodsVb vObject){
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

			if(ValidationUtil.isValid(vObject.getFtpSubGroupId()))
				strAudit.append("FTP_SUB_GROUP_ID"+auditDelimiterColVal+vObject.getFtpSubGroupId().trim());
			else
				strAudit.append("FTP_SUB_GROUP_ID"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("REPRICING_FLAG_AT"+auditDelimiterColVal+vObject.getRepricingFlagAt());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getRepricingFlag()))
				strAudit.append("REPRICING_FLAG"+auditDelimiterColVal+vObject.getRepricingFlag().trim());
			else
				strAudit.append("REPRICING_FLAG"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("METHOD_TYPE_AT"+auditDelimiterColVal+vObject.getMethodTypeAt());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getMethodType()))
				strAudit.append("METHOD_TYPE"+auditDelimiterColVal+vObject.getMethodType().trim());
			else
				strAudit.append("METHOD_TYPE"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("TENOR_TYPE_NT"+auditDelimiterColVal+vObject.getTenorTypeNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_TENOR_TYPE"+auditDelimiterColVal+vObject.getFtpTenorType());
			strAudit.append(auditDelimiter);

				strAudit.append("METHOD_BAL_TYPE_NT"+auditDelimiterColVal+vObject.getMethodBalTypeNt());
			strAudit.append(auditDelimiter);

				strAudit.append("METHOD_BAL_TYPE"+auditDelimiterColVal+vObject.getMethodBalType());
			strAudit.append(auditDelimiter);

				strAudit.append("INTEREST_BASIS_NT"+auditDelimiterColVal+vObject.getInterestBasisNt());
			strAudit.append(auditDelimiter);

				strAudit.append("INTEREST_BASIS"+auditDelimiterColVal+vObject.getInterestBasis());
			strAudit.append(auditDelimiter);

				strAudit.append("APPLY_RATE_NT"+auditDelimiterColVal+vObject.getApplyRateNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_APPLY_RATE"+auditDelimiterColVal+vObject.getFtpApplyRate());
			strAudit.append(auditDelimiter);

			if(ValidationUtil.isValid(vObject.getFtpCurveId()))
				strAudit.append("FTP_CURVE_ID"+auditDelimiterColVal+vObject.getFtpCurveId().trim());
			else
				strAudit.append("FTP_CURVE_ID"+auditDelimiterColVal+"NULL");
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_MT_STATUS_NT"+auditDelimiterColVal+vObject.getFtpMtStatusNt());
			strAudit.append(auditDelimiter);

				strAudit.append("FTP_MT_STATUS"+auditDelimiterColVal+vObject.getFtpMtStatus());
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
		serviceName = "FtpMethods";
		serviceDesc = "FTP methods";
		tableName = "FTP_METHODS";
		childTableName = "FTP_METHODS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
		
	}

	protected ExceptionCode doUpdateRecordForNonTrans(FtpMethodsVb vObject) throws RuntimeCustomException {
		List<FtpMethodsVb> collTemp = null;
		FtpMethodsVb vObjectlocal = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.MODIFY;
		strErrorDesc = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		// Search if record already exists in pending. If it already exists, check for
		// status
		collTemp = doSelectPendingRecord(vObject);
		if (collTemp == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		if (collTemp.size() > 0) {
			vObjectlocal = ((ArrayList<FtpMethodsVb>) collTemp).get(0);

			// Check if the record is pending for deletion. If so return the error
			if (vObjectlocal.getRecordIndicator() == Constants.STATUS_DELETE) {
				exceptionCode = getResultObject(Constants.RECORD_PENDING_FOR_DELETION);
				throw buildRuntimeCustomException(exceptionCode);
			}
			vObject.setDateCreation(vObjectlocal.getDateCreation());
			if (vObjectlocal.getRecordIndicator() == Constants.STATUS_INSERT) {
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_INSERT);
				retVal = doUpdatePend(vObject);
			} else {
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_UPDATE);
				retVal = doUpdatePend(vObject);
			}
			if (retVal != Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = getResultObject(retVal);
				throw buildRuntimeCustomException(exceptionCode);
			}
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		} else {
			collTemp = null;
			collTemp = selectApprovedRecord(vObject);

			if (collTemp == null) {
				exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
				throw buildRuntimeCustomException(exceptionCode);
			}

			// Even if record is not there in Appr. table reject the record
			if (collTemp.size() == 0) {
//				exceptionCode = getResultObject(Constants.ATTEMPT_TO_MODIFY_UNEXISTING_RECORD);
//				throw buildRuntimeCustomException(exceptionCode);
				// Try inserting the record
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_INSERT);
				String systemDate = getSystemDate();
				vObject.setDateLastModified(systemDate);
				vObject.setDateCreation(systemDate);
				retVal = doInsertionPend(vObject);
				if (retVal != Constants.SUCCESSFUL_OPERATION) {
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}else {
				// This is required for Audit Trail.
				if (collTemp.size() > 0) {
					vObjectlocal = ((ArrayList<FtpMethodsVb>) collTemp).get(0);
					vObject.setDateCreation(vObjectlocal.getDateCreation());
				}
				vObject.setDateCreation(vObjectlocal.getDateCreation());
				// Record is there in approved, but not in pending. So add it to pending
				vObject.setVerifier(0);
				vObject.setRecordIndicator(Constants.STATUS_UPDATE);
				retVal = doInsertionPendWithDc(vObject);
				if (retVal != Constants.SUCCESSFUL_OPERATION) {
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
				}
			}
			return getResultObject(Constants.SUCCESSFUL_OPERATION);
		}
	}
	
	public String getMethodType(FTPGroupsVb dObj) {
		String balTypeDesc = "";
		try {
			String sql = "";
			Object objParams[] = new Object[6];
			objParams[0] = dObj.getCountry();
			objParams[1] = dObj.getLeBook();
			objParams[2] = dObj.getFtpSubGroupId();
			objParams[3] = dObj.getCountry();
			objParams[4] = dObj.getLeBook();
			objParams[5] = dObj.getFtpSubGroupId();	
//			REPRICING_FLAG_AT
			StringBuffer strApprove = new StringBuffer(" SELECT (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE) ||' ('|| "
					+ " CASE WHEN REPRICING_FLAG = 'NOMINAL' THEN 'N' ELSE 'R' END "
					+ "||')' DESC_COL from FTP_METHODS TAppr ");
			strApprove.append(" WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ? ");
			
			StringBuffer strPend = new StringBuffer(" SELECT (SELECT ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB =  METHOD_TYPE) ||' ('||  CASE WHEN REPRICING_FLAG = 'NOMINAL' THEN 'N' ELSE 'R' END ||')' DESC_COL from FTP_METHODS_PEND TPend ");
			strPend.append(" WHERE COUNTRY = ?  AND LE_BOOK = ?  AND FTP_SUB_GROUP_ID = ? ");
			
			String strWhereNotExists = new String( " Not Exists (Select 'X' From FTP_METHODS_PEND TPend WHERE TAppr.COUNTRY = TPend.COUNTRY "
					+ "AND TAppr.LE_BOOK = TPend.LE_BOOK AND TAppr.FTP_SUB_GROUP_ID = TPend.FTP_SUB_GROUP_ID AND TAppr.REPRICING_FLAG = TPend.REPRICING_FLAG )");
			strApprove.append(" AND "+strWhereNotExists);
			
			sql = " SELECT LISTAGG(DESC_COL, ', ') WITHIN GROUP (ORDER BY DESC_COL) AS METHOD_TYPE_DESC FROM( "+strApprove.toString() + " UNION " + strPend.toString()+") ";
			RowMapper mapper = new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (rs.getString("DESC_COL"));
				}
			};
			balTypeDesc =  getJdbcTemplate().queryForObject(sql, objParams, String.class);
		}catch(Exception ex) {
			
		}
		return balTypeDesc;
	}
}
