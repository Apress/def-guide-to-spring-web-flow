package com.ervacon.swfextensions.listeners;

import java.util.Stack;

import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

/**
 * Listener that tracks a view state history in flow scope. Makes
 * a "previousViewStateId" attribute available in flash scope that
 * can be used in a global <i>back</i> transition:
 * <pre>
 * &lt;transition on="back" to="${flashScope.previousViewStateId}"/&gt;
 * </pre>
 * <p>
 * This listener does not support subflows.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @author Erwin Vervaet
 */
public class BackListener extends FlowExecutionListenerAdapter {

	@SuppressWarnings("unchecked")
	private Stack<String> getViewStateIds(RequestContext context) {
		return (Stack<String>)context.getFlowScope().get("viewStateIds");
	}
	
	public void sessionCreated(RequestContext context, FlowSession session) {
		session.getScope().put("viewStateIds", new Stack<String>());
	}
	
	public void eventSignaled(RequestContext context, Event event) {
		if ("back".equals(event.getId())) {
			if (!getViewStateIds(context).isEmpty()) {
				context.getFlashScope().put(
					"previousViewStateId", getViewStateIds(context).pop());
			}
		}
	}

	public void stateEntered(RequestContext context, StateDefinition previousState, StateDefinition state) {
		if (previousState instanceof ViewState) {
			if (previousState.getId().equals(state.getId())) {
				return;
			}
			
			if ("back".equals(context.getLastEvent().getId())) {
				return;
			}
			
			getViewStateIds(context).push(previousState.getId());
		}
	}
}
