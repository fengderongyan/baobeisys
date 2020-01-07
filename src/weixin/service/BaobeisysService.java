package weixin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sgy.util.ResultUtils;
import com.sgy.util.WxUtils;
import com.sgy.util.db.BatchSql;

import web.service.BaseService;
import weixin.entity.ApplyMsg;
import weixin.entity.WxUserInfo;

@Service
public class BaobeisysService extends BaseService{
	
	@Value("${weixinpay.fwappid}")
	private String fwappid;
	@Value("${weixinpay.fwappsecret}")
	private String fwappsecret;
	@Value("${access_token_url}")
	private String access_token_url; 
	@Value("${send_template_msg}")
	private String send_template_msg;
	
	public Map getBuilList(){
		Map map = new HashMap();
		String sql = " select build_num, sale_status from t_wx_build_sale_status a order by build_num ";
		List list = db.queryForList(sql);
		map.put("errorCode", 0);
		map.put("data", list);
		return map;

	}
	
	public Map getUnitList(String build_num){
		String sql = " select CONCAT(sale_status,'') sale_status from t_wx_build_sale_status where build_num = ? ";
		String sale_status = db.queryForString(sql, new Object[]{build_num});
		if("0".equals(sale_status)){
			return ResultUtils.returnError(14001, "暂未出售");
		}
		if("2".equals(sale_status)){
			return ResultUtils.returnError(14002, "已售罄");
		}
		
	    sql = " select distinct unit_num from t_wx_door_info where build_num = ? order by unit_num ";
		List tempList = db.queryForList(sql, new Object[]{build_num});
		List unitList = new ArrayList();
		for(int i = 0;i < tempList.size(); i++){
			Map unitMap = (Map)tempList.get(i);
			Map tempMap = new HashMap();
			String unit_num = str.get(unitMap, "unit_num");
			String doorSql = " select id, door_num, status from t_wx_door_info where build_num = ? and  unit_num = ? order by door_num desc";
			logger.debug(str.getSql(doorSql, new Object[]{build_num, unit_num}));
			List tempList2 = db.queryForList(doorSql, new Object[]{build_num, unit_num});
			List doorList = new ArrayList();
			for(int j = 0; j < tempList2.size(); j++){
				Map doorMap = (Map)tempList2.get(j);
				doorList.add(doorMap);
			}
			tempMap.put("unit_num", unit_num);
			tempMap.put("door_list", doorList);
			unitList.add(tempMap);
		}
		
		Map resultMap = new HashMap();
		resultMap.put("errorCode", 0);
		resultMap.put("data", unitList);
		return resultMap;
	}
	
	/**
	 * 描述：获取房屋销售详情
	 * @param request
	 * @param id
	 * @return
	 * @see weixin.service.baobeisysService#getDoorMapById()
	 * @author zhangyongbin
	 */
	public Map getDoorMapById(HttpServletRequest request, String door_info_id){
		String openid = this.getWxUserInfo(request).getOpenId();
		String sql = "SELECT" +
				"	id door_info_id," +
				"	build_num," +
				"	unit_num," +
				"	area," +
				"	concat(door_floor, '/', total_floor) door_floor," +
				"	door_num," +
				"	door_type," +
				"	price," +
				"	total_price," +
				"	direction," +
				"	status," +
				"	door_imgs," +
				"	remark, " + 
				"   real_price " +
				"FROM " +
				"	t_wx_door_info a where id = ? ";
		return db.queryForMap(sql, new Object[]{door_info_id});
	}
	
