package com.sgy.util.excel;

import jxl.Cell;
import jxl.Sheet;

public class ExcelHelper {
	public static String getContents(Sheet sheet, int rowIndex, int colIndex) {
		String contents = "";
		try {
			Cell[] cells = sheet.getRow(rowIndex);
			contents = cells[colIndex].getContents().trim();
		} catch (Exception e) {
			contents = "";
		}
		return contents;
	}
}
