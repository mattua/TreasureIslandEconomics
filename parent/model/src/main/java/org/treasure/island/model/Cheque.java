package org.treasure.island.model;

public class Cheque {

	private final AccountHolder payer;
	
	private final AccountHolder payee;
	
	private final double amount;

	private Cheque(AccountHolder payer, AccountHolder payee, double amount) {
		super();
		this.payer = payer;
		this.payee = payee;
		this.amount = amount;
	}
	
	
	
}
