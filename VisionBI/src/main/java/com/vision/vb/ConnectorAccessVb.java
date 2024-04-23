package com.vision.vb;

import java.util.List;

public class ConnectorAccessVb extends CommonVb {

	private String clientId = "";
	private static final long serialVersionUID = 1L;
	private String connectorId = "";
	private int userGroupAt = 1;
	private String userGroup = "";
	private int userProfileAt = 2;
	private String userProfile = "";
	private int visionId;
	private String writeFlag = "";
	private int dataConnectorStatusNt = 0;
	private int dataConnectorStatus = 0;
	List<SmartSearchVb> smartSearchOpt = null;

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

	public int getUserGroupAt() {
		return userGroupAt;
	}

	public void setUserGroupAt(int userGroupAt) {
		this.userGroupAt = userGroupAt;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public int getUserProfileAt() {
		return userProfileAt;
	}

	public void setUserProfileAt(int userProfileAt) {
		this.userProfileAt = userProfileAt;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public int getVisionId() {
		return visionId;
	}

	public void setVisionId(int visionId) {
		this.visionId = visionId;
	}

	public String getWriteFlag() {
		return writeFlag;
	}

	public void setWriteFlag(String writeFlag) {
		this.writeFlag = writeFlag;
	}

	public int getDataConnectorStatusNt() {
		return dataConnectorStatusNt;
	}

	public void setDataConnectorStatusNt(int dataConnectorStatusNt) {
		this.dataConnectorStatusNt = dataConnectorStatusNt;
	}

	public int getDataConnectorStatus() {
		return dataConnectorStatus;
	}

	public void setDataConnectorStatus(int dataConnectorStatus) {
		this.dataConnectorStatus = dataConnectorStatus;
	}

	public List<SmartSearchVb> getSmartSearchOpt() {
		return smartSearchOpt;
	}

	public void setSmartSearchOpt(List<SmartSearchVb> smartSearchOpt) {
		this.smartSearchOpt = smartSearchOpt;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
