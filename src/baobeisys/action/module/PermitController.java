package baobeisys.action.module;



import baobeisys.service.module.PermitService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;

/**
 * 角色权限管理
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value="/permit/")
public class PermitController extends BaseController {
    /**
     * spring 注入类
     */
    @Autowired
    public PermitService permitService;
    
    /**
     * 当选择角色下拉框的时候，要跳转的界面
     * @return
     */
    @RequestMapping(value = "roleQuery.do")
    public String roleQuery() {
        request.setAttribute("roles", permitService.getRoleList());
        return COM_PATH + "baobeisys/module/module/permit/role_query";
    }

    /**
     * 保存角色菜单
     * @param response
     * @return
     */
    @RequestMapping(value="roleSave.do")
    public void roleSave(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(permitService.saveRoleModule(request), response);
    }
    
    /**
     * 模块权限管理 加载树
     * @return
     */
    @RequestMapping(value="queryModulePermit.do")
    public String queryPermit() {
        return COM_PATH +"baobeisys/module/module/modulepermit/modulepermit_query";
    }
    
    /**
     * 选择模块树，动态加载角色
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value="showReplaceRole.do")
    public String showReplaceRole(HttpServletResponse response) {
        request.setAttribute("roleGroupList", permitService.getRoleGroupList());
        request.setAttribute("roleList", permitService.getRoleList());
        request.setAttribute("roleStrs", permitService.getRolesByModuleId(request));
        return COM_PATH +"baobeisys/module/module/modulepermit/ajaxModuleRole";
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
        List<?> list  = permitService.getModulePermitList(request);
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
                    + ",checked:" + StringHelper.get(map, "CHECKED")
                    );
            sb.append("}");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.writeText(sb, response);
    }
    
    /**
     * 保存或修改模块角色配置
     * @param response
     */
    @RequestMapping(value="saveModuleRole.do")
    public void saveModuleRole(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(permitService.saveModuleRole(request), response);
    }
}