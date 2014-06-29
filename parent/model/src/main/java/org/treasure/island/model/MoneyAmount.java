package org.treasure.island.model;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyAmount {

	public static final NumberFormat GBP_FORMAT() {
		return NumberFormat.getCurrencyInstance(Locale.UK);
	}
	
	public final double amount;
	
	public final Currency currency;

	public MoneyAmount(double amount, Currency currency) {
		super();
		this.amount = amount;
		this.currency = currency;
	}
	public static double roundToMoney(double amount){
		return ((double) Math.round(100.0*amount))/100.0;
	}
	public static double roundDownToMoney(double amount){
		return ((double) Math.floor(100.0*amount))/100.0;
	}
}
