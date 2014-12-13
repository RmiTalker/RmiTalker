package com.rmi.server.imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



import com.rmi.client.inter.RMIClientInter;
import com.rmi.domain.Message;
import com.rmi.domain.User;
import com.rmi.server.dao.SqlHelper;
import com.rmi.server.inter.RMIServerInter;
import com.rmi.server.model.UserService;
import com.rmi.server.view.RmiServerFrame;

public class RMIServerImp extends UnicastRemoteObject implements RMIServerInter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<String, RMIClientInter> clientList = new HashMap<>();
	private HashMap<String, ArrayList<String>> friends = new HashMap<>();

	private RmiServerFrame frame = null;

	public RMIServerImp() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setFrame(RmiServerFrame frame) {
		this.frame = frame;
	}

	public RmiServerFrame getFrame() {
		return this.frame;
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

		String sql = "select u.uid,name,realname,sex from users u,relation r where u.uid=r.fid and u.status=1 and r.uid=?";
		String[] parameters = { uid };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		
		try {
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt(1));
				user.setName(rs.getString(2));
				user.setRealname(rs.getString(3));
				user.setSex(rs.getInt(4));
				if (clientList.containsKey(user.getUserId().toString())){
					//System.out.println(user.getUserId());
					user.setOnline(true);
				}else{
					user.setOnline(false);
				}
					
				al.add(user);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}
		if (!al.isEmpty()) {
			int size = al.size();
			//System.out.println(size);
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
		if (userService.checkUser(user)) {
			String uid = user.getUserId().toString();
			// whether the user has logined

			if (clientList.containsKey(uid)) {
				return false;
			}
			result = true;
			clientList.put(uid, client);
			friends.put(uid, userService.getAllFriendsID(uid));

			// tell the user friends the user has logined
			Iterator<String> it = clientList.keySet().iterator();
			while (it.hasNext()) {
				String elem = (String) it.next();
				if (((ArrayList<String>) friends.get(elem)).contains(uid)) {
					((RMIClientInter) clientList.get(elem))
							.sendFriendlist(getAllFriends(elem));
				}
			}
			//update the server active list
			User u = userService.getUserById(uid);
			frame.addOnlineUser(u);
			

		}

		return result;
	}

	@Override
	public User register(User u) throws RemoteException {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		String uid = userService.addUser(u);
		u.setUserId(Integer.parseInt(uid));
		frame.updateUsers(userService.getAllUser());
		return u;
	}

	@Override
	public boolean logout(String uid) throws RemoteException {
		// TODO Auto-generated method stub
		if (uid != null && uid != "") {
			clientList.remove(uid);
			friends.remove(uid);
			Iterator<String> it = clientList.keySet().iterator();
			while (it.hasNext()) {
				String elem = (String) it.next();
				if (((ArrayList<String>) friends.get(elem)).contains(uid)) {
					((RMIClientInter) clientList.get(elem))
							.sendFriendlist(getAllFriends(elem));
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

	@Override
	public boolean addFriend(String uid, String fid) throws RemoteException {
		// TODO Auto-generated method stub
		boolean result = false;
		UserService userService = new UserService();
		String sql = "select count(*) from users where uid=? or uid=?";
		String[] parameters = { uid, fid };
		ResultSet rs = SqlHelper.executeQuery(sql, parameters);
		try {
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 2) {
					//if (userService.deleteFriend(uid, fid)) {
						if (userService.addFriend(uid, fid)) {
							
							RMIClientInter client = (RMIClientInter) clientList.get(uid);
							
							if (client != null) {
								User user = userService.getUserById(fid);
								user.setOnline(clientList.get(fid) != null);
								client.addUser(user);
								friends.put(uid,userService.getAllFriendsID(uid));
							}
							RMIClientInter client2 = null;
							client2 = (RMIClientInter) clientList.get(fid);
							if (client != null) {
								User user = userService.getUserById(uid);
								user.setOnline(clientList.get(uid) != null);
								client2.addUser(user);
								friends.put(fid,userService.getAllFriendsID(fid));
							}
							result = true;
						//} else {
							//System.err.println("Failed: UserService.addFriend(uid, fid)!");
						//}
					} else {
						System.err.println("Failed: UserService.deleteFriend(uid, fid)!");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			SqlHelper.close(rs, SqlHelper.getPs(), SqlHelper.getct());
		}

		return result;
	}

	@Override
	public boolean sendMessage(String fromID, String toID, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
		if (fromID != null && fromID.trim() != "" && toID != null
				&& toID.trim() != "" && message != null && message.trim() != "") {
			UserService userService = new UserService();
			//the user is online
			if(clientList.containsKey(toID)){
				RMIClientInter client = clientList.get(toID);
				client.sendMessage(fromID, message);
				//save message into database
				//userService.saveMessage(fromID, toID, message, "1");
			}else{
				//the user is offline
				userService.saveMessage(fromID, toID, message, "0");
			}
			return true;

		}
		return false;
	}

	@Override
	public void getOfflineMsg(String uid) throws RemoteException {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		
		ArrayList<Message> al = userService.getAllUnReadMsg(uid);
		
		for (int i = 0; i < al.size(); i++) {
			Message m = (Message) al.get(i);
			sendMessage(m.getSender().toString(), m.getGetter().toString(), m.getMessage());
		}

	}

	@Override
	public User[] getAllUsers() throws RemoteException {
		// TODO Auto-generated method stub
		User[] users = null;
		ArrayList<User> al = new ArrayList<>();
		UserService userService = new UserService();
		al = userService.getAllUser();
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
	public boolean isFriend(String youId, String fid) throws RemoteException {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		
		return userService.isFriend(youId, fid);
	}

}
