package com.ervacon.springbank.web;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import com.ervacon.springbank.domain.Account;
import com.ervacon.springbank.domain.AccountNumber;
import com.ervacon.springbank.domain.AccountRepository;

public class PaymentPropertyEditorRegistrar implements PropertyEditorRegistrar {
	
	private AccountRepository accountRepository;
	
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void registerCustomEditors(PropertyEditorRegistry registry) {
		// when setting the debit account on the payment, load it from the repo
		registry.registerCustomEditor(Account.class, "debitAccount", new PropertyEditorSupport() {
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(accountRepository.getAccount(new AccountNumber(text)));
			}
		});
	}
}
