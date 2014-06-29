package org.treasure.island.model;

public class Goods {

	private static int goodCounter = 0;
	
	private int goodId=0;
	
	public boolean isAsset(){
		return (!goodsType.equals(ASSET_TYPE.CONSUMABLE));
	}
	
	public static enum GoodsType {
		FOOD(ASSET_TYPE.CONSUMABLE),CAR(ASSET_TYPE.DEPRECIATING);
		
		public final ASSET_TYPE assetType;

		private GoodsType(ASSET_TYPE assetType) {
			this.assetType = assetType;
		}
		
		
	};
	
	public static enum ASSET_TYPE{
		CONSUMABLE,APPRECIATING,DEPRECIATING
	}
	
	private GoodsType goodsType;
	public Double getMarketValue(){
		return price;
	}
	private double price;
	
	private Currency currency;

	public Currency getCurrency() {
		return currency;
	}
	private final String name;
	
	public Goods(String name,GoodsType goodsType, double price,Currency currency) {
		super();
		this.name=name;
		this.goodsType = goodsType;
		this.price = price;
		this.currency=currency;
		this.goodId=goodCounter++;
	}

	public GoodsType getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + goodId;
		result = prime * result
				+ ((goodsType == null) ? 0 : goodsType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goods other = (Goods) obj;
		if (goodId != other.goodId)
			return false;
		if (goodsType != other.goodsType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		return true;
	}

	 
}
