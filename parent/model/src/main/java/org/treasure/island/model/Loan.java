package org.treasure.island.model;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class Loan extends Obligation {

	public static enum PaymentPeriodicity {
		MONTHLY(30),ANNUALLY(360),QUARTERLY(90);
	
		public final int days;

		private PaymentPeriodicity(int days) {
			this.days = days;
		}
		
		
		
	}
	
	private PaymentPeriodicity paymentPeriodicity;
	
	public static Set<Loan> allLoans = new HashSet<Loan>();

	public static double dayDiff(Date from,Date to){
		
		return (to.getTime()-from.getTime())/(1000*60*60*24);
		
	}
	
	
	// Assumes no compound interest and bullet principal payment at the end
	public double getPresentValue(Date asof,double riskFreeRate){
		
		double pv = 0.0;
		long paymentsRemaining =  Math.round(getRemainingTerm(asof)/this.paymentPeriodicity.days);
		
		double paymentAmount = getIssueSize()*getInterestRate()*this.paymentPeriodicity.days/360.0;
		
		if (riskFreeRate>0&&riskFreeRate<1){
			pv= (getIssueSize()*(Math.pow((1+riskFreeRate),-paymentsRemaining)))+(paymentAmount*(1-Math.pow(1+riskFreeRate,-paymentsRemaining))/riskFreeRate);
		}
		else if (riskFreeRate==0.0){
			pv=(paymentAmount*paymentsRemaining)+getIssueSize();
		}
		
		return pv;
	}
	
	public double getRemainingTerm(Date asof){
		
		if (asof.before(getStartDate())){
			throw new IllegalStateException("Cannot value a loan before its start date");
		}
		if (asof.after(getEndDate())){
			throw new IllegalStateException("Cannot value a loan after its end date");
		}
		
		return dayDiff(asof,this.getEndDate());
		
	}
	
	
	public Loan(double amount, Date startDate, Date endDate,
			double interestRate, AccountHolder borrower, Bank lender,Account borrowerAccount,Currency currency) {
		
		super(amount, startDate, endDate,
				interestRate, borrower, lender,borrowerAccount,currency);
		
		this.paymentPeriodicity=PaymentPeriodicity.MONTHLY;
		
		
		allLoans.add(this);
		
		
		
	}
	
	

public static double getPVOfAllLoansIssueInCurrency(Date asof,double riskFreeRate,Currency currency){
	
	return getPVOfAllLoansIssueInCurrency(asof,riskFreeRate,currency);
	
	
}

public static double getFaceValueOfLoansIssueInCurrency(Date asof,Currency currency){
	
	return getFaceValueOfLoansIssueInCurrency(asof,currency);
	
	
}

private static List<Loan> getLoans(Currency currency,Bank[] banks){
	
	List<Loan> loans = new ArrayList<Loan>();
	for (Loan loan:allLoans){
		
		if (currency.equals(loan.getCurrency())&&(banks==null||Arrays.asList(banks).contains(loan.getLender()))){
			loans.add(loan);
		}
	}
	return loans;
	
}
public static double getFaceValueOfLoansIssueInCurrency(Bank[] banks,Date asof,Currency currency){
	
	double pv = 0.0;
	for (Loan loan:Loan.getLoans(currency,banks)){
		
			pv+=loan.getOutstandingBalance();
	}
	return pv;
	
	
}


public static double getPVOfAllLoansIssueInCurrency(Bank[] banks,Date asof,double riskFreeRate,Currency currency){
	
	double pv = 0.0;
	for (Loan loan:Loan.getLoans(currency,banks)){
		
			pv+=loan.getPresentValue(asof, riskFreeRate);
	
	}
	return pv;
	
	
}

public static void main(String[] args) throws Exception {
	
	DateFormat format = new SimpleDateFormat("ddMMMyyyy");
	Date from = format.parse("12Sep2011");
	Date to = format.parse("30Oct2011");
	System.out.println(dayDiff(from, to));
	
	
}

}