package weixin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sgy.util.ResultUtils;

import web.action.BaseController;
import weixin.service.BaobeisysService;

@Controller
@RequestMapping("/mobile/applet/baobeisys")
public class BaobeisysController extends BaseController{
	
	@Autowired
	private BaobeisysService baobeisysService;
	
	
	/**
	 * 描述：获取楼栋
	 * @return
	 * @see weixin.controller.DoorSearchController#getBuildList()
	 * @author zhangyongbin
	 * @throws IException 
	 */
	@ExceptionHandler(value = {Exception.class})
	@RequestMapping("/getBuildList.do")
	@ResponseBody
	public Map getBuildList(){
		return baobeisysService.getBuilList();
	}
	
	/**
	 * 描述：获取各个单元的房屋销售信息
	 * @param build_num
	 * @return
	 * @see weixin.controller.DoorSearchController#getUnitList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getUnitList.do")
	@ResponseBody
	public Map getUnitList(String build_num){
		if(StringUtils.isBlank(build_num)){
			Map map = new HashMap();
			map.put("errorCode", 12001);
			map.put("errorInfo", "build_num不能为空");
			return map;
		}
		return baobeisysService.getUnitList(build_num);
	}
	
	/**
	 * 描述：获取房屋销售详情
	 * @param id
	 * @return
	 * @see weixin.controller.baobeisysController#getDoorMapById()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getDoorMapById.do")
	@ResponseBody
	public Map getDoorMapById(HttpServletRequest request, String door_id){
		logger.debug("door_info_id::" + door_id);
		if(StringUtils.isBlank(door_id)){
			return ResultUtils.returnError(12001, "door_info_id不能为空");
		}
		return baobeisysService.getDoorMapById(request, door_id);
	}
	
	/**
	 * 描述：申请价格
	 * @return
	 * @see weixin.controller.baobeisysController#applyPrice()
	 * @author zhangyongbin
	 */
	@RequestMapping("/applyPrice.do")
	@ResponseBody
	public Map applyPrice(HttpServletRequest request, String door_info_id, String apply_price, 
			String customer_name, String customer_mobile, String remark){
		if(StringUtils.isBlank(door_info_id)){
			return ResultUtils.returnError(12001, "door_info_id不能为空");
		}
		if(StringUtils.isBlank(apply_price)){
			return ResultUtils.returnError(12001, "apply_price不能为空");
		}
		if(StringUtils.isBlank(customer_name)){
			return ResultUtils.returnError(12001, "customer_name不能为空");
		}
		if(StringUtils.isBlank(customer_mobile)){
			return ResultUtils.returnError(12001, "customer_mobile不能为空");
		}else if(!StringUtils.isNumeric(customer_mobile)){
			return ResultUtils.returnError(12003, "customer_mobile只能为数字");
		}
		return baobeisysService.applyPrice(request, door_info_id, apply_price, customer_name, customer_mobile, remark);
	}
	
	/**
	 * 描述：个人中心
	 * @param request
	 * @return
	 * @see weixin.controller.baobeisysController#getWxUser()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getWxUser.do")
	@ResponseBody
	public Map getWxUser(HttpServletRequest request){
		return baobeisysService.getUserByOpenId(request);
	}
	
	/**
	 * 描述：我出售的(列表)
	 * @param request
	 * @return
	 * @see weixin.controller.baobeisysController#getMySaleList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getMySaleList.do")
	@ResponseBody
	public Map getMySaleList(HttpServletRequest request){
		return baobeisysService.getMySaleList(request);
	}
	
	/**
	 * 描述：获取房屋销售状态
	 * @param request
	 * @param door_info_id
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-03
	 */
	@RequestMapping("/getSaleStatus.do")
	@ResponseBody
	public Map getSaleStatus(HttpServletRequest request, String door_info_id){
		if(StringUtils.isBlank(door_info_id)){
			return ResultUtils.returnError(12001, "door_info_id不能为空");
		}
		return baobeisysService.getSaleStatus(request, door_info_id);
	}
	
