package baobeisys.service.yjcfg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.util.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import web.service.BaseService;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.sgy.util.common.DateHelper;
import com.sgy.util.common.FileHelper;
import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;
import com.sgy.util.db.ProcHelper;
import com.sgy.util.excel.HssfHelper;

import baobeisys.action.sys.UserCfgController;
import baobeisys.action.yjcfg.YjCfgController;

/**
 * 用户管理
 * @date 2016-2-22
 */
@Service("yjCfgService")
public class YjCfgService extends BaseService {
	
	/**
	 * 查询用户列表
	 * @param request
	 * @return
	 */
	public List<?> getUserList(HttpServletRequest request) {
	    String bb_type = req.getValue(request, "bb_type");//报备类型1：饮酒报备 2：请假报备 3:用车报备
	    String operator_id = req.getValue(request, "operator_id");//编号
        String name = req.getValue(request, "name");//姓名
        String role_id = req.getValue(request, "role_id");//角色
        String org_name = req.getValue(request, "org_name");//归属组织
        String mobile = req.getValue(request, "mobile");//手机号码
        String status = req.getValue(request, "status");//状态
        List<String> paramsList = new ArrayList<String>();//查询参数
		String sql = " select a.operator_id, a.name, h.org_id, a.mobile, a.status, a.qm_name, a.qm_name_d, a.qm_url, " +
					 "         h.org_name, group_concat(b.role_name) roleNames " + 
		             "         from t_user a, t_role b, t_user_role c, t_organization h " + 
		             "        where a.operator_id = c.operator_id " + 
		             "          and b.role_id = c.role_id " + 
		             "          and a.org_id = h.org_id ";
		
		if(this.getUser(request).getRoleLevel() == 0){//超级管理员
		    sql += " and 1 = 1 ";
		}else{
		    sql += " and a.operator_id != 159999 ";
		}
		
        if(!operator_id.equals("")) {
            sql += " and a.operator_id=? ";
            paramsList.add(operator_id);
        }
        
        if(!name.equals("")) {
            sql += " and a.name like ? ";
            paramsList.add("%" + name + "%");
        }
        
        if(!org_name.equals("")) {
            sql += " and h.org_name like ? ";
            paramsList.add("%" + org_name + "%");
        }
        
        if(!mobile.equals("")) {
            sql += " and a.mobile = ? ";
            paramsList.add(mobile);
        }
        
        if(!status.equals("")) {
            sql += " and a.status = ? ";
            paramsList.add(status);
        }
        
        if(!role_id.equals("")) {
            sql += " and b.role_id = ? ";
            paramsList.add(role_id);
        }
        
        
        sql += " group by a.operator_id, a.name, h.org_id, h.org_name, a.mobile, a.status ";
        
        String sql2 = " select e.apply_oper_id, group_concat(e.deal_oper_name order by e.deal_lev SEPARATOR '-->') deal_oper_name from t_yjcfg_info e where ifnull(deal_oper_id, '') != '' and bb_type = ? group by e.apply_oper_id ";
        paramsList.add(bb_type);
        String sql3 = " select t.*, s.deal_oper_name from (" + sql + ") t left join (" + sql2 + ") s on t.operator_id = s.apply_oper_id order by t.org_id,t.operator_id ";
        //注意设置mysql ： SET GLOBAL group_concat_max_len=102400
        request.setAttribute("bb_type", bb_type);
        return db.queryForList(sql3, paramsList.toArray());
    }
	
	
  
    /**
     * 获取角色列表
     * @param request
     * @return 
     */
    public List getRoleList(HttpServletRequest request){
        List<String> paramsList = new ArrayList<String>(); // 查询参数
        String user_id = this.getUser(request).getOperatorId();
        this.getUser(request).getRoleIds();
        String sql = "";
        sql = " select a.role_id, a.role_name " +
              "    from t_role a, " + 
              "         (select min(role_level) role_level " + 
              "             from t_user_role c, t_role e " + 
              "            where operator_id = ? " + 
              "              and c.role_id = e.role_id) b " + 
              "   where a.status = 1  and a.role_level >= b.role_level";
        paramsList.add(user_id);
//        //视野权限控制
//        if(this.getUser(request).getRoleLevel() == 0){//系统管理员
//            sql += " and a.role_level >= b.role_level ";
//        }else {//其他权限
//            sql += " and a.role_level > b.role_level ";
//        }
        sql += " order by a.role_level, a.role_order ";
        logger.debug("sql :" + str.getSql(sql, paramsList));

        return db.queryForList(sql,paramsList);
    }
    
