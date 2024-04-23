package com.vision.vb;

public class CbWordDocumentStgVb extends WordStylesVb{
	private String reportId = "";
	private String sessionId = "";
	private int textOrder = 0;
	private int textTypeAt = 3001;
	private String textType = "";
	private String textContent = "";
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getTextOrder() {
		return textOrder;
	}
	public void setTextOrder(int textOrder) {
		this.textOrder = textOrder;
	}
	public int getTextTypeAt() {
		return textTypeAt;
	}
	public void setTextTypeAt(int textTypeAt) {
		this.textTypeAt = textTypeAt;
	}
	public String getTextType() {
		return textType;
	}
	public void setTextType(String textType) {
		this.textType = textType;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
}
