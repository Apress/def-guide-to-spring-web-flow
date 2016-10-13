package com.ervacon.springbank.user;

import java.io.Serializable;

public class User implements Serializable {
	
	private String userName;
	private String password;
	private long clientId;
	
	public User(String userName, String password, long clientId) {
		this.userName = userName;
		this.password = password;
		this.clientId = clientId;
	}

	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = encrypt(password);
	}
	
	public boolean checkPassword(String password) {
		return this.password.equals(encrypt(password));
	}
	
	public long getClientId() {
		return clientId;
	}
	
	// internal helpers
	
	private String encrypt(String password) {
		// a real app would encrypt the password
		return password;
	}
}