    public List getDDw(String data_type_code){
    	String sql = " select dd_item_code, dd_item_name from t_ddw where data_type_code = ? order by order_id ";
    	return db.queryForList(sql, new Object[]{data_type_code});
    }



	public List<?> getOrgList() {
		String sql = " select org_id id, org_name name, superior_id pId from t_organization where status = 1 order by org_lev, org_id ";
		return db.queryForList(sql);
	}



	public List getUserListByOrgId(HttpServletRequest request) {
		String org_id = req.getValue(request, "org_id");
		String operator_id = req.getValue(request, "operator_id");//编号
        String name = req.getValue(request, "name");//姓名
        String role_id = req.getValue(request, "role_id");//角色
        String org_name = req.getValue(request, "org_name");//归属组织
        String mobile = req.getValue(request, "mobile");//手机号码
        String status = req.getValue(request, "status");//状态
        List<String> paramsList = new ArrayList<String>();//查询参数
		String sql = " select a.operator_id, a.name, h.org_id, a.mobile, a.status, a.qm_name, a.qm_name_d, a.qm_url, " +
					 "         h.org_name, group_concat(b.role_name) roleNames " + 
		             "         from t_user a, t_role b, t_user_role c, t_organization h " + 
		             "        where a.operator_id = c.operator_id " + 
		             "          and b.role_id = c.role_id " + 
		             "          and a.org_id = h.org_id " + 
		             "			and h.org_id = ? " +
					 "			and a.status = 1 ";
		paramsList.add(org_id);
		if(this.getUser(request).getRoleLevel() == 0){//超级管理员
		    sql += " and 1 = 1 ";
		}else{
		    sql += " and a.operator_id != 159999 ";
		}
		
        if(!operator_id.equals("")) {
            sql += " and a.operator_id=? ";
            paramsList.add(operator_id);
        }
        
        if(!name.equals("")) {
            sql += " and a.name like ? ";
            paramsList.add("%" + name + "%");
        }
        
        if(!org_name.equals("")) {
            sql += " and h.org_name like ? ";
            paramsList.add("%" + org_name + "%");
        }
        
        if(!mobile.equals("")) {
            sql += " and a.mobile = ? ";
            paramsList.add(mobile);
        }
        
        if(!role_id.equals("")) {
            sql += " and b.role_id = ? ";
            paramsList.add(role_id);
        }
        sql += " group by a.operator_id, a.name, h.org_id, h.org_name, a.mobile, a.status ";
		return db.queryForList(sql, paramsList);
	}



	public List getDealUserList(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");//报备类型
		String deal_lev = req.getValue(request, "deal_lev");
    	String operatorIdStr = req.getValue(request, "operatorIdStr");
    	List dealUserList = new ArrayList();
    	if(!"".equals(operatorIdStr)){
        	String sql = " select deal_oper_id, deal_oper_name, count(deal_oper_id) operCnt from t_yjcfg_info a "
        			+ " where apply_oper_id in(" + operatorIdStr + ") and deal_lev = ? and bb_type = ? "
        			+ " group by deal_oper_id, deal_oper_name ";
        	List dealOperList = db.queryForList(sql, new Object[]{deal_lev, bb_type});
        	if(dealOperList != null && dealOperList.size() == 1){//说明被选择的人员都是由同一批人审核
        		Map map = (Map)dealOperList.get(0);
        		String deal_oper_name = str.get(map, "deal_oper_name");
        		String deal_oper_id = str.get(map, "deal_oper_id");
        		String operCnt = str.get(map, "operCnt");
        		if(!"".equals(deal_oper_id)){
        			String[] deal_oper_names = deal_oper_name.split(",");
            		String[] deal_oper_ids = deal_oper_id.split(",");
            		if((String.valueOf(operatorIdStr.split(",").length)).equals(operCnt)){//选择的人数与查询出的人数相同
            			for(int i = 0; i < deal_oper_ids.length; i++){
                			Map tmpMap = new HashMap();
                			tmpMap.put("deal_oper_id", deal_oper_ids[i]);
                			tmpMap.put("deal_oper_name", deal_oper_names[i]);
                			dealUserList.add(tmpMap);
                		}
            		} 
        			
        		}
        	}
    	}
		return dealUserList;
	}



