// ElevatorController.java
// Controller for Elevator Simulation
package org.treasure.island.sample.controller;

// Java core packages
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.treasure.island.sample.ElevatorConstants;
import org.treasure.island.sample.event.PersonMoveEvent;
import org.treasure.island.sample.event.PersonMoveListener;
import org.treasure.island.sample.model.ElevatorSimulation;


public class ElevatorController extends JPanel
   implements ElevatorConstants {

   // controller contains two JButtons
   private JButton firstControllerButton;
   private JButton secondControllerButton;

   // reference to model
   private ElevatorSimulation elevatorSimulation;

   public ElevatorController( ElevatorSimulation simulation )
   {
      elevatorSimulation = simulation;
      setBackground( Color.white );

      // add first button to controller
      firstControllerButton = new JButton( "First Floor" );
      add( firstControllerButton );

      // add second button to controller
      secondControllerButton = new JButton( "Second Floor" );
      add( secondControllerButton );

      // anonymous inner class registers for ActionEvents from
      // first Controller JButton
      firstControllerButton.addActionListener( 
         new ActionListener() {

            // invoked when a JButton has been pressed
            public void actionPerformed( ActionEvent event )
            {
               // add Person to first Floor
               elevatorSimulation.addPerson( FIRST_FLOOR_NAME );

               // disable user input
               firstControllerButton.setEnabled( false );
            }
         } // end anonymous inner class
      );

      // anonymous inner class registers for ActionEvents from
      // second Controller JButton
      secondControllerButton.addActionListener( 
         new ActionListener() {

            // invoked when a JButton has been pressed
            public void actionPerformed( ActionEvent event )
            {
               // add Person to second Floor
               elevatorSimulation.addPerson( SECOND_FLOOR_NAME );

               // disable user input
               secondControllerButton.setEnabled( false );
            }
         } // end anonymous inner class
      );

      // anonymous inner class enables user input on Floor if
      // Person enters Elevator on that Floor
      elevatorSimulation.addPersonMoveListener( 
         new PersonMoveListener() {

            // invoked when Person has entered Elevator
            public void personEntered( 
               PersonMoveEvent event )
            {
               // get Floor of departure 
               String location =
                  event.getLocation().getLocationName();

               // enable first JButton if first Floor departure
               if ( location.equals( FIRST_FLOOR_NAME ) )
                  firstControllerButton.setEnabled( true );

               // enable second JButton if second Floor
               else
                  secondControllerButton.setEnabled( true );

            } // end method personEntered

            // other methods implementing PersonMoveListener
            public void personCreated( 
               PersonMoveEvent event ) {}

            public void personArrived( 
               PersonMoveEvent event ) {}

            public void personExited( 
               PersonMoveEvent event ) {}

            public void personDeparted( 
               PersonMoveEvent event ) {}

            public void personPressedButton( 
               PersonMoveEvent event ) {}

         } // end anonymous inner class
      );
   } // end ElevatorController constructor
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
