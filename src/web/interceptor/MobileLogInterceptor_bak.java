package web.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import moi.service.log.MobileLogService;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import web.bean.SystemInfo;
import web.model.User;

import com.sgy.util.common.StringHelper;

/**
 * 
 * @Title: MobileLogInterceptor.java
 * @Description: 手机端日志拦截器
 * @author zhang
 * @date 2019
 */
public class MobileLogInterceptor_bak extends HandlerInterceptorAdapter {
	private SystemInfo systemInfo;
	private MobileLogService mobileLogService;
	private String mappingURL = "";   

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String url = request.getRequestURL().toString(); // 请求的url
		
		if (!"".equals(mappingURL) && StringHelper.isContains(url, mappingURL.split(","))) {
			return true;
		}
		
		if(url.indexOf("/mobile/")>-1){//手机端请求
			User user = (User)request.getSession().getAttribute("user");
        	try {
        		paramMap.put("operator_id", user.getOperatorId());
				paramMap.put("mobile", user.getMobile());
				paramMap.put("org_id", user.getOrgId());
				paramMap.put("org_name", user.getOrgName());
				paramMap.put("operate_url", url);
            }catch(Exception e){
				paramMap.put("operator_id", StringHelper.notEmpty(request.getParameter("userName")));
				paramMap.put("operate_url", url);
			}
			
			mobileLogService.createMobileLog(request, paramMap);
		}
		return super.preHandle(request, response, handler);
	}

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public MobileLogService getMobileLogService() {
		return mobileLogService;
	}

	public void setMobileLogService(MobileLogService mobileLogService) {
		this.mobileLogService = mobileLogService;
	}

	public String getMappingURL() {
		return mappingURL;
	}

	public void setMappingURL(String mappingURL) {
		this.mappingURL = mappingURL;
	}
}