package baobeisys.service.module;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.db.BatchSql;
import com.sgy.util.net.IpHelper;
/**
 * 后台角色权限管理
 * @date 2016-2-22
 */
@Service("adminPermitService")
public class AdminPermitService  extends BaseService {
    
    /**
     * 保存模块权限配置
     * @param request
     * @return
     */
    public int saveRoleModule(HttpServletRequest request) {
        String role_id = req.getValue(request, "role_id");
        String moduleIds = req.getValue(request, "moduleIds");
        String operatorId = this.getUser(request).getOperatorId();//操作人
        String ipAddress = IpHelper.getIpAddr(request);//IP地址
        String[] module_ids = moduleIds.split(",");
        
        String sql="";
        BatchSql batch = new BatchSql();
        //菜单权限操作历史表
        sql="insert into t_role_module_his (operator_id, operate_time, client_address, module_id, role_id) " +
            "select ?,sysdate(),?,a.* from t_role_module a where a.role_id = ?";
        batch.addBatch(sql, new Object[]{operatorId, ipAddress,role_id});
        
        
        //把没选的节点删除掉
        sql = "delete from t_role_module where role_id = ? ";
        batch.addBatch(sql, new Object[] {role_id});
        
        
        //循环插入表里不存在的节点
        for(String module_id : module_ids){
            if(!"".equals(module_id)){
                sql = "insert into t_role_module (role_id, module_id) select ?,? from  dual ";
                batch.addBatch(sql, new Object[]{role_id, module_id});
            }
        }
        return db.doInTransaction(batch);
    }
    
    /**
     * 根据节点获取权限列表
     * @param module_id
     * @return
     */
    public List<?> getPremitByModId(String module_id){
        String sql = "select module_id, permit_id, permit_name, permit_code, permit_url, permit_order " +
                " from t_permit where module_id = ? and status=1 order by permit_order";
        return db.queryForList(sql, new Object[]{module_id});
    }
    
    /**
     * @description 根据模块编号或权限编号 获取已经选中的角色
     * @author YUANFY 2013-08-08
     * @param request
     * @return
     */
    public String getRolesByModuleId(HttpServletRequest request){
        String mpId = req.getValue(request, "module_id");
        String module_id = "";
        String permit_id = "";
       /*if(!"".equals(mpId)){
            if(mpId.indexOf("P")>0){
                permit_id = mpId.substring(0, mpId.indexOf("_"));
            }else if(mpId.indexOf("M")>0){
                module_id = mpId.substring(0, mpId.indexOf("_"));
            }
        }*/
        String sql = "";
        if(!"".equals(mpId)){
            sql = "select GROUP_CONCAT(a.role_id) from t_role_module a where a.module_id=? ";
            return db.queryForString(sql, new Object[]{mpId});
        }
        return "";
    }
    
    /**
     * 根据节点获取权限列表
     * @param module_id
     * @return
     */
    public List getRoleList(HttpServletRequest request){
        String sql = "select * from t_role a where a.status = 1 ";
        if(this.getUser(request).getRoleLevel() == 0){
        	sql += " and 1 = 1 ";
        }else{
        	sql += " and role_level >= 1 ";
        }
        sql += " order by a.role_order";
        logger.debug(sql);
        return db.queryForList(sql);
    }
    
    /**
     * @description 获得模块权限树
     * @author YUANFY 2013-08-08
     * @return
     */
    public List<?> getModulePermitList(HttpServletRequest request){
        String role_id = req.getValue(request, "role_id");
        int roleLevel = this.getUser(request).getRoleLevel();
        System.out.println("roleLevel:" + roleLevel);
        String sql ="select t.*    " +
	        		"from  " +
	        		"(select distinct a.module_id ,                 " +
	        		"		a.superior_id  superior_id,                 " +
	        		"		a.module_level,                 " +
	        		"		a.module_name,                 " +
	        		"		a.module_order,                 " +
	        		"		(case when a.module_url = '' then 'true' else 'false' end) is_parent,                 " +
	        		"		case when m.module_id is not null then 'true' else 'false' end checked            " +
	        		"	from t_module a  " +
	        		"  left join (select module_id from t_role_module  where role_id = ?) m  on a.module_id = m.module_id       " +
	        		"  where   a.status=1   " +
	        		"  order by a.module_order, a.module_id) t where 1 = 1";
        if(roleLevel == 0){
        	sql += " and 1 = 1";
        }else{
        	sql += " and t.module_id not in(select dd_item_code from t_ddw s where s.data_type_code = 20001) ";
        }
        sql = str.getSql(sql, new Object[]{role_id});
        logger.debug("模块权限数 ： " + sql );
        return db.queryForList(sql);
        //return db.queryForList(sql,new Object[]{role_id});
    }  
    
