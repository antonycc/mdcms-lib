package uk.co.diyaccounting.util.io;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import uk.co.diyaccounting.util.net.UrlHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to manage data sets
 */
@Service("fileLikePaths")
public class FileLikePathService {

   private static Logger logger = LoggerFactory.getLogger(FileLikePathService.class);

   private FileLikeStreamAndListFactory streamFactory = new FileLikeStreamAndListFactory();
   private UrlHelper url = new UrlHelper();

   public static String filePrefix = "urn:diyaccounting.co.uk:file://";
   public static String classpathPrefix = "urn:diyaccounting.co.uk:classpath:";
   public static String urlPrefix = "urn:diyaccounting.co.uk:url:";
   public static String s3Prefix = "urn:diyaccounting.co.uk:bucket:s3://";

   //private AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().build();
   private S3Client amazonS3Client = null;
   private S3Presigner presigner = null;

   public boolean filesystemSupported = true;
   public boolean classpathSupported = true;
   public boolean httpSupported = true;
   public boolean bucketSupported = false;
   public String bucketRegion = null;
   public String bucketEndpoint = null;

   public synchronized void initS3Client() {
      if(this.amazonS3Client == null) {
         try {
            // If a non-default endpoint is used (e.g. for localstack), then authenticate with dummy credentials.
            if (StringUtils.isNoneBlank(this.bucketEndpoint)) {
               // TODO: https://github.com/spring-cloud/spring-cloud-aws/issues/556
               String s3Endpoint = this.bucketEndpoint;
               URI endpointOverride = new URI(s3Endpoint);
               //final String region = Region.EU_London.toString();
               String region = (StringUtils.isNoneBlank(this.bucketRegion) ? this.bucketRegion : "us-east-1"); // Region.US_EAST_1.toString();
               String awsAccessKey = "xaccess";
               String awsSecretKey = "ysecret";
               logger.info("Initialising S3 with endpoint configuration: {} and region {}", s3Endpoint, region);
               AwsCredentials credentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
               AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
               S3Configuration s3Configuration = S3Configuration.builder()
                     .pathStyleAccessEnabled(true)
                     .build();
               // BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
               // AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(s3Endpoint, region);
               //this.amazonS3Client = AmazonS3ClientBuilder.standard()
               //      .withEndpointConfiguration(endpoint)
               //      .withCredentials(new AWSStaticCredentialsProvider(credentials))
               //      .withPathStyleAccessEnabled(true)
               //      .build();
               this.amazonS3Client = S3Client.builder()
                     .endpointOverride(endpointOverride)
                     .region(Region.of(region))
                     .credentialsProvider(credentialsProvider)
                     .serviceConfiguration(s3Configuration)
                     .build();
               this.presigner = S3Presigner.create();
            } else {
               logger.info("Initialising S3 with standard build");
               this.amazonS3Client = S3Client.builder().build();
               this.presigner = S3Presigner.create();
               //this.amazonS3Client = AmazonS3ClientBuilder.standard().build();
               //String s3Endpoint = "http://localhost:4566";
               //String region = "us-east-1"; // Region.US_EAST_1.toString();
               //AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(s3Endpoint, region);
               //this.amazonS3Client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpoint).build();
            }
            //String fakeBucketName = UUID.randomUUID().toString().replace("-", "");
            //this.amazonS3Client.doesBucketExistV2(fakeBucketName);
         } catch (Exception e) {
            logger.warn("Could not initialise S3", e);
         }
      }
   }

   public String getDownloadUrlForAssetAtUrn(final String baseUrl, final String resourceBaseUrn, final String resourcePath) throws IOException {
      String downloadUrl = null;
      String baseMessage = "Deprecation: getDownloadUrlForAssetAtUrn() is still using baseUrl when";
      if(resourcePath.startsWith(this.filePrefix)) {
         String filePath = resourcePath.substring(this.filePrefix.length());
         downloadUrl = this.getFileAsDownloadUrl(baseUrl, resourceBaseUrn, filePath);
         logger.warn(baseMessage + " resourcePath.startsWith(this.filePrefix) for baseUrl: " + baseUrl);
      }else if (resourcePath.startsWith(this.classpathPrefix)) {
         String classpath = resourcePath.substring(this.classpathPrefix.length());
         downloadUrl = this.getResourceAsDownloadUrl(baseUrl, resourceBaseUrn, classpath);
         logger.warn(baseMessage + " resourcePath.startsWith(this.filePrefix) for baseUrl: " + baseUrl);
      }else if (resourcePath.startsWith(this.urlPrefix)) {
         String url = resourcePath.substring(this.urlPrefix.length());
         downloadUrl = this.getHttpObjectAsDownloadUrl(url);
      }else if (resourcePath.startsWith(this.s3Prefix)) {
         String objectPath = resourcePath.substring(this.s3Prefix.length());
         downloadUrl = this.getS3ObjectAsDownloadUrl(objectPath);
      }else{
         throw new IOException("Unknown prefix (expected urn:diyaccounting.co.uk:[file://|classpath:|url:|bucket:s3://]) on path: " +
               resourcePath);
      }
      return downloadUrl;
   }

