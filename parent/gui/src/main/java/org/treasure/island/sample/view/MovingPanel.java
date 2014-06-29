// MovingPanel.java
// JPanel subclass with on-screen moving capabilities
package org.treasure.island.sample.view;

// Java core packages
import java.util.Iterator;

public class MovingPanel extends ImagePanel {

   // should MovingPanel change position?
   private boolean moving;

   // number of pixels MovingPanel moves in both x and y values
   // per animationDelay milliseconds
   private double xVelocity;
   private double yVelocity;

   // constructor initializes position, velocity and image
   public MovingPanel( int identifier, String imageName )
   {
      super( identifier, imageName );

      // set MovingPanel velocity
      xVelocity = 0;
      yVelocity = 0;

   } // end MovingPanel constructor

   // update MovingPanel position and animation frame
   public void animate()
   {
      // update position according to MovingPanel velocity
      if ( isMoving() ) {
         double oldXPosition = getPosition().getX();
         double oldYPosition = getPosition().getY();

         setPosition( oldXPosition + xVelocity,
            oldYPosition + yVelocity );
      }

      // update all children of MovingPanel
      Iterator iterator = getChildren().iterator();

      while ( iterator.hasNext() ) {
         MovingPanel panel = ( MovingPanel ) iterator.next();
         panel.animate();
      }
   } // end method animate

   // is MovingPanel moving on screen?
   public boolean isMoving()
   {
      return moving;
   }

   // set MovingPanel to move on screen
   public void setMoving( boolean move )
   {
      moving = move;
   }

   // set MovingPanel x and y velocity
   public void setVelocity( double x, double y )
   {
      xVelocity = x;
      yVelocity = y;
   }

   // return MovingPanel x velocity
   public double getXVelocity()
   {
      return xVelocity;
   }

   // return MovingPanel y velocity
   public double getYVelocity()
   {
      return yVelocity;
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
