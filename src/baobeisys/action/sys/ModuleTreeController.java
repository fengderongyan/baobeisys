package baobeisys.action.sys;


import baobeisys.service.sys.ModuleTreeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;

/**
 * 加载菜单树
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value = "/sys/moduletree/")
public class ModuleTreeController extends BaseController {
    @Autowired
    private ModuleTreeService moduleTreeService;

    /**
     * 加载菜单树列表
     * @param response
     */
    @ResponseBody
    @SuppressWarnings({ "static-access", "unchecked" })
    @RequestMapping(value="loadModuleList.do")
    public void loadModuleList(HttpServletResponse response){
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        StringBuffer sb = new StringBuffer();
        List<?> list  = moduleTreeService.getModuleList(request);
        sb.append("[");
        Map map = null;
        for (int i = 0; i < list.size(); i++) {
            map = (Map)list.get(i);
            if(!"".equals(StringHelper.get(map, "MODULE_ICON"))){
                sb.append("{name:\"" + StringHelper.get(map, "MODULE_NAME")
                        + "\",id:\"" + StringHelper.get(map, "MODULE_ID")
                        + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
                        + "\",moduleUrl:\""+ StringHelper.get(map, "MODULE_URL")
                        + "\",openFlag:\""+ StringHelper.get(map, "OPEN_FLAG")
                        + "\",icon:\"../images/menu_img/"+ StringHelper.get(map, "MODULE_ICON")
                        + "\",moduleFlag:" + StringHelper.get(map, "MODULE_FLAG")
                        );
            }else{
                sb.append("{name:\"" + StringHelper.get(map, "MODULE_NAME")
                        + "\",id:\"" + StringHelper.get(map, "MODULE_ID")
                        + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
                        + "\",moduleUrl:\""+ StringHelper.get(map, "MODULE_URL")
                        + "\",openFlag:\""+ StringHelper.get(map, "OPEN_FLAG")
                        + "\",moduleFlag:" + StringHelper.get(map, "MODULE_FLAG")
                        );
            }
            sb.append("}");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        //System.out.print(sb);
        this.writeText(sb, response);
    }
    
//    /**
//     * 加载菜单树列表(主页面)
//     * @param response
//     */
//    @ResponseBody
//    @SuppressWarnings({ "static-access", "unchecked" })
//    @RequestMapping(value="indexLoadModuleList2.do")
//    public void indexLoadModuleList2(HttpServletResponse response){
//    	//xml解析错误
//    	//response 追加字节编码格式解决
//    	response.setContentType("text/text;charset=utf-8");
//    	response.setCharacterEncoding("UTF-8");
//        StringBuffer sb = new StringBuffer();
//        List<?> list  = moduleTreeService.getIndexModuleList(request);
//        sb.append("[");
//        Map map = null;
//        for (int i = 0; i < list.size(); i++) {
//            map = (Map)list.get(i);
//            if(!"".equals(StringHelper.get(map, "MODULE_ICON"))){
//                sb.append("{name:\"" + StringHelper.get(map, "MODULE_NAME")
//                        + "\",id:\"" + StringHelper.get(map, "MODULE_ID")
//                        + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
//                        + "\",moduleUrl:\""+ StringHelper.get(map, "MODULE_URL")
//                        + "\",openFlag:\""+ StringHelper.get(map, "OPEN_FLAG")
//                        + "\",moduleFlag:" + StringHelper.get(map, "MODULE_FLAG")
//                        );
//            }else{
//                sb.append("{name:\"" + StringHelper.get(map, "MODULE_NAME")
//                        + "\",id:\"" + StringHelper.get(map, "MODULE_ID")
//                        + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID")
//                        + "\",moduleUrl:\""+ StringHelper.get(map, "MODULE_URL")
//                        + "\",openFlag:\""+ StringHelper.get(map, "OPEN_FLAG")
//                        + "\",icon:\"../images/menu/"+ StringHelper.get(map, "ICON")
//                        + "\",moduleFlag:" + StringHelper.get(map, "MODULE_FLAG")
//                        );
//            }
//            sb.append("}");
//            if (i < list.size() - 1) {
//                sb.append(",");
//            }
//        }
//        sb.append("]");
//        //System.out.print(sb);
//        this.writeText(sb, response);
//    }
    
    @RequestMapping(value="indexLoadModuleList.do")
    @ResponseBody
    public List indexLoadModuleList(HttpServletRequest request, HttpServletResponse response){
    	
    	List moduleList =  moduleTreeService.getIndexModuleList(request);
    	List list = new  ArrayList();
    	for (int i = 0; i < moduleList.size(); i++) {
            Map temMap = (Map)moduleList.get(i);
            Map map = new HashMap();
            map.put("name", str.get(temMap, "MODULE_NAME"));
            map.put("id", str.get(temMap, "MODULE_ID"));
            map.put("pId", str.get(temMap, "SUPERIOR_ID"));
            map.put("moduleUrl", str.get(temMap, "MODULE_URL"));
            map.put("openFlag", str.get(temMap, "OPEN_FLAG"));
            map.put("icon", "../images/menu/" + str.get(temMap, "ICON"));
            map.put("moduleFlag", str.get(temMap, "MODULE_FLAG"));
            list.add(map);
        }
    	logger.debug(list.toArray());
    	return list;

	}
    
}
