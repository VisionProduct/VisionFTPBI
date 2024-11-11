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

import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.DCManualQueryVb;
import com.vision.vb.DesignAndAnalysisMagnifierVb;
import com.vision.vb.FTPCurveVb;
import com.vision.vb.FTPGroupsVb;
import com.vision.vb.FTPSourceConfigVb;
import com.vision.vb.FtpAddonVb;
import com.vision.vb.FtpPremiumsVb;
import com.vision.vb.MenuVb;
import com.vision.vb.ReviewResultVb;
import com.vision.vb.VcConfigMainColumnsVb;
import com.vision.vb.VcConfigMainTreeVb;
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
	
//	FTPGroupsDao fTPGroupsDao;
	
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
			ExceptionCode exceptionCode = ftpGroupsWb.getAllQueryPopupResult(vObject);
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
	public ResponseEntity<JSONExceptionCode> queryDetails(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getQueryResults(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	@RequestMapping(path = "/addOrModifyGroupMethodsSource", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> addOrModifyGroupMethodsSource(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.modifyRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/deleteGroupMethodsSourceAndAll", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteGroupMethodsSourceAndAll(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.deleteRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/deleteFTPCurve", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteRecordFTPCurve(@RequestBody List<FTPCurveVb> vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.deleteFTPCurve(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/deleteFTPAddon", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteFTPAddon(@RequestBody List<FtpAddonVb> vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.deleteFTPAddon(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/deleteFTPPremiums", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteFTPPremiums(@RequestBody List<FtpPremiumsVb> vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.deleteFTPPremiums(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/rejectFTPCurve", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> rejectFTPCurve(@RequestBody List<FTPCurveVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		FTPCurveVb vObject = new FTPCurveVb();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.rejectFtpCurves(vObjects, vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/rejectFTPAddon", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> rejectFTPAddon(@RequestBody List<FtpAddonVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		FtpAddonVb vObject = new FtpAddonVb();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.rejectFtpAddOn(vObjects, vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/rejectFTPPremiums", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> rejectFTPPremiums(@RequestBody List<FtpPremiumsVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		FtpPremiumsVb vObject = new FtpPremiumsVb();
		try {
//			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.rejectFtpPremiums(vObjects, vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/bulkRejectGroup", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> bulkRejectGroup(@RequestBody List<FTPGroupsVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		FTPGroupsVb vObject = new FTPGroupsVb();
		try {
			vObject.setActionType("Reject");
			exceptionCode = ftpGroupsWb.bulkReject(vObjects, vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/bulkDeleteGroup", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> bulkDeleteRecord(@RequestBody List<FTPGroupsVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		FTPGroupsVb vObject = new FTPGroupsVb();
		try {
			vObject.setActionType("Reject");
			exceptionCode = ftpGroupsWb.bulkDeleteRecord(vObjects, vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/rejctGroupMethodsSourceAndAll", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> rejectGroupMethodsSourceAndAll(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.reject(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/approveGroupMethodsSourceAndAll", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> approveGroupMethodsSourceAndAll(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setActionType("Add");
			exceptionCode = ftpGroupsWb.approve(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/bulkApproveGroup", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> bulkApproveGroup(@RequestBody List<FTPGroupsVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		FTPGroupsVb vObject = new FTPGroupsVb();
		try {
			vObject.setActionType("Approve");
			exceptionCode = ftpGroupsWb.bulkApprove(vObjects, vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/addOrModifyFtpCurve", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modifyRecordFtpCurve(@RequestBody List<FTPCurveVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			exceptionCode = ftpGroupsWb.modifyRecordFtpCurve(vObjects);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/addOrModifyFtpAddOn", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modifyRecordFtpAddOn(@RequestBody List<FtpAddonVb> vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			exceptionCode = ftpGroupsWb.modifyRecordFtpAddOn(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/addOrModifyFtpLiqPremium", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> modifyRecordFtpLiqPremium(@RequestBody List<FtpPremiumsVb> vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			exceptionCode = ftpGroupsWb.modifyRecordFtpLiqPremium(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/updateDefaultFlagRecord", method = RequestMethod.POST)
	@ApiOperation(value = "Add FTPGroups", notes = "Add FTPGroups", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> updateDefaultFlagRecord(@RequestBody List<FTPGroupsVb> vObjects) {
		JSONExceptionCode jsonExceptionCode = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			exceptionCode = ftpGroupsWb.updateDefaultRecord(vObjects);
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
//			exceptionCode = ftpGroupsWb.modifyRecord(exceptionCode,vObjects);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*@RequestMapping(path = "/viewDetailsFTPControls", method = RequestMethod.POST)
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
	}*/
	//addCurveId using for Reload
	@RequestMapping(path = "/reloadCurveId", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reloadCurveId(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			System.out.println("Query Start : "+(new Date()).toString());
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
	
	//addFtpAddOn using for Reload
	@RequestMapping(path = "/reloadFtpAddOn", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reloadFtpAddOn(@RequestBody FtpAddonVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode exceptionCode = ftpGroupsWb.reloadftpAddon(vObject);
			exceptionCode.setOtherInfo(vObject);
			System.out.println("Query End : "+(new Date()).toString());
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Query Results", exceptionCode.getRequest(),exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	//addFtpPremium using for Reload
	@RequestMapping(path = "/reloadFtpPremium", method = RequestMethod.POST)
	@ApiOperation(value = "Get All profile Data",notes = "Fetch all the existing records from the table",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reloadFtpPremium(@RequestBody FtpPremiumsVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
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
	
	/*-------------------------------------LIST CATALOG TREES COLUMNS QUERY SERVICE-------------------------------------------*/
	@RequestMapping(path = "/getQueryTreeColumns", method = RequestMethod.POST)
	@ApiOperation(value = "Listing of Design Query Catalog Tree Columns", notes = "Returns list of all Design Query Tree Columns based UserGroup", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getQueryTreeColumns(@RequestBody VcConfigMainTreeVb treeVb) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			List<VcConfigMainColumnsVb> queryList = ftpGroupsWb.getQueryTreeColumns(treeVb);
			if(queryList.size()>0) {
				jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, queryList.size()+" Columns listed for Catalog Tree",
						queryList);
			 } else {
				 jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "No Columns found",
						 null); 
			 }
		
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path="/getMagnefierData",method=RequestMethod.POST)
	@ApiOperation(value="Get Data For Magnefier Function",notes="Get Data For Magnefier Function",response=ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getMagnefierData(@RequestBody DesignAndAnalysisMagnifierVb magnefierVb){
		JSONExceptionCode jsonExceptionCode=null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			DCManualQueryVb dcManualQueryVb = new DCManualQueryVb();
			dcManualQueryVb.setQueryId(magnefierVb.getQueryId());
			List<DCManualQueryVb> queryList = ftpGroupsWb.getSpecificManualQuery(dcManualQueryVb);
			if(queryList.size()==0) {
				jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "No records found", dcManualQueryVb);
			} else {
				dcManualQueryVb = queryList.get(0);
				
				if(!queryList.get(0).getQueryColumnXML().contains("<name>"+magnefierVb.getUseColumn()+"</name>")
						&& !queryList.get(0).getQueryColumnXML().contains("<name>"+magnefierVb.getDisplayColumn()+"</name>")) {
					jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg()," Column names requested are invalid ");
				} else {
					exceptionCode = CommonUtils.formHashList(dcManualQueryVb);
					if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
						String[] hashArr = (String[]) exceptionCode.getOtherInfo();
						String[] hashValArr = (String[]) exceptionCode.getRequest();
						String dbScript = ftpGroupsWb.getDbScript(dcManualQueryVb.getDatabaseConnectivityDetails());
						if(ValidationUtil.isValid(dbScript)) {
							exceptionCode =  ftpGroupsWb.executeManualQueryForManifierDetails(dcManualQueryVb, dbScript, hashArr, hashValArr, magnefierVb);
							if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
								jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "List Specific Query", exceptionCode.getResponse());
							} else {
								jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg()," Problem in executing query ");
							}
						} else {
							jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg()," Problem in fetching connection configuration for variable : "+dcManualQueryVb.getDatabaseConnectivityDetails());
						}
					} else {
						jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, exceptionCode.getErrorMsg()," Problem with Hashvariable ");
					}
				}
			}
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode,HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode=new JSONExceptionCode(Constants.ERRONEOUS_OPERATION,rex.getMessage(),"");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode,HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/reviewGroupMethodsSourceAndAll", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reviewGroupMethodsSourceAndAll(@RequestBody FTPGroupsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
			ArrayList arrayList = ftpGroupsWb.reviewRecord(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", arrayList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/reviewFtpCurve", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reviewCurve(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
			List<ReviewResultVb> reviewList = ftpGroupsWb.reviewRecordCurve(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/reviewFtpAddon", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reviewAddon(@RequestBody FtpAddonVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
			List<ReviewResultVb> reviewList = ftpGroupsWb.reviewRecordAddon(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/reviewFtpPremiums", method = RequestMethod.POST)
	@ApiOperation(value = "Get Headers", notes = "Fetch all the existing records from the table", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> reviewPremiums(@RequestBody FtpPremiumsVb vObject) {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			vObject.setActionType("Query");
			List<ReviewResultVb> reviewList = ftpGroupsWb.reviewRecordPremium(vObject);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Listing", reviewList);			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/getCurveListFoOverAllReveiw", method = RequestMethod.POST)
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getCurveListFoOverAllReveiw(@RequestBody FTPCurveVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getCurveListFoOverAllReveiw(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getRequest(), exceptionCode.getResponse1(),exceptionCode.getOtherInfo());
			
//			int status,String message, Object request, Object response, Object response1, Object otherInfo
			
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	@RequestMapping(path = "/getAddonListFoOverAllReveiw", method = RequestMethod.POST)
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getAddonListFoOverAllReveiw(@RequestBody FtpAddonVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getAddonListFoOverAllReveiw(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getRequest(), exceptionCode.getResponse1(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
	@RequestMapping(path = "/getPremiumsListFoOverAllReveiw", method = RequestMethod.POST)
	@ApiOperation(value = "Get All ADF Schules",notes = "ADF Schules Details",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getPremiumsListFoOverAllReveiw(@RequestBody FtpPremiumsVb vObject) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode  exceptionCode= ftpGroupsWb.getPremiumsListFoOverAllReveiw(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(), exceptionCode.getErrorMsg(), exceptionCode.getRequest(), exceptionCode.getResponse1(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	
/*	//Add / Modify CurveId using for Reload
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
	
	
	*//********************************** Source Config Controls **********************************	 *//*
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
	
	*//********************************** Source Tuning **********************************	 *//*
	
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
		}*/
	
	
	
	
}
