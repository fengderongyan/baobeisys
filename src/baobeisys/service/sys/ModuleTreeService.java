package baobeisys.service.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

/**
 * 加载菜单树
 * @date 2016-2-22
 */
@Service("moduleTreeService")
public class ModuleTreeService extends BaseService {

    /**
     * 加载菜单树
     * @return
     */
    public List<?> getModuleList(HttpServletRequest request){
        String roles = this.getUser(request).getRoleIds();//获取角色
        logger.debug("roles : " + roles);
        List<String> paramsList = new ArrayList<String>();//查询参数
        String sql = "";
        sql = "select * " +
            "  from (select distinct a.module_id, a.superior_id, a.module_name, " + 
            "                        ifnull(a.module_url, 0) module_url, " + 
            "                        a.module_flag,a.module_level,a.module_order, " + 
            "                        a.module_icon,a.open_flag " + 
            "          from t_module a, t_role_module b " + 
            "         where a.module_id = b.module_id " + 
            "           and a.status = 1 " +
            "           and instr(CONCAT(',', ?,','),  CONCAT(',',b.role_id, ',') )>0 ) t" +
            "  order by module_order";
        paramsList.add(roles);
        logger.debug("module1: " + str.getSql(sql, paramsList));
        return db.queryForList(sql, paramsList);
    }
    
    
    /**
     * 加载菜单树
     * @return
     */
    public List<?> getIndexModuleList(HttpServletRequest request){
        String roles = this.getUser(request).getRoleIds();//获取角色
        logger.debug("roles : " + roles);
        List<String> paramsList = new ArrayList<String>();//查询参数
        String sql = "";
        sql = "select * " +
            "  from (select distinct a.module_id, a.superior_id, a.module_name, " + 
            "                        ifnull(a.module_url, 0) module_url, " + 
            "                        a.module_flag,a.module_level,a.module_order, " + 
            "                        a.module_icon,a.open_flag, " + 
            "						(case when a.module_flag = 1 then 'zTreeStyle/img/diy/1_close.png' " +
            "	   						  when a.module_flag = 2 then 'zTreeStyle/img/diy/1_open.png' end ) icon " +
            "          from t_module a, t_role_module b " + 
            "         where a.module_id = b.module_id " + 
            "           and a.status = 1 " +
            "           and instr(CONCAT(',', ?,','),  CONCAT(',',b.role_id, ',') )>0 ) t" +
            "  order by module_order";
        paramsList.add(roles);
        logger.debug("module1: " + str.getSql(sql, paramsList));
        return db.queryForList(sql, paramsList);
    }
}