package baobeisys.action.bbcount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sgy.util.common.DateHelper;

import baobeisys.service.bbcount.BbcountService;
import web.action.BaseController;


/**
 * 报备信息汇总
 * @date 2019-1-17
 */
@Controller
@RequestMapping(value = "/bbcount/")
public class BbcountController extends BaseController {
    /**
     * spring 类型注入
     */
    @Autowired
    public BbcountService bbcountService;
    
    /**
     * 报备详细信息主页面
     * @return
     */
    @RequestMapping(value="detailMain.do")
    public String sysLogMain(){
        return COM_PATH +"baobeisys/bbcount/detailMain";
    }
    
    /**
     * 饮酒明细Frame
     * @return
     */
    @RequestMapping(value="yjDetailFrame.do")
    public String yjDetailFrame(){
        request.setAttribute("begin_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("end_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("orgList", bbcountService.getAllOrgList(request));//组织列表
        return COM_PATH +"baobeisys/bbcount/yjDetailFrame";
    }
    
    /**
     * 饮酒明细List
     * @return
     */
    @RequestMapping(value="yjDetailList.do")
    public String yjDetailList(){
        request.setAttribute("list", bbcountService.getYjDetailList(request));
        return COM_PATH +"baobeisys/bbcount/yjDetailList";
    }

    /**
     * 请假明细Frame
     * @return
     */
    @RequestMapping(value="qjDetailFrame.do")
    public String qjDetailFrame(){
        request.setAttribute("begin_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("end_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("orgList", bbcountService.getAllOrgList(request));//组织列表
        return COM_PATH +"baobeisys/bbcount/qjDetailFrame";
    }
    
    /**
     * 请假明细List
     * @return
     */
    @RequestMapping(value="qjDetailList.do")
    public String qjDetailList(){
        request.setAttribute("list", bbcountService.getQjDetailList(request));
        return COM_PATH +"baobeisys/bbcount/qjDetailList";
    }
    
    /**
     * 用车明细Frame
     * @return
     */
    @RequestMapping(value="ycDetailFrame.do")
    public String ycDetailFrame(){
        request.setAttribute("begin_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("end_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("orgList", bbcountService.getAllOrgList(request));//组织列表
        return COM_PATH +"baobeisys/bbcount/ycDetailFrame";
    }
    
    /**
     * 用车明细List
     * @return
     */
    @RequestMapping(value="ycDetailList.do")
    public String ycDetailList(){
        request.setAttribute("list", bbcountService.getYcDetailList(request));
        return COM_PATH +"baobeisys/bbcount/ycDetailList";
    }
    
    /**
     * 报备汇总信息主页面
     * @return
     */
    @RequestMapping(value="totalMain.do")
    public String totalMain(){
        return COM_PATH +"baobeisys/bbcount/totalMain";
        
    }
    
    /**
     * 饮酒汇总Frame
     * @return
     */
    @RequestMapping(value="yjTotalFrame.do")
    public String yjTotalFrame(){
        request.setAttribute("begin_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("end_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("orgList", bbcountService.getAllOrgList(request));//组织列表
        return COM_PATH +"baobeisys/bbcount/yjTotalFrame";
    }
    
    /**
     * 饮酒汇总List
     * @return
     */
    @RequestMapping(value="yjTotalList.do")
    public String yjTotalList(){
        request.setAttribute("list", bbcountService.getYjTotalList(request));
        return COM_PATH +"baobeisys/bbcount/yjTotalList";
    }
    
    /**
     * 请假汇总Frame
     * @return
     */
    @RequestMapping(value="qjTotalFrame.do")
    public String qjTotalFrame(){
        request.setAttribute("begin_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("end_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("orgList", bbcountService.getAllOrgList(request));//组织列表
        return COM_PATH +"baobeisys/bbcount/qjTotalFrame";
    }
    
    /**
     * 请假汇总List
     * @return
     */
    @RequestMapping(value="qjTotalList.do")
    public String qjTotalList(){
        request.setAttribute("list", bbcountService.getQjTotalList(request));
        return COM_PATH +"baobeisys/bbcount/qjTotalList";
    }
    
    /**
     * 请假汇总Frame
     * @return
     */
    @RequestMapping(value="ycTotalFrame.do")
    public String ycTotalFrame(){
        request.setAttribute("begin_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("end_date", DateHelper.getToday("yyyy-MM-dd"));
        request.setAttribute("orgList", bbcountService.getAllOrgList(request));//组织列表
        return COM_PATH +"baobeisys/bbcount/ycTotalFrame";
    }
    
    /**
     * 请假汇总List
     * @return
     */
    @RequestMapping(value="ycTotalList.do")
    public String ycTotalList(){
        request.setAttribute("list", bbcountService.getYcTotalList(request));
        return COM_PATH +"baobeisys/bbcount/ycTotalList";
    }
    
    
}