	/*public Map getDoorMapById(HttpServletRequest request, String door_info_id){
		
		String openid = this.getWxUserInfo(request).getOpenId();
		String sql = "SELECT" +
					"	id door_info_id," +
					"	build_num," +
					"	unit_num," +
					"	area," +
					"	concat(door_floor, '/', total_floor) door_floor," +
					"	door_num," +
					"	door_type," +
					"	price," +
					"	total_price," +
					"	direction," +
					"	status," +
					"	door_imgs," +
					"	remark, " + 
					"   real_price, "
					+ " (select case when count(1) >= 1 then 1 else 0 end "
					+ "   from t_wx_apply_info b "
					+ "  where b.openid = ? and b.door_info_id = a.id and apply_status = 3) is_my_apply " +
					"FROM " +
					"	t_wx_door_info a where id = ? ";
		logger.debug(str.getSql(sql, new Object[]{openid, door_info_id}));
		Map tempMap = db.queryForMap(sql, new Object[]{openid, door_info_id});
		String is_my_apply = str.get(tempMap, "is_my_apply");//自己是否申请过
		String status = str.get(tempMap, "status");//房屋销售状态
		if(SALE_STATUS_ORDERING.equals(status)){//预定中
			return ResultUtils.returnError(14001, "该房屋已被预订");
		}
		if(SALE_STATUS_SALED.equals(status)){//已销售
			return ResultUtils.returnError(14001, "该房屋已售出");
		}
		
		if("1".equals(is_my_apply)){//只能查看自己的申请 apply_status：3领导已同意
			String applySql = " select apply_price, customer_name, customer_mobile, remark from t_wx_apply_info a "
					+ " where a.openid = ? and door_info_id = ? and apply_status = 3 order by apply_date desc ";
			List applyList = db.queryForList(applySql, new Object[]{openid, door_info_id});
			tempMap.put("applyList", applyList);
			
		} 
		
		return ResultUtils.returnOk(tempMap);
	}*/
	
	/**
	 * 描述：获取房屋销售状态
	 * @param request
	 * @param door_info_id
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-03
	 */
	public Map getSaleStatus(HttpServletRequest request, String door_info_id){
		String statusSql = " select status from t_wx_door_info where id = ? ";
		String status = db.queryForString(statusSql, new Object[]{door_info_id});
		return ResultUtils.returnOk(status);
	}
	
	/**
	 * 描述：申请价格
	 * @param request
	 * @param door_info_id
	 * @param apply_price
	 * @param customer_name
	 * @param customer_mobile
	 * @param remark
	 * @return
	 * @see weixin.service.baobeisysService#applyPrice()
	 * @author zhangyongbin
	 */
	public Map applyPrice(HttpServletRequest request, String door_info_id, String apply_price, String customer_name, String customer_mobile, String remark){
		String openid = this.getWxUserInfo(request).getOpenId();
		String group_id = this.getWxUserInfo(request).getGroupId();
		BatchSql batchSql = new BatchSql();
		String statusSql = " select concat(status, '') status, concat(ifnull(min_price, 0), '') min_price from t_wx_door_info where id = ? ";
		Map doorInfo = db.queryForMap(statusSql, new Object[]{door_info_id});
		String min_price = (String)doorInfo.get("min_price");
		String status = (String)doorInfo.get("status");
		if(!"0".equals(status)){
			return ResultUtils.returnError(14001, "您无法申请该房间");
		}
		
		Double minPrice = Double.parseDouble(min_price);
		Double applyPrice = Double.parseDouble(apply_price);//申请价
		String flag_boss_deal = "0";
		System.out.println("applyPrice < minPrice：：：" + applyPrice);
		System.out.println("applyPrice < minPrice：：：" + minPrice);
		System.out.println("applyPrice < minPrice：：：" + (applyPrice < minPrice));
		if(applyPrice < minPrice){//申请价格低于最低销售价
			flag_boss_deal = "1";
		}
		String sql = "INSERT INTO t_wx_apply_info ( " +
				"	openid, " +
				"	group_id, " +
				"	door_info_id, " +
				"	apply_status, " +
				"	apply_price, " +
				"	apply_date, " +
				"	customer_name, " +
				"	customer_mobile, " +
				"	remark, " +
				"	flag_boss_deal " +
				") " +
				"VALUES " +
				"	(?, ?, ?, 1 ,?, SYSDATE() ,?,?,?,?) ";
		//logger.debug(str.getSql(sql, new Object[]{openid, door_info_id, apply_price, customer_name, customer_mobile, remark}));
		//int result = db.update(sql, new Object[]{openid, group_id, door_info_id, apply_price, customer_name, customer_mobile, remark,flag_boss_deal});
		batchSql.addBatch(sql, new Object[]{openid, group_id, door_info_id, apply_price, customer_name, customer_mobile, remark,flag_boss_deal});
		//申请后立马改变状态为预订中
		sql = "UPDATE t_wx_door_info a " +
				" SET STATUS = 1 " +
				" WHERE  a.id = ? " +
				" AND a.`status` = 0 ";
		batchSql.addBatch(sql, new Object[]{door_info_id});
		int result = db.doInTransaction(batchSql);
		if(result == 1){
			return ResultUtils.returnOk(null);
		}else{
			return ResultUtils.returnError(13001, "数据保存失败");
		}
		
	}
	
