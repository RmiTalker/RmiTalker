package com.rmi.server.imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.rmi.client.inter.RMIClientInter;
import com.rmi.domain.User;
import com.rmi.server.dao.SqlHelper;
import com.rmi.server.inter.RMIServerInter;
import com.rmi.server.model.UserService;
import com.rmi.server.view.RmiServerFrame;

public class RMIServerImp extends UnicastRemoteObject implements RMIServerInter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String,RMIClientInter> clientList= new HashMap<>();
	private HashMap<String,ArrayList<String>> friends= new HashMap<>();
	
	private RmiServerFrame frame = null;

	public RMIServerImp() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkUsernumber(String userNumber) throws RemoteException {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		
		return userService.checkUserId(userNumber);
	}
	
	public User[] getAllFriends(String uid) throws RemoteException {
		// TODO Auto-generated method stub
		User[] users = new User[0];
		ArrayList<User> al = new ArrayList<>();
		
		String sql = "select u.uid,name,realname,sex from user u,relation r where u.uid=r.fid and u.status=1 and r.uid=?";
		String[] parameters={uid};
		ResultSet rs=SqlHelper.executeQuery(sql, parameters);
		try {
			while(rs.next()){
				User user = new User();
				user.setUserId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setRealname(rs.getString(3));
				user.setSex(rs.getInt(4));
				if (clientList.containsKey(user.getUserId()))
					user.setOnline(true);
				al.add(user);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		if (!al.isEmpty()) {
			int size = al.size();
			users = new User[size];
			for (int i = 0; i < size; i++) {
				users[i] = (User) al.get(i);
			}
		}
		return users;
	}

	@Override
	public boolean checkUser(User user, RMIClientInter client)
			throws RemoteException {
		// TODO Auto-generated method stub
		boolean result = false;
		UserService userService = new UserService();
		if(userService.checkUser(user)){
			String uid = user.getUserId().toString();
			//whether the user has logined
			
			if(clientList.containsKey(uid)){
				return false;
			}
			clientList.put(uid, client);
			friends.put(uid, userService.getAllFriendsID(uid));
			
			//tell the user friends the user has logined
			Iterator<String> it = clientList.keySet().iterator();
			while(it.hasNext()){
				String elem = (String) it.next();
				if(((ArrayList<String>) friends.get(elem)).contains(uid)){
					((RMIClientInter)clientList.get(elem)).sendFriendlist(getAllFriends(elem));
				}
			}
			User u = userService.getUserById(uid);
			frame.addOnlineUser(u);
			
		}
		
		return result;
	}

	@Override
	public User register(User u) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean logout(String uid) throws RemoteException {
		// TODO Auto-generated method stub
		if (uid != null && uid != "") {
			clientList.remove(uid);
			friends.remove(uid);
			Iterator<String> it = clientList.keySet().iterator();
			while(it.hasNext()){
				String elem = (String) it.next();
				if(((ArrayList<String>) friends.get(elem)).contains(uid)){
					((RMIClientInter)clientList.get(elem)).sendFriendlist(getAllFriends(elem));
				}
			}
			frame.removeOnlineUser(uid);
			return true;
		}
		
		return false;
	}

	@Override
	public User getUserById(String uid) throws RemoteException {
		// TODO Auto-generated method stub
		User user = null;
		UserService userService = new UserService();
		user = userService.getUserById(uid);
		return user;
	}

}
