package com.rmi.client.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import com.rmi.domain.User;
import com.rmi.server.inter.RMIServerInter;

import java.awt.SystemColor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.rmi.RemoteException;

import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;


public class Conversation extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel buttonPane;

	private JTextArea txtMsg;

	//private JButton btnOK;

	private JButton btnQuit;

	private String from;

	private String to;

	private String msg;

	private RMIServerInter server;

	private boolean send = false;
	
	private JPanel panel;
	private JButton btnSend;
	private JTextArea textArea_Input;
	private JPanel panel_1;
	private JPanel panel_2;

	public Conversation(String from, String to, String msg, RMIServerInter server) {
		this.from = from;
		this.to = to;
		this.server = server;
		this.msg = msg;

		if (msg == null)
			send = true;
		//Font font = new Font("��", Font.PLAIN, 12);
		//Color fcolor=new Color(13, 55, 85);
		//Color c1=new Color(241, 250, 255);
		Container container = getContentPane();
		//Color bgc = new Color(119, 202, 250);
		//container.setBackground(SystemColor.control);
		if(RmiClientLogin.icon!=null){
			setIconImage(RmiClientLogin.icon);
		}

		buttonPane = new JPanel();
		buttonPane.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		buttonPane.setBackground(SystemColor.control);
		getContentPane().setLayout(new BorderLayout(0, 0));
				
				panel_1 = new JPanel();
				panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
				getContentPane().add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BorderLayout(0, 0));
		
				txtMsg = new JTextArea(0, 0);
				panel_1.add(txtMsg);
				txtMsg.setEditable(false);
		container.add(buttonPane, BorderLayout.SOUTH);
		
		/*if (send) {
			btnOK = new JButton("Send");
		} else {
			btnOK = new JButton("Response");
		}*/
		//btnOK.setBounds(200, 15, 70, 25);
		//btnOK.setFont(font);
		//btnOK.setForeground(fcolor);
		//btnOK.setBackground(c1);
		//btnOK.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		//btnOK.addActionListener(this);
		//buttonPane.add(btnOK);
		buttonPane.setLayout(new BorderLayout(0, 0));
		
		panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		buttonPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		textArea_Input = new JTextArea();
		panel_2.add(textArea_Input);
		textArea_Input.setRows(2);
		textArea_Input.setLineWrap(true);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPane.add(panel, BorderLayout.SOUTH);
		btnQuit = new JButton("Close");
		panel.add(btnQuit);
		
		btnSend = new JButton("Send");
		panel.add(btnSend);
		btnSend.addActionListener(this);
		btnQuit.addActionListener(this);
		
		
		
		//this.setTitle("You ("+from+") are talking with " + to);
		if (!send) {
			txtMsg.setEditable(false);
			try {
				User u = this.server.getUserById(to);
				User u2 = this.server.getUserById(from);
				this.setTitle("You ("+u.getName()+") receive a message from" + u2.getName());
				//this.txtMsg.setText(this.msg);
				this.txtMsg.append(this.msg);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			try {
				User u = this.server.getUserById(to);
				User u2 = this.server.getUserById(from);
				this.setTitle("You ("+u2.getName()+") are talking with " + u.getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		
		this.setSize(410, 300);
		//textArea_Input.requestFocus();
		this.getRootPane().setDefaultButton(btnSend);
		setCenter();
	}
	private void setCenter() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = getSize();
		setLocation((scrSize.width - thisSize.width) / 2,
				(scrSize.height - thisSize.height) / 2);
	}
	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		Conversation c = new Conversation(null, null, null, null);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setVisible(true);
		c.textArea_Input.requestFocus();
	}*/

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.equals(btnQuit)) {
			this.dispose();
		} else if (btn.equals(btnSend)) {
			String txt = btn.getText();
			if (txt.equals("Send")) {
				String msg = this.textArea_Input.getText();
				if (msg.trim() == "") {
					JOptionPane.showMessageDialog(this, "Can't send an empty message.");
					return;
				} else {
					try {
						//this.server.sendMessage(from, to, msg);
						
						if (send) {
							this.server.sendMessage(from, to, msg);
							User u = this.server.getUserById(from);
							String message = u.getName() + ":\n"+msg+"\n\r";
							this.txtMsg.append(message);
							this.textArea_Input.setText("");
						} else {
							server.sendMessage(to, from, msg);
							User u = this.server.getUserById(to);
							String message = u.getName() + ":\n"+msg+"\n\r";
							this.txtMsg.append(message);
							this.textArea_Input.setText("");
						}
						//this.dispose();

					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(this, "The server is currently unavailable.");
						this.dispose();
					}
				}
			} /*else if (txt.equals("Response")) {
				this.txtMsg.setEditable(true);
				this.btnOK.setText("Send");
				this.setTitle("You ("+to+") are talking with" + from);
				this.txtMsg.setText("");
			}*/
		}
	}

}
