package com.ervacon.swfextensions.conversation.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.style.ToStringCreator;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.impl.ConversationLock;
import org.springframework.webflow.conversation.impl.ConversationLockFactory;
import org.springframework.webflow.conversation.impl.SimpleConversationId;

/**
 * A persistent conversation.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @see ConversationDao
 * 
 * @author Erwin Vervaet
 */
public class PersistentConversation implements Conversation, Serializable {
	
	private ConversationId id;
	private Map<Object, Object> attributes;
	
	private transient ConversationDao dao;
	
	private transient ConversationLock lock = ConversationLockFactory.createLock();
	private transient int lockCount = 0;
	private transient boolean ended = false;
	
	protected PersistentConversation() {
	}
	
	public PersistentConversation(ConversationId id, ConversationDao dao) {
		this.id = id;
		this.attributes = new HashMap<Object, Object>();
		this.dao = dao;
	}

	public ConversationId getId() {
		return id;
	}
	
	protected void setId(ConversationId id) {
		this.id = id;
	}

	public void lock() {
		lock.lock();
		lockCount++;
	}

	public Object getAttribute(Object name) {
		return attributes.get(name);
	}

	public void putAttribute(Object name, Object value) {
		attributes.put(name, value);
	}

	public void removeAttribute(Object name) {
		attributes.remove(name);
	}

	public void end() {
		dao.deleteConversation(id);
		ended = true;
	}

	public void unlock() {
		lockCount--;
		lock.unlock();
		if (lockCount == 0) {
			if (!ended) {
				dao.updateConversation(this);
			}
			PersistentConversationHolder.removeConversation(id);
		}
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", id).append("attributes", attributes).toString();
	}
	
	// persistence helpers
	
	public void setDao(ConversationDao dao) {
		this.dao = dao;
	}

	protected String getIdAsString() {
		return getId().toString();
	}
	
	protected void setIdAsString(String id) {
		setId(new SimpleConversationId(id));
	}

	protected byte[] getAttributesAsByteArray() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(attributes);
			oout.flush();
			return bout.toByteArray();
		}
		catch (Exception e) {
			// should not happen
			throw new RuntimeException("Exception serializing attributes map", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected void setAttributesAsByteArray(byte[] bytes) {
		try {
			this.attributes = (Map<Object, Object>)
					new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
		}
		catch (Exception e) {
			// should not happen
			throw new RuntimeException("Exception deserializing attributes map", e);
		}
	}
}
