// Person.java
// Person riding the elevator
package org.treasure.island.sample.model;

// Java core packages
import org.treasure.island.sample.event.PersonMoveEvent;
import org.treasure.island.sample.event.PersonMoveListener;

// Deitel packages

public class Person extends Thread {

   // identification number
   private int ID = -1;

   // represents whether Person is moving or waiting
   private boolean moving;

   // reference to Location (either on Floor or in Elevator)
   private Location location;

   // listener object for PersonMoveEvents
   private PersonMoveListener personMoveListener;

   // time in milliseconds to walk to Button on Floor
   private static final int TIME_TO_WALK = 3000;

   // types of messages Person may send
   public static final int PERSON_CREATED = 1;
   public static final int PERSON_ARRIVED = 2;
   public static final int PERSON_ENTERING_ELEVATOR = 3;
   public static final int PERSON_PRESSING_BUTTON = 4;
   public static final int PERSON_EXITING_ELEVATOR = 5;
   public static final int PERSON_EXITED = 6;

   // Person constructor set initial location
   public Person( int identifier, Location initialLocation )
   {
      super();

      ID = identifier; // assign unique identifier
      location = initialLocation; // set Floor Location
      moving = true; // start moving toward Button on Floor
   }

   // set listener for PersonMoveEvents
   public void setPersonMoveListener(
      PersonMoveListener listener )
   {
      personMoveListener = listener;
   }

   // set Person Location
   private void setLocation( Location newLocation )
   {
      location = newLocation;
   }

   // get current Location
   private Location getLocation()
   {
      return location;
   }

   // get identifier
   public int getID()
   {
      return ID;
   }

   // set if Person should move
   public void setMoving( boolean personMoving )
   {
      moving = personMoving;
   }

   // get if Person should move
   public boolean isMoving()
   {
      return moving;
   }

   // Person either rides or waits for Elevator
   public void run()
   {
      // indicate that Person thread was created
      sendPersonMoveEvent( PERSON_CREATED );

      // walk to Elevator
      pauseThread( TIME_TO_WALK );
      
      // stop walking at Elevator
      setMoving( false );

      // Person arrived at Elevator
      sendPersonMoveEvent( PERSON_ARRIVED );    
      
      // get Door on current Floor
      Door currentFloorDoor = location.getDoor();
      
      // get Elevator
      Elevator elevator = 
         ( (Floor) getLocation() ).getElevatorShaft().getElevator();

      // begin exclusive access to currentFloorDoor
      synchronized ( currentFloorDoor ) {

         // check whether Floor Door is open
         if ( !currentFloorDoor.isDoorOpen() ) {

            sendPersonMoveEvent( PERSON_PRESSING_BUTTON );
            pauseThread( 1000 );

            // press Floor's Button to request Elevator
            Button floorButton = getLocation().getButton();
            floorButton.pressButton( getLocation() );
         }      

         // wait for Floor door to open
         try {

            while ( !currentFloorDoor.isDoorOpen() )
               currentFloorDoor.wait();
         }
         
         // handle exception waiting for Floor door to open
         catch ( InterruptedException interruptedException ) {
            interruptedException.printStackTrace();
         }            

         // Floor Door takes one second to open
         pauseThread( 1000 );   

         // implicitly wait for exclusive access to elevator
         synchronized ( elevator ) { 

            // Person enters Elevator
            sendPersonMoveEvent( PERSON_ENTERING_ELEVATOR );

            // set Person Location to Elevator
            setLocation( elevator );

            // Person takes one second to enter Elevator
            pauseThread( 1000 );      

            // pressing Elevator Button takes one second
            sendPersonMoveEvent( PERSON_PRESSING_BUTTON );
            pauseThread( 1000 );

            // get Elevator's Button
            Button elevatorButton = getLocation().getButton();

            // press Elevator's Button
            elevatorButton.pressButton( location );

            // Door closing takes one second
            pauseThread( 1000 );
         }    
         
      } // give up exclusive access to Floor door

      // get exclusive access to Elevator
      synchronized( elevator ) {
         
         // get Elevator door
         Door elevatorDoor = getLocation().getDoor();
         
         // wait for Elevator door to open
         synchronized( elevatorDoor ) {
 
            try {
               
               while ( !elevatorDoor.isDoorOpen() )
                  elevatorDoor.wait();
            }
            
            // handle exception waiting for Elevator door to open
            catch ( InterruptedException interruptedException ) {
               interruptedException.printStackTrace();
            }       
            
            // waiting for Elevator's Door to open takes a second
            pauseThread( 1000 );
            
            // move Person onto Floor
            setLocation( elevator.getCurrentFloor() );
            
            // walk away from Elevator
            setMoving( true );
            
            // Person exiting Elevator
            sendPersonMoveEvent( PERSON_EXITING_ELEVATOR );
            
         } // release elevatorDoor lock, allowing door to close
         
      } // release elevator lock, allowing waiting Person to enter

      // walking from elevator takes five seconds
      pauseThread( 2 * TIME_TO_WALK );

      // Person exits simulation
      sendPersonMoveEvent( PERSON_EXITED );    
      
   } // end method run
   
   // pause thread for desired number of milliseconds
   private void pauseThread( int milliseconds )
   {
      try {
         sleep( milliseconds );
      }

      // handle exception if interrupted when paused
      catch ( InterruptedException interruptedException ) {
         interruptedException.printStackTrace();
      }
   } // end method pauseThread

   // send PersonMoveEvent to listener, depending on event type
   private void sendPersonMoveEvent( int eventType )
   {
      // create new event
      PersonMoveEvent event = 
         new PersonMoveEvent( this, getLocation(), getID() );

      // send Event to this listener, depending on eventType
      switch ( eventType ) {

         // Person has been created
         case PERSON_CREATED:
            personMoveListener.personCreated( event );
            break;

         // Person arrived at Elevator
         case PERSON_ARRIVED:
            personMoveListener.personArrived( event );
            break;

         // Person entered Elevator
         case PERSON_ENTERING_ELEVATOR:
            personMoveListener.personEntered( event );
            break;

         // Person pressed Button object
         case PERSON_PRESSING_BUTTON:
            personMoveListener.personPressedButton( event );
            break;

         // Person exited Elevator
         case PERSON_EXITING_ELEVATOR:
            personMoveListener.personDeparted( event );
            break;

         // Person exited simulation
         case PERSON_EXITED:
            personMoveListener.personExited( event );
            break;

         default:
            break;
      }
   } // end method sendPersonMoveEvent
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
