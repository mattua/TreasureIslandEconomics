// SoundEffects.java
// Returns AudioClip objects
package org.treasure.island.sample.view;

// Java core packages
import java.applet.Applet;
import java.applet.AudioClip;

public class SoundEffects {

   // location of sound files
   private String prefix = "";

   public SoundEffects() {}

   // get AudioClip associated with soundFile
   public AudioClip getAudioClip( String soundFile )
   {
      try {
         return Applet.newAudioClip( getClass().getResource( 
            prefix + soundFile ) );
      }

      // return null if soundFile does not exist
      catch ( NullPointerException nullPointerException ) {
         return null;
      }
   }

   // set prefix for location of soundFile
   public void setPathPrefix( String string )
   {
      prefix = string;
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
