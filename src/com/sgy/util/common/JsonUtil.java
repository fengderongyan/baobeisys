package com.sgy.util.common;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	
	/**
	 * 
	 * @param 普通MAP {"NAME":"张三","AGE":12}
	 * @return
	 */
	public static Map parseStringMap(String jsonData)
	{
		Type mapType = new TypeToken<Map<String,String>>(){}.getType();
		Gson gson = new Gson();
		Map map = gson.fromJson(jsonData, mapType);
		return map;
	}
	
	/**
	 * 
	 * @param [{"TEMPLATE_NAME":"覆盖验收模板","TEMPLATE_ID":100},{"TEMPLATE_NAME":"工维优化模板","TEMPLATE_ID":101},{"TEMPLATE_NAME":"日常测试模板","TEMPLATE_ID":102}]
	 * @return
	 */
	public static List parseMapList(String jsonData)
	{
		Type mapType = new TypeToken<List<Map<String,String>>>(){}.getType();
		Gson gson = new Gson();
		List list = gson.fromJson(jsonData, mapType);
		return list;
	}
	
	
	public static void main(String[] args)
	{
		//String jsonData="{\"NAME\":\"1\",\"AGE\":\"1\" }";
		String json="[{\"TEMPLATE_NAME\":\"覆盖验收模板\",\"TEMPLATE_ID\":100},{\"TEMPLATE_NAME\":\"工维优化模板\",\"TEMPLATE_ID\":101},{\"TEMPLATE_NAME\":\"日常测试模板\",\"TEMPLATE_ID\":102}]";
		List list=parseMapList(json);
	
		System.out.println(list);
	}
	
	public static String list2Json(List<Map<String,String>> list)
	{
		//Type mapType = new TypeToken<Map<String,String>>(){}.getType();
		Gson gson = new Gson();
		String result=gson.toJson(list);
		return result;
	}
	
	public static String  map2Json(Map<String,String> map)
	{
		//Type mapType = new TypeToken<Map<String,String>>(){}.getType();
		Gson gson = new Gson();
		String result=gson.toJson(map);
		return result;
	}
	
	public static String object2Json(Object object)
	{
		Gson gson = new Gson();
		String result=gson.toJson(object);
		return result;
	}
	
}
