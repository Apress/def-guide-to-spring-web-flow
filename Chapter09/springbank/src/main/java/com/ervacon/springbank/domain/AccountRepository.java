package com.ervacon.springbank.domain;

import java.util.List;

public interface AccountRepository {
	
	public AccountNumber openAccount(Long clientId);
	public void closeAccount(AccountNumber accountNumber);
	
	public List<Account> getAccounts(Long clientId);
	public Account getAccount(AccountNumber accountNumber);
	public Account getAccount(String accountNumber);
	
	public List<Entry> getAccountEntries(AccountNumber accountNumber);
	
	public List<Account> getBeneficiaries(Long clientId);
	public void addBeneficiary(Long clientId, Account beneficiary);
	public void removeBeneficiary(Long clientId, AccountNumber accountNumber);
}
