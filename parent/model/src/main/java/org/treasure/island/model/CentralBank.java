package org.treasure.island.model;



public class CentralBank extends Bank {

	public double requiredReserveRatio;
	
	private Country country;
	
	public static final double UNLIMITED = -1;
	
	public double baseRate;
	
	private Currency currency;
	
	public Currency getCurrency() {
		return currency;
	}

	public CentralBank(String name,Country country,double baseRate,double requiredReserveRatio,Currency currency){
		
		super(name,null,UNLIMITED,UNLIMITED);
		this.baseRate = baseRate;
		this.country=country;
		this.requiredReserveRatio=requiredReserveRatio;
		this.currency=currency;
		
	}
	
	public double getRequiredReserveRatio() {
		return requiredReserveRatio;
	}

	@Override
	public Double getReservesAtCentralBank()  {
		
		throw new IllegalStateException("This is a central bank. It does not have central bank reserves");
		
	}
	

}
