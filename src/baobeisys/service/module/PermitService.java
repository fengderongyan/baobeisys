package baobeisys.service.module;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.db.BatchSql;
/**
 * 角色权限管理
 * @date 2016-2-22
 */
@Service("permitService")
public class PermitService  extends BaseService {
    
    /**
     * 保存模块权限配置
     * @param request
     * @return
     */
    public int saveRoleModule(HttpServletRequest request) {
        String role_id = req.getValue(request, "role_id");
        String moduleIds = req.getValue(request, "moduleIds");
        String[] module_ids = moduleIds.split(",");
        
        String sql="";
        BatchSql batch = new BatchSql();
        
        //把没选的节点删除掉
        sql = "delete from t_role_module where role_id = ? ";
        batch.addBatch(sql, new Object[] {role_id});
        //权限模块
        sql = "delete from t_role_permit a where a.role_id=? ";
        batch.addBatch(sql, new Object[] {role_id});
        
        String permitId, moduleId;
        
        //循环插入表里不存在的节点
        for(String module_id : module_ids){
            permitId = "";
            moduleId = "";
            if(module_id.indexOf("P")>0){
                permitId = module_id.substring(0, module_id.indexOf("_"));
                if(!"".equals(permitId)){
                    sql = "insert into t_role_permit(role_id, permit_id, module_id) " +
                        " select ?,?,a.module_id from t_permit a where rownum=1 and a.permit_id= ? ";
                    batch.addBatch(sql, new Object[]{role_id,permitId,permitId});
                }
            }else if(module_id.indexOf("M")>0){
                moduleId = module_id.substring(0, module_id.indexOf("_"));
                if(!"".equals(moduleId)){
                    sql = "insert into t_role_module (role_id, module_id)select ?,? from  dual ";
                    batch.addBatch(sql, new Object[]{role_id, moduleId});
                }
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
        if(!"".equals(mpId)){
            if(mpId.indexOf("P")>0){
                permit_id = mpId.substring(0, mpId.indexOf("_"));
            }else if(mpId.indexOf("M")>0){
                module_id = mpId.substring(0, mpId.indexOf("_"));
            }
        }
        String sql = "";
        if(!"".equals(permit_id)){
            sql = "select wmsys.wm_concat(a.role_id) from t_role_permit a where a.permit_id=? ";
            return db.queryForString(sql, new Object[]{permit_id});
        }else if(!"".equals(module_id)){
            sql = "select wmsys.wm_concat(a.role_id) from t_role_module a where a.module_id=? ";
            return db.queryForString(sql, new Object[]{module_id});
        }
        return "";
    }
    
    /**
     * 获取角色列表
     * @return
     */
    public List getRoleList(){
        String sql ="select a.group_name || '→' || c.role_name group_role_name,c.role_name,  " +
                    "       b.role_id,a.group_id,a.group_name " + 
                    "  from t_role_group a, t_role_group_detail b, t_role c " + 
                    " where a.group_id = b.group_id " + 
                    "   and b.role_id = c.role_id " + 
                    "   order by a.group_name,c.role_order";
        System.out.println(sql);
        return db.queryForList(sql);
    }
    
    /**
     * 获取角色组信息列表
     * @return
     */
    public List getRoleGroupList(){
        String sql ="select group_id, group_name " +
                    "  from t_role_group a " + 
                    " where a.status = 1 " + 
                    " order by a.group_name";
        return db.queryForList(sql);
    }
    
    /**
     * @description 获得模块权限树
     * @author YUANFY 2013-08-08
     * @return
     */
    public List<?> getModulePermitList(HttpServletRequest request){
        String role_id = req.getValue(request, "role_id");
        String sql ="select * " +
                    "  from (select distinct a.module_id || '_M' module_id, " + 
                    "               a.superior_id || '_M' superior_id, " + 
                    "               a.module_level, " + 
                    "               a.module_name, " + 
                    "               a.module_order, " + 
                    "               case when a.module_folder_flag = 1 then 'true' " + 
                    "                 when p.module_id is not null then 'true' " + 
                    "                 else 'false' end is_parent, " + 
                    "               case when p.permit_id is not null then '1' else '0' end has_permits, " + 
                    "               case when m.module_id is not null then 'true' else 'false' end checked " + 
                    "          from t_module a, " + 
                    "               (select module_id from t_role_module where role_id = ?) m, " + 
                    "               t_permit p " + 
                    "         where a.module_id = m.module_id(+) " + 
                    "          and  a.module_id= p.module_id(+) " + 
                    "          and a.status=1 "+
                    "          union all " + 
                    //以上加载功能菜单，以下加载模块权限菜单 
                    "        select b.permit_id || '_P' module_id, " + 
                    "               b.module_id || '_M' superior_id, " + 
                    "               99 module_level, " + 
                    "               b.permit_name, " + 
                    "               b.permit_order module_order, " + 
                    "               'false' is_parent, " + 
                    "               '0' has_permits, " + 
                    "               case when p.permit_id is not null then 'true' else 'false' end checked " + 
                    "          from t_permit b, " + 
                    "               (select permit_id, module_id " + 
                    "                  from t_role_permit " + 
                    "                 where role_id = ?) p " + 
                    "         where b.module_id = p.module_id(+) " + 
                    "           and b.permit_id = p.permit_id(+)) " + 
                    " order by module_level, module_order, module_id";
        return db.queryForList(sql,new Object[]{role_id,role_id});
    }  
    
    /**
     * @description 模块角色保存
     * @author YUANFY 2013-08-07
     * @param request
     * @return
     */
    public int saveModuleRole(HttpServletRequest request) {
        String roleIdStrs = req.getValue(request, "roleIdStrs");
        List<String> paramList = new ArrayList<String>();
        String inSql = db.rebuildInSql(roleIdStrs, paramList, ",");
        String mpId = req.getValue(request, "module_id");
        String permit_id = "",module_id = "";
        String sql="";
        BatchSql batch = new BatchSql();
        sql = "select wmsys.wm_concat(a.role_id) from t_role a where a.role_id in("+inSql+")";
        roleIdStrs = db.queryForString(sql, paramList);
        //保存角色模块配置
        String[] roleids = roleIdStrs.split(",");
        if(!"".equals(mpId)){
            if(mpId.indexOf("P")>0){
                permit_id = mpId.substring(0, mpId.indexOf("_"));
                //把没选的权限删除掉
                sql = "delete from t_role_permit where permit_id = ? ";
                batch.addBatch(sql, new Object[]{permit_id});
                for(int i=0; i<roleids.length; i++){
                    if(!"".equals(roleids[i])){
                        sql = "insert into t_role_permit(role_id, permit_id, module_id) " +
                            " select ?,?,a.module_id from t_permit a where rownum=1 and a.permit_id= ? ";
                        batch.addBatch(sql, new Object[]{roleids[i],permit_id, permit_id});
                    }
                }
            }else if(mpId.indexOf("M")>0){
                //模块分配角色时，上级所有父节点都要分配选中的角色
                module_id = mpId.substring(0, mpId.indexOf("_"));
                String supmoduleid = "";
                sql =" select wmsys.wm_concat(a.module_id) moduleIds " +
                    " from t_module a " + 
                    " where a.status = 1 " + 
                    " start with module_id = ? " + 
                    " connect by prior superior_id = module_id ";
                supmoduleid = db.queryForString(sql, new Object[]{module_id});
                //把没选的模块删除掉
                sql = " delete from t_role_module where module_id = ? ";
                batch.addBatch(sql, new Object[]{module_id});
                if("".equals(supmoduleid)){
                    for(int i=0; i<roleids.length; i++){
                        if(!"".equals(roleids[i])){
                            sql = "insert into t_role_module(role_id, module_id) " +
                                " select ?,? from dual ";
                            batch.addBatch(sql, new Object[]{roleids[i], module_id});
                        }
                    }
                }else{
                    String[] moduleids = supmoduleid.split(",");
                    for(int i=0; i<moduleids.length; i++){
                        //把没选的模块并且关联到上级节点中没有子节点的模块配置删除
                        if(!"".equals(moduleids[i])){
                            sql = "delete from t_role_module " +
                                " where module_id = ? and not exists " + 
                                " (select 1 from t_role_module a where a.module_id in " + 
                                " (select b.module_id from t_module b where b.status = 1 " + 
                                " and b.superior_id = ? and b.module_id <> ? ))";
                            batch.addBatch(sql, new Object[]{moduleids[i],moduleids[i],module_id});
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
        }else{
            return 0;
        }
        return db.doInTransaction(batch);
    }
}
