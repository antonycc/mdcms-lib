package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.util.lang.ComparableUsingString;
import uk.co.polycode.mdcms.util.security.HashHelper;

import java.io.Serializable;
import java.util.UUID;

/**
 * A simple named entity for persistence tests
 * 
 * @author Antony
 */
public class TestEntity extends ComparableUsingString implements Serializable {

	private transient HashHelper hashHelper = new HashHelper();

   private static final long serialVersionUID = 1L;

   /**
    * The primary key of the entity
    */
   private UUID id;

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    */
   private String name;

	private String value;

	/**
	 * A hash of the products unique attributes
	 */
	private String hash;

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