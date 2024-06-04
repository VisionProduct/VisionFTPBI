package com.vision.wb;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.dao.AbstractDao;
import com.vision.dao.FTPGroupsDao;
import com.vision.dao.FTPSourceConfigDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.DeepCopy;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.CommonVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.ReviewResultVb;

@Component
public class FTPGroupsWb extends AbstractDynaWorkerBean<FTPGroupsVb>{
	@Autowired
	private FTPGroupsDao ftpGroupsDao;
	@Autowired
	private FTPSourceConfigDao ftpSourceConfigDao;
	
	public static Logger logger = LoggerFactory.getLogger(FTPGroupsWb.class);
	
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);//Status
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
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1304); //Repricing_Flag
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1310); // FTP_Curve_ID
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1301); // Method_Bal_type
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1302); // FTP_Tenor_Type
			arrListLocal.add(collTemp);
//			collTemp = getFtpGroupsDao().getVisionSbu();
			//collTemp = getFtpGroupsDao().getVisionSbu( String.valueOf( CustomContextHolder.getContext().getVisionId()), String.valueOf(System.currentTimeMillis()), "PR088" );
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(8); // Tenor_ Application_Code
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(6); // Interest Basis
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1303); // Operand
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1304); // FTP Apply Rate
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1360);//FTP Engine Method SubType
			arrListLocal.add(collTemp);
			collTemp = getFtpGroupsDao().callProcToPopulateVisionSBUData(); // Vision SBU
			arrListLocal.add(collTemp);			
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_GROUPS");
			arrListLocal.add(collTemp);

			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected void setAtNtValues(FTPGroupsVb vObject) {
		vObject.setFtpGroupAt(1301);
		vObject.setSourceReferenceAt(1302);
		vObject.setFtpControlStatusNt(1);
		vObject.setFtpGroupStatusNt(1);
		vObject.setRecordIndicatorNt(7);
		vObject.setDataSourceAt(10);
		
	}
	@Override
	protected void setVerifReqDeleteType(FTPGroupsVb vObject) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_GROUPS");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(false);
	}
	public ExceptionCode queryResultSource(FTPGroupsVb vObject){
		ExceptionCode exceptionCode = new ExceptionCode();
		
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		
		List<FTPGroupsVb> collSourceTemp = new ArrayList();
		List<FtpMethodsVb> collTempMethods = new ArrayList();
		collSourceTemp = (ArrayList)getFtpGroupsDao().getQueryResultsSource(vObject);
		collTempMethods = (ArrayList)getFtpGroupsDao().getQueryResultMethods(vObject);
		exceptionCode.setActionType("Query");
		if(collSourceTemp.size() == 0 && collTempMethods.size() == 0){
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
			doSetDesctiptionsAfterQuery(collSourceTemp);
			doSetDesctiptionsAfterQuery(vObject);
			
			FtpMethodsVb ftpMethodsVb = (FtpMethodsVb) collTempMethods.get(0);
			FTPCurveVb ftpCurveVb = new FTPCurveVb();
			String businessDate = ftpGroupsDao.businessDay();
			ftpCurveVb.setEffectiveDate(businessDate);
			
			ftpCurveVb.setFtpCurveId(ftpMethodsVb.getFtpCurveId());
			
			List<NumSubTabVb> tenoApplicatoinCodes = getNumSubTabDao().findActiveNumSubTabsByNumTab(8);
			
			ftpCurveVb.setTenorBucketApplicationCode(tenoApplicatoinCodes.get(0).getNumSubTab());
			ftpCurveVb.setLpTenorBucketApplicationCode(tenoApplicatoinCodes.get(0).getNumSubTab());
			
			List<FTPGroupsVb> curvesList = ftpGroupsDao.getQueryPopupResultsCurves(ftpCurveVb.getFtpCurveId(),ftpCurveVb.getEffectiveDate(),vObject,ftpCurveVb.isReloadFlag());
			
			List<FTPGroupsVb> addOnList = ftpGroupsDao.getQueryPopupResultsAddOn(ftpCurveVb.getFtpCurveId(),ftpCurveVb.getEffectiveDate(),vObject,ftpCurveVb.isReloadFlag());
			
			
			List<FTPGroupsVb> tenorCodeList = (ArrayList) ftpGroupsDao.getTenorCode(ftpCurveVb.getTenorBucketApplicationCode());
			List<FTPGroupsVb> lpTenorCodeList = (ArrayList) ftpGroupsDao.getTenorCode(ftpCurveVb.getLpTenorBucketApplicationCode());
			ArrayList colTemp = (ArrayList)ftpGroupsDao.getQueryPopupResultPremium(ftpCurveVb);
			ArrayList tmpList = new ArrayList();
			tmpList.add(colTemp); // Premium
			tmpList.add(tenorCodeList); // Tenor Code List
			tmpList.add(lpTenorCodeList); // LP Tenor Code List
			
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			arrListLocal.add(collSourceTemp); // FTP Source
			arrListLocal.add(collTempMethods); // FTP Method
			arrListLocal.add(curvesList);  // FTP Curves
			arrListLocal.add(addOnList);  // FTP Curves
			arrListLocal.add(tmpList); // FTP Premium
			
			
			
			exceptionCode.setOtherInfo(vObject);
//			exceptionCode.setResponse(collTemp);
			exceptionCode.setRequest(arrListLocal);
			return exceptionCode;
		}
	}
	public ExceptionCode reloadCurveId(FTPCurveVb ftpCurveVb){
		ExceptionCode exceptionCode = new ExceptionCode();
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		
		if(!ValidationUtil.isValid(ftpCurveVb.getEffectiveDate())) {
			String businessDate = ftpGroupsDao.businessDay();
			ftpCurveVb.setEffectiveDate(businessDate);
		}
		
		ArrayList colTemp = (ArrayList)ftpGroupsDao.getQueryPopupResultsCurves(ftpCurveVb.getFtpCurveId(),ftpCurveVb.getEffectiveDate(),ftpGroupsVb,ftpCurveVb.isReloadFlag());
		ftpCurveVb.setTotalRows(ftpCurveVb.getTotalRows());
		ftpCurveVb.setCurrentPage(ftpCurveVb.getCurrentPage());
		exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
		exceptionCode.setOtherInfo(ftpCurveVb);
		exceptionCode.setResponse(colTemp);
		return exceptionCode;
	}
	
	public ExceptionCode reloadFtpAddOn(FTPCurveVb ftpCurveVb){
		ExceptionCode exceptionCode = new ExceptionCode();
		FTPGroupsVb ftpGroupsVb = new FTPGroupsVb();
		
		if(!ValidationUtil.isValid(ftpCurveVb.getEffectiveDate())) {
			String businessDate = ftpGroupsDao.businessDay();
			ftpCurveVb.setEffectiveDate(businessDate);
		}
		
		ArrayList colTemp = (ArrayList)ftpGroupsDao.getQueryPopupResultsAddOn(ftpCurveVb.getFtpCurveId(),ftpCurveVb.getEffectiveDate(),ftpGroupsVb,ftpCurveVb.isReloadFlag());
		ftpCurveVb.setTotalRows(ftpCurveVb.getTotalRows());
		ftpCurveVb.setCurrentPage(ftpCurveVb.getCurrentPage());
		exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
		exceptionCode.setOtherInfo(ftpCurveVb);
		exceptionCode.setResponse(colTemp);
		return exceptionCode;
	}
	
	public ExceptionCode reloadFtpPremium(FTPCurveVb ftpCurveVb){
		ExceptionCode exceptionCode = new ExceptionCode();
		
		if(!ValidationUtil.isValid(ftpCurveVb.getEffectiveDate())) {
			String businessDate = ftpGroupsDao.businessDay();
			ftpCurveVb.setEffectiveDate(businessDate);
		}
		

		ArrayList tenorCodeList = (ArrayList) ftpGroupsDao.getTenorCode(ftpCurveVb.getTenorBucketApplicationCode());
		ArrayList lpTenorCodeList = (ArrayList) ftpGroupsDao.getTenorCode(ftpCurveVb.getLpTenorBucketApplicationCode());
		ArrayList colTemp = (ArrayList)ftpGroupsDao.getQueryPopupResultPremium(ftpCurveVb);
		ArrayList tmpList = new ArrayList();
		tmpList.add(colTemp); // Premium
		tmpList.add(tenorCodeList); // Tenor Code List
		tmpList.add(lpTenorCodeList); // LP Tenor Code List
		
		exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
		exceptionCode.setOtherInfo(ftpCurveVb);
		exceptionCode.setResponse(tmpList);
		return exceptionCode;
	}
	
	public ExceptionCode getQueryResultTuning(FTPSourceConfigVb ftpSourceConfigVb){
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			ArrayList colTemp = (ArrayList)ftpSourceConfigDao.getQueryResultTuning(ftpSourceConfigVb);
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			exceptionCode.setOtherInfo(ftpSourceConfigVb);
			exceptionCode.setResponse(colTemp);
			return exceptionCode;
		}catch(Exception ex){
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
	protected void doSetDesctiptionsAfterQuery(FTPGroupsVb vObject){
		/*List<FTPGroupsVb> lResult = getMergeTableDao().getBsGlDesc(vObject);
		if(lResult != null && !lResult.isEmpty()){
			vObject.setBsGlDescription(lResult.get(0).getBsGlDescription());
		}*/
	}
	
	public ExceptionCode getAllQueryPopupResult(FTPGroupsVb queryPopupObj){
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			setVerifReqDeleteType(queryPopupObj);
			doFormateDataForQuery(queryPopupObj);
			List<FTPGroupsVb> arrListResult = ftpGroupsDao.getQueryPopupResults(queryPopupObj);
			if(arrListResult!= null && arrListResult.size() > 0) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				List<FTPGroupsVb> groupList =  getSubOrdinatDetails(arrListResult);
				exceptionCode.setResponse(groupList);
			}else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
			exceptionCode.setOtherInfo(queryPopupObj);
			return exceptionCode;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the getAllQueryPopupResult results.", ex);
			return null;
		}
	}
	
	public ExceptionCode getAllQueryPopupResult1(FTPGroupsVb queryPopupObj){
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			setVerifReqDeleteType(queryPopupObj);
			doFormateDataForQuery(queryPopupObj);
			List<FTPGroupsVb> arrListResult = ftpGroupsDao.getQueryPopupResultsByGroup(queryPopupObj);
			if(arrListResult!= null && arrListResult.size() > 0) {
				for(FTPGroupsVb groupsVb : arrListResult) {
					List<FTPGroupsVb> childList =  ftpGroupsDao.getQueryPopupResultsDetails(groupsVb);
					groupsVb.setChildList(childList);
				}
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setResponse(arrListResult);
			}else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
			exceptionCode.setOtherInfo(queryPopupObj);
			return exceptionCode;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the getAllQueryPopupResult results.", ex);
			return null;
		}
	}
	
	public ExceptionCode getTableColumns(FTPSourceConfigVb queryPopupObj){
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			List<FTPSourceConfigVb> arrListResult = ftpSourceConfigDao.getTableColumns(queryPopupObj);
			if(arrListResult!= null && arrListResult.size() > 0) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setResponse(arrListResult);
			}else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
			exceptionCode.setOtherInfo(queryPopupObj);
			return exceptionCode;
		}catch(Exception ex){
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
	
	public ExceptionCode addModifyFtpCurve(List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPCurveVb> deepCopy = new DeepCopy<FTPCurveVb>();
		FTPCurveVb clonedObject = null;
		try{
			clonedObject = deepCopy.copy(vObject);
			exceptionCode = ftpGroupsDao.addModifyFtpCurve(ftpCurveList, vObject);
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
	public ExceptionCode addModifyFtpAddOn(List<FTPCurveVb> ftpCurveList,FTPCurveVb vObject){
		ExceptionCode exceptionCode  = null;
		DeepCopy<FTPCurveVb> deepCopy = new DeepCopy<FTPCurveVb>();
		FTPCurveVb clonedObject = null;
		try{
			clonedObject = deepCopy.copy(vObject);
			exceptionCode = ftpGroupsDao.addModifyFtpAddOn(ftpCurveList, vObject);
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
	
	public List<FTPGroupsVb> getSubOrdinatDetails(List<FTPGroupsVb> arrListResult){
		ExceptionCode exceptionCode =  new ExceptionCode();
		List<FTPGroupsVb> result = new ArrayList<FTPGroupsVb>();
		try {
			
			FTPGroupsVb vObjectParent = new FTPGroupsVb();
//			FTPGroupsVb vObjectParent = null;
			String previousConnectorId = "";
			for(FTPGroupsVb vObkj : arrListResult) {
//				String keyValue = rs.getString("COUNTRY")+""+rs.getString("LE_BOOK")+""+rs.getString("DATA_SOURCE")+""+rs.getString("FTP_GROUP");
				String keyValue = vObkj.getCountry()+""+vObkj.getLeBook()+""+vObkj.getDataSource()+""+vObkj.getFtpGroup();
				if (!ValidationUtil.isValid(previousConnectorId)) {
					vObjectParent = new FTPGroupsVb(vObkj.getCountry(), vObkj.getLeBook(), vObkj.getDataSource(), vObkj.getFtpGroup());
					previousConnectorId = vObkj.getCountry()+""+vObkj.getLeBook()+""+vObkj.getDataSource()+""+vObkj.getFtpGroup();
				} else if (!previousConnectorId.equalsIgnoreCase(keyValue)) {
					result.add(vObjectParent);
					vObjectParent = new FTPGroupsVb(vObkj.getCountry(), vObkj.getLeBook(), vObkj.getDataSource(), vObkj.getFtpGroup());
					previousConnectorId = vObkj.getCountry()+""+vObkj.getLeBook()+""+vObkj.getDataSource()+""+vObkj.getFtpGroup();
				}
				vObjectParent.getChildList().add(vObkj);
			}
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return result;
	}
	// Get FTP Method Details 
	public ExceptionCode getQueryResultMethods(FTPGroupsVb vObject){
		int intStatus = 1;
		setVerifReqDeleteType(vObject);
		List<FtpMethodsVb> collTemp = getFtpGroupsDao().getQueryResultMethods(vObject);
		if(collTemp.size() == 0){
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
//			doSetDesctiptionsAfterQuery(collTemp);
			doSetDesctiptionsAfterQuery(vObject);
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(collTemp);
			return exceptionCode;
		}
	}
	
	// Get FTP Source Details 
	public ExceptionCode getQueryResultsSource(FTPGroupsVb vObject){
		int intStatus = 1;
		setVerifReqDeleteType(vObject);
		List<FtpMethodsVb> collTemp = (ArrayList)getFtpGroupsDao().getQueryResultsSource(vObject);
		if(collTemp.size() == 0){
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
//				doSetDesctiptionsAfterQuery(collTemp);
			doSetDesctiptionsAfterQuery(vObject);
			ExceptionCode exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(collTemp);
			return exceptionCode;
		}
	}
	
}