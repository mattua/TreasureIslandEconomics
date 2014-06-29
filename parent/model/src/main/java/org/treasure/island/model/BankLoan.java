package org.treasure.island.model;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



public class BankLoan extends Loan {

	public static Set<BankLoan> allLoans = new HashSet<BankLoan>();

	
	public BankLoan(double amount, Date startDate, Date endDate,
			double interestRate, AccountHolder borrower, Bank lender,Account borrowerAccount,Currency currency) {
		
		super(amount, startDate, endDate,
				interestRate, borrower, lender,borrowerAccount,currency);
		allLoans.add(this);
		
		
		
	}

}
