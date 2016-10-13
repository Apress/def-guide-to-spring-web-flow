package com.ervacon.swfextensions.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.webflow.config.FlowExecutorFactoryBean;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.xml.XmlFlowRegistryFactoryBean;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.execution.support.ExternalRedirect;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.execution.support.FlowExecutionRedirect;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.executor.ResponseInstruction;
import org.springframework.webflow.executor.support.FlowExecutorArgumentHandler;
import org.springframework.webflow.executor.support.FlowRequestHandler;
import org.springframework.webflow.executor.support.RequestParameterFlowExecutorArgumentHandler;
import org.springframework.webflow.executor.support.ResponseInstructionHandler;

/**
 * A servlet that integrates Spring Web Flow into the raw Servlet API.
 * <p>
 * This servlet serves as an <b>example</b> illustrating how to integrate Spring Web Flow
 * into a web MVC framework. It acts as a flow executor front-end.
 * <p>
 * You can configure this servlet in web.xml like this:
 * <pre>
 *	&lt;servlet&gt;
 *		&lt;servlet-name&gt;flow-servlet&lt;/servlet-name&gt;
 *		&lt;servlet-class&gt;com.ervacon.flowservlet.web.FlowServlet&lt;/servlet-class&gt;
 *		&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *	&lt;/servlet&gt;
 *	&lt;servlet-mapping&gt;
 *		&lt;servlet-name&gt;flow-servlet&lt;/servlet-name&gt;
 *		&lt;url-pattern&gt;/flows.html&lt;/url-pattern&gt;
 *	&lt;/servlet-mapping&gt;
 * </pre>
 * The above configuration will delegate all /flows.html request to Spring Web Flow.
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @author Erwin Vervaet
 */
public class FlowServlet extends HttpServlet {
	
	private FlowExecutor flowExecutor;
	
	private FlowExecutorArgumentHandler argumentHandler;
	
	public void init() throws ServletException {
		log("initializing flow servlet");
		try {
			// setup flow registry
			XmlFlowRegistryFactoryBean flowRegistryFactory = new XmlFlowRegistryFactoryBean();
			flowRegistryFactory.setBeanFactory(new StaticListableBeanFactory());
			flowRegistryFactory.setFlowLocations(new Resource[] {
					new ServletContextResource(getServletContext(), "/WEB-INF/flows/test-flow.xml")
			});
			flowRegistryFactory.afterPropertiesSet();
			FlowDefinitionRegistry flowRegistry = flowRegistryFactory.getRegistry();
			
			// setup flow executor
			FlowExecutorFactoryBean flowExecutorFactory = new FlowExecutorFactoryBean();
			flowExecutorFactory.setDefinitionLocator(flowRegistry);
			flowExecutorFactory.afterPropertiesSet();
			flowExecutor = flowExecutorFactory.getFlowExecutor();
			
			// setup flow executor argument handler
			argumentHandler = new RequestParameterFlowExecutorArgumentHandler();
		}
		catch (Exception e) {
			throw new ServletException("FlowServlet initialization failed", e);
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log("servicing flow request");
		FlowRequestHandler handler = new FlowRequestHandler(flowExecutor, argumentHandler);
		ServletExternalContext context = new ServletExternalContext(getServletContext(), request, response);
		handleResponse(handler.handleFlowRequest(context), context);
	}
	
	private void handleResponse(final ResponseInstruction ri, final ServletExternalContext context) {
		log("handling flow response: " + ri);
		
		new ResponseInstructionHandler() {

			protected void handleApplicationView(ApplicationView view) throws Exception {
				@SuppressWarnings("unchecked")
				Map<String, Object> model = new HashMap<String, Object>(view.getModel());
				argumentHandler.exposeFlowExecutionContext(
						ri.getFlowExecutionKey(), ri.getFlowExecutionContext(), model);
				for (String key : model.keySet()) {
					context.getRequest().setAttribute(key, model.get(key));
				}
				context.getRequest().getRequestDispatcher("/WEB-INF/jsp/" + view.getViewName() + ".jsp").
						forward(context.getRequest(), context.getResponse());
			}

			protected void handleFlowDefinitionRedirect(FlowDefinitionRedirect redirect) throws Exception {
				String flowUrl = argumentHandler.createFlowDefinitionUrl(redirect, context);
				context.getResponse().sendRedirect(flowUrl);
			}

			protected void handleFlowExecutionRedirect(FlowExecutionRedirect redirect) throws Exception {
				String flowExecutionUrl = argumentHandler.createFlowExecutionUrl(
						ri.getFlowExecutionKey(), ri.getFlowExecutionContext(), context);
				context.getResponse().sendRedirect(flowExecutionUrl);
			}

			protected void handleExternalRedirect(ExternalRedirect redirect) throws Exception {
				String externalUrl = argumentHandler.createExternalUrl(redirect, ri.getFlowExecutionKey(), context);
				context.getResponse().sendRedirect(externalUrl);
			}

			protected void handleNull() throws Exception {
				// nothing to do
			}
			
		}.handleQuietly(ri);
	}
}
