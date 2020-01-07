package web.interceptor;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import web.bean.SystemInfo;
import web.model.User;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.spring.SpringUtil;

/**
 * 系统安全拦截器
 * 防止csrf攻击，请求加csrfToken参数
 * 校验请求头，校验请求来源是否是允许的站点
 * @author zhang
 * @createDate 2019-01-15
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    protected Logger logger = Logger.getLogger(getClass());
    private SystemInfo systemInfo;  //系统初始化缓存
    private String mappingURL = "";    //不需要拦截的URL
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String url = request.getRequestURL().toString();
        if (!"".equals(mappingURL) && StringHelper.isContains(url, mappingURL.split(","))) {
            return true;
        }
        
        /******************************记录请求来源***********************************************/
        //this.recordRequestReferer(request);
        
        
        /*******************************校验请求来源如果是允许的外部来源则放过去***********************/
        if (this.isAllowReferer(request)) {//如果是允许的外部请求来源，则放过去
            //logger.debug("允许的外部请求来源");
            return true;
        }
        
        /*******************************校验csrfToken防止csrf攻击*********************************/
        boolean hasCsrfToken = this.chkCSRFToken(request);
//      if (!hasCsrfToken) {//校验csrfToken不成功，则跳转错误页面
//          logger.debug("校验csrfToken失败");
//          return false;
//      }
        
        return super.preHandle(request, response, handler);
    }
    
    /**
     * 校验请求链接csrfToken是否存在于用户会话中
     * @param request
     * @return
     */
    public boolean chkCSRFToken(HttpServletRequest request){
        String csrfToken =  StringHelper.notEmpty(request.getParameter("csrfToken"));//获取页面的csrfToken
        //logger.debug("csrfToken:"+csrfToken);
        if (null == csrfToken || csrfToken.equals("")) {//如果没有传csrfToken，则校验失败
            return false;
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Map<String, String> csrfTokenMap = (Map<String, String>)session.getAttribute("csrfTokenMap");//获取session中的csrfToken集合
        if (null != csrfTokenMap) {
            String createTime = StringHelper.get(csrfTokenMap, csrfToken);//获取token创建的时间
            if (createTime.equals("")) {//如果页面传入的csrfToken不存在用户session中，则校验失败
                //还要在校验下token的有效期，目前有效期可以设置为1天
                return false;
            }
        } else {//用户没有token集，校验失败
            return false;
        }
        return true;
    }
    
    /**
     * 记录请求来源
     * @return
     */
    public void recordRequestReferer(HttpServletRequest request){
        String referer = StringHelper.notEmpty(request.getHeader("Referer"));//获取该连接的请求来源
        DBHelperSpring db = (DBHelperSpring) SpringUtil.getSpringBean("dbHelper");
        Enumeration<String> header = request.getHeaderNames();
        String headers = "";
        while (header.hasMoreElements()) {
            String name = header.nextElement();
            headers += ",["+name+":"+request.getHeader(name)+"]";
        }
        if (!headers.equals("")) {
            headers = headers.substring(1);
        }
        
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User)session.getAttribute("user");
        if (null == user) {//如果没有会话
            user = new User();
        }
        
//      if (!referer.equals("")) {
//          String sql = "insert into t_request_header(req_id, req_header, req_referer, req_url, req_time, req_operator)" +
//                  " values(sys_guid(), ?, ?, ?, sysdate, ?)";
//          db.update(sql, new Object[]{headers, referer, request.getRequestURL().toString(), user.getOperatorId()});
//      }
    }
    
    /**
     * 检验URL请求来源是否是系统允许的来源
     * 需要捕捉异常，手机端请求、后台请求可能会获取不到header
     * @return
     */
    public boolean isAllowReferer(HttpServletRequest request){
//      String referer = StringHelper.notEmpty(request.getHeader("Referer"));//获取该连接的请求来源
//      for (Map<String, Object> refererMap : systemInfo.getRefererList()) {
//          String reqType = StringHelper.get(refererMap, "REQ_TYPE");
//          String refererIp = StringHelper.get(refererMap, "REFERER_IP");
//          if (reqType.equals("2")) {//外部来源
//              if (!referer.equals("") && referer.contains(refererIp)) {//来源不为空，并且是允许的外部来源
//                  return true;
//              }
//          }
//      }
        
        return true;
    }

    public String getMappingURL() {
        return mappingURL;
    }
    
    public void setMappingURL(String mappingURL) {    
        this.mappingURL = mappingURL;    
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }
}