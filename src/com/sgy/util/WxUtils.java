package com.sgy.util;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.sgy.util.common.JsonUtil;
import com.sgy.util.common.StringHelper;
import com.sgy.util.net.HttpClient;

import weixin.entity.AccessToken;

public class WxUtils {
	private static AccessToken at;
	private static void getPrivateAccessToken(String url){
		try {
			HttpClient httpClient = new HttpClient(url);
			httpClient.setHttps(true);
			httpClient.get();
			String content = httpClient.getContent();
			Map map = JsonUtil.parseStringMap(content);
			String access_token = StringHelper.get(map, "access_token");
			String expires_in = StringHelper.get(map, "expires_in");
			at = new AccessToken(access_token, expires_in);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 描述：获取access_token
	 * @param url
	 * @return
	 * @author yanbs
	 * @Date : 2019-04-04
	 */
	public static String getAccessToken(String url){
		getPrivateAccessToken(url);
		return at.getAccess_token();
	}
	
	public static String getAccessTokenIfError(String url){
		getPrivateAccessToken(url);
		return at.getAccess_token();
	}
	
	public static void sendWeappTemplateMsg(Map<String, Object> map, String url){
		try {
			JSONObject json = (JSONObject)JSONObject.toJSON(map);
			String jsonStr = json.toString();
			HttpClient httpClient = new HttpClient(url);
			httpClient.setHttps(true);
			httpClient.setXmlParam(jsonStr);
			httpClient.post();
			String resultXml = httpClient.getContent();
			System.out.println("resultXml:" + resultXml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
