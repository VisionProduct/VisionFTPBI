package com.vision.vb;

public class ErrorCodesVb extends CommonVb{

	private static final long serialVersionUID = 1410935578921368062L;
	private String errorCode = "";
	private String errorDescription = "";
	private int errorTypeNt = 0;
	private int errorType = -1;
	private int errorStatusNt = 0;
	private int errorStatus = -1;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public int getErrorTypeNt() {
		return errorTypeNt;
	}
	public void setErrorTypeNt(int errorTypeNt) {
		this.errorTypeNt = errorTypeNt;
	}
	public int getErrorType() {
		return errorType;
	}
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	public int getErrorStatusNt() {
		return errorStatusNt;
	}
	public void setErrorStatusNt(int errorStatusNt) {
		this.errorStatusNt = errorStatusNt;
	}
	public int getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(int errorStatus) {
		this.errorStatus = errorStatus;
	}
}
