package com.ervacon.springbank.domain;

import java.io.Serializable;

public class Address implements Serializable {
	
	private String street;
	private String poBox;
	private String zipCode;
	private String city;
	
	public Address() {
	}
	
	public Address(String street, String poBox, String zipCode, String city) {
		this.street = street;
		this.poBox = poBox;
		this.zipCode = zipCode;
		this.city = city;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getPoBox() {
		return poBox;
	}
	
	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
}
