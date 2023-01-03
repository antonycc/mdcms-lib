package uk.co.diyaccounting.cms.dto;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A wrapper for a Markdown documents and supporting a query syntax.
 *
 * Newline regex credit:
 *    https://stackoverflow.com/questions/1331815/regular-expression-to-match-cross-platform-newline-characters
 *
 * @author Antony
 */
public class DocumentTitle implements Serializable {

	private static final long serialVersionUID = 1L;

   /**
    * The pattern for part of a path like expression using an index
    */
   private static final Pattern nodeIndexPattern = Pattern.compile("^/[\\p{Digit}]+");

   /**
    * The pattern for part of a path like expression using a name
    */
   private static final Pattern nodeNamePattern = Pattern.compile("^/[\\p{Alnum}-._ ~%]+");

   /**
    * The pattern for part of a path like expression separator
    */
   private static final Pattern slashPattern = Pattern.compile("/");

   /**
    * The pattern for part of a path like expression name leaf
    */
   private static final Pattern slashAtShortCutForSlashZeroAtPattern = Pattern.compile("^/@");

   /**
    * The pattern for meaningful characters in a body
    */
   private static final Pattern nonStructuralCharactersPattern = Pattern.compile("[\\p{Alnum}]+");

   /**
    * The pattern for a platform independent line separator
    */
   private static final Pattern lineSeparator = Pattern.compile("\\r\\n?|\\n");

   /**
    * Execute a Document Title query
    *
    * @param resourceName
    *           the name of the resource being evaluated, at the top level this is likely to be a filename
    * @param documentText
    *           the text of the document to evaluate the expression against
    * @param documentTitleQuery
    *           the path to read as a string
    * @param defaultValue
    *            the value to set if the result of the documentTitleQuery is blank
    *
    * @return the node contents as a string
    */
   public static String evaluate(final String resourceName, final String documentText, final String documentTitleQuery, final String defaultValue) {
      ListOrderedMap<String, String> sections = new ListOrderedMap<>();
      sections.put(resourceName, documentText);
      String value = DocumentTitle.evaluateNode(sections, documentTitleQuery);
      if (StringUtils.isBlank(value)) {
         return defaultValue;
      } else {
         return value;
      }
   }

   /**
    * Execute a Document Title query
    *
    * @param sections
    *           the document split into one level of  sections
    * @param documentTitleQuery
    *            the query to execute
    *
    * @return the node contents as a string
    */
   private static String evaluateNode(final ListOrderedMap<String,String> sections, final String documentTitleQuery) {
      Matcher slashAtShortCutForSlashZeroAtMatcher = slashAtShortCutForSlashZeroAtPattern.matcher(documentTitleQuery);
      String documentTitleQueryWithoutShortcut = slashAtShortCutForSlashZeroAtMatcher.replaceFirst("/0@");
      Matcher nodeIndexMatcher = nodeIndexPattern.matcher(documentTitleQueryWithoutShortcut);
      Matcher nodeNameMatcher = nodeNamePattern.matcher(documentTitleQueryWithoutShortcut);

      if(nodeIndexMatcher.find()){
         String subSectionSlashIndexString = nodeIndexMatcher.group();
         String subSectionIndexString = slashPattern.matcher(subSectionSlashIndexString).replaceFirst("");
         ListOrderedMap<String, String> subSections = DocumentTitle.split(sections.getValue(0));
         int subSectionIndex = Integer.parseInt(subSectionIndexString.trim());
         return DocumentTitle.findAndEvaluateChildNode(subSections, subSectionIndex, nodeIndexMatcher);
      }else if(nodeNameMatcher.find()){
         String subSectionSlashNameString = nodeNameMatcher.group();
         String subSectionNameString = slashPattern.matcher(subSectionSlashNameString).replaceFirst("");
         ListOrderedMap<String, String> subSections = DocumentTitle.split(sections.getValue(0));
         int subSectionIndex = subSections.indexOf(subSectionNameString.trim());
         return DocumentTitle.findAndEvaluateChildNode(subSections, subSectionIndex, nodeNameMatcher);
      }else{
         String name = sections.get(0);
         String value = sections.getValue(0);
         return DocumentTitle.evaluateLeaf(name, value, documentTitleQuery);
      }
   }

   /**
    * Execute a Document Title query
    *
    * @param subSections
    *           the sections split at the current heading level
    * @param nodeMatcher
    *            the matched that matched this node
    *
    * @return the node contents as a string
    */
   private static String findAndEvaluateChildNode(
         final ListOrderedMap<String, String> subSections,
         final int subSectionIndex,
         final Matcher nodeMatcher) {
      if (0 <= subSectionIndex && subSectionIndex < subSections.size()) {
         String subDocumentTitleQuery = nodeMatcher.replaceFirst("");
         ListOrderedMap<String, String> childNode = new ListOrderedMap<>();
         childNode.put(subSections.get(subSectionIndex), subSections.getValue(subSectionIndex));
         return DocumentTitle.evaluateNode(childNode, subDocumentTitleQuery);
      } else {
         return null;
      }
   }

