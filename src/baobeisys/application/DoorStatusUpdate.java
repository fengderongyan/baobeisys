package baobeisys.application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;


import com.alibaba.fastjson.JSONObject;
import com.sgy.util.PropertiesHelper;
import com.sgy.util.db.BatchSql;
import com.royasoft.servlet.ServletCode;


/** 
 * 定时任务执行
 * @date 2019-03-02 
 */
@Component
public class DoorStatusUpdate extends BaseBackgroundApplication{
  
    /** 
     * 线程执行默认调用方法
     */ 
    public void run() {
        
        while(true) {
        	try {
        		System.out.println("线程开启。。。");
            	//查询预订已经超过6小时的
            	String sql = " select concat(id, '') id from t_wx_door_info where status = 1 and date_add(status_update_time, interval 6 hour) < SYSDATE() ";
            	List idList = db.queryForList(sql);
            	String ids = "";
            	if(idList != null && idList.size() > 0){//有超过6小时的预订
            		for(int i = 0; i < idList.size(); i++){
            			Map idMap = (Map)idList.get(i);
            			String id = (String)idMap.get("id");
            			ids = ids + "," + id;
            		}
            		BatchSql batchSql = new BatchSql();
            		sql = " update t_wx_door_info set status = 0, status_update_time = null "
        					+ " where id in (" + ids.substring(1) + ") and status = 1 ";
            		batchSql.addBatch(sql);
            		
            		//6小时失效后，申请也失效
            		sql = " update t_wx_apply_info set status = 0 where door_info_id in(" + ids.substring(1) + ")";
            		batchSql.addBatch(sql);
            		sql = " update t_wx_sale_print_info set status = 0 where door_info_id in(" + ids.substring(1) + ") and status = 1 ";
            		batchSql.addBatch(sql);
            		db.doInTransaction(batchSql);
            		//操作完成休眠5分钟
            		try {
            			System.out.println("操作完成休眠");
                        Thread.sleep(1000 * 60 * 5);//线程休眠5分钟
                    } catch (InterruptedException e) {
                        logger.info("errorInfo : " + e.toString());
                        continue;
                    }
            	}else{
            		//没有数据休眠5分钟
            		try {
            			System.out.println("没有数据休眠");
                        Thread.sleep(1000 * 60 * 5);//线程休眠5分钟
                    } catch (InterruptedException e) {
                        logger.info("errorInfo : " + e.toString());
                        continue;
                    }
            	}
            	
			} catch (Exception e) {
				try {
					Thread.sleep(1000 * 60 * 5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					continue;
				}
			}
        	
           
            
        }
   }
    
}