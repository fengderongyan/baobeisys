package moi.service.yjbb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import moi.service.log.MobileLogService;
import baobeisys.action.sys.UserCfgController;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.sgy.util.ResultUtils;
import com.sgy.util.common.FileHelper;
import com.sgy.util.db.BatchSql;

import util.JPushUtil;

import web.service.BaseService;

/** 
 * 手机端流程处理
 * @author zhang
 * @date 2019-01-15 
 */
@Service("yjbbLcSevice")
public class YjbbLcSevice extends BaseService {
	
	
	/**
	 * 描述：获取字典表信息
	 * @param data_type_code
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-25
	 */
	public List getDDwByType(String data_type_code){
		String sql = " select dd_item_code, dd_item_name from t_ddw where data_type_code = ? order by order_id ";
		List res = db.queryForList(sql, new Object[]{data_type_code});
		return res;
	}
	
	/**
	 * 描述：饮酒立即报备
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-25
	 */
	public int insertYjbbApply(HttpServletRequest request) {
		String id = this.get32UUID();
    	String start_date = req.getValue(request, "start_date");
    	String address = req.getValue(request, "address");
    	String yj_type = req.getValue(request, "yj_type");
    	String jj_lxr_name = req.getValue(request, "jj_lxr_name");
    	String jj_lxr_phone = req.getValue(request, "jj_lxr_phone");
    	String flag_drive = req.getValue(request, "flag_drive");
    	String remark = req.getValue(request, "remark");
    	String deal_oper_id = "";//当前审核人
    	String deal_lev= "";//当前审核人审核等级
    	String operatorId = this.getUser(request).getOperatorId();//获取当前申请人
    	String operatorName = this.getUser(request).getName();
    	String sql = "";
    	BatchSql batchSql = new BatchSql();
		sql = "select " +
			  "	deal_oper_id, "
			  + " deal_lev " +
				"from " +
				"	t_yjcfg_info  " +
				"where " +
				"	apply_oper_id = ?  " +
				"	and deal_lev = ( select min( deal_lev ) deal_lev from t_yjcfg_info where apply_oper_id = ? ) " +
				"	and bb_type = 1 ";
		Map dealMap = db.queryForMap(sql, new Object[]{operatorId, operatorId});
		deal_oper_id = str.get(dealMap, "deal_oper_id");
		deal_lev = str.get(dealMap, "deal_lev");
		
		if(deal_oper_id == null || "".equals(deal_oper_id)){
			return -2;
		}
		sql = " insert into t_yjbb_apply  " +
				" ( id, " +
				"   bbr,  " +
				"	 start_date,  " +
				"	 address,  " +
				"	 yj_type,  " +
				"	 jj_lxr_name,  " +
				"	 jj_lxr_phone, " +
				"	 flag_drive, " +
				"	 remark, " +
				"	 record_date, " +
				"	 apply_status, " +
				"	 deal_oper_id, "
				+ "	 deal_lev) " +
				" values " +
				"   (?,?,STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s'), " +
				"	  ?,?,?,?,?,?,SYSDATE(), 0, ?, ?) ";
		batchSql.addBatch(sql, new Object[]{id, operatorId, start_date, address, yj_type, jj_lxr_name, jj_lxr_phone,
				flag_drive, remark, deal_oper_id, deal_lev});
		sql = " update t_user set jj_lxr_name = ?, jj_lxr_phone = ? where operator_id = ? ";
		batchSql.addBatch(sql, new Object[]{jj_lxr_name, jj_lxr_phone, operatorId});
		int res = db.doInTransaction(batchSql);
		if(res == 1){
			//极光推送消息
			this.pushMessage(deal_oper_id, "您有一条新的饮酒报备待审核", operatorName + "发起了一条饮酒报备申请，请及时审核");
		}
		
		return res;
	}
	
