package com.ervacon.springbank.domain;

public interface ClientRepository {
	
	public Client createClient(Client clientData);
	
	public Client getClient(long clientId);

}
