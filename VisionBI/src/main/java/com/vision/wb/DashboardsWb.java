package com.vision.wb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.vision.dao.AbstractDao;
import com.vision.dao.AlphaSubTabDao;
import com.vision.dao.CommonApiDao;
import com.vision.dao.CommonDao;
import com.vision.dao.DashboardsDao;
import com.vision.dao.ReportsDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.Constants;
import com.vision.util.DeepCopy;
import com.vision.util.ExcelExportUtil;
import com.vision.util.PDFExportUtil;
import com.vision.util.ValidationUtil;
import com.vision.vb.ColumnHeadersVb;
import com.vision.vb.DashboarUserDefVb;
import com.vision.vb.DashboardTabVb;
import com.vision.vb.DashboardTilesVb;
import com.vision.vb.DashboardVb;
import com.vision.vb.ExportVb;
import com.vision.vb.ReportFilterVb;
import com.vision.vb.ReportsVb;
	
@Component
public class DashboardsWb extends AbstractWorkerBean<DashboardVb> implements ServletContextAware{
	@Autowired
	private DashboardsDao  dashboardsDao;
	@Autowired
	private AlphaSubTabDao alphaSubTabDao;
	@Autowired
	private PDFExportUtil pdfExportUtil;
	@Autowired
	CommonApiDao commonApiDao;
	@Autowired
	ReportsDao reportsDao;
	@Autowired
	ReportsWb reportsWb;
	@Autowired
	CommonDao commonDao;
	
	private ServletContext servletContext;
	public void setServletContext(ServletContext arg0) {
		servletContext = arg0;
	}
	
	public static Logger logger = LoggerFactory.getLogger(DashboardsWb.class);
	//Component 1 - Dashboard Detail and Tab Detail
		public ExceptionCode getDashboardDetail(DashboardVb dObj){
			ExceptionCode exceptionCode = new ExceptionCode();
		try {
			DashboardVb mdmDashboardVb = dashboardsDao.getDashboardDetail(dObj);
			if (dashboardsDao.savedThemeExists(mdmDashboardVb.getDashboardId()) > 0) {
				mdmDashboardVb.setSavedDashboardTheme(dashboardsDao.getSavedTheme(dObj.getDashboardId()));
			}
			List<DashboardTabVb> tabslst = dashboardsDao.getDashboardTabDetails(dObj);
			// filters are taken from the Api -- /getReportFilter
			/*
			 * if("Y".equalsIgnoreCase(mdmDashboardVb.getFilterFlag())) {
			 * List<DashboardFilterVb> dashboardFilterlst =
			 * dashboardFilterProcess(mdmDashboardVb.getFilterRefCode());
			 * mdmDashboardVb.setDashboardFilterlst(dashboardFilterlst); }
			 */
			ArrayList<DashboardTabVb> tabDetails = new ArrayList<DashboardTabVb>();
			ArrayList<DashboardTilesVb> tileslstAll = new ArrayList<DashboardTilesVb>();
			tabslst.forEach(tabsVb -> {
				tabsVb.setDashboardId(dObj.getDashboardId());
				List<DashboardTilesVb> Tileslst = dashboardsDao.getDashboardTileDetails(tabsVb,false);
				for(DashboardTilesVb dashboardTilesVb :Tileslst) {
					if(ValidationUtil.isValid(dashboardTilesVb.getFilterRefCode())) {
						ExceptionCode exceptionCode2 = reportsWb.reportFilterProcess(dashboardTilesVb.getFilterRefCode(), new ReportsVb());
						if(exceptionCode2.getErrorCode() == Constants.SUCCESSFUL_OPERATION && exceptionCode2.getResponse() != null) {
							dashboardTilesVb.setReportFilterlst((List<ReportFilterVb>)exceptionCode2.getResponse());
						}
					}
				}
				tabsVb.setTileDetails(Tileslst);
				tabDetails.add(tabsVb);
			});
			mdmDashboardVb.setDashboardTabs(tabDetails);
			exceptionCode.setResponse(mdmDashboardVb);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			return exceptionCode;
		}catch(Exception ex){
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg(ex.getMessage());
				return exceptionCode;
			}
		}
	//Component 2 - Get the Tab result by passing Tab Id and Dashboard Id
	/*public ExceptionCode getTabResultData(MdmDashboardTabVb dObj){
		ExceptionCode exceptionCode = new ExceptionCode();
		try{ 
			promptCheck(dObj);
 			List<MdmDashboardTilesVb> resultlst = getTileResults(dObj);
 			exceptionCode.setResponse(resultlst);
 			exceptionCode.setOtherInfo(dObj);
 			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			return exceptionCode;
		}catch(Exception ex){
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(ex.getMessage());
			return exceptionCode;
		}
	}*/
	/*public List<MdmDashboardTilesVb> getTileResults(MdmDashboardTabVb dObj) throws SQLException{
		List<MdmDashboardTilesVb> tabTileslst = dashboardsDao.getDashboardTileDetails(dObj);
		ArrayList<MdmDashboardTilesVb> resultlst = new ArrayList<>();
		if(tabTileslst != null && tabTileslst.size() > 0) {
			List<DataFetcher> threads = new ArrayList<DataFetcher>(tabTileslst.size());
			tabTileslst.forEach(dashboardTilesVb -> {
				DataFetcher fetcher = new DataFetcher(dashboardTilesVb,dObj);
				fetcher.setDaemon(true);
				fetcher.start();
				try {
					fetcher.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				threads.add(fetcher);
			});
			for(DataFetcher df:threads){
				while(!df.dataFetched){
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				resultlst.add(df.dashboardTilesVb);
			}
			
			for(DataFetcher df:threads){
				int count = 0;
				if(!df.dataFetched){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					if(count > 150){
						count = 0;
						logger.info("Data fetch in progress for the report :"+ df.toString());
						continue;
					}
				}
				resultlst.add(df.dashboardTilesVb);
			}
			for(MdmDashboardTilesVb dashboardTilesVb : tabTileslst) {
				resultlst.add(dashboardTilesVb);
			}
		}
		return resultlst;
	}*/
	@Override
	protected void setAtNtValues(DashboardVb vObject) {
	}
	@Override
	protected void setVerifReqDeleteType(DashboardVb vObject) {
		vObject.setStaticDelete(false);
		vObject.setVerificationRequired(false);
	}
	@Override
	protected AbstractDao<DashboardVb> getScreenDao() {
		return dashboardsDao;
	}
	
