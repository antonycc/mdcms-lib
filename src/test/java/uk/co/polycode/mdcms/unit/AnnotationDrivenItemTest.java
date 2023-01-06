package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.polycode.mdcms.ops.AnnotationDrivenNameBodyItem;
import uk.co.polycode.mdcms.ops.TestAnnotationDrivenNameBodyItem;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.cms.service.ContentItemDelegate;
import uk.co.polycode.mdcms.cms.type.TextFieldType;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.reflect.ReflectionException;
import uk.co.polycode.mdcms.util.reflect.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class AnnotationDrivenItemTest {

   // Expected values - undefined values set in Setup

   private static final String expectedReadName = "simple";

   private static final String expectedReadBody = "<p>A simple piece of content.</p>" +
            System.lineSeparator() +
            "<p>The whole body is used as a single block of content.</p>";

   private static final String localPath = AnnotationDrivenItemTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String contentItemFileXhtml = "urn:diyaccounting.co.uk:file://" + localPath + "test1/simple.html";
   private static final String contentItemClasspathXhtml = "urn:diyaccounting.co.uk:classpath:/" + "test1/simple.html";

   @Test
   public void expectToFindSpecificValuesInContentItemAfterPopulationFromXhtml() throws ContentException {

      // instance under test "item" is initialised using constructor
      AnnotationDrivenNameBodyItem classUnderTest = null;
      classUnderTest = new AnnotationDrivenNameBodyItem();
      classUnderTest.setPath(AnnotationDrivenItemTest.contentItemFileXhtml);
      classUnderTest.populateContent(new FileLikePathService());

      // Check
      Assert.assertEquals(AnnotationDrivenItemTest.expectedReadName, classUnderTest.getName());
      String expectedBody = AnnotationDrivenItemTest.expectedReadBody.trim();
      String actualBody = classUnderTest.getBody().trim();
      Assert.assertEquals(expectedBody, actualBody);
   }

   @Test
   public void expectToFindSpecificValuesInContentItemAfterPopulationFromXhtmlUsingClasspath() throws ContentException {

      // instance under test "item" is initialised using constructor
      TestAnnotationDrivenNameBodyItem classUnderTest = null;
      classUnderTest = new TestAnnotationDrivenNameBodyItem();
      classUnderTest.setPath(AnnotationDrivenItemTest.contentItemClasspathXhtml);
      classUnderTest.populateContent(new FileLikePathService());

      // Check
      Assert.assertEquals(AnnotationDrivenItemTest.expectedReadName, classUnderTest.getName());
      String expectedBody = AnnotationDrivenItemTest.expectedReadBody.trim();
      String actualBody = classUnderTest.getBody().trim();
      Assert.assertEquals(expectedBody, actualBody);
   }

   @Test(expected = ContentException.class)
   public void expectExceptionWhenInvokingAttributeSetter() throws ReflectionException,
            IllegalAccessException,
            InvocationTargetException, ContentException, NoSuchMethodException, SecurityException {

      // Expected results
      String message = "Mock forced exception";

      // Create Object to test
      ReflectionHelper reflectionHelper = EasyMock.createNiceMock(ReflectionHelper.class);
      ReflectionException re = new ReflectionException(message, null);
      reflectionHelper.invoke(EasyMock.anyObject(Method.class), EasyMock.anyObject(Object.class),
               EasyMock.anyObject(String.class));
      EasyMock.expectLastCall().andThrow(re);
      EasyMock.expect(reflectionHelper.newInstance(null)).andReturn(new TextFieldType());
      EasyMock.replay(reflectionHelper);
      ContentItemDelegate contentItemDelegate = new ContentItemDelegate();
      contentItemDelegate.setReflect(reflectionHelper);

      // read attributes
      TestAnnotationDrivenNameBodyItem classUnderTest = new TestAnnotationDrivenNameBodyItem();
      classUnderTest.setPath(AnnotationDrivenItemTest.contentItemFileXhtml);
      classUnderTest.setContentItemDelegate(contentItemDelegate);
      classUnderTest.populateContent(new FileLikePathService());
   }

   @Test(expected = ContentException.class)
   public void expectExceptionWhenCreatingFieldType() throws ReflectionException,
            IllegalAccessException,
            InvocationTargetException, ContentException, NoSuchMethodException, SecurityException {

      // Expected results
      String message = "Mock forced exception";

      // Create Object to test
      ReflectionHelper reflectionHelper = EasyMock.createNiceMock(ReflectionHelper.class);
      ReflectionException re = new ReflectionException(message, null);
      EasyMock.expect(reflectionHelper.getConstructor(TextFieldType.class)).andThrow(re);
      EasyMock.replay(reflectionHelper);
      ContentItemDelegate contentItemDelegate = new ContentItemDelegate();
      contentItemDelegate.setReflect(reflectionHelper);

      // read attributes
      TestAnnotationDrivenNameBodyItem classUnderTest = new TestAnnotationDrivenNameBodyItem();
      classUnderTest.setPath(AnnotationDrivenItemTest.contentItemFileXhtml);
      classUnderTest.setContentItemDelegate(contentItemDelegate);
      classUnderTest.populateContent(new FileLikePathService());
   }
}