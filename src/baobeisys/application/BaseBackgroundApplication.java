package baobeisys.application;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.DBHelperSpring;

public class BaseBackgroundApplication extends Thread {
    protected static Logger logger = Logger.getLogger(BaseBackgroundApplication.class.getName());
    
    protected DBHelperSpring db;
    
    public StringHelper str = new StringHelper();
    
    public void sendSms(String msisdn, String content) {
       /* if(!msisdn.trim().equals("")) {
            String sql = "INSERT INTO lygweb.cdz_kfjk_to_send_sms" +
                        "  (serial_no, orgaddr, destaddr, to_send_time, flag, sms_detail)" + 
                        "VALUES" + 
                        "  (lygweb.my_sn.nextval , '移动报备系统' , '" + msisdn + "', sysdate , 0 , '" + content + "')";
            logger.debug(sql);
            db.update(sql);
        }*/
    }
    
    public DBHelperSpring getDb() {
        return db;
    }

    public void setDb(DBHelperSpring db) {
        this.db = db;
    }
}
