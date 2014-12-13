package com.rmi.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTime {
	
	public static void main(String[] args) {
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time1=df.format(date);
		System.out.println(time1);
	}

}
