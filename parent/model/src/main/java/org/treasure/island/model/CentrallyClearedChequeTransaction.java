package org.treasure.island.model;
public class CentrallyClearedChequeTransaction extends Transaction {

	private Account buyerAccount;
	private Account sellerAccount;

	public CentrallyClearedChequeTransaction(AccountHolder buyer, AccountHolder seller,
			Goods goods, Account buyerAccount, Account sellerAccount) {
		super(buyer, seller, goods);
		this.sellerAccount=sellerAccount;
		this.buyerAccount=buyerAccount;
		
		// Just take the first account if not specified
				if (buyerAccount==null) {
					buyerAccount = buyer.getAccounts().get(0);
				}
					
				if (buyerAccount==null) {
					throw new IllegalStateException("Can't do business with a buyer with no accounts");
				}
				
				
				if (sellerAccount==null) {
					throw new IllegalStateException("Can't do business with a seller with no accounts");
				}
					
				// TODO: Maybe turn this into a clearing house and not trust the bank with the task
				// of reducing its central bank balance
				buyerAccount.getBank().modifyAccountBalance(buyerAccount, -goods.getPrice());
				buyerAccount.getBank().getCentralBankAccount().modifyAccountBalance(-goods.getPrice());
				
				
				
				// TODO: NEED TO ADJUST FOR THE EXTRA LIABILITY THE
				// SELLER'S BANK HAS IN THE EXTRA DEPOSIT - IT WILL BE IN
				// A PAYMENT FROM THE BUYERS BANK
				sellerAccount.getBank().modifyAccountBalance(sellerAccount, goods.getPrice());
				sellerAccount.getBank().getCentralBankAccount().modifyAccountBalance(goods.getPrice());
				
				transferAssetValue();

	}

	
}
