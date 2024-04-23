package com.vision.wb;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vision.dao.AbstractDao;
import com.vision.dao.FTPGroupsDao;
import com.vision.vb.CommonVb;
import com.vision.vb.FTPCurveVb;

public class FTPAddOnDetailsWb extends AbstractDynaWorkerBean<FTPCurveVb>{
	private FTPGroupsDao ftpGroupsDao;
	
	public static Logger logger = LoggerFactory.getLogger(FTPAddOnDetailsWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1310);//Status
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);//Status
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_ADDON");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected void setAtNtValues(FTPCurveVb vObject) {
		vObject.setFtpCurveIdAt(1310);
		vObject.setFtpCurveStatusNt(1);
		vObject.setRecordIndicatorNt(7);
	}
	@Override
	protected void setVerifReqDeleteType(FTPCurveVb vObject) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_ADDON");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
		
	}
/*	public ExceptionCode queryResultSource(FTPGroupsVb vObject){
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList collTemp = new ArrayList();
		ArrayList collTempMethods = new ArrayList();
		collTemp = (ArrayList)getFtpGroupsDao().getQueryResultsSource(vObject);
		collTempMethods = (ArrayList)getFtpGroupsDao().getQueryResultMethods(vObject);
		exceptionCode.setActionType("Query");
		if(collTemp.size() == 0 && collTempMethods.size() == 0){
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 16, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}else{
			doSetDesctiptionsAfterQuery(collTemp);
			doSetDesctiptionsAfterQuery(vObject);
			exceptionCode = CommonUtils.getResultObject(getScreenDao().getServiceDesc(), 1, "Query", "");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(collTemp);
			exceptionCode.setRequest(collTempMethods);
			return exceptionCode;
		}
	}*/
	
/*	protected AbstractDao<FTPCurveVb> getScreenDao() {
		return ftpGroupsDao;
	}*/
	public FTPGroupsDao getFtpGroupsDao() {
		return ftpGroupsDao;
	}
	public void setFtpGroupsDao(FTPGroupsDao ftpGroupsDao) {
		this.ftpGroupsDao = ftpGroupsDao;
	}
/*	@Override
	protected void doSetDesctiptionsAfterQuery(FTPGroupsVb vObject){
		List<FTPGroupsVb> lResult = getMergeTableDao().getBsGlDesc(vObject);
		if(lResult != null && !lResult.isEmpty()){
			vObject.setBsGlDescription(lResult.get(0).getBsGlDescription());
		}*/
	@Override
	protected AbstractDao<FTPCurveVb> getScreenDao() {
		// TODO Auto-generated method stub
		return null;
	}
	}
