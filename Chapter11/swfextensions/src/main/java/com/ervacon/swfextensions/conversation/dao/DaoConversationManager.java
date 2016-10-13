package com.ervacon.swfextensions.conversation.dao;

import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationException;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.conversation.ConversationParameters;
import org.springframework.webflow.conversation.NoSuchConversationException;
import org.springframework.webflow.conversation.impl.SimpleConversationId;
import org.springframework.webflow.util.RandomGuidUidGenerator;
import org.springframework.webflow.util.UidGenerator;

/**
 * ConversationManager implementation that uses a DAO to persist
 * coversations in a database.
 * <p>
 * When configuring this conversation manager as a bean in a Spring application context,
 * make sure to supply the relevant {@link ConversationDao} using the
 * "conversationDao" property.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @see JdbcConversationDao
 * 
 * @author Erwin Vervaet
 */
public class DaoConversationManager implements ConversationManager {
	
	private UidGenerator conversationIdGenerator = new RandomGuidUidGenerator();
	
	private ConversationDao conversationDao;
	
	public void setConversationDao(ConversationDao conversationDao) {
		this.conversationDao = conversationDao;
	}

	public Conversation beginConversation(ConversationParameters conversationParameters)
			throws ConversationException {
		ConversationId convId = new SimpleConversationId(conversationIdGenerator.generateUid());
		conversationDao.createConversation(new PersistentConversation(convId, conversationDao));
		return getConversation(convId);
	}

	public Conversation getConversation(ConversationId id) throws ConversationException {
		if (PersistentConversationHolder.holdsConversation(id)) {
			// we already loaded the conversation for the calling thread
			return PersistentConversationHolder.getConversation(id);
		}
		else {
			// load the conversation
			PersistentConversation conversation = conversationDao.readConversation(id);
			if (conversation == null) {
				throw new NoSuchConversationException(id);
			}
			// cache it for the calling thread
			PersistentConversationHolder.putConversation(conversation);
			return conversation;
		}
	}

	public ConversationId parseConversationId(String encodedId) throws ConversationException {
		return new SimpleConversationId(conversationIdGenerator.parseUid(encodedId));
	}

}
