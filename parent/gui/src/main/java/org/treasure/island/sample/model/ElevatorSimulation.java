// ElevatorSimulation.java
// Elevator simulation model with ElevatorShaft and two Floors
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
import org.treasure.island.sample.event.ElevatorSimulationListener;
import org.treasure.island.sample.event.LightEvent;
import org.treasure.island.sample.event.LightListener;
import org.treasure.island.sample.event.PersonMoveEvent;
import org.treasure.island.sample.event.PersonMoveListener;

// Deitel packages

public class ElevatorSimulation implements ElevatorSimulationListener,
   ElevatorConstants {

   // declare two-Floor architecture in simulation
   private Floor firstFloor;
   private Floor secondFloor;

   // ElevatorShaft in simulation
   private ElevatorShaft elevatorShaft;

   // objects listening for events from ElevatorModel
   private Set personMoveListeners;
   private DoorListener doorListener;
   private ButtonListener buttonListener;
   private LightListener lightListener;
   private BellListener bellListener;
   private ElevatorMoveListener elevatorMoveListener;

   // cumulative number of people in simulation
   private int numberOfPeople = 0;

   // constructor instantiates ElevatorShaft and Floors
   public ElevatorSimulation()
   {
      // instantiate firstFloor and secondFloor objects
      firstFloor = new Floor( FIRST_FLOOR_NAME );
      secondFloor = new Floor( SECOND_FLOOR_NAME );

      // instantiate ElevatorShaft object
      elevatorShaft =
         new ElevatorShaft( firstFloor, secondFloor );

      // give elevatorShaft reference to first and second Floor
      firstFloor.setElevatorShaft( elevatorShaft );
      secondFloor.setElevatorShaft( elevatorShaft );

      // register for events from ElevatorShaft
      elevatorShaft.setDoorListener( this );
      elevatorShaft.setButtonListener( this );
      elevatorShaft.addElevatorMoveListener( this );
      elevatorShaft.setLightListener( this );
      elevatorShaft.setBellListener( this );

      // instantiate Set for ElevatorMoveListener objects
      personMoveListeners = new HashSet( 1 );

   } // end ElevatorModel constructor

   // return Floor with given name
   private Floor getFloor( String name )
   {
      if ( name.equals( FIRST_FLOOR_NAME ) )
         return firstFloor;
      else

         if ( name.equals( SECOND_FLOOR_NAME ) )
            return secondFloor;
         else
            return null;

   } // end method getFloor

   // add Person to Elevator Simulator
   public void addPerson( String floorName )
   {
      // instantiate new Person and place on Floor
      Person person = 
         new Person( numberOfPeople, getFloor( floorName ) );
      person.setName( Integer.toString( numberOfPeople ) );

      // register listener for Person events
      person.setPersonMoveListener( this );

      // start Person thread
      person.start();

      // increment number of Person objects in simulation
      numberOfPeople++;

   } // end method addPerson

   // invoked when Elevator has departed from Floor
   public void elevatorDeparted( ElevatorMoveEvent moveEvent )
   {
      elevatorMoveListener.elevatorDeparted( moveEvent );
   }

   // invoked when Elevator has arrived at destination Floor
   public void elevatorArrived( ElevatorMoveEvent moveEvent )
   {
      elevatorMoveListener.elevatorArrived( moveEvent );
   }

   // send PersonMoveEvent to listener, depending on event type
   private void sendPersonMoveEvent( 
      int eventType, PersonMoveEvent event )
   {
      Iterator iterator = personMoveListeners.iterator();

      while ( iterator.hasNext() ) {

         PersonMoveListener listener = 
            ( PersonMoveListener ) iterator.next();

         // send Event to this listener, depending on eventType
         switch ( eventType ) {

            // Person has been created
            case Person.PERSON_CREATED:
               listener.personCreated( event );
               break;

            // Person arrived at Elevator
            case Person.PERSON_ARRIVED:
               listener.personArrived( event );
               break;

            // Person entered Elevator
            case Person.PERSON_ENTERING_ELEVATOR:
               listener.personEntered( event );
               break;

            // Person pressed Button object
            case Person.PERSON_PRESSING_BUTTON:
               listener.personPressedButton( event );
               break;

            // Person exited Elevator
            case Person.PERSON_EXITING_ELEVATOR:
               listener.personDeparted( event );
               break;

            // Person exited simulation
            case Person.PERSON_EXITED:
               listener.personExited( event );
               break;

            default:
               break;
         }
      }
   } // end method sendPersonMoveEvent

   // invoked when Person has been created in model
   public void personCreated( PersonMoveEvent moveEvent )
   {
      sendPersonMoveEvent( Person.PERSON_CREATED, moveEvent );
   }

   // invoked when Person has arrived at Floor's Button
   public void personArrived( PersonMoveEvent moveEvent )
   {
      sendPersonMoveEvent( Person.PERSON_ARRIVED, moveEvent );
   }

   // invoked when Person has pressed Button
   public void personPressedButton( PersonMoveEvent moveEvent )
   {
      sendPersonMoveEvent( Person.PERSON_PRESSING_BUTTON, 
         moveEvent );
   }

   // invoked when Person has entered Elevator
   public void personEntered( PersonMoveEvent moveEvent )
   {
      sendPersonMoveEvent( Person.PERSON_ENTERING_ELEVATOR, 
         moveEvent );
   }

   // invoked when Person has departed from Elevator
   public void personDeparted( PersonMoveEvent moveEvent )
   {
      sendPersonMoveEvent( Person.PERSON_EXITING_ELEVATOR,
         moveEvent );
   }
  
   // invoked when Person has exited Simulation
   public void personExited( PersonMoveEvent moveEvent )
   {
      sendPersonMoveEvent( Person.PERSON_EXITED, moveEvent );
   }

   // invoked when Door has opened
   public void doorOpened( DoorEvent doorEvent )
   {
      doorListener.doorOpened( doorEvent );
   }

   // invoked when Door has closed
   public void doorClosed( DoorEvent doorEvent )
   {
      doorListener.doorClosed( doorEvent );
   }

   // invoked when Button has been pressed
   public void buttonPressed( ButtonEvent buttonEvent )
   {
      buttonListener.buttonPressed( buttonEvent );
   }

   // invoked when Button has been reset
   public void buttonReset( ButtonEvent buttonEvent )
   {
      buttonListener.buttonReset( buttonEvent );
   }

   // invoked when Bell has rung
   public void bellRang( BellEvent bellEvent )
   {
      bellListener.bellRang( bellEvent );
   }

   // invoked when Light has turned on
   public void lightTurnedOn( LightEvent lightEvent )
   {
      lightListener.lightTurnedOn( lightEvent );
   }

   // invoked when Light has turned off
   public void lightTurnedOff( LightEvent lightEvent )
   {
      lightListener.lightTurnedOff( lightEvent );
   }

   // set listener for ElevatorModelListener
   public void setElevatorSimulationListener( 
      ElevatorSimulationListener listener )
   {
      // ElevatorModelListener extends all interfaces below
      addPersonMoveListener( listener );
      setElevatorMoveListener( listener );
      setDoorListener( listener );
      setButtonListener( listener );
      setLightListener( listener );
      setBellListener( listener );
   }

   // set listener for PersonMoveEvents
   public void addPersonMoveListener(
      PersonMoveListener listener )
   {
      personMoveListeners.add( listener );
   }

   // set listener for DoorEvents
   public void setDoorListener( DoorListener listener )
   {
      doorListener = listener;
   }

   // set listener for ButtonEvents
   public void setButtonListener( ButtonListener listener )
   {
      buttonListener = listener;
   }

   // add listener for ElevatorMoveEvents
   public void setElevatorMoveListener(
      ElevatorMoveListener listener )
   {
      elevatorMoveListener = listener;
   }

   // set listener for LightEvents
   public void setLightListener( LightListener listener )
   {
      lightListener = listener;
   }

   // set listener for BellEvents
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
