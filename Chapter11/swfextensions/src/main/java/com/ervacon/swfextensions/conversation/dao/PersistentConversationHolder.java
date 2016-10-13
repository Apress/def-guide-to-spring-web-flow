package com.ervacon.swfextensions.conversation.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.NoSuchConversationException;

/**
 * Thread local holder for persistent conversations.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 *
 * @author Erwin Vervaet
 */
public class PersistentConversationHolder {

	public static final ThreadLocal<Map<ConversationId, PersistentConversation>> conversations =
		new ThreadLocal<Map<ConversationId, PersistentConversation>>() {
			protected Map<ConversationId, PersistentConversation> initialValue() {
				return new HashMap<ConversationId, PersistentConversation>();
			}
		};

	public static boolean holdsConversation(ConversationId id) {
		return conversations.get().containsKey(id);
	}
	
	public static void assertHoldsConversation(ConversationId id) throws NoSuchConversationException {
		if (!holdsConversation(id)) {
			throw new NoSuchConversationException(id);
		}
	}
	
	public static PersistentConversation getConversation(ConversationId id) throws NoSuchConversationException {
		assertHoldsConversation(id);
		return conversations.get().get(id);
	}
	
	public static void putConversation(PersistentConversation conversation) throws NoSuchConversationException {
		conversations.get().put(conversation.getId(), conversation);
	}
	
	public static void removeConversation(ConversationId id) throws NoSuchConversationException {
		assertHoldsConversation(id);
		conversations.get().remove(id);
	}
}
