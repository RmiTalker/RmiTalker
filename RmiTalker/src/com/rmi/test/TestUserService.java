package com.rmi.test;

import java.util.ArrayList;

import com.rmi.domain.Message;
import com.rmi.server.model.UserService;

public class TestUserService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		//System.out.println(userService.getAllFriendsID("1000").size());
		ArrayList<Message> al = userService.getAllUnReadMsg("10002");
		System.out.println(al.size());

	}

}
