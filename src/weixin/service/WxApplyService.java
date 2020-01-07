package weixin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.wxpay.sdk.WXPayUtil;
import com.sgy.util.net.HttpClient;

import web.service.BaseService;

@Service
public class WxApplyService extends BaseService{
	@Value("${weixinpay.appid}")
	private String appId;
	@Value("${weixinpay.appsecret}")
	private String appsecret;
	@Value("${weixinpay.out_trade_no}")
	private String out_trade_no;
	@Value("${weixinpay.mch_id}")
	private String mch_id;
	@Value("${weixinpay.mch_secret}")
	private String mch_secret;
	
	/**
	 * 统一下单
	 * @param openId
	 * @return
	 * @see weixin.WxApplyController#unifiedorder()
	 * @author zhangyongbin
	 */
	public Map unifiedorder(String total_fee, String openid){
		
		try {
			Map<String, String> param = new HashMap<String, String>();
			System.out.println("appId" + appId);
			param.put("appid", appId);//小程序ID
			param.put("mch_id", mch_id);//商户号
			param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
			param.put("body", "test");//商品描述
			param.put("out_trade_no", out_trade_no);//商户订单号
			param.put("total_fee", total_fee);//金额，单位为分
			param.put("spbill_create_ip", "127.0.0.1");//终端IP：无强制要求
			param.put("notify_url", "www.baidu.com");//通知地址：无强制要求
			param.put("trade_type", "JSAPI");//支付类型：JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付，不同trade_type决定了调起支付的方式
			param.put("limit_pay", "no_credit");//指定支付方式：可限制用户不能使用信用卡支付
			param.put("openid", openid);
			logger.debug(mch_secret);
			//生成带签名的xml
			String paramXml = WXPayUtil.generateSignedXml(param, mch_secret);
			//System.out.println(paramXml);
			//调用接口链接(微信小程序提供)
			HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setHttps(true);
			httpClient.setXmlParam(paramXml);
			httpClient.post();
			
			String resultXml = httpClient.getContent();
			Map resultMap = WXPayUtil.xmlToMap(resultXml);
			//返回结果内容重新封装
			Map map = new HashMap();
			if(resultMap != null && resultMap.size() > 0){
				map = new HashMap();
				map.put("prepay_id", resultMap.get("prepay_id"));
				map.put("out_trade_no", out_trade_no);
				map.put("total_fee", total_fee);
				map.put("return_code", resultMap.get("return_code"));
				map.put("result_code", resultMap.get("result_code"));
				map.put("return_msg", resultMap.get("return_msg"));
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 描述：查询订单
	 * @return
	 * @see weixin.WxApplyController#orderquery()
	 * @author zhangyongbin
	 */
	public Map orderquery(){
		
		return null;
	}
}