    /**
     * @description 模块角色保存
     * @param request
     * @return
     */
    public int saveModuleRole(HttpServletRequest request) {
        String roleIdStrs = req.getValue(request, "roleIdStrs");
        String mpId = req.getValue(request, "module_id");
        String permit_id = "",module_id = "";
        String sql="";
        BatchSql batch = new BatchSql();
        //保存角色模块配置
        String[] roleids = roleIdStrs.split(",");
        if(!"".equals(mpId)){
                //模块分配角色时，上级所有父节点都要分配选中的角色
                
        		//获取所有父节点编号
                String supmoduleid = "";
                sql ="select find_all_subsids(?) from dual ";
                String supid = db.queryForString(sql, new Object[]{mpId});
                String [] supIds = supid.split(",");
                for(int i = 0; i < supIds.length; i ++){
                	if(i%2 == 1){
                		supmoduleid += supIds[i] + ",";
                	}
                }
                supmoduleid = supmoduleid.substring(0, supmoduleid.length()-1);
                //删除原有模块权限信息
                sql = " delete from t_role_module where module_id = ? ";
                batch.addBatch(sql, new Object[]{mpId});
                if("".equals(supmoduleid)){
                    for(int i=0; i<roleids.length; i++){
                        if(!"".equals(roleids[i])){
                            sql = "insert into t_role_module(role_id, module_id) " +
                                " select ?,? from dual ";
                            batch.addBatch(sql, new Object[]{roleids[i], mpId});
                        }
                    }
                }else{
                    String[] moduleids = supmoduleid.split(",");
                    for(int i=0; i<moduleids.length; i++){
                        //把没选的模块并且关联到上级节点中没有子节点的模块配置删除
                        if(!"".equals(moduleids[i])){
                            sql = "delete from  " +
                            		"	t_role_module   " +
                            		"where module_id = ?  " +
                            		"and not exists   " +
                            		"	(select 1 from (select * from t_role_module a where a.module_id in   " +
                            		"			(select b.module_id from t_module b  " +
                            		"			where b.status = 1  and b.superior_id = ? and b.module_id <> ?) ) t ) ";
                            batch.addBatch(sql, new Object[]{moduleids[i],moduleids[i],mpId});
                        }
                    }
                    //选中当前节点，父节点角色同时增加当前子节点的角色（不删除父节点之前有的角色）
                    for(int i=0; i<moduleids.length; i++){
                        module_id = moduleids[i];
                        for(int j=0; j<roleids.length; j++){
                            if(!"".equals(roleids[j])){
                                sql = "insert into t_role_module(role_id, module_id) " +
                                    " select ?,? from dual " +
                                    " where not exists(select 1 from t_role_module b " +
                                    " where b.role_id=? and b.module_id=? )";
                                batch.addBatch(sql, new Object[]{roleids[j], module_id,roleids[j], module_id});
                            }
                        }
                    }
                }
            }else{
                return 0;
            }
        
        //return 1;
        return db.doInTransaction(batch);
    }
    
    public List getRoleList2(HttpServletRequest request){
    	int roleLevel = this.getUser(request).getRoleLevel();
    	String sql = " select role_id, role_name from t_role where status = 1 ";
    	if(roleLevel == 0){
    		sql += " and 1 = 1";
    	}else{
    		sql += " and role_level >=1 ";
    	}
    	return db.queryForList(sql);
    }
}
