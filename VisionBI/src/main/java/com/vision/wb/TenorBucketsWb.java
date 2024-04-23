package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.TenorBucketsDao;
import com.vision.util.CommonUtils;
import com.vision.vb.CommonVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.ReviewResultVb;
import com.vision.vb.TenorBucketsVb;
@Component
public class TenorBucketsWb extends AbstractDynaWorkerBean<TenorBucketsVb> {
	
	@Autowired
	private TenorBucketsDao tenorBucketsDao;
	public static Logger logger = LoggerFactory.getLogger(TenorBucketsWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(8);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("TENOR_BUCKETS");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected  List<ReviewResultVb> transformToReviewResults(List<TenorBucketsVb> approvedCollection, List<TenorBucketsVb> pendingCollection) {
		ResourceBundle rsb=CommonUtils.getResourceManger();
		ArrayList collTemp = getPageLoadValues();
		if(pendingCollection != null)
			getScreenDao().fetchMakerVerifierNames(pendingCollection.get(0));
		if(approvedCollection != null)
			getScreenDao().fetchMakerVerifierNames(approvedCollection.get(0));
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lTenorApplCode = new ReviewResultVb(rsb.getString("tenorApplicationCode"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorBucketApplicationCode() == -1?"":String.valueOf(pendingCollection.get(0).getTenorBucketApplicationCode()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorBucketApplicationCode() == -1?"":String.valueOf(approvedCollection.get(0).getTenorBucketApplicationCode()), !(approvedCollection.get(0).getTenorBucketApplicationCode() == pendingCollection.get(0).getTenorBucketApplicationCode()));
		lResult.add(lTenorApplCode);
		ReviewResultVb lTenorCode = new ReviewResultVb(rsb.getString("tenorCode"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorBucketCode(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorBucketCode(), !(approvedCollection.get(0).getTenorBucketCode().equalsIgnoreCase(pendingCollection.get(0).getTenorBucketCode())));
		lResult.add(lTenorCode);
		ReviewResultVb lTenorDescription = new ReviewResultVb(rsb.getString("tenorDescription"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorBucketDescription(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorBucketDescription(), !(approvedCollection.get(0).getTenorBucketDescription().equalsIgnoreCase(pendingCollection.get(0).getTenorBucketDescription())));
		lResult.add(lTenorDescription);
		ReviewResultVb lDayStart = new ReviewResultVb(rsb.getString("dayStart"),(pendingCollection == null || pendingCollection.isEmpty())?"":String.valueOf(pendingCollection.get(0).getDayStart()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":String.valueOf(approvedCollection.get(0).getDayStart()), !(approvedCollection.get(0).getDayStart().equalsIgnoreCase(pendingCollection.get(0).getDayStart())));
		lResult.add(lDayStart);
		ReviewResultVb lDayEnd = new ReviewResultVb(rsb.getString("dayEnd"),(pendingCollection == null || pendingCollection.isEmpty())?"":String.valueOf(pendingCollection.get(0).getDayEnd()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":String.valueOf(approvedCollection.get(0).getDayEnd()), !(approvedCollection.get(0).getDayEnd().equalsIgnoreCase(pendingCollection.get(0).getDayEnd())));
		lResult.add(lDayEnd);
		ReviewResultVb lStatus = new ReviewResultVb(rsb.getString("tenorStatus"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),pendingCollection.get(0).getTenorBucketStatus()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),approvedCollection.get(0).getTenorBucketStatus()), !(approvedCollection.get(0).getTenorBucketStatus() == pendingCollection.get(0).getTenorBucketStatus()));
		lResult.add(lStatus);
		ReviewResultVb lRecordIndicator = new ReviewResultVb(rsb.getString("recordIndicator"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1), pendingCollection.get(0).getRecordIndicator()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getRecordIndicator() == -1?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1), approvedCollection.get(0).getRecordIndicator()), !(approvedCollection.get(0).getRecordIndicator() == pendingCollection.get(0).getRecordIndicator()));
		lResult.add(lRecordIndicator);
		ReviewResultVb lMaker = new ReviewResultVb(rsb.getString("maker"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getMaker() == 0?"":pendingCollection.get(0).getMakerName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getMaker() == 0?"":approvedCollection.get(0).getMakerName(), !(approvedCollection.get(0).getMaker() == pendingCollection.get(0).getMaker()));
		lResult.add(lMaker);
		ReviewResultVb lVerifier = new ReviewResultVb(rsb.getString("verifier"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVerifier() == 0?"":pendingCollection.get(0).getVerifierName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVerifier() == 0?"":approvedCollection.get(0).getVerifierName(), !(approvedCollection.get(0).getVerifier() == pendingCollection.get(0).getVerifier()));
		lResult.add(lVerifier);
		ReviewResultVb lDateLastModified = new ReviewResultVb(rsb.getString("dateLastModified"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateLastModified(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateLastModified(), !(approvedCollection.get(0).getDateLastModified().equals(pendingCollection.get(0).getDateLastModified())));
		lResult.add(lDateLastModified);
		ReviewResultVb lDateCreation = new ReviewResultVb(rsb.getString("dateCreation"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDateCreation(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDateCreation(), !(approvedCollection.get(0).getDateCreation().equals(pendingCollection.get(0).getDateCreation())));
		lResult.add(lDateCreation);
		return lResult;
	}
	@Override
	protected void setAtNtValues(TenorBucketsVb vObject) {
		vObject.setTenorBucketApplicationCodeNt(8);
		vObject.setTenorBucketStatusNt(1);
		vObject.setRecordIndicatorNt(7);
	}
	@Override
	protected void setVerifReqDeleteType(TenorBucketsVb vObject) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("TENOR_BUCKETS");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
	}
	@Override
	protected AbstractDao<TenorBucketsVb> getScreenDao() {
		return tenorBucketsDao;
	}
	public TenorBucketsDao getTenorBucketsDao() {
		return tenorBucketsDao;
	}
	public void setTenorBucketsDao(TenorBucketsDao tenorBucketsDao) {
		this.tenorBucketsDao = tenorBucketsDao;
	}
	
}
