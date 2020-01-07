package baobeisys.action.sys;

import baobeisys.service.sys.DictionaryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

import com.sgy.util.common.DateHelper;

/**
 * 字典配置
 * @date 2018-12-25
 */
@Controller
@RequestMapping(value = "/sys/dictionary/")
public class DictionaryController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public DictionaryService dictionaryService;
    

    /**
     * 字典表配置主页面
     * @return
     */
    @RequestMapping(value="dictionaryFrame.do")
    public String dictionaryFrame(HttpServletRequest request){
        request.setAttribute("typeList", dictionaryService.getDictionaryType(request));
        return COM_PATH +"baobeisys/sys/dictionary/dictionaryFrame";
    }
    
    /**
     * 菜单访问流水主列表
     * @return
     */
    @RequestMapping(value="dictionaryList.do")
    public String dictionaryList(){
        request.setAttribute("list", dictionaryService.getDictionaryList(request));
        return COM_PATH +"baobeisys/sys/dictionary/dictionaryList";
    }
    
    /**
     * 字典表配置编辑页面
     * @return
     */
    @RequestMapping(value="dictionaryEdit.do")
    public String dictionaryEdit(HttpServletRequest request){
        request.setAttribute("typeList", dictionaryService.getDictionaryType(request));
        request.setAttribute("method", req.getValue(request, "method"));
        if(req.getValue(request, "method").equals("edit")){
        	request.setAttribute("dictionaryInfo", dictionaryService.getDicInfo(request));
        }
        return COM_PATH +"baobeisys/sys/dictionary/dictionaryEdit";
    }
    
    /** 
     * 获取所选字典类最大信息编码
     * @return
     */ 
    @RequestMapping(value = "ajaxMaxInfoId.do")
    public String ajaxMaxInfoId(){
        String data_type_code = req.getValue(request, "data_type_code");
        request.setAttribute("data_type_code", data_type_code);
        request.setAttribute("maxId", dictionaryService.getMaxIdByType(data_type_code));
        return COM_PATH + "baobeisys/sys/dictionary/ajaxspan";
    }
    
    /**
     * 校验信息编号是否存在
     */
    @RequestMapping(value = "checkInfoId.do")
    public void checkInfoId(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(dictionaryService.checkInfo(request), response);
    }
    
    /**
     * 保存信息
     * @return
     */
    @RequestMapping(value = "saveOrUpdate.do")
    public String saveOrUpdate() {
        request.setAttribute("result", dictionaryService.saveOrUpdate(request));
        return COM_PATH +"baobeisys/global/saveResult";
    }
    
    /**
     * 删除信息
     * @return
     */
    @RequestMapping(value = "deleteDictionaryCfg.do")
    public void deleteDictionaryCfg(HttpServletResponse response) {
    	//xml解析错误
    	//response 追加字节编码格式解决
    	response.setContentType("text/text;charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
        this.writeText(dictionaryService.deleteDictionaryCfg(request), response);
    }
    
}
