package com.sgy.util.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sgy.util.common.DateHelper;
import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;

public class PoiHelper {
	
	public Font headFont;
	public CellStyle headStyle;
	public Workbook wb;
	
	/**
	 * 构造函数
	 */
	public PoiHelper() {
		wb = new HSSFWorkbook();
		headFont = wb.createFont();
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headFont.setColor(HSSFColor.BLACK.index);

		headStyle = wb.createCellStyle();
		headStyle.setFont(headFont);
		headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	}

	/**
	 * 构造函数
	 * @param office_type office类型【2003 || 2007】
	 */
	public PoiHelper(String office_type) {
		if(office_type.equals("2003")){
			wb = new HSSFWorkbook();
		} else if(office_type.equals("2007")){
			wb = new XSSFWorkbook();
		}
		headFont = wb.createFont();
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headFont.setColor(HSSFColor.BLACK.index);

		headStyle = wb.createCellStyle();
		headStyle.setFont(headFont);
		headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	}
	
	/**
	 * 创建workbook
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Workbook createWorkBook(File file, String fileName) throws IOException {  
        if(fileName.toLowerCase().endsWith("xls")){  
            return new HSSFWorkbook(new FileInputStream(file));  
        }  
        if(fileName.toLowerCase().endsWith("xlsx")){  
            return new XSSFWorkbook(new FileInputStream(file));  
        }  
        return null;  
    }  

	/**
	 * 创建单元格
	 * @param wb excel文件
	 * @param row 行
	 * @param column 列号
	 * @param align 横向对齐方式
	 * @param valign 纵向对齐方式
	 * @return 格式化后的单元格
	 */
	public Cell createCell(Workbook wb, Row row, int column,
			int align, int valign) {
		Cell cell = row.createCell(column);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) align);
		cellStyle.setVerticalAlignment((short) valign);
		cell.setCellStyle(cellStyle);
		return cell;
	}

	/**
	 * 缺省对齐方式为:居中
	 * @param wb
	 * @param row
	 * @param column
	 * @return
	 */
	public Cell createCenterMiddleCell(Workbook wb, Row row,
			int column) {
		Cell cell = row.createCell(column);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cell.setCellStyle(cellStyle);
		return cell;
	}

	public Cell createCell(Workbook wb, Row row, int column) {
		Cell cell = row.createCell(column);
		return cell;
	}

	public static void main(String args[]) throws Exception {
		PoiHelper poiHelper = new PoiHelper();
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		Row row = sheet.createRow((short) 2);
		Cell cell = poiHelper.createCell(wb, row, 2);
		cell.setCellValue("中文测试");
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wb.write(fileOut);
		fileOut.close();
	}

	/**
	 * 
	 * @param columnIndex 列号（从0开始)
	 * @param columnWidth 列宽
	 */
	public void setColumnWidth(Sheet sheet, int columnIndex, int columnWidth) {
		sheet.setColumnWidth((short) columnIndex, (short) (35.7 * columnWidth));
	}

	public Font createFont(Workbook wb, short boldWeight, short color) {
		Font font = wb.createFont();
		if (boldWeight != -1)
			font.setBoldweight(boldWeight);
		if (color != -1)
			font.setColor(color);
		return font;
	}

	public CellStyle createCellStyle(Workbook wb, Font font,
			short valign, short align) {
		CellStyle cellStyle1 = wb.createCellStyle();
		if (font != null)
			cellStyle1.setFont(font);
		if (valign != -1)
			cellStyle1.setVerticalAlignment(valign);
		if (align != -1)
			cellStyle1.setAlignment(align);
		return cellStyle1;
	}

	public void merge(Sheet sheet, int row1, int col1, int row2, int col2) {
		sheet.addMergedRegion(new CellRangeAddress(row1, (short) col1, row2, (short) col2));
	}

	public Row createRow(Sheet sheet, int rowIndex) {
		Row row = sheet.createRow(rowIndex);
		return row;
	}

	/**
	 * eg: new PoiHelper().export(list, new String[][]{ {"用户号码", "MSISDN"},
	 * 				{"姓名", "NAME"}}, "sheet名称", "2003");
	 * @param list 数据
	 * @param map 数据键值对
	 * @param sheetName sheet名称
	 * @param office_type office类型【2003 || 2007】
	 * @return
	 */
	public Workbook export(List list, String[][] map, String sheetName, String office_type) {
		StringHelper str = new StringHelper();
		PoiHelper poiHelper = new PoiHelper(office_type);
		Sheet sheet = wb.createSheet(sheetName);
		Row row = sheet.createRow((short) 0);
		Cell cell = null;
		for (int i = 0; i < map.length; i++) {
			cell = poiHelper.createCell(wb, row, i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(map[i][0]);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i + 1);
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				poiHelper.createCell(wb, row, j).setCellValue(
						str.notEmpty(hash.get(map[j][1])));
			}
		}
		return wb;
	}

	public Workbook export2(List list, String[][] map, String sheetName, String office_type) {
		StringHelper str = new StringHelper();
		PoiHelper poiHelper = new PoiHelper(office_type);
		Sheet sheet = wb.createSheet(sheetName);
		Row row = sheet.createRow((short) 0);
		Cell cell = null;
		for (int i = 0; i < map.length; i++) {
			cell = poiHelper.createCell(wb, row, i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(map[i][0]);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i + 1);
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				if (map[j][2].equals("double")) {
					if (!str.notEmpty(hash.get(map[j][1])).equals("")) {
						poiHelper.createCell(wb, row, j).setCellValue(
								Double.valueOf(str
										.notEmpty(hash.get(map[j][1]))));
					}
				} else {
					poiHelper.createCell(wb, row, j).setCellValue(
							str.notEmpty(hash.get(map[j][1])));
				}
			}
		}
		return wb;
	}

	/**
	 * excel数据导入临时表
	 * @param file
	 * @param fileName 文件名
	 * @param map 导入行号的数组 map=new short[]{0,1}
	 * @param insert into t_test (id,msisdn) values (?,?)
	 * @param batchSql 批处理语句
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ 
	 */
	public int importExcelToTable(File file, String fileName, short[] map, String insertSql,
			BatchSql batchSql) {
		return this.importExcelToTable(file, fileName, map,insertSql,batchSql,0);
	}
	
	/**
	 * excel数据导入临时表
	 * @param file 文件
	 * @param fileName 文件名
	 * @param map 导入行号的数组 map=new short[]{0,1}
	 * @param insert into t_test (id,msisdn) values (?,?)
	 * @param batchSql 批处理语句
	 * @param row_index 改行之前的数据不导入表中
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ 
	 */
	public int importExcelToTable(File file, String fileName, short[] map, String insertSql,
			BatchSql batchSql, int row_index) {
		if (file == null) {
			return -1;
		}
		Workbook wb = null;
		try {
			wb = this.createWorkBook(file, fileName); 
		} catch (Exception ex) {
			return -2;
		}

		Sheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}

		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			Row row = (Row) iter.next();
			if (i <= row_index) {
				continue;
			}
			Object[] params = new Object[map.length];
			for (int j = 0; j < map.length; j++) {
				Cell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			batchSql.addBatch(insertSql, params);
		}
		return 1;
	}

	public String getCellStringValue(Cell cell) {
		DecimalFormat df = new DecimalFormat();
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		case Cell.CELL_TYPE_NUMERIC:
			// 判断是否是日期型的单元格
			if (DateUtil.isCellDateFormatted(cell)) {
				return new DateHelper().getDateString(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
			} else {
				try {
					return df.parse(String.valueOf(cell.getNumericCellValue())).toString().trim();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		default:
			return "";
		}
	}

	/**
	 * excel数据导入临时表(包含排序字段,但是不包含表头)
	 * @param file
	 * @param fileName 文件名
	 * @param map 导入行号的数组 map=new short[]{0,1}
	 * @param insert into t_test (id,msisdn) values (?,?)
	 * @param batchSql 批处理语句
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ 
	 */
	public int importExcelToTableWithOrder(File file, String fileName, short[] map,
			String insertSql, BatchSql batchSql) {
		if (file == null) {
			return -1;
		}

		Workbook wb = null;
		try {
			wb = this.createWorkBook(file, fileName); 
		} catch (Exception ex) {
			return -2;
		}

		Sheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}
		int index = 0;
		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			Row row = (Row) iter.next();
			if (i == 0) {
				continue;
			}
			Object[] params = new Object[map.length + 1];
			int j = 0;
			for (; j < map.length; j++) {
				Cell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			params[j] = index++;
			batchSql.addBatch(insertSql, params);
		}
		return 1;
	}

	/**
	 * excel数据导入临时表(包含排序字段和表头)
	 * @param file
	 * @param fileName 文件名
	 * @param map 导入行号的数组 map=new short[]{0,1}
	 * @param insert into t_test (id,msisdn) values (?,?)
	 * @param batchSql 批处理语句
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ 
	 */
	public int importExcelToTableWithOrderIncludeHead(File file, String fileName, short[] map,
			String insertSql, BatchSql batchSql) {
		if (file == null) {
			return -1;
		}
		Workbook wb = null;
		try {
			wb = this.createWorkBook(file, fileName);
		} catch (Exception ex) {
			return -2;
		}

		Sheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}
		int index = 0;
		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			Row row = (Row) iter.next();
			Object[] params = new Object[map.length + 1];
			int j = 0;
			for (; j < map.length; j++) {
				Cell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			params[j] = index++;
			batchSql.addBatch(insertSql, params);
		}
		return 1;
	}

	public Workbook export(List list, String sheet_name, String office_type) {
		StringHelper str = new StringHelper();
		PoiHelper poiHelper = new PoiHelper(office_type);
		Sheet sheet = wb.createSheet(sheet_name);
		Row row;
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i);
			Map map = (Map) list.get(i);
			Object[] values = map.values().toArray();
			for (int j = 0; j < values.length; j++) {
				poiHelper.createCell(wb, row, j).setCellValue(
						str.notEmpty(values[j]));
			}
		}
		return wb;
	}

	public String getCellCode(int index) {
		String rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (index / 25 < 1) {
			return String.valueOf(rows.charAt(index));
		} else {
			int cj = index / 26;
			if (cj == 0) {
				return String.valueOf(rows.charAt(index));
			}
			int mod = index % 26;
			return String.valueOf(rows.charAt(cj - 1))
					+ String.valueOf(rows.charAt(mod));

		}
	}
	/**
	 * 通过给定的模板导出
	 * @param dataList 
	 * @param filename 文件名 
	 * @param dir_get 获取模板的路径 
	 * @param dir_to 保存文件的路径 
	 * @param response 
	 * @throws Exception
	 */
	public void exportBytemplet(List dataList,String filename,String dir_get,String dir_to) throws Exception {
		Map beans = new HashMap();
		beans.put("dataList", dataList);
		XLSTransformer transformer = new XLSTransformer();
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(dir_get));
			Workbook workbook = transformer.transformXLS(is, beans);
			FileOutputStream out=new FileOutputStream(dir_to+filename);
			workbook.write(out);
			is.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
