package baobeisys.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.common.StringHelper;

/**
 * 日志查询功能
 * @date 2018-12-25
 */
@Service("logService")
public class LogService extends BaseService {
    
    /*****************************菜单访问流水************************************/
    /**
     * 菜单访问流水列表
     * @param request
     * @return
     */
    public List<?> getNodeHisList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String mobile = req.getValue(request, "mobile");//手机号码
        String operatorId = req.getValue(request, "operator_id");//人员编号
        String name = req.getValue(request, "name");//人员姓名
        String beginDate = req.getValue(request, "begin_date");//开始时间
        String endDate = req.getValue(request, "end_date");//结束时间
        String orgName = req.getValue(request, "org_name");//组织名称
        String moduleName = req.getValue(request, "module_name");//菜单名称
        
        String sql = "select a.OPERATOR_ID,  " +
	        		"	DATE_FORMAT(a.OPERATE_TIME, \"%Y-%m-%d %H:%i:%s\") OPERATE_TIME, MODULE_NAME, " +
	        		"	a.MODULE_NAME, fn_getorgname(a.ORG_ID) org_name, " +
	        		"	b.name, b.MOBILE " +
	        		"from t_operating_log a, t_user b " +
	        		"where a.operator_id = b.operator_id " +
	        		"and a.OP_TYPE_ID = 'menu'";

        if(!"".equals(operatorId)) {
            sql += " and a.operator_id = ?";
            paramsList.add(operatorId);
        }
        
        if(!"".equals(mobile)) {
            sql += " and b.mobile = ?";
            paramsList.add(mobile);
        }
        
        if(!"".equals(name)) {
            sql += " and b.name like  ? ";
            paramsList.add("%" + name + "%");
        }
        
        if(!"".equals(moduleName)) {
            sql += " and a.module_name like  ? ";
            paramsList.add("%" + moduleName + "%");
        }

        if(!"".equals(orgName)) {
            sql += " and fn_getorgname(a.ORG_ID) like  ? ";
            paramsList.add("%" + orgName + "%");
        }

        if(!"".equals(beginDate)) {
            sql += " and DATE_FORMAT(a.operate_time, \"%Y-%m-%d\") >= ? ";
            paramsList.add(beginDate);
        }

        if(!"".equals(endDate)) {
            sql += " and DATE_FORMAT(a.operate_time, \"%Y-%m-%d\") <= ? ";
            paramsList.add(endDate);
        }

        sql += " order by a.operate_time desc ";
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    /*****************************系统登录流水************************************/
     
    /**
     * 获取系统登录流水Sql
     * @param request
     * @return
     */
    public String getLoginHisSql(HttpServletRequest request){
        String beginDate = req.getValue(request, "begin_date"); // 开始时间
        String endDate = req.getValue(request, "end_date"); // 结束时间
        String operatorId = req.getValue(request, "operator_id");// 人员工号
        String mobile = req.getValue(request, "mobile");// 手机号码
        String name = req.getValue(request, "name"); // 姓名
        String orgId = req.getValue(request, "org_id");// 组织编号
        String orgName = req.getValue(request, "org_name"); // 组织名称
        List<String> paramsList = new ArrayList<String>();
        String sql = "";
        sql = "select DATE_FORMAT(a.operate_time,\"%Y-%m-%d %H:%i:%s\") operate_time, op_type_name, " +
        		"	a.CLIENT_ADDRESS, a.OPERATOR_ID, a.org_id, fn_getorgname(a.org_id) org_name, b.name, b.MOBILE " +
        		"from t_operating_log a, t_user b " +
        		"where a.operator_id = b.operator_id " +
        		"and LOWER(a.OP_TYPE_ID) = 'login'";//or lower(a.OP_TYPE_ID) = 'clientlogin' 

        if(!"".equals(name)) {
            sql += " and b.name like ? ";
            paramsList.add("%" + name + "%");
        }

        if(!"".equals(orgName)) {
            sql += " and fn_getorgname(a.org_id) like ? ";
            paramsList.add("%" + orgName + "%");
        }

        if(!"".equals(operatorId)) {
            sql += " and b.operator_id= ? ";
            paramsList.add(operatorId);
        }

        if(!"".equals(mobile)) {
            sql += " and b.mobile= ? ";
            paramsList.add(mobile);
        }

        if(!"".equals(beginDate)) {
            sql += " and DATE_FORMAT(a.operate_time,\"%Y-%m-%d\") >= ? ";
            paramsList.add(beginDate);
        }

        if(!"".equals(endDate)) {
            sql += " and DATE_FORMAT(a.operate_time,\"%Y-%m-%d\") <= ? ";
            paramsList.add(endDate);
        }

        if(!"".equals(orgId)) {
            sql += " and a.org_id= ? ";
            paramsList.add(orgId);
        }
        
        sql += " order by a.operate_time desc ";
        logger.debug("====="+str.getSql(sql, paramsList));
        return str.getSql(sql, paramsList);
    }
	
