package com.vision.util;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import com.vision.vb.WordStylesVb;

public class HTMLtoDOCX {

//	private XWPFDocument document;

	/*
	 * public HTMLtoDOCX(String html, String docxPath) throws Exception {
	 * 
	 * this.document = new XWPFDocument();
	 * 
	 * XWPFParagraph paragraph = null; Document htmlDocument = Jsoup.parse(html);
	 * Elements htmlParagraphs = htmlDocument.select("p"); for (Element
	 * htmlParagraph : htmlParagraphs) {
	 * 
	 * // System.out.println(htmlParagraph);
	 * 
	 * paragraph = document.createParagraph(); createParagraphFromHTML(paragraph,
	 * htmlParagraph); }
	 * 
	 * FileOutputStream out = new FileOutputStream(docxPath); document.write(out);
	 * out.close(); // document.clone();
	 * 
	 * }
	 */

	public HTMLtoDOCX(String html, XWPFDocument document) throws Exception {

		XWPFParagraph paragraph = null;
		Document htmlDocument = Jsoup.parse(html);
		Elements htmlParagraphs = htmlDocument.select("p");
		for (Element htmlParagraph : htmlParagraphs) {

			paragraph = document.createParagraph();
			createParagraphFromHTML(paragraph, htmlParagraph);
		}
	}

	public HTMLtoDOCX(String html, XWPFDocument document, WordStylesVb wordStylesVb, boolean isColHeader)
			throws Exception {

		XWPFParagraph paragraph = null;
		Document htmlDocument = Jsoup.parse(html);
		Elements htmlParagraphs = htmlDocument.select("p");
		for (Element htmlParagraph : htmlParagraphs) {

			paragraph = document.createParagraph();
			createParagraphFromHTML(paragraph, htmlParagraph, wordStylesVb, isColHeader);
		}
	}

	public HTMLtoDOCX(String html, XWPFParagraph paragraph, WordStylesVb wordStylesVb, boolean isColHeader)
			throws Exception {

		Document htmlDocument = Jsoup.parse(html);
		Elements htmlParagraphs = htmlDocument.select("p");
		for (Element htmlParagraph : htmlParagraphs) {

//			paragraph = document.createParagraph();
			createParagraphFromHTML(paragraph, htmlParagraph, wordStylesVb, isColHeader);
		}
	}

	void createParagraphFromHTML(XWPFParagraph paragraph, Element htmlParagraph) {

		ParagraphNodeVisitor nodeVisitor = new ParagraphNodeVisitor(paragraph);
		NodeTraversor.traverse(nodeVisitor, htmlParagraph);

	}

	void createParagraphFromHTML(XWPFParagraph paragraph, Element htmlParagraph, WordStylesVb wordStylesVb,
			boolean isColHeader) {

		ParagraphNodeVisitor nodeVisitor = new ParagraphNodeVisitor(paragraph, wordStylesVb, isColHeader);
		NodeTraversor.traverse(nodeVisitor, htmlParagraph);

	}

	private class ParagraphNodeVisitor implements NodeVisitor {

		String nodeName;
		boolean needNewRun;
		boolean isItalic;
		boolean isBold;
		boolean isUnderlined;
		int fontSize;
		String fontNameFromDb;
		String fontColor;
		XWPFParagraph paragraph;
		XWPFRun run;
		int lineBreakCount;

		ParagraphNodeVisitor(XWPFParagraph paragraph) {
			this.paragraph = paragraph;
			this.run = paragraph.createRun();
			this.nodeName = "";
			this.isItalic = false;
			this.isBold = false;
			this.isUnderlined = false;
			this.fontSize = 11;
			this.fontColor = "000000";
			this.lineBreakCount = 0;

		}

