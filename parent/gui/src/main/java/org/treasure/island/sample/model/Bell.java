// Bell.java
// Represents Bell in simulation
package org.treasure.island.sample.model;

// Deitel packages
import org.treasure.island.sample.event.BellEvent;
import org.treasure.island.sample.event.BellListener;
import org.treasure.island.sample.event.ElevatorMoveEvent;
import org.treasure.island.sample.event.ElevatorMoveListener;

public class Bell implements ElevatorMoveListener {

   // BellListener listens for BellEvent object
   private BellListener bellListener;

   // ring bell and send BellEvent object to listener
   private void ringBell( Location location )
   {
      if ( bellListener != null )
         bellListener.bellRang( 
            new BellEvent( this, location ) );
   }

   // set BellListener
   public void setBellListener( BellListener listener )
   {
      bellListener = listener;
   }

   // invoked when Elevator has departed
   public void elevatorDeparted( ElevatorMoveEvent moveEvent ) {}

   // invoked when Elevator has arrived
   public void elevatorArrived( ElevatorMoveEvent moveEvent )
   {
      ringBell( moveEvent.getLocation() );
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
