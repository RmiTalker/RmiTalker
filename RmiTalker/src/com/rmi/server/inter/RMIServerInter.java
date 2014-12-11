package com.rmi.server.inter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.rmi.client.inter.RMIClientInter;
import com.rmi.domain.User;

public interface RMIServerInter extends Remote{
	
	//check user number
	public boolean checkUsernumber(String userNumber) throws RemoteException;
	//authenticate user 
	public boolean checkUser(User user,RMIClientInter client) throws RemoteException;
	//User Registration
	public User register(User u)throws RemoteException;
	//get all friends info
	public User[] getAllFriends(String uid)throws RemoteException;
	//the user logout
	public boolean logout(String uid)throws RemoteException;
	//get user by uid
	public User getUserById(String uid)throws RemoteException;
	
	
	

}
