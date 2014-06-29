package org.treasure.island.model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person extends AccountHolder {

	
	public static Set<Person> population = new HashSet<Person>();

	private static int personCounter = 0;

	private Date dateOfBirth;

	

	public Person(String dateOfBirth, String name, double initialCashReserves,Currency currency)
			throws Exception {

		super(currency,initialCashReserves,name);

		this.dateOfBirth = new SimpleDateFormat("ddMMMyyyy").parse(dateOfBirth);
		this.id = personCounter++;

		population.add(this);
	}
	
	public void donate(AccountHolder recipient,Currency currency,double amount){
		
		if (amount<0){
			throw new IllegalArgumentException("Cannot lend negative amount");
		}
		
		recipient.incrementCashReserves(currency,amount);
		decrementCashReserves(currency,amount);
	}
	
	public void windfall(Goods goods){
		
		if (!goods.getGoodsType().assetType.equals(Goods.ASSET_TYPE.CONSUMABLE)){
			getNonCashAssets().add(goods);
		}
		
		
	}
	
	
	public void steal(Person victim,double amount,Currency currency){
		
		if (amount<0){
			throw new IllegalArgumentException("Cannot lend negative amount");
		}
		
		victim.decrementCashReserves(currency,amount);
		incrementCashReserves(currency,amount);
	}

}
