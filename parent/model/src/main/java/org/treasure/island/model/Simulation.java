package org.treasure.island.model;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Simulation {

	

	public static void main(String[] args) throws Exception {

		final double riskFreeRate =0.01;
		
		
		
		DateFormat format = new SimpleDateFormat("ddMMMyyyy");
		Date from = format.parse("12Sep2011");
		Date to = format.parse("30Oct2011");
		assertEqualAndPrint(Loan.dayDiff(from, to),48.0, true);
		
		ForeignExchange.getInstance().setRate(Currency.GBP, 2.0);
		ForeignExchange.getInstance().setRate(Currency.EUR, 1.75);

		assertEqualAndPrint(
				ForeignExchange.getInstance().convert(2.0, Currency.GBP,
						Currency.USD), 4.0, true);
		assertEqualAndPrint(
				ForeignExchange.getInstance().convert(4.0, Currency.USD,
						Currency.GBP), 2.0, true);

		assertEqualAndPrint(
				ForeignExchange.getInstance().convert(2.0, Currency.EUR,
						Currency.USD), 3.5, true);
		assertEqualAndPrint(
				ForeignExchange.getInstance().convert(3.5, Currency.USD,
						Currency.EUR), 2.0, true);

		
		assertEqualAndPrint(
				ForeignExchange.getInstance().convert(2.0, Currency.GBP,
						Currency.EUR), 2.2857142857142856, true);
		assertEqualAndPrint(
				ForeignExchange.getInstance().convert(2.2857142857142856,
						Currency.EUR, Currency.GBP), 2.0, true);

		Country uk = new Country("UK", "Europe", 60000000);

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP), 0.0, true, "M1");

		CentralBank boe = new CentralBank("Bank of England", uk, 0.2, 0.1,
				Currency.GBP);

		Bank barclays = new Bank("Barclays", boe, 50000000, 100000);
		assertEqualAndPrintCurrency(barclays.getNetWorth(Currency.GBP), 5.0E7,
				true, "barclays.getNetWorth()");

		assert (barclays.getReservesAtCentralBank() == 100000);

		Bank hsbc = new Bank("HSBC", boe, 60000000, 200000);
		assert (barclays.getReservesAtCentralBank() == 100000);

		assert (hsbc.getReservesAtCentralBank() == 200000);

		Shop lovage = new Shop("Lovage", 40000, Currency.GBP);

		barclays.openAccount(lovage, 1.0,
				new MoneyAmount(10000.0, Currency.GBP));
		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP), 0.0, true, "M1");

		Person natasha = new Person("12Sep1979", "Natasha", 3000, Currency.GBP);

		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 3000.0);

		assertEqualAndPrint(barclays.getDeposites(), 10000.0);
		assertEqualAndPrint(
				barclays.getCashReservesHeldInCurrency(Currency.GBP),
				49910000.0);

		barclays.openAccount(natasha, 4.0, new MoneyAmount(50.0, Currency.GBP));
		assertEqualAndPrint(
				barclays.getCashReservesHeldInCurrency(Currency.GBP),
				49910050.0);

		assertEqualAndPrint(barclays.getDeposites(), 10050.0);

		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 2950.0);

		hsbc.openAccount(natasha, 3.0, new MoneyAmount(100.0, Currency.GBP));

		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 2850.0);

		assertEqualAndPrint(natasha.getNetWorth(Currency.GBP), 3000.0);

		assertEqualAndPrint(barclays.getNetWorth(Currency.GBP), 5.0E7);

		natasha.deposit(hsbc, 40.0);
		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 2810.0);
		assertEqualAndPrint(hsbc.getDeposites(), 140.0);

		//long start = System.currentTimeMillis();
		//Gui.getInstance().render();
		//System.out.println("Render took "+(System.currentTimeMillis()-start));
		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 2810.0);
		assertEqualAndPrint(lovage.getCashReservesHeldInCurrency(Currency.GBP),
				30000.0);
		assertEqualAndPrint(natasha.getNonCashAssetValue(Currency.GBP), 0.0,
				true);

		natasha.cashPurchase(lovage, new Goods("Curry", Goods.GoodsType.FOOD,
				31.0, Currency.GBP));

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP), 2969.0, true,
				"M1");
		
		

		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 2779.0);
		assertEqualAndPrint(lovage.getCashReservesHeldInCurrency(Currency.GBP),
				30031.0);
		assertEqualAndPrint(natasha.getNonCashAssetValue(Currency.GBP), 0.0);

		assertEqualAndPrint(barclays.getDeposites(), 10050.0);
		assertEqualAndPrint(hsbc.getDeposites(), 140.0);

		natasha.debitcardPurchase(hsbc, lovage, new Goods("Cobra",
				Goods.GoodsType.FOOD, 4.5, Currency.GBP));

		assertEqualAndPrint(barclays.getDeposites(), 10054.5);
		assertEqualAndPrint(hsbc.getDeposites(), 135.5);

		assertEqualAndPrint(
				natasha.getCashReservesHeldInCurrency(Currency.GBP), 2779.0,
				true);
		assertEqualAndPrint(lovage.getCashReservesHeldInCurrency(Currency.GBP),
				30031.0, true);
		assertEqualAndPrint(natasha.getNonCashAssetValue(Currency.GBP), 0.0,
				true);

		assertEqualAndPrint(natasha.getAccountByBank(barclays).getBalance(),
				50.0, true);
		assertEqualAndPrint(barclays.getReservesAtCentralBank(), 100004.5, true);
		assertEqualAndPrint(hsbc.getReservesAtCentralBank(), 199995.5, true);

		// Lets create a new bank
		Bank nationwide = new Bank("Nationwide", boe, 2000000000, 18887000.0);
		assertEqualAndPrint(nationwide.getReservesAtCentralBank(), 18887000.0,
				true);
		assertEqualAndPrint(nationwide.getReserves(), 2000000000.0, true);
		assertEqualAndPrint(
				nationwide.getCashReservesHeldInCurrency(Currency.GBP),
				1.981113E9, true);

		// Natasha has 50 in her barclays account

		// Create a new person
		Person sasha = new Person("12Sep1981", "Sasha", 4000, Currency.GBP);

		nationwide.openAccount(sasha, 1.0,
				new MoneyAmount(3000.0, Currency.GBP));

		assertEqualAndPrint(natasha.getNonCashAssetValue(Currency.GBP), 0.0,
				true);
		assertEqualAndPrint(sasha.getNonCashAssetValue(Currency.GBP), 0.0, true);

		Goods ferrari = new Goods("Ferrari", Goods.GoodsType.CAR, 2000.0,
				Currency.GBP);
		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP), 2.0E9, false,
				"M1");

		natasha.windfall(ferrari);
		assertEqualAndPrint(natasha.getNonCashAssetValue(Currency.GBP), 2000.0,
				true);

		// Sasha buys a car from Natasha for £2000 with a cheque
		assertEqualAndPrintCurrency(nationwide.getNetWorth(Currency.GBP),
				2.0E9, true, "nationwide.getNetWorth()");
		assertEqualAndPrintCurrency(nationwide.getDeposites(), 3000.0, true,
				"nationwide.getDeposits()");
		assertEqualAndPrintCurrency(nationwide.getReservesAtCentralBank(),
				1.8887E7, true, "nationwide.getReservesAtCentralBank()");

		assertEqualAndPrintCurrency(hsbc.getNetWorth(Currency.GBP), 6.0E7,
				true, "hsbc.getNetWorth()");
		assertEqualAndPrintCurrency(hsbc.getDeposites(), 135.5, true,
				"hsbc.getDeposites()");
		assertEqualAndPrintCurrency(hsbc.getReservesAtCentralBank(), 199995.5,
				true, "hsbc.getReservesAtCentralBank()");

		sasha.centrallyClearedChequePurchase(nationwide, hsbc, natasha, ferrari);

		assertEqualAndPrintCurrency(natasha.getNonCashAssetValue(Currency.GBP),
				0.0, true, "natasha.getNonCashAssetValue()");
		assertEqualAndPrintCurrency(natasha.getNetWorth(Currency.GBP), 4964.5,
				true, "natasha.getNetWorth()");
		assertEqualAndPrintCurrency(sasha.getNetWorth(Currency.GBP), 4000.0,
				true, "sasha.getNetWorth()");

		assertEqualAndPrintCurrency(sasha.getNonCashAssetValue(Currency.GBP),
				2000.0, true, "sasha.getNonCashAssetValue()");

		assertEqualAndPrintCurrency(nationwide.getNetWorth(Currency.GBP),
				2.0E9, true, "nationwide.getNetWorth()");
		assertEqualAndPrintCurrency(nationwide.getDeposites(), 1000.0, true,
				"nationwide.getDeposits()");
		assertEqualAndPrintCurrency(nationwide.getReservesAtCentralBank(),
				1.8885E7, true, "nationwide.getReservesAtCentralBank()");

		assertEqualAndPrintCurrency(hsbc.getNetWorth(Currency.GBP), 6.0E7,
				true, "hsbc.getNetWorth()");
		assertEqualAndPrintCurrency(hsbc.getDeposites(), 2135.5, true,
				"hsbc.getDeposites()");
		assertEqualAndPrintCurrency(hsbc.getReservesAtCentralBank(), 201995.5,
				true, "hsbc.getReservesAtCentralBank()");

		assertEqualAndPrintCurrency(nationwide.getExcessReserves(),
				2.0000009E9, true, "nationwide.getExcessReserves()");
		assertEqualAndPrintCurrency(hsbc.getExcessReserves(), "£60,001,921.95",
				true, "hsbc.getExcessReserves()");
		assertEqualAndPrintCurrency(barclays.getExcessReserves(),
				"£50,009,049.05", true, "barclays.getExcessReserves()");

		new Loan(100, new Date(), new Date(), 1.1, sasha, nationwide,
				sasha.getAccountByBank(nationwide), Currency.GBP);
		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP), 2.0E9, false,
				"M1");

		// Nationwide's excess reserves have come down due to the new loan
		assertEqualAndPrintCurrency(nationwide.getExcessReserves(),
				2.00000089E9, true, "nationwide.getExcessReserves()");
		assertEqualAndPrintCurrency(hsbc.getExcessReserves(), "£60,001,921.95",
				true, "hsbc.getExcessReserves()");
		assertEqualAndPrintCurrency(barclays.getExcessReserves(),
				"£50,009,049.05", true, "barclays.getExcessReserves()");

		assertEqualAndPrintCurrency(nationwide.getNetWorth(Currency.GBP),
				2.0E9, true, "nationwide.getNetWorth()");

		// Nationwide has 1 loan on its books
		assertEqualAndPrint(nationwide.getLoansIssued().size(), 1, true,
				"nationwide.getLoansIssued().size()");
		assertEqualAndPrintCurrency(nationwide.getReservesAtCentralBank(),
				1.8885E7, true, "nationwide.getReservesAtCentralBank()");

		// Nationwide's deposits have gone up
		assertEqualAndPrintCurrency(nationwide.getDeposites(), 1100.0, true,
				"nationwide.getDeposites()");

		// Natasha's net worth also stays the same - she owes the loan but has
		// the money
		assertEqualAndPrintCurrency(sasha.getNetWorth(Currency.GBP), 4000.0,
				true, "sasha.getNetWorth()");
		assertEqualAndPrintCurrency(sasha.getLoanLiabilities(), 100.0, true,
				"sasha.getLoanLiabilities()");

		// Now lets say Natasha takes out a loan from Nationwide and pays it
		// into hsbc

		assertEqualAndPrintCurrency(nationwide.getExcessReserves(),
				2.00000089E9, true, "nationwide.getExcessReserves()");
		assertEqualAndPrintCurrency(hsbc.getExcessReserves(), "£60,001,921.95",
				true, "hsbc.getExcessReserves()");
		assertEqualAndPrintCurrency(barclays.getExcessReserves(),
				"£50,009,049.05", true, "barclays.getExcessReserves()");

		new Loan(300, new Date(), new Date(), 1.1, natasha, nationwide,
				natasha.getAccountByBank(hsbc), Currency.GBP);
		// Now lets see what the excess reserves of the banks are
		assertEqualAndPrintCurrency(nationwide.getExcessReserves(),
				2.00000059E9, true, "nationwide.getExcessReserves()");
		assertEqualAndPrintCurrency(hsbc.getExcessReserves(), "£60,002,191.95",
				true, "hsbc.getExcessReserves()");
		assertEqualAndPrintCurrency(barclays.getExcessReserves(),
				"£50,009,049.05", true, "barclays.getExcessReserves()");

		assertEqualAndPrintCurrency(nationwide.getReserveRatio(),
				1818182.4545454546, true, "nationwide.getReserveRatio()");
		assertEqualAndPrintCurrency(hsbc.getReserveRatio(), 24636.59843974543,
				true, "hsbc.getReserveRatio()");
		assertEqualAndPrintCurrency(barclays.getReserveRatio(),
				4973.897707494157, true, "barclays.getReserveRatio()");
		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP), 2.0E9, false,
				"M1");

		assertEqualAndPrintCurrency(natasha.getLoanLiabilities(), 300.0, true,
				"natasha.getLoanLiabilities()");

		// Nationwides reserves have gone down
		assertEqualAndPrintCurrency(nationwide.getReservesAtCentralBank(),
				1.88847E7, true, "nationwide.getReservesAtCentralBank()");
		// DEposits are unchanged
		assertEqualAndPrintCurrency(nationwide.getDeposites(), 1100.0, true,
				"nationwide.getDeposits()");
		assertEqualAndPrint(nationwide.getLoansIssued().size(), 2, true,
				"nationwide.getLoansIssued().size()");
		// Nationwide networth is unchanged
		assertEqualAndPrintCurrency(nationwide.getNetWorth(Currency.GBP),
				2.0E9, true, "nationwide.getNetWorth()");
		assertEqualAndPrintCurrency(natasha.getNetWorth(Currency.GBP), 4964.5,
				true, "natasha.getNetWorth()");

		// HSBC reserves have gone up
		assertEqualAndPrintCurrency(hsbc.getReservesAtCentralBank(),
				2.022955E5, true, "hsbc.getReservesAtCentralBank()");
		// DEposits are up
		assertEqualAndPrintCurrency(hsbc.getDeposites(), 2435.5, true,
				"hsbc.getDeposits()");
		assertEqualAndPrint(hsbc.getLoansIssued().size(), 0, true,
				"hsbc.getLoansIssued().size()");

		// networth is unchanged
		assertEqualAndPrintCurrency(hsbc.getNetWorth(Currency.GBP), 6.0E7,
				true, "hsbc.getNetWorth()");

		// Let's bring nationwide into the limit of excess reserves
		// We'll add a huge loan to sasha

		assertEqualAndPrintCurrency(nationwide.getExcessReserves(),
				2.00000059E9, true, "nationwide.getExcessReserves()");
		assertEqualAndPrintCurrency(nationwide.getCashReservesHeldInCurrency(Currency.GBP),
				2.00000059E9, false, "nationwide.getCashReservesHeldInCurrency(Currency.GBP)");
		assertEqualAndPrintCurrency(nationwide.getReservesAtCentralBank(),
				2.00000059E9, false, "nationwide.getReservesAtCentralBank()");
		assertEqualAndPrintCurrency(nationwide.getReserves(),
				2.00000059E9, false, "nationwide.getReserves()");
		
		assertEqualAndPrintCurrency(hsbc.getExcessReserves(), "£60,002,191.95",
				true, "hsbc.getExcessReserves()");
		assertEqualAndPrintCurrency(barclays.getExcessReserves(),
				"£50,009,049.05", true, "barclays.getExcessReserves()");

		assertEqualAndPrint(nationwide.getReserveRatio(), 1818182.4545454546,
				true, "nationwide.getReserveRatio()");
		assertEqualAndPrint(hsbc.getReserveRatio(), 24636.59843974543, true,
				"hsbc.getReserveRatio()");
		assertEqualAndPrint(barclays.getReserveRatio(), 4973.897707494157,
				true, "barclays.getReserveRatio()");

		// Loan is made from nationwide to natasha and paid into barclays.
		// Excess reserves therefore
		// get dimished by the loan amount
		new Loan(2000000589, new Date(), new Date(), 1.1, natasha, nationwide,
				natasha.getAccountByBank(barclays), Currency.GBP);

		assertEqualAndPrintCurrency(nationwide.getExcessReserves(), "£1.00",
				true, "nationwide.getExcessReserves()");
		assertEqualAndPrintCurrency(hsbc.getExcessReserves(), "£60,002,191.95",
				true, "hsbc.getExcessReserves()");
		assertEqualAndPrintCurrency(barclays.getExcessReserves(),
				"£1,850,009,579.15", true, "barclays.getExcessReserves()");

		assertEqualAndPrint(nationwide.getReserveRatio(), 0.1009090909090909,
				true, "nationwide.getReserveRatio()");
		assertEqualAndPrint(hsbc.getReserveRatio(), 24636.59843974543, true,
				"hsbc.getReserveRatio()");

			
		// Barclays reserve ratio drops to close to 1 because it now has an
		// enormous deposit backed up
		// by a reserve of similar magnitude
		assertEqualAndPrint(barclays.getReserveRatio(), 1.024999866956958,
				true, "barclays.getReserveRatio()");

		// Lets create a load of people
		assertEqualAndPrint(Person.population.size(), 2, true,
				"Person.population.size()");
		assertEqualAndPrint(Account.allAccounts.size(), 7, true,
				"Account.allAccounts.size()");

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP),
				"£2,000,007,953.50", true, "M1");

		for (int i = 0; i < 10; i++) {
			Person p = new Person("12Sep1981", "Sasha " + i, 10000000 * i,
					Currency.GBP);

			// THIS DOESNT LOOK RIGHT - these new people don't have that much
			// money
			nationwide.openAccount(p, 1.0, new MoneyAmount(i * 3000000.0,
					Currency.GBP));

			System.out.println(p.getTotalCashReserves(Currency.GBP));

		}
		assertEqualAndPrint(Person.population.size(), 12, true,
				"Person.population.size()");
		assertEqualAndPrint(Account.allAccounts.size(), 17, true,
				"Account.allAccounts.size()");

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP),
				"£2,450,007,953.50", true, "M1");
		assertEqualAndPrintCurrency(MoneySupply.getM1ForSpecificCurrency(Currency.USD), 0.0, true, "M1");
		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.USD),
				"£4,900,015,907.00", true, "M1");

		// Now create a new bank

		Person p1 = new Person("12Sep1981", "p1", 100, Currency.GBP);
		Person p2 = new Person("12Sep1981", "p2", 100, Currency.GBP);
		Person p3 = new Person("12Sep1981", "p3", 100, Currency.GBP);

		Bank b1 = new Bank("b1", boe, 1000.0, 0.0);
		Bank b2 = new Bank("b2", boe, 1000.0, 0.0);
		Bank b3 = new Bank("b3", boe, 1000.0, 0.0);
		b1.openAccount(p1, 1.0, new MoneyAmount(1.0, Currency.GBP));
		b1.openAccount(p2, 1.0, new MoneyAmount(0.0, Currency.GBP));
		b1.openAccount(p3, 1.0, new MoneyAmount(0.0, Currency.GBP));
		b2.openAccount(p1, 1.0, new MoneyAmount(1.0, Currency.GBP));
		b2.openAccount(p2, 1.0, new MoneyAmount(0.0, Currency.GBP));
		b2.openAccount(p3, 1.0, new MoneyAmount(0.0, Currency.GBP));
		b3.openAccount(p1, 1.0, new MoneyAmount(1.0, Currency.GBP));
		b3.openAccount(p2, 1.0, new MoneyAmount(0.0, Currency.GBP));
		b3.openAccount(p3, 1.0, new MoneyAmount(0.0, Currency.GBP));
		System.out.println("************");

		assertEqualAndPrint(b1.getReserveRatio(), 1001.0, true,
				"b1.getReserveRatio()");
		assertEqualAndPrint(b2.getReserveRatio(), 1001.0, true,
				"b2.getReserveRatio()");
		assertEqualAndPrint(b3.getReserveRatio(), 1001.0, true,
				"b3.getReserveRatio()");
		System.out.println("************");

		assertEqualAndPrintCurrency(b1.getExcessReserves(), "£1,000.90", true,
				"b1.getExcessReserves()");
		assertEqualAndPrintCurrency(b2.getExcessReserves(), "£1,000.90", true,
				"b2.getExcessReserves()");
		assertEqualAndPrintCurrency(b3.getExcessReserves(), "£1,000.90", true,
				"b3.getExcessReserves()");

		// all 3 banks have 3 customers, the same huge reserve ratio.
		System.out.println("************");

		System.out.println("b1 lends 1000.90 to p2 who pays it into bank b2");
		new Loan(1000.9, new Date(), new Date(), 1.1, p1, b1,
				p1.getAccountByBank(b2), Currency.GBP);
		assertEqualAndPrintCurrency(
				Bank.getCombinedDeposits(new Bank[] { b1, b2, b3 }),
				"£1,003.90", true, "combined deposits b1,b2,b3");
		assertEqualAndPrint(
				Bank.getCombinedReserveRatio(new Bank[] { b1, b2, b3 }),
				2.9913337981870707, true, "combined reserve ratio b1,b2,b3");
		assertEqualAndPrintCurrency(
				Bank.getCombinedExcessReserves(new Bank[] { b1, b2, b3 }),
				"£2,902.61", true, "combined excess reserves b1,b2,b3");

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP),
				"£2,450,009,254.40", true, "M1");
		System.out.println("************");

		assertEqualAndPrint(b1.getReserveRatio(), 0.1, true,
				"b1.getReserveRatio()");
		assertEqualAndPrint(b2.getReserveRatio(), 1.9981036031540076, true,
				"b2.getReserveRatio()");
		assertEqualAndPrint(b3.getReserveRatio(), 1001.0, true,
				"b3.getReserveRatio()");
		System.out.println("************");

		assertEqualAndPrintCurrency(b1.getExcessReserves(), "£0.00", true,
				"b1.getExcessReserves()");
		assertEqualAndPrintCurrency(b2.getExcessReserves(), "£1,901.71", true,
				"b2.getExcessReserves()");
		assertEqualAndPrintCurrency(b3.getExcessReserves(), "£1,000.90", true,
				"b3.getExcessReserves()");
		System.out.println("************");

		// now the reverse happens and b2 lends the same amount into b1 account
		new Loan(1000.90, new Date(), new Date(), 1.1, p2, b2,
				p2.getAccountByBank(b1), Currency.GBP);
		assertEqualAndPrintCurrency(
				Bank.getCombinedDeposits(new Bank[] { b1, b2, b3 }),
				"£2,004.80", true, "combined deposits b1,b2,b3");
		assertEqualAndPrint(
				Bank.getCombinedReserveRatio(new Bank[] { b1, b2, b3 }),
				1.4979050279329609, true, "combined reserve ratio b1,b2,b3");
		assertEqualAndPrintCurrency(
				Bank.getCombinedExcessReserves(new Bank[] { b1, b2, b3 }),
				"£2,802.52", true, "combined excess reserves b1,b2,b3");

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP),
				"£2,450,010,255.30", true, "M1");

		assertEqualAndPrint(b1.getReserveRatio(), 0.9991017067571614, true,
				"b1.getReserveRatio()");
		assertEqualAndPrint(b2.getReserveRatio(), 0.9991017067571614, true,
				"b2.getReserveRatio()");
		assertEqualAndPrint(b3.getReserveRatio(), 1001.0, true,
				"b3.getReserveRatio()");

		assertEqualAndPrintCurrency(b1.getExcessReserves(), "£900.81", true,
				"b1.getExcessReserves()");
		assertEqualAndPrintCurrency(b2.getExcessReserves(), "£900.81", true,
				"b2.getExcessReserves()");
		assertEqualAndPrintCurrency(b3.getExcessReserves(), "£1,000.90", true,
				"b3.getExcessReserves()");
		System.out.println("************");

		// Wow, so by reciprocating the loan, both banks are now have excess
		// reserves of £900 and
		// reserve ratios of around 1

		System.out.println("b3 lends 5m by increasing p3 deposits in b3");
		new Loan(1000.90, new Date(), new Date(), 1.1, p3, b3,
				p3.getAccountByBank(b3), Currency.GBP);
		assertEqualAndPrintCurrency(
				Bank.getCombinedDeposits(new Bank[] { b1, b2, b3 }),
				"£3,005.70", true, "combined deposits b1,b2,b3");
		assertEqualAndPrint(
				Bank.getCombinedReserveRatio(new Bank[] { b1, b2, b3 }),
				0.9991017067571615, true, "combined reserve ratio b1,b2,b3");
		assertEqualAndPrintCurrency(
				Bank.getCombinedExcessReserves(new Bank[] { b1, b2, b3 }),
				"£2,702.43", true, "combined excess reserves b1,b2,b3");

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP),
				"£2,450,011,256.20", true, "M1");

		System.out.println("************");

		assertEqualAndPrint(b1.getReserveRatio(), 0.9991017067571614, true,
				"b1.getReserveRatio()");
		assertEqualAndPrint(b2.getReserveRatio(), 0.9991017067571614, true,
				"b2.getReserveRatio()");
		assertEqualAndPrint(b3.getReserveRatio(), 0.9991017067571614, true,
				"b3.getReserveRatio()");
		System.out.println("************");
		assertEqualAndPrintCurrency(b1.getExcessReserves(), "£900.81", true,
				"b1.getExcessReserves()");
		assertEqualAndPrintCurrency(b2.getExcessReserves(), "£900.81", true,
				"b2.getExcessReserves()");
		assertEqualAndPrintCurrency(b3.getExcessReserves(), "£900.81", true,
				"b3.getExcessReserves()");

		// Wow again, so b3 made the loan by creating deposits in its own
		// account and
		// has exactly the same rr and er as b1 and 2 having made the same loan
		// to each other
		assertEqualAndPrintCurrency(b3.getReserves(), "£1,001.00", true,
				"b3.getReserves()");

		// lets see how much debt we can create by increasing deposits in this
		// own account
		// OK lets start with the excess reserves
		new Loan(900.81, new Date(), new Date(), 1.1, p3, b3,
				p3.getAccountByBank(b3), Currency.GBP);

		assertEqualAndPrintCurrency(
				Bank.getCombinedDeposits(new Bank[] { b1, b2, b3 }),
				"£3,906.51", true, "combined deposits b1,b2,b3");
		assertEqualAndPrint(
				Bank.getCombinedReserveRatio(new Bank[] { b1, b2, b3 }),
				0.7687168342075151, true, "combined reserve ratio b1,b2,b3");
		assertEqualAndPrintCurrency(
				Bank.getCombinedExcessReserves(new Bank[] { b1, b2, b3 }),
				"£2,612.35", true, "combined excess reserves b1,b2,b3");

		assertEqualAndPrintCurrency(
				MoneySupply.getM1(Currency.GBP),
				"£2,450,012,157.01", true, "M1");
		assertEqualAndPrintCurrency(
				MoneySupply.getTotalBankLoans(Currency.GBP),
				"£2,450,012,158.01", false, "total bank loans");

		assertEqualAndPrint(b3.getReserveRatio(), 0.5260917323186403, true,
				"b3.getReserveRatio()");
		assertEqualAndPrintCurrency(b3.getExcessReserves(), "£810.73", true,
				"b3.getExcessReserves()");
		assertEqualAndPrintCurrency(b3.getReserves(), "£1,001.00", true,
				"b3.getReserves()");

		// wow, we still haven't hit the required reserve ratio and notice how
		// M1 is creeping up

		// TODO: output and test the total amount of debt that is being created
		// Lets create some fresh banks
		Bank b4 = new Bank("b4", boe, 1000.0, 1000.0);
		Bank b5 = new Bank("b5", boe, 1000.0,1000.0);
		Bank b6 = new Bank("b6", boe, 1000.0, 1000.0);
		b4.openAccount(p1, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b4.openAccount(p2, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b4.openAccount(p3, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b5.openAccount(p1, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b5.openAccount(p2, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b5.openAccount(p3, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b6.openAccount(p1, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b6.openAccount(p2, 0.0, new MoneyAmount(0.0, Currency.GBP));
		b6.openAccount(p3, 0.0, new MoneyAmount(0.0, Currency.GBP));
		final double loanRate=0.15;
		
		assertEqualAndPrintCurrency(b4.getDeposites(), "£0.00", true,
				"b4.getDeposites()");
		assertEqualAndPrintCurrency(b5.getDeposites(), "£0.00", true,
				"b5.getDeposites()");
		assertEqualAndPrintCurrency(b4.getExcessReserves(), "£1,000.00", true,
				"b4.getExcessReserves()");
		assertEqualAndPrintCurrency(b5.getExcessReserves(), "£1,000.00", true,
				"b5.getExcessReserves()");
		assertEqualAndPrintCurrency(
				Bank.getCombinedDeposits(new Bank[] { b4, b5 }), "£0.00", true,
				"combined deposits b4,b5");
		assertEqualAndPrintCurrency(
				Bank.getCombinedExcessReserves(new Bank[] { b4, b5 }),
				"£2,000.00", true, "combined excess reserves b4,b5");

		try {
			// This loan is too big for the bank which only had £1000 starting
			// capital
			new Loan(1000.90, new Date(), new Date(), 1.1, p1, b4,
					p1.getAccountByBank(b5), Currency.GBP);
			// Should never reach here
			assert (false);
		} catch (Exception e) {
			// Make sure the loan hasn't been added to the balance sheet of b4
			assert (b4.getAssets(Currency.GBP) == 1000.0);
		}

		Date loanStart = format.parse("12Sep2011");
		Date loanEnd = format.parse("12Oct2013");
		
		
		assertEqualAndPrintCurrency(
				exhaustiveLendingBetweenTwoBanks(b4, b5, p1,loanStart,loanEnd,loanRate,riskFreeRate), "£19,999.80",
				true, "money Supply created Afterb4b5Exchange");

		// Now lets model the behaviour of b6 which just keeps all the deposits
		// to itself

		assertEqualAndPrintCurrency(exhaustiveLendingWithinOneBank(b6, p1,loanStart,loanEnd),
				"£9,999.99", true,
				"money Supply created from internal b6 loans");

		// TODO: now model what happens if someone turns up at bank b4 with 100
		// cash and it is recursively lent
		// between b4 and b5 vs what happens when they do the same at b6 and
		// it's all kept internally
		p1.deposit(b4, 50.0);

	
		
		assertEqualAndPrintCurrency(
				exhaustiveLendingBetweenTwoBanks(b4, b5, p1,loanStart,loanEnd,loanRate,riskFreeRate), "£450.08",
				true, "money Supply created Afterb4b5Exchange");

		// This enables the banking system to created 450 of new loans, with the original 50
		// still a deposit
		
		// Now lets see what the present value of all the loans created is, and see how much 
		// profit the banking sector has made
		
		assertEqualAndPrintCurrency(
				Loan.getPVOfAllLoansIssueInCurrency(new Bank[]{b4,b5},loanStart,0.02,Currency.GBP), "£17,455.49",
				true, "PV of all loans issued by b4,b5");
		
		assertEqualAndPrintCurrency(
				Loan.getPVOfAllLoansIssueInCurrency(new Bank[]{b4,b5},loanStart,0.01,Currency.GBP), "£21,575.81",
				true, "PV of all loans issued by b4,b5");
		
		// THIS IS THE ACTUAL VALUE OF THE INTEREST + PRINCIPAL OF THE LOANS
		assertEqualAndPrintCurrency(
				Loan.getPVOfAllLoansIssueInCurrency(new Bank[]{b4,b5},loanStart,0.0,Currency.GBP), "£26,840.47",
				true, "PV of all loans issued by b4,b5");
		
		// THIS IS THE PRINCIPAL ONLY, SO NOW WE CAN SEE THE DIFF BETWEEN INTEREST AND PRINCIPAL
		assertEqualAndPrintCurrency(
				Loan.getFaceValueOfLoansIssueInCurrency(new Bank[]{b4,b5},loanStart,Currency.GBP), "£20,449.88",
				true, "PV of all loans issued by b4,b5");
		
		
		/*
		 * Next steps:
		 * 
		 * 1)Read CFA chapter on central banks and model the IMF as a leaf bank (i.e. which
		 * has no children)
		 * 
		 * 2)Look into interbank loans
		 * 
		 * 3) Base rate and what effect that has on the money supply
		 * 
		 * 4) General OO refactoring- esp in the loan constructor - should move central bank adjustment into bank
		 * 
		 * 
		 * 5) model loan re/pre payments and why the system depends on other loans being created
		 *  
		 * 6) Banks DO get interest on their deposits at the central bank
		 * 
		 * 7) central banks lend at a rate and pay deposits at a lower rate - banks always lend to each other within this band
		 * 
		 */
		
		
		
		
		
		
	}

	public static double exhaustiveLendingWithinOneBank(Bank b6,
			AccountHolder p1,Date loanStart,Date loanEnd) {

		double initialMoneySupply = MoneySupply.getM1(
				Currency.GBP);

		boolean b6max = false;
		while (!b6max) {

			
			// b4 makes a loan of its maximum reserves to b5
			if (b6.getExcessReservesForInternalDepositLoan() == 0.0) {
				b6max = true;
			} else {
				new Loan(b6.getExcessReservesForInternalDepositLoan()/2,
						loanStart, loanEnd,0.07, p1, b6,
						p1.getAccountByBank(b6), Currency.GBP);
				
			}

		}

		return MoneySupply.getM1(Currency.GBP)
				- initialMoneySupply;

	}

	public static double exhaustiveLendingBetweenTwoBanks(Bank b4, Bank b5,
			AccountHolder p1,Date loanStart,Date loanEnd,double loanRate,double riskFreeRate) {

		double initialMoneySupplyBeforeExchange = MoneySupply.getM1(Currency.GBP);

		// We go on a loop of lending to see how much money we can create by
		// exchanging
		// maximum reserves between banks

		boolean b4max = false;
		boolean b5max = false;

		double extraMoneyCreated=0.0;
		double loansIssued = 0.0;
		while (!(b4max && b5max)) {

			assertEqualAndPrintCurrency(b4.getReserves(), "£1,000.00", false,
					"b4.getReserves()");
			assertEqualAndPrintCurrency(b4.getDeposites(), "£1,000.00", false,
					"b4.getDeposites()");
			assertEqualAndPrintCurrency(b4.getExcessReserves(), "£1,000.00", false,
					"b4.getExcessReserves()");
			assertEqualAndPrintCurrency(b5.getReserves(), "£1,000.00", false,
					"b5.getReserves()");
			assertEqualAndPrintCurrency(b5.getDeposites(), "£1,000.00", false,
					"b5.getDeposites()");
			assertEqualAndPrintCurrency(b5.getExcessReserves(), "£1,000.00", false,
					"b5.getExcessReserves()");
			// b4 makes a loan of its maximum reserves to b5
			double loanAmount = b4.getExcessReservesForExternalLoan();
			if (loanAmount == 0.0) {
				b4max = true;
			} else {
				b4max = false;
				
				loansIssued+=loanAmount;
				Loan l=new Loan(loanAmount, loanStart,
						loanEnd,loanRate, p1, b4, p1.getAccountByBank(b5),
						Currency.GBP);
				System.out.println("---------------------------------making loan of "+loanAmount);
				System.out.println("------------------------------PV "+l.getPresentValue(loanStart, riskFreeRate));
				System.out.println("-------------------------------------loans issued "+loansIssued);
				
				
			}
			assertEqualAndPrintCurrency(b4.getReserves(), "£1,000.00", false,
					"b4.getReserves()");
			assertEqualAndPrintCurrency(b4.getDeposites(), "£1,000.00", false,
					"b4.getDeposites()");
			assertEqualAndPrintCurrency(b4.getExcessReserves(), "£1,000.00", false,
					"b4.getExcessReserves()");
			assertEqualAndPrintCurrency(b5.getReserves(), "£1,000.00", false,
					"b5.getReserves()");
			assertEqualAndPrintCurrency(b5.getDeposites(), "£1,000.00", false,
					"b5.getDeposites()");
			assertEqualAndPrintCurrency(b5.getExcessReserves(), "£1,000.00", false,
					"b5.getExcessReserves()");
			
			
			// b5 makes a maximum loan of its own excess reserves which gets
			// paid back into b4
			loanAmount = b5.getExcessReservesForExternalLoan();
			if (loanAmount == 0.0) {
				b5max = true;
			} else {
				b5max = false;
				System.out.println("---------------------------------making loan of "+loanAmount);
				loansIssued+=loanAmount;
				new Loan(loanAmount, loanStart,
						loanEnd,loanRate, p1, b5, p1.getAccountByBank(b4),
						Currency.GBP);
				System.out.println("-------------------------------------loans issued "+loansIssued);
				
				
			}
			assertEqualAndPrintCurrency(b4.getReserves(), "£1,000.00", false,
					"b4.getReserves()");
			assertEqualAndPrintCurrency(b4.getDeposites(), "£1,000.00", false,
					"b4.getDeposites()");
			assertEqualAndPrintCurrency(b4.getExcessReserves(), "£1,000.00", false,
					"b4.getExcessReserves()");
			assertEqualAndPrintCurrency(b5.getReserves(), "£1,000.00", false,
					"b5.getReserves()");
			assertEqualAndPrintCurrency(b5.getDeposites(), "£1,000.00", false,
					"b5.getDeposites()");
			assertEqualAndPrintCurrency(b5.getExcessReserves(), "£1,000.00", false,
					"b5.getExcessReserves()");

			extraMoneyCreated= MoneySupply.getM1(Currency.GBP)
					- initialMoneySupplyBeforeExchange;
			System.out.println("created "+extraMoneyCreated);
		}

		return extraMoneyCreated;
	}

	public static void assertEqualAndPrint(Object toPrint, Object expectedValue) {
		assertEqualAndPrint(toPrint, expectedValue, true, "output: ");

	}

	public static void assertEqualAndPrint(String label, Object toPrint,
			Object expectedValue) {
		assertEqualAndPrint(toPrint, expectedValue, true, label);

	}

	public static void assertEqualAndPrint(Object toPrint,
			Object expectedValue, boolean test) {
		assertEqualAndPrint(toPrint, expectedValue, test, "output: ");
	}

	public static void assertEqualAndPrint(Object toPrint,
			Object expectedValue, boolean test, String label) {

		System.out.println(label + ": " + toPrint);

		if (test)
			assert (toPrint.equals(expectedValue));

	}

	public static void assertEqualAndPrintCurrency(Double toPrint,
			String expectedValue, boolean test, String label) {

		System.out.println(label + ": " + MoneyAmount.GBP_FORMAT().format(toPrint));

		if (test)
			assert (MoneyAmount.GBP_FORMAT().format(toPrint).equals(expectedValue));

	}

	public static void assertEqualAndPrintCurrency(Double toPrint,
			Object expectedValue, boolean test, String label) {

		System.out.println(label + ": " + MoneyAmount.GBP_FORMAT().format(toPrint));

		if (test)
			assert (toPrint.equals(expectedValue));

	}

}
