package baobeisys.service.module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.db.BatchSql;
import com.sgy.util.net.IpHelper;
import com.sgy.util.spring.RequestHelper;

/**
 * 后台模块管理
 * @date 2016-2-22
 */
@Service("adminModuleService")
public class AdminModuleService  extends BaseService {
    
    /**
     * 获得模块列表
     * @return
     */
    public List<?> getModuleList() {
        String sql = "select a.module_id,a.superior_id,a.module_url," +
            " a.module_level,a.module_name,a.module_order," + 
            " a.module_code,a.module_flag from t_module a where a.status=1 " + 
            " order by a.module_order,a.module_id ";
        return db.queryForList(sql);
    }
    
    /**
     * 获得模块信息
     * @param node_id
     * @return
     */
    public Map<?, ?> getModuleInfo(HttpServletRequest request) {
        String module_id = req.getValue(request, "module_id");
        String sql = "select a.module_id,a.superior_id,a.module_url," +
                     " a.module_level,a.module_name,a.module_order," +
                     " a.module_code,a.module_flag,a.module_icon," +
                     " case when instr(a.module_code,'.')>0 then " +
                     " substr(a.module_code,instr(a.module_code,'.')+1) else a.module_id end suffixCode," +
                     " case when instr(a.module_code,'.')>0 then " +
                     " substr(a.module_code,0,instr(a.module_code,'.')-1) else '' end prefixCode,open_flag," +
                     " fn_getModuleName(a.superior_id) superior_name " +
                     " from t_module a where a.module_id=? ";
        return db.queryForMap(sql, new Object[]{module_id});
    }
    
    /**
     * 根据模块ID获得权限列表
     * @param node_id
     * @return
     */
    public List<?> getPermitByModuleId(HttpServletRequest request){
        String module_id = req.getValue(request, "module_id");
        String sql ="select a.permit_id,a.permit_name,url_type, " +
            "a.permit_code,a.permit_url,a.permit_order, a.operate_level " + 
            "from t_permit a where a.module_id=? and a.status=1 " + 
            "order by a.permit_order ";
        return db.queryForList(sql, new Object[]{module_id});
    }
    
    /**
     * 验证模块编码是否存在
     * @param request
     * @return
     */
    public int checkModuleCodeExists(HttpServletRequest request){
        String module_code = req.getValue(request, "module_code");
        String module_id = req.getValue(request, "module_id");
        String sql = "select count(1) from t_module a " 
            + " where a.module_code='"+module_code+"' and a.status=1 ";
        if(!"".equals(module_id)){
            sql += " and a.module_id <>'"+module_id+"' ";
        }
        return db.queryForInt(sql);
    }
    
    /**
     * 验证是否存在子节点
     * @param request
     * @return
     */
    public int checkModuleSubExists(HttpServletRequest request){
        String module_id = req.getValue(request, "module_id");
        String sql = "select count(1) from t_module a " 
            + " where a.superior_id='"+module_id+"' and a.status=1 ";
        return db.queryForInt(sql);
    }
    
    /**
     * 验证权限编码是否存在
     * @param request
     * @return
     */
    public int checkPermitCodeExists(HttpServletRequest request){
        String permit_code = req.getValue(request, "permit_code");
        String module_id = req.getValue(request, "module_id");
        String sql = "select count(1) from t_permit a " +
                " where a.permit_code='"+permit_code+"' " +
                " and a.status=1 ";
        if(!"".equals(module_id)){
            sql += " and a.module_id<>'"+module_id+"' ";
        }
        return db.queryForInt(sql);
    }
    