	/**
	 * 描述：我出售的(详情)
	 * @param request
	 * @param door_info_id
	 * @return
	 * @see weixin.controller.baobeisysController#getMySaleInfo()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getMySaleInfo.do")
	@ResponseBody
	public Map getMySaleInfo(HttpServletRequest request, String door_info_id){
		if(StringUtils.isBlank(door_info_id)){
			return ResultUtils.returnError(12001, "door_info_id不能为空");
		}
		return baobeisysService.getMySaleInfo(request, door_info_id);
	}
	
	/**
	 * 描述：我的申请(列表)
	 * @param request
	 * @param apply_status
	 * @return
	 * @see weixin.controller.baobeisysController#getApplyList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getApplyList.do")
	@ResponseBody
	public Map getApplyList(HttpServletRequest request, String apply_status){
		return baobeisysService.getApplyList(request, apply_status);
	}
	
	/**
	 * 描述：我的申请(详情)
	 * @param request
	 * @param apply_id
	 * @return
	 * @see weixin.controller.baobeisysController#getApplyMap()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getApplyMap.do")
	@ResponseBody
	public Map getApplyMap(HttpServletRequest request, String apply_id){
		if(StringUtils.isBlank(apply_id)){
			return ResultUtils.returnError(12001, "apply_id不能为空");
		}
		return baobeisysService.getApplyMap(request, apply_id);
	}
	
	/**
	 * 描述：我的审核(列表)
	 * @param request
	 * @param apply_status
	 * @return
	 * @see weixin.controller.baobeisysController#getApplyList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getDealList.do")
	@ResponseBody
	public Map getDealList(HttpServletRequest request, String deal_status){
		return baobeisysService.getDealList(request, deal_status);
	}
	
	/**
	 * 描述：我的审核(详情)
	 * @param request
	 * @param apply_id
	 * @return
	 * @see weixin.controller.baobeisysController#getApplyMap()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getDealMap.do")
	@ResponseBody
	public Map getDealMap(HttpServletRequest request, String apply_id){
		if(StringUtils.isBlank(apply_id)){
			return ResultUtils.returnError(12001, "apply_id不能为空");
		}
		return baobeisysService.getDealMap(request, apply_id);
	}
	
	/**
	 * 描述：判断该记录是否被处理过，防止重复操作
	 * @param request
	 * @param apply_id
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-04
	 */
	@RequestMapping("/isDeal.do")
	@ResponseBody
	public Map isDeal(HttpServletRequest request, String apply_id){
		return baobeisysService.isDeal(request, apply_id);
	}
	
	/**
	 * 描述：审核申请
	 * @param request
	 * @param apply_id
	 * @return
	 * @see weixin.controller.baobeisysController#dealApply()
	 * @author zhangyongbin
	 */
	@RequestMapping("/dealApply.do")
	@ResponseBody
	public Map dealApply(HttpServletRequest request, String apply_id, String deal_status){
		if(StringUtils.isBlank(apply_id)){
			return ResultUtils.returnError(12001, "apply_id不能为空");
		}
		if(StringUtils.isBlank(deal_status)){
			return ResultUtils.returnError(12001, "deal_status不能为空");
		}
		return baobeisysService.dealApply(request, apply_id, deal_status);
	}
	
	@RequestMapping("/getMyMessageList.do")
	@ResponseBody
	public Map getMyMessageList(HttpServletRequest request){
		return baobeisysService.getMyMessageList(request);
	}
	
	/**
	 * 描述：我的消息详情
	 * @param msg_id
	 * @return
	 * @see weixin.controller.baobeisysController#getMyMessageMap()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getMyMessageMap.do")
	@ResponseBody
	public Map getMyMessageMap(String msg_id){
		if(StringUtils.isBlank(msg_id)){
			return ResultUtils.returnError(12001, "msg_id不能为空");
		}
		return baobeisysService.getMyMessageMap(msg_id);
	}
	
	/**
	 * 描述：获取banner
	 * @return
	 * @see weixin.controller.baobeisysController#getBanerList()
	 * @author zhangyongbin
	 */
	@RequestMapping("/getBanerList.do")
	@ResponseBody
	public Map getBanerList(){
		return baobeisysService.getBanerList();
	}
	
	
	@RequestMapping("/testMsg.do")
	@ResponseBody
	public Map testMsg(){
		baobeisysService.testMsg();
		return null;
	}
	
	/**
	 * 描述：获取小组销售报表
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-04
	 */
	@RequestMapping("/getGroupRpt.do")
	@ResponseBody
	public Map getGroupRpt(HttpServletRequest request){
		return baobeisysService.getGroupRpt(request);
	}
	
	@RequestMapping("/getGroupRptDetail.do")
	@ResponseBody
	public Map getGroupRptDetail(HttpServletRequest request){
		return baobeisysService.getGroupRptDetail(request);
	}
}