	/*public Map applyPrice(HttpServletRequest request, String door_info_id, String apply_price, String customer_name, String customer_mobile, String remark){
		String openid = this.getWxUserInfo(request).getOpenId();
		String statusSql = " select status from t_wx_door_info where id = ? ";
		String status = db.queryForString(statusSql, new Object[]{door_info_id});
		if(!SALE_STATUS_SALED.equals(status)){
			String sql = "INSERT INTO t_wx_apply_info ( " +
						"	openid, " +
						"	door_info_id, " +
						"	apply_status, " +
						"	apply_price, " +
						"	apply_date, " +
						"	customer_name, " +
						"	customer_mobile, " +
						"	remark " +
						") " +
						"VALUES " +
						"	(?,?, 1 ,?, SYSDATE() ,?,?,?) ";
			//logger.debug(str.getSql(sql, new Object[]{openid, door_info_id, apply_price, customer_name, customer_mobile, remark}));
			int result = db.update(sql, new Object[]{openid, door_info_id, apply_price, customer_name, customer_mobile, remark});
			if(result == 1){
				return ResultUtils.returnOk(null);
			}else{
				return ResultUtils.returnError(13001, "数据保存失败");
			}
		}else{//已销售
			return ResultUtils.returnError(14001, "该房屋已售出");
		}
	}*/
	
	public Map getUserByOpenId(HttpServletRequest request){
		WxUserInfo wxUserInfo = this.getWxUserInfo(request);
		Map userMap = new HashMap();
		userMap.put("name", wxUserInfo.getName());
		userMap.put("role_id", wxUserInfo.getRoleId());
		Map resultMap = new HashMap();
		resultMap.put("errorCode", 0);
		resultMap.put("data", userMap);
		return resultMap;
	}
	
	public Map getMySaleList(HttpServletRequest request){
		String openid = this.getWxUserInfo(request).getOpenId();
		String sql = "SELECT " +
					"  id door_info_id, "+
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info " +
					"FROM " +
					"	t_wx_door_info " +
					"WHERE sale_openid = ? " +
					"  and status = ? "
					+ " order by sale_date desc ";
		List saleList = db.queryForList(sql, new Object[]{openid, this.SALE_STATUS_SALED});
		return ResultUtils.returnOk(saleList);
	}
	
	public Map getMySaleInfo(HttpServletRequest request, String door_info_id){
		String sql = "SELECT " +
					"	a.id door_info_id," +
					"	a.build_num," +
					"	a.unit_num," +
					"	a.area," +
					"	a.door_floor," +
					"	a.door_num," +
					"	a.door_type," +
					"	a.price," +
					"	a.total_price," +
					"	a.direction," +
					"	a.status," +
					"	a.door_imgs," +
					"	a.remark, " + 
					"	DATE_FORMAT(b.record_date,'%Y-%m-%d %H:%i:%S') sale_date, " +
					"   b.price sale_price, " +
				    "   b.customer_mobile, " +
					"	b.customer_name " +
					"FROM " +
					"	t_wx_door_info a, " +
					"	t_wx_sale_print_info b " +
					"WHERE " +
					"  a.id = ? " +
					"  and a.sale_print_info_id = b.id " ;
		Map map = db.queryForMap(sql, new Object[]{door_info_id});
		return ResultUtils.returnOk(map);
	}
	
