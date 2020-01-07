package baobeisys.action.login;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import baobeisys.service.login.LoginService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;
import web.model.User;

import com.sgy.util.common.DateHelper;

/**
 * 人员登录
 * @date 2019-1-11
 */
@Controller
@RequestMapping(value="/login/")
public class LoginController extends BaseController {

	@Autowired
	public LoginService loginService;

	/**
	 * value="/login/login.do" 表示拦截的url
	 * method=RequestMethod.POST post请求 如果不写则是post和get方式都可以
	 * @param modelMap 可以放入值或对象供页面使用
	 * @param request
	 * @return
	 */
	@RequestMapping(value="login.do")
	public String login() {
		String operator_id = req.getValue(request, "loginName");
		String password = req.getValue(request, "loginPassword");
		String loginType = req.getValue(request, "loginType");//登陆类型  1 人员编号登陆  2手机号码登陆
		
		
		//校验用户
		int res = loginService.checkUser(request);
		request.setAttribute("loginRes", res);
		
		int checkRes = 1;
		
		request.setAttribute("operatorId", operator_id);
		request.setAttribute("pwd", password);
		
		if(res == 1){
		    checkRes = loginService.checkPassword(request);
	        request.setAttribute("checkRes", checkRes);
		}
		
		if (res == 1 && checkRes != -4) {
		    if("2".equals(loginType)){//手机号码登陆
		        //获取人员编号
		        operator_id = loginService.getOperatorIdByMobile(request, operator_id);
	        }
		    
			// 设置用户信息 user
			User user = new User();
			logger.debug("成功登录."); 
			loginService.setUserInfo(user, operator_id);
			
			Subject currentUser = SecurityUtils.getSubject();//获取当前活动用户信息；
			Session session = currentUser.getSession();
			session.setAttribute("user", user);
			System.out.println("当前活动用户是否认证："+currentUser.isAuthenticated());
			if(!currentUser.isAuthenticated()){
				//当前活动用户没有认证时，执行认证操作；首先将前台页面传入的username、password封装到token中；绝大部分使用UsernamePasswordToken;
				UsernamePasswordToken token = new UsernamePasswordToken(operator_id, user.getPassword());
				//如果有记住我的功能，则从页面获取rememberMe，如果选择记住我则为true，否则为false；
				token.setRememberMe(false);
				currentUser.login(token);  //调用login方法将token传入shiro管理库中进行认证操作；
			}
			
            // 记录登陆日志
            loginService.recordLoginLog(request, user.getOperatorId(), "login", "用户登录", "2", "", user.getOrgId(),
                    user.getCountyId());
            
//            if("123456".equals(password)) {
//                logger.debug("修改密码");
//                return "redirect:/baobeisys/personalsetting/changePassWord.do?operator_id="+operator_id;
//            }
           return "redirect:/login/index.do"; 
		}
		
		return COM_PATH +"baobeisys/login/loginResult";
	}
	
	/**
	 * 检查密码是否为初始密码
	 * @return
	 */
	@RequestMapping(value="checkPwd.do")
	public void checkPwd(HttpServletResponse response){
		//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
	    logger.debug("test");
        int res = loginService.checkPassword(request);
        this.writeText(res,response);
	}
	
	 /**
     * 修改密码页面
     */
	@RequestMapping(value="editPwdFrame.do")
    public String editPwdFrame(){
        return COM_PATH +"baobeisys/login/editPwd";
    }
    
    /**
     * 修改密码
     */
	@RequestMapping(value="editPwd.do")
    public void editPwd(HttpServletResponse response){
		//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        int result=loginService.editpwd(request);
        this.writeText(result,response);
    }

	/**
	 * 系统首页
	 * @return
	 */
	@RequestMapping(value="index.do")
	public String index() {
		request.setAttribute("loginDate", DateHelper.getToday("yyyy-MM-dd HH:mm:ss"));
		return COM_PATH +"baobeisys/index";
	}
	
	/**
	 * 系统首页
	 * @return
	 */
	@RequestMapping(value="downLoad.do")
	public String downLoad() {
		return COM_PATH +"baobeisys/down";
	}
	
	
}