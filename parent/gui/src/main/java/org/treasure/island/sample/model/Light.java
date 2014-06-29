// Light.java
// Light turns a light on or off
package org.treasure.island.sample.model;

// Deitel packages
import org.treasure.island.sample.event.ElevatorMoveEvent;
import org.treasure.island.sample.event.ElevatorMoveListener;
import org.treasure.island.sample.event.LightEvent;
import org.treasure.island.sample.event.LightListener;

public class Light implements ElevatorMoveListener {

   // Light state (on/off)
   private boolean lightOn;

   // time before Light turns off automatically (3 seconds)
   public static final int AUTOMATIC_TURNOFF_DELAY = 3000;

   // LightListener listens for when Light should turn on/off
   private LightListener lightListener;

   // location where Light turned on or off
   private Location lightLocation;

   // set LightListener
   public void setLightListener( LightListener listener )
   {
      lightListener = listener;
   }

   // turn on Light
   public void turnOnLight( Location location )
   {
      if ( !lightOn ) {

         lightOn = true;

         // send LightEvent to LightListener
         lightListener.lightTurnedOn( 
            new LightEvent( this, location ) );

         lightLocation = location;

         // declare Thread that ensures automatic Light turn off
         Thread thread = new Thread( 
            new Runnable() {

               public void run()
               {
                  // turn off Light if on for more than 3 seconds
                  try {
                     Thread.sleep( AUTOMATIC_TURNOFF_DELAY );
                     turnOffLight( lightLocation );
                 }

                  // handle exception if interrupted
                  catch ( InterruptedException exception ) {
                     exception.printStackTrace();
                  }
               }
            } // end anonymous inner class
         );

         thread.start();
      }
   } // end method turnOnLight

   // turn off Light
   public void turnOffLight( Location location )
   {
      if ( lightOn ) {

         lightOn = false;

         // send LightEvent to LightListener
         lightListener.lightTurnedOff( 
            new LightEvent( this, location ) );
      }
   } // end method turnOffLight

   // return whether Light is on or off
   public boolean isLightOn()
   {
      return lightOn;
   }

   // invoked when Elevator has departed
   public void elevatorDeparted( 
      ElevatorMoveEvent moveEvent )
   {
      turnOffLight( moveEvent.getLocation() );
   }

   // invoked when Elevator has arrived
   public void elevatorArrived( 
      ElevatorMoveEvent moveEvent )
   {
      turnOnLight( moveEvent.getLocation() );
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
