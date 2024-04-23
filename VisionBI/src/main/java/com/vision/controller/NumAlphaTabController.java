package com.vision.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.MenuVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.ReviewResultVb;
import com.vision.vb.TabVb;
import com.vision.vb.VisionVariablesVb;
import com.vision.wb.AlphaNumTabWb;
import com.vision.wb.AlphaSubTabWb;
import com.vision.wb.NumSubTabWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "NumAlphaTab")
@Api(value="NumAlphaTab", description="Obtaining alpha tab and num tab values for drop down fields")
public class NumAlphaTabController{
	
	@Autowired
	AlphaNumTabWb alphaNumTabWb;
	
	@Autowired
	AlphaSubTabWb alphaSubTabWb;
	
	@Autowired
	NumSubTabWb numSubTabWb;
	
	
	@RequestMapping(path = "/pageLoadValues", method = RequestMethod.GET)
	@ApiOperation(value = "Page Load Values", notes = "Load AT/NT Values on screen load", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> pageOnLoad() {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			MenuVb menuVb = new MenuVb();
			menuVb.setActionType("Clear");

			ArrayList arrayList = alphaNumTabWb.getPageLoadValues();

			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Page Load Values", arrayList);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/getAllQueryResults", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAllQueryResults(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
			ExceptionCode exceptionCode = alphaNumTabWb.getAllQueryPopupResult(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	@RequestMapping(path = "/getQueryDetails", method = RequestMethod.POST)
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> queryDetails(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= alphaNumTabWb.getQueryResults(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*-------------------------------------ADD VisionVariables------------------------------------------*/
	@RequestMapping(path = "/addNumAlpha", method = RequestMethod.POST)
	@ApiOperation(value = "Add NumAlpha", notes = "Add NumAlpha", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> add(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Add");
			exceptionCode = alphaNumTabWb.insertRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------ADD VisionVariables------------------------------------------*/
	@RequestMapping(path = "/modifyNumAlpha", method = RequestMethod.POST)
	@ApiOperation(value = "Add NumAlpha", notes = "Add NumAlpha", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modify(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Modify");
			exceptionCode = alphaNumTabWb.modifyRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------ADD VisionVariables------------------------------------------*/
	@RequestMapping(path = "/deleteNumAlpha", method = RequestMethod.POST)
	@ApiOperation(value = "Add NumAlpha", notes = "Add NumAlpha", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> delete(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Delete");
			exceptionCode = alphaNumTabWb.deleteRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/approveNumAlpha", method = RequestMethod.POST)
	@ApiOperation(value = "Approve NumAlpha", notes = "Approve existing NumAlpha", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> approve(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Approve");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode = alphaNumTabWb.approve(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/rejectNumAlpha", method = RequestMethod.POST)
	@ApiOperation(value = "Approve NumAlpha", notes = "Approve existing NumAlpha", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reject(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Approve");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode = alphaNumTabWb.reject(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/reviewNumAlpha", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> review(@RequestBody TabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
//			ExceptionCode exceptionCode = visionVariablesWb.reviewRecord(vObject);
			List<ReviewResultVb> reviewList = alphaNumTabWb.reviewRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/reviewNumSubTab", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reviewNumSubTab(@RequestBody NumSubTabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
//			ExceptionCode exceptionCode = visionVariablesWb.reviewRecord(vObject);
			List<ReviewResultVb> reviewList = numSubTabWb.reviewRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/reviewAlphaSubTab", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reviewAlphaSubTab(@RequestBody AlphaSubTabVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
//			ExceptionCode exceptionCode = visionVariablesWb.reviewRecord(vObject);
			List<ReviewResultVb> reviewList = alphaSubTabWb.reviewRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	
	
	
	/*-------------------------------------NUM TAB SERVICE-------------------------------------------*/
	@GetMapping(value = "/getNumTab")
	@ApiOperation(value = "Returns list of available num sub tab values",
	notes = "Pass a num tab value in request param, to get numsub tab values in list",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getNumTab(@RequestParam("pNumTab") int pNumTab){
		JSONExceptionCode jsonExceptionCode  = null;
		try {
			List<NumSubTabVb> collTemp = numSubTabWb.findActiveNumSubTabsByNumTab(pNumTab);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", collTemp);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION,rex.getMessage(),"");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "/getNumTabCol")
	@ApiOperation(value = "Returns list of num sub tab values for specified columns",
	notes = "Based on specified column, sub tab values will be provided in list",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getNumTab(@RequestParam("pNumTab") int pNumTab, @RequestParam("columns") String columns){
		JSONExceptionCode jsonExceptionCode  = null;
		try {
			List collTemp = numSubTabWb.findActiveNumSubTabsByNumTabCols(pNumTab, columns);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", collTemp);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION,rex.getMessage(),"");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	/*-------------------------------------ALPHA TAB SERVICE-------------------------------------------*/
	@GetMapping(value = "/getAlphaTab")
	@ApiOperation(value = "Returns list of available alpha sub tab values",
	notes = "Pass a alpha tab value in request param, to get alphasub tab values in list",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAlphaTab(@RequestParam("pAlphaTab") int pAlphaTab){
		JSONExceptionCode jsonExceptionCode  = null;
		try {
			List<AlphaSubTabVb> collTemp = alphaSubTabWb.findActiveAlphaSubTabsByAlphaTab(pAlphaTab);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", collTemp);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION,rex.getMessage(),"");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "/getAlphaTabCol")
	@ApiOperation(value = "Returns list of alpha sub tab values for specified columns",
	notes = "Based on specified column, alpha subtab values will be provided in list",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAlphaTabCol(@RequestParam("pAlphaTab") int pAlphaTab, @RequestParam("columns") String columns){
		JSONExceptionCode jsonExceptionCode  = null;
		try {
			List collTemp = alphaSubTabWb.findActiveAlphaSubTabsByAlphaTabCols(pAlphaTab, columns);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", collTemp);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION,rex.getMessage(),"");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
}