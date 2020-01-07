package moi.action.yjbb;

import baobeisys.service.login.LoginService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import moi.service.yjbb.YjbbLcSevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.ResultUtils;
import com.sgy.util.common.StringHelper;

import web.action.BaseController;
import web.model.User;

/** 
 * 手机端流程处理
 * @date 2019-1-15
 */
@Controller
@RequestMapping(value = "/mobile/yjbb/")
public class YjbbLcController extends BaseController{

    @Autowired
    YjbbLcSevice yjbbLcSevice;
    
    
    /**
     * 描述：获取报备类型
     * @return
     * @author yanbs
     * @Date : 2019-07-25
     */
    @RequestMapping(value = "getBbType.do")
    @ResponseBody
    public Map getBbType(){
    	List res = yjbbLcSevice.getDDwByType("30002");
    	return ResultUtils.returnOk(res);
    }
    
    /**
     * 描述：获取饮酒类型
     * @return
     * @author yanbs
     * @Date : 2019-07-25
     */
    @RequestMapping(value = "getYjType.do")
    @ResponseBody
    public Map getYjType(){
    	List res = yjbbLcSevice.getDDwByType("30003");
    	return ResultUtils.returnOk(res);
    }
    
    /**
     * 描述：获取请假类型
     * @return
     * @author yanbs
     * @Date : 2019-07-25
     */
    @RequestMapping(value = "getQjType.do")
    @ResponseBody
    public Map getQjType(){
    	List res = yjbbLcSevice.getDDwByType("30004");
    	return ResultUtils.returnOk(res);
    }
    
    /**
     * 描述：获取用车原因
     * @return
     * @author yanbs
     * @Date : 2019-08-05
     */
    @RequestMapping(value = "getYcReason.do")
    @ResponseBody
    public Map getYcReason(){
    	List res = yjbbLcSevice.getDDwByType("30005");
    	return ResultUtils.returnOk(res);
    }
    
    /**
     * 描述：获取所用车型
     * @return
     * @author yanbs
     * @Date : 2019-08-05
     */
    @RequestMapping(value = "getCarType.do")
    @ResponseBody
    public Map getCarType(){
    	List res = yjbbLcSevice.getDDwByType("30006");
    	return ResultUtils.returnOk(res);
    }
    
    /**
     * 描述：饮酒立即报备
     * @return
     * @author yanbs
     * @Date : 2019-07-25
     */
    @RequestMapping(value = "insertYjbbApply.do")
    @ResponseBody
    public Map insertYjbbApply(){
    	int res = yjbbLcSevice.insertYjbbApply(request);
    	if(res == -2){
    		return ResultUtils.returnError(12002, "您还没有配置审核人，不能报备");
    	}else if(res == 1){
    		return ResultUtils.returnOk();
    	}else{
    		return ResultUtils.returnError(-1, "后台请求失败");
    	}
    }
    
    /**
     * 描述：请假报备
     * @return
     * @author yanbs
     * @Date : 2019-08-01
     */
    @RequestMapping(value = "insertQjbbApply.do")
    @ResponseBody
    public Map insertQjbbApply(){
    	int res = yjbbLcSevice.insertQjbbApply(request);
    	if(res == -2){
    		return ResultUtils.returnError(12002, "您还没有配置审核人，不能报备");
    	}else if(res == 1){
    		return ResultUtils.returnOk();
    	}else{
    		return ResultUtils.returnError(-1, "后台请求失败");
    	}
    }
    
    /**
     * 描述：用车报备
     * @return
     * @author yanbs
     * @Date : 2019-08-01
     */
    @RequestMapping(value = "insertYcbbApply.do")
    @ResponseBody
    public Map insertYcbbApply(){
    	int res = yjbbLcSevice.insertYcbbApply(request);
    	if(res == -2){
    		return ResultUtils.returnError(12002, "您还没有配置审核人，不能报备");
    	}else if(res == 1){
    		return ResultUtils.returnOk();
    	}else{
    		return ResultUtils.returnError(-1, "后台请求失败");
    	}
    }
    
    /**
     * 描述：获取审核列表
     * @return
     * @author yanbs
     * @Date : 2019-07-25
     */
    @RequestMapping(value = "getDealList.do")
    @ResponseBody
    public Map getDealList(){
    	String bb_type = req.getValue(request, "bb_type");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		if("".equals(bb_type)){
			return ResultUtils.returnError(12001, "bb_type不能为空");
		}
		if("".equals(pageNum)){
			return ResultUtils.returnError(12001, "pageNum不能为空");
		}
		if("".equals(pageSize)){
			return ResultUtils.returnError(12001, "pageSize不能为空");
		}
    	List res = yjbbLcSevice.getDealList(request);
    	return ResultUtils.returnOk(res);
    }
    
