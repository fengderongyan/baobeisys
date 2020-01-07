package baobeisys.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.model.User;
import web.service.BaseService;

import com.sgy.util.db.BatchSql;
import com.sgy.util.common.StringHelper;

/**
 * 组织管理
 * @date 2016-2-22
 */
@Service("orgCfgService")
public class OrgCfgService extends BaseService {
	
	/**
	 * 查询组织列表
	 * @param request
	 * @return
	 */
	public List<?> getOrgCfgList(HttpServletRequest request) {
        String orgName = req.getValue(request, "org_name");//组织名称
        String orgId = req.getValue(request, "org_id");//组织编号
        String orgLev = req.getValue(request, "org_lev");//层级
        List<String> paramsList = new ArrayList<String>();//查询参数
        int pageSize = req.getPageSize(request, "pageSize"); //分页
        String sql = "";
		sql = "select org_id, org_name, (select org_name from t_organization b where b.status = 1 and b.org_lev = 1)county_name, " +
				"	a.org_lev orglev_name " +
				"from t_organization a where 1 = 1 and status = 1 " ;

		if(!"".equals(orgLev)) {
            sql += " and a.org_lev = ? ";
            paramsList.add(orgLev);
        }
		
        if(!"".equals(orgId)) {
            sql += " and a.org_id = ? ";
            paramsList.add(orgId);
        }

        if(!"".equals(orgName)) {
            sql += " and a.org_name like ? ";
            paramsList.add("%" + orgName + "%");
        }
        
        sql += " order by a.org_lev ";
        return db.getForList(sql, paramsList, pageSize, request);
    }
	

	
	/**
     * 查询层级列表
     * @param request
     * @return
     */
    public List getOrgLevList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>(); // 查询参数
        String sql = " select distinct (org_lev) " 
                   + "   from t_organization "
                   + "  order by org_lev ";

