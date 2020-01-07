package baobeisys.action.sys;

import baobeisys.service.sys.OrgCfgService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;

/**
 * 组织管理
 */
@Controller
@RequestMapping(value = "/sys/orgcfg/")
public class OrgCfgController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public OrgCfgService orgCfgService;

    /**
     * 组织管理界面
     * @return
     */
    @RequestMapping(value = "orgCfgFrame.do")
    public String orgCfgFrame() {
        request.setAttribute("orgLevList", orgCfgService.getOrgLevList(request));//层级
        return COM_PATH +"baobeisys/sys/orgcfg/orgCfgFrame";
    }

    /**
     * 组织列表
     * @return
     */
    @RequestMapping(value = "orgCfgList.do")
    public String orgCfgList() {
        request.setAttribute("orgList", orgCfgService.getOrgCfgList(request));
        return COM_PATH +"baobeisys/sys/orgcfg/orgCfgList";
    }

    /**
     * 修改组织信息界面
     * @return
     */
    @RequestMapping(value = "orgCfgEdit.do")
    public String orgCfgEdit() {
        String method = req.getValue(request, "method");
        if(!"create".equals(method)) {
            request.setAttribute("map", orgCfgService.getOrgInfo(request));
        }
        return COM_PATH +"baobeisys/sys/orgcfg/orgCfgEdit";
    }

    /**
     * 保存组织信息
     * @return
     */
    @RequestMapping(value = "saveOrUpdateOrg.do")
    public String saveOrUpdateOrg() {
        request.setAttribute("result", orgCfgService.saveOrUpdateOrg(request));
        return COM_PATH +"baobeisys/global/saveResult";
    }

    /**
     * 打开组织树
     * @return
     */
    @RequestMapping(value = "orgTreeFrame.do")
    public String orgTreeFrame() {
        request.setAttribute("orgList", orgCfgService.getOrgCfgList(request));
        return COM_PATH +"baobeisys/sys/orgcfg/inc/orgTreeFrame";
    }

    /**
     * 加载组织树
     * @param response
     */
    @SuppressWarnings( {"static-access", "unchecked"})
    @RequestMapping(value = "loadOrgTree.do")
    public void loadOrgTree(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        StringBuffer sb = new StringBuffer();
        List<?> list = orgCfgService.getOrgTreeList(request);
        sb.append("[");
        Map map = null;
        for(int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);

            sb.append("{name:\"" + StringHelper.get(map, "ORG_NAME") + 
                    "\",id:\"" + StringHelper.get(map, "ORG_ID") +
                    "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID") +
                    "\",orgLevel:\"" +StringHelper.get(map, "ORG_LEV") + 
                    "\",countyId:\"" +StringHelper.get(map, "COUNTY_ID") + 
                    "\",checked:" + StringHelper.get(map, "CHECKED"));
            sb.append("}");
            if(i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.writeText(sb, response);
    }

    /**
     * 获取组织编号
     */
    @RequestMapping(value = "getOrgId.do")
    public void getOrgId(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(orgCfgService.getOrgId(request), response);
    }

    /**
     * 校验组织编号是否存在
     */
    @RequestMapping(value = "checkOrgId.do")
    public void checkOrgId(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(orgCfgService.checkOrgId(request), response);
    }

    /**
     * 删除组织
     * @return
     */
    @RequestMapping(value = "deleteOrgCfg.do")
    public void deleteOrgCfg(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(orgCfgService.deleteOrgCfg(request), response);
    }
}
