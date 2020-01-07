package baobeisys.service.sys;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import web.service.BaseService;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.sgy.util.common.DateHelper;
import com.sgy.util.common.FileHelper;
import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;
import com.sgy.util.db.ProcHelper;
import com.sgy.util.excel.HssfHelper;

import baobeisys.action.sys.UserCfgController;

/**
 * 用户管理
 * @date 2016-2-22
 */
@Service("userCfgService")
public class UserCfgService extends BaseService {
	
	/**
	 * 查询用户列表
	 * @param request
	 * @return
	 */
	public List<?> getUserList(HttpServletRequest request) {
	    String operator_id = req.getValue(request, "operator_id");//编号
        String name = req.getValue(request, "name");//姓名
        String role_id = req.getValue(request, "role_id");//角色
        String maxRole = this.getUser(request).getMaxRoleLevel();
        String org_name = req.getValue(request, "org_name");//归属组织
        String org_id = this.getUser(request).getOrgId();//所在归属编号
        String mobile = req.getValue(request, "mobile");//手机号码
        String status = req.getValue(request, "status");//状态
        List<String> paramsList = new ArrayList<String>();//查询参数
        String org_lev = this.getUser(request).getOrgLevel();//用户归属层级
        int pageSize = req.getPageSize(request, "pageSize"); //分页
		String sql = " select a.operator_id, a.name, h.org_id, a.mobile, a.status, a.qm_name, a.qm_name_d, a.qm_url, " +
					 "         h.org_name, group_concat(b.role_name) roleNames " + 
		             /*"              h.org_name, wmsys.wm_concat(b.role_name) roleNames " +  oracle*/
		             "         from t_user a, t_role b, t_user_role c, t_organization h " + 
		             "        where a.operator_id = c.operator_id " + 
		             "          and b.role_id = c.role_id " + 
		             "          and a.org_id = h.org_id ";
		
		if(this.getUser(request).getRoleLevel() == 0){//超级管理员
		    sql += " and 1 = 1 ";
		}else{
		    sql += " and a.operator_id != 159999 ";
		}
		
        if(!operator_id.equals("")) {
            sql += " and a.operator_id=? ";
            paramsList.add(operator_id);
        }
        
        if(!name.equals("")) {
            sql += " and a.name like ? ";
            paramsList.add("%" + name + "%");
        }
        
        if(!org_name.equals("")) {
            sql += " and h.org_name like ? ";
            paramsList.add("%" + org_name + "%");
        }
        
        if(!mobile.equals("")) {
            sql += " and a.mobile = ? ";
            paramsList.add(mobile);
        }
        
        if(!status.equals("")) {
            sql += " and a.status = ? ";
            paramsList.add(status);
        }
        
        if(!role_id.equals("")) {
            sql += " and b.role_id = ? ";
            paramsList.add(role_id);
        }
        
        
        sql += " group by a.operator_id, a.name, h.org_id, h.org_name, a.mobile, a.status ";
        logger.debug(str.getSql(sql, paramsList));
        
        //sql += db.addSortString(request, " a.create_date desc,b.org_id,b.org_name,a.name ");
        return db.queryForList(sql, paramsList.toArray());
    }
	
