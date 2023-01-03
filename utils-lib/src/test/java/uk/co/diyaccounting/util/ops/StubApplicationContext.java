package uk.co.diyaccounting.util.ops;

import org.springframework.context.support.AbstractXmlApplicationContext;

/**
 * A test time replacement for loading the Spring context
 * 
 * @author Antony
 */
public class StubApplicationContext extends AbstractXmlApplicationContext {

   /**
    * Return a mock ApplicationContext
    */
   public StubApplicationContext(final String context) {
   }

}
