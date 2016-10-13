package com.ervacon.springbank.domain;

public class PaymentProcessingException extends Exception {
	
	private String code;
	
	public PaymentProcessingException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
