package com.ervacon.springbank.domain;

import java.io.Serializable;

import org.springframework.util.Assert;

public class AccountNumber implements Serializable {
	
	private String number;
	
	public AccountNumber(String number) {
		Assert.hasText(number, "The account number cannot be blank");
		this.number = number;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass().equals(this.getClass())) {
			AccountNumber other = (AccountNumber)obj;
			return this.number.equals(other.number);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return number.hashCode();
	}

	@Override
	public String toString() {
		return number;
	}
}
