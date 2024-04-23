/**
 * 
 */
package com.vision.authentication;
/**
 * @author Prabu.CJ
 *
 */
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.vision.dao.CommonDao;
import com.vision.dao.VisionUsersDao;
import com.vision.exception.ExceptionCode;
import com.vision.exception.RuntimeCustomException;
import com.vision.util.CommonUtils;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.MenuVb;
import com.vision.vb.ProfileData;
import com.vision.vb.VisionUsersVb;

@Component
public class AuthenticationBean {

	public static Logger logger = LoggerFactory.getLogger(AuthenticationBean.class);
	@Autowired
	private VisionUsersDao visionUsersDao;
	
	@Value("${app.clinetId}")
	private String clinetId;
	
	@Autowired
	private CommonDao commonDao;
	private ServletContext servletContext;
	@Autowired
	private JavaMailSender mailSender;

	public boolean processLogin(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("***processLogin***");
		HttpSession httpses = request.getSession();
		if (httpses == null || httpses.getAttribute("ntLoginDetails") == null) {
			request.setAttribute("status", "LoginError");
			logger.error("Invalid user Login Id");
			return false;
		}
		
//		String visionAppToken = String.valueOf(httpses.getAttribute("visionUserToken"));		
		VisionUsersVb lUser = new VisionUsersVb();
		String userId = (String) httpses.getAttribute("ntLoginDetails");
		try {
			if (ValidationUtil.isValid(userId)) {
				String strUserName = userId.indexOf("\\") >= 0
						? userId.substring(userId.indexOf("\\"), userId.length() - 1)
						: userId;
				VisionUsersVb visionUsersVb = new VisionUsersVb();
				visionUsersVb.setUserLoginId(strUserName.toUpperCase());
				visionUsersVb.setRecordIndicator(0);
				visionUsersVb.setUserStatus(0);
				List<VisionUsersVb> lUsers = new ArrayList<VisionUsersVb>();
				lUsers = visionUsersDao.getUserInfoForLogin(visionUsersVb);
				if (lUsers == null || lUsers.isEmpty() || lUsers.size() > 1) {
					request.setAttribute("status", "LoginError");
					logger.error("User does not exists or more than one user exists with same login id[" + strUserName + "]");
					return false;
				}
				lUser = ((ArrayList<VisionUsersVb>) lUsers).get(0);
				String userGrpProfile [] = lUser.getUserGrpProfile().split("-");
				if(!ValidationUtil.isValid(lUser.getUserGroup())) {
					lUser.setUserGroup(userGrpProfile[0]);
					lUser.setUserProfile(userGrpProfile[1]);
				}

				String country = commonDao.findVisionVariableValue("DQ_Default_Country");
				String leBook = commonDao.findVisionVariableValue("DQ_Default_LE_BOOK");

				if (ValidationUtil.isValid(country) && ValidationUtil.isValid(leBook)) {
					httpses.setAttribute("country", country);
					httpses.setAttribute("lebook", leBook);
					lUser.setCountry(country);
					lUser.setLeBook(leBook);
				} else {
					httpses.setAttribute("country", "ZZ");
					httpses.setAttribute("lebook", "99");
					
				}
				lUser.setClientId(clinetId);
				lUser.setLastSuccessfulLoginDate(lUser.getLastActivityDate());
				ArrayList<Object> result = getMenuForUser(lUser);
				//Update Restriction
				/*if("Y".equalsIgnoreCase(lUser.getUpdateRestriction()) || "Y".equalsIgnoreCase(lUser.getAutoUpdateRestriction())){
					Map<String, List> restrictionTree = visionUsersDao.getRestrictionTreeForUser();
					for (Map.Entry<String, List> entry : restrictionTree.entrySet()) {
						List categoryData = new ArrayList();
						categoryData.add(entry.getValue());
						categoryData.add(visionUsersDao.getVisionDynamicHashVariable(entry.getKey()));
						restrictionTree.put(entry.getKey(), categoryData);
					}
					restrictionTree = visionUsersDao.doUpdateRestrictionToUserObject(lUser, restrictionTree);
					lUser.setRestrictionMap(restrictionTree);
				}*/
				
				httpses.setAttribute("userDetails", lUser);
				httpses.setAttribute("menuDetails", result);
			} else {
				logger.error("Unusual case for login context");
				return false;
			}

		} catch (Exception e) {
			logger.error("Exception in AuthenticationBean : " + e.getMessage(), e);
			request.setAttribute("status", "LoginError");
			return false;
		}
		try {
			System.out.println("Write Login Audit..");
			writeUserLoginAudit(request, userId , "SUCCF", "Login Success", lUser.getVisionId());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return true;
	}

	
	public ArrayList<Object> getMenuForUser(VisionUsersVb lCurrentUser) {
		if (lCurrentUser == null) {
			throw new RuntimeCustomException("Invalida session. Please reload the application.");
		}
		ArrayList<MenuVb> resultMenu = new ArrayList<MenuVb>();
		ArrayList<Object> result = new ArrayList<Object>();
		try {
			//List<ProfileData> topLvlMenuList = commonDao.getTopLevelMenu(lCurrentUser.getVisionId());
			List<ProfileData> topLvlMenuList = commonDao.getTopLevelMenu(lCurrentUser);
			List<ProfileData> finalMenuGrouplst = new ArrayList<ProfileData>();
			if (topLvlMenuList != null && !topLvlMenuList.isEmpty()) {
				for (ProfileData profileData : topLvlMenuList) {
					ArrayList<MenuVb> resultChilds = new ArrayList<MenuVb>();
					MenuVb lMenuVb = new MenuVb();
					lMenuVb.setMenuName(profileData.getMenuItem());
					lMenuVb.setMenuGroup(profileData.getMenuGroup());
					lMenuVb.setMenuIcon(profileData.getMenuIcon());
					lMenuVb.setMenuProgram(profileData.getMenuProgram());
					lMenuVb.setProfileAdd(profileData.getProfileAdd());
					lMenuVb.setProfileModify(profileData.getProfileModify());
					lMenuVb.setProfileDelete(profileData.getProfileDelete());
					lMenuVb.setProfileView(profileData.getProfileView());
					lMenuVb.setProfileVerification(profileData.getProfileVerification());
					lMenuVb.setProfileUpload(profileData.getProfileUpload());
					lMenuVb.setProfileDownload(profileData.getProfileDownload());
//					lMenuVb.setRecordIndicator(0);
					lMenuVb.setMenuStatus(0);
					ArrayList<MenuVb> subMenuGroup = commonDao.getSubMenuItemsForMenuGroup(profileData.getMenuGroup());
					if(subMenuGroup != null && !subMenuGroup.isEmpty()) {
						for(MenuVb menuVb : subMenuGroup) {
							ArrayList<MenuVb> subMenus = commonDao.getSubMenuItemsForSubMenuGroup(profileData.getMenuGroup(),menuVb.getParentSequence(),lCurrentUser);
							if(subMenus != null && subMenus.size() > 0) {
								menuVb.setChildren(subMenus);
							}
							menuVb.setProfileAdd(profileData.getProfileAdd());
							menuVb.setProfileModify(profileData.getProfileModify());
							menuVb.setProfileDelete(profileData.getProfileDelete());
							menuVb.setProfileView(profileData.getProfileView());
							menuVb.setProfileVerification(profileData.getProfileVerification());
							menuVb.setProfileUpload(profileData.getProfileUpload());
							menuVb.setProfileDownload(profileData.getProfileDownload());
							resultChilds.add(menuVb);
						}	
					}
					
					/*if(resultChilds != null && resultChilds.size() > 0) {*/
						lMenuVb.setChildren(resultChilds);
						finalMenuGrouplst.add(profileData);
						resultMenu.add(lMenuVb);	
					/*}*/
				}
			}
			result.add(finalMenuGrouplst);
			result.add(resultMenu);
		} catch (Exception e) {
			logger.error(
					"Exception in getting menu for the user[" + lCurrentUser.getVisionId() + "]. : " + e.getMessage(),
					e);
			throw new RuntimeCustomException("Failed to retrieve menu for your profile. Please contact System Admin.");
		}
		return result;
	}
	public boolean isFileExists(String currentScreenName) {
		try {
			String realPath = servletContext.getRealPath(currentScreenName + "Help.pdf");
			File lFile = new File(realPath);
			return lFile.exists();
		} catch (Exception exception) {
			return false;
		}
	}

	private ArrayList<MenuVb> getAllChilds(MenuVb tmpMenuVb, List<MenuVb> pChildMenu) {
		ArrayList<MenuVb> childMenus = new ArrayList<MenuVb>();
		while (pChildMenu.size() > 0) {
			MenuVb menuVb = pChildMenu.get(0);
			if (tmpMenuVb.getMenuSequence() == menuVb.getParentSequence()
					&& tmpMenuVb.getMenuSequence() != menuVb.getMenuSequence()) {
				childMenus.add(menuVb);
				pChildMenu.remove(menuVb);
				menuVb.setChildren(getAllChilds(menuVb, pChildMenu));
			} else {
				break;
			}
		}
		return childMenus;
	}

	public void updateUnsuccessfulLoginAttempts(String userId) {
		try {
			visionUsersDao.updateUnsuccessfulLoginAttempts(userId);
		} catch (Exception e) {
			logger.error("Exception in AuthenticationBean : " + e.getMessage(), e);
		}
	}

	public String getUnsuccessfulLoginAttempts(String userId) {
		try {
			if (!ValidationUtil.isValid(userId)) {
				return "0";
			}
			String legalVehicle = visionUsersDao.getUnsuccessfulLoginAttempts(userId);
			return legalVehicle;
		} catch (Exception e) {
			logger.error("Exception in getting the Login attempts : " + e.getMessage(), e);
			return null;
		}
	}

	public String getMaxLoginAttempts() {
		try {
			String MaxLogin = getCommonDao().findVisionVariableValue("MAX_LOGIN");
			if (!ValidationUtil.isValid(MaxLogin))
				MaxLogin = "3";
			return MaxLogin;
		} catch (Exception e) {
			logger.error("Exception in getting the Max Login attempts : " + e.getMessage(), e);
			return null;
		}
	}

	public String getUserStatus(String userId) {
		try {
			if (!ValidationUtil.isValid(userId)) {
				return "0";
			}
			String userStatus = visionUsersDao.getNonActiveUsers(userId);
			return userStatus;
		} catch (Exception e) {
			logger.error("Exception in getting the User Status : " + e.getMessage(), e);
			return null;
		}
	}

	public String findVisionVariableValuePasswordURL() {
		try {
			String PasswordResetURL = getCommonDao().findVisionVariableValue("PASSWORD_RESETURL");
			return PasswordResetURL;
		} catch (Exception e) {
			logger.error("Exception in getting the Password Reset URL : " + e.getMessage(), e);
			return null;
		}
	}

	public String findVisionVariableValuePasswordResrtTime() {
		try {
			String PasswordResetTime = getCommonDao().findVisionVariableValue("Password_ResetTime");
			return PasswordResetTime;
		} catch (Exception e) {
			logger.error("Exception in getting the Password Reset Time : " + e.getMessage(), e);
			return null;
		}
	}

	public ExceptionCode callProcToPopulateForgotPasswordEmail(VisionUsersVb vObject, String resultForgotBy) {
		ExceptionCode exceptionCode = new ExceptionCode();
		try {
			vObject = getVisionUsersDao().callProcToPopulateForgotPasswordEmail(vObject, resultForgotBy);
			exceptionCode.setErrorMsg(vObject.getErrorMessage());
			exceptionCode.setErrorCode(Integer.parseInt(vObject.getStatus()));
			return exceptionCode;
		} catch (RuntimeCustomException rex) {
			logger.error("Insert Exception " + rex.getCode().getErrorMsg());
			logger.error(((vObject == null) ? "vObject is Null" : vObject.toString()));
			exceptionCode = rex.getCode();
			return exceptionCode;
		}
	}

	public ExceptionCode doSendEmail(VisionUsersVb vObject, String resultForgotBy) {
		ExceptionCode exceptionCode = null;
		try {
			MimeMessage msg = prepareEmail(vObject, resultForgotBy);
			getMailSender().send(msg);
			exceptionCode = CommonUtils.getResultObject("Your Email", Constants.SUCCESSFUL_OPERATION, "hasBeenSent",
					"");
		} catch (MailAuthenticationException e) {
			e.printStackTrace();
			exceptionCode = CommonUtils.getResultObject("Your Email", Constants.ERRONEOUS_OPERATION, "hasNotBeenSent",
					"");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		} catch (Exception e) {
			e.printStackTrace();
			exceptionCode = CommonUtils.getResultObject("Your Email", Constants.ERRONEOUS_OPERATION, "hasNotBeenSent",
					"");
			exceptionCode.setOtherInfo(vObject);
			return exceptionCode;
		}
		return exceptionCode;
	}

	private MimeMessage prepareEmail(final VisionUsersVb vObject, final String resultForgotBy)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo("dakshina.deenadayalan@sunoida.com");
		helper.setText("Greetings :)");
		helper.setSubject("Mail From Spring Boot");
		return message;
	}

