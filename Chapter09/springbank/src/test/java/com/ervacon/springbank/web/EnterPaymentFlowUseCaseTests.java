package com.ervacon.springbank.web;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistryImpl;
import org.springframework.webflow.definition.registry.FlowDefinitionResource;
import org.springframework.webflow.engine.builder.DefaultFlowServiceLocator;
import org.springframework.webflow.engine.builder.FlowServiceLocator;
import org.springframework.webflow.engine.builder.xml.XmlFlowRegistrar;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.execution.support.ExternalRedirect;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockParameterMap;
import org.springframework.webflow.test.execution.AbstractXmlFlowExecutionTests;

import com.ervacon.springbank.domain.AccountNumber;
import com.ervacon.springbank.domain.AccountRepository;
import com.ervacon.springbank.user.UserService;

public class EnterPaymentFlowUseCaseTests extends AbstractXmlFlowExecutionTests {

	private BeanFactory beanFactory;
	private UserService userService;
	private AccountRepository accountRepository;
	
	@Override
	protected void setUp() throws Exception {
		// boot real service layer because this is an integration test
		beanFactory = new ClassPathXmlApplicationContext("service-layer.xml");
		userService = (UserService)beanFactory.getBean("userService");
		accountRepository = (AccountRepository)beanFactory.getBean("accountRepository");
	}

	@Override
	protected FlowDefinitionResource getFlowDefinitionResource() {
		return createFlowDefinitionResource("src/main/webapp/WEB-INF/flows/enterPayment-flow.xml");
	}
	
	@Override
	protected FlowServiceLocator createFlowServiceLocator() {
		// setup a flow definition registry for the subflows
		FlowDefinitionRegistry subflowRegistry = new FlowDefinitionRegistryImpl();
		
		FlowServiceLocator flowServiceLocator =
			new DefaultFlowServiceLocator(subflowRegistry, beanFactory);
		
		// load beneficiaries subflow and register it
		XmlFlowRegistrar registrar = new XmlFlowRegistrar(flowServiceLocator);
		registrar.addResource(createFlowDefinitionResource(
				"src/main/webapp/WEB-INF/flows/beneficiaries-flow.xml"));
		registrar.registerFlowDefinitions(subflowRegistry);
		
		return flowServiceLocator;
	}
	
	@Override
	protected ExternalContext createExternalContext(ParameterMap requestParameters) {
		MockExternalContext externalContext = new MockExternalContext(requestParameters);
		// make sure the user is logged in
		externalContext.getSessionMap().put("user", userService.login("erwinv", "foobar"));
		return externalContext;
	}
	
	// test methods

	public void testStartFlow() {
		ApplicationView view = applicationView(startFlow());
		assertCurrentStateEquals("showSelectDebitAccount");
		assertModelAttributeNotNull("payment", view);
		assertModelAttributeCollectionSize(2, "accounts", view);
	}
	
	public void testSelectDebitAccount() {
		testStartFlow();
		MockParameterMap params = new MockParameterMap();
		params.put("debitAccount", "SpringBank-0");
		ApplicationView view = applicationView(signalEvent("next", params));
		assertCurrentStateEquals("showEnterPaymentInfo");
		assertModelAttributeEquals(new AccountNumber("SpringBank-0"), "payment.debitAccount.number", view);
	}
	
	public void testLaunchBeneficiariesFlow() {
		testSelectDebitAccount();
		ApplicationView view = applicationView(signalEvent("selectBeneficiary"));
		assertActiveFlowEquals("beneficiaries-flow");
		assertCurrentStateEquals("showBeneficiaries");
		assertModelAttributeCollectionSize(2, "accounts", view);
		assertModelAttributeCollectionSize(2, "beneficiaries", view);
	}
	
	public void testSelectBeneficiaryCancel() {
		testLaunchBeneficiariesFlow();
		ApplicationView view = applicationView(signalEvent("cancel"));
		assertActiveFlowEquals("enterPayment-flow");
		assertCurrentStateEquals("showEnterPaymentInfo");
		assertModelAttributeNull("payment.creditAccount.number", view);
	}
	
	public void testSelectBeneficiary() {
		testLaunchBeneficiariesFlow();
		MockParameterMap params = new MockParameterMap();
		params.put("accountNumber", "SpringBank-1");
		ApplicationView view = applicationView(signalEvent("select", params));
		assertActiveFlowEquals("enterPayment-flow");
		assertCurrentStateEquals("showEnterPaymentInfo");
		assertModelAttributeEquals(new AccountNumber("SpringBank-1"), "payment.creditAccount.number", view);
	}
	
	public void testEnterPaymentInfo() {
		testSelectBeneficiary();
		MockParameterMap params = new MockParameterMap();
		params.put("amount", "150");
		params.put("message", "Test transfer");
		signalEvent("next", params);
		assertCurrentStateEquals("showConfirmPayment");
	}
	
	public void testSubmit() {
		testEnterPaymentInfo();
		ExternalRedirect redirect = externalRedirect(signalEvent("submit"));
		assertFlowExecutionEnded();
		assertEquals("/balances/show.html?confirmationMessage=paymentSubmitted", redirect.getUrl());
		assertEquals(9850.0D,
				accountRepository.getAccount(new AccountNumber("SpringBank-0")).getBalance().doubleValue());
		assertEquals(2150.0D,
				accountRepository.getAccount(new AccountNumber("SpringBank-1")).getBalance().doubleValue());
	}
	
	public void testCancel() {
		testEnterPaymentInfo();
		ExternalRedirect redirect = externalRedirect(signalEvent("cancel"));
		assertFlowExecutionEnded();
		assertEquals("/balances/show.html", redirect.getUrl());
		assertEquals(10000.0D,
				accountRepository.getAccount(new AccountNumber("SpringBank-0")).getBalance().doubleValue());
		assertEquals(2000.0D,
				accountRepository.getAccount(new AccountNumber("SpringBank-1")).getBalance().doubleValue());
	}
}
