package com.ervacon.swfextensions.listeners;

import java.util.LinkedList;

import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

/**
 * Listener that tracks a trail of breadcrumbs.
 * The trail is stored in a conversation scoped attributed called "trail".
 * A breadcrumb is generated for each flow session.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @author Erwin Vervaet
 */
public class BreadcrumbListener extends FlowExecutionListenerAdapter {
	
	@Override
	public void sessionStarting(RequestContext context, FlowDefinition definition, MutableAttributeMap input) {
		@SuppressWarnings("unchecked")
		LinkedList<String> trail =
			(LinkedList<String>)context.getConversationScope().get("trail");
		if (trail == null) {
			trail = new LinkedList<String>();
			context.getConversationScope().put("trail", trail);
		}
		trail.add(definition.getId());
	}
	
	@Override
	public void sessionEnding(RequestContext context, FlowSession session, MutableAttributeMap output) {
		@SuppressWarnings("unchecked")
		LinkedList<String> trail =
			(LinkedList<String>)context.getConversationScope().get("trail");
		if (trail != null) {
			trail.removeLast();
		}
	}
}