    /**
     * 描述：获取审核详情
     * @return
     * @author yanbs
     * @Date : 2019-08-01
     */
    @RequestMapping(value = "getDealInfo.do")
    @ResponseBody
    public Map getDealInfo(){
    	String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	if("".equals(bb_type)){
			return ResultUtils.returnError(12001, "bb_type不能为空");
		}
    	if("".equals(apply_id)){
			return ResultUtils.returnError(12001, "apply_id不能为空");
		}
    	Map dealInfo = yjbbLcSevice.getDealInfo(request);
    	String apply_status = str.get(dealInfo, "apply_status");
    	String deal_lev = str.get(dealInfo, "deal_lev");
    	String bbr = str.get(dealInfo, "bbr");
    	List dealedList = yjbbLcSevice.getDealedList(request, apply_status, deal_lev, bbr);//获取该信息已审核列表
    	dealInfo.put("dealedList", dealedList);
    	return ResultUtils.returnOk(dealInfo);
    }
    
    /**
     * 描述：处理审核信息
     * @return
     * @author yanbs
     * @Date : 2019-08-01
     */
    @RequestMapping(value = "dealApplyInfo.do")
    @ResponseBody
    public Map dealApplyInfo(){
    	String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	String deal_status = req.getValue(request, "deal_status");
    	String deal_lev = req.getValue(request, "deal_lev");
    	String reject_reason = req.getValue(request, "reject_reason");
    	String bbr = req.getValue(request, "bbr");
    	if("".equals(bb_type)){
			return ResultUtils.returnError(12001, "bb_type不能为空");
		}
    	if("".equals(apply_id)){
			return ResultUtils.returnError(12001, "apply_id不能为空");
		}
    	if("".equals(deal_status)){
			return ResultUtils.returnError(12001, "deal_status不能为空");
		}
    	if("".equals(deal_lev)){
			return ResultUtils.returnError(12001, "deal_lev不能为空");
		}
    	if("".equals(bbr)){
    		return ResultUtils.returnError(12001, "bbr不能为空");
    	}
    	if("0".equals(deal_status) && "".equals(reject_reason)){
    		return ResultUtils.returnError(12001, "驳回时，reject_reason不能为空");
    	}
    	int res = yjbbLcSevice.dealApplyInfo(request);
    	if(res == -2){
    		return ResultUtils.returnError(12002, "该申请已被处理");
    	}else if(res == 1){
    		return ResultUtils.returnOk();
    	}else{
    		return ResultUtils.returnError(-1, "后台请求失败");
    	}
    	
    }
    
    /**
     * 描述：批量通过处理
     * @return
     * @author yanbs
     * @Date : 2019-08-06
     */
    @RequestMapping(value = "dealApplyInfos.do")
    @ResponseBody
    public Map dealApplyInfos(){
    	String applyIdStr = req.getValuesString(request, "apply_ids");
    	if("".equals(applyIdStr)){
    		return ResultUtils.returnError(12001, "apply_ids不能为空");
    	}
    	String bbrStr = req.getValuesString(request, "bbrs");
    	if("".equals(bbrStr)){
    		return ResultUtils.returnError(12001, "bbrs不能为空");
    	}
    	String deal_lev = req.getValue(request, "deal_lev");
    	if("".equals(deal_lev)){
    		return ResultUtils.returnError(12001, "deal_lev不能为空");
    	}
    	String bb_type = req.getValue(request, "bb_type");
    	if("".equals(bb_type)){
    		return ResultUtils.returnError(12001, "bb_type不能为空");
    	}
    	
    	String[] applyIds = applyIdStr.split(",");
    	String[] bbrs = bbrStr.split(",");
    	for (int i = 0; i < applyIds.length; i++) {
			String apply_id = applyIds[i];
			String bbr = bbrs[i];
			yjbbLcSevice.dealApplyInfo(request,bb_type, apply_id, bbr, "1", deal_lev);
		}
    	return ResultUtils.returnOk();
    }
    