	/**
     * 文件上传
     * @param request
     * @return
     */
   public int impQmResult(UserCfgController action, HttpServletRequest request, CommonsMultipartFile file, String fileFileName) {
        String operator = this.getUser(request).getOperatorId();
        String operator_id = req.getValue(request, "operator_id");
        logger.debug(file.getSize());
        
        if((int)file.getSize() / 1024 >= 2048){//图片大于2M
        	return 2;
        }
        // 上传文件存档
        String save_dir = request.getRealPath("/pic/");
        FileHelper fileHelper = new FileHelper();
        String qm_name = fileHelper.getToFileName(fileFileName);//后追加时间
        String fileName = save_dir + qm_name;
        String qm_url = save_dir + qm_name;
        fileHelper.copyFile(file, fileName);
        
        BatchSql batchSql = new BatchSql();
        
        String sql = "update t_user  " +
	        		"set qm_name = ?, qm_name_d = ?, qm_url = ?  " +
	        		"where operator_id = ? ";
        batchSql.addBatch(sql, new Object[]{ fileFileName, qm_name, qm_url, operator_id});
        
        return db.doInTransaction(batchSql);
    }
	
	
	/**
	 * 得到工号对应操作员具体信息
	 * @param request 
	 * @return
	 */
	public Map<String, Object> getUserInfo(HttpServletRequest request) {
        String operator_id = req.getValue(request, "operator_id");
        String sql = " select a.operator_id, a.name, a.password, a.org_id, a.status, a.mobile, " 
                   + "        a.email, a.user_order, b.org_name, b.org_lev " 
                   + "   from t_user a, t_organization b " 
                   + "  where operator_id = ? " 
                   + "    and a.org_id = b.org_id ";

        Map<String, Object> map = db.queryForMap(sql, new Object[] {operator_id});
        sql = " select role_id from t_user_role a where a.operator_id = ? ";
        List<Map<String, Object>> roleList = db.queryForList(sql, new Object[] {operator_id});
        String role_ids = "";
        Map<String, Object> roleMap = null;
        if(roleList.size() > 0) {
            for(int i = 0; i < roleList.size() - 1; i++) {
                roleMap = roleList.get(i);
                role_ids += StringHelper.notEmpty(roleMap.get("role_id")) + ",";
            }
            roleMap = roleList.get(roleList.size() - 1);
            role_ids += StringHelper.notEmpty(roleMap.get("role_id"));
            map.put("role_ids", role_ids);
        }
        return map;
    }
	
	/**
	 * 获取组织树
	 * @param request
	 * @return
	 */
    public List<?> getOrgList(HttpServletRequest request){
        List<String> paramsList = new ArrayList<String>();//查询参数
        String orgId = req.getValue(request, "org_id");//组织编号
        String org_id = this.getUser(request).getOrgId();//登陆人所在组织编号
        String org_lev = this.getUser(request).getOrgLevel();//登录人组织层级
        logger.debug("test: orgId " + orgId);
        
        //修改操作时选中当前模块节点
        String sqlw = "";
        //如果组织编号不为空  则选中
        if(!"".equals(orgId)) {
            sqlw += " case when a.org_id = ? then 'true' else 'false' end checked ";
            paramsList.add(orgId);
        } else {
            sqlw += " 'false' checked ";
        }
        String sql ="select a.org_id, a.org_name, a.org_lev, a.superior_id, " +sqlw+
                    "  from t_organization a " + 
                    " where a.status = 1 " ;
        
        if(org_lev.equals("2")){
            sql += " and a.county_id = ? ";
            paramsList.add(org_id);
        }
        if(org_lev.equals("3")){
            sql += " and a.area_id = ? ";
            paramsList.add(org_id);
        }
        if(org_lev.equals("4")){
            sql += " and a.town_id = ? ";
            paramsList.add(org_id);
        }
        if(org_lev.equals("")){
            sql += " and a.tiny_id = ? ";
            paramsList.add(org_id);
        }
        
        return db.queryForList(sql,paramsList);
    }
    
  
    /**
     * 获取角色列表
     * @param request
     * @return 
     */
    public List getRoleList(HttpServletRequest request){
        List<String> paramsList = new ArrayList<String>(); // 查询参数
        String user_id = this.getUser(request).getOperatorId();
        this.getUser(request).getRoleIds();
        String sql = "";
        sql = " select a.role_id, a.role_name " +
              "    from t_role a, " + 
              "         (select min(role_level) role_level " + 
              "             from t_user_role c, t_role e " + 
              "            where operator_id = ? " + 
              "              and c.role_id = e.role_id) b " + 
              "   where a.status = 1  and a.role_level >= b.role_level";
        paramsList.add(user_id);
//        //视野权限控制
//        if(this.getUser(request).getRoleLevel() == 0){//系统管理员
//            sql += " and a.role_level >= b.role_level ";
//        }else {//其他权限
//            sql += " and a.role_level > b.role_level ";
//        }
        sql += " order by a.role_level, a.role_order ";
        logger.debug("sql :" + str.getSql(sql, paramsList));

        return db.queryForList(sql,paramsList);
    }
    
    