	class DataFetcher extends Thread {
		boolean dataFetched = false;
		boolean errorOccured = false;
		String errorMsg = "";
		ExceptionCode exceptionCode;
		DashboardTilesVb dashboardTilesVb;
		DashboardTabVb dObj;
		public DataFetcher(DashboardTilesVb dashboardTilesVb){
			this.dashboardTilesVb = dashboardTilesVb;
		}
		public void run() {
			try{
				if("T".equalsIgnoreCase(dashboardTilesVb.getTileType())) {
					String tileResult = "";
					exceptionCode = dashboardsDao.getTilesReportData(dashboardTilesVb);
					if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
						tileResult = (String)exceptionCode.getResponse();
					}
					dashboardTilesVb.setTileDataSet(tileResult);
					List<DashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(dashboardTilesVb.getDashboardId(),dashboardTilesVb.getTabId(),dashboardTilesVb.getTileSequence(),dashboardTilesVb.getSubSequence(),true);
					dashboardTilesVb.setDrillDownlst(drillDowntabTileslst);
					if("Y".equalsIgnoreCase(dashboardTilesVb.getDoubleWidthFlag())) {
						dashboardTilesVb.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");	
					}else {
						dashboardTilesVb.setColumns("col-sm-12 col-lg-2 col-xl-2 col-md-2");
					}
				}else if("C".equalsIgnoreCase(dashboardTilesVb.getTileType())) {
					String chartType  = dashboardTilesVb.getChartType();
					exceptionCode = dashboardsDao.getChartReportData(dashboardTilesVb);
					if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
						dashboardTilesVb.setChartDataSet(exceptionCode.getResponse().toString());
					}
					List<DashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(dashboardTilesVb.getDashboardId(),dashboardTilesVb.getTabId(),dashboardTilesVb.getTileSequence(),dashboardTilesVb.getSubSequence(),true);
					dashboardTilesVb.setDrillDownlst(drillDowntabTileslst);
					if("Y".equalsIgnoreCase(dashboardTilesVb.getDoubleWidthFlag())) {
						dashboardTilesVb.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");	
					}else {
						dashboardTilesVb.setColumns("col-sm-12 col-lg-2 col-xl-2 col-md-2");
					}
					dashboardTilesVb.setChartList(reportsDao.getChartList(chartType));
				}else if("G".equalsIgnoreCase(dashboardTilesVb.getTileType())) {
					List gridDatalst = new ArrayList<>();
					exceptionCode = dashboardsDao.getGridData(dashboardTilesVb);
					if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION && exceptionCode.getResponse() != null) {
						gridDatalst = (List)exceptionCode.getResponse();
					}
					if(gridDatalst.size() > 0 && gridDatalst != null) {
						dashboardTilesVb.setGridColumnSet((List) gridDatalst.get(0));
						dashboardTilesVb.setGridColumnSet((List) gridDatalst.get(0));
						dashboardTilesVb.setGridDataSet((List) gridDatalst.get(1));
						//dashboardTilesVb.setGridColumnFormats((List) gridDatalst.get(2));
					}
					
