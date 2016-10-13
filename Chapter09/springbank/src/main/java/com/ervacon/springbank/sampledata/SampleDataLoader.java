package com.ervacon.springbank.sampledata;

import com.ervacon.springbank.domain.AccountNumber;
import com.ervacon.springbank.domain.AccountRepository;
import com.ervacon.springbank.domain.Address;
import com.ervacon.springbank.domain.Beneficiary;
import com.ervacon.springbank.domain.Deposit;
import com.ervacon.springbank.domain.Client;
import com.ervacon.springbank.domain.ClientRepository;
import com.ervacon.springbank.user.UserService;

public class SampleDataLoader {
	
	private AccountRepository accountRepository;
	private ClientRepository clientRepository;
	private UserService userService;
	
	public SampleDataLoader(AccountRepository accountRepository,
			ClientRepository clientRepository, UserService userService) {
		this.accountRepository = accountRepository;
		this.clientRepository = clientRepository;
		this.userService = userService;
	}

	public void initSampleData() {
		Client erwin = clientRepository.createClient(
				new Client(1L, "Erwin", "Vervaet", new Address("Schoolstraat", "41", "3360", "Bierbeek")));
		
		AccountNumber acc1 = accountRepository.openAccount(erwin.getClientId());
		new Deposit(10000.0d, "Initial amount", accountRepository.getAccount(acc1)).execute();
		AccountNumber acc2 = accountRepository.openAccount(erwin.getClientId());
		new Deposit(2000.0d, "Initial amount", accountRepository.getAccount(acc2)).execute();
		
		accountRepository.addBeneficiary(erwin.getClientId(), new Beneficiary("Bieke", "ING-123"));
		accountRepository.addBeneficiary(erwin.getClientId(),
				new Beneficiary("Accountant", new Address("Nieuwstraat", "12/4", "1000", "Brussel"), "ABN-339"));
		
		userService.registerUser("erwinv", "foobar", erwin.getClientId());
	}
}
