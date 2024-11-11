package com.vision.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.vb.FTPRatesVb;
import com.vision.vb.MenuVb;
import com.vision.vb.ReviewResultVb;
import com.vision.wb.FTPRatesWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("FTPRates")
@Api(value = "FTPRates", description = "FTPRates")
public class FTPRatesController{
	@Autowired
	FTPRatesWb ftpRatesWb;
	/*-------------------------------------FTPRates SCREEN PAGE LOAD------------------------------------------*/
	@GetMapping("/pageLoadValues")
	@ApiOperation(value = "Page Load Values", notes = "Load AT/NT Values on screen load", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> pageOnLoad() {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			MenuVb menuVb = new MenuVb();
			menuVb.setActionType("Clear");

			ArrayList arrayList = ftpRatesWb.getPageLoadValues();

			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Page Load Values", arrayList);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}

	/*------------------------------------FTPRates - FETCH HEADER RECORDS-------------------------------*/
	@PostMapping("/getAllQueryResults")
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAllQueryResults(@RequestBody FTPRatesVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
			ExceptionCode exceptionCode = ftpRatesWb.getAllQueryPopupResult(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	@PostMapping("/getQueryDetails")
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> queryDetails(@RequestBody FTPRatesVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpRatesWb.getQueryResults(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*-------------------------------------ADD FTPRates------------------------------------------*/
	@PostMapping("/addFTPRates")
	@ApiOperation(value = "Add FTPRates", notes = "Add FTPRates", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> add(@RequestBody List<FTPRatesVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ExceptionCode pRequestCode = new ExceptionCode();
		try {
			FTPRatesVb vObject = new FTPRatesVb();
			vObject.setActionType("Add");
			pRequestCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.insertRecord(pRequestCode, vObjects);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------MODIFY FTPRates------------------------------------------*/
	@PostMapping("/modifyFTPRates")
	@ApiOperation(value = "Modify FTPRates", notes = "Modify FTPRates Values", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modify(@RequestBody List<FTPRatesVb>  vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ExceptionCode pRequestCode = new ExceptionCode();
		try {
			FTPRatesVb vObject = new FTPRatesVb();
			vObject.setActionType("Modify");
			pRequestCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.modifyRecord(pRequestCode, vObjects);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}

	/*-------------------------------------DELETE FTPRates------------------------------------------*/
	@PostMapping("/deleteFTPRates")
	@ApiOperation(value = "Delete FTPRates", notes = "Delete existing FTPRates", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> delete(@RequestBody List<FTPRatesVb>  vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ExceptionCode pRequestCode = new ExceptionCode();
		try {
			FTPRatesVb vObject = new FTPRatesVb();
			vObject.setActionType("Delete");
			pRequestCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.deleteRecord(pRequestCode, vObjects);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------Reject FTPRates------------------------------------------*/
	@PostMapping("/rejectFTPRates")
	@ApiOperation(value = "Reject FTPRates", notes = "Reject existing FTPRates", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reject(@RequestBody FTPRatesVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Reject");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.reject(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@PostMapping("/approveFTPRates")
	@ApiOperation(value = "Approve FTPRates", notes = "Approve existing FTPRates", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> approve(@RequestBody FTPRatesVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Approve");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.approve(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@PostMapping("/bulkApproveFTPRates")
	@ApiOperation(value = "Approve FTPRates", notes = "Approve existing FTPRates", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> bulkApprove(@RequestBody List<FTPRatesVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			FTPRatesVb vObject = new FTPRatesVb();
			vObject.setActionType("Approve");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.bulkApprove(vObjects, vObject);
			String errorMessage= exceptionCode.getErrorMsg().replaceAll("- Approve -", "- Bulk Approve -");
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), errorMessage,
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@PostMapping("/bulkRejectFTPRates")
	@ApiOperation(value = "Approve FTPRates", notes = "Approve existing FTPRates", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> bulkReject(@RequestBody List<FTPRatesVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			FTPRatesVb vObject = new FTPRatesVb();
			vObject.setActionType("Reject");
			exceptionCode.setOtherInfo(vObject);
			exceptionCode = ftpRatesWb.bulkReject(vObjects, vObject);
			String errorMessage= exceptionCode.getErrorMsg().replaceAll("- Reject -", "- Bulk Reject -");
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), errorMessage,
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}	
	
	@PostMapping("/reviewFTPRates")
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> review(@RequestBody FTPRatesVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
//			ExceptionCode exceptionCode = ftpRatesWb.reviewRecord(vObject);
			List<ReviewResultVb> reviewList = ftpRatesWb.reviewRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}	

}
