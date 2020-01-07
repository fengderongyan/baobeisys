package weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import web.action.BaseController;
import weixin.service.WxLoginService;

/**
 * 描述：判断是否绑定手机号码
 * @author zhangyongbin
 * @Date : 2019年3月12日
 */
@Controller
@RequestMapping(value="/mobile/applet/wxLogin")
public class WxLoginController extends BaseController{
	@Autowired
	private WxLoginService wxLoginService;
	
	/**
	 * 描述：<br>
	 * @param js_code
	 * @return
	 * @see weixin.controller.WxLoginController#getOpenid()
	 * @author zhangyongbin
	 */
	@RequestMapping(value="/getOpenid.do")
	@ResponseBody
	public Map getOpenid(String js_code){
		return wxLoginService.getOpenid(js_code);
	}
	
	/**
	 * 描述：用户手机号与openId绑定
	 * @param openId
	 * @param mobile
	 * @return
	 * @see weixin.controller.WxLoginController#addOpenId()
	 * @author zhangyongbin
	 */
	
	@RequestMapping(value="/addOpenId.do")
	@ResponseBody
	public Map addOpenId(String openid, String mobile){
		//String timestamp = request.getParameter("timestamp");
		//System.out.println("timestamp::" + timestamp);
		if("".equals(openid)){
			Map map = new HashMap();
			map.put("errorCode", 12001);
			map.put("errorInfo", "openid不能为空");
			return map;
		}
		if("".equals(mobile)){
			Map map = new HashMap();
			map.put("errorCode", 12001);
			map.put("errorInfo", "mobile不能为空");
			return map;
		}
		return wxLoginService.addOpenId(openid, mobile);
	}
	
	@RequestMapping(value="/addOpenId2.do")
	public void addOpenId2(HttpServletResponse response){
		String echostr = request.getParameter("echostr");

		System.out.println("echostr:"+echostr);
		PrintWriter p;
		try {
			p = response.getWriter();
			p.print(echostr);
			p.flush();
			p.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