    /**
     * 保存信息
     * @param request
     * @return
     */
    public int saveOrUpdateUser(HttpServletRequest request) {
        String operator = this.getOperatorId(request);
        String method = req.getValue(request, "method");
        String name = req.getValue(request, "name"); //姓名
        String mobile = req.getValue(request, "mobile");//手机号码
        String operator_id = req.getValue(request, "operator_id");//工号
        String old_operator_id = req.getValue(request, "old_operator_id");//工号
        String org_id = req.getValue(request, "org_id");//组织编号
        String[] role_list = req.getValues(request, "role_id");//角色
        String email = req.getValue(request, "email");//邮箱
        String user_order = req.getValue(request, "user_order");//排序
        String sql = "";

        BatchSql batchSql = new BatchSql();
        if (method.equals("create")) {
            //插入用户信息
        	//String password = operator_id.substring(operator_id.length()-3)+mobile.substring(mobile.length()-3); //密码规则：工号后三位+手机号后三位
        	//密码规则：123456
        	String password = "123456";
            sql = "insert into t_user(operator_id,name,mobile,org_id,password,status,email," +
                  "create_operator,create_date) "  +
                  " values (?,?,?,?,md5(?),1,?,?,sysdate())";
            batchSql.addBatch(sql, new Object[] {operator_id, name, mobile, org_id,password, email, 
                    operator});
        } else if (method.equals("edit")) {
            //修改用户信息
            sql = "update t_user a set operator_id = ? ,a.name = ?, a.mobile = ?, a.org_id = ?," +
                  " email = ?,operating_code=? ,operating_date = sysdate()  " +
                  "  where operator_id = ? ";
            batchSql.addBatch(sql, new Object[] {operator_id, name, mobile, org_id, email,
                    operator,old_operator_id});
            
            //重新插入用户角色
            sql = "delete from t_user_role where operator_id=?";
            batchSql.addBatch(sql, new Object[]{old_operator_id});
        }
        
        for (int i = 0; i < role_list.length; i++) {
            String role_id = role_list[i];
            sql = "insert into t_user_role (operator_id,role_id) values (?,?)";
            batchSql.addBatch(sql, new Object[]{operator_id, role_id});
        }
        
        return db.doInTransaction(batchSql);
    }
	
	/**
     * 判断某个工号是否已存在 注意：即使是失效工号也不能有重复。
     * @param request
     * @return 1：已存在 0：不存在
     */
    public int getUserCountByOperatorId(HttpServletRequest request) {
        String oper_id = req.getValue(request, "operator_id");
        String sql = "select count(1) from t_user a where operator_id=? ";
        return db.queryForInt(sql, new Object[] {oper_id});
    }

    /**
     * 验证手机号码加状态正常唯一
     * @param request
     * @return
     */
    public int checkUserMsisdn(HttpServletRequest request) {
        String method = req.getValue(request, "method");
        String mobile = req.getValue(request, "mobile");//手机号码
        String operator_id = req.getValue(request, "operator_id");//工号
        String sql = "";
        int count = 0;
        if("create".equals(method)) {
            sql = "select count(1) from t_user a where a.mobile=? and a.status=1 ";
            count = db.queryForInt(sql, new Object[] {mobile});
        } else if("edit".equals(method)) {
            sql = "select count(1) from t_user a where a.mobile=? and a.operator_id<>? and a.status=1";
            count = db.queryForInt(sql, new Object[] {mobile, operator_id});
        }
        //logger.debug("用户号码是否已存在:" + count);
        return count;
    }
    
    /**
     * 验证工号是否唯一
     * @param request
     * @return
     */
    public int checkUserOperId(HttpServletRequest request) {
        String method = req.getValue(request, "method");
        String operator_id = req.getValue(request, "operator_id");//工号
        String sql = "";
        int count = 0;
        if("create".equals(method)) {
            sql = " select count(1) from t_user a where a.operator_id = ? ";
            count = db.queryForInt(sql, new Object[] {operator_id});
        } 
        logger.debug("用户号码是否已存在:" + count);
        return count;
    }
    
    /**
     * 验证手机号码加状态正常唯一（more）
     * @param request
     * @return
     */
    public String  checkMoreUserMsisdn(HttpServletRequest request) {
        String mobiles = req.getAjaxValue(request, "mobiles");
        mobiles = mobiles.substring(0, mobiles.length()-1);
        String sql = "";
        String count = "";
        int c = 0;
        String[] mobile = mobiles.split(",");
        for(String mobi : mobile){
        	sql = "select count(1) from t_user a where a.mobile=? and a.status=1 ";
            c = db.queryForInt(sql, new Object[] {mobi});
            if(c > 0){
            	return c + mobi;
            }
        }
        return "0";
    }
    
