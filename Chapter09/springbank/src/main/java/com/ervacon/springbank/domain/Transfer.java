package com.ervacon.springbank.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Transfer extends Transaction {
	
	private Account debitAccount;
	private Account creditAccount;

	public Transfer(BigDecimal amount, String message, Account debitAccount, Account creditAccount) {
		setDate(new Date());
		setAmount(amount);
		setMessage(message);
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
	}
	
	public Account getDebitAccount() {
		return debitAccount;
	}
	
	public Account getCreditAccount() {
		return creditAccount;
	}
	
	public void execute() {
		debitAccount.debit(this);
		creditAccount.credit(this);
	}
}
