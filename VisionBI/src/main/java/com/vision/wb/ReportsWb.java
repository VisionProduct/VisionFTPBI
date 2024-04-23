package com.vision.wb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.vision.authentication.CustomContextHolder;
import com.vision.dao.AbstractDao;
import com.vision.dao.CommonApiDao;
import com.vision.dao.CommonDao;
import com.vision.dao.ReportsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.CreateCsv;
import com.vision.util.DeepCopy;
import com.vision.util.ExcelExportUtil;
import com.vision.util.HTMLtoDOCX;
import com.vision.util.PDFExportUtil;
import com.vision.util.ValidationUtil;
import com.vision.vb.CbWordDocumentStgVb;
import com.vision.vb.ColumnHeadersVb;
import com.vision.vb.ColumnHeadersWordVb;
import com.vision.vb.PrdQueryConfig;
import com.vision.vb.PromptTreeVb;
import com.vision.vb.RCReportFieldsVb;
import com.vision.vb.ReportFilterVb;
import com.vision.vb.ReportStgVb;
import com.vision.vb.ReportUserDefVb;
import com.vision.vb.ReportsVb;
import com.vision.vb.SmartSearchVb;
import com.vision.vb.VisionUsersVb;

@Component
public class ReportsWb extends AbstractWorkerBean<ReportsVb> implements ServletContextAware {
	public ApplicationContext applicationContext;
	private ServletContext servletContext;

	@Autowired
	private ReportsDao reportsDao;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private PDFExportUtil pdfExportUtil;

	@Autowired
	CommonApiDao commonApiDao;

	@Value("${app.databaseType}")
	private String databaseType;

	@Value("${app.productName}")
	private String productName;

	@Value("${ftp.password}")
	private String password;

	public void setServletContext(ServletContext arg0) {
		servletContext = arg0;
	}

	public static Logger logger = LoggerFactory.getLogger(ReportsWb.class);

	@Override
	protected void setAtNtValues(ReportsVb vObject) {

	}

	@Override
	protected void setVerifReqDeleteType(ReportsVb vObject) {
		vObject.setStaticDelete(false);
		vObject.setVerificationRequired(false);
	}

	@Override
	protected AbstractDao<ReportsVb> getScreenDao() {
		return reportsDao;
	}

	final String SERVICE_NAME = "PRD Reports";
	private String base64Str;

