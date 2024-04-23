package com.vision.vb;

/**
 * @author Prabu.CJ
 *
 */
import java.util.ArrayList;
import java.util.List;

public class DataProfilerVb extends CommonVb {
	private int parentSequence = 0;
	private String separator = "-1";
	private String applicationAccessAt = "5000";
	private String applicationAccess = "";
	private String country = "";
	private String leBook = "";

	private String profilerId = "";
	private int profilerConfigAt = 806;
	private String profilerConfig = "";
	private String profilerName = "";
	private String profilerDescription = "";
	private String tableName = "";
	private String jsonScript = "";
	private String queryCondition = "";
	private String columnNameList = "";
	private String staticIdList = "";
	private int profilerStatusNt = 1;
	private int profilerStatus = -1;

	private String columnId = "";
	private String columnName = "";
	private int ruleAlphaFlag = 9;
	private int ruleAlphaCaseAt = 807;
	private String ruleAlphaCase = "";
	private int ruleAlphaAt = 810;
	private String ruleAlpha = "";
	private String ruleAlphaChar = "";
	private int ruleNumFlag = 9;
	private int ruleNumAt = 810;
	private String ruleNum = "";
	private String ruleNumChar ="";

	private String connectorId = "";
	private int ruleSplFlag = 9;
	private String ruleSplAt = "";
	private String ruleSpl = "";
	private String ruleSplChar = "";
	private String ruleLenthMin = "";
	private String ruleLenthMax = "";
	private String ruleDateFlag = "";
	private int ruleDateRHSTypeNt = 1;
	private int ruleDateRHSType = -1;
	private String ruleDateOperatorAt = "";
	private String ruleDateOperator = "";
	private String ruleDateRHSValue = "";
	private String ruleDateFormat = "";
	private int ruleOTHFlag = 0;
	private int ruleOTHBlindRep = 0;
	private int ruleOTHIncRep = 0;
	private String ruleOTHExpression = "";
	private int columnRuleStatusNt = 1;
	private int columnRuleStatus = -1;
	private String staticId = "";

	// For Profiler Dashoards
	private String status = "";
	private String errorMessage = "";
	private String columnDataType = "";
	private String domainCount = "";
	private String nonNullCount = "";
	private String nullCount = "";
	private String duplicateCount = "";
	private String distinctCount = "";
	private String nonUniqueCount = "";
	private String uniqueCount = "";
	private String patternsCount = "";
	private String minLengthCount = "";
	private String medianLengthCount = "";
	private String maxLenghtCount = "";
	private String completenessPercent = "";
	private String uniquenessPercent = "";
	private String att_flag;
	private String cus_flag;
	private String tableId;
	private String ruleId;
	private String ruleResultValue;
	private String ruleResultPercent;
	private String percentage;
	private String count;
	private String commissionPercentage;
	private String pin;
	private String pinCount;
	private String ruleName;
	private List<DataProfilerVb> rulelstR110 = null;
	private List<DataProfilerVb> rulelstR120 = null;
	private List<DataProfilerVb> rulelstR130 = null;
	private List<DataProfilerVb> rulelstR140 = null;
	private List<DataProfilerVb> rulelstR150 = null;
	private List<DataProfilerVb> rulelstR160 = null;
	private List<DataProfilerVb> rulelstR170 = null;
	private List<DataProfilerVb> rulelstR180 = null;
	private List<DataProfilerVb> rulelstR190 = null;
	private List<DataProfilerVb> rulelstR200 = null;
	private List<DataProfilerVb> rulelstR220 = null;
	private List<DataProfilerVb> rulelstR310 = null;
	private List<DataProfilerVb> rulelstR320 = null;
	private ArrayList<DataProfilerVb> children = null;

	private String column_names;
	private String criteria;
	private String inputvalue;
	private String condition;
	private ArrayList<SmartSearchVb> searchTableschild = null;

	private int prfAttrunNt = 330;
	private int prfAttrun = 2;
	private String prfAttRunStatusDesc = "";

	private int prfCusrun = 2;
	private int prfCusrunNt = 330;
	private String prfCusRunStatusDesc = "";

