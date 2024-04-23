package com.vision.vb;

public class InputDestinationVb {
	
	private String tableName = "";
	private String url = "";
	private String username = "";
	private String password = "";
	private String driver = "";
	private String format = "";
	private String stagingTableName = "";
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getStagingTableName() {
		return stagingTableName;
	}
	public void setStagingTableName(String stagingTableName) {
		this.stagingTableName = stagingTableName;
	}

	
}