   /**
    * Execute a Document Title query
    *
    * @param name
    *           the name of the leaf node
    * @param value
    *           the value of the leaf node
    * @param documentTitleQuery
    *           the path to read as a string
    *
    * @return the node contents as a string
    */
   private static String evaluateLeaf(final String name, final String value, final String documentTitleQuery) {
      if(StringUtils.isBlank(documentTitleQuery)){
         return value;
      }else if(documentTitleQuery.startsWith("@")){
         return documentTitleQuery.replaceFirst("@", name);
      }else if(documentTitleQuery.startsWith("/")){
         String remainderOfQuery = documentTitleQuery.replaceFirst("/", "");
         ListOrderedMap<String, String> subSections = DocumentTitle.split(value);
         String firstChildName = subSections.get(0);
         if(firstChildName.equals(subSections.getValue(0))){
            return "" + remainderOfQuery;
         }else if(value.contains(firstChildName)){
            int indexOfFirstChild = value.indexOf(firstChildName);
            String charactersBeforeFirstChild = value.substring(0, indexOfFirstChild);
            if(nonStructuralCharactersPattern.matcher(charactersBeforeFirstChild).find()){
               Matcher lineEndMatcher = lineSeparator.matcher(value.substring(0, indexOfFirstChild));
               int lastLineEnd = indexOfFirstChild;
               while(lineEndMatcher.find()){
                  lastLineEnd = lineEndMatcher.end();
               }
               return value.substring(lastLineEnd) + remainderOfQuery;
            }
         }
         return value + remainderOfQuery;
         //if(subSections.size() == 0){         // May never happen, could comment out
         //   return value + remainderOfQuery;  // May never happen, could comment out
         //}else{                               // May never happen, could comment out
         //   String children = value.substring(0, value.indexOf(subSections.get(0))) + remainderOfQuery;
         //   return children;
         //}
      }else {
         return value + documentTitleQuery;
      }
   }

   /**
    * Split the document into lists
    *
    * @param documentText
    *           the body of the document
    *
    * @return the as a list of maps
    */
   public static ListOrderedMap<String,String> split(final String documentText) {

      String[] setextHeadingPatternDelimiters = {"=", "-"};
      String[] atxHeadingPatternDelimiters = {"#", "##", "###", "####", "#####"};

      String[] sectionSplits = null;
      for(String headingPatternDelimiter : setextHeadingPatternDelimiters) {
         Pattern headingPattern = Pattern.compile("\\p{Space}{0,3}" + headingPatternDelimiter + "{3,}\\p{Space}*");
         sectionSplits = headingPattern.split(documentText);
         if(sectionSplits.length > 1) {
            return DocumentTitle.splitOnHeaderLevel(sectionSplits);
         }
      }

      for(String headingPatternDelimiter : atxHeadingPatternDelimiters) {
         String firstLineRegex = "^\\p{Space}{0,3}" + headingPatternDelimiter + "\\p{Space}+";
         String anyLineRegex = "\\r\\n?|\\n\\p{Space}{0,3}" + headingPatternDelimiter + "\\p{Space}+";
         Pattern headingPattern = Pattern.compile(firstLineRegex + "|" + anyLineRegex);
         Matcher headingPatternMatcher = headingPattern.matcher(documentText);
         boolean found = headingPatternMatcher.find();
         if(found){
           return DocumentTitle.splitOnHeaderLevelAtx(documentText, headingPatternMatcher);
         }
      }

      ListOrderedMap<String,String> sections = new ListOrderedMap<>();
      sections.put(sectionSplits[0], sectionSplits[0]);
      return sections;
   }

   /**
    * Split the document into lists
    *
    * @param documentText
    *           the body of the document
    * @param headingPatternMatcher
    *            the matcher which already matched the heading we are splitting on
    *
    * @return the as a list of maps
    */
   private static ListOrderedMap<String, String> splitOnHeaderLevelAtx(final String documentText, final Matcher headingPatternMatcher) {
      ListOrderedMap<String,String> sections = new ListOrderedMap<>();
      boolean foundCurrent = true;
      while(foundCurrent) {
         int headingPatternMatcherEnd = headingPatternMatcher.end();
         boolean foundNext = headingPatternMatcher.find();
         int headingPatternMatcherStartNext = foundNext ? headingPatternMatcher.start() : documentText.length();
         Matcher documentTextMatcher = lineSeparator.matcher(documentText);
         foundCurrent = documentTextMatcher.find(headingPatternMatcherEnd) && foundNext;
         String name = documentText.substring(headingPatternMatcherEnd, documentTextMatcher.start()).trim();
         String body = documentText.substring(documentTextMatcher.start(), headingPatternMatcherStartNext);
         String filteredBody = body.trim() + System.lineSeparator();
         sections.put(name, filteredBody);
         //foundCurrent = foundNext;
      }
      return sections;
   }

   /**
    * Split the document into lists
    *
    * @param sectionSplits
    *           the body of the document
    *
    * @return the as a list of maps
    */
   private static ListOrderedMap<String, String> splitOnHeaderLevel(final String[] sectionSplits) {
      ListOrderedMap<String,String> sections = new ListOrderedMap<>();
      for(int i=1; i<sectionSplits.length; i++) {
         String[] headerSectionLines = sectionSplits[i-1].split("\\r\\n?|\\n");
         String[] bodySectionLines = sectionSplits[i].split("\\r\\n?|\\n");
         String name = headerSectionLines[headerSectionLines.length-1].trim();
         List<String> bodySectionLinesAsList = Arrays.asList(
               i + 1 == sectionSplits.length
                     ? bodySectionLines : Arrays.copyOfRange(bodySectionLines, 0, bodySectionLines.length-1));
         String body = String.join(System.lineSeparator(), bodySectionLinesAsList);
         String filteredBody = body.trim() + System.lineSeparator();
         sections.put(name, filteredBody);
      }
      return sections;
   }
}
