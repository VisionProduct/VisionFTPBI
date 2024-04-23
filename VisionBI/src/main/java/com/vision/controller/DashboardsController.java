package com.vision.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vision.authentication.CustomContextHolder;
import com.vision.dao.CommonDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.DashboarUserDefVb;
import com.vision.vb.DashboardTilesVb;
import com.vision.vb.DashboardVb;
import com.vision.vb.ExportVb;
import com.vision.vb.ReportsVb;
import com.vision.vb.VisionUsersVb;
import com.vision.wb.DashboardsWb;
import com.vision.wb.VisionUploadWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("dashboards")
@Api(value="dashboards" , description="Dashboards")
public class DashboardsController {
	@Autowired
	DashboardsWb dashboardsWb;
	@Autowired
	VisionUploadWb visionUploadWb;
	@Autowired
	CommonDao commonDao;
	
	/*------------------------------------- DASHOARD ON LOAD------------------------------------------*/
	@RequestMapping(path = "/getDashboardOnLoad", method = RequestMethod.POST)
	@ApiOperation(value = "Load Dashboard Detail",notes = "Dashboard Detail and Tab detail",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardDetail(@RequestBody DashboardVb dObj){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			dObj.setActionType("Clear");
			exceptionCode = dashboardsWb.getDashboardDetail(dObj);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------- DASHOARD TAB DATA------------------------------------------*/
	@RequestMapping(path = "/getDashboardResultData", method = RequestMethod.POST)
	@ApiOperation(value = "Dashboard Result Data",notes = "Get Tab data by passing Tab Id and Dashboard Id and Tile Id",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardTabData(@RequestBody DashboardTilesVb dObj){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = dashboardsWb.getTileResultData(dObj);
//			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo(),exceptionCode.getRequest());
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getRequest(),exceptionCode.getResponse(),exceptionCode.getOtherInfo());
			
//			int status,String message, Object request, Object response, Object otherInfo
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------- DASHOARD TAB DRILL DOWN DATA------------------------------------------*/
	@RequestMapping(path = "/getDashboardDrillDownData", method = RequestMethod.POST)
	@ApiOperation(value = "Load Dashboard Detail",notes = "Get Tab data by passing Tab Id and Dashboard Id",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardDrillDownData(@RequestBody List<DashboardTilesVb> drillDownlst){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = dashboardsWb.getDrillDownData(drillDownlst);
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getRequest(), exceptionCode.getResponse(),
					exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------GET DASHBOARD LIST BY GROUP------------------------------------------*/
	@RequestMapping(path = "/getDashboardList", method = RequestMethod.GET)
	@ApiOperation(value = "Get Dasshboard List",notes = "Get List of Dashboard on Group wise",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardList(){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = dashboardsWb.getDashboardList();
			exceptionCode.setRequest(commonDao.getAllBusinessDate());
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(), exceptionCode.getResponse(),exceptionCode.getOtherInfo(),exceptionCode.getRequest());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------DASHBOARD EXCEL EXPORT------------------------------------------*/
	@RequestMapping(path = "/dashboardExcelExport", method = RequestMethod.POST)
	@ApiOperation(value = "Dasshboard Excel Export",notes = "dashboard and widget excel export",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> dashboardExcelExport(@RequestParam("reportTitle") String reportTitle,@RequestParam("promptLabel") String promptLabel,
			@RequestParam("exportImage") MultipartFile[] files,
			HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ReportsVb reportsVb = new ReportsVb();
		try{
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();
			reportsVb.setMaker(visionUsersVb.getVisionId());
			reportsVb.setMakerName(visionUsersVb.getUserName());
			reportsVb.setReportTitle(reportTitle);
			reportsVb.setPromptLabel(promptLabel);
			exceptionCode = dashboardsWb.exportToExcel(files,reportsVb);
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "xlsx");
				request.setAttribute("fileName", ""+exceptionCode.getOtherInfo()+".xlsx");
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
	/*------------------------------------ DASHBOARD PDF EXPORT-------------------------------*/
	@RequestMapping(path = "/dashboardPdfExport", method = RequestMethod.POST)
	@ApiOperation(value = "Dasshboard PDF Export",notes = "dashboard and Widget pdf export",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> dashboardPdfExport(@RequestParam("reportTitle") String reportTitle,@RequestParam("promptLabel") String promptLabel,
			@RequestParam("exportImage") MultipartFile[] files,
			HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		ReportsVb reportsVb = new ReportsVb();
		try{
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();
			reportsVb.setMaker(visionUsersVb.getVisionId());
			reportsVb.setMakerName(visionUsersVb.getUserName());
			reportsVb.setReportTitle(reportTitle);
			reportsVb.setPromptLabel(promptLabel);
			exceptionCode = dashboardsWb.exportToPdf(files,reportsVb);
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
	
	/*------------------------------------- DASHOARD GRID EXCEL EXPORT------------------------------------------*/
	@RequestMapping(path = "/dashboardExcelDataforGrid", method = RequestMethod.POST)
	@ApiOperation(value = "Dashboard Excel Data",notes = "Get Tab data by passing Tab Id and Dashboard Id and Tile Id",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardExcelData(@RequestBody DashboardTilesVb dObj,HttpServletRequest request,HttpServletResponse response){
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();
			DashboardVb dashboardVb =new DashboardVb();
			int currentUserId = visionUsersVb.getVisionId();
			dObj.setExportFlag(true);
			if(!ValidationUtil.isValid(dObj.getPromptLabel())) {
				if(ValidationUtil.isValid(dObj.getTileTable9())) {
					dObj.setPromptLabel("Table Name :  "+dObj.getTileTable9());
				}
			}
			ExceptionCode exceptionCode = dashboardsWb.exportToXls(dObj,dashboardVb,currentUserId,"0");
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "xlsx");
				request.setAttribute("fileName", ""+exceptionCode.getOtherInfo()+".xlsx");
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
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------ DASHBOARD GRID PDF EXPORT-------------------------------*/
	@RequestMapping(path = "/dashboardPdfExportforGrid", method = RequestMethod.POST)
	@ApiOperation(value = "Get PDF Report",notes = "Export Report Data to PDF",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardPdfData(@RequestBody DashboardTilesVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			vObject.setActionType("Query");
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();

			int currentUserId = visionUsersVb.getVisionId();
			if(!ValidationUtil.isValid(vObject.getPromptLabel())) {
				if(ValidationUtil.isValid(vObject.getTileTable9())) {
					vObject.setPromptLabel("Table Name :  "+vObject.getTileTable9());
				}
			}
			ExceptionCode exceptionCode1 = dashboardsWb.exportToPdf(currentUserId,vObject);
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
	/*-------------------------------------SAVE WIDGET------------------------------------------*/
	@RequestMapping(path = "/saveUserDefForDashboard", method = RequestMethod.POST)
	@ApiOperation(value = "save User Def For Dashboard",notes = "save User Def For Dashboard",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> saveUserDefForDashboard(@RequestBody DashboarUserDefVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = dashboardsWb.saveDashboardTheme(vObject) ;
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = dashboardsWb.saveDashboardUserSettings(vObject) ;
			}
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*-------------------------------------DELETE WIDGET------------------------------------------*/
	@RequestMapping(path = "/deleteSavedUserTheme", method = RequestMethod.POST)
	@ApiOperation(value = "deleteSavedUserTheme",notes = "deleteSavedUserTheme",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> deleteUserWidget(@RequestBody DashboarUserDefVb vObject){
		JSONExceptionCode jsonExceptionCode  = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			exceptionCode = dashboardsWb.deleteDashboardTheme(vObject) ;
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				exceptionCode = dashboardsWb.deleteDashboardUserSettings(vObject,true) ;
			}
			jsonExceptionCode = new JSONExceptionCode(exceptionCode.getErrorCode(),exceptionCode.getErrorMsg(),exceptionCode.getOtherInfo());
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}catch(RuntimeCustomException rex){
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.METHOD_FAILURE);
		}	
	}
	/*------------------------------------ DASHBOARD PDF EXPORT-------------------------------*/
	@RequestMapping(path = "/dashboardExport", method = RequestMethod.POST)
	@ApiOperation(value = "Get Dashboard Report",notes = "Export Dashboard to PDF",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getDashboardExport(@RequestParam("dashboardId") String dashboardId,@RequestParam("dashboardName") String dashboardName,
			@RequestParam("applicationTheme") String applicationTheme,@RequestParam("promptLabel") String promptLabel,@RequestParam("exportTabDetailsLst") String exportTabDetailsLst,
			HttpServletRequest request,HttpServletResponse response,@RequestParam("exportImage") String exportImage) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			ArrayList<ExportVb> tabsLst = new ArrayList();
			JSONArray array = new JSONArray(exportTabDetailsLst);
			for(int i=0; i < array.length(); i++) 
			{  
				JSONObject tabDetails = array.getJSONObject(i);
				try {
					ExportVb dObj = new ObjectMapper().readValue(tabDetails.toString(), ExportVb.class);
					tabsLst.add(dObj);	
				}catch(Exception e) {
					e.printStackTrace();
				}
			} 
			ExportVb vObject = new ExportVb();
			vObject.setDashboardId(dashboardId);
			vObject.setDashboardName(dashboardName);
			vObject.setApplicationTheme(applicationTheme);
			vObject.setExportTabDetailsLst(tabsLst);
			vObject.setPromptLabel(promptLabel);
			vObject.setExportImage(exportImage);
			
			
			
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();

			int currentUserId = visionUsersVb.getVisionId();
			ExceptionCode exceptionCode1 = dashboardsWb.dashboardExport(currentUserId,vObject);
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "pdf");
				request.setAttribute("fileName", ""+exceptionCode1.getOtherInfo()+".pdf");
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Dashboard is unable to Export.Contact System Admin!!", null);
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
	/*------------------------------------ DASHBOARD EXCEL EXPORT FOR ALL TILES-------------------------------*/
	@RequestMapping(path = "/dashboardExcelExportforAllTiles", method = RequestMethod.POST)
	@ApiOperation(value = "Get Dashboard Excel Export",notes = "Export Dashboard to Excel",response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> dashboardExcelExport(@RequestBody ExportVb vObject,HttpServletRequest request,HttpServletResponse response) {
		JSONExceptionCode jsonExceptionCode  = null;
		try{
			VisionUsersVb visionUsersVb =CustomContextHolder.getContext();

			int currentUserId = visionUsersVb.getVisionId();
			ExceptionCode exceptionCode1 = dashboardsWb.dashboardExportForExcel(vObject,currentUserId);
			if(exceptionCode1.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				request.setAttribute("fileExtension", "pdf");
				request.setAttribute("fileName", ""+exceptionCode1.getOtherInfo()+".pdf");
				request.setAttribute("filePath", exceptionCode1.getResponse());
				visionUploadWb.setExportXlsServlet(request, response);
				if(response.getStatus() == 404) {
					jsonExceptionCode =	new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, "Dashboard is unable to Export.Contact System Admin!!", null);
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
}