		ParagraphNodeVisitor(XWPFParagraph paragraph, WordStylesVb wordStylesVb, boolean isColHeader) {
			this.paragraph = paragraph;
			this.run = paragraph.createRun();
			this.nodeName = "";
			/*
			 * this.isItalic = false; this.isBold = false; this.isUnderlined = false;
			 * this.fontSize = 11;
			 */
			this.fontColor = "000000";

//			String textContent = wordStylesVb.getTextContent();
			String fontName = wordStylesVb.getFontName();
			int fontSize = wordStylesVb.getFontSize();
			String fontColour = wordStylesVb.getFontColour();
			String textHighlightColour = wordStylesVb.getTextHighlightColour();
			String emphasisType = wordStylesVb.getEmphasisType();
			String alignmentType = wordStylesVb.getAlignmentType();
			int lineBreakCount = wordStylesVb.getLineBreakCount();
			int lineSpacing = wordStylesVb.getLineSpacing();
			int indentCount = wordStylesVb.getIndentCount();
			this.fontNameFromDb = fontName;
			this.fontSize = fontSize;
			this.fontColor = fontColour;
			if ("CENTER".equalsIgnoreCase(alignmentType))
				paragraph.setAlignment(ParagraphAlignment.CENTER);
			else if ("LEFT".equalsIgnoreCase(alignmentType))
				paragraph.setAlignment(ParagraphAlignment.LEFT);
			else if ("RIGHT".equalsIgnoreCase(alignmentType))
				paragraph.setAlignment(ParagraphAlignment.RIGHT);

			paragraph.setSpacingAfter(lineSpacing);
			paragraph.setSpacingBefore(lineSpacing);
			paragraph.setIndentationLeft(indentCount);
			if (isColHeader) {
				if (ValidationUtil.isValid(emphasisType)) {
					if (emphasisType.contains("B"))
						this.isBold = true;
					if (emphasisType.contains("I"))
						this.isItalic = true;
					if (emphasisType.contains("S"))
						run.setStrike(true);
					if (emphasisType.contains("U"))
						this.isUnderlined = true;
				}
			}
			this.lineBreakCount = lineBreakCount;
			if ("Y".equalsIgnoreCase(wordStylesVb.getNewPage()))
				run.addBreak(BreakType.PAGE);
			run.setFontFamily(fontName);
			run.setFontSize(fontSize);

		}

		@Override
		public void head(Node node, int depth) {
			nodeName = node.nodeName();

			boolean isFont = false;

			if ("#text".equals(nodeName)) {
				run.setText(((TextNode) node).text());
			} else if ("i".equals(nodeName)) {
				isItalic = true;
			} else if ("sup".equals(nodeName)) {
				run.setSubscript(VerticalAlign.SUPERSCRIPT);
			} else if ("sub".equals(nodeName)) {
				run.setSubscript(VerticalAlign.SUBSCRIPT);
			} else if ("c".equals(nodeName)) {
				paragraph.setAlignment(ParagraphAlignment.CENTER);
			} else if ("l".equals(nodeName)) {
				paragraph.setAlignment(ParagraphAlignment.LEFT);
			} else if ("r".equals(nodeName)) {
				paragraph.setAlignment(ParagraphAlignment.RIGHT);
			} else if ("b".equals(nodeName)) {
				isBold = true;
			} else if ("u".equals(nodeName)) {
				isUnderlined = true;
			} else if ("br".equals(nodeName)) {
				run.addBreak();
			} else if ("font".equals(nodeName)) {
				isFont = true;
				fontColor = (!"".equals(node.attr("color"))) ? node.attr("color").substring(1) : "000000";
				fontSize = (!"".equals(node.attr("size"))) ? Integer.parseInt(node.attr("size")) : 11;
			}
			run.setItalic(isItalic);
			run.setBold(isBold);
			if (isUnderlined)
				run.setUnderline(UnderlinePatterns.SINGLE);
			else
				run.setUnderline(UnderlinePatterns.NONE);
			run.setColor(fontColor);
			if (!isFont) {
				run.setFontSize(fontSize);
				run.setFontFamily(fontNameFromDb);
			}
		}

		@Override
		public void tail(Node node, int depth) {
			nodeName = node.nodeName();
			boolean isFont = false;
			if ("#text".equals(nodeName)) {
				run = paragraph.createRun(); // after setting the text in the run a new run is needed
			} else if ("i".equals(nodeName)) {
				isItalic = false;
			} else if ("b".equals(nodeName)) {
				isBold = false;
			} else if ("u".equals(nodeName)) {
				isUnderlined = false;
			} else if ("br".equals(nodeName)) {
				run = paragraph.createRun(); // after setting a break a new run is needed
			} else if ("font".equals(nodeName)) {
				fontColor = "000000";
				fontSize = 11;
				isFont = true;
			}
			run.setItalic(isItalic);
			run.setBold(isBold);
			if (isUnderlined)
				run.setUnderline(UnderlinePatterns.SINGLE);
			else
				run.setUnderline(UnderlinePatterns.NONE);
			run.setColor(fontColor);
			if (!isFont) {
				run.setFontSize(fontSize);
				run.setFontFamily(fontNameFromDb);
			}else {
				run.setFontSize(fontSize);				
			}
		}
	}

}