    /**
     * 系统登录流水
     * @param request
     * @return
     */
    public List<Map<String, Object>> getLoginHisList(HttpServletRequest request) {
        String sql = this.getLoginHisSql(request);
        return db.getForList(sql, req.getPageSize(request, "pageSize"), request);
    }
    
    
    /**
     * 移动端登录流水
     * @param request
     * @return
     */
    public List<Map<String, Object>> getMobileLoginHisList(HttpServletRequest request) {
        String sql = this.getMobileLoginHisSql(request);
        return db.getForList(sql, req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取移动端登录流水
     * @param request
     * @return
     */
    public String getMobileLoginHisSql(HttpServletRequest request){
        String beginDate = req.getValue(request, "begin_date"); // 开始时间
        String endDate = req.getValue(request, "end_date"); // 结束时间
        String operatorId = req.getValue(request, "operator_id");// 人员工号
        String mobile = req.getValue(request, "mobile");// 手机号码
        String name = req.getValue(request, "name"); // 姓名
        String orgId = req.getValue(request, "org_id");// 组织编号
        String orgName = req.getValue(request, "org_name"); // 组织名称
        List<String> paramsList = new ArrayList<String>();
        String sql = "";
        sql = "select DATE_FORMAT(a.operate_time,\"%Y-%m-%d %H:%i:%s\") operate_time, op_type_name, " +
        		"	a.CLIENT_ADDRESS, a.OPERATOR_ID, a.org_id, fn_getorgname(a.org_id) org_name, b.name, b.MOBILE " +
        		"from t_operating_log a, t_user b " +
        		"where a.operator_id = b.operator_id " +
        		"and lower(a.OP_TYPE_ID) = 'clientlogin'  ";

        if(!"".equals(name)) {
            sql += " and b.name like ? ";
            paramsList.add("%" + name + "%");
        }

        if(!"".equals(orgName)) {
            sql += " and fn_getorgname(a.org_id) like ? ";
            paramsList.add("%" + orgName + "%");
        }

        if(!"".equals(operatorId)) {
            sql += " and b.operator_id= ? ";
            paramsList.add(operatorId);
        }

        if(!"".equals(mobile)) {
            sql += " and b.mobile= ? ";
            paramsList.add(mobile);
        }

        if(!"".equals(beginDate)) {
            sql += " and DATE_FORMAT(a.operate_time,\"%Y-%m-%d\") >= ? ";
            paramsList.add(beginDate);
        }

        if(!"".equals(endDate)) {
            sql += " and DATE_FORMAT(a.operate_time,\"%Y-%m-%d\") <= ? ";
            paramsList.add(endDate);
        }

        if(!"".equals(orgId)) {
            sql += " and a.org_id= ? ";
            paramsList.add(orgId);
        }
        
        sql += " order by a.operate_time desc ";
        logger.debug("====="+str.getSql(sql, paramsList));
        return str.getSql(sql, paramsList);
    }
    
    /**
     * 导出登录流水
     * 后台导出
     * @param request
     * @param response
     */
    public void exportLoginHis(HttpServletRequest request, HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        String sql = this.getLoginHisSql(request);
        List<Map<String, Object>> list = db.queryForList(sql);

        String[] columns = new String[] {"org_name", "operator_id", "name", "mobile", "operate_time",
                "client_address", "op_type_name"};
        String[] columntitles = new String[] {"组织机构", "人员编号", "姓名", "手机号码", "登录时间", "登录IP", "登录类型"};
        String excelName = "登录流水表";
        String sheetName = "登录流水";

        //更新导出状态
        this.updateExportLogStatus(db, request);

        try {
            // 打开文件
            response.reset();
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename=" +
                    java.net.URLEncoder.encode(excelName + ".xls", "UTF-8"));
            response.setCharacterEncoding("UTF-8");

            WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = book.createSheet(sheetName, 0);

            WritableFont font1 = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD);
            WritableCellFormat cellFormat1 = new WritableCellFormat(font1);
            cellFormat1.setWrap(true); // 设置自动换行
            cellFormat1.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN); // 设置边框
            cellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
            cellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            jxl.write.WritableCellFormat wcf1 = new jxl.write.WritableCellFormat(cellFormat1);

            WritableFont font2 = new WritableFont(WritableFont.createFont("宋体"), 10);
            WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
            cellFormat2.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
            cellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
            jxl.write.WritableCellFormat wcf2 = new jxl.write.WritableCellFormat(cellFormat2);

            // 设置行的高度
            sheet.setRowView(0, 600);

            // 设置列的宽度
            for(int i = 0; i < columntitles.length; i++) {
                sheet.setColumnView(i, 15);
            }
            sheet.setColumnView(0, 10);
            sheet.setColumnView(1, 35);
            sheet.setColumnView(5, 20);
            sheet.setColumnView(6, 25);
            sheet.setColumnView(7, 20);

            // 设置列名
            for(int i = 0; i < columntitles.length; i++) {
                sheet.addCell(new Label(i, 0, columntitles[i], wcf1));
            }
            // 写入数据
            for(int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                if(map != null) {
                    for(int j = 0; j < columns.length; j++) {
                        sheet.addCell(new Label(j, i + 1, StringHelper.get(map, columns[j]), wcf2));
                    }
                }
            }
            book.write();
            book.close();
        }
        catch (Exception e) {
            logger.debug("导出登录流水异常！");
        }
    }
    
    /*****************************信息下载流水************************************/
    /**
     * 信息下载流水列表
     * @param request
     * @return
     */
    public List<?> getDownloadHisList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String mobile = req.getValue(request, "mobile");//手机号码
        String operatorId = req.getValue(request, "operator_id");//人员编号
        String name = req.getValue(request, "name");//人员姓名
        String beginDate = req.getValue(request, "begin_date");//开始时间
        String endDate = req.getValue(request, "end_date");//结束时间
        String orgName = req.getValue(request, "org_name");//组织名称
        String moduleName = req.getValue(request, "module_name");//菜单名称
        
        String sql = "select  " +
	        		"	a.operator_id, a.ip_address, DATE_FORMAT(a.begin_date,\"%Y-%m-%d %H:%i:%s\") begin_date, a.remark,  " +
	        		"	b.name, b.ORG_ID, fn_getorgname(b.ORG_ID) org_name, b.mobile " +
	        		"from t_info_export_log a, t_user b " +
	        		"where a.operator_id = b.operator_id ";

        if(!"".equals(operatorId)) {
            sql += " and a.operator_id = ?";
            paramsList.add(operatorId);
        }
        
        if(!"".equals(mobile)) {
            sql += " and b.mobile = ?";
            paramsList.add(mobile);
        }
        
        if(!"".equals(name)) {
            sql += " and b.name like  ? ";
            paramsList.add("%" + name + "%");
        }
        
        if(!"".equals(orgName)) {
            sql += " and fn_getorgname(b.ORG_ID) like  ? ";
            paramsList.add("%" + orgName + "%");
        }

        if(!"".equals(beginDate)) {
            sql += " and DATE_FORMAT(a.begin_date, \"%Y-%m-%d\") >= ? ";
            paramsList.add(beginDate);
        }

        if(!"".equals(endDate)) {
            sql += " and DATE_FORMAT(a.begin_date, \"%Y-%m-%d\") <= ? ";
            paramsList.add(endDate);
        }

        sql += " order by a.begin_date desc ";
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }

}
