package org.treasure.island.model;
import java.util.HashMap;
import java.util.Map;


public class ForeignExchange {

	private static ForeignExchange instance;
	
	private Currency anchorCurrency = Currency.USD;
	
	
	
	public Currency getAnchorCurrency() {
		return anchorCurrency;
	}

	public static ForeignExchange getInstance(){
		
		if (instance==null){
			instance = new ForeignExchange();
		}
		return instance;
	}
	
	private Map<Currency,Double> rates=new HashMap<Currency,Double>();
	
	public double convert(double amount,Currency from,Currency to){
		
		if (from.equals(to)){
			return amount;
		}
		else if (to.equals(this.getAnchorCurrency())){
			return amount*rates.get(from);
		}
		else if (from.equals(this.anchorCurrency)){
			return amount/rates.get(to);
		}
		else {
			
			return amount*rates.get(from)/rates.get(to);
			
		}
		
		
	}
	
	public static void main(String[] args){
		
		ForeignExchange.getInstance().setRate(Currency.GBP,2.0);
		ForeignExchange.getInstance().setRate(Currency.EUR,1.7);
		
		System.out.println(ForeignExchange.getInstance().convert(1.0, Currency.GBP, Currency.USD));		
		System.out.println(ForeignExchange.getInstance().convert(2.0, Currency.USD, Currency.GBP));		
		
		System.out.println(ForeignExchange.getInstance().convert(1.0, Currency.EUR, Currency.USD));		
		System.out.println(ForeignExchange.getInstance().convert(1.7, Currency.USD, Currency.EUR));		
		
		System.out.println(ForeignExchange.getInstance().convert(1.0, Currency.GBP, Currency.EUR));		
		System.out.println(ForeignExchange.getInstance().convert(1.2, Currency.EUR, Currency.GBP));		
		
		
	}
	
	
	
	public void setRate(Currency from,Double rate){
		
		if (from.equals(this.getAnchorCurrency())){
			throw new IllegalStateException("Cannot set the conversion rate for the anchor currency "+this.getAnchorCurrency());
		}
		
		rates.put(from,rate);
		
	}
			
	
}
