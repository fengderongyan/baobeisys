package com.sgy.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ExcelToHtml {

	public static HSSFFont headFont;
	public static HSSFCellStyle headStyle;

	static class HeaderCell {
		public int rowIndex;
		public int colIndex;
		public String text;
		public float width;
		public String bgcolor;
		public String fontColor;
		public String fontHeight;
		public HeaderRegion headerRegion;
		public boolean ascDisplay;
		public boolean descDisplay;
	}

	static class HeaderRegion {
		public int targetRowFrom;
		public int targetRowTo;
		public int targetColumnFrom;
		public int targetColumnTo;
		public String text;
		public int rowLength;
		public int colLength;
	}

	public static String getCellStringValue(HSSFCell cell) {
		DecimalFormat df = new DecimalFormat();
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		case HSSFCell.CELL_TYPE_NUMERIC:
			try {
				return df.parse(String.valueOf(cell.getNumericCellValue()))
						.toString().trim();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		default:
			return "";
		}
	}

	public static HeaderCell[][] excelToHtml(File file) {

		ExcelColorHelper colorHelper = new ExcelColorHelper();
		HeaderCell headerCell = null;
		HeaderRegion headerRegion = null;
		HSSFFont font;
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception ex) {

		}
		HSSFSheet sheet = wb.getSheetAt(0);
		int rowNum = sheet.getPhysicalNumberOfRows();
		int cellNum = sheet.getRow(0).getPhysicalNumberOfCells();
		HeaderCell[][] headerCells = new HeaderCell[rowNum][cellNum];
		HeaderRegion[] headerRegions = new HeaderRegion[sheet.getNumMergedRegions()];
		for (int k = 0; k < sheet.getNumMergedRegions(); k++) {
			Region region = sheet.getMergedRegionAt(k);
			headerRegion = new HeaderRegion();
			headerRegion.targetRowFrom = region.getRowFrom();
			headerRegion.targetRowTo = region.getRowTo();
			headerRegion.targetColumnFrom = region.getColumnFrom();
			headerRegion.targetColumnTo = region.getColumnTo();
			headerRegion.text = getCellStringValue(sheet.getRow(
					region.getRowFrom()).getCell(region.getColumnFrom()));
			headerRegion.colLength = 1 + (region.getColumnTo() - region.getColumnFrom());
			headerRegion.rowLength = 1 + (region.getRowTo() - region.getRowFrom());
			headerRegions[k] = headerRegion;
		}

		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			HSSFRow row = (HSSFRow) iter.next();
			int cellNums = row.getPhysicalNumberOfCells();

			for (int j = 0; j < cellNums; j++) {
				headerCell = new HeaderCell();
				HSSFCell cell_tmp = row.getCell((short)j);
				headStyle = cell_tmp.getCellStyle();
				headFont = wb.getFontAt(cell_tmp.getCellStyle().getFontIndex());//得到单元格的字体 
				headerCell.rowIndex = i;
				headerCell.colIndex = j;
				headerCell.text = getCellStringValue(cell_tmp);;
				headerCell.width = sheet.getColumnWidth((short)j) / 32;
				headerCell.bgcolor = colorHelper.getHex(headStyle.getFillForegroundColor());
				headerCell.fontColor = colorHelper.getHex(headFont.getColor());
				headerCell.fontHeight = String.valueOf(headFont.getFontHeight() / 20);

				boolean hasRegion = false;
				for (int k = 0; k < headerRegions.length; k++) {
					if (i >= headerRegions[k].targetRowFrom
							&& i <= headerRegions[k].targetRowTo
							&& j >= headerRegions[k].targetColumnFrom
							&& j <= headerRegions[k].targetColumnTo) {
						headerCell.headerRegion = headerRegions[k];
						hasRegion = true;
					}
				}
				if (!hasRegion) {
					HeaderRegion headerRegion2 = new HeaderRegion();
					headerRegion2.targetRowFrom = i;
					headerRegion2.targetRowTo = i;
					headerRegion2.targetColumnFrom = j;
					headerRegion2.targetColumnTo = j;
					headerRegion2.text = getCellStringValue(sheet.getRow(i).getCell((short)j));
					headerRegion2.colLength = 1;
					headerRegion2.rowLength = 1;
					headerCell.headerRegion = headerRegion2;
				}
				headerCell.ascDisplay = (i == headerCell.headerRegion.targetRowFrom && j == headerCell.headerRegion.targetColumnFrom) ? true
						: false;
				headerCell.descDisplay = (i == headerCell.headerRegion.targetRowTo && j == headerCell.headerRegion.targetColumnFrom) ? true
						: false;
				headerCells[i][j] = headerCell;
			}
		}
		return headerCells;
	}
	
	public static HeaderCell[][] excelToHtml(CommonsMultipartFile file) {

        ExcelColorHelper colorHelper = new ExcelColorHelper();
        HeaderCell headerCell = null;
        HeaderRegion headerRegion = null;
        HSSFFont font;
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(file.getInputStream());
        } catch (Exception ex) {
            
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getPhysicalNumberOfRows();
        int cellNum = sheet.getRow(0).getPhysicalNumberOfCells();
        HeaderCell[][] headerCells = new HeaderCell[rowNum][cellNum];
        HeaderRegion[] headerRegions = new HeaderRegion[sheet.getNumMergedRegions()];
        for (int k = 0; k < sheet.getNumMergedRegions(); k++) {
            Region region = sheet.getMergedRegionAt(k);
            headerRegion = new HeaderRegion();
            headerRegion.targetRowFrom = region.getRowFrom();
            headerRegion.targetRowTo = region.getRowTo();
            headerRegion.targetColumnFrom = region.getColumnFrom();
            headerRegion.targetColumnTo = region.getColumnTo();
            headerRegion.text = getCellStringValue(sheet.getRow(
                    region.getRowFrom()).getCell(region.getColumnFrom()));
            headerRegion.colLength = 1 + (region.getColumnTo() - region.getColumnFrom());
            headerRegion.rowLength = 1 + (region.getRowTo() - region.getRowFrom());
            headerRegions[k] = headerRegion;
        }

        Iterator iter = sheet.rowIterator();
        for (int i = 0; iter.hasNext(); i++) {
            HSSFRow row = (HSSFRow) iter.next();
            int cellNums = row.getPhysicalNumberOfCells();

            for (int j = 0; j < cellNums; j++) {
                headerCell = new HeaderCell();
                HSSFCell cell_tmp = row.getCell((short)j);
                headStyle = cell_tmp.getCellStyle();
                headFont = wb.getFontAt(cell_tmp.getCellStyle().getFontIndex());//得到单元格的字体 
                headerCell.rowIndex = i;
                headerCell.colIndex = j;
                headerCell.text = getCellStringValue(cell_tmp);;
                headerCell.width = sheet.getColumnWidth((short)j) / 32;
                headerCell.bgcolor = colorHelper.getHex(headStyle.getFillForegroundColor());
                headerCell.fontColor = colorHelper.getHex(headFont.getColor());
                headerCell.fontHeight = String.valueOf(headFont.getFontHeight() / 20);

                boolean hasRegion = false;
                for (int k = 0; k < headerRegions.length; k++) {
                    if (i >= headerRegions[k].targetRowFrom
                            && i <= headerRegions[k].targetRowTo
                            && j >= headerRegions[k].targetColumnFrom
                            && j <= headerRegions[k].targetColumnTo) {
                        headerCell.headerRegion = headerRegions[k];
                        hasRegion = true;
                    }
                }
                if (!hasRegion) {
                    HeaderRegion headerRegion2 = new HeaderRegion();
                    headerRegion2.targetRowFrom = i;
                    headerRegion2.targetRowTo = i;
                    headerRegion2.targetColumnFrom = j;
                    headerRegion2.targetColumnTo = j;
                    headerRegion2.text = getCellStringValue(sheet.getRow(i).getCell((short)j));
                    headerRegion2.colLength = 1;
                    headerRegion2.rowLength = 1;
                    headerCell.headerRegion = headerRegion2;
                }
                headerCell.ascDisplay = (i == headerCell.headerRegion.targetRowFrom && j == headerCell.headerRegion.targetColumnFrom) ? true
                        : false;
                headerCell.descDisplay = (i == headerCell.headerRegion.targetRowTo && j == headerCell.headerRegion.targetColumnFrom) ? true
                        : false;
                headerCells[i][j] = headerCell;
            }
        }
        return headerCells;
    }
	
	/**
	 * flag -1 :倒序输出 1 正序输出
	 * @param headerCells
	 * @param flag
	 * @return
	 */
	public static String getHtmlStr(HeaderCell[][] headerCells,int flag){
		
		if(headerCells==null || headerCells.length==0){
			return "";
		}
		int rowNum = headerCells.length;
		int cellNum = headerCells[0].length;
		String htmlStr = "";// <table border=1 >";
		
		int i = 0;
		boolean bool = i < rowNum;
		if(flag==-1){
			i = rowNum-1;
			bool =  i >= 0;
		}
		while (bool) {
			htmlStr += "<tr>";
			for (int j = 0; j < cellNum; j++) {
				boolean bool_tem =  flag==-1 ? headerCells[i][j].descDisplay :headerCells[i][j].ascDisplay;
				if (bool_tem) {
					htmlStr += "\\n<td style=\"text-align: center;";

					if (!headerCells[i][j].fontColor.equals("000000")) {
						if(!headerCells[i][j].fontColor.equals("")){
							htmlStr += " color :"
								+ headerCells[i][j].fontColor + ";";
						}
					}
					htmlStr +="\"";
					if (!headerCells[i][j].bgcolor.equals("FFFFFF")) {
						if(!headerCells[i][j].bgcolor.equals("")){
							htmlStr += " bgcolor =\""
								+ headerCells[i][j].bgcolor + "\"";
						}
					}
//					if (headerCells[i][j].headerRegion.colLength <= 1) {
//						htmlStr += " width =\""
//								+ headerCells[i][j].width + "\"";
//					}
					if (headerCells[i][j].headerRegion.colLength > 1) {
						htmlStr += " colspan="
								+ headerCells[i][j].headerRegion.colLength
								+ " ";
					}
					if (headerCells[i][j].headerRegion.rowLength > 1) {
						htmlStr += " rowspan="
								+ headerCells[i][j].headerRegion.rowLength
								+ "";
					}
					htmlStr += ">" + headerCells[i][j].headerRegion.text
							+ "</td>";
				}
			}
			htmlStr += "</tr>";
			if(flag==-1){
				i--;
				bool =  i >= 0;
			}else{
				i++;
				bool = i < rowNum;
			}
		}
		//htmlStr += "</table>";
		return htmlStr;
	}
	
	public static Map getHtmlMap(File file){
		HeaderCell[][] headerCell = excelToHtml(file);
		Map htmlMap = new Hashtable();
		htmlMap.put("head", getHtmlStr(headerCell,1));
		htmlMap.put("foot", getHtmlStr(headerCell,-1));
		return htmlMap;
	}
	
	public static Map getHtmlMap(CommonsMultipartFile file){
        HeaderCell[][] headerCell = excelToHtml(file);
        Map htmlMap = new Hashtable();
        htmlMap.put("head", getHtmlStr(headerCell,1));
        htmlMap.put("foot", getHtmlStr(headerCell,-1));
        return htmlMap;
    }

	/** 
     * @author 徐强 2017-08-08
     * @param file
     * @return
     */ 
//    private static HeaderCell[][] excelToHtml(CommonsMultipartFile file) {
//        // TODO Auto-generated method stub
//        return null;
//    }

    public static void main(String args[]) {
		Map htmlMap =  getHtmlMap(new File("E:/test.xls"));
	}
}
