package com.ervacon.springbank.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Entry implements Serializable {
	
	private BigDecimal appliedAmount;
	private Transaction transaction;

	public Entry(BigDecimal appliedAmount, Transaction transaction) {
		this.appliedAmount = appliedAmount;
		this.transaction = transaction;
	}
	
	public BigDecimal getAppliedAmount() {
		return appliedAmount;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
}
