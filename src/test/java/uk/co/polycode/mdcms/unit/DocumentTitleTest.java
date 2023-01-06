package uk.co.polycode.mdcms.unit;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.junit.Assert;
import org.junit.Test;
import uk.co.polycode.mdcms.cms.dto.DocumentTitle;

import java.util.UUID;

/**
 * Test the Document Title query syntax
 * 
 * @author Antony
 */
public class DocumentTitleTest {

   private final String resourceName = "simple.md";

   private final String simpleName = "simple";
   private final String simpleBody =
         "<p>A simple piece of content.</p>\n" +
         "<p>The whole body is used as a single block of content.</p>\n";
   private final String simple = simpleName + "\n" + "======\n" + simpleBody;
   private final String simpleAtx = "# " + simpleName + "\n" + simpleBody;

   private final String twoName = "two";
   private final String twoBody =
         "A simple piece of content with two parts.\n" +
         "This one is just text.\n";
   private final String two = twoName + "\n" + "======\n" + twoBody;
   private final String twoAtx = "# " + twoName + "\n" + twoBody;

   private final String simpleLevelTwoName = "leveltwo";
   private final String simpleLevelTwoBody =
         "Two: A simple piece of content with two parts.\n" +
         "Two: This one is just text.\n";
   private final String simpleLevelTwo = simpleLevelTwoName + "\n" + "--------\n" + simpleLevelTwoBody;
   private final String simpleLevelTwoAtx = "## " + simpleLevelTwoName + "\n" + simpleLevelTwoBody;

   private final String twoSection = simple + "\n" + two;
   private final String twoSectionAtx = simpleAtx + "\n" + twoAtx;

   private final String headlessNested =
         simpleBody + "\n" + simpleLevelTwo + "\n";
   //private final String headlessNestedAtx =
   //      simpleBody + "\n" + simpleLevelTwoAtx + "\n";

   private final String twoSectionNested =
         simple + "\n" + simpleLevelTwo + "\n" +
         two + "\n" + simpleLevelTwo + "\n";
   private final String twoSectionNestedAtx =
         simpleAtx + "\n" + simpleLevelTwoAtx + "\n" +
               twoAtx + "\n" + simpleLevelTwoAtx + "\n";

   @Test
   public void expectSplits() {

      // Test parameters

      // Expected values
      ListOrderedMap<String, String> sections;

      // Create Object to test and check
      sections = DocumentTitle.split(this.simple);
      Assert.assertEquals(1, sections.size());

      // Check by index
      sections = DocumentTitle.split(this.twoSection);
      Assert.assertEquals(2, sections.size());

      sections = DocumentTitle.split(this.twoSectionNested);
      Assert.assertEquals(2, sections.size());

      sections = DocumentTitle.split(this.headlessNested);
      Assert.assertEquals(1, sections.size());
   }

   @Test
   public void expectDefaults() {

      // Test parameters
      String defaultName = UUID.randomUUID().toString();
      String defaultBody = UUID.randomUUID().toString();
      String defaultChildren = UUID.randomUUID().toString();

      // Expected values
      int expectedNumberOfSections = 1;

      // Create Object to test

      // Check
      ListOrderedMap<String, String> sections = DocumentTitle.split(this.simple);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(defaultName, DocumentTitle.evaluate(this.resourceName, this.simple, "/9999@", defaultName));
      Assert.assertEquals(defaultBody, DocumentTitle.evaluate(this.resourceName, this.simple, "/9999", defaultBody));
      Assert.assertEquals(defaultChildren, DocumentTitle.evaluate(this.resourceName, this.simple, "/9999/", defaultChildren));
      Assert.assertEquals(defaultName, DocumentTitle.evaluate(this.resourceName, this.simple, "/nosuchsection@", defaultName));
      Assert.assertEquals(defaultBody, DocumentTitle.evaluate(this.resourceName, this.simple, "/nosuchsection", defaultBody));
      Assert.assertEquals(defaultChildren, DocumentTitle.evaluate(this.resourceName, this.simple, "/nosuchsection/", defaultChildren));
   }

