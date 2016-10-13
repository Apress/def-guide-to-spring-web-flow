package com.ervacon.springbank.domain;

import java.math.BigDecimal;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PaymentValidator implements Validator {

	public boolean supports(Class clazz) {
		return Payment.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		Payment payment = (Payment)obj;
		if (payment.getAmount() == null || payment.getAmount().compareTo(new BigDecimal(0)) < 0) {
			errors.rejectValue("amount", "error.invalidPaymentAmount", "The payment amount is invalid");
		}
		ValidationUtils.rejectIfEmpty(errors, "debitAccount.number", "error.debitAccountRequired",
				"The debit account is required");
		ValidationUtils.rejectIfEmpty(errors, "creditAccount.number", "error.creditAccountRequired",
				"The credit account is required");
	}
}
