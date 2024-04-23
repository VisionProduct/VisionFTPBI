package com.vision.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vision.authentication.CustomContextHolder;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.AlphaSubTabVb;
import com.vision.vb.NumSubTabVb;
import com.vision.vb.RaProfileVb;
import com.vision.vb.SmartSearchVb;

@Component
public class RaProfilePrivilegesDao extends AbstractDao<RaProfileVb> {
	@Value("${app.productName}")
	private String productName;
	@Value("${app.databaseType}")
	private String databaseType;

	@Override
	protected RowMapper getMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				RaProfileVb profilePrivilegesVb = new RaProfileVb();
				profilePrivilegesVb.setUserGroup(rs.getString("USER_GROUP"));
				profilePrivilegesVb.setUserGroupDesc(rs.getString("USER_GROUP_Desc"));
				profilePrivilegesVb.setUserProfile(rs.getString("USER_PROFILE"));
				profilePrivilegesVb.setUserProfileDesc(rs.getString("USER_PROFILE_Desc"));
				profilePrivilegesVb.setMenuGroup(rs.getString("MENU_GROUP"));
				profilePrivilegesVb.setMenuGroupDesc(rs.getString("MENU_GROUP_NAME"));
				profilePrivilegesVb.setProfileAdd(rs.getString("P_ADD"));
				profilePrivilegesVb.setProfileModify(rs.getString("P_MODIFY"));
				profilePrivilegesVb.setProfileDelete(rs.getString("P_DELETE"));
				profilePrivilegesVb.setProfileInquiry(rs.getString("P_INQUIRY"));
				profilePrivilegesVb.setProfileVerification(rs.getString("P_VERIFICATION"));
				profilePrivilegesVb.setProfileUpload(rs.getString("P_EXCEL_UPLOAD"));
				profilePrivilegesVb.setProfileDownload(rs.getString("P_DOWNLOAD"));
				profilePrivilegesVb.setDateCreation(rs.getString("DATE_CREATION"));
				return profilePrivilegesVb;
			}
		};
		return mapper;
	}

