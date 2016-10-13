package com.ervacon.springbank.domain;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class AccountNumberEditor extends PropertyEditorSupport {

	public void setAsText(String text) throws IllegalArgumentException {
		setValue(StringUtils.hasText(text) ? new AccountNumber(text) : null);
	}
	
	public String getAsText() {
		return getValue() == null ? "" : getValue().toString();
	}
}
