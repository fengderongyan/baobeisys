package baobeisys.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;
import com.sgy.util.net.IpHelper;

/**
 * 角色管理
 * @date 2018-12-22
 */
@Service("roleCfgService")
public class RoleCfgService extends BaseService {
	
	/**
	 * 查询角色列表
	 * @param request
	 * @return
	 */
	public List<?> getRolesList(HttpServletRequest request) {
        String roleName = req.getValue(request, "role_name");//角色
        String status = req.getValue(request, "status");//状态
        int roleLevel = this.getUser(request).getRoleLevel();
        List<String> paramsList = new ArrayList<String>();
        int pageSize = req.getPageSize(request, "pageSize");
        
		String sql = "select a.role_id, " +
		             "       a.role_name, " + 
		             "       a.role_order, " +
		             "       a.status, " + 
		             "       (case " + 
		             "         when a.status = 1 then " + 
		             "          '有效' " + 
		             "         when a.status = 0 then " + 
		             "          '无效' " + 
		             "       end) status_name, " + 
		             "       fn_getusername(a.record_id) record_name, " + 
		             "       DATE_FORMAT(a.record_date, '%Y-%m-%d %h:%i:%s') record_date " + 
		             "  from t_role a " + 
		             " where 1 = 1 ";
		
		if(roleLevel == 0 ){//超级管理员
			sql += " and 1 = 1 ";
		}else if(roleLevel == 1){//系统管理员
			sql += " and role_level >= 1 ";
		}else{
			sql += " and 1 = 2 ";
		}
		
        if(!roleName.equals("")) {
            sql += " and a.role_name like ? ";
            paramsList.add("%" + roleName + "%");
        }
        if(!status.equals("")) {
            sql += " and a.status = ? ";
            paramsList.add(status);
        }
        sql += " order by a.role_order ";
        
        return db.getForList(sql, paramsList, pageSize, request);
    }
	
	/**
     * 根据节点获取权限列表
     * @param module_id
     * @return
     */
    public List getRoleList(){
        String sql = "select * from t_role a where a.status = 1 order by a.role_order";
        return db.queryForList(sql);
    }
	
	
	/**
	 * 得到工号对应操作员具体信息
	 * @param request 
	 * @return
	 */
	public Map getRoleInfo(HttpServletRequest request) {
        String roleId = req.getValue(request, "role_id");
        String sql = "select role_id, " +
                     "             role_name, " + 
                     "             role_order, " + 
                     "             status, " + 
                     "             remark, " + 
                     "             role_level, " + 
                     "             record_id, " + 
                     "             record_date " + 
                     "        from t_role " + 
                     "       where role_id = ? ";
        
        logger.debug("map : " + str.getSql(sql, new Object[]{roleId}));
        return db.queryForMap(sql, new Object[]{roleId});
    }
    
    /**
     * 新增、修改信息
     * @param request
     * @return
     */
    public int saveOrUpdateRole(HttpServletRequest request) {
        String operator = this.getOperatorId(request);
        String method = req.getValue(request, "method");
        String roleName = req.getValue(request, "role_name");//状态
        String status = req.getValue(request, "status");//状态
        String roleOrder = req.getValue(request, "role_order");//角色排序
        String roleId = req.getValue(request, "roleid");;//角色ID
        String sql = "";
        BatchSql batchSql = new BatchSql();
        if (method.equals("create")) {
            sql = "insert into t_role " +
                  "         (role_name, " + 
                  "          role_order, " + 
                  "          status, " + 
                  "          role_level, " + 
                  "          record_id, " + 
                  "          record_date) " + 
                  "       values " + 
                  "         (?, ?, ?, 1, ?, sysdate()) ";
            logger.debug("角色新增： " + str.getSql(sql, new Object[] {roleName, roleOrder, status, operator}));
            batchSql.addBatch(sql, new Object[] {roleName, roleOrder, status, operator});
        } else if (method.equals("edit")) {
            //修改角色信息
            sql = "update t_role " +
                  "   set role_name   = ?, " + 
                  "       role_order  = ?, " + 
                  "       status      = ?, " + 
                  "       record_id   = ?, " + 
                  "       record_date = sysdate() " + 
                  " where role_id = ? ";
            logger.debug("角色修改： " + str.getSql(sql, new Object[] {roleName, roleOrder, status, operator, roleId}));
            batchSql.addBatch(sql, new Object[] {roleName, roleOrder, status, operator, roleId});
        }
        
        return db.doInTransaction(batchSql);
    }
    
    /**
     * 删除角色信息
     * @param request
     * @return
     */
    public int delRole(HttpServletRequest request) {
        String roleId = req.getValue(request, "role_id");//角色名称
        String ipAddress = IpHelper.getIpAddr(request);//IP地址
        String operatorId = this.getUser(request).getOperatorId();//操作人
        System.out.println("operatorId:" + operatorId);
        String sql = "";
        BatchSql batchSql = new BatchSql();
        
        sql = "delete from t_role where role_id = ? ";
        logger.debug("del t_role : " + str.getSql(sql, new Object[]{roleId}));
        batchSql.addBatch(sql, new Object[]{roleId});
        
        //删除对应角色权限视野信息
        sql = "insert into t_role_module_his " +
              "  (operator_id, operate_time, client_address, module_id, role_id) " + 
              "  select ?, sysdate(), ?, module_id, role_id from t_role_module where role_id = ? ";
        logger.debug("t_role_module_his : " + str.getSql(sql, new Object[]{operatorId, ipAddress, roleId}));
        batchSql.addBatch(sql, new Object[]{operatorId, ipAddress, roleId});
        
        sql = "delete from t_role_module where role_id = ? ";
        batchSql.addBatch(sql, new Object[] {roleId});
        
        return db.doInTransaction(batchSql);
    }
	
}
