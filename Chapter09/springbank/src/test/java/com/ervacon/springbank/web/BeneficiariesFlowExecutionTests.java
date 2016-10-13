package com.ervacon.springbank.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;

import java.util.Collections;

import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowServiceLocator;
import org.springframework.webflow.test.MockParameterMap;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import com.ervacon.springbank.domain.Account;
import com.ervacon.springbank.domain.AccountRepository;
import com.ervacon.springbank.domain.Beneficiary;
import com.ervacon.springbank.user.User;

public class BeneficiariesFlowExecutionTests extends AbstractXmlFlowExecutionTests {

	private AccountRepository accountRepository = createMock(AccountRepository.class);

	@Override
	protected FlowDefinitionResource getFlowDefinitionResource() {
		return createFlowDefinitionResource("src/main/webapp/WEB-INF/flows/beneficiaries-flow.xml");
	}
	
	@Override
	protected void registerMockServices(MockFlowServiceLocator serviceRegistry) {
		serviceRegistry.registerBean("accountRepository", accountRepository);
	}

	@Override
	protected ExternalContext createExternalContext(ParameterMap requestParameters) {
		MockExternalContext externalContext = new MockExternalContext(requestParameters);
		externalContext.getSessionMap().put("user", new User("test", "test", 0L));
		return externalContext;
	}
	
	// test methods

	public void testStartflow() {
		expect(accountRepository.getAccounts(0L)).andReturn(
				Collections.<Account>singletonList(new Beneficiary("Account", "0")));
		expect(accountRepository.getBeneficiaries(0L)).andReturn(
				Collections.<Account>singletonList(new Beneficiary("Beneficiary", "9")));
		replay(accountRepository);
		
		ApplicationView view = applicationView(startFlow());
		assertCurrentStateEquals("showBeneficiaries");
		assertModelAttributeCollectionSize(1, "accounts", view);
		assertModelAttributeCollectionSize(1, "beneficiaries", view);
	}
	
	public void testSelect() {
		setFlowExecutionListener(new FlowExecutionListenerAdapter() {
			@Override
			public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
				assertEquals("endSelected", session.getState().getId());
				assertTrue(output.contains("beneficiary"));
			}
		});

		testStartflow();
		
		reset(accountRepository);
		expect(accountRepository.getAccount("9")).andReturn(new Beneficiary("Beneficiary", "9"));
		replay(accountRepository);
		
		MockParameterMap params = new MockParameterMap();
		params.put("accountNumber", "9");
		nullView(signalEvent("select", params));
		assertFlowExecutionEnded();
	}
	
	public void testCancel() {
		setFlowExecutionListener(new FlowExecutionListenerAdapter() {
			@Override
			public void sessionEnded(RequestContext context, FlowSession session, AttributeMap output) {
				assertTrue(output.isEmpty());
			}
		});

		testStartflow();
		nullView(signalEvent("cancel"));
		assertFlowExecutionEnded();
	}
}
