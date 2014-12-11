package com.rmi.server.dao;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class SqlHelper {
	// Define variables needed.
			public static Connection ct = null;
			// 大多数情况下，我们使用的是PreparedStatement来替代Statement可以防止sql注入
			private static PreparedStatement ps = null;
			private static ResultSet rs = null;
			private static CallableStatement cs = null;

			private static String userName = "";
			private static String psw = "";
			private static String driver = "";
			private static String url = "";
			private static InputStream fis = null;
			static {
				Properties pp = new Properties();
				try {
					fis= SqlHelper.class.getClassLoader().getResourceAsStream("dbinfo.properties");
					pp.load(fis);
					userName=pp.getProperty("username");
					psw = pp.getProperty("password");
					driver = pp.getProperty("driver");
					url = pp.getProperty("url");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			// 加载驱动，只�?���?��
			// 得到连接
			public static Connection getConnection() {
				try {
					Class.forName(driver);
					ct = DriverManager.getConnection(url,
							userName, psw);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("get connection: ok");
				return ct;
			}

			// 调用存储过程，又返回Result
			// sql call过程（？？？�?
			public static CallableStatement callPro2(String sql, String[] inparameters,
					Integer[] outparameters) {
				try {
					ct = getConnection();
					cs = ct.prepareCall(sql);
					if (inparameters != null) {
						for (int i = 0; i < inparameters.length; i++) {
							cs.setObject(i + 1, inparameters[i]);
						}
					}

					// 给out参数赋�?
					if (outparameters != null) {
						for (int i = 0; i < outparameters.length; i++) {
							cs.registerOutParameter(inparameters.length + 1 + i,
									outparameters[i]);
						}
					}
					cs.execute();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {

				}
				return cs;
			}

			// 调用存储过程
			// sql象{sql过程（？？？）}
			public static void callProl(String sql, String[] parameters) {
				try {
					ct = getConnection();
					cs = ct.prepareCall(sql);
					if (parameters != null) {
						for (int i = 0; i < parameters.length; i++) {
							cs.setObject(i + 1, parameters[i]);
						}
					}
					cs.execute();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					close(rs, cs, ct);
				}
			}

			// 统一的select
			// ResultSet->Arraylist
			public static ResultSet executeQuery(String sql, String[] parameters) {
				try {
					ct = getConnection();
					ps = ct.prepareStatement(sql);
					if (parameters != null && !parameters.equals("")) {
						for (int i = 0; i < parameters.length; i++) {
							ps.setString(i + 1, parameters[i]);
						}
						// System.out.println(ps);
					}
					rs = ps.executeQuery();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// close(rs,ps,ct);
				}
				// System.out.println(rs);
				return rs;
			}

			// 如果有多个update/delete/insert[�?��考虑事务]
			public static void executeUpdate2(String sql[], String[][] parameters) {
				try {
					// 核心
					// 1.获得连接
					ct = getConnection();
					// 因为这时，用户传入的可能是多个Sql语句
					ct.setAutoCommit(false);
					for (int i = 0; i < sql.length; i++) {
						if (parameters[i] != null) {
							ps = ct.prepareStatement(sql[i]);
							for (int j = 0; j < parameters[i].length; i++) {
								ps.setString(j + 1, parameters[i][j]);
							}
							ps.executeUpdate();
						}
					}
					ct.commit();
				} catch (Exception e) {
					e.printStackTrace();
					// 回滚
					try {
						ct.rollback();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					throw new RuntimeException(e.getMessage());
				} finally {
					close(rs, ps, ct);
				}
			}

			// 先写�?��update/delete/insert
			// sql格式�?update 表明 set 字段�?�?where 字段=�?
			public static void executeUpdate(String sql, String[] parameters) {
				// 1.创建�?��ps
				try {
					ct = getConnection();
					ps = ct.prepareStatement(sql);
					// 给？赋�?
					if (parameters != null) {
						for (int i = 0; i < parameters.length; i++) {
							ps.setString(i + 1, parameters[i]);
						}
					}
					// 执行
					ps.executeUpdate();

				} catch (Exception e) {
					e.printStackTrace();// �?��阶段
					// 抛出异常，抛出运行异常，可以给调用该函数的函数一个�?�?
					// 可以处理，也可以放弃处理
					throw new RuntimeException(e.getMessage());
				} finally {
					// 关闭资源
					close(rs, ps, ct);
				}
			}

			// 关闭资源的函�?
			public static void close(ResultSet rs, Statement ps, Connection ct) {
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					rs = null;
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ps = null;
				}
				if (ct != null) {
					try {
						ct.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ct = null;
				}
			}

			public static Connection getct() {
				return ct;
			}

			public static PreparedStatement getPs() {
				return ps;
			}

			public static ResultSet getRs() {
				return rs;
			}

			public static CallableStatement getCs() {
				return cs;
			}

}
