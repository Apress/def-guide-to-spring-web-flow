package com.ervacon.springbank.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public abstract class Transaction implements Serializable {
	
	private Date date;
	private BigDecimal amount;
	private String message;

	public Date getDate() {
		return date;
	}
	
	protected void setDate(Date date) {
		this.date = date;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	protected void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getMessage() {
		return message;
	}
	
	protected void setMessage(String message) {
		this.message = message;
	}
	
	public abstract void execute();
}
