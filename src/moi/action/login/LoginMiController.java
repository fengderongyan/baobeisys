package moi.action.login;

import baobeisys.service.login.LoginService;
import util.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import moi.service.login.LoginMiSevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;
import web.model.User;

/** 
 * 手机端登录
 * @date 2019-1-11
 */
@Controller
@RequestMapping(value = "/mobile/loginmi/")
public class LoginMiController extends BaseController{

    @Autowired
    LoginMiSevice loginMiSevice;
    
    @Autowired
    LoginService loginService;
    
    /**
     * 登录验证
     * @param response
     */
    @RequestMapping(value="checkLogin.do")
    public void checkLogin(HttpServletResponse response) {
    	
		String operator_id =  req.getValue(request, "loginName"); 
		String password = req.getValue(request, "loginPassword"); 
		String registrationId = req.getValue(request, "registrationId"); 
		System.out.println("operator_id : " + operator_id + " -- password : " + password + " -- registrationId : " + registrationId );
		
		int result = loginMiSevice.checkUserLogin(operator_id, password);
		System.out.println("checkResutl : " + result);
		User user = new User();

		Map map = new HashMap();
		Map returnMap = new HashMap();
		//登录成功
		if (result == 1){
			response.addCookie(new Cookie("username", operator_id));
			user.setOperatorId(operator_id);
			System.out.println("成功登录.");
			//更新token
			loginService.updateUserToken(operator_id);
			//保存or更新设备ID(保证registrationId唯一)
			loginService.saveOrUpdateUserRegistrationId(operator_id, registrationId);
			//设置user
			loginService.setUserInfo(user,operator_id);
			
			String isChangePwd = "0";
			isChangePwd = loginService.getIsChangePwd(operator_id);
			logger.debug("isChangePwd : " + isChangePwd);
			// 记录登陆日志
            loginService.recordLoginLog(request, user.getOperatorId(), "clientLogin", "移动端登录", "2", "", user.getOrgId(),
                    user.getCountyId());
			request.getSession().setAttribute("FCK.userDir", "/upload/userfiles/" + user.getOperatorId() + "/");
			request.getSession().setAttribute("user", user);
			logger.debug("移动端登录");
			String qmurl = "http://139.224.15.52/sys/pic/"+user.getQm_name();//192.168.0.129:8080
			map.put("operatorId", operator_id);//工号
			map.put("name", user.getName());//姓名
			map.put("loginRes", String.valueOf(result));//登录请求结果
			map.put("orgName", user.getOrgName());//组织名称
			map.put("roleNames", user.getRoleNames());//角色名称
			map.put("orgId", user.getOrgId());//组织id
			map.put("roleLev", user.getMaxRoleLevel());//用户最大角色层级
			map.put("txPic", qmurl);//头像路径地址
			map.put("token", user.getToken());//token
			map.put("jjLxrName", user.getJjLxrName());//紧急联系人名称
			map.put("jjLxrPhone", user.getJjLxrPhone());//紧急联系人电话
			map.put("isChangePwd", isChangePwd);//是否改密
			returnMap.put("errorCode", 0);
			returnMap.put("data", map);
		}else if(result == -1){//用户名不存在
			//map.put("loginRes", String.valueOf(result));
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "user not exist!");
			
		}else if(result == -2){//用户名与密码不匹配
			//map.put("loginRes", String.valueOf(result));
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", " User name does not match password !");
		}else if(result == -3){//数据库连接失败
			//map.put("loginRes", String.valueOf(result));
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Failure of database connection!");
		}else {
			//map.put("loginRes", String.valueOf(result));
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "request was aborted!");
		}
		this.writeJsonData(returnMap, response);
    }
    
    /**
     * 判断版本号
     * @param response
     */
    @RequestMapping(value="checkVersion.do")
    public void checkVersion(HttpServletResponse response, HttpServletRequest request){
    	Map map = new HashMap();
		Map returnMap = new HashMap();
		
    	PropertiesUtil proUtil = new PropertiesUtil();
    	String appOlderVersion = proUtil.getAppVersionPropertiesValue("app_version");//APP版本号
    	String appNewVersion = request.getHeader("version");//手机端version
    	
    	//空值处理
    	if(appOlderVersion.equals("")){
    		returnMap.put("errorCode", -1);
    		returnMap.put("errorInfo", "Unable to get background version number!");
    		this.writeJsonData(returnMap, response);
    		return ;
    	}
    	if(appNewVersion.equals("")){
    		returnMap.put("errorCode", -1);
    		returnMap.put("errorInfo", "Unable to get Mobile terminal version number!");
    		this.writeJsonData(returnMap, response);
    		return ;
    	}
    	
    	if(!appOlderVersion.equals(appNewVersion)){//当前版本号与后台版本号不一样
    		returnMap.put("errorCode", -4);//版本更新
    		returnMap.put("errorInfo", "There is a new version.");
    		this.writeJsonData(returnMap, response);
    		return ;
    	}else {//当前版本号与后台版本一致
    		returnMap.put("errorCode", 0);//不存在版本更新
    		this.writeJsonData(returnMap, response);
    		return ;
    	}
    	
    }
    
    /*
     * 修改密码
     */
    @RequestMapping(value="changPwd.do")
    public void changPwd(HttpServletResponse response){
    	String operator_id =  req.getValue(request, "loginName"); //登录工号
		String password = req.getValue(request, "loginPassword");  //密码
		Map map = new HashMap();
		Map returnMap = new HashMap();
		//空值判断
		if("".equals(operator_id)){
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "can not get param named 'loginName'");
			this.writeJsonData(returnMap, response);
			return ;
		}
		if("".equals(password)){
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "can not get param named 'loginPassword'");
			this.writeJsonData(returnMap, response);
			return ;
		}
		
		int changResult = loginService.changPwd(operator_id, password);
		
		if(changResult == 0){
			returnMap.put("errorCode", -1);
			returnMap.put("errorInfo", "Save failed!");
			this.writeJsonData(returnMap, response);
			return ;
		}else{
			returnMap.put("errorCode", 0);
			map.put("changResult", 1);
			returnMap.put("data", map);
			this.writeJsonData(returnMap, response);
			return ;
		}
		
		
    }
    
    /**
     * 退出登录
     * @param response
     */
    @RequestMapping(value="/loginOut.do")
    public void loginOut(HttpServletResponse response) {
    	request.getSession().setMaxInactiveInterval(0);
    	this.writeJsonData(1, response);
    }
}
