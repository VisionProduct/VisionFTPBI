package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.FTPTermRatesDao;
import com.vision.util.CommonUtils;
import com.vision.vb.FTPTermRatesVb;
import com.vision.vb.CommonVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.ReviewResultVb;

@Component
public class FTPTermRatesWb extends AbstractDynaWorkerBean<FTPTermRatesVb>{
	
	@Autowired
	private FTPTermRatesDao ftpTermRatesDao;
	public static Logger logger = LoggerFactory.getLogger(FTPTermRatesWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_TERM_RATES");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected List<ReviewResultVb> transformToReviewResults(List<FTPTermRatesVb> approvedCollection, List<FTPTermRatesVb> pendingCollection) {
		ArrayList collTemp = getPageLoadValues();
		ResourceBundle rsb = CommonUtils.getResourceManger();
		if(pendingCollection != null)
			getScreenDao().fetchMakerVerifierNames(pendingCollection.get(0));
		if(approvedCollection != null)
			getScreenDao().fetchMakerVerifierNames(approvedCollection.get(0));
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lGradeCode = new ReviewResultVb(rsb.getString("ftpRateID"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpRateId(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpRateId());
		lResult.add(lGradeCode);
		ReviewResultVb lGradeDescription = new ReviewResultVb(rsb.getString("effectiveDate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEffectiveDate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEffectiveDate());
		lResult.add(lGradeDescription);
		ReviewResultVb lGradeSequence = new ReviewResultVb(rsb.getString("ftpCurve"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getFtpCurve(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getFtpCurve());
		lResult.add(lGradeSequence);	
		
		
		ReviewResultVb lGradeSequence1 = new ReviewResultVb(rsb.getString("addOnDepositRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAddOnDepositRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAddOnDepositRate());
		lResult.add(lGradeSequence1);
		
		
		ReviewResultVb lGradeSequence2 = new ReviewResultVb(rsb.getString("subsidy"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getSubsidy(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getSubsidy());
		lResult.add(lGradeSequence2);
		
		
		ReviewResultVb lGradeSequence3 = new ReviewResultVb(rsb.getString("addOnLendingRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAddOnLendingRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAddOnLendingRate());
		lResult.add(lGradeSequence3);
		
		
		ReviewResultVb lGradeSequence4 = new ReviewResultVb(rsb.getString("requiredReserveRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getRequiredReserveRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRequiredReserveRate());
		lResult.add(lGradeSequence4);
		
		
		ReviewResultVb lGradeSequence5 = new ReviewResultVb(rsb.getString("glAllocationRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getGlAllocationRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getGlAllocationRate());
		lResult.add(lGradeSequence5);
		
		ReviewResultVb lGradeSequence6 = new ReviewResultVb(rsb.getString("insuranceRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getInsuranceRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getInsuranceRate());
		lResult.add(lGradeSequence6);
		
		ReviewResultVb lGradeSequence7 = new ReviewResultVb(rsb.getString("addOnAttRate1"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAddOnAttRate1(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAddOnAttRate1());
		lResult.add(lGradeSequence7);
		
		
		ReviewResultVb lGradeSequenc8 = new ReviewResultVb(rsb.getString("addOnAttRate2"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAddOnAttRate2(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAddOnAttRate2());
		lResult.add(lGradeSequenc8);
		
		ReviewResultVb lGradeSequence9 = new ReviewResultVb(rsb.getString("addOnAttRate3"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAddOnAttRate3(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAddOnAttRate3());
		lResult.add(lGradeSequence9);
		
		ReviewResultVb lGradeSequence10 = new ReviewResultVb(rsb.getString("addOnAttRate4"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAddOnAttRate4(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAddOnAttRate4());
		lResult.add(lGradeSequence10);
		
		ReviewResultVb lGradeStatus = new ReviewResultVb(rsb.getString("status"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),pendingCollection.get(0).getTermRateStatus()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),approvedCollection.get(0).getTermRateStatus()));
		lResult.add(lGradeStatus);
		
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
	protected void setAtNtValues(FTPTermRatesVb vObject) {
		vObject.setTermRateStatusNt(1);
		vObject.setRecordIndicatorNt(7);
	}

	@Override
	protected void setVerifReqDeleteType(FTPTermRatesVb vObject) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_TERM_RATES");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
	}
	@Override
	protected AbstractDao<FTPTermRatesVb> getScreenDao() {
		return ftpTermRatesDao;
	}
	public FTPTermRatesDao getFtpTermRatesDao() {
		return ftpTermRatesDao;
	}
	public void setFtpTermRatesDao(FTPTermRatesDao ftpTermRatesDao) {
		this.ftpTermRatesDao = ftpTermRatesDao;
	}
}
