package com.rmi.server.model;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.rmi.domain.Message;
import com.rmi.domain.User;
import com.rmi.server.dao.SqlHelper;
import com.rmi.server.tools.uidGenerator;

public class UserService {

	public boolean checkUser(User user) {
		boolean flag = false;
		String sql = "select * from users where uid=? and password=?";
		String[] parameters = { user.getUserId() + "", user.getPassword() };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		try {
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		return flag;
	}

	public String addUser(User user) {
		// generate a uid
		String uid = null;
		uid = uidGenerator.getNewUID();

		// get system date time
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(date);

		// handle with user sex
		String sql = "insert into users(uid,password,name,realname,sex,time) values(?,?,?,?,?,?)";
		String parameters[] = { uid, user.getPassword(), user.getName(),
				user.getRealname(), user.getSex()+"", time };
		try {
			SqlHelper.executeUpdate(sql, parameters);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uid;
	}

	public User getUserById(String uid) {
		User user = new User();
		String sql = "select * from users where uid=?";
		String[] parameters = { uid };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		try {
			while (rs.next()) {
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
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		return user;
	}

	public boolean checkUserId(String uid) {
		boolean flag = false;
		String sql = "select * from users where uid=?";
		String[] parameters = { uid };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		try {
			while (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		return flag;
	}

	public ArrayList<User> getAllUser() {

		ArrayList<User> al = new ArrayList<User>();
		String sql = "select * from users";
		ResultSet rs = SqlHelper.executeQuery(sql, null);
		try {
			while (rs.next()) {
				User user = new User();

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
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}

		return al;

	}

	public ArrayList<String> getAllFriendsID(String uid) {

		ArrayList<String> al = new ArrayList<>();
		String sql = "select u.uid from users u,relation r where u.uid=r.fid and u.status=1 and r.uid=?";
		String[] parameters = { uid };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		try {
			while (rs.next()) {
				al.add(rs.getInt(1) + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}

		return al;
	}

	public boolean deleteFriend(String uid, String fid) {
		boolean flag = false;

		String sql = "delete from relation where uid=? and fid=?";
		String parameters1[] = { uid, fid };
		String parameters2[] = { fid, uid };

		try {
			SqlHelper.executeUpdate(sql, parameters1);
			SqlHelper.executeUpdate(sql, parameters2);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean addFriend(String uid, String fid) {
		boolean flag = false;

		String sql = "insert into relation(uid,fid,relation) values(?,?,1)";
		String parameters1[] = { uid, fid };
		String parameters2[] = { fid, uid };

		try {
			SqlHelper.executeUpdate(sql, parameters1);
			SqlHelper.executeUpdate(sql, parameters2);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean saveMessage(String fromId, String toId, String message,
			String isread) {

		if (fromId != null && fromId.trim() != "" && toId != null
				&& toId.trim() != "" && message != null && message.trim() != ""
				&& isread != null) {

			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendtime = df.format(date);

			String sql = "insert into message(`from`,`to`,message,send_date,isread) values(?,?,?,?,?)";
			String parameters[] = { fromId, toId, message, sendtime, isread };
			try {
				SqlHelper.executeUpdate(sql, parameters);
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return false;

	}
	
	public ArrayList<Message> getAllUnReadMsg(String uid){
		ArrayList<Message> al = new ArrayList<>();
		String sql = "select 'from','to',message,send_date from message where isread=0 and 'to'=? order by send_date";
		String parameters[] = {uid};
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		try {
			while (rs.next()) {
				Message msg = new Message();
				msg.setSender(rs.getInt(1));
				msg.setGetter(rs.getInt(2));
				msg.setMessage(rs.getString(3));
				msg.setDate(rs.getString(4));
				
				al.add(msg);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}

		return al;
	}

}
