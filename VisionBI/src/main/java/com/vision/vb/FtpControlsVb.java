package com.vision.vb;

public class FtpControlsVb extends CommonVb {

	private static final long serialVersionUID = 2073522091526813534L;
	
	private String country = "";
	private String leBook = "";
	private String methodReference = "";
	private String ftpDescription = "";
	private String ftpReference ="";
	private int StatusNt = 1;
	private int Status = -1;
	private int sourceReferenceAt=1302;
	private String sourceReference="-1";
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLeBook() {
		return leBook;
	}
	public void setLeBook(String leBook) {
		this.leBook = leBook;
	}
	public String getMethodReference() {
		return methodReference;
	}
	public void setMethodReference(String methodReference) {
		this.methodReference = methodReference;
	}
	public String getFtpDescription() {
		return ftpDescription;
	}
	public void setFtpDescription(String ftpDescription) {
		this.ftpDescription = ftpDescription;
	}
	public String getFtpReference() {
		return ftpReference;
	}
	public void setFtpReference(String ftpReference) {
		this.ftpReference = ftpReference;
	}
	public int getStatusNt() {
		return StatusNt;
	}
	public void setStatusNt(int statusNt) {
		StatusNt = statusNt;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getSourceReferenceAt() {
		return sourceReferenceAt;
	}
	public void setSourceReferenceAt(int sourceReferenceAt) {
		this.sourceReferenceAt = sourceReferenceAt;
	}
	public String getSourceReference() {
		return sourceReference;
	}
	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}
}
