package com.sgy.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.sgy.util.common.StringHelper;
import com.sgy.util.spring.SpringHelper;

/**
 * 数据库操作类
 * @author chang
 * @createDate Aug 5, 2013
 * @description
 */
public class DBHelperSpring extends JdbcDaoSupport {
	private static Logger logger = Logger.getLogger(DBHelperSpring.class);
	public final static int DEFAULT_FETCHSIZE = 32;//默认的fetchsize
	public TransactionTemplate transactionTemplate;
	public LobHandler lobHandler;
	public StringHelper str = new StringHelper();

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public synchronized static DBHelperSpring getInstance() {
		DBHelperSpring db = null;
		try {
			db = (DBHelperSpring) SpringHelper.getBean("dbHelper");
		} catch (Exception e) {
			logger.error("创建数据操作对象失败！");
		}
		return db;
	}

	public synchronized static Connection getDBConnection() {
		Connection conn = null;
		DBHelperSpring db = DBHelperSpring.getInstance();
		try {
			conn = db.getConnection();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return conn;
	}

	/**
	 * 查询条件确定时
	 * @param sql 传入的sql语句: select *from table where name=? and msisdn like '%?%'
	 * @param objects 传入的参数 new Object[] { "张三",1234}
	 * @param rowMapper
	 * @return
	 */
	public Object queryForObject(String sql, Object[] objects, RowMapper<?> rowMapper) {
		Object object = null;
		object = this.getJdbcTemplate().queryForObject(sql, objects, rowMapper);
		return object;
	}

	/**
	 * 返回一条记录
	 * @param sql 传入的sql语句: select *from table where user_id=?
	 * @param objects
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, Object[] objects) {
		Map<String, Object> map = null;
		try {
			map = this.getJdbcTemplate().queryForMap(sql, objects);
		} catch (EmptyResultDataAccessException e) {

		} catch (Exception e) {
			logger.error(e);
			logger.error(str.getSql(sql, objects));
		}
		if (map == null)
			map = new HashMap<String, Object>();
		return map;
	}
	
	/**
     * 返回一条记录 支持占位符 
     * @param sql 传入的sql语句: select *from table where user_id=?
     * @param list 查询时条件不确定，将条件放入一个List<String>中
     * @return
     */
    public Map<String, Object> queryForMap(String sql, List<String> list) {
        
        List<String> cntSqlList = new ArrayList<String>();
        cntSqlList.addAll(list);
        String orderSql = this.replaceSqlOrder(sql);
        //需要查看替换掉order by之后占位符是否减少
        int cnt = this.getCharCnt(orderSql);
        if (cnt != cntSqlList.size()) {//如果替换order by之后占位符少了，将list从后去除cntSqlList.size()-cnt个参数
            int removeCnt = 0;
            int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
            for (int i = cntSqlList.size()-1; i >= 0; i--) {
                cntSqlList.remove(i);
                removeCnt ++;
                if (removeCnt == rCnt) {
                    break;
                }
            }
        }
        String cntSql = this.replaceSqlCount(orderSql);
        //需要查看替换成count(1)之后占位符是否减少
        cnt = this.getCharCnt(cntSql);
        if (cnt != cntSqlList.size()) {//如果替换count(1)之后占位符少了，将list从前去除list.size()-cnt个参数
            int removeCnt = 0;
            int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
            for (int i = 0; i < cntSqlList.size(); i++) {
                cntSqlList.remove(i);
                removeCnt ++;
                if (removeCnt == rCnt) {
                    break;
                }
            }
        }
        
        return this.queryForMap(sql, list.toArray());
    }
    

	public Map<String, Object> queryForMap(String sql) {
		Map<String, Object> map = null;
		try {
			map = this.getJdbcTemplate().queryForMap(sql);
		} catch (EmptyResultDataAccessException e) {

		} catch (Exception e) {
			logger.error(e);
			logger.error(sql);
		}
		if (map == null)
			map = new HashMap<String, Object>();
		return map;
	}

	/**
	 * 获取某个字段的值
	 * @param sql
	 * @param args
	 * @return
	 */
	public String queryForString(String sql, Object[] args) {
		try {
			return StringHelper.notEmpty(this.getJdbcTemplate().queryForObject(sql, args, String.class));
		} catch (Exception ex) {
			logger.error(str.getSql(sql, args));
			return "";
		}
	}

	public String queryForString(String sql) {
		try {
			return StringHelper.notEmpty(this.getJdbcTemplate().queryForObject(sql, null, String.class));
		} catch (Exception ex) {
			logger.error(sql);
			return "";
		}
	}
	
	public String queryForString(String sql,List<String> list) {
		try {
			return StringHelper.notEmpty(this.getJdbcTemplate().queryForObject(sql, list.toArray(), String.class));
		} catch (Exception ex) {
			logger.error(sql);
			return "";
		}
	}
	/**
	 * 返回一条记录剔除null值
	 * @param sql 传入的sql语句: select *from table where user_id=?
	 * @param objects
	 * @return
	 */
	public Map<String, Object> queryForMapNotNull(String sql, Object[] objects) {
		Map<String, Object> map = null;
		Map<String, Object> temp = new HashMap<String, Object>();
		try {
			map = this.getJdbcTemplate().queryForMap(sql, objects);
			if (map != null) {
				Set<String> s = map.keySet();
				for (Iterator<String> iter = s.iterator(); iter.hasNext();) {
					String key = StringHelper.notEmpty(iter.next()).toString();
					String value = StringHelper.notEmpty(map.get(key)).toString();
					temp.put(key, value);
				}
				map.clear();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return temp;
	}
	/**
	 * 返回相应sequence的下一个值
	 * @param sequenceName sequence名称
	 * @return 下个值
	 */
	public String getNextSequenceValue(String sequenceName) {
		Map<String, Object> map = null;
		String nextVal = "";
		try {
			map = this.getJdbcTemplate().queryForMap("select " + sequenceName + ".NEXTVAL SEQ from dual");
			nextVal = StringHelper.get(map, "SEQ");
		} catch (Exception e) {
			logger.error(e);
		}
		return nextVal;
	}
	
	/**
	 * Mysql返回相应sequence的下一个值
	 * @param sequenceName sequence名称
	 * @return 下个值
	 */
	public String getMysqlNextSequenceValue(String sequenceName) {
		String nextVal = "";
		String sql = "";
		try {
			sql = "select nextval('" + sequenceName + "') from dual";
			nextVal = this.queryForString(sql);
		} catch (Exception e) {
			logger.error(e);
		}
		return nextVal;
	}
	
	/**
	 * 获取数据库当前时间
	 * @param time_formate 日期格式
	 * @return
	 */
	public String getSysdate(String time_formate) {
		Map<String, Object> map = null;
		String sysdate = "";
		try {
			map = this.getJdbcTemplate().queryForMap("select to_char(sysdate,'"+time_formate+"') date_time from dual");
			sysdate = StringHelper.get(map, "DATE_TIME");
		} catch (Exception e) {
			logger.error(e);
		}
		return sysdate;
	}

	/**
	 * 返回数据集
	 * @param sql 传入的sql语句: select *from table where user_id=?
	 * @param objects
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Object[] objects) 
	{
		return this.queryForList(sql, objects, DEFAULT_FETCHSIZE);
	}
	
	/**
	 * 返回数据集
	 * 查询时条件不确定，将条件放入一个List<String>中
	 * @param sql
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, List<String> list) {
		return this.queryForList(sql, list.toArray(), DEFAULT_FETCHSIZE);
	}

	/**
	 * 查询条件不确定时返回数据集
	 * @param sql sql_where拼接 sql="select * from table where name='"+v_name+"'";
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql) {
		return this.queryForList(sql, DEFAULT_FETCHSIZE);
	}
	
	/**
	 * 查询条件不确定时返回数据集
	 * @param sql sql_where拼接 sql="select * from table where name='"+v_name+"'";
	 * @param fetchSize 一次获取的数据条数
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, int fetchSize) {
		JdbcTemplate jdbc = this.getJdbcTemplate();
		jdbc.setFetchSize(fetchSize);
		List<Map<String, Object>> list = null;
		try {
			list = jdbc.queryForList(sql);
		} catch (Exception e) {
			logger.error(e);
			logger.error(sql);
		}
		if (list == null){
			list = new ArrayList<Map<String, Object>>();
		}
		return list;
	}
	
	/**
	 * 返回数据集
	 * @param sql 传入的sql语句: select *from table where user_id=?
	 * @param objects
	 * @param fetchSize
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Object[] objects, int fetchSize) {
		JdbcTemplate jdbc = this.getJdbcTemplate();
		jdbc.setFetchSize(fetchSize);
		List<Map<String, Object>> list = null;
		try {
			list = jdbc.queryForList(sql, objects);
		} catch (Exception e) {
			logger.error(e);
			logger.error(sql);
		}
		if (list == null)
			list = new ArrayList<Map<String, Object>>();
		return list;
	}
	
	/**
	 * 返回数据集
	 * 查询时条件不确定，将条件放入一个List<String>中
	 * @param sql
	 * @param list
	 * @param fetchSize
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, List<String> list, int fetchSize) {
		return this.queryForList(sql, list.toArray(), fetchSize);
	}
	

	/**
	 * insert,update,delete 操作
	 * @param sql 传入的语句 sql="insert into tables values(?,?)";
	 * @param objects
	 * @return 0:失败 1:成功
	 */
	public int update(String sql, Object[] objects) {
		int exc = 1;
		try {
			this.getJdbcTemplate().update(sql, objects);
		} catch (Exception e) {
			exc = 0;
			logger.error(e);
			logger.error(str.getSql(sql, objects));
		}
		return exc;
	}

	public int update(String sql) {
		int exc = 1;
		try {
			this.getJdbcTemplate().update(sql);
		} catch (Exception e) {
			exc = 0;
			logger.error(sql);
			logger.error(e);
		}
		return exc;
	}

	/**
	 * 返还记录数
	 * @param sql 传入的sql语句 select count(*) from table where name=?
	 * @param objects 参数值
	 * @return -1:数据库异常
	 */
	public int queryForInt(String sql, Object[] objects) {
		int exc = -1;
		try {
			exc = this.getJdbcTemplate().queryForInt(sql, objects);
		} catch (Exception e) {
			exc = -1;
			logger.error(e);
		}
		return exc;
	}
	
	/**
	 * 返还记录数
	 * @param sql 传入的sql语句 select count(*) from table where name=?
	 * @param list 参数值
	 * @return -1:数据库异常
	 */
	public int queryForInt(String sql, List<String> list) {
		return this.queryForInt(sql, list.toArray());
	}

	/**
	 * 返还记录数
	 * @param sql 传入的sql语句直接拼接好
	 * @return
	 */
	public int queryForInt(String sql) {
		return this.getJdbcTemplate().queryForInt(sql);
	}

	/**
	 * 返还记录数--返回记录数超出int范围
	 * @param sql 传入的sql语句直接拼接好 sql="select count(*) from table where name='"+mike+"'"
	 * @return
	 */
	public Long queryForLong(String sql) {
		return this.getJdbcTemplate().queryForLong(sql);
	}

	/**
	 * displayTag 数据库分页
	 * @param sql
	 * @param pageSize
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getForList(String sql, int pageSize, HttpServletRequest request) {
		// 页码标识的参数名
		String pageIndexName = new org.displaytag.util.ParamEncoder("row")
				.encodeParameterName(org.displaytag.tags.TableTagParameters.PARAMETER_PAGE);
		// 自动取得当前页码标识,开始默认为0
		int pageIndex = GenericValidator.isBlankOrNull(request
				.getParameter(pageIndexName)) ? 0 : (Integer.parseInt(request
				.getParameter(pageIndexName)) - 1);
		// beginIndex与size在displayTag显示时调用
		// beginIndex为当前开始序列
		// size为总数,displayTag根据size与pageSize自动计算总页码
		request.setAttribute("beginIndex", pageSize * pageIndex);
		int size = this.queryForInt(this.replaceSqlCount(this.replaceSqlOrder(sql)));
		request.setAttribute("size", new Integer(size));
		int first = pageIndex * pageSize;
		int last = first + pageSize;
		
		// 导出csv格式的数据
		if ("1".equals(request.getParameter("d-5394226-e"))) 
		{
            request.setAttribute("csvExportSql", sql);
            return new ArrayList<Map<String, Object>>(0);
        }
		
		if (request.getParameter("6578706f7274") == null) {
			/* oracle数据库分页查询
	         * sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
	            + " )AA  where rownum < " +last+ " )BB where rr<" + last + " and rr>=" + first;*/
	        //MySQL数据库分页查询
	        sql = " select (@i:=@i+1) as rownum, CC.* from "
	        		+ "(SELECT * FROM (SELECT AA.*  FROM (" + sql
	                + " )AA   limit 0, " +last+ " ) as BB limit " + first + ", " + last + ")  as CC ";
	        
		}
		return this.queryForList(sql);
	}
	
	/**
	 * 手机端数据库分页
	 * @param sql
	 * @param pageSize
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getForListForMobile(String sql, int pageNum,int pageSize) {
		//起始偏移量 
		int first = pageNum * 10 ; //起始偏移量
		int last = first + pageSize; //结束偏移量
		String allSql = "";
		allSql = " select * from ( " + sql + " ) as AA limit " + first + ", " + last ;
		logger.debug("allSql : " + allSql);
		return this.queryForList(allSql);
	}
	
	/**
	 * 手机端数据库分页
	 * @param sql
	 * @param pageSize
	 * @param pageIndex
	 * @param list 占位符
	 * @return
	 */
	public List<Map<String, Object>> getForListForMobile(String sql, int pageSize, int pageIndex, List<String> list) {
		int first = pageIndex * pageSize + 1;
		int last = first + pageSize;
		
		List<String> cntSqlList = new ArrayList<String>();
		cntSqlList.addAll(list);
		String orderSql = this.replaceSqlOrder(sql);
		//需要查看替换掉order by之后占位符是否减少
		int cnt = this.getCharCnt(orderSql);
		if (cnt != cntSqlList.size()) {//如果替换order by之后占位符少了，将list从后去除cntSqlList.size()-cnt个参数
			int removeCnt = 0;
			int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
			for (int i = cntSqlList.size()-1; i >= 0; i--) {
				cntSqlList.remove(i);
				removeCnt ++;
				if (removeCnt == rCnt) {
					break;
				}
			}
		}
		String cntSql = this.replaceSqlCount(orderSql);
		//需要查看替换成count(1)之后占位符是否减少
		cnt = this.getCharCnt(cntSql);
		if (cnt != cntSqlList.size()) {//如果替换count(1)之后占位符少了，将list从前去除list.size()-cnt个参数
			int removeCnt = 0;
			int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
			for (int i = 0; i < cntSqlList.size(); i++) {
				cntSqlList.remove(i);
				removeCnt ++;
				if (removeCnt == rCnt) {
					break;
				}
			}
		}
		sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
		+ " )AA  where rownum < " +last+ " )BB where rr<" + last + " and rr>=" + first;
		/***********************sql语句替换结束**************************/
		logger.debug("==========="+str.getSql(sql, list));
		return this.queryForList(sql, list);
	}
	
	/**
	 * 手机端数据库分页
	 * @param sql
	 * @param pageSize
	 * @param pageIndex
	 * @param list 占位符
	 * @return
	 */
	public List<Map<String, Object>> getForListForMobile(String sql, int pageSize, String compareKey,String loadType,String 
			compareKeyValue, List<String> list) {
		List<String> cntSqlList = new ArrayList<String>();
		cntSqlList.addAll(list);
		String orderSql = this.replaceSqlOrder(sql);
		//需要查看替换掉order by之后占位符是否减少
		int cnt = this.getCharCnt(orderSql);
		if (cnt != cntSqlList.size()) {//如果替换order by之后占位符少了，将list从后去除cntSqlList.size()-cnt个参数
			int removeCnt = 0;
			int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
			for (int i = cntSqlList.size()-1; i >= 0; i--) {
				cntSqlList.remove(i);
				removeCnt ++;
				if (removeCnt == rCnt) {
					break;
				}
			}
		}
		String cntSql = this.replaceSqlCount(orderSql);
		//需要查看替换成count(1)之后占位符是否减少
		cnt = this.getCharCnt(cntSql);
		if (cnt != cntSqlList.size()) {//如果替换count(1)之后占位符少了，将list从前去除list.size()-cnt个参数
			int removeCnt = 0;
			int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
			for (int i = 0; i < cntSqlList.size(); i++) {
				cntSqlList.remove(i);
				removeCnt ++;
				if (removeCnt == rCnt) {
					break;
				}
			}
		}
		if(loadType.equals("up")){
			if(compareKeyValue.equals("-1")||compareKeyValue.equals("100000000000000000000000000000000000000000000000000000000000000000")){
				sql = "select * from (SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  )BB where rr<="+pageSize+") ORDER by "+compareKey+" desc";
			}else{
				sql = "select * from (SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  where "+compareKey+">"+compareKeyValue+" )BB where rr<="+pageSize+")  ORDER by "+compareKey+" desc";
			}
		}else{
            if(compareKeyValue.equals("-1")||compareKeyValue.equals("100000000000000000000000000000000000000000000000000000000000000000")){
            	sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
    			+ " )AA )BB where rr<="+pageSize;
			}else{
				sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  where "+compareKey+"<"+compareKeyValue+")BB where rr<="+pageSize;
			}
		}
		/***********************sql语句替换结束**************************/
		return this.queryForList(sql, list);
	}
	
