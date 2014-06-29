// ElevatorShaft.java
// Represents elevator shaft, which contains elevator
package org.treasure.island.sample.model;

// Java core packages
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.treasure.island.sample.event.BellEvent;
import org.treasure.island.sample.event.BellListener;
import org.treasure.island.sample.event.ButtonEvent;
import org.treasure.island.sample.event.ButtonListener;
import org.treasure.island.sample.event.DoorEvent;
import org.treasure.island.sample.event.DoorListener;
import org.treasure.island.sample.event.ElevatorMoveEvent;
import org.treasure.island.sample.event.ElevatorMoveListener;
import org.treasure.island.sample.event.LightEvent;
import org.treasure.island.sample.event.LightListener;

// Deitel packages

public class ElevatorShaft implements ElevatorMoveListener,
   LightListener, BellListener {

   // Elevator
   private Elevator elevator;

   // Buttons on Floors
   private Button firstFloorButton;
   private Button secondFloorButton;

   // Doors on Floors
   private Door firstFloorDoor;
   private Door secondFloorDoor;

   // Lights on Floors
   private Light firstFloorLight;
   private Light secondFloorLight;

   // listeners
   private DoorListener doorListener;
   private ButtonListener buttonListener;
   private LightListener lightListener;
   private BellListener bellListener;
   private Set elevatorMoveListeners;

   // constructor initializes aggregated components
   public ElevatorShaft( Floor firstFloor, Floor secondFloor )
   {
      // instantiate Set for ElevatorMoveListeners
      elevatorMoveListeners = new HashSet( 1 );

      // anonymous inner class listens for ButtonEvents
      ButtonListener floorButtonListener = 
         new ButtonListener() {

         // called when Floor Button has been pressed
         public void buttonPressed( ButtonEvent buttonEvent )
         {
            // request elevator move to location
            Location location = buttonEvent.getLocation();
            buttonListener.buttonPressed( buttonEvent );
            elevator.requestElevator( location );
         }

         // called when Floor Button has been reset
         public void buttonReset( ButtonEvent buttonEvent ) 
         {
            buttonListener.buttonReset( buttonEvent );
         }
      }; // end anonymous inner class

      // instantiate Floor Buttons
      firstFloorButton = new Button();
      secondFloorButton = new Button();

      // register anonymous ButtonListener with Floor Buttons
      firstFloorButton.setButtonListener( 
         floorButtonListener );
      secondFloorButton.setButtonListener( 
         floorButtonListener );

      // Floor Buttons listen for ElevatorMoveEvents
      addElevatorMoveListener( firstFloorButton );
      addElevatorMoveListener( secondFloorButton );

      // anonymous inner class listens for DoorEvents
      DoorListener floorDoorListener = new DoorListener() {

         // called when Floor Door has opened
         public void doorOpened( DoorEvent doorEvent )
         {
            // forward event to doorListener
            doorListener.doorOpened( doorEvent );
         }

         // called when Floor Door has closed
         public void doorClosed( DoorEvent doorEvent )
         {
            // forward event to doorListener
            doorListener.doorClosed( doorEvent );
         }
      }; // end anonymous inner class

      // instantiate Floor Doors
      firstFloorDoor = new Door();
      secondFloorDoor = new Door();

      // register anonymous DoorListener with Floor Doors
      firstFloorDoor.addDoorListener( floorDoorListener );
      secondFloorDoor.addDoorListener( floorDoorListener );

      // instantiate Lights, then listen for LightEvents
      firstFloorLight = new Light();
      addElevatorMoveListener( firstFloorLight );
      firstFloorLight.setLightListener( this );
      
      secondFloorLight = new Light();
      addElevatorMoveListener( secondFloorLight );
      secondFloorLight.setLightListener( this );

      // instantiate Elevator object
      elevator = new Elevator( firstFloor, secondFloor );

      // register for ElevatorMoveEvents from elevator
      elevator.addElevatorMoveListener( this );

      // listen for BellEvents from elevator
      elevator.setBellListener( this );

      // anonymous inner class listens for ButtonEvents from
      // elevator
      elevator.setButtonListener( 
         new ButtonListener() {

            // invoked when button has been pressed
            public void buttonPressed( ButtonEvent buttonEvent )
            {
               // send event to listener
               buttonListener.buttonPressed( buttonEvent );
            }

            // invoked when button has been reset
            public void buttonReset( ButtonEvent buttonEvent )
            {
               // send event to listener
               buttonListener.buttonReset( 
                  new ButtonEvent( this, elevator ) );
            }
         } // end anonymous inner class
      );

      // anonymous inner class listens for DoorEvents from
      // elevator
      elevator.setDoorListener( 
         new DoorListener() {

            // invoked when door has opened
            public void doorOpened( DoorEvent doorEvent )
            {
               // send event to listener
               doorListener.doorOpened( doorEvent );
            }

            // invoked when door has closed
            public void doorClosed( DoorEvent doorEvent )
            {
               // send event to listener
               doorListener.doorClosed( doorEvent );
            }
         } // end anonymous inner class
      );

      // start Elevator Thread
      elevator.start();

   } // end ElevatorShaft constructor

   // get Elevator
   public Elevator getElevator()
   {
      return elevator;
   }

   // get Door on first Floor
   public Door getFirstFloorDoor()
   {
      return firstFloorDoor;
   }

   // get Door on second Floor
   public Door getSecondFloorDoor()
   {
      return secondFloorDoor;
   }

   // get Button on first Floor
   public Button getFirstFloorButton()
   {
      return firstFloorButton;
   }

   // get Button on second Floor
   public Button getSecondFloorButton()
   {
      return secondFloorButton;
   }

   // get Light on first Floor
   public Light getFirstFloorLight()
   {
      return firstFloorLight;
   }

   // get Light on second Floor
   public Light getSecondFloorLight()
   {
      return secondFloorLight;
   }

   // invoked when Bell rings
   public void bellRang( BellEvent bellEvent )
   {
      bellListener.bellRang( bellEvent );
   }

   // invoked when Light turns on
   public void lightTurnedOn( LightEvent lightEvent )
   {
      lightListener.lightTurnedOn( lightEvent );
   }

   // invoked when Light turns off
   public void lightTurnedOff( LightEvent lightEvent )
   {
      lightListener.lightTurnedOff( lightEvent );
   }

   // invoked when Elevator departs
   public void elevatorDeparted( ElevatorMoveEvent moveEvent )
   {
      Iterator iterator = elevatorMoveListeners.iterator();

      // iterate Set of ElevatorMoveEvent listeners
      while ( iterator.hasNext() ) {

         // get respective ElevatorMoveListener from Set
         ElevatorMoveListener listener = 
            ( ElevatorMoveListener ) iterator.next();

         // send ElevatorMoveEvent to this listener
         listener.elevatorDeparted( moveEvent );
      }
   } // end method elevatorDeparted

   // invoked when Elevator arrives
   public void elevatorArrived( ElevatorMoveEvent moveEvent )
   {
      // obtain iterator from Set
      Iterator iterator = elevatorMoveListeners.iterator();

      // get next DoorListener
      while ( iterator.hasNext() ) {

         // get next ElevatorMoveListener from Set
         ElevatorMoveListener listener = 
            ( ElevatorMoveListener ) iterator.next();

         // send ElevatorMoveEvent to this listener
         listener.elevatorArrived( moveEvent );

      } // end while loop
   } // end method elevatorArrived

   // set listener to DoorEvents
   public void setDoorListener( DoorListener listener )
   {
      doorListener = listener;
   }

   // set listener to ButtonEvents
   public void setButtonListener( ButtonListener listener )
   {
      buttonListener = listener;
   }

   // add listener to ElevatorMoveEvents
   public void addElevatorMoveListener( 
      ElevatorMoveListener listener )
   {
      elevatorMoveListeners.add( listener );
   }

   // set listener to LightEvents
   public void setLightListener( LightListener listener )
   {
      lightListener = listener;
   }

   // set listener to BellEvents
   public void setBellListener( BellListener listener )
   {
      bellListener = listener;
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
