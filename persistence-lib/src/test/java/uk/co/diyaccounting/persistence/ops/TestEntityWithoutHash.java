package uk.co.diyaccounting.persistence.ops;

import org.hibernate.annotations.GenericGenerator;
import uk.co.diyaccounting.persistence.TransactionManager;
import uk.co.diyaccounting.util.lang.ComparableUsingString;
import uk.co.diyaccounting.persistence.UniqueQuery;
import uk.co.diyaccounting.util.security.HashHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.UUID;

/**
 * A simple named entity for persistence tests
 *
 * @author Antony
 */
@Entity
@NamedQueries({
   @NamedQuery(
            name = "TestEntityWithoutHash." + TransactionManager.FIND_ALL_QUERY,
            query = "SELECT tewh "
                     + "FROM TestEntityWithoutHash tewh"),
                     @NamedQuery(
                              name = "TestEntityWithoutHash." + TransactionManager.FIND_UNIQUE_QUERY,
                              query = "SELECT tewh "
                                       + "FROM TestEntityWithoutHash tewh "
                                       + "WHERE "
                                       + "tewh.name = :name")
})
@Table(
         name = "test_entity_without_hash",
         uniqueConstraints =
         @UniqueConstraint(columnNames = { "name" }))
public class TestEntityWithoutHash extends UniqueQuery implements Serializable {

   @Transient
   private transient HashHelper hashHelper = new HashHelper();

   @Transient
   private static final long serialVersionUID = 1L;

   /**
    * The primary key of the entity
    */
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "id")
   private UUID id;

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    */
   @Column(name = "name", nullable = false, unique = true)
   private String name;

   /*
    * (non-Javadoc)
    *
    * @see uk.co.diyaccounting.persistence.Unique#setUniqueParameters(javax.persistence.Query)
    */
   @Override
   public void setUniqueParameters(final Query query) {
      query.setParameter("name", this.getName());
   }

   @Override
   public void copyAttributesForUpdate(final ComparableUsingString o) {
      TestEntityWithoutHash entity = (TestEntityWithoutHash)o;
      this.setName(entity.getName());
   }

   /**
    * A string version including the unique attribute(s) which make up the primary key of this entity
    *
    * @return A string version of this entity
    */
   @Override
   public String toString() {
      return "" + this.getName();
   }

   /**
    * The primary key of the entity
    *
    * @return the id
    */
   public UUID getId() {
      return this.id;
   }

   /**
    * The primary key of the entity
    *
    * @param id
    *           the id to set
    */
   public void setId(final UUID id) {
      this.id = id;
   }

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    *
    * @return the name
    */
   public String getName() {
      return this.name;
   }

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    *
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
   }
}
