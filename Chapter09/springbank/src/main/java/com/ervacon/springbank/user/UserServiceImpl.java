package com.ervacon.springbank.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ervacon.springbank.domain.ClientRepository;

public class UserServiceImpl implements UserService, AuthenticationProvider {
	
	private Map<String, User> users = new HashMap<String, User>();
	
	private ClientRepository clientRepository;
	
	public UserServiceImpl(ClientRepository clientRepository) {
		Assert.notNull(clientRepository, "A client repository is required");
		this.clientRepository = clientRepository;
	}
	
	public User registerUser(String userName, String password, long clientId)
			throws UserServiceException {
		if (!StringUtils.hasText(userName)) {
			throw new UserServiceException("Invalid user name");
		}
		if (users.containsKey(userName)) {
			throw new UserServiceException("User name '" + userName + "' is already in use");
		}
		if (clientRepository.getClient(clientId) == null) {
			throw new UserServiceException("Identified client does not exist");
		}
		User newUser = new User(userName, password, clientId);
		users.put(userName, newUser);
		return newUser;
	}
	
	public User login(String userName, String password) throws UserServiceException {
		User user = users.get(userName);
		if (user == null) {
			throw new UserServiceException("Unknown user '" + userName + "'");
		}
		if (user.checkPassword(password)) {
			return user;
		}
		else {
			throw new UserServiceException("Incorrect password");
		}
	}
	
	// implementing AuthenticationProvider
	
	public boolean supports(Class clazz) {
		return true;
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			User user = login((String)authentication.getPrincipal(), (String)authentication.getCredentials());
			return new UserWrappingAuthenticationToken(user);
		}
		catch (UserServiceException e) {
			throw new BadCredentialsException(e.getMessage(), e);
		}
	}
}