        return db.queryForList(sql,paramsList);
    }
	
	/**
	 * 获取组织详细信息
	 * @param request 
	 * @return
	 */
	public Map<String, Object> getOrgInfo(HttpServletRequest request) {
	    String orgId = req.getValue(request, "org_id");
	    String sql = " select org_id, org_name, superior_id, fn_getorgname(a.superior_id) superior_name, " +
	    		     "        status, org_lev, county_id  " +
                     "   from t_organization a " + 
                     "  where a.status = 1 " +
                     "    and a.org_id = ? ";
	    logger.debug("orgInfo : " + str.getSql(sql, new Object[]{orgId}));
		return db.queryForMap(sql, new Object[]{orgId});
	}
	
	/**
     * 获取父节点的层级
     * @param request
     * @return
     */
	public int getLevBySid(String id){
	    String sql = "";
	    sql = " select distinct (org_lev) from t_organization where superior_id = ? ";
	    return db.queryForInt(sql, new Object[] {id});
	}
	
	/**
	 * 保存组织信息
	 * @param request
	 * @return
	 */
	public int saveOrUpdateOrg(HttpServletRequest request) {
        String method = req.getValue(request, "method");
        String oldOrgId = req.getValue(request, "old_org_id"); //组织编号
        String orgId = req.getValue(request, "org_id"); //组织编号
        String orgName = req.getValue(request, "org_name"); //组织名称
        String superiorId = req.getValue(request, "superior_id");//上级组织编号
        String orgLevel = req.getValue(request, "org_level");//当前组织层级
        String countyId = req.getValue(request, "county_id");//城市编号
        logger.debug("orgLevel : " + orgLevel);
        int result = 0;
        String sql = "";
        BatchSql batchSql = new BatchSql();
        
		if ("create".equals(method)) {//新增
            sql = " insert into t_organization(org_id, org_name, superior_id, status, org_lev, county_id ) " +
                  "  values(?, ?, ?, 1, ?, ?)";
            batchSql.addBatch(sql, new Object[] {orgId, orgName, superiorId, orgLevel, countyId});
            logger.debug("sql: " + str.getSql(sql, new Object[] {orgId, orgName, superiorId, orgLevel, countyId}));
		} else if ("edit".equals(method)) {//修改
            sql = " update t_organization " 
                + "         set org_id = ?, org_name = ?, " 
                + "             superior_id = ?,  " 
                + "             org_lev = ? " 
                + "       where org_id = ? ";
            batchSql.addBatch(sql, new Object[] {orgId, orgName, superiorId, orgLevel, oldOrgId});
            logger.debug("edit : " + str.getSql(sql, new Object[] {orgId, orgName, superiorId, orgLevel, oldOrgId}));
		}
        return db.doInTransaction(batchSql);
	}
	
	/**
     * 获取组织编号
     * @param request
     * @return
     */
    public String getOrgId(HttpServletRequest request) {
       //return db.getNextSequenceValue("seq_t_organization_id");
       return db.getMysqlNextSequenceValue("t_organization_sid");
    }
	
	/**
     * 验证组织编号是否存在
     * @param request
     * @return
     */
    public int checkOrgId(HttpServletRequest request) {
        String newOrgId = req.getValue(request, "org_id");//新组织编号
        String oldOrgId = req.getValue(request, "old_org_id");//组织编号
        String sql = "select count(1) from t_organization a where a.org_id = ? ";
        //如果新组织编号跟上次的组织编号一样，说明组织没变过，就要排除掉本组织编号
        if(newOrgId.equals(oldOrgId)){
            sql += " and a.org_id <> '"+newOrgId+"'";
        }
        return db.queryForInt(sql, new Object[] {newOrgId});
    }
	
	/**
     * 删除组织
     * @param request
     * @return
     */
    public int deleteOrgCfg(HttpServletRequest request) {
        String orgId = req.getValue(request, "org_id");
        BatchSql batchSql = new BatchSql();
        String sql = "";
        sql = " update t_organization a set a.status = 0 where a.org_id = ? ";
        batchSql.addBatch(sql, new Object[] {orgId});
        return db.doInTransaction(batchSql);
    }
    
    /**
     * 获取组织树Sql
     * @param request
     * @return
     */
    public String getSchOrgSql(HttpServletRequest request) {
        String orgIds = req.getValue(request, "orgIds");//组织编号
        String orgName = req.getValue(request, "org_name");//组织名称
        String rejectOrgIds = req.getValue(request, "rejectOrgIds");//要剔除的组织编号
        User user = this.getUser(request); 
        String userMaxRoleLevel = user.getMaxRoleLevel();//权限层级
        String regionId = user.getCountyId();//当前登录人地市
        List<String> paramsList = new ArrayList<String>();// 查询参数
        String sql = "";
        //修改操作时选中当前模块节点
        String sqlw = "";
        //如果组织编号不为空  则选中
        if(!"".equals(orgIds)) {
            sqlw += " case when a.org_id = ? then 'true' else 'false' end checked ";
            paramsList.add(orgIds);
        } else {
            sqlw += " 'false' checked ";
        }
        
        sql ="select a.org_id, a.org_name, a.superior_id, a.org_lev, a.county_id, " +sqlw+
             "  from t_organization a " + 
             " where a.status = 1  " ;

        //按组织名称
        if(!"".equals(orgName)) {
            sql += " and a.org_name like ?  ";
            paramsList.add("%" + orgName + "%");
        }
        
        //要剔除的组织编号
        if(!"".equals(rejectOrgIds)) {
            sql += " and a.org_id <> ? ";
            paramsList.add(rejectOrgIds);
        }
        
        return str.getSql(sql, paramsList);
    }
    
    /**
     * 获取组织树
     * @param request
     * @return
     */
    public List<?> getOrgTreeList(HttpServletRequest request) {
        String sql = this.getSchOrgSql(request);
        return db.queryForList(sql);
    }
}
