package com.rmi.client.inter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.rmi.domain.User;

public interface RMIClientInter extends Remote {

	// receive friends list
	public boolean sendFriendlist(User[] users) throws RemoteException;

	// receive message
	public boolean sendMessage(String uid, String message)
			throws RemoteException;

	// receive the server shut down
	public void serverShutup() throws RemoteException;

	// the user number re-login
	public void IDReLogin() throws RemoteException;

	// add friend
	public void addUser(User user) throws RemoteException;

}