   public String getHostForUrn(final String resourcePath) throws IOException {
      String host = null;
      if(resourcePath.startsWith(this.filePrefix)) {
         logger.warn("Extracting a host for a none network protocol[{}]. Returning empty string.", resourcePath);
         host = "";
      }else if (resourcePath.startsWith(this.classpathPrefix)) {
         logger.warn("Extracting a host for a none network protocol[{}]. Returning empty string.", resourcePath);
         host = "";
      }else if (resourcePath.startsWith(this.urlPrefix)) {
         String url = resourcePath.substring(this.urlPrefix.length());
         URI uri = URI.create(url);
         host = uri.getHost();
      }else if (resourcePath.startsWith(this.s3Prefix)) {
         String objectPath = resourcePath.substring(this.s3Prefix.length());
         URI uri = URI.create(objectPath);
         host = uri.getHost();
         if(StringUtils.isBlank(host) && objectPath.contains("/")){
            host = objectPath.substring(0, objectPath.indexOf("/"));
         }
      }else{
         throw new IOException("Unknown prefix (expected urn:diyaccounting.co.uk:[file://|classpath:|url:|bucket:s3://]) on path: " +
               resourcePath);
      }
      logger.info("Extracted host [{}] from resourcePath [{}]", host, resourcePath);
      return host;
   }

   public InputStream getInputStreamForUrn(final String resourcePath) throws IOException {
      InputStream is;
      if(resourcePath.startsWith(this.filePrefix)) {
         String filePath = resourcePath.substring(this.filePrefix.length());
         is = this.getFileAsInputStream(filePath);
      }else if (resourcePath.startsWith(this.classpathPrefix)) {
         String classpath = resourcePath.substring(this.classpathPrefix.length());
         is = this.getResourceAsInputStream(classpath);
      }else if (resourcePath.startsWith(this.urlPrefix)) {
         String url = resourcePath.substring(this.urlPrefix.length());
         is = this.getHttpObjectAsInputStream(url);
      }else if (resourcePath.startsWith(this.s3Prefix)) {
         String objectPath = resourcePath.substring(this.s3Prefix.length());
         is = this.getS3ObjectAsInputStream(objectPath);
      }else{
         throw new IOException("Unknown prefix (expected urn:diyaccounting.co.uk:[file://|classpath:|url:|bucket:s3://]) on path: " +
               resourcePath);
      }
      return is;
   }

   public OutputStream getOutputStreamForUrn(final String resourcePath) throws IOException {
      OutputStream os;
      if(resourcePath.startsWith(this.filePrefix)) {
         String filePath = resourcePath.substring(this.filePrefix.length());
         os = this.getFileAsOutputStream(filePath);
      }else if (resourcePath.startsWith(this.classpathPrefix)) {
         String classpath = resourcePath.substring(this.classpathPrefix.length());
         os = this.getResourceAsOutputStream(classpath);
      }else if (resourcePath.startsWith(this.urlPrefix)) {
         String url = resourcePath.substring(this.urlPrefix.length());
         os = this.getHttpObjectAsOutputStream(url);
      }else if (resourcePath.startsWith(this.s3Prefix)) {
         String objectPath = resourcePath.substring(this.s3Prefix.length());
         os = this.getS3ObjectAsOutputStream(objectPath);
      }else{
         throw new IOException("Unknown prefix (expected urn:diyaccounting.co.uk:[file://|classpath:|url:|bucket:s3://]) on path: " +
               resourcePath);
      }
      return os;
   }

