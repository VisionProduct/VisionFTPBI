package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.AlphaSubTabDao;
import com.vision.dao.NumSubTabDao;
import com.vision.dao.RaProfilePrivilegesDao;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.RaProfileVb;
import com.vision.vb.ReviewResultVb;

@Component
public class RaProfilePrivilegesWb extends AbstractWorkerBean<RaProfileVb>{
	@Autowired
	private RaProfilePrivilegesDao raProfilePrivilegesDao;
	@Autowired
	private NumSubTabDao numSubTabDao;
	@Autowired
	private AlphaSubTabDao alphaSubTabDao;
	
	public static Logger logger = LoggerFactory.getLogger(RaProfilePrivilegesWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp =numSubTabDao.findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = numSubTabDao.findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = alphaSubTabDao.findActiveAlphaSubTabsByAlphaTab(1);
			arrListLocal.add(collTemp);
			collTemp =alphaSubTabDao.findActiveAlphaSubTabsByAlphaTab(2);
			arrListLocal.add(collTemp);
			ArrayList<NumSubTabVb> topLevelMenuGrouplst = (ArrayList<NumSubTabVb>)raProfilePrivilegesDao.getTopLevelMenu();
			ArrayList menuGroupLst = new ArrayList();
			for(NumSubTabVb menuGroupVb : topLevelMenuGrouplst) {
				ArrayList screenList = (ArrayList<AlphaSubTabVb>)raProfilePrivilegesDao.findScreenName(menuGroupVb.getNumSubTab());
				menuGroupVb.setChildren(screenList);
				menuGroupLst.add(menuGroupVb);
			}
			arrListLocal.add(menuGroupLst);
			
			ArrayList<AlphaSubTabVb> addedUserGrouplst = (ArrayList<AlphaSubTabVb>)raProfilePrivilegesDao.findUserGroupPP();
			ArrayList userProfilelst = new ArrayList();
			for(AlphaSubTabVb alphaSubTabVb : addedUserGrouplst) {
				ArrayList userProfiles = (ArrayList<AlphaSubTabVb>)raProfilePrivilegesDao.findUserProfilePP(alphaSubTabVb.getAlphaSubTab());
				alphaSubTabVb.setChildren(userProfiles);
				userProfilelst.add(alphaSubTabVb);
			}
			arrListLocal.add(userProfilelst);
			ArrayList dashboardList = (ArrayList<AlphaSubTabVb>)raProfilePrivilegesDao.getAllDashboardList("D");
			arrListLocal.add(dashboardList);
			ArrayList reportList = (ArrayList<AlphaSubTabVb>)raProfilePrivilegesDao.getAllDashboardList("R");
			arrListLocal.add(reportList);
			collTemp = alphaSubTabDao.findActiveAlphaSubTabsByAlphaTab(8000);
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	protected List<ReviewResultVb> transformToReviewResults(List<RaProfileVb> approvedCollection, List<RaProfileVb> pendingCollection) {	
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList collTemp = getPageLoadValues();
		if(pendingCollection != null)
			getScreenDao().fetchMakerVerifierNames(pendingCollection.get(0));
		if(approvedCollection != null)
			getScreenDao().fetchMakerVerifierNames(approvedCollection.get(0));
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lUserGroup = new ReviewResultVb(rsb.getString("userGroup"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(3),pendingCollection.get(0).getUserGroup()),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(3),approvedCollection.get(0).getUserGroup()));
		lResult.add(lUserGroup);
		ReviewResultVb lUserProfile = new ReviewResultVb(rsb.getString("userProfile"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(4),pendingCollection.get(0).getUserProfile()),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(4),approvedCollection.get(0).getUserProfile()));
		lResult.add(lUserProfile);
		ReviewResultVb lPAdd = new ReviewResultVb("P "+rsb.getString("add"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":"Y".equalsIgnoreCase(pendingCollection.get(0).getProfileAdd())?"Yes":"No",
						("Y".equalsIgnoreCase(approvedCollection.get(0).getProfileAdd())?"Yes":"No"));
		lResult.add(lPAdd);
		ReviewResultVb lPModify = new ReviewResultVb("P "+rsb.getString("modify"),"Y".equalsIgnoreCase(pendingCollection.get(0).getProfileModify())?"Yes":"No",
				("Y".equalsIgnoreCase(approvedCollection.get(0).getProfileModify())?"Yes":"No"));
		lResult.add(lPModify);		
		ReviewResultVb lPDelete = new ReviewResultVb("P "+rsb.getString("delete"),"Y".equalsIgnoreCase(pendingCollection.get(0).getProfileDelete())?"Yes":"No",
				("Y".equalsIgnoreCase(approvedCollection.get(0).getProfileDelete())?"Yes":"No"));
		lResult.add(lPDelete);		
		ReviewResultVb lPInQuery = new ReviewResultVb("P "+rsb.getString("inquiry"),"Y".equalsIgnoreCase(pendingCollection.get(0).getProfileInquiry())?"Yes":"No",
				("Y".equalsIgnoreCase(approvedCollection.get(0).getProfileInquiry())?"Yes":"No"));
		lResult.add(lPInQuery);
		ReviewResultVb lPVerifiction = new ReviewResultVb("P "+rsb.getString("verification"),"Y".equalsIgnoreCase(pendingCollection.get(0).getProfileVerification())?"Yes":"No",
				("Y".equalsIgnoreCase(approvedCollection.get(0).getProfileVerification())?"Yes":"No"));
		lResult.add(lPVerifiction);			
		ReviewResultVb lPUpload = new ReviewResultVb(rsb.getString("excelUpload"),"Y".equalsIgnoreCase(pendingCollection.get(0).getProfileUpload())?"Yes":"No",
				("Y".equalsIgnoreCase(approvedCollection.get(0).getProfileUpload())?"Yes":"No"));
		lResult.add(lPUpload);
		ReviewResultVb lPrrofileStatus = new ReviewResultVb(rsb.getString("profileStatus"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),pendingCollection.get(0).getProfileStatus()),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),approvedCollection.get(0).getProfileStatus()));
		lResult.add(lPrrofileStatus);  
		ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),pendingCollection.get(0).getRecordIndicator()),
		            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),approvedCollection.get(0).getRecordIndicator()));
		lResult.add(lRecordIndicator);
		ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMaker() == 0?"":pendingCollection.get(0).getMakerName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMaker() == 0?"":approvedCollection.get(0).getMakerName());
		lResult.add(lMaker);
		ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifier() == 0?"":pendingCollection.get(0).getVerifierName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifier() == 0?"":approvedCollection.get(0).getVerifierName());
		lResult.add(lVerifier);
		ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
		            (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified());
		lResult.add(lDateLastModified);
		ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
		            (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation());
		lResult.add(lDateCreation);
		return lResult;
	}
	@Override
	protected void setAtNtValues(RaProfileVb vObject) {
		vObject.setMenuGroupNt(176);
		vObject.setProfileStatusNt(1);
		vObject.setRecordIndicatorNt(7);
		vObject.setUserGroupAt(1);
		vObject.setUserProfileAt(2);
	}

	@Override
	protected void setVerifReqDeleteType(RaProfileVb vObject) {
		vObject.setStaticDelete(false);
		vObject.setVerificationRequired(false);
		
	}

	@Override
	protected AbstractDao<RaProfileVb> getScreenDao() {
		return raProfilePrivilegesDao;
	}
	public ExceptionCode userHomeDashboardUpdate(RaProfileVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			int retVal = raProfilePrivilegesDao.doDeleteProfileDashboard(vObject);
			retVal = raProfilePrivilegesDao.doInsertionProfileDashboard(vObject);
			if(retVal == Constants.SUCCESSFUL_OPERATION) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setErrorMsg("Home Dashboard Applied for User Group/Profile");
			}else {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Error on applying Home Dashboard");
			}
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(""+e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode getAllQueryPopupResult(RaProfileVb queryPopupObj){
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			setVerifReqDeleteType(queryPopupObj);
			doFormateDataForQuery(queryPopupObj);
			List<RaProfileVb> arrListResult = raProfilePrivilegesDao.getQueryPopupResults(queryPopupObj);
			List<RaProfileVb> resultslst = new ArrayList<RaProfileVb>();
			if(arrListResult!= null && arrListResult.size() > 0) {
				arrListResult.forEach(resultVb -> {
					if("9998".equalsIgnoreCase(resultVb.getMenuGroup()) || "9999".equals(resultVb.getMenuGroup())){
						List<AlphaSubTabVb> reportlst = raProfilePrivilegesDao.getSelectedDashboardList(resultVb);
						resultVb.setDashboardReportlst(reportlst);
					}
					resultslst.add(resultVb);
				});
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
}
