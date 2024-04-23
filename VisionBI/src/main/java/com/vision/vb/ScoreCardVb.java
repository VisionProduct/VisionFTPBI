package com.vision.vb;

/**
 * @author Prabu.CJ
 *
 */
import java.util.ArrayList;
import java.util.List;

public class ScoreCardVb extends CommonVb {
	private int parentSequence = 0;
	private String separator = "-1";
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";

	private String country              = "";       
	private String leBook               = "";
	private String scardId              = "";
	private String scardName            = "";
	private String scardDesc            = "";
	private String tableName            = "";
	private int scardStatusNt           =  1;
	private int scardStatus             = -1;
	private String columnId             = "";
	private String columnName           = "";
	private int scoreTypeNt             =  1;
	private int scoreType               = -1;
	private String threshHigh           = "";
	private String threshLow            = "";
	private String scardVersionCurr     = "";
	private int columnStatusNt          =  1;
	private int columnStatus            = -1;
	private String colValue             = "";
	private String ruleAlphaFlag        = "";
	private String ruleAlphaException   = "";
	private String ruleAlphaCaseAt      = "";
	private String ruleAlphaCase        = "";
	private String ruleNumFlag          = "";
	private String ruleNumException     = "";
	private String ruleSplFlag          = "";
	private String ruleSplAt            = "";
	private String ruleSpl              = "";
	private String ruleSplChar          = "";
	private String ruleLenMin           = "";
	private String ruleLenMax           = "";
	private String ruleDateFlag         = "";
	private int ruleDateRhsTypeNt       =  1;
	private int ruleDateRhsType         = -1;
	private String ruleDateOperatorAt   = "";
	private String ruleDateOperator     = "";
	private String ruleDateRhsValue     = "";
	private String ruleOthFlag          = "";
	private String ruleOthBlindrep      = "";
	private String ruleOthIncrep        = "";
	private String ruleOthExpression    = "";
	private int sCardTypeNt       =  1;
	private int sCardType         = -1;
	private String columnValue    = "";
	private String tempTableName            = "";
	private ArrayList<ScoreCardVb> patternTableschild = null;
	private ArrayList<ScoreCardVb> patternDatalist = null;
	private ArrayList<ScoreCardVb> columnListTablechild = null;
	private String status = "";
	private String errorMessage = "";
	private String tableId = "";
	private String tempcolumnValue;


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

	public int getParentSequence() {
		return parentSequence;
	}

