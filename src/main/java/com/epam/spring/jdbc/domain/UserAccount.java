package com.epam.spring.jdbc.domain;

public class UserAccount extends DomainObject {
	
	private Long userId;
	
	private double amount;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}
	
	
}
