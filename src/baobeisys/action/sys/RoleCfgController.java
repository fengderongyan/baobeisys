package baobeisys.action.sys;


import baobeisys.service.sys.RoleCfgService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;

/**
 * 角色管理
 * @date 2018-12-12
 */
@Controller
@RequestMapping(value = "/sys/rolecfg/")
public class RoleCfgController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public RoleCfgService roleCfgService;

    /**
     * 角色查询主界面
     * @return
     */
    @RequestMapping(value = "roleFrame.do")
    public String roleFrame() {
        //request.setAttribute("rolesList", roleCfgService.getRoleList(request));
        return COM_PATH +"baobeisys/sys/rolecfg/frame";
    }

    /**
     * 角色列表
     * @return
     */
    @RequestMapping(value = "roleList.do")
    public String roleList() {
        request.setAttribute("roleList", roleCfgService.getRolesList(request));
        
        return COM_PATH +"baobeisys/sys/rolecfg/list";
    }
    
    /**
     * 修改角色的信息
     * @return
     */
    @RequestMapping(value = "edit.do")
    public String edit() {
    	String method = req.getValue(request, "method");
    	if(!"create".equals(method)){
    		request.setAttribute("roleInfo", roleCfgService.getRoleInfo(request));
    	}
        return COM_PATH +"baobeisys/sys/rolecfg/edit";
    }

    
    /**
     * 新增或修改角色信息
     * @param response
     * @return
     */
    @RequestMapping(value="saveOrUpdateRole.do")
    public String saveOrUpdateRole(HttpServletRequest request) {
    	int result = roleCfgService.saveOrUpdateRole(request);
    	request.setAttribute("result", result);
    	return COM_PATH + "baobeisys/global/saveResult";
    }

    /**
     * 删除用户
     * @return
     */
    @RequestMapping(value = "deleteRole.do")
    public void deleteUser(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(roleCfgService.delRole(request), response);
    }
    
}
