package uk.co.polycode.mdcms.system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
public class S3StreamTest {

   private static final Logger logger = LoggerFactory.getLogger(S3StreamTest.class);

   // Use localstack endpoint, created with:
   // docker compose build
   // docker compose up
   // <new tty>
   // Add to /etc/hosts:
   // 127.0.0.1       localhost bucket.localhost
   // The @Before method creates the objects using Java but here is the AWS CLI equivalent
   // aws --endpoint-url=http://localhost:4566 s3 mb s3://bucket
   // aws --endpoint-url=http://localhost:4566 s3 cp ./src/test/resources/s3resource.txt s3://bucket/s3resource.txt
   // aws --endpoint-url=http://localhost:4566 s3 cp ./src/test/resources/s3resource.txt s3://bucket/s3resource2.txt
   // aws --endpoint-url=http://localhost:4566 s3 ls s3://bucket
   // >> 2021-05-15 22:37:01         12 s3resource.txt
   // >> 2021-05-15 22:37:06         12 s3resource2.txt

   @Autowired
   @Qualifier("fileLikePaths")
   private FileLikePathService fileLikePathService;

   private String s3Bucket = "bucket";

   private String s3ObjectKey = "s3resource.txt";

   private String s3Resource = this.s3Bucket + "/" + this.s3ObjectKey;

   private String expectedS3Resource = "S3 resource";

   @Before
   public void copyFiles(){
      this.fileLikePathService.initS3Client();
      CreateBucketRequest bucketRequest = CreateBucketRequest.builder().bucket(this.s3Bucket).build();
      this.fileLikePathService.getAmazonS3Client().createBucket(bucketRequest);
      PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(this.s3Bucket).key(this.s3ObjectKey).build();
      RequestBody requestBody = RequestBody.fromString(this.expectedS3Resource);
      this.fileLikePathService.getAmazonS3Client().putObject(putObjectRequest, requestBody);
      CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
            .copySource(this.s3ObjectKey)
            .destinationBucket(this.s3ObjectKey)
            .destinationKey("s3resource2.txt")
            .build();
      this.fileLikePathService.getAmazonS3Client().copyObject(copyObjectRequest);
      //this.fileLikePathService.getAmazonS3Client().createBucket(this.s3Bucket);
      //this.fileLikePathService.getAmazonS3Client().putObject(this.s3Bucket, this.s3ObjectKey, this.expectedS3Resource);
      //this.fileLikePathService.getAmazonS3Client().copyObject(this.s3Bucket, this.s3ObjectKey, this.s3Bucket, "s3resource2.txt");
   }

   @Test
   public void testS3Object() throws IOException {

      // Test parameters

      // Create file a helper instance that returns a broken input stream

      // Instance to test
      FileLikePathService classUnderTest = this.fileLikePathService;
      //classUnderTest.getStreamFactory().setAmazonS3Client(this.amazonS3Client);

      // Read content into an Input Source
      InputStream actualIS = classUnderTest.getS3ObjectAsInputStream(this.s3Resource);
      Reader r = new InputStreamReader(actualIS);
      BufferedReader br = new BufferedReader(r);
      String line;

      // Read the resource into a list parsing each line
      String resource = null;
      StringBuilder buf = new StringBuilder();
      while ((line = br.readLine()) != null) {
         buf.append(line);
      }
      resource = buf.toString();
      S3StreamTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedS3Resource, resource);
   }

   @Test(expected = IOException.class)
   public void testAndFailToLoadS3Object() throws IOException {

      // Instance to test
      FileLikePathService classUnderTest = this.fileLikePathService;
      //classUnderTest.getStreamFactory().setAmazonS3Client(this.amazonS3Client);

      // Read content into an Input Source
      classUnderTest.getS3ObjectAsInputStream(this.s3Resource + UUID.randomUUID().toString());
   }

   @Test
   public void expectPagesFromBucket() {

      String[] expectedPageList = {"s3resource.txt", "s3resource2.txt"};
      String bucket = "bucket";
      String filter = ".txt";

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = this.fileLikePathService;
      //classUnderTest.getStreamFactory().setAmazonS3Client(this.amazonS3Client);

      // Execute
      String[] actualPageList = classUnderTest.getContentListForBucket(bucket, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
            expectedPageList.length,
            actualPageList.length);
   }
}