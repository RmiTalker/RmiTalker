package com.rmi.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.rmi.domain.User;
import com.rmi.server.inter.RMIServerInter;

public class SearchFriendFrm extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTable users;
	DefaultTableModel tm;
	JTextField txtID;
	String[]colnames={"ID","Name","Gender"};
	RMIServerInter server;
	String uid;
	UserListPane scrollPane;
	
	public SearchFriendFrm(UserListPane p1,String uid,RMIServerInter server){
		setTitle("Add Friend");
		if(RmiClientLogin.icon!=null){
			setIconImage(RmiClientLogin.icon);
		}
		Font font = new Font("кн", Font.PLAIN, 12);
		Color fcolor=new Color(13, 55, 85);
		Color c1=new Color(241, 250, 255);
		this.server=server;
		this.uid=uid;
		this.scrollPane=p1;
		Container container=getContentPane();
		container.setLayout(null);
		JPanel pane1=new JPanel(new BorderLayout());
		tm=new DefaultTableModel();
		tm.setColumnIdentifiers(colnames);
		users=new JTable(tm);
		users.setBackground(c1);
		pane1.add(new JScrollPane(users));
		pane1.setBounds(10,10,350,200);
		container.add(pane1);
		setSize(380,300);
		setResizable(false);
		txtID=new JTextField();
		txtID.setBounds(12,225,90,25);
		JButton btnQuit=new JButton("Close");
		btnQuit.setFont(font);
		btnQuit.setForeground(fcolor);
		btnQuit.setBackground(c1);
		JButton btnSearch=new JButton("Find");
		btnSearch.setFont(font);
		btnSearch.setForeground(fcolor);
		btnSearch.setBackground(c1);
		btnSearch.setBounds(105,225,60,25);
		JButton btnAdd=new JButton("Add");
		btnAdd.setFont(font);
		btnAdd.setBackground(c1);
		btnAdd.setForeground(fcolor);
		btnAdd.setBounds(190,225,80,25);
		btnQuit.setBounds(285,225,80,25);
		container.add(txtID);
		container.add(btnSearch);
		container.add(btnQuit);
		container.add(btnAdd);
		container.setBackground(new Color(126,194,213));
		btnSearch.addActionListener(this);
		btnQuit.addActionListener(this);
		btnAdd.addActionListener(this);
		setCenter();
		if(server!=null){
			try {
				updateUsers(server.getAllUsers());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "The server is unavailable temporarily.");
				e.printStackTrace();
			}
		}
	}
	private void setCenter() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		setLocation((scrSize.width - thisSize.width) / 2,
				(scrSize.height - thisSize.height) / 2);
	}
	public void updateUsers(User[] users) {
		if (users == null)
			return;
		tm.getDataVector().clear();
		tm.fireTableDataChanged();
		for (int i = 0; i < users.length; i++) {
			if(users[i].getUserId().toString().equals(this.uid)) continue;
			
			String[] rowData = { users[i].getUserId().toString(), users[i].getName(),converSex(users[i].getSex()) };
			tm.addRow(rowData);
		}
	}
	
	public String converSex(int s){
		if(s==1){
			return "M";
		}else{
			return "F";
		}
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd=e.getActionCommand();
		if(cmd.equals("Close")){
			this.dispose();
		}else if(cmd.equals("Find")){
			String id=txtID.getText();
			if(id.trim().equals("")){
				JOptionPane.showMessageDialog(this, "Please enter the user number to find.");
				return;
			}
			try{
				Long.parseLong(id);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(this, "Please enter the user number to find.");
				return;
			}
			
			if(id.equals(this.uid)) return;
			User user=null;
			try {
				user = server.getUserById(id);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, "The server is unavailable temporarily.");
				e1.printStackTrace();
			}
			if(user!=null){
				updateUsers(new User[]{user});
			}
		}else if(cmd.equals("Add")){
			int row=users.getSelectedRow();
			if(row<0) return;
			String id=(String)tm.getValueAt(row, 0);
			try {
				if(server.isFriend(this.uid, id)){
					JOptionPane.showMessageDialog(this,"Sorry, "+id+" was already your friend!");
				}else{
					server.addFriend(this.uid, id);
					JOptionPane.showMessageDialog(this, "A new friend "+id+" has been added successfully!");
				}
			//	scrollPane.updateUserlist(server.getAllFriends(this.id));
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/*public static void main(String[]args){
		//new SearchFriendFrm(null).setVisible(true);
	}*/
}
