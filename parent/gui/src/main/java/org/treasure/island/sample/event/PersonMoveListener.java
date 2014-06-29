// PersonMoveListener.java
// Methods invoked when Person moved
package org.treasure.island.sample.event;

public interface PersonMoveListener {

   // invoked when Person has been instantiated in model
   public void personCreated( PersonMoveEvent moveEvent );

   // invoked when Person arrived at elevator
   public void personArrived( PersonMoveEvent moveEvent );

   // invoked when Person departed from elevator
   public void personDeparted( PersonMoveEvent moveEvent );

   // invoked when Person pressed Button
   public void personPressedButton(
      PersonMoveEvent moveEvent );

   // invoked when Person entered Elevator
   public void personEntered( PersonMoveEvent moveEvent );

   // invoked when Person exited simulation
   public void personExited( PersonMoveEvent moveEvent );
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
