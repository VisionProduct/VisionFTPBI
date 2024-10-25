package com.vision.exception;

public class JSONExceptionCode {
 
	private int status = 0;
	private String message = null;
	private Object response = null;
	private Object response1 = null;
	private Object request = null;
	private Object otherInfo = null;
	
	public JSONExceptionCode() {}
	
	public JSONExceptionCode(int status,String message, Object response, Object otherInfo) {
		this.status = status;
		this.message = message;
		this.response = response;
		this.otherInfo = otherInfo;
	}
	public JSONExceptionCode(int status,String message, Object request, Object response, Object otherInfo) {
		this.status = status;
		this.message = message;
		this.response = response;
		this.request = request;
		this.otherInfo = otherInfo;
	}
	public JSONExceptionCode(int status,String message, Object response) {
		this.status = status;
		this.message = message;
		this.response = response;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}

	public Object getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(Object otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Object getRequest() {
		return request;
	}

	public void setRequest(Object request) {
		this.request = request;
	}

	public Object getResponse1() {
		return response1;
	}

	public void setResponse1(Object response1) {
		this.response1 = response1;
	}
		
		 
}