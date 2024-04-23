package com.vision.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vision.dao.CommonApiDao;
import com.vision.dao.CommonDao;
import com.vision.dao.ReportsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.JSONExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.ColumnHeadersVb;
import com.vision.vb.CommonApiModel;
import com.vision.vb.ReportsVb;
import com.vision.vb.VisionUsersVb;
import com.vision.wb.LoginServicesWb;
import com.vision.wb.ReportsWb;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "menuAndUserDetails")
@Api(value = "menuAndUserDetails", description = "All services that is needed during login")
public class LoginServicesController {
	
	@Autowired
	LoginServicesWb loginServicesWb;
	
	@Value("${app.productName}")
	public String productName;
	
	@Autowired
	CommonApiDao commonApiDao;
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	ReportsWb reportswb;
	
	@Autowired
	ReportsDao reportsDao;
	
	@RequestMapping(path = "/getMenuDetails", method = RequestMethod.GET)
	@ApiOperation(value = "Menu Details", notes = "Get menu details of the application based on current user", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getMenuDetails() {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			LinkedHashMap<String, Object> menuMap = loginServicesWb.getMenuForUser();
			/* Vision Business day - Code start */
			CommonApiModel vObject = new CommonApiModel();
			// Vision business day query
			vObject.setQueryId("PRDVSVBDATE");
			vObject.setParam1(productName);
			ExceptionCode exceptionCode = commonApiDao.getCommonResultDataFetch(vObject);
			ArrayList vbdList = (ArrayList) exceptionCode.getResponse();
			String showOptionValue = commonDao.findVisionVariableValue("PRD_SHOW_CALENDAR");
			// showOptionValue - Values (DEFAULT and CALENDAR)
			if(!ValidationUtil.isValid(showOptionValue))
				showOptionValue = "DEFAULT";
			String columnHeaderXml = reportsDao.getColumnXmlWithReportId("PRDBUSDET");
			ReportsVb reportsVb = new ReportsVb();
			ArrayList<ColumnHeadersVb> colHeaders = reportsDao.getColumnHeaders(columnHeaderXml,reportsVb);
			if (vbdList != null && vbdList.size() > 0) {
				menuMap.put("showOption", showOptionValue);
				menuMap.put("vbdList", vbdList);
				menuMap.put("vbdColumnHeaderList", colHeaders);
			}
			/* Vision Business day - Code End */
			String idleTimeOut = commonDao.findVisionVariableValue("VS_IDLE_TIMEOUT");
			if(!ValidationUtil.isValid(idleTimeOut))
				idleTimeOut = "120";
			menuMap.put("idleTimeOut", idleTimeOut);
			String idleElapsedTime = commonDao.findVisionVariableValue("VS_IDLE_ELAPSED_TIME");
			if(!ValidationUtil.isValid(idleElapsedTime))
				idleElapsedTime = "10";
			menuMap.put("idleElapsedTime", idleElapsedTime);
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "Menu Details", menuMap, null);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
	@RequestMapping(path = "/getUserDetails", method = RequestMethod.GET)
	//@ApiOperation(value = "User Details", notes = "Get detailed information about current user corresponding to the product", response = ResponseEntity.class)
	public ResponseEntity<JSONExceptionCode> getUserDetails() {
		JSONExceptionCode jsonExceptionCode = null;
		try {
			VisionUsersVb userVb = loginServicesWb.getUserDetails();
			jsonExceptionCode = new JSONExceptionCode(Constants.SUCCESSFUL_OPERATION, "User Detail", userVb, null);
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		} catch (RuntimeCustomException rex) {
			jsonExceptionCode = new JSONExceptionCode(Constants.ERRONEOUS_OPERATION, rex.getMessage(), "");
			return new ResponseEntity<JSONExceptionCode>(jsonExceptionCode, HttpStatus.OK);
		}
	}
	
}
