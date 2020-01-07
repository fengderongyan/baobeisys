package baobeisys.service.sys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;

/**
 * 密码功能
 * @date 2016-2-22
 */
@SuppressWarnings("unchecked")
@Service("passwordService")
public class PasswordService extends BaseService {
    
    /**
     * 验证输入的密码是否和原密码一致
     * @param request
     * @return
     */
    public int checkPassWord(HttpServletRequest request) {
        int result =0;
        String operatorId = req.getValue(request, "operator_id");//人员工号
        String oldPassword = req.getValue(request, "old_password");//原密码
        String sql = "select password,md5(?) old_password from t_user where operator_id = ? ";
        Map map = (Map) db.queryForMap(sql, new Object[]{oldPassword,operatorId});
        if ((str.get(map, "password")).equals(str.get(map, "old_password"))) {//密码一致返回1
             result=1;
        }
        return result;
    }
    
    /**
     * 保存修改密码
     * @param request
     * @return
     */
    public int savePassWord(HttpServletRequest request) {
        String operatorId = req.getValue(request, "operator_id");//人员工号
        String newPassword = req.getValue(request, "new_password");//新密码
        String sql = "";
        //修改密码
        sql = " update t_user set password = md5(?) " +
              " where operator_id = ?";
        
        return db.update(sql, new Object[] {newPassword,operatorId});
    }
    
    /********************************忘记密码功能*******************************************/
    
    /**
     * 发送短信验证码
     * @param request
     * @return
     */
    public int sendMsgCode(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
        BatchSql batch = new BatchSql();
        String code = Integer.toString((int)((Math.random()*9+1)*100000));
        String sql = "";
        int result = 0;
        
        //验证用户是否存在
        sql ="select count(1) " + 
            "   from t_user a " + 
            "  where a.mobile = ? ";
        String userCnt =db.queryForString(sql, new Object[]{msisdn,msisdn});
        // 用户号码不存在
        if("0".equals(userCnt)) {
            return result; // 不存在该用户
        }
        
        // 把之前的验证码置为无效
        sql = " update t_mobile_validate_code a set a.state = 0 where a.state = 1 and a.mobile = ? ";
        batch.addBatch(sql, new Object[] {msisdn});
        
        // 插入验证码表
        sql = " insert into t_mobile_validate_code (mobile, code, create_date, state) " +
              " values (?, ?, sysdate, 1)";
        batch.addBatch(sql, new Object[] {msisdn, code});
        result = db.doInTransaction(batch);
        logger.debug("==="+msisdn+"===发送短信验证码为：====="+code);
        
        if(result == 1) {
            //此处调用发送短信过程
            final String v_sql = " call p_send_sms(?, ?) ";
            final String servnumber = msisdn;
            final String sms_content = "获取的动态验证码为："+code+"，30分钟内有效！";
            
            new Thread(){
                public void run ()
                {
                    db.update(v_sql, new Object[] {servnumber,sms_content});
                }
            }.run();
        }
        return result;
    }
    
    /**
     * 检验验证码
     * @param request
     * @return
     */
    public int checkMsgCode(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");//手机号码
        String verify_code = request.getParameter("verify_code");//验证码
        String sql = "";
        int result = 0;
        
        // 验证短信验证码是否有效
        sql = " select count(1) from t_mobile_validate_code a " + 
              "  where a.state = 1 and a.mobile = ? and a.code = ? " + 
              "    and sysdate < trunc(a.create_date, 'mi') + 30 / 24 / 60 ";
        logger.debug("验证短信验证码是否有效：------>" + StringHelper.getSql(sql, new Object[]{msisdn,verify_code}));
        result = db.queryForInt(sql, new Object[]{msisdn,verify_code});
        if(result == 0)
        {
            return -1;  // 验证码无效
        }
        return result;
    }
    
    /**
     * 保存修改密码
     * @param request
     * @return
     */
    public int saveForgetPassWord(HttpServletRequest request) {
        String msisdn = req.getValue(request, "msisdn");//人员工号
        String newPassword = req.getValue(request, "new_password");//新密码
        BatchSql batchSql = new BatchSql();
        String sql = "";
        //修改后台密码
        sql = " update t_user set password = md5(?) " +
              " where mobile = ?";
        batchSql.addBatch(sql, new Object[] {newPassword,msisdn});
        
        return db.doInTransaction(batchSql);
    }

}
