package com.sgy.util;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.spring.SpringHelper;
public class DBHelperSms extends DBHelperSpring {
	private static Logger logger = Logger.getLogger(DBHelperSpring.class); 
	public synchronized static DBHelperSms getInstance() {
		DBHelperSms db = null;
		try {
			db = (DBHelperSms) SpringHelper.getBean("dbHelperSms");
		} catch (Exception e) {
			logger.error("创建数据操作对象失败！");
		}
		return db;
	}

	public synchronized static Connection getDBConnection() {
		Connection conn = null;
		DBHelperSms db = DBHelperSms.getInstance();
		try {
			conn = db.getConnection();
		} catch (Exception e) {
			logger.error("dbSms获取数据库Connection失败！");
			logger.error(e.toString());
		}
		return conn;
	} 
}
