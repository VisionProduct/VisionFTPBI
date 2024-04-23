package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.FtpControlsDao;
import com.vision.util.CommonUtils;
import com.vision.vb.CommonVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.FtpControlsVb;
import com.vision.vb.ReviewResultVb;
import com.vision.vb.AlphaSubTabVb;

@Component
public class FtpControlsWb extends AbstractWorkerBean<FtpControlsVb>{
	
	@Autowired
	private FtpControlsDao ftpControlsDao;
	public static Logger logger = LoggerFactory.getLogger(FtpControlsWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1302);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_CONTROLS");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected List<ReviewResultVb> transformToReviewResults(List<FtpControlsVb> approvedCollection, List<FtpControlsVb> pendingCollection) {
		ArrayList collTemp = getPageLoadValues();
		ResourceBundle rsb = CommonUtils.getResourceManger();
		if(pendingCollection != null)
			getScreenDao().fetchMakerVerifierNames(pendingCollection.get(0));
		if(approvedCollection != null)
			getScreenDao().fetchMakerVerifierNames(approvedCollection.get(0));
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lCountry = new ReviewResultVb("Country",
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry());
		lResult.add(lCountry);
		ReviewResultVb lLeBook = new ReviewResultVb("LE Book",
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook());
		lResult.add(lLeBook);
		ReviewResultVb lMethodRefrenceId = new ReviewResultVb(rsb.getString("methodReference"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMethodReference(),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMethodReference());
		lResult.add(lMethodRefrenceId);
		ReviewResultVb lFtpDescription = new ReviewResultVb(rsb.getString("ftpDescription"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpDescription(),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpDescription());
		lResult.add(lFtpDescription); 
		ReviewResultVb lFtpReference = new ReviewResultVb(rsb.getString("ftpReference"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpReference(),
		        (approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpReference());
		lResult.add(lFtpReference);
		 ReviewResultVb lStatus = new ReviewResultVb(rsb.getString("status"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),pendingCollection.get(0).getStatus()),
			        (approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),approvedCollection.get(0).getStatus()));
			    lResult.add(lStatus);
	    ReviewResultVb lSourceReference = new ReviewResultVb(rsb.getString("sourceReference"),
	    		(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(2),pendingCollection.get(0).getSourceReference()),
	            (approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(2),approvedCollection.get(0).getSourceReference()));
	    lResult.add(lSourceReference);	
	ReviewResultVb lRecordIndicator = new ReviewResultVb("Record Indicator",(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1), pendingCollection.get(0).getRecordIndicator()),
			(approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1), approvedCollection.get(0).getRecordIndicator()));
	lResult.add(lRecordIndicator);
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
	protected void setVerifReqDeleteType(FtpControlsVb vObject){
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_CONTROLS");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
	}
	@Override
	protected void setAtNtValues(FtpControlsVb vObject){
		vObject.setRecordIndicatorNt(7);
		vObject.setStatusNt(1);
		vObject.setSourceReferenceAt(1302);
	}
	
	public FtpControlsDao getFtpControlsDao() {
		return ftpControlsDao;
	}
	public void setFtpControlsDao(FtpControlsDao ftpControlsDao) {
		this.ftpControlsDao = ftpControlsDao;
	}
	@Override
	protected AbstractDao<FtpControlsVb> getScreenDao() {
		return ftpControlsDao;
	}
}