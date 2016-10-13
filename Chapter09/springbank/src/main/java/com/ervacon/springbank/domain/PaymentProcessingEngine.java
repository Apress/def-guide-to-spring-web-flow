package com.ervacon.springbank.domain;

public interface PaymentProcessingEngine {
	
	public void submit(Payment payment) throws PaymentProcessingException;

}