   public String[] getContentListForUrn(final String resourcePath, final String filter) throws  IOException{
      String[] contentItemNames = new String[0];

      if(resourcePath.endsWith(filter)){
         logger.warn("Path {} is an individual item not a path for filter {}. Returning empty list", resourcePath, filter);
         //int lastSlash = resourcePath.lastIndexOf("/");
         //if(lastSlash != -1){
         //   contentItemNames = new String[1];
         //   contentItemNames[0] = resourcePath.substring(lastSlash+1);
         //   logger.warn("Returning {} as single resource", contentItemNames[0]);
         //}else{
         //   logger.warn("Returning empty list");
         //}
      }else if(resourcePath.startsWith(this.filePrefix)) {
         String filePath = resourcePath.substring(this.filePrefix.length());
         contentItemNames = this.getContentListForFilepath(filePath, filter);
      }else if (resourcePath.startsWith(this.classpathPrefix)) {
         String resourcePackage = resourcePath.substring(this.classpathPrefix.length());
         contentItemNames = this.getContentListForClasspath(resourcePackage, filter);
      }else if (resourcePath.startsWith(this.urlPrefix)) {
         String urlPath = resourcePath.substring(this.urlPrefix.length());
         contentItemNames = this.getContentListForUrl(urlPath, filter);
      }else if (resourcePath.startsWith(this.s3Prefix)) {
         String s3Path = resourcePath.substring(this.s3Prefix.length());
         String bucketName = s3Path.substring(0, s3Path.indexOf("/"));
         contentItemNames = this.getContentListForBucket(bucketName, filter);
      }else{
         throw new IOException("Unknown prefix (expected urn:diyaccounting.co.uk:[file://|classpath]) on path: " + resourcePath);
      }
      return contentItemNames;
   }

   public String getFileAsDownloadUrl(final String baseUrl, final String resourceBaseUrn, final String resource) throws IOException {
      logger.debug("getFileAsDownloadUrl({})", resource);
      this.validateFilesystemSupport();
      return this.getStreamFactory().getFileAsDownloadUrl(baseUrl, resourceBaseUrn, resource);
   }

   /**
    * Obtain an input stream for a file
    *
    * @param resource
    *           - the file name of the resource to load
    * @return an Input Stream that will read the resource
    * @throws IOException
    *            if the resource cannot be loaded
    */
   public InputStream getFileAsInputStream(final String resource) throws IOException {
      logger.debug("getFileAsInputStream({})", resource);
      this.validateFilesystemSupport();
      return this.getStreamFactory().getFileAsInputStream(resource);
   }

   public OutputStream getFileAsOutputStream(final String resource) throws IOException {
      logger.debug("getFileAsOutputStream({})", resource);
      this.validateFilesystemSupport();
      return this.getStreamFactory().getFileAsOutputStream(resource);
   }

   public String[] getContentListForFilepath(final String path, final String filter) {
      logger.debug("getContentListForFilepath({}, {})", path, filter);
      this.validateFilesystemSupport();
      String pwd = System.getProperty("user.dir");
      logger.debug("In folder {}", pwd);
      logger.info("Retrieving content list from file path {} matching {}", path, filter);
      List<String> resources = this.getStreamFactory().getFilenamesForFilepath(path, filter);
      return this.inspectAndConvertToArray(path, filter, resources);
   }

   public void validateFilesystemSupport(){
      if(!this.filesystemSupported){
         throw new IllegalStateException("Filesystem file like objects are not supported.");
      }
   }

   public String getResourceAsDownloadUrl(final String baseUrl, final String resourceBaseUrn, final String resource) throws IOException {
      logger.debug("getResourceAsDownloadUrl({})", resource);
      this.validateClasspathSupport();
      return this.getStreamFactory().getResourceAsDownloadUrl(baseUrl, resourceBaseUrn, resource);
   }

   /**
    * Obtain an input stream for a classpath resource
    *
    * @param resource
    *           - the name of the classpath resource to load
    * @return an Input Stream that will read the resource
    * @throws IOException
    *            if the resource cannot be loaded
    */
   public InputStream getResourceAsInputStream(final String resource) throws IOException {
      logger.debug("getResourceAsInputStream({})", resource);
      this.validateClasspathSupport();
      return this.getStreamFactory().getResourceAsInputStream(resource);
   }

   public OutputStream getResourceAsOutputStream(final String resource) throws IOException {
      logger.debug("getResourceAsOutputStream({})", resource);
      this.validateClasspathSupport();
      return this.getStreamFactory().getResourceAsOutputStream(resource);
   }

