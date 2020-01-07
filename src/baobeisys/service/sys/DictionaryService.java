package baobeisys.service.sys;

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

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;

/**
 * 字典配置
 * @date 2018-12-25
 */
@Service("dictionaryService")
public class DictionaryService extends BaseService {
    
	/**
	 * 获取字典类型列表
	 * @return
	 */
	public List<?> getDictionaryType(HttpServletRequest request){
		String sql = "select  " +
					"	DISTINCT data_type_code, data_type_name  " +
					"from t_ddw " +
					"where 1 = 1 ";
		if(this.getUser(request).getRoleLevel() == 0){
			sql += " and 1 = 1 ";
		}else{
			sql += " and dd_lev >= 1 ";
		}
		sql += "order by DATA_TYPE_CODE ";
		return db.queryForList(sql);
	}
	
    /**
     * 信息列表
     * @param request
     * @return
     */
    public List<?> getDictionaryList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String data_type_code = req.getValue(request, "data_type_code");//字典类型
        String operatorId = this.getUser(request).getOperatorId();
        String dd_item_name = req.getValue(request, "dd_item_name");//字典信息名称
        int roleLevel = this.getUser(request).getRoleLevel();
        String sql = "select  " +
	        		"	a.DATA_TYPE_CODE, a.DATA_TYPE_NAME, a.DD_ITEM_CODE, a.DD_ITEM_NAME, id " +
	        		"from t_ddw a " +
	        		"where 1 = 1 ";
        if(roleLevel == 0){//超级管理员
        	sql += "and 1 = 1 ";
        }else if(roleLevel == 1){//系统级别管理员
        	sql += " and dd_lev >= 1 ";
        }else{
        	sql += " and 1 = 2 ";
        }
       
        if(!"".equals(data_type_code)) {
            sql += " and a.data_type_code = ?";
            paramsList.add(data_type_code);
        }
        
        if(!"".equals(dd_item_name)) {
            sql += " and a.dd_item_name like  ? ";
            paramsList.add("%" + dd_item_name + "%");
        }
        sql += " order by a.DATA_TYPE_CODE, a.order_id ";
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取指定类型最大信息编码
     * @param typeId
     * @return
     */
    public String getMaxIdByType(String typeId){
    	String sql = " select MAX(dd_item_code) from t_ddw where DATA_TYPE_CODE = ? ";
    	sql = str.getSql(sql, new Object[]{typeId});
    	return db.queryForString(sql);
    }
    
    /**
     * 获取字典配置信息
     * @param request
     * @return
     */
    public Map<String, Object> getDicInfo(HttpServletRequest request){
    	String id = req.getValue(request, "id");
    	String sql = " select * from t_ddw where id = ? ";
    	return db.queryForMap(sql, new Object[]{id});
    }
    
    /**
     * 验证信息编号是否存在
     * @param request
     * @return
     */
    public int checkInfo(HttpServletRequest request) {
        String dd_item_code = req.getValue(request, "dd_item_code");//信息编码
        String data_type_code = req.getValue(request, "data_type_code");//类型
        String sql = "select count(1) from t_ddw a where a.dd_item_code = ? and a.data_type_code = ? ";
        //如果新组织编号跟上次的组织编号一样，说明组织没变过，就要排除掉本组织编号
        String id = req.getValue(request, "id");
        if(!id.equals("")){
            sql += " and a.id <> '"+id+"'";
        }
        return db.queryForInt(sql, new Object[] {dd_item_code, data_type_code});
    }
    
    /**
	 * 保存信息
	 * @param request
	 * @return
	 */
	public int saveOrUpdate(HttpServletRequest request) {
		String method = req.getValue(request, "method");
        String data_type_code = req.getValue(request, "data_type_code"); //字典类型编码
        String dd_item_code = req.getValue(request, "dd_item_code");//信息编码
        String dd_item_name = req.getValue(request, "dd_item_name"); //信息名称
        String order_id = req.getValue(request, "order_id"); //排序
        String id = req.getAjaxValue(request, "dicId");
        String sql = "";
        BatchSql batchSql = new BatchSql();
		if ("create".equals(method)) {//新增
			sql = "insert into t_ddw " +
            		"(data_type_name, data_type_code, dd_item_name, dd_item_code, dd_lev, order_id) " +
            		"values((select DISTINCT data_type_name from (select data_type_name from t_ddw where DATA_TYPE_CODE = ?) b), ?, ?, ?, 1, ?)" ;
            batchSql.addBatch(sql, new Object[] {data_type_code, data_type_code, dd_item_name, dd_item_code, order_id});
		} else {//修改
            sql = " update t_ddw set dd_item_name = ?, order_id = ? where id = ? ";
            batchSql.addBatch(sql, new Object[]{dd_item_name, order_id, id});
		}
        return db.doInTransaction(batchSql);
	}
	
	/**
     * 删除字典信息
     * @param request
     * @return
     */
    public int deleteDictionaryCfg(HttpServletRequest request) {
        String id = req.getValue(request, "id");
        BatchSql batchSql = new BatchSql();
        String sql = "";
        sql = " delete from t_ddw where id = ? ";
        batchSql.addBatch(sql, new Object[] {id});
        return db.doInTransaction(batchSql);
    }

}
