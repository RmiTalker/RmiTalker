package com.rmi.test;

public class TestImages {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String iconfile = ClassLoader.getSystemResource("").toString();
		//iconfile = iconfile.substring(6);
		System.out.println(iconfile);

	}

}