    /**
     * 验证组织是否存在
     * 不存在返回 0
     * @param request
     * @return
     */
    public String  checkMoreUserOrg(HttpServletRequest request) {
        String orgIds = req.getAjaxValue(request, "orgIds");
        logger.debug(orgIds);
        orgIds = orgIds.substring(0, orgIds.length()-1);
        String sql = "";
        String count = "";
        int c = 0;
        String[] oid = orgIds.split(",");
        for(String id : oid){
        	
        	sql = " select count(1) from dual where ? in (select DISTINCT org_id from t_organization b where b.STATUS = 1  ) ";
            c = db.queryForInt(sql, new Object[] {id});
            if(c == 0){
            	return c + id;
            }
        }
        return "1";
    }
    
    /**
     * 验证角色是否存在
     * 不存在返回 0
     * @param request
     * @return
     */
    public String  checkMoreUserRole(HttpServletRequest request) {
        String roleIds = req.getAjaxValue(request, "roleIds");
        roleIds = roleIds.substring(0, roleIds.length()-1);
        String sql = "";
        String count = "";
        int c = 0;
        String[] oid = roleIds.split(",");
        for(String id : oid){
        	
        	sql = " select count(1) from dual where ? in (select DISTINCT ROLE_NAME  from t_role b where b.STATUS = 1 ) ";
            c = db.queryForInt(sql, new Object[] {id});
            if(c == 0){
            	return c + id;
            }
        }
        return "1";
    }
    
    /**
     * 多用户保存信息
     * @param request
     * @return
     */
    public int saveMoreUserInfo(HttpServletRequest request) {
        String operator = this.getOperatorId(request);
        String[] operatorIds = req.getValues(request, "operator_id");//工号
        String[] names = req.getValues(request, "name"); //姓名
        String[] mobiles = req.getValues(request, "mobile");//手机号码
        String[] orgIds = req.getValues(request, "org_id");//组织
        String[] roleIds = req.getValues(request, "role_id");//角色
        String sql = "";

        BatchSql batchSql = new BatchSql();
        //角色名称转换为角色ID
        String rId = "";
        String pwd = "123456";
        for(int i = 0; i< roleIds.length; i ++){
        	rId += this.getIdByName(roleIds[i]) + ",";
        }
        String[] rIds = rId.split(",");
        //保存
        for(int i = 0; i < operatorIds.length; i++){
        	//密码规则：工号后三位+手机号后三位
        	sql = "insert into t_user(operator_id,name,mobile,org_id,password,status," +
                  "create_operator,create_date) "  +
                  " values (?,?,?,?,md5(?),1,?,sysdate()) ";
        	batchSql.addBatch(sql, new Object[]{operatorIds[i], names[i], mobiles[i], orgIds[i], pwd, operator });
        	//角色保存
        	sql = " insert into t_user_role (operator_id, role_id) values(?, ?) ";
        	batchSql.addBatch(sql, new Object[]{operatorIds[i], rIds[i]});
        }
        return db.doInTransaction(batchSql);
    }
    
    /**
     * 根据角色名称获取角色Id
     * @param name
     * @return
     */
    public String getIdByName (String name){
    	String sql = " select a.role_id from t_role a where a.role_name = ? ";
    	return db.queryForString(sql, new Object[]{name});
    }

    /**
     * 工号失效，恢复，密码复位操作
     * @param request
     * @return
     */
    public int deleteUser(HttpServletRequest request) {
        String operator_id = req.getValue(request, "operator_id");
        String flag = req.getValue(request, "flag");
        String sql = "";
        BatchSql batchSql = new BatchSql();
        if("".equals(flag)) {
            return 0;
        }
        if(flag.equals("1")) {//删除
            sql = "update t_user a set a.status=0  where operator_id=?";
            batchSql.addBatch(sql, new Object[] {operator_id});
            sql = "update t_user_role a set a.status=0  where operator_id=?";
            batchSql.addBatch(sql, new Object[] {operator_id});

        } else if(flag.equals("0")) {//恢复有效
            sql = "update t_user a set a.status=1 where operator_id=?";
            batchSql.addBatch(sql, new Object[] {operator_id});
            sql = "update t_user_role a set a.status=1 where operator_id=?";
            batchSql.addBatch(sql, new Object[] {operator_id});

        } else if(flag.equals("3")) {//密码复位
        	sql = "select a.mobile from t_user a where a.operator_id = ?";
        	String mobile = db.queryForString(sql, new Object[] {operator_id});
        	
        	//String password = operator_id.substring(operator_id.length()-3)+mobile.substring(mobile.length()-3);
        	String password = "123456";
            sql = "update t_user a set a.password= md5(?) where operator_id=?";
            batchSql.addBatch(sql, new Object[] {password,operator_id});
        }
        return db.doInTransaction(batchSql);
    }
    
