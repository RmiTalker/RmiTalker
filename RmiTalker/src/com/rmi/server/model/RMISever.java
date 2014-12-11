package com.rmi.server.model;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

import com.rmi.server.imp.RMIServerImp;

public class RMISever {
	
	private Remote reg = null;

	
	public void RMISeverStart(int s_port){
		try {
			RMIServerImp rmi = new RMIServerImp();
			reg = LocateRegistry.createRegistry(s_port);
			Naming.rebind("//localhost:"+s_port+"/rmi", rmi);
			
			System.out.println("Bild is OK");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public void CloseRMIServer(){
		System.out.println("releasing port... ");
		try {
			java.rmi.server.UnicastRemoteObject.unexportObject(reg, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
		reg = null;
	}
	
	public String getHostIp(){
		String hostIp = "";
		try {
			hostIp = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hostIp;
	}

}
