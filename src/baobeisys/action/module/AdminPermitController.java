package baobeisys.action.module;

import baobeisys.service.module.AdminPermitService;

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
 * 后台角色权限管理
 * @date 2018-12-22
 */
@Controller
@RequestMapping(value="/adminpermit/")
public class AdminPermitController extends BaseController {
    /**
     * spring 注入类
     */
    @Autowired
    public AdminPermitService adminPermitService;
    
    /**
     * 当选择角色下拉框的时候，要跳转的界面
     * @return
     */
    @RequestMapping(value="roleQuery.do")
    public String roleQuery(HttpServletRequest request) 
    {
        request.setAttribute("roles", adminPermitService.getRoleList2(request));
        request.setAttribute("roleId", req.getValue(request, "role_id"));
        return COM_PATH +"baobeisys/module/adminmodule/permit/role_query";
    }

    /**
     * 保存角色菜单
     * @param response
     * @return
     */
    @RequestMapping(value="roleSave.do")
    public void roleSave(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决loadModulePermit
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminPermitService.saveRoleModule(request), response);
    }
    
    /**
     * @description 模块权限管理 加载树
     * @return
     */
    @RequestMapping(value="queryModulePermit.do")
    public String queryPermit() {
        return COM_PATH +"baobeisys/module/adminmodule/modulepermit/modulepermit_query";
    }
    
    /**
     * @description 选择模块树，动态加载角色
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="showReplaceRole.do")
    public String showReplaceRole(HttpServletResponse response) {
        request.setAttribute("roleList", adminPermitService.getRoleList(request));
        request.setAttribute("roleStrs", adminPermitService.getRolesByModuleId(request));
        return COM_PATH +"baobeisys/module/adminmodule/modulepermit/ajaxModuleRole";
    }
    
    /**
     * 加载模块权限树
     * @param response
     */
    @SuppressWarnings({ "static-access", "unchecked" })
    @RequestMapping(value="loadModulePermit.do")
    public void loadModulePermit(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        StringBuffer sb = new StringBuffer();
        List<?> list  = adminPermitService.getModulePermitList(request);
        sb.append("[");
        Map map = null;
        for (int i = 0; i < list.size(); i++) {
            map = (Map)list.get(i);
            String module_name = StringHelper.get(map, "MODULE_NAME");
            String module_id = StringHelper.get(map, "MODULE_ID");
            sb.append("{name:\"" + module_name
                    + "\",id:\"" + module_id
                    + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
                    + "\",level:"+ StringHelper.get(map, "MODULE_LEVEL")
                    + ",isParent:" + StringHelper.get(map, "IS_PARENT")
                    + ",checked:" + StringHelper.get(map, "CHECKED"));
            sb.append("}");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.writeText(sb, response);
    }
    
    /**
     * @description 保存或修改模块角色配置
     * @author YUANFY 2013-08-07
     * @param response
     */
    @RequestMapping(value="saveModuleRole.do")
    public void saveModuleRole(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminPermitService.saveModuleRole(request), response);
    }
}