	public void setParentSequence(int parentSequence) {
		this.parentSequence = parentSequence;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getApplicationAccessAt() {
		return applicationAccessAt;
	}

	public void setApplicationAccessAt(String applicationAccessAt) {
		this.applicationAccessAt = applicationAccessAt;
	}

	public String getApplicationAccess() {
		return applicationAccess;
	}

	public void setApplicationAccess(String applicationAccess) {
		this.applicationAccess = applicationAccess;
	}

	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	
	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getRuleAlphaFlag() {
		return ruleAlphaFlag;
	}

	public void setRuleAlphaFlag(String ruleAlphaFlag) {
		this.ruleAlphaFlag = ruleAlphaFlag;
	}

	public String getRuleAlphaException() {
		return ruleAlphaException;
	}

	public void setRuleAlphaException(String ruleAlphaException) {
		this.ruleAlphaException = ruleAlphaException;
	}

	public String getRuleAlphaCaseAt() {
		return ruleAlphaCaseAt;
	}

	public void setRuleAlphaCaseAt(String ruleAlphaCaseAt) {
		this.ruleAlphaCaseAt = ruleAlphaCaseAt;
	}

	public String getRuleAlphaCase() {
		return ruleAlphaCase;
	}

	public void setRuleAlphaCase(String ruleAlphaCase) {
		this.ruleAlphaCase = ruleAlphaCase;
	}

	public String getRuleNumFlag() {
		return ruleNumFlag;
	}

	public void setRuleNumFlag(String ruleNumFlag) {
		this.ruleNumFlag = ruleNumFlag;
	}

	public String getRuleNumException() {
		return ruleNumException;
	}

	public void setRuleNumException(String ruleNumException) {
		this.ruleNumException = ruleNumException;
	}

	public String getRuleSplFlag() {
		return ruleSplFlag;
	}

	public void setRuleSplFlag(String ruleSplFlag) {
		this.ruleSplFlag = ruleSplFlag;
	}

	public String getRuleSplAt() {
		return ruleSplAt;
	}

	public void setRuleSplAt(String ruleSplAt) {
		this.ruleSplAt = ruleSplAt;
	}

	public String getRuleSpl() {
		return ruleSpl;
	}

	public void setRuleSpl(String ruleSpl) {
		this.ruleSpl = ruleSpl;
	}

	public String getRuleSplChar() {
		return ruleSplChar;
	}

	public void setRuleSplChar(String ruleSplChar) {
		this.ruleSplChar = ruleSplChar;
	}
	
	public String getRuleDateFlag() {
		return ruleDateFlag;
	}

	public void setRuleDateFlag(String ruleDateFlag) {
		this.ruleDateFlag = ruleDateFlag;
	}

	public String getRuleDateOperatorAt() {
		return ruleDateOperatorAt;
	}

	public void setRuleDateOperatorAt(String ruleDateOperatorAt) {
		this.ruleDateOperatorAt = ruleDateOperatorAt;
	}

	public String getRuleDateOperator() {
		return ruleDateOperator;
	}

	public void setRuleDateOperator(String ruleDateOperator) {
		this.ruleDateOperator = ruleDateOperator;
	}

	public String getScardId() {
		return scardId;
	}

	public void setScardId(String scardId) {
		this.scardId = scardId;
	}

	public String getScardName() {
		return scardName;
	}

	public void setScardName(String scardName) {
		this.scardName = scardName;
	}

	public String getScardDesc() {
		return scardDesc;
	}

	public void setScardDesc(String scardDesc) {
		this.scardDesc = scardDesc;
	}

	public int getScardStatusNt() {
		return scardStatusNt;
	}

	public void setScardStatusNt(int scardStatusNt) {
		this.scardStatusNt = scardStatusNt;
	}

	public int getScardStatus() {
		return scardStatus;
	}

	public void setScardStatus(int scardStatus) {
		this.scardStatus = scardStatus;
	}

	public int getScoreTypeNt() {
		return scoreTypeNt;
	}

	public void setScoreTypeNt(int scoreTypeNt) {
		this.scoreTypeNt = scoreTypeNt;
	}

	public int getScoreType() {
		return scoreType;
	}

	public void setScoreType(int scoreType) {
		this.scoreType = scoreType;
	}

	public String getThreshHigh() {
		return threshHigh;
	}

	public void setThreshHigh(String threshHigh) {
		this.threshHigh = threshHigh;
	}

	public String getThreshLow() {
		return threshLow;
	}

	public void setThreshLow(String threshLow) {
		this.threshLow = threshLow;
	}

	public String getScardVersionCurr() {
		return scardVersionCurr;
	}

	public void setScardVersionCurr(String scardVersionCurr) {
		this.scardVersionCurr = scardVersionCurr;
	}

	public int getColumnStatusNt() {
		return columnStatusNt;
	}

	public void setColumnStatusNt(int columnStatusNt) {
		this.columnStatusNt = columnStatusNt;
	}

	public int getColumnStatus() {
		return columnStatus;
	}

	public void setColumnStatus(int columnStatus) {
		this.columnStatus = columnStatus;
	}

	public String getColValue() {
		return colValue;
	}

	public void setColValue(String colValue) {
		this.colValue = colValue;
	}

	public String getRuleLenMin() {
		return ruleLenMin;
	}

	public void setRuleLenMin(String ruleLenMin) {
		this.ruleLenMin = ruleLenMin;
	}

	public String getRuleLenMax() {
		return ruleLenMax;
	}

	public void setRuleLenMax(String ruleLenMax) {
		this.ruleLenMax = ruleLenMax;
	}

	public int getRuleDateRhsTypeNt() {
		return ruleDateRhsTypeNt;
	}

	public void setRuleDateRhsTypeNt(int ruleDateRhsTypeNt) {
		this.ruleDateRhsTypeNt = ruleDateRhsTypeNt;
	}

	public int getRuleDateRhsType() {
		return ruleDateRhsType;
	}

	public void setRuleDateRhsType(int ruleDateRhsType) {
		this.ruleDateRhsType = ruleDateRhsType;
	}

	public String getRuleDateRhsValue() {
		return ruleDateRhsValue;
	}

	public void setRuleDateRhsValue(String ruleDateRhsValue) {
		this.ruleDateRhsValue = ruleDateRhsValue;
	}

	public String getRuleOthFlag() {
		return ruleOthFlag;
	}

	public void setRuleOthFlag(String ruleOthFlag) {
		this.ruleOthFlag = ruleOthFlag;
	}

	public String getRuleOthBlindrep() {
		return ruleOthBlindrep;
	}

	public void setRuleOthBlindrep(String ruleOthBlindrep) {
		this.ruleOthBlindrep = ruleOthBlindrep;
	}

	public String getRuleOthIncrep() {
		return ruleOthIncrep;
	}

	public void setRuleOthIncrep(String ruleOthIncrep) {
		this.ruleOthIncrep = ruleOthIncrep;
	}

	public String getRuleOthExpression() {
		return ruleOthExpression;
	}

	public void setRuleOthExpression(String ruleOthExpression) {
		this.ruleOthExpression = ruleOthExpression;
	}

	public ArrayList<ScoreCardVb> getPatternTableschild() {
		return patternTableschild;
	}

	public void setPatternTableschild(ArrayList<ScoreCardVb> patternTableschild) {
		this.patternTableschild = patternTableschild;
	}

	

	public ArrayList<ScoreCardVb> getColumnListTablechild() {
		return columnListTablechild;
	}

	public void setColumnListTablechild(ArrayList<ScoreCardVb> columnListTablechild) {
		this.columnListTablechild = columnListTablechild;
	}

	public int getsCardTypeNt() {
		return sCardTypeNt;
	}

	public void setsCardTypeNt(int sCardTypeNt) {
		this.sCardTypeNt = sCardTypeNt;
	}

	public int getsCardType() {
		return sCardType;
	}

	public void setsCardType(int sCardType) {
		this.sCardType = sCardType;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	public String getTempTableName() {
		return tempTableName;
	}

	public void setTempTableName(String tempTableName) {
		this.tempTableName = tempTableName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTempcolumnValue() {
		return tempcolumnValue;
	}

	public void setTempcolumnValue(String tempcolumnValue) {
		this.tempcolumnValue = tempcolumnValue;
	}

	public ArrayList<ScoreCardVb> getPatternDatalist() {
		return patternDatalist;
	}

	public void setPatternDatalist(ArrayList<ScoreCardVb> patternDatalist) {
		this.patternDatalist = patternDatalist;
	}


	
}