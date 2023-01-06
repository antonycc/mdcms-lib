package uk.co.polycode.mdcms.cms.dto;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.cms.service.ContentItemDelegate;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.FieldType;
import uk.co.polycode.mdcms.cms.type.MdContent;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.io.UtilConstants;
import uk.co.polycode.mdcms.util.xml.XMLHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Base class for all content items which provides the data retrieval for fields of all supported types/
 */
public abstract class AbstractItem implements Serializable {

	private static final long serialVersionUID = 1L;
   private static final Logger logger = LoggerFactory.getLogger(AbstractItem.class);

   private transient String path = null;
   private transient ContentItemDelegate contentItemDelegate = new ContentItemDelegate();
   private transient XMLHelper xmlHelper = new XMLHelper();

   public void populateContent(final FileLikePathService fileLikePathService) {
      logger.info("Loading content from {}", this.path);
      //try {
         InputStream is = this.getInputStreamForUrn(fileLikePathService, this.path);

         // Method commented out, to use remove comments.
         // this.logSourceDocument();
         if (this.getPath().endsWith(".md")) {
            String mdDocument = this.readInputSourceIntoMarkdownString(is);
            this.readMdAttributes(this.path, mdDocument);
         } else {
            InputSource inputSource = new InputSource(is);
            this.readAttributes(inputSource);
         }
         this.postPopulationConfig();
      //}catch(IOException e){
      //   throw new ContentException("Could not load content from: ", this.path, e);
      //}
   }

   private InputStream getInputStreamForUrn(final FileLikePathService fileLikePathService, final String resourcePath) throws ContentException {
      try {
         return fileLikePathService.getInputStreamForUrn(resourcePath);
      }catch(IOException e){
         throw new ContentException("Could not create input stream for path", resourcePath, e);
      }
   }

   public String[] contentList(final FileLikePathService fileLikePathService) {
      String filter = this.getFilter();
      try{
         return fileLikePathService.getContentListForUrn(this.path, filter);
      }catch(IOException e){
         throw new ContentException("Could not create list for path", this.path, e);
      }
   }

   public abstract String getExtension();

   public abstract String getFilter();

   /**
    * Read the supplied item as a piece of content. Any readable content is used to populate the content item's
    * attributes.
    * See:
    *    https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet
    * 
    * @param inputSource
    *           the inputSource to set
    * 
    */
   public void readAttributes(final InputSource inputSource) {
      Document document = this.parseInputSourceIntoXmlDocument(inputSource);
      readAttributesIntoAnnotatedFields(null, null, document);
   }

   /**
    * Read the supplied item as a piece of content. Any readable content is used to populate the content item's
    * attributes.
    * See:
    *    https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet
    *
    * @param resourceName
    *           the name of the document
    * @param documentText
    *           document as text
    */
   public void readMdAttributes(final String resourceName, final String documentText) {
      readAttributesIntoAnnotatedFields(resourceName, documentText, null);
   }

   /**
    * Introspect the underlying class looking for annotated fields
    *
    * @param resourceName
    *           the name of the document
    * @param documentText
    *           document as text
    * @param document
    *            the document an an XML document.
    */
   private void readAttributesIntoAnnotatedFields(final String resourceName,
                                                  final String documentText,
                                                  final Document document) {
      Field[] fields = this.getDeclaredAndInheritedFields();
      for(Field field : fields) {
         logger.trace("Checking field: " + field.getName());
         //logger.debug("document: " + document);
         if(documentText != null) {
            MdContent contentAnnotation = this.getMdContentAnnotation(field);
            if (contentAnnotation == null) {
               //logger.debug("No content annotation");
               continue;
            }
            Class<? extends FieldType> type = contentAnnotation.type();
            FieldType fieldTypeInstance = this.contentItemDelegate.createOrRetriveInstanceOf(type, this.path);
            String defaultValue = contentAnnotation.defaultValue();
            String query = contentAnnotation.path();
            //logger.debug("documentTitleQuery: " + documentTitleQuery);
            Object contentElement = fieldTypeInstance.readMdNodeIntoFieldTypeSpecificObject(
                  resourceName,
                  documentText,
                  query, defaultValue);
            // set the value if there one was available
            //logger.debug("contentElement: " + contentElement);
            if(contentElement.getClass().isAssignableFrom(String.class) && contentAnnotation.trim()) {
               String stringFieldValue = ((String) contentElement).trim();
               if(field.getType().isArray()){
                  String[] stringArrayFieldValue = stringFieldValue.split("\\r?\\n");
                  for(int i=0; i<stringArrayFieldValue.length; i++){
                     String s = stringArrayFieldValue[i];
                     s = StringUtils.trim(s);
                     if(s.startsWith("-")){
                        s = s.substring(1);
                        s = StringUtils.trim(s);
                     }
                     stringArrayFieldValue[i] = s;
                  }
                  this.contentItemDelegate.invokeSetter(this, query, field, stringArrayFieldValue);
               }else {
                  this.contentItemDelegate.invokeSetter(this, query, field, stringFieldValue);
               }
            }else {
               this.contentItemDelegate.invokeSetter(this, query, field, contentElement);
            }
            // AbstractItem.logger.debug("Read value from path {}", path);
         }else if(document != null){
            Content contentAnnotation = this.getContentAnnotation(field);
            if (contentAnnotation == null) {
               //logger.debug("No content annotation");
               continue;
            }
            String[] prune = contentAnnotation.prune();
            Class<? extends FieldType> type = contentAnnotation.type();
            FieldType fieldTypeInstance = this.contentItemDelegate.createOrRetriveInstanceOf(type, this.path);
            String defaultValue = contentAnnotation.defaultValue();
            XMLHelper savedXmlHelper = fieldTypeInstance.getXmlHelper();
            fieldTypeInstance.setXmlHelper(this.xmlHelper);
            String query = contentAnnotation.path();
            Object contentElement = fieldTypeInstance.readXmlNodeIntoFieldTypeSpecificObject(this.path, document,
                  query, defaultValue, prune);
            fieldTypeInstance.setXmlHelper(savedXmlHelper);
            // set the value if there one was available
            //logger.debug("contentElement: " + contentElement);
            this.contentItemDelegate.invokeSetter(this, query, field, contentElement);
            // AbstractItem.logger.debug("Read value from path {}", path);
         }

      }
   }

