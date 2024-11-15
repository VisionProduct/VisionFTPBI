package com.vision.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.MagnifierResultVb;
import com.vision.vb.ReportsVb;
import com.vision.vb.SmartSearchVb;
import com.vision.vb.VisionUsersVb;
import com.vision.wb.ReportsWb;
@Component
public class MagnifierDao extends AbstractDao<MagnifierResultVb> {
	
	@Value("${app.databaseType}")
	private String databaseType;
	
	@Autowired
	CommonDao commonDao;
	@Autowired
	ReportsWb reportsWb;
	public RowMapper getTwoColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				magnifierResultVb.setColumnOne(rs.getString(1));
				magnifierResultVb.setColumnThree(rs.getString(2));
				return magnifierResultVb;
			}
		};
		return mapper;
	}
	public RowMapper getThreeColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
					magnifierResultVb.setColumnOne(rs.getString(2));
					magnifierResultVb.setColumnTwo(rs.getString(3));
					magnifierResultVb.setColumnThree(rs.getString(4));
				}else {
					magnifierResultVb.setColumnOne(rs.getString(1));
					magnifierResultVb.setColumnTwo(rs.getString(2));
					magnifierResultVb.setColumnThree(rs.getString(3));					
				}
				
				magnifierResultVb.setMagnifierResult(magnifierResultVb.getColumnOne()+","+magnifierResultVb.getColumnTwo()+","+magnifierResultVb.getColumnThree());
				return magnifierResultVb;
			}
		};
		return mapper;
	}
	public RowMapper getLevel1ColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				magnifierResultVb.setColumnOne(rs.getString(1));
				magnifierResultVb.setColumnTwo(rs.getString(2));
				magnifierResultVb.setColumnThree(rs.getString(3));
				magnifierResultVb.setColumnFour(rs.getString(4));
				magnifierResultVb.setColumnFive(rs.getString(5));
				magnifierResultVb.setColumnSix(rs.getString(6));
				magnifierResultVb.setColumnSeven(rs.getString(7));
				magnifierResultVb.setColumnEight(rs.getString(8));
				magnifierResultVb.setColumnNine(rs.getString(9));
				magnifierResultVb.setColumnTen(rs.getString(10));
				
				return magnifierResultVb;
			}
		};
		return mapper;
	}
	public RowMapper getLevelADFColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				magnifierResultVb.setColumnOne(rs.getString(1));
				magnifierResultVb.setColumnTwo(rs.getString(2));
				magnifierResultVb.setColumnThree(rs.getString(3));
				magnifierResultVb.setColumnFour(rs.getString(4));
				magnifierResultVb.setColumnFive(rs.getString(5));
				magnifierResultVb.setColumnSix(rs.getString(6));
				magnifierResultVb.setColumnSeven(rs.getString(7));
				magnifierResultVb.setColumnEight(rs.getString(8));
				magnifierResultVb.setColumnNine(rs.getString(9));
				magnifierResultVb.setColumnTen(rs.getString(10));
				magnifierResultVb.setColumnEleven(rs.getString(11));
				magnifierResultVb.setColumnTwelve(rs.getString(12));
				magnifierResultVb.setColumnThirteen(rs.getString(13));
				magnifierResultVb.setColumnFourteen(rs.getString(14));
				return magnifierResultVb;
			}
		};
		return mapper;
	}
	public RowMapper getLevel2ColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				magnifierResultVb.setColumnThree(rs.getString(1));
				magnifierResultVb.setColumnFour(rs.getString(2));
				magnifierResultVb.setColumnFive(rs.getString(3));
				magnifierResultVb.setColumnSix(rs.getString(4));
				magnifierResultVb.setColumnSeven(rs.getString(5));
				magnifierResultVb.setColumnEight(rs.getString(6));
				magnifierResultVb.setColumnNine(rs.getString(7));
				magnifierResultVb.setColumnTen(rs.getString(8));
				return magnifierResultVb;
			}
		};
		return mapper;
	}	
	public RowMapper getLevel3ColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				magnifierResultVb.setColumnFive(rs.getString(1));
				magnifierResultVb.setColumnSix(rs.getString(2));
				magnifierResultVb.setColumnSeven(rs.getString(3));
				magnifierResultVb.setColumnEight(rs.getString(4));
				magnifierResultVb.setColumnNine(rs.getString(5));
				magnifierResultVb.setColumnTen(rs.getString(6));
				return magnifierResultVb;
			}
		};
		return mapper;
	}	
	public RowMapper getLevel4ColMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				magnifierResultVb.setColumnSeven(rs.getString(1));
				magnifierResultVb.setColumnEight(rs.getString(2));
				magnifierResultVb.setColumnNine(rs.getString(3));
				magnifierResultVb.setColumnTen(rs.getString(4));
				return magnifierResultVb;
			}
		};
		return mapper;
	}	
	public RowMapper getTwoColumnMagnifierMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb magnifierResultVb = new MagnifierResultVb();
				if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
					magnifierResultVb.setColumnNine(rs.getString(2));
					magnifierResultVb.setColumnTen(rs.getString(3));
				}else {
					magnifierResultVb.setColumnNine(rs.getString(1));
					magnifierResultVb.setColumnTen(rs.getString(2));

				}
				magnifierResultVb.setMagnifierResult(magnifierResultVb.getColumnNine()+","+magnifierResultVb.getColumnTen());
				return magnifierResultVb;
			}
		};
		return mapper;
	}	
	public List<MagnifierResultVb> getQueryPopupResults(MagnifierResultVb dObj){
		List<MagnifierResultVb> collTmp = getMagnifierQuery(dObj);
		MagnifierResultVb vObject = new MagnifierResultVb();
		if(collTmp != null && collTmp.size() > 0) {
			vObject = collTmp.get(0); 
			dObj.setQuery(vObject.getTargetField12());
			dObj.setNumberOfCols(Integer.parseInt(vObject.getTargetField13()));
			dObj.setTargetField14(vObject.getTargetField14());
			dObj.setSpecialFilter(vObject.getSpecialFilter());
			dObj.setOrderByCond(vObject.getOrderByCond());
			if(!ValidationUtil.isValid(dObj.getOrderByCond()))
				dObj.setOrderByCond("ID");
		}else {
			return null;
		}
		Vector<Object> params = new Vector<Object>();
		StringBuffer strBufApprove = null;
		String[] magColumns = dObj.getTargetField14().split(",");
		Connection con = null;
		Statement stmt = null;
		int totalRows = 0;
		String tmpTableOrg = String.valueOf("TMP"+System.currentTimeMillis())+vObject.getTargetField11();
		try
		{
			dObj.setVerificationRequired(false);
			dObj.setRecordIndicator(0);
			String query = dObj.getQuery();
			con = getConnection();
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
				    ResultSet.CONCUR_READ_ONLY);
			ReportsVb reportsVb = new ReportsVb();
			reportsVb.setPromptValue1(dObj.getPromptValue1());
			reportsVb.setPromptValue2(dObj.getPromptValue2());
			reportsVb.setPromptValue3(dObj.getPromptValue3());
			reportsVb.setPromptValue4(dObj.getPromptValue4());
			reportsVb.setPromptValue5(dObj.getPromptValue5());
			reportsVb.setPromptValue6(dObj.getPromptValue6());
			reportsVb.setPromptValue7(dObj.getPromptValue7());
			reportsVb.setPromptValue8(dObj.getPromptValue8());
			reportsVb.setPromptValue9(dObj.getPromptValue9());
			reportsVb.setPromptValue10(dObj.getPromptValue10());
			query = reportsWb.replacePromptVariables(query,reportsVb,false);
			//query = replaceMagnifierHash(query, dObj);
			String createTabScript = "SELECT * FROM ("+query+") T1";
			if("ORACLE".equalsIgnoreCase(databaseType)) {
				totalRows = stmt.executeUpdate("CREATE TABLE "+tmpTableOrg+" AS ("+createTabScript+")");
			}else if("MSSQL".equalsIgnoreCase(databaseType)) {
				if(createTabScript.contains("ORDER BY")) {
				String query1 = query.substring(0, query.indexOf("ORDER BY"));
				createTabScript = "SELECT * FROM ("+query1+") T1";
				String orderBy = vObject.getOrderByCond();
				String finalQuery = "Select * into "+tmpTableOrg+" FROM ("+createTabScript+") A ORDER BY "+orderBy;
					totalRows = stmt.executeUpdate(finalQuery);
				}else {
					totalRows = stmt.executeUpdate("Select * into "+tmpTableOrg+" FROM ("+createTabScript+") A");
				}
			}
			if(totalRows == 0) {
				return null;
			}
			String sqlTempTable = "";
			/*if("ORACLE".equalsIgnoreCase(databaseType)) {
				query = tmpTableOrg;
			}else if("MSSQL".equalsIgnoreCase(databaseType)) {
				query = "SELECT * FROM "+tmpTableOrg+"";
			}*/
			query = "SELECT * FROM "+tmpTableOrg+" ";
			ResultSet rsData = stmt.executeQuery(query);
			ResultSetMetaData metaData = rsData.getMetaData();
			int colCount = metaData.getColumnCount();
			String[] columns = new String[colCount];
			while(rsData.next()){
				for(int cn = 1;cn <= colCount;cn++) {
					String columnName = metaData.getColumnName(cn);
					columns[cn-1] = columnName;
				}
				break;
			}	
			rsData.close();
			query = query+" where 1=1 " ;
			//query = "SELECT * FROM ("+query+")";
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				SmartSearchVb data = dObj.getSmartSearchOpt().get(0);
				query = query+" AND ( ";
				for(int len = 0;len < dObj.getNumberOfCols();len++) {
					if(len == 0) {
						query = query + " UPPER("+magColumns[len] +") LIKE UPPER('%"+data.getValue()+"%')";	
					}else {
						query = query + " OR UPPER("+magColumns[len] +") LIKE UPPER('%"+data.getValue()+"%')";
					}
				}
				query = query+" ) ";
				data.getValue();
			}
