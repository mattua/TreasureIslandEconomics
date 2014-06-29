// Floor.java
// Represents a Floor located next to an ElevatorShaft
package org.treasure.island.sample.model;

// Deitel packages
import org.treasure.island.sample.ElevatorConstants;

public class Floor extends Location 
   implements ElevatorConstants {

   // reference to ElevatorShaft object
   private ElevatorShaft elevatorShaft;

   // Floor constructor sets name of Floor
   public Floor( String name )
   {
      setLocationName( name );
   }

   // get first or second Floor Button, using Location name
   public Button getButton()
   {
      if ( getLocationName().equals( FIRST_FLOOR_NAME ) )
         return getElevatorShaft().getFirstFloorButton();
      else

         if ( getLocationName().equals( SECOND_FLOOR_NAME ) )
            return getElevatorShaft().getSecondFloorButton();
      else

         return null;

   } // end method getButton

   // get first or second Floor Door, using Location name
   public Door getDoor()
   {
      if ( getLocationName().equals( FIRST_FLOOR_NAME ) )
         return getElevatorShaft().getFirstFloorDoor();
      else 

         if ( getLocationName().equals( SECOND_FLOOR_NAME ) )
            return getElevatorShaft().getSecondFloorDoor();
      else

         return null;

   } // end method getDoor

   // get ElevatorShaft reference
   public ElevatorShaft getElevatorShaft()
   {
      return elevatorShaft;
   }

   // set ElevatorShaft reference
   public void setElevatorShaft( ElevatorShaft shaft )
   {
      elevatorShaft = shaft;
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
