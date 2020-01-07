package web.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import web.bean.SystemInfo;
import web.model.User;
import web.service.systemlog.SystemLogService;

import com.sgy.util.common.StringHelper;

/**
 * 系统日志拦截器
 * @author 
 * 
 */
public class LogInterceptor extends HandlerInterceptorAdapter
{
    private SystemInfo systemInfo;
    private SystemLogService systemLogService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception
    {
        String module_id = "";
        String module_name = "";
        String permit_code = "";
        String permit_name = "";
        String operate_Level = "";
        
        String url = request.getRequestURL().toString(); // 请求url
        
        if(url.indexOf("/mobile/") == -1){//非手机端请求
        	Map<String, Object> paramMap = new HashMap<String, Object>();
            User user = (User)request.getSession().getAttribute("user");
            List<Map<String, Object>> permitList = systemInfo.getRolePermitList();
            for(Map<String, Object> map : permitList) {
                if(url.endsWith(StringHelper.get(map, "permit_url"))) {
                    module_id = StringHelper.get(map, "module_id");
                    module_name = StringHelper.get(map, "module_name");
                    permit_code = StringHelper.get(map, "permit_code");
                    permit_name = StringHelper.get(map, "permit_name");
                    operate_Level = StringHelper.get(map, "operate_Level");
                    break;
                }
            }
            
            // 如果url没有配置在t_permit中，或者操作级别为0，则不会记录操作日志
            if("".equals(operate_Level) || "0".equals(operate_Level)) {
                return true;
            } else {
                paramMap.put("operator_id", user.getOperatorId());
                paramMap.put("op_type_id", permit_code);
                paramMap.put("op_type_name", permit_name);
                paramMap.put("op_level_id", operate_Level);
                paramMap.put("operate_result", "0");
                paramMap.put("module_id", module_id);
                paramMap.put("module_name", module_name);
                paramMap.put("org_id", user.getOrgId());
                // 如果操作级别不为1（一般），则必须记录操作内容
                if(!"1".equals(operate_Level)) {
                    paramMap.put("operate_content", systemLogService.getRequestCommonInfo(request));
                }
                systemLogService.createSystemLog(request, paramMap);
            }
        }
        
        return super.preHandle(request, response, handler);
    }

    public SystemInfo getSystemInfo()
    {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo)
    {
        this.systemInfo = systemInfo;
    }

    public SystemLogService getSystemLogService()
    {
        return systemLogService;
    }

    public void setSystemLogService(SystemLogService systemLogService)
    {
        this.systemLogService = systemLogService;
    }

}