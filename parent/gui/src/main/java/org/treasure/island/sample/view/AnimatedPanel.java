// AnimatedPanel.java
// MovingPanel subclass with animation capabilities
package org.treasure.island.sample.view;

// Java core packages
import java.awt.*;
import java.util.*;

// Java extenstion packages
import javax.swing.*;

public class AnimatedPanel extends MovingPanel {

   // should ImageIcon cycle frames
   private boolean animating;

   // frame cycle rate (i.e., rate advancing to next frame)
   private int animationRate;
   private int animationRateCounter;
   private boolean cycleForward = true;

   // individual ImageIcons used for animation frames
   private ImageIcon imageIcons[];

   // storage for all frame sequences
   private java.util.List frameSequences;
   private int currentAnimation;

   // should loop (continue) animation at end of cycle?
   private boolean loop;

   // should animation display last frame at end of animation?
   private boolean displayLastFrame;

   // helps determine next displayed frame
   private int currentFrameCounter;

   // constructor takes array of filenames and screen position
   public AnimatedPanel( int identifier, String imageName[] )
   {
      super( identifier, imageName[0] );

      // creates ImageIcon objects from imageName string array
      imageIcons = new ImageIcon[ imageName.length ];

      for ( int i = 0; i < imageIcons.length; i++ ) {
         imageIcons[i] = new ImageIcon( 
            getClass().getResource( imageName[i] ) );
      }
      
      frameSequences = new ArrayList();

   } // end AnimatedPanel constructor

   // update icon position and animation frame
   public void animate()
   {
      super.animate();

      // play next animation frame if counter > animation rate
      if ( frameSequences != null && isAnimating() ) {

         if ( animationRateCounter > animationRate ) {
            animationRateCounter = 0;
            determineNextFrame();
         }
         else
            animationRateCounter++;
      }
   } // end method animate

   // determine next animation frame
   private void determineNextFrame()
   {
      int frameSequence[] = 
         ( int[] ) frameSequences.get( currentAnimation );

      // if no more animation frames, determine final frame,
      // unless loop is specified
      if ( currentFrameCounter >= frameSequence.length ) {
         currentFrameCounter = 0;

         // if loop is false, terminate animation
         if ( !isLoop() ) {

            setAnimating( false );

            if ( isDisplayLastFrame() )

               // display last frame in sequence
               currentFrameCounter = frameSequence.length - 1;
         }
      }

      // set current animation frame
      setCurrentFrame( frameSequence[ currentFrameCounter ] );
      currentFrameCounter++;

   } // end method determineNextFrame

   // add frame sequence (animation) to frameSequences ArrayList
   public void addFrameSequence( int frameSequence[] )
   {
      frameSequences.add( frameSequence );
   }

   // ask if AnimatedPanel is animating (cycling frames)
   public boolean isAnimating()
   {
      return animating;
   }

   // set AnimatedPanel to animate
   public void setAnimating( boolean animate )
   {
      animating = animate;
   }

   // set current ImageIcon
   public void setCurrentFrame( int frame )
   {
      setIcon( imageIcons[ frame ] );
   }

   // set animation rate
   public void setAnimationRate( int rate )
   {
      animationRate = rate;
   }

   // get animation rate
   public int getAnimationRate()
   {
      return animationRate;
   }

   // set whether animation should loop
   public void setLoop( boolean loopAnimation  )
   {
      loop = loopAnimation;
   }
   
   // get whether animation should loop
   public boolean isLoop()
   {
      return loop;
   }

   // get whether to display last frame at animation end
   private boolean isDisplayLastFrame()
   {
      return displayLastFrame;
   }

   // set whether to display last frame at animation end
   public void setDisplayLastFrame( boolean displayFrame )
   {
      displayLastFrame = displayFrame;
   }

   // start playing animation sequence of given index
   public void playAnimation( int frameSequence )
   {
      currentAnimation = frameSequence;
      currentFrameCounter = 0;
      setAnimating( true );
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
