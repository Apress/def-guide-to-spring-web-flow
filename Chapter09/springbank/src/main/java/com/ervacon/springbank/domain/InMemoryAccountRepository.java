package com.ervacon.springbank.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.util.Assert;

public class InMemoryAccountRepository implements AccountRepository {

	private long ACCOUNT_NUMBER_COUNTER = 0L;
	
	private synchronized long nextAccountNumber() {
		return ACCOUNT_NUMBER_COUNTER++;
	}

	private ClientRepository clientRepository;

	private Map<AccountNumber, LocalAccount> localAccounts =
		new HashMap<AccountNumber, LocalAccount>();
	
	private Map<Long, List<Account>> beneficiaries = 
		new HashMap<Long, List<Account>>();
	
	public InMemoryAccountRepository(ClientRepository clientRepository) {
		Assert.notNull(clientRepository, "A client repository is required");
		this.clientRepository = clientRepository;
	}

	public AccountNumber openAccount(Long clientId) {
		Client client = clientRepository.getClient(clientId);
		AccountNumber newAccountNumber = new AccountNumber("SpringBank-" + nextAccountNumber());
		LocalAccount newAccount = new LocalAccount(client, newAccountNumber);
		localAccounts.put(newAccountNumber, newAccount);
		client.addAccountNumber(newAccountNumber);
		return newAccountNumber;
	}
	
	public void closeAccount(AccountNumber accountNumber) {
		LocalAccount account = localAccounts.get(accountNumber);
		Assert.notNull(account, "Only local accounts can be closed");
		Assert.state(account.empty(),
				"This account cannot be closed, it is not empty");
		localAccounts.remove(accountNumber);
		account.getClient().removeAccountNumber(account.getNumber());
	}
	
	public List<Account> getAccounts(Long clientId) {
		Client client = clientRepository.getClient(clientId);
		List<Account> res = new LinkedList<Account>();
		for (AccountNumber number : client.getAccountNumbers()) {
			res.add(getAccount(number));
		}
		return res;
	}

	public Account getAccount(AccountNumber accountNumber) {
		// should have some real business logic here to decide the
		// type of account to return
		if (localAccounts.containsKey(accountNumber)) {
			return localAccounts.get(accountNumber);
		}
		else {
			for (List<Account> accounts : beneficiaries.values()) {
				for (Account account : accounts) {
					if (account.getNumber().equals(accountNumber)) {
						return account;
					}
				}
			}
			return new Beneficiary();
		}
	}
	
	public Account getAccount(String accountNumber) {
		return getAccount(new AccountNumber(accountNumber));
	}
	
	public List<Entry> getAccountEntries(AccountNumber accountNumber) {
		if (localAccounts.containsKey(accountNumber)) {
			return localAccounts.get(accountNumber).getHistory();
		}
		else {
			return Collections.emptyList();
		}
	}
	
	public List<Account> getBeneficiaries(Long clientId) {
		if (beneficiaries.containsKey(clientId)) {
			return Collections.unmodifiableList(beneficiaries.get(clientId));
		}
		else {
			return Collections.emptyList();
		}
	}
	
	public void addBeneficiary(Long clientId, Account beneficiary) {
		List<Account> clientBeneficiaries = beneficiaries.get(clientId);
		if (clientBeneficiaries == null) {
			clientBeneficiaries = new ArrayList<Account>();
			beneficiaries.put(clientId, clientBeneficiaries);
		}
		clientBeneficiaries.add(beneficiary);
	}
		
	public void removeBeneficiary(Long clientId, AccountNumber accountNumber) {
		List<Account> clientBeneficiaries = beneficiaries.get(clientId);
		if (clientBeneficiaries != null) {
			for (ListIterator<Account> it = clientBeneficiaries.listIterator(); it.hasNext(); ) {
				if (it.next().getNumber().equals(accountNumber)) {
					it.remove();
				}
			}
		}
	}
}
