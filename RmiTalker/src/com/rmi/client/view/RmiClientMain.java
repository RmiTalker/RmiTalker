package com.rmi.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.rmi.client.imp.RMIClientImp;
import com.rmi.client.inter.RMIClientInter;
import com.rmi.domain.User;
import com.rmi.server.inter.RMIServerInter;

public class RmiClientMain extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	RMIServerInter server;
	RMIClientImp client;
	
	String uid;
	JPanel header;
	UserListPane content;
	JLabel head,title;
	
	public RmiClientMain(String uid,RMIServerInter s, RMIClientInter client){
		this.server = s;
		this.uid = uid;
		if(client instanceof RMIClientImp){
			this.client=(RMIClientImp)client;
		}
		setTitle("RmiTalker-("+uid+")");
		if(RmiClientLogin.icon!=null){
			setIconImage(RmiClientLogin.icon);
		}
		Container container=getContentPane();
		container.setLayout(new BorderLayout());
		
		header=new JPanel(new FlowLayout(FlowLayout.LEFT));
		Color bgc=new Color(119,202,250);
		header.setBackground(bgc);
		String imageurl="images/boy.jpg";
		User user;
		try {
			user = s.getUserById(uid);
			
			if(user.getSex()==1){
				imageurl="images/boy.jpg";
			}else{
				imageurl="images/girl.jpg";
			}
			head=new JLabel(new ImageIcon(imageurl));
			head.setBorder(BorderFactory.createEtchedBorder());
			header.add(head);
			title=new JLabel(user.getName()+"("+user.getUserId()+")");
			header.add(title);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		content=new UserListPane(uid,s);
		this.client.setClient(this);
		container.add(header,BorderLayout.NORTH);
		container.add(content,BorderLayout.CENTER);
		
		this.setSize(300, 600);
		setPosition();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					server.logout(RmiClientMain.this.uid);
				} catch (RemoteException e1) {
				//	e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
	}
	
	private void setPosition() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		setLocation((scrSize.width - thisSize.width),40);
	}
	public void updateUserlist(User[]users){
		content.updateUserlist(users);
	}
	public void addUser(User user){
		content.addUser(user);
	}
	public void quit(String message){
		JOptionPane.showMessageDialog(this, message);
		System.exit(0);
	}
	public void receiveMessage(String from,String message){
//		Conversation converstion=new Conversation(from,id,message,this.server);
//		converstion.setVisible(true);
		content.receiveMessage(from, message);
	}
	public void receiveOfflineMessage(){
		try {
			this.server.getOfflineMsg(uid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public static void main(String[] args) throws HeadlessException {
		RmiClientMain rmiClientMain = new RmiClientMain(uid, s, client);
	}*/

}
