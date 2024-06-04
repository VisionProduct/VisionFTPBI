package com.vision.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.SmartSearchVb;
@Component
public class FTPGroupsDao extends AbstractDao<FTPGroupsVb> {
	@Autowired
	FTPSourceConfigDao ftpSourceConfigDao;
	
	@Autowired
	FtpMethodsDao ftpMethodsDao;
	
	public FtpMethodsDao getFtpMethodsDao() {
		return ftpMethodsDao;
	}
	public void setFtpMethodsDao(FtpMethodsDao ftpMethodsDao) {
		this.ftpMethodsDao = ftpMethodsDao;
	}
	public FTPSourceConfigDao getFtpSourceConfigDao() {
		return ftpSourceConfigDao;
	}
	public void setFtpSourceConfigDao(FTPSourceConfigDao ftpSourceConfigDao) {
		this.ftpSourceConfigDao = ftpSourceConfigDao;
	}
	
	String VariableStatusNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TAppr.FTP_GRP_STATUS", "FTP_GRP_STATUS_DESC");
	String VariableStatusNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 1, "TPend.FTP_GRP_STATUS", "FTP_GRP_STATUS_DESC");

	String RecordIndicatorNtApprDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TAppr.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	String RecordIndicatorNtPendDesc = ValidationUtil.numAlphaTabDescritpionQuery("NT", 7, "TPend.RECORD_INDICATOR", "RECORD_INDICATOR_DESC");
	
	/*FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
	ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
	ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
	ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
	ftpSourceConfigVb.setDataSourceDescription(rs.getString("DATA_SOURCE_DESC"));
	ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
	ftpSourceConfigVb.setFtpReference(rs.getString("FTP_REFERENCE"));
	ftpSourceConfigVb.setGroupSeq(rs.getInt("FTP_GROUP_SEQ"));
	ftpSourceConfigVb.setFtpDescription(rs.getString("FTP_DESCRIPTION"));
	ftpSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
	ftpSourceConfigVb.setMethodReference(rs.getString("METHOD_REFERENCE"));
	ftpSourceConfigVb.setMethodDescription(rs.getString("METHOD_DESCRIPTION"));
	ftpSourceConfigVb.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
	ftpSourceConfigVb.setFtpGroupStatus(rs.getInt("FTP_GRP_STATUS"));
	ftpSourceConfigVb.setStatusDesc(rs.getString("FTP_GRP_STATUS_DESC"));
	ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_GRP_STATUS"));
	ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
	ftpSourceConfigVb.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
	ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
	ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
	ftpSourceConfigVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
	ftpSourceConfigVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
	ftpSourceConfigVb.setDateCreation(rs.getString("DATE_CREATION"));
	return ftpSourceConfigVb;*/
	
	/*public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			FTPGroupsVb vObjectParent = new FTPGroupsVb();
//			FTPGroupsVb vObjectParent = null;
			String previousConnectorId = "";
			List<FTPGroupsVb> result = new ArrayList<FTPGroupsVb>();
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb childVb = new FTPGroupsVb();
				
				
				String keyValue = rs.getString("COUNTRY")+""+rs.getString("LE_BOOK")+""+rs.getString("DATA_SOURCE")+""+rs.getString("FTP_GROUP");
				if (!ValidationUtil.isValid(previousConnectorId)) {
					vObjectParent = new FTPGroupsVb(rs.getString("COUNTRY"), rs.getString("LE_BOOK"),rs.getString("DATA_SOURCE"), rs.getString("FTP_GROUP"));
					previousConnectorId = rs.getString("COUNTRY")+""+rs.getString("LE_BOOK")+""+rs.getString("DATA_SOURCE")+""+rs.getString("FTP_GROUP");
				} else if (!previousConnectorId.equalsIgnoreCase(keyValue)) {
					result.add(vObjectParent);
					vObjectParent = new FTPGroupsVb(rs.getString("COUNTRY"), rs.getString("LE_BOOK"),rs.getString("DATA_SOURCE"), rs.getString("FTP_GROUP"));
					previousConnectorId = rs.getString("COUNTRY")+""+rs.getString("LE_BOOK")+""+rs.getString("DATA_SOURCE")+""+rs.getString("FTP_GROUP");
				}
				childVb.setCountry(rs.getString("COUNTRY"));
				childVb.setLeBook(rs.getString("LE_BOOK"));
				childVb.setDataSource(rs.getString("DATA_SOURCE"));
				childVb.setDataSourceDescription(rs.getString("DATA_SOURCE_DESC"));
				childVb.setFtpGroup(rs.getString("FTP_GROUP"));
				childVb.setFtpReference(rs.getString("FTP_REFERENCE"));
				childVb.setGroupSeq(rs.getInt("FTP_GROUP_SEQ"));
				childVb.setFtpDescription(rs.getString("FTP_DESCRIPTION"));
				childVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				childVb.setMethodReference(rs.getString("METHOD_REFERENCE"));
				childVb.setMethodDescription(rs.getString("METHOD_DESCRIPTION"));
				childVb.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
				childVb.setFtpGroupStatus(rs.getInt("FTP_GRP_STATUS"));
				childVb.setStatusDesc(rs.getString("FTP_GRP_STATUS_DESC"));
				childVb.setDbStatus(rs.getInt("FTP_GRP_STATUS"));
				childVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				childVb.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
				childVb.setMaker(rs.getInt("MAKER"));
				childVb.setVerifier(rs.getInt("VERIFIER"));
				childVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				childVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				childVb.setDateCreation(rs.getString("DATE_CREATION"));

				vObjectParent.getChildList().add(childVb);
				return vObjectParent;
			}
		
		};
		return mapper;
	}*/
	public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			List<FTPGroupsVb> result = new ArrayList<FTPGroupsVb>();
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
				return ftpSourceConfigVb;
			}
		
		};
		return mapper;
	}
	
	public RowMapper getQueryPopupMapperDetails(){
		RowMapper mapper = new RowMapper() {
			List<FTPGroupsVb> result = new ArrayList<FTPGroupsVb>();
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setDataSourceDescription(rs.getString("DATA_SOURCE_DESC"));
				ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
				ftpSourceConfigVb.setFtpGroupDesc(rs.getString("FTP_GROUP_DESC"));
				ftpSourceConfigVb.setFtpReference(rs.getString("FTP_REFERENCE"));
				ftpSourceConfigVb.setGroupSeq(rs.getInt("FTP_GROUP_SEQ"));
				ftpSourceConfigVb.setFtpDescription(rs.getString("FTP_DESCRIPTION"));
				ftpSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				ftpSourceConfigVb.setSourceReferenceDesc(rs.getString("SOURCE_REFERENCE_DESC"));
				ftpSourceConfigVb.setMethodReference(rs.getString("METHOD_REFERENCE"));
				ftpSourceConfigVb.setMethodDescription(rs.getString("METHOD_DESCRIPTION"));
				ftpSourceConfigVb.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
				ftpSourceConfigVb.setFtpGroupStatus(rs.getInt("FTP_GRP_STATUS"));
				ftpSourceConfigVb.setStatusDesc(rs.getString("FTP_GRP_STATUS_DESC"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_GRP_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setRecordIndicatorDesc(rs.getString("RECORD_INDICATOR_DESC"));
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
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setSourceReference(rs.getString("FTP_GROUP"));
				ftpSourceConfigVb.setFtpReference(rs.getString("FTP_REFERENCE"));
				ftpSourceConfigVb.setGroupSeq(rs.getInt("FTP_GROUP_SEQ"));
				ftpSourceConfigVb.setFtpDescription(rs.getString("FTP_DESCRIPTION"));
				ftpSourceConfigVb.setSourceReference(rs.getString("SOURCE_REFERENCE"));
				ftpSourceConfigVb.setMethodReference(rs.getString("METHOD_REFERENCE"));
				ftpSourceConfigVb.setMethodDescription(rs.getString("METHOD_DESCRIPTION"));
				ftpSourceConfigVb.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
				ftpSourceConfigVb.setFtpGroupStatus(rs.getInt("FTP_GRP_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_GRP_STATUS"));
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
	
	
	protected RowMapper getFtpRatesDetailsMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpCurveVb = new FTPCurveVb();
			/*	ftpCurveVb.setTenorBucketApplicationCode((rs.getInt("TENOR_APPLICATION_CODE")));
				ftpCurveVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				ftpCurveVb.setVisionSbu(rs.getString("VISION_SBU"));
				ftpCurveVb.setCurrency(rs.getString("CURRENCY"));
				ftpCurveVb.setIntStart(rs.getString("INT_RATE_START"));
				ftpCurveVb.setIntEnd(rs.getString("INT_RATE_END"));
				ftpCurveVb.setAmountStart(rs.getString("AMOUNT_START"));
				ftpCurveVb.setAmountEnd(rs.getString("AMOUNT_END"));
				*/				
				ftpCurveVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpCurveVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
				ftpCurveVb.setFtpCurveId(rs.getString("FTP_CURVE"));
				ftpCurveVb.setAddOnDepositRate(rs.getString("ADDON_DEPOSIT_RATE"));
				ftpCurveVb.setSubsidy(rs.getString("SUBSIDY"));
				ftpCurveVb.setAddOnLendingRate(rs.getString("ADDON_LENDING_RATE"));
				ftpCurveVb.setRequiredReserveRate(rs.getString("REQUIRED_RESERVE_RATE"));
				ftpCurveVb.setGlAllocationRate(rs.getString("GL_ALLOCATION_RATE"));
				ftpCurveVb.setInsuranceRate(rs.getString("INSURANCE_RATE"));
				ftpCurveVb.setAddOnAttRate1(rs.getString("ADDON_ATT_RATE1"));
				ftpCurveVb.setAddOnAttRate1(rs.getString("ADDON_ATT_RATE1"));
				ftpCurveVb.setAddOnAttRate1(rs.getString("ADDON_ATT_RATE1"));
				ftpCurveVb.setAddOnAttRate2(rs.getString("ADDON_ATT_RATE2"));
				ftpCurveVb.setAddOnAttRate3(rs.getString("ADDON_ATT_RATE3"));
				ftpCurveVb.setAddOnAttRate4(rs.getString("ADDON_ATT_RATE4"));
				ftpCurveVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpCurveVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpCurveVb.setMaker(rs.getInt("MAKER"));
				ftpCurveVb.setVerifier(rs.getInt("VERIFIER"));
//				ftpCurveVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				return ftpCurveVb;
			}
		};
		return mapper;
	}
	
	
	
	protected RowMapper getGroupMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPGroupsVb ftpSourceConfigVb = new FTPGroupsVb();
				ftpSourceConfigVb.setCountry(rs.getString("COUNTRY"));
				ftpSourceConfigVb.setLeBook(rs.getString("LE_BOOK"));
				ftpSourceConfigVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpSourceConfigVb.setFtpGroup(rs.getString("FTP_GROUP"));
				ftpSourceConfigVb.setFtpReference(rs.getString("FTP_REFERENCE"));
				ftpSourceConfigVb.setGroupSeq(rs.getInt("FTP_GROUP_SEQ"));
				ftpSourceConfigVb.setDefaultGroup(rs.getString("FTP_DEFAULT_FLAG"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getTenorBucketsMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				ftpSourceConfigVb.setTenorDecription(rs.getString("TENOR_DESCRIPTION"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getCurveMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpCurveId(rs.getString("FTP_CURVE"));
				/*ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));*/
				ftpSourceConfigVb.setActualEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setTenorBucketApplicationCode(rs.getInt("TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setTenorBucketCode(rs.getString("TENOR_CODE"));
				ftpSourceConfigVb.setVisionSbu(rs.getString("VISION_SBU_ATTRIBUTE"));
				ftpSourceConfigVb.setCurrency(rs.getString("CURRENCY"));
				ftpSourceConfigVb.setIntStart(rs.getString("INT_RATE_START"));
				ftpSourceConfigVb.setIntEnd(rs.getString("INT_RATE_END"));
				ftpSourceConfigVb.setAmountStart(rs.getString("AMOUNT_START"));
				ftpSourceConfigVb.setAmountEnd(rs.getString("AMOUNT_END"));
				ftpSourceConfigVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
				ftpSourceConfigVb.setSubRate(rs.getString("FTP_CURVE"));
				ftpSourceConfigVb.setFtpCurveStatus(rs.getInt("FTP_CURVE_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_CURVE_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				ftpSourceConfigVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpSourceConfigVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpSourceConfigVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getAddOnMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpSourceConfigVb.setActualEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setCustomerId(rs.getString("CUSTOMER_ID"));
				ftpSourceConfigVb.setContractId(rs.getString("CONTRACT_ID"));
				ftpSourceConfigVb.setAddOnRate(rs.getString("ADDON_RATE"));
				ftpSourceConfigVb.setSubRate(rs.getString("SUBSIDY_RATE"));
				ftpSourceConfigVb.setFtpCurveStatus(rs.getInt("FTP_ADDON_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_ADDON_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				ftpSourceConfigVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpSourceConfigVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpSourceConfigVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpSourceConfigVb.setMaker(rs.getInt("MAKER"));
				ftpSourceConfigVb.setVerifier(rs.getInt("VERIFIER"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getPremiumMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setTenorBucketApplicationCode(rs.getInt("FTP_TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setTenorBucketCode(rs.getString("FTP_TENOR_CODE"));
				ftpSourceConfigVb.setLpTenorBucketApplicationCode(rs.getInt("LP_TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setLpTenorBucketCode(rs.getString("LP_TENOR_CODE"));
				ftpSourceConfigVb.setVisionSbu(rs.getString("VISION_SBU_ATTRIBUTE"));
				ftpSourceConfigVb.setCurrency(rs.getString("CURRENCY"));
				ftpSourceConfigVb.setFtpRateId(rs.getString("LP_RATE_ID"));
				ftpSourceConfigVb.setSubRate(rs.getString("LP_RATE"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getPopMapperPremium(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setTenorBucketApplicationCode(rs.getInt("FTP_TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setLpTenorBucketApplicationCode(rs.getInt("LP_TENOR_APPLICATION_CODE"));
				ftpSourceConfigVb.setVisionSbu(rs.getString("VISION_SBU_ATTRIBUTE"));
				ftpSourceConfigVb.setCurrency(rs.getString("CURRENCY"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getMapperSource(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPSourceConfigVb ftpSourceConfigVb = new FTPSourceConfigVb();
				ftpSourceConfigVb.setSequence(rs.getInt("SOURCE_SEQUENCE"));
				ftpSourceConfigVb.setTableName(rs.getString("TABLE_NAME"));
				ftpSourceConfigVb.setColName(rs.getString("COLUMN_NAME"));
				ftpSourceConfigVb.setOperand(rs.getString("OPERAND"));
				ftpSourceConfigVb.setConditionValue1(rs.getString("CONDITION_VALUE1"));
				ftpSourceConfigVb.setConditionValue2(rs.getString("CONDITION_VALUE2"));
				ftpSourceConfigVb.setFtpSourceConfigStatus(rs.getInt("FTP_SC_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected RowMapper getMapperMethods(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FtpMethodsVb ftpMethodsVb = new FtpMethodsVb();
				ftpMethodsVb = new FtpMethodsVb();
				ftpMethodsVb.setFtpCurveId(rs.getString("FTP_CURVE_ID"));
				ftpMethodsVb.setMethodType(rs.getString("METHOD_TYPE"));
				ftpMethodsVb.setMethodTypeDesc(rs.getString("METHOD_TYPE_DESC"));
				ftpMethodsVb.setMethodSubType(rs.getString("METHOD_SUB_TYPE"));
				ftpMethodsVb.setMethodSubTypeDesc(rs.getString("METHOD_SUB_TYPE_DESC"));
				ftpMethodsVb.setFtpTenorType(rs.getInt("FTP_TENOR_TYPE"));
				ftpMethodsVb.setFtpTenorTypeDesc(rs.getString("FTP_TENOR_TYPE_DESC"));
				ftpMethodsVb.setMethodBalType(rs.getInt("METHOD_BAL_TYPE"));
				ftpMethodsVb.setMethodBalTypeDesc(rs.getString("METHOD_BAL_TYPE_DESC"));
				ftpMethodsVb.setRepricingFlag(rs.getString("REPRICING_FLAG"));
				ftpMethodsVb.setRepricingFlagDesc(rs.getString("REPRICING_FLAG_DESC"));
				ftpMethodsVb.setLpTenorType(rs.getInt("LP_TENOR_TYPE"));
				ftpMethodsVb.setLpTenorTypeDesc(rs.getString("LP_TENOR_TYPE_DESC"));
				ftpMethodsVb.setStatus(rs.getInt("FTP_MT_STATUS"));
				ftpMethodsVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				
				ftpMethodsVb.setInterestBasis(rs.getInt("INTEREST_BASIS"));
				ftpMethodsVb.setFtpApplyRate(rs.getInt("FTP_APPLY_RATE"));
				ftpMethodsVb.setAddonApplyRate(rs.getInt("ADDON_APPLY_RATE"));
				ftpMethodsVb.setLpApplyRate(rs.getInt("LP_APPLY_RATE"));
				
				
				ftpMethodsVb.setInterestBasisDesc(rs.getString("INTEREST_BASIS_DESC"));
				ftpMethodsVb.setFtpApplyRateDesc(rs.getString("FTP_APPLY_RATE_DESC"));
				ftpMethodsVb.setAddonApplyRateDesc(rs.getString("ADDON_APPLY_RATE_DESC"));
				ftpMethodsVb.setLpApplyRateDesc(rs.getString("LP_APPLY_RATE_DESC"));
				return ftpMethodsVb;
			}
		};
		return mapper;
	}
	public List<FTPGroupsVb> getQueryPopupResults1(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, " + 
			" TAppr.DATA_SOURCE,TAPPR.FTP_GROUP " + 
			" From FTP_GROUPS TAppr");
		try{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK) like ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())){
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())){
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("TAppr.FTP_GROUP = ?", strBufApprove);
			}
			//check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1){
				if (dObj.getRecordIndicator() > 3){
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
				}else{
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
				}
			}
			String orderBy = " Order By COUNTRY, LE_BOOK, DATA_SOURCE, FTP_GROUP ";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getQueryPopupMapperDetails());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResults(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("SELECT TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,\n" + 
				"(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC\n" + 
				",TAppr.FTP_GROUP,TAppr.FTP_REFERENCE,TAppr.FTP_GROUP_SEQ,\n" + 
				"				T2.FTP_DESCRIPTION,T2.SOURCE_REFERENCE,\n" + 
				"(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB T1 WHERE ALPHA_TAB = SOURCE_REFERENCE_AT AND ALPHA_sub_TAB= SOURCE_REFERENCE ) SOURCE_REFERENCE_DESC,\n" + 
				"				T2.METHOD_REFERENCE,\n" + 
				"				(SELECT MIN(METHOD_DESCRIPTION) FROM FTP_METHODS WHERE METHOD_REFERENCE = T2.METHOD_REFERENCE) METHOD_DESCRIPTION,\n" + 
				"				TAppr.FTP_DEFAULT_FLAG,\n" + 
				"				TAppr.FTP_GRP_STATUS, "+VariableStatusNtApprDesc
				+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
				+ ",TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED \n" + 
				"				FROM FTP_GROUPS TAppr,FTP_CONTROLS T2");
		StringBuffer strBufPending =new StringBuffer("");
		try{
			/*if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK) like ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())){
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())){
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("TAppr.FTP_GROUP = ?", strBufApprove);
			}
			//check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1){
				if (dObj.getRecordIndicator() > 3){
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
				}else{
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
				}
			}*/
			CommonUtils.addToQuery(" TAppr.COUNTRY = T2.COUNTRY"+
					"	AND TAppr.LE_BOOK = T2.LE_BOOK"+
					"	AND TAppr.FTP_REFERENCE = T2.FTP_REFERENCE ",strBufApprove);
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

					case "dataSource":
						CommonUtils.addToQuerySearch(" upper(TAppr.DATA_SOURCE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.DATA_SOURCE) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpGroup":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_GROUP) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_GROUP) "+ val, strBufPending, data.getJoinType());
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
			
			String orderBy = " ORDER BY TAppr.COUNTRY, TAppr.LE_BOOK, DATA_SOURCE, FTP_GROUP, SOURCE_REFERENCE, FTP_GROUP_SEQ ";
			
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getQueryPopupMapperDetails());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResults(FTPGroupsVb dObj,int status){
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("SELECT T1.COUNTRY,T1.LE_BOOK,T1.DATA_SOURCE,T1.FTP_GROUP,T1.FTP_REFERENCE,T1.FTP_GROUP_SEQ,"+
				"	T2.FTP_DESCRIPTION,T2.SOURCE_REFERENCE,T2.METHOD_REFERENCE,"+
				"  (SELECT MIN(METHOD_DESCRIPTION) FROM FTP_METHODS WHERE METHOD_REFERENCE = T2.METHOD_REFERENCE) METHOD_DESCRIPTION,"+
				"   T1.FTP_DEFAULT_FLAG,"+
				"	T1.FTP_GRP_STATUS,T1.RECORD_INDICATOR,T1.MAKER,T1.VERIFIER,T1.INTERNAL_STATUS,T1.DATE_CREATION,T1.DATE_LAST_MODIFIED "+
				"   FROM FTP_GROUPS T1,FTP_CONTROLS T2");
		try{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(T1.COUNTRY) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(T1.LE_BOOK) like ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())){
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("T1.DATA_SOURCE = ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())){
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("T1.FTP_GROUP = ?", strBufApprove);
			}
			CommonUtils.addToQuery(" T1.COUNTRY = T2.COUNTRY"+
					"	AND T1.LE_BOOK = T2.LE_BOOK"+
					"	AND T1.FTP_REFERENCE = T2.FTP_REFERENCE ",strBufApprove);
			String orderBy = " Order By FTP_GROUP_SEQ";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultsSource(FTPGroupsVb dObj){
		dObj.setRecordIndicator(0);
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("SELECT SOURCE_SEQUENCE,TABLE_NAME,COLUMN_NAME,OPERAND,CONDITION_VALUE1,CONDITION_VALUE2,FTP_SC_STATUS,RECORD_INDICATOR "+
				" FROM FTP_SOURCE_CONFIG ");
		try{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(COUNTRY) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(LE_BOOK) like ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getSourceReference())){
				params.addElement(dObj.getSourceReference());
				CommonUtils.addToQuery("SOURCE_REFERENCE = ?", strBufApprove);
			}
			String orderBy = " Order By SOURCE_SEQUENCE";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getMapperSource());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FtpMethodsVb> getQueryResultMethods(FTPGroupsVb dObj){
		List<FtpMethodsVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		
		String strBufApprove = new String("SELECT METHOD_TYPE, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB = METHOD_TYPE )METHOD_TYPE_DESC "
				+ ", METHOD_SUB_TYPE, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_SUB_TYPE_AT AND ALPHA_SUB_TAB = METHOD_SUB_TYPE ) METHOD_SUB_TYPE_DESC , "
				+ "REPRICING_FLAG, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = REPRICING_FLAG_AT AND ALPHA_SUB_TAB = REPRICING_FLAG ) REPRICING_FLAG_DESC, "
				+ "FTP_TENOR_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = FTP_TENOR_TYPE) FTP_TENOR_TYPE_DESC,"
				+ "LP_TENOR_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = LP_TENOR_TYPE) LP_TENOR_TYPE_DESC,"
				+ "METHOD_BAL_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = METHOD_BAL_TYPE_NT AND NUM_SUB_TAB = METHOD_BAL_TYPE ) METHOD_BAL_TYPE_DESC,"
				+ "INTEREST_BASIS, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = INTEREST_BASIS_NT AND NUM_SUB_TAB = INTEREST_BASIS) INTEREST_BASIS_DESC,"
				+ "FTP_APPLY_RATE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = FTP_APPLY_RATE ) FTP_APPLY_RATE_DESC ,"
				+ "ADDON_APPLY_RATE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = ADDON_APPLY_RATE ) ADDON_APPLY_RATE_DESC ,"
				+ "LP_APPLY_RATE,  (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB =  APPLY_RATE_NT AND NUM_SUB_TAB = LP_APPLY_RATE )LP_APPLY_RATE_DESC ,"+
				 "FTP_CURVE_ID,FTP_MT_STATUS,RECORD_INDICATOR FROM FTP_METHODS WHERE METHOD_REFERENCE = ? AND FTP_MT_STATUS = 0");
		
		if(ValidationUtil.isValid(dObj.getRepricingFlag())){
			/*strBufApprove = new String("SELECT METHOD_TYPE, METHOD_SUB_TYPE, REPRICING_FLAG,FTP_TENOR_TYPE,LP_TENOR_TYPE,METHOD_BAL_TYPE,INTEREST_BASIS,FTP_APPLY_RATE,ADDON_APPLY_RATE,LP_APPLY_RATE,"+
					 "FTP_CURVE_ID,FTP_MT_STATUS,RECORD_INDICATOR FROM FTP_METHODS WHERE METHOD_REFERENCE = ? AND FTP_MT_STATUS = 0 and REPRICING_FLAG='"+dObj.getRepricingFlag()+"'");*/
			
			strBufApprove = new String("SELECT METHOD_TYPE, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_TYPE_AT AND ALPHA_SUB_TAB = METHOD_TYPE )METHOD_TYPE_DESC "
					+ ", METHOD_SUB_TYPE, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = METHOD_SUB_TYPE_AT AND ALPHA_SUB_TAB = METHOD_SUB_TYPE ) METHOD_SUB_TYPE_DESC , "
					+ "REPRICING_FLAG, (SELECT ALPHA_SUBTAB_DESCRIPTION from ALPHA_SUB_TAB WHERE ALPHA_TAB = REPRICING_FLAG_AT AND ALPHA_SUB_TAB = REPRICING_FLAG ) REPRICING_FLAG_DESC, "
					+ "FTP_TENOR_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = FTP_TENOR_TYPE) FTP_TENOR_TYPE_DESC,"
					+ "LP_TENOR_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = TENOR_TYPE_NT AND NUM_SUB_TAB = LP_TENOR_TYPE) LP_TENOR_TYPE_DESC,"
					+ "METHOD_BAL_TYPE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = METHOD_BAL_TYPE_NT AND NUM_SUB_TAB = METHOD_BAL_TYPE ) METHOD_BAL_TYPE_DESC,"
					+ "INTEREST_BASIS, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = INTEREST_BASIS_NT AND NUM_SUB_TAB = INTEREST_BASIS) INTEREST_BASIS_DESC,"
					+ "FTP_APPLY_RATE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = FTP_APPLY_RATE ) FTP_APPLY_RATE_DESC ,"
					+ "ADDON_APPLY_RATE, (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB = APPLY_RATE_NT AND NUM_SUB_TAB = ADDON_APPLY_RATE ) ADDON_APPLY_RATE_DESC ,"
					+ "LP_APPLY_RATE,  (SELECT NUM_SUBTAB_DESCRIPTION from NUM_SUB_TAB WHERE NUM_TAB =  APPLY_RATE_NT AND NUM_SUB_TAB = LP_APPLY_RATE )LP_APPLY_RATE_DESC ,"+
					 "FTP_CURVE_ID,FTP_MT_STATUS,RECORD_INDICATOR FROM FTP_METHODS WHERE METHOD_REFERENCE = ? AND FTP_MT_STATUS = 0 and REPRICING_FLAG='"+dObj.getRepricingFlag()+"'");
		}
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getMethodReference();
		
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getMapperMethods());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultsGroup(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 5;
		String strBufApprove = "SELECT COUNTRY,LE_BOOK,DATA_SOURCE,FTP_GROUP,FTP_GROUP_SEQ,FTP_DEFAULT_FLAG,FTP_REFERENCE FROM FTP_GROUPS WHERE COUNTRY = ? "+
						" AND LE_BOOK = ? AND DATA_SOURCE=? AND FTP_REFERENCE = ? AND FTP_GROUP = ? ";
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getDataSource();
		objParams[3] = dObj.getFtpReference();
		objParams[4] = dObj.getFtpGroup();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getGroupMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultControls(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 4;
		String strBufApprove = new String("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, " + 
			" TAppr.DATA_SOURCE,TAPPR.FTP_GROUP " + 
			" From FTP_GROUPS TAppr where Country = ? and LE_Book = ? and Data_Source = ? and FTP_Group =?");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getDataSource();
		objParams[3] = dObj.getFtpGroup();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getGroupMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultGroup(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 6;
		String strBufApprove = new String("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, " + 
			" TAppr.DATA_SOURCE,TAPPR.FTP_GROUP,TAPPR.FTP_REFERENCE,TAPPR.FTP_GROUP_SEQ,TAPPR.FTP_DEFAULT_FLAG " + 
			" From FTP_GROUPS TAppr where Country = ? and LE_Book = ? and Data_Source = ? and FTP_REFERENCE = ? and FTP_Group =? and FTP_GROUP_SEQ = ? ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getCountry();
		objParams[1] = dObj.getLeBook();
		objParams[2] = dObj.getDataSource();
		objParams[3] = dObj.getFtpReference();
		objParams[4] = dObj.getFtpGroup();
		objParams[5] = dObj.getGroupSeq();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getGroupMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultCurves(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,TENOR_APPLICATION_CODE,TENOR_CODE,VISION_SBU_ATTRIBUTE,"
				+ " CURRENCY,TO_CHAR(INT_RATE_START,'9,990.999999990') INT_RATE_START,TO_CHAR(INT_RATE_END,'9,990.999999990') INT_RATE_END,TO_CHAR(AMOUNT_START,'99,999,999,999,999,999,990.99990') AMOUNT_START ,TO_CHAR(AMOUNT_END,'99,999,999,999,999,999,990.99990') AMOUNT_END,FTP_CURVE_STATUS,"
				+ " (SELECT TO_CHAR(FTP_CURVE,'9,990.999999990') FTP_CURVE FROM FTP_TERM_RATES WHERE " 
	            + " FTP_RATE_ID = T1.FTP_RATE_ID AND EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_TERM_RATES WHERE FTP_RATE_ID = T1.FTP_RATE_ID ) AND EFFECTIVE_DATE >= T1.EFFECTIVE_DATE) FTP_CURVE,"
				+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION,FTP_RATE_ID FROM FTP_CURVES T1"
				+ " WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE= TO_DATE(?,'DD-MM-RRRR') ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
				
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getCurveMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getFTPRatesDataDao(FTPGroupsVb dObj,FTPCurveVb vObject){
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select Distinct  TAppr.FTP_RATE_ID ,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE, "+
				" TO_CHAR(TAppr.FTP_CURVE,'9,990.999999990')FTP_CURVE, TO_CHAR (TAppr.ADDON_DEPOSIT_RATE, '9,990.999999990')ADDON_DEPOSIT_RATE,TO_CHAR (TAppr.SUBSIDY, '9,990.999999990')SUBSIDY,TO_CHAR (TAppr.ADDON_LENDING_RATE, '9,990.999999990')ADDON_LENDING_RATE,TO_CHAR (TAppr.REQUIRED_RESERVE_RATE, '9,990.999999990')REQUIRED_RESERVE_RATE ,"+
				" TO_CHAR (TAppr.GL_ALLOCATION_RATE, '9,990.999999990')GL_ALLOCATION_RATE,TO_CHAR (TAppr.INSURANCE_RATE, '9,990.999999990')INSURANCE_RATE,TO_CHAR (TAppr.ADDON_ATT_RATE1, '9,990.999999990')ADDON_ATT_RATE1,TO_CHAR (TAppr.ADDON_ATT_RATE2, '9,990.999999990')ADDON_ATT_RATE2,TO_CHAR (TAppr.ADDON_ATT_RATE3, '9,990.999999990')ADDON_ATT_RATE3,TO_CHAR (TAppr.ADDON_ATT_RATE4, '9,990.999999990')ADDON_ATT_RATE4,TAppr.MAKER, TAppr.VERIFIER, TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR "+
			" From FTP_TERM_RATES TAppr ");
	/*	StringBuffer strBufPending = new StringBuffer("Select Distinct TPend.FTP_RATE_ID ,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE, "+
				" TPend.FTP_CURVE,TPend.ADDON_DEPOSIT_RATE,TPend.SUBSIDY,TPend.ADDON_LENDING_RATE,TPend.REQUIRED_RESERVE_RATE ,"+
				" TPend.GL_ALLOCATION_RATE,TPend.INSURANCE_RATE,TPend.ADDON_ATT_RATE1,TPend.ADDON_ATT_RATE2,TPend.ADDON_ATT_RATE3,TPend.ADDON_ATT_RATE4,TPend.MAKER, TPend.VERIFIER, TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR " +
			" From FTP_TERM_RATES_PEND TPend ");*/

		try
		{
			if (ValidationUtil.isValid(vObject.getFtpRateId()))
			{
				params.addElement(vObject.getFtpRateId());
				CommonUtils.addToQuery("TAppr.FTP_RATE_ID = ?", strBufApprove);
				/*CommonUtils.addToQuery("TPend.FTP_RATE_ID = ?", strBufPending);*/
			}
			if (ValidationUtil.isValid(vObject.getEffectiveDate()))
			{
				params.addElement(vObject.getEffectiveDate());
				CommonUtils.addToQuery("TAppr.EFFECTIVE_DATE <= TO_DATE(?,'DD-MM-RRRR') ", strBufApprove);
				/*CommonUtils.addToQuery("TPend.EFFECTIVE_DATE <= TO_DATE(?,'DD-MM-RRRR')", strBufPending);*/
			}
			
			
			//check if the column [RECORD_INDICATOR] should be included in the query
			if (vObject.getRecordIndicator() != -1){
				if (vObject.getRecordIndicator() > 3){
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
					/*CommonUtils.addToQuery("TPend.RECORD_INDICATOR > ?", strBufPending);*/
				}else{
					params.addElement(new Integer(vObject.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
					/*CommonUtils.addToQuery("TPend.RECORD_INDICATOR = ?", strBufPending);*/
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			logger.error("UNION");
			/*logger.error(((strBufPending==null)? "strBufPending is Null":strBufPending.toString()));*/

			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}

		String orderBy = " Order By EFFECTIVE_DATE DESC";
		return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getFtpRatesDetailsMapper());
	}
	

	

	public List<FTPGroupsVb> getQueryPopupResultsCurves(String ftpCurve,String effectiveDate,FTPGroupsVb dObj,Boolean flag){
		dObj.setRecordIndicator(0);
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer();
		if(flag){
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR (t1.EFFECTIVE_DATE, 'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TENOR_APPLICATION_CODE,TENOR_CODE,NVL (VISION_SBU_ATTRIBUTE, '000') VISION_SBU_ATTRIBUTE,"+
					" CURRENCY,TO_CHAR (INT_RATE_START, '9,990.999999990') INT_RATE_START,"+
					" TO_CHAR (INT_RATE_END, '9,990.999999990') INT_RATE_END,TO_CHAR (AMOUNT_START, '99,999,999,999,999,999,990.99990')"+
					" AMOUNT_START,TO_CHAR (AMOUNT_END, '99,999,999,999,999,999,990.99990') AMOUNT_END,"+
					" FTP_CURVE_STATUS,FTP_RATE_ID,(SELECT TO_CHAR (t3.FTP_CURVE, '9,990.999999990') FTP_CURVE"+
					" FROM FTP_TERM_RATES t3 WHERE     t3.FTP_RATE_ID = T1.FTP_RATE_ID "+
					" AND t3.EFFECTIVE_DATE = (SELECT MAX (EFFECTIVE_DATE) FROM FTP_TERM_RATES t2"+
					" WHERE t2.FTP_RATE_ID = T1.FTP_RATE_ID and  t2.EFFECTIVE_DATE <= t1.Effective_Date)) FTP_CURVE,"+
					" RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,"+
					" DATE_CREATION FROM FTP_CURVES T1 WHERE"+
					" EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_CURVES t2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID"+
					" AND t2.EFFECTIVE_DATE <= TO_DATE ('"+effectiveDate+"', 'DD-MM-RRRR')) ");
		}else{
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR (t1.EFFECTIVE_DATE, 'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TENOR_APPLICATION_CODE,TENOR_CODE,NVL (VISION_SBU_ATTRIBUTE, '000') VISION_SBU_ATTRIBUTE,"+
					" CURRENCY,TO_CHAR (INT_RATE_START, '9,990.999999990') INT_RATE_START,"+
					" TO_CHAR (INT_RATE_END, '9,990.999999990') INT_RATE_END,TO_CHAR (AMOUNT_START, '99,999,999,999,999,999,990.99990')"+
					" AMOUNT_START,TO_CHAR (AMOUNT_END, '99,999,999,999,999,999,990.99990') AMOUNT_END,"+
					" FTP_CURVE_STATUS,FTP_RATE_ID,(SELECT TO_CHAR (t3.FTP_CURVE, '9,990.999999990') FTP_CURVE"+
					" FROM FTP_TERM_RATES t3 WHERE     t3.FTP_RATE_ID = T1.FTP_RATE_ID "+
					" AND t3.EFFECTIVE_DATE = (SELECT MAX (EFFECTIVE_DATE) FROM FTP_TERM_RATES t2"+
					" WHERE t2.FTP_RATE_ID = T1.FTP_RATE_ID and  t2.EFFECTIVE_DATE <= t1.Effective_Date)) FTP_CURVE,"+
					" RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,"+
					" DATE_CREATION FROM FTP_CURVES T1 WHERE"+
					" EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM FTP_CURVES t2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID"+
					" AND t2.EFFECTIVE_DATE = TO_DATE ('"+effectiveDate+"', 'DD-MM-RRRR')) ");
		}
			
		try{
			if (ValidationUtil.isValid(ftpCurve))
			{
				params.addElement("%" + ftpCurve.toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(FTP_CURVE_ID) like ?", strBufApprove);
			}
			/*if (ValidationUtil.isValid(effectiveDate) && flag)
			{
				params.addElement(effectiveDate);
				CommonUtils.addToQuery("EFFECTIVE_DATE >= TO_DATE(?,'DD-MM-RRRR')", strBufApprove);
			}else{
				params.addElement(effectiveDate);
				CommonUtils.addToQuery("EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR')", strBufApprove);
			}*/
			String orderBy = " Order By TENOR_APPLICATION_CODE";
			
			Object objParams[]= objParams = new Object[params.size()];

			for(int Ctr=0; Ctr < params.size(); Ctr++)
				objParams[Ctr] = (Object) params.elementAt(Ctr);
			
//			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getCurveMapper());
			return  getJdbcTemplate().query(strBufApprove.toString()+" "+orderBy, objParams, getCurveMapper());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	@Override
	protected List<FTPGroupsVb> selectApprovedRecord(FTPGroupsVb vObject){
		return getQueryResultsGroup(vObject);
	}
	public int doInsertionGroups(FTPGroupsVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_GROUPS( " + 
			" COUNTRY, LE_BOOK, DATA_SOURCE_AT, DATA_SOURCE, FTP_GROUP_AT, FTP_GROUP,FTP_REFERENCE, FTP_GROUP_SEQ, FTP_DEFAULT_FLAG, " + 
			" FTP_GRP_STATUS_NT, FTP_GRP_STATUS,RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getCountry(), vObject.getLeBook(),
				vObject.getDataSourceAt(),vObject.getDataSource(), vObject.getFtpGroupAt(), vObject.getFtpGroup(), vObject.getFtpReference(),
			 vObject.getGroupSeq() , vObject.getDefaultGroup(),vObject.getFtpGroupStatusNt(),
			 vObject.getFtpGroupStatus(), vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public int doInsertionControls(FTPGroupsVb vObject){
		String query = "Insert Into FTP_CONTROLS( " + 
			" COUNTRY, LE_BOOK, FTP_REFERENCE, FTP_DESCRIPTION, SOURCE_REFERENCE_AT,SOURCE_REFERENCE, " + 
			" METHOD_REFERENCE,FTP_CONTROLS_STATUS_NT, FTP_CONTROLS_STATUS,RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getCountry(), vObject.getLeBook(), vObject.getFtpReference(),
			 vObject.getFtpDescription(), vObject.getSourceReferenceAt(), vObject.getSourceReference(),
			 vObject.getMethodReference() , vObject.getFtpControlStatusNt(),vObject.getFtpControlStatus(),
			 vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public int getCountFtpReference(FTPGroupsVb vObjMain){
		String sql = "Select COUNT(1) from FTP_CONTROLS WHERE FTP_REFERENCE = '"+vObjMain.getFtpReference()+"' AND SOURCE_REFERENCE = '"+vObjMain.getSourceReference()+"' AND METHOD_REFERENCE = '"+vObjMain.getMethodReference()+"' ";
		int i = getJdbcTemplate().queryForObject(sql, Integer.class);
		return i;
	}
	@Override
	protected void setServiceDefaults(){
		serviceName = "FtpGroups";
		serviceDesc = "FTP Groups";//CommonUtils.getResourceManger().getString("ftpGroup");
		tableName = "FTP_GROUPS";
		childTableName = "FTP_GROUPS";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}
	public List<FTPGroupsVb> getQueryResultAddOn(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,TO_CHAR(ADDON_RATE,'9,990.999999990')ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'9,990.999999990') SUBSIDY_RATE,FTP_ADDON_STATUS,"
				+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON"
				+ " WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE= TO_DATE(?,'DD-MM-RRRR') ");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) { 
			strBufApprove = new String("SELECT FTP_CURVE_ID,FORMAT(EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,"
					+ "FORMAT(ADDON_RATE,'#,##0.000000000')ADDON_RATE,FORMAT(SUBSIDY_RATE,'#,##0.000000000') SUBSIDY_RATE,FTP_ADDON_STATUS,"
					+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON"
					+ " WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE= FORMAT(?,'dd-MM-yyyy') ");
		}
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		/*objParams[2] = dObj.getCustomerId();
		objParams[3] = dObj.getContractId();*/
				
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getAddOnMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryResultPremium(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 8;
		String strBufApprove = new String("SELECT T1.FTP_CURVE_ID,TO_CHAR(T1.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE ,T1.FTP_TENOR_APPLICATION_CODE,T1.FTP_TENOR_CODE,"+
								" T1.LP_TENOR_APPLICATION_CODE,T1.LP_TENOR_CODE,"+ 
								" T1.VISION_SBU_ATTRIBUTE,T1.CURRENCY,T1.LP_RATE_ID,(SELECT LP_RATE FROM LP_TERM_RATES WHERE "+
								" LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE = T1.EFFECTIVE_DATE) LP_RATE "+
								" FROM FTP_PREMIUMS T1 WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = to_date(?,'DD-MM-RRRR') AND FTP_TENOR_APPLICATION_CODE  = ? "+ 
								" AND FTP_TENOR_CODE = ? AND LP_TENOR_APPLICATION_CODE = ? "+
								" AND LP_TENOR_CODE = ? AND VISION_SBU_ATTRIBUTE = ? AND CURRENCY =?");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new String("SELECT T1.FTP_CURVE_ID,FORMAT(T1.EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE ,T1.FTP_TENOR_APPLICATION_CODE,T1.FTP_TENOR_CODE,"+
					" T1.LP_TENOR_APPLICATION_CODE,T1.LP_TENOR_CODE,"+ 
					" T1.VISION_SBU_ATTRIBUTE,T1.CURRENCY,T1.LP_RATE_ID,(SELECT LP_RATE FROM LP_TERM_RATES WHERE "+
					" LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE = T1.EFFECTIVE_DATE) LP_RATE "+
					" FROM FTP_PREMIUMS T1 WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = FORMAT(?,'dd-MM-yyyy') AND FTP_TENOR_APPLICATION_CODE  = ? "+ 
					" AND FTP_TENOR_CODE = ? AND LP_TENOR_APPLICATION_CODE = ? "+
					" AND LP_TENOR_CODE = ? AND VISION_SBU_ATTRIBUTE = ? AND CURRENCY =?");
		}
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		objParams[2] = dObj.getTenorBucketApplicationCode();
		objParams[3] = dObj.getTenorBucketCode();
		objParams[4] = dObj.getLpTenorBucketApplicationCode();
		objParams[5] = dObj.getLpTenorBucketCode();
		objParams[6] = dObj.getVisionSbu();
		objParams[7] = dObj.getCurrency();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getPremiumMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResultsLp(FTPGroupsVb vObject,FTPCurveVb dObj){
		vObject.setRecordIndicator(0);
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("SELECT DISTINCT TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,FTP_CURVE_ID,FTP_TENOR_APPLICATION_CODE,LP_TENOR_APPLICATION_CODE,"+
						" NVL(VISION_SBU_ATTRIBUTE,'000') VISION_SBU_ATTRIBUTE,CURRENCY FROM FTP_PREMIUMS ");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove =new StringBuffer("SELECT DISTINCT FORMAT(EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,FTP_CURVE_ID,FTP_TENOR_APPLICATION_CODE,LP_TENOR_APPLICATION_CODE,"+
					" NVL(VISION_SBU_ATTRIBUTE,'000') VISION_SBU_ATTRIBUTE,CURRENCY FROM FTP_PREMIUMS ");
		}
		try{
			if (ValidationUtil.isValid(dObj.getFtpCurveId()))
			{
				params.addElement(dObj.getFtpCurveId());
				CommonUtils.addToQuery("FTP_CURVE_ID  = ?", strBufApprove);
			}
			if (dObj.getTenorBucketApplicationCode() != -1)
			{
				params.addElement(dObj.getTenorBucketApplicationCode());
				CommonUtils.addToQuery("FTP_TENOR_APPLICATION_CODE = ?", strBufApprove);
			}
			if (dObj.getLpTenorBucketApplicationCode() != -1){
				params.addElement(dObj.getLpTenorBucketApplicationCode());
				CommonUtils.addToQuery("LP_TENOR_APPLICATION_CODE = ?", strBufApprove);
			}
			if(ValidationUtil.isValid(dObj.getEffectiveDate())){
				params.addElement(dObj.getEffectiveDate());
				if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
					CommonUtils.addToQuery("EFFECTIVE_DATE <= "+dateConvert+" ", strBufApprove);
				}else {
					CommonUtils.addToQuery("EFFECTIVE_DATE <= TO_DATE(?,'DD-MM-RRRR') ", strBufApprove);
				}
			}
			if(!"-1".equalsIgnoreCase(dObj.getVisionSbu())){
				params.addElement(dObj.getVisionSbu());
				CommonUtils.addToQuery("NVL(VISION_SBU_ATTRIBUTE,'000') = ?", strBufApprove);
			}
			if(ValidationUtil.isValid(dObj.getCurrency())){
				params.addElement(dObj.getCurrency());
				CommonUtils.addToQuery(" CURRENCY = ? ", strBufApprove);
			}
			String orderBy = " Order By EFFECTIVE_DATE DESC";
			return getQueryPopupResults(vObject,new StringBuffer(), strBufApprove, "", orderBy, params,getPopMapperPremium());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResultPremium(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 6;
		String strBufApprove = new String("SELECT T1.FTP_CURVE_ID,TO_CHAR(T1.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,T1.FTP_TENOR_APPLICATION_CODE,T1.FTP_TENOR_CODE,"+
								" T1.LP_TENOR_APPLICATION_CODE,T1.LP_TENOR_CODE,"+ 
								" T1.VISION_SBU_ATTRIBUTE,T1.CURRENCY,T1.LP_RATE_ID,"+
								" (SELECT TO_CHAR(LP_RATE,'9,990.999999990') LP_RATE FROM LP_TERM_RATES WHERE "+
								" LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM LP_TERM_RATES "+
								" WHERE LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE <= T1.EFFECTIVE_DATE)) LP_RATE "+
								" FROM FTP_PREMIUMS T1 WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = to_date(?,'DD-MM-RRRR') AND FTP_TENOR_APPLICATION_CODE  = ? "+ 
								" AND LP_TENOR_APPLICATION_CODE = ? "+
								" AND NVL(VISION_SBU_ATTRIBUTE,'000') = ? AND CURRENCY =?");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new String("SELECT T1.FTP_CURVE_ID,format(T1.EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,T1.FTP_TENOR_APPLICATION_CODE,T1.FTP_TENOR_CODE,"+
					" T1.LP_TENOR_APPLICATION_CODE,T1.LP_TENOR_CODE,"+ 
					" T1.VISION_SBU_ATTRIBUTE,T1.CURRENCY,T1.LP_RATE_ID,"+
					" (SELECT format(LP_RATE,'#,##0.000000000') LP_RATE FROM LP_TERM_RATES WHERE "+
					" LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM LP_TERM_RATES "+
					" WHERE LP_RATE_ID = T1.LP_RATE_ID AND EFFECTIVE_DATE <= T1.EFFECTIVE_DATE)) LP_RATE "+
					" FROM FTP_PREMIUMS T1 WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = to_date(?,'dd-MM-yyyy') AND FTP_TENOR_APPLICATION_CODE  = ? "+ 
					" AND LP_TENOR_APPLICATION_CODE = ? "+
					" AND "+nullFun+"(VISION_SBU_ATTRIBUTE,'000') = ? AND CURRENCY =?");
		}
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpCurveId();
		objParams[1] = dObj.getEffectiveDate();
		objParams[2] = dObj.getTenorBucketApplicationCode();
		objParams[3] = dObj.getLpTenorBucketApplicationCode();
		objParams[4] = dObj.getVisionSbu();
		objParams[5] = dObj.getCurrency();
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getPremiumMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResultsAddOn(String ftpCurve,String effectiveDate,FTPGroupsVb dObj,Boolean flag){
		dObj.setRecordIndicator(0);
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer();
		if(flag){
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,TO_CHAR(ADDON_RATE,'9,990.999999990') ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'9,990.999999990') SUBSIDY_RATE,FTP_ADDON_STATUS,"
					+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON T1"
					+ " WHERE EFFECTIVE_DATE = (Select max(Effective_date) from FTP_ADDON T2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID AND T2.EFFECTIVE_DATE <= TO_DATE('"+effectiveDate+"','DD-MM-RRRR')) ");
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,format(EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,format(ADDON_RATE,'#,##0.000000000') ADDON_RATE,format(SUBSIDY_RATE,'#,##0.000000000') SUBSIDY_RATE,FTP_ADDON_STATUS,"
						+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON T1"
						+ " WHERE EFFECTIVE_DATE = (Select max(Effective_date) from FTP_ADDON T2 WHERE t2.FTP_CURVE_ID = T1.FTP_CURVE_ID AND T2.EFFECTIVE_DATE <= conver(datetime, '"+effectiveDate+"',103)) ");
			}
		}else{
			strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,TO_CHAR(ADDON_RATE,'9,990.999999990') ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'9,990.999999990') SUBSIDY_RATE,FTP_ADDON_STATUS,"
					+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON"
					+ " WHERE EFFECTIVE_DATE = (Select max(Effective_date) from FTP_ADDON where EFFECTIVE_DATE = TO_DATE('"+effectiveDate+"','DD-MM-RRRR')) ");
			if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
				strBufApprove =new StringBuffer("SELECT FTP_CURVE_ID,format(EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,format(ADDON_RATE,'#,##0.000000000') ADDON_RATE,TO_CHAR(SUBSIDY_RATE,'#,##0.000000000') SUBSIDY_RATE,FTP_ADDON_STATUS,"
						+ " RECORD_INDICATOR,MAKER,VERIFIER,INTERNAL_STATUS,DATE_LAST_MODIFIED,DATE_CREATION FROM FTP_ADDON"
						+ " WHERE EFFECTIVE_DATE = (Select max(Effective_date) from FTP_ADDON where EFFECTIVE_DATE = conver(datetime, '"+effectiveDate+"',103) ");
			}
		}		
		try{
			if (ValidationUtil.isValid(ftpCurve))
			{
				params.addElement("%" + ftpCurve.toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(FTP_CURVE_ID) like ?", strBufApprove);
			}
			String orderBy = " Order By CUSTOMER_ID";
//			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getAddOnMapper());
			
			Object objParams[]= objParams = new Object[params.size()];

			for(int Ctr=0; Ctr < params.size(); Ctr++)
				objParams[Ctr] = (Object) params.elementAt(Ctr);
			
//			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getCurveMapper());
			return  getJdbcTemplate().query(strBufApprove.toString()+" "+orderBy, objParams, getAddOnMapper());
			
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	protected ExceptionCode doInsertApprRecordForNonTrans(FTPGroupsVb vObject) throws RuntimeCustomException{
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList<FTPGroupsVb> collTempGroups = new ArrayList<FTPGroupsVb>();
		collTempGroups = (ArrayList<FTPGroupsVb>)getQueryResultGroup(vObject);
		int retVal = 0;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		vObject.setVerifier(getIntCurrentUserId());
		if(collTempGroups.size() > 0){
			int intStaticDeletionFlag = getStatus(((ArrayList<FTPGroupsVb>)collTempGroups).get(0));
			if (intStaticDeletionFlag == Constants.PASSIVATE){
				logger.error("Collection size is greater than zero - Duplicate record found, but inactive");
				exceptionCode = getResultObject(Constants.RECORD_ALREADY_PRESENT_BUT_INACTIVE);
				throw buildRuntimeCustomException(exceptionCode);
			}else{
				logger.error("Collection size is greater than zero - Duplicate record found");
				exceptionCode = getResultObject(Constants.DUPLICATE_KEY_INSERTION);
				throw buildRuntimeCustomException(exceptionCode);
			}
		}
		if(getCountFtpReference(vObject) == 0){
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorSevr("F");
			exceptionCode.setErrorMsg("FTP Reference["+vObject.getFtpReference()+"],Source Reference & Method Reference combination is not maintained in the FTP Controls table");
			throw buildRuntimeCustomException(exceptionCode);
		}
		if(collTempGroups != null && collTempGroups.size() == 0){
			retVal = doInsertionGroups(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
			}
			retVal = doInsertionControls(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					throw buildRuntimeCustomException(exceptionCode);
			}
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public int doUpdateDefaultFlag(FTPGroupsVb vObject){
		String query = "Update FTP_GROUPS Set FTP_DEFAULT_FLAG = 'N' " + 
				" Where COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? ";
		Object args[] = {vObject.getCountry(),vObject.getLeBook(),vObject.getDataSource(),vObject.getFtpGroup()};
		return getJdbcTemplate().update(query,args);
	}
	public int doUpdateAppr(FTPGroupsVb vObject){
		String query = "Update FTP_GROUPS Set FTP_DEFAULT_FLAG = ?,FTP_GROUP_SEQ = ?,FTP_GRP_STATUS=? " + 
				" Where COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? "+
				" AND FTP_REFERENCE = ? ";
		Object args[] = {vObject.getDefaultGroup(),vObject.getGroupSeq(),vObject.getFtpGroupStatus(),vObject.getCountry(),
				vObject.getLeBook(),vObject.getDataSource(),vObject.getFtpGroup(),vObject.getFtpReference()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected int doDeleteAppr(FTPGroupsVb vObject){
		String query = "Delete From FTP_GROUPS Where " + 
		"COUNTRY = ? And LE_BOOK = ?  And DATA_SOURCE = ? And FTP_GROUP = ? AND FTP_REFERENCE = ? ";

		Object args[] = {vObject.getCountry(),vObject.getLeBook(),vObject.getDataSource(),vObject.getFtpGroup(),vObject.getFtpReference()};
		return getJdbcTemplate().update(query,args);
	}
	protected int deleteFtpCurves(FTPCurveVb vObject){
		String query = "DELETE FROM FTP_CURVES WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = "+dateConvert;
		Object args[] = {vObject.getFtpCurveId(),vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	protected int deleteFtpAddOn(FTPCurveVb vObject){
		String query = "DELETE FROM FTP_ADDON WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = "+dateConvert;
		Object args[] = {vObject.getFtpCurveId(),vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	protected int deleteFtpPremium(FTPCurveVb vObject){
		String query = "DELETE FROM FTP_PREMIUMS WHERE FTP_CURVE_ID = ? AND EFFECTIVE_DATE = "+dateConvert+" AND FTP_TENOR_APPLICATION_CODE  = ? "
				+" AND FTP_TENOR_CODE = ? AND LP_TENOR_APPLICATION_CODE = ? "
				+" AND LP_TENOR_CODE = ? AND VISION_SBU_ATTRIBUTE = ? AND CURRENCY =?";
		Object args[] = {vObject.getFtpCurveId(),vObject.getEffectiveDate(),vObject.getTenorBucketApplicationCode(),vObject.getTenorBucketCode(),
				vObject.getLpTenorBucketApplicationCode(),vObject.getLpTenorBucketCode(),vObject.getVisionSbu(),vObject.getCurrency()};
		return getJdbcTemplate().update(query,args);
	}
	protected int doUpdateApprMethods(FtpMethodsVb vObject){
		String query = "Update FTP_METHODS Set METHOD_DESCRIPTION = ?,FTP_CURVE_ID = ?, " +
				"METHOD_TYPE = ?, METHOD_SUB_TYPE=?, FTP_TENOR_TYPE = ?,METHOD_BAL_TYPE = ?,FTP_MT_STATUS = ?,LP_TENOR_TYPE = ?,MAKER = ?, VERIFIER = ?, " +
				"INTERNAL_STATUS = ?,INTEREST_BASIS = ?,FTP_APPLY_RATE = ?,ADDON_APPLY_RATE = ?,LP_APPLY_RATE= ?,DATE_LAST_MODIFIED = SysDate Where METHOD_REFERENCE = ? and REPRICING_FLAG = ? ";
		Object[] args = {vObject.getMethodDescription(),vObject.getFtpCurveId(),
				vObject.getMethodType(), vObject.getMethodSubType(), vObject.getFtpTenorType(),vObject.getMethodBalType(),
				vObject.getStatus(),
				vObject.getLpTenorType(),
				vObject.getMaker(),
				vObject.getVerifier(), vObject.getInternalStatus(),vObject.getInterestBasis(),vObject.getFtpApplyRate(),vObject.getAddonApplyRate(),vObject.getLpApplyRate(),vObject.getMethodReference(),vObject.getRepricingFlag()};
		return getJdbcTemplate().update(query,args);
	}
	@Override
	protected ExceptionCode doUpdateApprRecordForNonTrans(FTPGroupsVb vObject) throws RuntimeCustomException  {
		List<FTPGroupsVb> collTemp = null;
		FTPGroupsVb vObjectlocal = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		if("RUNNING".equalsIgnoreCase(getBuildStatus(vObject))){
			exceptionCode = getResultObject(Constants.BUILD_IS_RUNNING);
			throw buildRuntimeCustomException(exceptionCode);
		}
		/*if (getQueryResultsGroup(vObject) == 0){
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}*/
		// Even if record is not there in Appr. table reject the record
		collTemp  = getQueryResultsGroup(vObject);
		if (collTemp == null || collTemp.size() == 0){
			exceptionCode = getResultObject(Constants.ATTEMPT_TO_MODIFY_UNEXISTING_RECORD);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setVerifier(getIntCurrentUserId());
		int retVal1 = doUpdateDefaultFlag(vObject);
		retVal = doUpdateAppr(vObject);
		if (retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		}
		String systemDate = getSystemDate();
		vObject.setDateLastModified(systemDate);
		//exceptionCode = writeAuditLog(vObject, vObjectlocal);
		/*if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR);
			throw buildRuntimeCustomException(exceptionCode);
		}*/
		exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
		return exceptionCode;
	}
	@Override
	protected String getAuditString(FTPGroupsVb vObject)
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
	public int doInsertionFtpCurves(FTPCurveVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_CURVES( " + 
			" FTP_CURVE_ID, EFFECTIVE_DATE, TENOR_APPLICATION_CODE, TENOR_CODE, VISION_SBU_ATTRIBUTE, CURRENCY,INT_RATE_START, INT_RATE_END, AMOUNT_START, " + 
			" AMOUNT_END, FTP_RATE_ID,FTP_CURVE_STATUS, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getFtpCurveId(), vObject.getEffectiveDate(), vObject.getTenorBucketApplicationCode(),
			 vObject.getTenorBucketCode(), vObject.getVisionSbu(), vObject.getCurrency(), vObject.getIntStart().replaceAll(",", ""),
			 vObject.getIntEnd().replaceAll(",", ""), vObject.getAmountStart().replaceAll(",", ""),vObject.getAmountEnd().replaceAll(",", ""),
			 vObject.getFtpRateId(), vObject.getFtpCurveStatus(),vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public int doInsertionFtpAddOn(FTPCurveVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_ADDON( " + 
			" FTP_CURVE_ID, EFFECTIVE_DATE,CUSTOMER_ID,CONTRACT_ID,ADDON_RATE,SUBSIDY_RATE,FTP_ADDON_STATUS, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?,"+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getFtpCurveId(), vObject.getEffectiveDate(), vObject.getCustomerId(),
			 vObject.getContractId(), vObject.getAddOnRate().replaceAll(",", ""), vObject.getSubRate().replaceAll(",",""),
			 vObject.getFtpCurveStatus(),vObject.getRecordIndicator(),
			 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public int doInsertionFtpPremium(FTPCurveVb vObject){
		setServiceDefaults();
		String query = "Insert Into FTP_PREMIUMS( " + 
			" FTP_CURVE_ID, EFFECTIVE_DATE,FTP_TENOR_APPLICATION_CODE,FTP_TENOR_CODE,LP_TENOR_APPLICATION_CODE,LP_TENOR_CODE, "+
			" VISION_SBU_ATTRIBUTE,CURRENCY,LP_RATE_ID,FTP_PREMIUM_STATUS, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,"+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getFtpCurveId(), vObject.getEffectiveDate(), vObject.getTenorBucketApplicationCode(),vObject.getTenorBucketCode(),
				vObject.getLpTenorBucketApplicationCode(),vObject.getLpTenorBucketCode(),vObject.getVisionSbu(),vObject.getCurrency(),vObject.getFtpRateId(),
				vObject.getFtpCurveStatus(),vObject.getRecordIndicator(),
				vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
		return getJdbcTemplate().update(query,args);
	}
	public ExceptionCode updateSourceMethod(ExceptionCode exceptionCode,List<FTPSourceConfigVb> ftpSoureList,FtpMethodsVb ftpMethodsVb){
		setServiceDefaults();
		ExceptionCode exceptionCode1 = new ExceptionCode();
		int retVal = 0;
		for(FTPSourceConfigVb vObjVb : ftpSoureList){
			if(vObjVb.isChecked()){
				if(!vObjVb.isNewRecord()){
					retVal = getFtpSourceConfigDao().doUpdateAppr(vObjVb);	
				}
				if (retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode1 = getResultObject(retVal);
					return exceptionCode1;
					//throw buildRuntimeCustomException(exceptionCode1);
				}
			}
		}
		retVal = doUpdateApprMethods(ftpMethodsVb);
		if (retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode1 = getResultObject(retVal);
			return exceptionCode1;
			//throw buildRuntimeCustomException(exceptionCode1);
		}
		exceptionCode1 = getResultObject(retVal);
		return exceptionCode1;
	}
	@Transactional(rollbackForClassName={"com.vision.exception.RuntimeCustomException"})
	public ExceptionCode addModifyFtpCurve(List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		collTemp  = getQueryResultCurves(vObject);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpCurves(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		for(FTPCurveVb vObjVb : ftpCurveList){
			/*if(vObjVb.isChecked()){*/
				vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
				vObjVb.setMaker(getIntCurrentUserId());
				vObjVb.setVerifier(getIntCurrentUserId());
				retVal = doInsertionFtpCurves(vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
			/*}*/
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public String businessDay(){
		String sql ="SELECT TO_CHAR(BUSINESS_DATE,'DD-MM-RRRR') BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE "+ 
				" COUNTRY = (SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_COUNTRY') "+
				" AND LE_BOOK =(SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_LE_BOOK')";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			sql ="SELECT format(BUSINESS_DATE,'dd-MM-yyyy') BUSINESS_DATE FROM VISION_BUSINESS_DAY WHERE "+ 
					" COUNTRY = (SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_COUNTRY') "+
					" AND LE_BOOK =(SELECT VALUE FROM VISION_VARIABLES WHERE VARIABLE ='DEFAULT_LE_BOOK')";
		}
		return getJdbcTemplate().queryForObject(sql,null,String.class);
	}
	@Override
	protected int getStatus(FTPGroupsVb records){return records.getFtpGroupStatus();}

	@Override
	protected void setStatus(FTPGroupsVb vObject,int status){vObject.setFtpGroupStatus(status);}
	@Transactional(rollbackForClassName={"com.vision.exception.RuntimeCustomException"})
	public ExceptionCode addModifyFtpAddOn(List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		collTemp  = getQueryResultAddOn(vObject);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpAddOn(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		for(FTPCurveVb vObjVb : ftpCurveList){
			/*if(vObjVb.isChecked()){*/
				vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
				vObjVb.setMaker(getIntCurrentUserId());
				vObjVb.setVerifier(getIntCurrentUserId());
				retVal = doInsertionFtpAddOn(vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
			/*}*/
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public List<FTPGroupsVb> getTenorCode(int tenorAppCode){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		String strBufApprove = new String("SELECT TENOR_CODE,TENOR_DESCRIPTION FROM TENOR_BUCKETS WHERE TENOR_APPLICATION_CODE = ? AND TENOR_STATUS = 0 ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = tenorAppCode;
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getTenorBucketsMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	@Transactional(rollbackForClassName={"com.vision.exception.RuntimeCustomException"})
	public ExceptionCode addModifyFtpPremium(ExceptionCode exceptionCode,List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		for(FTPCurveVb vObjVb : ftpCurveList){
			/*if(vObjVb.isChecked()){*/
				collTemp  = getQueryResultPremium(vObjVb);
				if (collTemp!=null && collTemp.size() > 0){
					retVal = deleteFtpPremium(vObjVb);
					if(retVal != Constants.SUCCESSFUL_OPERATION){
						exceptionCode = getResultObject(retVal);
						return exceptionCode;
					}
				}
				vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
				vObjVb.setFtpCurveStatus(Constants.STATUS_ZERO);
				vObjVb.setMaker(getIntCurrentUserId());
				vObjVb.setVerifier(getIntCurrentUserId());
				retVal = doInsertionFtpPremium(vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
				exceptionCode =  addLpRates(exceptionCode,vObjVb);
				if(retVal != Constants.SUCCESSFUL_OPERATION){
					exceptionCode = getResultObject(retVal);
					return exceptionCode;
				}
			/*}*/
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public List<FTPGroupsVb> getQueryResultLPRates(String lpRateId,String effectiveDate){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("Select TAppr.LP_RATE_ID,TO_CHAR(TAppr.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TO_CHAR(TAppr.LP_RATE,'9,999.999990') LP_RATE From LP_TERM_RATES TAppr Where TAppr.LP_RATE_ID = ?"+
					" AND TAppr.EFFECTIVE_DATE = (Select Max(Effective_date) from lp_term_rates where Effective_date <= TO_DATE(?,'DD-MM-RRRR') and lp_rate_id = Tappr.Lp_rate_ID)");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new String("Select TAppr.LP_RATE_ID,fromat(TAppr.EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,"+
					" format(TAppr.LP_RATE,'#,##0.000000') LP_RATE From LP_TERM_RATES TAppr Where TAppr.LP_RATE_ID = ?"+
					" AND TAppr.EFFECTIVE_DATE = (Select Max(Effective_date) from lp_term_rates where Effective_date <= "+dateConvert+" and lp_rate_id = Tappr.Lp_rate_ID)");
		}
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = lpRateId;
		objParams[1] = effectiveDate;
		
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getlpRateMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getCheckResultLPRates(String lpRateId,String effectiveDate){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("Select TAppr.LP_RATE_ID,TO_CHAR(TAppr.EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,"+
					" TO_CHAR(TAppr.LP_RATE,'9,999.999990') LP_RATE From LP_TERM_RATES TAppr Where TAppr.LP_RATE_ID = ?"+
					" AND TAppr.EFFECTIVE_DATE = TO_DATE(?,'DD-MM-RRRR') ");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new String("Select TAppr.LP_RATE_ID,format(TAppr.EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,"+
					" format(TAppr.LP_RATE,'#,##0.000000') LP_RATE From LP_TERM_RATES TAppr Where TAppr.LP_RATE_ID = ?"+
					" AND TAppr.EFFECTIVE_DATE = "+dateConvert+" ");
		}
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = lpRateId;
		objParams[1] = effectiveDate;
		
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getlpRateMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	protected RowMapper getlpRateMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpRateId(rs.getString("LP_RATE_ID"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setSubRate(rs.getString("LP_RATE"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected int deleteFtpLPRates(FTPCurveVb vObject){
		String query = "DELETE FROM LP_TERM_RATES WHERE LP_RATE_ID = ? AND EFFECTIVE_DATE = "+dateConvert;
		Object args[] = {vObject.getFtpRateId(),ValidationUtil.isValid(vObject.getRateEffectiveDate())?vObject.getRateEffectiveDate():vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	
	protected int doInsertionLPRates(FTPCurveVb vObject){
		String query = "Insert Into LP_TERM_RATES( " + 
		"LP_RATE_ID, EFFECTIVE_DATE, LP_RATE, LP_RATE_STATUS_NT, LP_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
			" Values (?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, ?, "+systemDate+", "+systemDate+")";

		Object args[] = {vObject.getFtpRateId(),ValidationUtil.isValid(vObject.getRateEffectiveDate())?vObject.getRateEffectiveDate():vObject.getEffectiveDate(), vObject.getSubRate().replaceAll(",", ""),
			 vObject.getFtpCurveStatusNt(), vObject.getFtpCurveStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};

		return getJdbcTemplate().update(query,args);
	}
	public ExceptionCode addLpRates(ExceptionCode exceptionCode,FTPCurveVb vObjVb){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObjVb.setMaker(getIntCurrentUserId());
		int retVal = 0;
		String effectiveDate = "";
		if(ValidationUtil.isValid(vObjVb.getRateEffectiveDate()))
			effectiveDate = vObjVb.getRateEffectiveDate();
		else
			effectiveDate = vObjVb.getEffectiveDate();
		collTemp  = getQueryResultLPRates(vObjVb.getFtpRateId(),effectiveDate);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpLPRates(vObjVb);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		vObjVb.setRecordIndicator(Constants.STATUS_ZERO);
		vObjVb.setFtpCurveStatus(Constants.STATUS_ZERO);
		vObjVb.setMaker(getIntCurrentUserId());
		vObjVb.setVerifier(getIntCurrentUserId());
		retVal = doInsertionLPRates(vObjVb);
		if(retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			return exceptionCode;
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public List<FTPGroupsVb> getQueryResultFtpRates(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("SELECT FTP_RATE_ID,TO_CHAR(FTP_CURVE,'9,990.999999990') FTP_CURVE,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,FTP_RATE_STATUS,"
				 +" RECORD_INDICATOR_NT,RECORD_INDICATOR from FTP_TERM_RATES t1 WHERE FTP_RATE_STATUS = 0 AND FTP_RATE_ID = ? and Effective_Date = "
				 +" (Select Max(Effective_date) from FTP_TERM_RATES where Effective_date <= TO_DATE(?,'DD-MM-RRRR') and FTP_RATE_ID = t1.FTP_RATE_ID) ");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new String("SELECT FTP_RATE_ID,format(FTP_CURVE,'#,##0.000000000') FTP_CURVE,format(EFFECTIVE_DATE,'dd-MM-yyyy') EFFECTIVE_DATE,FTP_RATE_STATUS,"
					 +" RECORD_INDICATOR_NT,RECORD_INDICATOR from FTP_TERM_RATES t1 WHERE FTP_RATE_STATUS = 0 AND FTP_RATE_ID = ? and Effective_Date = "
					 +" (Select Max(Effective_date) from FTP_TERM_RATES where Effective_date <= "+dateConvert+" and FTP_RATE_ID = t1.FTP_RATE_ID) ");
		}
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpRateId();
		objParams[1] = dObj.getEffectiveDate();

		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getFtpRatesMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	public List<FTPGroupsVb> getCheckResultFtpRates(FTPCurveVb dObj){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 2;
		String strBufApprove = new String("SELECT FTP_RATE_ID,TO_CHAR(FTP_CURVE,'9,990.999999990') FTP_CURVE,TO_CHAR(EFFECTIVE_DATE,'DD-MM-RRRR') EFFECTIVE_DATE,FTP_RATE_STATUS,"
				 +" RECORD_INDICATOR_NT,RECORD_INDICATOR from FTP_TERM_RATES t1 WHERE FTP_RATE_STATUS = 0 AND FTP_RATE_ID = ? and "
				 +" Effective_date = TO_DATE(?,'DD-MM-RRRR') ");
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new String("SELECT FTP_RATE_ID,format(FTP_CURVE,'#,##0.000000000') FTP_CURVE,format(EFFECTIVE_DATE,'dd-MM-yyyyy') EFFECTIVE_DATE,FTP_RATE_STATUS,"
					 +" RECORD_INDICATOR_NT,RECORD_INDICATOR from FTP_TERM_RATES t1 WHERE FTP_RATE_STATUS = 0 AND FTP_RATE_ID = ? and "
					 +" Effective_date = "+dateConvert+" ");
		}
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = dObj.getFtpRateId();
		objParams[1] = dObj.getEffectiveDate();

		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getFtpRatesMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	protected RowMapper getFtpRatesMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPCurveVb ftpSourceConfigVb = new FTPCurveVb();
				ftpSourceConfigVb.setFtpRateId(rs.getString("FTP_RATE_ID"));
				ftpSourceConfigVb.setSubRate(rs.getString("FTP_CURVE"));
				ftpSourceConfigVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpSourceConfigVb.setFtpCurveStatus(rs.getInt("FTP_RATE_STATUS"));
				ftpSourceConfigVb.setDbStatus(rs.getInt("FTP_RATE_STATUS"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR_NT"));
				ftpSourceConfigVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				return ftpSourceConfigVb;
			}
		};
		return mapper;
	}
	protected int deleteFtpRates(FTPCurveVb vObject){
		String query = "Delete From FTP_TERM_RATES Where FTP_RATE_ID = ? AND EFFECTIVE_DATE = "+dateConvert; 
		Object args[] = {vObject.getFtpRateId(),vObject.getEffectiveDate()};
		return getJdbcTemplate().update(query,args);
	}
	protected int doInsertionFtpRates(FTPCurveVb vObject){
		String query = "Insert Into FTP_TERM_RATES( " + 
				" FTP_RATE_ID, FTP_CURVE, EFFECTIVE_DATE, FTP_RATE_STATUS_NT, FTP_RATE_STATUS,"+
				" RECORD_INDICATOR_NT,RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION) " + 
				" Values (?, ?, "+dateConvert+", ?, ?, ?, ?, ?, ?, ?, "+systemDate+","+systemDate+")";

			Object args[] = {vObject.getFtpRateId() ,vObject.getSubRate().replaceAll(",", ""),
				 vObject.getEffectiveDate(), vObject.getFtpCurveStatusNt(), vObject.getFtpCurveStatus(),
				 vObject.getRecordIndicatorNt(), vObject.getRecordIndicator(),
				 vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus()};
			return getJdbcTemplate().update(query,args);
	}
	public ExceptionCode addModifyFtpRates(ExceptionCode exceptionCode,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		collTemp  = getCheckResultFtpRates(vObject);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpRates(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setMaker(getIntCurrentUserId());
		vObject.setVerifier(getIntCurrentUserId());
		retVal = doInsertionFtpRates(vObject);
		if(retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			return exceptionCode;
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}
	public List<FTPGroupsVb> getVisionSbu(String visionId,String sessionId,String promptId){
		List<FTPGroupsVb> collTemp = null;
		final int intKeyFieldsCount = 3;
		String strBufApprove = new String("SELECT FIELD_1 AS VISION_SBU,FIELD_2 AS SBU_DESCRIPTION FROM PROMPTS_STG WHERE PROMPT_ID = ? AND VISION_ID = ? AND SESSION_ID = ? "+
						                  " ORDER BY SORT_FIELD ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = promptId;
		objParams[1] = visionId;
		objParams[2] = sessionId;
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove, objParams, getSbuMapper());
			return collTemp;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResults Exception :   ");
			logger.error(((strBufApprove == null) ? "strQueryAppr is Null" : strBufApprove.toString()));
			return null;
		}
	}
	protected RowMapper getSbuMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb vObjVb = new AlphaSubTabVb();
				vObjVb.setAlphaSubTab(rs.getString("VISION_SBU"));
				vObjVb.setDescription(rs.getString("SBU_DESCRIPTION"));
				return vObjVb;
			}
		};
		return mapper;
	}
	public List callProcToPopulateVisionSBUData(){
		setServiceDefaults();
		strErrorDesc = "";
		Connection con = null;
		CallableStatement cs =  null;
		ArrayList<FTPGroupsVb> sbuList = new ArrayList<FTPGroupsVb>();
		try{
			String sessionId = "";
			sessionId = String.valueOf(System.currentTimeMillis()); 
			String promptId = "PR088";
			con = getConnection();
			cs = con.prepareCall("{call PR_RS_PROMPT_SBU_PARENT(?,?,?,?,?)}");
			cs.setString(1, String.valueOf(intCurrentUserId));//VisionId
	        cs.setString(2, sessionId);//Session Id
	        cs.setString(3, promptId);//Prompt Id
	        cs.registerOutParameter(4, java.sql.Types.VARCHAR); //Status
	        cs.registerOutParameter(5, java.sql.Types.VARCHAR); //Error Message
			ResultSet rs = cs.executeQuery();
		    rs.close();
		    /*if("-1".equalsIgnoreCase(cs.getString(4))){
		    	strErrorDesc = cs.getString(5);
				throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
		    }else{*/
		    	sbuList = (ArrayList<FTPGroupsVb>) getVisionSbu(String.valueOf(intCurrentUserId),sessionId,promptId);
		    /*}*/
		}catch(Exception ex){
			ex.printStackTrace();
			strErrorDesc = ex.getMessage().trim();
			throw buildRuntimeCustomException(getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION));
		}finally{
			JdbcUtils.closeStatement(cs);
//			DataSourceUtils.releaseConnection(con, getDataSource());
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
	return sbuList;
	}
	public ExceptionCode addModifyLtpRates(ExceptionCode exceptionCode,FTPCurveVb vObject){
		List<FTPGroupsVb> collTemp = null;
		strApproveOperation =Constants.MODIFY;
		strErrorDesc  = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		int retVal = 0;
		String effectiveDate ="";
		if(ValidationUtil.isValid(vObject.getRateEffectiveDate()))
			effectiveDate = vObject.getRateEffectiveDate();
		else
			effectiveDate = vObject.getEffectiveDate();
			collTemp  = getCheckResultLPRates(vObject.getFtpRateId(),effectiveDate);
		if (collTemp!=null && collTemp.size() > 0){
			retVal = deleteFtpLPRates(vObject);
			if(retVal != Constants.SUCCESSFUL_OPERATION){
				exceptionCode = getResultObject(retVal);
				return exceptionCode;
			}
		}
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setMaker(getIntCurrentUserId());
		vObject.setVerifier(getIntCurrentUserId());
		retVal = doInsertionLPRates(vObject);
		if(retVal != Constants.SUCCESSFUL_OPERATION){
			exceptionCode = getResultObject(retVal);
			return exceptionCode;
		}
		exceptionCode = getResultObject(retVal);
		return exceptionCode;
	}	
	
	public int getResults(FTPCurveVb ftpCurveVb) {
		String query = "Select Count(1) FROM FTP_CURVES Where FTP_CURVE_ID = '"+ftpCurveVb.getFtpCurveId()+"' AND EFFECTIVE_DATE <= TO_DATE('"+ftpCurveVb.getEffectiveDate()+"','DD-MM-RRRR') "; 
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Select Count(1) FROM FTP_CURVES Where FTP_CURVE_ID = '"+ftpCurveVb.getFtpCurveId()+"' AND EFFECTIVE_DATE <= CONVERT(datetime,'"+ftpCurveVb.getEffectiveDate()+"',103) ";
		}
		return getJdbcTemplate().queryForObject(query, Integer.class);
	}
	public int getResults1(FTPCurveVb ftpCurveVb) {
		String query = "Select Count(1) FROM FTP_ADDON Where FTP_CURVE_ID = '"+ftpCurveVb.getFtpCurveId()+"' AND EFFECTIVE_DATE <= TO_DATE('"+ftpCurveVb.getEffectiveDate()+"','DD-MM-RRRR') ";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Select Count(1) FROM FTP_ADDON Where FTP_CURVE_ID = '"+ftpCurveVb.getFtpCurveId()+"' AND EFFECTIVE_DATE <= CONVERT(datetime,'"+ftpCurveVb.getEffectiveDate()+"',103) ";
		}
		return getJdbcTemplate().queryForObject(query, Integer.class);
	}
	public int getResults2(FTPCurveVb ftpCurveVb) {
		String query = "Select Count(1) FROM FTP_PREMIUMS Where FTP_CURVE_ID = '"+ftpCurveVb.getFtpCurveId()+"' AND EFFECTIVE_DATE <= TO_DATE('"+ftpCurveVb.getEffectiveDate()+"','DD-MM-RRRR') ";
		if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Select Count(1) FROM FTP_PREMIUMS Where FTP_CURVE_ID = '"+ftpCurveVb.getFtpCurveId()+"' AND EFFECTIVE_DATE <= CONVERT(datetime,'"+ftpCurveVb.getEffectiveDate()+"',103) ";
		}
		return getJdbcTemplate().queryForObject(query, Integer.class);
	}
	public int getDefaultFlagCount(FTPGroupsVb ftpGroupsVb) {
		String query = "Select count(1) from Ftp_Groups where ftp_group = '"+ftpGroupsVb.getFtpGroup()+"' and Country = '"+ftpGroupsVb.getCountry()+"' and le_book ='"+ftpGroupsVb.getLeBook()+"' and Data_Source ='"+ftpGroupsVb.getDataSource()+"' and ftp_Default_Flag = 'Y'  "; 
		return getJdbcTemplate().queryForObject(query, Integer.class);
	}
	
	
	public List<FTPGroupsVb> getQueryPopupResultsByGroup(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("Select Distinct TAppr.COUNTRY, TAppr.LE_BOOK, " + 
			" TAppr.DATA_SOURCE,TAPPR.FTP_GROUP " + 
			" From FTP_GROUPS TAppr");
		StringBuffer strBufPending =new StringBuffer("");
		try{
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

					case "dataSource":
						CommonUtils.addToQuerySearch(" upper(TAppr.DATA_SOURCE) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.DATA_SOURCE) "+ val, strBufPending, data.getJoinType());
						break;

					case "ftpGroup":
						CommonUtils.addToQuerySearch(" upper(TAppr.FTP_GROUP) "+ val, strBufApprove, data.getJoinType());
						CommonUtils.addToQuerySearch(" upper(TPend.FTP_GROUP) "+ val, strBufPending, data.getJoinType());
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
			String orderBy = " Order By COUNTRY, LE_BOOK, DATA_SOURCE, FTP_GROUP ";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getQueryPopupMapper());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	public List<FTPGroupsVb> getQueryPopupResultsDetails(FTPGroupsVb dObj){
		List<FTPGroupsVb> collTemp = null;
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove =new StringBuffer("SELECT TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.DATA_SOURCE,\n" + 
				"(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = DATA_SOURCE_AT AND ALPHA_sub_TAB= DATA_SOURCE ) DATA_SOURCE_DESC\n" + 
				",TAppr.FTP_GROUP,(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB TAppr WHERE ALPHA_TAB = FTP_GROUP_AT AND ALPHA_sub_TAB= FTP_GROUP ) FTP_GROUP_DESC"
				+ ",TAppr.FTP_REFERENCE,TAppr.FTP_GROUP_SEQ,\n" + 
				"				T2.FTP_DESCRIPTION,T2.SOURCE_REFERENCE,\n" + 
				"(SELECT  ALPHA_SUBTAB_DESCRIPTION FROM ALPHA_SUB_TAB T1 WHERE ALPHA_TAB = SOURCE_REFERENCE_AT AND ALPHA_sub_TAB= SOURCE_REFERENCE ) SOURCE_REFERENCE_DESC,\n" + 
				"				T2.METHOD_REFERENCE,\n" + 
				"				(SELECT MIN(METHOD_DESCRIPTION) FROM FTP_METHODS WHERE METHOD_REFERENCE = T2.METHOD_REFERENCE) METHOD_DESCRIPTION,\n" + 
				"				TAppr.FTP_DEFAULT_FLAG,\n" + 
				"				TAppr.FTP_GRP_STATUS, "+VariableStatusNtApprDesc
				+ ",TAppr.RECORD_INDICATOR, "+RecordIndicatorNtApprDesc
				+ ",TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS,TAppr.DATE_CREATION,TAppr.DATE_LAST_MODIFIED \n" + 
				"				FROM FTP_GROUPS TAppr,FTP_CONTROLS T2");
		StringBuffer strBufPending =new StringBuffer("");
		try{
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement("%" + dObj.getCountry().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY) like ?", strBufApprove);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement("%" + dObj.getLeBook().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK) like ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getDataSource())){
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
			}
			if (!"-1".equalsIgnoreCase(dObj.getFtpGroup())){
				params.addElement(dObj.getFtpGroup());
				CommonUtils.addToQuery("TAppr.FTP_GROUP = ?", strBufApprove);
			}
			//check if the column [RECORD_INDICATOR] should be included in the query
			if (dObj.getRecordIndicator() != -1){
				if (dObj.getRecordIndicator() > 3){
					params.addElement(new Integer(0));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR > ?", strBufApprove);
				}else{
					params.addElement(new Integer(dObj.getRecordIndicator()));
					CommonUtils.addToQuery("TAppr.RECORD_INDICATOR = ?", strBufApprove);
				}
			}
			CommonUtils.addToQuery(" TAppr.COUNTRY = T2.COUNTRY"+
					"	AND TAppr.LE_BOOK = T2.LE_BOOK"+
					"	AND TAppr.FTP_REFERENCE = T2.FTP_REFERENCE ",strBufApprove);
			
			
			String orderBy = " ORDER BY TAppr.COUNTRY, TAppr.LE_BOOK, DATA_SOURCE, FTP_GROUP, SOURCE_REFERENCE, FTP_GROUP_SEQ ";
			
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove, "", orderBy, params,getQueryPopupMapperDetails());
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
