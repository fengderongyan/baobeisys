package baobeisys.action.module;


import baobeisys.service.module.AdminModuleService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;


/**
 * 后台模块管理
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value="/adminmodule/")
public class AdminModuleController extends BaseController {
    
    /**
     * spring依赖注入
     */
    @Autowired
    public AdminModuleService adminModuleService;
    
    /**
     * 模块管理主页面
     * @return
     */
    @RequestMapping(value="moduleFrame.do")
    public String moduleFrame() {
    	logger.debug("into  moduleFrame.do");
        return COM_PATH +"baobeisys/module/adminmodule/frame";
    }
    
    /**
     * 查询模块树
     * @return
     */
    @RequestMapping(value="queryModule.do")
    public String queryModule() {
    	logger.debug("into queryModule.do");
        return COM_PATH +"baobeisys/module/adminmodule/modulecfg/module_query";
    }
    
    /**
     * 加载菜单树列表
     * @param response
     */
    @SuppressWarnings({ "static-access", "unchecked" })
    @RequestMapping(value="loadModuleList.do")
    public void loadModuleList(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
    	
        StringBuffer sb = new StringBuffer();
        List<?> list  = adminModuleService.getModuleList();
        sb.append("[");
        Map map = null;
        for (int i = 0; i < list.size(); i++) {
            map = (Map)list.get(i);
            
            sb.append("{name:\"" + StringHelper.get(map, "MODULE_NAME")
                    + "\",id:\"" + StringHelper.get(map, "MODULE_ID")
                    + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
                    + "\",moduleFlag:" + StringHelper.get(map, "MODULE_FLAG")
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
     * 转到模块更新，删除页面
     * @return
     */
    @RequestMapping(value="editModule.do")
    public String editModule() {
    	logger.debug("editModule.do");
        String action = req.getValue(request, "action");
        request.setAttribute("action", action);
        //URL类型
        //request.setAttribute("urlTypeList", adminModuleService.getDictItemList("URL.TYPE"));
        if("add".equals(action)){
            String module_id = adminModuleService.db.getMysqlNextSequenceValue("t_module_sid");
            request.setAttribute("module_id", module_id);
            //request.setAttribute("suffix_code", adminModuleService.getModulePrefixCode());
        }
        if("update".equals(action)) {
            request.setAttribute("info", adminModuleService.getModuleInfo(request));
            //request.setAttribute("permitList", adminModuleService.getPermitByModuleId(request));
        }
        return COM_PATH +"baobeisys/module/adminmodule/modulecfg/module_detail";
    }
    
    /**
     * 打开上级模块目录
     * @param response
     * @return
     */
    @RequestMapping(value="getSuperiorTree.do")
    public String getSuperiorTree(HttpServletResponse response){
        return COM_PATH +"baobeisys/module/adminmodule/modulecfg/module_superior";
    }
    
    /**
     * 验证模块配置编码是否存在
     * @param response
     */
    @RequestMapping(value="checkModuleCodeExists.do")
    public void checkModuleCodeExists(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminModuleService.checkModuleCodeExists(request), response);
    }
    
    /**
     * 验证是否存在子节点
     * @param response
     */
    @RequestMapping(value="checkModuleSubExists.do")
    public void checkModuleSubExists(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminModuleService.checkModuleSubExists(request), response);
    }
    
    /**
     * 验证权限编码已经存在
     * @param response
     */
    @RequestMapping(value="checkPermitCodeExists.do")
    public void checkPermitCodeExists(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminModuleService.checkPermitCodeExists(request), response);
    }
    
    /**
     * 模块配置操作
     * @return
     */
    @RequestMapping(value="saveOrUpdate.do")
    public String saveOrUpdate() {
        request.setAttribute("result", adminModuleService.saveOrUpdate(request));
        return COM_PATH +"baobeisys/global/saveResult";
    }
    
    /**
     * 删除
     * @param response
     */
    @RequestMapping(value="deleteModule.do")
    public void deleteModule(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminModuleService.deleteModule(request), response);
    }
    
    /**
     * @description 获得模块默认编码前缀
     * @author YUANFY 2013-08-06
     * @param response
     */
    @RequestMapping(value="getModuleDefaultCodePrefix.do")
    public void getModuleDefaultCodePrefix(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(adminModuleService.getModuleDefaultCodePrefix(request), response);
    }
    
    /**
     * 加载模块树
     * @param response
     */
    @SuppressWarnings({ "static-access", "unchecked" })
    @RequestMapping(value="loadSuperiorModule.do")
    public void loadSuperiorModule(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        StringBuffer sb = new StringBuffer();
        List<?> list  = adminModuleService.getSuperiorModuleList(request);
        sb.append("[");
        Map map = null;
        for (int i = 0; i < list.size(); i++) {
            map = (Map)list.get(i);
            sb.append("{name:\"" + StringHelper.get(map, "MODULE_NAME")
                    + "\",id:\"" + StringHelper.get(map, "MODULE_ID")
                    + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
                    + "\",level:"+ StringHelper.get(map, "MODULE_LEVEL")
                    + ",isParent:" + StringHelper.get(map, "IS_PARENT")
                    + ",checked:" + StringHelper.get(map, "CHECKED")
                    + ",open:" + StringHelper.get(map, "IS_OPEN")
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
     * 获取图标列表
     */
    @RequestMapping(value = "showModuleIconList.do")
    public String showModuleIconList() {
        request.setAttribute("moduleIconList", adminModuleService.getModuleIconList(request));
        return COM_PATH + "baobeisys/module/adminmodule/modulecfg/moduleIconList";
    }
}