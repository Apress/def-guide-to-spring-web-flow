package com.ervacon.springbank.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Client implements Serializable {
	
	private long clientId;
	private String firstName;
	private String lastName;
	private Address address;
	private Set<AccountNumber> accountNumbers = new HashSet<AccountNumber>();
	
	public Client(String firstName, String lastName, Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public Client(long clientId, String firstName, String lastName, Address address) {
		this.clientId = clientId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public long getClientId() {
		return clientId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getDisplayName() {
		return firstName + " " + lastName;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public Set<AccountNumber> getAccountNumbers() {
		return Collections.unmodifiableSet(accountNumbers);
	}
	
	protected void addAccountNumber(AccountNumber number) {
		accountNumbers.add(number);
	}
	
	protected void removeAccountNumber(AccountNumber number) {
		accountNumbers.remove(number);
	}
}