    /**
     * 新增模块
     * @param request
     * @return
     */
    public int saveOrUpdate(HttpServletRequest request) {
        String action = req.getValue(request, "action");
        String module_level = req.getValue(request, "module_level");
        String module_code = req.getValue(request, "module_code");  //模块代码
        String module_id = req.getValue(request, "module_id");      //模块编号
        String module_name = req.getValue(request, "module_name");  //模块名称
        String module_order = req.getValue(request, "module_order");//模块顺序
        String module_url = req.getValue(request, "module_url");    //模块连接
        String superior_id = req.getValue(request, "superior_id");  //上级编号
        String module_icon = req.getValue(request, "module_icon");  //菜单图标
        String open_flag = req.getValue(request, "open_flag"); // 打开方式
        
        if("".equals(module_url)){//如果url为空就是目录，目录没有打开方式，置为空
            open_flag="1";
        }
        
        String[] permit_id = req.getValues(request, "permit_id"); //权限ID
        String[] permit_name = req.getValues(request, "permit_name"); //权限名称
        String[] permit_code_prefix = req.getValues(request, "permit_code_prefix");
        String[] permit_code_middle = req.getValues(request, "permit_code_middle");
        String[] permit_code_suffix = req.getValues(request, "permit_code_suffix");
        String[] permit_url = req.getValues(request, "permit_url");       //权限URL
        String[] operate_level = req.getValues(request, "operate_level"); //操作级别
        String[] url_type = req.getValues(request, "url_type"); //url类型
        
        String hidPermitId = req.getValue(request, "hidPermitId");
        String sql = "";
        BatchSql batch = new BatchSql();
        
        String module_flag = "".equals(module_url)?"1":"2";//1：目录 2：具体模块
        String permit_code = "";
        
        
        sql = "select replace(sys_connect_by_path(a.module_name, '/ '),' ','') module_name_path " +
              "  from t_module a " + 
              " where a.module_id = ? " + 
              " start with a.superior_id= '0' " + 
              " connect by prior a.module_id = a.superior_id ";
        String module_path = db.queryForString(sql, new Object[]{superior_id});
        
        if("0".equals(superior_id)){
            module_path="/"+module_name;
        }
        //新增模块信息
        if("add".equals(action)){
            sql = "insert into t_module (module_id, superior_id, module_url, " + 
                "   module_level, module_name,module_order, status, " + 
                "   module_code, module_flag,module_path,module_icon,open_flag) " + 
                "values(?, ?, ?, ?, ?, ?, 1,  ?, ?, ?,?,?) ";
            batch.addBatch(sql, new Object[]{module_id, superior_id, module_url,
                    module_level, module_name, module_order, module_code,
                    module_flag, module_path,module_icon,open_flag});
            
        } else if("update".equals(action)){//修改模块信息
            sql ="update t_module " +
                "   set superior_id  = ?, module_url   = ?, module_level = ?, " + 
                "       module_name  = ?, module_order = ?,  " + 
                "       module_code  = ?, module_flag = ? , " +
                "        module_path = ?,module_icon = ?,open_flag = ?" + 
                " where module_id = ? ";
           
            batch.addBatch(sql, new Object[]{superior_id, module_url, module_level, module_name,
                    module_order, module_code, module_flag,
                    module_path,module_icon,open_flag, module_id});
            db.doInTransaction(batch);
            String[] hid_ids = hidPermitId.split(",");
            String per_id = "";
            //修改时，把子菜单权限赋给父级菜单
            //获取模块所有角色权限
            sql = " select GROUP_CONCAT(role_id) moduleIds " +
            	  "   from t_role_module " +
            	  "  where module_id = ? ";
            String roleIds = db.queryForString(sql, new Object[]{module_id});
            
            //获取所有父节点
            sql = "select find_all_subsids(?) ";
            
            String supmoduleid = db.queryForString(sql, new Object[]{module_id});
            String[] moduleids = supmoduleid.split(",");
            	
            String[] roleids = roleIds.split(",");
          //选中当前节点，父节点角色同时增加当前子节点的角色（不删除父节点之前有的角色）
            for(int i=0; i<moduleids.length; i++){
                module_id = moduleids[i];
                if(i%2 ==0){
                	logger.debug(moduleids[i]);
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

        }
        
       
        return db.doInTransaction(batch);
    }
    
    /**
     * 删除模块配置
     * @param request
     * @return
     */
    public int deleteModule(HttpServletRequest request){
        String module_id = req.getValue(request, "module_id");
        String operatorId = this.getUser(request).getOperatorId();//操作人
        String ipAddress = IpHelper.getIpAddr(request);//IP地址
        String sql = "";
        BatchSql batch = new BatchSql();
        
        // 判断该模块下是否存在其它节点
        sql = "select count(1) from t_module where superior_id = ? and status=1 ";
        if (db.queryForInt(sql, new Object[]{module_id}) > 0) {
            return -2;
        }
        
        //菜单操作历史表
        sql="insert into t_module_his " +
            "select a.*, ?, sysdate(), ? from t_module a where a.module_id = ?";
        batch.addBatch(sql, new Object[]{operatorId, ipAddress,module_id});
        
        sql = "delete from t_module  where module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        sql = "delete from t_role_module  where module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        return db.doInTransaction(batch);
    }
    
    /**
     * @description 获得默认模块编码前缀
     * @author YUANFY 2013-08-06
     * @param request
     * @return
     */
    public String getModuleDefaultCodePrefix(HttpServletRequest request){
        String module_id = req.getValue(request, "module_id");
        String sql ="select a.module_code from t_module a " +
                    "where a.module_level=1 " + 
                    "and a.module_id<>0 " + 
                    "start with a.module_id=? " + 
                    "connect by NOCYCLE prior a.superior_id = a.module_id";
        return db.queryForString(sql,new Object[]{module_id});
    }
    
    /**
     * @description 获得模块前缀编码
     * @author YUANFY 2013-08-06
     * @return
     */
    public String getModulePrefixCode(){
        return db.getNextSequenceValue("seq_t_module_id");
    }
    
    /**
     * 获得上级目录树
     * @return
     */
    public List<?> getSuperiorModuleList(HttpServletRequest request){
        String module_id = req.getValue(request, "superior_id");
        //修改操作时选中当前模块节点
        String sqlw = "";
        if(!"".equals(module_id)){
            sqlw += " ,case when a.module_id='"+module_id+"' then 'true' else 'false' end checked ";
        }else{
            sqlw += " ,'false' checked ";
        }
        
        //修改操作时默认打开到已经选中的节点
        String sqlw2 = "";
        if(!"".equals(module_id)){
            sqlw2 = " ,case when instr(find_all_subsids('"+module_id+"'), a.module_id)" +
                    " then 'true' else 'false' end is_open  " ;
        }else{
            sqlw2 = " ,case when a.module_id = '0' then 'true' else 'false' end is_open  " ;
        }
        
        String sql = "select a.module_id,a.superior_id,a.module_level," +
                " a.module_name,a.module_order," +
                " ifnull((select 'true' from t_module b where b.superior_id=a.module_id and b.module_flag=1 and b.status=1 limit 1),'false') is_parent " +
                sqlw+sqlw2+
                " from t_module a where a.status=1 and a.module_flag=1 " +
                " order by a.module_order,a.module_id ";
        logger.debug("获得上级目录树------>"+sql);
        return db.queryForList(sql);
    }
    
    /**
     * 获取图标列表
     * @param request
     * @return
     */
    public List<String> getModuleIconList(HttpServletRequest request) {
        List<String> list = new ArrayList<String>();
        String path = "";
        // 菜单树结点图标
        path = "/images/menu_img/";
        File file = new File(new RequestHelper().getWebRootRealPath() + path);

        File[] files = file.listFiles();
        if(files != null && files.length > 0) {
            for(File icon : files) {
                list.add(icon.getName());
            }
        }
        return list;
    }
}
