package com.rmi.domain;

public class Message {

	private Integer id;
	private Integer sender;
	private Integer getter;
	private String message;
	private String date;
	private Integer isread;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSender() {
		return sender;
	}

	public void setSender(Integer sender) {
		this.sender = sender;
	}

	public Integer getGetter() {
		return getter;
	}

	public void setGetter(Integer getter) {
		this.getter = getter;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getIsread() {
		return isread;
	}

	public void setIsread(Integer isread) {
		this.isread = isread;
	}

}
