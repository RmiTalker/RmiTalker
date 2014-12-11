package com.rmi.client.inter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.rmi.domain.User;

public interface RMIClientInter extends Remote {

	// receive friends list
	public boolean sendFriendlist(User[] users) throws RemoteException;

	// ���ܺ�����Ϣ
	public boolean sendMessage(String id, String message)
			throws RemoteException;

	// ���ܷ������ĶϿ���Ϣ
	public void serverShutup() throws RemoteException;

	// �����ظ���¼
	public void IDReLogin() throws RemoteException;

	// ��Ӻ��ѵ��б���
	public void addUser(User user) throws RemoteException;

}