public List<RaProfileVb> getQueryPopupResults(RaProfileVb dObj){
	StringBuffer strBufApprove = null;
		Vector<Object> params = new Vector<Object>();
		if("ORACLE".equalsIgnoreCase(databaseType)) {
		strBufApprove = new StringBuffer( " SELECT *			  		    "+	
				"   FROM (SELECT User_Group,                                            "+
				"                (SELECT Alpha_subtab_description                       "+
				"                   FROM alpha_Sub_tab                                  "+
				"                  WHERE alpha_tab = 1 AND Alpha_sub_tab = User_Group)  "+
				"                   User_Group_desc,                                    "+
				"                User_Profile,                                          "+
				"                (SELECT Alpha_subtab_description                       "+
				"                   FROM alpha_Sub_tab                                  "+
				"                  WHERE alpha_tab = 2 AND Alpha_sub_tab = User_Profile)  "+
				"                   User_Profile_desc,                                  "+
				"                t2.MENU_GROUP_SEQ Menu_group,                                         "+
				"                T2.MENU_GROUP_NAME, "+
				"                P_Add,                                                 "+
				"                P_Modify,                                              "+
				"                P_Delete,                                              "+
				"                P_Inquiry,                                             "+
				"                P_Verification,                                        "+
				"                P_Excel_Upload,P_DOWNLOAD, "+
				"                To_CHAR(t1.DATE_CREATION, 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION  "+
				"           FROM PRD_PROFILE_PRIVILEGES_NEW t1, PRD_MENU_GROUP t2          "+
				"          WHERE UPPER(MENU_GROUP) = UPPER(T2.MENU_GROUP_SEQ) and T1.Application_Access = '"+productName+"' "+
				" 	and T1.Application_Access = T2.Application_Access AND User_Group = '"+dObj.getUserGroup()+"'"+
				"          and User_profile = '"+dObj.getUserProfile()+"' ) tappr " );
		}else if("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			strBufApprove = new StringBuffer( " SELECT *			  		    "+	
					"   FROM (SELECT User_Group,                                            "+
					"                (SELECT Alpha_subtab_description                       "+
					"                   FROM alpha_Sub_tab                                  "+
					"                  WHERE alpha_tab = 1 AND Alpha_sub_tab = User_Group)  "+
					"                   User_Group_desc,                                    "+
					"                User_Profile,                                          "+
					"                (SELECT Alpha_subtab_description                       "+
					"                   FROM alpha_Sub_tab                                  "+
					"                  WHERE alpha_tab = 2 AND Alpha_sub_tab = User_Profile)  "+
					"                   User_Profile_desc,                                  "+
					"                t2.MENU_GROUP_SEQ Menu_group,                                         "+
					"                T2.MENU_GROUP_NAME, "+
					"                P_Add,                                                 "+
					"                P_Modify,                                              "+
					"                P_Delete,                                              "+
					"                P_Inquiry,                                             "+
					"                P_Verification,                                        "+
					"                P_Excel_Upload,P_DOWNLOAD,t2.Parent_Sequence,t2.Menu_Sequence,                             "+
					"                Format(t1.DATE_CREATION, 'dd-MM-yyyy HH:mm:ss') DATE_CREATION  "+
					"           FROM PRD_PROFILE_PRIVILEGES_NEW t1, PRD_VISION_MENU t2        "+
					"          WHERE UPPER(MENU_GROUP) = UPPER(T2.MENU_GROUP_SEQ) and T1.Application_Access = '"+productName+"' "+
					" 		and T1.Application_Access = T2.Application_Access AND User_Group = '"+dObj.getUserGroup()+"'"+
					"          and User_profile = '"+dObj.getUserProfile()+"' ) tappr " );
		}
		try
		{
			
			if (dObj.getSmartSearchOpt() != null && dObj.getSmartSearchOpt().size() > 0) {
				int count = 1;
				for (SmartSearchVb data: dObj.getSmartSearchOpt()){
					if(count == dObj.getSmartSearchOpt().size()) {
						data.setCondition("");
					} else {
						if(!ValidationUtil.isValid(data.getCondition()) && !("AND".equalsIgnoreCase(data.getCondition()) || "OR".equalsIgnoreCase(data.getCondition()))) {
							data.setCondition("AND");
						}
					}
					String val = CommonUtils.criteriaBasedVal(data.getCriteria(), data.getInputvalue());
					switch (data.getColumn_names()) {
					case "userGroupDesc":
						CommonUtils.addToQuerySearch(" upper(TAPPR.User_Group_desc) "+ val, strBufApprove, data.getCondition());
						break;

					case "userProfileDesc":
						CommonUtils.addToQuerySearch(" upper(TAPPR.User_Profile_desc) "+ val, strBufApprove, data.getCondition());
						break;
						
					case "menuGroupDesc":
						CommonUtils.addToQuerySearch(" upper(TAPPR.MENU_GROUP_NAME) "+ val, strBufApprove, data.getCondition());
						break;

						
					default:
					}
					count++;
				}
			}
			String orderBy=" Order by MENU_GROUP_NAME ";
			return getQueryPopupResults(dObj,new StringBuffer(), strBufApprove,new String(), orderBy, params);
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			logger.error(((strBufApprove==null)? "strBufApprove is Null":strBufApprove.toString()));
			if (params != null)
				for(int i=0 ; i< params.size(); i++)
					logger.error("objParams[" + i + "]" + params.get(i).toString());
			return null;
			}
}
	public List<RaProfileVb> getQueryResultsForReview(RaProfileVb dObj, int intStatus) {

		List<RaProfileVb> collTemp = null;
		final int intKeyFieldsCount = 3;
		StringBuffer strQueryAppr = new StringBuffer(" SELECT *			  		    "+	
				"   FROM (SELECT User_Group,                                            "+
				"                (SELECT Alpha_subtab_description                       "+
				"                   FROM alpha_Sub_tab                                  "+
				"                  WHERE alpha_tab = 1 AND Alpha_sub_tab = User_Group)  "+
				"                   User_Group_desc,                                    "+
				"                User_Profile,                                          "+
				"                (SELECT Alpha_subtab_description                       "+
				"                   FROM alpha_Sub_tab                                  "+
				"                  WHERE alpha_tab = 2 AND Alpha_sub_tab = User_Profile)  "+
				"                   User_Profile_desc,                                  "+
				"                t2.MENU_GROUP_SEQ Menu_group,                                         "+
				"                T2.MENU_GROUP_NAME, "+
				"                P_Add,                                                 "+
				"                P_Modify,                                              "+
				"                P_Delete,                                              "+
				"                P_Inquiry,                                             "+
				"                P_Verification,                                        "+
				"                P_Excel_Upload,P_DOWNLOAD, "+
				"                To_CHAR(t1.DATE_CREATION, 'DD-MM-YYYY HH24:MI:SS') DATE_CREATION  "+
				"           FROM PRD_PROFILE_PRIVILEGES_NEW t1, PRD_MENU_GROUP t2          "+
				"          WHERE UPPER(MENU_GROUP) = UPPER(T2.MENU_GROUP_SEQ) and T1.Application_Access = '"+productName+"' "+
				" 	and T1.Application_Access = T2.Application_Access AND User_Group = ? "+
				"          and User_profile = ? AND t1.MENU_GROUP = ? ) tappr " );

		Object objParams[] = new Object[intKeyFieldsCount];
		objParams[0] = new String(dObj.getUserGroup()); // [USER_GROUP]
		objParams[1] = new String(dObj.getUserProfile()); // [USER_PROFILE]
		objParams[2] = new String(dObj.getMenuGroup()); // [SCREEN NAME]
		try {
			logger.info("Executing approved query");
			collTemp = getJdbcTemplate().query(strQueryAppr.toString(), objParams, getMapper());
			return collTemp;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error: getQueryResultsForReview Exception :   ");
			logger.error(((strQueryAppr == null) ? "strQueryAppr is null" : strQueryAppr.toString()));

			return null;
		}
	}

	@Override
	protected List<RaProfileVb> selectApprovedRecord(RaProfileVb vObject) {
		return getQueryResultsForReview(vObject, Constants.STATUS_ZERO);
	}

	@Override
	protected List<RaProfileVb> doSelectPendingRecord(RaProfileVb vObject) {
		return getQueryResultsForReview(vObject, Constants.STATUS_PENDING);
	}

	@Override
	protected void setServiceDefaults() {
		serviceName = "ProfilePrivileges";
		serviceDesc = "ProfilePrivileges";
		tableName = "PRD_Profile_privileges";
		childTableName = "PRD_Profile_privileges";
		intCurrentUserId = CustomContextHolder.getContext().getVisionId();
	}

	@Override
	protected int getStatus(RaProfileVb records) {
		return records.getProfileStatus();
	}

	@Override
	protected void setStatus(RaProfileVb vObject, int status) {
		vObject.setProfileStatus(status);
	}

	@Override
	protected int doInsertionAppr(RaProfileVb vObject) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_Profile_privileges_New(USER_GROUP, USER_PROFILE, MENU_GROUP, "
					+ " P_ADD, P_MODIFY, P_DELETE, P_INQUIRY, P_VERIFICATION, P_EXCEL_UPLOAD,P_DOWNLOAD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,Application_Access,EXCLUDE_MENU_PROGRAM_LIST) "
					+ " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, 0, SysDate,SysDate,'" + productName + "',?)";
		} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_Profile_privileges_New(USER_GROUP, USER_PROFILE, MENU_GROUP, "
					+ " P_ADD, P_MODIFY, P_DELETE, P_INQUIRY, P_VERIFICATION, P_EXCEL_UPLOAD,P_DOWNLOAD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,Application_Access,EXCLUDE_MENU_PROGRAM_LIST) "
					+ " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, 0, GetDate(), GetDate(),'" + productName+"',?)";
		}
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), vObject.getMenuGroup(),
				vObject.getProfileAdd(), vObject.getProfileModify(), vObject.getProfileDelete(),
				vObject.getProfileInquiry(), vObject.getProfileVerification(), vObject.getProfileUpload(),
				vObject.getProfileDownload(), vObject.getMaker(), vObject.getVerifier(),vObject.getExcludeMenu() };

		return getJdbcTemplate().update(query, args);
	}

	private int doInsertDashboardAccess(RaProfileVb vObject, String dashboardId) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_DASHBOARD_ACCESS( " + " USER_GROUP,USER_PROFILE, DASHBOARD_ID, "
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,PRODUCT_NAME) "
					+ " Values (?, ?, ?, 0, 0, ?, ?, 0, SysDate, SysDate,?)";
		} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_DASHBOARD_ACCESS( " + " USER_GROUP,USER_PROFILE, DASHBOARD_ID, "
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,PRODUCT_NAME) "
					+ " Values (?, ?, ?, 0, 0, ?, ?, 0, GetDate(), GetDate(),?)";
		}
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), dashboardId, vObject.getMaker(),
				vObject.getVerifier(),vObject.getApplication()};
		return getJdbcTemplate().update(query, args);
	}

	private int doInsertReportAccess(RaProfileVb vObject, String reportId) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_REPORT_ACCESS( " + " USER_GROUP,USER_PROFILE, REPORT_ID, "
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,PRODUCT_NAME,RS_ID,RS_TYPE,RS_TYPE_AT) "
					+ " Values (?, ?, ?, 0, 0, ?, ?, 0, SysDate,SysDate,?, ?, ?, ?)";
		} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_REPORT_ACCESS( " + " USER_GROUP,USER_PROFILE, REPORT_ID, "
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,PRODUCT_NAME,RS_ID,RS_TYPE,RS_TYPE_AT) "
					+ " Values (?, ?, ?, 0, 0, ?, ?, 0, GetDate(), GetDate(),?, ?, ?, ?)";
		}

		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), reportId, vObject.getMaker(),
				vObject.getVerifier(),vObject.getApplication(),reportId,vObject.getRsType(),vObject.getRsTypeAt()};
		return getJdbcTemplate().update(query, args);
	}

	private int doDeleteDashboardAccess(RaProfileVb vObject) {
		String query = "Delete From PRD_DASHBOARD_ACCESS Where USER_GROUP = ? And USER_PROFILE = ? and PRODUCT_NAME = ? ";
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(),vObject.getApplication()};
		return getJdbcTemplate().update(query, args);
	}

	private int doDeleteReportAccess(RaProfileVb vObject) {
		String query = "Delete From PRD_REPORT_ACCESS Where USER_GROUP = ? And USER_PROFILE = ? and PRODUCT_NAME = ? ";
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(),vObject.getApplication()};
		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int doInsertionPend(RaProfileVb vObject) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_Profile_privileges_PEND( " + " USER_GROUP, USER_PROFILE, SCREEN_NAME, "
					+ " P_ADD, P_MODIFY, P_DELETE, P_INQUIRY, P_VERIFICATION, P_EXCEL_UPLOAD,P_DOWNLOAD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION) "
					+ " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, 0, SysDate,SysDate)";
		} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_Profile_privileges_PEND( " + " USER_GROUP, USER_PROFILE, SCREEN_NAME, "
					+ " P_ADD, P_MODIFY, P_DELETE, P_INQUIRY, P_VERIFICATION, P_EXCEL_UPLOAD,P_DOWNLOAD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION) "
					+ " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, 0, GetDate(), GetDate())";
		}
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), vObject.getScreenName(),
				vObject.getProfileAdd(), vObject.getProfileModify(), vObject.getProfileDelete(),
				vObject.getProfileInquiry(), vObject.getProfileVerification(), vObject.getProfileUpload(),
				vObject.getProfileDownload(), vObject.getMaker(), vObject.getVerifier() };

		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int doInsertionPendWithDc(RaProfileVb vObject) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_Profile_privileges_PEND( " + " USER_GROUP, USER_PROFILE, SCREEN_NAME, "
					+ " P_ADD, P_MODIFY, P_DELETE, P_INQUIRY, P_VERIFICATION, P_EXCEL_UPLOAD,P_DOWNLOAD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION) "
					+ " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, 0, SysDate, SysDate)";
		} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_Profile_privileges_PEND( " + " USER_GROUP, USER_PROFILE, SCREEN_NAME, "
					+ " P_ADD, P_MODIFY, P_DELETE, P_INQUIRY, P_VERIFICATION, P_EXCEL_UPLOAD,P_DOWNLOAD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION) "
					+ " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, 0, GetDate(), GetDate())";
		}

		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), vObject.getScreenName(),
				vObject.getProfileAdd(), vObject.getProfileModify(), vObject.getProfileDelete(),
				vObject.getProfileInquiry(), vObject.getProfileVerification(), vObject.getProfileUpload(),
				vObject.getProfileDownload(), vObject.getMaker(), vObject.getVerifier() };

		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int doUpdateAppr(RaProfileVb vObject) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Update PRD_Profile_privileges_NEW Set "
					+ " P_ADD = ?, P_MODIFY = ?,P_DELETE = ?, P_INQUIRY = ?, P_VERIFICATION = ?, P_EXCEL_UPLOAD = ?,P_DOWNLOAD = ?,MAKER = ?, VERIFIER = ?, "
					+ " DATE_LAST_MODIFIED = SYSDATE ,EXCLUDE_MENU_PROGRAM_LIST= ? "
					+ " Where USER_GROUP = ?  And USER_PROFILE = ? And MENU_GROUP = ? and Application_Access = '"+ productName + "' ";
		} else if ("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Update PRD_Profile_privileges Set "
					+ " P_ADD = ?, P_MODIFY = ?,P_DELETE = ?, P_INQUIRY = ?, P_VERIFICATION = ?, P_EXCEL_UPLOAD = ?,P_DOWNLOAD = ?,MAKER = ?, VERIFIER = ?, "
					+ " DATE_LAST_MODIFIED = GETDATE(),EXCLUDE_MENU_PROGRAM_LIST = ? "
					+ " Where USER_GROUP = ?  And USER_PROFILE = ? And MENU_GROUP = ? and Application_Access = '"+ productName + "' ";
		}
		Object args[] = { vObject.getProfileAdd(), vObject.getProfileModify(), vObject.getProfileDelete(),
				vObject.getProfileInquiry(), vObject.getProfileVerification(), vObject.getProfileUpload(),
				vObject.getProfileDownload(), vObject.getMaker(), vObject.getVerifier(),vObject.getExcludeMenu(),vObject.getUserGroup(),
				vObject.getUserProfile(), vObject.getMenuGroup() };

		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int doUpdatePend(RaProfileVb vObject) {
		String query = "";
		if("ORACLE".equalsIgnoreCase(databaseType)) {
		query = "Update PRD_Profile_privileges Set "
				+ " P_ADD = ?, P_MODIFY = ?,P_DELETE = ?, P_INQUIRY = ?, P_VERIFICATION = ?, P_EXCEL_UPLOAD = ?,P_DOWNLOAD = ?,MAKER = ?, VERIFIER = ?, "
				+ " DATE_LAST_MODIFIED = Sysdate "
				+ " Where USER_GROUP = ?  And USER_PROFILE = ? And SCREEN_NAME = ? and Application_Access = '"
				+ productName + "' ";
		}else if("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Update PRD_Profile_privileges Set "
					+ " P_ADD = ?, P_MODIFY = ?,P_DELETE = ?, P_INQUIRY = ?, P_VERIFICATION = ?, P_EXCEL_UPLOAD = ?,P_DOWNLOAD = ?,MAKER = ?, VERIFIER = ?, "
					+ " DATE_LAST_MODIFIED = GetDate() "
					+ " Where USER_GROUP = ?  And USER_PROFILE = ? And SCREEN_NAME = ? and Application_Access = '"
					+ productName + "' ";
		}
		Object args[] = { vObject.getProfileAdd(), vObject.getProfileModify(), vObject.getProfileDelete(),
				vObject.getProfileInquiry(), vObject.getProfileVerification(), vObject.getProfileUpload(),
				vObject.getProfileDownload(), vObject.getMaker(), vObject.getVerifier(), vObject.getUserGroup(),
				vObject.getUserProfile(), vObject.getScreenName() };

		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int doDeleteAppr(RaProfileVb vObject) {
		String query = "Delete From PRD_Profile_privileges_NEW Where USER_GROUP = ? And USER_PROFILE = ? And MENU_GROUP = ? and Application_Access = '"
				+ productName + "' ";
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), vObject.getMenuGroup() };
		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected int deletePendingRecord(RaProfileVb vObject) {
		String query = "Delete From PRD_Profile_privileges_NEW Where USER_GROUP = ? And USER_PROFILE = ? And MENU_GROUP = ? and Application_Access = '"
				+ productName + "'";
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), vObject.getMenuGroup()};
		return getJdbcTemplate().update(query, args);
	}

	@Override
	protected String frameErrorMessage(RaProfileVb vObject, String strOperation) {
		// specify all the key fields and their values first
		String strErrMsg = new String("");
		try {
			strErrMsg = strErrMsg + " USER_GROUP:" + vObject.getUserGroup();
			strErrMsg = strErrMsg + " USER_PROFILE:" + vObject.getUserProfile();
			strErrMsg = strErrMsg + " SCREEN_NAME:" + vObject.getScreenName();
			// Now concatenate the error message that has been sent
			if ("Approve".equalsIgnoreCase(strOperation))
				strErrMsg = strErrMsg + " failed during approve Operation. Bulk Approval aborted !!";
			else
				strErrMsg = strErrMsg + " failed during reject Operation. Bulk Rejection aborted !!";
		} catch (Exception ex) {
			strErrorDesc = ex.getMessage();
			strErrMsg = strErrMsg + strErrorDesc;
			logger.error(strErrMsg, ex);
		}
		// Return back the error message string
		return strErrMsg;
	}

	@Override
	protected String getAuditString(RaProfileVb vObject) {
		final String auditDelimiter = vObject.getAuditDelimiter();
		final String auditDelimiterColVal = vObject.getAuditDelimiterColVal();
		StringBuffer strAudit = new StringBuffer("");
		try {
			strAudit.append("USER_GROUP_AT" + auditDelimiterColVal + vObject.getUserGroupAt());
			strAudit.append(auditDelimiter);
			if (ValidationUtil.isValid(vObject.getUserGroup()))
				strAudit.append("USER_GROUP" + auditDelimiterColVal + vObject.getUserGroup().trim());
			else
				strAudit.append("USER_GROUP" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			strAudit.append("USER_PROFILE_AT" + auditDelimiterColVal + vObject.getUserProfileAt());
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getUserProfile()))
				strAudit.append("USER_PROFILE" + auditDelimiterColVal + vObject.getUserProfile().trim());
			else
				strAudit.append("USER_PROFILE" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			strAudit.append("MENU_GROUP" + auditDelimiterColVal + vObject.getMenuGroup());
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileAdd()))
				strAudit.append("P_ADD" + auditDelimiterColVal + vObject.getProfileAdd().trim());
			else
				strAudit.append("P_ADD" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileModify()))
				strAudit.append("P_MODIFY" + auditDelimiterColVal + vObject.getProfileModify().trim());
			else
				strAudit.append("P_MODIFY" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileDelete()))
				strAudit.append("P_DELETE" + auditDelimiterColVal + vObject.getProfileDelete().trim());
			else
				strAudit.append("P_DELETE" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileInquiry()))
				strAudit.append("P_INQUIRY" + auditDelimiterColVal + vObject.getProfileInquiry().trim());
			else
				strAudit.append("P_INQUIRY" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileVerification()))
				strAudit.append("P_VERIFICATION" + auditDelimiterColVal + vObject.getProfileVerification().trim());
			else
				strAudit.append("P_VERIFICATION" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileUpload()))
				strAudit.append("P_EXCEL_UPLOAD" + auditDelimiterColVal + vObject.getProfileUpload().trim());
			else
				strAudit.append("P_EXCEL_UPLOAD" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getProfileDownload()))
				strAudit.append("P_DOWNLOAD" + auditDelimiterColVal + vObject.getProfileDownload().trim());
			else
				strAudit.append("P_DOWNLOAD" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			strAudit.append("PROFILE_STATUS_NT" + auditDelimiterColVal + vObject.getProfileStatusNt());
			strAudit.append(auditDelimiter);
			strAudit.append("PROFILE_STATUS" + auditDelimiterColVal + vObject.getProfileStatus());
			strAudit.append(auditDelimiter);
			strAudit.append("RECORD_INDICATOR_NT" + auditDelimiterColVal + vObject.getRecordIndicatorNt());
			strAudit.append(auditDelimiter);
			strAudit.append("RECORD_INDICATOR" + auditDelimiterColVal + vObject.getRecordIndicator());
			strAudit.append(auditDelimiter);
			strAudit.append("MAKER" + auditDelimiterColVal + vObject.getMaker());
			strAudit.append(auditDelimiter);
			strAudit.append("VERIFIER" + auditDelimiterColVal + vObject.getVerifier());
			strAudit.append(auditDelimiter);
			strAudit.append("INTERNAL_STATUS" + auditDelimiterColVal + vObject.getInternalStatus());
			strAudit.append(auditDelimiter);
			if (ValidationUtil.isValid(vObject.getDateLastModified()))
				strAudit.append("DATE_LAST_MODIFIED" + auditDelimiterColVal + vObject.getDateLastModified().trim());
			else
				strAudit.append("DATE_LAST_MODIFIED" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

			if (ValidationUtil.isValid(vObject.getDateCreation()))
				strAudit.append("DATE_CREATION" + auditDelimiterColVal + vObject.getDateCreation().trim());
			else
				strAudit.append("DATE_CREATION" + auditDelimiterColVal + "NULL");
			strAudit.append(auditDelimiter);

		} catch (Exception ex) {
			strErrorDesc = ex.getMessage();
			strAudit = strAudit.append(strErrorDesc);
			ex.printStackTrace();
		}
		return strAudit.toString();
	}

	public List<AlphaSubTabVb> findScreenName(int menuGroup) {
		String sql = " Select MENU_PROGRAM ALPHA_SUB_TAB,Menu_Name ALPHA_SUBTAB_DESCRIPTION from PRD_VISION_MENU where SEPARATOR = 'N' "
				+ " and Menu_Group = ? and Application_Access = '" + productName + "' order by menu_Sequence ";
		Object[] lParams = new Object[1];
		lParams[0] = menuGroup;
		return getJdbcTemplate().query(sql, lParams, getAlpSubTabMapper());
	}

	public List<NumSubTabVb> getTopLevelMenu() throws DataAccessException {
		String sql = " Select MENU_GROUP_SEQ,MENU_GROUP_NAME from PRD_MENU_GROUP where MENU_GROUP_Status = 0 and APPLICATION_ACCESS='"+productName+"' ";
		Object[] lParams = new Object[1];
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				NumSubTabVb vObject = new NumSubTabVb();
				vObject.setDescription(rs.getString("MENU_GROUP_NAME"));
				vObject.setNumSubTab(rs.getInt("MENU_GROUP_SEQ"));
				return vObject;
			}
		};
		List<NumSubTabVb> profileData = getJdbcTemplate().query(sql, mapper);
		return profileData;
	}

	protected RowMapper getAlpSubTabMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb alphaSubTabVb = new AlphaSubTabVb();
				alphaSubTabVb.setAlphaSubTab(rs.getString("ALPHA_SUB_TAB"));
				alphaSubTabVb.setDescription(rs.getString("ALPHA_SUBTAB_DESCRIPTION"));
				return alphaSubTabVb;
			}
		};
		return mapper;
	}
	protected RowMapper getReportLstMapper() {
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlphaSubTabVb alphaSubTabVb = new AlphaSubTabVb();
				alphaSubTabVb.setAlphaSubTab(rs.getString("ALPHA_SUB_TAB"));
				alphaSubTabVb.setDescription(rs.getString("ALPHA_SUBTAB_DESCRIPTION"));
				alphaSubTabVb.setApplicationId(rs.getString("APPLICATION_ID"));
				return alphaSubTabVb;
			}
		};
		return mapper;
	}

	public List<AlphaSubTabVb> findUserGroupPP() {
		String sql = " Select distinct User_Group ALPHA_SUB_TAB,(Select alpha_subtab_description from Alpha_sub_tab "
				+ " where Alpha_tab = 1 and alpha_sub_tab=User_Group) ALPHA_SUBTAB_DESCRIPTION from PRD_PROFILE_PRIVILEGES_NEW  "
				+ " WHERE APPLICATION_ACCESS='"+productName+"' ";
		Object[] lParams = new Object[0];
		return getJdbcTemplate().query(sql, lParams, getAlpSubTabMapper());
	}

	public List<AlphaSubTabVb> findUserProfilePP(String userGroup) {
		String sql = " Select distinct User_Profile ALPHA_SUB_TAB,(Select alpha_subtab_description from Alpha_sub_tab "
				+ " where Alpha_tab = 2 and alpha_sub_tab=User_Profile) ALPHA_SUBTAB_DESCRIPTION from PRD_PROFILE_PRIVILEGES_NEW where User_Group = ? "
				+ " AND APPLICATION_ACCESS='"+productName+"' ";
		Object[] lParams = new Object[1];
		lParams[0] = userGroup;
		return getJdbcTemplate().query(sql, lParams, getAlpSubTabMapper());
	}

	public int doDeleteProfileDashboard(RaProfileVb vObject) {
		try {
			String query = "Delete From PRD_PROFILE_DASHBOARDS Where USER_GROUP = ? And USER_PROFILE = ? AND APPLICATION_ACCESS = ? ";
			Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(),productName};
			return getJdbcTemplate().update(query, args);
		} catch (Exception e) {
			return 0;
		}
	}

	public int doInsertionProfileDashboard(RaProfileVb vObject) {
		String query = "";
		if ("ORACLE".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_PROFILE_DASHBOARDS(USER_GROUP, USER_PROFILE, HOME_DASHBOARD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,APPLICATION_ACCESS) "
					+ " Values (?, ?, ?, 0, 0, ?, ?, 0, Sysdate, Sysdate,'" + productName + "')";
		}else if("MSSQL".equalsIgnoreCase(databaseType) || "SQLSERVER".equalsIgnoreCase(databaseType)) {
			query = "Insert Into PRD_PROFILE_DASHBOARDS(USER_GROUP, USER_PROFILE, HOME_DASHBOARD,"
					+ " PROFILE_STATUS, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS,"
					+ " DATE_LAST_MODIFIED, DATE_CREATION,APPLICATION_ACCESS) "
					+ " Values (?, ?, ?, 0, 0, ?, ?, 0, GETDATE(), GETDATE(),'" + productName + "')";
		}
		Object args[] = { vObject.getUserGroup(), vObject.getUserProfile(), vObject.getHomeDashboard(),
				vObject.getMaker(), vObject.getVerifier() };

		return getJdbcTemplate().update(query, args);
	}

	// Get Dashboard List
	public List<AlphaSubTabVb> getAllDashboardList(String type) {
		String sql = "";
		try {
			if (type == "D")
				sql = " SELECT DASHBOARD_ID ALPHA_SUB_TAB,DASHBOARD_NAME ALPHA_SUBTAB_DESCRIPTION,APPLICATION_ID FROM PRD_DASHBOARDS WHERE STATUS = 0  ORDER BY DASHBOARD_NAME ";
			else
				sql = " SELECT Report_ID ALPHA_SUB_TAB,Report_Title ALPHA_SUBTAB_DESCRIPTION,APPLICATION_ID FROM PRD_REPORT_MASTER WHERE Status = 0 order by Report_Title ";

			/*Object[] lParams = new Object[1];
			lParams[0] = productName;*/
			return getJdbcTemplate().query(sql, getReportLstMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public List<AlphaSubTabVb> getSelectedDashboardList(RaProfileVb vObject) {
		String sql = "";
		try {
			if ("9998".equalsIgnoreCase(vObject.getMenuGroup()))
				sql = " SELECT REPORT_ID ALPHA_SUB_TAB,'' ALPHA_SUBTAB_DESCRIPTION FROM PRD_REPORT_ACCESS WHERE USER_GROUP='"
						+ vObject.getUserGroup() + "' AND USER_PROFILE='" + vObject.getUserProfile()
						+ "' ORDER BY REPORT_ID ";
			else if ("9999".equalsIgnoreCase(vObject.getMenuGroup()))
				sql = "  SELECT DASHBOARD_ID ALPHA_SUB_TAB,'' ALPHA_SUBTAB_DESCRIPTION FROM PRD_DASHBOARD_ACCESS WHERE USER_GROUP='"
						+ vObject.getUserGroup() + "' AND USER_PROFILE='" + vObject.getUserProfile()
						+ "' ORDER BY DASHBOARD_ID ";

			return getJdbcTemplate().query(sql, getAlpSubTabMapper());
		} catch (Exception e) {
			return null;
		}
	}

	public ExceptionCode doInsertApprRecordForNonTrans(RaProfileVb vObject) throws RuntimeCustomException {
		List<RaProfileVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strCurrentOperation = Constants.ADD;
		strApproveOperation = Constants.ADD;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		collTemp = selectApprovedRecord(vObject);
		if (collTemp == null) {
			logger.error("Collection is null for Select Approved Record");
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		// If record already exists in the approved table, reject the addition
		if (collTemp.size() > 0) {
			int intStaticDeletionFlag = getStatus(((ArrayList<RaProfileVb>) collTemp).get(0));
			if (intStaticDeletionFlag == Constants.PASSIVATE) {
				logger.error("Collection size is greater than zero - Duplicate record found, but inactive");
				exceptionCode = getResultObject(Constants.RECORD_ALREADY_PRESENT_BUT_INACTIVE);
				throw buildRuntimeCustomException(exceptionCode);
			} else {
				logger.error("Collection size is greater than zero - Duplicate record found");
				exceptionCode = getResultObject(Constants.DUPLICATE_KEY_INSERTION);
				throw buildRuntimeCustomException(exceptionCode);
			}
		}
		// Try inserting the record
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setVerifier(getIntCurrentUserId());
		retVal = doInsertionAppr(vObject);
		if (retVal != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		} else {
			insertReportDashboardAccess(vObject);
			exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
		}
		String systemDate = getSystemDate();
		vObject.setDateLastModified(systemDate);
		vObject.setDateCreation(systemDate);
		/*
		 * exceptionCode = writeAuditLog(vObject, null); if(exceptionCode.getErrorCode()
		 * != Constants.SUCCESSFUL_OPERATION){ exceptionCode =
		 * getResultObject(Constants.AUDIT_TRAIL_ERROR); throw
		 * buildRuntimeCustomException(exceptionCode); }
		 */
		return exceptionCode;
	}

	public ExceptionCode doUpdateApprRecordForNonTrans(RaProfileVb vObject) throws RuntimeCustomException {
		List<RaProfileVb> collTemp = null;
		RaProfileVb vObjectlocal = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.MODIFY;
		strErrorDesc = "";
		strCurrentOperation = Constants.MODIFY;
		setServiceDefaults();
		vObject.setMaker(getIntCurrentUserId());
		collTemp = selectApprovedRecord(vObject);
		if (collTemp == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObjectlocal = ((ArrayList<RaProfileVb>) collTemp).get(0);
		// Even if record is not there in Appr. table reject the record
		if (collTemp.size() == 0) {
			exceptionCode = getResultObject(Constants.ATTEMPT_TO_MODIFY_UNEXISTING_RECORD);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObject.setRecordIndicator(Constants.STATUS_ZERO);
		vObject.setVerifier(getIntCurrentUserId());
		vObject.setDateCreation(vObjectlocal.getDateCreation());
		retVal = doUpdateAppr(vObject);
		if (retVal != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		} else {
			insertReportDashboardAccess(vObject);
			exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
		}
		String systemDate = getSystemDate();
		vObject.setDateLastModified(systemDate);
		/*
		 * exceptionCode = writeAuditLog(vObject, vObjectlocal);
		 * if(exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION){
		 * exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR); throw
		 * buildRuntimeCustomException(exceptionCode); }
		 */
		return exceptionCode;
	}

	protected ExceptionCode doDeleteApprRecordForNonTrans(RaProfileVb vObject) throws RuntimeCustomException {
		List<RaProfileVb> collTemp = null;
		ExceptionCode exceptionCode = null;
		strApproveOperation = Constants.DELETE;
		strErrorDesc = "";
		strCurrentOperation = Constants.DELETE;
		setServiceDefaults();
		RaProfileVb vObjectlocal = null;
		vObject.setMaker(getIntCurrentUserId());
		if ("RUNNING".equalsIgnoreCase(getBuildStatus(vObject))) {
			exceptionCode = getResultObject(Constants.BUILD_IS_RUNNING);
			throw buildRuntimeCustomException(exceptionCode);
		}
		collTemp = selectApprovedRecord(vObject);
		if (collTemp == null) {
			exceptionCode = getResultObject(Constants.ERRONEOUS_OPERATION);
			throw buildRuntimeCustomException(exceptionCode);
		}
		// If record already exists in the approved table, reject the addition
		if (collTemp.size() > 0) {
			int intStaticDeletionFlag = getStatus(((ArrayList<RaProfileVb>) collTemp).get(0));
			if (intStaticDeletionFlag == Constants.PASSIVATE) {
				exceptionCode = getResultObject(Constants.CANNOT_DELETE_AN_INACTIVE_RECORD);
				throw buildRuntimeCustomException(exceptionCode);
			}
		} else {
			exceptionCode = getResultObject(Constants.ATTEMPT_TO_DELETE_UNEXISTING_RECORD);
			throw buildRuntimeCustomException(exceptionCode);
		}
		vObjectlocal = ((ArrayList<RaProfileVb>) collTemp).get(0);
		vObject.setDateCreation(vObjectlocal.getDateCreation());

		// delete the record from the Approve Table
		retVal = doDeleteAppr(vObject);
		if ("REPORTS".equalsIgnoreCase(vObject.getScreenName())
				|| "reports".equalsIgnoreCase(vObject.getScreenName())) {
			doDeleteReportAccess(vObject);
		} else if ("DASHBOARDS".equalsIgnoreCase(vObject.getScreenName())
				|| "dashboards".equalsIgnoreCase(vObject.getScreenName())) {
			doDeleteDashboardAccess(vObject);
		}
//			vObject.setRecordIndicator(-1);
		String systemDate = getSystemDate();
		vObject.setDateLastModified(systemDate);

		if (retVal != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(retVal);
			throw buildRuntimeCustomException(exceptionCode);
		}
		else {
			exceptionCode = getResultObject(Constants.SUCCESSFUL_OPERATION);
		}
		/*if (exceptionCode.getErrorCode() != Constants.SUCCESSFUL_OPERATION) {
			exceptionCode = getResultObject(Constants.AUDIT_TRAIL_ERROR);
			throw buildRuntimeCustomException(exceptionCode);
		}*/
		return exceptionCode;
	}

	private void insertReportDashboardAccess(RaProfileVb vObject) {
		try {
			if ("9998".equalsIgnoreCase(vObject.getMenuGroup())) {
				doDeleteReportAccess(vObject);
				if (vObject.getDashboardReportlst() != null && vObject.getDashboardReportlst().size() > 0) {
					vObject.getDashboardReportlst().forEach(reportVb -> {
						vObject.setApplication(reportVb.getApplicationId());
						if(!ValidationUtil.isValid(vObject.getRsType())) {
							vObject.setRsType("REPORT");
						}
						doInsertReportAccess(vObject, reportVb.getAlphaSubTab());
					});
				}
			}
			if ("9999".equalsIgnoreCase(vObject.getMenuGroup())) {
				doDeleteDashboardAccess(vObject);
				if (vObject.getDashboardReportlst() != null && vObject.getDashboardReportlst().size() > 0) {
					vObject.getDashboardReportlst().forEach(dashboardVb -> {
						vObject.setApplication(dashboardVb.getApplicationId());
						doInsertDashboardAccess(vObject, dashboardVb.getAlphaSubTab());
					});
				}
			}
		} catch (Exception e) {
			logger.error("Error while inserting Dashboard Access:" + e.getMessage());

		}
	}
}