					List<DashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(dashboardTilesVb.getDashboardId(),dashboardTilesVb.getTabId(),dashboardTilesVb.getTileSequence(),dashboardTilesVb.getSubSequence(),true);
					dashboardTilesVb.setDrillDownlst(drillDowntabTileslst);
					if("Y".equalsIgnoreCase(dashboardTilesVb.getDoubleWidthFlag())) {
						dashboardTilesVb.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");	
					}else {
						dashboardTilesVb.setColumns("col-sm-12 col-lg-2 col-xl-2 col-md-2");
					}
				}
				dataFetched = true;
				errorOccured = false;
			}catch(RuntimeCustomException rex){
				dataFetched = true;
				errorOccured = true;
				exceptionCode = rex.getCode();
			}catch(Exception e){
				dataFetched = true;
				errorOccured = true;
				errorMsg = e.getMessage();
			}
		}
	}
	public ExceptionCode getTileResultData(DashboardTilesVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try{
			Connection con = null;
			DashboardTilesVb childTileObj = new DashboardTilesVb();
			ArrayList<DashboardTilesVb> childTilelst = new ArrayList<DashboardTilesVb>();
			int parentSeq = vObject.getParentSequence();
			if("T".equalsIgnoreCase(vObject.getTileType()) || "S".equalsIgnoreCase(vObject.getTileType()) || "SC".equalsIgnoreCase(vObject.getTileType())) {
				if("Y".equalsIgnoreCase(vObject.getSubTiles())) {
 					List<DashboardTilesVb> subTilelst = dashboardsDao.getDashboardSubTileDetails(vObject);
 					ArrayList<DashboardTilesVb> resultlst = new ArrayList<>();
 					if(subTilelst != null && subTilelst.size() > 0) {
 						List<DataFetcher> threads = new ArrayList<DataFetcher>(subTilelst.size());
 						subTilelst.forEach(subTileVb -> {
 							subTileVb.setPromptValue1(vObject.getPromptValue1());
 							subTileVb.setPromptValue2(vObject.getPromptValue2());
 							subTileVb.setPromptValue3(vObject.getPromptValue3());
 							subTileVb.setPromptValue4(vObject.getPromptValue4());
 							subTileVb.setPromptValue5(vObject.getPromptValue5());
 							subTileVb.setPromptValue6(vObject.getPromptValue6());
 							subTileVb.setPromptValue7(vObject.getPromptValue7());
 							subTileVb.setPromptValue8(vObject.getPromptValue8());
 							subTileVb.setPromptValue9(vObject.getPromptValue9());
 							subTileVb.setPromptValue10(vObject.getPromptValue10());
 							DataFetcher fetcher = new DataFetcher(subTileVb);
 							fetcher.setDaemon(true);
 							fetcher.start();
 							try {
 								fetcher.join();
 							} catch (InterruptedException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
 							threads.add(fetcher);
 						});
 						for(DataFetcher df:threads){
 							int count = 0;
 							if(!df.dataFetched){
 								try {
 									Thread.sleep(2000);
 								} catch (InterruptedException e) {
 									e.printStackTrace();
 								}
 								count++;
 								if(count > 150){
 									count = 0;
 									logger.info("Data fetch in progress for the report :"+ df.toString());
 									continue;
 								}
 							}
 						}
 						for(DashboardTilesVb dashboardTilesVb : subTilelst) {
 							childTilelst.add(dashboardTilesVb);
 						}
 					}
					vObject.setChildTileslst(childTilelst);
	 			}else {
	 				String tileResult="";
					exceptionCode = dashboardsDao.getTilesReportData(vObject);
					if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
						tileResult = (String)exceptionCode.getResponse();
					}
					childTileObj.setTileDataSet(tileResult);
					List<DashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(vObject.getDashboardId(),vObject.getTabId(),vObject.getTileSequence(),vObject.getSubSequence(),false);
					childTileObj.setDrillDownlst(drillDowntabTileslst);
					childTilelst.add(childTileObj);
					vObject.setChildTileslst(childTilelst);
					vObject.setParentSequence(parentSeq);
					if("Y".equalsIgnoreCase(vObject.getDoubleWidthFlag())) {
						vObject.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");	
					}else {
						vObject.setColumns("col-sm-12 col-lg-2 col-xl-2 col-md-2");
					}
				}
			}else if("C".equalsIgnoreCase(vObject.getTileType())) {
				if("Y".equalsIgnoreCase(vObject.getSubTiles())) {
					List<DashboardTilesVb> subTilelst = dashboardsDao.getDashboardSubTileDetails(vObject);
 					ArrayList<DashboardTilesVb> resultlst = new ArrayList<>();
 					if(subTilelst != null && subTilelst.size() > 0) {
 						List<DataFetcher> threads = new ArrayList<DataFetcher>(subTilelst.size());
 						subTilelst.forEach(subTileVb -> {
 							subTileVb.setPromptValue1(vObject.getPromptValue1());
 							subTileVb.setPromptValue2(vObject.getPromptValue2());
 							subTileVb.setPromptValue3(vObject.getPromptValue3());
 							subTileVb.setPromptValue4(vObject.getPromptValue4());
 							subTileVb.setPromptValue5(vObject.getPromptValue5());
 							subTileVb.setPromptValue6(vObject.getPromptValue6());
 							subTileVb.setPromptValue7(vObject.getPromptValue7());
 							subTileVb.setPromptValue8(vObject.getPromptValue8());
 							subTileVb.setPromptValue9(vObject.getPromptValue9());
 							subTileVb.setPromptValue10(vObject.getPromptValue10());
 							DataFetcher fetcher = new DataFetcher(subTileVb);
 							fetcher.setDaemon(true);
 							fetcher.start();
 							try {
 								fetcher.join();
 							} catch (InterruptedException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
 							threads.add(fetcher);
 						});
 						for(DataFetcher df:threads){
 							int count = 0;
 							if(!df.dataFetched){
 								try {
 									Thread.sleep(2000);
 								} catch (InterruptedException e) {
 									e.printStackTrace();
 								}
 								count++;
 								if(count > 150){
 									count = 0;
 									logger.info("Data fetch in progress for the report :"+ df.toString());
 									continue;
 								}
 							}
 						}
 						for(DashboardTilesVb dashboardTilesVb : subTilelst) {
 							childTilelst.add(dashboardTilesVb);
 						}
 					}
					vObject.setChildTileslst(childTilelst);
				}else {
					String chartType  = vObject.getChartType();
					exceptionCode = dashboardsDao.getChartReportData(vObject);
					if(!ValidationUtil.isValid(exceptionCode.getResponse()) && exceptionCode.getErrorCode()!= Constants.SUCCESSFUL_OPERATION) {
						//exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
						exceptionCode.setErrorCode(exceptionCode.getErrorCode());
						exceptionCode.setErrorMsg(exceptionCode.getErrorMsg());
					}
					if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
						childTileObj.setChartDataSet(exceptionCode.getResponse().toString());
					}
					List<DashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(vObject.getDashboardId(),vObject.getTabId(),vObject.getTileSequence(),vObject.getSubSequence(),false);
					childTileObj.setDrillDownlst(drillDowntabTileslst);
					childTileObj.setChartList(reportsDao.getChartList(chartType));
					childTilelst.add(childTileObj);
					if (dashboardsDao.saveDashboardUserSefExists(vObject,false) > 0) {
						vObject.setSavedChart(dashboardsDao.getSavedChart(vObject,false));
					}
					vObject.setChildTileslst(childTilelst);
					vObject.setParentSequence(parentSeq);
					if("Y".equalsIgnoreCase(vObject.getDoubleWidthFlag())) {
						vObject.setColumns("col-sm-12 col-lg-6 col-xl-6 col-md-6");
					}else {
						vObject.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");
					}
				}
				if(con != null) {
					con.close();
				}
			}else if("G".equalsIgnoreCase(vObject.getTileType())) {
				List gridDatalst = new ArrayList<>();
				exceptionCode = dashboardsDao.getGridData(vObject);
				if(exceptionCode.getErrorCode() == Constants.ERRONEOUS_OPERATION) {
					return exceptionCode;
				}
				if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION && exceptionCode.getResponse() != null) {
					gridDatalst = (List)exceptionCode.getResponse();
				}
				if(gridDatalst.size() > 0 && gridDatalst != null)
					childTileObj.setGridColumnSet((List) gridDatalst.get(0));
				if(!ValidationUtil.isValid(childTileObj.getGridColumnSet())) {
					exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					exceptionCode.setErrorMsg("Columns not configured!");
				}
				childTileObj.setGridDataSet((List) gridDatalst.get(1));
				if(!ValidationUtil.isValid(childTileObj.getGridDataSet())) {
					exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					exceptionCode.setErrorMsg("No Data Found!");
				}
				/*childTileObj.setGridColumnFormats((List) gridDatalst.get(2));
				if(!ValidationUtil.isValid(childTileObj.getGridColumnFormats())) {
					exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					exceptionCode.setErrorMsg("Column Format not Configured!");
				}*/
				List<DashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(vObject.getDashboardId(),vObject.getTabId(),vObject.getTileSequence(),vObject.getSubSequence(),false);
				childTileObj.setDrillDownlst(drillDowntabTileslst);
				childTilelst.add(childTileObj);
				vObject.setChildTileslst(childTilelst);
				vObject.setParentSequence(parentSeq);
				if("Y".equalsIgnoreCase(vObject.getDoubleWidthFlag())) {
					vObject.setColumns("col-sm-12 col-lg-6 col-xl-6 col-md-6");
				}else {
					vObject.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");
				}
			}
			exceptionCode.setResponse(vObject);
			exceptionCode.setOtherInfo(vObject);
			if(!ValidationUtil.isValid(exceptionCode.getErrorCode())) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);	
			}
 			return exceptionCode;
		}catch(Exception ex){
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(ex.getMessage());
			return exceptionCode;
		}
	}
	public ExceptionCode getDrillDownData(List<DashboardTilesVb> drillDownlst) {
		ExceptionCode exceptionCode = new ExceptionCode();
		DashboardTilesVb dashboardTilesVb = new DashboardTilesVb();
		try {
			//List<MdmDashboardTilesVb> drillDowntabTileslst = dashboardsDao.getTileDrillDownDetails(dObj.getDashboardId(),dObj.getTabId(),dObj.getTileSequence());
			List<DashboardTilesVb> drillDownResultlst = new ArrayList();
			if(drillDownlst != null && drillDownlst.size() > 0) {
				for(DashboardTilesVb dObj : drillDownlst) {
					dObj.setIsDrillDown(true);
					if("C".equalsIgnoreCase(dObj.getTileType())) {
						//Connection con =null;
						String chartType = dObj.getChartType();
						exceptionCode = dashboardsDao.getChartReportData(dObj);
						if(!ValidationUtil.isValid(exceptionCode.getResponse())) {
							exceptionCode.setErrorCode(exceptionCode.getErrorCode());
							exceptionCode.setErrorMsg(exceptionCode.getErrorMsg());
						}
						if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
							dObj.setChartDataSet(exceptionCode.getResponse().toString());
						}
						if("Y".equalsIgnoreCase(dObj.getDoubleWidthFlag())) {
							dObj.setColumns("col-sm-12 col-lg-6 col-xl-6 col-md-6");	
						}else {
							dObj.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");
						}
						dObj.setChartList(reportsDao.getChartList(chartType));
						if (dashboardsDao.saveDashboardUserSefExists(dObj, true) > 0) {
							dObj.setSavedChart(dashboardsDao.getSavedChart(dObj, true));
						}
					}/*else if("T".equalsIgnoreCase(dObj.getTileType())) {
						String drillDownTileResult = dashboardsDao.getTilesReportData(dObj);
						dObj.setTileDataSet(drillDownTileResult);
						if("Y".equalsIgnoreCase(dObj.getDoubleWidthFlag())) {
							dObj.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");	
						}else {
							dObj.setColumns("col-sm-12 col-lg-2 col-xl-2 col-md-2");
						}
					}*/else if("G".equalsIgnoreCase(dObj.getTileType())) {
						List gridDatalst = new ArrayList<>();
						exceptionCode = dashboardsDao.getGridData(dObj);
						if(exceptionCode.getErrorCode() == Constants.ERRONEOUS_OPERATION) {
							return exceptionCode;
						}
						if(exceptionCode.getResponse() != null) {
							gridDatalst = (List)exceptionCode.getResponse();
							if(gridDatalst == null || gridDatalst.size() == 0) {
								exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
								exceptionCode.setErrorMsg("Empty Result set...");
							}
						}
						
						dObj.setGridColumnSet((List) gridDatalst.get(0));
						if(!ValidationUtil.isValid(dObj.getGridColumnSet())) {
							exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
							exceptionCode.setErrorMsg("Column are not Configured !! ");
						}
						dObj.setGridDataSet((List) gridDatalst.get(1));
						if(!ValidationUtil.isValid(dObj.getGridDataSet())) {
							exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
							exceptionCode.setErrorMsg("No Data Found!");
						}
						/*dObj.setGridColumnFormats((List) gridDatalst.get(2));
						if(!ValidationUtil.isValid(dObj.getGridColumnFormats())) {
							exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
							exceptionCode.setErrorMsg("Column Format not Configured!");
						}*/
						if("Y".equalsIgnoreCase(dObj.getDoubleWidthFlag())) {
							dObj.setColumns("col-sm-12 col-lg-6 col-xl-6 col-md-6");
						}else {
							dObj.setColumns("col-sm-12 col-lg-4 col-xl-4 col-md-4");
						}
					}
					drillDownResultlst.add(dObj);
				}
			}
			dashboardTilesVb.setDrillDownlst(drillDownResultlst);
			if(!ValidationUtil.isValid(exceptionCode.getErrorCode())) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);	
			}
			exceptionCode.setResponse(dashboardTilesVb);
			exceptionCode.setOtherInfo(drillDownlst);
		}catch(Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(""+e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode getDashboardList() {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			List<ReportsVb> reportTree = null;
			List<ReportsVb> lResult = new ArrayList<ReportsVb>(0);
			reportTree = dashboardsDao.getDashboardFolderStructure();
			lResult = reportsWb.createPraentChildReportsTree(reportTree, null);
			exceptionCode.setResponse(lResult);
			if(lResult != null && lResult.size() > 0) {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			}else {
				exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
			}
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	/*public ExceptionCode getDashboardList() {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			ArrayList<AlphaSubTabVb> applicationlst = (ArrayList<AlphaSubTabVb>) dashboardsDao.findDashboardApplicationCategory();
			for(AlphaSubTabVb alphaSubTabVb : applicationlst) {
				ArrayList<AlphaSubTabVb> dashboardGrplst = (ArrayList<AlphaSubTabVb>) dashboardsDao.findDashboardCategory(alphaSubTabVb.getAlphaSubTab());
				ArrayList<AlphaSubTabVb> dashboardlst = new ArrayList<AlphaSubTabVb>();
				for(AlphaSubTabVb dashboardGrp : dashboardGrplst) {
					ArrayList dashboards = (ArrayList<AlphaSubTabVb>)dashboardsDao.getDashboadList(dashboardGrp.getAlphaSubTab(),alphaSubTabVb.getAlphaSubTab());
					dashboardGrp.setChildren(dashboards);
					dashboardlst.add(dashboardGrp);
				}
				alphaSubTabVb.setChildren(dashboardlst);
			}
			exceptionCode.setResponse(applicationlst);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}*/
	public ExceptionCode exportToExcel(MultipartFile[] files,ReportsVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			SXSSFWorkbook workBook = new SXSSFWorkbook(500);
			SXSSFSheet sheet = (SXSSFSheet) workBook.createSheet("Report");
			Map<Integer, XSSFCellStyle>  styls = ExcelExportUtil.createStyles(workBook,"");
			ExcelExportUtil.createPromptsPage(vObject, sheet, workBook, assetFolderUrl, styls,0);
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				String sheetName = fileName.substring(0,fileName.lastIndexOf("."));
				sheet = (SXSSFSheet) workBook.createSheet(sheetName);
				InputStream is=file.getInputStream();
				byte[] bytes = IOUtils.toByteArray(is);
				int pictureIdx = workBook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
				is.close();
				Drawing drawing = sheet.createDrawingPatriarch();
				XSSFClientAnchor anchor = new XSSFClientAnchor();
				anchor.setAnchorType(ClientAnchor.MOVE_DONT_RESIZE);
				anchor.setCol1(0);
				anchor.setRow1(0);
				Picture pict = drawing.createPicture(anchor, pictureIdx);
				if (pict != null)
					pict.resize(1);
			}
			String filePath = System.getProperty("java.io.tmpdir");
			if(!ValidationUtil.isValid(filePath)){
				filePath = System.getenv("TMP");
			}
			if(ValidationUtil.isValid(filePath)){
				filePath = filePath + File.separator;
			}
			File lFile = new File(filePath+ValidationUtil.encode("Dashboard")+"_"+vObject.getMaker()+".xlsx");
			if(lFile.exists()){
				lFile.delete();
			}
			lFile.createNewFile();
			FileOutputStream fileOS = new FileOutputStream(lFile);
			workBook.write(fileOS);
			fileOS.flush();
			fileOS.close();
			exceptionCode.setResponse(filePath);
			exceptionCode.setOtherInfo("Dashboard_"+vObject.getMaker());
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode exportToPdf(MultipartFile[] files,ReportsVb reportsVb) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			//exceptionCode = pdfExportUtil.dashboardExportToPdf(files,reportsVb,assetFolderUrl);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode exportToXls(DashboardTilesVb vObject,DashboardVb dashboardVb,int currentUserId,String workBookCnt){
			ExceptionCode exceptionCode = new ExceptionCode();
			ReportsVb reportsVb = new ReportsVb();
			int  rowNum = 0;
			try{
				ArrayList<DashboardTilesVb> tileHeaderData = new ArrayList<DashboardTilesVb>();
				exceptionCode = getTileResultData(vObject);	
				if(exceptionCode.getErrorCode()==Constants.ERRONEOUS_OPERATION) {
					return exceptionCode;
				}
				else {
				if(exceptionCode.getResponse()!=null) {
					DashboardTilesVb dashboardTilesVb = (DashboardTilesVb)exceptionCode.getResponse();
						tileHeaderData = (ArrayList)dashboardTilesVb.getChildTileslst();
						if(tileHeaderData != null &&  tileHeaderData.size() > 0){
							vObject.setGridColumnSet(tileHeaderData.get(0).getGridColumnSet());
							vObject.setGridDataSet(tileHeaderData.get(0).getGridDataSet());
						}	
					}
				}
				String tileCaption = "";
				tileCaption = vObject.getTileCaption().trim();
				vObject.setTileCaption(tileCaption);
				reportsVb.setReportTitle(tileCaption);
				List<ColumnHeadersVb> colHeaderslst=vObject.getGridColumnSet();
				List<HashMap<String, String>> dataLst =vObject.getGridDataSet();
				List<String> colTypes = new ArrayList<String>();
				Map<Integer,Integer> columnWidths = new HashMap<Integer,Integer>(colHeaderslst.size());
				for (int loopCnt = 0; loopCnt < colHeaderslst.size(); loopCnt++) {
					columnWidths.put(Integer.valueOf(loopCnt), Integer.valueOf(-1));
					ColumnHeadersVb colHVb = colHeaderslst.get(loopCnt);
					if (colHVb.getColspan() <= 1 && colHVb.getNumericColumnNo() != 99) {
						colTypes.add(colHVb.getColType());
					}
				}
				/*for(int loopCnt= 0; loopCnt < colHeaderslst.size(); loopCnt++){
					columnWidths.put(Integer.valueOf(loopCnt), Integer.valueOf(-1));
				}*/
				int headerCnt  = 0;
				String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
				dashboardVb.setMaker(currentUserId);
				getScreenDao().fetchMakerVerifierNames(dashboardVb);
				reportsVb.setMakerName(dashboardVb.getMakerName());
				reportsVb.setPromptLabel(vObject.getPromptLabel());
				reportsVb.setApplicationTheme(vObject.getApplicationTheme());
				boolean createHeadersAndFooters = true;
				//Excel Report Header
				logger.info("Excel Export Data Write Begin["+ vObject.getDashboardId()+":"+vObject.getTabId()+"]");
				SXSSFWorkbook workBook = new SXSSFWorkbook(500);
				SXSSFSheet sheet =(SXSSFSheet) workBook.createSheet("Summary");
				Map<Integer, XSSFCellStyle>  styls = ExcelExportUtil.createStyles(workBook,vObject.getApplicationTheme());
				ExcelExportUtil.createPromptsPage(reportsVb, sheet, workBook, assetFolderUrl, styls, headerCnt);
				sheet =(SXSSFSheet) workBook.createSheet("Report");

				if(createHeadersAndFooters)
				headerCnt = ExcelExportUtil.writeHeadersRA(reportsVb, colHeaderslst, rowNum, sheet, styls, colTypes,columnWidths);
				
				++rowNum;
				if(createHeadersAndFooters)
					rowNum = ExcelExportUtil.writeReportDataRA(workBook,reportsVb, colHeaderslst, dataLst, sheet, rowNum, styls, colTypes, columnWidths,false,assetFolderUrl);
				
				headerCnt = colTypes.size();
				int noOfSheets = workBook.getNumberOfSheets();
				for(int a =1 ;a < noOfSheets;a++) {
					sheet = (SXSSFSheet) workBook.getSheetAt(a);
					int loopCount = 0;
					for(loopCount=0; loopCount<headerCnt;loopCount++){
						sheet.setColumnWidth(loopCount, columnWidths.get(loopCount));
					}
				}
				String filePath = System.getProperty("java.io.tmpdir");
				if(!ValidationUtil.isValid(filePath)){
					filePath = System.getenv("TMP");
				}
				if(ValidationUtil.isValid(filePath)){
					filePath = filePath + File.separator;
				}
				File lFile = new File(filePath+ValidationUtil.encode(tileCaption)+"_"+currentUserId+".xlsx");
				if(lFile.exists()){
					lFile.delete();
				}
				lFile.createNewFile();
				FileOutputStream fileOS = new FileOutputStream(lFile);
				workBook.write(fileOS);
				fileOS.flush();
				fileOS.close();
				logger.info("Excel Export Data Write End["+vObject.getDashboardId()+":"+vObject.getTabId()+"]");
				exceptionCode.setResponse(filePath);
				exceptionCode.setOtherInfo(vObject.getTileCaption()+"_"+currentUserId);
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("Report Export Excel Exception at: " + vObject.getDashboardId()+" : "+vObject.getTileCaption());
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			}
			return exceptionCode;
	}
	public ExceptionCode exportToPdf(int currentUserId,DashboardTilesVb vObject){
		ExceptionCode exceptionCode = new ExceptionCode();
		ReportsVb reportsVb = new ReportsVb();
		try {
			exceptionCode = dashboardsDao.getGridData(vObject);
			List gridDatalst = new ArrayList<>();
			if(exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION && exceptionCode.getResponse() != null) {
				gridDatalst = (List)exceptionCode.getResponse();
			}else {
				return exceptionCode;
			}
			if (gridDatalst.size() > 0 && gridDatalst != null) {
				vObject.setGridColumnSet((List) gridDatalst.get(0));
				if (!ValidationUtil.isValid(vObject.getGridColumnSet())) {
					exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					exceptionCode.setErrorMsg("Columns not configured!");
				}
				vObject.setGridDataSet((List) gridDatalst.get(1));
				if (!ValidationUtil.isValid(vObject.getGridDataSet())) {
					exceptionCode.setErrorCode(Constants.NO_RECORDS_FOUND);
					exceptionCode.setErrorMsg("No Data Found!");
				}
			}
				
			String tileCaption = "";
			tileCaption = vObject.getTileCaption().trim();
			vObject.setTileCaption(tileCaption);
			reportsVb.setReportTitle(tileCaption);
			List<ColumnHeadersVb> colHeaderslst=vObject.getGridColumnSet();
			List<HashMap<String, String>> dataLst =vObject.getGridDataSet();
			
			List<String> colTypes = new ArrayList<String>();
			Map<Integer,Integer> columnWidths = new HashMap<Integer,Integer>(colHeaderslst.size());
			for(int loopCnt= 0; loopCnt < colHeaderslst.size(); loopCnt++){
				columnWidths.put(Integer.valueOf(loopCnt), Integer.valueOf(-1));
				ColumnHeadersVb colHVb = colHeaderslst.get(loopCnt);
				if(colHVb.getColspan() <= 1 && colHVb.getNumericColumnNo() != 99) {
					colTypes.add(colHVb.getColType());
				}
			}
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			DashboardVb dashboardVb = new DashboardVb();
			dashboardVb.setMaker(currentUserId);
			getScreenDao().fetchMakerVerifierNames(dashboardVb);
			reportsVb.setMakerName(dashboardVb.getMakerName());
			//getScreenDao().fetchMakerVerifierNames(vObject);
			reportsVb.setPromptLabel(vObject.getPromptLabel());
			reportsVb.setApplicationTheme(vObject.getApplicationTheme());
			
			List<HashMap<String, String>> totalLst = new ArrayList<HashMap<String, String>>();
			/*ReportsVb resultVb = new ReportsVb();
			if (exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
				if (ValidationUtil.isValid(exceptionCode.getResponse())) {
					resultVb = (ReportsVb) exceptionCode.getResponse();
					dataLst = resultVb.getGridDataSet();
					totalLst = resultVb.getTotal();
				}
			}*/
			logger.info("Pdf Export Data Write Begin["+reportsVb.getReportId()+":"+reportsVb.getSubReportId()+"]");
			/*if(!reportOrientation.equalsIgnoreCase(reportsVb.getReportOrientation())) {
				reportsVb.setPdfGrwthPercent(0);
			}*/
			ArrayList<ColumnHeadersVb> columnHeadersFinallst = new ArrayList<ColumnHeadersVb>();
			colHeaderslst.forEach(colHeadersVb -> {
				if(colHeadersVb.getColspan() <= 1 && colHeadersVb.getNumericColumnNo() != 99) {
					columnHeadersFinallst.add(colHeadersVb);
				}
			});
			exceptionCode = pdfExportUtil.exportToPdfDashboard(colHeaderslst, dataLst, reportsVb, assetFolderUrl,colTypes,currentUserId,totalLst,columnHeadersFinallst);
			logger.info("Pdf Export Data Write End["+reportsVb.getReportId()+":"+reportsVb.getSubReportId()+"]");
		}catch(Exception e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode saveDashboardTheme(DashboarUserDefVb vObject) {
		ExceptionCode exceptionCode  =  new ExceptionCode();
		try {
			exceptionCode = deleteDashboardTheme(vObject);
			int retVal = dashboardsDao.saveDashboardTheme(vObject);
			if(retVal == Constants.ERRONEOUS_OPERATION) {
				exceptionCode.setErrorCode(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
				exceptionCode.setErrorMsg("Unable to save Dashboard Theme.Contact Admin!!");
			}else {
				exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
				exceptionCode.setErrorMsg("Dashboard Theme Saved Successfully");
			}
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode deleteDashboardTheme(DashboarUserDefVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		int retVal = 0;
		try {
			if(dashboardsDao.savedThemeExists(vObject.getDashboardId()) > 0) {
				retVal = dashboardsDao.deleteDashboardThemeExists(vObject);
				if (retVal == Constants.SUCCESSFUL_OPERATION) {
					exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
					exceptionCode.setErrorMsg("Dashboard theme deleted successfully");
				}else {
					exceptionCode.setErrorCode(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
					exceptionCode.setErrorMsg("No settings found to delete");
				}
			} else {
				exceptionCode.setErrorCode(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
				exceptionCode.setErrorMsg("No settings found to delete");
			}
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode deleteDashboardUserSettings(DashboarUserDefVb vObject,Boolean flag) {
		ExceptionCode exceptionCode = new ExceptionCode();
		int retVal = 0;
		try {
			if(flag) {
				retVal = dashboardsDao.deleteDashboardUserSettingExists(vObject.getDashboardId());
			} else {
				if (vObject.getDashboardUserDefLst() != null && vObject.getDashboardUserDefLst().size() > 0) {
					for (DashboardTilesVb dObj : vObject.getDashboardUserDefLst()) {
						if (dashboardsDao.saveDashboardUserSefExists(dObj,true) > 0) {
							retVal = dashboardsDao.deleteDashboardUserSettingExists(dObj);
						}
					}
				}
			}
			
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Dashboard User Settings deleted Successfully");
			exceptionCode.setOtherInfo(vObject);
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode saveDashboardUserSettings(DashboarUserDefVb vObject) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			deleteDashboardUserSettings(vObject,false);
			if(vObject.getDashboardUserDefLst() != null && !vObject.getDashboardUserDefLst().isEmpty()) {
				vObject.getDashboardUserDefLst().forEach(userDefVb -> {
					int retVal = dashboardsDao.saveDashboardUserSettings(userDefVb);
				});
			}
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Dashboard - User defined setting saved successfully!!");
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		exceptionCode.setOtherInfo(vObject);
		return exceptionCode;
	}
	public ExceptionCode dashboardExport(int userId,ExportVb exportVb) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			DashboardVb dashboardVb = new DashboardVb();
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			dashboardVb.setMaker(userId);
			getScreenDao().fetchMakerVerifierNames(dashboardVb);
			exportVb.setMakerName(dashboardVb.getMakerName());
			
			if(exportVb.getExportTabDetailsLst() != null && exportVb.getExportTabDetailsLst().size() > 0) {
				exportVb.setTabId(exportVb.getExportTabDetailsLst().get(0).getTabId());
				String pdfGrwthWidth = dashboardsDao.getPdfGrwthWidth(exportVb);
				exportVb.setPdfGrwthWidth(pdfGrwthWidth);	
			}
			exceptionCode = pdfExportUtil.dashboardExportNewforPdf(exportVb, assetFolderUrl, userId);
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
			exceptionCode.setErrorMsg("Sucess");
		}catch(Exception e) {
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}
	public ExceptionCode dashboardExportForExcel(ExportVb vObject,int currentUserId) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			DashboardVb dashboardVb = new DashboardVb();
			ReportsVb reportsVb = new ReportsVb();
			int headerCnt = 0;
			String assetFolderUrl = servletContext.getRealPath("/WEB-INF/classes/images");
			dashboardVb.setMaker(currentUserId);
			getScreenDao().fetchMakerVerifierNames(dashboardVb);
			reportsVb.setMakerName(vObject.getMakerName());
			reportsVb.setPromptLabel(vObject.getPromptLabel());
			reportsVb.setApplicationTheme(vObject.getApplicationTheme());
			SXSSFWorkbook workBook = new SXSSFWorkbook(500);
			SXSSFSheet sheet = (SXSSFSheet) workBook.createSheet("Summary");
			Map<Integer, XSSFCellStyle> styls = ExcelExportUtil.createStyles(workBook,vObject.getApplicationTheme());
			ExcelExportUtil.createPromptsPage(reportsVb, sheet, workBook, assetFolderUrl, styls, headerCnt);
			sheet = (SXSSFSheet) workBook.createSheet("Report");
			
			DashboardTabVb dashboardTabVb = new DashboardTabVb();
			dashboardTabVb.setDashboardId(vObject.getDashboardId());
			dashboardTabVb.setTabId(vObject.getTabId());
			List<DashboardTilesVb> tileslst = dashboardsDao.getDashboardTileDetails(dashboardTabVb,false);
			if(tileslst != null && tileslst.size() > 0) {
				for(DashboardTilesVb tilesVb : tileslst) {
					if("T".equalsIgnoreCase(tilesVb.getTileType())) {
						exceptionCode = dashboardsDao.getTilesReportData(tilesVb);
						if(exceptionCode != null && exceptionCode.getErrorCode() == Constants.SUCCESSFUL_OPERATION) {
							
						}
					}
				}
			}

			String filePath = System.getProperty("java.io.tmpdir");
			if (!ValidationUtil.isValid(filePath)) {
				filePath = System.getenv("TMP");
			}
			if (ValidationUtil.isValid(filePath)) {
				filePath = filePath + File.separator;
			}
			File lFile = new File(filePath + ValidationUtil.encode("Dashboard_Export") + "_" + currentUserId + ".xlsx");
			if (lFile.exists()) {
				lFile.delete();
			}
			lFile.createNewFile();
			FileOutputStream fileOS = new FileOutputStream(lFile);
			workBook.write(fileOS);
			fileOS.flush();
			fileOS.close();
		}catch(Exception e) {
			
		}
		return exceptionCode;
	}
}
