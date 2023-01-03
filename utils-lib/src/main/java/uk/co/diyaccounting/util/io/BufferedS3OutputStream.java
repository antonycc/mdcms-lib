package uk.co.diyaccounting.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BufferedS3OutputStream extends BufferedOutputStream {
	private static final Logger logger = LoggerFactory.getLogger(BufferedS3OutputStream.class);
	public ByteArrayOutputStream baos;
	public S3Client amazonS3Client;
	public String bucketName;
	public String objectKey;

	public BufferedS3OutputStream(
			ByteArrayOutputStream baos,
			final S3Client amazonS3Client,
	      final String bucketName,
			final String objectKey) {
		super(baos);
		this.baos = baos;
		this.amazonS3Client = amazonS3Client;
		this.bucketName = bucketName;
		this.objectKey = objectKey;
	}

	// https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3.html#putObject-java.lang.String-java.lang.String-java.lang.String-
	@Override
	public synchronized void flush() throws IOException {
		super.flush();
		byte[] bytes = this.baos.toByteArray();
		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(objectKey).build();
		RequestBody requestBody = RequestBody.fromBytes(bytes);
		PutObjectResponse response = amazonS3Client.putObject(putObjectRequest, requestBody);
		if(response.sdkHttpResponse().isSuccessful()){
			logger.info("Successfully written {} bytes to bucket {} object {}", bytes.length, this.bucketName, this.objectKey);
		}else{
			logger.warn("Failed to write {} bytes to bucket {} object {}", bytes.length, this.bucketName, this.objectKey);
		}
		//ObjectMetadata metadata = null;
		//ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		//PutObjectResult result = this.amazonS3Client.putObject(this.bucketName, this.objectKey, bais, metadata);
		//long bytesWritten = result.getMetadata().getContentLength();
		//if(bytesWritten != bytes.length){
		//	logger.warn("Only {} bytes written out of {} for bucket {} object {}", bytesWritten, bytes.length, this.bucketName, this.objectKey);
		//}else{
		//	logger.info("Wrote bytes to bucket {} object {}", bytesWritten, this.bucketName, this.objectKey);
		//}
	}
}
