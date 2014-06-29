package org.treasure.island.model;
import java.util.HashSet;
import java.util.Set;

public class Account {

	private static int accountCounter = 0;

	private double overdraft = 0.0;
	public double getOverdraft() {
		return overdraft;
	}



	private double interestRate=0.0;
	
	private final Currency currency;
	
	public Currency getCurrency() {
		return currency;
	}

	public Account(Bank bank,Double interestRate,AccountHolder accountHolder,Currency currency) {

		super();
		this.currency=currency;
		this.interestRate=interestRate;
		this.bank = bank;
		
		this.accountNumber = accountCounter++;
		this.accountHolder = accountHolder;

		allAccounts.add(this);
	}
	
	public synchronized void modifyAccountBalance(double amount){
		
		if (!getBank().equals(this.getBank())){
			throw new IllegalStateException("Bank can't operate on account not administered by the bank");
		}
		
		if (getBalance()+amount<getOverdraft()){
			throw new IllegalStateException("Overdraft limit exceeded or not enabled "+getBalance());
		}
		
		setBalance(getBalance()+amount);
			
	}
	
	

	public static Set<Account> allAccounts = new HashSet<Account>();

	private Bank bank;

	private Double balance=0.0;

	private Integer accountNumber;

	private AccountHolder accountHolder;

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Double getBalance() {
		return balance;
	}

	private void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}
}
