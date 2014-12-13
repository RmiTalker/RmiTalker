package com.rmi.server.inter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.rmi.client.inter.RMIClientInter;
import com.rmi.domain.User;

public interface RMIServerInter extends Remote {
	/**
	 * @author Sam
	 * @description This is RMI server interface to provide server functions to
	 *              client
	 * @throws RemoteException
	 */

	// check user number
	public boolean checkUsernumber(String userNumber) throws RemoteException;

	// authenticate user
	public boolean checkUser(User user, RMIClientInter client)
			throws RemoteException;

	// User Registration: input a user who is not a user number; return a user
	// who has a user number.
	public User register(User u) throws RemoteException;

	// get all friends info
	public User[] getAllFriends(String uid) throws RemoteException;

	// the user logout
	public boolean logout(String uid) throws RemoteException;

	// get user by uid
	public User getUserById(String uid) throws RemoteException;

	// add a friend: input your id and friend's id; return boolean
	public boolean addFriend(String uid, String fid) throws RemoteException;

	// send message
	public boolean sendMessage(String fromID, String toID, String message)
			throws RemoteException;

	// get offline message
	public void getOfflineMsg(String uid) throws RemoteException;
	
	//get all users to add friend
	public User[] getAllUsers()throws RemoteException;

}
