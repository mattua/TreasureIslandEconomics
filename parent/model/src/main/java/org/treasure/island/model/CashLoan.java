package org.treasure.island.model;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



public class CashLoan extends Loan {

	public static Set<CashLoan> allLoans = new HashSet<CashLoan>();

	
	public CashLoan(double amount, Date startDate, Date endDate,
			double interestRate, AccountHolder borrower, Bank lender,Account borrowerAccount,Currency currency) {
		
		super(amount, startDate, endDate,
				interestRate, borrower, lender,borrowerAccount,currency);
		allLoans.add(this);
		
		
		
	}

}
