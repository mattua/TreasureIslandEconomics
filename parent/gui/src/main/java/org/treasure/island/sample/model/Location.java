// Location.java
// Abstract superclass representing location in simulation
package org.treasure.island.sample.model;

// Deitel packages

public abstract class Location {

   // name of Location
   private String locationName;

   // set name of Location
   protected void setLocationName( String name )
   {
      locationName = name;
   }

   // return name of Location
   public String getLocationName()
   {
      return locationName;
   }

   // return Button at Location
   public abstract Button getButton();

   // return Door object at Location
   public abstract Door getDoor();
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
