package org.treasure.island.model;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



public class Bond extends Obligation {

	public static Set<Bond> allBonds = new HashSet<Bond>();


	public Bond(double amount, Date startDate, Date endDate,
			double interestRate, AccountHolder borrower, Bank lender,Account borrowerAccount,Currency currency) {
		
		super(amount, startDate, endDate,
				interestRate, borrower, lender,borrowerAccount,currency);
		allBonds.add(this);
		
		
		
	}

}
