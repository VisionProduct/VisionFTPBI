package com.vision.vb;

public class WordStylesVb {
	private int pageNumber= 0;
	private String newPage = "N";
	private int startingIndex= 0;
	private String fontName = "";
	private int fontSize = 10;
	private String fontColour = "000000";
	private String textHighlightColour = "";
	private int emphasisTypeAt = 3002;
	private String emphasisType = "";
	private int alignmentTypeAt = 3003;
	private String alignmentType = "";
	private int lineBreakCount= 0;
	private int lineSpacing= 0;
	private int indentCount= 0;
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getNewPage() {
		return newPage;
	}
	public void setNewPage(String newPage) {
		this.newPage = newPage;
	}
	public int getStartingIndex() {
		return startingIndex;
	}
	public void setStartingIndex(int startingIndex) {
		this.startingIndex = startingIndex;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public String getFontColour() {
		return fontColour;
	}
	public void setFontColour(String fontColour) {
		this.fontColour = fontColour;
	}
	public String getTextHighlightColour() {
		return textHighlightColour;
	}
	public void setTextHighlightColour(String textHighlightColour) {
		this.textHighlightColour = textHighlightColour;
	}
	public int getEmphasisTypeAt() {
		return emphasisTypeAt;
	}
	public void setEmphasisTypeAt(int emphasisTypeAt) {
		this.emphasisTypeAt = emphasisTypeAt;
	}
	public String getEmphasisType() {
		return emphasisType;
	}
	public void setEmphasisType(String emphasisType) {
		this.emphasisType = emphasisType;
	}
	public int getAlignmentTypeAt() {
		return alignmentTypeAt;
	}
	public void setAlignmentTypeAt(int alignmentTypeAt) {
		this.alignmentTypeAt = alignmentTypeAt;
	}
	public String getAlignmentType() {
		return alignmentType;
	}
	public void setAlignmentType(String alignmentType) {
		this.alignmentType = alignmentType;
	}
	public int getLineBreakCount() {
		return lineBreakCount;
	}
	public void setLineBreakCount(int lineBreakCount) {
		this.lineBreakCount = lineBreakCount;
	}
	public int getLineSpacing() {
		return lineSpacing;
	}
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	public int getIndentCount() {
		return indentCount;
	}
	public void setIndentCount(int indentCount) {
		this.indentCount = indentCount;
	}
}
