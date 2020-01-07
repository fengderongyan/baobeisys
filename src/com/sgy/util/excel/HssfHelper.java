package com.sgy.util.excel;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import web.service.BaseService;

import com.sgy.util.Constants;
import com.sgy.util.common.DateHelper;
import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;
import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.db.ProcHelper;
import com.sgy.util.spring.RequestHelper;
import com.sgy.util.spring.SpringHelper;

public class HssfHelper {
	public HSSFFont headFont;
	public HSSFCellStyle headStyle;
	public HSSFWorkbook wb;
	public final Logger logger = Logger.getLogger(this.getClass());

	public HssfHelper() {
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
	 * 
	 * @param wb
	 *            excel文件
	 * @param row
	 *            行
	 * @param column
	 *            列号
	 * @param align
	 *            横向对齐方式
	 * @param valign
	 *            纵向对齐方式
	 * @return 格式化后的单元格
	 */
	public HSSFCell createCell(HSSFWorkbook wb, HSSFRow row, int column,
			int align, int valign) {
		HSSFCell cell = row.createCell((short) column);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment((short) align);
		cellStyle.setVerticalAlignment((short) valign);
		cell.setCellStyle(cellStyle);
		//cell.setEncoding((short) 1); // 支持中文导出
		return cell;
	}

	/**
	 * 缺省对齐方式为:居中
	 * 
	 * @param wb
	 * @param row
	 * @param column
	 * @return
	 */
	public HSSFCell createCenterMiddleCell(HSSFWorkbook wb, HSSFRow row,
			int column) {
		HSSFCell cell = row.createCell((short) column);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cell.setCellStyle(cellStyle);
		//cell.setEncoding((short) 1); // 支持中文导出
		return cell;
	}

	public HSSFCell createCell(HSSFWorkbook wb, HSSFRow row, int column) {
		HSSFCell cell = row.createCell((short) column);
		//cell.setEncoding((short) 1); // 支持中文导出
		return cell;
	}

	public static void main(String args[]) throws Exception {
		HssfHelper hssfHelper = new HssfHelper();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");
		HSSFRow row = sheet.createRow((short) 2);
		// HSSFCell cell=hssfHelper.createCell(wb, row,
		// 0,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		HSSFCell cell = hssfHelper.createCell(wb, row, 2);
		cell.setCellValue("中文测试");
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wb.write(fileOut);
		fileOut.close();
	}

	/**
	 * 
	 * @param columnIndex
	 *            列号（从0开始)
	 * @param columnWidth
	 *            列宽
	 */
	public void setColumnWidth(HSSFSheet sheet, int columnIndex, int columnWidth) {
		sheet.setColumnWidth((short) columnIndex, (short) (35.7 * columnWidth));
	}

	public HSSFFont createFont(HSSFWorkbook wb, short boldWeight, short color) {
		HSSFFont font = wb.createFont();
		if (boldWeight != -1)
			font.setBoldweight(boldWeight);
		if (color != -1)
			font.setColor(color);
		return font;
	}

	public HSSFCellStyle createCellStyle(HSSFWorkbook wb, HSSFFont font,
			short valign, short align) {
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		if (font != null)
			cellStyle1.setFont(font);
		if (valign != -1)
			cellStyle1.setVerticalAlignment(valign);
		if (align != -1)
			cellStyle1.setAlignment(align);
		return cellStyle1;
	}

	public void merge(HSSFSheet sheet, int row1, int col1, int row2, int col2) {
		sheet.addMergedRegion(new Region(row1, (short) col1, row2, (short) col2));
	}

	public HSSFRow createRow(HSSFSheet sheet, int rowIndex) {
		HSSFRow row = sheet.createRow(rowIndex);
		return row;
	}

	/**
	 * eg: new HssfHelper().export(list, new String[][]{ {"用户号码", "MSISDN"},
	 * {"姓名", "NAME"}, {"投诉类型", "COMPLAIN_TYPE"}, {"工单流水号","TASKNO"},
	 * {"录音流水号","RECORDNO"}, {"投诉事由","COMPLAIN_CONTENT"}});
	 * 
	 * @param list
	 * @param map
	 * @return
	 */
	public HSSFWorkbook export(List list, String[][] map, String sheetName) {
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper();
		HSSFSheet sheet = wb.createSheet(sheetName);
		HSSFRow row = sheet.createRow((short) 0);
		HSSFCell cell = null;
		for (int i = 0; i < map.length; i++) {
			cell = hssfHelper.createCell(wb, row, i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(map[i][0]);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i + 1);
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				hssfHelper.createCell(wb, row, j).setCellValue(
						str.notEmpty(hash.get(map[j][1])));
			}
		}
		return wb;
	}

