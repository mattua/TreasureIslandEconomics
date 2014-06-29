// ElevatorView.java
// View for ElevatorSimulation
package org.treasure.island.sample.view;

// Java core packages
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import org.treasure.island.sample.ElevatorConstants;
import org.treasure.island.sample.event.BellEvent;
import org.treasure.island.sample.event.ButtonEvent;
import org.treasure.island.sample.event.DoorEvent;
import org.treasure.island.sample.event.ElevatorMoveEvent;
import org.treasure.island.sample.event.ElevatorSimulationListener;
import org.treasure.island.sample.event.LightEvent;
import org.treasure.island.sample.event.PersonMoveEvent;


public class ElevatorView extends JPanel 
   implements ActionListener, ElevatorSimulationListener,
      ElevatorConstants {

   // ElevatorView dimensions
   private static final int VIEW_WIDTH = 800;
   private static final int VIEW_HEIGHT = 435;

   // offset for positioning Panels in ElevatorView
   private static final int OFFSET = 10;

   // Elevator repaints components every 50 ms
   private static final int ANIMATION_DELAY = 50;

   // horizontal distance constants
   private static final int PERSON_TO_BUTTON_DISTANCE = 400;
   private static final int BUTTON_TO_ELEVATOR_DISTANCE = 50;
   private static final int PERSON_TO_ELEVATOR_DISTANCE = 
      PERSON_TO_BUTTON_DISTANCE + BUTTON_TO_ELEVATOR_DISTANCE;

   // times walking to Floor's Button and Elevator
   private static final int TIME_TO_BUTTON = 3000; // 3 seconds
   private static final int TIME_TO_ELEVATOR = 1000; // 1 second

   // time traveling in Elevator (5 seconds)
   private static final int ELEVATOR_TRAVEL_TIME = 5000;

   // Door images for animation
   private static final String doorFrames[] = {
      "images/door1.png", "images/door2.png", "images/door3.png",
      "images/door4.png", "images/door5.png" };

   // Person images for animation
   private static final String personFrames[] = { 
      "images/bug1.png", "images/bug2.png", "images/bug3.png", 
      "images/bug4.png", "images/bug5.png", "images/bug6.png",
      "images/bug7.png", "images/bug8.png" };

   // Light images for animation
   private static final String lightFrames[] = {
      "images/lightOff.png", "images/lightOn.png" };

   // Floor Light images for animation
   private static final String firstFloorLightFrames[] = {
      "images/firstFloorLightOff.png", 
      "images/firstFloorLightOn.png" };

   private static final String secondFloorLightFrames[] = {
      "images/secondFloorLightOff.png", 
      "images/secondFloorLightOn.png", };

   // Floor Button images for animation
   private static final String floorButtonFrames[] = { 
      "images/floorButtonUnpressed.png",
      "images/floorButtonPressed.png",
      "images/floorButtonLit.png" };

   // Elevator Button images for animation
   private static final String elevatorButtonFrames[] = {
      "images/elevatorButtonUnpressed.png",
      "images/elevatorButtonPressed.png",
      "images/elevatorButtonLit.png" };

   // Bell images for animation
   private static final String bellFrames[] = { 
      "images/bell1.png", "images/bell2.png", 
      "images/bell3.png" };

   private static final String floorImage = 
      "images/floor.png";
   private static final String ceilingImage = 
      "images/ceiling.png";
   private static final String elevatorImage = 
      "images/elevator.png";
   private static final String wallImage = 
      "images/wall.jpg";
   private static final String elevatorShaftImage = 
      "images/elevatorShaft.png";

   // audio files
   private static final String bellSound = "bell.wav";
   private static final String doorOpenSound = "doorOpen.wav";
   private static final String doorCloseSound = "doorClose.wav";
   private static final String elevatorSound = "elevator.au";
   private static final String buttonSound = "button.wav";
   private static final String walkingSound = "walk.wav";
   private static final String elevatorMusicSound = "liszt.mid";

   // ImagePanels for Floors, ElevatorShaft, wall and ceiling
   private ImagePanel firstFloorPanel;
   private ImagePanel secondFloorPanel;
   private ImagePanel elevatorShaftPanel;
   private ImagePanel wallPanel;
   private ImagePanel ceilingPanel;

   // MovingPanels for Elevator
   private MovingPanel elevatorPanel;

   // AnimatedPanels for Buttons, Bell, Lights and Door
   private AnimatedPanel firstFloorButtonPanel;
   private AnimatedPanel secondFloorButtonPanel;
   private AnimatedPanel elevatorButtonPanel;
   private AnimatedPanel bellPanel;
   private AnimatedPanel elevatorLightPanel;
   private AnimatedPanel firstFloorLightPanel;
   private AnimatedPanel secondFloorLightPanel;
   private AnimatedPanel doorPanel;

   // List containing AnimatedPanels for all Person objects
   private java.util.List personAnimatedPanels;

   // AudioClips for sound effects
   private AudioClip bellClip;
   private AudioClip doorOpenClip;
   private AudioClip doorCloseClip;
   private AudioClip elevatorClip;
   private AudioClip buttonClip;
   private AudioClip walkClip;

   // ElevatorMusic to play in Elevator
   private AudioClip elevatorMusicClip;

   // Timer for animation controller; 
   private javax.swing.Timer animationTimer;

   // distance from top of screen to display Floors
   private int firstFloorPosition;
   private int secondFloorPosition;

   // Elevator's velocity
   private double elevatorVelocity;

   // ElevatorView constructor
   public ElevatorView()
   {
      // specifiy null Layout
      super( null );

      instantiatePanels();
      placePanelsOnView();
      initializeAudio();

      // calculate distance Elevator travels
      double floorDistance = 
         firstFloorPosition - secondFloorPosition;

      // calculate time needed for travel
      double time = ELEVATOR_TRAVEL_TIME / ANIMATION_DELAY;

      // determine Elevator velocity (rate = distance / time)
      elevatorVelocity = ( floorDistance + OFFSET ) / time;

      // start animation Thread
      startAnimation();

   } // end ElevatorView constructor

   // instantiate all Panels (Floors, Elevator, etc.)
   private void instantiatePanels()
   {
      // instantiate ImagePanels representing Floors
      firstFloorPanel = new ImagePanel( 0, floorImage );
      secondFloorPanel = new ImagePanel( 0, floorImage );

      // calculate first and second Floor positions
      firstFloorPosition = 
         VIEW_HEIGHT - firstFloorPanel.getHeight();
      secondFloorPosition = 
         ( int ) ( firstFloorPosition / 2 ) - OFFSET;

      firstFloorPanel.setPosition( 0, firstFloorPosition );
      secondFloorPanel.setPosition( 0, secondFloorPosition );

      wallPanel = new ImagePanel( 0, wallImage );

      // create and position ImagePanel for ElevatorShaft
      elevatorShaftPanel = 
         new ImagePanel( 0, elevatorShaftImage );

      double xPosition = PERSON_TO_ELEVATOR_DISTANCE + OFFSET;
      double yPosition = 
         firstFloorPosition - elevatorShaftPanel.getHeight();

      elevatorShaftPanel.setPosition( xPosition, yPosition );

      // create and position ImagePanel for ceiling
      ceilingPanel = new ImagePanel( 0, ceilingImage );

      yPosition = elevatorShaftPanel.getPosition().getY() -
         ceilingPanel.getHeight();

      ceilingPanel.setPosition( xPosition, yPosition );

      // create and position MovingPanel for Elevator
      elevatorPanel = new MovingPanel( 0, elevatorImage );

      yPosition = firstFloorPosition - elevatorPanel.getHeight();

      elevatorPanel.setPosition( xPosition, yPosition );

      // create and position first Floor Button
      firstFloorButtonPanel = 
         new AnimatedPanel( 0, floorButtonFrames );

      xPosition = PERSON_TO_BUTTON_DISTANCE + 2 * OFFSET;
      yPosition = firstFloorPosition - 5 * OFFSET;
      firstFloorButtonPanel.setPosition( xPosition, yPosition );

      int floorButtonPressedFrameOrder[] = { 0, 1, 2 };
      firstFloorButtonPanel.addFrameSequence( 
         floorButtonPressedFrameOrder );

      // create and position second Floor Button
      secondFloorButtonPanel = 
         new AnimatedPanel( 1, floorButtonFrames );

      xPosition = PERSON_TO_BUTTON_DISTANCE + 2 * OFFSET;
      yPosition = secondFloorPosition - 5 * OFFSET;
      secondFloorButtonPanel.setPosition( xPosition, yPosition );

      secondFloorButtonPanel.addFrameSequence( 
         floorButtonPressedFrameOrder );

      // create and position Floor Lights
      firstFloorLightPanel = 
         new AnimatedPanel( 0, firstFloorLightFrames );

      xPosition = elevatorPanel.getLocation().x - 4 * OFFSET;
      yPosition = 
         firstFloorButtonPanel.getLocation().y - 10 * OFFSET;
      firstFloorLightPanel.setPosition( xPosition, yPosition );

      secondFloorLightPanel = 
         new AnimatedPanel( 1, secondFloorLightFrames );

      yPosition = 
         secondFloorButtonPanel.getLocation().y - 10 * OFFSET;
      secondFloorLightPanel.setPosition( xPosition, yPosition );

      // create and position Door AnimatedPanels
      doorPanel = new AnimatedPanel( 0, doorFrames );
      int doorOpenedFrameOrder[] = { 0, 1, 2, 3, 4 };
      int doorClosedFrameOrder[] = { 4, 3, 2, 1, 0 };
      doorPanel.addFrameSequence( doorOpenedFrameOrder );
      doorPanel.addFrameSequence( doorClosedFrameOrder );

      // determine where Door is located relative to Elevator
      yPosition = 
         elevatorPanel.getHeight() - doorPanel.getHeight();

      doorPanel.setPosition( 0, yPosition );

      // create and position Light AnimatedPanel
      elevatorLightPanel = new AnimatedPanel( 0, lightFrames );
      elevatorLightPanel.setPosition( OFFSET, 5 * OFFSET );

      // create and position Bell AnimatedPanel
      bellPanel = new AnimatedPanel( 0, bellFrames );

      yPosition = elevatorLightPanel.getPosition().getY() +
         elevatorLightPanel.getHeight() + OFFSET;

      bellPanel.setPosition( OFFSET, yPosition );
      int bellRingAnimation[] = { 0, 1, 0, 2 };
      bellPanel.addFrameSequence( bellRingAnimation );

      // create and position Elevator's Button AnimatedPanel
      elevatorButtonPanel = 
         new AnimatedPanel( 0, elevatorButtonFrames );

      yPosition = elevatorPanel.getHeight() - 6 * OFFSET;
      elevatorButtonPanel.setPosition( 10 * OFFSET, yPosition );

      int buttonPressedFrameOrder[] = { 0, 1, 2 };
      elevatorButtonPanel.addFrameSequence( 
         buttonPressedFrameOrder );

      // create List to store Person AnimatedPanels
      personAnimatedPanels = new ArrayList();

   } // end method instantiatePanels

   // place all Panels on ElevatorView
   private void placePanelsOnView()
   {
      // add Panels to ElevatorView
      add( firstFloorPanel );
      add( secondFloorPanel );
      add( ceilingPanel );
      add( elevatorPanel );
      add( firstFloorButtonPanel );
      add( secondFloorButtonPanel );
      add( firstFloorLightPanel );
      add( secondFloorLightPanel );
      add( elevatorShaftPanel );
      add( wallPanel );

      // add Panels to Elevator's MovingPanel
      elevatorPanel.add( doorPanel );
      elevatorPanel.add( elevatorLightPanel );
      elevatorPanel.add( bellPanel );
      elevatorPanel.add( elevatorButtonPanel );

   } // end method placePanelsOnView

   // get sound effects and elevatorMusic
   private void initializeAudio()
   {
      // create AudioClip sound effects from audio files
      SoundEffects sounds = new SoundEffects();
      sounds.setPathPrefix( "sounds/" );

      bellClip = sounds.getAudioClip( bellSound );
      doorOpenClip = sounds.getAudioClip( doorOpenSound );
      doorCloseClip = sounds.getAudioClip( doorCloseSound );
      elevatorClip = sounds.getAudioClip( elevatorSound );
      buttonClip = sounds.getAudioClip( buttonSound );
      walkClip = sounds.getAudioClip( walkingSound );
      elevatorMusicClip = sounds.getAudioClip( elevatorMusicSound );

   } // end method initializeAudio

   // starts animation by repeatedly drawing images to screen
   public void startAnimation()
   {
      if ( animationTimer == null ) {
         animationTimer = 
            new javax.swing.Timer( ANIMATION_DELAY, this );
         animationTimer.start();
      }
      else

         if ( !animationTimer.isRunning() )
            animationTimer.restart();
   }

   // stop animation
   public void stopAnimation()
   {
      animationTimer.stop();
   }

   // update AnimatedPanels animation in response to Timer
   public void actionPerformed( ActionEvent actionEvent )
   {
      elevatorPanel.animate();

      firstFloorButtonPanel.animate();
      secondFloorButtonPanel.animate();

      Iterator iterator = getPersonAnimatedPanelsIterator();

      while ( iterator.hasNext() ) {

         // get Person's AnimatedPanel from Set
         AnimatedPanel personPanel = 
            ( AnimatedPanel ) iterator.next();

         personPanel.animate(); // update panel
      }

      repaint(); // paint all Components

   } // end method actionPerformed

   private Iterator getPersonAnimatedPanelsIterator()
   {
      // obtain iterator from List
      synchronized( personAnimatedPanels )
      {
         return new ArrayList( personAnimatedPanels ).iterator();
      }
   }

   // stop sound clip of Person walking
   private void stopWalkingSound()
   {
      // stop playing walking sound
      walkClip.stop();

      Iterator iterator = getPersonAnimatedPanelsIterator();

      // but if Person is still walking, then keep playing
      while ( iterator.hasNext() ) {
         AnimatedPanel panel = ( AnimatedPanel ) iterator.next();

         if ( panel.getXVelocity() != 0 )
            walkClip.loop();
      }
   } // end method stopWalkingSound

   // returns Person AnimatedPanel with proper identifier
   private AnimatedPanel getPersonPanel( PersonMoveEvent event )
   {
      Iterator iterator = getPersonAnimatedPanelsIterator();

      while ( iterator.hasNext() ) {

         // get next AnimatedPanel
         AnimatedPanel personPanel = 
            ( AnimatedPanel ) iterator.next();
         
         // return AnimatedPanel with identifier that matches
         if ( personPanel.getID() == event.getID() )
            return personPanel;
      }

      // return null if no match with correct identifier
      return null;

   } // end method getPersonPanel

   // invoked when Elevator has departed from Floor
   public void elevatorDeparted( ElevatorMoveEvent moveEvent )
   {
      String location = 
         moveEvent.getLocation().getLocationName();

      // determine if Person is on Elevator
      Iterator iterator = getPersonAnimatedPanelsIterator();

      while ( iterator.hasNext() ) {

         AnimatedPanel personPanel =
            ( AnimatedPanel ) iterator.next();

         double yPosition = personPanel.getPosition().getY();
         String panelLocation;

         // determine on which Floor the Person entered
         if ( yPosition > secondFloorPosition )
            panelLocation = FIRST_FLOOR_NAME;
         else
            panelLocation = SECOND_FLOOR_NAME;

         int xPosition = 
            ( int ) personPanel.getPosition().getX();

         // if Person is inside Elevator
         if ( panelLocation.equals( location ) 
            && xPosition > PERSON_TO_BUTTON_DISTANCE + OFFSET ) {

            // remove Person AnimatedPanel from ElevatorView
            remove( personPanel );

            // add Person AnimatedPanel to Elevator
            elevatorPanel.add( personPanel, 1 );
            personPanel.setLocation( 2 * OFFSET, 9 * OFFSET );
            personPanel.setMoving( false );
            personPanel.setAnimating( false );
            personPanel.setVelocity( 0, 0 );
            personPanel.setCurrentFrame( 1 );
         }
      } // end while loop

      // determine Elevator velocity depending on Floor
      if ( location.equals( FIRST_FLOOR_NAME ) )
         elevatorPanel.setVelocity( 0, -elevatorVelocity );
      else

         if ( location.equals( SECOND_FLOOR_NAME ) )
            elevatorPanel.setVelocity( 0, elevatorVelocity );

      // begin moving Elevator and play Elevator music
      elevatorPanel.setMoving( true );

      if ( elevatorClip != null )
         elevatorClip.play();

      elevatorMusicClip.play();

   } // end method elevatorDeparted

   // invoked when Elevator has arrived at destination Floor
   public void elevatorArrived( ElevatorMoveEvent moveEvent )
   {
      // stop Elevator and music
      elevatorPanel.setMoving( false );
      elevatorMusicClip.stop();

      double xPosition = elevatorPanel.getPosition().getX();
      double yPosition;

      // set Elevator's position to either first or second Floor
      if ( elevatorPanel.getYVelocity() < 0 )
         yPosition = 
            secondFloorPosition - elevatorPanel.getHeight();
      else
         yPosition = 
            firstFloorPosition - elevatorPanel.getHeight();

      elevatorPanel.setPosition( xPosition, yPosition );

   } // end method elevatorArrived

   // invoked when Person has been created in model
   public void personCreated( PersonMoveEvent personEvent )
   {
      int personID = personEvent.getID();
 
      String floorLocation = 
         personEvent.getLocation().getLocationName();

      // create AnimatedPanel representing Person
      AnimatedPanel personPanel = 
         new AnimatedPanel( personID, personFrames );

      // determine where Person should be drawn initially
      // negative xPosition ensures Person drawn offscreen
      double xPosition = - personPanel.getWidth();
      double yPosition = 0;

      if ( floorLocation.equals( FIRST_FLOOR_NAME ) )
         yPosition = firstFloorPosition +
            ( firstFloorPanel.getHeight() / 2 );
      else

         if ( floorLocation.equals( SECOND_FLOOR_NAME ) )
            yPosition = secondFloorPosition + 
               ( secondFloorPanel.getHeight() / 2 );

      yPosition -= personPanel.getHeight();

      personPanel.setPosition( xPosition, yPosition );

      // add some animations for each Person
      int walkFrameOrder[] = { 1, 0, 1, 2 };
      int pressButtonFrameOrder[] = { 1, 3, 3, 4, 4, 1 };
      int walkAwayFrameOrder[] = { 6, 5, 6, 7 };
      personPanel.addFrameSequence( walkFrameOrder );
      personPanel.addFrameSequence( pressButtonFrameOrder );
      personPanel.addFrameSequence( walkAwayFrameOrder );

      // have Person begin walking to Elevator
      personPanel.playAnimation( 0 );
      personPanel.setLoop( true );
      personPanel.setAnimating( true );
      personPanel.setMoving( true );

      // determine Person velocity
      double time = 
         ( double ) ( TIME_TO_BUTTON / ANIMATION_DELAY );

      double xDistance = PERSON_TO_BUTTON_DISTANCE - 
         2 * OFFSET + personPanel.getSize().width;
      double xVelocity = xDistance / time;

      personPanel.setVelocity( xVelocity, 0 );
      personPanel.setAnimationRate( 1 );

      walkClip.loop(); // play sound clip of Person walking

      // store in personAnimatedPanels
      synchronized( personAnimatedPanels )
      {
         personAnimatedPanels.add( personPanel );
      }

      add( personPanel, 0 );

   } // end method personCreated

   // invoked when Person has arrived at Elevator
   public void personArrived( PersonMoveEvent personEvent )
   {
      // find Panel associated with Person that issued event
      AnimatedPanel panel = getPersonPanel( personEvent );

      if ( panel != null ) { // if Person exists

         // Person stops at Floor Button
         panel.setMoving( false );
         panel.setAnimating( false );
         panel.setCurrentFrame( 1 );
         stopWalkingSound();

         double xPosition = PERSON_TO_BUTTON_DISTANCE - 
            ( panel.getSize().width / 2 );
         double yPosition = panel.getPosition().getY();

         panel.setPosition( xPosition, yPosition );
      }
   } // end method personArrived

   // invoked when Person has pressed Button
   public void personPressedButton( PersonMoveEvent personEvent )
   {
      // find Panel associated with Person that issued event
      AnimatedPanel panel = getPersonPanel( personEvent );

      if ( panel != null ) { // if Person exists

         // Person stops walking and presses Button
         panel.setLoop( false );
         panel.playAnimation( 1 );

         panel.setVelocity( 0, 0 );
         panel.setMoving( false );
         panel.setAnimating( true );
         stopWalkingSound();
      }
   } // end method personPressedButton

   // invoked when Person has started to enter Elevator
   public void personEntered( PersonMoveEvent personEvent )
   {
      // find Panel associated with Person that issued event
      AnimatedPanel panel = getPersonPanel( personEvent );

      if ( panel != null ) {

         // determine velocity
         double time = TIME_TO_ELEVATOR / ANIMATION_DELAY;

         double distance = 
            elevatorPanel.getPosition().getX() - 
            panel.getPosition().getX() + 2 * OFFSET;

         panel.setVelocity( distance / time, -1.5 );

         // Person starts walking
         panel.setMoving( true );
         panel.playAnimation( 0 );
         panel.setLoop( true );
      }
   } // end method personEntered

   // invoked when Person has departed from Elevator
   public void personDeparted( PersonMoveEvent personEvent)
   {
      // find Panel associated with Person that issued event
      AnimatedPanel panel = getPersonPanel( personEvent );

      if ( panel != null ) { // if Person exists

         // determine velocity (in opposite direction)
         double time = TIME_TO_BUTTON / ANIMATION_DELAY;
         double xVelocity = - PERSON_TO_BUTTON_DISTANCE / time;

         panel.setVelocity( xVelocity, 0 );

         // remove Person from Elevator
         elevatorPanel.remove( panel );

         double xPosition = 
            PERSON_TO_ELEVATOR_DISTANCE + 3 * OFFSET;
         double yPosition = 0;

         String floorLocation = 
            personEvent.getLocation().getLocationName();

         // determine Floor onto which Person exits
         if ( floorLocation.equals( FIRST_FLOOR_NAME ) )
            yPosition = firstFloorPosition +
               ( firstFloorPanel.getHeight() / 2 );
         else

            if ( floorLocation.equals( SECOND_FLOOR_NAME ) )
               yPosition = secondFloorPosition + 
                  ( secondFloorPanel.getHeight() / 2 );

         yPosition -= panel.getHeight();

         panel.setPosition( xPosition, yPosition );

         // add Person to ElevatorView
         add( panel, 0 );

         // Person starts walking
         panel.setMoving( true );
         panel.setAnimating( true );
         panel.playAnimation( 2 );
         panel.setLoop( true );
         walkClip.loop();
      }
   } // end method PersonDeparted

   // invoked when Person has exited simulation
   public void personExited( PersonMoveEvent personEvent)
   {
      // find Panel associated with Person that issued moveEvent
      AnimatedPanel panel = getPersonPanel( personEvent );

      if ( panel != null ) { // if Person exists

         panel.setMoving( false );
         panel.setAnimating( false );

         // remove Person permanently and stop walking sound
         synchronized( personAnimatedPanels )
         {
            personAnimatedPanels.remove( panel );
         }
         remove( panel );
         stopWalkingSound();
      }
   } // end method personExited

   // invoked when Door has opened in model
   public void doorOpened( DoorEvent doorEvent )
   {
      // get DoorEvent Location
      String location = 
         doorEvent.getLocation().getLocationName();

      // play animation of Door opening
      doorPanel.playAnimation( 0 );
      doorPanel.setAnimationRate( 2 );
      doorPanel.setDisplayLastFrame( true );

      // play sound clip of Door opening
      if ( doorOpenClip != null )
         doorOpenClip.play();

   } // end method doorOpened

   // invoked when Door has closed in model
   public void doorClosed( DoorEvent doorEvent )
   {
      // get DoorEvent Location
      String location = 
         doorEvent.getLocation().getLocationName();

      // play animation of Door closing
      doorPanel.playAnimation( 1 );
      doorPanel.setAnimationRate( 2 );
      doorPanel.setDisplayLastFrame( true );

      // play sound clip of Door closing
      if ( doorCloseClip != null )
         doorCloseClip.play();

   } // end method doorClosed

   // invoked when Button has been pressed in model
   public void buttonPressed( ButtonEvent buttonEvent )
   {
      // get ButtonEvent Location
      String location = 
         buttonEvent.getLocation().getLocationName();

      // press Elevator Button if from Elevator
      if ( location.equals( ELEVATOR_NAME ) ) {
         elevatorButtonPanel.playAnimation( 0 );
         elevatorButtonPanel.setDisplayLastFrame( true );
      }

      // press Floor Button if from Floor
      else

         if ( location.equals( FIRST_FLOOR_NAME ) ) {
            firstFloorButtonPanel.playAnimation( 0 );
            firstFloorButtonPanel.setDisplayLastFrame( true );
         }
      else

         if ( location.equals( SECOND_FLOOR_NAME ) ) {
            secondFloorButtonPanel.playAnimation( 0 );
            secondFloorButtonPanel.setDisplayLastFrame( true );
         }

      if ( buttonClip != null )
         buttonClip.play(); // play button press sound clip

   } // end method buttonPressed

   // invoked when Button has been reset in model
   public void buttonReset( ButtonEvent buttonEvent )
   {
      // get ButtonEvent Location
      String location = 
         buttonEvent.getLocation().getLocationName();

      // reset Elevator Button if from Elevator
      if ( location.equals( ELEVATOR_NAME ) ) {

         // return to first frame if still animating
         if ( elevatorButtonPanel.isAnimating() )
            elevatorButtonPanel.setDisplayLastFrame( false );
         else
            elevatorButtonPanel.setCurrentFrame( 0 );
      }

      // reset Floor Button if from Floor
      else

         if ( location.equals( FIRST_FLOOR_NAME ) ) {

            // return to first frame if still animating
            if ( firstFloorButtonPanel.isAnimating() )
               firstFloorButtonPanel.setDisplayLastFrame( 
                  false );
            else
               firstFloorButtonPanel.setCurrentFrame( 0 );
         }
      else

         if ( location.equals( SECOND_FLOOR_NAME ) ) {

            // return to first frame if still animating
            if ( secondFloorButtonPanel.isAnimating() )
               secondFloorButtonPanel.setDisplayLastFrame( 
                  false );
            else
               secondFloorButtonPanel.setCurrentFrame( 0 );
         }

   } // end method buttonReset

   // invoked when Bell has rung in model
   public void bellRang( BellEvent bellEvent )
   {
      bellPanel.playAnimation( 0 ); // animate Bell

      if ( bellClip != null ) // play Bell sound clip
         bellClip.play();
   }

   // invoked when Light turned on in model
   public void lightTurnedOn( LightEvent lightEvent )
   {
      // turn on Light in Elevator
      elevatorLightPanel.setCurrentFrame( 1 );

      String location = 
         lightEvent.getLocation().getLocationName();

      // turn on Light on either first or second Floor
      if ( location.equals( FIRST_FLOOR_NAME ) )
         firstFloorLightPanel.setCurrentFrame( 1 );

      else

         if ( location.equals( SECOND_FLOOR_NAME ) )
            secondFloorLightPanel.setCurrentFrame( 1 );

   } // end method lightTurnedOn

   // invoked when Light turned off in model
   public void lightTurnedOff( LightEvent lightEvent )
   {
      // turn off Light in Elevator
      elevatorLightPanel.setCurrentFrame( 0 );

      String location = 
         lightEvent.getLocation().getLocationName();

      // turn off Light on either first or second Floor
      if ( location.equals( FIRST_FLOOR_NAME ) )
         firstFloorLightPanel.setCurrentFrame( 0 );

      else

         if ( location.equals( SECOND_FLOOR_NAME ) )
            secondFloorLightPanel.setCurrentFrame( 0 );

   } // end method lightTurnedOff

   // return preferred size of ElevatorView
   public Dimension getPreferredSize()
   {
      return new Dimension( VIEW_WIDTH, VIEW_HEIGHT );
   }

   // return minimum size of ElevatorView
   public Dimension getMinimumSize()
   {
      return getPreferredSize();
   }

      // return maximum size of ElevatorView
   public Dimension getMaximumSize()
   {
      return getPreferredSize();
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
