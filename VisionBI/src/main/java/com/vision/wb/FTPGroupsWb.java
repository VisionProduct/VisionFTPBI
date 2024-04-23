package com.vision.wb;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.dao.AbstractDao;
import com.vision.dao.FTPGroupsDao;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.DeepCopy;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.CommonVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.ReviewResultVb;

@Component
public class FTPGroupsWb extends AbstractDynaWorkerBean<FTPGroupsVb>{
	private FTPGroupsDao ftpGroupsDao;
	
	public static Logger logger = LoggerFactory.getLogger(FTPGroupsWb.class);
	
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);//Status
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1301);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1302);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(10);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1303);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1304);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1310);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1301);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1302);
			arrListLocal.add(collTemp);
//			collTemp = getFtpGroupsDao().getVisionSbu();
			//collTemp = getFtpGroupsDao().getVisionSbu( String.valueOf( CustomContextHolder.getContext().getVisionId()), String.valueOf(System.currentTimeMillis()), "PR088" );
			collTemp = getFtpGroupsDao().callProcToPopulateVisionSBUData();
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(8);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(6);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1303);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1304);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_GROUPS");
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1360);
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
	
	/*public ExceptionCode getSubOrdinatDetails(List<FTPGroupsVb> arrListResult){
		ExceptionCode exceptionCode =  new ExceptionCode();
		DeepCopy<FTPGroupsVb> deepCopy = new DeepCopy<FTPGroupsVb>();
		List<FTPGroupsVb> lResult = new ArrayList<>();
		VisionUsersVb visionUsersVb = SessionContextHolder.getContext();
		try {

			List<FTPGroupsVb> subOrdinatelstCopy = new CopyOnWriteArrayList<FTPGroupsVb>(deepCopy.copyCollection(arrListResult));
			List<FTPGroupsVb> childList = new ArrayList<>();
			
			for(FTPGroupsVb promptVb:subOrdinatelstCopy){
				if(promptVb.getChildId() == promptVb.getParentId() || promptVb.getChildId() == visionUsersVb.getVisionId()){
					subOrdinatelstCopy.remove(promptVb);
				}
			}
											
			FTPGroupsVb parentVb = subOrdinatelstCopy.stream()
										.filter(lstObj -> (visionUsersVb.getVisionId()== lstObj.getParentId()))
										.findAny()
										.orElse(null);
			FTPGroupsVb subOrdinateVb = new FTPGroupsVb();
			if(parentVb == null) {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND );
				exceptionCode.setErrorMsg("No Subordinates are available");
				return exceptionCode;
			} else {
				subOrdinateVb.setParentId(parentVb.getParentId());
				subOrdinateVb.setChildId(parentVb.getParentId());
				subOrdinateVb.setParentMakerName(visionUsersVb.getUserName());
				subOrdinateVb.setChildMakerName(visionUsersVb.getUserName());
				subOrdinateVb.setLayoutLst(getLayoutDetails(vObject, subOrdinateVb.getChildId(),visionUsersVb.getVisionId()));
				addChilds(subOrdinateVb, subOrdinatelstCopy, vObject,visionUsersVb.getVisionId());
			}
			vObject.setChildren(subOrdinateVb);
			exceptionCode.setResponse(vObject);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	private void addChilds(FTPGroupsVb subOrdinateVb, List<FTPGroupsVb> subOrdinatelstCopy,FTPGroupsVb vObject,int currentUserId) {
		for(FTPGroupsVb bnfReviewVb:subOrdinatelstCopy){
			if(subOrdinateVb.getChildId() == bnfReviewVb.getParentId()){
				if(subOrdinateVb.getChildren() == null){
					subOrdinateVb.setChildren(new ArrayList<FTPGroupsVb>(0));
				}
				bnfReviewVb.setLayoutLst(getLayoutDetails(vObject,bnfReviewVb.getChildId(),currentUserId));
				subOrdinateVb.getChildren().add(bnfReviewVb);
				subOrdinatelstCopy.remove(bnfReviewVb);
				addChilds(bnfReviewVb, subOrdinatelstCopy,vObject,currentUserId);
			}
		}
	}*/
	
}