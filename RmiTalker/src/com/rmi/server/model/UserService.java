package com.rmi.server.model;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.rmi.domain.User;
import com.rmi.server.dao.SqlHelper;


public class UserService {
	
	public boolean checkUser(User user){
		boolean flag = false;
		String sql="select * from users where uid=? and password=?";
		String[] parameters={user.getUserId()+"",user.getPassword()};
		ResultSet rs=SqlHelper.executeQuery(sql, parameters);
		try {
			if (rs.next()) {
				flag=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		return flag;
	}
	
	public boolean addUser(User user){
		boolean b=true;
		String sql="insert into users(uid,password,name,realname,sex) values(?,?,?,?,?)";
		String parameters[]={user.getName(),user.getPassword(),user.getName(),user.getRealname(),user.getSex()+""};
		try{
			SqlHelper.executeUpdate(sql, parameters);
		}catch(Exception e){
			b=false;
			e.printStackTrace();
		}
		return b;
	}
	
	public User getUserById(String uid){
		User user=new User();
		String sql="select * from users where uid=?";
		String[] parameters={uid};
		ResultSet rs=SqlHelper.executeQuery(sql, parameters);
		try {
			while(rs.next()){
				user.setId(rs.getInt(1));
				user.setUserId(rs.getInt(2));
				user.setPassword(rs.getString(3));
				user.setName(rs.getString(4));
				user.setRealname(rs.getString(5));
				user.setSex(rs.getInt(6));
				user.setStatus(rs.getInt(7));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		return user;
	}
	
	public boolean checkUserId(String uid){
		boolean flag = false;
		String sql="select * from users where uid=?";
		String[] parameters={uid};
		ResultSet rs=SqlHelper.executeQuery(sql, parameters);
		try {
			while(rs.next()){
				flag=true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		return flag;
	}
	
	public ArrayList<User> getAllUser(){
		
		ArrayList<User> al=new ArrayList<User>();
		String sql="select * from users";
		ResultSet rs=SqlHelper.executeQuery(sql, null);
		try {
			while(rs.next()){
				User user=new User();
				
				user.setId(rs.getInt(1));
				user.setUserId(rs.getInt(2));
				user.setPassword(rs.getString(3));
				user.setName(rs.getString(4));
				user.setRealname(rs.getString(5));
				user.setSex(rs.getInt(6));
				user.setStatus(rs.getInt(7));
				user.setTime(rs.getString(8));
				
				al.add(user);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		
		return al;
		
	}
	
	public ArrayList<String> getAllFriendsID(String uid) {
		
		ArrayList<String> al = new ArrayList<>();
		String sql="select u.uid from users u,relation r where u.uid=r.fid and u.status=1 and r.uid=?";
		String[] parameters={uid};
		ResultSet rs=SqlHelper.executeQuery(sql, parameters);
		try {
			while(rs.next()){
				al.add(rs.getInt(1)+"");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		
		return al;
	}

}
