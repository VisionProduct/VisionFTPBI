package com.vision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vision.authentication.CustomContextHolder;
import com.vision.util.ValidationUtil;
import com.vision.vb.VisionUsersVb;
import com.vision.wb.LoginUserServices;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("applicationLoadController")
@Api(value = "applicationLoadController", description = "Controller that provides details for loading application")
public class ApplicationLoadController {
	
	@Autowired
	private LoginUserServices loginUsersServices;
	@Value("${app.clinetId}")
	private String clinetId;
	
	@GetMapping("/getAppLoadValues")
	public ResponseEntity<VisionUsersVb> getAppLoadValues() {
		try {
			VisionUsersVb contextVb = CustomContextHolder.getContext();
			if(!ValidationUtil.isValid(contextVb.getClientId()))
				contextVb.setClientId(clinetId);
			return ResponseEntity.ok().body(contextVb);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
