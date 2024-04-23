package com.vision.wb;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vision.dao.AbstractDao;
import com.vision.dao.FTPCalculationsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.CommonVb;
import com.vision.vb.FTPCalculationsVb;
import com.vision.vb.FTPRefCodeVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.ReviewResultVb;

@Component
public class FTPCalculationsWb extends AbstractDynaWorkerBean<FTPCalculationsVb>{
	@Autowired
	private FTPCalculationsDao ftpCalculationsDao;
	
	public FTPCalculationsDao getFtpCalculationsDao() {
		return ftpCalculationsDao;
	}
	public void setFtpCalculationsDao(FTPCalculationsDao ftpCalculationsDao) {
		this.ftpCalculationsDao = ftpCalculationsDao;
	}
	public static Logger logger = LoggerFactory.getLogger(FTPCalculationsWb.class);
	
	
	public ArrayList getPageLoadValues(){
		List collTemp = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		try{
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(4505);
			arrListLocal.add(collTemp);
			collTemp = getNumSubTabDao().findActiveNumSubTabsByNumTab(7);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(10);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(3);
			arrListLocal.add(collTemp);
			collTemp = getFtpCalculationsDao().getOucAttributeLevel();
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(45);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(4508);
			arrListLocal.add(collTemp);
			collTemp = getAlphaSubTabDao().findActiveAlphaSubTabsByAlphaTab(4509);
			arrListLocal.add(collTemp);
			collTemp = getCommonDao().findVerificationRequiredAndStaticDelete("FTP_ENGINE");
			arrListLocal.add(collTemp);
			return arrListLocal;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception in getting the Page load values.", ex);
			return null;
		}
	}
	@Override
	protected List<ReviewResultVb> transformToReviewResults(List<FTPCalculationsVb> approvedCollection, List<FTPCalculationsVb> pendingCollection) {
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
		ReviewResultVb lvisionSbu = new ReviewResultVb(rsb.getString("visionSBU"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getVisionSbu(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getVisionSbu());
		lResult.add(lvisionSbu);
		ReviewResultVb lProduct = new ReviewResultVb(rsb.getString("product"),(pendingCollection == null || pendingCollection.isEmpty())?"":pendingCollection.get(0).getProduct(),
				(approvedCollection == null || approvedCollection.isEmpty())?"":approvedCollection.get(0).getProduct());
		lResult.add(lProduct);
		ReviewResultVb lPoolRateStatus = new ReviewResultVb(rsb.getString("status"),(pendingCollection == null || pendingCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),pendingCollection.get(0).getFtpEngineStatus()),
				(approvedCollection == null || approvedCollection.isEmpty())?"":getNtDescription((List<NumSubTabVb>) collTemp.get(0),approvedCollection.get(0).getFtpEngineStatus()));
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
	protected AbstractDao<FTPCalculationsVb> getScreenDao() {
		return ftpCalculationsDao;
	}
	@Override
	protected void setAtNtValues(FTPCalculationsVb vObject) {
		vObject.setDataSourceAt(10);
		vObject.setFtpEngineStatusNt(1);
		vObject.setRecordIndicatorNt(7);			
	}
	@Override
	protected void setVerifReqDeleteType(FTPCalculationsVb object) {
		ArrayList<CommonVb> lCommVbList =(ArrayList<CommonVb>) getCommonDao().findVerificationRequiredAndStaticDelete("FTP_ENGINE");
		object.setStaticDelete(lCommVbList.get(0).isStaticDelete());
		object.setVerificationRequired(lCommVbList.get(0).isVerificationRequired());
	}
	@Override
	protected void doSetDesctiptionsAfterQuery(FTPCalculationsVb vObject){
		List<FTPCalculationsVb> lResult = getFtpCalculationsDao().getPoolCodeDescription(vObject);
		if(lResult != null && !lResult.isEmpty()){
			vObject.setPoolCodeDesc(lResult.get(0).getPoolCodeDesc());
		}
	}
	@Override
	protected  ExceptionCode doValidate(FTPCalculationsVb vObject){
		ExceptionCode exceptionCode = new ExceptionCode(); 
		if(ValidationUtil.isValid(vObject.getPoolCode()) && !(getFtpCalculationsDao().validatePoolCode(vObject) > 0)){
			String msg ="Country["+vObject.getCountry()+"] LE Book["+vObject.getLeBook()+"] Pool Code["+vObject.getPoolCode()+"] combination does not exists in POOL_CODES table.";			
			exceptionCode.setErrorMsg(msg);
		}
		return exceptionCode;
	}
	@Override
	protected void doFormateData(FTPCalculationsVb vObject){
		
	}	
	private String getOucAttributeLevelDescription(List<FTPCalculationsVb> alphaSubTabs, String alphaSubTab){
		for(FTPCalculationsVb alphaSubTabVb:alphaSubTabs){
			//if(alphaSubTabVb.getOucAttributeLevel().equalsIgnoreCase(alphaSubTab)) return alphaSubTabVb.getReportCategoryDesc();
		}
		return "Invalid";
	}
	public ExceptionCode getFtpRefCodes(FTPCalculationsVb vObject,ExceptionCode exceptionCode){
		try{
			ArrayList<FTPRefCodeVb> ftpRefCodelst = (ArrayList<FTPRefCodeVb>)getFtpCalculationsDao().getFtpRefCodeResults(vObject);
			exceptionCode.setResponse(ftpRefCodelst);
			ArrayList defaultValueslst = new ArrayList();
			String countryLeBook = vObject.getCountry()+"-"+vObject.getLeBook();
			Boolean datePresent = true;
			if(!ValidationUtil.isValid(vObject.getLatestDate())){
				vObject.setLatestDate(vObject.getEffectiveDate());
				datePresent = false;
			}
			if(!ValidationUtil.isValid(vObject.getEarliestDate())){
				String earliestDate = getFtpCalculationsDao().getVisionPrevYearBusinessDate(vObject.getLatestDate());
				vObject.setEarliestDate(earliestDate);
			}
			defaultValueslst.add(exceptionCode.getRequest());
			ArrayList<FTPCalculationsVb> ftpProductBalance = new ArrayList<FTPCalculationsVb>();
			if("CNC".equalsIgnoreCase(vObject.getFtpMethod()) && datePresent){
				ftpProductBalance = (ArrayList<FTPCalculationsVb>)getFtpCalculationsDao().getFtpProductBalances(vObject);	
			}
			defaultValueslst.add(ftpProductBalance);
			exceptionCode.setRequest(defaultValueslst);
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;	
		}catch(Exception e){
			exceptionCode.setErrorMsg(e.getMessage());
			return exceptionCode;
		}
	}
	@Transactional(rollbackForClassName={"com.vision.exception.RuntimeCustomException"})
	public ExceptionCode doInsertApprRecord(List<FTPCalculationsVb> vObjects) throws RuntimeCustomException {
		ExceptionCode exceptionCode = null;
		String strErrorDesc  = "";
		String strCurrentOperation = Constants.ADD;
		String strApproveOperation =Constants.ADD;		
		//setServiceDefaults();
		try {
				for(FTPCalculationsVb vObject : vObjects){
					if(vObject.isChecked()){
						exceptionCode = getFtpCalculationsDao().doInsertApprRecordForNonTransFtpRates(vObject);
					}
				}
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setErrorMsg("FTP Rates Applied Successfully!!");
		}catch (RuntimeCustomException rcException) {
			throw rcException;
		}catch (UncategorizedSQLException uSQLEcxception) {
			strErrorDesc = uSQLEcxception.getMessage();
			exceptionCode.setErrorMsg(strErrorDesc);
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			/*exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);*/
		}catch(Exception ex){
			logger.error("Error in Add.",ex);
			strErrorDesc = ex.getMessage();
			exceptionCode.setErrorMsg(strErrorDesc);
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			/*exceptionCode = getResultObject(Constants.WE_HAVE_ERROR_DESCRIPTION);
			throw buildRuntimeCustomException(exceptionCode);*/
		}
		return exceptionCode;
	}
}