   @Test
   public void expectResourceName() {

      // Test parameters
      String defaultChildren = UUID.randomUUID().toString();

      // Expected values

      // Create Object to test

      // Check
      Assert.assertEquals(this.simple, DocumentTitle.evaluate(this.resourceName, this.simple, "", null));
      Assert.assertEquals(this.resourceName, DocumentTitle.evaluate(this.resourceName, this.simple, "@", null));
      Assert.assertEquals(this.simple, DocumentTitle.evaluate(this.resourceName, this.simple, "/", defaultChildren));
   }

   @Test
   public void expectSplitOnHeadingOne() {

      // Test parameters
      String defaultName = UUID.randomUUID().toString();
      String defaultBody = UUID.randomUUID().toString();

      // Expected values
      int expectedNumberOfSections = 1;

      // Create Object to test

      // Check
      ListOrderedMap<String, String> sections = DocumentTitle.split(this.simple);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.simple, "/0@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.simple, "/0", defaultBody));
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.simple, "/simple@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.simple, "/simple", defaultBody));
      Assert.assertEquals(this.simple, DocumentTitle.evaluate(this.resourceName, this.simple, "/", ""));

      // Create Object to test (ATX)

      // Check (ATX)
      sections = DocumentTitle.split(this.simpleAtx);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.simpleAtx, "/0@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.simpleAtx, "/0", defaultBody));
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.simpleAtx, "/simple@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.simpleAtx, "/simple", defaultBody));
      Assert.assertEquals(this.simpleAtx, DocumentTitle.evaluate(this.resourceName, this.simpleAtx, "/", ""));
   }

