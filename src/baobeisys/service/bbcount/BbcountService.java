package baobeisys.service.bbcount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import web.service.BaseService;


/**
 * 报备信息
 * @date 2019-1-17
 */
@Service("bbcountService")
public class BbcountService extends BaseService {
	
	/**
	 * 获取所有组织列表
	 * @param request
	 * @return
	 */
	public List<?> getAllOrgList(HttpServletRequest request) {
        
        String sql = "  select org_id, org_name from t_organization where STATUS = 1 and org_lev <> 2  order by org_lev, SUPERIOR_ID ";
        
        return db.queryForList(sql);
    }
    
    /**
     * 获取请假明细列表
     * @param request
     * @return
     */
    public List<?> getQjDetailList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String org_id = req.getValue(request, "org_id");//归属组织
        String begin_date = req.getValue(request, "begin_date");//报备时间（开始）
        String end_date = req.getValue(request, "end_date");//报备时间（结束）
        String bbr = req.getValue(request, "bbr");//报备人
        
        String sql = "SELECT " +
        		"	c.org_name, " +
        		"	b.name, " +
        		"	a.bb_day_cnt, " +
        		"	(select dd_item_name from t_ddw t where data_type_code = '30004' and t.dd_item_code = a.qj_type) qj_type, " +
        		"  date_format(a.begin_date, '%Y-%m-%d %H:%i:%s') begin_date, " +
        		"  date_format(a.end_date, '%Y-%m-%d %H:%i:%s') end_date " +
        		"FROM " +
        		"	t_qjbb_apply a, " +
        		"	t_user b, " +
        		"	t_organization c " +
        		"WHERE " +
        		"	apply_status IN (1, 2, 3) " +
        		"AND a.bbr = b.operator_id " +
        		"AND b.status = 1 " +
        		"AND b.ORG_ID = c.ORG_ID ";

        if(!"".equals(org_id)) {
            sql += " and c.org_id = ?";
            paramsList.add(org_id);
        }
        