	/**
	 * 描述：我的申请（列表）
	 * @param request
	 * @param apply_status
	 * @return
	 * @see weixin.service.baobeisysService#getApplyList()
	 * @author zhangyongbin
	 */
	public Map getApplyList(HttpServletRequest request, String apply_status){
		String openid = this.getWxUserInfo(request).getOpenId();
		String applyName = this.getWxUserInfo(request).getName();
		String sql = "SELECT " +
					"	a.id apply_id, " +
					"	'" + applyName + "' apply_name, " +
					"	DATE_FORMAT(a.apply_date,'%Y-%m-%d %H:%i:%S') apply_date, " +
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info, " +
					"	b.id door_info_id, " +
					"	b.area, " +
					"	b.price, " +
					"	a.apply_price, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"(select deal_status from t_wx_apply_deal t where a.id = t.apply_id and t.deal_role = 1)  boss_deal_status, " +
					"(select deal_name from t_wx_apply_deal t where a.id = t.apply_id and t.deal_role = 1) boss_deal_name, " +
					"(select DATE_FORMAT(t.deal_date,'%Y-%m-%d %H:%i:%S') deal_date from t_wx_apply_deal t where a.id = t.apply_id and t.deal_role = 1) boss_deal_date, " +
					"(select deal_status from t_wx_apply_deal t where a.id = t.apply_id and t.deal_role = 2)  manage_deal_status, " +
					"(select deal_name from t_wx_apply_deal t where a.id = t.apply_id and t.deal_role = 2) manage_deal_name, " +
					"(select DATE_FORMAT(t.deal_date,'%Y-%m-%d %H:%i:%S') deal_date from t_wx_apply_deal t where a.id = t.apply_id and t.deal_role = 2) manage_deal_date " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b " +
					"WHERE " +
					"	a.door_info_id = b.id " +
					"AND a.openid = ? ";
		if(StringUtils.isNotBlank(apply_status)){
			if("0".equals(apply_status)){//申请中 1：销售经理审核中 2：董事长审核中 4:销售总监审核中
				sql = " AND a.apply_status in (1, 2, 4) ";
			}else if("1".equals(apply_status)){//已同意 
				sql = " AND a.apply_status = 3 ";
			}else if("2".equals(apply_status)){//已驳回 -1：销售经理驳回 -2：董事长驳回 -4:销售总监驳回
				sql = " AND a.apply_status in (-1, -2, -4) ";
			}
		}
		sql += " order by a.apply_date desc ";
		logger.debug(str.getSql(sql, new Object[]{openid}));
		return ResultUtils.returnOk(db.queryForList(sql, new Object[]{openid}));
	}

