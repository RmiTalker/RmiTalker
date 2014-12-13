package com.rmi.client.imp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.rmi.client.inter.RMIClientInter;
import com.rmi.client.view.RmiClientMain;
import com.rmi.domain.User;

public class RMIClientImp extends UnicastRemoteObject implements RMIClientInter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	RmiClientMain client = null;

	public RMIClientImp() throws RemoteException {
	}

	public RmiClientMain getClient() {
		return client;
	}

	public void setClient(RmiClientMain client) {
		this.client = client;
	}

	@Override
	public boolean sendFriendlist(User[] users) throws RemoteException {
		// TODO Auto-generated method stub
		if(users!=null){
			client.updateUserlist(users);
			return true;
		}
		return false;
	}

	@Override
	public boolean sendMessage(String uid, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
		client.receiveMessage(uid, message);
		return true;
	}

	@Override
	public void serverShutup() throws RemoteException {
		// TODO Auto-generated method stub
		this.client.quit("The server is currently unavailable (because it is overloaded or down for maintenance). Please try again later.");
	}

	@Override
	public void IDReLogin() throws RemoteException {
		// TODO Auto-generated method stub
		this.client.quit("Your user number has been logged in elsewhere. The client will now exit.");
	}

	@Override
	public void addUser(User user) throws RemoteException {
		// TODO Auto-generated method stub
		if(user!=null){
			client.addUser(user);
		}
	}

}
