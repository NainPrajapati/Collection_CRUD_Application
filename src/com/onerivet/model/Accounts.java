package com.onerivet.model;

public class Accounts {
	private String accountNumber;
	private final String userId;
	private Double balance;


	public Accounts(String userId, Double balance){
	    this.userId = userId;
	    this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	 public void setAccountNumber(String accountNumber) {
	        this.accountNumber = accountNumber;
	    }
	public String getUserId() {
		return userId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	
}