    /**
     * 根据组织获取角色列表
     * @param request
     * @return
     */
    public List<?> getRoleListByOgrLevel(HttpServletRequest request) {
        String org_level = req.getValue(request, "org_level");//组织层级
        String org_id = req.getValue(request, "org_id");//组织编号
        List<String> paramsList = new ArrayList<String>(); // 查询参数
        String sql ="select a.role_id, a.role_name " +
                    "  from t_role a " + 
                    " where a.status = 1 " ;
        if("99".equals(org_id)||"0".equals(org_id)){//如果是根或者省公司，只加载省公司角色
            sql+=" and a.role_level = 1 ";
        }else{
            if("2".equals(org_level)){//如果是地市层级，只加载地市角色
                sql+=" and a.role_level = 2 ";
            }else if("3".equals(org_level)){//如果是县区层级，加载县区角色和客户经理
                sql+=" and a.role_level >= 3 ";
            }else{//其它的不加载角色
                sql+=" and 1 = 2 ";
            }
        }
        sql+= " order by a.role_order ";
        return db.queryForList(sql,paramsList);
    }
    
    /** 
     * 用户验证 导入
     * @author 张成才 2019-01-11
     * @param action
     * @param request
     * @param file
     * @param fileFileName
     * @return
     */ 
    public String impUserCheckResult(UserCfgController action, HttpServletRequest request, CommonsMultipartFile file, String fileFileName) {
        //临时表名
        String operatMonth = req.getValue(request, "operatMonth");
        String operatorId = this.getUser(request).getOperatorId();
        String v_table_name = "";
        String import_code = this.getUser(request).getOperatorId();
        v_table_name = "ls_" + new DateHelper().getToday("yyyyMMddss") + Math.round(Math.random() * 1000);
        //Excel表头格式验证
        try {
            if(file == null) {
                request.setAttribute("error", "上传文件不存在！");
                return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
            }
            HSSFWorkbook wb = null;
            try {
                wb = new HSSFWorkbook(file.getInputStream());
                HSSFSheet sheet1 = wb.getSheetAt(0);
                int num = sheet1.getLastRowNum();
                if(num < 1) {
                    request.setAttribute("error", "Excel中无数据！");
                    return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
                }
            }
            catch (Exception ex) {
                request.setAttribute("error", "Excel解析错误！");
                return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
            }
            // 上传文件存档
            String save_dir = request.getRealPath("/WEB-INF/jsp/baobeisys") + "/upload/userinfo/";
            FileHelper fileHelper = new FileHelper();
            String fileName = save_dir + fileHelper.getToFileName(fileFileName);
            fileHelper.copyFile(file, fileName);
            
            HSSFRow row = wb.getSheetAt(0).getRow(0);
            int cols = row.getPhysicalNumberOfCells();
            String[] columns = null;
            String table_columns = null;
            String insertSql = null;
            String[][] header = null;
            String err = "";
            //导入 和 覆盖 模版字段相同
            columns = new String[] {"工号", "姓名", "手机", "邮箱", "用户归属组织编号", "用户角色"};
            // 创建临时表
            table_columns = " ( operator_id varchar(300), name varchar(300), mobile varchar(300), " +
            				" email varchar(300), org_id varchar(300), role_id varchar(400) )";
            // 插入数据到临时表
            insertSql = "insert into " + v_table_name.toLowerCase() + " values(?, ?, ?, ?, ?, ?)";
            header = new String[][] { {"工号", "operator_id"}, {"姓名", "name"},
                        {"手机", "mobile"}, {"邮箱", "email"},
                        {"用户归属组织编号", "org_id"}, {"用户角色", "role_id"}, {"错误信息", "error"}};
            err = this.checkExcelHead(wb, action, request, cols, columns);
            
            if(!err.equals("")){
                return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
            }
          
            // 导入数据库
            BatchSql batchSql = new BatchSql();
            String createSql = "create table " + v_table_name.toLowerCase() + table_columns;
            batchSql.addBatch(createSql);
            HssfHelper hssfHelper = new HssfHelper();
            short[] map = new short[cols];
            for(int i = 0; i < cols; i++) {
                map[i] = Short.parseShort(Integer.toString(i));
            }
            hssfHelper.importExcelToTable(file, map, insertSql, batchSql, 0);
            //数据库建立临时表
            int excuser = db.doInTransaction(batchSql);
            logger.debug(" excuser  :" + excuser);
            if(excuser == 0) {
                request.setAttribute("impResult", 0);
                return "WEB-INF/jsp/baobeisys/sys/usercfg/impResult";
            } else {
            	String processSql = "";
            	BatchSql batch = new BatchSql();
            	//错误信息预留字段
            	processSql = " alter table  " + v_table_name.toLowerCase() + " add (error varchar(400)) ";
            	batch.addBatch(processSql);
                //导入信息前后去空格
                processSql = " update " + v_table_name.toLowerCase() + 
                		     " set operator_id = trim(operator_id), " +
                		     "     name = trim(name), " +
                		     "     mobile = trim(mobile), " +
                		     "     email = trim(email), " +
                		     "     org_id = trim(org_id), " +
                		     "     role_id = trim(role_id) " ;
                batch.addBatch(processSql);
                //删除空数据
                processSql = " delete from " + v_table_name.toLowerCase() + 
		           		     " where operator_id = '' " +
		           		     "   and name = '' " +
		           		     "   and mobile = '' " +
		           		     "   and email = '' " +
		           		     "   and org_id = '' " +
		           		     "   and role_id = '' " ;
                batch.addBatch(processSql);
	           //工号不能为空
	           processSql = " update " + v_table_name.toLowerCase() + 
	        		   		"    set error = '工号不能为空！' " +
	        		   		"  where operator_id = '' " +
	        		   		"    and error is  null ";
	           logger.debug("processSql : " + processSql);
	           batch.addBatch(processSql);
	           //工号必须为数字
	           processSql = " update " + v_table_name.toLowerCase() + 
		       		   		"    set error = '工号必须为数字！' " +
		       		   		"  where (operator_id REGEXP '[^0-9.]') = 1 " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
	           
	           //工号在Excel中重复
	           processSql = " update " + v_table_name.toLowerCase() + 
	        		   		" a, (select operator_id from " + v_table_name.toLowerCase() + " group by operator_id having count(1) > 1 ) b  " + 
		       		   		"    set a.error = '工号在Excel中重复' " +
		       		   		"  where a.operator_id = b.operator_id " +
		       		   		"    and a.error is  null ";
	           batch.addBatch(processSql);
	           //工号在数据库中存在
	           processSql = " update " + v_table_name.toLowerCase() + 
	       		   			" a, t_user b  " + 
		       		   		"    set a.error = '工号已经录入！' " +
		       		   		"  where a.operator_id = b.operator_id " +
		       		   		"    and a.error is  null ";
	           batch.addBatch(processSql);
	           //姓名不能为空
	           processSql = " update " + v_table_name.toLowerCase() + 
	        		   		"    set error = '姓名不能为空！' " +
	        		   		"  where name = '' " +
	        		   		"    and error is  null ";
	           batch.addBatch(processSql);
	           //手机号码不能为空 
	           processSql = " update " + v_table_name.toLowerCase() + 
		       		   		"    set error = '手机不能为空！' " +
		       		   		"  where mobile = '' " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
	           //手机号码必须为数字
	           processSql = " update " + v_table_name.toLowerCase() + 
		       		   		"    set error = '手机号码必须为数字！' " +
		       		   		"  where  (mobile REGEXP '[^0-9.]') = 1 " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
	          
	           //组织编号不能为空 
	           processSql = " update " + v_table_name.toLowerCase() + 
		       		   		"    set error = '组织编号不能为空！' " +
		       		   		"  where org_id = '' " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
	           //组织编号不存在
	           processSql = " update " + v_table_name.toLowerCase() + " a  " + 
		       		   		"    set a.error = '组织编号不存在！' " +
		       		   		"  where a.org_id not in(select b.org_id from t_organization b where b.status = 1) " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
            
	           //用户角色不能为空
	           processSql = " update " + v_table_name.toLowerCase() + 
		       		   		"    set error = '用户角色不能为空！' " +
		       		   		"  where role_id = '' " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
	           
	           //用户角色不存在
	           processSql = " update " + v_table_name.toLowerCase() + " a " + 
		       		   		"    set a.error = '用户角色不存在！' " +
		       		   		"  where a.role_id not in(select b.role_name from t_role b ) " +
		       		   		"    and error is  null ";
	           batch.addBatch(processSql);
               
	           //插入用户表
	           processSql = "insert into t_user(OPERATOR_ID, name,  PASSWORD, org_id, STATUS, mobile, email, CREATE_DATE, OPERATING_CODE) " +
	        		   		"select operator_id, name, md5('123456'), org_id, 1, mobile, email, SYSDATE(), " + operatorId + 
	        		   		" from " + v_table_name.toLowerCase() + " where error is null ";
	          batch.addBatch(processSql);
	          
	          //更新角色编码
	          processSql = " update " + v_table_name.toLowerCase() + " a, t_role b " + 
		       		   		"    set a.role_id = b.role_id " +
		       		   		"  where a.role_id = b.role_name " ;
	          batch.addBatch(processSql);
	          //插入角色表(无错数据)
	          processSql = "insert into t_user_role (OPERATOR_ID, role_id, status) " +
	        		  	   "select operator_id, role_id, 1 from " + v_table_name.toLowerCase() + " where error is null ";
	          batch.addBatch(processSql);
              
	          //删除临时表无错数据
	          processSql = " delete from  " + v_table_name.toLowerCase() + " where error is  null ";
		      batch.addBatch(processSql);
                
                int result = 0;
                result = db.doInTransaction(batch);
                
                if(result == 1) {// 导入成功
                    request.setAttribute("impResult", 1);
                    
                    String dir = request.getRealPath("WEB-INF/jsp/baobeisys") + "/download/userinfo/";
                    HSSFWorkbook wbExport = new HSSFWorkbook();
                    String sql = "select t.* from " + v_table_name.toLowerCase() + " t ";
                    List exp_list = db.queryForList(sql);

                    wbExport = new HssfHelper().export(exp_list, header, "sheet1");
                    String to_file_name = v_table_name.toLowerCase() + ".xls";
                    File outFile = new File(dir, to_file_name);
                    try {
                        FileOutputStream outStream = new FileOutputStream(outFile);
                        wbExport.write(outStream);
                        outStream.close();
                        request.setAttribute("to_file_name", to_file_name);
                    }
                    catch (Exception e) {
                        request.setAttribute("impResult", -6);
                        logger.error(e.toString());
                    }
                } else if(result == 0) {
                	request.setAttribute("impResult",-1);
                }
            }
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            request.setAttribute("impResult", -4);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("impResult", -3);
        }
        //logger.debug("ls_table : " + v_table_name.toLowerCase());
        db.update(" drop table " + v_table_name );
        return "WEB-INF/jsp/baobeisys/sys/usercfg/impResult";
    }
    
