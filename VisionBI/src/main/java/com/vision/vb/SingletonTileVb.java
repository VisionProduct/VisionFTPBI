package com.vision.vb;

public class SingletonTileVb {
	private static final long serialVersionUID = -3560810828175889720L;
	
	private int placeHolder;
	private String sourceCol = "";
	private String dataType = "";
	private String numberFormat = "";
	private String scaling = "";
	private String decimalCount = "";
	private String preCaption = "";
	private String postCaption = "";
	private String toolTip = "";
	private String tileCaption = "";
	private String ddKeyValue = "";
	public String getTileCaption() {
		return tileCaption;
	}
	public void setTileCaption(String tileCaption) {
		this.tileCaption = tileCaption;
	}
	public String getToolTip() {
		return toolTip;
	}
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	public String getPreCaption() {
		return preCaption;
	}
	public void setPreCaption(String preCaption) {
		this.preCaption = preCaption;
	}
	public String getPostCaption() {
		return postCaption;
	}
	public void setPostCaption(String postCaption) {
		this.postCaption = postCaption;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	private String prefix = "";
	private String suffix = "";
	
	public int getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(int placeHolder) {
		this.placeHolder = placeHolder;
	}
	public String getSourceCol() {
		return sourceCol;
	}
	public void setSourceCol(String sourceCol) {
		this.sourceCol = sourceCol;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getNumberFormat() {
		return numberFormat;
	}
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
	public String getScaling() {
		return scaling;
	}
	public void setScaling(String scaling) {
		this.scaling = scaling;
	}
	public String getDecimalCount() {
		return decimalCount;
	}
	public void setDecimalCount(String decimalCount) {
		decimalCount = decimalCount;
	}
	public String getDdKeyValue() {
		return ddKeyValue;
	}
	public void setDdKeyValue(String ddKeyValue) {
		this.ddKeyValue = ddKeyValue;
	}
	
	
}
