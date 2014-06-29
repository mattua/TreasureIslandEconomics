package org.treasure.island.model;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class Obligation {

	
	
	
	public static Set<Loan> allLoans = new HashSet<Loan>();

	private int id;

	private static int loanCounter = 0;

	private final double issueSize;

	private final Date startDate;

	private final Date endDate;

	private final double interestRate;

	private double outstandingBalance;

	private Currency currency;

	private AccountHolder borrower;

	public double getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	private Account borrowerAccount;
	private Bank lender;

	public void pig() {

	}

	public Obligation(double amount, Date startDate, Date endDate,
			double interestRate, AccountHolder borrower, Bank lender,
			Account borrowerAccount, Currency currency) {
		super();
		this.issueSize = amount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.interestRate = interestRate;
		this.borrower = borrower;
		this.lender = lender;
		this.borrowerAccount = borrowerAccount;
		this.outstandingBalance = amount;
		this.currency = currency;

		if (!borrowerAccount.getAccountHolder().equals(borrower)) {
			throw new IllegalStateException(
					"Borrower can only receive the loan into their own account");
		}

		// First ensure the bank has sufficient excess reserves to lend

		double availableToLend = 0.0;
		if (!borrowerAccount.getBank().equals(lender)) {
			availableToLend = lender.getExcessReservesForExternalLoan();
		} else {
			// if loan is made via a direct deposit creation then they can
			// expand the deposits
			// rather than lend the money out via diminishing the reserves
			// TODO: this should really be coded in the bank class
			availableToLend = lender.getExcessReservesForInternalDepositLoan();
		}

		if (amount > availableToLend) {
			throw new IllegalStateException("Cannot make loan of " + amount
					+ " since bank only has excess reserves of "
					+ availableToLend + " to lend.");
		}

		// Note if the loan is credited into the account of the borrower
		// at the same bank as the lender, the the reserves are unchanged
		// If paid into another bank, the deposit at the other bank needs
		// to be compensated by a transfer of reserves from the lending bank
		// to the bank of the borrower's account

		// Transfer cash reserves into central bank - this should be moved out
		// of here. This is only done if the loan is paid into a different bank

		if (!borrowerAccount.getBank().equals(lender)) {
			if (lender.getReservesAtCentralBank() - amount < 0.0) {

				lender.getCentralBank().depositCashInAccount(
						lender.getCentralBankAccount(),
						amount - lender.getReservesAtCentralBank());
			}
			lender.getCentralBankAccount().modifyAccountBalance(-amount);
			borrowerAccount.getBank().getCentralBankAccount()
					.modifyAccountBalance(amount);
		}

		borrowerAccount.modifyAccountBalance(amount);

		this.id = loanCounter++;

	}

	public Currency getCurrency() {
		return currency;
	}

	public AccountHolder getBorrower() {
		return borrower;
	}

	public void setBorrower(AccountHolder borrower) {
		this.borrower = borrower;
	}

	public Bank getLender() {
		return lender;
	}

	public void setLender(Bank lender) {
		this.lender = lender;
	}

	public double getIssueSize() {
		return issueSize;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public double getInterestRate() {
		return interestRate;
	}

}
