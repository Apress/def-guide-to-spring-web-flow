package com.ervacon.springbank.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;

import java.math.BigDecimal;
import java.util.Collections;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.MappingContext;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.engine.EndState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.execution.support.ExternalRedirect;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowServiceLocator;
import org.springframework.webflow.test.MockParameterMap;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import com.ervacon.springbank.domain.Account;
import com.ervacon.springbank.domain.AccountNumber;
import com.ervacon.springbank.domain.AccountRepository;
import com.ervacon.springbank.domain.Beneficiary;
import com.ervacon.springbank.domain.Payment;
import com.ervacon.springbank.domain.PaymentProcessingEngine;
import com.ervacon.springbank.user.User;

public class EnterPaymentFlowExecutionTests extends AbstractXmlFlowExecutionTests {

	private AccountRepository accountRepository = createMock(AccountRepository.class);
	private PaymentProcessingEngine paymentProcessingEngine = createMock(PaymentProcessingEngine.class);
	
	private Account debitAccount = new Beneficiary("Debit", "Test-0");
	private Account creditAccount = new Beneficiary("Credit", "Test-1");
	
	@Override
	protected FlowDefinitionResource getFlowDefinitionResource() {
		return createFlowDefinitionResource("src/main/webapp/WEB-INF/flows/enterPayment-flow.xml");
	}
	
	@Override
	protected void registerMockServices(MockFlowServiceLocator serviceRegistry) {
		serviceRegistry.registerBean("accountRepository", accountRepository);
		serviceRegistry.registerBean("paymentProcessingEngine", paymentProcessingEngine);
		
		// setup mock beneficiaries flow
		Flow beneficiariesFlow = new Flow("beneficiaries-flow");
		beneficiariesFlow.setOutputMapper(new AttributeMapper() {
			public void map(Object source, Object target, MappingContext context) {
				((MutableAttributeMap)target).put("beneficiary", creditAccount);
			}
		});
		new EndState(beneficiariesFlow, "endSelected");
		serviceRegistry.registerSubflow(beneficiariesFlow);
	}
		
	@Override
	protected ExternalContext createExternalContext(ParameterMap requestParameters) {
		MockExternalContext externalContext = new MockExternalContext(requestParameters);
		externalContext.getSessionMap().put("user", new User("test", "test", 0L));
		return externalContext;
	}
	
	// test methods

	public void testStartFlow() {
		expect(accountRepository.getAccounts(0L)).andReturn(Collections.<Account>singletonList(debitAccount));
		replay(accountRepository);
		
		ApplicationView view = applicationView(startFlow());
		assertCurrentStateEquals("showSelectDebitAccount");
		assertModelAttributeNotNull("payment", view);
		assertModelAttributeCollectionSize(1, "accounts", view);
	}

	public void testSelectDebitAccount() {
		testStartFlow();
		
		reset(accountRepository);
		expect(accountRepository.getAccount(debitAccount.getNumber())).andReturn(debitAccount);
		replay(accountRepository);
		
		MockParameterMap params = new MockParameterMap();
		params.put("debitAccount", "Test-0");
		ApplicationView view = applicationView(signalEvent("next", params));
		assertCurrentStateEquals("showEnterPaymentInfo");
		assertModelAttributeEquals(new AccountNumber("Test-0"), "payment.debitAccount.number", view);
	}

	public void testSelectDebitAccountFailure() {
		testStartFlow();
		signalEvent("next");
		assertCurrentStateEquals("showEnterPaymentInfo");
		MockParameterMap params = new MockParameterMap();
		params.put("creditAccount.number", "Test-1");
		params.put("amount", "150");
		params.put("message", "Test transfer");
		signalEvent("next", params);
		// validation failure: debit account not selected!
		assertCurrentStateEquals("showEnterPaymentInfo");
	}

	public void testSelectBeneficiary() {
		testSelectDebitAccount();
		ApplicationView view = applicationView(signalEvent("selectBeneficiary"));
		assertCurrentStateEquals("showEnterPaymentInfo");
		assertModelAttributeEquals(new AccountNumber("Test-1"), "payment.creditAccount.number", view);
	}

	public void testEnterPaymentInfo() {
		testSelectBeneficiary();
		MockParameterMap params = new MockParameterMap();
		params.put("amount", "150");
		params.put("message", "Test transfer");
		ApplicationView view = applicationView(signalEvent("next", params));
		assertCurrentStateEquals("showConfirmPayment");
		assertModelAttributeEquals(new BigDecimal(150), "payment.amount", view);
		assertModelAttributeEquals("Test transfer", "payment.message", view);
	}

	public void testEnterPaymentInfoFailure() {
		testSelectBeneficiary();
		MockParameterMap params = new MockParameterMap();
		params.put("message", "Test transfer");
		signalEvent("next", params);
		// validation failure: no amount
		assertCurrentStateEquals("showEnterPaymentInfo");
	}

	public void testSubmit() throws Exception {
		testEnterPaymentInfo();
		
		paymentProcessingEngine.submit((Payment)getFlowExecution().getConversationScope().get("payment"));
		replay(paymentProcessingEngine);
		
		ExternalRedirect redirect = externalRedirect(signalEvent("submit"));
		assertFlowExecutionEnded();
		assertEquals("/balances/show.html?confirmationMessage=paymentSubmitted", redirect.getUrl());
	}

}
