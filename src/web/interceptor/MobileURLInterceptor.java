package web.interceptor;

import net.sf.json.JSONObject;
import web.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import weixin.entity.WxUserInfo;
import weixin.service.WxLoginService;
import baobeisys.service.login.LoginService;

import com.sgy.util.Constants;
import com.sgy.util.common.StringHelper;
import com.sgy.util.spring.SpringUtil;

/**
 * URL拦截，判断当前用户是否认证，如果没有认证则跳转到登录页面
 * @author zhang
 * @createDate 2019-01-22
 * @description
 */
public class MobileURLInterceptor extends URLInterceptor {
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
			LoginService loginService = (LoginService) SpringUtil.getSpringBean("loginService");
			
			/*String token = request.getHeader("token");
			logger.debug("token : " + token);
			String operId = loginService.getOperIdByToken(token);
			this.initUser(operId, request);*/
			
			if (!"".equals(mappingURL) && StringHelper.isContains(url, mappingURL.split(","))) {
				return true;
			}
			//手机端登录
			if (url.indexOf("/mobile/") > -1) {
				logger.debug("mobile request url is ：" + url);
				String token = request.getHeader("token");
				logger.debug("token : " + token);
				String operId = loginService.getOperIdByToken(token);
				this.initUser(operId, request);
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
			logger.debug("token验证");
			// 设置用户信息 user
			User user = new User();
			loginService.setUserInfo(user, operator_id);
			Session session = currentUser.getSession();
			session.setAttribute("user", user);
//			logger.debug("用户"+operator_id+"是否认证：" + currentUser.isAuthenticated());
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