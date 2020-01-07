package moi.service.login;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import moi.service.log.MobileLogService;

import org.springframework.stereotype.Service;

import web.service.BaseService;

/** 
 * 手机端登录
 * @author zhang
 * @date 2019-01-15 
 */
@Service("loginMiSevice")
public class LoginMiSevice extends BaseService {
	
	public MobileLogService mobileLogService;

    @Resource(name = "mobileLogService")
    public void setMobileLogService(MobileLogService mobileLogService) {
        this.mobileLogService = mobileLogService;
    }
    
    /**
	 * 校验登录
	 * @param operator_id 用户名
	 * @param password 密码
	 * @return 1：成功登录 -1:用户名不存在 -2:用户名与密码不匹配 -3:无法连接数据库
	 */
	public int checkUserLogin(String operator_id, String password){
		String sql = "";
		int exc = 0;

		sql = "select count(1) from t_user where operator_id = ?  and status=1";
		exc = db.queryForInt(sql, new Object[] { operator_id });
		if (exc == -1){
			return -3;
		}
		if (exc == 0){
			return -1;
		}

		sql = "select count(1) from t_user where operator_id = ?  and status = 1 and password = md5(?)";
		exc = db.queryForInt(sql, new Object[] { operator_id, password });

		if (exc == 0){
			return -2;
		}
		return exc;
	}
    
    public void recordMobileLog(HttpServletRequest request, String operatorId,
            String orgId, String orgName, String countyId, String url) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("operator_id", operatorId);
        map.put("mobile", operatorId);
        map.put("org_id", orgId);
        map.put("org_name", orgName);
        map.put("operate_url", url);
        map.put("region_id", countyId);
        
        mobileLogService.createMobileLog(request, map);
    }

}