	/**
	 * 描述：我的申请（详情）
	 * @param request
	 * @param apply_id
	 * @return
	 * @see weixin.service.baobeisysService#getApplyMap()
	 * @author zhangyongbin
	 */
	public Map getApplyMap(HttpServletRequest request, String apply_id) {
		String sql = "SELECT " +
					"	fn_getNameByOpenid (a.openid) apply_name, " +
					"	DATE_FORMAT( " +
					"		a.apply_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) apply_date, " +
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info, " +
					"	b.area, " +
					"	b.price, " +
					"	a.apply_price, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.customer_name, " +
					"	a.customer_mobile " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b " +
					"WHERE a.door_info_id = b.id " +
					"  and a.id = ? ";
		return ResultUtils.returnOk(db.queryForMap(sql, new Object[]{apply_id}));
	}
	/**
	 * 描述：我的审批（列表）
	 * @param request
	 * @param deal_status
	 * @return
	 * @see weixin.service.baobeisysService#getDealList()
	 * @author zhangyongbin
	 */
	public Map getDealList(HttpServletRequest request, String deal_status) {
		String openid = this.getWxUserInfo(request).getOpenId();
		String group_id = this.getWxUserInfo(request).getGroupId();
		String roleId = this.getWxUserInfo(request).getRoleId();
		List<String> paramList = new ArrayList<String>();
		String sql = "select * from " +
					"(SELECT " +
					"	fn_getNameByOpenid (a.openid) apply_name, " +
					"	DATE_FORMAT( " +
					"		a.apply_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) apply_date, " +
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info, " +
					"	a.id apply_id, "+
					"	b.area, " +
					"	b.price, " +
					"	a.apply_price, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.customer_name, " +
					"	a.customer_mobile, " +
					"	c.deal_status " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b, " +
					"	t_wx_apply_deal c " +
					"WHERE " +
					"	a.door_info_id = b.id " +
					"AND a.id = c.apply_id " +
					"AND c.deal_openid = ? "+
					"AND c.deal_role = ? " +
				    "union ALL " +
					"select fn_getNameByOpenid (a.openid) apply_name, " +
					"	DATE_FORMAT( " +
					"		a.apply_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) apply_date, " +
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info, " +
					"	a.id apply_id, "+
					"	b.area, " +
					"	b.price, " +
					"	a.apply_price, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.customer_name, " +
					"	a.customer_mobile, " +
					"  '0' deal_status " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b " +
					"WHERE " +
					"	a.door_info_id = b.id ";
			paramList.add(openid);
			paramList.add(roleId);
			
			if(ROLE_BOSS.equals(roleId)){//董事长
				sql += " and a.apply_status = 2 ";
			}else if("4".equals(roleId)){//销售总监
				sql += " and a.apply_status = 4 ";
			}else{//销售经理
				sql += " and a.apply_status = 1 and a.group_id = ? ";
				paramList.add(group_id);
			}
			sql += ") t where 1 = 1 ";
			
			if(StringUtils.isNotBlank(deal_status)){
				sql += " and t.deal_status = ? ";
				paramList.add(deal_status);
			}
			sql += "order by t.apply_date desc ";
		logger.debug(str.getSql(sql, paramList));
		return ResultUtils.returnOk(db.queryForList(sql, paramList));
	}
	/**
	 * 描述：我的审批（详情）
	 * @param request
	 * @param deal_status
	 * @return
	 * @see weixin.service.baobeisysService#getDealList()
	 * @author zhangyongbin
	 */
	public Map getDealMap(HttpServletRequest request, String apply_id) {
		String roleId = this.getWxUserInfo(request).getRoleId();
		String openid = this.getWxUserInfo(request).getOpenId();
		String group_id = this.getWxUserInfo(request).getGroupId();
		String sql = "SELECT " +
					"	fn_getNameByOpenid (a.openid) apply_name, " +
					"	DATE_FORMAT( " +
					"		a.apply_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) apply_date, " +
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info, " +
					"	b.area, " +
					"	b.price, " +
					"	a.apply_price, " +
					"	b.min_price, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.customer_name, " +
					"	a.customer_mobile, " +
					"	c.deal_status " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b, " +
					"	t_wx_apply_deal c " +
					"WHERE " +
					"	a.door_info_id = b.id " +
					"AND a.id = c.apply_id " +
					"AND c.apply_id = ? " +
					"AND c.deal_openid = ? "+
					"AND c.deal_role = ? " +
					"union ALL " +
					"select fn_getNameByOpenid (a.openid), " +
					"	DATE_FORMAT( " +
					"		a.apply_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) apply_date, " +
					"	concat( " +
					"		build_num, " +
					"		'-', " +
					"		unit_num, " +
					"		'-', " +
					"		door_num " +
					"	) door_info, " +
					"	b.area, " +
					"	b.price, " +
					"	b.min_price, " +
					"	a.apply_price, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.customer_name, " +
					"	a.customer_mobile, " +
					"  '0' deal_status " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b " +
					"WHERE " +
					"	a.door_info_id = b.id " +
					"	and a.id = ? ";
		if(ROLE_BOSS.equals(roleId)){//董事长
			sql += " and a.apply_status = 2 ";
		}else if("4".equals(roleId)){//销售总监
			sql += " and a.apply_status = 4 ";
		}else{//销售经理
			sql += " and a.apply_status = 1 and a.group_id = " + group_id;
		}
		logger.debug(str.getSql(sql, new Object[]{apply_id, openid, roleId, apply_id}));
		return ResultUtils.returnOk(db.queryForMap(sql, new Object[]{apply_id, openid, roleId, apply_id}));
	}
	
