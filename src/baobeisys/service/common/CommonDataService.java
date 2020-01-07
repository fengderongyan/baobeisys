package baobeisys.service.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.net.IpHelper;

/**
 * 系统公共类
 * @date 2016-2-22
 */
@Service("commonDataService")
public class CommonDataService extends BaseService {
    
    /**
     * 记录信息导出日志
     * @param request
     * @return
     */
    public int recordInfoExportLog(HttpServletRequest request) {
        String exportType = req.getValue(request, "export_type");//导出类型：1 displaytag导出   2 自定义导出
        String operatorId = this.getOperatorId(request);//操作人工号
        String moduleId = req.getValue(request, "module_id"); // 模块编号
        String requestUrl = req.getValue(request, "request_url"); // 请求的URL地址
        String condition = req.getValue(request, "condition");//查询条件
        String remark = req.getValue(request, "remark");//备注
        String status = "2".equals(exportType) ? "0" : "1";//导出状态  如果为自定义导出默认插入 0 否则默认插入 1
        String ip_address = IpHelper.getIpAddr(request);//IP地址
        String logId = db.getMysqlNextSequenceValue("t_info_export_log_sid");//唯一编号
        int result = -1;
        
        String sql = "";
        sql = " insert into t_info_export_log(log_id, operator_id, export_type, begin_date,"  + 
              " status, module_id, request_url, conditions, remark,ip_address)"  + 
              " values(?,?,?,sysdate(),?,?,?,?,?,?)";
        result = db.update(sql, new Object[] {logId, operatorId, exportType, status, moduleId, requestUrl, condition,
                remark, ip_address});
        if(result == 1) {//如果插入成功，则返回日志编号
            return Integer.parseInt(logId);
        }
        return result;
    }

    /**
     * 更新导出状态
     * @param request
     * @return
     */
    public int updateExportLogStatus(DBHelperSpring db,HttpServletRequest request){
        String log_id = req.getValue(request, "log_id");//记录唯一编号
        String sql = "update t_info_export_log a set a.status=1 ,end_date = sysdate() where a.log_id =?";
        return db.update(sql, new Object[]{log_id});
    }
    
    /**
     * 获取导出日志标识
     * @param request
     * @return
     */
    public String getExportLogStatus(HttpServletRequest request){
        String log_id = req.getValue(request, "log_id");//记录唯一编号
        String sql = "select count(1) from t_info_export_log a where a.status=1 and a.log_id=?  ";
        return db.queryForString(sql, new Object[]{log_id});
    }
}
