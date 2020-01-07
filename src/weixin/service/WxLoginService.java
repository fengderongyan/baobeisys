package weixin.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.WXPayUtil;
import com.sgy.util.ResultUtils;
import com.sgy.util.common.JsonUtil;
import com.sgy.util.net.HttpClient;

import web.service.BaseService;
import weixin.entity.WxUserInfo;

@Service
public class WxLoginService extends BaseService{
	
	@Value("${weixinpay.appid}")
	private String appid;
	@Value("${weixinpay.appsecret}")
	private String appsecret;
	@Value("${jscode2session}")
	private String jscode2session; 
	
	
	public Map getOpenid(String js_code){
		
		try {
			System.out.println("js_code:" + js_code);
			String jscode2session_url = jscode2session.replace("APPID", appid).replace("SECRET", appsecret).replace("JSCODE", js_code);
			System.out.println("jscode2session_url:" + jscode2session_url);
			HttpClient httpClient = new HttpClient(jscode2session_url);
			httpClient.setHttps(true);
			httpClient.get();
			String resultXml = httpClient.getContent();
			System.out.println("resultXml:" + resultXml);
			Map resultMap = JsonUtil.parseStringMap(resultXml);
			
			return ResultUtils.returnOk(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtils.returnError(111111, "获取openid异常");
		}
	}
	

	public WxUserInfo getWxUserInfo(String openId) {
		try {
			String sql = "SELECT " +
						 "	id, " +
						 "	openid, " +
						 "	name, " +
						 "	mobile, " +
						 "	STATUS, " +
						 "	role_id, " +
						 "	group_id, " +
						 "	record_id, " +
						 "	DATE_FORMAT(record_date,'%Y-%m-%d %H:%i:%S') record_date, " +
						 "	update_id, " +
						 "	DATE_FORMAT(update_date,'%Y-%m-%d %H:%i:%S') update_date " +
						 "FROM " +
						 "	t_wx_user_info a where openid = ? ";
			Map map = db.queryForMap(sql, new Object[]{openId});
			if(map != null && map.size() > 0){
				WxUserInfo wxUserInfo = new WxUserInfo();
				wxUserInfo.setId(str.get(map, "id"));
				wxUserInfo.setOpenId(openId);
				wxUserInfo.setName(str.get(map, "name"));
				wxUserInfo.setMobile(str.get(map, "mobile"));
				wxUserInfo.setStatus(str.get(map, "status"));
				wxUserInfo.setRoleId(str.get(map, "role_id"));
				wxUserInfo.setGroupId(str.get(map, "group_id"));
				wxUserInfo.setRecordId(str.get(map, "record_id"));
				wxUserInfo.setRecordDate(str.get(map, "record_date"));
				wxUserInfo.setUpdateId(str.get(map, "update_id"));
				wxUserInfo.setUpdateDate(str.get(map, "update_date"));
				return wxUserInfo;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 描述：用户手机号与openId绑定
	 * @param openId
	 * @param mobile
	 * @return
	 * @see weixin.service.WxLoginService#addOpenId()
	 * @author zhangyongbin
	 */
	public Map addOpenId(String openId, String mobile){
		Map map = new HashMap();
		try {
			String sql = " select count(1) from t_wx_user_info where mobile = ? and status = 1";
			String isMobileExists = db.queryForString(sql, new Object[]{mobile});
			logger.debug(str.getSql(sql, new Object[]{mobile}));
			if("0".equals(isMobileExists)){//手机号码不存在
				map.put("errorCode", 11001);
				map.put("errorInfo", "手机号在系统中不存在");
				return map;
			}
			sql = " select count(1) from t_wx_user_info where mobile = ? and openId = ? and status = 1 ";
			String isUserExists = db.queryForString(sql, new Object[]{mobile, openId});
			if("1".equals(isUserExists)){//用户已存在
				map.put("errorCode", 11005);
				map.put("errorInfo", "用户已存在");
				return map;
			}
			System.out.println("mobile::" + mobile);
			sql = " update t_wx_user_info set openId = ? where mobile = ? and status = 1 ";
			logger.debug(str.getSql(sql, new Object[]{openId, mobile}));
			int result = db.update(sql, new Object[]{openId, mobile});
			if(result == 1){
				map.put("errorCode", 0);
				map.put("data", "手机号绑定成功");
				return map;
			}else{
				map.put("errorCode", 15002);
				map.put("errorInfo", "数据库操作失败");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Map erorMap = new HashMap();
			erorMap.put("errorCode", 15002);
			erorMap.put("errorInfo", "数据库操作失败");
			return erorMap;
		}
	}
}
