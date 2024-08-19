package com.vision.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vision.dao.FTPGroupsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpMethodsVb;
import com.vision.vb.MenuVb;
import com.vision.wb.FTPGroupsWb;
import com.vision.wb.FTPSourceConfigWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "FTPGroups")
@Api(value="FTPGroups", description="Obtaining alpha tab and num tab values for drop down fields")
public class FTPGroupsController {
	@Autowired
	FTPGroupsWb ftpGroupsWb;
	
	FTPGroupsDao fTPGroupsDao;
	
	@Autowired
	FTPSourceConfigWb ftpSourceConfigWb;
	
	@RequestMapping(path = "/pageLoadValues", method = RequestMethod.GET)
	@ApiOperation(value = "Page Load Values", notes = "Load AT/NT Values on screen load", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> pageOnLoad() {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			MenuVb menuVb = new MenuVb();
			menuVb.setActionType("Clear");

			ArrayList arrayList = ftpGroupsWb.getPageLoadValues();

			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Page Load Values", arrayList);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/getAllQueryResults", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAllQueryResults(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
			ExceptionCode exceptionCode = ftpGroupsWb.getAllQueryPopupResult1(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	@RequestMapping(path = "/addGroupControls", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> add(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.insertRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/modifyGroupControls", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modify(@RequestBody List<FTPGroupsVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			FTPGroupsVb ftpGroupsVb =new FTPGroupsVb();
			ftpGroupsVb.setActionType("Modify");
			exceptionCode.setOtherInfo(ftpGroupsVb);
			exceptionCode = ftpGroupsWb.modifyRecord(exceptionCode,vObjects);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/viewDetailsFTPControls", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> viewDetailsFTPControls(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
			ExceptionCode exceptionCode = ftpGroupsWb.queryResultSource(vObject);
			exceptionCode.setOtherInfo(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getRequest(),exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	//addCurveId using for Reload
	@RequestMapping(path = "/reloadCurveId", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reloadCurveId(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
		   if(ValidationUtil.isValid(vObject.getEffectiveDate())){
			   vObject.setEffectiveDate(vObject.getEffectiveDate());
		   }else{
			   String businessDate = ftpGroupsWb.getFtpGroupsDao().businessDay();
			   vObject.setEffectiveDate(businessDate);
		   }
			
			ExceptionCode exceptionCode = ftpGroupsWb.reloadCurveId(vObject);
			exceptionCode.setOtherInfo(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getRequest(),exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	//Add / Modify CurveId using for Reload
	@RequestMapping(path = "/addModifyFtpCurve", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> addModifyFtpCurve(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Modify");
			exceptionCode = ftpGroupsWb.addModifyFtpCurve(vObject.getFtpCurveList(),vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	//addFtpAddOn using for Reload
	@RequestMapping(path = "/reloadFtpAddOn", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reloadFtpAddOn(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
		   if(ValidationUtil.isValid(vObject.getEffectiveDate())){
			   vObject.setEffectiveDate(vObject.getEffectiveDate());
		   }else{
			   String businessDate = ftpGroupsWb.getFtpGroupsDao().businessDay();
			   vObject.setEffectiveDate(businessDate);
		   }
			
			ExceptionCode exceptionCode = ftpGroupsWb.reloadFtpAddOn(vObject);
			exceptionCode.setOtherInfo(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getRequest(),exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	//Add / Modify Add On using for Reload
	@RequestMapping(path = "/addModifyFtpAddOn", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> addModifyFtpAddOn(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Modify");
			exceptionCode = ftpGroupsWb.addModifyFtpAddOn(vObject.getFtpCurveList(),vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	//addFtpPremium using for Reload
	@RequestMapping(path = "/reloadFtpPremium", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reloadFtpPremium(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
		   if(ValidationUtil.isValid(vObject.getEffectiveDate())){
			   vObject.setEffectiveDate(vObject.getEffectiveDate());
		   }else{
			   String businessDate = ftpGroupsWb.getFtpGroupsDao().businessDay();
			   vObject.setEffectiveDate(businessDate);
		   }
			
			ExceptionCode exceptionCode = ftpGroupsWb.reloadFtpPremium(vObject);
			exceptionCode.setOtherInfo(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getRequest(),exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/********************************** Source Config Controls **********************************	 */
	@RequestMapping(path = "/modifySourceControls", method = RequestMethod.POST)
	@ApiOperation(value = "Add SourceControls", notes = "Add SourceControls", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modifySourceControls(@RequestBody List<FTPSourceConfigVb> ftpSourceConfigList) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ExceptionCode pRequestCode = new ExceptionCode();
		try {
			FTPSourceConfigVb vObject = new FTPSourceConfigVb();
			vObject.setActionType("Modify");
			pRequestCode.setOtherInfo(vObject);
			exceptionCode = ftpSourceConfigWb.modifyRecord(pRequestCode, ftpSourceConfigList);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/deleteSourceControls", method = RequestMethod.POST)
	@ApiOperation(value = "Add SourceControls", notes = "Add SourceControls", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteSourceControls(@RequestBody List<FTPSourceConfigVb> ftpSourceConfigList) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ExceptionCode pRequestCode = new ExceptionCode();
		try {
			FTPSourceConfigVb vObject = new FTPSourceConfigVb();
			vObject.setActionType("Modify");
			pRequestCode.setOtherInfo(vObject);
			exceptionCode = ftpSourceConfigWb.deleteRecord(pRequestCode, ftpSourceConfigList);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	/********************************** Source Tuning **********************************	 */
	
	@RequestMapping(path = "/getQueryResultTuning", method = RequestMethod.POST)
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getQueryResultTuning(@RequestBody FTPSourceConfigVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getQueryResultTuning(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}	
	@RequestMapping(path = "/addModifyFtpTuning", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> addModifyFtpTuning(@RequestBody FTPSourceConfigVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.getFtpSourceConfigDao().addModifyFtpTuning(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/getTableColumns", method = RequestMethod.POST)
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getTableColumns(@RequestBody FTPSourceConfigVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getTableColumns(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	// Get FTP Method Details BY FTP Reference
	@RequestMapping(path = "/getQueryFtpMethodDetails", method = RequestMethod.POST)
	@ApiOperation(value = "Get FTP Method Details",notes = "FTP Method Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getQueryResultMethods(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getQueryResultMethods(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	// Get FTP Source Details BY FTP Reference
		@RequestMapping(path = "/getQueryResultsSource", method = RequestMethod.POST)
		@ApiOperation(value = "Get FTP Method Details",notes = "FTP Method Details",response = ResponseEntity.class)
		public ResponseEntity<JSONExceptionCode> getQueryResultsSource(@RequestBody FTPGroupsVb vObject) {
			JSONExceptionCode jsonExceptionCode  = null;
			try{
				vObject.setActionType("Query");
				ExceptionCode  exceptionCode= ftpGroupsWb.getQueryResultsSource(vObject);
				jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}catch(RuntimeCustomException rex){
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}	
		}
		
		// Get FTP RateId Details BY FTP RateId
		@RequestMapping(path = "/getFTPRateIdDetails", method = RequestMethod.POST)
		@ApiOperation(value = "Get FTP RATE ID Details ",notes = "FTP RATEID Details",response = ResponseEntity.class)
		public ResponseEntity<JSONExceptionCode> getFtpRateId(@RequestBody FTPCurveVb vObject) {
			JSONExceptionCode jsonExceptionCode  = null;
			try{
				vObject.setActionType("Query");
				ExceptionCode  exceptionCode= ftpGroupsWb.getFtpRateIdDetails(vObject);
				jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}catch(RuntimeCustomException rex){
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}	
		}
	
		
		@RequestMapping(path = "/modifyFtpPremium", method = RequestMethod.POST)
		@ApiOperation(value = "Add FTPPremium", notes = "Add FTPGroups", response = ResponseEntity.class)
		public ResponseEntity<JSONExceptionCode> modifyFtpPremium(@RequestBody FTPCurveVb vObject) {
			JSONExceptionCode jsonExceptionCode = null;
			ExceptionCode exceptionCode = new ExceptionCode();
			try {
				vObject.setActionType("Modify");
				exceptionCode = ftpGroupsWb.modifyFtpPremium(vObject.getFtpCurveList(),vObject);
				jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),exceptionCode.getResponse(),
						exceptionCode.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			} catch (RuntimeCustomException rex) {
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}
	
	
	
	
}
