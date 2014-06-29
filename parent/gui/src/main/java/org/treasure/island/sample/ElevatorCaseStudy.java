// ElevatorCaseStudy.java
// Application with Elevator Model, View, and Controller (MVC)
package org.treasure.island.sample;

// Java core packages
import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.treasure.island.sample.controller.ElevatorController;
import org.treasure.island.sample.model.ElevatorSimulation;
import org.treasure.island.sample.view.ElevatorView;

// Deitel packages

public class ElevatorCaseStudy extends JFrame {

   // model, view and controller
   private ElevatorSimulation model;
   private ElevatorView view;
   private ElevatorController controller;

   // constructor instantiates model, view, and controller
   public ElevatorCaseStudy()
   {
      super( "Deitel Elevator Simulation" );

      // instantiate model, view and ,controller
      model = new ElevatorSimulation();
      view = new ElevatorView();
      controller = new ElevatorController( model );

      // register View for Model events
      model.setElevatorSimulationListener( view );

      // add view and controller to ElevatorSimulation
      getContentPane().add( view, BorderLayout.CENTER );
      getContentPane().add( controller, BorderLayout.SOUTH );

   } // end ElevatorSimulation constructor

   // main method starts program
   public static void main( String args[] )
   {
      // instantiate ElevatorSimulation
      ElevatorCaseStudy caseStudy = new ElevatorCaseStudy();
      caseStudy.setDefaultCloseOperation( EXIT_ON_CLOSE );
      caseStudy.pack();
      caseStudy.setVisible( true );
   }
}


