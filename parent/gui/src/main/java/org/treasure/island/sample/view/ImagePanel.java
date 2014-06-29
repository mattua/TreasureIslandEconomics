// ImagePanel.java
// JPanel subclass for positioning and displaying ImageIcon
package org.treasure.island.sample.view;

// Java core packages
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

   // identifier
   private int ID;

   // on-screen position
   private Point2D.Double position;

   // imageIcon to paint on screen
   private ImageIcon imageIcon;

   // stores all ImagePanel children
   private Set panelChildren;

   // constructor initializes position and image
   public ImagePanel( int identifier, String imageName )
   {
      super( null ); // specify null layout
      setOpaque( false ); // make transparent

      // set unique identifier
      ID = identifier;

      // set location
      position = new Point2D.Double( 0, 0 );
      setLocation( 0, 0 );

      // create ImageIcon with given imageName
      imageIcon = new ImageIcon(
         getClass().getResource( imageName ) );

      Image image = imageIcon.getImage();
      setSize( 
         image.getWidth( this ), image.getHeight( this ) );

      // create Set to store Panel children
      panelChildren = new HashSet();

   } // end ImagePanel constructor

   // paint Panel to screen
   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );

      // if image is ready, paint it to screen
      imageIcon.paintIcon( this, g, 0, 0 );
   }

   // add ImagePanel child to ImagePanel
   public void add( ImagePanel panel )
   {
      panelChildren.add( panel );
      super.add( panel );
   }

   // add ImagePanel child to ImagePanel at given index
   public void add( ImagePanel panel, int index )
   {
      panelChildren.add( panel );
      super.add( panel, index );
   }

   // remove ImagePanel child from ImagePanel
   public void remove( ImagePanel panel )
   {
      panelChildren.remove( panel );
      super.remove( panel );
   }

   // sets current ImageIcon to be displayed
   public void setIcon( ImageIcon icon )
   {
      imageIcon = icon;
   }

   // set on-screen position
   public void setPosition( double x, double y )
   {
      position.setLocation( x, y );
      setLocation( ( int ) x,  ( int ) y );
   }

   // return ImagePanel identifier
   public int getID()
   {
      return ID;
   }

   // get position of ImagePanel
   public Point2D.Double getPosition()
   {
      return position;
   }

   // get imageIcon
   public ImageIcon getImageIcon()
   {
      return imageIcon;
   }

   // get Set of ImagePanel children
   public Set getChildren()
   {
      return panelChildren;
   }
}


 /**************************************************************************
 * (C) Copyright 1992-2003 by Deitel & Associates, Inc. and               *
 * Prentice Hall. All Rights Reserved.                                    *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
