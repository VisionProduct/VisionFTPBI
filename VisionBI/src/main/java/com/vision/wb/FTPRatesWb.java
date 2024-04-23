package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.AbstractDao;
import com.vision.dao.FTPRatesDao;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.CommonVb;
import com.vision.vb.FTPRatesVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.ReviewResultVb;
@Component
public class FTPRatesWb extends AbstractDynaWorkerBean<FTPRatesVb>{
	
	@Autowired
	private FTPRatesDao ftpRatesDao;
	
	public FTPRatesDao getFtpRatesDao() {
		return ftpRatesDao;
	}
	public void setFtpRatesDao(FTPRatesDao ftpRatesDao) {
		this.ftpRatesDao = ftpRatesDao;
	}
	public static Logger logger = LoggerFactory.getLogger(FTPRatesWb.class);
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(1);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(10);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(3);
			arrListLocal.add(collTemp);
			collTemp = getFtpRatesDao().getOucAttributeLevel();
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(45);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_RATES");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected List<ReviewResultVb> transformToReviewResults(List<FTPRatesVb> approvedCollection, List<FTPRatesVb> pendingCollection) {
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
		ReviewResultVb lPoolCode = new ReviewResultVb(rsb.getString("poolCode"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getPoolCode(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getPoolCode());
		lResult.add(lPoolCode);
		ReviewResultVb lpoolCodeDesc = new ReviewResultVb(rsb.getString("description"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getPoolCodeDesc(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getPoolCodeDesc());
		lResult.add(lpoolCodeDesc);
		ReviewResultVb leffectiveDate = new ReviewResultVb(rsb.getString("effectiveDate"),
				(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getEffectiveDate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getEffectiveDate());
		lResult.add(leffectiveDate);
		ReviewResultVb lDataSource = new ReviewResultVb(rsb.getString("dataSource"),(pendingCollection == null || pendingCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(2),pendingCollection.get(0).getDataSource()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getAtDescription((List<AlphaSubTabVb>)collTemp.get(2),approvedCollection.get(0).getDataSource()));
		lResult.add(lDataSource);
		ReviewResultVb lCurrency = new ReviewResultVb(rsb.getString("currency"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCurrency(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCurrency());
		lResult.add(lCurrency);
		ReviewResultVb lDebitPoolRate = new ReviewResultVb(rsb.getString("debitPoolRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getDebitPoolRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getDebitPoolRate());
		lResult.add(lDebitPoolRate);
		ReviewResultVb lCreditPoolRate = new ReviewResultVb(rsb.getString("creditPoolRate"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getCreditPoolRate(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getCreditPoolRate());
		lResult.add(lCreditPoolRate);
		ReviewResultVb lvisionSbu = new ReviewResultVb(rsb.getString("visionSBU"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVisionSbu(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVisionSbu());
		lResult.add(lvisionSbu);
		ReviewResultVb lProduct = new ReviewResultVb(rsb.getString("product"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getProduct(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getProduct());
		lResult.add(lProduct);
		ReviewResultVb ltenorRateStart = new ReviewResultVb(rsb.getString("tenorRateStart"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorRateStart(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorRateStart());
		lResult.add(ltenorRateStart);
		ReviewResultVb ltenorRateEnd = new ReviewResultVb(rsb.getString("tenorRateEnd"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getTenorRateEnd(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getTenorRateEnd());
		lResult.add(ltenorRateEnd);
		ReviewResultVb lintRateStart = new ReviewResultVb(rsb.getString("interestRateStart"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getInterestRateStart(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getInterestRateStart());
		lResult.add(lintRateStart);
		ReviewResultVb lintRateEnd = new ReviewResultVb(rsb.getString("interestRateEnd"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getInterestRateEnd(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getInterestRateEnd());
		lResult.add(lintRateEnd);
		ReviewResultVb lavgStart = new ReviewResultVb(rsb.getString("averageStart"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAvgStart(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAvgEnd());
		lResult.add(lavgStart);
		ReviewResultVb lavgEnd = new ReviewResultVb(rsb.getString("averageEnd"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getAvgEnd(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getAvgEnd());
		lResult.add(lavgEnd);
		ReviewResultVb lPoolRateStatus = new ReviewResultVb(rsb.getString("status"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),pendingCollection.get(0).getPoolRateStatus()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),approvedCollection.get(0).getPoolRateStatus()));
		lResult.add(lPoolRateStatus);
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
	protected AbstractDao<FTPRatesVb> getScreenDao() {
		return ftpRatesDao;
	}
	@Override
	protected void setAtNtValues(FTPRatesVb vObject) {
		vObject.setDataSourceAt(10);
		vObject.setPoolRateStatusNt(1);
		vObject.setRecordIndicatorNt(7);			
	}
	@Override
	protected void setVerifReqDeleteType(FTPRatesVb object) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_RATES");
		object.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		object.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
	}
	@Override
	protected void doSetDesctiptionsAfterQuery(FTPRatesVb vObject){
		List<FTPRatesVb> lResult = getFtpRatesDao().getPoolCodeDescription(vObject);
		if(lResult != null && !lResult.isEmpty()){
			vObject.setPoolCodeDesc(lResult.get(0).getPoolCodeDesc());
		}
	}
	@Override
	protected  ExceptionCode doValidate(FTPRatesVb vObject){
		ExceptionCode exceptionCode = new ExceptionCode(); 
		if(ValidationUtil.isValid(vObject.getPoolCode()) && !(getFtpRatesDao().validatePoolCode(vObject) > 0)){
			String msg ="Country["+vObject.getCountry()+"] LE Book["+vObject.getLeBook()+"] Pool Code["+vObject.getPoolCode()+"] combination does not exists in POOL_CODES table.";			
			exceptionCode.setErrorMsg(msg);
		}
		return exceptionCode;
	}
	@Override
	protected void doFormateData(FTPRatesVb vObject){
		if(ValidationUtil.isValid(vObject.getAvgStart())){
			vObject.setAvgStart(vObject.getAvgStart().replaceAll(",", ""));
		}
		if(ValidationUtil.isValid(vObject.getAvgEnd())){
			vObject.setAvgEnd(vObject.getAvgEnd().replaceAll(",", ""));
		}
	}	
	private String getOucAttributeLevelDescription(List<FTPRatesVb> alphaSubTabs, String alphaSubTab){
		for(FTPRatesVb alphaSubTabVb:alphaSubTabs){
			//if(alphaSubTabVb.getOucAttributeLevel().equalsIgnoreCase(alphaSubTab)) return alphaSubTabVb.getReportCategoryDesc();
		}
		return "Invalid";
	}
}