//			System.out.println("query : "+query);
			if(ValidationUtil.isValid(dObj.getSpecialFilterValue()) && ValidationUtil.isValid(dObj.getSpecialFilter())) {
				query=query+" AND UPPER("+dObj.getSpecialFilter()+") = UPPER('"+dObj.getSpecialFilterValue()+"') ";
			}
			VisionUsersVb visionUsersVb = CustomContextHolder.getContext();
			if(("Y".equalsIgnoreCase(visionUsersVb.getUpdateRestriction()))){
				if(ValidationUtil.isValid(visionUsersVb.getCountry()) && "Y".equalsIgnoreCase(vObject.getRestrictionCountry())){
					query = query + " and Country IN ('"+visionUsersVb.getCountry()+ "') ";
				}
				if(ValidationUtil.isValid(visionUsersVb.getLeBook()) && "Y".equalsIgnoreCase(vObject.getRestrictionLeBook())){
					query = query + " and LE_BOOK IN ('"+visionUsersVb.getLeBook()+ "') ";
				}
				if(ValidationUtil.isValid(visionUsersVb.getAccountOfficer()) && "Y".equalsIgnoreCase(vObject.getRestrictionAo())){
					query = query + " and account_Officer IN ('"+visionUsersVb.getAccountOfficer()+ "'"+commonDao.restrictedAoPermission("MDM")+") ";
				}
				if(ValidationUtil.isValid(visionUsersVb.getOucAttribute()) && "Y".equalsIgnoreCase(vObject.getRestrictionOuc())){
					query = query + " and Vision_ouc IN ('"+visionUsersVb.getOucAttribute()+ "') ";
				}
				if(ValidationUtil.isValid(visionUsersVb.getSbuCode()) && "Y".equalsIgnoreCase(vObject.getRestrictionSbu())){
					query = query + " and Vision_Sbu IN ('"+visionUsersVb.getSbuCode()+ "') ";
				}
			}
			if(ValidationUtil.isValid(dObj.getPrompt1()) && !"'ALL'".equalsIgnoreCase(dObj.getPrompt1())) {
				query = query+" and Vision_ouc= "+dObj.getPrompt1()+" ";
			}
			if(ValidationUtil.isValid(dObj.getPrompt2()) && !"'ALL'".equalsIgnoreCase(dObj.getPrompt2())) {
				String visionSbu = commonDao.getVisionSbu(dObj.getPrompt2());
				query = query+" and Vision_Sbu= "+visionSbu+" ";
			}
			if(ValidationUtil.isValid(dObj.getPrompt3()) && !"'ALL'".equalsIgnoreCase(dObj.getPrompt3())) {
				query = query+" and account_Officer= "+dObj.getPrompt3()+" ";
			}
			String orderBy = " ORDER BY "+dObj.getOrderByCond()+" ";
			strBufApprove = new StringBuffer(query);
			if (dObj.getNumberOfCols() == 4) {
				return getQueryPopupResults(dObj, new StringBuffer(), strBufApprove, "", orderBy, params,
						getLevel4ColMapper());
			}  else if(dObj.getNumberOfCols() == 3){
				return getQueryPopupResults(dObj, new StringBuffer(), strBufApprove, "", orderBy, params,
						getThreeColMapper());
			}else {
				return getQueryPopupResults(dObj, new StringBuffer(), strBufApprove, "", orderBy, params,
						getTwoColumnMagnifierMapper());
			}
		} catch (Exception ex){
			
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is Null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;

		}finally {
			try {
				if("ORACLE".equalsIgnoreCase(databaseType)) {
					stmt.executeUpdate("DROP TABLE "+tmpTableOrg+" PURGE ");
				}else if("MSSQL".equalsIgnoreCase(databaseType)) {
					stmt.executeUpdate("DROP TABLE "+tmpTableOrg);
				}
				stmt.close();
				if(con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	protected RowMapper magnifierQueryMapper(){
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				MagnifierResultVb vObject = new MagnifierResultVb();
				vObject.setTargetField11(rs.getString("Query_ID"));
				vObject.setTargetField12(rs.getString("Query_Description"));
				vObject.setTargetField13(rs.getString("Column_Count"));
				vObject.setTargetField14(rs.getString("Mag_Column"));
				vObject.setRestrictionCountry(rs.getString("Country_Flag"));
				vObject.setRestrictionLeBook(rs.getString("LE_Book_Flag"));
				vObject.setRestrictionAo(rs.getString("AO_Flag"));
				vObject.setRestrictionOuc(rs.getString("OUC_Flag"));
				vObject.setRestrictionSbu(rs.getString("SBU_Flag"));
				vObject.setSpecialFilter(rs.getString("SPECIAL_FILTER_COL"));
				vObject.setOrderByCond(rs.getString("ORDER_BY"));
				return vObject;
			}
		};
		return mapper;
	}
	public List<MagnifierResultVb> getMagnifierQuery(MagnifierResultVb dObj){
		List<MagnifierResultVb> collTemp = null;
		final int intKeyFieldsCount = 1;
		setServiceDefaults();
		StringBuffer strBufApprove = new StringBuffer(" Select Query_ID,Query_Description,Column_Count,Mag_Column,Country_Flag,"+
				" LE_Book_Flag,AO_Flag,OUC_Flag,SBU_Flag,SPECIAL_FILTER_COL,ORDER_BY "+
				" from PRD_MAGNIFIER_QUERIES where Query_ID = ? ");
		
		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getQuery());
		
		try
		{
			collTemp = getJdbcTemplate().query(strBufApprove.toString(),objParams,magnifierQueryMapper());
			return collTemp;
		}catch(Exception ex){
			logger.error(((strBufApprove == null) ? "Error while getting Magnifier Query is Null" : strBufApprove.toString()));
			if (objParams != null)
				for(int i=0 ; i< objParams.length; i++)
					logger.error("objParams[" + i + "]" + objParams[i].toString());
			return null;
		}
	} 
	public ExceptionCode getConnection(String dbScript){
		ExceptionCode exceptionCode = new ExceptionCode();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String dbIP = "";
		String jdbcUrl = "";
		String dbServiceName = getValue(dbScript, "SERVICE_NAME");
		String dbOracleSid = getValue(dbScript, "SID");
		String dbUserName = getValue(dbScript, "USER");
		if(!ValidationUtil.isValid(dbUserName))
			dbUserName = getValue(dbScript, "DB_USER");
		String dbPassWord = getValue(dbScript, "PWD");
		if(!ValidationUtil.isValid(dbPassWord))
			dbPassWord = getValue(dbScript, "DB_PWD");
		String dbPortNumber = getValue(dbScript, "DB_PORT");
		String dbPostFix = getValue(dbScript, "SUFFIX");
		String dbPreFix = getValue(dbScript, "PREFIX");
		String dataBaseName = getValue(dbScript, "DB_NAME");
		String dataBaseType = getValue(dbScript, "DATABASE_TYPE");
		String dbInstance = getValue(dbScript, "DB_INSTANCE");
		String dbIp = getValue(dbScript, "DB_IP");		
		String serverName = getValue(dbScript, "SERVER_NAME");
		String dbSetParam1 =getValue(dbScript, "DB_SET_PARAM1");
		String dbSetParam2 =getValue(dbScript, "DB_SET_PARAM2");
		String dbSetParam3 =getValue(dbScript, "DB_SET_PARAM3");
		String version = getValue(dbScript, "JAR_VERSION");
		
		String hostName = dbServiceName;
		if(!ValidationUtil.isValid(hostName)){
			hostName = dbOracleSid;
		}
		if(ValidationUtil.isValid(dbIp))
			dbIP = dbIp;
		try{
			if("ORACLE".equalsIgnoreCase(dataBaseType)){
				jdbcUrl = "jdbc:oracle:thin:@"+dbIP+":"+dbPortNumber+":"+hostName;
				con = CommonUtils.getDbConnection(jdbcUrl, dbUserName, dbPassWord, "ORACLE", version);
			}else if("MSSQL".equalsIgnoreCase(dataBaseType)){
				if(ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)){
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";instanceName="+dbInstance+";databaseName="+hostName;
				}else if(ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(hostName)){
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";instanceName="+dbInstance;
				}else if(!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(hostName)){
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";databaseName="+hostName;
				}else{
					jdbcUrl = "jdbc:sqlserver://"+dbIP+":"+dbPortNumber+";databaseName="+hostName;
				}
				
				con = CommonUtils.getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MSSQL", version);
			}else if("MYSQL".equalsIgnoreCase(dataBaseType)){
				
				if(ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)){
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"//instanceName="+dbInstance+"//databaseName="+dataBaseName;
				}else if(ValidationUtil.isValid(dbInstance) && !ValidationUtil.isValid(dataBaseName)){
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"//instanceName="+dbInstance;
				}else if(!ValidationUtil.isValid(dbInstance) && ValidationUtil.isValid(dataBaseName)){
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"/"+dataBaseName;
				}else{
					jdbcUrl = "jdbc:mysql://"+dbIp+":"+dbPortNumber+"/"+dataBaseName;
				}	
				
				con = CommonUtils.getDbConnection(jdbcUrl, dbUserName, dbPassWord, "MYSQL", version);
			}else if("POSTGRESQL".equalsIgnoreCase(dataBaseType)){
				jdbcUrl = "jdbc:postgresql://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
				con = CommonUtils.getDbConnection(jdbcUrl, dbUserName, dbPassWord, "POSTGRESQL", version);
			}else if("SYBASE".equalsIgnoreCase(dataBaseType)){
				/* jdbc:sybase:Tds:194.170.154.136:7834?ServiceName=PROD_RCB */
	//			jdbcUrl="jdbc:jtds:sybase://"+dbIP+":"+dbPortNumber+"/"+dataBaseName;
				jdbcUrl="jdbc:sybase:Tds:"+dbIP+":"+dbPortNumber+"?ServiceName="+hostName;
				con = CommonUtils.getDbConnection(jdbcUrl, dbUserName, dbPassWord, "SYBASE", version);
			}else if("INFORMIX".equalsIgnoreCase(dataBaseType)){
				jdbcUrl = "jdbc:informix-sqli://"+dbIP+":"+dbPortNumber+"/"+dataBaseName+":informixserver="+serverName;
				con = CommonUtils.getDbConnection(jdbcUrl, dbUserName, dbPassWord, "INFORMIX", version);
			}
			if(con==null){
				exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
				exceptionCode.setErrorMsg("Problem in gaining connection with datasource");
				return exceptionCode;
			}
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:"+e.getMessage());
			return exceptionCode;
		} catch (SQLException e) {
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:"+e.getMessage());
			return exceptionCode;
		}catch(Exception e){
			e.printStackTrace();
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg("Problem in gaining connection - Cause:"+e.getMessage());
			return exceptionCode;
		}
		exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		exceptionCode.setResponse(con);
		return exceptionCode;
	}
	/*public String replaceHashTag(String str,String[] hashArr, String[] hashValArr){
		int hashIndex = 0;
		if(hashArr!=null && hashValArr!=null && hashArr.length==hashValArr.length){
			for(String hashVar:hashArr){
				str = str.replaceAll("#"+hashVar+"#", hashValArr[hashIndex]);
				hashIndex++;
			}
		}
		return str;
	}*/
	public String replaceHashTag(String str,String[] hashArr, String[] hashValArr){
		if(hashArr!=null && hashValArr!=null && hashArr.length==hashValArr.length){
			Pattern pattern = Pattern.compile("#(.*?)#",Pattern.DOTALL);
			Matcher matcher = pattern.matcher(str);
			while(matcher.find()){
				int hashIndex = 0;
				String matchedStr = matcher.group(1);
				matchedStr = matchedStr.toUpperCase().replaceAll("\\s", "\\_");
				String useStr = matcher.group(1);
				useStr =CommonUtils.escapeMetaCharacters(useStr);
				for(String hashVar:hashArr){
					if(matchedStr.equalsIgnoreCase(hashVar))
						str = str.replaceAll("#"+useStr+"#", "'"+hashValArr[hashIndex]+"'");
					hashIndex++;
				}
			}
		}
		return str;
	}
	public String getValue(String source, String key){
		try {
			Matcher regexMatcher = Pattern.compile("\\{"+key+":#(.*?)\\$@!(.*?)\\#}").matcher(source);
			return regexMatcher.find()? regexMatcher.group(2) :null;
		}catch(Exception e){
			return null;
		}
	}
	public String replaceMagnifierHash(String reportQuery,MagnifierResultVb promptsVb) {
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_1#", promptsVb.getPromptValue1());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_2#", promptsVb.getPromptValue2());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_3#", promptsVb.getPromptValue3());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_4#", promptsVb.getPromptValue4());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_5#", promptsVb.getPromptValue5());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_6#", promptsVb.getPromptValue6());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_7#", promptsVb.getPromptValue7());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_8#", promptsVb.getPromptValue8());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_9#", promptsVb.getPromptValue9());
		reportQuery = reportQuery.replaceAll("#PROMPT_VALUE_10#", promptsVb.getPromptValue10());
		reportQuery = reportQuery.replaceAll("#VISION_ID#", ""+CustomContextHolder.getContext().getVisionId());
		return reportQuery;
	}
}

