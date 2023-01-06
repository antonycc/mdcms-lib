package uk.co.polycode.mdcms.cms.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This member is populated from content read from a document.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.FIELD)
public @interface MdContent {

   /**
    * The path (if set) from the annotation
    */
   String path();

   /**
    *
    */
   String defaultValue() default "";

   /**
    * Should the value be trimmed
    */
   boolean trim() default false;

   /**
    * The type of content, defaulting to TEXT
    */
   Class<? extends FieldType> type() default TextFieldType.class;
}
