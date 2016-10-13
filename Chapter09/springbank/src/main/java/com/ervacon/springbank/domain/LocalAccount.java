package com.ervacon.springbank.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalAccount extends Account {

	private Client client;
	private List<Entry> history = new LinkedList<Entry>();
	
	public LocalAccount(Client client, AccountNumber number) {
		this.client = client;
		setNumber(number);
	}

	public Client getClient() {
		return client;
	}

	public synchronized List<Entry> getHistory() {
		return Collections.unmodifiableList(new CopyOnWriteArrayList<Entry>(history));
	}
	
	public synchronized void debit(Transaction transaction) {
		setBalance(getBalance().subtract(transaction.getAmount()));
		history.add(new Entry(transaction.getAmount().negate(), transaction));
	}
	
	public synchronized void credit(Transaction transaction) {
		setBalance(getBalance().add(transaction.getAmount()));
		history.add(new Entry(transaction.getAmount(), transaction));
	}
	
	public AccountHolder getHolder() {
		// the client is the holder
		AccountHolder holder = new AccountHolder();
		holder.setName(client.getDisplayName());
		holder.setAddress(client.getAddress());
		return holder;
	}

}
