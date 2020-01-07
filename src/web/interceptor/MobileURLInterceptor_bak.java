package web.interceptor;

import baobeisys.service.login.LoginService;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import web.model.User;

import com.sgy.util.Constants;
import com.sgy.util.base64.DES;
import com.sgy.util.common.StringHelper;
import com.sgy.util.spring.SpringUtil;

/**
 * URL拦截，判断当前用户是否认证，如果没有认证则跳转到登录页面
 * @author 陈静
 * @createDate 12 18, 2013
 * @description
 */
public class MobileURLInterceptor_bak extends URLInterceptor {
	//日志记录
	protected Logger logger = Logger.getLogger(getClass());
	private static Map<String,String> operIdMd5Map = new HashMap<String, String>();
	//不需要拦截的URL
	private String mappingURL = "";   
	//DES加密解密key
	private String key = Constants.APP_HTTP_KEY;
	
    public void setMappingURL(String mappingURL) {    
    	this.mappingURL = mappingURL;    
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURL().toString();
		if (!"".equals(mappingURL) && StringHelper.isContains(url, mappingURL.split(","))) {
			return true;
		}
		
		if (url.indexOf("/mobile/") > -1) {
			logger.debug("mobile request url is ："+url);
			//获取COOLIE
			Cookie[] cookie = request.getCookies();
			String operator_key = "";
			String org_id = "";
			String mobile = "";
			String clientType = "";//登录终端类型 mobile_type:手机
			String imei=""; //手机IMEI
			String imsi=""; //手机IMSI
			String mobileModel=""; //手机类型名称，如HTC、XIAOMI等
			String versionCode=""; //应用程序版本号 如11
			String versionName=""; //应用程序版本名称 如1.1
			String versionAndroid=""; //手机的ANDROID系统版本
			String session_id="";
			String httpServerPort = "";
			for(int i = 0;i< cookie.length;i++){
				if(key.equals(cookie[i].getName())){
					operator_key = cookie[i].getValue();
				}
				if("org_id".equals(cookie[i].getName())){
					org_id = cookie[i].getValue();
				}
				if("mobile".equals(cookie[i].getName())){
					mobile = cookie[i].getValue();
				}
				if("client_type".equals(cookie[i].getName())){
					clientType = cookie[i].getValue();
				}
				if("mobileModel".equals(cookie[i].getName())){
					mobileModel = cookie[i].getValue();
				}
				if("imsi".equals(cookie[i].getName())){
					imsi = cookie[i].getValue();
				}
				if("version_code".equals(cookie[i].getName())){
					versionCode = cookie[i].getValue();
				}
				if("versionName".equals(cookie[i].getName())){
					versionName = cookie[i].getValue();
				}
				if("versionAndroid".equals(cookie[i].getName())){
					versionAndroid = cookie[i].getValue();
				}
				if("imei".equals(cookie[i].getName())){
					imei = cookie[i].getValue();
				}
				if(Constants.COOKIE_SESSION_ID.equals(cookie[i].getName())){
					session_id = cookie[i].getValue();
				}
				if("httpServerPort".equals(cookie[i].getName())){
					httpServerPort = cookie[i].getValue();
				}
			}
//			String decryptOperator_key = DES.decryptDES(URLDecoder.decode(operator_key), key);
			String decryptOperator_key = operator_key;
			logger.debug("operator_key="+operator_key+",decryptOperator_key:"+decryptOperator_key);
			
			//获取的信息时以“|”分隔
			String operator_md5 =decryptOperator_key;
			/*String operator_time = StringHelper.Right(decryptOperator_key, decryptOperator_key.indexOf("|"));;
			logger.debug("operator_md5="+operator_md5+",operator_time="+operator_time);*/
			if("".equals(operator_key)) {//如果为空，不是手机客户端访问 
				return super.preHandle(request, response, handler);
			}else{
				Subject currentUser = SecurityUtils.getSubject();
				Session session = currentUser.getSession();
				//将部分信息放到SESSION中
				session.setAttribute("org_id", org_id);
				session.setAttribute("mobile", mobile);
				session.setAttribute("client_type", clientType);
				session.setAttribute("mobile_model", mobileModel);
				session.setAttribute("imsi", imsi);
				session.setAttribute("os_version", versionAndroid);
				session.setAttribute("imei", imei);
				session.setAttribute("session_id", session_id);
				session.setAttribute("client_version", versionCode);
				session.setAttribute("httpServerPort", httpServerPort);
				
				String operator_id = StringHelper.get(operIdMd5Map, operator_md5);
				if(operator_id.equals("")){
					operator_id = getOperIdByMd5(operator_md5);
					operIdMd5Map.put(operator_md5, operator_id);
				}
//				logger.debug("operator_id:"+operator_id);
				User user = (User)session.getAttribute("user");
				if(null == user) {
//					logger.debug("no session:"+operator_id);
					this.initUser(operator_id, request);
				} else {
					String cur_operator_id = user.getOperatorId();
					if(!cur_operator_id.equals(operator_id)) {
//					    logger.debug("工号不一致进行覆盖:"+operator_id);
					    this.initUser(operator_id, request);
					}
				}
			}
			response.addHeader("P3P", "CP=CAO PSA OUR");
		}
		
		return true;
	}
	
	/** 
	 * 系统初始化用户信息
	 * @param operator_id
	 * @param request
	 */ 
	public void initUser(String operator_id, HttpServletRequest request)  {
		Subject currentUser = SecurityUtils.getSubject();
		LoginService loginService = (LoginService) SpringUtil.getSpringBean("loginService");
		// 设置用户信息 user
		User user = new User();
		loginService.setUserInfo(user, operator_id);
		Session session = currentUser.getSession();
		session.setAttribute("user", user);
//		logger.debug("用户"+operator_id+"是否认证：" + currentUser.isAuthenticated());
		// 当前活动用户没有认证时，执行认证操作；首先将前台页面传入的username、password封装到token中；绝大部分使用UsernamePasswordToken;
		UsernamePasswordToken token = new UsernamePasswordToken(operator_id, user.getPassword());
		// 如果有记住我的功能，则从页面获取rememberMe，如果选择记住我则为true，否则为false；
		token.setRememberMe(false);
		currentUser.login(token); // 调用login方法将token传入shiro管理库中进行认证操作；
	}

	public String getOperIdByMd5(String operator_md5){
		LoginService loginService = (LoginService) SpringUtil.getSpringBean("loginService");
		return loginService.getOperIdByMd5(operator_md5);
	}
}