package com.onerivet.model;

public class Users {
	private final String userId;
	private String userName;
	private String userEmail;
		
	private static int counter = 1000;

	public Users(String userName, String userEmail){
	    this.userId = "USR" + (++counter);
	    this.userName = userName;
	    this.userEmail = userEmail;
	}

	
	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
}