    /**
     * 描述：获取等待审核列表
     * @return
     * @author yanbs
     * @Date : 2019-08-06
     */
    @RequestMapping(value = "getApplyList.do")
    @ResponseBody
    public Map getApplyList(){
    	String bb_type = req.getValue(request, "bb_type");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		if("".equals(bb_type)){
    		return ResultUtils.returnError(12001, "bb_type不能为空");
    	}
		if("".equals(pageNum)){
    		return ResultUtils.returnError(12001, "pageNum不能为空");
    	}
		if("".equals(pageSize)){
    		return ResultUtils.returnError(12001, "pageSize不能为空");
    	}
		List applyList = yjbbLcSevice.getApplyList(request);
		return ResultUtils.returnOk(applyList);
    }
    
    /**
     * 描述：在审核详情中点击归家/归队
     * @return
     * @author yanbs
     * @Date : 2019-08-07
     */
    @RequestMapping(value = "doFinishApply.do")
    @ResponseBody
    public Map doFinishApply(){
    	String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	if("".equals(bb_type)){
    		return ResultUtils.returnError(12001, "bb_type不能为空");
    	}
    	if("".equals(apply_id)){
    		return ResultUtils.returnError(12001, "apply_id不能为空");
    	}
    	int res = yjbbLcSevice.doFinishApply(request);
    	if(res == 1){
    		return ResultUtils.returnOk();
    	}else{
    		return ResultUtils.returnError(-1, "后台请求失败");
    	}
    }
    
    
    /**
     * 描述：获取已结束列表
     * @return
     * @author yanbs
     * @Date : 2019-08-06
     */
    @RequestMapping(value = "getFinishApplyList.do")
    @ResponseBody
    public Map getFinishApplyList(){
    	String bb_type = req.getValue(request, "bb_type");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		if("".equals(bb_type)){
    		return ResultUtils.returnError(12001, "bb_type不能为空");
    	}
		if("".equals(pageNum)){
    		return ResultUtils.returnError(12001, "pageNum不能为空");
    	}
		if("".equals(pageSize)){
    		return ResultUtils.returnError(12001, "pageSize不能为空");
    	}
		List applyList = yjbbLcSevice.getFinishApplyList(request);
		return ResultUtils.returnOk(applyList);
    }
    
    
    /**
     * 描述：获取组织列表
     * @return
     * @author yanbs
     * @Date : 2019-08-06
     */
    @RequestMapping(value = "getOrgList.do")
    @ResponseBody
    public Map getOrgList(){
    	List orgList = yjbbLcSevice.getOrgList(request);
		return ResultUtils.returnOk(orgList);
    }
    
    /**
     * 描述：查看全部
     * @return
     * @author yanbs
     * @Date : 2019-08-06
     */
    @RequestMapping(value = "getAllList.do")
    @ResponseBody
    public Map getAllList(){
    	String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		String bb_type = req.getValue(request, "bb_type");
		if("".equals(pageNum)){
    		return ResultUtils.returnError(12001, "pageNum不能为空");
    	}
		if("".equals(pageSize)){
    		return ResultUtils.returnError(12001, "pageSize不能为空");
    	}
		if("".equals(bb_type)){
    		return ResultUtils.returnError(12001, "bb_type不能为空");
    	}
    	List allList = yjbbLcSevice.getAllList(request);
		return ResultUtils.returnOk(allList);
    }
    
    
    /**
     * 描述：获取消息列表
     * @return
     * @author yanbs
     * @Date : 2019-08-08
     */
    @RequestMapping(value = "getPushList.do")
    @ResponseBody
    public Map getPushList(){
    	String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		if("".equals(pageNum)){
    		return ResultUtils.returnError(12001, "pageNum不能为空");
    	}
		if("".equals(pageSize)){
    		return ResultUtils.returnError(12001, "pageSize不能为空");
    	}
    	List pushList = yjbbLcSevice.getPushList(request);
    	return ResultUtils.returnOk(pushList);
    }
    
    /**
     * 描述：获取消息详情
     * @return
     * @author yanbs
     * @Date : 2019-08-08
     */
    @RequestMapping(value = "getPushInfo.do")
    @ResponseBody
    public Map getPushInfo(){
    	String id = req.getValue(request, "id");
		if("".equals(id)){
    		return ResultUtils.returnError(12001, "id不能为空");
    	}
    	Map pushInfo = yjbbLcSevice.getPushInfo(request);
    	
    	return ResultUtils.returnOk(pushInfo);
    }
}
