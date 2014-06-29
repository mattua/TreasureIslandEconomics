// ElevatorDoor.java
// Opens and closes floor Door when elevator arrives and departs.
package org.treasure.island.sample.model;

// Java core packages
import org.treasure.island.sample.event.ElevatorMoveEvent;
import org.treasure.island.sample.event.ElevatorMoveListener;

// Deitel packages

public class ElevatorDoor extends Door implements ElevatorMoveListener {

    // open ElevatorDoor and corresponding Floor Door
   public synchronized void openDoor( Location location )
   {    
      location.getDoor().openDoor( location );
      
      super.openDoor( location );
      
   } // end method openDoor

   // close ElevatorDoor and Corresponding Floor Door
   public synchronized void closeDoor( Location location )
   {      
      location.getDoor().closeDoor( location );
      
      super.closeDoor( location );
      
   } // end method closeDoor
   
   // invoked when Elevator has departed
   public void elevatorDeparted( ElevatorMoveEvent moveEvent ) {}

   // invoked when Elevator has arrived
   public void elevatorArrived( ElevatorMoveEvent moveEvent )
   {
      openDoor( moveEvent.getLocation() );
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
