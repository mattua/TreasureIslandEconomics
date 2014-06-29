// Elevator.java
// Travels between Floors in the ElevatorShaft
package org.treasure.island.sample.model;

// Java core packages
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.treasure.island.sample.ElevatorConstants;
import org.treasure.island.sample.event.BellEvent;
import org.treasure.island.sample.event.BellListener;
import org.treasure.island.sample.event.ButtonEvent;
import org.treasure.island.sample.event.ButtonListener;
import org.treasure.island.sample.event.DoorEvent;
import org.treasure.island.sample.event.DoorListener;
import org.treasure.island.sample.event.ElevatorMoveEvent;
import org.treasure.island.sample.event.ElevatorMoveListener;

// Deitel packages

public class Elevator extends Location implements Runnable, 
   BellListener, ElevatorConstants {

   // manages Elevator thread
   private boolean elevatorRunning = false;

   // describes Elevator state (idle or moving)
   private boolean moving = false;

   // current Floor
   private Location currentFloorLocation;

   // destination Floor
   private Location destinationFloorLocation;
   
   // Elevator needs to service other Floor
   private boolean summoned;
   
   // listener objects
   private Set elevatorMoveListeners;
   private BellListener bellListener;
   private ButtonListener elevatorButtonListener;
   private DoorListener elevatorDoorListener;

   // ElevatorDoor, Button and Bell on Elevator
   private ElevatorDoor elevatorDoor;
   private Button elevatorButton;
   private Bell bell;

   public static final int ONE_SECOND = 1000;

   // time needed to travel between Floors (5 seconds)
   private static final int TRAVEL_TIME = 5 * ONE_SECOND;

   // Elevator's thread to handle asynchronous movement
   private Thread thread;

   // constructor creates variables; registers for ButtonEvents
   public Elevator( Floor firstFloor, Floor secondFloor )
   {
      setLocationName( ELEVATOR_NAME );

      // instantiate Elevator's Door, Button and Bell
      elevatorDoor = new ElevatorDoor();
      elevatorButton = new Button();
      bell = new Bell();

      // register Elevator for BellEvents
      bell.setBellListener( this );

      // instantiate listener Set
      elevatorMoveListeners = new HashSet( 1 );

      // start Elevator on first Floor
      currentFloorLocation = firstFloor;
      destinationFloorLocation = secondFloor;

      // register elevatorButton for ElevatorMoveEvents
      addElevatorMoveListener( elevatorButton );

      // register elevatorDoor for ElevatorMoveEvents
      addElevatorMoveListener( elevatorDoor );

      // register bell for ElevatorMoveEvents
      addElevatorMoveListener( bell );

      // anonymous inner class listens for ButtonEvents from
      // elevatorButton
      elevatorButton.setButtonListener( 
         new ButtonListener() {

            // invoked when elevatorButton has been pressed
            public void buttonPressed( ButtonEvent buttonEvent )
            {
               // send ButtonEvent to listener
               elevatorButtonListener.buttonPressed( 
                  buttonEvent );

               // start moving Elevator to destination Floor
               setMoving( true );
            }

            // invoked when elevatorButton has been reset
            public void buttonReset( ButtonEvent buttonEvent )
            {
               // send ButtonEvent to listener
               elevatorButtonListener.buttonReset( 
                  buttonEvent );
            }
         } // end anonymous inner class
      );

      // anonymous inner class listens for DoorEvents from
      // elevatorDoor
      elevatorDoor.addDoorListener( 
         new DoorListener() {

            // invoked when elevatorDoor has opened
            public void doorOpened( DoorEvent doorEvent )
            {
               // send DoorEvent to listener
               elevatorDoorListener.doorOpened( new DoorEvent( 
                  doorEvent.getSource(), Elevator.this ));
            }

            // invoked when elevatorDoor has closed
            public void doorClosed( DoorEvent doorEvent )
            {
               // send DoorEvent to listener
               elevatorDoorListener.doorClosed( new DoorEvent( 
                 doorEvent.getSource(), Elevator.this ));
            }
         } // end anonymous inner class
      );
   } // end Elevator constructor

   // swaps current Floor Location with opposite Floor Location
   private void changeFloors()
   {
      Location location = currentFloorLocation;
      currentFloorLocation = destinationFloorLocation;
      destinationFloorLocation = location;
   }

   // start Elevator thread
   public void start()
   {
      if ( thread == null )
         thread = new Thread( this );

      elevatorRunning = true;
      thread.start();
   }

   // stop Elevator thread; method run terminates
   public void stopElevator()
   {
      elevatorRunning = false;
   }

   // Elevator thread's run method
   public void run()
   {
      while ( isElevatorRunning() ) {
         
         // remain idle until awoken
         while ( !isMoving() )
            pauseThread( 10 );
         
         // pause while passenger exits (if one exists)
         pauseThread( ONE_SECOND );
         
         // close elevatorDoor
         getDoor().closeDoor( currentFloorLocation );         

         // closing Door takes one second
         pauseThread( ONE_SECOND );

         // issue elevatorDeparted Event
         sendDepartureEvent( currentFloorLocation );

         // Elevator needs 5 seconds to travel
         pauseThread( TRAVEL_TIME );

         // stop Elevator
         setMoving( false );

         // swap Floor Locations
         changeFloors();

         // issue elevatorArrived Event
         sendArrivalEvent( currentFloorLocation );

      } // end while loop

   } // end method run
   
   // pause concurrent thread for number of milliseconds
   private void pauseThread( int milliseconds )
   {
      try {
         Thread.sleep( milliseconds );
      }

      // handle if interrupted while sleeping
      catch ( InterruptedException exception ) {
         exception.printStackTrace();
      }
   } // end method pauseThread

   // return Button on Elevator
   public Button getButton()
   {
      return elevatorButton;
   }

   // return Door on Elevator
   public Door getDoor()
   {
      return elevatorDoor;
   }

   // set if Elevator should move
   private void setMoving( boolean elevatorMoving )
   {
      moving = elevatorMoving;
   }

   // is Elevator moving?
   public boolean isMoving()
   {
      return moving;
   }

   // is Elevator thread running?
   private boolean isElevatorRunning()
   {
      return elevatorRunning;
   }

   // register ElevatorMoveListener for ElevatorMoveEvents
   public void addElevatorMoveListener( 
      ElevatorMoveListener listener )
   {
      elevatorMoveListeners.add( listener );
   }

   // register BellListener fpr BellEvents
   public void setBellListener( BellListener listener )
   {
      bellListener = listener;
   }

   // register ButtonListener for ButtonEvents
   public void setButtonListener( ButtonListener listener )
   {
      elevatorButtonListener = listener;
   }

   // register DoorListener for DoorEvents
   public void setDoorListener( DoorListener listener )
   {
      elevatorDoorListener = listener;
   }

   // notify all ElevatorMoveListeners of arrival
   private void sendArrivalEvent( Location location )
   {
      // obtain iterator from Set
      Iterator iterator = elevatorMoveListeners.iterator();

      // get next DoorListener
      while ( iterator.hasNext() ) {

         // get next ElevatorMoveListener from Set
         ElevatorMoveListener listener = 
            ( ElevatorMoveListener ) iterator.next();

         // send event to listener
         listener.elevatorArrived( new 
            ElevatorMoveEvent( this, location ) );

      } // end while loop

      // service queued request, if one exists
      if ( summoned ) {
         setMoving( true ); // start moving Elevator
      }

      summoned = false; // request has been serviced

   } // end method sendArrivalEvent

   // notify all ElevatorMoveListeners of departure
   private void sendDepartureEvent( Location location )
   {
      // obtain iterator from Set
      Iterator iterator = elevatorMoveListeners.iterator();

      // get next DoorListener
      while ( iterator.hasNext() ) {

         // get next ElevatorMoveListener from Set
         ElevatorMoveListener listener = 
            ( ElevatorMoveListener ) iterator.next();

         // send ElevatorMoveEvent to this listener
         listener.elevatorDeparted( new ElevatorMoveEvent( 
            this, currentFloorLocation ) );

      } // end while loop
   } // end method sendDepartureEvent

   // request Elevator
   public void requestElevator( Location location )
   {
      // if Elevator is idle
      if ( !isMoving() ) {

         // if Elevator is on same Floor of request
         if ( location == currentFloorLocation )

            // Elevator has already arrived; send arrival event
            sendArrivalEvent( currentFloorLocation );

         // if Elevator is on opposite Floor of request
         else {
            setMoving( true ); // move to other Floor
         }
      }
      else // if Elevator is moving

         // if Elevator departed from same Floor as request
         if ( location == currentFloorLocation )
            summoned = true;
      
          // if Elevator is traveling to Floor of request,
          // simply continue traveling

   } // end method requestElevator

   // invoked when bell has rung
   public void bellRang( BellEvent bellEvent )
   {
      // send event to bellLirdstener
      if ( bellListener != null )
         bellListener.bellRang( bellEvent );
   }
   
   // get the currentFloorLocation of the Elevator 
   public Location getCurrentFloor()
   {
      return currentFloorLocation;
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