   /**
    * Load the data from the classpath and return a Reader
    *
    * @param path
    *           the classpath to query for the resource containing the data
    * @return a reader from which to access the data
    * @throws IOException
    *            if the resource does not exist (or is not visible)
    */
   public Reader loadFileFromClasspath(final String path) throws IOException {
      logger.debug("loadFileFromClasspath({})", path);
      this.validateClasspathSupport();
      FileLikePathService.logger.debug("Loading: {}", path);
      InputStream is = this.getResourceAsInputStream(path);
      if (is == null) {
         throw new IOException("Could not open InputStream to: " + path);
      }
      return new InputStreamReader(is, UtilConstants.DEFAULT_ENCODING);
   }

   public String[] getContentListForClasspath(final String path, final String filter) {
      logger.debug("getContentListForClasspath({}, {})", path, filter);
      this.validateClasspathSupport();
      logger.info("Retrieving content list from package {} matching {}", path, filter);
      List<String> resources = this.getStreamFactory().getResourceNamesForClasspath(filter, path);
      return this.inspectAndConvertToArray(path, filter, resources);
   }

   public void validateClasspathSupport(){
      if(!this.classpathSupported){
         throw new IllegalStateException("Classpath file like objects are not supported.");
      }
   }

   public String getHttpObjectAsDownloadUrl(final String url) throws IOException {
      logger.debug("getHttpObjectAsDownloadUrl({})", url);
      this.validateHttpSupport();
      String downloadUrl = this.getStreamFactory().getHttpObjectAsDownloadUrl(url);
      return downloadUrl;
   }

   /**
    * Obtain an input stream for an HTTP object
    *
    * @param url
    *           - the url of the s3 object starting with the protocol
    * @return an Input Stream that will read the object
    * @throws IOException
    *            if the resource cannot be loaded
    */
   public InputStream getHttpObjectAsInputStream(final String url) throws IOException {
      logger.debug("getHttpObjectAsInputStream({})", url);
      this.validateHttpSupport();
      InputStream is = this.getStreamFactory().getHttpObjectAsInputStream(url);
      if(is == null){
         throw new IOException("Could not read HTTP object as stream: " + url);
      }
      return is;
   }

   public OutputStream getHttpObjectAsOutputStream(final String url) throws IOException {
      logger.debug("getHttpObjectAsOutputStream({})", url);
      this.validateHttpSupport();
      OutputStream os = this.getStreamFactory().getHttpObjectAsOutputStream(url);
      if(os == null){
         throw new IOException("Could not create output stream for HTTP object: " + url);
      }
      return os;
   }

   public String[] getContentListForUrl(final String path, final String filter) {
      logger.debug("getContentListForUrl({}, {})", path, filter);
      this.validateHttpSupport();
      logger.info("Retrieving content list from url {} matching {}", path, filter);
      ArrayList<String> resources = this.getFilenamesForUrl(path, filter);
      return this.inspectAndConvertToArray(path, filter, resources);
   }

   /**
    * Read directory browsing generated HTML
    */
   public ArrayList<String> getFilenamesForUrl(final String url, final String filter) {
      this.validateHttpSupport();
      String apacheFilePrefix = "<li><a href=\"";
      ArrayList<String> files = new ArrayList<>();
      try {
         InputStream is = this.getStreamFactory().getHttpObjectAsInputStream(url);
         Reader r = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(r);
         String line;
         while ((line = br.readLine()) != null) {
            if(line.startsWith(apacheFilePrefix)){
               String fileEntry = line.substring(apacheFilePrefix.length()).split("\"")[0];
               if(fileEntry.endsWith(filter)) {
                  String basename = FilenameUtils.getBaseName(fileEntry);
                  files.add(basename);
               }
            }
         }
      }catch(IOException e){
         logger.warn("Could not get directory listing for: " + url, e);
      }
      return files;
   }

   public void validateHttpSupport(){
      if(!this.httpSupported){
         throw new IllegalStateException("HTTP file like objects are not supported.");
      }
   }

   public String getS3ObjectAsDownloadUrl(final String objectPath) throws IOException {
      logger.debug("getS3ObjectAsDownloadUrl({})", objectPath);
      this.validateBucketSupport();
      this.initS3Client();
      String url = this.getStreamFactory().getS3ObjectAsDownloadUrl(this.amazonS3Client, this.presigner, objectPath);
      if(url == null){
         throw new IOException("Could not obtain download URL for S3 object: " + objectPath);
      }
      return url;
   }

