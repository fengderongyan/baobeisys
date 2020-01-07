package weixin.entity;

import java.util.HashMap;
import java.util.Map;

public class ApplyMsg {
	
	private String first;//标题
	private String apply_name;//申请人
	private String apply_date;//申请时间
	private String door_info;//申请房号
	private String apply_price;//申请价格
	//private String remark;//备注
	
	private Map getApplyData;
	
	/**
	 * 描述：获取模板消息中的data
	 * @return
	 * @see weixin.entity.ApplyMsg#getGetApplyData()
	 * @author zhangyongbin
	 */
	public Map getGetApplyData() {
		Map firstMap = new HashMap();
		Map dataMap = new HashMap();
		System.out.println("first:" + first);
		firstMap.put("value", first);
		firstMap.put("color", "#173177");
		dataMap.put("first", firstMap);

		Map applyNameMap = new HashMap();
		applyNameMap.put("value", apply_name);
		applyNameMap.put("color", "#173177");
		dataMap.put("apply_name", applyNameMap);

		Map applyDateMap = new HashMap();
		applyDateMap.put("value", apply_date);
		applyDateMap.put("color", "#173177");
		dataMap.put("apply_date", applyDateMap);
		
		Map doorInfoMap = new HashMap();
		doorInfoMap.put("value", door_info);
		doorInfoMap.put("color", "#173177");
		dataMap.put("door_info", doorInfoMap);
		
		Map applyPriceMap = new HashMap();
		applyPriceMap.put("value", apply_price);
		applyPriceMap.put("color", "#173177");
		dataMap.put("apply_price", applyPriceMap);
		
		return dataMap;
	}
//	public String getRemark() {
//		return remark;
//	}
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getApply_name() {
		return apply_name;
	}
	public void setApply_name(String apply_name) {
		this.apply_name = apply_name;
	}
	public String getApply_date() {
		return apply_date;
	}
	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}
	public String getDoor_info() {
		return door_info;
	}
	public void setDoor_info(String door_info) {
		this.door_info = door_info;
	}
	public String getApply_price() {
		return apply_price;
	}
	public void setApply_price(String apply_price) {
		this.apply_price = apply_price;
	}
	
	
	

}
