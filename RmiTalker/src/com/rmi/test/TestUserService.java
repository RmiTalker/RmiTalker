package com.rmi.test;

import com.rmi.server.model.UserService;

public class TestUserService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserService userService = new UserService();
		System.out.println(userService.getAllFriendsID("1000").size());

	}

}
