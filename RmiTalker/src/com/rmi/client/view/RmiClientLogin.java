package com.rmi.client.view;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RmiClientLogin {

	private JFrame frmRmitalkerLogin;
	private JTextField textField_serverid;
	private JTextField textField;
	private JTextField textField_usernumber;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RmiClientLogin window = new RmiClientLogin();
					window.frmRmitalkerLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RmiClientLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRmitalkerLogin = new JFrame();
		frmRmitalkerLogin.setResizable(false);
		frmRmitalkerLogin.setVisible(true);
		frmRmitalkerLogin.setTitle("RmiTalker Login");
		frmRmitalkerLogin.setIconImage((new ImageIcon("images/rmitalker.jpg")
				.getImage()));
		frmRmitalkerLogin.setBounds(100, 100, 340, 256);
		frmRmitalkerLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRmitalkerLogin.getContentPane().setLayout(new BorderLayout(0, 0));

		JLabel lblBanner = new JLabel(new ImageIcon("images/banner.jpg"));
		frmRmitalkerLogin.getContentPane().add(lblBanner, BorderLayout.NORTH);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmRmitalkerLogin.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_center = new JPanel();
		tabbedPane.addTab("Login", null, panel_center, null);
		panel_center.setLayout(new GridLayout(2, 2));

		JLabel lblUserNumber = new JLabel("User Number", JLabel.CENTER);
		panel_center.add(lblUserNumber);

		textField_usernumber = new JTextField();

		panel_center.add(textField_usernumber);
		textField_usernumber.setColumns(15);
		textField_usernumber.setBounds(100, 20, 150, 20);
		textField_usernumber.requestFocus();

		JLabel lblPassword = new JLabel("Password", JLabel.CENTER);
		panel_center.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setColumns(15);
		panel_center.add(passwordField);

		JPanel panel_down = new JPanel();
		frmRmitalkerLogin.getContentPane().add(panel_down, BorderLayout.SOUTH);
		panel_down.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel_down.add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel_down.add(panel_1, BorderLayout.SOUTH);

		JLabel lblServerIp = new JLabel("Server IP");
		panel_1.add(lblServerIp);

		textField_serverid = new JTextField();
		textField_serverid.setText("192.168.199.103");
		panel_1.add(textField_serverid);
		textField_serverid.setColumns(10);

		JLabel lblPort = new JLabel("Port");
		panel_1.add(lblPort);

		textField = new JTextField();
		textField.setText("9999");
		panel_1.add(textField);
		textField.setColumns(4);

		JButton btnRegister = new JButton("Register");
		btnRegister.setFocusPainted(false);
		panel.add(btnRegister);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_serverid.setEditable(false);
				textField.setEditable(false);
			}
		});
		btnLogin.setFocusPainted(false);
		panel.add(btnLogin);
		//btnLogin.requestFocus();

		
	}
	
	public String getUsernumber(){
		return textField_usernumber.getText();
	}
	public String getPassword(){
		return new String(passwordField.getPassword());
	}
	public String getServerIp(){
		return textField_serverid.getText();
	}
	public String getPort(){
		return textField.getText();
	}
}
