Document Title
==============

A query structure for Markdown documents. I started with "Document Title" as an
example document title and it seems a good project title, we have 
Document Title.

To replace a CMS based on Atlassian Confluence's XHTML exports, the intention is 
to move to a BitBucket repository as a CMS and PipeLines as the publish tool. 
The visual editing aspect of Markdown was attractive but as I write this it 
occurs to me that I can just import the latest XHTML export to BitBucket. All
of the switch to BitBucket can be achieved before Document Title is needed. We 
have Document Title.

We have some examples of Document Title's Markdown query syntax. It's not a 
language, every thing is an array, indexed by labels or zero indexed. The 
value of an array item which contains a child array item, is an array, unless 
it looks like one of the structures which are:
- Text, this section is free text and it's Markdown, it could be HTML.
- List, the objectName section is a list so are it's items and child section.
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


