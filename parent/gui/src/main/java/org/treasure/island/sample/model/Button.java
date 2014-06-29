// Button.java
// Sends ButtonEvents to ButtonListeners when accessed
package org.treasure.island.sample.model;

// Deitel packages
import org.treasure.island.sample.event.*;

public class Button implements ElevatorMoveListener {

   // ButtonListener
   private ButtonListener buttonListener = null;

   // represent whether Button is pressed
   private boolean pressed = false;

   // set listener
   public void setButtonListener( ButtonListener listener )
   {
      buttonListener = listener;
   }

   // press Button and send ButtonEvent
   public void pressButton( Location location )
   {
      pressed = true;

      buttonListener.buttonPressed( 
         new ButtonEvent( this, location ) );
   }

   // reset Button and send ButtonEvent
   public void resetButton( Location location )
   {
      pressed = false;

      buttonListener.buttonReset( 
         new ButtonEvent( this, location ) );
   }

   // return whether button is pressed
   public boolean isButtonPressed()
   {
      return pressed;
   }

   // invoked when Elevator has departed
   public void elevatorDeparted( ElevatorMoveEvent moveEvent ) {}

   // invoked when Elevator has arrived
   public void elevatorArrived( ElevatorMoveEvent moveEvent )
   {
      resetButton( moveEvent.getLocation() );
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