   /**
    * Obtain an input stream for an Amazon S3 bucket.
    *
    * @param objectPath
    *           - the path to the s3 object starting with the bucket name
    * @return an Input Stream that will read the object
    * @throws IOException
    *            if the resource cannot be loaded
    */
   public InputStream getS3ObjectAsInputStream(final String objectPath) throws IOException {
      logger.debug("getS3ObjectAsInputStream({})", objectPath);
      this.validateBucketSupport();
      this.initS3Client();
      InputStream is = this.getStreamFactory().getS3ObjectAsInputStream(this.amazonS3Client, objectPath);
      if(is == null){
         throw new IOException("Could not read S3 object as stream: " + objectPath);
      }
      return is;
   }

   public OutputStream getS3ObjectAsOutputStream(final String objectPath) throws IOException {
      logger.debug("getS3ObjectAsOutputStream({})", objectPath);
      this.validateBucketSupport();
      this.initS3Client();
      OutputStream os = this.getStreamFactory().getS3ObjectAsOutputStream(this.amazonS3Client, objectPath);
      if(os == null){
         throw new IOException("Could not create S3 object output stream: " + objectPath);
      }
      return os;
   }

   public String[] getContentListForBucket(final String path, final String filter) {
      logger.debug("getS3ObjectAsOutputStream({}, {})", path, filter);
      this.validateBucketSupport();
      this.initS3Client();
      logger.info("Retrieving content list from s3 bucket {} matching {}", path, filter);
      List<String> resources = this.getStreamFactory().getObjectKeysForBucket(this.amazonS3Client, path, filter);
      return this.inspectAndConvertToArray(path, filter, resources);
   }

   public void validateBucketSupport(){
      if(!this.bucketSupported){
         throw new IllegalStateException("S3 bucket file like objects are not supported.");
      }
   }

   /**
    * Check if a file exists catching any exceptions and warning
    * 
    * @param path
    *           the path to check
    * @return true is the path can be confirmed to exist, false otherwise
    */
   public boolean exists(final String path) {
      File file = new File(path);
      return this.exists(file);
   }

   /**
    * Check if a file exists catching any exceptions and warning
    * 
    * @param file
    *           the file to check
    * @return true is the path can be confirmed to exist, false otherwise
    */
   public boolean exists(final File file) {
      try {
         return file.exists();
      } catch (SecurityException e) {
         FileLikePathService.logger.warn("Attempted to access inaccessible path: {} because of {}", file.getPath(), e.getClass().getSimpleName());
      } catch (Throwable e) {
         FileLikePathService.logger.warn("Exception checking path: {} because of {}", file.getPath(), e.getClass().getSimpleName());
      }
      return false;
   }

   private String[] inspectAndConvertToArray(final String path, final String filter, final List<String> resources) {
      if(resources.isEmpty()){
         logger.warn("Found no matching items in {} for filter {}", path, filter);
         return new String[0];
      }
      logger.info("Found {} matching items in {} for filter {}", resources.size(), path, filter);
      String[] contentItemNames = new String[resources.size()];
      contentItemNames = resources.toArray(contentItemNames);
      return contentItemNames;
   }

   public void setFilesystemSupported(final boolean filesystemSupported) {
      this.filesystemSupported = filesystemSupported;
   }

   public void setClasspathSupported(final boolean classpathSupported) {
      this.classpathSupported = classpathSupported;
   }

   public void setHttpSupported(final boolean httpSupported) {
      this.httpSupported = httpSupported;
   }

   public void setBucketSupported(final boolean bucketSupported) {
      this.bucketSupported = bucketSupported;
   }

   public void setBucketRegion(final String bucketRegion) {
      this.bucketRegion = bucketRegion;
   }

   public void setBucketEndpoint(final String bucketEndpoint) {
      this.bucketEndpoint = bucketEndpoint;
   }

   public FileLikeStreamAndListFactory getStreamFactory() {
      return this.streamFactory;
   }

   public void setStreamFactory(final FileLikeStreamAndListFactory streamFactory) {
      this.streamFactory = streamFactory;
   }


   public S3Client getAmazonS3Client() {
      return amazonS3Client;
   }

   public void setAmazonS3Client(final S3Client amazonS3Client) {
      this.amazonS3Client = amazonS3Client;
   }
}
