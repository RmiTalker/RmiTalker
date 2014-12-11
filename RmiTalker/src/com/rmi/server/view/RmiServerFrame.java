package com.rmi.server.view;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RowSorter;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

import java.awt.GridLayout;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;

import com.rmi.domain.User;
import com.rmi.server.model.RMISever;
import com.rmi.server.model.UserService;
import com.rmi.server.tools.MyTable;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RmiServerFrame {

	private JFrame frame;
	private JTextField textField_ServerIp;
	private JTextField textField_Port;
	private MyTable table_ative;
	private String columns[] = { "ID", "Name", "Realname", "Gender" };
	private String columns_register[] = { "ID", "Name", "Realname", "Gender",
			"Registration Time" };
	//private Object rows[][] = { { "1", "sam", "wangzhenyu", "M" },
			//{ "2", "judy", "xinfeng", "F" } };
	private JTable table_register;
	private JTabbedPane tabbedPane;
	private JLabel lblTip;
	private JLabel lblServerIp;
	private DefaultTableModel model2;
	private DefaultTableModel model;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RmiServerFrame window = new RmiServerFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RmiServerFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				RMISever rmiServer = new RMISever();
				textField_ServerIp.setText(rmiServer.getHostIp());
				//System.out.println(rmiServer.getHostIp());
			}
		});
		frame.setBounds(100, 100, 650, 500);
		frame.setTitle("RmiTalker Server");
		frame.setIconImage((new ImageIcon("images/server.png").getImage()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel_Up = new JPanel();
		frame.getContentPane().add(panel_Up, BorderLayout.NORTH);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip("Start server...", "ok");
				textField_Port.setEditable(false);
			}
		});
		btnStart.setFocusPainted(false);
		panel_Up.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTip("Server closed", "err");
				textField_Port.setEditable(true);
			}
		});
		btnStop.setFocusPainted(false);
		panel_Up.add(btnStop);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 1) {
					// System.out.println("change");
					model2.setRowCount(0);
					// get user info
					UserService us = new UserService();

					ArrayList<User> al = us.getAllUser();
					if (al != null) {
						// lblTip.setText("The database connection is ok");
						try {
							setTip("The database connection is ok", "ok");
							for (int i = 0; i < al.size(); i++) {
								Object[] o = new Object[5];
								o[0] = al.get(i).getUserId();
								o[1] = al.get(i).getName();
								o[2] = al.get(i).getRealname();
								if (al.get(i).getSex() == 1) {
									o[3] = 'M';
								} else {
									o[3] = 'F';
								}
								o[4] = al.get(i).getTime();
								model2.addRow(o);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							setTip("Failed: Database Connection", "err");
						}
					} else {
						setTip("Failed: Database Connection", "err");
					}
				}
			}
		});
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_active = new JPanel();
		tabbedPane.addTab("Active User", null, panel_active, null);
		panel_active.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane_active = new JScrollPane();
		panel_active.add(scrollPane_active);

		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table_ative = new MyTable(model);
		// sort
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table_ative.setRowSorter(sorter);
		table_ative.getTableHeader().setReorderingAllowed(false);
		table_ative.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_ative.setFillsViewportHeight(true);
		table_ative.getColumnModel().getColumn(0).setPreferredWidth(15);
		table_ative.getColumnModel().getColumn(3).setPreferredWidth(15);
		scrollPane_active.setViewportView(table_ative);

		JPanel panel_register = new JPanel();
		tabbedPane.addTab("Registered User", null, panel_register, null);
		panel_register.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane_register = new JScrollPane();
		panel_register.add(scrollPane_register);

		model2 = new DefaultTableModel();
		model2.setColumnIdentifiers(columns_register);
		table_register = new MyTable(model2);

		// sort
		RowSorter<TableModel> sorter2 = new TableRowSorter<TableModel>(model2);
		table_register.setRowSorter(sorter2);
		table_register.getTableHeader().setReorderingAllowed(false);
		table_register.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_register.setFillsViewportHeight(true);
		table_register.getColumnModel().getColumn(0).setPreferredWidth(15);
		table_register.getColumnModel().getColumn(3).setPreferredWidth(15);
		scrollPane_register.setViewportView(table_register);

		JPanel panel_down = new JPanel();
		frame.getContentPane().add(panel_down, BorderLayout.SOUTH);
		panel_down.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		lblServerIp = new JLabel("Server IP:");
		panel_down.add(lblServerIp);

		textField_ServerIp = new JTextField();
		textField_ServerIp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RMISever rmiServer = new RMISever();
				lblServerIp.setText(rmiServer.getHostIp());
			}
		});
		textField_ServerIp.setEditable(false);
		panel_down.add(textField_ServerIp);
		textField_ServerIp.setColumns(15);

		JLabel lblPort = new JLabel("Port:");
		panel_down.add(lblPort);

		textField_Port = new JTextField();
		textField_Port.setText("9999");
		panel_down.add(textField_Port);
		textField_Port.setColumns(6);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_Port.setEditable(false);
			}
		});
		btnSave.setFocusPainted(false);
		panel_down.add(btnSave);

		lblTip = new JLabel();
		lblTip.setFont(new Font("Segoe UI", Font.BOLD, 12));

		panel_down.add(lblTip);
	}

	public void setTip(String tip, String type) {
		lblTip.setText(tip);
		if (type.equals("ok")) {
			lblTip.setForeground(Color.GREEN);
		} else if (type.equals("err")) {
			lblTip.setForeground(Color.RED);
		}
	}

	public void setServerIP(String ip) {
		textField_ServerIp.setText(ip);
	}

	public String getPort() {
		return textField_Port.getText();
	}
	
	public void addOnlineUser(User user){
		if(user==null) return;
		Object[] o = new Object[4];
		o[0] = user.getUserId();
		o[1] = user.getName();
		o[2] = user.getRealname();
		if (user.getSex() == 1) {
			o[3] = 'M';
		} else {
			o[3] = 'F';
		}
		model.addRow(o);		
	}
	
	public void removeOnlineUser(String uid){
		if(uid==null||uid=="") return;
		
		Vector v=model.getDataVector();
		int size=v.size();
		for(int i=0;i<size;i++){
			if(((Vector)v.elementAt(i)).elementAt(0).toString().equals(uid)){
				v.remove(i);
				break;
			}
		}
		model.fireTableDataChanged();
	}
}
