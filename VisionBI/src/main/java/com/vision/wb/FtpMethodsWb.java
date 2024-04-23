package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.FtpMethodsDao;
import com.vision.util.CommonUtils;
import com.vision.vb.CommonVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.ReviewResultVb;
import com.vision.vb.AlphaSubTabVb;
@Component
public class FtpMethodsWb extends AbstractDynaWorkerBean<FtpMethodsVb>{
	
	@Autowired
	private FtpMethodsDao ftpMethodsDao;
	public static Logger logger = LoggerFactory.getLogger(FtpMethodsWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1303);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1304);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1302);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1301);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1310);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(6);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1304);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_METHODS");
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
	protected List<ReviewResultVb> transformToReviewResults(List<FtpMethodsVb> approvedCollection, List<FtpMethodsVb> pendingCollection) {
		ArrayList collTemp = getPageLoadValues();
		ResourceBundle rsb = CommonUtils.getResourceManger();
		if(pendingCollection != null)
			getScreenDao().fetchMakerVerifierNames(pendingCollection.get(0));
		if(approvedCollection != null)
			getScreenDao().fetchMakerVerifierNames(approvedCollection.get(0));
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lMethodRefrenceId = new ReviewResultVb(rsb.getString("methodReference"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMethodReference(),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMethodReference());
		lResult.add(lMethodRefrenceId);
		ReviewResultVb lDescription = new ReviewResultVb(rsb.getString("methodDescription"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMethodDescription(),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMethodDescription());
		lResult.add(lDescription); 
		ReviewResultVb lMethodType = new ReviewResultVb(rsb.getString("methodType"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(2),pendingCollection.get(0).getMethodType()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(2),approvedCollection.get(0).getMethodType()));
		lResult.add(lMethodType);
		ReviewResultVb lMethodSubType = new ReviewResultVb(rsb.getString("methodSubSubType"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(10),pendingCollection.get(0).getMethodSubType()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(10),approvedCollection.get(0).getMethodSubType()));
		lResult.add(lMethodSubType);
	ReviewResultVb lFtpTenorType = new ReviewResultVb(rsb.getString("ftpTenorType"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(4),pendingCollection.get(0).getFtpTenorType()),
	        (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(4),approvedCollection.get(0).getFtpTenorType()));
	lResult.add(lFtpTenorType);
	    ReviewResultVb lMethodBalType = new ReviewResultVb(rsb.getString("methodBalType"),
	    	(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(5),pendingCollection.get(0).getMethodBalType()),
            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(5),approvedCollection.get(0).getMethodBalType()));
    lResult.add(lMethodBalType);
    ReviewResultVb lFtpApplyRate = new ReviewResultVb(rsb.getString("ftpApplyRate"),
	    	(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(9),pendingCollection.get(0).getFtpApplyRate()),
            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(9),approvedCollection.get(0).getFtpApplyRate()));
    lResult.add(lFtpApplyRate);
    ReviewResultVb lAddOnApplyRate = new ReviewResultVb(rsb.getString("addonApplyRate"),
	    	(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(10),pendingCollection.get(0).getAddonApplyRate()),
            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(10),approvedCollection.get(0).getAddonApplyRate()));
    lResult.add(lAddOnApplyRate);
    ReviewResultVb lLpApplyRate = new ReviewResultVb(rsb.getString("lpApplyRate"),
	    	(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(11),pendingCollection.get(0).getLpApplyRate()),
            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(11),approvedCollection.get(0).getLpApplyRate()));
    lResult.add(lLpApplyRate);
    ReviewResultVb lIntBais = new ReviewResultVb(rsb.getString("interestBasis"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(8),pendingCollection.get(0).getInterestBasis()),
            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(8),approvedCollection.get(0).getInterestBasis()));
    lResult.add(lIntBais);
    ReviewResultVb lStatus = new ReviewResultVb(rsb.getString("status"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),pendingCollection.get(0).getStatus()),
        (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),approvedCollection.get(0).getStatus()));
    lResult.add(lStatus);
    ReviewResultVb lRepricingFlag = new ReviewResultVb(rsb.getString("repricingFlag"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(3),pendingCollection.get(0).getMethodType()),
			(approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(3),approvedCollection.get(0).getMethodType()));
	lResult.add(lRepricingFlag);
    ReviewResultVb lLPTenorType = new ReviewResultVb(rsb.getString("lpTenorType"),
    		(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(4),pendingCollection.get(0).getLpTenorType()),
            (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(4),approvedCollection.get(0).getLpTenorType()));
    lResult.add(lLPTenorType);	
    ReviewResultVb lFtpCurveId = new ReviewResultVb(rsb.getString("ftpCurveId"),
			(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(7),pendingCollection.get(0).getFtpCurveId()),
			(approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(7),approvedCollection.get(0).getFtpCurveId()));
	lResult.add(lFtpCurveId);
		ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMaker() == 0?"":pendingCollection.get(0).getMakerName(),
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
	protected void setVerifReqDeleteType(FtpMethodsVb vObject){
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_METHODS");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
	}
	@Override
	protected void setAtNtValues(FtpMethodsVb vObject){
		vObject.setRecordIndicatorNt(7);
		vObject.setStatusNt(1);
		vObject.setMethodTypeAt(1303);
		vObject.setRepricingFlagAt(1304);
		vObject.setFtpTenorTypeNt(1302);
		vObject.setMethodBalTypeNt(1301);
		vObject.setLpTenorTypeNt(1302);
		vObject.setFtpCurveIdAt(1310);
		vObject.setInterestBasisNt(6);
		vObject.setFtpApplyRateNt(1304);
	}
	
	public FtpMethodsDao getFtpMethodsDao() {
		return ftpMethodsDao;
	}
	public void setFtpMethodsDao(FtpMethodsDao ftpMethodsDao) {
		this.ftpMethodsDao = ftpMethodsDao;
	}
	@Override
	protected AbstractDao<FtpMethodsVb> getScreenDao() {
		return ftpMethodsDao;
	}
}