	/**
	 * 描述：请假报表
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-08-01
	 */
	public int insertQjbbApply(HttpServletRequest request) {
		String begin_date = req.getValue(request, "begin_date");
		String bb_end_date = req.getValue(request, "bb_end_date");
		String bb_day_cnt = req.getValue(request, "bb_day_cnt");
		String bb_address = req.getValue(request, "bb_address");
		String qj_type = req.getValue(request, "qj_type");
		String jj_lxr_name = req.getValue(request, "jj_lxr_name");
		String jj_lxr_phone = req.getValue(request, "jj_lxr_phone");
		String remark = req.getValue(request, "remark");
		String operatorId = this.getOperatorId(request);
		String operatorName = this.getUser(request).getName();
		String id = this.get32UUID();
		String deal_oper_id = "";
		String deal_lev= "";//当前审核人审核等级
		String sql = "";
		BatchSql batchSql = new BatchSql();
		sql = "select " +
				  "	deal_oper_id, "
				  + " deal_lev " +
					"from " +
					"	t_yjcfg_info  " +
					"where " +
					"	apply_oper_id = ?  " +
					"	and deal_lev = ( select min( deal_lev ) deal_lev from t_yjcfg_info where apply_oper_id = ? ) " +
					"	and bb_type = 2 ";
		Map dealMap = db.queryForMap(sql, new Object[]{operatorId, operatorId});
		deal_oper_id = str.get(dealMap, "deal_oper_id");
		deal_lev = str.get(dealMap, "deal_lev");
		if(deal_oper_id == null || "".equals(deal_oper_id)){
			return -2;
		}
		sql = " INSERT INTO t_qjbb_apply ( " +
				"	id, " +
				"	bbr, " +
				"	begin_date, " +
				"	bb_end_date, " +
				"	bb_day_cnt, " +
				"	bb_address, " +
				"	qj_type, " +
				"	jj_lxr_name, " +
				"	jj_lxr_phone, " +
				"	remark, " +
				"	record_date, " +
				"	record_id, " +
				"	apply_status, " +
				"	deal_oper_id, "
				+ " deal_lev " +
				") " +
				" values " +
				" (?, ?, STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s'), STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s'), "
				+ " ?, ?, ?, ?,?,?, SYSDATE(), ?, 0, ?, ?) ";
		batchSql.addBatch(sql, new Object[]{id, operatorId, begin_date, bb_end_date, bb_day_cnt, bb_address, qj_type, 
				jj_lxr_name, jj_lxr_phone, remark, operatorId, deal_oper_id, deal_lev});
		sql = " update t_user set jj_lxr_name = ?, jj_lxr_phone = ? where operator_id = ? ";
		batchSql.addBatch(sql, new Object[]{jj_lxr_name, jj_lxr_phone, operatorId});
		int res = db.doInTransaction(batchSql);
		if(res == 1){
			//极光推送消息
			this.pushMessage(deal_oper_id, "您有一条新的请假报备待审核", operatorName + "发起了一条请假报备申请，请及时审核");
		}
		
		return res;
	}
	
