package baobeisys.action.sys;


import baobeisys.service.sys.UserCfgService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import web.action.BaseController;

import com.sgy.util.common.StringHelper;

/**
 * 用户管理
 * @date 2016-2-22
 */
@Controller
@RequestMapping(value = "/sys/usercfg/")
public class UserCfgController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public UserCfgService userCfgService;
    private String file;
    public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	private String fileFileName;

    /**
     * 用户查询界面
     * @return
     */
    @RequestMapping(value = "userFrame.do")
    public String userFrame() {
        request.setAttribute("rolesList", userCfgService.getRoleList(request));
        return COM_PATH +"baobeisys/sys/usercfg/frameUser";
    }

    /**
     * 用户列表
     * @return
     */
    @RequestMapping(value = "userList.do")
    public String queryUser() {
        request.setAttribute("userList", userCfgService.getUserList(request));
        
        return COM_PATH +"baobeisys/sys/usercfg/listUser";
    }
    

    /**
     * 修改用户的信息
     * @return
     */
    @RequestMapping(value = "editUser.do")
    public String editUser() {
        String method = req.getValue(request, "method");
        String operator_id = req.getValue(request, "operator_id");
        request.setAttribute("userInfo", userCfgService.getUserInfo(request));
        request.setAttribute("rolesList", userCfgService.getRoleList(request));
        return COM_PATH +"baobeisys/sys/usercfg/editUser";
    }
    
    /**
     * 多个用户信息新增
     * @return
     */
    @RequestMapping(value = "editMoreUser.do")
    public String editMoreUser() {
        return COM_PATH +"baobeisys/sys/usercfg/editMoreUser";
    }

    /**
     * 加载组织树
     * @param response
     */
    @SuppressWarnings( {"static-access", "unchecked"})
    @RequestMapping(value = "loadOrgListTree.do")
    public void loadOrgListTree(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        StringBuffer sb = new StringBuffer();
        List<?> list = userCfgService.getOrgList(request);
        sb.append("[");
        Map map = null;
        for(int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);

            sb.append("{name:\"" + StringHelper.get(map, "ORG_NAME") 
                    + "\",id:\"" + StringHelper.get(map, "ORG_ID") 
                    + "\",pId:\"" + StringHelper.get(map, "SUPERIOR_ID") 
                    + "\",org_level:\"" + StringHelper.get(map, "ORG_LEVEL") 
                    + "\",checked:" + StringHelper.get(map, "CHECKED"));
            sb.append("}");
            if(i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.writeText(sb, response);
    }
    
    /**
     * 签名上传界面
     * @return
     */
    @RequestMapping(value = "qmInfoUpload.do")
    public String qmInfoUpload() {
    	request.setAttribute("operatorId", req.getValue(request, "operator_id"));
        return COM_PATH +"baobeisys/sys/usercfg/qminfoup";
    }
    
    /**
     * 上传文件
     * @return
     */
    @RequestMapping(value="impFile.do")
    public String impFile(@RequestParam(value="file") CommonsMultipartFile file) {
        fileFileName = file.getOriginalFilename();
        request.setAttribute("result", userCfgService.impQmResult(this, request, file, fileFileName));
        //return COM_PATH +"baobeisys/cgjc/clsc/saveResult";
        return COM_PATH + "baobeisys/sys/usercfg/saveResult";
    }

    /**
     * 插入或新增一个用户
     * @return
     */
    @RequestMapping(value = "saveOrUpdateUser.do")
    public String saveOrUpdateUser() {
        request.setAttribute("result", userCfgService.saveOrUpdateUser(request));
        return COM_PATH +"baobeisys/global/saveResult";
    }
    
    /**
     * 多个用户信息保存
     * @return
     */
    @RequestMapping(value = "saveMoreUser.do")
    public String saveMoreUser() {
        request.setAttribute("result", userCfgService.saveMoreUserInfo(request));
        return COM_PATH +"baobeisys/global/saveResult";
    }

    /**
     * 判断是否存在该用户
     */
    @RequestMapping(value = "checkUserExist.do")
    public void checkUserExist(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.getUserCountByOperatorId(request), response);
    }

    /**
     * 验证手机号码加状态唯一
     * @param response
     */
    @RequestMapping(value = "checkUserMsisdn.do")
    public void checkUserMsisdn(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.checkUserMsisdn(request), response);
    }
    
    /**
     * 验证工号状态唯一
     * @param response
     */
    @RequestMapping(value = "checkUserOperId.do")
    public void checkUserOperId(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.checkUserOperId(request), response);
    }
    
    /**
     * 验证手机号码加状态唯一(more)
     * @param response
     */
    @RequestMapping(value = "checkMoreUserMsisdn.do")
    public void checkMoreUserMsisdn(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.checkMoreUserMsisdn(request), response);
    }
    
    /**
     * 验证组织是否存在(more)
     * @param response
     */
    @RequestMapping(value = "checkMoreOrg.do")
    public void checkMoreOrg(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.checkMoreUserOrg(request), response);
    }
    
    /**
     * 验证角色是否存在(more)
     * @param response
     */
    @RequestMapping(value = "checkMoreRole.do")
    public void checkMoreRole(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.checkMoreUserRole(request), response);
    }

    /**
     * 删除用户
     * @return
     */
    @RequestMapping(value = "deleteUser.do")
    public void deleteUser(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(userCfgService.deleteUser(request), response);
    }
    
    /**
     * 根据组织获取角色列表
     * @return
     */
    @RequestMapping(value = "getRoleListByOgrLevel")
    public String getRoleListByOgrLevel() {
        request.setAttribute("userInfo", userCfgService.getUserInfo(request));
        request.setAttribute("rolesList", userCfgService.getRoleListByOgrLevel(request));
        return COM_PATH +"baobeisys/sys/usercfg/ajaxrolelist";
    }
    
    /**
     * 信息导入页面
     * @return
     */
    @RequestMapping(value="importUserFrame.do")
    public String importCheckFrame(){
        return COM_PATH +"baobeisys/sys/usercfg/impframe";
    }
    
    /**
     * 信息导入
     * @return
     */
    @RequestMapping(value="impInfo.do")
    public String impInfo(@RequestParam(value="file") CommonsMultipartFile file) {
        String fileFileName = file.getOriginalFilename();
        String result= userCfgService.impUserCheckResult(this, request, file, fileFileName);
        return result;
    }
    
    
}
