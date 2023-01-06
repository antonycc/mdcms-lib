package uk.co.polycode.mdcms.util.spring;

import java.lang.reflect.Constructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import uk.co.polycode.mdcms.util.reflect.ReflectionHelper;

/**
 * Create Spring context in a separate class so mocks can prevent Spring initialisation
 * 
 * @author antony
 */
public class ApplicationContextFactory {

   /**
    * Default application context class
    */
   private Class<?> clazz = ClassPathXmlApplicationContext.class;

   /**
    * Create Spring context
    * 
    * @param context
    *           the classpath of the Spring context configuration
    * 
    * @return a Spring context
    */
   public ApplicationContext createApplicationContext(final String context) {
      return this.createApplicationContext(context, this.clazz);
   }

   /**
    * Create Spring context
    * 
    * @param context
    *           the classpath of the Spring context configuration
    * 
    * @return a Spring context
    */
   public ApplicationContext createApplicationContext(final String context, final Class<?> clazz) {
      ReflectionHelper helper = new ReflectionHelper();
      Constructor<?> constructor = helper.getConstructor(clazz, String.class);
      Object[] parameters = {context};
      ApplicationContext applicationContext;
      applicationContext = (ApplicationContext) helper.newInstance(constructor, parameters);
      return applicationContext;
   }

   /**
    * Default application context class
    * 
    * @param clazz
    *           the clazz to set
    */
   public void setClazz(final Class<?> clazz) {
      this.clazz = clazz;
   }
}