   @Test
   public void expectSplitOnHeadingTwo() {

      // Test parameters
      String defaultName = UUID.randomUUID().toString();
      String defaultBody = UUID.randomUUID().toString();

      // Expected values
      int expectedNumberOfSections = 1;

      // Create Object to test

      // Check
      ListOrderedMap<String, String> sections = DocumentTitle.split(this.simpleLevelTwo);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.simpleLevelTwo, "/@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.simpleLevelTwo, "/0@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.simpleLevelTwo, "/0", defaultBody));

      // Create Object to test (ATX)

      // Check (ATX)
      sections = DocumentTitle.split(this.simpleLevelTwoAtx);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.simpleLevelTwoAtx, "/0@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.simpleLevelTwoAtx, "/0", defaultBody));
   }

   @Test
   public void expectQueryStringToBeAppended() {

      // Test parameters
      String extension = "x" + UUID.randomUUID().toString();
      String namePath = "/@" + extension;
      String bodyPath = "/0" + extension;
      String childrenPath = "/" + extension;
      String defaultName = UUID.randomUUID().toString();
      String defaultBody = UUID.randomUUID().toString();
      String defaultChildren = UUID.randomUUID().toString();

      // Expected values
      int expectedNumberOfSections = 1;
      String expectedName = this.simpleName + extension;
      String expectedBody = this.simpleBody + extension;

      // Create Object to test

      // Check
      ListOrderedMap<String, String> sections = DocumentTitle.split(this.simple);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(expectedName, DocumentTitle.evaluate(this.resourceName, this.simple, namePath, defaultName));
      Assert.assertEquals(expectedBody, DocumentTitle.evaluate(this.resourceName, this.simple, bodyPath, defaultBody));
      Assert.assertEquals(defaultChildren, DocumentTitle.evaluate(this.resourceName, this.simple, childrenPath, defaultChildren));
   }

   @Test
   public void expectSplitOnHeadingOneWithTwoSections() {

      // Test parameters
      String defaultName = UUID.randomUUID().toString();
      String defaultBody = UUID.randomUUID().toString();

      // Expected values
      int expectedNumberOfSections = 2;

      // Create Object to test

      // Check by index
      ListOrderedMap<String, String> sections = DocumentTitle.split(this.twoSection);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/0@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/0", defaultBody));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/1@", defaultName));
      Assert.assertEquals(this.twoBody, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/1", defaultBody));

      // Check by name
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/simple@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/simple", defaultBody));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/two@", defaultName));
      Assert.assertEquals(this.twoBody, DocumentTitle.evaluate(this.resourceName, this.twoSection, "/two", defaultBody));

      // Create Object to test (ATX)

      // Check by index (ATX)
      sections = DocumentTitle.split(this.twoSectionAtx);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/0@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/0", defaultBody));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/1@", defaultName));
      Assert.assertEquals(this.twoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/1", defaultBody));

      // Check by name (ATC)
      sections = DocumentTitle.split(this.twoSectionAtx);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/simple@", defaultName));
      Assert.assertEquals(this.simpleBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/simple", defaultBody));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/two@", defaultName));
      Assert.assertEquals(this.twoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionAtx, "/two", defaultBody));
   }

   @Test
   public void expectSplitOnHeadingOneWithTwoSectionsNested() {

      // Test parameters
      String defaultName = UUID.randomUUID().toString();
      String defaultBody = UUID.randomUUID().toString();

      // Expected values
      int expectedNumberOfSections = 2;
      String expectedTrailingSubsection = this.simpleLevelTwo;
      String expectedBodyWithTrailingSubsection = this.simpleBody + "\n" + expectedTrailingSubsection;
      String expectedTrailingSubsectionAtx = this.simpleLevelTwoAtx;
      String expectedBodyWithTrailingSubsectionAtx = this.simpleBody + "\n" + expectedTrailingSubsectionAtx;

      // Create Object to test

      // Check by index
      ListOrderedMap<String, String> sections = DocumentTitle.split(this.twoSectionNested);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0@", defaultName));
      Assert.assertEquals(expectedBodyWithTrailingSubsection, sections.getValue(0));
      Assert.assertEquals(expectedBodyWithTrailingSubsection, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0", defaultBody));
      Assert.assertEquals(expectedTrailingSubsection, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0/", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0/0@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0/@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0/0", defaultBody));
      Assert.assertEquals("", DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/0/0/", ""));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/1@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/1/0", defaultBody));

      // Check by name
      Assert.assertEquals(expectedBodyWithTrailingSubsection, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/simple", defaultBody));
      Assert.assertEquals(expectedTrailingSubsection, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/simple/", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/simple/0@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/simple/@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/simple/0", defaultBody));
      Assert.assertEquals("", DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/simple/0/", ""));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/two@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNested, "/1/leveltwo", defaultBody));

      // Create Object to test (ATX)
      // Check by index (ATX)
      sections = DocumentTitle.split(this.twoSectionNestedAtx);
      Assert.assertEquals(expectedNumberOfSections, sections.size());
      Assert.assertEquals(this.simpleName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0@", defaultName));
      Assert.assertEquals(expectedBodyWithTrailingSubsectionAtx, sections.getValue(0));
      Assert.assertEquals(expectedBodyWithTrailingSubsectionAtx, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0", defaultBody));
      Assert.assertEquals(expectedTrailingSubsectionAtx, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0/", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0/0@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0/@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0/0", defaultBody));
      Assert.assertEquals("", DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/0/0/", ""));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/1@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/1/0", defaultBody));

      // Check by name (ATX)
      Assert.assertEquals(expectedBodyWithTrailingSubsectionAtx, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/simple", defaultBody));
      Assert.assertEquals(expectedTrailingSubsectionAtx, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/simple/", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/simple/leveltwo@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/simple/@", defaultBody));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/simple/leveltwo", defaultBody));
      Assert.assertEquals(defaultBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/simple/leveltwo/", defaultBody));
      Assert.assertEquals(this.twoName, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/two@", defaultName));
      Assert.assertEquals(this.simpleLevelTwoBody, DocumentTitle.evaluate(this.resourceName, this.twoSectionNestedAtx, "/two/leveltwo", defaultBody));
   }
}