	public Map isDeal(HttpServletRequest request, String apply_id){
		String roleId = this.getWxUserInfo(request).getRoleId();
		String sql = " select count(1) from t_wx_apply_info a where a.id = ? ";
		if(ROLE_BOSS.equals(roleId)){//董事长
			sql += " and a.apply_status = 2 ";
		}else if("4".equals(roleId)){//营销总监
			sql += " and a.apply_status = 4 ";
		}else{
			sql += " and a.apply_status = 1 ";
		}
		int apply_flag = db.queryForInt(sql, new Object[]{apply_id});
		if(apply_flag == 0){
			return ResultUtils.returnError(14001, "该申请已被处理");
		}else if(apply_flag == -1){
			return ResultUtils.returnError(13001, "数据操作失败");
		}
		return null;
	}
	
	
	public Map dealApply(HttpServletRequest request, String apply_id, String deal_status){
		String openid = this.getWxUserInfo(request).getOpenId();
		String roleId = this.getWxUserInfo(request).getRoleId();
		String dealName = this.getWxUserInfo(request).getName();
		System.out.println("role_id::::::::::" + roleId);
		String sql1 = " select count(1) from t_wx_apply_info a where a.id = ? ";
		if(ROLE_BOSS.equals(roleId)){//董事长
			sql1 += " and a.apply_status = 2 ";
		}else if("4".equals(roleId)){//营销总监
			sql1 += " and a.apply_status = 4 ";
		}else{
			sql1 += " and a.apply_status = 1 ";
		}
		int apply_flag = db.queryForInt(sql1, new Object[]{apply_id});
		if(apply_flag == 0){
			return ResultUtils.returnError(14001, "该申请已被处理");
		}else if(apply_flag == -1){
			return ResultUtils.returnError(13001, "数据操作失败");
		}
		
		
		if(ROLE_BOSS.equals(roleId) || ROLE_MANAGE.equals(roleId) || "4".equals(roleId)){
			BatchSql batchSql = new BatchSql();
			String sql = " select flag_boss_deal from t_wx_apply_info a where a.id = ? ";
			int flag_boss_deal = db.queryForInt(sql, new Object[]{apply_id});
			
			String apply_status = "";
			if(flag_boss_deal == 1){//需要董事长处理
				if("1".equals(deal_status) && ROLE_BOSS.equals(roleId)){
					apply_status = "3";//同意
				}else if("2".equals(deal_status) && ROLE_BOSS.equals(roleId)){
					apply_status = "-2";//董事长驳回
				}else if("1".equals(deal_status) && ROLE_MANAGE.equals(roleId)){
					apply_status = "4";//销售经理同意,销售总监审核
				}else if("2".equals(deal_status) && ROLE_MANAGE.equals(roleId)){
					apply_status = "-1";//销售经理驳回
				}else if("1".equals(deal_status) && "4".equals(roleId)){
					apply_status = "2";//销售总监同意，董事长审核
				}else if("2".equals(deal_status) && "4".equals(roleId)){
					apply_status = "-4";//销售总监驳回
				}
				
			}else{//不需要董事长处理
				if("1".equals(deal_status) && ROLE_MANAGE.equals(roleId)){//销售经理同意
					apply_status = "4";
				}else if("2".equals(deal_status) && ROLE_MANAGE.equals(roleId)){//销售经理驳回
					apply_status = "-1";
				}else if("1".equals(deal_status) && "4".equals(roleId)){//销售总监同意
					apply_status = "3";
				}else if("2".equals(deal_status) && "4".equals(roleId)){
					apply_status = "-4";
				}
			}
			//记录审核流水
			sql = " INSERT INTO t_wx_apply_deal ( " +
					"	apply_id, " +
					"	deal_role, " +
					"	deal_openid, " +
					"	deal_status, " +
					"	deal_date, " +
					"	deal_name " +
					") " +
					"VALUES " +
					"	(?,?,?,?, SYSDATE(), ?) ";
			logger.debug(str.getSql(sql, new Object[]{apply_id, roleId, openid, deal_status, dealName}));
			batchSql.addBatch(sql, new Object[]{apply_id, roleId, openid, deal_status, dealName});
			//更新申请状态
			sql = " update t_wx_apply_info a set a.apply_status = ? where a.id = ? ";
			batchSql.addBatch(sql, new Object[]{apply_status, apply_id});
			if("3".equals(apply_status)){//已同意
				//更新预定中领导的同意时间
				sql = "UPDATE t_wx_door_info a " +
						"SET status_update_time = SYSDATE() " +
						"WHERE " +
						"	a.id = ( " +
						"		SELECT " +
						"			b.door_info_id " +
						"		FROM " +
						"			t_wx_apply_info b " +
						"		WHERE " +
						"			b.id = ? " +
						"	) " +
						"AND a.`status` = 1 ";
				batchSql.addBatch(sql, new Object[]{apply_id});
			}
			if("-1".equals(apply_status) || "-2".equals(apply_status) || "-4".equals(apply_status)){//领导驳回
				sql = " UPDATE t_wx_door_info a " +
						" SET status_update_time = null, "
						+ "  status = 0 " +
						" WHERE " +
						"	a.id = ( " +
						"		SELECT " +
						"			b.door_info_id " +
						"		FROM " +
						"			t_wx_apply_info b " +
						"		WHERE " +
						"			b.id = ? " +
						"	) " +
						"AND a.`status` = 1 ";
				batchSql.addBatch(sql, new Object[]{apply_id});
				//驳回，申请失效
				sql = " update t_wx_apply_info set status = 0 where id = ? ";
				batchSql.addBatch(sql, new Object[]{apply_id});
			}
			int result = db.doInTransaction(batchSql);
			if(result == 1){
				return ResultUtils.returnOk(null);
			}else{
				return ResultUtils.returnError(13001, "数据保存失败");
			}
		}else{
			return ResultUtils.returnError(13003, "您无权限操作");
		}
	}
	
