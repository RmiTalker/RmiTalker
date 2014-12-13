package com.rmi.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.rmi.domain.User;
import com.rmi.server.inter.RMIServerInter;

public class UserListPane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel contentPane;

	JList<User> userlist;

	DefaultListModel<User> model;

	String uid;

	RMIServerInter server;

	public UserListPane(String uid, RMIServerInter server) {
		//get user number
		this.uid = uid;
		this.server = server;

		Color bgc = new Color(119, 202, 250);

		contentPane = new JPanel(new BorderLayout());
		userlist = new JList<>();
		userlist.setCellRenderer(new MyCellRenderer());
		userlist.setBackground(Color.white);
		contentPane.add(userlist, BorderLayout.CENTER);

		JPanel setupPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setupPane.setBackground(bgc);
		JButton btnAdd = new JButton();
		btnAdd.setIcon(new ImageIcon("images/search.png"));
		btnAdd.setBorder(BorderFactory.createEmptyBorder());
		setupPane.add(btnAdd);
		contentPane.setBorder(BorderFactory.createLineBorder(bgc, 20));
		contentPane.add(setupPane, BorderLayout.SOUTH);
		this.add(contentPane);

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "test");
				SearchFriendFrm search = new SearchFriendFrm(UserListPane.this,
						UserListPane.this.uid, UserListPane.this.server);
				search.setVisible(true);
			}
		});
		userlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					JList<User> users = (JList<User>) me.getSource();
					int index = users.locationToIndex(me.getPoint());
					DefaultListModel<User> model=(DefaultListModel<User>)users.getModel();
					
					User user = (User) model.getElementAt(index);
					String msg = user.getMessage();
					Conversation conv;
					if (msg != null && msg.length() > 0) {
						conv = new Conversation(user.getUserId().toString(),UserListPane.this.uid,  msg, UserListPane.this.server);
						user.setMessage(null);
//						model.setElementAt(user, index)
					} else {
						conv = new Conversation(UserListPane.this.uid, user.getUserId().toString(), null, UserListPane.this.server);
					}

					conv.setVisible(true);
				}
			}
		});
		try {
			User[] users = server.getAllFriends(uid);
			// userlist.setListData(users);
			model = new DefaultListModel<>();
			for (int i = 0; i < users.length; i++) {
				model.addElement(users[i]);
			}
			userlist.setModel(model);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error(get the friend's list):the server could not be contacted.");
			// e.printStackTrace();
		}
		getViewport().setView(contentPane);
	}

	public void updateUserlist(User[] users) {
		model.removeAllElements();
		for (int i = 0; i < users.length; i++) {
			model.addElement(users[i]);
		}
	}

	public void addUser(User user) {
		model.addElement(user);
	}

	public void receiveMessage(String from, String message) {
		User user = null;
		// System.out.println(from+":"+message);
		for (int i = 0; i < model.size(); i++) {
			user = (User) model.getElementAt(i);
			if (user.getUserId().toString().equals(from)) {
				String s=user.getMessage();
				s=(s==null?"":s);
				s+=from+": "+message+"\n\r";
				user.setMessage(s);
				model.remove(i);
				model.add(i, user);
				break;
			}
		}
		// System.out.println(user.getId()+":"+user.getMessage());
	}

}

class MyCellRenderer extends JLabel implements ListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static ImageIcon boyIcon = null;

	static ImageIcon boygray = null;

	static ImageIcon girlIcon = null;

	static ImageIcon girlgray = null;
	static {
		try {
			boyIcon = new ImageIcon("images/boy1.jpg");			
			girlIcon = new ImageIcon("images/girl1.jpg");
			
			boygray = new ImageIcon("images/boy2.jpg");
			girlgray = new ImageIcon("images/girl2.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.

	public Component getListCellRendererComponent(JList list, Object value, // value
																			// to
																			// display
			int index, // cell index
			boolean isSelected, // is the cell selected
			boolean cellHasFocus) // the list and the cell have the focus
	{
		User user = (User) value;
		Integer sex = user.getSex();
		String name = user.getName();
		String uid = user.getUserId().toString();
		// System.out.println(user.getMessage()+"ok");
		
		boolean online = user.getOnline();
		//System.out.println(online);
		
		setText(name + "(" + uid + ")");
		ImageIcon img = null;
		if (sex==1) {
			if (online) {
				img = boyIcon;
			} else {
				img = boygray;
			}
		} else {
			if (online)
				img = girlIcon;
			else
				img = girlgray;
		}

		setIcon(img);
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		//	setAlignmentY(1);
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		if (user.getMessage() != null && user.getMessage().length() > 0) 
			setForeground(Color.ORANGE);
		setOpaque(true);
		return this;
	}
}
