package baobeisys.action.common;

import baobeisys.service.common.CommonDataService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import web.action.BaseController;
import web.model.User;

/**
 * 系统公共类 
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value = "/commondata/")
public class CommonDataController extends BaseController {

	@Autowired
	public CommonDataService commonDataService;
	
    /**
     * 获取导出日志状态标识（用于禁用按钮恢复）
     * @param response
     */
    @RequestMapping(value = "getExportLogStatus")
    public void getExportLogStatus(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(commonDataService.getExportLogStatus(request), response);
    }
    
    /**
     * 记录信息导出日志
     * @param response
     */
    @RequestMapping(value = "recordInfoExportLog")
    public void recordInfoExportLog(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(commonDataService.recordInfoExportLog(request), response);
    }
    
    /**
     * 记录系统退出日志
     * @return
     */
    @RequestMapping(value="recordLogout.do")
    public void recordLogout(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        User user = this.getUser(request);
        // 记录退出登陆日志
        commonDataService.recordLoginLog(request, user.getOperatorId(), "logout", "用户退出", "1","", user.getOrgId(), 
                user.getCountyId());
//        logger.debug(this.getUser(request).getOperatorId() + "用户退出！");
    }
    
    /**
     * 菜单点击日志
     * @param response
     */
    @ResponseBody 
    @RequestMapping(value="addNodeLog.do")
    public void addNodeLog(HttpServletResponse response){
        User user = this.getUser(request);
        // 菜单点击日志
        response.setContentType("text/text;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        commonDataService.addNodeLog(request, user.getOperatorId(), user.getOrgId(),user.getCountyId());
       
    }
    
    /**
     * 重新加载系统信息
     * @param response
     */
    @RequestMapping(value="loadSystemInfo.do")
    public String loadSystemInfo(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        commonDataService.getSystemInfo().loadSystemData();
        request.setAttribute("result", "1");
        return "inc/saveResult";
    }
}
