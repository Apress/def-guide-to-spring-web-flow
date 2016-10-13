package com.ervacon.springbank.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Payment implements Serializable {
	
	private Account debitAccount = new Beneficiary();
	private Account creditAccount = new Beneficiary();
	private BigDecimal amount;
	private String message = "";
	
	public Account getDebitAccount() {
		return debitAccount;
	}
	
	public void setDebitAccount(Account debitAccount) {
		this.debitAccount = debitAccount;
	}
	
	public Account getCreditAccount() {
		return creditAccount;
	}
	
	public void setCreditAccount(Account creditAccount) {
		this.creditAccount = creditAccount;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