	public ExceptionCode getReportList() {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			List<ReportsVb> reportTree = null;
			List<ReportsVb> lResult = new ArrayList<ReportsVb>(0);
			reportTree = reportsDao.getReportSuiteFolderStructure();
			lResult = createPraentChildReportsTree(reportTree, null);
			exceptionCode.setResponse(lResult);
			if (lResult != null && lResult.size() > 0) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			} else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ExceptionCode reportFilterProcess(String filterRefCode, ReportsVb vObject1) {
		List<ReportFilterVb> filterlst = new ArrayList<ReportFilterVb>();
		ExceptionCode exceptionCode = new ExceptionCode();
		Connection conExt = null;
		VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
		try {
			String defaultCountry = commonDao.findVisionVariableValue("DEFAULT_COUNTRY");
			String defaultLeBook = commonDao.findVisionVariableValue("DEFAULT_LE_BOOK");
			List<ReportFilterVb> filterDetaillst = reportsDao.getReportFilterDetail(filterRefCode);
			if (filterDetaillst != null && filterDetaillst.size() > 0) {
				ReportFilterVb filterObj = filterDetaillst.get(0);
				String filterObjProp = ValidationUtil.isValid(filterObj.getFilterRefXml())
						? filterObj.getFilterRefXml().replaceAll("\n", "").replaceAll("\r", "")
						: "";
				String promptXml = CommonUtils.getValueForXmlTag(filterObjProp, "Prompts");
				int filterCnt = Integer.parseInt(CommonUtils.getValueForXmlTag(promptXml, "PromptCount"));
				for (int i = 1; i <= filterCnt; i++) {
					ReportFilterVb vObject = new ReportFilterVb();
					String refXml = CommonUtils.getValueForXmlTag(filterObjProp, "Prompt" + i);

					vObject.setFilterSeq(Integer.parseInt(CommonUtils.getValueForXmlTag(refXml, "Sequence")));
					vObject.setFilterLabel(CommonUtils.getValueForXmlTag(refXml, "Label"));
					vObject.setFilterType(CommonUtils.getValueForXmlTag(refXml, "Type"));
					vObject.setFilterSourceId(CommonUtils.getValueForXmlTag(refXml, "SourceId"));
					if (!ValidationUtil.isValid(vObject.getFilterSourceId())) {
						vObject.setFilterSourceId("DUMMY"); // Source Id will not be there for DATE/TEXTD filters
					}
					vObject.setDependencyFlag(CommonUtils.getValueForXmlTag(refXml, "DependencyFlag"));
					vObject.setDependentPrompt(CommonUtils.getValueForXmlTag(refXml, "DependentPrompt"));
					vObject.setMultiWidth(CommonUtils.getValueForXmlTag(refXml, "MultiWidth"));
					vObject.setMultiSelect(CommonUtils.getValueForXmlTag(refXml, "MultiSelect"));
					vObject.setSpecificTab(CommonUtils.getValueForXmlTag(refXml, "SpecificTab")); // Only for Dashboards
					vObject.setDefaultValueId(CommonUtils.getValueForXmlTag(refXml, "DefaultValue"));
					vObject.setFilterRow(CommonUtils.getValueForXmlTag(refXml, "FilterRow"));
					vObject.setBudgetFilter(CommonUtils.getValueForXmlTag(refXml, "BudgetFilter")); // For Budget Review
					vObject.setFilterDateFormat(CommonUtils.getValueForXmlTag(refXml, "DateFormat"));
					vObject.setFilterDateRestrict(CommonUtils.getValueForXmlTag(refXml, "DateRestrict"));
					if (ValidationUtil.isValid(vObject.getFilterDateRestrict())) {
						String userCountry = ValidationUtil.isValid(visionUsersVb.getCountry())
								? visionUsersVb.getCountry()
								: defaultCountry + "-" + defaultLeBook;
						userCountry = userCountry.replaceAll("'", "");
						vObject.setFilterDateRestrict(
								vObject.getFilterDateRestrict().replaceAll("#VU_CLEB#", userCountry));
					}
					vObject.setIsRequired(CommonUtils.getValueForXmlTag(refXml, "Required"));
					vObject.setVbdEnable(CommonUtils.getValueForXmlTag(refXml, "VbdEnable"));
					vObject.setVbdParams(CommonUtils.getValueForXmlTag(refXml, "VbdParams"));

					/*
					 * Important Note : ----------------- Use for Multi Entity Business Date purpose
					 * (Only for DATE type Prompt) ParamDelim : Dependency Prompt should be
					 * mentioned, Param Delim should be seperator of Dep.Prompt (Eg :
					 * 10100-KE-01,PromptDelim : -) ParamPos : (10100-KE-01, Param Pos : 2,3 results
					 * to KE-01 pass to the Date Prompt)
					 * <DateRestrict>0Y:0Y,3M:0M,0D:0D:VBD_#VU_CLEB# </DateRestrict> ChangeDefault :
					 * Y or N, To Change Default Prompt Value
					 * 
					 */

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "ParamDelim")))
						vObject.setParamDelim(CommonUtils.getValueForXmlTag(refXml, "ParamDelim"));
					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "ParamPos")))
						vObject.setParamPos(CommonUtils.getValueForXmlTag(refXml, "ParamPos"));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "ChangeDefault")))
						vObject.setChangeDefault(CommonUtils.getValueForXmlTag(refXml, "ChangeDefault"));

					if (!ValidationUtil.isValid(vObject.getIsRequired())) {
						vObject.setIsRequired("Y");
					}
					vObject.setFilterRefCode(filterRefCode);
					if (ValidationUtil.isValid(vObject.getDefaultValueId()) && ("PROJECT_DESC".equalsIgnoreCase(vObject.getDefaultValueId()) 
							|| "CONFIG_DESC".equalsIgnoreCase(vObject.getDefaultValueId())) ) {
						LinkedHashMap<String, String> filterDefaultValueMap = new LinkedHashMap<String, String>();
						if("PROJECT_DESC".equalsIgnoreCase(vObject.getDefaultValueId()))
							filterDefaultValueMap.put(vObject1.getPromptValue1(), vObject1.getPromptValue1());
						else
							filterDefaultValueMap.put(vObject1.getPromptValue2(), vObject1.getPromptValue2());
						
						vObject.setFilterDefaultValue(filterDefaultValueMap);	
					}else if (ValidationUtil.isValid(vObject.getDefaultValueId())) {
						// defaultValueSrc = replaceFilterHashVariables(vObject.getDefaultValueId(),
						// vObject);
						// vObject.setFilterDefaultValue(reportsDao.getReportFilterValue(defaultValueSrc));
						LinkedHashMap<String, String> filterDefaultValueMap = new LinkedHashMap<String, String>();
						exceptionCode = getReportFilterSourceValue(vObject);
						LinkedHashMap<String, String> filterMap = (LinkedHashMap<String, String>) exceptionCode
								.getResponse();
						if (filterMap != null) {
							for (Map.Entry<String, String> entry : filterMap.entrySet()) {
								String key = entry.getKey();
								if (key.contains("@")) {
									key = key.replace("@", "");
									filterDefaultValueMap.put(key, entry.getValue());
								}

							}
							vObject.setFilterDefaultValue(filterDefaultValueMap);
						}
					}
					if (!ValidationUtil.isValid(vObject.getDependencyFlag())) {
						vObject.setDependencyFlag("N");
					}
					if (!ValidationUtil.isValid(vObject.getVbdEnable())) {
						vObject.setVbdEnable("N");
					}
					/*
					 * if ("N".equalsIgnoreCase(vObject.getDependencyFlag()) &&
					 * ValidationUtil.isValid(vObject.getFilterSourceId())) {
					 * vObject.setFilterSourceVal(getFilterSourceValue(vObject)); }
					 */
					filterlst.add(vObject);
				}
			}
			exceptionCode.setResponse(filterlst);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			return exceptionCode;
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
			return exceptionCode;
		}
	}

	public ExceptionCode getFilterSourceValue(ReportFilterVb vObject) {
		Connection conExt = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			List<PrdQueryConfig> sourceQuerylst = reportsDao.getSqlQuery(vObject.getFilterSourceId());
			vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));
			if (sourceQuerylst != null && sourceQuerylst.size() > 0) {
				PrdQueryConfig sourceQueryDet = sourceQuerylst.get(0);
				if ("QUERY".equalsIgnoreCase(sourceQueryDet.getDataRefType())) {
					sourceQueryDet.setQueryProc(replaceFilterHashVariables(sourceQueryDet.getQueryProc(), vObject));
					exceptionCode = reportsDao.getReportFilterValue(sourceQueryDet.getQueryProc());
				} else if ("PROCEDURE".equalsIgnoreCase(sourceQueryDet.getDataRefType())) {
					if (sourceQueryDet.getQueryProc().contains("#")) {
						sourceQueryDet.setQueryProc(replaceFilterHashVariables(sourceQueryDet.getQueryProc(), vObject));
						exceptionCode = reportsDao.getFilterPromptWithHashVar(vObject, sourceQueryDet, "COMBO");
					} else {
						exceptionCode = reportsDao.getComboPromptData(vObject, sourceQueryDet);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return exceptionCode;
	}

	public String replaceFilterHashVariables(String query, ReportFilterVb vObject) {

		query = query.replaceAll("#VISION_ID#", "'" + CustomContextHolder.getContext().getVisionId() + "'");
		query = query.replaceAll("#SESSION_ID#", "'" + vObject.getDateCreation() + "'");
		query = query.replaceAll("#PROMPT_ID#", "'" + vObject.getFilterSourceId() + "'");

		query = query.replaceAll("#PROMPT_VALUE_1#",
				ValidationUtil.isValid(vObject.getFilter1Val()) ? vObject.getFilter1Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_2#",
				ValidationUtil.isValid(vObject.getFilter2Val()) ? vObject.getFilter2Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_3#",
				ValidationUtil.isValid(vObject.getFilter3Val()) ? vObject.getFilter3Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_4#",
				ValidationUtil.isValid(vObject.getFilter4Val()) ? vObject.getFilter4Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_5#",
				ValidationUtil.isValid(vObject.getFilter5Val()) ? vObject.getFilter5Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_6#",
				ValidationUtil.isValid(vObject.getFilter6Val()) ? vObject.getFilter6Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_7#",
				ValidationUtil.isValid(vObject.getFilter7Val()) ? vObject.getFilter7Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_8#",
				ValidationUtil.isValid(vObject.getFilter8Val()) ? vObject.getFilter8Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_9#",
				ValidationUtil.isValid(vObject.getFilter9Val()) ? vObject.getFilter9Val() : "''");
		query = query.replaceAll("#PROMPT_VALUE_10#",
				ValidationUtil.isValid(vObject.getFilter10Val()) ? vObject.getFilter10Val() : "''");

		query = query.replaceAll("#NS_PROMPT_VALUE_1#",
				ValidationUtil.isValid(vObject.getFilter1Val()) ? vObject.getFilter1Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_2#",
				ValidationUtil.isValid(vObject.getFilter2Val()) ? vObject.getFilter2Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_3#",
				ValidationUtil.isValid(vObject.getFilter3Val()) ? vObject.getFilter3Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_4#",
				ValidationUtil.isValid(vObject.getFilter4Val()) ? vObject.getFilter4Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_5#",
				ValidationUtil.isValid(vObject.getFilter5Val()) ? vObject.getFilter5Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_6#",
				ValidationUtil.isValid(vObject.getFilter6Val()) ? vObject.getFilter6Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_7#",
				ValidationUtil.isValid(vObject.getFilter7Val()) ? vObject.getFilter7Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_8#",
				ValidationUtil.isValid(vObject.getFilter8Val()) ? vObject.getFilter8Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_9#",
				ValidationUtil.isValid(vObject.getFilter9Val()) ? vObject.getFilter9Val().replaceAll("'", "") : "''");
		query = query.replaceAll("#NS_PROMPT_VALUE_10#",
				ValidationUtil.isValid(vObject.getFilter10Val()) ? vObject.getFilter10Val().replaceAll("'", "") : "''");

		ReportsVb promptsVb = new ReportsVb();
		promptsVb.setPromptValue1(vObject.getFilter1Val());
		promptsVb.setPromptValue2(vObject.getFilter2Val());
		promptsVb.setPromptValue3(vObject.getFilter3Val());
		promptsVb.setPromptValue4(vObject.getFilter4Val());
		promptsVb.setPromptValue5(vObject.getFilter5Val());
		promptsVb.setPromptValue6(vObject.getFilter6Val());
		promptsVb.setPromptValue7(vObject.getFilter7Val());
		promptsVb.setPromptValue8(vObject.getFilter8Val());
		promptsVb.setPromptValue9(vObject.getFilter9Val());
		promptsVb.setPromptValue10(vObject.getFilter10Val());

		query = commonDao.applyUserRestriction(query);
		query = applyPrPromptChange(query, promptsVb);
		query = applySpecialPrompts(query, promptsVb);
		return query;
	}

	public ExceptionCode getResultData(ReportsVb vObject, Boolean exportFlag) {
		ExceptionCode exceptionCode = new ExceptionCode();
		int totalRecords = 0;
		List datalst = new ArrayList();
		Connection conExt = null;
		try {
			ExceptionCode exConnection = commonDao.getReqdConnection(conExt, vObject.getDbConnection());
			if (exConnection.getErrorCode() != Constants.ERRONEOUS_OPERATION && exConnection.getResponse() != null) {
				conExt = (Connection) exConnection.getResponse();
			} else {
				exceptionCode.setErrorCode(exConnection.getErrorCode());
				exceptionCode.setErrorMsg(exConnection.getErrorMsg());
				exceptionCode.setResponse(exConnection.getResponse());
				return exceptionCode;
			}
			if ("G".equalsIgnoreCase(vObject.getObjectType())) {
				if (exportFlag) {
					exceptionCode = reportsDao.extractReportData(vObject, conExt, exportFlag);
				} else {
					exceptionCode = reportsDao.extractReportData(vObject, conExt);
				}
				if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
					datalst = (ArrayList) exceptionCode.getResponse();
					vObject.setGridDataSet(datalst);
					if (exceptionCode.getResponse1() != null)
						vObject.setColumnHeaderslst((ArrayList<ColumnHeadersVb>) exceptionCode.getResponse1());
					// Filter the Data which doensnot contains Format Type S
					List datalstNew = new ArrayList<>();
					for (Map<String, Object> dataLstMap : (List<Map<String, Object>>) datalst) {
						if (dataLstMap.containsKey("FORMAT_TYPE")) {
							if (dataLstMap.get("FORMAT_TYPE") == null)
								dataLstMap.put("FORMAT_TYPE", "D");
						}
						datalstNew.add(dataLstMap);
					}
					List<Map<String, Object>> finalDatalst = (List<Map<String, Object>>) datalstNew.stream()
							.filter(hashmap -> ((HashMap<String, String>) hashmap).containsKey("FORMAT_TYPE"))
							.filter(hashmap -> !((HashMap<String, String>) hashmap).get("FORMAT_TYPE")
									.equalsIgnoreCase("S"))
							.collect(Collectors.toList());
					if (finalDatalst != null && !finalDatalst.isEmpty())
						vObject.setGridDataSet(finalDatalst);

					totalRecords = (int) exceptionCode.getRequest();
					vObject.setTotalRows(totalRecords);
					if (vObject.getCurrentPage() == 1) {
						List<Map<String, Object>> totallst = (List<Map<String, Object>>) datalst.stream()
								.filter(hashmap -> ((HashMap<String, String>) hashmap).containsKey("FORMAT_TYPE"))
								.filter(hashmap -> ((HashMap<String, String>) hashmap).get("FORMAT_TYPE")
										.equalsIgnoreCase("S"))
								.collect(Collectors.toList());
						vObject.setTotal(totallst);
						if (totallst == null || totallst.isEmpty()) {
							List<ReportsVb> sumStringLst = new ArrayList<>();
							StringJoiner sumString = new StringJoiner(",");
							vObject.getColumnHeaderslst().forEach(colHeadersVb -> {
								if (!"T".equalsIgnoreCase(colHeadersVb.getColType())
										&& (colHeadersVb.getColspan() == 0 || colHeadersVb.getColspan() == 1)
										&& "Y".equalsIgnoreCase(colHeadersVb.getSumFlag())) {
									sumString.add("SUM(" + colHeadersVb.getDbColumnName() + ") "
											+ colHeadersVb.getDbColumnName());
								}
							});
							ExceptionCode exceptionCode1 = new ExceptionCode();
							String query = null;
							if (sumString.length() > 0) {
								query = "SELECT " + sumString.toString() + ",'S' FORMAT_TYPE FROM ("
										+ vObject.getFinalExeQuery() + ") TOT ";
								exceptionCode1 = commonApiDao.getCommonResultDataQuery(query, conExt);
								if (exceptionCode1.getResponse() != null) {
									sumStringLst = (List<ReportsVb>) exceptionCode1.getResponse();
									vObject.setTotal(sumStringLst);
								}
							}
						}
					}
				} else {
					exceptionCode.setResponse(vObject);
					return exceptionCode;
				}
			} else if ("C".equalsIgnoreCase(vObject.getObjectType())) {
				exceptionCode = reportsDao.getChartReportData(vObject, vObject.getFinalExeQuery(), conExt);
				if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
					vObject.setChartData(exceptionCode.getResponse().toString());
				}
			} else if ("T".equalsIgnoreCase(vObject.getObjectType()) || "SC".equalsIgnoreCase(vObject.getObjectType())
					|| "S".equalsIgnoreCase(vObject.getObjectType())) {
				exceptionCode = reportsDao.getTilesReportData(vObject, vObject.getFinalExeQuery(), conExt);
				if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
					vObject.setTileData(exceptionCode.getResponse().toString());
				}
			}
			// exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(vObject);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Success");
		} catch (Exception e) {
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
			return exceptionCode;
		} finally {
			try {
				conExt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * if(vObject.getPromptTree() != null) { PromptTreeVb promptTreeVb = new
			 * PromptTreeVb(); if(ValidationUtil.isValid(promptTreeVb.getTableName())) {
			 * if(!"REPORTS_STG".equalsIgnoreCase(promptTreeVb.getTableName()))
			 * reportsDao.deleteTempTable(promptTreeVb.getTableName()); else
			 * reportsDao.deleteReportsStgData(promptTreeVb); }
			 * if(ValidationUtil.isValid(promptTreeVb.getColumnHeaderTable()))
			 * reportsDao.deleteColumnHeadersData(promptTreeVb); }
			 */
		}
		return exceptionCode;
	}

	public ExceptionCode getReportDetails(ReportsVb vObject) throws SQLException {
		ExceptionCode exceptionCode = new ExceptionCode();
		ExceptionCode exceptionCodeProc = new ExceptionCode();
		PrdQueryConfig prdQueryConfig = new PrdQueryConfig();
		List<PrdQueryConfig> sqlQueryList = new ArrayList<PrdQueryConfig>();
		DeepCopy<ReportsVb> clonedObj = new DeepCopy<ReportsVb>();
		PromptTreeVb promptTreeVb = new PromptTreeVb();
		String reportType = vObject.getReportType();
		ReportsVb subReportsVb = new ReportsVb();
		try {
			List<ReportsVb> reportDatalst = new ArrayList<ReportsVb>();
			if (!ValidationUtil.isValid(vObject.getObjectType())) {
				vObject.setObjectType("G");
			}
			subReportsVb = clonedObj.copy(vObject);
			if (ValidationUtil.isValid(vObject.getSubReportId())) {
				if (ValidationUtil.isValid(subReportsVb.getDdFlag())
						&& "Y".equalsIgnoreCase(subReportsVb.getDdFlag())) {
					reportDatalst = reportsDao.getSubReportDetail(vObject);
					if (reportDatalst != null && reportDatalst.size() > 0) {
						subReportsVb = reportDatalst.get(0);
						subReportsVb.setNextLevel(reportsDao.getIntReportNextLevel(subReportsVb));
					}
				} else {
					subReportsVb.setNextLevel("0");
				}

			} else {
				reportDatalst = reportsDao.getSubReportDetail(vObject);
				if (reportDatalst != null && reportDatalst.size() > 0) {
					subReportsVb = reportDatalst.get(0);
					if (ValidationUtil.isValid(subReportsVb.getDdFlag())
							&& "Y".equalsIgnoreCase(subReportsVb.getDdFlag())) {
						subReportsVb.setNextLevel(reportsDao.getNextLevel(subReportsVb));
					}
				} else {
					exceptionCode.setOtherInfo(vObject);
					exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
					exceptionCode.setErrorMsg(
							"Report Levels Not Maintained for the ReportId[" + vObject.getReportId() + "] !!");
					return exceptionCode;
				}
			}
			sqlQueryList = reportsDao.getSqlQuery(subReportsVb.getDataRefId());
			if (sqlQueryList != null && sqlQueryList.size() > 0) {
				prdQueryConfig = sqlQueryList.get(0);
			} else {
				exceptionCode.setOtherInfo(vObject);
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode
						.setErrorMsg("Query not maintained for the Data Ref Id[" + subReportsVb.getDataRefId() + "]");
				return exceptionCode;
			}
			subReportsVb.setDbConnection(prdQueryConfig.getDbConnectionName());
			vObject.setDbConnection(prdQueryConfig.getDbConnectionName());
			ArrayList<ColumnHeadersVb> colHeaders = new ArrayList<ColumnHeadersVb>();
			List<ColumnHeadersVb> columnHeadersXmllst = reportsDao.getReportColumns(subReportsVb);
			String columnHeaderXml = "";
			if (!"CB".equalsIgnoreCase(reportType)) {
				if ((columnHeadersXmllst == null || columnHeadersXmllst.isEmpty())) {
					if ("QUERY".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
						exceptionCode.setOtherInfo(vObject);
						exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
						exceptionCode.setErrorMsg(
								"Column Headers Not Maintained for the ReportId[" + subReportsVb.getReportId()
										+ "] and Sub Report Id[" + subReportsVb.getSubReportId() + "] !!");
						return exceptionCode;
					}
				} else {
					columnHeaderXml = columnHeadersXmllst.get(0).getColumnXml();

				}
			}
			subReportsVb.setReportTitle(CommonUtils.getValueForXmlTag(columnHeaderXml, "OBJECT_CAPTION"));
			subReportsVb.setGrandTotalCaption(CommonUtils.getValueForXmlTag(columnHeaderXml, "GRANDTOTAL_CAPTION"));

			if ("G".equalsIgnoreCase(subReportsVb.getObjectType())) {
				colHeaders = getColumnHeaders(columnHeaderXml, vObject);
				subReportsVb.setColumnHeaderslst(colHeaders);
			} else if ("C".equalsIgnoreCase(subReportsVb.getObjectType())
					|| "T".equalsIgnoreCase(subReportsVb.getObjectType())
					|| "SC".equalsIgnoreCase(subReportsVb.getObjectType())
					|| "S".equalsIgnoreCase(subReportsVb.getObjectType())) {
				subReportsVb.setColHeaderXml(columnHeaderXml);
				subReportsVb.setDdKeyPromptLabel(CommonUtils.getValueForXmlTag(columnHeaderXml, "DDKEY_PROMPT_LABEL"));
			}
			String finalExeQuery = "";
			String maxRecords = commonDao.findVisionVariableValue("PRD_REPORT_MAXROW");
			subReportsVb.setMaxRecords(Integer.parseInt(ValidationUtil.isValid(maxRecords) ? maxRecords : "5000"));
			String maxRecordPerPage = commonDao.findVisionVariableValue("PRD_REPORT_MAX_PERPAGE");
			int maxPerPage = ValidationUtil.isValid(maxRecordPerPage) ? Integer.parseInt(maxRecordPerPage) : 100;
			subReportsVb.setMaxRecPerPage(maxPerPage);
			// prdQueryConfig.setQueryProc(prdQueryConfig.getQueryProc().toUpperCase());
			String queryTmp = prdQueryConfig.getQueryProc();
			queryTmp = queryTmp.toUpperCase();
			vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));

			if ("W".equalsIgnoreCase(vObject.getReportType()) || "I".equalsIgnoreCase(vObject.getReportType())) {
				if ("0".equalsIgnoreCase(vObject.getScalingFactor())) {
					vObject.setScalingFactor(subReportsVb.getScalingFactor());
				} else {
					subReportsVb.setScalingFactor(vObject.getScalingFactor());
				}
			}
			if ("W".equalsIgnoreCase(vObject.getReportType())) {
				if (ValidationUtil.isValid(vObject.getReportPeriodFormat())) {
					VisionUsersVb visionUsers = CustomContextHolder.getContext();
					String country = "";
					String leBook = "";
					String[] promptVal = new String[2];
					if (ValidationUtil.isValid(vObject.getVbdParamValue()))
						promptVal = vObject.getVbdParamValue().split("-");
					if (ValidationUtil.isValid(visionUsers.getCountry())) {
						country = visionUsers.getCountry();
					} else if (promptVal != null && promptVal.length > 0) {
						country = promptVal[0];
					} else {
						country = commonDao.findVisionVariableValue("DEFAULT_COUNTRY");
					}
					if (ValidationUtil.isValid(visionUsers.getLeBook())) {
						leBook = visionUsers.getLeBook();
					} else if (ValidationUtil.isValid(vObject.getVbdParamValue())) {
						leBook = promptVal[1];
					} else {
						leBook = commonDao.findVisionVariableValue("DEFAULT_LE_BOOK");
					}

					subReportsVb.setReportPeriod(
							commonDao.getCurrentDateInfo(vObject.getReportPeriodFormat(), country + "-" + leBook));
				}

			}
			if ("QUERY".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				if ("G".equalsIgnoreCase(subReportsVb.getObjectType())
						|| "T".equalsIgnoreCase(subReportsVb.getObjectType())
						|| "SC".equalsIgnoreCase(subReportsVb.getObjectType())
						|| "S".equalsIgnoreCase(subReportsVb.getObjectType())) {
					if (queryTmp.contains("ORDER BY")) {
						String orderBy = queryTmp.substring(queryTmp.lastIndexOf("ORDER BY"), queryTmp.length());
						prdQueryConfig.setQueryProc(
								prdQueryConfig.getQueryProc().substring(0, queryTmp.lastIndexOf("ORDER BY")));
						prdQueryConfig.setSortField(orderBy);
					}
					if (!ValidationUtil.isValid(prdQueryConfig.getSortField())) {
						exceptionCode.setOtherInfo(vObject);
						exceptionCode.setErrorMsg(
								"ORDER BY is mandatory in Query for [" + subReportsVb.getDataRefId() + "] !!");
						return exceptionCode;
					}
				}
				finalExeQuery = replacePromptVariables(prdQueryConfig.getQueryProc(), vObject, false);
			} else if ("PROCEDURE".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				vObject.setSubReportId(subReportsVb.getSubReportId());
				if (vObject.getPromptTree() == null) {
					finalExeQuery = replacePromptVariables(prdQueryConfig.getQueryProc(), vObject, true);
					logger.info("Procedure Execution Start ReportID[" + vObject.getReportId() + "]SubReport["
							+ vObject.getSubReportId() + "]");
					exceptionCodeProc = reportsDao.callProcforReportData(vObject, finalExeQuery);
					logger.info("Procedure Execution End ReportID[" + vObject.getReportId() + "]SubReport["
							+ vObject.getSubReportId() + "]");
				} else {
					exceptionCodeProc.setResponse(vObject.getPromptTree());
					exceptionCodeProc.setErrorCode(Constants.STATUS_ZERO);
				}
				if (exceptionCodeProc.getErrorCode() == Constants.STATUS_ZERO) {
					promptTreeVb = (PromptTreeVb) exceptionCodeProc.getResponse();
					if (ValidationUtil.isValid(promptTreeVb.getTableName())) {
						if ("REPORTS_STG".equalsIgnoreCase(promptTreeVb.getTableName().toUpperCase())) {
							finalExeQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " WHERE SESSION_ID='"
									+ promptTreeVb.getSessionId() + "' AND REPORT_ID='" + promptTreeVb.getReportId()
									+ "' ";
						} else {
							finalExeQuery = "SELECT * FROM " + promptTreeVb.getTableName() + " ";
						}
						prdQueryConfig.setSortField("ORDER BY SORT_FIELD");
						if (ValidationUtil.isValid(promptTreeVb.getColumnHeaderTable())) {
							colHeaders = (ArrayList<ColumnHeadersVb>) reportsDao.getColumnHeaderFromTable(promptTreeVb);
							if (colHeaders != null && !colHeaders.isEmpty()) {
								subReportsVb.setColumnHeaderslst(colHeaders);
							}
						}
						if (!"CB".equalsIgnoreCase(reportType)) {
							if (!"CBW".equalsIgnoreCase(reportType)) {
								if (subReportsVb.getColumnHeaderslst() == null
										|| subReportsVb.getColumnHeaderslst().isEmpty()) {
									exceptionCode.setOtherInfo(vObject);
									exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
									exceptionCode.setErrorMsg("Column Headers Not Maintained for the ReportId["
											+ subReportsVb.getReportId() + "] and Sub Report Id["
											+ subReportsVb.getSubReportId() + "] !!");
									return exceptionCode;
								}
							}
						}
						subReportsVb.setPromptTree(promptTreeVb);
					} else {
						exceptionCode.setOtherInfo(vObject);
						exceptionCode.setErrorMsg(
								"Output Table not return from Procedure but the Procedure return Success Status");
						return exceptionCode;
					}
				} else {
					exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
					exceptionCode.setErrorMsg(exceptionCodeProc.getErrorMsg());
					exceptionCode.setOtherInfo(vObject);
					return exceptionCode;
				}
			} else if ("STATIC".equalsIgnoreCase(prdQueryConfig.getDataRefType())) {
				exceptionCode.setResponse(subReportsVb);
				exceptionCode.setRequest(prdQueryConfig.getDataRefType()); // CB Static Word document
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setErrorMsg("Success");
				return exceptionCode;
			}
			subReportsVb.setSortField(prdQueryConfig.getSortField());
			String masterReportType = "";
			masterReportType = reportsDao.getReportType(vObject.getReportId());
			if (ValidationUtil.isValid(masterReportType) && "W".equalsIgnoreCase(masterReportType)
					&& "C".equalsIgnoreCase(subReportsVb.getObjectType()))
				subReportsVb.setChartList(reportsDao.getChartList(vObject.getChartType()));

			// save user def settings
			List savedUserSetLst = new ArrayList<>();
			ArrayList<SmartSearchVb> smartSearchlst = new ArrayList<SmartSearchVb>();
			ReportUserDefVb reportUserDefVb = new ReportUserDefVb();
			if (!ValidationUtil.isValid(vObject.getRunType()))
				vObject.setRunType("R");

			if (!"W".equalsIgnoreCase(masterReportType) && "G".equalsIgnoreCase(vObject.getObjectType())
					&& "R".equalsIgnoreCase(vObject.getRunType())) {
				savedUserSetLst = reportsDao.getSavedUserDefSetting(subReportsVb, true);

			}
			if (savedUserSetLst != null && !savedUserSetLst.isEmpty())
				reportUserDefVb = (ReportUserDefVb) savedUserSetLst.get(0);

			if (!ValidationUtil.isValid(vObject.getColumnsToHide()))
				subReportsVb.setColumnsToHide(reportUserDefVb.getColumnsToHide());
			else
				subReportsVb.setColumnsToHide(vObject.getColumnsToHide());
			if (!ValidationUtil.isValid(vObject.getApplyGrouping()))
				subReportsVb.setApplyGrouping(reportUserDefVb.getApplyGrouping());
			else
				subReportsVb.setApplyGrouping(vObject.getApplyGrouping());
			if (!ValidationUtil.isValid(vObject.getShowDimensions()))
				subReportsVb.setShowDimensions(reportUserDefVb.getShowDimensions());
			else
				subReportsVb.setShowDimensions(vObject.getShowDimensions());
			if (!ValidationUtil.isValid(vObject.getShowMeasures()))
				subReportsVb.setShowMeasures(reportUserDefVb.getShowMeasures());
			else
				subReportsVb.setShowMeasures(vObject.getShowMeasures());
			if (ValidationUtil.isValid(reportUserDefVb.getSearchColumn())) {
				String[] searchArr = reportUserDefVb.getSearchColumn().split(",");
				for (String strArr : searchArr) {
					SmartSearchVb smartSearchVb = new SmartSearchVb();
					String[] searcCondArr = strArr.split("!@#");
					smartSearchVb.setObject(searcCondArr[0]);
					smartSearchVb.setCriteria(searcCondArr[1]);
					smartSearchVb.setValue(searcCondArr[2]);
					smartSearchlst.add(smartSearchVb);
				}
			}
			if ("Y".equalsIgnoreCase(subReportsVb.getApplyGrouping())) {
				// vObject.setScreenSortColumn("ORDER BY " + subReportsVb.getShowDimensions());
				subReportsVb.setSortField("ORDER BY " + subReportsVb.getShowDimensions());
				if (!ValidationUtil.isValid(vObject.getScreenSortColumn())
						&& ValidationUtil.isValid(reportUserDefVb.getSortColumn()))
					subReportsVb.setSortField(reportUserDefVb.getSortColumn());
				if (ValidationUtil.isValid(vObject.getScreenSortColumn()))
					subReportsVb.setSortField(vObject.getScreenSortColumn());
			} else {
				if (!ValidationUtil.isValid(vObject.getScreenSortColumn())
						&& !ValidationUtil.isValid(reportUserDefVb.getSortColumn()))
					subReportsVb.setSortField(prdQueryConfig.getSortField());
				else if (!ValidationUtil.isValid(vObject.getScreenSortColumn())
						&& ValidationUtil.isValid(reportUserDefVb.getSortColumn()))
					subReportsVb.setSortField(reportUserDefVb.getSortColumn());
				else
					subReportsVb.setSortField(vObject.getScreenSortColumn());
			}

			if (smartSearchlst != null && !smartSearchlst.isEmpty()
					&& !ValidationUtil.isValid(vObject.getSmartSearchOpt())) {
				vObject.setSmartSearchOpt(smartSearchlst);
			}
			if (vObject.getSmartSearchOpt() != null && vObject.getSmartSearchOpt().size() > 0) {
				finalExeQuery = "SELECT * FROM ( " + finalExeQuery + " ) TEMP WHERE ";
				for (int len = 0; len < vObject.getSmartSearchOpt().size(); len++) {
					SmartSearchVb data = vObject.getSmartSearchOpt().get(len);
					String searchVal = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getValue());
					if (len > 0)
						finalExeQuery = finalExeQuery + " AND ";
					if ("MSSQL".equalsIgnoreCase(databaseType)) {
						if ("GREATER".equalsIgnoreCase(data.getCriteria())
								|| "GREATEREQUALS".equalsIgnoreCase(data.getCriteria())
								|| "LESSER".equalsIgnoreCase(data.getCriteria())
								|| "LESSEREQUALS".equalsIgnoreCase(data.getCriteria()))
							finalExeQuery = finalExeQuery + "(" + data.getObject() + ") " + searchVal;
						else
							finalExeQuery = finalExeQuery + " UPPER(" + data.getObject() + ") " + searchVal;
					} else {
						finalExeQuery = finalExeQuery + " UPPER(" + data.getObject() + ") " + searchVal;
					}
				}
			}
			logger.info("Final Query Prepared to fetch Data for Report Id[" + vObject.getReportId() + "]SubReport["
					+ vObject.getSubReportId() + "]");
			subReportsVb.setReportUserDeflst(savedUserSetLst);
			subReportsVb.setFinalExeQuery(finalExeQuery);
			// subReportsVb.setSortField(prdQueryConfig.getSortField());
			subReportsVb.setPromptLabel(vObject.getPromptLabel());
			subReportsVb.setDbConnection(prdQueryConfig.getDbConnectionName());
			subReportsVb.setDrillDownLabel(vObject.getDrillDownLabel());
			subReportsVb.setCurrentPage(vObject.getCurrentPage());
			subReportsVb.setWidgetTheme(vObject.getWidgetTheme());

			exceptionCode.setResponse(subReportsVb);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Success");
			return exceptionCode;
		} catch (Exception ex) {
			logger.error("Exception while getting Report Data[" + vObject.getReportId() + "]SubSeq["
					+ vObject.getNextLevel() + "]...!!");
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(ex.getMessage());
			exceptionCode.setOtherInfo(subReportsVb);
			return exceptionCode;
		}
	}

	public String replacePromptVariables(String reportQuery, ReportsVb promptsVb, Boolean isProcedure) {
		try {
			if (isProcedure)
				promptsVb = replaceProcedurePrompt(promptsVb);
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_1#",
					ValidationUtil.isValid(promptsVb.getPromptValue1()) ? promptsVb.getPromptValue1() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_2#",
					ValidationUtil.isValid(promptsVb.getPromptValue2()) ? promptsVb.getPromptValue2() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_3#",
					ValidationUtil.isValid(promptsVb.getPromptValue3()) ? promptsVb.getPromptValue3() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_4#",
					ValidationUtil.isValid(promptsVb.getPromptValue4()) ? promptsVb.getPromptValue4() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_5#",
					ValidationUtil.isValid(promptsVb.getPromptValue5()) ? promptsVb.getPromptValue5() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_6#",
					ValidationUtil.isValid(promptsVb.getPromptValue6()) ? promptsVb.getPromptValue6() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_7#",
					ValidationUtil.isValid(promptsVb.getPromptValue7()) ? promptsVb.getPromptValue7() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_8#",
					ValidationUtil.isValid(promptsVb.getPromptValue8()) ? promptsVb.getPromptValue8() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_9#",
					ValidationUtil.isValid(promptsVb.getPromptValue9()) ? promptsVb.getPromptValue9() : "''");
			reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_10#",
					ValidationUtil.isValid(promptsVb.getPromptValue10()) ? promptsVb.getPromptValue10() : "''");

			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_1#",
					promptsVb.getPromptValue1().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_2#",
					promptsVb.getPromptValue2().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_3#",
					promptsVb.getPromptValue3().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_4#",
					promptsVb.getPromptValue4().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_5#",
					promptsVb.getPromptValue5().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_6#",
					promptsVb.getPromptValue6().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_7#",
					promptsVb.getPromptValue7().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_8#",
					promptsVb.getPromptValue8().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_9#",
					promptsVb.getPromptValue9().replaceAll("'", ""));
			reportQuery = reportQuery.replaceAll("#NS_PROMPT_VALUE_10#",
					promptsVb.getPromptValue10().replaceAll("'", ""));

			reportQuery = reportQuery.replaceAll("#DDKEY1#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey1()) ? promptsVb.getDrillDownKey1() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY2#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey2()) ? promptsVb.getDrillDownKey2() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY3#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey3()) ? promptsVb.getDrillDownKey3() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY4#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey4()) ? promptsVb.getDrillDownKey4() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY5#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey5()) ? promptsVb.getDrillDownKey5() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY6#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey6()) ? promptsVb.getDrillDownKey6() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY7#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey7()) ? promptsVb.getDrillDownKey7() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY8#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey8()) ? promptsVb.getDrillDownKey8() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY9#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey9()) ? promptsVb.getDrillDownKey9() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY10#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey10()) ? promptsVb.getDrillDownKey10() : "''");
			reportQuery = reportQuery.replaceAll("#DDKEY0#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey0()) ? promptsVb.getDrillDownKey0() : "''");

			reportQuery = reportQuery.replaceAll("#NS_DDKEY1#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey1())
							? promptsVb.getDrillDownKey1().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY2#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey2())
							? promptsVb.getDrillDownKey2().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY3#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey3())
							? promptsVb.getDrillDownKey3().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY4#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey4())
							? promptsVb.getDrillDownKey4().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY5#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey5())
							? promptsVb.getDrillDownKey5().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY6#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey6())
							? promptsVb.getDrillDownKey6().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY7#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey7())
							? promptsVb.getDrillDownKey7().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY8#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey8())
							? promptsVb.getDrillDownKey8().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY9#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey9())
							? promptsVb.getDrillDownKey9().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY10#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey10())
							? promptsVb.getDrillDownKey10().replaceAll("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_DDKEY0#",
					ValidationUtil.isValid(promptsVb.getDrillDownKey0())
							? promptsVb.getDrillDownKey0().replaceAll("'", "")
							: "''");

			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_1#",
					ValidationUtil.isValid(promptsVb.getDataFilter1()) ? promptsVb.getDataFilter1() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_2#",
					ValidationUtil.isValid(promptsVb.getDataFilter2()) ? promptsVb.getDataFilter2() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_3#",
					ValidationUtil.isValid(promptsVb.getDataFilter3()) ? promptsVb.getDataFilter3() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_4#",
					ValidationUtil.isValid(promptsVb.getDataFilter4()) ? promptsVb.getDataFilter4() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_5#",
					ValidationUtil.isValid(promptsVb.getDataFilter5()) ? promptsVb.getDataFilter5() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_6#",
					ValidationUtil.isValid(promptsVb.getDataFilter6()) ? promptsVb.getDataFilter6() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_7#",
					ValidationUtil.isValid(promptsVb.getDataFilter7()) ? promptsVb.getDataFilter7() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_8#",
					ValidationUtil.isValid(promptsVb.getDataFilter8()) ? promptsVb.getDataFilter8() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_9#",
					ValidationUtil.isValid(promptsVb.getDataFilter9()) ? promptsVb.getDataFilter9() : "''");
			reportQuery = reportQuery.replaceAll("#LOCAL_FILTER_10#",
					ValidationUtil.isValid(promptsVb.getDataFilter10()) ? promptsVb.getDataFilter10() : "''");

			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_1#",
					ValidationUtil.isValid(promptsVb.getDataFilter1()) ? promptsVb.getDataFilter1().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_2#",
					ValidationUtil.isValid(promptsVb.getDataFilter2()) ? promptsVb.getDataFilter2().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_3#",
					ValidationUtil.isValid(promptsVb.getDataFilter3()) ? promptsVb.getDataFilter3().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_4#",
					ValidationUtil.isValid(promptsVb.getDataFilter4()) ? promptsVb.getDataFilter4().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_5#",
					ValidationUtil.isValid(promptsVb.getDataFilter5()) ? promptsVb.getDataFilter5().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_6#",
					ValidationUtil.isValid(promptsVb.getDataFilter6()) ? promptsVb.getDataFilter6().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_7#",
					ValidationUtil.isValid(promptsVb.getDataFilter7()) ? promptsVb.getDataFilter7().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_8#",
					ValidationUtil.isValid(promptsVb.getDataFilter8()) ? promptsVb.getDataFilter8().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_9#",
					ValidationUtil.isValid(promptsVb.getDataFilter9()) ? promptsVb.getDataFilter9().replace("'", "")
							: "''");
			reportQuery = reportQuery.replaceAll("#NS_LOCAL_FILTER_10#",
					ValidationUtil.isValid(promptsVb.getDataFilter10()) ? promptsVb.getDataFilter10().replace("'", "")
							: "''");

			reportQuery = reportQuery.replaceAll("#FILTER_POSITION#",
					ValidationUtil.isValid(promptsVb.getFilterPosition()) ? "'" + promptsVb.getFilterPosition() + "'"
							: "''");

			reportQuery = reportQuery.replaceAll("#REPORT_ID#", "'" + promptsVb.getReportId() + "'");
			reportQuery = reportQuery.replaceAll("#SUB_REPORT_ID#", "'" + promptsVb.getSubReportId() + "'");
			reportQuery = reportQuery.replaceAll("#VISION_ID#",
					"'" + CustomContextHolder.getContext().getVisionId() + "'");
			reportQuery = reportQuery.replaceAll("#SESSION_ID#", "'" + promptsVb.getDateCreation() + "'");
			reportQuery = reportQuery.replaceAll("#SCALING_FACTOR#",
					ValidationUtil.isValid(promptsVb.getScalingFactor()) ? promptsVb.getScalingFactor() : "1");

			// Below is used only on MPR Report - MDM for Prime Bank - Deepak
			if (promptsVb.getReportId().contains("MPR") && ValidationUtil.isValid(promptsVb.getPromptValue6())) {
				reportQuery = reportQuery.replaceAll("#PYM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "PYM"));
				reportQuery = reportQuery.replaceAll("#NYM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "NYM"));
				reportQuery = reportQuery.replaceAll("#PM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "PM"));
				reportQuery = reportQuery.replaceAll("#NM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "NM"));
				reportQuery = reportQuery.replaceAll("#CM#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "CM"));
				reportQuery = reportQuery.replaceAll("#CY#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "CY"));
				reportQuery = reportQuery.replaceAll("#PY#",
						reportsDao.getDateFormat(promptsVb.getPromptValue6(), "PY"));
			}

			reportQuery = commonDao.applyUserRestriction(reportQuery);
			reportQuery = applyPrPromptChange(reportQuery, promptsVb);
			reportQuery = applySpecialPrompts(reportQuery, promptsVb);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportQuery;
	}

	public ExceptionCode getIntReportsDetail(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		List<ReportsVb> reportsLst = new ArrayList<>();
		try {
			reportsLst = reportsDao.getReportsDetail(vObject);
			if (reportsLst != null && !reportsLst.isEmpty()) {
				reportsLst.forEach(reportDet -> {
					reportDet.setNextLevel(reportsDao.getNextLevel(reportDet));
					List<ColumnHeadersVb> colHeadersLst = new ArrayList<ColumnHeadersVb>();
					colHeadersLst = reportsDao.getReportColumns(reportDet);
					if (colHeadersLst != null && colHeadersLst.size() > 0) {
						reportDet.setReportTitle(
								CommonUtils.getValueForXmlTag(colHeadersLst.get(0).getColumnXml(), "OBJECT_CAPTION"));
						reportDet.setGrandTotalCaption(CommonUtils
								.getValueForXmlTag(colHeadersLst.get(0).getColumnXml(), "GRANDTOTAL_CAPTION"));
						reportDet.setColumnHeaderslst(colHeadersLst);
					}
				});
			} else {
				exceptionCode.setErrorMsg("Report Details not found!!");
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			}
			exceptionCode.setOtherInfo(vObject);
			exceptionCode.setResponse(reportsLst);
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ArrayList<ColumnHeadersVb> getColumnHeaders(String colHeadersXml, ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList<ColumnHeadersVb> colHeaders = new ArrayList<ColumnHeadersVb>();
		try {
			if (ValidationUtil.isValid(colHeadersXml)) {
				colHeadersXml = ValidationUtil.isValid(colHeadersXml)
						? colHeadersXml.replaceAll("\n", "").replaceAll("\r", "")
						: "";
				String colDetXml = CommonUtils.getValueForXmlTag(colHeadersXml, "COLUMNS");
				int colCount = 0;
				colCount = Integer.parseInt(CommonUtils.getValueForXmlTag(colDetXml, "COLUMN_COUNT"));
				for (int i = 1; i <= colCount; i++) {
					ColumnHeadersVb colHeadersVb = new ColumnHeadersVb();
					String refXml = CommonUtils.getValueForXmlTag(colDetXml, "COLUMN" + i);
					if (!ValidationUtil.isValid(refXml))
						continue;
					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "LABLE_ROW_NUM")))
						colHeadersVb.setLabelRowNum(
								Integer.parseInt(CommonUtils.getValueForXmlTag(refXml, "LABLE_ROW_NUM")));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "LABLE_COL_NUM")))
						colHeadersVb.setLabelColNum(
								Integer.parseInt(CommonUtils.getValueForXmlTag(refXml, "LABLE_COL_NUM")));

					String caption = CommonUtils.getValueForXmlTag(refXml, "CAPTION");
					String ruleFlag = CommonUtils.getValueForXmlTag(refXml, "RULE_FLAG");
					String recordsSpanFlag = CommonUtils.getValueForXmlTag(refXml, "RECORDS_SPAN");
					if(ValidationUtil.isValid(recordsSpanFlag)) {
						colHeadersVb.setDataGroupFlag(recordsSpanFlag);
					}
					colHeadersVb.setRuleFlag(ruleFlag);
					// #PROMPT_VALUE_1#!@DD-Mon-RRRR!@-0

					if (caption.contains("PROMPT_VALUE")) {
						if (caption.contains("!@")) {
							caption = formQueryForCaption(caption, vObject);
						} else {
							caption = replacePromptVariables(caption, vObject, false);
						}
					}
					if (caption.contains("#")) {
						String cap = caption.replaceAll("#", "");
						List resultLst = new ArrayList<>();
						resultLst = commonDao.getDateFormatforCaption();
						HashMap<String, String> dateMap = (HashMap<String, String>) resultLst.get(0);
						String val = dateMap.get(cap);
						caption = caption.replaceAll(caption, val);
						// caption = replaceDate(caption, val);
					}

					colHeadersVb.setCaption(caption);

					// colHeadersVb.setCaption(CommonUtils.getValueForXmlTag(refXml, "CAPTION"));
					colHeadersVb.setColType(CommonUtils.getValueForXmlTag(refXml, "COL_TYPE"));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "ROW_SPAN")))
						colHeadersVb.setRowspan(Integer.parseInt(CommonUtils.getValueForXmlTag(refXml, "ROW_SPAN")));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "COL_SPAN")))
						colHeadersVb.setColspan(Integer.parseInt(CommonUtils.getValueForXmlTag(refXml, "COL_SPAN")));
					String dbColName = CommonUtils.getValueForXmlTag(refXml, "SOURCE_COLUMN").toUpperCase();
					colHeadersVb.setDbColumnName(dbColName);
					String drillDownLabel = CommonUtils.getValueForXmlTag(refXml, "DRILLDOWN_LABEL_FLAG");
					if (ValidationUtil.isValid(drillDownLabel) && "Y".equalsIgnoreCase(drillDownLabel)) {
						colHeadersVb.setDrillDownLabel(true);
					} else {
						colHeadersVb.setDrillDownLabel(false);
					}
					colHeadersVb.setScaling(CommonUtils.getValueForXmlTag(refXml, "SCALING"));
					colHeadersVb.setDecimalCnt(CommonUtils.getValueForXmlTag(refXml, "DECIMALCNT"));
					colHeadersVb.setColumnWidth(CommonUtils.getValueForXmlTag(refXml, "COLUMN_WIDTH"));
					colHeadersVb.setGroupingFlag(CommonUtils.getValueForXmlTag(refXml, "GROUPING_FLAG"));
					String sumFlag = CommonUtils.getValueForXmlTag(refXml, "SUM_FLAG");
					if (ValidationUtil.isValid(sumFlag)) {
						colHeadersVb.setSumFlag(sumFlag);
					} else {
						colHeadersVb.setSumFlag("N");
					}
					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "COLOR_DIFF")))
						colHeadersVb.setColorDiff(CommonUtils.getValueForXmlTag(refXml, "COLOR_DIFF"));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "DISPLAY_ORDER")))
						colHeadersVb.setDisplayOrder(CommonUtils.getValueForXmlTag(refXml, "DISPLAY_ORDER"));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "FONT_STYLE")))
						colHeadersVb.setFontStyle(CommonUtils.getValueForXmlTag(refXml, "FONT_STYLE"));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "BG_COLOR")))
						colHeadersVb.setBgColor(CommonUtils.getValueForXmlTag(refXml, "BG_COLOR"));

					if (ValidationUtil.isValid(CommonUtils.getValueForXmlTag(refXml, "FONT_COLOR")))
						colHeadersVb.setFontColor(CommonUtils.getValueForXmlTag(refXml, "FONT_COLOR"));

					colHeaders.add(colHeadersVb);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return colHeaders;
	}

	public ExceptionCode exportToXls(ReportsVb vObject, int currentUserId, String workBookCnt) {
		ExceptionCode exceptionCode = new ExceptionCode();
		ReportsVb reportsVb = new ReportsVb();
		int rowNum = 0;
		try {
			String reportTitle = "";
			int screenFreeze = vObject.getFreezeColumn();
			String applicationTheme = vObject.getApplicationTheme();
			reportTitle = vObject.getReportTitle();
			String screenGroupColumn = vObject.getScreenGroupColumn();
			String screenSortColumn = vObject.getScreenSortColumn();
			String[] hiddenColumns = null;
			if (ValidationUtil.isValid(vObject.getColumnsToHide())) {
				hiddenColumns = vObject.getColumnsToHide().split("!@#");
			}
			exceptionCode = getReportDetails(vObject);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					vObject = (ReportsVb) exceptionCode.getResponse();
				}
			} else {
				return exceptionCode;
			}
			List<ColumnHeadersVb> colHeaderslst = vObject.getColumnHeaderslst();
			List<ColumnHeadersVb> updatedLst = vObject.getColumnHeaderslst();
			if (hiddenColumns != null) {
				for (int ctr = 0; ctr < hiddenColumns.length; ctr++) {
					updatedLst = formColumnHeader(updatedLst, hiddenColumns[ctr]);
				}
			}
			if (updatedLst != null && updatedLst.size() > 0) {
				int finalMaxRow = updatedLst.stream().mapToInt(ColumnHeadersVb::getLabelRowNum).max().orElse(0);
				for (ColumnHeadersVb colObj : updatedLst) {
					if (colObj.getRowspan() > finalMaxRow) {
						colObj.setRowspan(colObj.getRowspan() - finalMaxRow);
					}
				}
				colHeaderslst = updatedLst;
			}

			List<String> colTypes = new ArrayList<String>();
			Map<Integer, Integer> columnWidths = new HashMap<Integer, Integer>(colHeaderslst.size());
			List<ReportStgVb> reportsStgs = new ArrayList<ReportStgVb>();
			for (int loopCnt = 0; loopCnt < colHeaderslst.size(); loopCnt++) {
				columnWidths.put(Integer.valueOf(loopCnt), Integer.valueOf(-1));
				ColumnHeadersVb colHVb = colHeaderslst.get(loopCnt);
				if (colHVb.getColspan() <= 1 && colHVb.getNumericColumnNo() != 99) {
					colTypes.add(colHVb.getColType());
				}
			}
			int headerCnt = 0;

			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			// String assetFolderUrl = commonDao.findVisionVariableValue("MDM_IMAGE_PATH");
			// assetFolderUrl = "E:\\RA_Image\\";
			vObject.setReportTitle(reportTitle);
			vObject.setMaker(currentUserId);
			if (ValidationUtil.isValid(screenGroupColumn))
				vObject.setScreenGroupColumn(screenGroupColumn);
			if (ValidationUtil.isValid(screenSortColumn))
				vObject.setScreenSortColumn(screenSortColumn);
			getScreenDao().fetchMakerVerifierNames(vObject);
			if (ValidationUtil.isValid(applicationTheme))
				vObject.setApplicationTheme(applicationTheme);
			if (screenFreeze != 0) {
				vObject.setFreezeColumn(screenFreeze);
			}
			// ExcelExportUtil.createPrompts(vObject, new ArrayList(), sheet, workBook,
			// assetFolderUrl, styls, headerCnt);
			boolean createHeadersAndFooters = true;
			// Excel Report Header
			/// rowNum = ExcelExportUtil.writeHeadersRA(vObject, colHeaderslst, rowNum,
			// sheet, styls, colTypes,columnWidths);
			logger.info("Excel Export Data Extraction Begin[" + vObject.getReportId() + ":" + vObject.getSubReportId()
					+ "]");
			exceptionCode = getResultData(vObject, true);
			logger.info(
					"Excel Export Data Extraction End[" + vObject.getReportId() + ":" + vObject.getSubReportId() + "]");
			List<HashMap<String, String>> dataLst = null;
			List<HashMap<String, String>> totalLst = null;
			ReportsVb resultVb = new ReportsVb();
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					resultVb = (ReportsVb) exceptionCode.getResponse();
					dataLst = resultVb.getGridDataSet();
					totalLst = resultVb.getTotal();
				}
			}
			logger.info(
					"Excel Export Data Write Begin[" + vObject.getReportId() + ":" + vObject.getSubReportId() + "]");
			SXSSFWorkbook workBook = new SXSSFWorkbook(500);
			String sheetName = vObject.getReportTitle().trim();
			// sheetName = WorkbookUtil.createSafeSheetName(sheetName);
			// vObject.setReportTitle(sheetName);
			SXSSFSheet sheet = (SXSSFSheet) workBook.createSheet("Summary");

			// SXSSFSheet sheet = (SXSSFSheet) workBook.getSheet(vObject.getReportTitle());
			Map<Integer, XSSFCellStyle> styls = ExcelExportUtil.createStyles(workBook, applicationTheme);
			ExcelExportUtil.createPromptsPage(vObject, sheet, workBook, assetFolderUrl, styls, headerCnt);
			sheet = (SXSSFSheet) workBook.createSheet("Report");
			int ctr = 1;
			int sheetCnt = 3;
			do {
				if ((rowNum + vObject.getMaxRecords()) > SpreadsheetVersion.EXCEL2007.getMaxRows()) {
					rowNum = 0;
					sheet = (SXSSFSheet) workBook.createSheet("" + sheetCnt);
					sheetCnt++;
					createHeadersAndFooters = true;
				}
				if (createHeadersAndFooters) {
					rowNum = ExcelExportUtil.writeHeadersRA(vObject, colHeaderslst, rowNum, sheet, styls, colTypes,
							columnWidths);
					sheet.createFreezePane(vObject.getFreezeColumn(), rowNum);
				}
				createHeadersAndFooters = false;
				// writing data into excel
				ctr++;
				rowNum = ExcelExportUtil.writeReportDataRA(workBook, vObject, colHeaderslst, dataLst, sheet, rowNum,
						styls, colTypes, columnWidths, false, assetFolderUrl);
				vObject.setCurrentPage(ctr);
				dataLst = new ArrayList();
				exceptionCode = getResultData(vObject, true);
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					resultVb = (ReportsVb) exceptionCode.getResponse();
					dataLst = resultVb.getGridDataSet();
				}
			} while (dataLst != null && !dataLst.isEmpty());

			if (totalLst != null)
				rowNum = ExcelExportUtil.writeReportDataRA(workBook, vObject, colHeaderslst, totalLst, sheet, rowNum,
						styls, colTypes, columnWidths, true, assetFolderUrl);

			headerCnt = colTypes.size();
			int noOfSheets = workBook.getNumberOfSheets();
			for (int a = 1; a < noOfSheets; a++) {
				sheet = (SXSSFSheet) workBook.getSheetAt(a);
				int loopCount = 0;
				for (loopCount = 0; loopCount < headerCnt; loopCount++) {
					sheet.setColumnWidth(loopCount, columnWidths.get(loopCount));
				}
			}
			String filePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(filePath)) {
				filePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(filePath)) {
				filePath = filePath + File.separator;
			}
			File lFile = new File(
					filePath + ValidationUtil.encode(vObject.getReportTitle()) + "_" + currentUserId + ".xlsx");
			if (lFile.exists()) {
				lFile.delete();
			}
			lFile.createNewFile();
			FileOutputStream fileOS = new FileOutputStream(lFile);
			workBook.write(fileOS);
			fileOS.flush();
			fileOS.close();
			logger.info("Excel Export Data Write End[" + vObject.getReportId() + ":" + vObject.getSubReportId() + "]");
			exceptionCode.setResponse(filePath);
			exceptionCode.setOtherInfo(vObject.getReportTitle() + "_" + currentUserId);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"Report Export Excel Exception at: " + vObject.getReportId() + " : " + vObject.getReportTitle());
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
		}
		return exceptionCode;
	}

	public ExceptionCode exportToPdf(int currentUserId, ReportsVb reportsVb) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			String applicationTheme = reportsVb.getApplicationTheme();
			String reportOrientation = reportsVb.getReportOrientation();
			String reportTitle = reportsVb.getReportTitle();
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			// String assetFolderUrl = commonDao.findVisionVariableValue("MDM_IMAGE_PATH");
			String screenGroupColumn = reportsVb.getScreenGroupColumn();
			String screenSortColumn = reportsVb.getScreenSortColumn();
			String[] hiddenColumns = null;
			if (ValidationUtil.isValid(reportsVb.getColumnsToHide())) {
				hiddenColumns = reportsVb.getColumnsToHide().split("!@#");
			}
			reportsVb.setMaker(CustomContextHolder.getContext().getVisionId());
			exceptionCode = getReportDetails(reportsVb);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					reportsVb = (ReportsVb) exceptionCode.getResponse();
					// exceptionCode = getResultData(subReportsVb);
				}
			} else {
				return exceptionCode;
			}
			List<ColumnHeadersVb> colHeaderslst = reportsVb.getColumnHeaderslst();
			ArrayList<ColumnHeadersVb> updatedLst = new ArrayList<ColumnHeadersVb>();
			if (hiddenColumns != null) {
				for (int ctr = 0; ctr < hiddenColumns.length; ctr++) {
					updatedLst = formColumnHeader(colHeaderslst, hiddenColumns[ctr]);
					colHeaderslst = updatedLst;
				}
			}

			if (updatedLst != null && updatedLst.size() > 0) {
				int finalMaxRow = updatedLst.stream().mapToInt(ColumnHeadersVb::getLabelRowNum).max().orElse(0);
				for (ColumnHeadersVb colObj : updatedLst) {
					if (colObj.getRowspan() > finalMaxRow) {
						colObj.setRowspan(colObj.getRowspan() - finalMaxRow);
					}
				}
				colHeaderslst = updatedLst;

			}

			List<String> colTypes = new ArrayList<String>();
			Map<Integer, Integer> columnWidths = new HashMap<Integer, Integer>(colHeaderslst.size());
			for (int loopCnt = 0; loopCnt < colHeaderslst.size(); loopCnt++) {
				columnWidths.put(Integer.valueOf(loopCnt), Integer.valueOf(-1));
				ColumnHeadersVb colHVb = colHeaderslst.get(loopCnt);
				if (colHVb.getColspan() <= 1 && colHVb.getNumericColumnNo() != 99) {
					colTypes.add(colHVb.getColType());
				}
			}
			logger.info("Pdf Export Data Extraction Begin[" + reportsVb.getReportId() + ":" + reportsVb.getSubReportId()
					+ "]");
			exceptionCode = getResultData(reportsVb, true);
			logger.info("Pdf Export Data Extraction End[" + reportsVb.getReportId() + ":" + reportsVb.getSubReportId()
					+ "]");
			List<HashMap<String, String>> dataLst = null;
			List<HashMap<String, String>> totalLst = null;
			ReportsVb resultVb = new ReportsVb();
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					resultVb = (ReportsVb) exceptionCode.getResponse();
					dataLst = resultVb.getGridDataSet();
					totalLst = resultVb.getTotal();
				}
			}
			logger.info(
					"Pdf Export Data Write Begin[" + reportsVb.getReportId() + ":" + reportsVb.getSubReportId() + "]");
			/*
			 * if(!reportOrientation.equalsIgnoreCase(reportsVb.getReportOrientation())) {
			 * reportsVb.setPdfGrwthPercent(0); }
			 */
			reportsVb.setReportTitle(reportTitle);
			reportsVb.setMaker(currentUserId);
			reportsVb.setReportOrientation(reportOrientation);
			reportsVb.setApplicationTheme(applicationTheme);
			getScreenDao().fetchMakerVerifierNames(reportsVb);
			// Grouping on PDF
			String[] capGrpCols = null;
			ArrayList<String> groupingCols = new ArrayList<String>();
			ArrayList<ColumnHeadersVb> columnHeadersFinallst = new ArrayList<ColumnHeadersVb>();
			colHeaderslst.forEach(colHeadersVb -> {
				if (colHeadersVb.getColspan() <= 1 && colHeadersVb.getNumericColumnNo() != 99) {
					columnHeadersFinallst.add(colHeadersVb);
				}
			});
			if (ValidationUtil.isValid(screenGroupColumn)) {
				reportsVb.setPdfGroupColumn(screenGroupColumn);
			}
			if (ValidationUtil.isValid(reportsVb.getPdfGroupColumn()))
				capGrpCols = reportsVb.getPdfGroupColumn().split("!@#");

			if (reportsVb.getTotalRows() <= reportsVb.getMaxRecords() && capGrpCols != null && capGrpCols.length > 0) {
				for (String grpStr : capGrpCols) {
					for (ColumnHeadersVb colHeader : columnHeadersFinallst) {
						if (grpStr.equalsIgnoreCase(colHeader.getCaption().toUpperCase())) {
							groupingCols.add(colHeader.getDbColumnName());
							break;
						}
					}
				}
			}
			final String[] grpColNames = capGrpCols;
			Map<String, List<HashMap<String, String>>> groupingMap = new HashMap<String, List<HashMap<String, String>>>();
			if (reportsVb.getTotalRows() <= reportsVb.getMaxRecords()
					&& (groupingCols != null && groupingCols.size() > 0)) {
				switch (groupingCols.size()) {
				case 1:
					groupingMap = dataLst.stream()
							.collect(Collectors.groupingBy(m -> (m.get(groupingCols.get(0))) == null ? ""
									: grpColNames[0] + ": " + m.get(groupingCols.get(0))));
					break;
				case 2:
					groupingMap = dataLst.stream()
							.collect(Collectors.groupingBy(
									m -> (m.get(groupingCols.get(0)) + " >> " + m.get(groupingCols.get(1))) == null ? ""
											: grpColNames[0] + ": " + m.get(groupingCols.get(0)) + " >> "
													+ grpColNames[1] + ": " + m.get(groupingCols.get(1))));
					break;
				case 3:
					groupingMap = dataLst.stream()
							.collect(Collectors.groupingBy(m -> (m.get(groupingCols.get(0)) + " >> "
									+ m.get(groupingCols.get(1)) + " >> " + m.get(groupingCols.get(2))) == null
											? ""
											: grpColNames[0] + ": " + m.get(groupingCols.get(0)) + " >> "
													+ grpColNames[1] + ": " + m.get(groupingCols.get(1)) + " >> "
													+ grpColNames[2] + ": " + m.get(groupingCols.get(2))));
					break;
				case 4:
					groupingMap = dataLst.stream().collect(
							Collectors.groupingBy(m -> (m.get(groupingCols.get(0)) + " >> " + m.get(groupingCols.get(1))
									+ " >> " + m.get(groupingCols.get(2)) + " >> " + m.get(groupingCols.get(3))) == null
											? ""
											: grpColNames[0] + ": " + m.get(groupingCols.get(0)) + " >> "
													+ grpColNames[1] + ": " + m.get(groupingCols.get(1)) + " >> "
													+ grpColNames[2] + ": " + m.get(groupingCols.get(2)) + " >> "
													+ grpColNames[2] + ": " + m.get(groupingCols.get(3))));
					break;
				case 5:
					groupingMap = dataLst.stream()
							.collect(Collectors.groupingBy(m -> (m.get(groupingCols.get(0)) + " >> "
									+ m.get(groupingCols.get(1)) + " >> " + m.get(groupingCols.get(2)) + " >> "
									+ m.get(groupingCols.get(3)) + " >> " + m.get(groupingCols.get(4))) == null
											? ""
											: grpColNames[0] + ": " + m.get(groupingCols.get(0)) + " >> "
													+ grpColNames[1] + ": " + m.get(groupingCols.get(1)) + " >> "
													+ grpColNames[2] + ": " + m.get(groupingCols.get(2)) + " >> "
													+ grpColNames[2] + ": " + m.get(groupingCols.get(3)) + " >> "
													+ grpColNames[2] + ": " + m.get(groupingCols.get(4))));
					break;
				}
				Map<String, List<HashMap<String, String>>> sortedMap = new TreeMap<String, List<HashMap<String, String>>>();
				if (ValidationUtil.isValid(screenSortColumn)) {
					if (screenSortColumn.contains(groupingCols.get(0))) {
						String value = screenSortColumn.substring(9, screenSortColumn.length()).toUpperCase();
						String[] col = value.split(",");
						for (int i = 0; i < col.length; i++) {
							if (col[i].contains(groupingCols.get(0))) {
								String val = col[i];
								if (val.contains("DESC")) {
									sortedMap = new TreeMap<String, List<HashMap<String, String>>>(
											Collections.reverseOrder());
									sortedMap.putAll(groupingMap);
								} else {
									sortedMap = new TreeMap<String, List<HashMap<String, String>>>(groupingMap);
								}
							}
						}
					} else {
						sortedMap = new TreeMap<String, List<HashMap<String, String>>>(groupingMap);
					}
				} else {
					sortedMap = new TreeMap<String, List<HashMap<String, String>>>(groupingMap);
				}

				exceptionCode = pdfExportUtil.exportToPdfRAWithGroup(colHeaderslst, dataLst, reportsVb, assetFolderUrl,
						colTypes, currentUserId, totalLst, sortedMap, columnHeadersFinallst);
			} else {
				exceptionCode = pdfExportUtil.exportToPdfRA(colHeaderslst, dataLst, reportsVb, assetFolderUrl, colTypes,
						currentUserId, totalLst, columnHeadersFinallst);
			}
			logger.info(
					"Pdf Export Data Write End[" + reportsVb.getReportId() + ":" + reportsVb.getSubReportId() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ExceptionCode exportMultiExcel(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			HashMap<String, ExceptionCode> resultMap = new HashMap<String, ExceptionCode>();
			ReportsVb reportVb = new ReportsVb();
			exceptionCode = getIntReportsDetail(vObject);
			List<ReportsVb> detailReportlst = (ArrayList<ReportsVb>) exceptionCode.getResponse();
			List<DataFetcher> threads = new ArrayList<DataFetcher>(detailReportlst.size());
			detailReportlst.forEach(reportsVb -> {
				reportsVb.setPromptValue1(vObject.getPromptValue1());
				reportsVb.setPromptValue2(vObject.getPromptValue2());
				reportsVb.setPromptValue3(vObject.getPromptValue3());
				reportsVb.setPromptValue4(vObject.getPromptValue4());
				reportsVb.setPromptValue5(vObject.getPromptValue5());
				reportsVb.setPromptValue6(vObject.getPromptValue6());
				reportsVb.setPromptValue7(vObject.getPromptValue7());
				reportsVb.setPromptValue8(vObject.getPromptValue8());
				reportsVb.setPromptValue9(vObject.getPromptValue9());
				reportsVb.setPromptValue10(vObject.getPromptValue10());
				reportsVb.getReportTitle();
				DataFetcher fetcher = new DataFetcher(reportsVb, resultMap);
				fetcher.setDaemon(true);
				fetcher.start();
				try {
					fetcher.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				threads.add(fetcher);

			});
			for (DataFetcher df : threads) {
				int count = 0;
				if (!df.dataFetched) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					if (count > 150) {
						count = 0;
						logger.info("Data fetch in progress for the report :" + df.toString());
						continue;
					}
				}
			}
			exceptionCode = exportMultiXls(vObject, resultMap);
		} catch (Exception e) {

		}
		return exceptionCode;
	}

	class DataFetcher extends Thread {
		boolean dataFetched = false;
		boolean errorOccured = false;
		String errorMsg = "";
		ExceptionCode exceptionCode;
		ReportsVb dObj = new ReportsVb();
		HashMap<String, ExceptionCode> resultMap = new HashMap<String, ExceptionCode>();

		public DataFetcher(ReportsVb reportsVb, HashMap<String, ExceptionCode> resultMap) {
			this.dObj = reportsVb;
			this.resultMap = resultMap;
		}

		public void run() {
			try {

				ExceptionCode exceptionCode = getReportDetails(dObj);
				if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
					if (ValidationUtil.isValid(exceptionCode.getResponse())) {
						ReportsVb subReportsVb = (ReportsVb) exceptionCode.getResponse();
						exceptionCode = getResultData(subReportsVb, false);
						exceptionCode.setRequest(dObj.getReportTitle());
					}
				}
				resultMap.put(dObj.getCurrentLevel(), exceptionCode);
			} catch (RuntimeCustomException rex) {
				dataFetched = true;
				errorOccured = true;
				exceptionCode = rex.getCode();
			} catch (Exception e) {
				dataFetched = true;
				errorOccured = true;
				errorMsg = e.getMessage();
			}
		}
	}

	public ExceptionCode exportMultiXls(ReportsVb vObject, HashMap<String, ExceptionCode> resultMap) {
		ExceptionCode exceptionCode = new ExceptionCode();
		ReportsVb reportsVb = new ReportsVb();
		try {
			VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
			int currentUserId = visionUsersVb.getVisionId();
			SXSSFWorkbook workBook = new SXSSFWorkbook(500);
			SXSSFSheet sheet = null;
			String filePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(filePath)) {
				filePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(filePath)) {
				filePath = filePath + File.separator;
			}
			File lFile = new File(
					filePath + ValidationUtil.encode(vObject.getReportTitle()) + "_" + currentUserId + ".xlsx");
			if (lFile.exists())
				lFile.delete();
			lFile.createNewFile();
			int headerCnt = 0;
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			sheet = (SXSSFSheet) workBook.createSheet("Report Detail");
			// workBook.createSheet(vObject.getReportTitle());
			Map<Integer, XSSFCellStyle> styls = ExcelExportUtil.createStyles(workBook, "");
			ExcelExportUtil.createPromptsPage(vObject, sheet, workBook, assetFolderUrl, styls, headerCnt);
			List<HashMap<String, String>> dataLst = new ArrayList<>();
			List<HashMap<String, String>> totalLst = new ArrayList<>();
			List<ColumnHeadersVb> colHeaderslst = new ArrayList<>();
			ReportsVb reportsStgs = null;
			Map<String, ExceptionCode> sortedMap = resultMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
							HashMap::new));
			for (Map.Entry<String, ExceptionCode> entry : sortedMap.entrySet()) {
				ExceptionCode resultDataException = (ExceptionCode) entry.getValue();
				reportsStgs = (ReportsVb) resultDataException.getResponse();
				String reportTitle = (String) resultDataException.getRequest();
				vObject.setScreenName(reportTitle);
				if (reportsStgs != null) {
					dataLst = reportsStgs.getGridDataSet();
					totalLst = reportsStgs.getTotal();
					colHeaderslst = ((ReportsVb) reportsStgs).getColumnHeaderslst();
				} else {
					dataLst = new ArrayList<>();
					totalLst = new ArrayList<>();
					colHeaderslst = new ArrayList<>();
				}
				workBook.createSheet(reportTitle);
				sheet = (SXSSFSheet) workBook.getSheet(reportTitle);
				List<String> colTypes = new ArrayList<String>();
				Map<Integer, Integer> columnWidths = new HashMap<Integer, Integer>(colHeaderslst.size());
				for (int loopCnt = 0; loopCnt < colHeaderslst.size(); loopCnt++) {
					columnWidths.put(Integer.valueOf(loopCnt), Integer.valueOf(-1));
					ColumnHeadersVb colHVb = colHeaderslst.get(loopCnt);
					if (colHVb.getColspan() <= 1) {
						colTypes.add(colHVb.getColType());
					}
				}
				// int headerCnt = 0;
				logger.info(
						"Report Export Excel Starts at: " + vObject.getReportId() + " : " + vObject.getReportTitle());
//				String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
				// String assetFolderUrl = commonDao.findVisionVariableValue("MDM_IMAGE_PATH");
				vObject.setMaker(currentUserId);
				getScreenDao().fetchMakerVerifierNames(vObject);
				boolean createHeadersAndFooters = true;
//				Map<Integer, XSSFCellStyle> styls = ExcelExportUtil.createStyles(workBook);
				// ExcelExportUtil.createPrompts(vObject, sheet, workBook, assetFolderUrl,
				// styls, headerCnt);
				int rowNum = 0;
				rowNum = ExcelExportUtil.writeHeadersRA(vObject, colHeaderslst, rowNum, sheet, styls, colTypes,
						columnWidths);
				rowNum = ExcelExportUtil.writeReportDataRA(workBook, vObject, colHeaderslst, dataLst, sheet, rowNum,
						styls, colTypes, columnWidths, false, assetFolderUrl);
				if (totalLst != null)
					rowNum = ExcelExportUtil.writeReportDataRA(workBook, vObject, colHeaderslst, totalLst, sheet,
							rowNum, styls, colTypes, columnWidths, true, assetFolderUrl);

				headerCnt = colTypes.size();
				int loopCount = 0;
				for (loopCount = 0; loopCount < headerCnt; loopCount++) {
					sheet.setColumnWidth(loopCount, columnWidths.get(loopCount));
				}
			}
			FileOutputStream fileOS = new FileOutputStream(lFile);
			workBook.write(fileOS);
			fileOS.flush();
			fileOS.close();
			logger.info("Report Export Excel End at: " + vObject.getReportId() + " : " + vObject.getReportTitle());
			exceptionCode.setResponse(filePath);
			exceptionCode.setOtherInfo(vObject.getReportTitle() + "_" + currentUserId);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"Report Export Excel Exception at: " + vObject.getReportId() + " : " + vObject.getReportTitle());
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
		}
		return exceptionCode;
	}

	public ExceptionCode exportMultiPdf(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			HashMap<String, ExceptionCode> resultMap = new HashMap<String, ExceptionCode>();
			ReportsVb reportVb = new ReportsVb();
			exceptionCode = getIntReportsDetail(vObject);
			List<ReportsVb> detailReportlst = (ArrayList<ReportsVb>) exceptionCode.getResponse();
			detailReportlst.get(0).getReportTitle();
			List<DataFetcher> threads = new ArrayList<DataFetcher>(detailReportlst.size());
			detailReportlst.forEach(reportsVb -> {
				reportsVb.setPromptValue1(vObject.getPromptValue1());
				reportsVb.setPromptValue2(vObject.getPromptValue2());
				reportsVb.setPromptValue3(vObject.getPromptValue3());
				reportsVb.setPromptValue4(vObject.getPromptValue4());
				reportsVb.setPromptValue5(vObject.getPromptValue5());
				reportsVb.setPromptValue6(vObject.getPromptValue6());
				reportsVb.setPromptValue7(vObject.getPromptValue7());
				reportsVb.setPromptValue8(vObject.getPromptValue8());
				reportsVb.setPromptValue9(vObject.getPromptValue9());
				reportsVb.setPromptValue10(vObject.getPromptValue10());
				reportsVb.getReportTitle();
				DataFetcher fetcher = new DataFetcher(reportsVb, resultMap);
				fetcher.setDaemon(true);
				fetcher.start();
				try {
					fetcher.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				threads.add(fetcher);

			});
			for (DataFetcher df : threads) {
				int count = 0;
				if (!df.dataFetched) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					if (count > 150) {
						count = 0;
						logger.info("Data fetch in progress for the report :" + df.toString());
						continue;
					}
				}
			}
			exceptionCode = pdfExportUtil.exportMultiReportPdf(vObject, resultMap, assetFolderUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exceptionCode;
	}

	public ExceptionCode getTreePromptData(ReportFilterVb vObject) {
		List<PromptTreeVb> promptTree = null;
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));
			List<PrdQueryConfig> sourceQuerylst = reportsDao.getSqlQuery(vObject.getFilterSourceId());
			if (sourceQuerylst != null && sourceQuerylst.size() > 0) {
				PrdQueryConfig sourceQueryDet = sourceQuerylst.get(0);
				if (sourceQueryDet.getQueryProc().contains("#")) {
					sourceQueryDet.setQueryProc(replaceFilterHashVariables(sourceQueryDet.getQueryProc(), vObject));
					exceptionCode = reportsDao.getFilterPromptWithHashVar(vObject, sourceQueryDet, "TREE");
				} else {
					exceptionCode = reportsDao.getTreePromptData(vObject, sourceQueryDet);
				}
				if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION
						&& exceptionCode.getResponse() != null) {
					promptTree = (List<PromptTreeVb>) exceptionCode.getResponse();
					promptTree = createPraentChildRelations(promptTree, vObject.getFilterString());
					exceptionCode.setResponse(promptTree);
					exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
					exceptionCode.setErrorMsg("successful operation");
				}
			} else {
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Query not maintained for the DATA_REF_ID " + vObject.getFilterSourceId());
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public List<PromptTreeVb> createPraentChildRelations(List<PromptTreeVb> promptTreeList, String filterString) {
		DeepCopy<PromptTreeVb> deepCopy = new DeepCopy<PromptTreeVb>();
		List<PromptTreeVb> lResult = new ArrayList<PromptTreeVb>(0);
		List<PromptTreeVb> promptTreeListCopy = new CopyOnWriteArrayList<PromptTreeVb>(
				deepCopy.copyCollection(promptTreeList));
		// Top Roots are added.
		for (PromptTreeVb promptVb : promptTreeListCopy) {
			if (promptVb.getField1().equalsIgnoreCase(promptVb.getField3())) {
				lResult.add(promptVb);
				promptTreeListCopy.remove(promptVb);
			}
		}
		// For each top node add all child's and to that child's add sub child's
		// recursively.
		for (PromptTreeVb promptVb : lResult) {
			addChilds(promptVb, promptTreeListCopy);
		}
		// Get the sub tree from the filter string if filter string is not null.
		if (ValidationUtil.isValid(filterString)) {
			lResult = getSubTreeFrom(filterString, lResult);
		}
		// set the empty lists to null. this is required for UI to display the leaf
		// nodes properly.
		nullifyEmptyList(lResult);
		return lResult;
	}

	private void addChilds(PromptTreeVb vObject, List<PromptTreeVb> promptTreeListCopy) {
		for (PromptTreeVb promptTreeVb : promptTreeListCopy) {
			if (vObject.getField1().equalsIgnoreCase(promptTreeVb.getField3())) {
				if (vObject.getChildren() == null) {
					vObject.setChildren(new ArrayList<PromptTreeVb>(0));
				}
				vObject.getChildren().add(promptTreeVb);
				addChilds(promptTreeVb, promptTreeListCopy);
			}
		}
	}

	private List<PromptTreeVb> getSubTreeFrom(String filterString, List<PromptTreeVb> result) {
		List<PromptTreeVb> lResult = new ArrayList<PromptTreeVb>(0);
		for (PromptTreeVb promptTreeVb : result) {
			if (promptTreeVb.getField1().equalsIgnoreCase(filterString)) {
				lResult.add(promptTreeVb);
				return lResult;
			} else if (promptTreeVb.getChildren() != null) {
				lResult = getSubTreeFrom(filterString, promptTreeVb.getChildren());
				if (lResult != null && !lResult.isEmpty())
					return lResult;
			}
		}
		return lResult;
	}

	private void nullifyEmptyList(List<PromptTreeVb> lResult) {
		for (PromptTreeVb promptTreeVb : lResult) {
			if (promptTreeVb.getChildren() != null) {
				nullifyEmptyList(promptTreeVb.getChildren());
			}
			if (promptTreeVb.getChildren() != null && promptTreeVb.getChildren().isEmpty()) {
				promptTreeVb.setChildren(null);
			}
		}
	}

	public ExceptionCode createCBReport(ReportsVb reportsVb) {
		ExceptionCode exceptionCode = new ExceptionCode();
		FileOutputStream fileOS = null;
		File lfile = null;
		File lfileRs = null;
		String fileNames = "";
		String tmpFileName = "";
		String filesNameslst[] = null;
		ReportsVb vObject = new ReportsVb();
		PromptTreeVb promptTree = new PromptTreeVb();
		String destFilePath = System.getProperty("java.io.tmpdir");
		if (!ValidationUtil.isValid(destFilePath))
			destFilePath = System.getenv("TMP");
		if (ValidationUtil.isValid(destFilePath))
			destFilePath = destFilePath + File.separator;
		try {
			exceptionCode = getReportDetails(reportsVb);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				vObject = (ReportsVb) exceptionCode.getResponse();
				if (ValidationUtil.isValid(vObject))
					promptTree = vObject.getPromptTree();
			} else if (exceptionCode.getErrorCode() == Constants.NO_RECORDS_FOUND) {
				exceptionCode.setErrorMsg("No Records Found");
				return exceptionCode;
			} else {
				return exceptionCode;
			}

			if (promptTree == null || "1".equalsIgnoreCase(promptTree.getStatus())) {

			}
			lfile = createTemplateFile(reportsVb);
			lfileRs = lfile;
			OPCPackage pkg = OPCPackage.open(new FileInputStream(lfile.getAbsolutePath()));
			XSSFWorkbook workbook = new XSSFWorkbook(pkg);
			FileOutputStream fileOS1 = new FileOutputStream(lfile);
			workbook.write(fileOS1);
			pkg = OPCPackage.open(new FileInputStream(lfile.getAbsolutePath()));
			workbook = new XSSFWorkbook(pkg);
			XSSFCellStyle cs = (XSSFCellStyle) workbook.createCellStyle();
			cs.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
			cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setColor(IndexedColors.WHITE.getIndex());
			font.setBold(true);
			cs.setFont(font);

			long min = 0;
			long max = 1000;
			workbook.setForceFormulaRecalculation(true);
			List<RCReportFieldsVb> results = null;
			// logger.info("Get CB Report Data Begin["+reportsVb.getReportId()+"]Session
			// Id["+promptTree.getSessionId()+"]Record Count ["+min+"-"+max+"]");
			results = reportsDao.getCBKReportData(reportsVb, promptTree, min, max);
			// logger.info("Get CB Report Data End["+reportsVb.getReportId()+"]Session
			// Id["+promptTree.getSessionId()+"] results size :["+results.size()+"]");
			logger.info("Get CB Report Data End[" + reportsVb.getReportId() + "]Session Id[" + promptTree.getSessionId()
					+ "]Record Count [" + min + "-" + max + "]");
			String tabId = "";
			do {
				for (RCReportFieldsVb result : results) {
					/*
					 * String actualFile = lfile.getName(); String fileName =
					 * actualFile.substring(0, actualFile.lastIndexOf("_"));
					 * if(ValidationUtil.isValid(result.getExcelFileName()) &&
					 * !result.getExcelFileName().equalsIgnoreCase(fileName)){ FileOutputStream
					 * fileOS1 = new FileOutputStream(lfile); workbook.write(fileOS1); String
					 * excelFileName = result.getExcelFileName();
					 * if(excelFileName.toUpperCase().contains(".XLSX"))
					 * result.setExcelFileName(excelFileName.substring(0,
					 * excelFileName.toUpperCase().indexOf(".XLSX"))); lfile = new
					 * File(destFilePath+result.getExcelFileName()+"_"+CustomContextHolder.
					 * getContext().getVisionId()+".xlsx"); String filePath =
					 * lfile.getAbsolutePath(); filePath = filePath.substring(0,
					 * filePath.indexOf(result.getExcelFileName())); if(filePath.contains("temp"))
					 * filePath = filePath.substring(0, filePath.indexOf("temp"));
					 * if(!lfile.exists()) ExcelExportUtil.createTemplateFile(lfile);
					 * 
					 * if(!tmpFileName.contains(excelFileName)){
					 * if(!ValidationUtil.isValid(fileNames)){ fileNames = lfile.toString(); }else{
					 * fileNames = fileNames+"#"+lfile.toString(); }
					 * if(!ValidationUtil.isValid(tmpFileName)){ tmpFileName = excelFileName; }else{
					 * tmpFileName = tmpFileName+"#"+excelFileName; } } pkg = OPCPackage.open( new
					 * FileInputStream(lfile.getAbsolutePath())); workbook = new XSSFWorkbook(pkg);
					 * cs = (XSSFCellStyle)workbook.createCellStyle(); byte[] greenClr = {(byte) 0,
					 * (byte) 92, (byte) 140}; XSSFColor greenXClor = new XSSFColor(greenClr);
					 * cs.setFillForegroundColor(greenXClor);
					 * cs.setFillPattern(CellStyle.SOLID_FOREGROUND); font= workbook.createFont();
					 * font.setColor(IndexedColors.WHITE.getIndex()); font.setBold(true);
					 * cs.setFont(font); }
					 */
					int noOfsheets = workbook.getNumberOfSheets();
					tabId = result.getTabelId();
					if (Integer.parseInt(result.getTabelId()) > (noOfsheets - 1)) {
						workbook.createSheet(result.getTabelId());
					}
					XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(result.getTabelId()));
					Row row = null;
					row = sheet.getRow(Integer.parseInt(result.getRowId()) - 1);
					if (row == null) {
						row = sheet.createRow(Integer.parseInt(result.getRowId()) - 1);
					}
					Cell cell = row.getCell(Integer.parseInt(result.getColId()));
					if (cell == null) {
						cell = row.createCell(Integer.parseInt(result.getColId()));
					}
					if (cell == null || row == null) {
						throw new RuntimeCustomException("Invalid Report data,Tab ID[" + result.getTabelId()
								+ "] Row Id[" + Integer.parseInt(result.getRowId()) + "], Col Id["
								+ Integer.parseInt(result.getColId()) + "] does not exists in template.");
					}
					if (!ValidationUtil.isValid(result.getValue1())) {

					} else if (("C".equalsIgnoreCase(result.getColType())
							|| "N".equalsIgnoreCase(result.getColType()))) {
						cell.setCellValue(Double.parseDouble(result.getValue1()));
					} else if ("F".equalsIgnoreCase(result.getColType())) {
						cell.setCellType(Cell.CELL_TYPE_FORMULA);
						cell.setCellFormula(result.getValue1());
					} else {
						cell.setCellValue(result.getValue1());
					}
					/*
					 * if(ValidationUtil.isValid(result.getSheetName())){
					 * workbook.setSheetName(Integer.parseInt(result.getTabelId()),result.
					 * getSheetName()); }
					 */
					if (ValidationUtil.isValid(result.getRowStyle())) {
						/*
						 * byte[] greenClr = {(byte) 0, (byte) 92, (byte) 140}; XSSFColor greenXClor =
						 * new XSSFColor(greenClr);
						 */
						if ("FHT".equalsIgnoreCase(result.getRowStyle())) {
							cell.setCellStyle(cs);
						}
						if ("FHTF".equalsIgnoreCase(result.getRowStyle())) {
							sheet.createFreezePane(0, Integer.parseInt(result.getRowId()));
							cell.setCellStyle(cs);
						}
					}
				}
				min = max;
				max += 1000;
				// System.out.println("min : "+min+" max : "+max);
				logger.info("Get CB Report Data Begin[" + reportsVb.getReportId() + "]Session Id["
						+ promptTree.getSessionId() + "]Record Count [" + min + "-" + max + "]");
				results = reportsDao.getCBKReportData(reportsVb, promptTree, min, max);
				logger.info("Get CB Report Data End[" + reportsVb.getReportId() + "]Session Id["
						+ promptTree.getSessionId() + "]Record Count [" + min + "-" + max + "]");
			} while (!results.isEmpty());
			fileOS = new FileOutputStream(lfile);
			workbook.write(fileOS);
			// add list of files to Zip
			String fileslst[] = fileNames.split("#");
			filesNameslst = tmpFileName.split("#");
			if (ValidationUtil.isValid(tmpFileName) && filesNameslst.length == 1) {
				reportsVb.setTemplateId(filesNameslst[0]);
			}
			if (fileslst.length > 1) {
				File f = new File(destFilePath + reportsVb.getReportTitle() + ".zip");
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
				for (int a = 0; a < fileslst.length; a++) {
					FileInputStream fis = new FileInputStream(fileslst[a]);
					File f1 = new File(fileslst[a]);
					String tmpfileName = filesNameslst[a] + ".xlsx";
					ZipEntry e = new ZipEntry("" + tmpfileName);
					out.putNextEntry(e);
					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						out.write(bytes, 0, length);
					}
					fis.close();
					f1.delete();
				}
				out.closeEntry();
				out.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
			logger.error("Exception in CBK Report Generation", e);
			return exceptionCode;
			// throw new RuntimeCustomException(e.getMessage());
		} finally {
			try {
				if (ValidationUtil.isValid(promptTree))
					reportsDao.callProcToCleanUpTables(promptTree);
				if (fileOS != null) {
					fileOS.flush();
					fileOS.close();
					fileOS = null;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		// exceptionCode = CommonUtils.getResultObject("", 1, "", "");
		String fileName = reportsVb.getTemplateId().substring(0, reportsVb.getTemplateId().indexOf(".xlsx"));
		// exceptionCode.setOtherInfo(fileName+"_"+CustomContextHolder.getContext().getVisionId());
		exceptionCode.setOtherInfo(fileName);
		exceptionCode.setResponse(destFilePath);
		if (filesNameslst != null && filesNameslst.length > 1) {
			exceptionCode.setRequest(reportsVb.getReportTitle());
			if (lfileRs.exists()) {
				lfileRs.delete();
			}
		}
		return exceptionCode;
	}

	public File createTemplateFile(ReportsVb reportsVb) {
		File lfile = null;
		FileChannel source = null;
		FileChannel destination = null;
		try {
			String fileName = reportsVb.getTemplateId();
			// fileName = ValidationUtil.encode(fileName.substring(0,
			// fileName.indexOf(".xlsx")));
			fileName = fileName.substring(0, fileName.indexOf(".xlsx"));
			String destFilePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(destFilePath)) {
				destFilePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(destFilePath)) {
				destFilePath = destFilePath + File.separator;
			}
			// lfile = new
			// File(destFilePath+fileName+"_"+CustomContextHolder.getContext().getVisionId()+".xlsx");
			lfile = new File(destFilePath + fileName + ".xlsx");
			String filePath = lfile.getAbsolutePath();
			filePath = filePath.substring(0, filePath.indexOf(fileName));
			if (filePath.contains("temp")) {
				filePath = filePath.substring(0, filePath.indexOf("temp"));
			}
			if (lfile.exists()) {
				lfile.delete();
			}
			String templateFilePath = commonDao.findVisionVariableValue("PRD_CB_TEMPLATE_PATH");
			File lSourceFile = new File(templateFilePath + reportsVb.getTemplateId());
			if (!lSourceFile.exists()) {
				throw new RuntimeCustomException(
						"Invalid Report Configuration, Invalid file name or file does not exists @ " + lSourceFile);
			}
			source = new RandomAccessFile(lSourceFile, "rw").getChannel();
			destination = new RandomAccessFile(lfile, "rw").getChannel();
			long position = 0;
			long count = source.size();
			source.transferTo(position, count, destination);
		} catch (Exception e) {
			throw new RuntimeCustomException("Invalid Report Configuration, Invalid file name or file does not exists");
		} finally {
			if (source != null) {
				try {
					source.close();
				} catch (Exception ex) {
				}
			}
			if (destination != null) {
				try {
					destination.close();
				} catch (Exception ex) {
				}
			}
			logger.info("Template File Successfully Created");
		}
		return lfile;
	}

	private ArrayList<ColumnHeadersVb> formColumnHeader(List<ColumnHeadersVb> orgColList, String hiddenColumn) {

		ArrayList<ColumnHeadersVb> updatedColList = new ArrayList<ColumnHeadersVb>();
		int maxHeaderRow = orgColList.stream().mapToInt(ColumnHeadersVb::getLabelRowNum).max().orElse(0);

		ColumnHeadersVb matchingObj = orgColList.stream()
				.filter(p -> p.getDbColumnName().equalsIgnoreCase(hiddenColumn)).findAny().orElse(null);

		final int hiddenColNum = matchingObj.getLabelColNum();
		final int hiddenRowNum = matchingObj.getLabelRowNum();

		if (maxHeaderRow > 1) {
			int rowNum = hiddenRowNum - 1;
			for (int rnum = rowNum; rowNum >= 1; rowNum--) {
				ColumnHeadersVb Obj = new ColumnHeadersVb();
				try {
					Obj = orgColList.stream()
							.filter(p -> (p.getLabelRowNum() == rnum && p.getLabelColNum() <= hiddenColNum))
							.max(Comparator.comparingInt(ColumnHeadersVb::getLabelColNum)).get();
				} catch (Exception e) {

				}
				for (int i = 0; i < orgColList.size(); i++) {
					if (orgColList.get(i).equals(Obj)) {
						orgColList.get(i).setColspan(orgColList.get(i).getColspan() - 1);
						orgColList.get(i).setNumericColumnNo(99);
						if (orgColList.get(i).getColspan() == 0) {
							orgColList.remove(i);
						}
					}
				}
			}
		}
		for (ColumnHeadersVb colHeaderVb : orgColList) {
			if (colHeaderVb.getLabelColNum() > hiddenColNum) {
				colHeaderVb.setLabelColNum(colHeaderVb.getLabelColNum() - 1);
			}
			if (!colHeaderVb.equals(matchingObj))
				updatedColList.add(colHeaderVb);

		}
		return updatedColList;
	}

	public ExceptionCode getReportFilterSourceValue(ReportFilterVb vObject) {
		// LinkedHashMap<String, String> filterSourceVal = new LinkedHashMap<String,
		// String>();
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			if (!ValidationUtil.isValid(vObject.getDefaultValueId())) {
				vObject.setDefaultValueId(vObject.getFilterSourceId());
			}
			vObject.setDateCreation(String.valueOf(Math.abs(ThreadLocalRandom.current().nextInt())));
			List<PrdQueryConfig> sourceQuerylst = reportsDao.getSqlQuery(vObject.getDefaultValueId());
			if (sourceQuerylst != null && sourceQuerylst.size() > 0) {
				PrdQueryConfig sourceQueryDet = sourceQuerylst.get(0);
				if ("QUERY".equalsIgnoreCase(sourceQueryDet.getDataRefType())) {
					sourceQueryDet.setQueryProc(replaceFilterHashVariables(sourceQueryDet.getQueryProc(), vObject));
					exceptionCode = reportsDao.getReportPromptsFilterValue(sourceQueryDet, null);
				} else if ("PROCEDURE".equalsIgnoreCase(sourceQueryDet.getDataRefType())) {
					if (sourceQueryDet.getQueryProc().contains("#")) {
						sourceQueryDet.setQueryProc(replaceFilterHashVariables(sourceQueryDet.getQueryProc(), vObject));
						exceptionCode = reportsDao.getFilterPromptWithHashVar(vObject, sourceQueryDet, "COMBO");
					} else {
						exceptionCode = reportsDao.getComboPromptData(vObject, sourceQueryDet);
					}
				}
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ExceptionCode exportReportToCsv(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			String reportTitle = "";
			reportTitle = vObject.getReportTitle();
			String[] hiddenColumns = null;
			if (ValidationUtil.isValid(vObject.getColumnsToHide())) {
				hiddenColumns = vObject.getColumnsToHide().split("!@#");
			}
			exceptionCode = getReportDetails(vObject);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					vObject = (ReportsVb) exceptionCode.getResponse();
				}
			} else {
				return exceptionCode;
			}
			List<ColumnHeadersVb> colHeaderslst = vObject.getColumnHeaderslst();
			List<ColumnHeadersVb> updatedLst = new ArrayList();
			List<ColumnHeadersVb> columnHeadersFinallst = new ArrayList<ColumnHeadersVb>();
			colHeaderslst.forEach(colHeadersVb -> {
				if (colHeadersVb.getColspan() <= 1) {
					columnHeadersFinallst.add(colHeadersVb);
				}
			});
			for (ColumnHeadersVb columnHeadersVb : columnHeadersFinallst) {
				if (hiddenColumns != null) {
					for (int ctr = 0; ctr < hiddenColumns.length; ctr++) {
						if (columnHeadersVb.getDbColumnName().equalsIgnoreCase(hiddenColumns[ctr])) {
							updatedLst.add(columnHeadersVb);
							break;
						}
					}
				}
			}

			if (updatedLst != null && !updatedLst.isEmpty()) {
				columnHeadersFinallst.removeAll(updatedLst);
			}
			exceptionCode = getResultData(vObject, true);
			List<HashMap<String, String>> dataLst = null;
			List<HashMap<String, String>> totalLst = null;
			ReportsVb resultVb = new ReportsVb();
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					resultVb = (ReportsVb) exceptionCode.getResponse();
					dataLst = resultVb.getGridDataSet();
					totalLst = resultVb.getTotal();
				}
			}
			vObject.setReportTitle(reportTitle);
			int currentUserId = CustomContextHolder.getContext().getVisionId();
			CreateCsv createCSV = new CreateCsv();
			String filePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(filePath)) {
				filePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(filePath)) {
				filePath = filePath + File.separator;
			}
			FileWriter fw = null;
			fw = new FileWriter(
					filePath + ValidationUtil.encode(vObject.getReportTitle()) + "_" + currentUserId + ".csv");
			PrintWriter out = new PrintWriter(fw);
			int rowNum = 0;
			int ctr = 1;
			String csvSeperator = "";
			if ("\t".equalsIgnoreCase(vObject.getCsvDelimiter()))
				csvSeperator = "	";
			else
				csvSeperator = vObject.getCsvDelimiter();
			rowNum = createCSV.writeHeadersToCsv(columnHeadersFinallst, vObject, fw, rowNum, out, csvSeperator);
			do {
				ctr++;
				rowNum = createCSV.writeDataToCsv(columnHeadersFinallst, dataLst, vObject, currentUserId, fw, rowNum,
						out, csvSeperator);
				vObject.setCurrentPage(ctr);
				dataLst = new ArrayList();
				exceptionCode = getResultData(vObject, true);
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					resultVb = (ReportsVb) exceptionCode.getResponse();
					dataLst = resultVb.getGridDataSet();
				}
			} while (dataLst != null && !dataLst.isEmpty());

			if (totalLst != null)
				rowNum = createCSV.writeDataToCsv(columnHeadersFinallst, totalLst, vObject, currentUserId, fw, rowNum,
						out, csvSeperator);

			out.flush();
			out.close();
			fw.close();
			exceptionCode.setResponse(filePath);
			exceptionCode.setOtherInfo(vObject.getReportTitle() + "_" + currentUserId);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	@SuppressWarnings("unchecked")
	public ExceptionCode getWidgetList() {
		ExceptionCode exceptionCode = new ExceptionCode();
		ArrayList collTemp = new ArrayList();
		ArrayList<ReportFilterVb> filterLst = new ArrayList<>();
		ReportsVb reportVb = new ReportsVb();
		try {

			String defaultPrompt = commonDao.findVisionVariableValue("PRD_WIDGET_FILTER_" + productName + "");
			ExceptionCode globalFilterExcep = reportFilterProcess(defaultPrompt, null);
			ArrayList<ReportFilterVb> globalFilterLst = (ArrayList<ReportFilterVb>) globalFilterExcep.getResponse();
			ArrayList<ReportsVb> widgetlst = (ArrayList<ReportsVb>) reportsDao.getWidgetList();
			if (widgetlst != null && widgetlst.size() > 0) {
				for (ReportsVb reportsVb : widgetlst) {
					StringJoiner filterPosition = new StringJoiner("-");
					if ("Y".equalsIgnoreCase(reportsVb.getFilterFlag())) {
						HashMap<String, Object> filterMapVal = new HashMap<String, Object>();
						ExceptionCode exceptionCodeTemp = new ExceptionCode();
						exceptionCodeTemp = reportFilterProcess(reportsVb.getFilterRefCode(), null);
						if (exceptionCodeTemp.getResponse() != null
								&& exceptionCodeTemp.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
							filterLst = (ArrayList<ReportFilterVb>) exceptionCodeTemp.getResponse();
						}
						if (filterLst != null && !filterLst.isEmpty()) {
							filterLst.forEach(vObj -> {
								// Global Filter Identifier Start
								Boolean globalFlag = false;
								List<ReportFilterVb> matchlst = globalFilterLst.stream()
										.filter(n -> n.getFilterSourceId().equalsIgnoreCase(vObj.getFilterSourceId()))
										.collect(Collectors.toList());

								if (matchlst != null && !matchlst.isEmpty()) {
									vObj.setGlobalFilter(true);
								} else {
									vObj.setGlobalFilter(false);
								}
								// End
								if (!vObj.getGlobalFilter()) {
									ExceptionCode exceptionCodeFilter = getFilterSourceValue(vObj);
									if (exceptionCodeFilter.getResponse() != null)
										vObj.setFilterValueMap(
												(LinkedHashMap<String, String>) exceptionCodeFilter.getResponse());
								}
							});
						}
						reportsVb.setFilterPosition(filterPosition.toString());
						List<ReportFilterVb> filterLstNew = filterLst.stream().filter(n -> n.getGlobalFilter() == false)
								.collect(Collectors.toList());

						int ctr = 1;
						for (ReportFilterVb vObj : filterLstNew) {
							vObj.setFilterSeq(ctr);
							ctr++;
						}
						filterLst.forEach(vObj -> {
							// Global Filter and Local Filter Identifier Start
							Boolean globalFlag = false;
							List<ReportFilterVb> matchlst = globalFilterLst.stream()
									.filter(n -> n.getFilterSourceId().equalsIgnoreCase(vObj.getFilterSourceId()))
									.collect(Collectors.toList());

							List<ReportFilterVb> localMatchlst = filterLstNew.stream()
									.filter(n -> n.getFilterSourceId().equalsIgnoreCase(vObj.getFilterSourceId()))
									.collect(Collectors.toList());

							if (matchlst != null && !matchlst.isEmpty()) {
								filterPosition.add("G" + matchlst.get(0).getFilterSeq());
							} else if (localMatchlst != null && !localMatchlst.isEmpty()) {
								filterPosition.add("L" + localMatchlst.get(0).getFilterSeq());
							}
							// End
						});
						reportsVb.setFilterPosition(filterPosition.toString());
						reportsVb.setReportFilters(filterLstNew);
					}
				}
			}
			collTemp.add(widgetlst);
			collTemp.add(defaultPrompt);
			ArrayList userWidgetlst = (ArrayList<ReportsVb>) reportsDao.getUserWidgets();
			collTemp.add(userWidgetlst);
			ArrayList savedUserDefLst = (ArrayList<ReportsVb>) reportsDao.getSavedUserDefSetting(reportVb, false);
			collTemp.add(savedUserDefLst);
			exceptionCode.setResponse(collTemp);
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ExceptionCode deleteSavedWidget(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		int retVal = 0;
		try {
			if (reportsDao.userWidgetExists(vObject) > 0) {
				retVal = reportsDao.deleteWidgetExists(vObject);
				if (retVal == Constants.ERRONEOUS_OPERATION) {
					exceptionCode.setErrorCode(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
					exceptionCode.setErrorMsg("Attempt to delete non-existant record!!");
				} else {
					exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
					exceptionCode.setErrorMsg("User Preferance Widget Deleted Successfully");
				}
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		exceptionCode.setOtherInfo(vObject);
		return exceptionCode;
	}

	public ExceptionCode saveWidget(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			deleteSavedWidget(vObject);
			int retVal = reportsDao.saveWidget(vObject);
			if (retVal == Constants.ERRONEOUS_OPERATION) {
				exceptionCode.setErrorCode(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
				exceptionCode.setErrorMsg("Unable to save widgets.Contact Admin!!");
			} else {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setErrorMsg("User Preferance Widget Saved Successfully");
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		exceptionCode.setOtherInfo(vObject);
		return exceptionCode;
	}

	public ExceptionCode exportWidgetToPdf(String reportTitle, String promptLabel, String gridReportIds,
			String fileName, ArrayList<ReportsVb> reportslst, String appTheme, String base64Str) {
		ExceptionCode exceptionCode = new ExceptionCode();
		ReportsVb reportsVb = new ReportsVb();
		VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
		try {
			reportsVb.setMaker(visionUsersVb.getVisionId());
			reportsVb.setMakerName(visionUsersVb.getUserName());
			reportsVb.setReportTitle(reportTitle);
			reportsVb.setPromptLabel(promptLabel);
			reportsVb.setApplicationTheme(appTheme);
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			if (ValidationUtil.isValid(gridReportIds)) {
				// Write Code for Grid Export
			}

			String screenCapturedPath = commonDao.findVisionVariableValue("PRD_EXPORT_PATH");
			String capturedImage = screenCapturedPath + fileName + ".png";

			// Converting Base64 to Image file
			String base64ImageString = base64Str.replace("data:image/png;base64,", "");
			byte[] data = DatatypeConverter.parseBase64Binary(base64ImageString);
			File fileImg = new File(capturedImage);
			if (fileImg.exists())
				fileImg.delete();
			fileImg.createNewFile();
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileImg));
			outputStream.write(data);
			outputStream.close();

			exceptionCode = pdfExportUtil.dashboardExportToPdf(capturedImage, reportsVb, assetFolderUrl, fileName,
					reportslst);
			File lFile = new File(capturedImage);
			if (lFile.exists()) {
				lFile.delete();
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ExceptionCode saveReportUserDef(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			deleteSavedReportUserDef(vObject);
			if (vObject.getReportUserDeflst() != null && !vObject.getReportUserDeflst().isEmpty()) {
				vObject.getReportUserDeflst().forEach(userDefVb -> {
					int retVal = reportsDao.saveReportUserDef(userDefVb);
				});
			}
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Widgets saved Successfully");
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		exceptionCode.setOtherInfo(vObject);
		return exceptionCode;
	}

	public ExceptionCode deleteSavedReportUserDef(ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		int retVal = 0;
		try {
			if (vObject.getReportUserDeflst() != null && vObject.getReportUserDeflst().size() > 0) {
				vObject.getReportUserDeflst().forEach(userDefVb -> {
					if (reportsDao.reportUserDefExists(userDefVb) > 0) {
						reportsDao.deleteReportUserDef(userDefVb);
					}
				});
			}
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Widgets deleted Successfully");
			exceptionCode.setOtherInfo(vObject);
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	public ReportsVb replaceProcedurePrompt(ReportsVb vObject) {
		vObject.setPromptValue1(
				ValidationUtil.isValid(vObject.getPromptValue1()) ? vObject.getPromptValue1().replaceAll("','", "'',''")
						: vObject.getPromptValue1());
		vObject.setPromptValue2(
				ValidationUtil.isValid(vObject.getPromptValue2()) ? vObject.getPromptValue2().replaceAll("','", "'',''")
						: vObject.getPromptValue2());
		vObject.setPromptValue3(
				ValidationUtil.isValid(vObject.getPromptValue3()) ? vObject.getPromptValue3().replaceAll("','", "'',''")
						: vObject.getPromptValue3());
		vObject.setPromptValue4(
				ValidationUtil.isValid(vObject.getPromptValue4()) ? vObject.getPromptValue4().replaceAll("','", "'',''")
						: vObject.getPromptValue4());
		vObject.setPromptValue5(
				ValidationUtil.isValid(vObject.getPromptValue5()) ? vObject.getPromptValue5().replaceAll("','", "'',''")
						: vObject.getPromptValue5());
		vObject.setPromptValue6(
				ValidationUtil.isValid(vObject.getPromptValue6()) ? vObject.getPromptValue6().replaceAll("','", "'',''")
						: vObject.getPromptValue6());
		vObject.setPromptValue7(
				ValidationUtil.isValid(vObject.getPromptValue7()) ? vObject.getPromptValue7().replaceAll("','", "'',''")
						: vObject.getPromptValue7());
		vObject.setPromptValue8(
				ValidationUtil.isValid(vObject.getPromptValue8()) ? vObject.getPromptValue8().replaceAll("','", "'',''")
						: vObject.getPromptValue8());
		vObject.setPromptValue9(
				ValidationUtil.isValid(vObject.getPromptValue9()) ? vObject.getPromptValue9().replaceAll("','", "'',''")
						: vObject.getPromptValue9());
		vObject.setPromptValue10(ValidationUtil.isValid(vObject.getPromptValue10())
				? vObject.getPromptValue10().replaceAll("','", "'',''")
				: vObject.getPromptValue10());
		vObject.setDataFilter1(
				ValidationUtil.isValid(vObject.getDataFilter1()) ? vObject.getDataFilter1().replaceAll("','", "'',''")
						: vObject.getDataFilter1());
		vObject.setDataFilter2(
				ValidationUtil.isValid(vObject.getDataFilter2()) ? vObject.getDataFilter2().replaceAll("','", "'',''")
						: vObject.getDataFilter2());
		vObject.setDataFilter3(
				ValidationUtil.isValid(vObject.getDataFilter3()) ? vObject.getDataFilter3().replaceAll("','", "'',''")
						: vObject.getDataFilter3());
		vObject.setDataFilter4(
				ValidationUtil.isValid(vObject.getDataFilter4()) ? vObject.getDataFilter4().replaceAll("','", "'',''")
						: vObject.getDataFilter4());
		vObject.setDataFilter5(
				ValidationUtil.isValid(vObject.getDataFilter5()) ? vObject.getDataFilter5().replaceAll("','", "'',''")
						: vObject.getDataFilter5());
		vObject.setDataFilter6(
				ValidationUtil.isValid(vObject.getDataFilter6()) ? vObject.getDataFilter6().replaceAll("','", "'',''")
						: vObject.getDataFilter6());
		vObject.setDataFilter7(
				ValidationUtil.isValid(vObject.getDataFilter7()) ? vObject.getDataFilter7().replaceAll("','", "'',''")
						: vObject.getDataFilter7());
		vObject.setDataFilter8(
				ValidationUtil.isValid(vObject.getDataFilter8()) ? vObject.getDataFilter8().replaceAll("','", "'',''")
						: vObject.getDataFilter8());
		vObject.setDataFilter9(
				ValidationUtil.isValid(vObject.getDataFilter9()) ? vObject.getDataFilter9().replaceAll("','", "'',''")
						: vObject.getDataFilter9());
		vObject.setDataFilter10(
				ValidationUtil.isValid(vObject.getDataFilter10()) ? vObject.getDataFilter10().replaceAll("','", "'',''")
						: vObject.getDataFilter10());
		return vObject;
	}

	public ExceptionCode saveReportSettings(ReportUserDefVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			if (reportsDao.reportUserDefExists(vObject) > 0)
				reportsDao.deleteReportUserDef(vObject);
			int retVal = reportsDao.saveReportUserDef(vObject);
			if (retVal == Constants.SUCCESSFUL_OPERATION) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setErrorMsg("Report settings saved Successfully");
			}
		} catch (Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		exceptionCode.setOtherInfo(vObject);
		return exceptionCode;
	}

	public List<ReportsVb> createPraentChildReportsTree(List<ReportsVb> reportList, String filterString) {
		DeepCopy<ReportsVb> deepCopy = new DeepCopy<ReportsVb>();
		List<ReportsVb> lResult = new ArrayList<ReportsVb>(0);
		List<ReportsVb> reportTreeListCopy = new CopyOnWriteArrayList<ReportsVb>(deepCopy.copyCollection(reportList));
		// Top Roots are added.
		for (ReportsVb promptVb : reportTreeListCopy) {
			if (promptVb.getReportCategory().equalsIgnoreCase(promptVb.getSubCategory())) {
				lResult.add(promptVb);
				reportTreeListCopy.remove(promptVb);
			}
		}
		// For each top node add all child's and to that child's add sub child's
		// recursively.
		for (ReportsVb reportsVb : lResult) {
			addChildsReportSuite(reportsVb, reportTreeListCopy);
		}
		nullifyEmptyListReport(lResult);
		return lResult;
	}

	private void addChildsReportSuite(ReportsVb vObject, List<ReportsVb> reportTreeListCopy) {
		for (ReportsVb promptTreeVb : reportTreeListCopy) {
			if (vObject.getSubCategory().equalsIgnoreCase(promptTreeVb.getReportCategory())) {
				if (vObject.getChildren() == null) {
					vObject.setChildren(new ArrayList<ReportsVb>(0));
				}
				vObject.getChildren().add(promptTreeVb);
				addChildsReportSuite(promptTreeVb, reportTreeListCopy);
			}
		}
	}

	private void nullifyEmptyListReport(List<ReportsVb> lResult) {
		for (ReportsVb reportsVb : lResult) {
			if (reportsVb.getChildren() != null) {
				nullifyEmptyListReport(reportsVb.getChildren());
			}
			if (reportsVb.getChildren() != null && reportsVb.getChildren().isEmpty()) {
				reportsVb.setChildren(null);
			}
		}
	}

	public String pfPromptHashReplace(String query, String restrictStr, String restrictVal) {
		try {
			String replaceStr = "";
			String orgSbuStr = StringUtils.substringBetween(query, restrictStr, ")#");
			if (ValidationUtil.isValid(restrictVal) && !"'ALL'".equalsIgnoreCase(restrictVal))
				replaceStr = " AND " + orgSbuStr + " IN (" + restrictVal + ")";
			restrictStr = restrictStr.replace("(", "\\(");
			orgSbuStr = orgSbuStr.replace("|", "\\|");
			query = query.replaceAll(restrictStr + orgSbuStr + "\\)#", replaceStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	public String applyPrPromptChange(String sqlQuery, ReportsVb promptVb) {
		VisionUsersVb visionUserVb = CustomContextHolder.getContext();
		// VU_CLEB,VU_CLEB_AO,VU_CLEB_LV,VU_SBU,VU_PRODUCT,VU_OUC
		if (sqlQuery.contains("#PF_PROMPT_VALUE_1"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_1(", promptVb.getPromptValue1());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_2"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_2(", promptVb.getPromptValue2());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_3"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_3(", promptVb.getPromptValue3());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_4"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_4(", promptVb.getPromptValue4());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_5"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_5(", promptVb.getPromptValue5());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_6"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_6(", promptVb.getPromptValue6());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_7"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_7(", promptVb.getPromptValue7());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_8"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_8(", promptVb.getPromptValue8());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_9"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_9(", promptVb.getPromptValue9());
		if (sqlQuery.contains("#PF_PROMPT_VALUE_10"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_10(", promptVb.getPromptValue10());

		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_1"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_1(",
					promptVb.getPromptValue1().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_2"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_2(",
					promptVb.getPromptValue2().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_3"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_3(",
					promptVb.getPromptValue3().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_4"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_4(",
					promptVb.getPromptValue4().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_5"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_5(",
					promptVb.getPromptValue5().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_6"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_6(",
					promptVb.getPromptValue6().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_7"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_7(",
					promptVb.getPromptValue7().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_8"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_8(",
					promptVb.getPromptValue8().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_9"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_9(",
					promptVb.getPromptValue9().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_PROMPT_VALUE_10"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_PROMPT_VALUE_10(",
					promptVb.getPromptValue10().replaceAll("'", ""));

		if (sqlQuery.contains("#PF_DDKEY1"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY1(", promptVb.getDrillDownKey1());
		if (sqlQuery.contains("#PF_DDKEY2"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY2(", promptVb.getDrillDownKey2());
		if (sqlQuery.contains("#PF_DDKEY3"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY3(", promptVb.getDrillDownKey3());
		if (sqlQuery.contains("#PF_DDKEY4"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY4(", promptVb.getDrillDownKey4());
		if (sqlQuery.contains("#PF_DDKEY5"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY5(", promptVb.getDrillDownKey5());
		if (sqlQuery.contains("#PF_DDKEY6"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY6(", promptVb.getDrillDownKey6());
		if (sqlQuery.contains("#PF_DDKEY7"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY7(", promptVb.getDrillDownKey7());
		if (sqlQuery.contains("#PF_DDKEY8"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY8(", promptVb.getDrillDownKey8());
		if (sqlQuery.contains("#PF_DDKEY9"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY9(", promptVb.getDrillDownKey9());
		if (sqlQuery.contains("#PF_DDKEY10"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY10(", promptVb.getDrillDownKey10());
		if (sqlQuery.contains("#PF_DDKEY0"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY10(", promptVb.getDrillDownKey0());

		if (sqlQuery.contains("#PF_NS_DDKEY1"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY1(", promptVb.getDrillDownKey1().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY2"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY2(", promptVb.getDrillDownKey2().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY3"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY3(", promptVb.getDrillDownKey3().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY4"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY4(", promptVb.getDrillDownKey4().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY5"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY5(", promptVb.getDrillDownKey5().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY6"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY6(", promptVb.getDrillDownKey6().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY7"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY7(", promptVb.getDrillDownKey7().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY8"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY8(", promptVb.getDrillDownKey8().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY9"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY9(", promptVb.getDrillDownKey9().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY10"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY10(", promptVb.getDrillDownKey10().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_DDKEY0"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY10(", promptVb.getDrillDownKey0().replaceAll("'", ""));

		if (sqlQuery.contains("#PF_LOCAL_FILTER_1"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_1(", promptVb.getDataFilter1());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_2"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_2(", promptVb.getDataFilter2());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_3"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_3(", promptVb.getDataFilter3());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_4"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_4(", promptVb.getDataFilter4());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_5"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_5(", promptVb.getDataFilter5());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_6"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_6(", promptVb.getDataFilter6());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_7"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_7(", promptVb.getDataFilter7());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_8"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_8(", promptVb.getDataFilter8());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_9"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_9(", promptVb.getDataFilter9());
		if (sqlQuery.contains("#PF_LOCAL_FILTER_10"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER_10(", promptVb.getDataFilter10());

		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_1"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_1(",
					promptVb.getDataFilter1().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_2"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_2(",
					promptVb.getDataFilter2().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_3"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_3(",
					promptVb.getDataFilter3().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_4"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_4(",
					promptVb.getDataFilter4().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_5"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_5(",
					promptVb.getDataFilter5().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_6"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_6(",
					promptVb.getDataFilter6().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_7"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_7(",
					promptVb.getDataFilter7().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_8"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_8(",
					promptVb.getDataFilter8().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_9"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_9(",
					promptVb.getDataFilter9().replaceAll("'", ""));
		if (sqlQuery.contains("#PF_NS_LOCAL_FILTER_10"))
			sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_NS_LOCAL_FILTER_10(",
					promptVb.getDataFilter10().replaceAll("'", ""));

		return sqlQuery;
	}

	public String applySpecialPrompts(String sqlQuery, ReportsVb promptVb) {
		try {
			String promptArray[][] = new String[10][10];
			if (ValidationUtil.isValid(promptVb.getPromptValue1())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue1(), sqlQuery, promptArray, 0);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue2())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue2(), sqlQuery, promptArray, 1);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue3())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue3(), sqlQuery, promptArray, 2);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue4())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue4(), sqlQuery, promptArray, 3);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue5())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue5(), sqlQuery, promptArray, 4);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue6())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue6(), sqlQuery, promptArray, 5);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue7())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue7(), sqlQuery, promptArray, 6);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue8())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue8(), sqlQuery, promptArray, 7);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue9())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue9(), sqlQuery, promptArray, 8);
			}
			if (ValidationUtil.isValid(promptVb.getPromptValue10())) {
				promptArray = commonDao.PromptSplit(promptVb.getPromptValue10(), sqlQuery, promptArray, 9);
			}
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < 11; j++) {
					if (ValidationUtil.isValid(promptArray[i - 1][j - 1])) {
						if (sqlQuery.contains("#PROMPT_VALUE_" + i + "." + j + "#"))
							sqlQuery = sqlQuery.replace("#PROMPT_VALUE_" + i + "." + j + "#",
									promptArray[i - 1][j - 1]);
						if (sqlQuery.contains("#NS_PROMPT_VALUE_" + i + "." + j + "#"))
							sqlQuery = sqlQuery.replace("#NS_PROMPT_VALUE_" + i + "." + j + "#",
									promptArray[i - 1][j - 1].replaceAll("'", ""));
						if (sqlQuery.contains("#PF_PROMPT_VALUE_" + i + "." + j + "#"))
							sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_PROMPT_VALUE_" + i + "." + j + "(",
									promptArray[i - 1][j - 1]);
					} else {
						sqlQuery = sqlQuery.replace("#PROMPT_VALUE_" + i + "." + j + "#", "NULL");
						sqlQuery = sqlQuery.replace("#NS_PROMPT_VALUE_" + i + "." + j + "#", "NULL");
					}
				}
			}

			String promptDrillDwnArray[][] = new String[10][10];
			if (ValidationUtil.isValid(promptVb.getDrillDownKey1())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey1(), sqlQuery, promptDrillDwnArray,
						0);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey2())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey2(), sqlQuery, promptDrillDwnArray,
						1);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey3())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey3(), sqlQuery, promptDrillDwnArray,
						2);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey4())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey4(), sqlQuery, promptDrillDwnArray,
						3);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey5())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey5(), sqlQuery, promptDrillDwnArray,
						4);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey6())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey6(), sqlQuery, promptDrillDwnArray,
						5);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey7())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey7(), sqlQuery, promptDrillDwnArray,
						6);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey8())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey8(), sqlQuery, promptDrillDwnArray,
						7);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey9())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey9(), sqlQuery, promptDrillDwnArray,
						8);
			}
			if (ValidationUtil.isValid(promptVb.getDrillDownKey10())) {
				promptDrillDwnArray = commonDao.PromptSplit(promptVb.getDrillDownKey10(), sqlQuery, promptDrillDwnArray,
						9);
			}
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < 11; j++) {
					if (ValidationUtil.isValid(promptDrillDwnArray[i - 1][j - 1])) {
						if (sqlQuery.contains("#DDKEY" + i + "." + j + "#"))
							sqlQuery = sqlQuery.replace("#DDKEY" + i + "." + j + "#",
									promptDrillDwnArray[i - 1][j - 1]);
						if (sqlQuery.contains("#NS_DDKEY" + i + "." + j + "#"))
							sqlQuery = sqlQuery.replace("#NS_DDKEY" + i + "." + j + "#",
									promptDrillDwnArray[i - 1][j - 1].replaceAll(",", ""));
						if (sqlQuery.contains("#PF_DDKEY" + i + "." + j + "#"))
							sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_DDKEY" + i + "." + j + "(",
									promptDrillDwnArray[i - 1][j - 1]);
					} else {
						sqlQuery = sqlQuery.replace("#DDKEY" + i + "." + j + "#", "NULL");
						sqlQuery = sqlQuery.replace("#NS_DDKEY" + i + "." + j + "#", "NULL");
					}
				}
			}

			String promptLocalFiltArray[][] = new String[10][10];
			if (ValidationUtil.isValid(promptVb.getDataFilter1())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter1(), sqlQuery, promptLocalFiltArray,
						0);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter2())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter2(), sqlQuery, promptLocalFiltArray,
						1);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter3())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter3(), sqlQuery, promptLocalFiltArray,
						2);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter4())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter4(), sqlQuery, promptLocalFiltArray,
						3);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter5())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter5(), sqlQuery, promptLocalFiltArray,
						4);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter6())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter6(), sqlQuery, promptLocalFiltArray,
						5);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter7())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter7(), sqlQuery, promptLocalFiltArray,
						6);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter8())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter8(), sqlQuery, promptLocalFiltArray,
						7);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter9())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter9(), sqlQuery, promptLocalFiltArray,
						8);
			}
			if (ValidationUtil.isValid(promptVb.getDataFilter10())) {
				promptLocalFiltArray = commonDao.PromptSplit(promptVb.getDataFilter10(), sqlQuery, promptLocalFiltArray,
						9);
			}
			for (int i = 1; i < 11; i++) {
				for (int j = 1; j < 11; j++) {
					if (ValidationUtil.isValid(promptLocalFiltArray[i - 1][j - 1])) {
//						if(sqlQuery.contains("#LOCAL_FILTER"+i+"."+j+"#"))
						sqlQuery = sqlQuery.replace("#LOCAL_FILTER" + i + "." + j + "#",
								promptLocalFiltArray[i - 1][j - 1]);
						if (sqlQuery.contains("#NS_LOCAL_FILTER" + i + "." + j + "#"))
							sqlQuery = sqlQuery.replace("#NS_LOCAL_FILTER" + i + "." + j + "#",
									promptLocalFiltArray[i - 1][j - 1].replaceAll(",", ""));
						if (sqlQuery.contains("#PF_LOCAL_FILTER" + i + "." + j + "#"))
							sqlQuery = pfPromptHashReplace(sqlQuery, "#PF_LOCAL_FILTER" + i + "." + j + "(",
									promptLocalFiltArray[i - 1][j - 1]);
					} else {
						sqlQuery = sqlQuery.replace("#LOCAL_FILTER" + i + "." + j + "#", "NULL");
						sqlQuery = sqlQuery.replace("#NS_LOCAL_FILTER" + i + "." + j + "#", "NULL");
					}
				}
			}
			return sqlQuery;
		} catch (Exception ex) {
			ex.printStackTrace();
			return sqlQuery;
		}
	}

	public ExceptionCode listReportFilesFromFtpServer(String node, String file, String ext, String path,
			String serverName, int currentUserId) {
		List collTemp = null;
		ReportsVb vObjTab = null;
		ArrayList credentialsList = null;
		ArrayList<Object> arrListLocal = new ArrayList<Object>();
		ExceptionCode exceptionCode = null;
		TelnetConnection telnetConnection = null;
		try {
			logger.info("Report Starts for Type E ");
			String environmentNode = System.getenv("VISION_NODE_NAME");
			if (ValidationUtil.isValid(node)) {
				environmentNode = node;

			}
			String environmentParam = System.getenv("VISION_SERVER_ENVIRONMENT");
			if (!ValidationUtil.isValid(environmentParam))
				environmentParam = "PRODUCTION";
			if (serverName.contains(",")) {
				serverName = serverName.replaceAll(",", "','");
			}
			// KDIC
			/*
			 * environmentParam = "UAT"; environmentNode = "U2";
			 */
			logger.info("environmentParam : " + environmentParam + " environmentNode : " + environmentNode
					+ "serverName : " + serverName);
			if (ValidationUtil.isValid(environmentNode) && !"ALL".equalsIgnoreCase(environmentNode)) {
				collTemp = reportsDao.findServerCredentials(environmentParam, environmentNode, "");
			} else {
				collTemp = reportsDao.findServerCredentials(environmentParam, "", serverName);
			}
			arrListLocal.add(collTemp);

			if (arrListLocal == null || arrListLocal.size() == 0) {
				exceptionCode = CommonUtils.getResultObject(
						"PRD Reports - Environment :" + environmentParam + " - Node :" + environmentNode,
						Constants.NO_RECORDS_FOUND, "", "");
				exceptionCode.getErrorMsg();
				return exceptionCode;
			}
			logger.info("List files from SFTP server Start");
			exceptionCode = listFilesFromSFtpServer(arrListLocal, credentialsList, ext, file, path, currentUserId);
			logger.info("List files from SFTP server Start");
		} catch (Exception e) {
			exceptionCode = CommonUtils.getResultObject("PRD Reports", Constants.FILE_UPLOAD_REMOTE_ERROR, "Download",
					"");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String res = errors.toString();
			String segments[] = res.split("@-:@");
			String errorDesc = segments[1];

			if (ValidationUtil.isValid(errorDesc)) {
				exceptionCode = CommonUtils.getResultObject("PRD Reports - " + errorDesc,
						Constants.FILE_UPLOAD_REMOTE_ERROR, "Download", "");
			}
			throw new RuntimeCustomException(exceptionCode);
		} finally {
			if (telnetConnection != null && telnetConnection.isConnected()) {
				telnetConnection.disconnect();
				telnetConnection = null;
			}
		}
		logger.info("Report End for Type E ");
		return exceptionCode;
	}

	public class MyUserInfo implements UserInfo {
		public String getPassword() {
			return password;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
			return;
		}
	}

	public ExceptionCode listFilesFromSFtpServer(ArrayList<Object> arrListLocal, ArrayList credentialsList, String ext,
			String exactFile, String path, int currentUserId) {
		ExceptionCode exceptionCode = null;
		ReportsVb vObjTab = null;
		exactFile = exactFile.replaceAll("'", "");
		String[] extArr = ext.split(",");
		String extension = "";
		String destPath = "";
		try {
			// exactFile = "ALL";
			String filePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(filePath)) {
				filePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(filePath)) {
				filePath = filePath + File.separator;
			}
			logger.info("temp path : " + filePath);
			String checkFile = "";
			for (int arr = 0; arr < arrListLocal.size(); arr++) {
				credentialsList = (ArrayList) arrListLocal.get(arr);
				if (credentialsList != null && credentialsList.size() > 0) {
					Calendar timestamp = null;
					for (int Ctr = 0; Ctr < credentialsList.size(); Ctr++) {
						vObjTab = (ReportsVb) credentialsList.get(Ctr);

						JSch jsch = new JSch();
						Session session = jsch.getSession(vObjTab.getUserName(), vObjTab.getHostName());
						{
							UserInfo ui = new MyUserInfo();
							session.setUserInfo(ui); // OR non-interactive version. Relies in host key being in
														// known-hosts file
							session.setPassword(vObjTab.getPassword());
						}
						session.setConfig("StrictHostKeyChecking", "no");
						session.connect();

						Channel channel = session.openChannel("sftp");
						channel.connect();
						logger.info("Channel Connected");
						ChannelSftp sftpChannel = (ChannelSftp) channel;
						sftpChannel.cd(path);

						Vector<ChannelSftp.LsEntry> vtc = sftpChannel.ls("*.*");

						Map<String, String> fileMap = new HashMap();
						for (ChannelSftp.LsEntry lsEntry : vtc) {
							fileMap.put(lsEntry.getFilename(), lsEntry.getFilename());
						}
						logger.info("File Exists in Host[" + vObjTab.getHostName() + "] User Name :["
								+ vObjTab.getUserName() + "] Password :[" + vObjTab.getPassword() + "] File name ["
								+ exactFile + "]");
						if (fileMap != null && fileMap.size() > 0) {
							if (ValidationUtil.isValid(exactFile) && !"ALL".equalsIgnoreCase(exactFile)) {
								for (String extS : extArr) {
									extension = extS;
									checkFile = fileMap.get(exactFile + "." + extension);
									if (ValidationUtil.isValid(checkFile))
										break;
								}
								if (!ValidationUtil.isValid(checkFile)) {
									continue;
								} else {
									destPath = filePath + ValidationUtil.encode(exactFile) + "_" + currentUserId + "."
											+ extension;
									logger.info("dest Path : " + exactFile + "." + extension + destPath);
									try {
										sftpChannel.get(exactFile + "." + extension, destPath);
									} catch (SftpException e) {
										sftpChannel.exit();
										sftpChannel.disconnect();
										exceptionCode = CommonUtils.getResultObject(SERVICE_NAME,
												Constants.FILE_UPLOAD_REMOTE_ERROR, "Download",
												"@-:@" + vObjTab.getHostName() + "@-:@");
										exceptionCode.setRequest(e.getMessage());
										return exceptionCode;
									}
									sftpChannel.exit();
									sftpChannel.disconnect();
								}
								logger.info("channel Disconnected");
								session.disconnect();
								break;
							} else {
								File f1 = null;
								destPath = filePath + File.separator;
								logger.info("dest Path : " + destPath);
								f1 = new File(filePath + "FilesToZip");
								if (f1.exists())
									f1.delete();
								f1.mkdir();
								FileOutputStream fos = new FileOutputStream(destPath + File.separator + "Files.zip");
								ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
								String key = "";
								String value = "";
								for (Map.Entry map : fileMap.entrySet()) {
									key = (String) map.getKey();
									value = (String) map.getValue();
									sftpChannel.get(key, f1.getAbsolutePath());
									FileInputStream fis = new FileInputStream(
											destPath + "FilesToZip" + File.separator + key);
									ZipEntry ze = new ZipEntry(key);
									zipOut.putNextEntry(ze);
									byte[] tmp = new byte[4 * 1024];
									int size = 0;
									while ((size = fis.read(tmp)) != -1) {
										zipOut.write(tmp, 0, size);
									}
									fis.close();
								}
								zipOut.close();
								fos.close();
								f1.delete();
								sftpChannel.exit();
								sftpChannel.disconnect();
								logger.info("channel Disconnected");
								session.disconnect();
								break;
							}
						} else {
							exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
							exceptionCode.setErrorMsg("File not Found..!");
							return exceptionCode;
						}
					}
					exceptionCode = CommonUtils.getResultObject("", 1, "", "");
					exceptionCode.setResponse(filePath);
					exceptionCode.setRequest(exactFile + "_" + currentUserId);
					exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				} else {
					if (!ValidationUtil.isValid(exactFile)) {
						exceptionCode = CommonUtils.getResultObject(SERVICE_NAME + " - Credentials",
								Constants.NO_RECORDS_FOUND, "", "");
						exceptionCode.setRequest(exceptionCode.getErrorMsg());
						return exceptionCode;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			exceptionCode = CommonUtils.getResultObject(SERVICE_NAME, Constants.FILE_UPLOAD_REMOTE_ERROR, "Download",
					"@-:@FTP -" + vObjTab.getHostName() + "@-:@");
			throw new RuntimeCustomException(exceptionCode);
		}
		return exceptionCode;
	}

	public String replacePromptValues(String promptVal, ReportsVb vObj) {
		String promptValue = "";
		try {
			switch (promptVal) {
			case "#PROMPT_VALUE_1#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue1())
						? vObj.getPromptValue1().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_2#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue2())
						? vObj.getPromptValue2().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_3#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue3())
						? vObj.getPromptValue3().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_4#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue4())
						? vObj.getPromptValue4().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_5#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue5())
						? vObj.getPromptValue5().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_6#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue6())
						? vObj.getPromptValue6().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_7#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue7())
						? vObj.getPromptValue7().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_8#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue8())
						? vObj.getPromptValue8().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_9#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue9())
						? vObj.getPromptValue9().replaceAll("'", "")
						: "";
				break;
			case "#PROMPT_VALUE_10#":
				promptValue = ValidationUtil.isValid(vObj.getPromptValue10())
						? vObj.getPromptValue10().replaceAll("'", "")
						: "";
				break;
			}
		} catch (Exception e) {

		}
		return promptValue;
	}

	public String replaceBudgetPrompts(String query, ReportFilterVb vObject) {
		try {
			String[] filterVal = new String[4];
			if (vObject.getFilterRefCode().contains("-")) {
				filterVal = vObject.getFilterRefCode().split("-");
			} else {
				filterVal[0] = vObject.getFilterRefCode().substring(0, 2);
				filterVal[1] = vObject.getFilterRefCode().substring(2, 4);
				filterVal[2] = vObject.getFilterRefCode().substring(4, 8);
				filterVal[3] = vObject.getFilterRefCode().substring(8);
			}
			query = query.replaceAll("#VISION_ID#", "'" + CustomContextHolder.getContext().getVisionId() + "'");
			query = query.replaceAll("#PROMPT_VALUE_1#",
					ValidationUtil.isValid(filterVal[0]) ? "'" + filterVal[0] + "'" : "''");
			query = query.replaceAll("#PROMPT_VALUE_2#",
					ValidationUtil.isValid(filterVal[1]) ? "'" + filterVal[1] + "'" : "''");
			query = query.replaceAll("#PROMPT_VALUE_3#",
					ValidationUtil.isValid(filterVal[2]) ? "'" + filterVal[2] + "'" : "''");
			query = query.replaceAll("#PROMPT_VALUE_4#",
					ValidationUtil.isValid(filterVal[3]) ? "'" + filterVal[3] + "'" : "''");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	public ExceptionCode createCBKReport1(ReportsVb vObject) {
		ExceptionCode exceptionCode = null;
		FileOutputStream fileOS = null;
		File lfile = null;
		File lfileRs = null;
		String fileNames = "";
		String tmpFileName = "";
		String filesNameslst[] = null;
		String destFilePath = System.getProperty("java.io.tmpdir");
		if (!ValidationUtil.isValid(destFilePath))
			destFilePath = System.getenv("TMP");
		if (ValidationUtil.isValid(destFilePath))
			destFilePath = destFilePath + File.separator;
		try {
			ReportsVb reportsVb = new ReportsVb();
			exceptionCode = getReportDetails(vObject);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					reportsVb = (ReportsVb) exceptionCode.getResponse();
				}
			} else {
				return exceptionCode;
			}
			List<List<ColumnHeadersVb>> columnHeadersStgsList = new ArrayList<List<ColumnHeadersVb>>();
			List<List<ReportStgVb>> reportStgList = new ArrayList<List<ReportStgVb>>();
			List<String> rowCol = new ArrayList<>();
			PromptTreeVb promptTree = reportsVb.getPromptTree();
			for (int idx = 1; idx < 7; idx++) {
				PromptTreeVb dObj = new PromptTreeVb();
				dObj.setReportId(promptTree.getReportId());
				dObj.setSessionId(promptTree.getSessionId() + "_" + idx);
				dObj.setColumnHeaderTable(promptTree.getColumnHeaderTable());
				List<ColumnHeadersVb> columnHeaders = reportsDao.getColumnHeaderForCbkWork(dObj);
				columnHeadersStgsList.add(columnHeaders);

				List<ReportStgVb> reportsStgs = reportsDao.getReportsStgMaxData(dObj);
				if (idx != 1)
					reportStgList.add(reportsStgs);
				else
					reportStgList.add(new ArrayList<ReportStgVb>());

				int maxOfRowLevels = reportsDao.getMaxOfRowsInHeader(dObj);
				int maxOfColumnCount = reportsDao.getMaxOfColumCountInHeader(dObj);
				rowCol.add(maxOfRowLevels + "-" + maxOfColumnCount);
			}
			promptTree.setSessionId(promptTree.getSessionId() + "_0");
			List<ReportStgVb> reportOtherList = reportsDao.getReportsOtherData(promptTree);

			System.out.println("list : " + columnHeadersStgsList.size());
			System.out.println("reportStgList : " + reportStgList.size());
			System.out.println("reportOtherList : " + reportOtherList.size());
//			XWPFDocument document = new XWPFDocument();
//			 CustomXWPFDocument document = new CustomXWPFDocument();

			File file = createTemplateDocFile(vObject);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			writeStaticContent(document, reportOtherList);
//			FileOutputStream out = new FileOutputStream(new File("C:\\cb_word\\Prakash_Final.docx"));			 
			FileOutputStream out = new FileOutputStream(file);

			/*
			 * File lFile = new
			 * File(destFilePath+ValidationUtil.encode(reportWriterVb.getReportTitle())+"_"+
			 * CustomContextHolder.getContext().getVisionId()+".docx"); if(lFile.exists()){
			 * lFile.delete(); } lFile.createNewFile(); FileOutputStream out = new
			 * FileOutputStream(lFile);
			 */

			for (int count = 0; count < 6;) {
				List<ColumnHeadersVb> columnHeaders = columnHeadersStgsList.get(count);
				List<ReportStgVb> reportsStgs = reportStgList.get(count);
				String rowColumn = rowCol.get(count);
				String rowColumn1[] = rowColumn.split("-");
				int maxRows = Integer.parseInt(rowColumn1[0]);
				int maxColos = Integer.parseInt(rowColumn1[1]);
				int totalRowsWithStg = reportsStgs.size() + maxRows;
				XWPFTable table = document.createTable(totalRowsWithStg, maxColos);
				table.setCellMargins(100, 100, 0, 100); // (top, left, bottom, right);
				table.setWidth(8000);

				XWPFTableRow tableRow = table.getRow(0);
				// tableRow.setHeight(120);
				int col = 0;
				int row = columnHeaders.get(0).getLabelRowNum();
				for (ColumnHeadersVb columnHeadersVb : columnHeaders) {
					if (columnHeadersVb.getLabelRowNum() != row) {
						tableRow = table.getRow(columnHeadersVb.getLabelRowNum() - 1);
					}
					if ((count == 4 || count == 5) && columnHeadersVb.getLabelColNum() > 8) {
						WriteTableHeaderCell(tableRow.getCell(columnHeadersVb.getLabelColNum() - 1),
								columnHeadersVb.getCaption(), "Times New Roman", 12, "CENTER", 0, true, false, true);
					} else {
						WriteTableHeaderCell(tableRow.getCell(columnHeadersVb.getLabelColNum() - 1),
								columnHeadersVb.getCaption(), "Times New Roman", 12, "CENTER", 0, true, false, false);
					}
					col++;
				}
				for (ColumnHeadersVb columnHeadersVb : columnHeaders) {
					int rowSpan = columnHeadersVb.getRowspan();
					int colSpan = columnHeadersVb.getColspan();
					int rowNum = columnHeadersVb.getLabelRowNum() - 1;
					int colNum = columnHeadersVb.getLabelColNum() - 1;
//		        	 System.out.println("Caption : "+columnHeadersVb.getCaption()+" Row Span : "+rowSpan+" Col Span : "+colSpan+" Row Num : "+rowNum+" Rol Num : "+colNum);
					if (rowSpan != 0 && colSpan != 0) {
						int lastRow = rowNum + columnHeadersVb.getRowspan() - 1;
						int lastCol = colNum + columnHeadersVb.getColspan() - 1;
						mergeCell(table, rowNum, lastRow, colNum, lastCol);
					} else {
						if (rowSpan != 0) {
							int lastRow = rowNum + columnHeadersVb.getRowspan() - 1;
//			        		 System.out.println("Col Row["+colNum+"] Row Start ["+rowNum+"] Row End["+lastRow+"]");
							mergeCellVertically(table, colNum, rowNum, lastRow);
						}
						if (colSpan != 0) {
							int lastCol = colNum + columnHeadersVb.getColspan() - 1;
//			        		 System.out.println("row["+rowNum+"] Col Start ["+colNum+"] Col End["+lastCol+"]");
							// Table , int row, int fromCell, int toCell
							mergeCellHorizontally(table, rowNum, colNum, lastCol);
						}
					}
				}

				String CAP_COL = "captionColumn";
				String DATA_COL = "dataColumn";
				int rowNum = maxRows;
				int r = 0;
				int captionCol = 1;
				if (count > 1)
					captionCol = 2;
				for (ReportStgVb reportStgVb : reportsStgs) {
					tableRow = table.getRow(rowNum);
					for (int loopCount = 0; loopCount < maxColos; loopCount++) {
						int index = 0;
						String type = "";
						if (loopCount < captionCol)
							type = CAP_COL;
						else
							type = DATA_COL;
						if (CAP_COL.equalsIgnoreCase(type)) {
							index = (loopCount + 1);
							String value = ExcelExportUtil.findValue(reportStgVb, type, index);
							WriteTableCell(tableRow.getCell(loopCount), value, "Times New Roman", 12, "LEFT", 0, false,
									false);
						} else {
							index = ((loopCount + 1) - captionCol);
							String value = ExcelExportUtil.findValue(reportStgVb, type, index);
							if ((count == 4 || count == 5) && loopCount > 7) {
								WriteTableCell(tableRow.getCell(loopCount), value, "Times New Roman", 10, "RIGHT", 0,
										false, false, true);
							} else {
								WriteTableCell(tableRow.getCell(loopCount), value, "Times New Roman", 10, "RIGHT", 0,
										false, false);
							}
						}
						widthCellsAcrossRow(table, rowNum, loopCount, 5000);
					}
					r++;
					rowNum++;
				}

				/*
				 * CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
				 * width.setType(STTblWidth.DXA); width.setW(BigInteger.valueOf(10500));
				 */

				if (count == 0) {
					WriteText(document, "2B.	Analysis of Selected Categories of Customer", "Times New Roman", 12,
							"LEFT", 0, true, false, true);
				} else if (count == 1) {
					WriteText(document, "3.	Analysis of Selected Categories of Product/Service", "Times New Roman", 12,
							"LEFT", 0, true, false, true);
				} else if (count == 2) {
					WriteText(document, "b. Value of transactions: (Oct-December 2015)", "Times New Roman", 12, "LEFT",
							0, true, false, true);
				} else if (count == 3) {
					WriteText(document,
							"4.	Geographic Location of Clients—Business Activities in Ksh. M as at _________________",
							"Times New Roman", 12, "LEFT", 0, true, false, true);
				} else if (count > 3) {
					WriteText(document, "", "Times New Roman", 12, "LEFT", 0, true, false, true);
				}
				count++;
			}
			CTDocument1 document1 = document.getDocument();
			CTBody body = document1.getBody();
			if (!body.isSetSectPr()) {
				body.addNewSectPr();
			}
			/*
			 * CTSectPr section = body.getSectPr();
			 * 
			 * if(!section.isSetPgSz()) { section.addNewPgSz(); } CTPageSz pageSize =
			 * section.getPgSz();
			 * 
			 * pageSize.setW(BigInteger.valueOf(15840));
			 * pageSize.setH(BigInteger.valueOf(12240));
			 */
			document.write(out);
			out.close();
			System.out.println("create_table.docx written successully");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in CBK Report Generation", e);
			throw new RuntimeCustomException(e.getMessage());
		} finally {
			try {
				// reportsDao.callProcToCleanUpTables(reportWriterVb);
				if (fileOS != null) {
					fileOS.flush();
					fileOS.close();
					fileOS = null;
				}
			} catch (Exception ex) {
			}
		}
		exceptionCode = CommonUtils.getResultObject("", 1, "", "");
		exceptionCode.setOtherInfo(vObject.getReportTitle());
		return exceptionCode;
	}

	public static void WriteTableCell(XWPFTableCell cell, String text, String fontFamily, int fontSize,
			String alignment, int breaks, boolean isBold, boolean isItalic) {
		// XWPFParagraph paragraph = cell.getParagraphs().get(0);
//			XWPFParagraph paragraph = cell.addParagraph();
		cell.setVerticalAlignment(XWPFVertAlign.BOTTOM);
		XWPFParagraph paragraph = cell.getParagraphs().get(0);
//			paragraph.setWordWrap(true);
		XWPFRun run = paragraph.createRun();
		if ("CENTER".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.CENTER);
		else if ("LEFT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.LEFT);
		else if ("RIGHT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.RIGHT);

		paragraph.setVerticalAlignment(TextAlignment.CENTER);
//			paragraph.setSpacingBeforeLines(10);
//			paragraph.setSpacingBefore(200);
//			paragraph.setSpacingBefore(10);
//		    paragraph.setSpacingAfter(10);
//		    paragraph.setSpacingAfterLines(10);
//		    paragraph.setSpacingBeforeLines(100);
//			cell.getCTTc().getTcPr().getTcW().setW(BigInteger.valueOf((long) 5000));
		if (text.indexOf("|") > 0) {
			String[] lines = text.split("\\|");
			run.setText(lines[0], 0); // set first line into XWPFRun
			for (int i = 1; i < lines.length; i++) {
				// add break and insert new text
				run.addBreak(BreakType.TEXT_WRAPPING);
				run.setText("          " + lines[i]);
//                    run.setText(lines[i], i);
			}
		} else if (text.indexOf("#") > 0) {
			String[] lines = text.split("\\#");
			run.setText(lines[0], 0); // set first line into XWPFRun
			for (int i = 1; i < lines.length; i++) {
				// add break and insert new text
				run.addBreak(BreakType.TEXT_WRAPPING);
				run.setText(" " + lines[i]);
//                    run.setText(lines[i], i);
			}
		} else {
			run.setText(text, 0);
		}
//			run.setText(text);
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		if (isBold)
			run.setBold(true);
		if (isItalic)
			run.setItalic(true);
		paragraph.setWordWrap(true);
	}

	public static void WriteTableCell(XWPFTableCell cell, String text, String fontFamily, int fontSize,
			String alignment, int breaks, boolean isBold, boolean isItalic, boolean isBagroundColor) {
		// XWPFParagraph paragraph = cell.getParagraphs().get(0);
//			XWPFParagraph paragraph = cell.addParagraph();
		cell.setVerticalAlignment(XWPFVertAlign.BOTTOM);
		XWPFParagraph paragraph = cell.getParagraphs().get(0);
//			paragraph.setWordWrap(true);
		XWPFRun run = paragraph.createRun();
		if ("CENTER".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.CENTER);
		else if ("LEFT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.LEFT);
		else if ("RIGHT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.RIGHT);

		paragraph.setVerticalAlignment(TextAlignment.CENTER);
//			paragraph.setSpacingBeforeLines(10);
//			paragraph.setSpacingBefore(200);
//			paragraph.setSpacingBefore(10);
//		    paragraph.setSpacingAfter(10);
//		    paragraph.setSpacingAfterLines(10);
//		    paragraph.setSpacingBeforeLines(100);
//			cell.getCTTc().getTcPr().getTcW().setW(BigInteger.valueOf((long) 5000));
		if (isBagroundColor)
			cell.setColor("CACACA");
		run.setText(text);
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		if (isBold)
			run.setBold(true);
		if (isItalic)
			run.setItalic(true);
		paragraph.setWordWrap(true);
	}

	public static void WriteTableHeaderCell(XWPFTableCell cell, String text, String fontFamily, int fontSize,
			String alignment, int breaks, boolean isBold, boolean isItalic, boolean isBagroundColor) {
		// XWPFParagraph paragraph = cell.getParagraphs().get(0);
//			XWPFParagraph paragraph = cell.addParagraph();
		cell.setVerticalAlignment(XWPFVertAlign.BOTTOM);
		XWPFParagraph paragraph = cell.getParagraphs().get(0);
//			paragraph.setWordWrap(true);
		XWPFRun run = paragraph.createRun();
		if ("CENTER".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.CENTER);
		else if ("LEFT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.LEFT);
		else if ("RIGHT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.RIGHT);

		paragraph.setVerticalAlignment(TextAlignment.CENTER);
		if (isBagroundColor)
			cell.setColor("CACACA");
		run.setText(text);
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		if (isBold)
			run.setBold(true);
		if (isItalic)
			run.setItalic(true);
		paragraph.setWordWrap(true);
	}

	private static void mergeCell(XWPFTable table, int fromRow, int toRow, int fromCell, int toCell) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
				XWPFTableCell cell = table.getRow(fromRow).getCell(cellIndex);
				if (cellIndex == fromCell) {
					// The first merged cell is set with RESTART merge value
					cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
				} else {
					// Cells which join (merge) the first one, are set with CONTINUE
					cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
				}
			}
		}
	}

	private static void mergeCellHorizontally(XWPFTable table, int row, int fromCell, int toCell) {
		for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
			XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
			if (cellIndex == fromCell) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	private static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	private static void widthCellsAcrossRow(XWPFTable table, int rowNum, int colNum, int width) {
		XWPFTableCell cell = table.getRow(rowNum).getCell(colNum);
		if (cell.getCTTc().getTcPr() == null)
			cell.getCTTc().addNewTcPr();
		if (cell.getCTTc().getTcPr().getTcW() == null)
			cell.getCTTc().getTcPr().addNewTcW();
		cell.getCTTc().getTcPr().getTcW().setW(BigInteger.valueOf((long) width));
	}

	public static void writeStaticContent(XWPFDocument document, List<ReportStgVb> reportOtherList) {

		WriteText(document, "Central Bank of Kenya", "Times New Roman", 12, "CENTER", 1, true, false, false);

		WriteText(document, "Data collection template on Money Laundering and Terrorism Financing", "Times New Roman",
				12, "CENTER", 2, true, false, false);

		WriteText(document, "Name of bank:                      __FAMILY BANK LTD__________________", "Times New Roman",
				12, "LEFT", 0, true, false, false);

		WriteText(document, "Information in respect of:   _ Period ending", "Times New Roman", 12, "LEFT", 0, true,
				false, false);

		WriteText(document, "Return prepared by:  _______________                _ __", "Times New Roman", 12, "LEFT",
				0, true, false, false);

		WriteText(document,
				"                                                  (Signature)              (Name and Title)",
				"Times New Roman", 12, "LEFT", 0, false, true, false);

		WriteText(document, "Authorized by:           _______________                _ ___", "Times New Roman", 12,
				"LEFT", 0, true, false, false);

		WriteText(document,
				"                                                  (Signature)              (Name and Title)",
				"Times New Roman", 12, "LEFT", 0, false, true, false);

		WriteText(document, "Date of submission to CBK: ______", "Times New Roman", 12, "LEFT", 0, true, false, false);

		WriteText(document, "Profile of the bank:", "Times New Roman", 12, "LEFT", 0, true, false, false);

		WriteText(document, "1. Number of subsidiaries:", "Times New Roman", 12, "LEFT", 0, false, false, false);
		WriteText(document, "    List of subsidiaries: ----------------", "Times New Roman", 12, "LEFT", 1, false,
				false, false);
		WriteText(document, "2. Number of branches:", "Times New Roman", 12, "LEFT", 0, false, false, false);
		WriteText(document, "3. Number of Agents:", "Times New Roman", 12, "LEFT", 0, false, false, false);
		WriteText(document, "4. Number of ATMs:", "Times New Roman", 12, "LEFT", 0, false, false, false);
		WriteText(document, "5. List of products/services offered by the bank:", "Times New Roman", 12, "LEFT", 1,
				false, false, false);

		XWPFParagraph p1 = document.createParagraph();
		p1.setBorderTop(Borders.THREE_D_ENGRAVE);
		p1 = document.createParagraph();
		p1.setBorderTop(Borders.THREE_D_ENGRAVE);

		WriteText(document,
				"All questions should be answered, leaving no sections blank. Insert N/A when not applicable to your bank. If requested data is not available, insert N/K. Indicate any estimates by adding ‘E’ to the data provided.",
				"Times New Roman", 12, "LEFT", 0, true, false, false);

		WriteText(document, "1.	General Information", "Times New Roman", 12, "LEFT", 1, true, false, false);
		WriteText(document, "2.	General Information", "Times New Roman", 12, "LEFT", 1, true, false, false);
		WriteText(document, "i.	Number of employees  2019", "Times New Roman", 12, "LEFT", 1, false, false, false);
		WriteText(document, "ii.	Total number of transactions  and the value during the quarter:", "Times New Roman",
				12, "LEFT", 1, false, false, false);
		int cout = 0;
		if (reportOtherList != null && reportOtherList.size() > 0) {
			for (ReportStgVb reportStgVb : reportOtherList) {
				if (cout == 0)
					WriteText(document,
							"a)	Conducted directly in the bank premises; Number:  " + reportStgVb.getDataColumn1()
									+ "    Value:" + reportStgVb.getDataColumn2() + "",
							"Times New Roman", 12, "LEFT", 1, false, false, false);
				if (cout == 1)
					WriteText(document,
							"b)	Conducted through the bank’s agents; Number:  " + reportStgVb.getDataColumn1()
									+ "    Value:" + reportStgVb.getDataColumn2() + "",
							"Times New Roman", 12, "LEFT", 1, false, false, false);
				if (cout == 2)
					WriteText(document,
							"c)	Conducted through the mobile phone; Number:  " + reportStgVb.getDataColumn1()
									+ "    Value:" + reportStgVb.getDataColumn2() + "",
							"Times New Roman", 12, "LEFT", 1, false, false, false);
				if (cout == 3)
					WriteText(document,
							"d)	Conducted through Internet; Number:  " + reportStgVb.getDataColumn1() + "    Value:"
									+ reportStgVb.getDataColumn2() + "",
							"Times New Roman", 12, "LEFT", 1, false, false, false);
				if (cout == 4)
					WriteText(document,
							"e)	Conducted through ATM; Number:  " + reportStgVb.getDataColumn1() + "    Value:"
									+ reportStgVb.getDataColumn2() + "",
							"Times New Roman", 12, "LEFT", 1, false, false, false);
				if (cout == 5)
					WriteText(document,
							"f)	Conducted through other channels (Credit cards, POS).  	Value: "
									+ reportStgVb.getDataColumn2(),
							"Times New Roman", 12, "LEFT", 3, false, false, false);
				cout++;
			}
		} else {
			WriteText(document, "a)	Conducted directly in the bank premises; Number:  ___________    Value:_________",
					"Times New Roman", 12, "LEFT", 1, false, false, false);
			WriteText(document,
					"b)	Conducted through the bank’s agents; Number:  _______________    Value:____________",
					"Times New Roman", 12, "LEFT", 1, false, false, false);
			WriteText(document,
					"c)	Conducted through the mobile phone; Number:  ______________    Value:___ _____________",
					"Times New Roman", 12, "LEFT", 1, false, false, false);
			WriteText(document, "d)	Conducted through Internet; Number:  _______________    Value:_______________",
					"Times New Roman", 12, "LEFT", 1, false, false, false);
			WriteText(document, "e)	Conducted through ATM; Number:  _______________    Value:________________",
					"Times New Roman", 12, "LEFT", 1, false, false, false);
			WriteText(document, "f)	Conducted through other channels (Credit cards, POS).  	Value: ", "Times New Roman",
					12, "LEFT", 3, false, false, false);
		}

		WriteText(document, "2A.	Customer Information", "Times New Roman", 12, "LEFT", 0, true, false, false);
		WriteText(document,
				"3.	Total number of customers:		 of which Account holding customer:; occasional  (one off customer):___________",
				"Times New Roman", 12, "LEFT", 0, false, false, false);
		WriteText(document, "4.	Customer Risk Profile: ", "Times New Roman", 12, "LEFT", 0, false, false, false);
	}

	public static void WriteText(XWPFDocument document, String text, String fontFamily, int fontSize, String alignment,
			int breaks, boolean isBold, boolean isItalic, boolean isBreakBeofre) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		if ("CENTER".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.CENTER);
		else if ("LEFT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.LEFT);
		else if ("RIGHT".equalsIgnoreCase(alignment))
			paragraph.setAlignment(ParagraphAlignment.RIGHT);
		if (isBreakBeofre) {
			run.addBreak();
		}

		run.setText(text);
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);

		if (isBold)
			run.setBold(true);
		if (isItalic)
			run.setItalic(true);
		for (int i = 1; i <= breaks; i++) {
			run.addBreak();
		}
	}

	public File createTemplateDocFile(ReportsVb reportWriterVb) {
		File lfile = null;
		FileChannel source = null;
		FileChannel destination = null;
		try {
			String fileName = reportWriterVb.getTemplateId();
			fileName = ValidationUtil.encode(fileName.substring(0, fileName.indexOf(".docx")));
			String destFilePath = System.getProperty("java.io.tmpdir");
//			destFilePath = "C:\\java";
			if (!ValidationUtil.isValid(destFilePath)) {
				destFilePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(destFilePath)) {
				destFilePath = destFilePath + File.separator;
			}
			String fileNameDest = reportWriterVb.getReportTitle();
			lfile = new File(destFilePath + ValidationUtil.encode(fileNameDest) + "_"
					+ CustomContextHolder.getContext().getVisionId() + ".docx");
			String filePath = lfile.getAbsolutePath();
			filePath = filePath.substring(0, filePath.indexOf(ValidationUtil.encode(fileNameDest)));
			if (filePath.contains("temp")) {
				filePath = filePath.substring(0, filePath.indexOf("temp"));
			}
			if (lfile.exists()) {
				lfile.delete();
			}
//			lfile.createNewFile();
			String templateFilePath = commonDao.findVisionVariableValue("PRD_CB_TEMPLATE_PATH");
			File lSourceFile = new File(templateFilePath + fileName + ".docx");
			if (!lSourceFile.exists()) {
				throw new RuntimeCustomException(
						"Invalid Report Configuration, Invalid file name or file does not exists @ " + filePath);
			}
			source = new RandomAccessFile(lSourceFile, "rw").getChannel();
			destination = new RandomAccessFile(lfile, "rw").getChannel();
			long position = 0;
			long count = source.size();
			source.transferTo(position, count, destination);
		} catch (Exception e) {
			throw new RuntimeCustomException("Invalid Report Configuration, Invalid file name or file does not exists");
		} finally {
			if (source != null) {
				try {
					source.close();
				} catch (Exception ex) {
				}
			}
			if (destination != null) {
				try {
					destination.close();
				} catch (Exception ex) {
				}
			}
			logger.info("Template File Successfully Created");
		}
		return lfile;
	}

	public String formQueryForCaption(String caption, ReportsVb vObject) {
		String sql = "";
		String retStr = "";
		String promptStr = "";
		String[] captionArr = caption.split("!@");
		if (captionArr.length == 3) {
			promptStr = replacePromptVariables(captionArr[0], vObject, false);
			if ("ORACLE".equalsIgnoreCase(databaseType)) {
				if ("dd-Mon-RRRR".equalsIgnoreCase(captionArr[1]) || "DD-Mon-RRRR".equalsIgnoreCase(captionArr[1])) {
					sql = "Select To_Char(To_Date(" + promptStr + ")" + captionArr[2] + ",'" + captionArr[1]
							+ "')CAPTION from Dual";
					// Select To_Char(To_Date('10-Mar-2022')-1,'DD-Mon-RRRR') from Dual
				} else if ("Mon-RRRR".equalsIgnoreCase(captionArr[1])) {
					promptStr = promptStr.replaceAll("'", "");
					sql = "Select To_Char(add_months('01-" + promptStr + "'," + captionArr[2] + "),'" + captionArr[1]
							+ "')CAPTION from Dual";
					// Select To_Char(add_months('01-Nov-2022',-2),'Mon-RRRR') from Dual
				}
			}
			ExceptionCode exceptionCode = commonApiDao.getCommonResultDataQuery(sql);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				List result = (ArrayList) exceptionCode.getResponse();
				if (result != null && result.size() > 0) {
					HashMap<String, String> map = (HashMap<String, String>) result.get(0);
					retStr = map.get("CAPTION");
				}
			}
		}

		return retStr;
	}

	private static void setColumnWidth(XWPFTable table, int colIndex, int width) {
		for (int r = 0; r < table.getNumberOfRows(); r++) {
			XWPFTableCell cell = table.getRow(r).getCell(colIndex);
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr == null) {
				tcPr = cell.getCTTc().addNewTcPr();
			}
			CTTblWidth cellWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
			cellWidth.setType(STTblWidth.DXA);
			cellWidth.setW(BigInteger.valueOf(width));
		}
	}

	public ExceptionCode createCBWordReport(ReportsVb vObject) {
		ExceptionCode exceptionCode = null;
		FileOutputStream fileOS = null;
		File lfile = null;
		File lfileRs = null;
		String fileNames = "";
		String tmpFileName = "";
		String filesNameslst[] = null;
		String destFilePath = System.getProperty("java.io.tmpdir");
		if (!ValidationUtil.isValid(destFilePath))
			destFilePath = System.getenv("TMP");
		if (ValidationUtil.isValid(destFilePath))
			destFilePath = destFilePath + File.separator;
		try {
			ReportsVb reportsVb = new ReportsVb();
			String staticWordDodcument = "";
			exceptionCode = getReportDetails(vObject);
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					reportsVb = (ReportsVb) exceptionCode.getResponse();
				}
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					staticWordDodcument = (String) exceptionCode.getRequest();
				}
			} else {
				return exceptionCode;
			}
			if (!ValidationUtil.isValid(staticWordDodcument)) {

				File file = createTemplateDocFile(vObject);
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				XWPFDocument document = new XWPFDocument(fis);
				FileOutputStream out = new FileOutputStream(file);
				PromptTreeVb promptTree = reportsVb.getPromptTree();

				List<CbWordDocumentStgVb> wordDocStg = reportsDao.getWordDocmentData(promptTree);

				for (CbWordDocumentStgVb cbWordDocumentStgVb : wordDocStg) {
					if ("TABLE".equalsIgnoreCase(cbWordDocumentStgVb.getTextType())) {
						promptTree.setReportId(cbWordDocumentStgVb.getReportId());
						promptTree.setSessionId(cbWordDocumentStgVb.getSessionId());
						List<ColumnHeadersWordVb> columnHeaders = reportsDao.getColumnHeaderForCbWord(promptTree);
						if (columnHeaders != null && columnHeaders.size() > 0) {
							ColumnHeadersWordVb columnHeadersTempVb = columnHeaders.get(0);
							List<ReportStgVb> reportsStgs = reportsDao.getReportsStgForWord(promptTree);
							// getTableData(columnHeaders, reportsStgs);
							System.out.println(
									"Col Header Size : " + columnHeaders.size() + " STG Size : " + reportsStgs.size());

							int tableRows = reportsStgs.size() + columnHeadersTempVb.getMaxRowNum();
							int tableColums = columnHeadersTempVb.getMaxColNum();
							/*int lineBreakCount = cbWordDocumentStgVb.getLineBreakCount();

							if (ValidationUtil.isValid(lineBreakCount) && lineBreakCount > 0) {
								XWPFRun run = document.createParagraph().createRun();
								for (int i = 1; i <= lineBreakCount; i++) {
									run.addBreak();
								}
							}*/
							XWPFTable table = document.createTable(tableRows, tableColums);
							table.setCellMargins(100, 100, 100, 100); // (top, left, bottom, right);
//						     table.setWidth(10000);
							XWPFTableRow tableRow = table.getRow(0);
							tableRow.setHeight(100);
							int col = 0;
							int row = columnHeaders.get(0).getLabelRowNum();
							for (ColumnHeadersWordVb columnHeadersVb : columnHeaders) {
								if (columnHeadersVb.getLabelRowNum() != row) {
									tableRow = table.getRow(columnHeadersVb.getLabelRowNum() - 1);
								}
//					        	WriteTableCell(tableRow.getCell(columnHeadersVb.getLabelColNum()-1), columnHeadersVb.getCaption(),  "Times New Roman", 15, "CENTER", 0, true, false);
//					        	HTMLtoDOCX doc = new HTMLtoDOCX("<p>"+columnHeadersVb.getCaption()+"</p>", document, columnHeadersVb);

								XWPFTableCell cell = tableRow.getCell(col);
								if (ValidationUtil.isValid(columnHeadersVb.getTextHighlightColour())
										&& columnHeadersVb.getTextHighlightColour().length() == 6)
									cell.setColor(columnHeadersVb.getTextHighlightColour());
								XWPFParagraph paragraph = cell.getParagraphs().get(0);
								HTMLtoDOCX htmLtoDOCX = new HTMLtoDOCX("<p>" + columnHeadersVb.getCaption() + "</p>",
										paragraph, columnHeadersVb, true);

								col++;
							}
							for (ColumnHeadersWordVb columnHeadersVb : columnHeaders) {
								int rowSpan = columnHeadersVb.getRowspan() - 1;
								int colSpan = columnHeadersVb.getColspan() - 1;
								int rowNum = columnHeadersVb.getLabelRowNum() - 1;
								int colNum = columnHeadersVb.getLabelColNum() - 1;
								if (colSpan != 0) {
									int lastCol = colNum + columnHeadersVb.getColspan();
//									System.out.println(rowNum + ":" + colNum + ":" + lastCol);
									mergeCellHorizontally(table, rowNum, colNum, lastCol - 1);
								}
								if (rowSpan != 0) {
									int lastRow = colNum + columnHeadersVb.getRowspan();
//									System.out.println(rowNum + ":" + colNum + ":" + lastRow);
									mergeCellVertically(table, colNum, rowNum, lastRow - 1);
								}
							}
							String CAP_COL = "captionColumn";
							String DATA_COL = "dataColumn";
							int rowNum = 1;
							int r = 0;
							int captionCol = cbWordDocumentStgVb.getLineSpacing();
							for (ReportStgVb reportStgVb : reportsStgs) {
								tableRow = table.getRow(rowNum);
								for (int loopCount = 0; loopCount < tableColums; loopCount++) {
									int index = 0;
									ColumnHeadersWordVb columnHeadersVb = columnHeaders.get(loopCount);
//									ColumnHeadersWordVb columnHeadersVb = new ColumnHeadersWordVb();
									String type = "";
									if (loopCount < captionCol)
										type = CAP_COL;
									else
										type = DATA_COL;
									if (CAP_COL.equalsIgnoreCase(type)) {
										index = (loopCount + 1);
										String value = ExcelExportUtil.findValue(reportStgVb, type, index);
										XWPFTableCell cell = tableRow.getCell(loopCount);
										XWPFParagraph paragraph = cell.getParagraphs().get(0);
										HTMLtoDOCX htmLtoDOCX = new HTMLtoDOCX("<p>" + value + "</p>", paragraph,
												columnHeadersVb, false);
									} else {
										index = ((loopCount + 1) - captionCol);
										String value = ExcelExportUtil.findValue(reportStgVb, type, index);
										XWPFTableCell cell = tableRow.getCell(loopCount);
										XWPFParagraph paragraph = cell.getParagraphs().get(0);
										HTMLtoDOCX htmLtoDOCX = new HTMLtoDOCX("<p>" + value + "</p>", paragraph,
												columnHeadersVb, false);
									}
									widthCellsAcrossRow(table, rowNum, loopCount, 10000);
								}
								r++;
								rowNum++;
							}
							for (int loopCount = 0; loopCount < tableColums; loopCount++) {
								ColumnHeadersWordVb columnHeadersVb = columnHeaders.get(loopCount);
								int width = 6000;
								if(ValidationUtil.isValid(columnHeadersVb.getColumnWidth())) {
									width = Integer.parseInt(columnHeadersVb.getColumnWidth());
								}
								setColumnWidth(table, loopCount, width*1000);
							}
				            
							cbWordDocumentStgVb.setTextContent("");
							WriteText(document, cbWordDocumentStgVb);
						}
					} // Table DAta End
					else if ("HTML".equalsIgnoreCase(cbWordDocumentStgVb.getTextType())) {
						HTMLtoDOCX doc = new HTMLtoDOCX("<p>" + cbWordDocumentStgVb.getTextContent() + "</p>", document,
								cbWordDocumentStgVb, true);
						lineBreaks(document, cbWordDocumentStgVb);
						
					} else {
						WriteText(document, cbWordDocumentStgVb);
					}

				}
				CTDocument1 document1 = document.getDocument();
				CTBody body = document1.getBody();
				if (!body.isSetSectPr()) {
					body.addNewSectPr();
				}
				document.write(out);
				out.close();
			} else {
				File file = createTemplateDocFile(vObject);
			}

			System.out.println("create_table.docx written successully");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception in CBK Report Generation", e);
			throw new RuntimeCustomException(e.getMessage());
		} finally {
			try {
				// reportsDao.callProcToCleanUpTables(reportWriterVb);
				if (fileOS != null) {
					fileOS.flush();
					fileOS.close();
					fileOS = null;
				}
			} catch (Exception ex) {
			}
		}
		exceptionCode = CommonUtils.getResultObject("", 1, "", "");
		exceptionCode.setOtherInfo(vObject.getReportTitle());
		exceptionCode.setResponse(destFilePath);
		return exceptionCode;
	}

	public static void WriteText(XWPFDocument document, CbWordDocumentStgVb columnHeadersVb) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		String textContent = columnHeadersVb.getTextContent();
		String fontName = columnHeadersVb.getFontName();
		int fontSize = columnHeadersVb.getFontSize();
		String fontColour = columnHeadersVb.getFontColour();
		String textHighlightColour = columnHeadersVb.getTextHighlightColour();
		String emphasisType = columnHeadersVb.getEmphasisType();
		String alignmentType = columnHeadersVb.getAlignmentType();
		int lineBreakCount = columnHeadersVb.getLineBreakCount();
		int lineSpacing = columnHeadersVb.getLineSpacing();
		int indentCount = columnHeadersVb.getIndentCount();

		if ("CENTER".equalsIgnoreCase(alignmentType))
			paragraph.setAlignment(ParagraphAlignment.CENTER);
		else if ("LEFT".equalsIgnoreCase(alignmentType))
			paragraph.setAlignment(ParagraphAlignment.LEFT);
		else if ("RIGHT".equalsIgnoreCase(alignmentType))
			paragraph.setAlignment(ParagraphAlignment.RIGHT);

		paragraph.setSpacingAfter(lineSpacing);
		paragraph.setSpacingBefore(lineSpacing);
//		paragraph.setIndentationLeft(indentCount*200);
		paragraph.setIndentationLeft(indentCount);
//		paragraph.setIndentationRight(indentCount);
		if (ValidationUtil.isValid(emphasisType)) {
			if (emphasisType.contains("B"))
				run.setBold(true);
			if (emphasisType.contains("I"))
				run.setItalic(true);
			if (emphasisType.contains("S"))
				run.setStrike(true);
			if (emphasisType.contains("U"))
				run.setUnderline(UnderlinePatterns.THICK);
		}
		if ("Y".equalsIgnoreCase(columnHeadersVb.getNewPage()))
			run.addBreak(BreakType.PAGE);
		run.setText(textContent);
		run.setFontFamily(fontName);
		run.setFontSize(fontSize);
//		if(ValidationUtil.isValid(textHighlightColour))
//			run.setColor(textHighlightColour);
//		run.setColor("CACACA");

//			run.setBold(true);
//			run.setItalic(true);
		for (int i = 1; i <= lineBreakCount; i++) {
			run.addBreak();
		}
	}
	
	public static void lineBreaks(XWPFDocument document, CbWordDocumentStgVb columnHeadersVb) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		int lineBreakCount = columnHeadersVb.getLineBreakCount();
		for (int i = 1; i <= lineBreakCount; i++) {
			run.addBreak();
		}
	}

}