        if(!"".equals(begin_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') >= ? ";
            paramsList.add(begin_date);
        }
        
        if(!"".equals(end_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') <=  ? ";
            paramsList.add(end_date);
        }
        
        if(!"".equals(bbr)) {
            sql += " and b.name like concat('%', ?, '%') ";
            paramsList.add(bbr);
        }
        sql += " order by a.begin_date desc ";
        logger.debug("qj detail : " +  str.getSql(sql, paramsList.toArray()));
        //return db.queryForList(sql, paramsList.toArray());
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取饮酒明细列表
     * @param request
     * @return
     */
    public List<?> getYjDetailList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String org_id = req.getValue(request, "org_id");//归属组织
        String begin_date = req.getValue(request, "begin_date");//报备时间（开始）
        String end_date = req.getValue(request, "end_date");//报备时间（结束）
        String bbr = req.getValue(request, "bbr");//报备人
        
        String sql = "SELECT " +
	        		"	c.org_name, " +
	        		"	b.name, " +
	        		"	(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type, " +
	        		"  date_format(a.start_date, '%Y-%m-%d %H:%i:%s') start_date " +
	        		"FROM " +
	        		"	t_yjbb_apply a, " +
	        		"	t_user b, " +
	        		"	t_organization c " +
	        		"WHERE " +
	        		"	apply_status IN (1, 2, 3) " +
	        		"AND a.bbr = b.operator_id " +
	        		"AND b.status = 1 " +
	        		"AND b.ORG_ID = c.ORG_ID ";

        if(!"".equals(org_id)) {
            sql += " and c.org_id = ? ";
            paramsList.add(org_id);
        }
        
        if(!"".equals(begin_date)) {
            sql += " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= ? ";
            paramsList.add(begin_date);
        }
        
        if(!"".equals(end_date)) {
            sql += " and DATE_FORMAT(a.start_date, '%Y-%m-%d') <=  ? ";
            paramsList.add(end_date);
        }
        
        if(!"".equals(bbr)) {
            sql += " and b.name like concat('%', ?, '%') ";
            paramsList.add(bbr);
        }
        sql += " order by a.start_date desc ";
        logger.debug("yj detail : " +  str.getSql(sql, paramsList.toArray()));
        //return db.queryForList(sql, paramsList.toArray());
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取用车明细列表
     * @param request
     * @return
     */
    public List<?> getYcDetailList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String org_id = req.getValue(request, "org_id");//归属组织
        String begin_date = req.getValue(request, "begin_date");//报备时间（开始）
        String end_date = req.getValue(request, "end_date");//报备时间（结束）
        String bbr = req.getValue(request, "bbr");//报备人
        
        String sql = "SELECT " +
        		"	c.org_name, " +
        		"	b.name, " +
        		"	(select dd_item_name from t_ddw t where data_type_code = '30005' and t.dd_item_code = a.yc_reason) yc_reason, " +
        		"  (select dd_item_name from t_ddw t where data_type_code = '30006' and t.dd_item_code = a.car_type) car_type, " +
        		"  date_format(a.begin_date, '%Y-%m-%d %H:%i:%s') begin_date " +
        		"FROM " +
        		"	t_ycbb_apply a, " +
        		"	t_user b, " +
        		"	t_organization c " +
        		"WHERE " +
        		"	apply_status IN (1, 2, 3) " +
        		"AND a.bbr = b.operator_id " +
        		"AND b.status = 1 " +
        		"AND b.ORG_ID = c.ORG_ID ";

        if(!"".equals(org_id)) {
            sql += " and c.org_id = ?";
            paramsList.add(org_id);
        }
        
        if(!"".equals(begin_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') >= ? ";
            paramsList.add(begin_date);
        }
        
        if(!"".equals(end_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') <=  ? ";
            paramsList.add(end_date);
        }
        
        if(!"".equals(bbr)) {
            sql += " and b.name like concat('%', ?, '%') ";
            paramsList.add(bbr);
        }
        sql += " order by a.begin_date desc ";
        logger.debug("qj detail : " +  str.getSql(sql, paramsList.toArray()));
        //return db.queryForList(sql, paramsList.toArray());
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    
    /**
     * 获取请假汇总列表
     * @param request
     * @return
     */
    public List<?> getQjTotalList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String org_id = req.getValue(request, "org_id");//归属组织
        String begin_date = req.getValue(request, "begin_date");//报备时间（开始）
        String end_date = req.getValue(request, "end_date");//报备时间（结束）
        String bbr = req.getValue(request, "bbr");//报备人
        
        String sql = "SELECT  " +
        		"		c.org_name,  " +
        		"		b.name,  " +
        		"		(select dd_item_name from t_ddw t where data_type_code = '30004' and t.dd_item_code = a.qj_type) qj_type, " +
        		"	  count(1) cnt, " +
        		"    sum(a.bb_day_cnt) total_day " +
        		"	FROM  " +
        		"		t_qjbb_apply a,  " +
        		"		t_user b,  " +
        		"		t_organization c  " +
        		"	WHERE  " +
        		"		apply_status IN (1, 2, 3)  " +
        		"	AND a.bbr = b.operator_id  " +
        		"	AND b.status = 1  " +
        		"	AND b.ORG_ID = c.ORG_ID";
        if(!"".equals(org_id)) {
            sql += " and c.org_id = ?";
            paramsList.add(org_id);
        }
        
        if(!"".equals(begin_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') >= ? ";
            paramsList.add(begin_date);
        }
        
        if(!"".equals(end_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') <=  ? ";
            paramsList.add(end_date);
        }
        
        if(!"".equals(bbr)) {
            sql += " and b.name like  ? ";
            paramsList.add("%" + bbr + "%");
        }
        sql += " group by c.org_id, c.org_name,a.bbr,b.name,qj_type order by c.org_id,a.bbr ";
        logger.debug("qj total : " +  str.getSql(sql, paramsList.toArray()));
        //return db.queryForList(sql, paramsList.toArray());
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取饮酒汇总列表
     * @param request
     * @return
     */
    public List<?> getYjTotalList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String org_id = req.getValue(request, "org_id");//归属组织
        String begin_date = req.getValue(request, "begin_date");//报备时间（开始）
        String end_date = req.getValue(request, "end_date");//报备时间（结束）
        String bbr = req.getValue(request, "bbr");//报备人
        
        String sql = "SELECT  " +
        		"		c.org_name,  " +
        		"		b.name,  " +
        		"		(select dd_item_name from t_ddw t where data_type_code = '30003' and t.dd_item_code = a.yj_type) yj_type, " +
        		"	  count(1) cnt " +
        		"	FROM  " +
        		"		t_yjbb_apply a,  " +
        		"		t_user b,  " +
        		"		t_organization c  " +
        		"	WHERE  " +
        		"		apply_status IN (1, 2, 3)  " +
        		"	AND a.bbr = b.operator_id  " +
        		"	AND b.status = 1  " +
        		"	AND b.ORG_ID = c.ORG_ID ";
        if(!"".equals(org_id)) {
            sql += " and c.org_id = ?";
            paramsList.add(org_id);
        }
        
        if(!"".equals(begin_date)) {
            sql += " and DATE_FORMAT(a.start_date, '%Y-%m-%d') >= ? ";
            paramsList.add(begin_date);
        }
        
        if(!"".equals(end_date)) {
            sql += " and DATE_FORMAT(a.start_date, '%Y-%m-%d') <=  ? ";
            paramsList.add(end_date);
        }
        
        if(!"".equals(bbr)) {
            sql += " and b.name like  ? ";
            paramsList.add("%" + bbr + "%");
        }
        sql += " group by c.org_id, c.org_name, a.bbr, b.name, yj_type order by c.org_id,a.bbr ";
        logger.debug("yj total : " +  str.getSql(sql, paramsList.toArray()));
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取用车汇总列表
     * @param request
     * @return
     */
    public List<?> getYcTotalList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String org_id = req.getValue(request, "org_id");//归属组织
        String begin_date = req.getValue(request, "begin_date");//报备时间（开始）
        String end_date = req.getValue(request, "end_date");//报备时间（结束）
        String bbr = req.getValue(request, "bbr");//报备人
        
        String sql = "SELECT  " +
        		"		c.org_name,  " +
        		"		b.name,  " +
        		"		(select dd_item_name from t_ddw t where data_type_code = '30005' and t.dd_item_code = a.yc_reason) yc_reason, " +
        		"	  count(1) cnt " +
        		"	FROM  " +
        		"		t_ycbb_apply a,  " +
        		"		t_user b,  " +
        		"		t_organization c  " +
        		"	WHERE  " +
        		"		apply_status IN (1, 2, 3)  " +
        		"	AND a.bbr = b.operator_id  " +
        		"	AND b.status = 1  " +
        		"	AND b.ORG_ID = c.ORG_ID ";
        if(!"".equals(org_id)) {
            sql += " and c.org_id = ?";
            paramsList.add(org_id);
        }
        
        if(!"".equals(begin_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') >= ? ";
            paramsList.add(begin_date);
        }
        
        if(!"".equals(end_date)) {
            sql += " and DATE_FORMAT(a.begin_date, '%Y-%m-%d') <=  ? ";
            paramsList.add(end_date);
        }
        
        if(!"".equals(bbr)) {
            sql += " and b.name like  ? ";
            paramsList.add("%" + bbr + "%");
        }
        sql += " group by c.org_id, c.org_name, a.bbr, b.name, a.yc_reason order by c.org_id, a.bbr ";
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
}
