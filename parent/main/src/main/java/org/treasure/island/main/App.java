package org.treasure.island.main;

import javax.swing.SwingUtilities;

import org.treasure.island.gui.Gui;
import org.treasure.island.model.Simulation;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws Exception {

		Simulation.main(null);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Gui.getInstance().render();
			}
		});
		
	
	}
}
