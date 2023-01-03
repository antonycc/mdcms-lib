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
                  name = "TestEntity." + TransactionManager.FIND_ALL_QUERY,
                  query = "SELECT te "
                           + "FROM TestEntity te"),
         @NamedQuery(
                  name = "TestEntity." + TransactionManager.FIND_UNIQUE_QUERY,
                  query = "SELECT te "
                           + "FROM TestEntity te "
                           + "WHERE "
                           + "te.name = :name") ,
			@NamedQuery(
					name = "TestEntity." + TransactionManager.FIND_BY_HASH_QUERY,
					query = "SELECT te "
							+ "FROM TestEntity te "
							+ "WHERE "
							+ "te.hash = :hash")
})
@Table(
		name = "test_entity",
		uniqueConstraints =
		@UniqueConstraint(columnNames = { "hash" }))
public class TestEntity extends UniqueQuery implements Serializable {

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

	@Column(name = "value", nullable = false, unique = false)
	private String value;

	/**
	 * A hash of the products unique attributes
	 */
	@Column(name = "hash", nullable = false, unique = true)
	private String hash;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.Unique#setUniqueParameters(javax.persistence.Query)
    */
   @Override
   public void setUniqueParameters(final Query query) {
      query.setParameter("name", this.getName());
   }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.diyaccounting.persistence.Unique#setHashParameters(javax.persistence.Query)
	 */
	@Override
	public void setHashParameters(final Query query) {
		query.setParameter("hash", this.getHash());
	}


   @Override
   public void copyAttributesForUpdate(final ComparableUsingString o) {
      TestEntity entity = (TestEntity)o;
      this.setName(entity.getName());
      this.setValue(entity.getValue());
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

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    * 
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
	   this.updateHash();
   }

	/**
	 * A hash of the unique attributes
	 *
	 * @return the hash
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * A hash of the products unique attributes
	 *
	 * @param hash
	 *           the hash to set
	 */
	public void setHash(final String hash) {
		this.hash = hash;
	}

	/**
	 * Calculate a new hash and set it to the hash attribute
	 */
	public void updateHash() {
		this.hash = this.calculateHash(this.toString());
	}

	/**
	 * Calculate a new hash and set it to the hash attribute
	 */
	public String calculateHash(final String stringToHash) {
		return this.hashHelper.getHash(stringToHash);
		//return stringToHash;
	}

}