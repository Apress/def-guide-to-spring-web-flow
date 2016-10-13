package com.ervacon.springbank;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class SpringConfigTests extends TestCase {
	
	public void testLoadServiceLayer() {
		new ClassPathXmlApplicationContext("service-layer.xml");
	}
}
