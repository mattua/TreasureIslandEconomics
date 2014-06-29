package org.treasure.island.model;
import java.util.HashSet;
import java.util.Set;


public class Shop extends AccountHolder {

	public static int shopCounter = 0;
	
	public static Set<Shop> allShops = new HashSet<Shop>();

	public Shop(String name, double initialCashReserves,Currency currency)
			throws Exception {

		super(currency,initialCashReserves,name);

		this.id = shopCounter++;

		allShops.add(this);
	}
	
}
