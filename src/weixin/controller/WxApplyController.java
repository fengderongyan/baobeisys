package weixin.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.wxpay.sdk.WXPayUtil;
import com.sgy.util.net.HttpClient;
import com.sgy.util.net.HttpHelper;

import web.action.BaseController;
import weixin.service.WxApplyService;

@Controller
@RequestMapping(value="/mobile/applet/wxapply")
public class WxApplyController extends BaseController{
	
	@Autowired
	private WxApplyService wxApplyService;
	
	
	/**
	 * 描述：<br>
	 * @param out_trade_no 商户订单号
	 * @param mch_id 商户号
	 * @param total_fee 金额
	 * @param openid 
	 * @return
	 * @see weixin.controller.WxApplyController#unifiedorder()
	 * @author zhangyongbin
	 */
	@RequestMapping(value="/unifiedorder.do")
	@ResponseBody
	public Map unifiedorder(String total_fee, String openid){
		total_fee = "1";
		openid = "oJGq15O1sp39Y-A_bT-8VUhte3Sk";
		Map map = wxApplyService.unifiedorder(total_fee, openid);
		return map;
	}
	
	
	
}
