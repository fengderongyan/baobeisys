package com.sgy.util.location;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.EncodingUtil;

import com.sgy.util.common.StringHelper;

public class HttpUtil {
	
	// 每次创建一个新的HttpClient请求
	private boolean createNewHttpClientEveryTime = false;
	
	// 最大HttpClient重复请求数
	private int maximumRepeatHttpClientRequestNum = 2;
	
	public static HttpClient httpClient; 
 
	public HttpUtil() {}


	public HttpUtil(boolean createNewHttpClientEveryTime) {
		this.createNewHttpClientEveryTime = createNewHttpClientEveryTime;
	}
	
	public synchronized HttpClient getHttpClient() {
		try{
			if (createNewHttpClientEveryTime || null == this.httpClient) {
	            HttpClient httpClient = null;
				httpClient = new HttpClient();
				// 设置字符集
				httpClient.getParams().setContentCharset("UTF-8");
				// Socket连接超时
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
				// 请求上，返回超时时间
				httpClient.getHttpConnectionManager().getParams().setSoTimeout(15000);
				// 设置最大连接数
				httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(20);
				// 设置最大活动连接,默认20
				httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(40);
	
				//httpClient.getParams().setParameter("http.socket.timeout", 60000);
				//httpClient.setTimeout(5*1000);
				//httpClient.setConnectionTimeout(5*1000);
				
				// 连接超时
				httpClient.getParams().setConnectionManagerTimeout(30000);
				if(!createNewHttpClientEveryTime) {
				    this.httpClient = httpClient;
				}
		        return httpClient;
			}
		}catch (Exception e) {
		    System.out.println("getHttpClient error："+e.getMessage());
		}
		return this.httpClient;
	}
 
