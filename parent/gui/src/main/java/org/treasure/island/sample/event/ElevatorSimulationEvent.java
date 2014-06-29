// ElevatorSimulationEvent.java
// Basic event packet in Elevator simulation
package org.treasure.island.sample.event;

// Deitel packages
import org.treasure.island.sample.model.Location;

public class ElevatorSimulationEvent {

   // Location where ElevatorSimulationEvent was generated
   private Location location;
   
   // source Object that generated ElevatorSimulationEvent
   private Object source;

   // ElevatorModelEvent constructor sets Location
   public ElevatorSimulationEvent(Object source, Location location)
   {
      setSource( source );
      setLocation( location );
   }

   // set ElevatorSimulationEvent Location
   public void setLocation( Location eventLocation )
   {
      location = eventLocation;
   }

   // get ElevatorSimulationEvent Location
   public Location getLocation()
   {
      return location;
   }

   // set ElevatorSimulationEvent source
   private void setSource( Object eventSource )
   {
      source = eventSource;
   }

   // get ElevatorSimulationEvent source
   public Object getSource()
   {
      return source;
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
