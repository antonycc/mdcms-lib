mdcms-lib
=========

Super lightweight content management system that uses Markdown as a storage format.
The content management system for https://www.diyaccounting.co.uk/

"Document Title"
================

A query structure for Markdown documents. I started with "Document Title" as an
example document title, and it seems a good project title, we have
Document Title. Which was later released as mdcms-lib from the naming used
within the DIY Accounting web app which is also being open sourced. When this
library is worthy of a name we might go back to Document Title.

To replace a CMS based on Atlassian Confluence's XHTML exports, the intention is
to move to a BitBucket repository as a CMS and PipeLines as the publishing tool.
The visual editing aspect of Markdown was attractive but as I write this it
occurs to me that I can just import the latest XHTML export to BitBucket. All
the switch to BitBucket can be achieved before Document Title is needed. We
have Document Title.

We have some examples of Document Title's Markdown query syntax. It's not a
language, every thing is an array, indexed by labels or zero indexed. The
value of an array item which contains a child array item, is an array, unless
it looks like one of the structures which are:
- Text, this section is free text, and it's Markdown, it could be HTML.
- List, the objectName section is a list so are its items and child section.
- Table, the headers are not special, the rows are pipe delimited members.
- Image, an image is parsed into a list of alt value, url value, hover value

Did you notice that the image is a list and the image attribute name value
pairs are lists. Everything is a list in Document Title. I do not think the
title, Document Title is a list. It could be though, why not? Text is a List.
What is a sentence if not a list of words? Let's go nuts, file names are lists.
File names are period delimited lists. Document Title is a list.

Tables are awkward because the hierarchy starts with the rows. Rows are lists
the children of rows are items in columns. The index of the row comes first

Examples of path like expressions:
```
@ = "README.md"
 = <The whole document>
/ = <The children of whole document>
@/0 = "README"
@/0/0 = "R"
/0/@ = "Document Title"
/0/@/1 = "Title"
/0 = <This whole section>
/1/@ = "objectName"
/1 = Some pre-amble. singleAttribute1... listAttribute... Child section...
/1/ = singleAttribute1... listAttribute... Child section...
/objectName/singleAttribute1 = singleValue1
/objectName/listAttribute = listValue1 listValue2
/objectName/listAttribute/@ = listAttribute
/objectName/listAttribute/1 = listValue2
/objectName/2/0 = listValue1
/objectName/Child section/@ = Child section
/objectName/Child section = singleAttribute2
/objectName/Child section/singleAttribute2 = singleValue2
/objectName/3/@ = Child section
/objectName/3/0/@ = singleAttribute2
/objectName/3/0 = singleValue2
/objectName/Child section/@ = Child section
/tableName/0/@ = Tables
/tableName/2/0 = col 1 is
/tableName/3/Are = <strong>centered</strong>
/tableName/Col 2 is/2 = $12
/tableName/4/Cool = alt alt text, url https://githu..., hover Logo Title Text 1
/body = <p><strong>strong html text</strong></p>
/An Image/alt/@ = alt
/An Image/alt = alt text
/An Image/alt/0 = alt
/An Image/1 = url https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png
/An Image/hover = "Logo Title Text 1"
/An Image/url = https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png
```

objectName
===========
Some pre-amble.

- singleAttribute1
    - singleValue1
- listAttribute
    - listValue1
    - listValue2

# Atx section
## atxSingleAttribute1
### atxSingleValue1
## atxListAttribute
### atxListValue1
### atxListValue2

Child section
-------------

- singleAttribute2
    - singleValue2

tableName
==========
| Tables   |      Are      |  Cool |
|----------|:-------------:|------:|
| col 1 is |  left-aligned | $1600 |
| col 2 is |    <strong>centered</strong>   |   $12 |
| col 3 is | right-aligned |   ![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1") |
An Image
========
![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

body
----
<p>
	<strong>strong html text</strong>
</p>


Development environment set-up
==============================

Maven build
-----------
```bash
$ which mvn && mvn --version
/usr/local/bin/mvn
Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
Maven home: /usr/local/Cellar/maven/3.8.6/libexec
Java version: 11.0.16.1, vendor: Homebrew, runtime: /usr/local/Cellar/openjdk@11/11.0.16.1_1/libexec/openjdk.jdk/Contents/Home
Default locale: en_GB, platform encoding: UTF-8
OS name: "mac os x", version: "13.0.1", arch: "x86_64", family: "mac"
$ mvn clean install
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  51.865 s
[INFO] Finished at: 2022-12-31T22:55:15+01:00
[INFO] ------------------------------------------------------------------------
$
```

Releasing
=========

# two stage release
```bash
$ mvn release:prepare
$ mvn release:perform
```

# two stage release re-tried
```bash
$ mvn release:prepare -Dresume=false
$ mvn release:perform
```

# full console run - the middle mnv line is suitable for a CI server with it's own workspace
```bash
$ mvn - -batch-mode clean release:clean release:prepare -DdryRun=true
$ git reset - -hard HEAD && find . -name "*.next" -type f -exec rm "{}" \; && find . -name "*.tag" -type f -exec rm "{}" \;
$ mvn - -batch-mode clean release:clean release:prepare
$ mvn release:clean
```

Backlog
=======

MDCMS Enhancements:
```
[ ] Add Swagger
[ ] Minimise dependencies
[ ] FileHelper / SteamHelper should be initialised as Spring beans with a get/list interface for each persistnce adaptor
[ ] Use "XxxxPage" as the pageId not "page" - HTML and ApiController change
[ ] Use "PageXxxx" as the resource name
[ ] Change StreamFactory filters to compile a reg ex for all but s3
[ ] Change S3 list matcher to use the built-in pre-fix matching (instead of the regex) 
[ ] Add DynamoDb as a content source
[ ] Import s3 sales ++ catalogue into a relational store as a data pipeline
[ ] Make the default to look for a matching attribute name so @MdContent(path = "/keywords") => @MdContent() private String keywords;
[ ] Find solution for images to be published directly online.
[ ] Consider a Spring library reduction tailored to an API based webapp rather than a webapp
[ ] Link the page update timestamp metadata to the content file updates 
[ ] Align cms-lib with: https://www.markdownguide.org/cheat-sheet/ especially unordered lists [ -\n -\n -\n]
[ ] Change a docker based app to pull the content from HTTP instead of a filesystem resource
urn:mdcms:https:mysite.com/my-bucket/someobj
```

MDCMS JS:
```
[ ] Filesystem only version: The JS locates the md files from a relative path and converts them to JSON for HandleBarsJS 
[ ] Filesystem only version: Mapping object links JSON keys to types and a path in MD document (like MDContent).
[ ] Clean the flow content in the CMS Something like https://github.com/apostrophecms/sanitize-html
```
