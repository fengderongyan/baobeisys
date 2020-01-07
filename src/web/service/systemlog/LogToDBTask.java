package web.service.systemlog;

import java.util.Map;

import org.apache.log4j.Logger;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.DBHelperSpring;

/**
 * 记录系统日志
 * @author 戴晓飞
 *
 */
public class LogToDBTask implements Runnable
{
    private DBHelperSpring db;
    private Map<String, Object> map;
    public final Logger logger = Logger.getLogger(this.getClass());
    
    public LogToDBTask(){}
    
    public LogToDBTask(DBHelperSpring db, Map<String, Object> map)
    {
        this.db = db;
        this.map = map;
    }
    
    public DBHelperSpring getDb()
    {
        return db;
    }

    public void setDb(DBHelperSpring db)
    {
        this.db = db;
    }
    
    public void run()
    {
    	String sql = "";
        String operator_id = StringHelper.get(map, "operator_id");
        String org_id = StringHelper.get(map, "org_id");	
    	sql = "insert into t_operating_log(operating_srl, operate_time, op_type_id, op_type_name, " +
            "       op_level_id, operate_content, operate_result, module_id, module_name, client_name, client_address, " + 
            "       client_port, server_address, server_port, server_mac,org_id,operator_id) " + 
            "values(nextval('seq_t_operating_log_id'), sysdate(), ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ifnull(?,0),?,?,?) ";
      
    	db.update(sql, new Object[] {StringHelper.get(map, "op_type_id"),
                StringHelper.get(map, "op_type_name"), StringHelper.get(map, "op_level_id"),
                StringHelper.get(map, "operate_content"), StringHelper.get(map, "operate_result"),
                StringHelper.get(map, "module_id"), StringHelper.get(map, "module_name"),
                StringHelper.get(map, "client_name"), StringHelper.get(map, "client_address"),
                StringHelper.get(map, "client_port"), StringHelper.get(map, "server_address"),
                StringHelper.get(map, "server_port"), StringHelper.get(map, "server_mac"),
                org_id,operator_id});
    }

    public Map<String, Object> getMap()
    {
        return map;
    }

    public void setMap(Map<String, Object> map)
    {
        this.map = map;
    }
    
}
