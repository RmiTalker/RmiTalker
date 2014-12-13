package com.rmi.server.tools;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.rmi.server.dao.SqlHelper;


public class uidGenerator {
	
	private static String uid;

	static {
		String sql="select count(*) from users";
		ResultSet rs=SqlHelper.executeQuery(sql, null);
		int count=0;
		try {
			rs.next();
			count=rs.getInt(1);
			uid = String.valueOf(10000+count - 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
	}

	public static synchronized String getNewUID() {
		uid = String.valueOf(Long.parseLong(uid) + 1);
		return uid;
	}

	public static void main(String[] args) {
		System.out.println(getNewUID());
		System.out.println(getNewUID());
	}

}
