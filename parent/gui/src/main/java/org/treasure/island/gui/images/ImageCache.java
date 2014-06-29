package org.treasure.island.gui.images;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.SwingConstants;

public class ImageCache {

	//public static final String IMAGE_DIR = "src/main/java/org/treasure/island/gui/images/";
	
	
	public static enum IMAGE {

		BANK("bank2.jpg", 80), PERSON("person.jpg", 20), MONEY("bill2.JPG",100), CENTRAL_BANK(
				"bank2.jpg",80), SHOP("shop.jpg",60);

		public String getPathToImage() {
			return imageName;
		}

		private final String imageName;
		private final int scale;

		private IMAGE(String imageName, int scale) {

			this.imageName = imageName;
			this.scale = scale;
		}
	}

	public static Map<IMAGE, Image> cache = new HashMap<IMAGE, Image>();

	static {

		long start = System.currentTimeMillis();
		for (IMAGE entity : IMAGE.values()) {
			try {
				
				/*
				 * Interestingly, the non static getClass.getResource
				 * returns null so we have to use the static classloader
				 * resource. TODO: find out why this is the case
				 */
				URL url = ClassLoader.getSystemResource(entity.imageName);
				
				cache.put(
						entity,
						ImageIO.read(url)
								.getScaledInstance(entity.scale, entity.scale,
										SwingConstants.CENTER));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		System.out.println("Populating images took "
				+ (System.currentTimeMillis() - start));

	}

	public static Image getImage(IMAGE entity) {

		return cache.get(entity);

	}

}
