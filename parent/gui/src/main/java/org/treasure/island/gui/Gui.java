package org.treasure.island.gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.treasure.island.gui.images.ImageCache;
import org.treasure.island.gui.images.ImageCache.IMAGE;
import org.treasure.island.model.AccountHolder;
import org.treasure.island.model.Bank;
import org.treasure.island.model.CentralBank;
import org.treasure.island.model.Currency;
import org.treasure.island.model.MoneyAmount;
import org.treasure.island.model.Person;
import org.treasure.island.model.Shop;

public class Gui {

	private static Gui instance;

	public static Gui getInstance() {
		
		if (instance==null){
			instance = new Gui();
		}
		
		return instance;
	}

	JFrame f = new JFrame("Treasure Island Economics");
	VerticalPanel panel = new VerticalPanel();
	
	private Gui() {

		this.f = new JFrame("Treasure Island Economics");
		this.f.setLayout(new FlowLayout());
		f.setBackground(Color.WHITE);
		//f.setSize(1500, 1000);

		panel = new VerticalPanel();
		//panel.setSize(15000, 1500);

	}

	
	
	public void render() {

		// Check the business model is still in tact
		try {

			// Create a JFrame, which is a Window with "decorations",
			// i.e.
			// title, border and close-button

			
			// Set a simple Layout Manager that arranges the contained
			// Components
			
			panel.removeAll();
			
			panel.add(renderBanks());

			HorizontalPanel clientsPanel = new HorizontalPanel();
			clientsPanel.add(renderShops());

			clientsPanel.add(renderPopulation());

			panel.add(clientsPanel);
			panel.add(renderMoney(100000));
			panel.add(renderMoney(10000));


			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			// Set the visibility as true, thereby displaying it
			f.setVisible(true);
			JScrollPane scrollBar = new JScrollPane(panel,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

			// the key here is setcontentpane rather than just adding the pane
			// to get the scroll bar
			f.setContentPane(scrollBar);

		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

	}



	private static class VerticalPanel extends WhitePanel {

		public VerticalPanel() {

			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBackground(Color.WHITE);
			
		}

	}

	private static class WhitePanel extends JPanel {

		public WhitePanel() {

			super();
			this.setBackground(Color.WHITE);
			this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		}

	}

	private static class HorizontalPanel extends WhitePanel {

		public HorizontalPanel() {

			super();
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setBackground(Color.WHITE);

		}

	}

	public static WhitePanel renderBanks() throws Exception {

		long start = System.currentTimeMillis();
		VerticalPanel mainPanel = new VerticalPanel();
		HorizontalPanel centralBanks = new HorizontalPanel();
		HorizontalPanel commercialBanks = new HorizontalPanel();

		mainPanel.add(centralBanks);
		mainPanel.add(commercialBanks);

		for (Bank bank : Bank.getAllBanks()) {

			if (bank instanceof CentralBank) {
				centralBanks.add(renderCentralBank(bank));
			} else {
				commercialBanks.add(renderBank(bank));
			}

		}
		System.out.println("rendering banks took "+(System.currentTimeMillis()-start));
		return mainPanel;
	}

	public static WhitePanel renderCentralBank(Bank bank) throws Exception {

		return renderBank(bank, 140, 140);

	}

	public static WhitePanel renderBank(Bank bank) throws Exception {

		// default dimensions of bank icon
		return renderBank(bank, 90, 90);

	}

	private static class WhiteLabel extends JLabel {

		public WhiteLabel(String s) {

			super(s);
			this.setBackground(Color.WHITE);
			this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		}

	}

	public static WhitePanel renderBank(Bank bank, int width, int height)
			throws Exception {

		VerticalPanel f = new VerticalPanel();

		JLabel label = new JLabel(bank.getName(), new ImageIcon(
		// ImageIO.read(new File("src/gui/images/bank2.jpg"))
				ImageCache.getImage(IMAGE.BANK).getScaledInstance(width,
						height, java.awt.Image.SCALE_SMOOTH)),
				SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);

		f.add(label);
		f.add(new WhiteLabel("Deposits:  "
				+ MoneyAmount.GBP_FORMAT().format(bank.getDeposites())));

		if (!(bank instanceof CentralBank)) {
			f.add(new WhiteLabel("Rserves:  "
					+ MoneyAmount.GBP_FORMAT().format(bank.getReserves())));
			f.add(new WhiteLabel("Reserve Ratio:  " + bank.getReserveRatio()));
			f.add(new WhiteLabel("Excess Reserves:  "
					+ MoneyAmount.GBP_FORMAT().format(bank.getExcessReserves())));
			f.add(renderAssets(bank));
			f.add(renderLiabilities(bank));

			// f.add(renderMoney(bank.getReserves()));
		}
		return f;

	}

	private static JLabel getMoneyImage(int scale) throws Exception {

		if (scale ==0){
			return new JLabel();
		}
		JLabel label = new JLabel(new ImageIcon(ImageCache
				.getImage(IMAGE.MONEY).getScaledInstance(scale, 3*scale,
						java.awt.Image.SCALE_SMOOTH)), SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);

		return label;
	}

	public static HorizontalPanel renderPopulation() throws Exception {

		long start = System.currentTimeMillis();
		HorizontalPanel f = new HorizontalPanel();
		for (Person person : Person.population) {
			f.add(renderPerson(person));
		}
		System.out.println("rendering population took "+(System.currentTimeMillis()-start));
		
		return f;
	}

	public static VerticalPanel renderPerson(Person person) throws Exception {

		long start = System.currentTimeMillis();
		VerticalPanel f = new VerticalPanel();
		
		HorizontalPanel personMoney = new HorizontalPanel();
		JLabel label = new JLabel(person.getName(), new ImageIcon(ImageCache
				.getImage(IMAGE.PERSON)), SwingConstants.CENTER);
		label.setVerticalTextPosition(SwingConstants.TOP);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		
		personMoney.add(label);
		//personMoney.add(renderMoney(person.getCashReservesHeldInCurrency(Currency.GBP)));
		
		f.add(personMoney);
		f.add(renderAssets(person));
		f.add(renderLiabilities(person));

		System.out.println("render person took: " + (System.currentTimeMillis()-start));
		return f;

	}

	public static HorizontalPanel renderShops() throws Exception {

		HorizontalPanel f = new HorizontalPanel();

		for (Shop shop : Shop.allShops) {
			f.add(renderShop(shop));
		}
		return f;

	}

	// TODO:Incorporate different ccy
	public static WhiteLabel renderAssets(AccountHolder accountHolder) {

		WhiteLabel label = new WhiteLabel(MoneyAmount.GBP_FORMAT().format(
				accountHolder.getAssets(Currency.GBP)));
		label.setForeground(Color.GREEN);

		return label;

	}

	public static WhiteLabel renderLiabilities(AccountHolder accountHolder) {

		WhiteLabel label = new WhiteLabel(MoneyAmount.GBP_FORMAT().format(
				accountHolder.getLiabilities(Currency.GBP)));
		label.setForeground(Color.RED);

		return label;

	}

	public static VerticalPanel renderShop(Shop shop) throws Exception {

		VerticalPanel f = new VerticalPanel();
		JLabel image = new JLabel(shop.getName(), new ImageIcon(ImageCache
				.getImage(IMAGE.SHOP)), SwingConstants.CENTER);
		image.setVerticalTextPosition(SwingConstants.TOP);
		image.setHorizontalTextPosition(SwingConstants.CENTER);
		f.add(image);
		f.add(renderAssets(shop));
		f.add(renderLiabilities(shop));

		return f;

	}

	public static VerticalPanel renderMoney(double amount) throws Exception {

		double scalingFactor = 0.001;
		
		VerticalPanel f = new VerticalPanel();
	
		f.add(getMoneyImage((int) (scalingFactor * amount)));
		
		f.add(new WhiteLabel(MoneyAmount.GBP_FORMAT().format(amount)));
	
		
		
		return f;
	}

}
