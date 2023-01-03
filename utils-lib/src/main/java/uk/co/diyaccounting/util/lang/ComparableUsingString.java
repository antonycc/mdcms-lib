package uk.co.diyaccounting.util.lang;

/**
 * Created by antony on 14/02/2016.
 */
public class ComparableUsingString implements Comparable<ComparableUsingString> {
   /**
    * Is this object of greater precedence than the passed in object?
    *
    * @param o
    *           the object to compare against
    *
    * @return the value 0 if this object's precedence value is equal to the argument object's precedence value; a value
    *         less than 0 if this object's precedence value is numerically less than the argument object's precedence
    *         value; and a value greater than 0 if this object's precedence value is numerically greater than the
    *         argument object's precedence value (signed comparison).
    */
   @Override
   public int compareTo(final ComparableUsingString o) {
      String thisString = this.toString();
      String oString = o.toString();
      return thisString.compareTo(oString);
   }

   /**
    * Two entities are "equal" if their string versions are equal.
    *
    * @param o
    *           the object to compare this one to
    *
    * @return true if the two objects are equal
    */
   @Override
   public final boolean equals(final Object o) {
      // if (obj == null) {
      // return false;
      // }
      // String objString = obj.toString();
      // String thisString = this.toString();
      // return this.compareTo((Unique)obj);
      // return thisString.equals(objString);
      if (o == null) {
         return false;
      } else if (!(o.getClass().isInstance(this))) {
         return false;
      } else {
         return this.compareTo((ComparableUsingString) o) == 0;
      }
   }

   /**
    * Calculate the hash code using the Java String hash code on the toString output.
    *
    * @return the hashCode
    */
   @Override
   public final int hashCode() {
      return this.toString().hashCode();
   }
}
