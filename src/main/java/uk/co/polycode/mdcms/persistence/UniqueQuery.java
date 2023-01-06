package uk.co.polycode.mdcms.persistence;

import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import javax.persistence.Query;

/**
 * Methods to support access to a unique entity
 *
 * @author Antony
 *
 */
public abstract class UniqueQuery extends ComparableUsingString {

   /**
    * Add parameters to a query for a unique entity
    *
    * @param query
    *           the query to populate
    */
   public abstract void setUniqueParameters(Query query);

   /**
    * Add parameters to a query for a unique entity using a hash
    *
    * @param query
    *           the query to populate
    */
   public void setHashParameters(Query query) {
      // NOP
   }

   /**
    * Copy non system generated attributes into this object in preparation for an update to persistence
    */
   public abstract void copyAttributesForUpdate(ComparableUsingString o);

}