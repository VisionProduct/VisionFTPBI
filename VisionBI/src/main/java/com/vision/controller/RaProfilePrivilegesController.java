package com.vision.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vision.dao.RaProfilePrivilegesDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.RaProfileVb;
import com.vision.wb.RaProfilePrivilegesWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("raProfileSetup")
@Api(value="raProfileSetup" , description="This is to set the Privileges based on User Group/Profile")
public class RaProfilePrivilegesController {
	@Autowired
	private RaProfilePrivilegesWb raprofilePrivilegesWb;
	@Autowired
	RaProfilePrivilegesDao raProfilePrivilegesDao;
	
	/*-------------------------------------RA PROFILE SETUP SCREEN PAGE LOAD------------------------------------------*/
	@RequestMapping(path = "/pageLoadValues", method = RequestMethod.GET)
	@ApiOperation(value = "Page Load Values",notes = "Load AT/NT Values on screen load",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> profilePrivilegesLoad(){
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			ArrayList arrayList = raprofilePrivilegesWb.getPageLoadValues();
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Page Load Values", arrayList);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*-------------------------------------GET ALL PROFILE DATA RECORDS------------------------------------------*/
	@RequestMapping(path = "/getAllQueryResults", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAllQueryResults(@RequestBody RaProfileVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			vObject.setVerificationRequired(false);
			vObject.setRecordIndicator(0);
			ExceptionCode exceptionCode = raprofilePrivilegesWb.getAllQueryPopupResult(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*-------------------------------------RA PROFILE SETUP - ADD------------------------------------------*/
	@RequestMapping(path = "/addProfileRa", method = RequestMethod.POST)
	@ApiOperation(value = "Add Profile Privileges",notes = "Add Profile Proviliges",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> add(@RequestBody RaProfileVb profileData){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			profileData.setActionType("Add");
			exceptionCode = raprofilePrivilegesWb.insertRecord(profileData);
			if(exceptionCode.getErrorCode() ==Constants.SUCCESSFUL_OPERATION){
				jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, exceptionCode.getErrorMsg(), exceptionCode.getOtherInfo());
			}else {
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			}
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------RA PROFILE SETUP - MODIFY------------------------------------------*/
	@RequestMapping(path = "/modifyProfileRa", method = RequestMethod.POST)
	@ApiOperation(value = "Modify Profile Privileges",notes = "Modify Profile Proviliges",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modify(@RequestBody RaProfileVb profileData){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			profileData.setActionType("Add");
			exceptionCode = raprofilePrivilegesWb.modifyRecord(profileData);
			if(exceptionCode.getErrorCode() ==Constants.SUCCESSFUL_OPERATION){
				jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, exceptionCode.getErrorMsg(), exceptionCode.getOtherInfo());
			}else {
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			}
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------RA PROFILE SETUP - DELETE------------------------------------------*/
	@RequestMapping(path = "/deleteProfileRa", method = RequestMethod.POST)
	@ApiOperation(value = "Delete Profile Privileges",notes = "Delete Profile Proviliges",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> delete(@RequestBody RaProfileVb profileData){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			profileData.setActionType("Delete");
			exceptionCode = raprofilePrivilegesWb.deleteRecord(profileData);
			if(exceptionCode.getErrorCode() ==Constants.SUCCESSFUL_OPERATION){
				jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, exceptionCode.getErrorMsg(), exceptionCode.getOtherInfo());
			}else {
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			}
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------RA PROFILE SETUP - ADD------------------------------------------*/
	@RequestMapping(path = "/updateHomeDashboard", method = RequestMethod.POST)
	@ApiOperation(value = "Update Home Dashboard",notes = "Update Home Dashboard",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> updateHomeDashboard(@RequestBody RaProfileVb profileData){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			profileData.setActionType("Modify");
			exceptionCode = raprofilePrivilegesWb.userHomeDashboardUpdate(profileData);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
		}
	}
	/*-------------------------------------RA PROFILE SETUP - GET DASHBOARDLIST------------------------------------------*/
	
	@RequestMapping(path = "/getReportDashboardList", method = RequestMethod.POST)
	@ApiOperation(value = "Get Report Dashboard list",notes = "List of Dashboard/Reports for profile setup",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAllReportDashboards(@RequestParam String type){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			
			List<AlphaSubTabVb> reportDashboardlst = raProfilePrivilegesDao.getAllDashboardList(type);
			exceptionCode.setResponse(reportDashboardlst);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo(),exceptionCode.getResponse());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
}