	public int doPasswordResetInsertion(VisionUsersVb vObj) {
		int intValue = getCommonDao().doPasswordResetInsertion(vObj);
		return intValue;
	}

	public VisionUsersVb getUserDetails() {
		return CustomContextHolder.getContext();
	}

	public VisionUsersDao getVisionUsersDao() {
		return visionUsersDao;
	}

	public void setVisionUsersDao(VisionUsersDao visionUsersDao) {
		this.visionUsersDao = visionUsersDao;
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setServletContext(ServletContext arg0) {
		servletContext = arg0;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	public void writeUserLoginAudit(HttpServletRequest request,String userLoginId,String status,String comments,int visionId) throws UnknownHostException{
		try {
			VisionUsersVb vObject = new VisionUsersVb();
			vObject.setUserLoginId(userLoginId);
			String ipAddress = request.getRemoteAddr();
			if("0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)){
				ipAddress = InetAddress.getLocalHost().getHostAddress() ;
			}
			//logger.info("IP Address:"+ipAddress);
			//logger.info("Temp Host :"+request.getRemoteHost());
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			vObject.setIpAddress(ipAddress);
			vObject.setRemoteHostName(inetAddress.getHostName());
			//logger.info("Host Name:"+inetAddress.getHostName());
			vObject.setLoginStatus(status);
			vObject.setComments(comments);
			vObject.setVisionId(visionId);
			String str = ""; 
			String macAddress = "";
			vObject.setMacAddress(commonDao.getMacAddress(ipAddress));
			//logger.info("MAC Address :"+vObject.getMacAddress());
			visionUsersDao.insertUserLoginAudit(vObject);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
