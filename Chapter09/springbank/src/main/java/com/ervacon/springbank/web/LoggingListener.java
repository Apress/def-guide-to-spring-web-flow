package com.ervacon.springbank.web;

import org.apache.log4j.Logger;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.EnterStateVetoException;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;

public class LoggingListener extends FlowExecutionListenerAdapter {

	private Logger logger = Logger.getLogger(LoggingListener.class);

	public void requestSubmitted(RequestContext context) {
		log("requestSubmitted", context);
	}

	public void requestProcessed(RequestContext context) {
		log("requestProcessed", context);
	}

	public void sessionStarting(RequestContext context, FlowDefinition definition, MutableAttributeMap input) {
		log("sessionStarting", context);
	}

	public void sessionCreated(RequestContext context, FlowSession session) {
		log("sessionCreated", context);
	}

	public void sessionStarted(RequestContext context, FlowSession session) {
		log("sessionStarted", context);
	}

	public void eventSignaled(RequestContext context, Event event) {
		log("eventSignaled", context);
	}

	public void stateEntering(RequestContext context, StateDefinition state) throws EnterStateVetoException {
		log("stateEntering", context);
	}

	public void stateEntered(RequestContext context, StateDefinition previousState, StateDefinition newState) {
		log("stateEntered", context);
	}

	public void resumed(RequestContext context) {
		log("resumed", context);
	}

	public void paused(RequestContext context, ViewSelection selectedView) {
		log("paused", context);
	}

	public void sessionEnding(RequestContext context, FlowSession session, MutableAttributeMap output) {
		log("sessionEnding", context);
	}

	public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
		log("sessionEnded", context);
	}

	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
		log("exceptionThrown", context);
	}

	private void log(String msg, RequestContext context) {
		boolean active = context.getFlowExecutionContext().isActive();
		FlowSession activeSession = active ? context.getFlowExecutionContext().getActiveSession() : null;
		logger.info(String.format("%16s [ active = %-5s | root = %-5s | status = %-8s ]",
				msg,
				active,
				active ? activeSession.isRoot() : false,
				active ? activeSession.getStatus() : ""));
	}
}