	public static NameValuePair[] convertMapToNameValuePairs(
			Map<String, String> params) {
		if (params != null) {
			Set<String> keys = params.keySet();
			NameValuePair[] pairs = new NameValuePair[keys.size()];
			Iterator<String> it = keys.iterator();
			int i = 0;
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				try {
					pairs[i++] = new NameValuePair(key, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return pairs;
		}
		return null;
	}

//	public static Map<String, String> decodeByDecodeNames(
//			List<String> decodeNames, Map<String, String> map) {
//		Set<String> keys = map.keySet();
//		Iterator<String> it = keys.iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			String value = map.get(key);
//			for (String decodeName : decodeNames) {
//				if (key.equals(decodeName)) {
//					value = URLDecoder.decode(value);
//					map.put(key, value);
//				}
//			}
//		}
//		return map;
//	}

	public static String buildQueryString(Map<String, String> map) {
		String queryString = "";
		if(map == null){
			return queryString;
		}
		try{
			NameValuePair[] pairs = convertMapToNameValuePairs(map);
			queryString = EncodingUtil.formUrlEncode(pairs, "UTF-8"); 
			return queryString;
		}catch (Exception e) {
		}
		return queryString;
	}

	public static String buildUrlByQueryStringAndBaseUrl(String baseUrl,
			String queryString) {
		return baseUrl + queryString;
	}

	public static String buildUrlByQueryStringMapAndBaseUrl(String baseUrl,
			Map<String, String> map) {
		return buildUrlByQueryStringAndBaseUrl(baseUrl, buildQueryString(map));
	}

	/**
	 * @param url 请求的URL
	 * @param params 参数
	 * @return
	 */
	public String doGet(String url, Map<String, String> params) {
		String result = null;
		GetMethod getMethod = null ;
		int status = -1;      // 请求状态
		int loopCount = 0;    // 循环次数
		while(status == -1 && loopCount < maximumRepeatHttpClientRequestNum){
            loopCount ++;
    		try {
		        url = HttpUtil.buildUrlByQueryStringMapAndBaseUrl(url, params);
	            getMethod = new GetMethod(url);
	            HttpClient httpclient = getHttpClient();
	            getMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
	            status = httpclient.executeMethod(getMethod);
	            if (status != HttpStatus.SC_OK) {
	                result = null;
	                status = -1;
	            } else {
	                status = 1;
	                result = new String(getMethod.getResponseBody(), getMethod.getResponseCharSet());
	            }
    		} catch (Exception e) {
    		    status = -1;
    		    System.out.println("error："+e.getMessage());
    		} finally {
    			try{
    			    // 释放连接
    				getMethod.abort();
    				getMethod.releaseConnection();
    			}catch (Exception e){}
    		}
		}
		return result;
	}

	public String doGet(String url) {
		return doGet(url, null);
	}

	public String doPost(String url, Map<String, String> params) {
		String result = null;
		PostMethod postMethod =  null ;
		int status = -1;
		try {
			postMethod = new PostMethod(url);
			NameValuePair[] pairs = convertMapToNameValuePairs(params);
			HttpClient httpclient = getHttpClient();
			postMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			postMethod.setRequestBody(pairs);
			status = httpclient.executeMethod(postMethod);
			if (status != HttpStatus.SC_OK) {
			    status = -1;
			} else {
				result = new String(postMethod.getResponseBody(), postMethod.getResponseCharSet());
			}
		} catch (Exception e) {
			System.out.println("error："+e.getMessage());
		} finally {
			try{
				postMethod.abort();
				// 释放连接
				postMethod.releaseConnection();
			}catch (Exception e) {
				System.out.println("#最终释放链接时异常："+e.getMessage());
			}
		}
		return result;

	}

	public String doPost(String url) {
		return doPost(url, null);
	}
	
	public static void main(String[] args) {
		HttpUtil httpUtil = new HttpUtil();
		//http://ttlbs.gnway.net/lbsapi.aspx?type=0&lac=25106&cell=44761
		//http://lbs.muyoukeji.com/getmarker?lac=25106&cellid=44761
		//http://v.juhe.cn/sdk/index?key=bcb6c9293bb72e7b5ca2bd5654afa06d&lac=25106&cell=44761&coor=1
		//String result = httpUtil.doGet("http://v.juhe.cn/sdk/index?key=bcb6c9293bb72e7b5ca2bd5654afa06d&lac=25106&cell=44761&coor=1");
		String result = httpUtil.doGet("http://113.4.17.107:7005/RealWorldMohurd/lbsMgmt/otherSystemLbsLocate.do?serviceId=PQQNz3iuvbgvMsRVpttgVQ==&mobile=13814384038");
		//httpUtil.parseResult(result);
		/*int suc = 0;
		for (int i = 0; i < 100; i++) {
			String result = httpUtil.doGet("http://v.juhe.cn/sdk/index?key=bcb6c9293bb72e7b5ca2bd5654afa06d&lac=25106&cell=44761&coor=1");
			Map res = httpUtil.parseResult(result);
			if (StringHelper.get(res, "resultcode").equals("2000")) {
				suc ++;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				
			}
		}
		System.out.println("suc:"+suc);*/
	}
	
	public Map parseResult(String result){
		JSONObject jsonObject = JSONObject.fromObject(result); 
        Map map = new HashMap(); 
        for(Iterator iter = jsonObject.keys(); iter.hasNext();){ 
            String key = (String)iter.next(); 
            map.put(key, jsonObject.get(key)); 
        } 
        String resultCode = StringHelper.get(map, "resultcode");
        if (resultCode.equals("2000")) {//请求成功，正常获取到信息
			String reqRes = StringHelper.get(map, "result");
			jsonObject = JSONObject.fromObject(reqRes); 
	        Map resMap = new HashMap(); 
	        for(Iterator iter = jsonObject.keys(); iter.hasNext();){ 
	            String key = (String)iter.next(); 
	            resMap.put(key, jsonObject.get(key)); 
	        } 
	        resMap.put("resultcode", "2000"); 
	        return resMap;
		} else {
			return map;
		}
	}
}
