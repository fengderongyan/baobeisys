package com.sgy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultUtils {
	
	/**
	 * 描述：正常返回结果
	 * @param ojb
	 * @return
	 * @see com.sgy.util.ResultUtils#returnOk()
	 * @author zhangyongbin
	 */
	public static Map<String, Object> returnOk(Object obj){
		Map map = new HashMap();
		map.put("errorCode", 0);
		map.put("data", obj);
		return map;
	}
	
	public static Map<String, Object> returnOk(){
		Map map = new HashMap();
		map.put("errorCode", 0);
		map.put("data", "");
		return map;
	}
	
	public static Map<String, Object> returnWebOk(){
		Map map = new HashMap();
		map.put("appcode", 1);
		map.put("appmsg", "操作成功");
		return map;
	}
	

	
	public static Map<String, Object> returnWebError(){
		Map map = new HashMap();
		map.put("appmsg", "操作失败");
		return map;
	}
	
	public static Map<String, Object> returnWebError(String error){
		Map map = new HashMap();
		map.put("appmsg", error);
		return map;
	}
	
	/**
	 * 描述：返回错误信息
	 * @param errorCode
	 * @param errorInfo
	 * @return
	 * @see com.sgy.util.ResultUtils#returnError()
	 * @author zhangyongbin
	 */
	public static Map<String, Object> returnError(int errorCode, String errorInfo){
		Map map = new HashMap();
		map.put("errorCode", errorCode);
		map.put("errorInfo", errorInfo);
		return map;
	}
}
