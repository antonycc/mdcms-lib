package uk.co.polycode.mdcms.util.reflect;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An reflection helper library
 */
public class ReflectionHelper {

   /**
    * Create a new default instance of the Reflection Helper. Normally the instance would be used directly
    */
   public ReflectionHelper() {
   }

   /**
    * Get the named method wrapping exceptions with a content exception. Throws ReflectionException if the method does
    * not exist
    * 
    * @param clazz
    *           the class to examine for the method
    * @param methodName
    *           the method to look for
    * @param parameterTypes
    *           the parameters in the method
    * 
    * @return the method
    */
   public Method getMethod(final Class<?> clazz, final String methodName,
            final Class<?>... parameterTypes) {
      try {
         return clazz.getDeclaredMethod(methodName, parameterTypes);
      } catch (NoSuchMethodException e) {
         throw new ReflectionException("The class should define an accessible method called " + methodName, e);
      }
   }

   /**
    * Get the specified constructor wrapping constructors with a content exception. Throws ReflectionException if the
    * constructor does not exist
    * 
    * @param clazz
    *           the class to examine for the method
    * @param parameterTypes
    *           the parameters in the method
    * 
    * @return the constructor
    */
   public Constructor<?> getConstructor(final Class<?> clazz,
            final Class<?>... parameterTypes) {
      try {
         return clazz.getDeclaredConstructor(parameterTypes);
      } catch (NoSuchMethodException e) {
         throw new ReflectionException("The class " + clazz.getName()
                  + " should define an accessible constructor with argument supplied"
                  + ArrayUtils.toString(parameterTypes), e);
      }
   }

   /**
    * Get a new instance of the specified object by passing the parameters to the constructor. Throws
    * ReflectionException if the constructor is not accessible, throws an exception or the underlying class cannot be
    * instantiated
    * 
    * @param constructor
    *           the constructor to invoke
    * @param parameters
    *           the parameters in the method
    * 
    * @return the new instance
    */
   public Object newInstance(final Constructor<?> constructor,
            final Object... parameters) {
      try {
         return constructor.newInstance(parameters);
      } catch (InvocationTargetException e) {
         throw new ReflectionException("The underlying field constructor generated an exception on construction of "
                  + constructor.getDeclaringClass(), e);
      } catch (IllegalAccessException e) {
         throw new ReflectionException("The underlying field constructor could not be accessed on an object of type "
                  + constructor.getDeclaringClass() + " using " + ArrayUtils.toString(parameters), e);
      } catch (InstantiationException e) {
         throw new ReflectionException("The content type could not create an object of type "
                  + constructor.getDeclaringClass() + " using " + ArrayUtils.toString(parameters), e);
      }
   }

   /**
    * Get setter method from a field name using the bean convention
    * 
    * @param method
    *           the method to invoke
    * @param obj
    *           the instance to invoke the method on
    * @param parameters
    *           the parameters in the method
    */
   public Object invoke(final Method method, final Object obj, final Object... parameters) {
      try {
         return method.invoke(obj, parameters);
      } catch (IllegalAccessException e) {
         throw new ReflectionException("The method could not be accessed on an object of type "
                  + method.getDeclaringClass() + " using " + ArrayUtils.toString(parameters), e);
      } catch (InvocationTargetException e) {
         throw new ReflectionException("The underlying constructor generated an exception on construction of "
                  + method.getDeclaringClass(), e);
      }
   }

   /**
    * If the type is a primitive find the boxed equivalent, otherwise return the passed in class. Throws
    * ReflectionException if the boxed type does not exist
    * 
    * @param type
    *           the possibly primitive type to check
    * 
    * @return an object type (possibly the boxed version of a primitive)
    */
   /*
    * public Class<?> getBoxedType(final Class<?> type) { Class<?> boxedType;
    * 
    * Class<?>[] types = new Class<?>[1]; types[0] = type; // Get the ASM type equivalent Type[] asmTypes =
    * TypeUtils.getTypes(types); // get the name of the unboxed class String boxedName =
    * TypeUtils.getBoxedType(asmTypes[0]).getClassName(); // get the class object boxedType = this.getClass(boxedName);
    * 
    * return boxedType; }
    */

   /**
    * Get a class by name. wraps JDK method to allow mocks to test invocation failure. Throws ReflectionException if the
    * named class does not exist
    * 
    * @param name
    * 
    * @return the class object matching the name
    */
   public Class<?> getClass(final String name) {
      try {
         Class<?> clazz;
         clazz = Class.forName(name);
         return clazz;
      } catch (ClassNotFoundException e) {
         throw new ReflectionException(e.getMessage(), e);
      }
   }

}
