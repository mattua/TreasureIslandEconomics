package org.treasure.island.model;
import java.util.Locale;


public enum Currency {

	GBP(Locale.UK),USD(Locale.US),EUR(Locale.ITALY);
	
	private final Locale locale;
	
	private Currency(Locale locale){
		this.locale=locale;
	}
}
