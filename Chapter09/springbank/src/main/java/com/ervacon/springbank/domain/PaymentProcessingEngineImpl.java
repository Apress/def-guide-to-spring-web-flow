package com.ervacon.springbank.domain;

import java.math.BigDecimal;

import org.springframework.util.Assert;

public class PaymentProcessingEngineImpl implements PaymentProcessingEngine {

	private AccountRepository accountRepository;
	
	public PaymentProcessingEngineImpl(AccountRepository accountRepository) {
		Assert.notNull(accountRepository, "An account repository is required");
		this.accountRepository = accountRepository;
	}
	
	public void submit(Payment payment) throws PaymentProcessingException {
		Account debitAccount = accountRepository.getAccount(payment.getDebitAccount().getNumber());
		Account creditAccount = accountRepository.getAccount(payment.getCreditAccount().getNumber());
		enforcePaymentPolicy(debitAccount, creditAccount, payment.getAmount());
		new Transfer(payment.getAmount(), payment.getMessage(), debitAccount, creditAccount).execute();
	}

	private void enforcePaymentPolicy(Account debitAccount, Account creditAccount, BigDecimal amount)
			throws PaymentProcessingException {
		// in a real system this would probably delegate to strategy
		if (debitAccount.equals(creditAccount)) {
			throw new PaymentProcessingException(
					"error.invalidAccounts",
					"The debit and credit account of a payment cannot be the same");
		}
		if (debitAccount.getBalance().compareTo(amount) < 0) {
			throw new PaymentProcessingException(
					"error.insufficientBalance",
					"The balance of the debit account is insufficient");
		}
	}
}
