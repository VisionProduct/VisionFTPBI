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
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.FTPRatesVb;
import com.vision.vb.VisionUsersVb;

@Component
public class FTPRatesDao extends AbstractDao<FTPRatesVb> {
	
	public RowMapper getQueryPopupMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPRatesVb ftpRatesVb = new FTPRatesVb();
				ftpRatesVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpRatesVb.setCountry(rs.getString("COUNTRY"));
				ftpRatesVb.setLeBook(rs.getString("LE_BOOK"));
				ftpRatesVb.setPoolCode(rs.getString("POOL_CODE"));
				ftpRatesVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				
				return ftpRatesVb;
			}
		};
		return mapper;
	}
	public RowMapper getDesciptionMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPRatesVb ftpRatesVb = new FTPRatesVb();
				ftpRatesVb.setPoolCodeDesc(rs.getString("POOL_CODE_DESCRIPTION"));
				return ftpRatesVb;
			}
		};
		return mapper;
	}
	public RowMapper getProductDescriptionMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPRatesVb ftpRatesVb = new FTPRatesVb();
				ftpRatesVb.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
				return ftpRatesVb;
			}
		};
		return mapper;
	}
	@Override
	protected RowMapper getMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPRatesVb ftpRatesVb = new FTPRatesVb();
				ftpRatesVb.setDataSourceAt(rs.getInt("DATA_SOURCE_AT"));
				ftpRatesVb.setDataSource(rs.getString("DATA_SOURCE"));
				ftpRatesVb.setCountry(rs.getString("COUNTRY"));
				ftpRatesVb.setLeBook(rs.getString("LE_BOOK"));
				ftpRatesVb.setCurrency(rs.getString("CURRENCY"));
				ftpRatesVb.setPoolCode(rs.getString("POOL_CODE"));
				if(rs.getString("INT_RATE_START")==null)
					ftpRatesVb.setInterestRateStart("");
				else
					ftpRatesVb.setInterestRateStart(rs.getString("INT_RATE_START"));
				if(rs.getString("INT_RATE_END")==null)
					ftpRatesVb.setInterestRateEnd("");
				else
					ftpRatesVb.setInterestRateEnd(rs.getString("INT_RATE_END"));
				if(rs.getString("AVG_START")==null)
					ftpRatesVb.setAvgStart("");
				else
					ftpRatesVb.setAvgStart(rs.getString("AVG_START"));
				if(rs.getString("AVG_END")==null)
					ftpRatesVb.setAvgEnd("");
				else
					ftpRatesVb.setAvgEnd(rs.getString("AVG_END"));
				
				if(rs.getString("OUC_ATTRIBUTE")==null)
					ftpRatesVb.setOucAttribute("");
				else
					ftpRatesVb.setOucAttribute(rs.getString("OUC_ATTRIBUTE"));
				if(rs.getString("OUC_ATTRIBUTE_LEVEL")==null)
					ftpRatesVb.setOucAttributeLevel("");
				else
					ftpRatesVb.setOucAttributeLevel(rs.getString("OUC_ATTRIBUTE_LEVEL"));
				if(rs.getString("RISK_ASSET_CLASS")==null)
					ftpRatesVb.setRiskAssetClass("");
				else
					ftpRatesVb.setRiskAssetClass(rs.getString("RISK_ASSET_CLASS"));
				if(rs.getString("LIQUIDITY_RATE")==null)
					ftpRatesVb.setLiquidityRate("");
				else
					ftpRatesVb.setLiquidityRate(rs.getString("LIQUIDITY_RATE"));
				if(rs.getString("NDIC_RATE")==null)
					ftpRatesVb.setNdicRate("");
				else
					ftpRatesVb.setNdicRate(rs.getString("NDIC_RATE"));
				ftpRatesVb.setDebitPoolRate(rs.getString("DEBIT_POOL_RATE"));
				ftpRatesVb.setCreditPoolRate(rs.getString("CREDIT_POOL_RATE"));
				ftpRatesVb.setPoolRateStatusNt(rs.getInt("POOL_RATE_STATUS_NT"));
				ftpRatesVb.setPoolRateStatus(rs.getInt("POOL_RATE_STATUS"));
				ftpRatesVb.setDbStatus(rs.getInt("POOL_RATE_STATUS"));
				ftpRatesVb.setRecordIndicatorNt(rs.getInt("RECORD_INDICATOR_NT"));
				ftpRatesVb.setRecordIndicator(rs.getInt("RECORD_INDICATOR"));
				ftpRatesVb.setMaker(rs.getInt("MAKER"));
				ftpRatesVb.setVerifier(rs.getInt("VERIFIER"));
				ftpRatesVb.setInternalStatus(rs.getInt("INTERNAL_STATUS"));
				ftpRatesVb.setDateLastModified(rs.getString("DATE_LAST_MODIFIED"));
				ftpRatesVb.setDateCreation(rs.getString("DATE_CREATION"));
				ftpRatesVb.setPoolCodeDesc(rs.getString("POOL_CODE_DESCRIPTION"));
				if(rs.getString("PRODUCT")!= null){
					ftpRatesVb.setProduct((rs.getString("PRODUCT")));
				}else{
					ftpRatesVb.setProduct("");
				}
				ftpRatesVb.setEffectiveDate(rs.getString("EFFECTIVE_DATE"));
				ftpRatesVb.setVisionSbu(rs.getString("VISION_SBU"));
				if(rs.getString("TENOR_START")==null)
					ftpRatesVb.setTenorRateStart("");
				else
					ftpRatesVb.setTenorRateStart(rs.getString("TENOR_START"));
				if(rs.getString("TENOR_END")==null)
					ftpRatesVb.setTenorRateEnd("");
				else
					ftpRatesVb.setTenorRateEnd(rs.getString("TENOR_END"));
				ftpRatesVb.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));

				return ftpRatesVb;
			}
		};
		return mapper;
	}
	public RowMapper getOucLevelMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				FTPRatesVb ftpRatesVb = new FTPRatesVb();
				ftpRatesVb.setOucAttributeLevel(rs.getString("OUC_ATTRIBUTE_LEVEL"));
				ftpRatesVb.setOucAttributeLevelDesc(rs.getString("OUC_ATTRIBUTE_LEVEL_DESC"));
				return ftpRatesVb;
			}
		};
		return mapper;
	}
	public List<AlphaSubTabVb> getOucAttributeLevel(){
		StringBuffer strQueryAppr = new StringBuffer("select OUC_ATTRIBUTE_LEVEL,OUC_ATTRIBUTE_LEVEL_DESC from ouc_attribute_levels");
		try
		{ 
			return getJdbcTemplate().query(strQueryAppr.toString(),getOucLevelMapper());
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			logger.error(((strQueryAppr == null) ? "strQueryAppr is null" : strQueryAppr.toString()));
			return null;
		}
	}
	public List<FTPRatesVb> getQueryPopupResults(FTPRatesVb dObj){
		if(ValidationUtil.isValid(dObj.getAvgStart())){
			dObj.setAvgStart(dObj.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(dObj.getAvgEnd())){
			dObj.setAvgEnd(dObj.getAvgEnd().replaceAll(",", ""));
		}
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = new StringBuffer("Select Distinct TAppr.DATA_SOURCE, " + 
			"  To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE, TAppr.COUNTRY, TAppr.LE_BOOK, TAppr.POOL_CODE " +
			" From FTP_RATES TAppr ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From FTP_RATES_PEND TPend Where " + 
			"TAppr.DATA_SOURCE = TPend.DATA_SOURCE " + 
			"And TAppr.COUNTRY = TPend.COUNTRY " + 
			"And TAppr.LE_BOOK = TPend.LE_BOOK " + 
			"And TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE " +
			"And TAppr.POOL_CODE = TPend.POOL_CODE )");

		StringBuffer strBufPending = new StringBuffer("Select Distinct TPend.DATA_SOURCE, " + 
			" To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,  TPend.COUNTRY, TPend.LE_BOOK, TPend.POOL_CODE " +
			" From FTP_RATES_PEND TPend ");


		try
		{
			if (ValidationUtil.isValid(dObj.getDataSource()) && !"-1".equalsIgnoreCase(dObj.getDataSource()))
			{
				params.addElement(dObj.getDataSource());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DATA_SOURCE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getVisionSbu()) && !"-1".equalsIgnoreCase(dObj.getVisionSbu()))
			{
				params.addElement(dObj.getVisionSbu());
				CommonUtils.addToQuery("TAppr.VISION_SBU = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.VISION_SBU = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getEffectiveDate())){
				params.addElement(dObj.getEffectiveDate().toUpperCase());
				CommonUtils.addToQuery("To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') = ?", strBufApprove);
				CommonUtils.addToQuery("To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getCountry()))
			{
				params.addElement(dObj.getCountry().toUpperCase());
				CommonUtils.addToQuery("UPPER(TAppr.COUNTRY)  = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.COUNTRY)  = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getLeBook()))
			{
				params.addElement(dObj.getLeBook().toUpperCase());
				CommonUtils.addToQuery("UPPER(TAppr.LE_BOOK)  = ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.LE_BOOK)  = ?", strBufPending);
			}
			/*if (ValidationUtil.isValid(dObj.getCurrency()))
			{
				params.addElement("%" + dObj.getCurrency().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.CURRENCY) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.CURRENCY) like ?", strBufPending);
			}*/
			if (ValidationUtil.isValid(dObj.getPoolCode()))
			{
				params.addElement("%" + dObj.getPoolCode().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.POOL_CODE) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.POOL_CODE) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getProduct()))
			{
				params.addElement("%" + dObj.getProduct().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.PRODUCT) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.PRODUCT) like ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getTenorRateStart()))
			{
				params.addElement(dObj.getTenorRateStart());
				CommonUtils.addToQuery("TAppr.TENOR_START =  ?", strBufApprove);
				CommonUtils.addToQuery("TPend.TENOR_START =  ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getTenorRateEnd()))
			{
				params.addElement(dObj.getTenorRateEnd());
				CommonUtils.addToQuery("TAppr.TENOR_END =  ?", strBufApprove);
				CommonUtils.addToQuery("TPend.TENOR_END =  ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getInterestRateStart()))
			{
				params.addElement(dObj.getInterestRateStart());
				CommonUtils.addToQuery("TAppr.INT_RATE_START = TO_NUMBER(?,'9,990.9999999') ", strBufApprove);
				CommonUtils.addToQuery("TPend.INT_RATE_START = TO_NUMBER(?,'9,990.9999999') ", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getInterestRateEnd()))
			{
				params.addElement(dObj.getInterestRateEnd());
				CommonUtils.addToQuery("TAppr.INT_RATE_END = TO_NUMBER(?,'9,990.9999999') ", strBufApprove);
				CommonUtils.addToQuery("TPend.INT_RATE_END = TO_NUMBER(?,'9,990.9999999') ", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAvgStart()))
			{
				params.addElement(dObj.getAvgStart());
				CommonUtils.addToQuery("TAppr.AVG_START = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.AVG_START = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getAvgEnd()))
			{
				params.addElement(dObj.getAvgEnd());
				CommonUtils.addToQuery("TAppr.AVG_END = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.AVG_END = ?", strBufPending);
			}
			/*if (ValidationUtil.isValid(dObj.getOucAttribute()))
			{
				params.addElement(dObj.getOucAttribute());
				CommonUtils.addToQuery("TAppr.OUC_ATTRIBUTE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.OUC_ATTRIBUTE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getOucAttributeLevel()))
			{
				params.addElement(dObj.getOucAttributeLevel());
				CommonUtils.addToQuery("TAppr.OUC_ATTRIBUTE_LEVEL = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.OUC_ATTRIBUTE_LEVEL = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getRiskAssetClass()))
			{
				params.addElement(dObj.getRiskAssetClass());
				CommonUtils.addToQuery("TAppr.RISK_ASSET_CLASS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.RISK_ASSET_CLASS = ?", strBufPending);
			}*/
			/*if (ValidationUtil.isValid(dObj.getLiquidityRate()))
			{
				params.addElement(dObj.getLiquidityRate());
				CommonUtils.addToQuery("TAppr.LIQUIDITY_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.LIQUIDITY_RATE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getNdicRate()))
			{
				params.addElement(dObj.getNdicRate());
				CommonUtils.addToQuery("TAppr.NDIC_RATE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.NDIC_RATE = ?", strBufPending);
			}*/
			if (ValidationUtil.isValid(dObj.getDebitPoolRate()))
			{
				params.addElement(dObj.getDebitPoolRate());
				CommonUtils.addToQuery("TAppr.DEBIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999') ", strBufApprove);
				CommonUtils.addToQuery("TPend.DEBIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999') ", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getCreditPoolRate()))
			{
				params.addElement(dObj.getCreditPoolRate());
				CommonUtils.addToQuery("TAppr.CREDIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999') ", strBufApprove);
				CommonUtils.addToQuery("TPend.CREDIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999') ", strBufPending);
			}
			if (dObj.getPoolRateStatus() != -1)
			{
				params.addElement(dObj.getPoolRateStatus());
				CommonUtils.addToQuery("TAppr.POOL_RATE_STATUS = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.POOL_RATE_STATUS = ?", strBufPending);
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
			//Update Restriction - UBA
			VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
			if(("Y".equalsIgnoreCase(visionUsersVb.getUpdateRestriction()) || "Y".equalsIgnoreCase(visionUsersVb.getAutoUpdateRestriction()))){
				if(ValidationUtil.isValid(visionUsersVb.getCountry())){
					CommonUtils.addToQuery("TAppr.COUNTRY||'-'||TAppr.LE_BOOK IN ("+visionUsersVb.getCountry()+") ", strBufApprove);
					CommonUtils.addToQuery("TPend.COUNTRY||'-'||TPend.LE_BOOK IN ("+visionUsersVb.getCountry()+") ", strBufPending);
				}
				if(ValidationUtil.isValid(visionUsersVb.getSbuCode())){
					CommonUtils.addToQuery("TAppr.VISION_SBU IN ("+visionUsersVb.getSbuCode()+") ", strBufApprove);
					CommonUtils.addToQuery("TPend.VISION_SBU IN ("+visionUsersVb.getSbuCode()+") ", strBufPending);
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

		String orderBy = " Order By  EFFECTIVE_DATE, DATA_SOURCE, COUNTRY, LE_BOOK, POOL_CODE ";
		return getQueryPopupResults(dObj,strBufPending, strBufApprove, strWhereNotExists, orderBy, params, getQueryPopupMapper());
	}

	public List<FTPRatesVb> getQueryResults(FTPRatesVb dObj, int intStatus){
		setServiceDefaults();
		Vector<Object> params = new Vector<Object>();
		setServiceDefaults();
		
		StringBuffer strBufApprove = new StringBuffer("Select TAppr.DATA_SOURCE_AT,TAppr.DATA_SOURCE, " + 
			"TAppr.VISION_SBU, To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE , TAppr.PRODUCT,"+
			" (Select Product_Description from Product_Codes PC where TAppr.Product = PC.Product) Product_Description,"+ 
			"TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.CURRENCY,TAppr.POOL_CODE, " +
			"TAppr.TENOR_START, TAppr.TENOR_END, " +
			"TRIM(TO_CHAR(TAppr.INT_RATE_START,'9,990.9999999')) INT_RATE_START, TRIM(TO_CHAR(TAppr.INT_RATE_END,'9,990.9999999')) INT_RATE_END, " +
			"TRIM(TO_CHAR(TAppr.AVG_START,'999,999,999,999,999,990.99999'))  AVG_START, TRIM(TO_CHAR(TAppr.AVG_END,'999,999,999,999,999,990.99999')) AVG_END," + 
			" TRIM(TO_CHAR(TAppr.DEBIT_POOL_RATE,'9,990.9999999')) AS DEBIT_POOL_RATE, TRIM(TO_CHAR(TAppr.CREDIT_POOL_RATE,'9,990.9999999')) AS CREDIT_POOL_RATE,TAppr.POOL_RATE_STATUS_NT,TAppr.POOL_RATE_STATUS, " + 
			"TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR,TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS, " + 
			"To_Char(TAppr.DATE_LAST_MODIFIED, 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED,To_Char(TAppr.DATE_CREATION, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION, TAppr.OUC_ATTRIBUTE, TAppr.OUC_ATTRIBUTE_LEVEL, TAppr.RISK_ASSET_CLASS,TRIM(TO_CHAR(TAppr.LIQUIDITY_RATE,'9,990.9999999')) LIQUIDITY_RATE, TRIM(TO_CHAR(TAppr.NDIC_RATE,'9,990.9999999')) NDIC_RATE ,PO.POOL_CODE_DESCRIPTION From FTP_RATES TAppr Join Pool_Codes PO on PO.COUNTRY = TAppr.COUNTRY AND PO.LE_BOOK = TAppr.LE_BOOK AND PO.POOL_CODE = TAppr.POOL_CODE ");

		String strWhereNotExists = new String(" Not Exists (Select 'X' From FTP_RATES_PEND TPend Where " + 
			"TAppr.DATA_SOURCE = TPend.DATA_SOURCE " + 
			"And NVL(TAppr.VISION_SBU, 0) = NVL(TPend.VISION_SBU, 0) " +
			"And TAppr.EFFECTIVE_DATE = TPend.EFFECTIVE_DATE " +
			"And NVL(TAppr.PRODUCT, 0) = NVL(TPend.PRODUCT, 0) " +
			"And NVL(TAppr.TENOR_START, 0) = NVL(TPend.TENOR_START, 0) " +
			"And NVL(TAppr.TENOR_END, 0) = NVL(TPend.TENOR_END, 0) " +
			"And TAppr.COUNTRY = TPend.COUNTRY " + 
			"And TAppr.LE_BOOK = TPend.LE_BOOK " + 
			"And TAppr.CURRENCY = TPend.CURRENCY " + 
			/*"And NVL(TAppr.INT_RATE_START,9999) = NVL(TPend.INT_RATE_START,9999) " +
			"And NVL(TAppr.INT_RATE_END,9999) = NVL(TPend.INT_RATE_END,9999) " +
			"And NVL(TAppr.AVG_START,0) = NVL(TPend.AVG_START,0) " +
			"And NVL(TAppr.AVG_END,0) = NVL(TPend.AVG_END,0) " +*/
			"And TAppr.POOL_CODE = TPend.POOL_CODE)");

		StringBuffer strBufPending = new StringBuffer("Select TPend.DATA_SOURCE_AT,TPend.DATA_SOURCE, " + 
				"TPend.VISION_SBU, To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE , TPend.PRODUCT,"+
				" (Select Product_Description from Product_Codes PC where TPend.Product = PC.Product) Product_Description,"+
				"TPend.COUNTRY,TPend.LE_BOOK,TPend.CURRENCY,TPend.POOL_CODE, " +
				"TPend.TENOR_START, TPend.TENOR_END, " +
				"TRIM(TO_CHAR(TPend.INT_RATE_START,'9,990.9999999')) INT_RATE_START, TRIM(TO_CHAR(TPend.INT_RATE_END,'9,990.9999999')) INT_RATE_END, " +
				"TRIM(TO_CHAR(TPend.AVG_START,'999,999,999,999,999,990.99999'))  AVG_START, TRIM(TO_CHAR(TPend.AVG_END,'999,999,999,999,999,990.99999')) AVG_END," + 
				" TRIM(TO_CHAR(TPend.DEBIT_POOL_RATE,'9,990.9999999')) AS DEBIT_POOL_RATE, TRIM(TO_CHAR(TPend.CREDIT_POOL_RATE,'9,990.9999999')) AS CREDIT_POOL_RATE,TPend.POOL_RATE_STATUS_NT,TPend.POOL_RATE_STATUS, " + 
				"TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR,TPend.MAKER,TPend.VERIFIER,TPend.INTERNAL_STATUS, " + 
				"To_Char(TPend.DATE_LAST_MODIFIED, 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED,To_Char(TPend.DATE_CREATION, " + 
				" 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION, TPend.OUC_ATTRIBUTE, TPend.OUC_ATTRIBUTE_LEVEL, TPend.RISK_ASSET_CLASS, TRIM(TO_CHAR(TPend.LIQUIDITY_RATE,'9,990.9999999')) LIQUIDITY_RATE, TRIM(TO_CHAR(TPend.NDIC_RATE,'9,990.9999999')) NDIC_RATE, PO.POOL_CODE_DESCRIPTION From FTP_RATES_Pend TPend Join Pool_Codes PO on PO.COUNTRY = TPend.COUNTRY AND PO.LE_BOOK = TPend.LE_BOOK AND PO.POOL_CODE = TPend.POOL_CODE ");

		try
		{
			if (ValidationUtil.isValid(dObj.getDataSource()) && !"-1".equalsIgnoreCase(dObj.getDataSource()))
			{
				params.addElement(dObj.getDataSource().toUpperCase());
				CommonUtils.addToQuery("TAppr.DATA_SOURCE = ?", strBufApprove);
				CommonUtils.addToQuery("TPend.DATA_SOURCE = ?", strBufPending);
			}
			if (ValidationUtil.isValid(dObj.getEffectiveDate())){
				params.addElement(dObj.getEffectiveDate().toUpperCase());
				CommonUtils.addToQuery("To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') = ?", strBufApprove);
				CommonUtils.addToQuery("To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') = ?", strBufPending);
			}
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
		    if (ValidationUtil.isValid(dObj.getCurrency()))
			{
				params.addElement("%" + dObj.getCurrency().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.CURRENCY) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.CURRENCY) like ?", strBufPending);
			} 
			if (ValidationUtil.isValid(dObj.getPoolCode()))
			{
				params.addElement("%" + dObj.getPoolCode().toUpperCase() + "%" );
				CommonUtils.addToQuery("UPPER(TAppr.POOL_CODE) like ?", strBufApprove);
				CommonUtils.addToQuery("UPPER(TPend.POOL_CODE) like ?", strBufPending);
			}
			
			if (dObj.getPoolRateStatus() != -1)
			{
				params.addElement(dObj.getPoolRateStatus());
				CommonUtils.addToQuery("TAppr.POOL_RATE_STATUS like ?", strBufApprove);
				CommonUtils.addToQuery("TPend.POOL_RATE_STATUS like ?", strBufPending);
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
			String orderBy = " Order By DATA_SOURCE, EFFECTIVE_DATE, VISION_SBU, COUNTRY, LE_BOOK, POOL_CODE, PRODUCT, TENOR_START, TENOR_END, INT_RATE_START, INT_RATE_END, AVG_START, AVG_END ";
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

	public List<FTPRatesVb> getQueryResultsForReview(FTPRatesVb dObj, int intStatus){
		List<FTPRatesVb> collTemp = null;
		final int intKeyFieldsCount = 8;
		Vector<Object> params= new Vector<Object>();
		StringBuffer strQueryAppr = new StringBuffer("Select TAppr.DATA_SOURCE_AT,TAppr.DATA_SOURCE, " + 
			" To_Char(TAppr.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE ,TAppr.VISION_SBU_AT,TAppr.VISION_SBU,TAppr.COUNTRY,TAppr.LE_BOOK,TAppr.CURRENCY,TAppr.POOL_CODE,TAppr.PRODUCT, " +
			" (Select Product_Description from Product_Codes PC where TAppr.Product = PC.Product) Product_Description,"+
			" TAppr.TENOR_START, " +
			" TAppr.TENOR_END, " +
			" TRIM(TO_CHAR(TAppr.INT_RATE_START,'9,990.9999999')) AS INT_RATE_START, " +
			" TRIM(TO_CHAR(TAppr.INT_RATE_END,'9,990.9999999')) AS INT_RATE_END, " +
			" TRIM(TO_CHAR(TAppr.AVG_START,'999,999,999,999,999,990.99999')) AS AVG_START, " +
			" TRIM(TO_CHAR(TAppr.AVG_END,'999,999,999,999,999,990.99999')) AS AVG_END, " + 
			" TRIM(TO_CHAR(TAppr.DEBIT_POOL_RATE,'9,990.9999999')) AS DEBIT_POOL_RATE, TRIM(TO_CHAR(TAppr.CREDIT_POOL_RATE,'9,990.9999999')) AS CREDIT_POOL_RATE,TAppr.POOL_RATE_STATUS_NT,TAppr.POOL_RATE_STATUS, " + 
			" TAppr.RECORD_INDICATOR_NT,TAppr.RECORD_INDICATOR,TAppr.MAKER,TAppr.VERIFIER,TAppr.INTERNAL_STATUS, " + 
			" To_Char(TAppr.DATE_LAST_MODIFIED, 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED," +
			" To_Char(TAppr.DATE_CREATION, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION, TAppr.OUC_ATTRIBUTE, TAppr.OUC_ATTRIBUTE_LEVEL, TAppr.RISK_ASSET_CLASS, TAppr.LIQUIDITY_RATE, TAppr.NDIC_RATE, PO.POOL_CODE_DESCRIPTION " +
			" From FTP_RATES TAppr Join Pool_Codes PO on PO.COUNTRY = TAppr.COUNTRY AND PO.LE_BOOK = TAppr.LE_BOOK AND PO.POOL_CODE = TAppr.POOL_CODE " + 
			" Where TAppr.DATA_SOURCE = ?  And TAppr.COUNTRY = ?  " +
			" And TAppr.LE_BOOK = ?  And TAppr.CURRENCY = ?  " +
			" And TAppr.POOL_CODE = ? And TAppr.EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY')"   );

		StringBuffer strQueryPend = new StringBuffer("Select TPend.DATA_SOURCE_AT,TPend.DATA_SOURCE, " + 
			" To_Char(TPend.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE ,TPend.VISION_SBU_AT, TPend.VISION_SBU,TPend.COUNTRY,TPend.LE_BOOK,TPend.CURRENCY,TPend.POOL_CODE,TPend.PRODUCT, " +
			" (Select Product_Description from Product_Codes PC where TPend.Product = PC.Product) Product_Description,"+
			" TPend.TENOR_START, " +
			" TPend.TENOR_END, " +
			" TRIM(TO_CHAR(TPend.INT_RATE_START,'9,990.9999999')) AS INT_RATE_START, " +
			" TRIM(TO_CHAR(TPend.INT_RATE_END,'9,990.9999999')) AS INT_RATE_END, " +
			" TRIM(TO_CHAR(TPend.AVG_START,'999,999,999,999,999,990.99999')) AS AVG_START, " +
			" TRIM(TO_CHAR(TPend.AVG_END,'999,999,999,999,999,990.99999')) AS AVG_END, " +
			" TRIM(TO_CHAR(TPend.DEBIT_POOL_RATE,'9,990.9999999')) AS DEBIT_POOL_RATE, TRIM(TO_CHAR(TPend.CREDIT_POOL_RATE,'9,990.9999999')) AS CREDIT_POOL_RATE,TPend.POOL_RATE_STATUS_NT,TPend.POOL_RATE_STATUS, " + 
			" TPend.RECORD_INDICATOR_NT,TPend.RECORD_INDICATOR,TPend.MAKER,TPend.VERIFIER,TPend.INTERNAL_STATUS, " + 
			" To_Char(TPend.DATE_LAST_MODIFIED, 'DD-MM-YYYY HH24:MI:SS') DATE_LAST_MODIFIED," +
			" To_Char(TPend.DATE_CREATION, " + 
			" 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION, TPend.OUC_ATTRIBUTE, TPend.OUC_ATTRIBUTE_LEVEL, TPend.RISK_ASSET_CLASS, TPend.LIQUIDITY_RATE, TPend.NDIC_RATE, PO.POOL_CODE_DESCRIPTION " +
			" From FTP_RATES_PEND TPend Join Pool_Codes PO on PO.COUNTRY = TPend.COUNTRY AND PO.LE_BOOK = TPend.LE_BOOK AND PO.POOL_CODE = TPend.POOL_CODE " + 
			" Where TPend.DATA_SOURCE = ?  " + 
			" And TPend.COUNTRY = ?  " + 
			" And TPend.LE_BOOK = ?  " + 
			" And TPend.CURRENCY = ?  " + 
			" And TPend.POOL_CODE = ? " +
			" And TPend.EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY')" );
		
		params.add(dObj.getDataSource());	//[DATA_SOURCE]
		params.add(dObj.getCountry());	//[COUNTRY]
		params.add(dObj.getLeBook());	//[LE_BOOK]
		params.add(dObj.getCurrency());	//[CURRENCY]
		params.add(dObj.getPoolCode());	//[POOL_CODE]
		params.add(dObj.getEffectiveDate());	//[EFFECTIVE_DATE]
				
		try
		{
			if(!dObj.isVerificationRequired()){intStatus =0;}
			if(intStatus == 0)
			{
				logger.info("Executing approved query");
				collTemp = getJdbcTemplate().query(strQueryAppr.toString(),params.toArray(),getMapper());
			}else{
				logger.info("Executing pending query");
				collTemp = getJdbcTemplate().query(strQueryPend.toString(),params.toArray(),getMapper());
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
	protected List<FTPRatesVb> selectApprovedRecord(FTPRatesVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<FTPRatesVb> doSelectPendingRecord(FTPRatesVb vObject){
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}

	@Override
	protected void setServiceDefaults(){
		serviceName = "FTPRates";
		serviceDesc = CommonUtils.getResourceManger().getString("ftpRates");
		tableName = "FTP_RATES";
		childTableName = "FTPL_RATES";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}

	@Override
	protected int getStatus(FTPRatesVb records){return records.getPoolRateStatus();}

	@Override
	protected void setStatus(FTPRatesVb vObject,int status){vObject.setPoolRateStatus(status);}

	@Override
	protected int doInsertionAppr(FTPRatesVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getCreditPoolRate())){
			vObject.setCreditPoolRate(vObject.getCreditPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getDebitPoolRate())){
			vObject.setDebitPoolRate(vObject.getDebitPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getDebitPoolRate())){
			vObject.setDebitPoolRate(vObject.getDebitPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getLiquidityRate())){
			vObject.setLiquidityRate(vObject.getLiquidityRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getNdicRate())){
			vObject.setNdicRate(vObject.getNdicRate().replaceAll(",", ""));
		}
		String query = "Insert Into FTP_RATES( " + 
		"DATA_SOURCE_AT, DATA_SOURCE, VISION_SBU, COUNTRY, LE_BOOK, CURRENCY, POOL_CODE, PRODUCT,EFFECTIVE_DATE, TENOR_START,TENOR_END, INT_RATE_START, INT_RATE_END, AVG_START, AVG_END, " + 
			" DEBIT_POOL_RATE, CREDIT_POOL_RATE, POOL_RATE_STATUS_NT, POOL_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, OUC_ATTRIBUTE, OUC_ATTRIBUTE_LEVEL, RISK_ASSET_CLASS, LIQUIDITY_RATE, NDIC_RATE) " + 
			" Values (?, ?, ?, ?, ?, ?, ?, ?, To_Date(?, 'DD-MM-YYYY'), ?, ?,?, ?, ?, ?, TO_NUMBER(?,'9,990.9999999'), TO_NUMBER(?,'9,990.9999999'), ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate, ?, ?, ?, ?, ?)";

		Object args[] = {vObject.getDataSourceAt(), vObject.getDataSource(),
			 vObject.getVisionSbu(), vObject.getCountry(), vObject.getLeBook(), vObject.getCurrency(),
			 vObject.getPoolCode(), vObject.getProduct(),vObject.getEffectiveDate(), vObject.getTenorRateStart(), vObject.getTenorRateEnd(), vObject.getInterestRateStart(), vObject.getInterestRateEnd(),
			 vObject.getAvgStart(), vObject.getAvgEnd(), vObject.getDebitPoolRate(), vObject.getCreditPoolRate(),
			 vObject.getPoolRateStatusNt(), vObject.getPoolRateStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(),vObject.getOucAttribute(),
			 vObject.getOucAttributeLevel(), vObject.getRiskAssetClass(), vObject.getLiquidityRate(), vObject.getNdicRate()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPend(FTPRatesVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getCreditPoolRate())){
			vObject.setCreditPoolRate(vObject.getCreditPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getDebitPoolRate())){
			vObject.setDebitPoolRate(vObject.getDebitPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getDebitPoolRate())){
			vObject.setDebitPoolRate(vObject.getDebitPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getOucAttribute())){
			vObject.setOucAttribute(vObject.getOucAttribute().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel())){
			vObject.setOucAttributeLevel(vObject.getOucAttributeLevel().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getLiquidityRate())){
			vObject.setLiquidityRate(vObject.getLiquidityRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getNdicRate())){
			vObject.setNdicRate(vObject.getNdicRate().replaceAll(",", ""));
		}
		String query = "Insert Into FTP_RATES_PEND(DATA_SOURCE_AT, DATA_SOURCE, VISION_SBU, COUNTRY, LE_BOOK, CURRENCY, POOL_CODE, PRODUCT,"+
			" EFFECTIVE_DATE, TENOR_START,TENOR_END, INT_RATE_START, INT_RATE_END, AVG_START, AVG_END, " + 
			" DEBIT_POOL_RATE, CREDIT_POOL_RATE, POOL_RATE_STATUS_NT, POOL_RATE_STATUS, RECORD_INDICATOR_NT, " + 
			" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, OUC_ATTRIBUTE, OUC_ATTRIBUTE_LEVEL, RISK_ASSET_CLASS, LIQUIDITY_RATE, NDIC_RATE) " + 
			" Values (?, ?, ?, ?, ?, ?, ?, ?,To_Date(?, 'DD-MM-YYYY'), ?, ?, ?, ?, ?, ?, TO_NUMBER(?,'9,990.9999999'), TO_NUMBER(?,'9,990.9999999'), ?, ?, ?, ?, ?, ?, ?, SysDate, SysDate, ?, ?, ?, ?, ?)";

		Object args[] = {vObject.getDataSourceAt(), vObject.getDataSource(), 
			 vObject.getVisionSbu(), vObject.getCountry(), vObject.getLeBook(), vObject.getCurrency(),
			 vObject.getPoolCode(),vObject.getProduct(),vObject.getEffectiveDate(), vObject.getTenorRateStart(), vObject.getTenorRateEnd(), vObject.getInterestRateStart(), vObject.getInterestRateEnd(),vObject.getAvgStart(),
			 vObject.getAvgEnd(), vObject.getDebitPoolRate(), vObject.getCreditPoolRate(),
			 vObject.getPoolRateStatusNt(), vObject.getPoolRateStatus(), vObject.getRecordIndicatorNt(),
			 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(), vObject.getInternalStatus(), 
			 vObject.getOucAttribute(), vObject.getOucAttributeLevel(), vObject.getRiskAssetClass(), vObject.getLiquidityRate(), vObject.getNdicRate()};

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doInsertionPendWithDc(FTPRatesVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateStart())){
			vObject.setInterestRateStart(vObject.getInterestRateStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getInterestRateEnd())){
			vObject.setInterestRateEnd(vObject.getInterestRateEnd().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getCreditPoolRate())){
			vObject.setCreditPoolRate(vObject.getCreditPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getDebitPoolRate())){
			vObject.setDebitPoolRate(vObject.getDebitPoolRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getDebitPoolRate())){
			vObject.setDebitPoolRate(vObject.getDebitPoolRate().replaceAll(",", ""));
		}
		/*if(ValidationUtil.isValid(vObject.getOucAttribute())){
			vObject.setOucAttribute(vObject.getOucAttribute().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getOucAttributeLevel())){
			vObject.setOucAttributeLevel(vObject.getOucAttributeLevel().replaceAll(",", ""));
		}*/
		if(ValidationUtil.isValid(vObject.getLiquidityRate())){
			vObject.setLiquidityRate(vObject.getLiquidityRate().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getNdicRate())){
			vObject.setNdicRate(vObject.getNdicRate().replaceAll(",", ""));
		}
		String query ="Insert Into FTP_RATES_PEND(DATA_SOURCE_AT, DATA_SOURCE, VISION_SBU, COUNTRY, LE_BOOK, CURRENCY, POOL_CODE, PRODUCT,"+
				" EFFECTIVE_DATE, TENOR_START,TENOR_END, INT_RATE_START, INT_RATE_END, AVG_START, AVG_END, " + 
				" DEBIT_POOL_RATE, CREDIT_POOL_RATE, POOL_RATE_STATUS_NT, POOL_RATE_STATUS, RECORD_INDICATOR_NT, " + 
				" RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, DATE_LAST_MODIFIED, DATE_CREATION, OUC_ATTRIBUTE, OUC_ATTRIBUTE_LEVEL, RISK_ASSET_CLASS, LIQUIDITY_RATE, NDIC_RATE) " + 
				" Values (?, ?, ?, ?, ?, ?, ?, ?,To_Date(?, 'DD-MM-YYYY'), ?, ?, ?, ?, ?, ?, TO_NUMBER(?,'9,990.9999999'), TO_NUMBER(?,'9,990.9999999'), ?, ?, ?, ?, ?, ?, ?, SysDate, To_Date(?, 'DD-MM-YYYY HH24:MI:SS'), ?, ?, ?, ?, ?)";
		
		Object args[] = {vObject.getDataSourceAt(), vObject.getDataSource(), 
				 vObject.getVisionSbu(), vObject.getCountry(), vObject.getLeBook(), vObject.getCurrency(),
				 vObject.getPoolCode(),vObject.getProduct(),vObject.getEffectiveDate(), vObject.getTenorRateStart(), vObject.getTenorRateEnd(), vObject.getInterestRateStart(), vObject.getInterestRateEnd(),vObject.getAvgStart(),
				 vObject.getAvgEnd(), vObject.getDebitPoolRate(), vObject.getCreditPoolRate(),
				 vObject.getPoolRateStatusNt(), vObject.getPoolRateStatus(), vObject.getRecordIndicatorNt(),
				 vObject.getRecordIndicator(), vObject.getMaker(), vObject.getVerifier(),vObject.getInternalStatus(),vObject.getDateCreation(),
				 vObject.getOucAttribute(), vObject.getOucAttributeLevel(), vObject.getRiskAssetClass(), vObject.getLiquidityRate(), vObject.getNdicRate()}; 

		return getJdbcTemplate().update(query,args);
	}

	@Override
	protected int doUpdateAppr(FTPRatesVb vObject){
		Vector<Object> params = new Vector<Object>();
		String tenorStart = "";
		String tenorEnd = "";
		String intRateStart = "";
		String intRateEnd = "";
		String avgStart = ""; 
		String avgEnd = "";
		String liquidityRate = "";
		String ndicRate = "";
		if (ValidationUtil.isValid(vObject.getTenorRateStart())) tenorStart = vObject.getTenorRateStart().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getTenorRateEnd())) tenorEnd = vObject.getTenorRateEnd().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getInterestRateStart())) intRateStart = vObject.getInterestRateStart().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getInterestRateEnd())) intRateEnd = vObject.getInterestRateEnd().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getAvgStart())) avgStart = vObject.getAvgStart().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getAvgEnd())) avgEnd = vObject.getAvgEnd().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getLiquidityRate())) liquidityRate = vObject.getLiquidityRate().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getNdicRate())) ndicRate = vObject.getNdicRate().replaceAll(",", "");
		
		avgStart = avgStart.replaceAll(",", "");
		avgEnd = avgEnd.replaceAll(",", "");
		StringBuilder query = new StringBuilder("Update FTP_RATES  Set " + 
		" VISION_SBU = ?,"+  
		" PRODUCT = ?,TENOR_START = ? ,TENOR_END = ? , "+
		" INT_RATE_START = ? ,INT_RATE_END = ?, "+
		" AVG_START = ?,AVG_END = ?, "+
            		" DEBIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999'), CREDIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999'),  " + 
					" POOL_RATE_STATUS = ?,  RECORD_INDICATOR = ?, MAKER = ?, " + 
					" VERIFIER = ?, DATE_LAST_MODIFIED = SysDate, OUC_ATTRIBUTE = ?, OUC_ATTRIBUTE_LEVEL = ?, RISK_ASSET_CLASS = ?, LIQUIDITY_RATE = ?, NDIC_RATE = ?" +
		" Where DATA_SOURCE = ? " +
		" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
		" And COUNTRY = ? " +
		" And LE_BOOK = ? " +
		" And POOL_CODE = ? and Currency = ? " );
		
		params.add(vObject.getVisionSbu());
		params.add(vObject.getProduct());
		params.add(tenorStart);
		params.add(tenorEnd);
		params.add(intRateStart);
		params.add(intRateEnd);
		params.add(avgStart);
		params.add(avgEnd);
		params.add(vObject.getDebitPoolRate());
		params.add(vObject.getCreditPoolRate());
		params.add(vObject.getPoolRateStatus());
		params.add(vObject.getRecordIndicator());
		params.add(vObject.getMaker());
		params.add(vObject.getVerifier());
		params.add(vObject.getOucAttribute());
		params.add(vObject.getOucAttributeLevel());
		params.add(vObject.getRiskAssetClass());
		params.add(liquidityRate);
		params.add(ndicRate);
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getPoolCode());
		params.add(vObject.getCurrency());
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}

	@Override
	protected int doUpdatePend(FTPRatesVb vObject){
		Vector<Object> params = new Vector<Object>();
		String tenorStart = "";
		String tenorEnd = "";
		String intRateStart = "";
		String intRateEnd = "";
		String avgStart = ""; 
		String avgEnd = "";
		String liquidityRate = "";
		String ndicRate = "";
		if (ValidationUtil.isValid(vObject.getTenorRateStart())) tenorStart = vObject.getTenorRateStart().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getTenorRateEnd())) tenorEnd = vObject.getTenorRateEnd().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getInterestRateStart())) intRateStart = vObject.getInterestRateStart().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getInterestRateEnd())) intRateEnd = vObject.getInterestRateEnd().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getAvgStart())) avgStart = vObject.getAvgStart().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getAvgEnd())) avgEnd = vObject.getAvgEnd().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getLiquidityRate())) liquidityRate = vObject.getLiquidityRate().replaceAll(",", "");
		if (ValidationUtil.isValid(vObject.getNdicRate())) ndicRate = vObject.getNdicRate().replaceAll(",", "");
		avgStart = avgStart.replaceAll(",", "");
		avgEnd = avgEnd.replaceAll(",", "");
		StringBuilder query = new StringBuilder("Update FTP_RATES_PEND Set " + 
		" VISION_SBU = ?,"+  
		" PRODUCT = ?,TENOR_START = ? ,TENOR_END = ? , "+
		" INT_RATE_START = ? ,INT_RATE_END = ?, "+
		" AVG_START = ?,AVG_END = ?, "+
            		" DEBIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999'), CREDIT_POOL_RATE = TO_NUMBER(?,'9,990.9999999'),  " + 
					" POOL_RATE_STATUS = ?,  RECORD_INDICATOR = ?, MAKER = ?, " + 
					" VERIFIER = ?, DATE_LAST_MODIFIED = SysDate, OUC_ATTRIBUTE = ?, OUC_ATTRIBUTE_LEVEL = ?, RISK_ASSET_CLASS = ?, LIQUIDITY_RATE = ?, NDIC_RATE = ?" +
		" Where DATA_SOURCE = ? " +
		" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
		" And COUNTRY = ? " +
		" And LE_BOOK = ? " +
		" And POOL_CODE = ? and Currency = ? " );
		
		params.add(vObject.getVisionSbu());
		params.add(vObject.getProduct());
		params.add(tenorStart);
		params.add(tenorEnd);
		params.add(intRateStart);
		params.add(intRateEnd);
		params.add(avgStart);
		params.add(avgEnd);
		params.add(vObject.getDebitPoolRate());
		params.add(vObject.getCreditPoolRate());
		params.add(vObject.getPoolRateStatus());
		params.add(vObject.getRecordIndicator());
		params.add(vObject.getMaker());
		params.add(vObject.getVerifier());
		params.add(vObject.getOucAttribute());
		params.add(vObject.getOucAttributeLevel());
		params.add(vObject.getRiskAssetClass());
		params.add(liquidityRate);
		params.add(ndicRate);
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getPoolCode());
		params.add(vObject.getCurrency());
		
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}

	@Override
	protected int doDeleteAppr(FTPRatesVb vObject){
		String tenorStart = "9999";
		String tenorEnd = "9999";
		String intReateStart = "9999";
		String intReateEnd = "9999";
		String avgEnd = "9999";
		String avgStart = "9999";
		Vector<Object> params = new Vector<Object>();
		if(vObject.getTenorRateStart()!="") 
			tenorStart = vObject.getTenorRateStart();
		if(vObject.getTenorRateEnd()!="") 
			tenorEnd = vObject.getTenorRateEnd();
		if(vObject.getInterestRateStart()!="") 
			intReateStart = vObject.getInterestRateStart();
		if(vObject.getInterestRateEnd()!="") 
			intReateEnd = vObject.getInterestRateEnd();
		if(vObject.getAvgStart()!="") 
			avgStart = vObject.getAvgStart();
		if(vObject.getAvgEnd()!="") 
			avgEnd = vObject.getAvgEnd();
		avgStart = avgStart.replaceAll(",", "");
		avgEnd = avgEnd.replaceAll(",", "");
		StringBuffer query =new StringBuffer("Delete From FTP_RATES Where " + 
		" DATA_SOURCE = ? " + 
		" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
		" And COUNTRY = ? " + 
		" And LE_BOOK = ? " + 
		" And CURRENCY = ? " +
		" And POOL_CODE = ? and Currency = ? ") ;
		
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getCurrency());
		params.add(vObject.getPoolCode());
		params.add(vObject.getCurrency());
		
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}

	@Override
	protected int deletePendingRecord(FTPRatesVb vObject){
		String tenorStart = "9999";
		String tenorEnd = "9999";
		String intReateStart = "9999";
		String intReateEnd = "9999";
		String avgEnd = "9999";
		String avgStart = "9999";
		Vector<Object> params = new Vector<Object>();
		if(vObject.getTenorRateStart()!="") 
			tenorStart = vObject.getTenorRateStart();
		if(vObject.getTenorRateEnd()!="") 
			tenorEnd = vObject.getTenorRateEnd();
		if(vObject.getInterestRateStart()!="") 
			intReateStart = vObject.getInterestRateStart();
		if(vObject.getInterestRateEnd()!="") 
			intReateEnd = vObject.getInterestRateEnd();
		if(vObject.getAvgStart()!="") 
			avgStart = vObject.getAvgStart();
		if(vObject.getAvgEnd()!="") 
			avgEnd = vObject.getAvgEnd();
		avgStart = avgStart.replaceAll(",", "");
		avgEnd = avgEnd.replaceAll(",", "");
		
		StringBuffer query =new StringBuffer("Delete From FTP_RATES_PEND Where " + 
		" DATA_SOURCE = ? " + 
		" And EFFECTIVE_DATE = To_Date(?, 'DD-MM-YYYY') " + 
		" And COUNTRY = ? " + 
		" And LE_BOOK = ? " + 
		" And CURRENCY = ? " +
		" And POOL_CODE = ? and Currency = ? ") ;
		
		params.add(vObject.getDataSource());
		params.add(vObject.getEffectiveDate());
		params.add(vObject.getCountry());
		params.add(vObject.getLeBook());
		params.add(vObject.getCurrency());
		params.add(vObject.getPoolCode());
		params.add(vObject.getCurrency());
		
		return getJdbcTemplate().update(query.toString(),params.toArray());
	}

	@Override
	protected String frameErrorMessage(FTPRatesVb vObject, String strOperation)
	{
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try {
			strErrMsg =  strErrMsg + " DATA_SOURCE:" + vObject.getDataSource();
			strErrMsg =  strErrMsg + " EFFECTIVE_DATE:" + vObject.getEffectiveDate();
			strErrMsg =  strErrMsg + " COUNTRY:" + vObject.getCountry();
			strErrMsg =  strErrMsg + " LE_BOOK:" + vObject.getLeBook();
			strErrMsg =  strErrMsg + " CURRENCY:" + vObject.getCurrency();
			strErrMsg =  strErrMsg + " POOL_CODE:" + vObject.getPoolCode();
			strErrMsg =  strErrMsg + " PRODUCT:" + vObject.getProduct();
			strErrMsg =  strErrMsg + " VISION_SBU:" + vObject.getVisionSbu();
			strErrMsg =  strErrMsg + " TENOR_START:" + vObject.getTenorRateStart();
			strErrMsg =  strErrMsg + " TENOR_END:" + vObject.getTenorRateEnd();
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
	protected String getAuditString(FTPRatesVb vObject)
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
			
			if(vObject.getPoolCode() != null)
				strAudit.append(vObject.getPoolCode().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(vObject.getPoolCodeDesc() != null)
				strAudit.append(vObject.getPoolCodeDesc().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getEffectiveDate());
			strAudit.append("!|#");
			
			strAudit.append(vObject.getDataSourceAt());
			strAudit.append("!|#");
			if(vObject.getDataSource() != null)
				strAudit.append(vObject.getDataSource().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(vObject.getCurrency() != null)
				strAudit.append(vObject.getCurrency().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getDebitPoolRate());
			strAudit.append("!|#");
			strAudit.append(vObject.getCreditPoolRate());
			strAudit.append("!|#");

			strAudit.append(vObject.getVisionSbuAt());
			strAudit.append("!|#");
			if(vObject.getVisionSbu() != null)
				strAudit.append(vObject.getVisionSbu().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if(vObject.getProduct() != null)
				strAudit.append(vObject.getProduct().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			if(vObject.getTenorRateStart() != null)
				strAudit.append(vObject.getTenorRateStart().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			if(vObject.getTenorRateEnd() != null)
				strAudit.append(vObject.getTenorRateEnd().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");

			if(vObject.getInterestRateStart() != null)
				strAudit.append(vObject.getInterestRateStart().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			if(vObject.getInterestRateEnd() != null)
				strAudit.append(vObject.getInterestRateEnd().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			if(vObject.getAvgStart() != null)
				strAudit.append(vObject.getAvgStart().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			if(vObject.getAvgEnd() != null)
				strAudit.append(vObject.getAvgEnd().trim());
			else
				strAudit.append("NULL");
			strAudit.append("!|#");
			
			strAudit.append(vObject.getPoolRateStatusNt());
			strAudit.append("!|#");
			strAudit.append(vObject.getPoolRateStatus());
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
	public List<FTPRatesVb> getPoolCodeDescription(FTPRatesVb dObj)
	{
		List<FTPRatesVb> collTemp = null;
		String query = "select POOL_CODE_DESCRIPTION from POOL_CODES where COUNTRY = '"+dObj.getCountry()+"' AND LE_BOOK = '"+dObj.getLeBook()+"' AND POOL_CODE = '"+dObj.getPoolCode()+"'";			
		try
		{
			collTemp = getJdbcTemplate().query(query, getDesciptionMapper());
			return collTemp;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public List<FTPRatesVb> getProductDescription(FTPRatesVb dObj)
	{
		List<FTPRatesVb> collTemp = null;
		String query = "select PRODUCT_DESCRIPTION from PRODUCT_CODES where PRODUCT_STATUS =0 ORDER BY PRODUCT";			
		try
		{
			collTemp = getJdbcTemplate().query(query, getProductDescriptionMapper());
			return collTemp;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public int validatePoolCode(FTPRatesVb vObDlyVb){
		final int intKeyFieldsCount = 3;
		int count = 0;
		String strQuery = new String("Select  count(1) From Pool_Codes Where COUNTRY = ? AND LE_BOOK=? AND POOL_CODE =? AND Pool_Code_Status=0");
		Object objParams[] = new Object[intKeyFieldsCount];
		if(vObDlyVb.getCountry() == null || vObDlyVb.getLeBook() == null || vObDlyVb.getPoolCode() == null){
			return 1;
		}
		objParams[0] =vObDlyVb.getCountry();
		objParams[1] =vObDlyVb.getLeBook();
		objParams[2] =vObDlyVb.getPoolCode();
		try
		{
			count = getJdbcTemplate().queryForObject(strQuery.toString(),objParams, Integer.class);
			return  count;
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}
	}
}
