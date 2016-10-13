package com.ervacon.springbank.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Withdrawal extends Transaction {
	
	private Account debitAccount;

	public Withdrawal(BigDecimal amount, String message, Account debitAccount) {
		setDate(new Date());
		setAmount(amount);
		setMessage(message);
		this.debitAccount = debitAccount;
	}
	
	public Account getDebitAccount() {
		return debitAccount;
	}
	
	public void execute() {
		debitAccount.debit(this);
	}

}
