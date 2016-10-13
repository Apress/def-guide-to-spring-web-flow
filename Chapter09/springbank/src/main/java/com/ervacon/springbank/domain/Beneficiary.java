package com.ervacon.springbank.domain;

public class Beneficiary extends Account {

	AccountHolder holder = new AccountHolder();
	
	public Beneficiary() {
	}
	
	public Beneficiary(String name, String accountNumber) {
		this(name, new Address(), accountNumber);
	}
	
	public Beneficiary(String name, Address address, String accountNumber) {
		holder.setName(name);
		holder.setAddress(address);
		setNumber(new AccountNumber(accountNumber));
	}
	
	public void setNumber(AccountNumber number) {
		super.setNumber(number);
	}
	
	public void debit(Transaction transaction) {
		// communicate with other bank
	}

	public void credit(Transaction transaction) {
		// communicate with other bank
	}

	public AccountHolder getHolder() {
		return holder;
	}

}
