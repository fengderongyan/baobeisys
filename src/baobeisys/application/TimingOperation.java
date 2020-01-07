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
public class TimingOperation extends BaseBackgroundApplication{
  
    String token = "";
    /** 
     * 线程执行默认调用方法
     */ 
    public void run() {
        
        while(true) {  
        	//等待条件
        	
            if(true) {//等待条件不满足
                try {
                	logger.info("测试执行");
                    Thread.sleep(1000 * 30);//线程休眠30s
                } catch (InterruptedException e) {
                    logger.info("errorInfo : " + e.toString());
                    continue;
                }
            } else {
                for(;;){
                   
	                //每五分钟跑一次数据
	                try {
	                    Thread.sleep(1000 * 60 * 5);
	                } catch (InterruptedException e) {
	                    logger.info("errorInfo : " + e.toString());
	                    continue;
	                }
	            }
	        }
            
        }
   }
    
}