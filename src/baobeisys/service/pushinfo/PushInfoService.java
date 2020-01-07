package baobeisys.service.pushinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import util.JPushUtil;

import org.springframework.stereotype.Service;

import web.service.BaseService;

import com.sgy.util.common.StringHelper;
import com.sgy.util.db.BatchSql;

/**
 * 消息推送
 * @date 2019-02-13
 */
@Service("pushInfoService")
public class PushInfoService extends BaseService {
	/**
     * 信息列表
     * @param request
     * @return
     */
    public List<?> getPushInfoList(HttpServletRequest request) {
        List<String> paramsList = new ArrayList<String>();
        String title = req.getValue(request, "title");//标题
        String operatorId = this.getUser(request).getOperatorId();
        //int pageNum = Integer.valueOf(request.getParameter("pageNum"));//页
        
        String sql = " select a.id, a.title, a.content, DATE_FORMAT(a.push_date, '%Y-%m-%d') push_date from t_push_all_info a where 1 = 1 ";

        if(!"".equals(title)) {
            sql += " and a.title like ? ";
            paramsList.add("%" + title + "%");
        }
        
        sql += " order by a.push_date desc ";
        logger.debug("getPushInfoList : " + str.getSql(sql, paramsList.toArray()));
        //sql = str.getSql(sql, paramsList.toArray());
        return db.getForList(sql, paramsList, req.getPageSize(request, "pageSize"), request);
        //return db.getForList(sql, paramsList.toArray(), req.getPageSize(request, "pageSize"), request);
    }
    
    /**
     * 获取推送信息详情
     * @param request
     * @return
     */
    public Map<String, Object> getDicInfo(HttpServletRequest request){
    	String id = req.getValue(request, "id");
    	String sql = " select a.id, a.title, a.content, DATE_FORMAT(a.push_date, '%Y-%m-%d') push_date from t_push_all_info a where id = ? ";
    	return db.queryForMap(sql, new Object[]{id});
    }
    
    /**
	 * 保存信息 && 推送
	 * @param request
	 * @return
	 */
	public int saveOrUpdate(HttpServletRequest request) {
		String method = req.getValue(request, "method");
        String title = req.getValue(request, "title");//标题
        String content = req.getValue(request, "content"); //推送内容
        String operatorId = this.getUser(request).getOperatorId();
        int result = 0;
        String sql = "";
        BatchSql batchSql = new BatchSql();
        
        JPushUtil push = new JPushUtil();
        //推送信息
        push.pushNotice("4", "", title);
        
        sql = "insert into t_push_all_info " +
        		"	(id, title, content, push_date) " +
        		"VALUES(nextval('t_push_all_info_sid'), ?, ?, sysdate()) ";
        batchSql.addBatch(sql, new Object[]{title, content});
        
        return db.doInTransaction(batchSql);
	}

}
