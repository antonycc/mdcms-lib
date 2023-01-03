package uk.co.diyaccounting.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.diyaccounting.util.reflect.ReflectionException;
import uk.co.diyaccounting.util.reflect.ReflectionHelper;

/**
 * A reflected bean with proxy methods
 */
public class BeanGetterSetter {

   /**
    * A logger from java logging
    */
   private static Logger logger = LoggerFactory.getLogger(BeanGetterSetter.class);

   /**
    * The underlying object
    */
   private Object obj;

   /**
    * The underlying field
    */
   private Field field;

   /**
    * The getter
    */
   private Method get = null;

   /**
    * The setter
    */
   private Method set = null;

   /**
    * The reflection helper delegate
    */
   private transient ReflectionHelper reflect = new ReflectionHelper();

   /**
    * Private, an underlying object is required
    */
   private BeanGetterSetter() {
   }

   /**
    * Introspect the bean extracting setters and getters.
    */
   public BeanGetterSetter(final Object obj, final Field field) {
      this();
      this.obj = obj;
      this.field = field;

      // Introspect the setter
      String setterName = this.getBeanMethodName("set", this.field.getName());
      try {
         this.set = this.reflect.getMethod(this.obj.getClass(), setterName, this.field.getType());
      } catch (ReflectionException e) {
         String msg = "Could not pick setter for " + this.field.getName();
         BeanGetterSetter.logger.warn(msg);
         BeanGetterSetter.logger.debug(msg, e);
         this.set = null;
      }

      // Introspect the getter
      String getterName = this.getBeanMethodName("get", this.field.getName());
      try {
         this.get = this.reflect.getMethod(this.obj.getClass(), getterName);
      } catch (ReflectionException e) {
         // Cater for using isXXX as the bean convention for boolean
         // if ((field.getType() == boolean.class) || (field.getType() == Boolean.class)) {
         // try {
         // getterName = this.getBeanMethodName("is", this.field.getName());
         // this.get = reflect.getMethod(this.obj.getClass(), getterName);
         // } catch (ReflectionException e2) {
         // BeanGetterSetter.logger.warn("Could not pick getter for " + this.field.getName());
         // }
         // } else {
         String msg = "Could not pick getter for " + this.field.getName();
         BeanGetterSetter.logger.warn(msg);
         BeanGetterSetter.logger.debug(msg, e);
         this.get = null;
         // }
      }
   }

   /**
    * Generate a bean method name from the field
    * 
    * @param prefix
    *           the bean method prefix
    * 
    * @return the bean setter or getter name
    */
   public String getBeanMethodName(final String prefix, final String fieldName) {
      StringBuilder buf = new StringBuilder();
      buf.append(prefix);
      buf.append(Character.toUpperCase(fieldName.charAt(0)));
      buf.append(fieldName.substring(1));
      return buf.toString();
   }

   /**
    * Is there a getter setter pair?
    * 
    * @return true is there is a populated getter and setter pair
    */
   public boolean isGetterSetterPair() {
      if (this.get == null) {
         return false;
      }
      if (this.set == null) {
         return false;
      }
      return this.getTestValue(this.field.getType()) != null;
   }

   /**
    * Proxy to the object's setter. Throws ReflectionException if the setter throws one when passed a value such as if
    * the value passed does not match the setters method signature.
    * 
    * @param newFieldValue
    *           value to pass to the setter
    */
   public void setFieldValue(final Object newFieldValue) {

      // BeanGetterSetter.logger.debug(this.field.getType().getName() + "." + this.set.getName());

      // Only attempt to set of there is a setter
      // if (this.set == null) {
      // BeanGetterSetter.logger.warn("No setter defined, skipping");
      // return;
      // }

      // Only attempt to set of there is testable value
      if (newFieldValue == null) {
         BeanGetterSetter.logger.warn("No setter defined, skipping");
         return;
      }

      // Log expected and actual types
      // BeanGetterSetter.logger.warn("Field Type: " + this.field.getType().getName());
      // BeanGetterSetter.logger.warn("Value Type: " + newFieldValue.getClass().getName());

      // Translate the value to the target type if a constructor is provided that can do this
      Object boxedValue;
      // if (!this.field.getType().isPrimitive() && !this.field.getType().equals(newFieldValue.getClass())) {
      // Class<?> boxedType = reflect.getBoxedType(this.field.getType());
      // Constructor<?> boxedTypeConstructor = reflect.getConstructor(boxedType, newFieldValue.getClass());
      // boxedValue = reflect.newInstance(boxedTypeConstructor, newFieldValue);
      // } else {
      boxedValue = newFieldValue;
      // }

      this.reflect.invoke(this.set, this.obj, boxedValue);
   }

   /**
    * Proxy to the objects getter. Throws ReflectionException if the setter throws one when passed a value such as if
    * the object does not have the specified getter.
    */
   public Object getFieldValue() {

      // Only attempt to get of there is a getter
      // if (this.get == null) {
      // BeanGetterSetter.logger.warn("No getter defined, skipping");
      // return null;
      // }

      // BeanGetterSetter.logger.debug(this.field.getType().getName() + "." + this.get.getName());
      return this.reflect.invoke(this.get, this.obj);
   }

   /**
    * get the type of the underlying field
    * 
    * @return the type of the field
    */
   public Class<?> getFieldType() {
      return this.field.getType();
   }

   /**
    * Generate an expected value for a given type. Throws ReflectionException if the constructor can not be created
    * 
    * @return an object to use when testing getters and setters
    */
   public Object getTestValue(final Class<?> type) {
      Object expectedValue = null;

      // Primitive - Boolean
      if (type.equals(boolean.class)) {
         expectedValue = true;
      }

      // Boxed primitive - Long
      if (type.equals(Long.class)) {
         UUID uuid = UUID.randomUUID();
         expectedValue = Long.valueOf(uuid.getLeastSignificantBits());
      }

      // String
      if (type.equals(String.class)) {
         UUID uuid = UUID.randomUUID();
         expectedValue = uuid.toString();
      }

      // Use reflect to generate a value using the default constructor
      if (expectedValue == null) {
         try {
            Constructor<?> constructor;
            constructor = this.reflect.getConstructor(type);
            expectedValue = this.reflect.newInstance(constructor);
         } catch (ReflectionException e) {
            BeanGetterSetter.logger.warn("Could not obtain test value for field due to {} {}", e.getClass(),
                     e.getMessage());
         }
      }

      return expectedValue;
   }
}
