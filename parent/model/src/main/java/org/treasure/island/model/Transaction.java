package org.treasure.island.model;
public abstract class Transaction {

	protected AccountHolder buyer;

	protected AccountHolder seller;

	protected Goods goods;

	public Transaction(AccountHolder buyer, AccountHolder seller, Goods goods) {
		super();
		this.buyer = buyer;
		this.seller = seller;
		this.goods = goods;
	}

	
	public void transferAssetValue() {
		{
			if (!goods.getGoodsType().assetType
					.equals(Goods.ASSET_TYPE.CONSUMABLE)) {
				buyer.getNonCashAssets().add(goods);
				seller.getNonCashAssets().remove(goods);
				

			}
		}

	}
}
