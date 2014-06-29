package org.treasure.island.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bank extends AccountHolder {

	private static int bankCounter = 0;

	private static Set<Bank> allBanks = new HashSet<Bank>();

	public static synchronized Set<Bank> getAllBanks(){
		return allBanks;
	}
	
	public double getReserveRatio() {

		double reseveRatio = getReserves() / getDeposites();
		System.out.println(this.name + " calculating getReserveRatio: "
				+ reseveRatio + " reserves/deposits " + getReserves() + " "
				+ getDeposites());
		return reseveRatio;

	}

	public static CentralBank validateCommonCentralBank(Bank[] banks) {

		CentralBank centralBank = null;
		for (Bank bank : banks) {

			if (centralBank != null
					&& !centralBank.equals(bank.getCentralBank())) {
				throw new RuntimeException(
						"Cannot combine banks with a different central bank");
			}
			centralBank = bank.getCentralBank();
		}
		return centralBank;

	}

	public static double getCombinedExcessReserves(Bank[] banks) {

		// Make sure all the banks share the same central bank
		CentralBank centralBank = validateCommonCentralBank(banks);

		return calculateExcessReserves(getCombinedDeposits(banks),
				getCombinedReserves(banks),
				centralBank.getRequiredReserveRatio());

	}

	public static double getCombinedReserveRatio(Bank[] banks) {

		return getCombinedReserves(banks) / getCombinedDeposits(banks);

	}

	public static double getCombinedDeposits(Bank[] banks) {

		double combinedDeposites = 0.0;
		for (Bank bank : banks) {

			combinedDeposites += bank.getDeposites();

		}
		return combinedDeposites;

	}

	public static double getCombinedReserves(Bank[] banks) {

		double combinedReserves = 0.0;
		for (Bank bank : banks) {

			combinedReserves += bank.getReserves();

		}
		return combinedReserves;

	}

	/*
	 * Note excess reserves is the amount that can be lent out in cash or into a
	 * cheque cashed at another bank
	 */
	private static double calculateExcessReserves(double deposits,
			double reserves, double requiredReservRatio) {

		if (deposits == 0.0) {
			return reserves;
		}

		double excessReserves = reserves - (requiredReservRatio * deposits);

		return excessReserves;

	}

	public double getExcessReservesForInternalDepositLoan() {

		return MoneyAmount.roundDownToMoney(getExcessReserves()
				/ getCentralBank().getRequiredReserveRatio());

	}

	public double getExcessReservesForExternalLoan() {

		// Retain 1 dollar in so save on precision details
		return MoneyAmount.roundDownToMoney(getExcessReserves());

	}

	public double getExcessReserves() {

		double excessReserves = calculateExcessReserves(getDeposites(),
				getReserves(), getCentralBank().getRequiredReserveRatio());

		// Bit of a hack to get around stupid double precision
		if (Math.round(excessReserves) < 0) {
			throw new IllegalStateException(
					"Bank is below its required reserve ratio at "
							+ getReserveRatio());
		}

		return excessReserves;

	}

	private final CentralBank centralBank;

	public double getLiabilities(Currency currency) {

		// A bank's liabilities are its deposits plus any outstanding loans it
		// has taken out itself from
		// other banks
		return getDeposites() + super.getLiabilities(currency);
	}

	// For now only assume banks can make loans
	// Eventually may want to allow people/accountholders to make loans
	public synchronized double getAssets(Currency currency) {

		Double loanAssets = 0.0;
		for (Loan loan : getLoansIssued()) {

			loanAssets += loan.getOutstandingBalance();
		}

		return super.getAssets(currency) + loanAssets;
	}

	public synchronized Double getDeposites() {

		Double deposites = 0.0;

		for (Account account : getDepositAccounts()) {
			deposites += account.getBalance();
		}

		return MoneyAmount.roundToMoney(deposites);
	}

	public Double getReserves() {

		// Only reserves held in the banks main currency count towards reserves
		return MoneyAmount.roundToMoney(getReservesAtCentralBank()
				+ getCashReservesHeldInCurrency(this.getCentralBank()
						.getCurrency()));
	}

	public List<Account> getDepositAccounts() {

		List<Account> accounts = new ArrayList<Account>();
		for (Account account : Account.allAccounts) {

			if (account.getBank().equals(this)) {
				accounts.add(account);

			}

		}
		return accounts;

	}

	@Override
	public synchronized void depositCashInAccount(Account account, Double amount) {

		if (!account.getBank().equals(this)) {
			throw new IllegalStateException(
					"Bank can't operate on account not administered by the bank");
		}
		incrementCashReserves(account.getCurrency(), amount);
		account.modifyAccountBalance(amount);

		if (amount > account.getAccountHolder().getCashReservesHeldInCurrency(
				account.getCurrency())) {
			throw new IllegalArgumentException(
					"Account holder does not have enough cash reserves to pay this"
							+ " amount into the account.");
		}

		account.getAccountHolder().decrementCashReserves(account.getCurrency(),
				amount);
	}

	public synchronized void withdrawCash(Account account, double amount) {
		if (amount > account.getBalance()) {
			throw new IllegalArgumentException(
					"Withdrawal too large and no overdraft agreed for this account");
		}
		if (!account.getBank().equals(this)) {
			throw new IllegalStateException(
					"Bank can't operate on account not administered by the bank");
		}

		decrementCashReserves(account.getCurrency(), amount);

		account.modifyAccountBalance(-amount);
		account.getAccountHolder().incrementCashReserves(account.getCurrency(),
				amount);
	}

	public synchronized void modifyAccountBalance(Account account, double amount) {

		if (!account.getBank().equals(this)) {
			throw new IllegalStateException(
					"Bank can't operate on account not administered by the bank");
		}

		account.modifyAccountBalance(amount);

	}

	// This is the only way loans should be made in the system
	public void makeLoan(double amount, Date startDate, Date endDate,
			double interestRate, AccountHolder borrower,
			Account borrowerAccount, Currency currency) {

		if (!borrower.equals(borrowerAccount.getAccountHolder())) {
			throw new IllegalStateException(
					"Can't pay loan into account not belonging to borrower");
		}

		new Loan(amount, startDate, endDate, interestRate, borrower, this,
				borrowerAccount, currency);
	}

	public Account getCentralBankAccount() {
		for (Account account : getAccounts()) {

			if (account.getBank().equals(this.centralBank)) {
				return account;
			}
		}
		throw new IllegalStateException("Bank must have a central bank account");

	}

	public Double getReservesAtCentralBank() {

		return MoneyAmount.roundToMoney(getCentralBankAccount().getBalance());
	}

	public Account openAccount(AccountHolder accountHolder,
			Double interestRate, MoneyAmount moneyAmount) {

		Account account = new Account(this, interestRate, accountHolder,
				moneyAmount.currency);

		// Currently only cash can be used to open bank accounts
		depositCashInAccount(account, moneyAmount.amount);

		return account;
	}

	public List<Loan> getLoansIssued() {

		List<Loan> loans = new ArrayList<Loan>();
		for (Loan loan : Loan.allLoans) {

			if (loan.getLender().equals(this)) {
				loans.add(loan);
			}

		}
		return loans;

	}

	public Bank(String name, CentralBank centralBank, double openingCapital,
			double cashInCentralBank) {
		super(centralBank == null ? null : centralBank.getCurrency(),
				openingCapital, name);

		this.centralBank = centralBank;
		// Cash reserves at central bank do not earn interest
		if (centralBank != null) {
			centralBank.openAccount(this, 0.0, new MoneyAmount(
					cashInCentralBank, centralBank.getCurrency()));
		}

		this.id = bankCounter++;
		allBanks.add(this);
	}

	public CentralBank getCentralBank() {
		return centralBank;
	}

}
