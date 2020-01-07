package baobeisys.action.yjcfg;


import baobeisys.service.yjcfg.YjCfgService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;

/**
 * 饮酒报备配置
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value = "/yjcfg")
public class YjCfgController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public YjCfgService yjCfgService;

    /**
     * 用户查询界面
     * @return
     */
    @RequestMapping(value = "/cfgFrame.do")
    public String userFrame() {
        request.setAttribute("rolesList", yjCfgService.getRoleList(request));
        return COM_PATH + "baobeisys/yjcfg/frameUser";
    }

    /**
     * 用户列表
     * @return
     */
    @RequestMapping(value = "/userList.do")
    public String queryUser() {
        request.setAttribute("userList", yjCfgService.getUserList(request));
        
        return COM_PATH +"baobeisys/yjcfg/listUser";
    }
    
    @RequestMapping(value = "/yjcfgMain.do")
    public String yjcfgMain(){
    	request.setAttribute("yjbbLevList", yjCfgService.getDDw("30001"));
    	return COM_PATH + "baobeisys/yjcfg/yjcfgMain";
    }
    
    @RequestMapping(value = "/yjcfg.do")
    public String yjcfg(){
    	String deal_lev = req.getValue(request, "deal_lev");
    	String operatorIdStr = req.getValue(request, "operatorIdStr");
    	List dealUserList = yjCfgService.getDealUserList(request);
    	request.setAttribute("dealUsers", dealUserList);
    	return COM_PATH + "baobeisys/yjcfg/yjcfg";
    }
    
    /**
     * 加载菜单树列表
     * @param response
     */
    @SuppressWarnings({ "static-access", "unchecked" })
    @RequestMapping(value="/loadOrgList.do")
    @ResponseBody
    public List loadOrgList(HttpServletResponse response){
    	 List<?> list  = yjCfgService.getOrgList();
        return list;
	        
    }
 
    /**
     * 描述：点击树节点，获取对应组织的人员信息
     * @return
     * @author yanbs
     * @Date : 2019-07-17
     */
    @RequestMapping(value="/getUserListByOrgId.do")
    public String getUserListByOrgId(){
    	List userList = yjCfgService.getUserListByOrgId(request);
    	request.setAttribute("userList", userList);
    	return COM_PATH + "baobeisys/yjcfg/dealUser";
    }
    
    /**
     * 描述：删除某个审核人
     * @return
     * @author yanbs
     * @Date : 2019-07-17
     */
    @RequestMapping(value="/deleteDealUser.do")
    @ResponseBody
    public List deleteDealUser(){
    	List dealUserList = yjCfgService.deleteDealUser(request);
    	return dealUserList;
    }
    
    @RequestMapping(value="/addDealOper.do")
    @ResponseBody
    public List addDealOper(){
    	List dealUserList = yjCfgService.addDealOper(request);
    	return dealUserList;
    }
}