   public String readInputSourceIntoMarkdownString(final InputStream is) {
      StringWriter writer = new StringWriter();
      try {
         IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      }catch(IOException e){
         throw new ContentException("Could not parse underlying document from InputSource (document null)", this.path);
      }
      return writer.toString();
   }

   /**
    * Get the underlying XML document for this content item If the document hasn't been created when this method is
    * called the input source is read to create a document
    * 
    * @param inputSource
    *           the inputSource to set
    * @returns - the document for this content item
    */
   private Document parseInputSourceIntoXmlDocument(final InputSource inputSource) {

      // Parse and create the document if necessary
      Document document = null;
      try {
         document = this.xmlHelper.parseXMLDocument(inputSource);
         logger.debug("parseInputSourceIntoXmlDocument: no exception");
      } catch (Exception e) {
         throw new ContentException("Could not parse underlying document from InputSource (exception)", this.path, e);
      }
	   if(document == null){
		   throw new ContentException("Could not parse underlying document from InputSource (document null)", this.path);
	   }

      return document;
   }

   /**
    * Walk up the object hierarchy collecting all the declared fields in an array
    * 
    * @return all the fields
    */
   private Field[] getDeclaredAndInheritedFields() {
      Field[] fields = new Field[0];

      Class<?> clazz = this.getClass();
      do {
         Field[] newFields = clazz.getDeclaredFields();
         int oldSize = fields.length;
         fields = Arrays.copyOf(fields, oldSize + newFields.length);
         System.arraycopy(newFields, 0, fields, oldSize, newFields.length);
         clazz = clazz.getSuperclass();
      } while (clazz != null);

      return fields;
   }

   /**
    * Read the field and check for annotations of type Content
    * 
    * @return an annotation of type Content or null
    */
   private Content getContentAnnotation(final Field field) {
      return field.getAnnotation(Content.class);
   }

   /**
    * Read the field and check for annotations of type MdContent
    *
    * @return an annotation of type MdContent or null
    */
   private MdContent getMdContentAnnotation(final Field field) {
      return field.getAnnotation(MdContent.class);
   }

   /**
    * Change the default implementation of the XML Helper
    * 
    * @param xmlHelper
    */
   public void setXmlHelper(final XMLHelper xmlHelper) {
      this.xmlHelper = xmlHelper;
   }

   /**
    * The content path that was being accessed when the exception occurred
    * 
    * @param path
    *           the path to set
    */
   public void setPath(final String path) {
      this.path = path;
   }

   /**
    * A delegate to remove complex processing from the AbstractItem
    * 
    * @param contentItemDelegate
    *           the contentItemDelegate to set
    */
   public void setContentItemDelegate(final ContentItemDelegate contentItemDelegate) {
      this.contentItemDelegate = contentItemDelegate;
   }

   /**
    * Update any attributes that depend on others being populated first. derived fields and such Content Types should
    * override this method to perform there own config, the default implementation does nothing.
    * 
    */
   public void postPopulationConfig() {
   }

   /**
    * The content path that was being accessed when the exception occurred
    * 
    * @return the path to set
    */
   protected String getPath() {
      return this.path;
   }

   /**
    * Log the contents of the source document
    */
   // private void logSourceDocument() {
   // StringWriter sw = new StringWriter();
   // InputStream is = AbstractItem.class.getResourceAsStream(this.path);
   // try {
   // InputStreamReader isr = new InputStreamReader(is, "UTF-8");
   // BufferedReader br = new BufferedReader(isr);
   // PrintWriter pw = new PrintWriter(sw);
   // String line;
   // while ((line = br.readLine()) != null) {
   // pw.println(line);
   // }
   // } catch (Exception e) {
   // AbstractItem.logger.warn("Could not read full data from: [{}] due to {}:{}",
   // this.path,
   // e.getClass().getSimpleName(),
   // e.getMessage());
   // }
   // AbstractItem.logger.trace("Parsing content: [{}]", sw.toString());
   // }

}