	/**
	 * 描述：获取我的消息
	 * @return
	 * @see weixin.service.baobeisysService#getMyMessageList()
	 * @author zhangyongbin
	 */
	public Map getMyMessageList(HttpServletRequest request){
		String openid = this.getWxUserInfo(request).getOpenId();
		String sql = " select id msg_id, read_status, title "
				+ "     from t_wx_my_message where openid = ? order by create_date desc";
		return ResultUtils.returnOk(db.queryForList(sql, new Object[]{openid}));
	}
	
	/**
	 * 描述：我的消息详情
	 * @param msg_id
	 * @return
	 * @see weixin.service.baobeisysService#getMyMessageMap()
	 * @author zhangyongbin
	 */
	public Map getMyMessageMap(String msg_id){
		String sql = " update t_wx_my_message set read_status = 1 where id = ? ";
		int result = db.update(sql, new Object[]{msg_id});
		if(result == 1){
			sql = " select id msg_id, title, detail_info, fn_getusername(create_id) create_name, "
					+ " DATE_FORMAT(create_date, '%Y-%m-%d %H:%i:%S') create_date "
					+ " from t_wx_my_message where id = ? ";
			return ResultUtils.returnOk(db.queryForList(sql, new Object[]{msg_id}));
		}
		return ResultUtils.returnError(13001, "数据操作失败");
		
	}
	
	/**
	 * 描述：获取banner
	 * @return
	 * @see weixin.service.baobeisysService#getBanerList()
	 * @author zhangyongbin
	 */
	public Map getBanerList(){
		String sql = " select img_url from t_wx_banner where status = 1 ";
		return ResultUtils.returnOk(db.queryForList(sql));
	}
	
	public void testMsg() {
		String access_token_url2 = access_token_url.replace("APPID", fwappid).replace("APPSECRET", fwappsecret);
		System.out.println("access_token_url2::" + access_token_url2);
		String access_token = WxUtils.getAccessToken(access_token_url2);
		
		ApplyMsg applyMsg = new ApplyMsg();
		applyMsg.setFirst("价格申请");
		applyMsg.setApply_name("测试申请人");
		applyMsg.setApply_date("2019-3-19 10:19:00");
		applyMsg.setApply_price("20000");
		applyMsg.setDoor_info("1-1-0101");
		//applyMsg.setRemark("客户要求20000");
		Map applyData = applyMsg.getGetApplyData();
		System.out.println(str.get(applyData, "first"));
		Map m = new HashMap();
		m.put("touser", "opXvE5MIxj6pOh-df0B0Yq4x2cLQ");
		m.put("template_id", "F_dJ_WC46JXxRqcLrMeVC-mHqRSeAeldWH-s0cHbEVw");
		m.put("url", "www.baidu.com");
		m.put("data", applyData);
		send_template_msg = send_template_msg.replace("ACCESS_TOKEN", access_token);
		WxUtils.sendWeappTemplateMsg(m, send_template_msg);
		
	}
	
