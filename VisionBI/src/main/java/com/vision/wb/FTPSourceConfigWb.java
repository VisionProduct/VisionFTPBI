package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.FTPSourceConfigDao;
import com.vision.util.CommonUtils;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.CommonVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.ReviewResultVb;

@Component
public class FTPSourceConfigWb extends AbstractDynaWorkerBean<FTPSourceConfigVb>{
	@Autowired
	private FTPSourceConfigDao ftpSourceConfigDao;
	
	public static Logger logger = LoggerFactory.getLogger(FTPSourceConfigWb.class);
	
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(1302);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1303);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1305);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_SOURCE_CONFIG");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}	
	@Override
	protected List<ReviewResultVb> transformToReviewResults(List<FTPSourceConfigVb> approvedCollection, List<FTPSourceConfigVb> pendingCollection) {
		ResourceBundle rsb = CommonUtils.getResourceManger();
		ArrayList collTemp = getPageLoadValues();
		if(pendingCollection != null)
			getScreenDao().fetchMakerVerifierNames(pendingCollection.get(0));
		if(approvedCollection != null)
			getScreenDao().fetchMakerVerifierNames(approvedCollection.get(0));
		ArrayList<ReviewResultVb> lResult = new ArrayList<ReviewResultVb>();
		ReviewResultVb lCountry = new ReviewResultVb(rsb.getString("country"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCountry(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCountry());
		lResult.add(lCountry);
		ReviewResultVb lLeBook = new ReviewResultVb(rsb.getString("leBook"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getLeBook(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getLeBook());
		lResult.add(lLeBook);
		ReviewResultVb lSourceReference = new ReviewResultVb(rsb.getString("sourceReference"),(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(2),pendingCollection.get(0).getSourceReference()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>) collTemp.get(2),approvedCollection.get(0).getSourceReference()));
		lResult.add(lSourceReference);
		ReviewResultVb lSequence = new ReviewResultVb(rsb.getString("sequence"),(pendingCollection == null || pendingCollection.isEmpty())?"":String.valueOf(pendingCollection.get(0).getSequence()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":String.valueOf(approvedCollection.get(0).getSequence()));
		lResult.add(lSequence);
		ReviewResultVb lTableName = new ReviewResultVb(rsb.getString("tableName"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTableName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTableName());
		lResult.add(lTableName);
		ReviewResultVb lColumnName = new ReviewResultVb(rsb.getString("columnName"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getColName(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getColName());
		lResult.add(lColumnName);
		ReviewResultVb lOperand = new ReviewResultVb(rsb.getString("operand"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getOperand(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getOperand());
		lResult.add(lOperand);
		ReviewResultVb lConditionValue1 = new ReviewResultVb(rsb.getString("conditionValue")+" 1",(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getConditionValue1(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getConditionValue1());
		lResult.add(lConditionValue1);
		ReviewResultVb lConditionValue2 = new ReviewResultVb(rsb.getString("conditionValue")+" 2",(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getConditionValue2(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getConditionValue2());
		lResult.add(lConditionValue2);
		ReviewResultVb lFtpSourceConfigStatus = new ReviewResultVb(rsb.getString("status"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),pendingCollection.get(0).getFtpSourceConfigStatus()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(1),approvedCollection.get(0).getFtpSourceConfigStatus()));
		lResult.add(lFtpSourceConfigStatus);
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
	protected void setAtNtValues(FTPSourceConfigVb vObject) {
		vObject.setSourceReferenceAt(1302);
		vObject.setFtpSourceConfigStatusNt(1);
		vObject.setRecordIndicatorNt(7);
	}

	@Override
	protected void setVerifReqDeleteType(FTPSourceConfigVb vObject) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_SOURCE_CONFIG");
		vObject.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		vObject.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
		
	}

	@Override
	protected AbstractDao<FTPSourceConfigVb> getScreenDao() {
		return ftpSourceConfigDao;
	}
	public FTPSourceConfigDao getFtpSourceConfigDao() {
		return ftpSourceConfigDao;
	}
	public void setFtpSourceConfigDao(FTPSourceConfigDao ftpSourceConfigDao) {
		this.ftpSourceConfigDao = ftpSourceConfigDao;
	}
	@Override
	protected void doSetDesctiptionsAfterQuery(FTPSourceConfigVb vObject){
		/*List<FTPSourceConfigVb> lResult = getMergeTableDao().getBsGlDesc(vObject);
		if(lResult != null && !lResult.isEmpty()){
			vObject.setBsGlDescription(lResult.get(0).getBsGlDescription());
		}*/
	}
}