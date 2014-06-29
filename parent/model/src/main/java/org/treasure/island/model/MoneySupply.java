package org.treasure.island.model;
public class MoneySupply {

	private static MoneySupply instance;

	public static MoneySupply getInstance() {
		if (instance == null) {
			instance = new MoneySupply();
		}
		return instance;
	}

	private MoneySupply() {

	}

	public static Double getTotalBankLoans(Currency currency) {

		double loans = 0.0;
		for (Loan loan : Loan.allLoans) {
			if (!(loan.getLender() instanceof CentralBank)
					&& loan.getCurrency().equals(currency)) {
				loans += loan.getOutstandingBalance();
			}
		}
		return loans;
	}

	public static double getM1ForSpecificCurrency(Currency currency) {

		double m1 = 0.0;
		for (Person person : Person.population) {

			m1 += person.getCashReservesHeldInCurrency(currency);

			for (Account account : Account.allAccounts) {
				if (account.getAccountHolder().equals(person)
						&& account.getCurrency().equals(currency)) {
					m1 += account.getBalance();
				}

			}

		}
		return m1;

	}

	public static double getM1(Currency currency) {

		double m1 = 0.0;
		for (Person person : Person.population) {

			m1 += person.getTotalCashReserves(currency);

			for (Account account : Account.allAccounts) {
				if (account.getAccountHolder().equals(person)) {
					m1 += ForeignExchange.getInstance().convert(
							account.getBalance(), account.getCurrency(),
							currency);
				}

			}

		}
		return m1;

	}

	public double getM2() {
		return 0.0;
	}

}
