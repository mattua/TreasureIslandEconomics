package org.treasure.island.model;

public class CashTransaction extends Transaction {

	

	public CashTransaction(AccountHolder buyer, AccountHolder seller,
			Goods goods) {
		super(buyer,seller,goods);
			
	}
	
	public void transact(){
		seller.incrementCashReserves(goods.getCurrency(),goods.getPrice());
		buyer.decrementCashReserves(goods.getCurrency(),goods.getPrice());
		transferAssetValue();
	}
	
}
