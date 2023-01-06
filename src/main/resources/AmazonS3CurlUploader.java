/**
 * Usage:
 *    javac AmazonS3CurlUploader.java
 *    echo 'Hello World!' > test.txt
 *    java -cp "." AmazonS3CurlUploader "diyaccounting-polycode-install" te*.txt
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AmazonS3CurlUploader {

	private static final String ALGORITHM = "HmacSHA1";
	private static final String CONTENT_TYPE = "application/octet-stream";
	private static final String ENCODING = "UTF8";

	public static void main(String[] args){
		AmazonS3CurlUploader s3 = new AmazonS3CurlUploader();
		Path localFile = FileSystems.getDefault().getPath(".", args[1]);
		String s3Bucket = args[0];
		String s3FileName = args[1];
		String s3AccessKey = System.getenv("AWS_ACCESS_KEY_ID");
		String s3SecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
		s3.delete(localFile, s3Bucket, s3FileName, s3AccessKey, s3SecretKey);
		s3.upload(localFile, s3Bucket, s3FileName, s3AccessKey, s3SecretKey);
	}

	public void delete(Path localFile, String s3Bucket, String s3FileName, String s3AccessKey, String s3SecretKey) {
		boolean result;
		try {
			Process delete = this.createDelete(localFile, s3Bucket, s3FileName, s3AccessKey, s3SecretKey);
			delete.waitFor();
			System.out.println(this.convertStreamToString(delete.getInputStream()));
			//System.out.println(this.convertStreamToString(delete.getErrorStream()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace(System.out);
		}
	}

	public void upload(Path localFile, String s3Bucket, String s3FileName, String s3AccessKey, String s3SecretKey) {
		boolean result;
		try {
			Process put = this.createPut(localFile, s3Bucket, s3FileName, s3AccessKey, s3SecretKey);
			put.waitFor();
			System.out.println(this.convertStreamToString(put.getInputStream()));
			//System.out.println(this.convertStreamToString(put.getErrorStream()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace(System.out);
		}
	}

	private Process createDelete(Path file, String bucket, String fileName,
	                             String accessKey, String secretKey) throws IOException {
		String datetimeStr = this.createDatetime();
		String signature = this.createSignature(datetimeStr, bucket, fileName, "DELETE", secretKey);
		return new ProcessBuilder(
				"curl", "--include", "--silent",
				"--request", "DELETE", "http://" + bucket + ".s3.amazonaws.com/" + fileName,
				"--header", "Host: " + bucket + ".s3.amazonaws.com",
				"--header", "Date: " + datetimeStr,
				"--header", "Content-Type: " + CONTENT_TYPE,
				"--header", "Authorization: AWS " + accessKey + ":" + signature)
				.start();
	}

	private Process createPut(Path file, String bucket, String fileName,
	                                  String accessKey, String secretKey) throws IOException {
		String datetimeStr = this.createDatetime();
		String signature = this.createSignature(datetimeStr, bucket, fileName, "PUT", secretKey);
		return new ProcessBuilder(
				"curl", "--include", "--silent",
				"--request", "PUT", "http://" + bucket + ".s3.amazonaws.com/" + fileName,
				"--header", "Host: " + bucket + ".s3.amazonaws.com",
				"--header", "Date: " + datetimeStr,
				"--header", "Content-Type: " + CONTENT_TYPE,
				"--header", "Authorization: AWS " + accessKey + ":" + signature,
				"--upload-file", file.toString())
				.start();
	}

	private String createDatetime(){
		return ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
	}

	private String createSignature(String datetimeStr, String bucket, String fileName, String action, String secretKey){
		String stringToSign = action + "\n\n" + CONTENT_TYPE + "\n" + datetimeStr + "\n" + "/" + bucket + "/" + fileName;
		return Base64.getEncoder().encodeToString(this.hmacSHA1(stringToSign, secretKey));
	}

	private byte[] hmacSHA1(String data, String key) {
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM));
			return mac.doFinal(data.getBytes(ENCODING));
		} catch (NoSuchAlgorithmException | InvalidKeyException
				| UnsupportedEncodingException e) {
			return new byte[] {};
		}
	}

	private String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
