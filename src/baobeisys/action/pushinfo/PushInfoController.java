package baobeisys.action.pushinfo;

import baobeisys.service.pushinfo.PushInfoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import web.action.BaseController;

import com.sgy.util.common.DateHelper;

/**
 * 推送消息
 * @date 2018-02-13
 */
@Controller
@RequestMapping(value = "/pushinfo/pushInfo/")
public class PushInfoController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public PushInfoService pushInfoService;

    /**
     * 推送消息Frame
     * @return
     */
    @RequestMapping(value="pushFrame.do")
    public String pushFrame(){
        return COM_PATH +"baobeisys/pushinfo/pushFrame";
    }
    
    /**
     * 推送消息List
     * @return
     */
    @RequestMapping(value="pushList.do")
    public String pushList(){
    	request.setAttribute("list", pushInfoService.getPushInfoList(request));
        return COM_PATH +"baobeisys/pushinfo/pushList";
    }
    
    /**
     * 编辑页面
     * @return
     */
    @RequestMapping(value="pushEdit.do")
    public String pushEdit(){
        request.setAttribute("method", req.getValue(request, "method"));
        if(req.getValue(request, "method").equals("show")){//详情
        	request.setAttribute("pushInfo", pushInfoService.getDicInfo(request));
        }
        return COM_PATH +"baobeisys/pushinfo/pushEdit";
    }
    
    /**
     * 保存信息
     * @return
     */
    @RequestMapping(value = "saveAndPush.do")
    public String saveAndPush() {
        request.setAttribute("result", pushInfoService.saveOrUpdate(request));
        return COM_PATH +"baobeisys/global/saveResult";
    }
    
}
