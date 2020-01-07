package baobeisys.service.module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;
import com.sgy.util.spring.RequestHelper;

/**
 * 模块管理
 * @date 2016-2-22
 */
@Service("moduleService")
public class ModuleService  extends BaseService {
    
    /**
     * 获得模块列表
     * @return
     */
    public List<?> getModuleList() {
        String sql ="select module_id,superior_id,module_url,module_level,module_name,module_order, " +
                    "       module_code,module_folder_flag,module_path,module_icon,module_os, " + 
                    "       client_engine,client_activity_name,client_type,web_open_flag " + 
                    "  from t_module a " + 
                    " where a.status = 1 " + 
                    " order by a.module_os desc, a.module_order, a.module_id";
        return db.queryForList(sql);
    }
    
    /**
     * 获得模块信息
     * @param node_id
     * @return
     */
    public Map<?, ?> getModuleInfo(HttpServletRequest request) {
        String module_id = req.getValue(request, "module_id");
        String sql = "select module_id,superior_id,module_url,module_level,module_name,module_order," +
                " module_code,module_folder_flag,module_path,module_icon,module_os," +
                " client_engine,client_activity_name,client_type,web_open_flag," +
                " case when instr(a.module_code,'.')>0 then " +
                " substr(a.module_code,instr(a.module_code,'.')+1) else a.module_id end suffixCode," +
                " case when instr(a.module_code,'.')>0 then " +
                " substr(a.module_code,0,instr(a.module_code,'.')-1) else '' end prefixCode," +
                " fn_getmodulename(a.superior_id) superior_name,todo_method,has_ejkhdcd,open_ejcd_type " +
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
     * 根据模块编号获取参数列表
     * @param request
     * @return
     */
    public List<?> getParamListByModuleId(HttpServletRequest request) {
        String module_id = req.getValue(request, "module_id");
        String sql = " select a.param_name, a.param_value from t_module_param a " +
                     "  where a.module_id = ? and a.status = 1 ";
        return db.queryForList(sql, new Object[] {module_id});
    }
    
    /**
     * 验证模块编码是否存在
     * @param request
     * @return
     */
    public int checkModuleCodeExists(HttpServletRequest request){
        String module_code = req.getValue(request, "module_code");
        String module_id = req.getValue(request, "module_id");
        List<String> paramsList = new ArrayList<String>(); // 查询参数
        String sql = "select count(1) from t_module a " + 
                    " where a.module_code=? and a.status=1 ";
        paramsList.add(module_code);
        
        if(!"".equals(module_id)){
            sql += " and a.module_id <>? ";
            paramsList.add(module_id);
        }
        
        return db.queryForInt(sql,paramsList);
    }
    
    /**
     * 验证是否存在子节点
     * @param request
     * @return
     */
    public int checkModuleSubExists(HttpServletRequest request){
        String module_id = req.getValue(request, "module_id");
        String sql = "select count(1) from t_module a " + 
                    " where a.superior_id= ? and a.status=1 ";
        return db.queryForInt(sql,new Object[]{module_id});
    }
    
    /**
     * 验证权限编码是否存在
     * @param request
     * @return
     */
    public int checkPermitCodeExists(HttpServletRequest request){
        String permit_code = req.getValue(request, "permit_code");
        String module_id = req.getValue(request, "module_id");
        List<String> paramsList = new ArrayList<String>(); // 查询参数
        String sql = "select count(1) from t_permit a " +
                " where a.permit_code=? " +
                " and a.status=1 ";
        paramsList.add(permit_code);
        if(!"".equals(module_id)){
            sql += " and a.module_id<>? ";
            paramsList.add(module_id);
        }
        return db.queryForInt(sql,paramsList);
    }
    
    /**
     * 新增模块
     * @param request
     * @return
     */
    public int saveOrUpdate(HttpServletRequest request) {
        String method = req.getValue(request, "method");
        String operatorId = this.getOperatorId(request);
        String module_level = req.getValue(request, "module_level");//模块层级
        String superior_id = req.getValue(request, "superior_id");//上级编号
        String module_name = req.getValue(request, "module_name");//模块名称
        String module_order = req.getValue(request, "module_order");//模块顺序
        String module_id = req.getValue(request, "module_id");//模块编号
        String module_url = req.getValue(request, "module_url");//模块连接
        String module_os = req.getValue(request, "module_os");//模块类型：1:PC菜单   2:客户端菜单
        String module_code = req.getValue(request, "module_code");//模块代码
        String module_folder_flag = "".equals(module_url)?"1":"2";//1：目录 2：具体模块
        String has_ejkhdcd = req.getValue(request, "has_ejkhdcd");//是否有二级菜单
        String open_ejcd_type = "";//二级菜单打开方式
        String todo_method = "";
        String module_icon = "";
        String client_type = "";
        String client_activity_name = "";
        String client_engine = "";
        String web_open_flag = "";
        
        if("1".equals(module_os)){//PC菜单
            web_open_flag = req.getValue(request, "web_open_flag");//web端打开方式：1 Tab打开   2浏览器打开   3 全屏打开
            module_icon = req.getValue(request, "module_icon");//导航图标
            todo_method = req.getValue(request, "todo_method");//待办对应的方法
            if("".equals(module_url)){//如果url为空就是目录，目录没有打开方式，置为空
                web_open_flag="";
            }
           
        }else if("2".equals(module_os)){//客户端菜单
            module_icon = req.getValue(request, "client_module_icon");//客户端图标
            client_type = StringHelper.toSql(req.getValuesString(request, "client_type", ","));//客户端适用系统：ANDROID，ANDROID_PAD，IOS 
            client_activity_name = req.getValue(request, "client_activity_name");//客户端类名全部路径
            client_engine = req.getValue(request, "client_engine");//客户端引擎【1：本地客户端 2：本地HTML 3：远程HTML】
            if("1".equals(has_ejkhdcd)){//如果有二级菜单
                open_ejcd_type = req.getValue(request, "open_ejcd_type");//二级菜单打开方式
            }
        }
        
        
        String[] permit_id = req.getValues(request, "permit_id"); //权限ID
        String[] permit_name = req.getValues(request, "permit_name"); //权限名称
        String[] url_type = req.getValues(request, "url_type");       //URL类型
        String[] permit_code_prefix = req.getValues(request, "permit_code_prefix");
        String[] permit_code_middle = req.getValues(request, "permit_code_middle");
        String[] permit_code_suffix = req.getValues(request, "permit_code_suffix");
        String[] permit_url = req.getValues(request, "permit_url");       //权限URL
        String[] operate_level = req.getValues(request, "operate_level"); //操作级别
        String hidPermitId = req.getValue(request, "hidPermitId");
        String permit_code = "";
        String sql = "";
        BatchSql batch = new BatchSql();
        
        
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
        if("add".equals(method)){
            sql ="insert into t_module (module_id,superior_id,module_url,module_level,module_name,module_order, " +
                "module_code,module_folder_flag,module_path,module_icon,module_os,client_engine,client_activity_name, " + 
                "client_type,web_open_flag,create_operator,create_date,operating_code," +
                "operating_date,todo_method,has_ejkhdcd,open_ejcd_type) " + 
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,sysdate,?,?,?)";
            batch.addBatch(sql, new Object[] {module_id, superior_id, module_url, module_level, module_name,
                    module_order, module_code, module_folder_flag, module_path, module_icon, module_os, client_engine,
                    client_activity_name, client_type, web_open_flag, operatorId, operatorId, todo_method, has_ejkhdcd,
                    open_ejcd_type});
            if("1".equals(module_os)){//PC菜单，插入模块权限
                for (int i = 0; i < permit_name.length; i++) {
                    if(!"".equals(permit_name[i]) && !"".equals(permit_url[i]) && !"".equals(operate_level[i])){
                        sql =" insert into t_permit " +
                            "  (permit_id, module_id, permit_name, " + 
                            "   permit_code, permit_url, permit_order, status, operate_level,url_type) " + 
                            "   values(seq_t_permit_id.nextval, ?, ?, ?, ?, ?, 1, ?,?)";
                        permit_code = permit_code_prefix[i]+"."+permit_code_middle[i]+"."+permit_code_suffix[i];
                        batch.addBatch(sql, new Object[] {module_id, permit_name[i], permit_code, permit_url[i],
                                (i + 1), operate_level[i], url_type[i]});
                    }
                }
               
            }
        } else if("update".equals(method)){//修改模块信息
            
            sql ="update t_module set superior_id = ? ,module_url = ? ,module_level = ? ,module_name = ? , " +
                "module_order = ? ,module_code = ? ,module_folder_flag = ? ,module_path = ? ,module_icon = ? , " + 
                "module_os = ? ,client_engine = ? ,client_activity_name = ? ,client_type = ? ,web_open_flag = ? ," + 
                "operating_code = ? ,operating_date = sysdate,todo_method = ?,has_ejkhdcd = ?,open_ejcd_type = ?" +
                " where module_id = ?";
            batch.addBatch(sql, new Object[] {superior_id, module_url, module_level, module_name, module_order,
                    module_code, module_folder_flag, module_path, module_icon, module_os, client_engine,
                    client_activity_name, client_type, web_open_flag, operatorId, todo_method, has_ejkhdcd,
                    open_ejcd_type, module_id});
            if("1".equals(module_os)){//如果是PC菜单
                for (int i = 0; i < permit_id.length; i++) {
                    if ("".equals(permit_id[i])) {
                        if (!"".equals(permit_name[i]) && !"".equals(permit_url[i]) && !"".equals(operate_level[i])) {
                            sql =" insert into t_permit " +
                                "  (module_id, permit_id, permit_name, " + 
                                "   permit_code, permit_url, permit_order, status, operate_level,url_type) " + 
                                "   values (?, seq_t_permit_id.nextval, ?, ?, ?, ?, 1, ?,?)";
                            permit_code = permit_code_prefix[i]+"."+permit_code_middle[i]+"."+permit_code_suffix[i];
                            batch.addBatch(sql, new Object[] {module_id, permit_name[i], permit_code, permit_url[i],
                                    (i + 1), operate_level[i], url_type[i]});
                        }
                    } else {
                        sql ="update t_permit " +
                            "   set permit_name = ?,permit_code = ?, permit_url = ?, operate_level = ?,url_type=? " + 
                            " where permit_id = ? and module_id = ? ";
                        permit_code = permit_code_prefix[i]+"."+permit_code_middle[i]+"."+permit_code_suffix[i];
                        batch.addBatch(sql, new Object[]{permit_name[i], permit_code, 
                                permit_url[i], operate_level[i],url_type[i], permit_id[i], module_id});
                    }
                }
                
                //删除已经去掉的
                String[] hid_ids = hidPermitId.split(",");
                String per_id = "";
                for(int i=0; i<hid_ids.length; i++){
                    per_id = hid_ids[i];
                    if(!"".equals(per_id)){
                        sql = "delete from t_permit a where a.module_id=? and a.permit_id=? ";
                        batch.addBatch(sql, new Object[]{module_id, per_id});
                        
                        sql = "delete from t_role_permit a where a.module_id=? and a.permit_id=? ";
                        batch.addBatch(sql, new Object[]{module_id, per_id});
                    }
                }
            }else if("2".equals(module_os)) {//如果是客户端手机则把原来的菜单权限删掉
                sql = "delete from t_permit a where a.module_id=?  ";
                batch.addBatch(sql, new Object[]{module_id});
                
                sql = "delete from t_role_permit a where a.module_id=? ";
                batch.addBatch(sql, new Object[]{module_id});
            }
            
            // 删除模块参数
            sql = " delete from t_module_param a where a.module_id = ? ";
            batch.addBatch(sql, new Object[] {module_id});
        }
        if("2".equals(module_os)){//客户端菜单
            String[] param_names = req.getValues(request, "param_name"); //参数名称
            String[] param_values = req.getValues(request, "param_value");//参数值
            for(int i = 0; i < param_names.length; i++) {
                if(!"".equals(param_names[i]) && !"".equals(param_values[i])) {
                    sql = " insert into t_module_param (param_id, module_id, param_name, param_value, status) " +
                          " values (seq_t_module_param_id.nextval, ?, ?, ?, 1) ";
                    batch.addBatch(sql, new Object[] {module_id, param_names[i], param_values[i]});
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
        String sql = "";
        BatchSql batch = new BatchSql();
        
        // 判断该模块下是否存在其它节点
        sql = "select count(1) from t_module where superior_id = ? and status=1 ";
        if (db.queryForInt(sql, new Object[]{module_id}) > 0) {
            return -2;
        }
        
        sql = "delete from t_module a where a.module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        sql = "delete from t_permit a where a.module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        sql = "delete from t_role_module a where a.module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        sql = "delete from t_role_permit a where a.module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        sql = "delete from t_module_param a where a.module_id=?";
        batch.addBatch(sql, new Object[]{module_id});
        
        return db.doInTransaction(batch);
    }
    
    /**
     * 获得默认模块编码前缀
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
     * 获得模块前缀编码
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
            sqlw2 = " ,case when a.module_id='"+module_id+"' " +
                    " or a.module_id = '0' " +
                    " or a.module_id in (select o.module_id from t_module o start with o.module_id='"+module_id+"' " +
                    " connect by prior o.superior_id=o.module_id) then 'true' else 'false' end is_open  " ;
        }else{
            sqlw2 = " ,case when a.module_id = '0' then 'true' else 'false' end is_open  " ;
        }
        
        String sql = "select a.module_id,a.superior_id,a.module_level," +
                " a.module_name,a.module_order," +
                " ifnull((select 'true' from t_module b where b.superior_id=a.module_id and b.module_folder_flag=1 and b.status=1 and rownum=1),'false') is_parent " +
                sqlw+sqlw2+
                " from t_module a where a.status=1 and a.module_folder_flag=1 " +
                " order by a.module_level,a.module_order,a.module_id ";
//        logger.debug("获得上级目录树------>"+sql);
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
        path = "/images/menu_img/big_icon/";
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
