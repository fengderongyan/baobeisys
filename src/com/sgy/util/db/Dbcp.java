package com.sgy.util.db;

import java.io.InputStream;
import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
 
import org.apache.log4j.Logger;

public class Dbcp
{
	
	public static Logger logger = Logger.getLogger(Dbcp.class);
	private static final String config = "commons-dbcp.properties";
	private BasicDataSource ds;
	private static Dbcp db;

	private Dbcp(String config)
	{
		this.ds = null;
		try
		{
			this.ds = createDataSource(config);
		}
		catch (Throwable thr)
		{
			throw new RuntimeException(thr);
		}
	}

	public synchronized static Dbcp getInstance()
	{

		if (db == null)
			try
			{
				db = new Dbcp(config);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		return db;
	}

	public static BasicDataSource createDataSource(String propertiesFile)
	        throws Exception
	{
		Properties properties = new Properties();
		InputStream stream = Dbcp.class.getResourceAsStream(
		        propertiesFile);
		properties.load(stream);
		stream.close();
		BasicDataSource ds = (BasicDataSource) BasicDataSourceFactory
		        .createDataSource(properties);
		return ds;
	}

	public Connection getConnection() throws SQLException
	{
		return this.ds.getConnection();
	}

	public static void close(Connection conn)
	{
		if (conn != null)
			try
			{
				conn.close();
			}
			catch (Throwable localThrowable)
			{
			}
	}

	public static void close(Statement stmt)
	{
		if (stmt != null)
			try
			{
				stmt.close();
			}
			catch (Throwable localThrowable)
			{
			}
	}

	public static void close(ResultSet rs)
	{
		if (rs != null)
			try
			{
				rs.close();
			}
			catch (Throwable localThrowable)
			{
			}
	}

	public static void close(Connection conn, Statement stmt, ResultSet rs)
	{
		close(rs);
		close(stmt);
		close(conn);
	}

	public static void close(Connection conn, Statement stmt)
	{
		close(stmt);
		close(conn);
	}

	public static void close(Statement stmt, ResultSet rs)
	{
		close(rs);
		close(stmt);
	}

	public int getExecuteCount(String sql)
	{
		int queryNum = -1;
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = this.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs != null)
			{
				rs.next();
				queryNum = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			logger.info(sql);
		}
		finally
		{
			closeAll(conn, rs, stmt);

		}
		return queryNum;
	}

	public void closeAll(Connection conn, ResultSet rs, Statement stmt)
	{
		close(conn);
		close(rs);
		close(stmt);
	}

	public String getNextSequence(String seq_name)
	{
		String sql="select "+seq_name+".nextval seq_value from dual";
		Map map=db.getValue(sql);
		return (String)map.get("SEQ_VALUE");
	}
	
	public String getStringValue(String sql)
	{
	    Map map=db.getValue(sql);
		return (String)map.get("STRING_VALUE");
	}
	public int execute(String sqlstr)
	{
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = getConnection();
			stmt = conn.createStatement();
			stmt.execute(sqlstr);
			return 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.info(sqlstr);
		}
		finally
		{
			closeAll(conn, rs, stmt);
		}
		return 0;
	}

	public ArrayList getList(String sqlString)
	{
		ArrayList<Map> pkv = new ArrayList<Map>();
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlString);
			ResultSetMetaData rsmd = rs.getMetaData();
			int num = rsmd.getColumnCount();
			while (rs.next())
			{
				Map map = new HashMap();
				for (int i = 1; i <= num; i++)
				{
					String key = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null)
						value = "";
					map.put(key, value);
				}
				pkv.add(map);
			}
		}
		catch (SQLException e)
		{
			logger.error(sqlString);
			logger.error(e.toString());
			// e.printStackTrace();
		}
		finally
		{
			closeAll(conn, rs, stmt);
		}
		return pkv;
	}

	public Map getValue(String sql)
	{
		Map map = new HashMap();
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;

		try
		{
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int num = rsmd.getColumnCount();
			if (rs.next())
			{
				for (int i = 1; i <= num; i++)
				{
					String key = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null)
						value = "";
					map.put(key, value);
				}
			}
		}
		catch (Exception e)
		{
			logger.info(sql);
			logger.info(e.toString());
		}
		finally
		{
			 closeAll(conn,rs,stmt);
		}
		return map;
	}

	 
}