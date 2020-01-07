package web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import web.model.User;

import com.sgy.util.Constants;
import com.sgy.util.common.StringHelper;

/**
 * URL拦截，判断当前用户是否认证，如果没有认证则跳转到登录页面
 * @author zhang
 * @createDate 2019
 * @description
 */
public class URLInterceptor extends HandlerInterceptorAdapter 
{
    protected Logger logger = Logger.getLogger(getClass());
    private String mappingURL = "";    //不需要拦截的URL
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        if (!"".equals(mappingURL) && StringHelper.isContains(url, mappingURL.split(","))) {
            return true;
        }
        
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/baobeisys/error.jsp");
            return false;
        } 
        
        boolean hasPermit = this.chkPermit(url, request);
//      if (!hasPermit) {//判断当前用户是否有访问该链接的权限 
//          return false;
//      }
        
        response.addHeader("P3P", "CP=CAO PSA OUR");
        return super.preHandle(request, response, handler);
    }

    /**
     * 校验用户是否有访问url的权限
     * @param url
     * @param request
     * @return true:有权限  false:无权限
     */
    public boolean chkPermit(String url, HttpServletRequest request){
        Subject currentUser = SecurityUtils.getSubject();
        String preStr = (String) request.getSession().getServletContext().getAttribute("app");
        String url2 = url.replace("http://127.0.0.1", "");
        String url3 = url2.substring(url2.indexOf(preStr));
        String premitUrl = "";
        premitUrl = url3.substring(preStr.length()+1);
        if (premitUrl.contains("?")) {//如果包含参数，将参数截取掉
            premitUrl = premitUrl.substring(0, premitUrl.indexOf("?"));
        }
        boolean hasPermit = currentUser.isPermitted(premitUrl);
//      logger.debug("premitUrl:"+premitUrl+"----hasPermit:"+hasPermit);
        return hasPermit;
    }

    public String getMappingURL() {
        return mappingURL;
    }
    
    public void setMappingURL(String mappingURL) 
    {    
        this.mappingURL = mappingURL;    
    }
}