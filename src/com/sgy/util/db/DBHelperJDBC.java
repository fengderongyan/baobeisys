package com.sgy.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

public class DBHelperJDBC {
	public static Connection getConnection() throws ClassNotFoundException,
			SQLException {
		// Change these settings according to your local configuration
		String driver = "oracle.jdbc.driver.OracleDriver";
		String connectString = "jdbc:oracle:thin:@10.37.154.20:1521:orcl";
		String user = "wxdkh";
		String password = "wxdkh";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connectString, user,
				password);
		return conn;
	}

	public Hashtable getValue(String sql) {
		Hashtable hstab = new Hashtable();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int num = rsmd.getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= num; i++) {
					String key = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null)
						value = "";
					hstab.put(key, value);
				}
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
				e.printStackTrace();
			}

		}

		return hstab;
	}
	public ArrayList getList(String sqlString) {

		ArrayList pkv = new ArrayList();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlString);
			ResultSetMetaData rsmd = rs.getMetaData();
			int num = rsmd.getColumnCount();
			while (rs.next()) {
				Hashtable table = new Hashtable();
				for (int i = 1; i <= num; i++) {
					String key = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null)
						value = "";
					table.put(key, value);
				}
				pkv.add(table);
			}

		} catch (Exception e) {
			System.out.println(e.toString() + "----------------------");
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pkv;
	}

}
