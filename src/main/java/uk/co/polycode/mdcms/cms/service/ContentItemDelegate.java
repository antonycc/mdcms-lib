package uk.co.polycode.mdcms.cms.service;

import org.apache.commons.lang3.StringUtils;
import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.type.FieldType;
import uk.co.polycode.mdcms.util.reflect.ReflectionException;
import uk.co.polycode.mdcms.util.reflect.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * A delegate to remove complex processing from the AbstractItem
 * 
 * @author antony
 */
public class ContentItemDelegate {

   /**
    * The reflection helper delegate
    */
   private ReflectionHelper reflect = new ReflectionHelper();

   /**
    * A map between primitive types and the boxed classes
    */
   private Map<Class<?>, Class<?>> boxedTypeMap = null;

   /**
    * A map field type classes and instances of these
    */
   private Map<Class<? extends FieldType>, FieldType> fieldTypeMap = new HashMap<Class<? extends FieldType>, FieldType>();

   /**
    * Default constructor
    */
   public ContentItemDelegate() {
      this.populateBoxedTypes();
   }

   /**
    * Obtain an instance of a field type from the class and store in a member variable for later use
    * 
    * @param fieldType
    *           the class to create an instance of
    * @param contentPath
    *           the path the content item was read from
    * 
    * @return an instance of the field type
    */
   public FieldType createOrRetriveInstanceOf(final Class<? extends FieldType> fieldType, final String contentPath) {
      if (this.fieldTypeMap.containsKey(fieldType)) {
         return this.fieldTypeMap.get(fieldType);
      } else {
         FieldType fieldTypeInstance = this.createInstanceOf(fieldType, contentPath);
         this.fieldTypeMap.put(fieldType, fieldTypeInstance);
         return this.fieldTypeMap.get(fieldType);
      }
   }

   /**
    * Obtain an instance of a field type from the class and store in a member variable for later use
    * 
    * @param fieldType
    *           the class to create an instance of
    * @param contentPath
    *           the path the content item was read from
    * 
    * @return an instance of the field type
    */
   private FieldType createInstanceOf(final Class<? extends FieldType> fieldType, final String contentPath) {
      try {
         Constructor<?> constructor = this.reflect.getConstructor(fieldType);
         return (FieldType) this.reflect.newInstance(constructor);
      } catch (ReflectionException e) {
         throw new ContentException("Could not create instance of field type " + fieldType.getSimpleName(),
                  contentPath, e);
      }
   }

   /**
    * Get setter method from a field name using the bean convention
    * 
    * @param item
    *           the content item to invoke the setter on
    * @param path
    *           the path used to access the underlying content
    * @param field
    *           - the field to invoke the setter on
    * @param newFieldValue
    *           - the new value to pass to the setter
    */
   public void invokeSetter(final AbstractItem item, final String path, final Field field,
            final Object newFieldValue) {
      String fieldName = field.getName();
      Class<?> fieldType = field.getType();
      Class<?> clazz = field.getDeclaringClass();
      //Class<?> newFieldType = newFieldValue != null ? newFieldValue.getClass() : fieldType;
	   // When unit testing, is appeared that there was no route to have a null default value
	   Class<?> newFieldType = newFieldValue.getClass();
	   try {
         // Use a bean convention to locate the setter
         String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
         Method setter = this.reflect.getMethod(clazz, setterName, fieldType);

         // If assignment is possible assign
         if (fieldType.isAssignableFrom(newFieldType)) {
            this.reflect.invoke(setter, item, newFieldValue);
            return;
         }

         // If the field type is a boxed type box it
         Class<?> boxedType = this.boxedTypeMap.get(fieldType);
         if (boxedType != null) {
            if(newFieldType.isAssignableFrom(String.class)){
               String stringFieldValue = ((String)newFieldValue).trim();
               if(StringUtils.isNotBlank(stringFieldValue)) {
                  Constructor<?> boxedTypeConstructor = this.reflect.getConstructor(boxedType, newFieldType);
                  Object boxedValue = this.reflect.newInstance(boxedTypeConstructor, stringFieldValue);
                  this.reflect.invoke(setter, item, boxedValue);
               }
               return;
            }else{
               Constructor<?> boxedTypeConstructor = this.reflect.getConstructor(boxedType, newFieldType);
               Object boxedValue = this.reflect.newInstance(boxedTypeConstructor, newFieldValue);
               this.reflect.invoke(setter, item, boxedValue);
               return;
            }
         }

         // If the field type has a constructor matching the new value's type use the constructor
         Constructor<?> fieldConstructor = this.reflect.getConstructor(fieldType, newFieldType);
         Object constructedValue = this.reflect.newInstance(fieldConstructor, newFieldValue);
         this.reflect.invoke(setter, item, constructedValue);
         return;

      } catch (ReflectionException e) {
         throw new ContentException("Could not invoke setter for " + fieldName + " with " + newFieldValue, path, e);
      }
   }

   /**
    * Set the reflection helper
    * 
    * @param reflect
    *           the reflect to set
    */
   public void setReflect(final ReflectionHelper reflect) {
      this.reflect = reflect;
   }

   /**
    * Populate the map of boxed types
    */
   private void populateBoxedTypes() {
      this.boxedTypeMap = new HashMap<Class<?>, Class<?>>();
      this.boxedTypeMap.put(boolean.class, Boolean.class);
      this.boxedTypeMap.put(byte.class, Byte.class);
      this.boxedTypeMap.put(char.class, Character.class);
      this.boxedTypeMap.put(double.class, Double.class);
      this.boxedTypeMap.put(float.class, Float.class);
      this.boxedTypeMap.put(int.class, Integer.class);
      this.boxedTypeMap.put(long.class, Long.class);
      this.boxedTypeMap.put(short.class, Short.class);
   }

}
