package moi.service.log;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.DBHelperSpring;

/**
 * 记录系统日志
 * @author zhang
 * @createDate 2019-01-15
 */
public class MobileLogToDBTask implements Runnable {
    private DBHelperSpring db;
    private Map<String, Object> map;
    public final Logger logger = Logger.getLogger(this.getClass());
    String sql = "";
    
    public MobileLogToDBTask(){}
    
    public MobileLogToDBTask(DBHelperSpring db, Map<String, Object> map) {
        this.db = db;
        this.map = map;
    }
    
    public void run() {   
        //根据操作操作的url获取对应的模块
        String operate_url = StringHelper.get(map, "operate_url").trim();
        String deal_url = operate_url.substring(operate_url.indexOf("/mobile/") );
        String url = "";
        String url_id = "";
        //请求URL和后台URL库对比
        //根据URL和后台比对，取URL对应id
        //
        /*if(!operate_url.equals("")){
            List<Map<String,Object>> moduleUrlList = db.queryForList("select url_id,url from t_mobile_module_url t where t.status=1");
            for (int i=0; i<moduleUrlList.size(); i++){
                url = StringHelper.get(moduleUrlList.get(i), "url").trim();
                if( deal_url.equals(url) ){ //完全匹配
                    url_id = StringHelper.get(moduleUrlList.get(i), "url_id");
                    break;
                }else if(deal_url.indexOf("?")>-1){ //如果有参数
                    if( url.equals(deal_url.substring(0, deal_url.indexOf("?")))){ //去掉参数后完全相同
                        url_id = StringHelper.get(moduleUrlList.get(i), "url_id");
                        break;
                    }else if( url.indexOf(deal_url.substring(0, deal_url.indexOf("?")))>-1  ){ //去掉参数包含
                        url_id = StringHelper.get(moduleUrlList.get(i), "url_id");
                        break;
                    }
                } else if(url.indexOf(deal_url)>-1 && deal_url.indexOf("?")==-1){ //不带参数,包含
                        url_id = StringHelper.get(moduleUrlList.get(i), "url_id");
                        break;
                }
            }
        }*/
        sql = " insert into t_mobile_operating_log(operating_srl,operator_id,"+ 
			  " operate_date, operate_url, operate_content, mobile, imsi, os_version, client_version, mobile_model, url_id," +
			  " imei, session_id, org_id, org_name,client_type) " + 
              " values(nextval('t_mobile_operating_log_sid'), ?, " +
              "  sysdate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) ";
        db.update(sql, new Object[]{
        		StringHelper.get(map, "operator_id"), StringHelper.get(map, "operate_url"), StringHelper.get(map, "operate_content"), 
        		StringHelper.get(map, "mobile"), StringHelper.get(map, "imsi"), StringHelper.get(map, "os_version"), 
        		StringHelper.get(map, "client_version"), StringHelper.get(map, "mobile_model"), url_id,
        		StringHelper.get(map, "imei"), StringHelper.get(map, "session_id"),StringHelper.get(map, "org_id"), 
        		StringHelper.get(map, "org_name"),StringHelper.get(map, "client_type")});
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
    
    public DBHelperSpring getDb() {
        return db;
    }

    public void setDb(DBHelperSpring db) {
        this.db = db;
    }
}