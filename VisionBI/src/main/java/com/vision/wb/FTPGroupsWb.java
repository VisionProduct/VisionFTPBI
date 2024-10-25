package com.vision.wb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.CommonDao;
import com.vision.dao.FTPGroupsDao;
import com.vision.dao.FTPSourceConfigDao;
import com.vision.dao.FtpAddonDao;
import com.vision.dao.FtpCurvesDao;
import com.vision.dao.FtpMethodsDao;
import com.vision.dao.FtpPremiumsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.DeepCopy;
import com.vision.util.ValidationUtil;
import com.vision.vb.CommonVb;
import com.vision.vb.DCManualQueryVb;
import com.vision.vb.DesignAndAnalysisMagnifierVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpAddonVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.FtpPremiumsVb;
import com.vision.vb.ReviewResultVb;
import com.vision.vb.SmartSearchVb;
import com.vision.vb.VcConfigMainColumnsVb;
import com.vision.vb.VcConfigMainTreeVb;


@Component
public class FTPGroupsWb extends AbstractWorkerBean<FTPGroupsVb> {
	
	@Value("${app.databaseType}")
	 private String databaseType;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private FTPGroupsDao ftpGroupsDao;
	@Autowired
	private FTPSourceConfigDao ftpSourceConfigDao;
	
	@Autowired
	private FtpMethodsDao ftpMethodsDao;
	
	@Autowired
	private FtpCurvesDao ftpCurvesDao;
	
	@Autowired
	private FtpAddonDao ftpAddonDao;
	
	@Autowired
	private FtpPremiumsDao ftpPremiumsDao;

	public static Logger logger = LoggerFactory.getLogger(FTPGroupsWb.class);

	public ArrayList getPageLoadValues() {
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try {
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);// Status
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7); // Record Indicator
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1301); // FTP Group
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1302); // Source Reference
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(10); // Data Source
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1303); // Method_Type
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1304); // Repricing_Flag
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1310); // FTP_Curve_ID
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1301); // Method_Bal_type
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1302); // FTP_Tenor_Type
			arrListLocal.add(collTemp);