	/**
	 * 描述：用车报备
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-08-01
	 */
	public int insertYcbbApply(HttpServletRequest request){
		String begin_date = req.getValue(request, "begin_date");
		String yc_reason = req.getValue(request, "yc_reason");
		String car_type = req.getValue(request, "car_type");
		String route = req.getValue(request, "route");
		String address = req.getValue(request, "address");
		String txry_name = req.getValue(request, "txry_name");
		String remark = req.getValue(request, "remark");
		String operatorId = this.getOperatorId(request);
		String operatorName = this.getUser(request).getName();
		String id = this.get32UUID();
		String deal_oper_id = "";
		String deal_lev= "";//当前审核人审核等级
		String sql = "";
		sql = "select " +
				  "	deal_oper_id, "
				  + " deal_lev " +
					"from " +
					"	t_yjcfg_info  " +
					"where " +
					"	apply_oper_id = ?  " +
					"	and deal_lev = ( select min( deal_lev ) deal_lev from t_yjcfg_info where apply_oper_id = ? ) " +
					"	and bb_type = 2 ";
		Map dealMap = db.queryForMap(sql, new Object[]{operatorId, operatorId});
		deal_oper_id = str.get(dealMap, "deal_oper_id");
		deal_lev = str.get(dealMap, "deal_lev");
		if(deal_oper_id == null || "".equals(deal_oper_id)){
			return -2;
		}
		sql = " INSERT INTO t_ycbb_apply ( " +
				"	id, " +
				"	bbr, " +
				"	begin_date, " +
				"	yc_reason, " +
				"	car_type, " +
				"	route, " +
				"	address, " +
				"	txry_name, " +
				"	remark, " +
				"	record_date, " +
				"	record_id, " +
				"	apply_status, " +
				"	deal_oper_id, "
				+ " deal_lev " +
				") " +
				" values " +
				" (?, ?, STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s'),?, "
				+ " ?, ?, ?, ?, ?, SYSDATE(), ?, 0, ?, ?) ";
		int res = db.update(sql, new Object[]{id, operatorId, begin_date, yc_reason, car_type, route, address, txry_name,
				remark, operatorId, deal_oper_id, deal_lev});
		if(res == 1){
			//极光推送消息
			this.pushMessage(deal_oper_id, "您有一条新的用车报备待审核", operatorName + "发起了一条用车报备申请，请及时审核");
		}
		
		return res;
	}
	
	
	/**
	 * 描述：获取审核列表
	 * @param request
	 * @return
	 * @author yanbs
	 * @Date : 2019-07-25
	 */
	public List getDealList(HttpServletRequest request) {
		String operatorId = this.getUser(request).getOperatorId();
		String operatorIdStr = "," + operatorId + ",";
		String bb_type = req.getValue(request, "bb_type");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		List paramList = new ArrayList();
		String sql = "";
		if("1".equals(bb_type)){//饮酒报备
			sql = " select " +
					"  id apply_id, " +
					"   fn_getusername(bbr) bbr_name,  " +
					"	date_format(start_date, '%Y-%m-%d %H:%i:%s') start_date,  " +
					"	address,  " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type,  " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	flag_drive, " +
					"	remark, " +
					"	date_format(record_date, '%Y-%m-%d %H:%i:%s') record_date, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归家' when 3 then '已归家(隔天)' when -1 then '驳回' end apply_status_name, "
					+ " deal_lev " +
					" from t_yjbb_apply a where instr(concat(',', deal_oper_id, ','), ?) > 0 and apply_status = '0' " +
					"  and a.id not in (select t.apply_id from t_bb_deal t where t.bb_type = ? and t.deal_lev = a.deal_lev and a.id = t.apply_id) " +
					" order by record_date desc ";
			paramList.add(operatorIdStr);
			paramList.add(bb_type);
			return db.getForListForMobile(str.getSql(sql, paramList), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("2".equals(bb_type)){
			sql = "select  id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	DATE_FORMAT(bb_end_date, '%Y-%m-%d %H:%i:%s') bb_end_date, " +
					"	bb_day_cnt, " +
					"	bb_address, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30004' and dd_item_code = qj_type) qj_type, " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	deal_oper_id," +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, "
					+ " deal_lev " +
					"from t_qjbb_apply a " +
					"where instr(concat(',', deal_oper_id, ','), ?) > 0  " +
					"  and apply_status = '0' " +
					"  and a.id not in (select t.apply_id from t_bb_deal t where t.bb_type = ? and t.deal_lev = a.deal_lev and a.id = t.apply_id) " +
					" order by record_date desc ";
			paramList.add(operatorIdStr);
			paramList.add(bb_type);
			return db.getForListForMobile(str.getSql(sql, paramList), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("3".equals(bb_type)){//用车报备
			sql = "select id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30005' and dd_item_code = yc_reason) yc_reason, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30006' and dd_item_code = car_type) car_type, " +
					"	route, " +
					"	address, " +
					"	txry_name, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_ycbb_apply a " +
					"where instr(concat(',', deal_oper_id, ','), ?) > 0  " +
					"  and apply_status = '0' " +
					"  and a.id not in (select t.apply_id from t_bb_deal t where t.bb_type = ? and t.deal_lev = a.deal_lev and a.id = t.apply_id) " +
					" order by record_date desc ";
			paramList.add(operatorIdStr);
			paramList.add(bb_type);
			return db.getForListForMobile(str.getSql(sql, paramList), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}
		
		return null;
	}

	
	public Map getDealInfo(HttpServletRequest request){
		String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	String sql = "";
    	Map map = new HashMap();
    	if("1".equals(bb_type)){//饮酒报备
			sql = " select " +
					"   id apply_id, " +
					"   fn_getusername(bbr) bbr_name,  " +
					"	bbr, " +
					"	fn_getrolenames(bbr) roleNames, " +
					"	(select fn_getorgname(org_id) from t_user t where t.operator_id = a.bbr ) org_name, " +
					"	date_format(start_date, '%Y-%m-%d %H:%i:%s') start_date,  " +
					"	address,  " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type,  " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	flag_drive, " +
					"	remark, " +
					"	date_format(record_date, '%Y-%m-%d %H:%i:%s') record_date, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	apply_status, "
					+ " deal_lev " +
					" from t_yjbb_apply a where id = ? ";
			map = db.queryForMap(sql, new Object[]{apply_id});
		}else if("2".equals(bb_type)){
			sql = "select id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	bbr, " +
					"	fn_getrolenames(bbr) roleNames, " +
					"	(select fn_getorgname(org_id) from t_user t where t.operator_id = a.bbr ) org_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	DATE_FORMAT(bb_end_date, '%Y-%m-%d %H:%i:%s') bb_end_date, " +
					"	bb_day_cnt, " +
					"	bb_address, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30004' and dd_item_code = qj_type) qj_type, " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_qjbb_apply a where id = ? ";
			map = db.queryForMap(sql, new Object[]{apply_id});
		}else if("3".equals(bb_type)){//用车报备
			sql = "select id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	bbr, " +
					"	fn_getrolenames(bbr) roleNames, " +
					"	(select fn_getorgname(org_id) from t_user t where t.operator_id = a.bbr ) org_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30005' and dd_item_code = yc_reason) yc_reason, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30006' and dd_item_code = car_type) car_type, " +
					"	route, " +
					"	address, " +
					"	txry_name, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_ycbb_apply a where id = ? ";
			map = db.queryForMap(sql, new Object[]{apply_id});
		}
    	return map;
	}
	
	public List getDealedList(HttpServletRequest request, String apply_status, String deal_lev, String bbr){
		String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	if("0".equals(apply_status)){
    		String sql = " select * from (SELECT " +
					" fn_getusername(deal_oper_id) deal_oper_name, "+
	    			" case when deal_status = 1 then '同意' when deal_status = 0 then '驳回' end deal_status_name, " +
	    			"  DATE_FORMAT(deal_date, '%Y-%m-%d %H:%i:%s') deal_date, " +
	    			"  reject_reason, " +
	    			"  deal_lev " +
	    			" from t_bb_deal a " +
	    			"where bb_type = ? " +
	    			"  and apply_id = ? " +
				    " union all select replace(deal_oper_name, ',', '/') deal_oper_name, '未审核' deal_status_name, '' deal_date, '' reject_reason, deal_lev "
    				+ " from t_yjcfg_info where deal_lev >= ? and apply_oper_id = ? and bb_type = ? ) a order by deal_lev ";
    		return db.queryForList(sql, new Object[]{bb_type, apply_id, deal_lev, bbr, bb_type});
    	}else{
    		String sql = "SELECT " +
					" fn_getusername(deal_oper_id) deal_oper_name, "+
	    			" case when deal_status = 1 then '同意' when deal_status = 0 then '驳回' end deal_status_name, " +
	    			"  DATE_FORMAT(deal_date, '%Y-%m-%d %H:%i:%s') deal_date, " +
	    			"  reject_reason, " +
	    			"  deal_lev " +
	    			" from t_bb_deal a " +
	    			"where bb_type = ? " +
	    			"  and apply_id = ? " +
    				" order by deal_lev ";
    		return db.queryForList(sql, new Object[]{bb_type, apply_id});
    	}
	}
	
	public int dealApplyInfo(HttpServletRequest request){
		String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	String bbr = req.getValue(request, "bbr");
    	String deal_status = req.getValue(request, "deal_status");
    	String deal_lev = req.getValue(request, "deal_lev");
    	String reject_reason = req.getValue(request, "reject_reason");
    	String operatorId = this.getUser(request).getOperatorId();
    	String operatorName = this.getUser(request).getName();
    	String apply_status = "";
    	String next_deal_oper_id = "";
    	String next_deal_lev = "-1";
    	String sql = "";
    	String id = this.get32UUID();
    	BatchSql batchSql = new BatchSql();
    	
    	String sqlCnt = " select count(1) from t_bb_deal where deal_lev = ? and apply_id = ? and bb_type = ? ";
    	int cnt = db.queryForInt(sqlCnt, new Object[]{deal_lev, apply_id, bb_type});
    	if(cnt > 0){
    		return -2;
    	}
    	if("0".equals(deal_status)){//不同意
    		apply_status = "-1";
    	}else if("1".equals(deal_status)){//同意
    		sql = "select " +
  				  "	deal_oper_id, "
  				  + " deal_lev " +
  					"from " +
  					"	t_yjcfg_info  a " +
  					"where " +	
  					"	apply_oper_id = ?  " +
  					"	and deal_lev = ( select min(deal_lev) deal_lev from t_yjcfg_info where apply_oper_id = ? and deal_lev > ? ) " +
  					"	and bb_type = ? ";
	  		Map dealMap = db.queryForMap(sql, new Object[]{bbr, bbr, deal_lev, bb_type});//获取下级审核人
	  		if(dealMap != null || dealMap.size() > 0){
	  			next_deal_oper_id = str.get(dealMap, "deal_oper_id");
	  			next_deal_lev = (str.get(dealMap, "deal_lev")).equals("") ? "-1" : str.get(dealMap, "deal_lev");
	  		}
	  		
	  		if("".equals(next_deal_oper_id)){//没有下级审核，流程结束
	  			apply_status = "1";
	  		}else{//有下级审核人
	  			apply_status = "0";
	  		}
    	}
    	
    	sql = "INSERT INTO t_bb_deal ( " +
    			"	id, " +
    			"	bb_type, " +
    			"	apply_id, " +
    			"	deal_status, " +
    			"	deal_oper_id, " +
    			"	deal_lev, " +
    			"	reject_reason, " +
    			"	deal_date " +
    			") " +
    			"VALUES " +
    			"(?, ?, ?, ?, ?, ?, ?, SYSDATE()) ";
    	batchSql.addBatch(sql, new Object[]{id, bb_type, apply_id, deal_status, operatorId, deal_lev, reject_reason});
    	
    	String tableName = "";
    	if("1".equals(bb_type)){
    		tableName = "t_yjbb_apply";
    	}else if("2".equals(bb_type)){
    		tableName = "t_qjbb_apply";
    	}else if("3".equals(bb_type)){
    		tableName = "t_ycbb_apply";
    	}
    	sql = " update " + tableName + 
    		  "    set apply_status = ?, " +
    		  "        deal_oper_id = ?, " +
    		  "        deal_lev = ?, " +
    		  "        reject_reason = ? " +
    		  "   where id = ? ";
    	batchSql.addBatch(sql, new Object[]{apply_status, next_deal_oper_id, next_deal_lev, reject_reason, apply_id});
    	int res = db.doInTransaction(batchSql);
    	
    	//极光推送
    	String push_title = "";
    	String push_content = "";
    	if("0".equals(deal_status)){//驳回
    		push_title = "您申请的报备已被驳回";
    		push_content = "审核人：" + operatorName +",驳回理由：" + reject_reason;
    	}else if("1".equals(deal_status) && "0".equals(apply_status)){//同意,但是还未完成
    		push_title = "您申请的报备有了新进展";
    		push_content = "审核人：" + operatorName + "，已同意";
    	}else if("1".equals(deal_status) && "1".equals(apply_status)){//同意。并且审核流程结束
    		push_title = "您申请的报备审核已通过";
    		push_content = "审核人：" + operatorName + "，已同意";
    	}
    	this.pushMessage(bbr, push_title, push_content);
		return res;
	}
	
	public int dealApplyInfo(HttpServletRequest request, String bb_type, String apply_id, String bbr, String deal_status, String deal_lev){
    	String operatorId = this.getUser(request).getOperatorId();
    	String operatorName = this.getUser(request).getName();
    	String apply_status = "";
    	String next_deal_oper_id = "";
    	String next_deal_lev = "-1";
    	String sql = "";
    	String id = this.get32UUID();
    	BatchSql batchSql = new BatchSql();
    	
    	String sqlCnt = " select count(1) from t_bb_deal where deal_lev = ? and apply_id = ? and bb_type = ? ";
    	int cnt = db.queryForInt(sqlCnt, new Object[]{deal_lev, apply_id, bb_type});
    	if(cnt > 0){
    		return -2;
    	}
    	if("0".equals(deal_status)){//不同意
    		apply_status = "-1";
    	}else if("1".equals(deal_status)){//同意
    		sql = "select " +
  				  "	deal_oper_id, "
  				  + " deal_lev " +
  					"from " +
  					"	t_yjcfg_info  a " +
  					"where " +	
  					"	apply_oper_id = ?  " +
  					"	and deal_lev = ( select min(deal_lev) deal_lev from t_yjcfg_info where apply_oper_id = ? and deal_lev > ? ) " +
  					"	and bb_type = ? ";
	  		Map dealMap = db.queryForMap(sql, new Object[]{bbr, bbr, deal_lev, bb_type});//获取下级审核人
	  		if(dealMap != null || dealMap.size() > 0){
	  			next_deal_oper_id = str.get(dealMap, "deal_oper_id");
	  			next_deal_lev = (str.get(dealMap, "deal_lev")).equals("") ? "-1" : str.get(dealMap, "deal_lev");
	  		}
	  		
	  		if("".equals(next_deal_oper_id)){//没有下级审核，流程结束
	  			apply_status = "1";
	  		}else{//有下级审核人
	  			apply_status = "0";
	  		}
    	}
    	
    	sql = "INSERT INTO t_bb_deal ( " +
    			"	id, " +
    			"	bb_type, " +
    			"	apply_id, " +
    			"	deal_status, " +
    			"	deal_oper_id, " +
    			"	deal_lev, " +
    			"	deal_date " +
    			") " +
    			"VALUES " +
    			"(?, ?, ?, ?, ?, ?, SYSDATE()) ";
    	batchSql.addBatch(sql, new Object[]{id, bb_type, apply_id, deal_status, operatorId, deal_lev});
    	
    	sql = " update t_yjbb_apply " + 
    		  "    set apply_status = ?, " +
    		  "        deal_oper_id = ?, " +
    		  "        deal_lev = ? " +
    		  "   where id = ? ";
    	batchSql.addBatch(sql, new Object[]{apply_status, next_deal_oper_id, next_deal_lev, apply_id});
    	int res = db.doInTransaction(batchSql);
    	
    	//极光推送
    	String push_title = "";
    	String push_content = "";
    	if("0".equals(apply_status)){//同意,但是还未完成
    		push_title = "您申请的报备有了新进展";
    		push_content = "审核人：" + operatorName + "，已同意";
    	}else if("1".equals(apply_status)){//同意。并且审核流程结束
    		push_title = "您申请的报备审核已通过";
    		push_content = "审核人：" + operatorName + "，已同意";
    	}
    	this.pushMessage(bbr, push_title, push_content);
		return res;
	}
	
	
	public List getApplyList(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		String operatorId = this.getOperatorId(request);
		String sql = "";
		if("1".equals(bb_type)){
			sql = " select " +
					"  id apply_id, " +
					"   fn_getusername(bbr) bbr_name,  " +
					"	date_format(start_date, '%Y-%m-%d %H:%i:%s') start_date,  " +
					"	address,  " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type,  " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	flag_drive, " +
					"	remark, " +
					"	date_format(record_date, '%Y-%m-%d %H:%i:%s') record_date, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					" deal_lev " +
					" from t_yjbb_apply a " +
					" where bbr = ? " +
				    "   and apply_status in (0, 1) "
				    + " order by record_date desc ";
			return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("2".equals(bb_type)){
			sql = "select  id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	DATE_FORMAT(bb_end_date, '%Y-%m-%d %H:%i:%s') bb_end_date, " +
					"	bb_day_cnt, " +
					"	bb_address, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30004' and dd_item_code = qj_type) qj_type, " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_qjbb_apply a " +
					" where bbr = ? " +
					"   and apply_status in (0, 1) "
					+ " order by record_date desc ";
			return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("3".equals(bb_type)){
			sql = "select id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30005' and dd_item_code = yc_reason) yc_reason, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30006' and dd_item_code = car_type) car_type, " +
					"	route, " +
					"	address, " +
					"	txry_name, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_ycbb_apply a " +
					" where bbr = ? " +
					"   and apply_status in (0, 1) "
					+ " order by record_date desc ";
			return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}
		return null;
	}

	public List getFinishApplyList(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		String operatorId = this.getOperatorId(request);
		String sql = "";
		if("1".equals(bb_type)){
			sql = " select " +
					"  id apply_id, " +
					"   fn_getusername(bbr) bbr_name,  " +
					"	date_format(start_date, '%Y-%m-%d %H:%i:%s') start_date,  " +
					"	address,  " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type,  " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	flag_drive, " +
					"	remark, " +
					"	date_format(record_date, '%Y-%m-%d %H:%i:%s') record_date, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					" deal_lev " +
					" from t_yjbb_apply a " +
					" where bbr = ? " +
				    "   and apply_status in (2, 3, -1) "
				    + " order by record_date desc ";
			return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("2".equals(bb_type)){
			sql = "select  id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	DATE_FORMAT(bb_end_date, '%Y-%m-%d %H:%i:%s') bb_end_date, " +
					"	bb_day_cnt, " +
					"	bb_address, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30004' and dd_item_code = qj_type) qj_type, " +
					"	jj_lxr_name,  " +
					"	jj_lxr_phone, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_qjbb_apply a " +
					" where bbr = ? " +
					"   and apply_status in (2, 3,  -1) "
					+ " order by record_date desc ";
			return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("3".equals(bb_type)){
			sql = "select id apply_id, " +
					"	fn_getusername(bbr) bbr_name, " +
					"	DATE_FORMAT(begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30005' and dd_item_code = yc_reason) yc_reason, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30006' and dd_item_code = car_type) car_type, " +
					"	route, " +
					"	address, " +
					"	txry_name, " +
					"	remark, " +
					"	apply_status, " +
					"	reject_reason, " +
					"	case apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	deal_oper_id,"
					+ " deal_lev " +
					"from t_ycbb_apply a " +
					" where bbr = ? " +
					"   and apply_status in (2, 3,  -1) "
					+ " order by record_date desc ";
			return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}
		return null;
	}

	public List getOrgList(HttpServletRequest request) {
		String sql = "SELECT " +
					"	org_id, " +
					"	org_name " +
					"FROM " +
					"	t_organization " +
					"WHERE " +
					"	STATUS = 1 " +
					"ORDER BY " +
					"	org_lev, " +
					"	org_id ";
		return db.queryForList(sql);
	}

	public int doFinishApply(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");
    	String apply_id = req.getValue(request, "apply_id");
    	String sql = "";
    	if("1".equals(bb_type)){
    		sql = " update t_yjbb_apply set apply_status = case when DATE_FORMAT(start_date,'%Y-%m-%d') < DATE_FORMAT(SYSDATE(),'%Y-%m-%d') then 3 else 2 end, end_date = SYSDATE() "
    				+ " where id = ? and apply_status = 1 ";
    		
    		return db.update(sql, new Object[]{apply_id});
    	}else if("2".equals(bb_type)){
    		sql = " update t_qjbb_apply set apply_status = case when DATE_FORMAT(begin_date,'%Y-%m-%d') < DATE_FORMAT(SYSDATE(),'%Y-%m-%d') then 3 else 2 end, end_date = SYSDATE() "
    				+ " where id = ? and apply_status = 1 ";
    		return db.update(sql, new Object[]{apply_id});
    	}else if("3".equals(bb_type)){
    		sql = " update t_ycbb_apply set apply_status = case when DATE_FORMAT(begin_date,'%Y-%m-%d') < DATE_FORMAT(SYSDATE(),'%Y-%m-%d') then 3 else 2 end, end_date = SYSDATE() "
    				+ " where id = ? and apply_status = 1 ";
    		return db.update(sql, new Object[]{apply_id});
    	}
    	return 0;
		
	}


	/**
	 * 描述：极光推送
	 * @param push_operator_id//推送目标用户
	 * @param push_title//标题
	 * @param push_content//内容
	 * @param push_type//推送类别
	 * @author yanbs
	 * @Date : 2019-08-07
	 */
	public void pushMessage(String push_operator_id, String push_title, String push_content){
		String sql = " select operator_id, registration_id from t_user where operator_id in(" + push_operator_id + ")";
		List<Map<String, Object>> regList = db.queryForList(sql);
		BatchSql batchSql = new BatchSql();
		JPushUtil jPushUtil = new JPushUtil();
		for (Map<String, Object> map : regList) {
			String registration_id = str.get(map, "registration_id");
			String operator_id = str.get(map, "operator_id");
			jPushUtil.pushMsg(registration_id, push_content, "", "", push_title);
			sql = "INSERT INTO t_push_info ( " +
					"	push_title, " +
					"	push_content, " +
					"	push_operator_id, " +
					"	is_read, " +
					"	record_id, " +
					"	record_date " +
					") " +
					"VALUES " +
					"	(?,?,?,0,?, SYSDATE()) ";
			batchSql.addBatch(sql, new Object[]{push_title, push_content, operator_id, 159999});
		}
		db.doInTransaction(batchSql);
	}

	public List getPushList(HttpServletRequest request) {
		String operatorId = this.getOperatorId(request);
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		String sql = " select id, push_title, case is_read when 1 then '已读' when 0 then '未读' end is_read_name "
				+ " from t_push_info where push_operator_id = ? order by record_date desc ";
		return db.getForListForMobile(str.getSql(sql, new Object[]{operatorId}), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
	}
	
	public Map getPushInfo(HttpServletRequest request){
		String id = req.getValue(request, "id");
		String sql = " update t_push_info set is_read = 1 where id = ? ";
		db.update(sql, new Object[]{id});
		sql = " select id, push_title, push_content from t_push_info where id = ? ";
		return db.queryForMap(sql, new Object[]{id});
	}

	public List getAllList(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");
		String apply_status = req.getValue(request, "apply_status");
		String org_id = req.getValue(request,"org_id");
		String search_date = req.getValue(request, "search_date");
		String pageNum = req.getValue(request, "pageNum");
		String pageSize = req.getValue(request, "pageSize");
		String sql = "";
		List paramList = new ArrayList();
		if("1".equals(bb_type)){
			sql = "SELECT " +
					"	a.id apply_id, " +
					"  b.name bbr_name, " +
					"	date_format(a.start_date, '%Y-%m-%d %H:%i:%s') start_date,  " +
					"	a.address,  " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type,  " +
					"	a.jj_lxr_name,  " +
					"	a.jj_lxr_phone, " +
					"	a.flag_drive, " +
					"	a.remark, " +
					"	date_format(a.record_date, '%Y-%m-%d %H:%i:%s') record_date, " +
					"	a.apply_status, " +
					"	a.reject_reason, " +
					"	case a.apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					" a.deal_lev " +
					"FROM " +
					"	t_yjbb_apply a, " +
					"	t_user b, " +
					"	t_organization c " +
					"where a.bbr = b.operator_id " +
					"  and b.org_id = c.org_id ";
			if("1".equals(apply_status)){
				sql += " and apply_status = 0 ";
			}else if("2".equals(apply_status)){
				sql += " and apply_status = 1 ";
			}else if("3".equals(apply_status)){
				sql += " and apply_status in(2, 3, -1) ";
			}
			if(!"".equals(org_id)){
				sql += " and c.org_id = ? ";
				paramList.add(org_id);
			}
			if("".equals(search_date)){//当天
				sql += " and date_format(a.record_date, '%Y-%m-%d') = date_format(SYSDATE(), '%Y-%m-%d') ";
			} else if("1".equals(search_date)){//一周内
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -1 week), '%Y-%m-%d') ";
			}else if("2".equals(search_date)){//一个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -1 month), '%Y-%m-%d') ";
			}else if("3".equals(search_date)){//三个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -3 month), '%Y-%m-%d') ";
			}else if("4".equals(search_date)){//六个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -6 month), '%Y-%m-%d') ";
			}else if("5".equals(search_date)){//12个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -12 month), '%Y-%m-%d') ";
			}
			return db.getForListForMobile(str.getSql(sql, paramList), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("2".equals(bb_type)){
			sql = "SELECT " +
					"	a.id apply_id, " +
					"  b.name bbr_name, " +
					"	DATE_FORMAT(a.begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	DATE_FORMAT(a.bb_end_date, '%Y-%m-%d %H:%i:%s') bb_end_date, " +
					"	a.bb_day_cnt, " +
					"	a.bb_address, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30004' and dd_item_code = qj_type) qj_type, " +
					"	a.jj_lxr_name,  " +
					"	a.jj_lxr_phone, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.reject_reason, " +
					"	case a.apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	a.deal_oper_id,"
					+ " a.deal_lev " +
					"FROM " +
					"	t_qjbb_apply a, " +
					"	t_user b, " +
					"	t_organization c " +
					"where a.bbr = b.operator_id " +
					"  and b.org_id = c.org_id ";
			if("1".equals(apply_status)){
				sql += " and apply_status = 0 ";
			}else if("2".equals(apply_status)){
				sql += " and apply_status = 1 ";
			}else if("3".equals(apply_status)){
				sql += " and apply_status in(2, 3, -1) ";
			}
			if(!"".equals(org_id)){
				sql += " and c.org_id = ? ";
				paramList.add(org_id);
			}
			if("".equals(search_date)){//当天
				sql += " and date_format(a.record_date, '%Y-%m-%d') = date_format(SYSDATE(), '%Y-%m-%d') ";
			} else if("1".equals(search_date)){//一周内
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -1 week), '%Y-%m-%d') ";
			}else if("2".equals(search_date)){//一个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -1 month), '%Y-%m-%d') ";
			}else if("3".equals(search_date)){//三个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -3 month), '%Y-%m-%d') ";
			}else if("4".equals(search_date)){//六个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -6 month), '%Y-%m-%d') ";
			}else if("5".equals(search_date)){//12个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -12 month), '%Y-%m-%d') ";
			}
			return db.getForListForMobile(str.getSql(sql, paramList), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}else if("3".equals(bb_type)){
			sql = "SELECT " +
					"	a.id apply_id, " +
					"  b.name bbr_name, " +
					"	DATE_FORMAT(a.begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30005' and dd_item_code = a.yc_reason) yc_reason, " +
					"	(select dd_item_name from t_ddw t where data_type_code = '30006' and dd_item_code = a.car_type) car_type, " +
					"	a.route, " +
					"	a.address, " +
					"	a.txry_name, " +
					"	a.remark, " +
					"	a.apply_status, " +
					"	a.reject_reason, " +
					"	case a.apply_status when 0 then '待审核' when 1 then '待归家' when 2 then '已归队' when 3 then '已归队(隔天)' when -1 then '驳回' end apply_status_name, " +
					"	a.deal_oper_id,"
					+ " a.deal_lev " +
					"FROM " +
					"	t_ycbb_apply a, " +
					"	t_user b, " +
					"	t_organization c " +
					"where a.bbr = b.operator_id " +
					"  and b.org_id = c.org_id ";
			if("1".equals(apply_status)){
				sql += " and apply_status = 0 ";
			}else if("2".equals(apply_status)){
				sql += " and apply_status = 1 ";
			}else if("3".equals(apply_status)){
				sql += " and apply_status in(2, 3, -1) ";
			}
			if(!"".equals(org_id)){
				sql += " and c.org_id = ? ";
				paramList.add(org_id);
			}
			if("".equals(search_date)){//当天
				sql += " and date_format(a.record_date, '%Y-%m-%d') = date_format(SYSDATE(), '%Y-%m-%d') ";
			} else if("1".equals(search_date)){//一周内
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -1 week), '%Y-%m-%d') ";
			}else if("2".equals(search_date)){//一个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -1 month), '%Y-%m-%d') ";
			}else if("3".equals(search_date)){//三个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -3 month), '%Y-%m-%d') ";
			}else if("4".equals(search_date)){//六个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -6 month), '%Y-%m-%d') ";
			}else if("5".equals(search_date)){//12个月
				sql += " and date_format(a.record_date, '%Y-%m-%d') >= date_format(date_add(SYSDATE(), interval -12 month), '%Y-%m-%d') ";
			}
			return db.getForListForMobile(str.getSql(sql, paramList), Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		}
		
		return null;
	}
}
