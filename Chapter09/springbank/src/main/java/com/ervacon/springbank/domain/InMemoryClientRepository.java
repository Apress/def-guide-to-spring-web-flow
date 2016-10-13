package com.ervacon.springbank.domain;

import java.util.HashMap;
import java.util.Map;

public class InMemoryClientRepository implements ClientRepository {
	
	private long CLIENT_ID_COUNTER = 0L;
	
	private synchronized long nextClientId() {
		return CLIENT_ID_COUNTER++;
	}
	
	private Map<Long, Client> clients = new HashMap<Long, Client>();

	public Client createClient(Client clientData) {
		Client newClient = new Client(
				nextClientId(), clientData.getFirstName(), clientData.getLastName(),
				clientData.getAddress());
		clients.put(newClient.getClientId(), newClient);
		return newClient;
	}

	public Client getClient(long clientId) {
		return clients.get(clientId);
	}

}