//			collTemp = getFtpGroupsDao().getVisionSbu();
			// collTemp = getFtpGroupsDao().getVisionSbu( String.valueOf(
			// CustomContextHolder.getContext().getVisionId()),
			// String.valueOf(System.currentTimeMillis()), "PR088" );
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(8); // Tenor_ Application_Code
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(6); // Interest Basis
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1303); // Operand
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1304); // FTP Apply Rate
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1360);// FTP Engine Method SubType
			arrListLocal.add(collTemp);
			collTemp = getFtpGroupsDao().callProcToPopulateVisionSBUData(); // Vision SBU
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_GROUPS");
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1305); // Frequency Tuning
			arrListLocal.add(collTemp);
			
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(59); // Vision Sector
			arrListLocal.add(collTemp);

			return arrListLocal;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}

	@Override
	protected void setAtNtValues(FTPGroupsVb vObject) {
		vObject.setFtpGroupAt(1301);
//		vObject.setSourceReferenceAt(1302);
//		vObject.setFtpControlStatusNt(1);
		if(vObject.getFtpMethodsList()!=null && vObject.getFtpMethodsList().size()>0) {
			for(FtpMethodsVb methodsVb : vObject.getFtpMethodsList()) {
				methodsVb.setFtpMtStatusNt(1);
				methodsVb.setRecordIndicatorNt(7);
				methodsVb.setMethodBalTypeNt(1301);
				methodsVb.setInterestBasisNt(6);
				methodsVb.setApplyRateNt(1304);
				methodsVb.setRepricingFlagAt(1304);
				methodsVb.setMethodTypeAt(1303);
				methodsVb.setTenorTypeNt(1302);
			}
		}
		if(vObject.getFtpSourceConfigList()!=null && vObject.getFtpSourceConfigList().size()>0) {
			for(FTPSourceConfigVb sourceConfigVb : vObject.getFtpSourceConfigList()) {
				sourceConfigVb.setOperandNt(1303);
				sourceConfigVb.setRecordIndicatorNt(7);
				sourceConfigVb.setFtpSourceConfigStatusNt(1);
			}
		}
		vObject.setFtpGroupStatusNt(1);
		vObject.setRecordIndicatorNt(7);
		vObject.setDataSourceAt(10);

	}

	@Override
	protected void setVerifReqDeleteType(FTPGroupsVb vObject) {
		ArrayList<CommonVb> lCommVbList = (ArrayList<CommonVb>) getCommonDao()
				.findVerificationRequiredAndStaticDelete("FTP_GROUPS");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
//		vObject.setVerificationRequired(true);
	}

	public ExceptionCode getAllQueryPopupResult(FTPGroupsVb queryPopupObj) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			setVerifReqDeleteType(queryPopupObj);
			doFormateDataForQuery(queryPopupObj);
			List<SmartSearchVb> groupSmartSearchOpt = new ArrayList<>();
			List<SmartSearchVb> detailSmartSearchOpt = new ArrayList<>();
			if(queryPopupObj.getSmartSearchOpt() != null && queryPopupObj.getSmartSearchOpt().size() > 0) {
				groupSmartSearchOpt = queryPopupObj.getSmartSearchOpt().stream().filter(vb -> ValidationUtil.isValid(vb.getScreenName()) && "GROUPSEARCH".equalsIgnoreCase(vb.getScreenName())).collect(Collectors.toList());
				detailSmartSearchOpt = queryPopupObj.getSmartSearchOpt().stream().filter(vb -> ValidationUtil.isValid(vb.getScreenName()) && "DETAILSEARCH".equalsIgnoreCase(vb.getScreenName())).collect(Collectors.toList());
			}
			if(groupSmartSearchOpt!=null && groupSmartSearchOpt.size()>0)
				queryPopupObj.setSmartSearchOpt(groupSmartSearchOpt);	
			List<FTPGroupsVb> arrListResult = ftpGroupsDao.getQueryPopupResultsByGroup(queryPopupObj);
			if (arrListResult != null && arrListResult.size() > 0) {
				for (FTPGroupsVb groupsVb : arrListResult) {
					groupsVb.setVerificationRequired(queryPopupObj.isVerificationRequired());
					groupsVb.setSmartSearchOpt(detailSmartSearchOpt);
					List<FTPGroupsVb> childList = ftpGroupsDao.getQueryPopupResultsDetailsNew(groupsVb);
					if (childList != null && childList.size() > 0) {
						for (FTPGroupsVb groupsVb1 : childList) {
							String methodTyp = ftpMethodsDao.getMethodType(groupsVb1);	
							groupsVb1.setMethodTypeDesc(methodTyp);
						}
					}
					groupsVb.setChildList(childList);
				}
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setResponse(arrListResult);
			} else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
			exceptionCode.setOtherInfo(queryPopupObj);
			return exceptionCode;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception in getting the getAllQueryPopupResult results.", ex);
			return null;
		}
	}
	public ExceptionCode getQueryResults(FTPGroupsVb vObject){
		int intStatus = 1;
		setVerifReqDeleteType(vObject);
		List<FTPGroupsVb> collTemp = getScreenDao().getQueryResults(vObject,intStatus);
		if (collTemp.size() == 0){
			intStatus = 0;
			collTemp = getScreenDao().getQueryResults(vObject,intStatus);
		}
		if(collTemp.size() == 0){
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
			List<FTPGroupsVb> sequenceList = ftpGroupsDao.getQueryAllSequences(vObject, intStatus);
			FTPGroupsVb ftpGroupsVb = collTemp.get(0);
			List<FtpMethodsVb> ftpMethodsList = ftpMethodsDao.getQueryResultsAllByParent(vObject);
			
			List<FTPSourceConfigVb> ftpSourceConfigList = ftpSourceConfigDao.getQueryResultsAllByParent(vObject);
			
			FTPCurveVb ftpCurveVb = new FTPCurveVb();
			if (!ValidationUtil.isValid(vObject.getEffectiveDate())) {
				String businessDate = ftpGroupsDao.getSystemDate1();
				ftpCurveVb.setEffectiveDate(businessDate);
			}
			if(ftpMethodsList!=null && ftpMethodsList.size()>0) {
				FtpMethodsVb ftpMethodsVb = (FtpMethodsVb) ftpMethodsList.get(0);
				ftpCurveVb.setFtpCurveId(ftpMethodsVb.getFtpCurveId());
				ftpCurveVb.setCountry(ftpGroupsVb.getCountry());
				ftpCurveVb.setLeBook(ftpGroupsVb.getLeBook());
				
				ArrayList ftpCurveList = (ArrayList) ftpCurvesDao.getQueryPopupResultsCurves(ftpCurveVb);
				ftpGroupsVb.setFtpCurveList(ftpCurveList);
			}
			ftpGroupsVb.setChildList(sequenceList);
			ftpGroupsVb.setFtpMethodsList(ftpMethodsList);
			ftpGroupsVb.setFtpSourceConfigList(ftpSourceConfigList);
			
			
			
			doSetDesctiptionsAfterQuery(((ArrayList<FTPGroupsVb>)collTemp).get(0));
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			getScreenDao().fetchMakerVerifierNames(((ArrayList<FTPGroupsVb>)collTemp).get(0));
			((ArrayList<FTPGroupsVb>)collTemp).get(0).setVerificationRequired(vObject.isVerificationRequired());
			((ArrayList<FTPGroupsVb>)collTemp).get(0).setStaticDelete(vObject.isStaticDelete());
			exceptionCode.setResponse(collTemp);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}
	}
	
	public ExceptionCode reloadCurveId(FTPCurveVb ftpCurveVb) {
		ExceptionCode exceptionCode = new ExceptionCode();

		if (!ValidationUtil.isValid(ftpCurveVb.getEffectiveDate())) {
//			String businessDate = ftpGroupsDao.businessDay();
			String businessDate = ftpGroupsDao.getSystemDateAlone();
			ftpCurveVb.setEffectiveDate(businessDate);
		}

		ArrayList colTemp = (ArrayList) ftpCurvesDao.getQueryPopupResultsCurves(ftpCurveVb);
		ftpCurveVb.setTotalRows(ftpCurveVb.getTotalRows());
		ftpCurveVb.setCurrentPage(ftpCurveVb.getCurrentPage());
		exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
		exceptionCode.setOtherInfo(ftpCurveVb);
		exceptionCode.setResponse(colTemp);
		return exceptionCode;
	}
	
	public ExceptionCode reloadftpAddon(FtpAddonVb ftpAddonVb) {
		ExceptionCode exceptionCode = new ExceptionCode();

		if (!ValidationUtil.isValid(ftpAddonVb.getEffectiveDate())) {
//			String businessDate = ftpGroupsDao.businessDay();
			String businessDate = ftpGroupsDao.getSystemDateAlone();
			ftpAddonVb.setEffectiveDate(businessDate);
		}

		ArrayList colTemp = (ArrayList) ftpAddonDao.getQueryPopupResultsAddOn(ftpAddonVb);
		ftpAddonVb.setTotalRows(ftpAddonVb.getTotalRows());
		ftpAddonVb.setCurrentPage(ftpAddonVb.getCurrentPage());
		exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
		exceptionCode.setOtherInfo(ftpAddonVb);
		exceptionCode.setResponse(colTemp);
		return exceptionCode;
	}
	
	public ExceptionCode reloadFtpPremium(FtpPremiumsVb ftpCurveVb) {
		ExceptionCode exceptionCode = new ExceptionCode();

		if (!ValidationUtil.isValid(ftpCurveVb.getEffectiveDate())) {
//			String businessDate = ftpGroupsDao.businessDay();
			String businessDate = ftpGroupsDao.getSystemDateAlone();
			ftpCurveVb.setEffectiveDate(businessDate);
		}

		ArrayList colTemp = (ArrayList) ftpPremiumsDao.getQueryPopupResultPremium(ftpCurveVb);
		exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
		exceptionCode.setOtherInfo(ftpCurveVb);
		exceptionCode.setResponse(colTemp);
		return exceptionCode;
	}
	public ExceptionCode modifyRecord(FTPGroupsVb vObject){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		FTPGroupsVb clonedObject = null;
		try{
			setAtNtValues(vObject);
			setVerifReqDeleteType(vObject);
			clonedObject = deepCopy.copy(vObject);
			doFormateData(vObject);
			exceptionCode = doValidate(vObject);
			if(exceptionCode!=null && exceptionCode.getErrorMsg()!=""){
				return exceptionCode;
			}
			if(!vObject.isVerificationRequired()){
				exceptionCode = ftpGroupsDao.doInsertrOrUpdateApprRecord(vObject);
			}else{
				exceptionCode = ftpGroupsDao.doUpdateRecord(vObject);
			}
			getScreenDao().fetchMakerVerifierNames(vObject);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Modify Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	protected void setAtNtValuesFtpCurve(List<FTPCurveVb> vObjects) {
		for(FTPCurveVb vObject : vObjects) {
			vObject.setTenorApplicationCodeNt(1301);
			vObject.setFtpCurveStatusNt(1);
			vObject.setRecordIndicatorNt(7);
			vObject.setVisionSbuAttributeAt(3);
			vObject.setTenorApplicationCodeNt(8);
		}
	}
	public ExceptionCode modifyRecordFtpCurve(List<FTPCurveVb> vObject){
		ExceptionCode exceptionCode  = null;
		DeepCopy<List<FTPCurveVb>> deepCopy = new DeepCopy<List<FTPCurveVb>>();
		List<FTPCurveVb> clonedObject = null;
		try{
			FTPGroupsVb groupsVb = new FTPGroupsVb();
			setAtNtValuesFtpCurve(vObject);
			setVerifReqDeleteType(groupsVb);
			clonedObject = deepCopy.copy(vObject);
			if(!groupsVb.isVerificationRequired()){
				exceptionCode = ftpCurvesDao.doInsertrOrUpdateApprRecord(vObject);
			}else{
				exceptionCode = ftpCurvesDao.doUpdateRecord(vObject);
			}
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Modify Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	protected void setAtNtValuesFtpAddOn(List<FtpAddonVb> vObjects) {
		for(FtpAddonVb vObject : vObjects ) {
			vObject.setTenorApplicationCodeNt(1301);
			vObject.setFtpAddonStatusNt(1);
			vObject.setRecordIndicatorNt(7);
			vObject.setTenorApplicationCodeNt(8);			
		}

	}
	public ExceptionCode modifyRecordFtpAddOn(List<FtpAddonVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<List<FtpAddonVb>> deepCopy = new DeepCopy<List<FtpAddonVb>>();
		List<FtpAddonVb> clonedObject = null;
		try{
			FTPGroupsVb groupsVb = new FTPGroupsVb();
			setAtNtValuesFtpAddOn(vObjects);
			setVerifReqDeleteType(groupsVb);
			clonedObject = deepCopy.copy(vObjects);
//			vObject.setVerificationRequired(groupsVb.isVerificationRequired());
			if(!groupsVb.isVerificationRequired()){
				exceptionCode = ftpAddonDao.doInsertrOrUpdateApprRecord(vObjects);
			}else{
				exceptionCode = ftpAddonDao.doUpdateRecord(vObjects);
			}
			exceptionCode.setOtherInfo(vObjects);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Modify Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObjects==null)? "vObject is Null":vObjects.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	protected void setAtNtValuesFtpLp(List<FtpPremiumsVb> vObjects) {
		for(FtpPremiumsVb vObject : vObjects){
			vObject.setTenorApplicationCodeNt(1301);
			vObject.setFtpPremiumStatusNt(1);
			vObject.setRecordIndicatorNt(7);
			vObject.setTenorApplicationCodeNt(8);
			vObject.setVisionSectorAt(59);
			vObject.setVisionSbuAttributeAt(3);			
		}
	}
	public ExceptionCode modifyRecordFtpLiqPremium(List<FtpPremiumsVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<List<FtpPremiumsVb>> deepCopy = new DeepCopy<List<FtpPremiumsVb>>();
		List<FtpPremiumsVb> clonedObject = null;
		try{
			FTPGroupsVb groupsVb = new FTPGroupsVb();
			setAtNtValuesFtpLp(vObjects);
			setVerifReqDeleteType(groupsVb);
			clonedObject = deepCopy.copy(vObjects);
			if(!groupsVb.isVerificationRequired()){
				exceptionCode = ftpPremiumsDao.doInsertrOrUpdateApprRecord(vObjects);
			}else{
				exceptionCode = ftpPremiumsDao.doUpdateRecord(vObjects);
			}
			exceptionCode.setOtherInfo(vObjects);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Modify Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObjects==null)? "vObject is Null":vObjects.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	
	public ExceptionCode updateDefaultRecord(List<FTPGroupsVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		try{
			FTPGroupsVb groupsVb = new FTPGroupsVb();
			setVerifReqDeleteType(groupsVb);
			if(vObjects != null && vObjects.size()>0) {
				for(FTPGroupsVb ftpGroupsVb : vObjects) {
					if(groupsVb.isVerificationRequired()) {
						
						exceptionCode = ftpGroupsDao.doUpdateRecordForNonTrans(ftpGroupsVb);
						if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							return exceptionCode;
						}	
						exceptionCode = ftpGroupsDao.doUpdateDefaultFlagInPend(ftpGroupsVb);
						if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							return exceptionCode;
						}	
					}else {
						exceptionCode = ftpGroupsDao.doUpdateDefaultFlag(ftpGroupsVb);
						if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
							return exceptionCode;
						}	
					}
					
				}
			}
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), Constants.SUCCESSFUL_OPERATION, "Save", "");
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Modify Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			return exceptionCode;
		}
	}
	public List<VcConfigMainColumnsVb> getQueryTreeColumns(VcConfigMainTreeVb treeVb) {
		try {
			List<VcConfigMainColumnsVb> arrListResult = ftpGroupsDao.getQueryTreeColumnsDetails(treeVb);
			return arrListResult;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeCustomException(e.getMessage());
		}
	}
	
	public List<DCManualQueryVb> getSpecificManualQuery(DCManualQueryVb vObj) {
		try {
			return ftpGroupsDao.getSpecificManualQueryDetails(vObj);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeCustomException(e.getMessage());
		}
	}
	public String getDbScript(String getMacroVar) throws DataAccessException{
		try {
			return commonDao.getScriptValue(getMacroVar);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeCustomException(e.getMessage());
		}
	}
	public ExceptionCode executeManualQueryForManifierDetails(DCManualQueryVb vObj, String dbScript, String[] hashArr, String[] hashValArr, DesignAndAnalysisMagnifierVb magnefierVb){
		ExceptionCode exceptionCode = new ExceptionCode();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String level = "";
		exceptionCode = CommonUtils.getConnection(dbScript);
		if(exceptionCode.getErrorCode()==Constants.SUCCESSFUL_OPERATION){
			con = (Connection) exceptionCode.getResponse();
		}else{
			return exceptionCode;
		}
		String dbSetParam1 = CommonUtils.getValue(dbScript, "DB_SET_PARAM1");
		String dbSetParam2 = CommonUtils.getValue(dbScript, "DB_SET_PARAM2");
		String dbSetParam3 = CommonUtils.getValue(dbScript, "DB_SET_PARAM3");
		String stgQuery = "";
		String sessionId = String.valueOf(System.currentTimeMillis());
		String stgTableName1 = "TVC_"+sessionId+"_STG_1";
		String stgTableName2 = "TVC_"+sessionId+"_STG_2";
		String stgTableName3 = "TVC_"+sessionId+"_STG_3";
		String sqlMainQuery = "";
		try{
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			if(ValidationUtil.isValid(dbSetParam1)){
				level = "DB Param 1";
				stmt.executeUpdate(dbSetParam1);
			}
			if(ValidationUtil.isValid(dbSetParam2)){
				level = "DB Param 2";
				stmt.executeUpdate(dbSetParam2);
			}
			if(ValidationUtil.isValid(dbSetParam3)){
				level = "DB Param 3";
				stmt.executeUpdate(dbSetParam3);
			}
			Pattern pattern = Pattern.compile("#(.*?)#");
			Matcher matcher = null;
			if(ValidationUtil.isValid(vObj.getStgQuery1())){
				stgQuery = vObj.getStgQuery1();
				matcher = pattern.matcher(stgQuery);
				while(matcher.find()){
					if("TVC_SESSIONID_STG_1".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName1);
					if("TVC_SESSIONID_STG_2".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName2);
					if("TVC_SESSIONID_STG_3".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName3);
				}
				level = "Staging 1";
				stgQuery = CommonUtils.replaceHashTag(stgQuery, hashArr, hashValArr);
				stmt.executeUpdate(stgQuery);
			}
			if(ValidationUtil.isValid(vObj.getStgQuery2())){
				stgQuery = vObj.getStgQuery2();
				matcher = pattern.matcher(stgQuery);
				while(matcher.find()){
					if("TVC_SESSIONID_STG_1".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName1);
					if("TVC_SESSIONID_STG_2".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName2);
					if("TVC_SESSIONID_STG_3".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName3);
				}
				level = "Staging 2";
				stgQuery = CommonUtils.replaceHashTag(stgQuery, hashArr, hashValArr);
				stmt.executeUpdate(stgQuery);
			}
			if(ValidationUtil.isValid(vObj.getStgQuery3())){
				stgQuery = vObj.getStgQuery3();
				matcher = pattern.matcher(stgQuery);
				while(matcher.find()){
					if("TVC_SESSIONID_STG_1".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName1);
					if("TVC_SESSIONID_STG_2".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName2);
					if("TVC_SESSIONID_STG_3".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName3);
				}
				level = "Staging 3";
				stgQuery = CommonUtils.replaceHashTag(stgQuery, hashArr, hashValArr);
				stmt.executeUpdate(stgQuery);
			}
			sqlMainQuery = vObj.getSqlQuery();
			matcher = pattern.matcher(sqlMainQuery);
			while(matcher.find()){
				if("TVC_SESSIONID_STG_1".equalsIgnoreCase(matcher.group(1)))
					sqlMainQuery = sqlMainQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName1);
				if("TVC_SESSIONID_STG_2".equalsIgnoreCase(matcher.group(1)))
					sqlMainQuery = sqlMainQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName2);
				if("TVC_SESSIONID_STG_3".equalsIgnoreCase(matcher.group(1)))
					sqlMainQuery = sqlMainQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName3);
				else
					sqlMainQuery = sqlMainQuery.replaceAll("#"+matcher.group(1)+"#", "#"+String.valueOf(matcher.group(1)).toUpperCase().replaceAll("\\s", "\\_")+"#");
			}
			level = "Main Query";
			
			LinkedHashMap<String,String> before2AfterChangeWordMap = new LinkedHashMap<String,String>();
			LinkedHashMap<String,String> before2AfterChangeIndexMap = new LinkedHashMap<String,String>();
			int index = 0;
			
			if(hashArr!=null && hashValArr!=null && hashArr.length==hashValArr.length){
				for(String variable:hashArr){
					int varGuessIndex = sqlMainQuery.indexOf("#"+variable+"#");
					while(varGuessIndex >= 0){
						String preString = sqlMainQuery.substring(0, varGuessIndex);
						String wholeWordBefChange = "";
						String wholeWordAfterChange = "";
						int startIndexOfMainSQL = 0;
						startIndexOfMainSQL = preString.indexOf(" ")!=-1?preString.lastIndexOf(" "):-1;
						int endIndexOfMainSQL = 0;
						endIndexOfMainSQL = sqlMainQuery.indexOf(" ",varGuessIndex+1);
						
						/* Get whole word  */
						wholeWordBefChange = (startIndexOfMainSQL != -1 && endIndexOfMainSQL != -1)
								? sqlMainQuery.substring(startIndexOfMainSQL, endIndexOfMainSQL)
								: (startIndexOfMainSQL == -1 && endIndexOfMainSQL != -1)
										? sqlMainQuery.substring(0, endIndexOfMainSQL)
										: sqlMainQuery.substring(startIndexOfMainSQL, sqlMainQuery.length());
						wholeWordBefChange = wholeWordBefChange.trim();
						wholeWordAfterChange = wholeWordBefChange.replaceFirst("#"+hashArr[index]+"#", hashValArr[index]).trim();
						
						String storingIndex = "";
						if(before2AfterChangeIndexMap.get(wholeWordAfterChange)!=null)
							storingIndex = before2AfterChangeIndexMap.get(wholeWordAfterChange);
						storingIndex = storingIndex + varGuessIndex + ",";
						before2AfterChangeIndexMap.put(wholeWordAfterChange.toUpperCase(),storingIndex);
						before2AfterChangeWordMap.put(wholeWordAfterChange.toUpperCase(), wholeWordBefChange);
						
						/* change the value in main query */
						if(startIndexOfMainSQL!=-1  && endIndexOfMainSQL!=-1)
							sqlMainQuery = sqlMainQuery.substring(0,startIndexOfMainSQL) + " " + wholeWordAfterChange + " " + sqlMainQuery.substring(endIndexOfMainSQL,sqlMainQuery.length());
						else if(startIndexOfMainSQL==-1)
							sqlMainQuery = wholeWordAfterChange + " " + sqlMainQuery.substring(endIndexOfMainSQL,sqlMainQuery.length());
						else if(endIndexOfMainSQL==-1)
							sqlMainQuery = sqlMainQuery.substring(0,startIndexOfMainSQL) + " " + wholeWordAfterChange;
						
						varGuessIndex = sqlMainQuery.indexOf("#"+variable+"#", varGuessIndex + 1);
					}
					index++;
				}
			}
			sqlMainQuery = CommonUtils.replaceHashTag(sqlMainQuery, hashArr, hashValArr);
			
			
			StringBuffer queryForExecution = new StringBuffer();
			
			queryForExecution.append("SELECT "+magnefierVb.getUseColumn()+", "+magnefierVb.getDisplayColumn());
			if("ORACLE".equalsIgnoreCase(databaseType))
				queryForExecution.append(" FROM ("+sqlMainQuery+")");
			else if("MSSQL".equalsIgnoreCase(databaseType))
				queryForExecution.append(" FROM ("+sqlMainQuery+") MANUAL_TEMP ");

			rs = stmt.executeQuery(String.valueOf(queryForExecution));
			
			List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
			while (rs.next()) {
				Map<String, String> responseMap = new HashMap<String, String>();
				responseMap.put(magnefierVb.getUseColumn(), rs.getString(magnefierVb.getUseColumn()));
				responseMap.put(magnefierVb.getDisplayColumn(), rs.getString(magnefierVb.getDisplayColumn()));
				returnList.add(responseMap);
			}
			
			exceptionCode.setResponse(returnList);
			
			if(ValidationUtil.isValid(vObj.getPostQuery())){
				stgQuery = vObj.getPostQuery();
				matcher = pattern.matcher(stgQuery);
				while(matcher.find()){
					if("TVC_SESSIONID_STG_1".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName1);
					if("TVC_SESSIONID_STG_2".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName2);
					if("TVC_SESSIONID_STG_3".equalsIgnoreCase(matcher.group(1)))
						stgQuery = stgQuery.replaceAll("#"+matcher.group(1)+"#", stgTableName3);
				}
				level = "Post Query";
				try{
					stgQuery = CommonUtils.replaceHashTag(stgQuery, hashArr, hashValArr);
					stmt.executeUpdate(stgQuery);
				}catch(Exception e){
					e.printStackTrace();
					exceptionCode.setOtherInfo(vObj);
					exceptionCode.setErrorCode(9999);
					exceptionCode.setErrorMsg("Warning - Post Query Execution Failed - Cause:"+e.getMessage());
					return exceptionCode;
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Validation failed at level - "+level+" - Cause:"+e.getMessage());
			exceptionCode.setOtherInfo(vObj);
			return exceptionCode;
		}catch(Exception e){
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Validation failed at level - "+level+" - Cause:"+e.getMessage());
			exceptionCode.setOtherInfo(vObj);
			return exceptionCode;
		}finally{
			String purge = "";
			if("ORACLE".equalsIgnoreCase(databaseType))
				purge = " PURGE";
			
			try{
				if(ValidationUtil.isValid(stgTableName1))
					stmt.executeUpdate("DROP TABLE "+stgTableName1+purge);
			}catch(Exception e){}
			try{
				if(ValidationUtil.isValid(stgTableName2))
					stmt.executeUpdate("DROP TABLE "+stgTableName2+purge);
			}catch(Exception e){}
			try{
				if(ValidationUtil.isValid(stgTableName3))
					stmt.executeUpdate("DROP TABLE "+stgTableName3+purge);
			}catch(Exception e){}
			try{
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(con!=null)
					con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		return exceptionCode;
	}
	public ExceptionCode deleteRecord(FTPGroupsVb vObject){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		FTPGroupsVb clonedObject = null;
		try{
			setAtNtValues(vObject);
			setVerifReqDeleteType(vObject);
			clonedObject = deepCopy.copy(vObject);
			doFormateData(vObject);
			exceptionCode = doValidate(vObject);
			if(exceptionCode!=null && exceptionCode.getErrorMsg()!=""){
				return exceptionCode;
			}
			if(!vObject.isVerificationRequired()){
				exceptionCode = getScreenDao().doDeleteApprRecord(vObject);
			}else{
				exceptionCode = getScreenDao().doDeleteRecord(vObject);
			}
			getScreenDao().fetchMakerVerifierNames(vObject);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Delete Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	
	
	public ExceptionCode bulkRejectFTPCurve(List<FTPCurveVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPCurveVb> deepCopy = new DeepCopy<FTPCurveVb>();
		List<FTPCurveVb> clonedObjects = null;
		FTPCurveVb queryPopObj = new FTPCurveVb();
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			queryPopObj.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			clonedObjects = deepCopy.copyCollection(vObjects);
			exceptionCode =  ftpCurvesDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	public ExceptionCode bulkRejectFTPAddOn(List<FtpAddonVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FtpAddonVb> deepCopy = new DeepCopy<FtpAddonVb>();
		List<FtpAddonVb> clonedObjects = null;
		FtpAddonVb queryPopObj = new FtpAddonVb();
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			queryPopObj.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			clonedObjects = deepCopy.copyCollection(vObjects);
			exceptionCode =  ftpAddonDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	public ExceptionCode bulkRejectFTPPremiums(List<FtpPremiumsVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FtpPremiumsVb> deepCopy = new DeepCopy<FtpPremiumsVb>();
		List<FtpPremiumsVb> clonedObjects = null;
		FtpPremiumsVb queryPopObj = new FtpPremiumsVb();
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			queryPopObj.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			clonedObjects = deepCopy.copyCollection(vObjects);
			exceptionCode =  ftpPremiumsDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	public ExceptionCode deleteFTPCurve(List<FTPCurveVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<List<FTPCurveVb>> deepCopy = new DeepCopy<List<FTPCurveVb>>();
		List<FTPCurveVb> clonedObject = null;
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		FTPCurveVb vObject = new FTPCurveVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			clonedObject = deepCopy.copy(vObjects);
//			doFormateData(vObject);
//			exceptionCode = doValidate(vObject);
			
			vObject.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			vObject.setStaticDelete(ftpGroupsVb.isStaticDelete());
			
			if(!vObject.isVerificationRequired()){
				exceptionCode = ftpCurvesDao.doDeleteApprRecord(vObjects, vObject);
			}else{
				exceptionCode = ftpCurvesDao.doDeleteRecord(vObjects, vObject);
			}
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Delete Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	public ExceptionCode deleteFTPAddon(List<FtpAddonVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<List<FtpAddonVb>> deepCopy = new DeepCopy<List<FtpAddonVb>>();
		List<FtpAddonVb> clonedObject = null;
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		FtpAddonVb vObject = new FtpAddonVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			clonedObject = deepCopy.copy(vObjects);
//			doFormateData(vObject);
//			exceptionCode = doValidate(vObject);
			
			vObject.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			vObject.setStaticDelete(ftpGroupsVb.isStaticDelete());
			
			if(!vObject.isVerificationRequired()){
				exceptionCode = ftpAddonDao.doDeleteApprRecord(vObjects, vObject);
			}else{
				exceptionCode = ftpAddonDao.doDeleteRecord(vObjects, vObject);
			}
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Delete Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	
	public ExceptionCode deleteFTPPremiums(List<FtpPremiumsVb> vObjects){
		ExceptionCode exceptionCode  = null;
		DeepCopy<List<FtpPremiumsVb>> deepCopy = new DeepCopy<List<FtpPremiumsVb>>();
		List<FtpPremiumsVb> clonedObject = null;
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		FtpPremiumsVb vObject = new FtpPremiumsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			clonedObject = deepCopy.copy(vObjects);
//			doFormateData(vObject);
//			exceptionCode = doValidate(vObject);
			
			vObject.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			vObject.setStaticDelete(ftpGroupsVb.isStaticDelete());
			
			if(!vObject.isVerificationRequired()){
				exceptionCode = ftpPremiumsDao.doDeleteApprRecord(vObjects, vObject);
			}else{
				exceptionCode = ftpPremiumsDao.doDeleteRecord(vObjects, vObject);
			}
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Delete Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(clonedObject);
			return exceptionCode;
		}
	}
	
	public ExceptionCode rejectFtpCurves(List<FTPCurveVb> vObjects,FTPCurveVb queryPopObj){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPCurveVb> deepCopy = new DeepCopy<FTPCurveVb>();
		List<FTPCurveVb> clonedObjects = null;
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			clonedObjects = deepCopy.copyCollection(vObjects);
			
			queryPopObj.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			queryPopObj.setStaticDelete(ftpGroupsVb.isStaticDelete());
			
			exceptionCode =  ftpCurvesDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	
	public ExceptionCode rejectFtpAddOn(List<FtpAddonVb> vObjects,FtpAddonVb queryPopObj){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FtpAddonVb> deepCopy = new DeepCopy<FtpAddonVb>();
		List<FtpAddonVb> clonedObjects = null;
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			clonedObjects = deepCopy.copyCollection(vObjects);
			
			queryPopObj.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			queryPopObj.setStaticDelete(ftpGroupsVb.isStaticDelete());
			
			exceptionCode =  ftpAddonDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	
	public ExceptionCode rejectFtpPremiums(List<FtpPremiumsVb> vObjects,FtpPremiumsVb queryPopObj){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FtpPremiumsVb> deepCopy = new DeepCopy<FtpPremiumsVb>();
		List<FtpPremiumsVb> clonedObjects = null;
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		try{
			setVerifReqDeleteType(ftpGroupsVb);
			clonedObjects = deepCopy.copyCollection(vObjects);
			
			queryPopObj.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
			queryPopObj.setStaticDelete(ftpGroupsVb.isStaticDelete());
			
			exceptionCode =  ftpPremiumsDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	
	
	public ArrayList reviewRecord(FTPGroupsVb vObject){
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			List<FTPGroupsVb> approvedGroupCollection = ftpGroupsDao.getQueryResults(vObject,0);
			List<FTPGroupsVb> pendingGroupCollection = ftpGroupsDao.getQueryResults(vObject,1);
			List<ReviewResultVb> groupReviewList =  transformToReviewResults(approvedGroupCollection,pendingGroupCollection);
			arrListLocal.add(groupReviewList);
			
			
			List<ReviewResultVb> sourceConfigResult = null;
			
			List<FTPSourceConfigVb> sourceConfigPendingList = ftpSourceConfigDao.getQueryResultsByParent(vObject, 1);
			if(ValidationUtil.isValid(sourceConfigPendingList)) {
				for(FTPSourceConfigVb sourceConfigVb : sourceConfigPendingList) {
					if(sourceConfigVb.getRecordIndicator() == 1) {
						List<FTPSourceConfigVb> approvedMethodCollection = ftpSourceConfigDao.getQueryResults(sourceConfigVb,0);
						List<FTPSourceConfigVb> pendingMethodCollection = ftpSourceConfigDao.getQueryResults(sourceConfigVb,1);
						sourceConfigResult =  transformToReviewResultsSourceConfig(approvedMethodCollection,pendingMethodCollection);
					}
				}
			}
			arrListLocal.add(sourceConfigResult);
			ArrayList<Object> methodsListAll = null;
			
			Map<String, ArrayList<Object>> methodsMaps = new HashMap<String, ArrayList<Object>>();
			
			List<FtpMethodsVb> methodsList = ftpMethodsDao.getQueryResultsByParent(vObject, 1);
			if(ValidationUtil.isValid(methodsList)) {
				for(FtpMethodsVb methodsVb : methodsList) {
					Map<String, ArrayList<Object>> childList = new HashMap<String, ArrayList<Object>>();
					methodsListAll   = new ArrayList<Object>();
					if(methodsVb.getRecordIndicator() == 1) {
						List<FtpMethodsVb> approvedMethodCollection = ftpMethodsDao.getQueryResults(methodsVb,0);
						List<FtpMethodsVb> pendingMethodCollection = ftpMethodsDao.getQueryResults(methodsVb,1);
						List<ReviewResultVb> groupMethodList =  transformToReviewResultsMethod(approvedMethodCollection,pendingMethodCollection);
						methodsListAll.add(groupMethodList);
					}
					
					List<FTPCurveVb> ftpCurveList = ftpCurvesDao.getQueryResultsByParent(methodsVb, 1, false);
					ArrayList<Object> childCurvesListAll = null;
					boolean isCurves = false;
					if(ftpCurveList!=null && ftpCurveList.size()>0) {
						childCurvesListAll = new ArrayList<Object>();
						for(FTPCurveVb ftpCurveVb : ftpCurveList) {
							if(ftpCurveVb.getRecordIndicator() == 1) {
								isCurves = true;
								List<ReviewResultVb> reviewResultCurveList =  reviewRecordCurve(ftpCurveVb);
								childCurvesListAll.add(reviewResultCurveList);
							}
						}
					}
					if(isCurves) {
						childList.put("CURVES", childCurvesListAll);
					}else {
						childList.put("CURVES", null);
					}
					
					ArrayList<Object> childAddOnListAll = null;
					List<FtpAddonVb> ftpAddOnList = ftpAddonDao.getQueryResultsByParent(methodsVb, 1, false);
					boolean isAddOn = false;
					if(ftpAddOnList != null && ftpAddOnList.size() > 0) {
						childAddOnListAll = new ArrayList<Object>();
						for(FtpAddonVb ftpAddonVb : ftpAddOnList) {
							if(ftpAddonVb.getRecordIndicator() == 1) {
								isAddOn = true;
								List<ReviewResultVb> reviewResultAddOnList =  reviewRecordAddon(ftpAddonVb);
								childAddOnListAll.add(reviewResultAddOnList);
							}
						}
					}
					if(isAddOn) {
						childList.put("ADDON", childAddOnListAll);
					}else {
						childList.put("ADDON", null);
					}					
					
					ArrayList<Object> childLPListAll = null;
					List<FtpPremiumsVb> ftpPremiumsList = ftpPremiumsDao.getQueryResultsByParent(methodsVb, 1, false);
					boolean isLp = false;
					if(ftpPremiumsList != null && ftpPremiumsList.size() > 0 ) {
						childLPListAll = new ArrayList<Object>();
						for(FtpPremiumsVb ftpPremiumsVb : ftpPremiumsList) {
							if(ftpPremiumsVb.getRecordIndicator() == 1) {
								isLp = true;
								List<ReviewResultVb> reviewResultPremiumList =  reviewRecordPremium(ftpPremiumsVb);
								childLPListAll.add(reviewResultPremiumList);
							}
						}
					}
					if(isLp) {
						childList.put("LP", childLPListAll);
					}else {
						childList.put("LP", null);
					}
					
					
					methodsListAll.add(childList);
					
					methodsMaps.put(methodsVb.getRepricingFlag(), methodsListAll);
				}
				arrListLocal.add(methodsMaps);
			}
			
			return arrListLocal;
		}catch(Exception ex){
			return null;
		}
	}
	
	@Override
	protected List<ReviewResultVb> transformToReviewResults(List<FTPGroupsVb> approvedCollection, List<FTPGroupsVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry(),(!pendingCollection.get(0).getCountry().equals(approvedCollection.get(0).getCountry())));
		if(lCountry.isDataDiffer())
			lResult.add(lCountry);

		ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook(),(!pendingCollection.get(0).getLeBook().equals(approvedCollection.get(0).getLeBook())));
		if(lLeBook.isDataDiffer())
			lResult.add(lLeBook);

		ReviewResultVb lDataSource = new ReviewResultVb(rsb.getString("dataSource"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDataSourceDescription(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDataSourceDescription(),(!pendingCollection.get(0).getDataSourceDescription().equals(approvedCollection.get(0).getDataSourceDescription())));
		if(lDataSource.isDataDiffer())
			lResult.add(lDataSource);

		ReviewResultVb lFtpGroup = new ReviewResultVb(rsb.getString("ftpGroup"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpGroupDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpGroupDesc(),(!pendingCollection.get(0).getFtpGroupDesc().equals(approvedCollection.get(0).getFtpGroupDesc())));
		lResult.add(lFtpGroup);

		ReviewResultVb lFtpSubGroupId = new ReviewResultVb(rsb.getString("ftpSubGroupId"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpSubGroupId(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpSubGroupId(),(!pendingCollection.get(0).getFtpSubGroupId().equals(approvedCollection.get(0).getFtpSubGroupId())));
		lResult.add(lFtpSubGroupId);

		ReviewResultVb lFtpSubGroupPriority = new ReviewResultVb(rsb.getString("ftpSubGroupPriority"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":""+pendingCollection.get(0).getFtpSubGroupPriority(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":""+approvedCollection.get(0).getFtpSubGroupPriority(),
					(pendingCollection.get(0).getFtpSubGroupPriority() != approvedCollection.get(0).getFtpSubGroupPriority()));
		lResult.add(lFtpSubGroupPriority);

		ReviewResultVb lFtpSubGroupDesc = new ReviewResultVb(rsb.getString("ftpSubGroupDesc"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpSubGroupDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpSubGroupDesc(),(!pendingCollection.get(0).getFtpSubGroupDesc().equals(approvedCollection.get(0).getFtpSubGroupDesc())));
		lResult.add(lFtpSubGroupDesc);

		ReviewResultVb lFtpDefaultFlag = new ReviewResultVb(rsb.getString("ftpDefaultFlag"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDefaultGroup(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDefaultGroup(),(!pendingCollection.get(0).getDefaultGroup().equals(approvedCollection.get(0).getDefaultGroup())));
		lResult.add(lFtpDefaultFlag);

		ReviewResultVb lFtpGrpStatus = new ReviewResultVb(rsb.getString("ftpGrpStatus"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getStatusDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getStatusDesc(),(!pendingCollection.get(0).getStatusDesc().equals(approvedCollection.get(0).getStatusDesc())));
		lResult.add(lFtpGrpStatus);

		ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRecordIndicatorDesc(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicatorDesc(),(!pendingCollection.get(0).getRecordIndicatorDesc().equals(approvedCollection.get(0).getRecordIndicatorDesc())));
			lResult.add(lRecordIndicator);
			
		ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMakerName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMakerName(),(!pendingCollection.get(0).getMakerName().equals(approvedCollection.get(0).getMakerName())));
		lResult.add(lMaker);
		
		ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifierName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifierName(),(!pendingCollection.get(0).getVerifierName().equals(approvedCollection.get(0).getVerifierName())));
		lResult.add(lVerifier);				

		ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(),(!pendingCollection.get(0).getDateLastModified().equals(approvedCollection.get(0).getDateLastModified())));
		lResult.add(lDateLastModified);
		ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(),(!pendingCollection.get(0).getDateCreation().equals(approvedCollection.get(0).getDateCreation())));
		lResult.add(lDateCreation);
		
	return lResult;
	}
	
	
	protected List<ReviewResultVb> transformToReviewResultsSourceConfig(List<FTPSourceConfigVb> approvedCollection, List<FTPSourceConfigVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry(),(!pendingCollection.get(0).getCountry().equals(approvedCollection.get(0).getCountry())));
		lResult.add(lCountry);

		ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook(),(!pendingCollection.get(0).getLeBook().equals(approvedCollection.get(0).getLeBook())));
		lResult.add(lLeBook);

		ReviewResultVb lFtpSubGroupId = new ReviewResultVb(rsb.getString("ftpSubGroupId"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpSubGroupId(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpSubGroupId(),(!pendingCollection.get(0).getFtpSubGroupId().equals(approvedCollection.get(0).getFtpSubGroupId())));
		lResult.add(lFtpSubGroupId);

		ReviewResultVb lFilterSequence = new ReviewResultVb(rsb.getString("filterSequence"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":""+pendingCollection.get(0).getFilterSequence(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":""+approvedCollection.get(0).getFilterSequence(),
					(pendingCollection.get(0).getFilterSequence() != approvedCollection.get(0).getFilterSequence()));
		lResult.add(lFilterSequence);

		ReviewResultVb lQueryType = new ReviewResultVb(rsb.getString("queryType"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getQueryTypeDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getQueryTypeDesc(),(!pendingCollection.get(0).getQueryType().equals(approvedCollection.get(0).getQueryType())));
		lResult.add(lQueryType);

		ReviewResultVb lTableName = new ReviewResultVb(rsb.getString("tableName"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTableName(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTableName(),(!pendingCollection.get(0).getTableName().equals(approvedCollection.get(0).getTableName())));
		lResult.add(lTableName);

		ReviewResultVb lColumnName = new ReviewResultVb(rsb.getString("columnName"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getColName(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getColName(),(!pendingCollection.get(0).getColName().equals(approvedCollection.get(0).getColName())));
		lResult.add(lColumnName);


		ReviewResultVb lOperand = new ReviewResultVb(rsb.getString("operand"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getOperand(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getOperand(),(!pendingCollection.get(0).getOperand().equals(approvedCollection.get(0).getOperand())));
		lResult.add(lOperand);

		ReviewResultVb lConditionValue1 = new ReviewResultVb(rsb.getString("conditionValue1"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getConditionValue1(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getConditionValue1(),(!pendingCollection.get(0).getConditionValue1().equals(approvedCollection.get(0).getConditionValue1())));
		lResult.add(lConditionValue1);

		ReviewResultVb lConditionValue2 = new ReviewResultVb(rsb.getString("conditionValue2"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getConditionValue2(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getConditionValue2(),(!pendingCollection.get(0).getConditionValue2().equals(approvedCollection.get(0).getConditionValue2())));
		lResult.add(lConditionValue2);

		ReviewResultVb lFtpScStatus = new ReviewResultVb(rsb.getString("ftpScStatus"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getStatusDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getStatusDesc(),(!pendingCollection.get(0).getStatusDesc().equals(approvedCollection.get(0).getStatusDesc())));
		lResult.add(lFtpScStatus);

		ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRecordIndicatorDesc(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicatorDesc(),(!pendingCollection.get(0).getRecordIndicatorDesc().equals(approvedCollection.get(0).getRecordIndicatorDesc())));
			lResult.add(lRecordIndicator);
			
		ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMakerName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMakerName(),(!pendingCollection.get(0).getMakerName().equals(approvedCollection.get(0).getMakerName())));
		lResult.add(lMaker);
		
		ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifierName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifierName(),(!pendingCollection.get(0).getVerifierName().equals(approvedCollection.get(0).getVerifierName())));
		lResult.add(lVerifier);				

		ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(),(!pendingCollection.get(0).getDateLastModified().equals(approvedCollection.get(0).getDateLastModified())));
		lResult.add(lDateLastModified);
		ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(),(!pendingCollection.get(0).getDateCreation().equals(approvedCollection.get(0).getDateCreation())));
		lResult.add(lDateCreation);
			
		return lResult; 
	}
	
	protected List<ReviewResultVb> transformToReviewResultsMethod(List<FtpMethodsVb> approvedCollection, List<FtpMethodsVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();

		ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry(),(!pendingCollection.get(0).getCountry().equals(approvedCollection.get(0).getCountry())));
		lResult.add(lCountry);

		ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook(),(!pendingCollection.get(0).getLeBook().equals(approvedCollection.get(0).getLeBook())));
		lResult.add(lLeBook);

		ReviewResultVb lFtpSubGroupId = new ReviewResultVb(rsb.getString("ftpSubGroupId"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpSubGroupId(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpSubGroupId(),(!pendingCollection.get(0).getFtpSubGroupId().equals(approvedCollection.get(0).getFtpSubGroupId())));
		lResult.add(lFtpSubGroupId);

		ReviewResultVb lRepricingFlag = new ReviewResultVb(rsb.getString("repricingFlag"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRepricingFlagDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRepricingFlagDesc(),(!pendingCollection.get(0).getRepricingFlagDesc().equals(approvedCollection.get(0).getRepricingFlagDesc())));
		lResult.add(lRepricingFlag);

		ReviewResultVb lMethodType = new ReviewResultVb(rsb.getString("methodType"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMethodTypeDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMethodTypeDesc(),(!pendingCollection.get(0).getMethodTypeDesc().equals(approvedCollection.get(0).getMethodTypeDesc())));
		lResult.add(lMethodType);

		ReviewResultVb lFtpTenorType = new ReviewResultVb(rsb.getString("ftpTenorType"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpTenorTypeDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpTenorTypeDesc(),(!pendingCollection.get(0).getFtpTenorTypeDesc().equals(approvedCollection.get(0).getFtpTenorTypeDesc())));
		lResult.add(lFtpTenorType);

		ReviewResultVb lMethodBalType = new ReviewResultVb(rsb.getString("methodBalType"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMethodBalTypeDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMethodBalTypeDesc(),(!pendingCollection.get(0).getMethodBalTypeDesc().equals(approvedCollection.get(0).getMethodBalTypeDesc())));
		lResult.add(lMethodBalType);

		ReviewResultVb lInterestBasis = new ReviewResultVb(rsb.getString("interestBasis"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getInterestBasisDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getInterestBasisDesc(),(!pendingCollection.get(0).getInterestBasisDesc().equals(approvedCollection.get(0).getInterestBasisDesc())));
		lResult.add(lInterestBasis);

		ReviewResultVb lFtpApplyRate = new ReviewResultVb(rsb.getString("ftpApplyRate"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpApplyRateDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpApplyRateDesc(),(!pendingCollection.get(0).getFtpApplyRateDesc().equals(approvedCollection.get(0).getFtpApplyRateDesc())));
		lResult.add(lFtpApplyRate);

		ReviewResultVb lFtpCurveId = new ReviewResultVb(rsb.getString("ftpCurveId"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpCurveId(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpCurveId(),(!pendingCollection.get(0).getFtpCurveId().equals(approvedCollection.get(0).getFtpCurveId())));
		lResult.add(lFtpCurveId);

		ReviewResultVb lFtpMtStatus = new ReviewResultVb(rsb.getString("ftpMtStatus"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getStatusDesc(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getStatusDesc(),(!pendingCollection.get(0).getStatusDesc().equals(approvedCollection.get(0).getStatusDesc())));
		lResult.add(lFtpMtStatus);

		ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRecordIndicatorDesc(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicatorDesc(),(!pendingCollection.get(0).getRecordIndicatorDesc().equals(approvedCollection.get(0).getRecordIndicatorDesc())));
			lResult.add(lRecordIndicator);
			
		ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMakerName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMakerName(),(!pendingCollection.get(0).getMakerName().equals(approvedCollection.get(0).getMakerName())));
		lResult.add(lMaker);
		
		ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifierName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifierName(),(!pendingCollection.get(0).getVerifierName().equals(approvedCollection.get(0).getVerifierName())));
		lResult.add(lVerifier);				

		ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(),(!pendingCollection.get(0).getDateLastModified().equals(approvedCollection.get(0).getDateLastModified())));
		lResult.add(lDateLastModified);
		ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
			(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(),(!pendingCollection.get(0).getDateCreation().equals(approvedCollection.get(0).getDateCreation())));
		lResult.add(lDateCreation);
		return lResult; 
	}
	
	public List<ReviewResultVb> reviewRecordCurve(FTPCurveVb vObject){
		try{
			List<FTPCurveVb> approvedCollection = ftpCurvesDao.getQueryResults(vObject,0);
			List<FTPCurveVb> pendingCollection = ftpCurvesDao.getQueryResults(vObject,1);
			return transformToReviewResultsCurves(approvedCollection,pendingCollection);
		}catch(Exception ex){
			return null;
		}
	}
	
	protected List<ReviewResultVb> transformToReviewResultsCurves(List<FTPCurveVb> approvedCollection, List<FTPCurveVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();

				ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry(),(!pendingCollection.get(0).getCountry().equals(approvedCollection.get(0).getCountry())));
				lResult.add(lCountry);

				ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook(),(!pendingCollection.get(0).getLeBook().equals(approvedCollection.get(0).getLeBook())));
				lResult.add(lLeBook);

				ReviewResultVb lFtpCurveId = new ReviewResultVb(rsb.getString("ftpCurveId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpCurveId(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpCurveId(),(!pendingCollection.get(0).getFtpCurveId().equals(approvedCollection.get(0).getFtpCurveId())));
				lResult.add(lFtpCurveId);

				ReviewResultVb lEffectiveDate = new ReviewResultVb(rsb.getString("effectiveDate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEffectiveDate(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEffectiveDate(),(!pendingCollection.get(0).getEffectiveDate().equals(approvedCollection.get(0).getEffectiveDate())));
				lResult.add(lEffectiveDate);

				ReviewResultVb lTenorApplicationCode = new ReviewResultVb(rsb.getString("tenorApplicationCode"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorApplicationCodeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorApplicationCodeDesc(),(!pendingCollection.get(0).getTenorApplicationCodeDesc().equals(approvedCollection.get(0).getTenorApplicationCodeDesc())));
				lResult.add(lTenorApplicationCode);

				ReviewResultVb lTenorCode = new ReviewResultVb(rsb.getString("tenorCode"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorCodeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorCodeDesc(),(!pendingCollection.get(0).getTenorCodeDesc().equals(approvedCollection.get(0).getTenorCodeDesc())));
				lResult.add(lTenorCode);

				ReviewResultVb lVisionSbuAttribute = new ReviewResultVb(rsb.getString("visionSbuAttribute"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVisionSbuAttributeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVisionSbuAttributeDesc(),(!pendingCollection.get(0).getVisionSbuAttribute().equals(approvedCollection.get(0).getVisionSbuAttribute())));
				lResult.add(lVisionSbuAttribute);

				ReviewResultVb lProductAttribute = new ReviewResultVb(rsb.getString("productAttribute"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getProductAttributeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getProductAttributeDesc(),(!pendingCollection.get(0).getProductAttributeDesc().equals(approvedCollection.get(0).getProductAttributeDesc())));
				lResult.add(lProductAttribute);

				ReviewResultVb lCurrency = new ReviewResultVb(rsb.getString("currency"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCurrency(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCurrency(),(!pendingCollection.get(0).getCurrency().equals(approvedCollection.get(0).getCurrency())));
				lResult.add(lCurrency);

				ReviewResultVb lIntRateStart = new ReviewResultVb(rsb.getString("intRateStart"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getIntRateStart()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getIntRateStart()),(!pendingCollection.get(0).getIntRateStart().equals(approvedCollection.get(0).getIntRateStart())));
				lResult.add(lIntRateStart);

				ReviewResultVb lIntRateEnd = new ReviewResultVb(rsb.getString("intRateEnd"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getIntRateEnd()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getIntRateEnd()),(!pendingCollection.get(0).getIntRateEnd().equals(approvedCollection.get(0).getIntRateEnd())));
				lResult.add(lIntRateEnd);

				ReviewResultVb lAmountStart = new ReviewResultVb(rsb.getString("amountStart"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getAmountStart()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getAmountStart()),(!pendingCollection.get(0).getAmountStart().equals(approvedCollection.get(0).getAmountStart())));
				lResult.add(lAmountStart);

				ReviewResultVb lAmountEnd = new ReviewResultVb(rsb.getString("amountEnd"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getAmountEnd()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getAmountEnd()),(!pendingCollection.get(0).getAmountEnd().equals(approvedCollection.get(0).getAmountEnd())));
				lResult.add(lAmountEnd);

				ReviewResultVb lEndDate = new ReviewResultVb(rsb.getString("endDate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEndDate(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEndDate(),(!pendingCollection.get(0).getEndDate().equals(approvedCollection.get(0).getEndDate())));
				lResult.add(lEndDate);

				ReviewResultVb lFtpRate = new ReviewResultVb(rsb.getString("ftpRate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getFtpRate()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getFtpRate()),(!pendingCollection.get(0).getFtpRate().equals(approvedCollection.get(0).getFtpRate())));
				lResult.add(lFtpRate);

				ReviewResultVb lCrrRate = new ReviewResultVb(rsb.getString("crrRate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getCrrRate()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getCrrRate()),(!pendingCollection.get(0).getCrrRate().equals(approvedCollection.get(0).getCrrRate())));
				lResult.add(lCrrRate);

				ReviewResultVb lFtpCurveStatus = new ReviewResultVb(rsb.getString("ftpCurveStatus"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getStatusDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getStatusDesc(),(!pendingCollection.get(0).getStatusDesc().equals(approvedCollection.get(0).getStatusDesc())));
				lResult.add(lFtpCurveStatus);

				ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRecordIndicatorDesc(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicatorDesc(),(!pendingCollection.get(0).getRecordIndicatorDesc().equals(approvedCollection.get(0).getRecordIndicatorDesc())));
					lResult.add(lRecordIndicator);
					
				ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMakerName(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMakerName(),(!pendingCollection.get(0).getMakerName().equals(approvedCollection.get(0).getMakerName())));
				lResult.add(lMaker);
				
				ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifierName(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifierName(),(!pendingCollection.get(0).getVerifierName().equals(approvedCollection.get(0).getVerifierName())));
				lResult.add(lVerifier);				

				ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(),(!pendingCollection.get(0).getDateLastModified().equals(approvedCollection.get(0).getDateLastModified())));
				lResult.add(lDateLastModified);
				ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(),(!pendingCollection.get(0).getDateCreation().equals(approvedCollection.get(0).getDateCreation())));
				lResult.add(lDateCreation);
				return lResult; 
				

	}	
	
	public List<ReviewResultVb> reviewRecordAddon(FtpAddonVb vObject){
		try{
			List<FtpAddonVb> approvedCollection = ftpAddonDao.getQueryResults(vObject,0);
			List<FtpAddonVb> pendingCollection = ftpAddonDao.getQueryResults(vObject,1);
			return transformToReviewResultsAddOn(approvedCollection,pendingCollection);
		}catch(Exception ex){
			return null;
		}
	}
	protected List<ReviewResultVb> transformToReviewResultsAddOn(List<FtpAddonVb> approvedCollection, List<FtpAddonVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();

				ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry(),(!pendingCollection.get(0).getCountry().equals(approvedCollection.get(0).getCountry())));
				lResult.add(lCountry);

				ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook(),(!pendingCollection.get(0).getLeBook().equals(approvedCollection.get(0).getLeBook())));
				lResult.add(lLeBook);

				ReviewResultVb lFtpCurveId = new ReviewResultVb(rsb.getString("ftpCurveId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpCurveId(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpCurveId(),(!pendingCollection.get(0).getFtpCurveId().equals(approvedCollection.get(0).getFtpCurveId())));
				lResult.add(lFtpCurveId);

				ReviewResultVb lEffectiveDate = new ReviewResultVb(rsb.getString("effectiveDate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEffectiveDate(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEffectiveDate(),(!pendingCollection.get(0).getEffectiveDate().equals(approvedCollection.get(0).getEffectiveDate())));
				lResult.add(lEffectiveDate);

				ReviewResultVb lTenorApplicationCode = new ReviewResultVb(rsb.getString("tenorApplicationCode"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorApplicationCodeDesc(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorApplicationCodeDesc(),(!pendingCollection.get(0).getTenorApplicationCodeDesc().equals(approvedCollection.get(0).getTenorApplicationCodeDesc())));
					lResult.add(lTenorApplicationCode);

				ReviewResultVb lTenorCode = new ReviewResultVb(rsb.getString("tenorCode"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorCodeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorCodeDesc(),(!pendingCollection.get(0).getTenorCodeDesc().equals(approvedCollection.get(0).getTenorCodeDesc())));
				lResult.add(lTenorCode);

				ReviewResultVb lCustomerId = new ReviewResultVb(rsb.getString("customerId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCustomerIdDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCustomerIdDesc(),(!pendingCollection.get(0).getCustomerId().equals(approvedCollection.get(0).getCustomerId())));
				lResult.add(lCustomerId);

				ReviewResultVb lContractId = new ReviewResultVb(rsb.getString("contractId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getContractIdDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getContractIdDesc(),(!pendingCollection.get(0).getContractId().equals(approvedCollection.get(0).getContractId())));
				lResult.add(lContractId);

				ReviewResultVb lSubsidyRate = new ReviewResultVb(rsb.getString("subsidyRate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getSubsidyRate()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getSubsidyRate()),(!pendingCollection.get(0).getSubsidyRate().equals(approvedCollection.get(0).getSubsidyRate())));
				lResult.add(lSubsidyRate);

				ReviewResultVb lEndDate = new ReviewResultVb(rsb.getString("endDate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEndDate(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEndDate(),(!pendingCollection.get(0).getEndDate().equals(approvedCollection.get(0).getEndDate())));
				lResult.add(lEndDate);

				ReviewResultVb lFtpCurveStatus = new ReviewResultVb(rsb.getString("ftpAddonStatus"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getStatusDesc(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getStatusDesc(),(!pendingCollection.get(0).getStatusDesc().equals(approvedCollection.get(0).getStatusDesc())));
				lResult.add(lFtpCurveStatus);

				ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRecordIndicatorDesc(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicatorDesc(),(!pendingCollection.get(0).getRecordIndicatorDesc().equals(approvedCollection.get(0).getRecordIndicatorDesc())));
					lResult.add(lRecordIndicator);
					
				ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMakerName(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMakerName(),(!pendingCollection.get(0).getMakerName().equals(approvedCollection.get(0).getMakerName())));
				lResult.add(lMaker);
				
				ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifierName(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifierName(),(!pendingCollection.get(0).getVerifierName().equals(approvedCollection.get(0).getVerifierName())));
				lResult.add(lVerifier);				

				ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(),(!pendingCollection.get(0).getDateLastModified().equals(approvedCollection.get(0).getDateLastModified())));
				lResult.add(lDateLastModified);
				ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(),(!pendingCollection.get(0).getDateCreation().equals(approvedCollection.get(0).getDateCreation())));
				lResult.add(lDateCreation);
				return lResult; 
				

	}

	public String formatToTwoDecimalPlaces(String numberString){
		try {
	        // Create a NumberFormat instance to handle comma separation (Locale.US assumes comma as thousands separator)
	        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
	        
	        // Parse the string to a number
	        Number number = format.parse(numberString);
	        
	        // Convert the Number to a double
	        double doubleValue = number.doubleValue();
	        
	        // Use DecimalFormat to format the number to two decimal places
	        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
			
	        return df.format(doubleValue);
		} catch (ParseException e) {
		}
		return numberString;
    }
	
	public List<ReviewResultVb> reviewRecordPremium(FtpPremiumsVb vObject){
		try{
			List<FtpPremiumsVb> approvedCollection = ftpPremiumsDao.getQueryResults(vObject,0);
			List<FtpPremiumsVb> pendingCollection = ftpPremiumsDao.getQueryResults(vObject,1);
			return transformToReviewResultsPremium(approvedCollection,pendingCollection);
		}catch(Exception ex){
			return null;
		}
	}
	
	protected List<ReviewResultVb> transformToReviewResultsPremium(List<FtpPremiumsVb> approvedCollection, List<FtpPremiumsVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();

				ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry(),(!pendingCollection.get(0).getCountry().equals(approvedCollection.get(0).getCountry())));
				lResult.add(lCountry);

				ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook(),(!pendingCollection.get(0).getLeBook().equals(approvedCollection.get(0).getLeBook())));
				lResult.add(lLeBook);

				ReviewResultVb lFtpCurveId = new ReviewResultVb(rsb.getString("ftpCurveId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpCurveId(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpCurveId(),(!pendingCollection.get(0).getFtpCurveId().equals(approvedCollection.get(0).getFtpCurveId())));
				lResult.add(lFtpCurveId);

				ReviewResultVb lEffectiveDate = new ReviewResultVb(rsb.getString("effectiveDate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEffectiveDate(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEffectiveDate(),(!pendingCollection.get(0).getEffectiveDate().equals(approvedCollection.get(0).getEffectiveDate())));
				lResult.add(lEffectiveDate);


				ReviewResultVb lLpTenorApplicationCode = new ReviewResultVb(rsb.getString("lpTenorApplicationCode"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLpTenorApplicationCodeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLpTenorApplicationCodeDesc(),(!pendingCollection.get(0).getLpTenorApplicationCodeDesc().equals(approvedCollection.get(0).getLpTenorApplicationCodeDesc())));
				lResult.add(lLpTenorApplicationCode);

				ReviewResultVb lLpTenorCode = new ReviewResultVb(rsb.getString("lpTenorCode"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLpTenorCodeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLpTenorCodeDesc(),(!pendingCollection.get(0).getLpTenorCodeDesc().equals(approvedCollection.get(0).getLpTenorCodeDesc())));
				lResult.add(lLpTenorCode);

				ReviewResultVb lVisionSector = new ReviewResultVb(rsb.getString("visionSector"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVisionSectorDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVisionSectorDesc(),(!pendingCollection.get(0).getVisionSectorDesc().equals(approvedCollection.get(0).getVisionSectorDesc())));
				lResult.add(lVisionSector);

				ReviewResultVb lVisionSbuAttribute = new ReviewResultVb(rsb.getString("visionSbuAttribute"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVisionSbuAttributeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVisionSbuAttributeDesc(),(!pendingCollection.get(0).getVisionSbuAttributeDesc().equals(approvedCollection.get(0).getVisionSbuAttributeDesc())));
				lResult.add(lVisionSbuAttribute);

				ReviewResultVb lProductAttribute = new ReviewResultVb(rsb.getString("productAttribute"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getProductAttributeDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getProductAttributeDesc(),(!pendingCollection.get(0).getProductAttributeDesc().equals(approvedCollection.get(0).getProductAttributeDesc())));
				lResult.add(lProductAttribute);

				ReviewResultVb lCustomerId = new ReviewResultVb(rsb.getString("customerId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCustomerIdDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCustomerIdDesc(),(!pendingCollection.get(0).getCustomerIdDesc().equals(approvedCollection.get(0).getCustomerIdDesc())));
				lResult.add(lCustomerId);

				ReviewResultVb lContractId = new ReviewResultVb(rsb.getString("contractId"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getContractIdDesc(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getContractIdDesc(),(!pendingCollection.get(0).getContractIdDesc().equals(approvedCollection.get(0).getContractIdDesc())));
				lResult.add(lContractId);

				ReviewResultVb lCurrency = new ReviewResultVb(rsb.getString("currency"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCurrency(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCurrency(),(!pendingCollection.get(0).getCurrency().equals(approvedCollection.get(0).getCurrency())));
				lResult.add(lCurrency);

				ReviewResultVb lPremiumRate = new ReviewResultVb(rsb.getString("premiumRate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":formatToTwoDecimalPlaces(pendingCollection.get(0).getPremiumRate()),
					(approvedCollection == null || approvedCollection.isEmpty())?"":formatToTwoDecimalPlaces(approvedCollection.get(0).getPremiumRate()),(!pendingCollection.get(0).getPremiumRate().equals(approvedCollection.get(0).getPremiumRate())));
				lResult.add(lPremiumRate);

				ReviewResultVb lEndDate = new ReviewResultVb(rsb.getString("endDate"),
					(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEndDate(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEndDate(),(!pendingCollection.get(0).getEndDate().equals(approvedCollection.get(0).getEndDate())));
				lResult.add(lEndDate);

				ReviewResultVb lFtpCurveStatus = new ReviewResultVb(rsb.getString("ftpPremiumStatus"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getStatusDesc(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getStatusDesc(),(!pendingCollection.get(0).getStatusDesc().equals(approvedCollection.get(0).getStatusDesc())));
				lResult.add(lFtpCurveStatus);

				ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRecordIndicatorDesc(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicatorDesc(),(!pendingCollection.get(0).getRecordIndicatorDesc().equals(approvedCollection.get(0).getRecordIndicatorDesc())));
					lResult.add(lRecordIndicator);
					
				ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMakerName(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMakerName(),(!pendingCollection.get(0).getMakerName().equals(approvedCollection.get(0).getMakerName())));
				lResult.add(lMaker);
				
				ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),
						(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifierName(),
						(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifierName(),(!pendingCollection.get(0).getVerifierName().equals(approvedCollection.get(0).getVerifierName())));
				lResult.add(lVerifier);				

				ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(),(!pendingCollection.get(0).getDateLastModified().equals(approvedCollection.get(0).getDateLastModified())));
				lResult.add(lDateLastModified);
				ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
					(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(),(!pendingCollection.get(0).getDateCreation().equals(approvedCollection.get(0).getDateCreation())));
				lResult.add(lDateCreation);
				return lResult; 
				

	}
	
	public ExceptionCode bulkApprove(List<FTPGroupsVb> vObjects,FTPGroupsVb queryPopObj){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		List<FTPGroupsVb> clonedObjects = null;
		try{
			setVerifReqDeleteType(queryPopObj);
//			doFormateData(vObjects);
			clonedObjects = deepCopy.copyCollection(vObjects);
			for (FTPGroupsVb object : vObjects) {
				object.setVerificationRequired(queryPopObj.isVerificationRequired());
			}
			exceptionCode =  ftpGroupsDao.bulkApprove(vObjects,queryPopObj);
//			ArrayList<FTPGroupsVb> tmpResult = (ArrayList<FTPGroupsVb>)getScreenDao().getQueryResults(queryPopObj,1);
//			exceptionCode.setResponse(tmpResult);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Approve Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	
	public ExceptionCode bulkReject(List<FTPGroupsVb> vObjects,FTPGroupsVb queryPopObj){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		List<FTPGroupsVb> clonedObjects = null;
		try{
			setVerifReqDeleteType(queryPopObj);
			clonedObjects = deepCopy.copyCollection(vObjects);
			exceptionCode =  ftpGroupsDao.doBulkReject(vObjects, queryPopObj);
			exceptionCode.setOtherInfo(queryPopObj);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Bulk Reject Exception " + rex.getCode().getErrorMsg());
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(queryPopObj);
			exceptionCode.setResponse(clonedObjects);
			return exceptionCode;
		}
	}
	
	public ExceptionCode bulkDeleteRecord(List<FTPGroupsVb> vObjects, FTPGroupsVb vObject){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		List<FTPGroupsVb> clonedObject = null;
		try{
//			setAtNtValues(vObjects);
			setVerifReqDeleteType(vObject);
			clonedObject = deepCopy.copyCollection(vObjects);
/*			doFormateData(vObjects);
			exceptionCode = doValidate(vObjects);
			if(exceptionCode!=null && exceptionCode.getErrorMsg()!=""){
				return exceptionCode;
			}*/
			for (FTPGroupsVb object : vObjects) {
				object.setVerificationRequired(vObject.isVerificationRequired());
			}
			if(!vObject.isVerificationRequired()){
				exceptionCode = ftpGroupsDao.doDeleteApprRecord(vObjects, vObject);
			}else{
				exceptionCode = ftpGroupsDao.doDeleteRecord(vObjects, vObject);
			}
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(vObjects);
			return exceptionCode;
		}catch(RuntimeCustomException rex){
			logger.error("Delete Exception " + rex.getCode().getErrorMsg());
			logger.error( ((vObject==null)? "vObject is Null":vObject.toString()));
			exceptionCode = rex.getCode();
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(clonedObject);
			return exceptionCode;
		}
	}
	
	public ExceptionCode getCurveListFoOverAllReveiw(FTPCurveVb vObject){
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		setVerifReqDeleteType(ftpGroupsVb);
		vObject.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
		vObject.setStaticDelete(ftpGroupsVb.isStaticDelete());
		List<FTPCurveVb> collTemp = ftpCurvesDao.getQueryPopupResultsCurvesOverAllReview(vObject);
		if(collTemp.size() == 0){
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
			 List<LocalDate> dates = new ArrayList<>();
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			 
			 for(FTPCurveVb ftpPremiumsVb : collTemp) {
				 LocalDate localDate = LocalDate.parse(ftpPremiumsVb.getEffectiveDate(), dateFormatter);
	             dates.add(localDate);
			 }
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			Map<Integer, Map<Integer, Map<Integer, List<FTPCurveVb>>>> groupedData = groupCurvesData(collTemp, dates);
			exceptionCode.setResponse(collTemp);
			exceptionCode.setResponse1(groupedData);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}
	}
	
	public Map<Integer, Map<Integer, Map<Integer, List<FTPCurveVb>>>> groupCurvesData(List<FTPCurveVb> dataList,  List<LocalDate> dates) {
		// Group the CurveData list by year and month using the dates
//        Map<Integer, Map<Integer, Map<Integer, List<FTPCurveVb>>>> groupedData = new TreeMap<>();
		 Map<Integer, Map<Integer, Map<Integer, List<FTPCurveVb>>>> groupedData = new TreeMap(Comparator.reverseOrder());

		 for (int i = 0; i < dates.size(); i++) {
			    LocalDate date = dates.get(i);
			    int year = date.getYear();
			    int month = date.getMonthValue();
			    int day = date.getDayOfMonth();

			    // Create nested maps if not present
			    groupedData
			        .computeIfAbsent(year, k -> new TreeMap<Integer, Map<Integer, List<FTPCurveVb>>>(Comparator.reverseOrder()))
			        .computeIfAbsent(month, k -> new TreeMap<Integer, List<FTPCurveVb>>(Comparator.reverseOrder()))
			        .computeIfAbsent(day, k -> new ArrayList<>())
			        .add(dataList.get(i));
		}
       /* groupedData.keySet().stream().sorted().forEach(year -> {
//            System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted() // Sort by month
                .forEach(month -> {
//                    System.out.println("  Month: " + month);
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted() // Sort by day
                        .forEach(day -> {
//                            groupedData.get(year).get(month).get(day).forEach(curveData ->System.out.println("      " + curveData));
                        });
                });
        });*/
        
        groupedData.keySet().stream().sorted(Comparator.reverseOrder()).forEach(year -> {
        	System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted(Comparator.reverseOrder()) // Sort by month in descending order
                .forEach(month -> {
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted(Comparator.reverseOrder()) // Sort by day in descending order
                        .forEach(day -> {
                            // Do something with the grouped data if needed
                            // groupedData.get(year).get(month).get(day).forEach(curveData -> System.out.println("Year: " + year + " Month: " + month + " Day: " + day + " " + curveData));
                        });
                });
        });
	
        return groupedData;
	}
	public ExceptionCode getAddonListFoOverAllReveiw(FtpAddonVb vObject){
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		setVerifReqDeleteType(ftpGroupsVb);
		vObject.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
		vObject.setStaticDelete(ftpGroupsVb.isStaticDelete());
		List<FtpAddonVb> collTemp = ftpAddonDao.getQueryPopupResultsAddOnOverAllReview(vObject);
		if(collTemp.size() == 0){
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
			 List<LocalDate> dates = new ArrayList<>();
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			 
			 for(FtpAddonVb ftpPremiumsVb : collTemp) {
				 LocalDate localDate = LocalDate.parse(ftpPremiumsVb.getEffectiveDate(), dateFormatter);
	             dates.add(localDate);
			 }
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			Map<Integer, Map<Integer, Map<Integer, List<FtpAddonVb>>>> groupedData = groupAddOnData(collTemp, dates);
			exceptionCode.setResponse(collTemp);
			exceptionCode.setResponse1(groupedData);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}
	}
	
	public Map<Integer, Map<Integer, Map<Integer, List<FtpAddonVb>>>> groupAddOnData(List<FtpAddonVb> dataList,  List<LocalDate> dates) {
		// Group the CurveData list by year and month using the dates
//        Map<Integer, Map<Integer, Map<Integer, List<FtpAddonVb>>>> groupedData = new TreeMap<>();
		 Map<Integer, Map<Integer, Map<Integer, List<FtpAddonVb>>>> groupedData = new TreeMap(Comparator.reverseOrder());

		 for (int i = 0; i < dates.size(); i++) {
			    LocalDate date = dates.get(i);
			    int year = date.getYear();
			    int month = date.getMonthValue();
			    int day = date.getDayOfMonth();

			    // Create nested maps if not present
			    groupedData
			        .computeIfAbsent(year, k -> new TreeMap<Integer, Map<Integer, List<FtpAddonVb>>>(Comparator.reverseOrder()))
			        .computeIfAbsent(month, k -> new TreeMap<Integer, List<FtpAddonVb>>(Comparator.reverseOrder()))
			        .computeIfAbsent(day, k -> new ArrayList<>())
			        .add(dataList.get(i));
		}
       /* groupedData.keySet().stream().sorted().forEach(year -> {
//            System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted() // Sort by month
                .forEach(month -> {
//                    System.out.println("  Month: " + month);
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted() // Sort by day
                        .forEach(day -> {
//                            groupedData.get(year).get(month).get(day).forEach(curveData ->System.out.println("      " + curveData));
                        });
                });
        });*/
        
        groupedData.keySet().stream().sorted(Comparator.reverseOrder()).forEach(year -> {
        	System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted(Comparator.reverseOrder()) // Sort by month in descending order
                .forEach(month -> {
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted(Comparator.reverseOrder()) // Sort by day in descending order
                        .forEach(day -> {
                            // Do something with the grouped data if needed
                            // groupedData.get(year).get(month).get(day).forEach(curveData -> System.out.println("Year: " + year + " Month: " + month + " Day: " + day + " " + curveData));
                        });
                });
        });
	
        return groupedData;
	}
	
	public ExceptionCode getPremiumsListFoOverAllReveiw(FtpPremiumsVb vObject){
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		setVerifReqDeleteType(ftpGroupsVb);
		vObject.setVerificationRequired(ftpGroupsVb.isVerificationRequired());
		vObject.setStaticDelete(ftpGroupsVb.isStaticDelete());
		List<FtpPremiumsVb> collTemp = ftpPremiumsDao.getQueryPopupResultsPremiumsOverAllReview(vObject);
		if(collTemp.size() == 0){
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
			 List<LocalDate> dates = new ArrayList<>();
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
			 
			 for(FtpPremiumsVb ftpPremiumsVb : collTemp) {
				 LocalDate localDate = LocalDate.parse(ftpPremiumsVb.getEffectiveDate(), dateFormatter);
	             dates.add(localDate);
			 }
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			Map<Integer, Map<Integer, Map<Integer, List<FtpPremiumsVb>>>> groupedData = groupPremiumsData(collTemp, dates);
			exceptionCode.setResponse(collTemp);
			exceptionCode.setResponse1(groupedData);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}
	}
	public Map<Integer, Map<Integer, Map<Integer, List<FtpPremiumsVb>>>> groupPremiumsData(List<FtpPremiumsVb> dataList,  List<LocalDate> dates) {
		// Group the CurveData list by year and month using the dates
//        Map<Integer, Map<Integer, Map<Integer, List<FtpPremiumsVb>>>> groupedData = new TreeMap<>();
		 Map<Integer, Map<Integer, Map<Integer, List<FtpPremiumsVb>>>> groupedData = new TreeMap(Comparator.reverseOrder());

		 for (int i = 0; i < dates.size(); i++) {
			    LocalDate date = dates.get(i);
			    int year = date.getYear();
			    int month = date.getMonthValue();
			    int day = date.getDayOfMonth();

			    // Create nested maps if not present
			    groupedData
			        .computeIfAbsent(year, k -> new TreeMap<Integer, Map<Integer, List<FtpPremiumsVb>>>(Comparator.reverseOrder()))
			        .computeIfAbsent(month, k -> new TreeMap<Integer, List<FtpPremiumsVb>>(Comparator.reverseOrder()))
			        .computeIfAbsent(day, k -> new ArrayList<>())
			        .add(dataList.get(i));
		}
       /* groupedData.keySet().stream().sorted().forEach(year -> {
//            System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted() // Sort by month
                .forEach(month -> {
//                    System.out.println("  Month: " + month);
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted() // Sort by day
                        .forEach(day -> {
//                            groupedData.get(year).get(month).get(day).forEach(curveData ->System.out.println("      " + curveData));
                        });
                });
        });*/
        
        groupedData.keySet().stream().sorted(Comparator.reverseOrder()).forEach(year -> {
        	System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted(Comparator.reverseOrder()) // Sort by month in descending order
                .forEach(month -> {
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted(Comparator.reverseOrder()) // Sort by day in descending order
                        .forEach(day -> {
                            // Do something with the grouped data if needed
                            // groupedData.get(year).get(month).get(day).forEach(curveData -> System.out.println("Year: " + year + " Month: " + month + " Day: " + day + " " + curveData));
                        });
                });
        });
	
        return groupedData;
	}
	
	
	
	// New End
	

	public ExceptionCode getQueryResultTuning(FTPSourceConfigVb ftpSourceConfigVb) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			ArrayList colTemp = (ArrayList) ftpSourceConfigDao.getQueryResultTuning(ftpSourceConfigVb);
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			exceptionCode.setOtherInfo(ftpSourceConfigVb);
			exceptionCode.setResponse(colTemp);
			return exceptionCode;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception in getting the getAllQueryPopupResult results.", ex);
			return null;
		}
	}

	@Override
	protected AbstractDao<FTPGroupsVb> getScreenDao() {
		return ftpGroupsDao;
	}

	public FTPGroupsDao getFtpGroupsDao() {
		return ftpGroupsDao;
	}

	public void setFtpGroupsDao(FTPGroupsDao ftpGroupsDao) {
		this.ftpGroupsDao = ftpGroupsDao;
	}

	@Override
	protected void doSetDesctiptionsAfterQuery(FTPGroupsVb vObject) {
		/*
		 * List<FTPGroupsVb> lResult = getMergeTableDao().getBsGlDesc(vObject);
		 * if(lResult != null && !lResult.isEmpty()){
		 * vObject.setBsGlDescription(lResult.get(0).getBsGlDescription()); }
		 */
	}



	public ExceptionCode getTableColumns(FTPSourceConfigVb queryPopupObj) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			List<FTPSourceConfigVb> arrListResult = ftpSourceConfigDao.getTableColumns(queryPopupObj);
			if (arrListResult != null && arrListResult.size() > 0) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setResponse(arrListResult);
			} else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
			exceptionCode.setOtherInfo(queryPopupObj);
			return exceptionCode;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception in getting the getAllQueryPopupResult results.", ex);
			return null;
		}
	}

	public FTPSourceConfigDao getFtpSourceConfigDao() {
		return ftpSourceConfigDao;
	}

	public void setFtpSourceConfigDao(FTPSourceConfigDao ftpSourceConfigDao) {
		this.ftpSourceConfigDao = ftpSourceConfigDao;
	}


}