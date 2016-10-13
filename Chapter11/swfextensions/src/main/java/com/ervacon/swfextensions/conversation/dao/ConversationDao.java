package com.ervacon.swfextensions.conversation.dao;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.conversation.ConversationId;

/**
 * Data access object for persistent conversations.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @see PersistentConversation
 * 
 * @author Erwin Vervaet
 */
@Transactional
public interface ConversationDao {
	
	public void createConversation(PersistentConversation conv);
	
	public PersistentConversation readConversation(ConversationId id);
	
	public void updateConversation(PersistentConversation conv);
	
	public void deleteConversation(ConversationId id);

}