	public HSSFWorkbook export2(List list, String[][] map, String sheetName) {
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper();
		HSSFSheet sheet = wb.createSheet(sheetName);
		HSSFRow row = sheet.createRow((short) 0);
		HSSFCell cell = null;
		for (int i = 0; i < map.length; i++) {
			cell = hssfHelper.createCell(wb, row, i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(map[i][0]);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i + 1);
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				if (map[j][2].equals("double")) {
					if (!str.notEmpty(hash.get(map[j][1])).equals("")) {
						hssfHelper.createCell(wb, row, j).setCellValue(
								Double.valueOf(str
										.notEmpty(hash.get(map[j][1]))));
					}
				} else {
					hssfHelper.createCell(wb, row, j).setCellValue(
							str.notEmpty(hash.get(map[j][1])));
				}
			}
		}
		return wb;
	}

	public HSSFWorkbook exportNumber(List list, String[][] map, String sheetName) {
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper();
		HSSFSheet sheet = wb.createSheet(sheetName);
		HSSFRow row = sheet.createRow((short) 0);
		HSSFCell cell = null;
		for (int i = 0; i < map.length; i++) {
			cell = hssfHelper.createCell(wb, row, i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(map[i][0]);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i + 1);
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				try {
					hssfHelper.createCell(wb, row, j).setCellValue(
							Float.valueOf(str.notEmpty(hash.get(map[j][1]))));
				} catch (Exception e) {
					hssfHelper.createCell(wb, row, j).setCellValue(
							str.notEmpty(hash.get(map[j][1])));
				}
			}
		}
		return wb;
	}

	public HSSFWorkbook exportByPageSize(List list, String[][] map, int pageSize) {
		if (pageSize <= 0)
			pageSize = 65530;
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		int currPage = 0;
		int pages = list.size() / pageSize + 1;
		for (int i = 0; i < list.size(); i++) {
			if (i % pageSize == 0) {
				int currPageSize = currPage == pages - 1 ? pageSize * currPage
						+ list.size() % pageSize : pageSize * (currPage + 1);
				String sheetName = (pageSize * currPage + 1) + "~"
						+ currPageSize;
				sheet = wb.createSheet(sheetName);
				row = sheet.createRow((short) 0);
				for (int j = 0; j < map.length; j++) {
					cell = hssfHelper.createCell(wb, row, j);
					cell.setCellStyle(headStyle);
					cell.setCellValue(map[j][0]);
				}
				currPage++;
			}
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				row = sheet.createRow((short) (i % pageSize) + 1);
				hssfHelper.createCell(wb, row, j).setCellValue(
						str.notEmpty(hash.get(map[j][1])));
			}
		}
		return wb;
	}

	/**
	 * 
	 * @param file
	 * @param map
	 *            导入行号的数组
	 * @param insertSql
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ insert
	 *         into t_test (id,msisdn) values (?,?) map=new short[]{0,1};
	 */
	public int importExcelToTable(File file, short[] map, String insertSql,
			DBHelperSpring db) {
		if (file == null) {
			return -1;
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception ex) {
			return -2;
		}
		List questionList = new ArrayList<Hashtable>();
		Hashtable question = null;
		List answers = null;

		HSSFSheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}
		BatchSql batch = new BatchSql();
		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			HSSFRow row = (HSSFRow) iter.next();
			if (i == 0) {
				continue;
			}
			Object[] params = new Object[map.length];
			for (int j = 0; j < map.length; j++) {
				HSSFCell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			batch.addBatch(insertSql, params);
		}
		return db.doInTransaction(batch);
	}

	/**
	 * 
	 * @param file
	 * @param map
	 *            导入行号的数组
	 * @param insertSql
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ insert
	 *         into t_test (id,msisdn) values (?,?) map=new short[]{0,1};
	 */
	public int importExcelToTable(File file, short[] map, String insertSql,
			BatchSql batchSql) {
		return this.importExcelToTable(file,map,insertSql,batchSql,0);
	}
	
	/**
	 * 
	 * @param file
	 * @param map 导入行号的数组
	 * @param insertSql
	 * @param row_index 从第几行开始导入
	 * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ insert
	 *         into t_test (id,msisdn) values (?,?) map=new short[]{0,1};
	 */
	public int importExcelToTable(File file, short[] map, String insertSql,
			BatchSql batchSql, int row_index) {
		if (file == null) {
			return -1;
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception ex) {
			return -2;
		}

		HSSFSheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}

		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			HSSFRow row = (HSSFRow) iter.next();
			if (i <= row_index) {
				continue;
			}
			Object[] params = new Object[map.length];
			for (int j = 0; j < map.length; j++) {
				HSSFCell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			batchSql.addBatch(insertSql, params);
		}
		return 1;
	}
	
	/**
     * 
     * @param file
     * @param map 导入行号的数组
     * @param insertSql
     * @param row_index 从第几行开始导入
     * @return -1:文件上传错误，该文件不存在！ -2:Excel读取错误！ -3:Excel导入错误，未找到Sheet！ insert
     *         into t_test (id,msisdn) values (?,?) map=new short[]{0,1};
     */
    public int importExcelToTable(CommonsMultipartFile file, short[] map, String insertSql,
            BatchSql batchSql, int row_index) {
        if (file == null) {
            return -1;
        }
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(file.getInputStream());
        } catch (Exception ex) {
            return -2;
        }

        HSSFSheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            return -3;
        }

        Iterator iter = sheet.rowIterator();
        for (int i = 0; iter.hasNext(); i++) {
            HSSFRow row = (HSSFRow) iter.next();
            if (i <= row_index) {
                continue;
            }
            Object[] params = new Object[map.length];
            for (int j = 0; j < map.length; j++) {
                HSSFCell cell = row.getCell(map[j]);
                params[j] = getCellStringValue(cell);
            }
            batchSql.addBatch(insertSql, params);
        }
        return 1;
    }

	public String getCellStringValue(HSSFCell cell) {
		DecimalFormat df = new DecimalFormat();
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();
		case HSSFCell.CELL_TYPE_NUMERIC:
			// 判断是否是日期型的单元格
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return new DateHelper().getDateString(cell.getDateCellValue(),
						"yyyy-MM-dd HH:mm:ss");
			} else {
				try {
					return df.parse(String.valueOf(cell.getNumericCellValue()))
							.toString().trim();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		default:
			return "";
		}
	}

	public int importExcelToTableWithOrder(File file, short[] map,
			String insertSql, BatchSql batchSql) {
		if (file == null) {
			return -1;
		}

		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception ex) {
			return -2;
		}

		HSSFSheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}
		int index = 0;
		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			HSSFRow row = (HSSFRow) iter.next();
			if (i == 0) {
				continue;
			}
			Object[] params = new Object[map.length + 1];
			int j = 0;
			for (; j < map.length; j++) {
				HSSFCell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			params[j] = index++;
			batchSql.addBatch(insertSql, params);
		}
		return 1;
	}

	public int importExcelToTableWithOrderIncludeHead(File file, short[] map,
			String insertSql, BatchSql batchSql) {
		if (file == null) {
			return -1;
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception ex) {
			return -2;
		}

		HSSFSheet sheet = wb.getSheetAt(0);
		if (sheet == null) {
			return -3;
		}
		int index = 0;
		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			HSSFRow row = (HSSFRow) iter.next();
			Object[] params = new Object[map.length + 1];
			int j = 0;
			for (; j < map.length; j++) {
				HSSFCell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell);
			}
			params[j] = index++;
			batchSql.addBatch(insertSql, params);
		}
		return 1;
	}

	public HSSFWorkbook export(List list, String sheet_name) {
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper();
		HSSFSheet sheet = wb.createSheet(sheet_name);
		HSSFRow row;
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i);
			Map map = (Map) list.get(i);
			Object[] values = map.values().toArray();
			for (int j = 0; j < values.length; j++) {
				hssfHelper.createCell(wb, row, j).setCellValue(
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
			HSSFWorkbook workbook = transformer.transformXLS(is, beans);
			FileOutputStream out=new FileOutputStream(dir_to+filename);
			workbook.write(out);
			is.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出csv文件
	 * @param sql
	 * @param map
	 *  map[0][2]==0  带序号
	 *  map[0][2]==1  科学计数法（数字转文本）
	 *  map[0][2]==2  替换,为，
	 *  map[0][2]==3  日期型
	 * @return
	 * @throws Exception
	 */
	public static void exportCsv(String sql, String[][] map, PrintWriter out) throws Exception {
		DBHelperSpring db = (DBHelperSpring) SpringHelper.getBean("dbHelper");
		StringHelper str = new StringHelper();
		Connection conn = db.getDataSource().getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
        try{
        	for (int i = 0; i < map.length; i++) {
				out.print(map[i][0]+",");
			}
			out.print("\r\n");
    		ResultSet rs = ps.executeQuery();
    		int r =  0;
			while(rs.next()) {
				for (int j = 0; j < map.length; j++) {
					Object ob = new Object();
					if(map[j][2].equals("0")){
						r = r + 1;
						ob = r;
					}else if(map[j][2].equals("3")){
						if(rs.getObject((map[j][1]))==null){
							ob="";
						}else{
							ob = str.notEmpty(rs.getDate(map[j][1])+" "+rs.getTime(map[j][1]));
						}
					}else{
						ob = str.notEmpty(rs.getObject((map[j][1])));
					}
					if(map[j][2].equals("1")){
						out.print("	"+ob+",");
					}else if(map[j][2].equals("2")){
						if(ob==null){
							out.print(ob+",");
						}else {
							out.print(ob.toString().replaceAll(",", "，").replaceAll("\r\n", " ")+",");
						}
					}else {
						out.print(ob+",");
					}
				}
				out.print("\r\n");
			}
			rs.close();
        }
        catch(Exception e){   
        	e.printStackTrace();        	
        }
        finally{
			ps.close();
			conn.close();
        }
	}
	public int importExcelToTableWithOrderIncludeHead(File file, short[] map,
			String insertSql, BatchSql batchSql, int sheetIndex) {
		if (file == null) {
			return -1;
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception ex) {
			return -2;
		}

		HSSFSheet sheet = wb.getSheetAt(sheetIndex);
		if (sheet == null) {
			return -3;
		}
		int index = 0;
		Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) {
			HSSFRow row = (HSSFRow) iter.next();
			Object[] params = new Object[map.length + 1];
			int j = 0;
			for (; j < map.length; j++) {
				HSSFCell cell = row.getCell(map[j]);
				params[j] = getCellStringValue(cell).trim().replace("　", "");
			}
			params[j] = index++;
			batchSql.addBatch(insertSql, params);
			// StringHelper str=new StringHelper();
			// System.out.println(str.getSql(insertSql,params));
		}
		return 1;
	}
	
	public void export(List list, HSSFSheet sheet) {
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper();
		HSSFRow row;
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i);
			Map map = (Map) list.get(i);
			Object[] values = map.values().toArray();
			for (int j = 0; j < values.length; j++) {
				hssfHelper.createCell(wb, row, j).setCellValue(
						str.notEmpty(values[j]));
			}
		}
	}
	
	public HSSFCell createHeadCell(HSSFWorkbook wb, HSSFRow row, int column) {
    	HSSFFont headFont = wb.createFont();
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headFont.setColor(HSSFColor.BLACK.index);
		HSSFCellStyle headStyle = wb.createCellStyle();
		headStyle.setFont(headFont);
		headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell = row.createCell((short)column);
        cell.setCellStyle(headStyle);
        return cell;
    }
	
	public void export2(HSSFWorkbook wb, List list, String[][] map, int headRows, int headCols, int sheetIndex) {
		StringHelper str = new StringHelper();
		HssfHelper hssfHelper = new HssfHelper(); 
		HSSFSheet sheet = wb.getSheetAt(sheetIndex);
		HSSFRow row = null; 
		HSSFCell cell = null;
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((short) i + headRows);
			Map hash = (Map) list.get(i);
			for (int j = 0; j < map.length; j++) {
				cell = hssfHelper.createCell(wb, row, j + headCols); 
				String val = str.notEmpty(hash.get(map[j][0]));
				if(map[j][1].equals("double")){
					if(!val.equals("")){
						cell.setCellValue(Double.valueOf(val));
					}				
				} else if(map[j][1].equals("date")) {//传入完整日期date类型字段,去除最后的.0后缀
					if(!val.equals("")){
						val = val.substring(0, val.length()-2);
						System.out.println(val);
						cell.setCellValue(val);
					}	
				} else{
					cell.setCellValue(val);
				}
			}
		}
	}
	
	/**
	 * 校验Excel表头
	 * @param hwb 导入的文件
	 * @param action
	 * @param titles 正确表头字符串数组
	 * @param sheet_index 表索引 从0开始
	 * @param row_index 行索引 从0开始
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String checkExcelHead(MultipartFile file, String[] titles, int sheet_index, int row_index) 
	{
		String message = "";
		if (file == null || file.getSize() == 0) 
		{
			message = "导入错误：文件不存在或文件大小为0!";
			return message;
		}
        
        try 
        {
        	HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            if (sheet == null) 
            {
            	message = "导入错误：未找到sheet！";
            }
            else
            {
            	HSSFRow row = wb.getSheetAt(sheet_index).getRow(row_index);
            	if (row == null) 
            	{
            		message = "Excel标题列加载错误：没有标题列!";
            		return message;
            	}
            	
            	int cols = row.getPhysicalNumberOfCells();
            	if (cols != titles.length) 
            	{
            		message = "导入错误：导入的Excel必须为" + titles.length + "列！";
            		return message;
            	}
            	
            	for (int i = 0; i < cols; i++) 
            	{
            		HSSFCell cell = row.getCell((short) i);
            		String cellValue = this.getCellStringValue(cell);
            		if (!cellValue.equals(titles[i])) 
            		{
            			message = "导入错误：Excel第" + this.getCellCode(i) + "列列名必须为“" + titles[i] + "”！";
            			break;
            		}
            	}
            	
            }
        
        }
        catch(Exception e)
        {
            message = "导入错误：文件读取异常!";
        }
        
		return message;
	}
	
    
    /**
     * 导入Excel文件到数据库表中
     * @param request
     * @param file	MultipartFile类型的文件
     * @param tempTableName	临时表名前缀
     * @param createFields	创建临时表的字段
     * @param insertFields	插入临时表的字段
     * @param excelParams	导出excel的参数
     * @param procName		存储过程名
     * @param procInParams	存储过程参数
     * @param startRow		从Excel第几行开始导入
     * @param orderFlag		顺序标志，如果为true，临时表中增加show_order字段，遍历excel时插入数据，输出结果文件时用于排序
     * @return 1：导入并解析成功  0：SQL语句执行失败  -1：导入失败（Excel数据问题或执行存储过程出现异常）  
     * 		  -2：文件上传错误，该文件不存在！  -3：Excel读取错误！  -4：Excel导入错误，未找到Sheet！ 
     * 		  -5：excel文件的列数不正确
     */
    public int impFileToDB(HttpServletRequest request, MultipartFile file, String tempTableName, 
            String[] createFields, String[][] insertFields, String[][] excelParams, String procName, 
            String[][] procInParams, int startRow, boolean orderFlag, DBHelperSpring db,String dirPath)
    {
    	if (file == null) 
		{
			return -2;	// 文件不存在
		}
		HSSFWorkbook wb = null;
		try 
		{
			wb = new HSSFWorkbook(file.getInputStream());
			int cols = wb.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();	// excel文件的列数
			// 如果读取到的excel文件的列数小于创建临时表的字段个数
			if(cols < createFields.length)
			{
				return -5;	// excel文件的列数不正确
			}
		} 
		catch (Exception ex) 
		{
			return -3;	// excel读取错误
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		if (sheet == null) 
		{
			return -4;	// excel导入错误，未找到sheet
		}
		
        BatchSql batchSql = new BatchSql();
        
        // 临时表名
        tempTableName = tempTableName + DateHelper.getToday("yyyyMMddHHmmss") + Math.round(Math.random() * 1000);
        
        // 创建临时表的SQL语句
        String createSql = "create table " + tempTableName + "(";
        StringBuffer field = new StringBuffer("");	// 创建语句的字段
        for(String createfield : createFields)
        {
        	field.append(createfield + " varchar(1000),");
        }
        // 拼接创建的SQL语句
        if(orderFlag)
        {
        	createSql = createSql + field.toString() + "show_order number)";
        }
        else
        {
        	createSql = createSql + field.deleteCharAt(field.length() - 1) + ")";
        }
//        System.out.println("创建语句： " + createSql);
        batchSql.addBatch(createSql);
        // 插入临时表的SQL语句
        String insertSql = "insert into " + tempTableName + "(";
        StringBuffer field2 = new StringBuffer("");	// 插入语句的字段
        for (int i = 0; i < insertFields.length; i++) 
		{
        	field2.append(insertFields[i][0] + ",");
		}
        if(orderFlag)
        {
        	insertSql = insertSql + field2.toString() + "show_order) values(";
        }
        else
        {
        	insertSql = insertSql + field2.deleteCharAt(field2.length() - 1) + ") values(";
        }
        
        // 拼接占位符
        StringBuffer quesMark = new StringBuffer("");
        for (int i = 0; i < insertFields.length; i++) 
		{
        	quesMark.append("?,");
		}
        // 顺序标志为true，则直接增加一个占位符
        if(orderFlag)
        {
        	quesMark.append("?");
        }
        else
        {
        	quesMark.deleteCharAt(quesMark.length() - 1); // 去掉末尾的逗号
        }
        // 拼接插入的SQL语句
        insertSql = insertSql + quesMark.toString() + ")";
//        System.out.println("插入语句： " + insertSql);
        // 将Excel导入数据库表中
        Iterator iter = sheet.rowIterator();
		for (int i = 0; iter.hasNext(); i++) 
		{
			HSSFRow row = (HSSFRow) iter.next();
			if (i < startRow) 
			{
				continue;
			}
			// 顺序标志为true，则直接需要给show_order插入参数
			Object[] params = null;
	        if(orderFlag)
	        {
	        	params = new Object[insertFields.length + 1];
				for (int j = 0; j < insertFields.length; j++) 
				{
					HSSFCell cell = row.getCell(Integer.parseInt(insertFields[j][1]));
					params[j] = getCellStringValue(cell);
				}
				params[insertFields.length] = i;
	        }
	        else
	        {
	        	params = new Object[insertFields.length];
				for (int j = 0; j < insertFields.length; j++) 
				{
					HSSFCell cell = row.getCell(Integer.parseInt(insertFields[j][1]));
					params[j] = getCellStringValue(cell);
				}
	        }
	        batchSql.addBatch(insertSql, params);
		}
        
		int v_result = 0;	// 存储过程执行结果
        int exc = db.doInTransaction(batchSql);
        if (exc == 0) 
        {
            return exc;		// SQl语句执行失败
        } 
        else 
        {
            // 调用存储过程
            ProcHelper proc = db.getProcHelper(procName);
            proc.setVarcharParam("v_table_name");
            proc.setValue("v_table_name", tempTableName);
            
            for(int i = 0; i < procInParams.length; i++)
            {
                String[] procInParam = procInParams[i];
                proc.setVarcharParam(procInParam[0]);
                proc.setValue(procInParam[0], procInParam[1]);
            }
            proc.setVarcharOutParam("v_result");
            
            try
            {
                Map procMap = (Map) proc.execute();
                v_result = Integer.parseInt(StringHelper.get(procMap, "v_result"));
            }
            catch (Exception e) 
            {
                logger.error(e);
            }
            // 导入失败，导出结果excel
            logger.debug("存储过程执行结果v_result: " + v_result);
            /*********************************导入失败，导出结果excel**********************************************/
            if (v_result == -1) 
            {
                String dir = new RequestHelper().getWebRootRealPath() + "/"+dirPath+"/download/";
                HSSFWorkbook wbExport = new HSSFWorkbook();
                
                String sql = "";
                if(orderFlag)
                {
                	sql = "select t.* from " + tempTableName + " t order by t.show_order";
                }
                else
                {
                	sql = "select t.* from " + tempTableName + " t";
                }
                
                List<?> exp_list = db.queryForList(sql);
                wbExport = new HssfHelper().export(exp_list, excelParams, "importResult");
                
                String to_file_name = tempTableName + ".xls";	// 结果文件名称
                File outFile = new File(dir, to_file_name);
                try 
                {
                    FileOutputStream outStream = new FileOutputStream(outFile);
                    wbExport.write(outStream);
                    outStream.close();
                } 
                catch (Exception e) 
                {
                    logger.error(e);
                }
                request.setAttribute("to_file_name", to_file_name);
            }
            // 删除临时表
//            System.out.println("临时表：" + tempTableName);
            db.update("drop table " + tempTableName +" purge");
        }
        
        return v_result;
    }
    
    /**
     * 导入Excel文件数据到数据库表中
     * @param request
     * @param file	MultipartFile类型的文件
     * @param tempTable	    临时表名
     * @param paramsStr		参数数组
     * @param procName		存储过程名
     * @param procInParams	存储过程参数
     * @param startRow		从Excel第几行开始导入
     * @param orderFlag		顺序标志，如果为true，临时表中增加show_order字段，遍历excel时插入数据，输出结果文件时用于排序
     * @return 存储过程执行结果（不要与0、-1和-2重复）  0：SQL语句执行失败  -1：导入失败（Excel数据问题或执行存储过程出现异常）  -2：Excel读取错误！
     */
	public int importExcelToDB(HttpServletRequest request, MultipartFile file, String tempTable, 
            String[] paramsStr, String procName, String[][] procInParams, int startRow, 
            boolean orderFlag, DBHelperSpring db)
    {
		HSSFWorkbook wb = null;
		try 
		{
			wb = new HSSFWorkbook(file.getInputStream());
		} 
		catch (Exception ex) 
		{
			logger.debug("excel读取错误！");
			return -2;
		}
		
        BatchSql batchSql = new BatchSql();
        
        // 创建临时表的SQL语句
        String createSql = this.getCreateSql(tempTable, paramsStr, orderFlag);
        batchSql.addBatch(createSql);
        
        // 插入临时表的SQL语句
        String insertSql = this.getInsertSql(tempTable, paramsStr, orderFlag);
        
        // 将Excel导入数据库表中
        Iterator<Row> iter = wb.getSheetAt(0).rowIterator();
		for (int i = 0; iter.hasNext(); i++) 
		{
			HSSFRow row = (HSSFRow) iter.next();
			if (i < startRow) 
			{
				continue;
			}
			// 顺序标志为true，则需要给show_order插入参数
			List<Object> paramsList = new ArrayList<Object>();
			for(String param : paramsStr)
	        {
	        	String[] temp = param.split(",");
	        	HSSFCell cell = row.getCell(Integer.parseInt(temp[2]));
	        	paramsList.add(getCellStringValue(cell));
	        }
			if(orderFlag)
        	{
        		paramsList.add(i);
        	}
	        batchSql.addBatch(insertSql, paramsList.toArray());
		}
        
        int exc = db.doInTransaction(batchSql);
        if (exc == 0) 
        {
            return 0;		// SQl语句执行失败
        } 
        
        return this.callValidProcedure(procName, tempTable, procInParams, db);
    }
    
    /**
     * 导入Excel文件数据到数据库表中
     * @param request
     * @param file	MultipartFile类型的文件
     * @param tempTablePrefix	临时表名前缀
     * @param paramsStr		参数数组
     * @param procName		存储过程名
     * @param procInParams	存储过程参数
     * @param startRow		从Excel第几行开始导入
     * @param orderFlag		顺序标志，如果为true，临时表中增加show_order字段，遍历excel时插入数据，输出结果文件时用于排序
     * @return 1：导入并解析成功  0：SQL语句执行失败  -1：导入失败（Excel数据问题或执行存储过程出现异常） -2：Excel读取错误！
     */
	public int importExcelToDB(HttpServletRequest request, MultipartFile file, String tempTablePrefix, 
            String[] paramsStr, String procName, String[][] procInParams, int startRow, 
            boolean orderFlag, DBHelperSpring db, String downloadPath)
    {
		HSSFWorkbook wb = null;
		try 
		{
			wb = new HSSFWorkbook(file.getInputStream());
		} 
		catch (Exception ex) 
		{
			return -2;
		}
		
        BatchSql batchSql = new BatchSql();
        // 临时表名
        String tempTableName = tempTablePrefix + DateHelper.getToday("yyyyMMddHHmmss") + Math.round(Math.random() * 1000);
        
        // 创建临时表的SQL语句
        String createSql = this.getCreateSql(tempTableName, paramsStr, orderFlag);
        batchSql.addBatch(createSql);
        
        // 插入临时表的SQL语句
        String insertSql = this.getInsertSql(tempTableName, paramsStr, orderFlag);
        
        // 将Excel导入数据库表中
        Iterator<Row> iter = wb.getSheetAt(0).rowIterator();
		for (int i = 0; iter.hasNext(); i++) 
		{
			HSSFRow row = (HSSFRow) iter.next();
			if (i < startRow) 
			{
				continue;
			}
			// 顺序标志为true，则需要给show_order插入参数
			List<Object> paramsList = new ArrayList<Object>();
			for(String param : paramsStr)
	        {
	        	String[] temp = param.split(",");
	        	HSSFCell cell = row.getCell(Integer.parseInt(temp[2]));
	        	paramsList.add(getCellStringValue(cell));
	        }
			if(orderFlag)
        	{
        		paramsList.add(i);
        	}
//			logger.debug("插入数据：" + StringHelper.getSql(insertSql, paramsList.toArray()));
	        batchSql.addBatch(insertSql, paramsList.toArray());
		}
        
		int v_result = 0;	// 定义存储过程执行结果
        int exc = db.doInTransaction(batchSql);
        if (exc == 0) 
        {
            return 0;		// SQl语句执行失败
        } 
        else
        {
            // 调用存储过程
        	v_result = this.callValidProcedure(procName, tempTableName, procInParams, db);
        	String dir = "";
            // 导入失败，导出结果excel
            if (v_result == -1) 
            {
                dir = new RequestHelper().getWebRootRealPath() + File.separator + downloadPath;
//                System.out.println("导出路径：" + dir);
				String to_file_name = this.exportErrorExcel(dir, tempTableName, paramsStr, db, orderFlag);
				request.setAttribute("to_file_name", to_file_name);
            }
            // 删除临时表
//            System.out.println("临时表：" + tempTableName);
            db.update("drop table " + tempTableName +" purge");
        }
        
        return v_result;
    }
    
    /**
     * 创建临时表SQL 
     * @author 戴晓飞 2013-11-03
     * @param tempTableName
     * @param paramsStr
     * @return
     */
    public String getCreateSql(String tempTableName, String[] paramsStr, boolean orderFlag)
    {
    	String createSql = "create table " + tempTableName + "(";
        StringBuffer field = new StringBuffer("");	// 字段串
        for(String param : paramsStr)
        {
        	String[] temp = param.split(",");
        	field.append(temp[1] + " varchar(1000),");
        }
        // 拼接创建的SQL语句
        if(orderFlag)
        {
        	createSql = createSql + field.toString() + "show_order number)";
        }
        else
        {
        	createSql = createSql + field.deleteCharAt(field.length() - 1) + ") tablespace D_QD1_DATA_01 ";
        }
//        logger.debug("插入临时表的SQL语句：" + createSql);
        return createSql;
    }
    
    /**
     * 插入临时表的SQL语句
     * @author 戴晓飞 2013-11-03
     * @param tempTableName
     * @param paramsStr
     * @return
     */
    public String getInsertSql(String tempTableName, String[] paramsStr, boolean orderFlag)
    {
    	String insertSql = "";
        StringBuffer field = new StringBuffer("");	  // 字段串
        StringBuffer quesMark = new StringBuffer(""); // 占位符串
        for(String param : paramsStr)
        {
        	String[] temp = param.split(",");
        	field.append(temp[1] + ",");
        	quesMark.append("?,");
        }
        // 顺序标志为true，则字段串加show_order字段，占位符串增加一个占位符
        if(orderFlag)
        {
        	field.append("show_order");
        	quesMark.append("?");
        }
        else
        {
        	field.deleteCharAt(field.length() - 1); // 去掉字段串末尾的逗号
        	quesMark.deleteCharAt(quesMark.length() - 1); // 去掉占位符串末尾的逗号
        }
        // 拼接插入的SQL语句
        insertSql = "insert into " + tempTableName + "(" + field.toString() + ") values(" + quesMark.toString() + ")";
//        logger.debug("拼接插入的SQL语句：" + insertSql);
        return insertSql;
    }
    
    /**
     * 调用存储过程验证导入的数据
     * @author 戴晓飞 2013-11-03
     * @param procName		存储过程名
     * @param tempTableName 临时表名
     * @param procInParams	存储过程参数（必须与存储过程中的参数顺序一致），v_table_name和v_result分别为临时表名和返回结果，
     * 						在本方法中已经提供，procInParams中无需再加，存储过程中参数顺序分别为第一和最后。
     * @param db
     * @return 返回存储过程执行结果v_result
     */
    @SuppressWarnings("unchecked")
    public int callValidProcedure(String procName, String tempTableName, String[][] procInParams,
    		DBHelperSpring db)
    {
    	int v_result = -1;
    	ProcHelper proc = db.getProcHelper(procName);
        proc.setVarcharParam("v_table_name");		 // 临时表名
        proc.setValue("v_table_name", tempTableName);
        
        for(int i = 0; i < procInParams.length; i++)
        {
            String[] procInParam = procInParams[i];
            proc.setVarcharParam(procInParam[0]);
            proc.setValue(procInParam[0], procInParam[1]);
//            logger.debug(procInParam[0]+ " = " +procInParam[1]);
        }
        proc.setVarcharOutParam("v_result"); // 存储过程返回结果
        
        try
        {
            Map<String, Object> procMap = proc.execute();
            v_result = Integer.parseInt(StringHelper.get(procMap, "v_result"));
        }
        catch (Exception e) 
        {
            logger.error("存储过程执行异常：" + e);
        }
//        logger.debug("存储过程执行结果v_result: " + v_result);
        return v_result;
    }
    
    /**
     * 导入失败，导出excel结果文件
     * @author 戴晓飞 2013-11-03
     * @param dir 			导出路径
     * @param tempTableName 临时表名
     * @param paramsStr 	参数数组，另外，“导入失败原因”在本方法中固定为check_remark，在参数数组paramsStr中无需再加，且存储过程中需要向临时表中增加该字段。
     * @param db 
     * @param orderFlag 	顺序标识
     * @return 返回结果文件的文件名
     */
    public String exportErrorExcel(String dir, String tempTableName, String[] paramsStr, 
    			DBHelperSpring db, boolean orderFlag)
    {
    	// 定义结果文件Excel的标题
    	String[][] excelParams = new String[paramsStr.length + 1][2];
    	for(int i=0;i<paramsStr.length;i++)
        {
        	String[] temp = paramsStr[i].split(",");
        	excelParams[i] = new String[]{temp[0], temp[1]};
        }
    	excelParams[paramsStr.length] = new String[]{"导入失败原因", "check_remark"};
    	
    	HSSFWorkbook wbExport = new HSSFWorkbook();
        String sql = "select * from " + tempTableName;
        // 顺序标识为true，则按照show_order排序
        if(orderFlag)
        {
        	sql += " order by show_order";
        }
        
        List<?> expList = db.queryForList(sql);
        wbExport = this.export(expList, excelParams, "importResult");
        String to_file_name = tempTableName + ".xls";	// 结果文件名称
        File outFile = new File(dir, to_file_name);
        try 
        {
            FileOutputStream outStream = new FileOutputStream(outFile);
            wbExport.write(outStream);
            outStream.close();
        } 
        catch (Exception e) 
        {
            logger.error("导出结果文件异常：" + e);
        }
        return to_file_name;
    }
    
    /**
     * 调用存储过程验证导入的数据
     * @author 徐明响 2013-12-15
     * @param procName		存储过程名
     * @param procInParams	存储过程参数（必须与存储过程中的参数顺序一致,v_result为返回结果，
     * 						在本方法中已经提供，procInParams中无需再加，存储过程中为最后一个。
     * @param db
     * @return 返回存储过程执行结果v_result
     */
    @SuppressWarnings("unchecked")
    public int callValidProcedure(String procName, String[][] procInParams, DBHelperSpring db)
    {
    	int v_result = -1;
    	ProcHelper proc = db.getProcHelper(procName);
        for(int i = 0; i < procInParams.length; i++)
        {
            String[] procInParam = procInParams[i];
            proc.setVarcharParam(procInParam[0]);
            proc.setValue(procInParam[0], procInParam[1]);
        }
        proc.setVarcharOutParam("v_result"); // 存储过程返回结果
        
        try
        {
            Map<String, Object> procMap = proc.execute();
            v_result = Integer.parseInt(StringHelper.get(procMap, "v_result"));
        }
        catch (Exception e) 
        {
            logger.error("存储过程执行异常：" + e);
        }
//        logger.debug("存储过程执行结果v_result: " + v_result);
        return v_result;
    }
    
    /**
     * 将数据导出到Excel文件
     * @param sql  查询语句
     * @param excelParams  Excel表头参数
     * @param excelName    Excel名称
     * @param db
     * @param response
     * @param request
     */
    public void exportDataToExcel2(String sql, String[][] excelParams, String excelName, 
            DBHelperSpring db, HttpServletResponse response,HttpServletRequest request)
    {
        List<Map<String, Object>> expDatalist = db.queryForList(sql);
        //更新导出状态,标识为已导出
        new BaseService().updateExportLogStatus(db, request);
        
        try{
            // 打开文件
            response.reset();
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(excelName+".xls", "UTF-8"));
            response.setCharacterEncoding("UTF-8");
    
            WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = book.createSheet(excelName, 0);
            
            // 设置标题样式
            WritableFont font1 = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD);
            WritableCellFormat cellFormat1 = new WritableCellFormat(font1);
            cellFormat1.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN); // 设置边框
            cellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            cellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            jxl.write.WritableCellFormat wcf1 = new jxl.write.WritableCellFormat(cellFormat1);
            
            // 设置内容样式
            WritableFont font2 = new WritableFont(WritableFont.createFont("宋体"), 10);
            WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
            cellFormat2.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
            cellFormat2.setAlignment(jxl.format.Alignment.CENTRE); 
            jxl.write.WritableCellFormat wcf2 = new jxl.write.WritableCellFormat(cellFormat2);
            
            // 设置首行的高度
            sheet.setRowView(0, 600);
            
            // 设置列名及列宽
            for(int i=0;i<excelParams.length;i++)
            {
                sheet.setColumnView(i, Integer.parseInt(excelParams[i][2]));
                sheet.addCell(new Label(i, 0, excelParams[i][0], wcf1));
            }
            
            // 写入数据
            for (int i = 0; i < expDatalist.size(); i++)
            {
               Map<String, Object> map = expDatalist.get(i);
               if(map != null)
               {
                   for(int j=0;j<excelParams.length;j++)
                   {
                       sheet.addCell(new Label(j, i + 1, StringHelper.get(map, excelParams[j][1]), wcf2));
                   }
               }
            }
            
            book.write();
            book.close();
        }
        catch(Exception e)
        {
            logger.debug("导出数据异常！");
        }
    }
    
    /**
     * 将数据导出到Excel文件
     * @param sql  查询语句
     * @param excelParams  Excel表头参数
     * @param excelName    Excel名称
     * @param db
     * @param response
     */
    public void exportDataToExcel(String sql, String[][] excelParams, String excelName, 
    		DBHelperSpring db, HttpServletResponse response)
    {
        List<Map<String, Object>> expDatalist = db.queryForList(sql);
        
        
        try{
            // 打开文件
            response.reset();
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(excelName+".xls", "UTF-8"));
            response.setCharacterEncoding("UTF-8");
    
            WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = book.createSheet(excelName, 0);
            
            // 设置标题样式
            WritableFont font1 = new WritableFont(WritableFont.createFont("微软雅黑"), 10, WritableFont.BOLD);
            WritableCellFormat cellFormat1 = new WritableCellFormat(font1);
            cellFormat1.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN); // 设置边框
            cellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            cellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            jxl.write.WritableCellFormat wcf1 = new jxl.write.WritableCellFormat(cellFormat1);
            
            // 设置内容样式
            WritableFont font2 = new WritableFont(WritableFont.createFont("宋体"), 10);
            WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
            cellFormat2.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
            cellFormat2.setAlignment(jxl.format.Alignment.CENTRE); 
            jxl.write.WritableCellFormat wcf2 = new jxl.write.WritableCellFormat(cellFormat2);
            
            // 设置首行的高度
            sheet.setRowView(0, 600);
            
            // 设置列名及列宽
            for(int i=0;i<excelParams.length;i++)
            {
                sheet.setColumnView(i, Integer.parseInt(excelParams[i][2]));
                sheet.addCell(new Label(i, 0, excelParams[i][0], wcf1));
            }
            
            // 写入数据
            for (int i = 0; i < expDatalist.size(); i++)
            {
               Map<String, Object> map = expDatalist.get(i);
               if(map != null)
               {
                   for(int j=0;j<excelParams.length;j++)
                   {
                       sheet.addCell(new Label(j, i + 1, StringHelper.get(map, excelParams[j][1]), wcf2));
                   }
               }
            }
            
            book.write();
            book.close();
        }
        catch(Exception e)
        {
            logger.debug("导出数据异常！");
        }
    }
    
    /** 
     * @description 导出复杂类型的数据成Excel文件
     * @author xuzh 2015-1-20
     * @param request
     * @param response
     * @param db 数据库操作类
     * @param dataList 导出的数据信息
     * @param headers 导出excel列头信息
     * @param excelName 下载文件名称
     */ 
    public void exportDataToExcelHssf(HttpServletRequest request, HttpServletResponse response,
            DBHelperSpring db, List<?> dataList, String[][] headers, String excelName) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.reset();
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(excelName+".xls", "UTF-8"));
            response.setCharacterEncoding("UTF-8");
            
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet("sheet");
            
            // 设置表格默认列宽度为16个字节
            sheet.setDefaultColumnWidth(16);
            
            // 生成一个样式
            HSSFCellStyle style = workbook.createCellStyle();
            
            // 设置这些样式
            style.setFillForegroundColor(HSSFColor.YELLOW.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            
            // 把字体应用到当前的样式
            style.setFont(font);
            
            // 生成并设置另一个样式
            HSSFCellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(HSSFColor.WHITE.index);
            style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            
            // 生成另一个字体
            HSSFFont font2 = workbook.createFont();
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            
            // 把字体应用到当前的样式
            style2.setFont(font2);
            
            // 声明一个画图的顶级管理器
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            
            // 产生表格标题行
            HSSFRow row = sheet.createRow(0);
            row.setHeight((short)400);
            HSSFCell cell = null;
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(headers[i][0]);
            }
            
            // 遍历集合数据，产生数据行
            for (int i = 0; i < dataList.size(); i++) {
                row = sheet.createRow((short) i + 1);
                Map dataMap = (Map) dataList.get(i);
                BufferedImage bufferImg = null;
                String dataValue = "";
                for (int j = 0; j < headers.length; j++) {
                    try {
                        cell = row.createCell(j);
                        cell.setCellStyle(style2);
                        dataValue = StringHelper.get(dataMap, headers[j][1]);
                        if("1".equals(headers[j][2])){// 图片格式的内容
                            if(!"".equals(dataValue)) {// 图片格式的内容不为空
                                String imgType = dataValue.substring(dataValue.indexOf(".") + 1);   // 图片类型：jpg、png
                                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                                bufferImg = ImageIO.read(new File(Constants.ATTACHMENT_ROOT + dataValue));
                                ImageIO.write(bufferImg, imgType, byteArrayOut);
                                row.setHeight((short) 2024);
                                sheet.setColumnWidth((short)j, 4196);
                                HSSFClientAnchor anchor = new HSSFClientAnchor(1, 1, 1023, 255, 
                                        (short)j, (short) i + 1, (short)j, (short) i + 1);
                                anchor.setAnchorType(1);
                                patriarch.createPicture(anchor, workbook.addPicture(
                                        byteArrayOut.toByteArray(), this.getPictureType(imgType)));
                            } else {
                                cell.setCellValue(dataValue);
                            }
                        } else {// 其他格式的内容
                            cell.setCellValue(dataValue);
                        }
                    } catch (Exception e) {
                        cell.setCellValue("");// 出现异常，则单元格设置成空
                        e.printStackTrace();
                    }
                }
            }
            
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("导出复杂类型的数据成Excel文件出现异常！" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // 更新导出状态,标识为已导出
            new BaseService().updateExportLogStatus(db, request);
        }
    }
    
    /** 
     * @description 获取图片类型对应的数值
     * @author xuzh 2015-1-20
     * @param imgType
     * @return
     */ 
    public int getPictureType(String imgType){
        int picType = 0;
        if("jpg".equalsIgnoreCase(imgType)){
            picType = HSSFWorkbook.PICTURE_TYPE_JPEG;
        } else if ("png".equalsIgnoreCase(imgType)){
            picType = HSSFWorkbook.PICTURE_TYPE_PNG;
        } else if ("dib".equalsIgnoreCase(imgType)){
            picType = HSSFWorkbook.PICTURE_TYPE_DIB;
        } else {
            picType = HSSFWorkbook.PICTURE_TYPE_JPEG;
        }
        return picType;
    }
    
}
