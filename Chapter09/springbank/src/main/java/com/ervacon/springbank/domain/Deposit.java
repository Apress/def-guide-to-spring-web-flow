package com.ervacon.springbank.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Deposit extends Transaction {
	
	private Account creditAccount;

	public Deposit(BigDecimal amount, String message, Account creditAccount) {
		setDate(new Date());
		setAmount(amount);
		setMessage(message);
		this.creditAccount = creditAccount;
	}
	
	public Deposit(double amount, String message, Account creditAccount) {
		this(new BigDecimal(amount), message, creditAccount);
	}
	
	public Account getCreditAccount() {
		return creditAccount;
	}
	
	public void execute() {
		creditAccount.credit(this);
	}
}