	public List deleteDealUser(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");
		String deal_lev = req.getValue(request, "deal_lev");
		String deal_oper_id = req.getValue(request, "deal_oper_id");
		String operatorIdStr = req.getValue(request, "operatorIdStr");
		String[] operartorIdArr = operatorIdStr.split(",");
		String sql = " select distinct deal_oper_id from t_yjcfg_info a "
				+ " where apply_oper_id in(" + operatorIdStr + ") and deal_lev = ? and bb_type = ? ";
		List dealOperList = db.queryForList(sql, new Object[]{deal_lev, bb_type});
		Map dealOperMap = (Map)dealOperList.get(0);
		String deal_oper_ids = str.get(dealOperMap, "deal_oper_id");
		deal_oper_ids = ("," + deal_oper_ids + ",").replace("," + deal_oper_id + ",", ",");
		if(deal_oper_ids.length() > 2){//长度超过2，说明还有数据
			deal_oper_ids = deal_oper_ids.substring(1, deal_oper_ids.lastIndexOf(","));
		}else{
			deal_oper_ids = "";
		}
		String deal_oper_name = "";
		List dealUserList = new ArrayList();
		if(!"".equals(deal_oper_ids)){
			String[] dealOperIdArr = deal_oper_ids.split(",");
			for(int i = 0; i < dealOperIdArr.length; i++){
				String dealSql = " select name from t_user where operator_id = ? ";
				String name = db.queryForString(dealSql, new Object[]{dealOperIdArr[i]});
				Map tmpMap  = new HashMap();
				tmpMap.put("deal_oper_id", dealOperIdArr[i]);
    			tmpMap.put("deal_oper_name", name);
    			dealUserList.add(tmpMap);
				deal_oper_name = deal_oper_name + "," + name;
			}
			if(!"".equals(deal_oper_name)){
				deal_oper_name =deal_oper_name.substring(1);
			}
		}
		
		BatchSql batchSql = new BatchSql();
		
		sql = " delete from t_yjcfg_info where apply_oper_id in(" + operatorIdStr + ") and deal_lev = ? and bb_type = ? ";
		batchSql.addBatch(sql, new Object[]{deal_lev, bb_type});
		if(!"".equals(deal_oper_ids)){
			for (int i = 0; i < operartorIdArr.length; i++) {
				String id = this.get32UUID();
				sql = " insert into t_yjcfg_info(id,bb_type,apply_oper_id,deal_oper_id, deal_oper_name, deal_lev, record_id, record_date) "
						+ " values(?,?,?,?,?,?,?,SYSDATE()) ";
				batchSql.addBatch(sql, new Object[]{id,bb_type,operartorIdArr[i],deal_oper_ids, deal_oper_name, deal_lev, this.getOperatorId(request)});
			}
		}
		db.doInTransaction(batchSql);
		return dealUserList;
	}



	public List addDealOper(HttpServletRequest request) {
		String bb_type = req.getValue(request, "bb_type");
		String flag_reset = req.getValue(request, "flag_reset");
		String deal_lev = req.getValue(request, "deal_lev");
		String deal_oper_id = req.getValue(request, "deal_oper_id");
		String deal_oper_name = req.getValue(request, "deal_oper_name");
		String operatorIdStr = req.getValue(request, "operatorIdStr");
		String[] operartorIdArr = operatorIdStr.split(",");
		BatchSql batchSql = new BatchSql();
		String sql = "";
		if("1".equals(flag_reset)){//全部重置重新选
			sql = " delete from t_yjcfg_info where apply_oper_id in(" + operatorIdStr + ") and deal_lev = ? and bb_type = ? ";
			batchSql.addBatch(sql, new Object[]{deal_lev, bb_type});
			for (int i = 0; i < operartorIdArr.length; i++) {
				String id = this.get32UUID();
				sql = " insert into t_yjcfg_info(id,bb_type,apply_oper_id,deal_oper_id, deal_oper_name, deal_lev, record_id, record_date) "
						+ " values(?,?,?,?,?,?,?,SYSDATE()) ";
				batchSql.addBatch(sql, new Object[]{id,bb_type,operartorIdArr[i],deal_oper_id, deal_oper_name, deal_lev, this.getOperatorId(request)});
			}
		}else{//继续添加
			sql = " update t_yjcfg_info "
					+ " set deal_oper_id = concat(deal_oper_id, ',', ?), "
					+ " 	deal_oper_name = concat(deal_oper_name, ',', ?), "
					+ " 	record_id = ?, "
					+ " 	record_date = SYSDATE() "
					+ " where apply_oper_id in (" + operatorIdStr + ") "
					+ "   and deal_lev = ? "
					+ "   and bb_type = ? ";
			batchSql.addBatch(sql, new Object[]{deal_oper_id, deal_oper_name, this.getOperatorId(request), deal_lev, bb_type});
		}
		db.doInTransaction(batchSql);
		//添加完成后，重新查询列表
		return getDealUserList(request);
	}

}
