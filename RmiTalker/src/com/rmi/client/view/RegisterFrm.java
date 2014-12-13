package com.rmi.client.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.rmi.domain.User;
import com.rmi.server.inter.RMIServerInter;
import java.awt.SystemColor;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;


public class RegisterFrm extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField txtid;
	JTextField txtname;
	JTextField txtrealname;
	JComboBox cmbSex;
	JPasswordField txtpwd;
	JPasswordField txtrepwd;
	RMIServerInter server;
	public RegisterFrm(RMIServerInter server){
		setTitle("Registration");
		if(RmiClientLogin.icon!=null){
			setIconImage(RmiClientLogin.icon);
		}
		//Font font = new Font("кн", Font.PLAIN, 12);
		//Color fcolor=new Color(13, 55, 85);
		Container container=getContentPane();
		container.setLayout(null);
		this.server=server;
		JLabel lblid=new JLabel("User Number");
		//lblid.setForeground(fcolor);
		//lblid.setFont(font);
		lblid.setBounds(49,39,66,25);
		JLabel lblname=new JLabel("Your Name");
		//lblname.setForeground(fcolor);
		//lblname.setFont(font);
		lblname.setBounds(49,74,60,25);
		JLabel lblrealname=new JLabel("Realname");
		lblrealname.setBounds(49,180,50,25);
		//lblrealname.setForeground(fcolor);
		//lblrealname.setFont(font);
		JLabel lblsex=new JLabel("Gender");
		//lblsex.setForeground(fcolor);
		//lblsex.setFont(font);
		lblsex.setBounds(49,215,36,25);
		JLabel lblpwd=new JLabel("Password");
		//lblpwd.setForeground(fcolor);
		//lblpwd.setFont(font);
		lblpwd.setBounds(49,110,50,25);
		JLabel lblrepwd=new JLabel("Re-enter");
		//lblrepwd.setForeground(fcolor);
		//lblrepwd.setFont(font);
		lblrepwd.setBounds(49,145,50,25);
		
		txtid=new JTextField();
		txtid.setEnabled(false);
		txtid.setEditable(false);
		txtid.setText("System Generation");
		txtid.setBackground(Color.white);
		txtid.setBounds(125,39,145,25);
		txtname=new JTextField();
		txtname.setBounds(125,74,145,25);
		txtrealname=new JTextField();
		txtrealname.setBounds(125,179,145,25);
		cmbSex=new JComboBox();
		cmbSex.addItem("M");
		cmbSex.addItem("F");
		//cmbSex.setBackground(Color.white);
		cmbSex.setBounds(125,214,50,25);
		txtpwd=new JPasswordField();
		txtpwd.setBounds(125,109,145,25);
		txtrepwd=new JPasswordField();
		txtrepwd.setBounds(125,144,145,25);
		
		JPanel p1=new JPanel(null);
		p1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		p1.setBounds(31,23,299,258);
		//Color c1=new Color(241, 250, 255);
		//p1.setBackground(SystemColor.controlHighlight);
		
		p1.add(lblid);
		p1.add(lblname);
		p1.add(lblpwd);
		p1.add(lblrealname);
		p1.add(lblrepwd);
		p1.add(lblsex);
		p1.add(txtid);
		p1.add(txtname);
		p1.add(txtrealname);	
		p1.add(txtpwd);	
		p1.add(txtrepwd);
		p1.add(cmbSex);
		container.add(p1);
		this.setSize(364,357);
		JButton btnOK=new JButton("Register");
		//btnOK.setForeground(fcolor);
		//btnOK.setFont(font);
		JButton btnQuit=new JButton("Close");
		//btnQuit.setForeground(fcolor);
		//btnQuit.setFont(font);
		//btnOK.setBackground(c1);
		//btnQuit.setBackground(c1);
		btnOK.setBounds(82,291,90,25);
		btnQuit.setBounds(195,291,90,25);
		btnOK.addActionListener(this);
		btnQuit.addActionListener(this);
		container.add(btnOK);
		container.add(btnQuit);
		container.setBackground(SystemColor.control);
		setResizable(false);
		setCenter();
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd=e.getActionCommand();
		if(cmd.equals("Close")){
			this.dispose();
		}else if(cmd.equals("Register")){
			String name=txtname.getText();
			String realname=txtrealname.getText();
			String sex=(String)cmbSex.getSelectedItem();
			String pwd=new String(txtpwd.getPassword());
			String repwd=new String(txtrepwd.getPassword());
			//System.out.println(name.trim().length());
			if(name.trim().equals("")||realname.trim().equals("")||pwd.trim().equals("")||
					repwd.trim().equals("")){
				JOptionPane.showMessageDialog(this, "NOT NULL!");
				return;
			}
			if(pwd.length()<6){
				JOptionPane.showMessageDialog(this, "Enter a password with at least 6 characters.");
				return;
			}
			if(!pwd.equals(repwd)){
				JOptionPane.showMessageDialog(this, "Sorry, the password and confirming password disagree!");
				return;
			}
			User user=new User();
			user.setName(name);
			user.setRealname(realname);
			user.setPassword(pwd);
			if(sex.equals("M")){
				user.setSex(1);
			}else{
				user.setSex(0);
			}
			
			try {
				user=server.register(user);
				JOptionPane.showMessageDialog(this,"Congratulations, Registration successful! Your RmiTalker number is: "+user.getUserId()+", password: "+
						user.getPassword()+". Remember, always keep your password confidential.","Success",JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
				return;
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed: Registeration! Please try again later.");
				this.dispose();
				return;
			}
			
		}
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
		new RegisterFrm(null).setVisible(true);
	}*/

}