	private String prfType = "";
	private String ruleNumException = "";
	private String ruleAlphaException = "";
	private String ruleDdId = "";
	private int processCount = 0;

	public int getRuleAlphaAt() {
		return ruleAlphaAt;
	}

	public void setRuleAlphaAt(int ruleAlphaAt) {
		this.ruleAlphaAt = ruleAlphaAt;
	}

	public String getRuleAlpha() {
		return ruleAlpha;
	}

	public void setRuleAlpha(String ruleAlpha) {
		this.ruleAlpha = ruleAlpha;
	}


	public int getRuleNumAt() {
		return ruleNumAt;
	}

	public void setRuleNumAt(int ruleNumAt) {
		this.ruleNumAt = ruleNumAt;
	}

	public String getRuleNum() {
		return ruleNum;
	}

	public void setRuleNum(String ruleNum) {
		this.ruleNum = ruleNum;
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

	public ArrayList<DataProfilerVb> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<DataProfilerVb> children) {
		this.children = children;
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

	public String getProfilerId() {
		return profilerId;
	}

	public void setProfilerId(String profilerId) {
		this.profilerId = profilerId;
	}

	public String getProfilerConfig() {
		return profilerConfig;
	}

	public void setProfilerConfig(String profilerConfig) {
		this.profilerConfig = profilerConfig;
	}

	public String getProfilerName() {
		return profilerName;
	}

	public void setProfilerName(String profilerName) {
		this.profilerName = profilerName;
	}

	public String getProfilerDescription() {
		return profilerDescription;
	}

	public void setProfilerDescription(String profilerDescription) {
		this.profilerDescription = profilerDescription;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getJsonScript() {
		return jsonScript;
	}

	public void setJsonScript(String jsonScript) {
		this.jsonScript = jsonScript;
	}

	public int getPrfAttrunNt() {
		return prfAttrunNt;
	}

	public void setPrfAttrunNt(int prfAttrunNt) {
		this.prfAttrunNt = prfAttrunNt;
	}

	public int getPrfAttrun() {
		return prfAttrun;
	}

	public void setPrfAttrun(int prfAttrun) {
		this.prfAttrun = prfAttrun;
	}

	public int getPrfCusrun() {
		return prfCusrun;
	}

	public void setPrfCusrun(int prfCusrun) {
		this.prfCusrun = prfCusrun;
	}

	public String getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}

	public String getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(String columnNameList) {
		this.columnNameList = columnNameList;
	}

	public String getStaticIdList() {
		return staticIdList;
	}

	public void setStaticIdList(String staticIdList) {
		this.staticIdList = staticIdList;
	}

	public int getProfilerStatusNt() {
		return profilerStatusNt;
	}

	public void setProfilerStatusNt(int profilerStatusNt) {
		this.profilerStatusNt = profilerStatusNt;
	}

	public int getProfilerStatus() {
		return profilerStatus;
	}

	public void setProfilerStatus(int profilerStatus) {
		this.profilerStatus = profilerStatus;
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


	public String getRuleAlphaCase() {
		return ruleAlphaCase;
	}

	public void setRuleAlphaCase(String ruleAlphaCase) {
		this.ruleAlphaCase = ruleAlphaCase;
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


	public String getRuleLenthMin() {
		return ruleLenthMin;
	}

	public void setRuleLenthMin(String ruleLenthMin) {
		this.ruleLenthMin = ruleLenthMin;
	}

	public String getRuleLenthMax() {
		return ruleLenthMax;
	}

	public void setRuleLenthMax(String ruleLenthMax) {
		this.ruleLenthMax = ruleLenthMax;
	}

	public String getRuleDateFlag() {
		return ruleDateFlag;
	}

	public void setRuleDateFlag(String ruleDateFlag) {
		this.ruleDateFlag = ruleDateFlag;
	}

	public int getRuleDateRHSTypeNt() {
		return ruleDateRHSTypeNt;
	}

	public void setRuleDateRHSTypeNt(int ruleDateRHSTypeNt) {
		this.ruleDateRHSTypeNt = ruleDateRHSTypeNt;
	}

	public int getRuleDateRHSType() {
		return ruleDateRHSType;
	}

	public void setRuleDateRHSType(int ruleDateRHSType) {
		this.ruleDateRHSType = ruleDateRHSType;
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

	public String getRuleDateRHSValue() {
		return ruleDateRHSValue;
	}

	public void setRuleDateRHSValue(String ruleDateRHSValue) {
		this.ruleDateRHSValue = ruleDateRHSValue;
	}

	public String getRuleOTHExpression() {
		return ruleOTHExpression;
	}

	public void setRuleOTHExpression(String ruleOTHExpression) {
		this.ruleOTHExpression = ruleOTHExpression;
	}

	public int getColumnRuleStatusNt() {
		return columnRuleStatusNt;
	}

	public void setColumnRuleStatusNt(int columnRuleStatusNt) {
		this.columnRuleStatusNt = columnRuleStatusNt;
	}

	public int getColumnRuleStatus() {
		return columnRuleStatus;
	}

	public void setColumnRuleStatus(int columnRuleStatus) {
		this.columnRuleStatus = columnRuleStatus;
	}

	public String getStaticId() {
		return staticId;
	}

	public void setStaticId(String staticId) {
		this.staticId = staticId;
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

	public String getColumnDataType() {
		return columnDataType;
	}

	public void setColumnDataType(String columnDataType) {
		this.columnDataType = columnDataType;
	}

	public String getDomainCount() {
		return domainCount;
	}

	public void setDomainCount(String domainCount) {
		this.domainCount = domainCount;
	}

	public String getNonNullCount() {
		return nonNullCount;
	}

	public void setNonNullCount(String nonNullCount) {
		this.nonNullCount = nonNullCount;
	}

	public String getNullCount() {
		return nullCount;
	}

	public void setNullCount(String nullCount) {
		this.nullCount = nullCount;
	}

	public String getDuplicateCount() {
		return duplicateCount;
	}

	public void setDuplicateCount(String duplicateCount) {
		this.duplicateCount = duplicateCount;
	}

	public String getDistinctCount() {
		return distinctCount;
	}

	public void setDistinctCount(String distinctCount) {
		this.distinctCount = distinctCount;
	}

	public String getNonUniqueCount() {
		return nonUniqueCount;
	}

	public void setNonUniqueCount(String nonUniqueCount) {
		this.nonUniqueCount = nonUniqueCount;
	}

	public String getUniqueCount() {
		return uniqueCount;
	}

	public void setUniqueCount(String uniqueCount) {
		this.uniqueCount = uniqueCount;
	}

	public String getPatternsCount() {
		return patternsCount;
	}

	public void setPatternsCount(String patternsCount) {
		this.patternsCount = patternsCount;
	}

	public String getMinLengthCount() {
		return minLengthCount;
	}

	public void setMinLengthCount(String minLengthCount) {
		this.minLengthCount = minLengthCount;
	}

	public String getMedianLengthCount() {
		return medianLengthCount;
	}

	public void setMedianLengthCount(String medianLengthCount) {
		this.medianLengthCount = medianLengthCount;
	}

	public String getMaxLenghtCount() {
		return maxLenghtCount;
	}

	public void setMaxLenghtCount(String maxLenghtCount) {
		this.maxLenghtCount = maxLenghtCount;
	}

	public String getCompletenessPercent() {
		return completenessPercent;
	}

	public void setCompletenessPercent(String completenessPercent) {
		this.completenessPercent = completenessPercent;
	}

	public String getUniquenessPercent() {
		return uniquenessPercent;
	}

	public void setUniquenessPercent(String uniquenessPercent) {
		this.uniquenessPercent = uniquenessPercent;
	}

	public String getAtt_flag() {
		return att_flag;
	}

	public void setAtt_flag(String att_flag) {
		this.att_flag = att_flag;
	}

	public String getCus_flag() {
		return cus_flag;
	}

	public void setCus_flag(String cus_flag) {
		this.cus_flag = cus_flag;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public String getRuleResultValue() {
		return ruleResultValue;
	}

	public String getRuleResultPercent() {
		return ruleResultPercent;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public void setRuleResultValue(String ruleResultValue) {
		this.ruleResultValue = ruleResultValue;
	}

	public void setRuleResultPercent(String ruleResultPercent) {
		this.ruleResultPercent = ruleResultPercent;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCommissionPercentage() {
		return commissionPercentage;
	}

	public void setCommissionPercentage(String commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPinCount() {
		return pinCount;
	}

	public void setPinCount(String pinCount) {
		this.pinCount = pinCount;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public List<DataProfilerVb> getRulelstR110() {
		return rulelstR110;
	}

	public void setRulelstR110(List<DataProfilerVb> rulelstR110) {
		this.rulelstR110 = rulelstR110;
	}

	public List<DataProfilerVb> getRulelstR120() {
		return rulelstR120;
	}

	public void setRulelstR120(List<DataProfilerVb> rulelstR120) {
		this.rulelstR120 = rulelstR120;
	}

	public List<DataProfilerVb> getRulelstR130() {
		return rulelstR130;
	}

	public void setRulelstR130(List<DataProfilerVb> rulelstR130) {
		this.rulelstR130 = rulelstR130;
	}

	public List<DataProfilerVb> getRulelstR140() {
		return rulelstR140;
	}

	public void setRulelstR140(List<DataProfilerVb> rulelstR140) {
		this.rulelstR140 = rulelstR140;
	}

	public List<DataProfilerVb> getRulelstR150() {
		return rulelstR150;
	}

	public void setRulelstR150(List<DataProfilerVb> rulelstR150) {
		this.rulelstR150 = rulelstR150;
	}

	public List<DataProfilerVb> getRulelstR160() {
		return rulelstR160;
	}

	public void setRulelstR160(List<DataProfilerVb> rulelstR160) {
		this.rulelstR160 = rulelstR160;
	}

	public List<DataProfilerVb> getRulelstR170() {
		return rulelstR170;
	}

	public void setRulelstR170(List<DataProfilerVb> rulelstR170) {
		this.rulelstR170 = rulelstR170;
	}

	public List<DataProfilerVb> getRulelstR180() {
		return rulelstR180;
	}

	public void setRulelstR180(List<DataProfilerVb> rulelstR180) {
		this.rulelstR180 = rulelstR180;
	}

	public List<DataProfilerVb> getRulelstR190() {
		return rulelstR190;
	}

	public void setRulelstR190(List<DataProfilerVb> rulelstR190) {
		this.rulelstR190 = rulelstR190;
	}

	public List<DataProfilerVb> getRulelstR200() {
		return rulelstR200;
	}

	public void setRulelstR200(List<DataProfilerVb> rulelstR200) {
		this.rulelstR200 = rulelstR200;
	}

	public List<DataProfilerVb> getRulelstR220() {
		return rulelstR220;
	}

	public void setRulelstR220(List<DataProfilerVb> rulelstR220) {
		this.rulelstR220 = rulelstR220;
	}

	public List<DataProfilerVb> getRulelstR310() {
		return rulelstR310;
	}

	public void setRulelstR310(List<DataProfilerVb> rulelstR310) {
		this.rulelstR310 = rulelstR310;
	}

	public List<DataProfilerVb> getRulelstR320() {
		return rulelstR320;
	}

	public void setRulelstR320(List<DataProfilerVb> rulelstR320) {
		this.rulelstR320 = rulelstR320;
	}

	public String getColumn_names() {
		return column_names;
	}

	public void setColumn_names(String column_names) {
		this.column_names = column_names;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getInputvalue() {
		return inputvalue;
	}

	public void setInputvalue(String inputvalue) {
		this.inputvalue = inputvalue;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getRuleOTHFlag() {
		return ruleOTHFlag;
	}

	public void setRuleOTHFlag(int ruleOTHFlag) {
		this.ruleOTHFlag = ruleOTHFlag;
	}

	public int getRuleOTHBlindRep() {
		return ruleOTHBlindRep;
	}

	public void setRuleOTHBlindRep(int ruleOTHBlindRep) {
		this.ruleOTHBlindRep = ruleOTHBlindRep;
	}

	public int getRuleOTHIncRep() {
		return ruleOTHIncRep;
	}

	public void setRuleOTHIncRep(int ruleOTHIncRep) {
		this.ruleOTHIncRep = ruleOTHIncRep;
	}

	public ArrayList<SmartSearchVb> getSearchTableschild() {
		return searchTableschild;
	}

	public void setSearchTableschild(ArrayList<SmartSearchVb> searchTableschild) {
		this.searchTableschild = searchTableschild;
	}

	public int getProfilerConfigAt() {
		return profilerConfigAt;
	}

	public void setProfilerConfigAt(int profilerConfigAt) {
		this.profilerConfigAt = profilerConfigAt;
	}

	public int getPrfCusrunNt() {
		return prfCusrunNt;
	}

	public void setPrfCusrunNt(int prfCusrunNt) {
		this.prfCusrunNt = prfCusrunNt;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

	public String getPrfType() {
		return prfType;
	}

	public void setPrfType(String prfType) {
		this.prfType = prfType;
	}

	public String getRuleNumException() {
		return ruleNumException;
	}

	public void setRuleNumException(String ruleNumException) {
		this.ruleNumException = ruleNumException;
	}

	public String getRuleAlphaException() {
		return ruleAlphaException;
	}

	public void setRuleAlphaException(String ruleAlphaException) {
		this.ruleAlphaException = ruleAlphaException;
	}

	public String getPrfAttRunStatusDesc() {
		return prfAttRunStatusDesc;
	}

	public void setPrfAttRunStatusDesc(String prfAttRunStatusDesc) {
		this.prfAttRunStatusDesc = prfAttRunStatusDesc;
	}

	public String getPrfCusRunStatusDesc() {
		return prfCusRunStatusDesc;
	}

	public void setPrfCusRunStatusDesc(String prfCusRunStatusDesc) {
		this.prfCusRunStatusDesc = prfCusRunStatusDesc;
	}

	public String getRuleDateFormat() {
		return ruleDateFormat;
	}

	public void setRuleDateFormat(String ruleDateFormat) {
		this.ruleDateFormat = ruleDateFormat;
	}

	public String getRuleDdId() {
		return ruleDdId;
	}

	public void setRuleDdId(String ruleDdId) {
		this.ruleDdId = ruleDdId;
	}

	public int getProcessCount() {
		return processCount;
	}

	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}

	public int getRuleAlphaFlag() {
		return ruleAlphaFlag;
	}

	public void setRuleAlphaFlag(int ruleAlphaFlag) {
		this.ruleAlphaFlag = ruleAlphaFlag;
	}

	public String getRuleAlphaChar() {
		return ruleAlphaChar;
	}

	public void setRuleAlphaChar(String ruleAlphaChar) {
		this.ruleAlphaChar = ruleAlphaChar;
	}

	public int getRuleNumFlag() {
		return ruleNumFlag;
	}

	public void setRuleNumFlag(int ruleNumFlag) {
		this.ruleNumFlag = ruleNumFlag;
	}

	public String getRuleNumChar() {
		return ruleNumChar;
	}

	public void setRuleNumChar(String ruleNumChar) {
		this.ruleNumChar = ruleNumChar;
	}

	public int getRuleSplFlag() {
		return ruleSplFlag;
	}

	public void setRuleSplFlag(int ruleSplFlag) {
		this.ruleSplFlag = ruleSplFlag;
	}

	public String getRuleSplChar() {
		return ruleSplChar;
	}

	public void setRuleSplChar(String ruleSplChar) {
		this.ruleSplChar = ruleSplChar;
	}

	public int getRuleAlphaCaseAt() {
		return ruleAlphaCaseAt;
	}

	public void setRuleAlphaCaseAt(int ruleAlphaCaseAt) {
		this.ruleAlphaCaseAt = ruleAlphaCaseAt;
	}

}