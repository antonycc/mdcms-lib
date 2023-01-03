Present
=======

Super lightweight content management system that uses Markdown as a storage format.
The content management system for https://www.diyaccounting.co.uk/

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
