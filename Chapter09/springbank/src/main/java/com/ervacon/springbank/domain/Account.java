package com.ervacon.springbank.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class Account implements Serializable {
	
	private AccountNumber number;
	private BigDecimal balance = new BigDecimal(0);

	public AccountNumber getNumber() {
		return number;
	}
	
	protected void setNumber(AccountNumber number) {
		this.number = number;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	protected void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public boolean empty() {
		return balance.compareTo(new BigDecimal(0)) == 0;
	}

	public abstract void debit(Transaction transaction);
	
	public abstract void credit(Transaction transaction);
	
	public abstract AccountHolder getHolder();
}