    /**
     * 检查Excel标题
     * @param request
     * @return
     */
    public String checkExcelHead(HSSFWorkbook wb, UserCfgController action,HttpServletRequest request, int cols, String[] columns) {
        HssfHelper hssfHelper = new HssfHelper();
        HSSFRow row = wb.getSheetAt(0).getRow(0);
        if(row == null) {
            request.setAttribute("error", "导入格式错误：没有标题行!");
            return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
        } else if(cols != columns.length) {
            request.setAttribute("error", "导入格式错误：导入的Excel必须为" + columns.length + "列！");
            return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
        }
        String cellValues = ",";
        for(short i = 0; i < columns.length; i++) {
            HSSFCell cell = row.getCell(i);
            String cellValue = hssfHelper.getCellStringValue(cell);
            cellValues += cellValue + ",";
            if(cellValue.equals("")) {
                request.setAttribute("error", "标题行格式错误：导入第" + (i + 1) + "列列名不能为空！");
                return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
            } else if(!cellValue.equals(columns[i])) {
                request.setAttribute("error", "标题行格式错误：导入第" + (i + 1) + "列列名必须为“" + columns[i] + "”！");
                return "WEB-INF/jsp/baobeisys/sys/usercfg/showErrors";
            }
        }
        return "";
    }
}
