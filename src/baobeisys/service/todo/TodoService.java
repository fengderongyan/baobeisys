package baobeisys.service.todo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sgy.util.common.DateHelper;

import web.service.BaseService;

/**
 * 加载系统首页相关信息
 * @date 2018-12-20
 */
@Service("todoService")
public class TodoService extends BaseService {
    
    /**
     * 获取手机端登录信息
     * @param request
     * @return
     */
    public List getMobiLoginList(HttpServletRequest request){
        String operatorId = this.getUser(request).getOperatorId();
        String sql = "select DATE_FORMAT( a.OPERATE_TIME, '%Y-%m-%d %h:%i:%s' ) OPERATE_TIME, " +
	        		"			 b.NAME  " +
	        		"  from t_operating_log a, t_user b  " +
	        		" where OP_TYPE_ID = 'clientLogin'  " +
	        		"	 and a.OPERATOR_ID = b.OPERATOR_ID  " +
	        		" order by a.OPERATE_TIME desc ";
        return db.queryForList(sql);
    }
    
}
