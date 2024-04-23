package com.vision.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vision.authentication.CustomContextHolder;
import com.vision.dao.CommonDao;
import com.vision.dao.ReportsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.PromptTreeVb;
import com.vision.vb.ReportFilterVb;
import com.vision.vb.ReportsVb;
import com.vision.vb.VisionUsersVb;
import com.vision.wb.ReportsWb;
import com.vision.wb.VisionUploadWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("reports")
@Api(value="reports" , description="Reports")
public class ReportsController {
	@Autowired
	ReportsWb reportsWb;
	@Autowired
	ReportsDao reportsDao;
	@Autowired
	VisionUploadWb visionUploadWb;
	@Autowired
	CommonDao commonDao;
	/*-------------------------------------GET REPORT LIST BY GROUP------------------------------------------*/
	@RequestMapping(path = "/getReportMaster", method = RequestMethod.GET)
	@ApiOperation(value = "Get Report List",notes = "Get List of Report on Group wise",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getReportMasterList(){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.getReportList();
			String reportAutoLoad = commonDao.findVisionVariableValue("PRD_REPORT_AUTOLOAD");
			exceptionCode.setOtherInfo(reportAutoLoad);
			exceptionCode.setRequest(commonDao.getAllBusinessDate());
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo(),exceptionCode.getRequest());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------GET REPORT FILTERS------------------------------------------*/
	@RequestMapping(path = "/getReportFilter", method = RequestMethod.POST)
	@ApiOperation(value = "Get Report Filter",notes = "Get Filter for both Reports and Dashboard",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getReportFilter(@RequestBody ReportsVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.reportFilterProcess(vObject.getFilterRefCode(), vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------GET CHILD DEPENDENT FILTER------------------------------------------*/
	@RequestMapping(path = "/getChildDependentFilter", method = RequestMethod.POST)
	@ApiOperation(value = "Get Dependency Filter Value",notes = "Get the value for Dependent filter",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getReportDependentFilter(@RequestBody ReportFilterVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.getFilterSourceValue(vObject);
			//exceptionCode.setResponse(filterSourceVal);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------GET REPORT CHILD DEPENDENT FILTER------------------------------------------*/
	@RequestMapping(path = "/getReportChildDependentFilter", method = RequestMethod.POST)
	@ApiOperation(value = "Get Report Prompt Dependency Filter Value",notes = "Get the value for Dependent filter",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDependentValueForReportPrompts(@RequestBody ReportFilterVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.getReportFilterSourceValue(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------FETCHING REPORTS RESULTS-------------------------------*/
	@RequestMapping(path = "/getReportData", method = RequestMethod.POST)
	@ApiOperation(value = "Get Report Data",notes = "Get the Report Data for the specified Report Id",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getSubReportResultData(@RequestBody ReportsVb vObject) throws SQLException {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			ExceptionCode exceptionCode = reportsWb.getReportDetails(vObject);
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					ReportsVb subReportsVb = (ReportsVb) exceptionCode.getResponse();
					exceptionCode =  reportsWb.getResultData(subReportsVb,false);
				}
			}
			//exceptionCode.setOtherInfo(vObject);
			reportsDao.insertReportsAudit(vObject, exceptionCode.getErrorMsg());
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*------------------------------------FETCHING REPORTS RESULTS-------------------------------*/
	@RequestMapping(path = "/getInteractiveReportsDetail", method = RequestMethod.POST)
	@ApiOperation(value = "Get Interactive Reports List",notes = "Get the list of Interactive Reports for the report id ",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getInteractiveReportsData(@RequestBody ReportsVb vObject) throws SQLException {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			ExceptionCode exceptionCode = reportsWb.getIntReportsDetail(vObject);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*------------------------------------MDM REPORTS EXCEL EXPORT-------------------------------*/
	@RequestMapping(path = "/reportExcelExport", method = RequestMethod.POST)
	@ApiOperation(value = "Get Excel Report",notes = "Export Report Data to Excel",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> excelExportReport(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();

			int currentUserId = visionUsersVb.getVisionId();
			ExceptionCode exceptionCode1 = reportsWb.exportToXls(vObject, currentUserId,"0");
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "xlsx");
				request.setAttribute("fileName", ""+exceptionCode1.getOtherInfo()+".xlsx");
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode1.getErrorCode(),exceptionCode1.getErrorMsg(),exceptionCode1.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*------------------------------------ REPORTS PDF EXPORT-------------------------------*/
	@RequestMapping(path = "/reportPdfExport", method = RequestMethod.POST)
	@ApiOperation(value = "Get PDF Report",notes = "Export Report Data to PDF",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> pdfExportReport(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();

			int currentUserId = visionUsersVb.getVisionId();
			ExceptionCode exceptionCode1 = reportsWb.exportToPdf(currentUserId,vObject);
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "pdf");
				request.setAttribute("fileName", ""+exceptionCode1.getOtherInfo()+".pdf");
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode1.getErrorCode(),exceptionCode1.getErrorMsg(),exceptionCode1.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*------------------------------------REPORTS EXCEL EXPORT-------------------------------*/
	@RequestMapping(path = "/MultiExcel", method = RequestMethod.POST)
	@ApiOperation(value = "Multi Excel",notes = "Export Report Data to Excel",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> exportMultiPanelExcel(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode exceptionCode1 = reportsWb.exportMultiExcel(vObject);
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "xlsx");
				request.setAttribute("fileName", ""+exceptionCode1.getOtherInfo()+".xlsx");
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode1.getErrorCode(),exceptionCode1.getErrorMsg(),exceptionCode1.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*------------------------------------ REPORTS PDF EXPORT-------------------------------*/
	@RequestMapping(path = "/MultiPdf", method = RequestMethod.POST)
	@ApiOperation(value = "Get PDF Report",notes = "Export Report Data to PDF",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> exportMultiPanelpdf(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode exceptionCode1 = reportsWb.exportMultiPdf(vObject);
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "pdf");
				request.setAttribute("fileName", ""+exceptionCode1.getOtherInfo()+".pdf");
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode1.getErrorCode(),exceptionCode1.getErrorMsg(),exceptionCode1.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*-------------------------------------GET TREE FILTER------------------------------------------*/
	@RequestMapping(path = "/getTreePromptData", method = RequestMethod.POST)
	@ApiOperation(value = "Get Tree Prompt Data",notes = "Get the value for Tree Prompt",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getTreePromptData(@RequestBody ReportFilterVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.getTreePromptData(vObject);
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION && exceptionCode.getResponse()!= null) {
				List<PromptTreeVb> promptTreelst = (List) exceptionCode.getResponse();
				exceptionCode.setResponse(promptTreelst);
			}
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------MDM CB REPORT EXPORT-------------------------------*/
	@RequestMapping(path = "/getCBreport", method = RequestMethod.POST)
	@ApiOperation(value = "Get CB Report Data",notes = "get CB Report Data",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getCBKReport(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode1 = new ExceptionCode();
		try{
			vObject.setActionType("Query");
			if(vObject.getReportId().equalsIgnoreCase("AMLCFT01")){
				exceptionCode1 = reportsWb.createCBKReport1(vObject);
			}else if("CBW".equalsIgnoreCase(vObject.getReportType())){
				exceptionCode1 = reportsWb.createCBWordReport(vObject);
			}else{
				exceptionCode1 = reportsWb.createCBReport(vObject);
			}
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				//docs Only for Family Bank
				if(vObject.getReportId().equalsIgnoreCase("AMLCFT01")){
					request.setAttribute("fileExtension", "docx");
					request.setAttribute("fileName", exceptionCode1.getOtherInfo()+".docx");
				}else if("CBW".equalsIgnoreCase(vObject.getReportType())){
					request.setAttribute("fileExtension", "docx");
					request.setAttribute("fileName", exceptionCode1.getOtherInfo()+".docx");
				}else {
					request.setAttribute("fileExtension", "xlsx");
					request.setAttribute("fileName", exceptionCode1.getOtherInfo()+".xlsx");
				}
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode1.getErrorCode(),exceptionCode1.getErrorMsg(),exceptionCode1.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/reportExportToCsv", method = RequestMethod.POST)
	@ApiOperation(value = "Get Export Report Data to CSV",notes = "Get Export Report Data to CSV",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getExportToCsv(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			ExceptionCode exceptionCode1 = reportsWb.exportReportToCsv(vObject);
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "txt");
				request.setAttribute("fileName", exceptionCode1.getOtherInfo());
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode1.getErrorCode(),exceptionCode1.getErrorMsg(),exceptionCode1.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	/*-------------------------------------GET REPORT LIST BY GROUP------------------------------------------*/
	@RequestMapping(path = "/getWidgetMaster", method = RequestMethod.GET)
	@ApiOperation(value = "Get Widget List",notes = "Get Widget List",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getWidgetMasterList(){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.getWidgetList();
			exceptionCode.setRequest(commonDao.getAllBusinessDate()); 
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo(),exceptionCode.getRequest());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------SAVE WIDGET------------------------------------------*/
	@RequestMapping(path = "/saveUserWidget", method = RequestMethod.POST)
	@ApiOperation(value = "Save User Widget",notes = "Save User Pref Widget",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> saveUserWidget(@RequestBody ReportsVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.saveWidget(vObject) ;
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = reportsWb.saveReportUserDef(vObject) ;
			}
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------DELETE WIDGET------------------------------------------*/
	@RequestMapping(path = "/deleteUserWidget", method = RequestMethod.POST)
	@ApiOperation(value = "Delete User Widget",notes = "Delete User saved Widget",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteUserWidget(@RequestBody ReportsVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.deleteSavedWidget(vObject) ;
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = reportsWb.deleteSavedReportUserDef(vObject) ;
			}
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------ WIDGET EXPORT-------------------------------*/
	@RequestMapping(path = "/widgetExport", method = RequestMethod.POST)
	@ApiOperation(value = "Widget Export PDF",notes = "Widget Export Pdf",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> widgetPdfExport(@RequestParam("reportTitle") String reportTitle,@RequestParam("promptLabel") String promptLabel,
			@RequestParam("gridReportIds") String gridReportIds,@RequestParam("fileName") String fileName,@RequestParam("appTheme") String appTheme,
			HttpServletRequest request,HttpServletResponse response,@RequestParam("exportImage") String exportImageBase64) throws JsonMappingException, JsonProcessingException , JSONException {
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			ArrayList<ReportsVb> reportslst = new ArrayList();
			JSONArray array = new JSONArray(gridReportIds);
			for(int i=0; i < array.length(); i++) 
			{  
				JSONObject gridObject = array.getJSONObject(i);
				try {
					ReportsVb dObj = new ObjectMapper().readValue(gridObject.toString(), ReportsVb.class);
					reportslst.add(dObj);	
				}catch(Exception e) {
					e.printStackTrace();
				}
			} 
			exceptionCode = reportsWb.exportWidgetToPdf(reportTitle,promptLabel,gridReportIds,fileName,reportslst,appTheme,exportImageBase64);
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "pdf");
				request.setAttribute("fileName", ""+exceptionCode.getOtherInfo()+".pdf");
				request.setAttribute("filePath", exceptionCode.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Unable to Export.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}	
	}
	/*-------------------------------------SAVE USER SETTING------------------------------------------*/
	@RequestMapping(path = "/saveUserSettingforReport", method = RequestMethod.POST)
	@ApiOperation(value = "Save User Setting for Report",notes = "Save User Pref Setting for Report",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> saveUserSetting(@RequestBody ReportsVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = reportsWb.saveReportUserDef(vObject) ;
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),"Report Settings Saved Successfully",exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------Report Type E Download-------------------------------*/
	@RequestMapping(path = "/XLReportList", method = RequestMethod.POST)
	@ApiOperation(value = "XL Report Download",notes = "Report Type E Download for Central Banks",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> XLReportList(@RequestBody ReportsVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			String extension="";
			String path="";
			String server="";
			String promptVal="";
			String field1="";
			String field2="";

			String macro = reportsDao.getDependentPrompt(vObject.getReportId(),"PARAMETER_1");
			if(ValidationUtil.isValid(macro)){
				macro =macro.trim();
			}else{
			   jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report Suite - Maintenance Issue", null);
			   return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
			}
			String[] otherColValue = macro.split("\\#}");
		    for(int h=0; h<otherColValue.length; h++){
		    	String source = otherColValue[h];
		    	if(ValidationUtil.isValid(source)){
				    String[] outPut = reportsDao.getKeyValue(source);
				    outPut[0]=outPut[0].toUpperCase();
					if("EXTENSION".equalsIgnoreCase(outPut[0])){
						extension = outPut[2].toUpperCase();
					}else if("PATH".equalsIgnoreCase(outPut[0])){
						path = outPut[2];
					}else if("VISION_SERVER_NAME".equalsIgnoreCase(outPut[0])){
						server= outPut[2].toUpperCase();
					}else if("PROMPT_FILE_NAME_ID".equalsIgnoreCase(outPut[0])){
						promptVal= outPut[2].toUpperCase();
						 String fileName = "";
						String getVal[]=promptVal.split("@'_'@");
						for(String str : getVal) {
							fileName = reportsWb.replacePromptValues(str,vObject);
							promptVal = promptVal.replaceAll(str, fileName);
						}
						promptVal = promptVal.replaceAll("@'_'@","_" );
					}
		    	}
		    }
			exceptionCode = reportsWb.listReportFilesFromFtpServer("",promptVal, extension, path, server, CustomContextHolder.getContext().getVisionId());
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if(ValidationUtil.isValid(promptVal) && "ALL".equalsIgnoreCase(promptVal)) {
					request.setAttribute("fileExtension", "zip");
					request.setAttribute("fileName", "Files.zip");
					request.setAttribute("filePath", exceptionCode.getResponse());
				} else {
					request.setAttribute("fileExtension", "xlsx");
					request.setAttribute("fileName", exceptionCode.getRequest());
					request.setAttribute("filePath", exceptionCode.getResponse());
				}
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Report is unable to Download.Contact System Admin!!", null);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
				}else{
					jsonExceptionCode =	new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Success", response);
					return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
				}
			}else{
				jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
				return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
			}
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
