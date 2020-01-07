package moi.service.log;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.sgy.util.db.DBHelperSpring;

/**
 * 手机日志组件
 * @author zhang
 * @createDate 2019-01-15
 */
public class MobileLogService {
    private DBHelperSpring db;
    
    private static ExecutorService executor = Executors.newCachedThreadPool();
    
    /**
     * 创建系统日志
     * @param request
     * @param map
     */
    public void createMobileLog(HttpServletRequest request, Map<String, Object> map) {
        //设置request内容
        String operate_content = this.getRequestCommonInfo(request);
        if(request.getSession() != null) {
        	//设置session中的信息
        	map.put("session_id", request.getSession().getId());
    		map.put("imei", request.getSession().getAttribute("imei"));
    		map.put("imsi", request.getSession().getAttribute("imsi"));
    		map.put("mobile_model", request.getSession().getAttribute("mobile_model"));
    		map.put("client_version", request.getSession().getAttribute("client_version"));
    		map.put("os_version", request.getSession().getAttribute("os_version"));
    	}
        map.put("operate_content", operate_content);
        //记录日志
        MobileLogToDBTask mmarketLogToDBTask = new MobileLogToDBTask(db, map);
        
        executor.execute(mmarketLogToDBTask);
    }
    
    /**
     * 解析request
     * 组装操作内容operateContent，限定格式: name=value 如有多个，则以“,”分隔
     * @param request
     * @return
     */
    public String getRequestCommonInfo(HttpServletRequest request) {
        String operateContent = "";
        Enumeration<?> enu = request.getParameterNames();
        String name = "";
        String value = "";
        while(enu.hasMoreElements()) {
            name = (String)enu.nextElement();
            value = request.getParameter(name);
            operateContent += name + "=" + value + ","; 
            
            if(name.equals("mobileModel")){
            	request.getSession().setAttribute("mobile_model", value);
            }
            if(name.equals("os_version")){
            	request.getSession().setAttribute("os_version", value);
            }
            if(name.equals("versionCode")){
            	request.getSession().setAttribute("client_version", value);
            }
            if(name.equals("imsi")){
            	request.getSession().setAttribute("imsi", value);
            }
            if(name.equals("imei")){
            	request.getSession().setAttribute("imei", value);
            }
        }
        
        if(operateContent.length() > 4000) {
            return operateContent.substring(0, 3996) + "...";
        }
        
        return operateContent;
    }
    
    public DBHelperSpring getDb() {
        return db;
    }
    
    public void setDb(DBHelperSpring db) {
        this.db = db;
    }
}