	public static void main(String[] args) {
		String id = ",1,2,3,4";
		System.out.println(Double.parseDouble("9"));
	}

	public Map getGroupRpt(HttpServletRequest request) {
		String roleId = this.getWxUserInfo(request).getRoleId();
		String group_id = this.getWxUserInfo(request).getGroupId();
		List paraList = new ArrayList();
		String sql = "SELECT " +
				"	a.group_id, " +
				"  (select group_name from t_wx_salegroup_info t where a.group_id = t.id) group_name, " +
				"  sum(case when b.status = 1 then 1 else 0 end) yuding_cnt, " +
				"  sum(case when b.status = 2 then 1 else 0 end) saled_cnt " +
				"FROM " +
				"	t_wx_apply_info a, " +
				"	t_wx_door_info b " +
				"WHERE " +
				"	a.door_info_id = b.id "
				+ " and a.status = 1 "
				+ " and a.group_id <> 0 ";
		if("2".equals(roleId) || "3".equals(roleId)){//销售人员或者销售经理
			sql += " and a.group_id = ? ";
			paraList.add(group_id);
		}
		sql += " group by  " +
				" a.group_id ";
		List groupRptList = db.queryForList(sql, paraList);
		logger.debug(str.getSql(sql, paraList));
		return ResultUtils.returnOk(groupRptList);
	}

	public Map getGroupRptDetail(HttpServletRequest request) {
		String group_id = req.getValue(request, "group_id");
		String sale_status = req.getValue(request, "sale_status");
		String sql = "";
		if("1".equals(sale_status)){//预订列表
			sql = " select " +
					"   concat(b.build_num, '-', b.unit_num, '-', b.door_num) door_info, " +
					"   b.area, " +
					"   b.price, " +
					"   a.apply_price, " +
					"   fn_getNameByOpenid(a.openid) apply_name, " +
					"   DATE_FORMAT(a.apply_date, '%Y-%m-%d %H:%i:%S') apply_date, " +
					"   a.customer_name, " +
					"   a.customer_mobile, " +
					"   case when a.apply_status = 1 then '销售经理审核中' when a.apply_status = 4 then '销售总监审核中' "
					+ " when a.apply_status = 2 then '董事长审核中' when a.apply_status = 3 then '已同意' end sale_status_name " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b " +
					"WHERE " +
					"	a.door_info_id = b.id " +
					"AND a.status = 1 " +
					"AND b.status = 1 " +
					"AND a.group_id = ?  "
					+ " order by a.apply_date desc " ;
		}else if("2".equals(sale_status)){//已成交
			sql = "SELECT " +
					"	concat(b.build_num, '-', b.unit_num, '-', b.door_num) door_info, " +
					"  c.area, " +
					"  c.price, " +
					"  c.total_price, " +
					"  c.sale_oper_name, " +
					"	DATE_FORMAT( " +
					"		c.record_date, " +
					"		'%Y-%m-%d %H:%i:%S' " +
					"	) record_date, " +
					"  c.customer_name, " +
					"  c.customer_mobile " +
					"FROM " +
					"	t_wx_apply_info a, " +
					"	t_wx_door_info b, " +
					"	t_wx_sale_print_info c " +
					"WHERE " +
					"	a.door_info_id = b.id " +
					"AND b.sale_print_info_id = c.id " +
					"AND a.status = 1 " +
					"AND b.status = 2 " +
					"AND a.group_id = ? order by c.record_date desc " ;
		}
		
		List resList = db.queryForList(sql, new Object[]{group_id});
		return ResultUtils.returnOk(resList);
	}
}
