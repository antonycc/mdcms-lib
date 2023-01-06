package uk.co.polycode.mdcms.cms.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This member is populated from content read from a document.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.FIELD)
public @interface Content {

   /**
    * The path (if set) from the annotation
    */
   String path();

   /**
    * The children to prune from path (if set) from the annotation
    */
   String[] prune() default {};

   /**
    * true if a ContentException should be thrown if this item indicated by the path is not populated
    */
   String defaultValue() default "";

   /**
    * The type of content, defaulting to TEXT
    */
   Class<? extends FieldType> type() default TextFieldType.class;
}
