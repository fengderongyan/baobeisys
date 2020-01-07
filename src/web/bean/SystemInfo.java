package web.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.DBHelperSpring;

/**
 * 系统基础信息，如系统角色，用户菜单等
 * @date 2016-2-22
 */
public class SystemInfo {
    public final Logger logger = Logger.getLogger(SystemInfo.class);
    private DBHelperSpring db;
    
    /**
     * 角色模块关系列表
     */
    private List<Map<String, Object>> roleModuleList;
    
    /**
     * 角色权限关系列表
     */
    private List<Map<String, Object>> rolePermitList;

    /**
     * 系统角色列表
     */
    private List<Map<String, Object>> roleList;
    
    /**
     * 字典配置
     */
    private List<Map<String, Object>> dictItemList;
    
    /**
     * 系统允许的请求来源列表
     */
    private List<Map<String, Object>> refererList;
    
    /** 所有错误提醒配置信息 例如：key = 1001， value = '错误描述！' */
    private Map<String, Object> globalError;
    
    public Map<String, Object> getGlobalError() {
        return globalError;
    }

    public void setGlobalError(Map<String, Object> globalError) {
        this.globalError = globalError;
    }
    
    public List<Map<String, Object>> getRefererList() {
        return refererList;
    }

    public void setRefererList(List<Map<String, Object>> refererList) {
        this.refererList = refererList;
    }
    
    /**
     * 系统启动时加载信息
     */
    public void loadSystemData() {
        logger.debug("*******************加载后台系统信息开始******************");
        String sql = "";
        
        //加载角色权限
        sql = "select distinct a.module_id, c.module_name, a.permit_id, " +
                "a.permit_name, a.permit_code, a.permit_url, b.role_id, a.operate_Level " +
                " from t_permit a, t_role_permit b, t_module c " +
                " where a.permit_id=b.permit_id and a.module_id=c.module_id and a.status=1" +
                " order by a.module_id";
        this.setRolePermitList(db.queryForList(sql));
        
        // 初始化字典表中的配置信息
        //this.setDictItemList(this.getInitDictItemList());
        
        // 错误描述定义信息 
        this.setGlobalError(this.getGlobalErrorMap());
        
        logger.debug("*******************加载后台系统信息结束******************");
    }
    
    /**
     * @description 获取需要初始化的字典表中配置信息
     * @return
     */
    /*private List<Map<String, Object>> getInitDictItemList() {
        String sql = " select a.item_id, a.item_name, a.group_id, a.item_order, a.remark, a.item_value, b.group_name " +
                     "   from t_dictionary_item a, t_dictionary_group b " +
                     "  where a.group_id = b.group_id and a.status = 1 " +
                     "    and b.status = 1 and b.is_init = 1 " +
                     "  order by a.item_order ";
        return db.queryForList(sql);
    }*/
    
    /**
     * 获取错误描述定义信息 键值对（1001, '错误描述'）
     * @return
     */
    private Map<String, Object> getGlobalErrorMap() {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        String sql = " select a.error_id, a.error_desc from t_res_error_define a where a.status = 1 ";
        List<Map<String, Object>> errorInfoList = db.queryForList(sql);
        for(Map<String, Object> map : errorInfoList) {
            errorMap.put(StringHelper.get(map, "error_id"), StringHelper.get(map, "error_desc"));
        }
        return errorMap;
    }
    

    public DBHelperSpring getDb() {
        return db;
    }
    public void setDb(DBHelperSpring db) {
        this.db = db;
    }

    public List<Map<String, Object>> getRoleModuleList() {
        return roleModuleList;
    }

    public void setRoleModuleList(List<Map<String, Object>> roleModuleList) {
        this.roleModuleList = roleModuleList;
    }

    public List<Map<String, Object>> getRolePermitList() {
        return rolePermitList;
    }

    public void setRolePermitList(List<Map<String, Object>> rolePermitList) {
        this.rolePermitList = rolePermitList;
    }

    public List<Map<String, Object>> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Map<String, Object>> roleList) {
        this.roleList = roleList;
    }

    public List<Map<String, Object>> getDictItemList() {
        return dictItemList;
    }

    public void setDictItemList(List<Map<String, Object>> dictItemList) {
        this.dictItemList = dictItemList;
    }

}