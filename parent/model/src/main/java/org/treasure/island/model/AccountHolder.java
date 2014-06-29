package org.treasure.island.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AccountHolder implements BalanceSheet {

	private static int accountHolderCounter =0;
	public final String name;
	public Integer id;
	
	private Map<Currency,Double> cashReserves = new HashMap<Currency,Double>();
	
	
	private void modifyCashReserves(Currency currency,double amount){
		
		Double soFar = cashReserves.get(currency)!=null?cashReserves.get(currency):0.0;
		Double latest = soFar + amount;
		
		cashReserves.put(currency,latest);
		
	}

	private List<Goods> nonCashAssets=new ArrayList<Goods>();
	
	public double getLiabilities(Currency currency){
		return getLoanLiabilities();
	}
	
	public String getName() {
		return name;
	}
	
	public void cashPurchase(AccountHolder shop,Goods goods){
		
		new CashTransaction(this, shop, goods).transact();
	}
	
	
	public Account getAccountByBank(Bank bank){
		for (Account account : getAccounts()) {

			if (account.getBank().equals(bank)) {
				return account;
				
			}

		}
		return null;
	}
	
	// Take the first debit card from the wallet for the given bank
	public void debitcardPurchase(Bank myBank,AccountHolder shop,Goods goods){
		
		for (Account account : getAccounts()) {

			if (account.getBank().equals(myBank)) {
				debitcardPurchase(account,shop,goods);
				break;
			}

		}
		
		
	}

	public void centrallyClearedChequePurchase(Bank myBank,Bank theirBank,AccountHolder shop,Goods goods){
		
		for (Account account : getAccounts()) {

			if (account.getBank().equals(myBank)) {
				centrallyClearedChequePurchase(account,theirBank,shop,goods);
				break;
			}

		}
		
		
	}
public void centrallyClearedChequePurchase(Account myAccount,Bank theirBank,AccountHolder payee,Goods goods){
		
		// Pass null, when calling from here we don't care about the
		// account the merchant is using to pay his money into
				
		
		new CentrallyClearedChequeTransaction(this, payee, goods,myAccount,payee.getAccountByBank(theirBank));
	}
	public void debitcardPurchase(Account myAccount,AccountHolder shop,Goods goods){
		
		// Pass null, when calling from here we don't care about the
		// account the merchant is using to pay his money into
		new DebitCardTransaction(this, shop, goods,myAccount,null);
	}
	
	
	public Double getNonCashAssetValue(Currency currency) {
		
		Double nonCashAssets=0.0;
		
		for (Goods goods:this.nonCashAssets){
			
			if (goods.isAsset()){
				nonCashAssets+=goods.getMarketValue();
			}
		}
		
		return nonCashAssets;
	}
	
	public List<Goods> getNonCashAssets() {
		return nonCashAssets;
	}

	public void setNonCashAssets(List<Goods> nonCashAssets) {
		this.nonCashAssets = nonCashAssets;
	}

	public synchronized void incrementCashReserves(Currency currency,double amount){
		if (amount<0){
			throw new IllegalArgumentException("Cannot increment by a negative number");
		}
		
		modifyCashReserves(currency, amount);
		
	}
	public synchronized void decrementCashReserves(Currency currency,double amount){
		if (amount<0){
			throw new IllegalArgumentException("Cannot decrement by a negative number");
		}
		modifyCashReserves(currency, -amount);
	}
	public Double getCashReservesHeldInCurrency(Currency currency) {
		return cashReserves.get(currency)!=null?cashReserves.get(currency):0.0;
	}
	
	public Double getTotalCashReserves(Currency currency) {
		
		double totalCashReserves = 0.0;
		
		for (Currency ccy:cashReserves.keySet()){
			
			totalCashReserves += ForeignExchange.getInstance().convert(cashReserves.get(ccy)!=null?cashReserves.get(ccy):0.0,ccy,currency);
			
		}
		return totalCashReserves;
		
	}
	

	public void setCashReserves(Currency currency,Double cashReserves) {
		
		this.cashReserves.put(currency, cashReserves);
	}

	public AccountHolder(Currency currency,double initialCashReserves,String name) {
		
		setCashReserves(currency, initialCashReserves);
		
		this.name=name;
		
	}

	public List<Account> getAccounts() {

		List<Account> accounts = new ArrayList<Account>();
		for (Account account : Account.allAccounts) {

			if (account.getAccountHolder().equals(this)){
				accounts.add(account);

			}
		
		}
		return accounts;

	}

	//Just put in the first account
	public void deposit(Double amount) {

			for (Account account : getAccounts()) {

				depositCashInAccount(account, amount);
				break;

			}

	}
	
	// Just put in the first matching account
	public void deposit(Bank bank, Double amount) {

		for (Account account : getAccounts()) {

			if (account.getBank().equals(bank)) {
				depositCashInAccount(account, amount);
				break;
			}

		}

	}

	public void withdrawCashFromAccount(Bank bank, Double amount) {

		for (Account account : getAccounts()) {

			if (account.getBank().equals(bank)) {
				withdrawCashFromAccount(account, amount);
				break;
			}

		}

	}

	
	public synchronized void depositCashInAccount(Account account, Double amount) {

		if (amount < 0) {
			throw new IllegalArgumentException(
					"Cannot deposit a negative number");
		}
		if (amount > cashReserves.get(account.getCurrency())) {
			throw new IllegalArgumentException(
					"Cannot deposit more than your cash reserves");
		}
		if (!account.getAccountHolder().equals(this)){
			throw new IllegalArgumentException(
					"Cannot pay into an account you don't own");
		}
		
		
		account.getBank().depositCashInAccount(account,amount);
		
		
	}
	

	public synchronized void withdrawCashFromAccount(Account account, Double amount) {

		if (amount < 0) {
			throw new IllegalArgumentException(
					"Cannot withdraw a negative number");
		}

		account.getBank().withdrawCash(account,amount);
		
	}
	
	
	public synchronized double getLoanLiabilities(){
		
		Double loanLiabilities = 0.0;
		for (Loan loan:getLoansTakenOut()){
			loanLiabilities+=loan.getOutstandingBalance();
		}
		return loanLiabilities;
	}
	
	
	public synchronized List<Loan> getLoansTakenOut(){
		
		List<Loan> loans=new ArrayList<Loan>();
		for (Loan loan:Loan.allLoans){
			
			if (loan.getBorrower().equals(this)){
				loans.add(loan);
			}
			
		}
		return loans;
		
	}
	
	

	public synchronized double getAssets(Currency currency){
		
	
		
		Double assets = 0.0;
		
		Double accountHoldings = 0.0;
		for (Account account : getAccounts()) {

			if (account.getAccountHolder().equals(this)) {
				accountHoldings += ForeignExchange.getInstance().convert(account.getBalance(),account.getCurrency(),currency);
			}

		}
		assets += accountHoldings + getTotalCashReserves(currency)+ getNonCashAssetValue(currency);
		
		return assets;
	}
	
	

	public synchronized Double getNetWorth(Currency currency) {

		

		return getAssets(currency)-getLiabilities(currency);
	}
}
