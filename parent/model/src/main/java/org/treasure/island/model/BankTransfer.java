package org.treasure.island.model;
import java.util.HashSet;
import java.util.Set;


/*
 *Lets try a new pattern here where the constructor regsiters the object and
 *also does the work 
 */
public class BankTransfer {

	public static Set<BankTransfer> allBankTransfers = new HashSet<BankTransfer>();
	private final int id;
	private static int bankTrasferCounter =0;
	public BankTransfer(Account from, Account to, double amount) {
		super();
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.id=bankTrasferCounter++;
		allBankTransfers.add(this);
		
		
		from.getBank().modifyAccountBalance(from, -amount);
		from.getBank().getCentralBankAccount().modifyAccountBalance(-amount);
		
		to.getBank().modifyAccountBalance(from, amount);
		from.getBank().getCentralBankAccount().modifyAccountBalance(amount);
		
		
	}

	
	
	
	private final Account from;
	
	
	private final Account to;
	
	public Account getFrom() {
		return from;
	}

	public Account getTo() {
		return to;
	}

	public double getAmount() {
		return amount;
	}

	private final double amount;
	
	
}
