package com.vision.vb;

public class FTPSourceConfigVb extends CommonVb{
	private String	country = "";
	private String	leBook = "";
	private int	sourceReferenceAt =  0;
	private String	sourceReference =  "";
	private int	sequence = 0;
	private String tableName = "";
	private String colName = "";
	private String operand= "-1";
	private String conditionValue1 = "";
	private String conditionValue2 = "";
	private int	ftpSourceConfigStatusNt =  0;
	private int	ftpSourceConfigStatus =  -1;
	private String dailyHint = "";
	private String monthlyHint = "";
	private String yearlyHint = "";
	private int	frquencyTuningNt =  0;
	private int	frquencyTuning =  1;
	
	public int getFrquencyTuningNt() {
		return frquencyTuningNt;
	}
	public void setFrquencyTuningNt(int frquencyTuningNt) {
		this.frquencyTuningNt = frquencyTuningNt;
	}
	public int getFrquencyTuning() {
		return frquencyTuning;
	}
	public void setFrquencyTuning(int frquencyTuning) {
		this.frquencyTuning = frquencyTuning;
	}
	public String getDailyHint() {
		return dailyHint;
	}
	public void setDailyHint(String dailyHint) {
		this.dailyHint = dailyHint;
	}
	public String getMonthlyHint() {
		return monthlyHint;
	}
	public void setMonthlyHint(String monthlyHint) {
		this.monthlyHint = monthlyHint;
	}
	public String getYearlyHint() {
		return yearlyHint;
	}
	public void setYearlyHint(String yearlyHint) {
		this.yearlyHint = yearlyHint;
	}
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
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getOperand() {
		return operand;
	}
	public void setOperand(String operand) {
		this.operand = operand;
	}
	public String getConditionValue1() {
		return conditionValue1;
	}
	public void setConditionValue1(String conditionValue1) {
		this.conditionValue1 = conditionValue1;
	}
	public String getConditionValue2() {
		return conditionValue2;
	}
	public void setConditionValue2(String conditionValue2) {
		this.conditionValue2 = conditionValue2;
	}
	public int getFtpSourceConfigStatusNt() {
		return ftpSourceConfigStatusNt;
	}
	public void setFtpSourceConfigStatusNt(int ftpSourceConfigStatusNt) {
		this.ftpSourceConfigStatusNt = ftpSourceConfigStatusNt;
	}
	public int getFtpSourceConfigStatus() {
		return ftpSourceConfigStatus;
	}
	public void setFtpSourceConfigStatus(int ftpSourceConfigStatus) {
		this.ftpSourceConfigStatus = ftpSourceConfigStatus;
	}
}
