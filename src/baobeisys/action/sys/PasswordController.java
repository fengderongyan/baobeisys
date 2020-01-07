package baobeisys.action.sys;

import baobeisys.service.sys.PasswordService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

/**
 * 密码功能
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value = "/sys/password/")
public class PasswordController extends BaseController {
    
    @Autowired
    private PasswordService passwordService;

    /**
     * 跳转修改密码页面
     * @return
     */
    @RequestMapping(value = "changePassWordFrame.do")
    public String changePassWordFrame() {
        String operator_id = this.getUser(request).getOperatorId();
        request.setAttribute("operatorId", operator_id);
        return COM_PATH +"baobeisys/sys/password/changePassWordFrame";
    }
    
    /**
     * 验证验证原密码是否正确
     */
    @RequestMapping(value = "checkPassWord.do")
    public void checkPassWord(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(passwordService.checkPassWord(request), response);
    }
    
    /**
     * 保存密码
     */
    @RequestMapping(value = "savePassWord.do")
    public void savePassWord(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(passwordService.savePassWord(request), response);
    }
    
    /**
     * 跳转修改密码页面
     * @return
     */
    @RequestMapping(value = "changePassWord.do")
    public String changePassWord() {
        request.setAttribute("qzxg_flag", "1");
        request.setAttribute("operatorId", req.getValue(request, "operator_id"));
        request.setAttribute("userType", req.getValue(request, "userType"));
        return COM_PATH +"baobeisys/sys/password/changePassWordFrame";
    }
    
    /********************************忘记密码功能*******************************************/
    /**
     * 获取验证码
     */
    @RequestMapping(value = "getMsgCode.do")
    public void getMsgCode(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(passwordService.sendMsgCode(request), response);
    }
    
    /**
     * 校验验证码
     */
    @RequestMapping(value = "checkMsgCode.do")
    public void checkMsgCode(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(passwordService.checkMsgCode(request), response);
    }
    
    /**
     * 保存密码
     */
    @RequestMapping(value = "saveForgetPassWord.do")
    public void saveForgetPassWord(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(passwordService.saveForgetPassWord(request), response);
    }
}
