package uk.co.polycode.mdcms.util.io;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FileLikeStreamAndListFactory {

   private static Logger logger = LoggerFactory.getLogger(FileLikeStreamAndListFactory.class);

   public FileLikeStreamAndListFactory() {
   }

   // Filesystem

   public String getFileAsDownloadUrl(final String baseUrl, final String resourceBaseUrn, final String resource) throws IOException {
      logger.info("Generating download URL for file: {}", resource);
      String fileName = FilenameUtils.getBaseName(resource) + "." + FilenameUtils.getExtension(resource);
      String encodedResourceUrn = this.buildEncodedUrn(resourceBaseUrn, fileName);
      String encodedFileName = UriUtils.encode(fileName, Charset.defaultCharset());
      String downloadUrl = baseUrl + encodedResourceUrn + "/" + encodedFileName;
      return downloadUrl;
   }

   private String buildEncodedUrn(final String resourceBaseUrn, final String fileName) {
      String resourceUrn = resourceBaseUrn + fileName;
      //String encodedResourceUrn = UriUtils.encode(resourceUrn, Charset.defaultCharset()).replace(":", "%3A");
      String encodedResourceUrn = Base64.getEncoder().encodeToString(resourceUrn.getBytes());
      return encodedResourceUrn;
   }

   public InputStream getFileAsInputStream(final String resource) throws IOException {
      logger.info("Loading file: {}", resource);
      // InputStream is = ClassLoader.getSystemResourceAsStream(path);
      return new FileInputStream(resource);
   }

   public OutputStream getFileAsOutputStream(final String resource) throws IOException {
      logger.info("Creating output stream for file: {}", resource);
      return new FileOutputStream(resource);
   }

   public List<String> getFilenamesForFilepath(final String path, final String filter) {
      File dir = new File(path);
      //File [] files = dir.listFiles();
      File [] files = dir.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
            logger.trace("Checking {}", name);
            return name.endsWith(filter);
         }
      });
      if(ArrayUtils.isEmpty(files)){
         //String msg = "Found no matching items found using for filter: " + filter;
         //ContentException ce = new ContentException(msg, path);
         //ce.fillInStackTrace();
         //logger.error(msg, ce);
      }
      List<String> l = new ArrayList<>();
      for (File file : files) {
         String basename = FilenameUtils.getBaseName(file.getName());
         l.add(basename);
      }
      return l;
   }

   // Classpath

   public String getResourceAsDownloadUrl(final String baseUrl, final String resourceBaseUrn, final String resource) throws IOException {
      logger.info("Generating download URL for resource: {}", resource);
      String fileName = FilenameUtils.getBaseName(resource) + "." + FilenameUtils.getExtension(resource);
      String encodedResourceUrn = this.buildEncodedUrn(resourceBaseUrn, fileName);
      String encodedFileName = UriUtils.encode(fileName, Charset.defaultCharset());
      String downloadUrl = baseUrl + encodedResourceUrn + "/" + encodedFileName;
      return downloadUrl;
   }

   public InputStream getResourceAsInputStream(final String resource) {
      logger.info("Loading resource: {}", resource);
      // InputStream is = ClassLoader.getSystemResourceAsStream(path);
      //return StreamFactory.class.getResourceAsStream(resource);
      return this.getClass().getResourceAsStream(resource);
   }

   // Update thus guy if we can solve the problem
   // https://stackoverflow.com/questions/61851505/how-do-i-write-a-file-to-my-classpathresource-folder-in-spring-boot
   public OutputStream getResourceAsOutputStream(final String resource) throws IOException{
      logger.info("Creating output stream for resource: {}", resource);
      URLConnection connection = this.getClass().getResource(resource).openConnection();
      connection.setDoOutput(true);
      return connection.getOutputStream();
   }

   public List<String> getResourceNamesForClasspath(final String filter, final String path) {
      String resourcePackage = path.replace("/", ".");
      ConfigurationBuilder configuration = new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(resourcePackage))
            .setScanners(new ResourcesScanner()
            );
      Reflections reflections = new Reflections(configuration);
      String pattern = ".*" + filter.replace(".","\\.");
      Set<String> resources = reflections.getResources(Pattern.compile(pattern));
      String filterPackage = path.startsWith("/") ? path.substring(1) : path ;
      List<String> l = new ArrayList<>();
      for (String resource : resources) {
         if(resource.startsWith(filterPackage)){
            String filename = resource.substring(filterPackage.length());
            String basename = FilenameUtils.getBaseName(filename);
            l.add(basename);
         }
      }
      return l;
   }

   // Http

   public String getHttpObjectAsDownloadUrl(final String urlString) throws IOException {
      logger.info("Generating download URL for httpObject: {}", urlString);
      return urlString;
   }

   public InputStream getHttpObjectAsInputStream(final String urlString) throws IOException {
      logger.info("Loading HTTP object: {}", urlString);
      URL url = new URL(urlString);
      return url.openStream();
   }

   public OutputStream getHttpObjectAsOutputStream(final String urlString) throws IOException {
      logger.info("Writing to HTTP object: {}", urlString);
      URL url = new URL(urlString);
      URLConnection connection = url.openConnection();
      connection.setDoOutput(true);
      return connection.getOutputStream();
   }

   // S3 Bucket

   public String getS3ObjectAsDownloadUrl(final S3Client amazonS3Client, final S3Presigner presigner, final String objectPath) throws IOException {
      logger.info("Generating download URL for s3 Object: {}", objectPath);
      String bucketName = "none";
      String objectKey = "none";
      try {
         bucketName = objectPath.substring(0, objectPath.indexOf("/"));
         objectKey = objectPath.substring(objectPath.indexOf("/") + 1);
         //HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucketName).build();
         HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
         amazonS3Client.headBucket(headBucketRequest);
         // Throws NoSuchBucketException - The specified bucket does not exist.
         //if(!amazonS3Client.doesBucketExistV2(bucketName)){
         //   throw new IOException("Bucket does not exist [" + bucketName + "]");
         //}
         logger.info("Requesting download URL for S3 object in bucket: {} key: {}", bucketName, objectKey);
         GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
         GetObjectPresignRequest getObjectPresignRequest =
               GetObjectPresignRequest.builder()
                     .signatureDuration(Duration.ofMinutes(10))
                     .getObjectRequest(getObjectRequest)
                     .build();
         PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
         String downloadUrl = presignedGetObjectRequest.url().toString();
         //S3Object s3Object = amazonS3Client.getObject(bucketName, objectKey);
         //InputStream is = s3Object.getObjectContent();
         logger.info("Got download URL {} for S3 object from bucket: {} key: {}", downloadUrl, bucketName, objectKey);
         return downloadUrl;
      }catch (Exception e){
         throw new IOException("Could not create download URL for bucket: [" + bucketName + "] object: [" + objectKey + "]", e);
      }

   }

   public InputStream getS3ObjectAsInputStream(final S3Client amazonS3Client, final String objectPath) throws IOException {
      logger.info("Loading S3 object: {}", objectPath);
      String bucketName = "none";
      String objectKey = "none";
      try {
         bucketName = objectPath.substring(0, objectPath.indexOf("/"));
         objectKey = objectPath.substring(objectPath.indexOf("/") + 1);
         //HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucketName).build();
         HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
         amazonS3Client.headBucket(headBucketRequest);
         // Throws NoSuchBucketException - The specified bucket does not exist.
         //if(!amazonS3Client.doesBucketExistV2(bucketName)){
         //   throw new IOException("Bucket does not exist [" + bucketName + "]");
         //}
         logger.info("Loading S3 object from bucket: {} key: {}", bucketName, objectKey);
         GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
         InputStream is = amazonS3Client.getObject(getObjectRequest);
         //S3Object s3Object = amazonS3Client.getObject(bucketName, objectKey);
         //InputStream is = s3Object.getObjectContent();
         logger.info("Got stream for S3 object from bucket: {} key: {}", bucketName, objectKey);
         return is;
      }catch (Exception e){
         throw new IOException("Could not create InputStream for bucket: [" + bucketName + "] object: [" + objectKey + "]", e);
      }
   }

   // Update this guy: https://stackoverflow.com/questions/40262512/how-to-get-outputstream-from-an-s3object
   public OutputStream getS3ObjectAsOutputStream(final S3Client amazonS3Client, final String objectPath) throws IOException {
      logger.info("Creating output stream for S3 object: {}", objectPath);
      String bucketName = "none";
      String objectKey = "none";
      try {
         bucketName = objectPath.substring(0, objectPath.indexOf("/"));
         objectKey = objectPath.substring(objectPath.indexOf("/") + 1);
         HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
         amazonS3Client.headBucket(headBucketRequest);
         //if(!amazonS3Client.doesBucketExistV2(bucketName)){
         //   throw new IOException("Bucket does not exist [" + bucketName + "]");
         //}
         logger.info("Creating output stream for S3 object from bucket: {} key: {}", bucketName, objectKey);
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         BufferedS3OutputStream bs3os = new BufferedS3OutputStream(baos, amazonS3Client, bucketName, objectKey);
         logger.debug("Got stream for S3 object from bucket: {} key: {}", bucketName, objectKey);
         return bs3os;
      }catch (Exception e){
         throw new IOException("Could not create InputStream for bucket: [" + bucketName + "] object: [" + objectKey + "]", e);
      }
   }

   public ArrayList<String> getObjectKeysForBucket(final S3Client amazonS3Client, final String bucketName, final String filter) {
      // Read bucket and filter on the postfix
      ArrayList<String> files = new ArrayList<>();
      try {
         //ListObjectsV2Result listObjects = null;
         HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
         amazonS3Client.headBucket(headBucketRequest);
         //if(amazonS3Client.doesBucketExistV2(bucketName)) {
         //do {
         logger.info("Bucket [{}] exists, reading objects...", bucketName);
         //listObjects = amazonS3Client.listObjectsV2(bucketName);
         //List<S3ObjectSummary> summaries = listObjects.getObjectSummaries();
         //logger.info("Found {} objects", summaries.size());
         ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
         ListObjectsV2Iterable responses = amazonS3Client.listObjectsV2Paginator(listObjectsV2Request);
         //for (S3ObjectSummary objectSummary : listObjects.getObjectSummaries()) {
         for (ListObjectsV2Response response : responses) {
            for(S3Object objectSummary : response.contents()) {
               String fileEntry = objectSummary.key();
               logger.debug("Checking key [{}] ends with {}", fileEntry, filter);
               if (fileEntry.endsWith(filter)) {
                  String basename = FilenameUtils.getBaseName(fileEntry);
                  logger.info("Adding matching file {}", basename);
                  files.add(basename);
               }
            }
         }
         //} while (listObjects.isTruncated());
         //}else{
         //   logger.warn("Bucket does not exist [{}]", bucketName);
         //}
      }catch(Exception e){
         logger.warn("Could not get bucket listing for: [{}]", bucketName);
         logger.warn(e.getMessage(), e);
      }
      return files;
   }

}
