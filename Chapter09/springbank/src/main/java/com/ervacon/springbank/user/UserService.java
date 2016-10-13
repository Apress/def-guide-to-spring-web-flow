package com.ervacon.springbank.user;

public interface UserService {
	
	public User registerUser(String userName, String password, long clientId)
		throws UserServiceException;
	
	public User login(String userName, String password) throws UserServiceException;

}