	/**
	 * 手机端数据库分页
	 * @param sql
	 * @param pageSize
	 * @param pageIndex
	 * @param list 占位符
	 * @return
	 */
	public List<Map<String, Object>> getForListForMobile(String sql, int pageSize, String compareKey,String loadType,String 
			compareKeyValue, List<String> list,String orderString) {
		List<String> cntSqlList = new ArrayList<String>();
		cntSqlList.addAll(list);
		String orderSql = this.replaceSqlOrder(sql);
		//需要查看替换掉order by之后占位符是否减少
		int cnt = this.getCharCnt(orderSql);
		if (cnt != cntSqlList.size()) {//如果替换order by之后占位符少了，将list从后去除cntSqlList.size()-cnt个参数
			int removeCnt = 0;
			int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
			for (int i = cntSqlList.size()-1; i >= 0; i--) {
				cntSqlList.remove(i);
				removeCnt ++;
				if (removeCnt == rCnt) {
					break;
				}
			}
		}
		String cntSql = this.replaceSqlCount(orderSql);
		//需要查看替换成count(1)之后占位符是否减少
		cnt = this.getCharCnt(cntSql);
		if (cnt != cntSqlList.size()) {//如果替换count(1)之后占位符少了，将list从前去除list.size()-cnt个参数
			int removeCnt = 0;
			int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
			for (int i = 0; i < cntSqlList.size(); i++) {
				cntSqlList.remove(i);
				removeCnt ++;
				if (removeCnt == rCnt) {
					break;
				}
			}
		}
		if(loadType.equals("up")){
			if(compareKeyValue.equals("-1")||compareKeyValue.equals("100000000000000000000000000000000000000000000000000000000000000000")){
				sql = "select * from (SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  )BB where rr<="+pageSize+")  "+orderString+" ";
			}else{
				sql = "select * from (SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  where "+compareKey+">"+compareKeyValue+" )BB where rr<="+pageSize+") "+orderString+" ";
			}
		}else{
            if(compareKeyValue.equals("-1")||compareKeyValue.equals("100000000000000000000000000000000000000000000000000000000000000000")){
            	sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
    			+ " )AA )BB where rr<="+pageSize;
			}else{
				sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  where "+compareKey+"<"+compareKeyValue+")BB where rr<="+pageSize;
			}
		}
		/***********************sql语句替换结束**************************/
		return this.queryForList(sql, list);
	}
	
	/**
	 * 去除简单SQL语句统计条数 中的多余 order by 
	 * @param sql
	 * @return
	 */
	public String getEasyCntSql(String sql){
	    
        String find="(O|o)(R|r)(D|d)(E|e)(R|r)\\s+(B|b)(Y|y)";
        String tmp="XXXXXX";
        String sql1=sql.replaceAll(find, tmp);
        if(sql1.contains(tmp)){
            String sql2=sql1.substring(sql1.lastIndexOf(tmp));
            if(sql2.contains(")")){
                String sql3=sql2.substring(0, sql2.lastIndexOf(")"));
                sql=sql1.replaceAll(sql3, "");
                sql=sql.replaceAll(tmp, "order by");
            }else{
                sql=sql1.replaceAll(sql2, "");
                sql=sql.replaceAll(tmp, "order by");
            }
        }
        return sql;
    }
	
	/**
     * displayTag 数据库分页，支持占位符
     * @param sql
     * @param list
     * @param pageSize
     * @param request
     * @return
     */
    public List<Map<String, Object>> getForList(String sql, List<String> list, int pageSize, HttpServletRequest request) {
        // 页码标识的参数名
        String pageIndexName = new org.displaytag.util.ParamEncoder("row")
                .encodeParameterName(org.displaytag.tags.TableTagParameters.PARAMETER_PAGE);
        // 自动取得当前页码标识,开始默认为0
        int pageIndex = GenericValidator.isBlankOrNull(request
                .getParameter(pageIndexName)) ? 0 : (Integer.parseInt(request
                .getParameter(pageIndexName)) - 1);
        
        String loadType = StringHelper.notEmpty(request.getParameter("loadType"));
        String upPageNum = StringHelper.notEmpty(request.getParameter("currenr_page_up"));
        String downPageNum = StringHelper.notEmpty(request.getParameter("currenr_page"));
        
        /*********************下面一段是替换sql语句中的order by，并将查询字段替换成count(1)*************************/
        List<String> cntSqlList = new ArrayList<String>();
        cntSqlList.addAll(list);
        String orderSql = this.replaceSqlOrder(sql);
        //需要查看替换掉order by之后占位符是否减少
        int cnt = this.getCharCnt(orderSql);
        if (cnt != cntSqlList.size()) {//如果替换order by之后占位符少了，将list从后去除cntSqlList.size()-cnt个参数
            int removeCnt = 0;
            int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
            Iterator<String> iterator = cntSqlList.iterator();  
            while(iterator.hasNext()) {  
                iterator.next();  
                iterator.remove();  
                removeCnt ++;
                if (removeCnt == rCnt) {
                    break;
                }
            }
        }
        String cntSql = this.replaceSqlCount(orderSql);
        //需要查看替换成count(1)之后占位符是否减少
        cnt = this.getCharCnt(cntSql);
        if (cnt != cntSqlList.size()) {//如果替换count(1)之后占位符少了，将list从前去除list.size()-cnt个参数
            int removeCnt = 0;
            int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
            Iterator<String> iterator = cntSqlList.iterator();  
            while(iterator.hasNext()) {  
                iterator.next();  
                iterator.remove();  
                removeCnt ++;
                if (removeCnt == rCnt) {
                    break;
                }
            }
        }
        /***********************sql语句替换结束**************************/
        
        // beginIndex与size在displayTag显示时调用，beginIndex为当前开始序列，size为总数,displayTag根据size与pageSize自动计算总页码
        request.setAttribute("beginIndex", pageSize * pageIndex);
        int size = this.queryForInt(cntSql, cntSqlList);
        request.setAttribute("size", new Integer(size));
        
        // 导出csv格式的数据
        if ("1".equals(request.getParameter("d-16544-e"))) 
        {
            request.setAttribute("csvExportSql", str.getSql(sql, list));
            return new ArrayList<Map<String, Object>>(0);
        }
        
        int first = 0;
        int last = 0;
        // 分页标志
        if (request.getParameter("6578706f7274") == null && upPageNum.equals("") && downPageNum.equals("")) {
            first = pageIndex * pageSize + 1;
            last = first + pageSize;
        } else {
            if (loadType.equals("up")) {//下拉
                first = StringHelper.toInteger(upPageNum) * pageSize + 1;
                last = first + pageSize;
            } else {
                first = StringHelper.toInteger(downPageNum) * pageSize + 1;
                last = first + pageSize;
            }
        }
        /* oracle数据库分页查询
         * sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
            + " )AA  where rownum < " +last+ " )BB where rr<" + last + " and rr>=" + first;*/
        //MySQL数据库分页查询（MySQL数据库么有后台分页）
        
        return this.queryForList(sql, list);
    }
	
	/**
	 * 非displaytag标签数据库分页
	 * @param sql sql语句
	 * @param pageSize 分页条数
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getForListPage(String sql, int pageSize, HttpServletRequest request) {
		// 自动取得当前页码标识,开始默认为0
		int pageIndex = GenericValidator.isBlankOrNull(request
				.getParameter("pageIndex")) ? 0 : (Integer.parseInt(request
				.getParameter("pageIndex")) - 1);
		//求出数据总条数
		int size = this.queryForInt(this.replaceSqlCount(this.replaceSqlOrder(sql)));
		request.setAttribute("size", new Integer(size));
		//从first开始取数据
		int first = pageIndex * pageSize + 1;
		//数据取至last结束
		int last = first + pageSize;
		sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  where rownum < " +last+ " )BB where rr<" + last + " and rr>=" + first;
		return this.queryForList(sql);
	}
	
	/**
     * 非displaytag标签数据库分页，支持占位符
     * @param sql
     * @param list
     * @param request
     * @return
     */
    public List<Map<String, Object>> getForListPage2(String sql,List<String> list,HttpServletRequest request) {
        // 自动取得当前页码标识,开始默认为0
        int pageIndex = GenericValidator.isBlankOrNull(request
                .getParameter("pageNo")) ? 0 : (Integer.parseInt(request
                .getParameter("pageNo")) - 1);
        //每页显示多少条,如果没有传参,则默认每页显示10条
        int pageSize = GenericValidator.isBlankOrNull(request
                .getParameter("pageSize")) ? 10 : (Integer.parseInt(request
                .getParameter("pageSize")));

        /*********************下面一段是替换sql语句中的order by，并将查询字段替换成count(1)*************************/
        List<String> cntSqlList = new ArrayList<String>();
        cntSqlList.addAll(list);
        String orderSql = this.replaceSqlOrder(sql);
        //需要查看替换掉order by之后占位符是否减少
        int cnt = this.getCharCnt(orderSql);
        if (cnt != cntSqlList.size()) {//如果替换order by之后占位符少了，将list从后去除cntSqlList.size()-cnt个参数
            int removeCnt = 0;
            int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
            for (int i = cntSqlList.size()-1; i >= 0; i--) {
                cntSqlList.remove(i);
                removeCnt ++;
                if (removeCnt == rCnt) {
                    break;
                }
            }
        }
        String cntSql = this.replaceSqlCount(orderSql);
        //需要查看替换成count(1)之后占位符是否减少
        cnt = this.getCharCnt(cntSql);
        if (cnt != cntSqlList.size()) {//如果替换count(1)之后占位符少了，将list从前去除list.size()-cnt个参数
            int removeCnt = 0;
            int rCnt = cntSqlList.size()-cnt;//需要剔除的参数个数
            for (int i = 0; i < cntSqlList.size(); i++) {
                cntSqlList.remove(i);
                removeCnt ++;
                if (removeCnt == rCnt) {
                    break;
                }
            }
        }
        
        //求出数据总条数
        int size = this.queryForInt(cntSql, cntSqlList);
        request.setAttribute("size", new Integer(size));
        request.setAttribute("pageSize", new Integer(pageSize));
        //从first开始取数据
        int first = pageIndex * pageSize + 1;
        //数据取至last结束
        int last = first + pageSize;
        sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
                + " )AA  where rownum < " +last+ " )BB where rr<" + last + " and rr>=" + first;
        //logger.debug("=非display分页,支持占位符=="+sql);
        return this.queryForList(sql,list);
    }

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * 事务处理
	 * @param batchSqls
	 * @return
	 */
	public int doInTransaction(final BatchSql batch) {
		int exc = 1;
		if (batch == null) {
			exc = 0;
		}
		try {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						TransactionStatus status) {
					List sqlList = batch.getSqlList();
					for (int i = 0; i < sqlList.size(); i++) {
						Map sqlMap = (Map) sqlList.get(i);
						String sql = (String) sqlMap.get("sql");
						Object[] objects = (Object[]) sqlMap.get("objects");
						String sql_type = (String) sqlMap.get("sql_type");
						// 如果包括大字段操作
						if (sql_type != null && sql_type.equals("clob")) {
							int[] colIndex = (int[]) sqlMap.get("clob_index");
							updateClob(sql, objects, colIndex);
						} else {
							getJdbcTemplate().update(sql, objects);
						}
					}
				}
			});
		} catch (Exception e) {
			exc = 0;
			logger.error(e);
		}
		return exc;
	}

	/**
	 * 过程调用
	 * @param sql
	 * @param inParam
	 * @param out
	 * @return
	 */
	public ProcHelper getProcHelper(String sql) {
		ProcHelper proc = null;
		try {
			proc = new ProcHelper(this.getDataSource(), sql);
			proc.setSql(sql);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return proc;
	}

	/**
	 * 
	 * @param sql
	 *            要执行的sql
	 * @param objects
	 *            参数数组
	 * @param clobIndex
	 *            指定大字段的参数序号
	 * @return
	 */
	public int updateClob(final String sql, final Object[] objects,
			final int[] clobIndex) {
		int exc = 1;
		try {
			getJdbcTemplate()
					.execute(
							sql,
							new AbstractLobCreatingPreparedStatementCallback(
									lobHandler) {
								protected void setValues(PreparedStatement ps,
										LobCreator lobCreator)
										throws SQLException {
									boolean clobFlag = false;
									for (int i = 1; i <= objects.length; i++) {
										Object param = objects[i - 1];
										clobFlag = false;
										for (int j = 0; j < clobIndex.length; j++) {

											if (i == clobIndex[j]) {
												clobFlag = true;
												break;
											}
										}
										if (clobFlag == true) {
											lobCreator.setClobAsString(ps, i,
													(String) param);
										} else {
											ps.setObject(i, param);
										}

									}
								}
							});

		} catch (Exception e) {
			exc = 0;
			logger.error(e);
		}
		exc = 1;
		return exc;

	}

	/**
	 * 返回排序子语句
	 */
	public String addSortString(HttpServletRequest request,
			String otherSortColumn) {
		String sort = "";
		// String pageIndexName = new org.displaytag.util.ParamEncoder("row")
		// .encodeParameterName(org.displaytag.tags.TableTagParameters.);
		String sortParameterName = new ParamEncoder("row")
				.encodeParameterName(TableTagParameters.PARAMETER_SORT);
		String sortColumnName = request.getParameter(sortParameterName);
		String orderParameter = new ParamEncoder("row")
				.encodeParameterName(TableTagParameters.PARAMETER_ORDER);
		String orderType = request.getParameter(orderParameter);

		if (sortColumnName != null && !sortColumnName.equals("")) {
			sort = " order by " + sortColumnName;
			if (orderType != null && !orderType.equals("")) {
				sort += orderType.equals("1") ? " asc " : " desc";
			}
			if (!otherSortColumn.equals(""))
				sort += "," + otherSortColumn;
		} else if (!otherSortColumn.equals("")) {
			sort += " order by " + otherSortColumn;
		}
		return sort;

	}
	
	/**
	 * 替换掉sql语句中的order
	 * 只替换掉最外面的order by 
	 * @param sql
	 * @return
	 */
	public String replaceSqlOrder(String sql){
		String oldSql = sql;
	    sql = sql.toLowerCase();
		String newSql = "";
		if (sql.contains(" order ")) {
			String orderSql = oldSql.substring(sql.lastIndexOf(" order "));
			if (orderSql.contains(")")) {
				newSql = oldSql;
			} else {
				newSql = oldSql.substring(0, sql.lastIndexOf(" order "));
			}
		} else {
			newSql = oldSql;
		}
        return newSql;
    }
	
	/**
	 * 替换查询字段为count(1)
	 * 如：select * from xxxx 替换为select count(1) from xxxx
	 * @param sql
	 * @return
	 */
	public String replaceSqlCount(String sql){
	    String oldSql = sql;
	    String newSql = "";
		
		sql = sql.toLowerCase();
		if (sql.contains("group ") || sql.contains("distinct ")) {//如果语句最后有group by 不需要替换，嵌套一层
		    /*String groupSql = oldSql.substring(sql.lastIndexOf("group "));
            if (!groupSql.contains(")")) {
                return "select count(1) from (" + sql + ")";
            }*/
            return "select count(1) from (" + sql + ")  as a ";
		}
		
		//以小写字母匹配所有的select和from
		Pattern p = Pattern.compile("[\\s(]select\\s|\\sfrom\\s", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(" "+sql);
		int iSelect = 0;
		int iFrom = 0;
		//找到最外面的select对应的的是第几个from
		while(m.find()) {
			if (m.group().trim().toLowerCase().endsWith("select")) {
				iSelect ++;
			}
			if (m.group().trim().toLowerCase().equals("from")) {
				iFrom ++;
			}
			if (iSelect == iFrom) {
				break;
			}
		}
		
		int iSearch = 0;
		int fromIndex = 0;//记录最外面select对应的from的位置
		//找到最外面的select对应的from的位置
		while(iSearch < iFrom){
			if ((sql.toLowerCase()).indexOf(" from ")>0) {
				fromIndex = (sql.toLowerCase()).indexOf(" from ");
				sql = sql.replaceFirst(" from ", " #### ");
				iSearch ++;
			}
		}
		newSql = " select count(1) from "+oldSql.substring(fromIndex+5);
		return newSql;
	}
	
	/**
	 * 获取sql语句中占位符的个数
	 * @param sql
	 * @return
	 */
	public int getCharCnt(String sql){
		int cnt = 0;
		Pattern p = Pattern.compile("\\?", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		while(m.find()){
			cnt ++;
		}
		return cnt;
	}
	
	/**
     * 处理sql语句中in的占位符
     * @param inParams in的参数
     * @param paramList 参数list
     * @param splitChart inParams的分隔符
     * @return
     */
    public String rebuildInSql(String inParams, List<String> paramList, String splitChart){
        String[] paramArray = inParams.split(splitChart);
        String inSql = "";
        for (int i = 0; i < paramArray.length; i++) {
            inSql += ",?";
            paramList.add(paramArray[i]);
        }
        if (!inSql.equals("")) {
            inSql = inSql.substring(1);
        }
        return inSql;
    }
    
    /**
	 * 数据库分页
	 * @param sql sql语句
	 * @param pageSize 分页条数
	 * @return
	 */
	public List<Map<String, Object>> getForList(HttpServletRequest request, String sql, int pageSize, int pageIndex) {
		// 自动取得当前页码标识,开始默认为0
		//从first开始取数据
		int first = pageIndex * pageSize + 1;
		//数据取至last结束
		int last = first + pageSize;
		sql = "SELECT * FROM (SELECT AA.*, rownum rr  FROM (" + sql
				+ " )AA  )BB where rr<" + last + " and rr>=" + first;
		return this.queryForList(sql);
	}
	
	public static void main(String[] args) {
		String sql = "select a.id, a.system_id, a.system_msg_id, a.send_org_name, a.send_user_name,  " +
				" (select b.sys_name from t_sub_system b where b.system_id = a.system_id) system_name, " +
				" to_char(a.receive_date, 'yyyy-mm-dd hh24:mi:ss') receive_date, a.receiver,  " +
				" a.task_type, a.task_title, a.task_name, a.is_urgent, a.deal_url, a.detail_url, " +
				" a.track_url, a.todo_type, to_char(a.in_date, 'yyyy-mm-dd hh24:mi:ss') in_date, " +
				" a.from_ipaddr, decode(a.status, 1, '已处理', 0, '未处理') status, to_char(a.deal_date, 'yyyy-mm-dd hh24:mi:ss') " +
				" from t_union_todo a where a.status >= 0 ";
		DBHelperSpring test = new DBHelperSpring();
		
		String newSql = test.replaceSqlCount(test.replaceSqlOrder(sql));
	}
	
	public List getColumnName(String sql) {
        List list=new ArrayList();
        
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int num = rsmd.getColumnCount();
                for (int i = 1; i <= num; i++) {
                    String key = rsmd.getColumnName(i);
                    Hashtable hstab = new Hashtable();
                    hstab.put("column", key);
                    list.add(hstab);
                }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return list;
    }
}
