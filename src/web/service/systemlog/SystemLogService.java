package web.service.systemlog;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.net.IpHelper;

/**
 * 系统日志组件
 * @author 戴晓飞
 *
 */
public class SystemLogService 
{
    private DBHelperSpring db;
    
    private static ExecutorService executor = Executors.newCachedThreadPool();
    
    /**
     * 创建系统日志
     * @param request
     * @param map
     * map中需要传递的字段：
     * op_type_id（操作类型编码，审计平台定义的操作类型），op_type_name（操作类型名称，所进行的具体操作名称）
     * op_level_id（操作级别，5：严重、4：警告、3：敏感、2：重要、1：一般），operate_content（操作内容）
     * operate_result（操作结果，0：成功 1：失败 ），module_id（模块id，应用操作的菜单编号）
     * module_name（模块名称，应用操作的菜单名称），task_code（操作对应的工单号，流程操作需要传进）
     */
    public void createSystemLog(HttpServletRequest request, Map<String, Object> map)
    {
    	
    	map.put("client_name", request.getRemoteHost());
        map.put("client_address", IpHelper.getIpAddr(request));
        map.put("client_port", request.getRemotePort());
        
        if(request.getSession() != null)
    	{
    		map.put("session_id", request.getSession().getId());
    		map.put("server_address", request.getSession().getServletContext().getAttribute("server_ip"));
    		map.put("server_port", request.getSession().getServletContext().getAttribute("server_port"));
    		map.put("server_mac", request.getSession().getServletContext().getAttribute("server_mac"));
    	}
        
        LogToDBTask logToDBTask = new LogToDBTask(db, map);
        
        executor.execute(logToDBTask);
    }
    
    /**
     * 解析request
     * 组装操作内容operateContent，限定格式: name=value 如有多个，则以“,”分隔
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public String getRequestCommonInfo(HttpServletRequest request)
    {
        String operateContent = "";
        Enumeration enu = request.getParameterNames();
        
        String name = "";
        String value = "";
        while(enu.hasMoreElements())
        {
            name = (String)enu.nextElement();
            value = request.getParameter(name);
            operateContent += name + "=" + value + ","; 
        }
        
        if(operateContent.length() > 4000)
        {
            return operateContent.substring(0, 3996) + "...";
        }
        
        return operateContent;
    }
    
    public DBHelperSpring getDb()
    {
        return db;
    }
    
    public void setDb(DBHelperSpring db)
    {
        this.db = db;
    }
}

