package com.premiumgeek.mturk;

import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

public class MturkHitGenerator implements RequestHandler<S3EventNotification, String> {

	private Map<String, String> environmentVariables = new HashMap<String, String>();

	//Used for unit tests
	public Map<String, String> getEnvironmentVariables() {
		return environmentVariables;
	}

	//Used for unit tests
	public void setEnvironmentVariables(Map<String, String> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

    public String handleRequest(S3EventNotification s3EventNotification, Context context) {
		System.out.println("Recieved S3 event notification");
		this.environmentVariables = System.getenv();
		try {
			//Get the source bucket and object
			S3EventNotificationRecord record = s3EventNotification.getRecords().get(0);
			String srcBucket = record.getS3().getBucket().getName(); // Source bucket name
			// Object key may have spaces or unicode non-ASCII characters.
			String srcKey = record.getS3().getObject().getKey().replace('+', ' '); // Source file name
			srcKey = URLDecoder.decode(srcKey, "UTF-8");
			System.out.println("Bucket: " + srcBucket + ", Key: " + srcKey);
			
			process(srcBucket, srcKey);            
		} catch (Exception e) {
			System.out.println("Exception thrown " + e.getMessage());
		}
		return "DONE";
    }

	public void process(String srcBucket, String srcKey) {
		//Get the metadata 
		AmazonS3 s3Client = new AmazonS3Client();
	    System.out.println("Retrieving the S3 object, bucket: " + srcBucket + " key: " + srcKey);
		S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
		ObjectMetadata metadata = s3Object.getObjectMetadata();
		String fromAddress = metadata.getUserMetaDataOf("from-addresses");
		fromAddress = extractEmailAddress(fromAddress);
		String cameraId = metadata.getUserMetaDataOf("camera-id");
		String subject = metadata.getUserMetaDataOf("subject");
		
		System.out.println("Loading entry from cv-user table using fromAddress: " + fromAddress + " cameraId: " + cameraId);
		CvUser cvUser = loadCvUserFromTable(fromAddress, cameraId);
		if (cvUser == null) {
			System.out.println("Could not find a matching record in cv-user for fromAddress: " + fromAddress + " cameraId: " + cameraId);
			return;
		}
		
		if ((subject !=null) && subject.length() > 0) {
			cvUser.setMturkQuestion(subject);
		} else {
			metadata.addUserMetadata("x-amz-meta-subject", cvUser.getMturkQuestion());          	
		}

		URL url = s3Client.getUrl(srcBucket, srcKey);
		String s3FileURL = url.toString();
		
		MyHIT myHit = new MyHIT();
		System.out.println("Creating Hit using s3FileURL: " + s3FileURL + " cvUser: " + cvUser);
		HIT hit = myHit.createHitUsingLayout(s3FileURL,cvUser);    
		metadata.addUserMetadata("hitid", hit.getHITId());
		
		System.out.println("adding hit to get SQS notification");
		myHit.setHITupdateNotification(this.environmentVariables.get("NOTIFICATION_ENDPOINT"), hit.getHITTypeId());

		System.out.println("Updating S3 metadata");
		CopyObjectRequest request = new CopyObjectRequest(srcBucket, srcKey, srcBucket, srcKey).withNewObjectMetadata(metadata);
		s3Client.copyObject(request);

		System.out.println("Making the S3 object public");
		makeS3ObjectPublic(srcBucket, srcKey);

		System.out.println("Inserting to cv-log");
		insertToCvLog(s3FileURL, metadata);
	}

	public String extractEmailAddress(String fromAddress) {
		Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(fromAddress);
	    while (m.find()) { //While loop should loop only once
	        fromAddress = m.group();
	    }
		return fromAddress;
	}

	public void insertToCvLog(String s3FileURL, ObjectMetadata metadata) {
		//log to cv-log table
		CvLog cvLog = new CvLog();
		cvLog.setUuid(UUID.randomUUID().toString());
		cvLog.setHitId(metadata.getUserMetaDataOf("hitid"));
		cvLog.setImageUrl(s3FileURL);
		String gmt = getDateInGMT();
		cvLog.setTimestamp(gmt);
		cvLog.setCameraId(metadata.getUserMetaDataOf("camera-id"));
		cvLog.setSubject(metadata.getUserMetaDataOf("subject"));
		cvLog.setFromAddress(metadata.getUserMetaDataOf("from-addresses"));
		
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();
		ddbClient.withRegion(Regions.US_WEST_2);
		DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
		mapper.save(cvLog);
	}

	private String getDateInGMT() {
		Date date = new Date();
	    DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z");
	    df.setTimeZone(TimeZone.getTimeZone("GMT"));
	    String gmt = df.format(date);
		return gmt;
	}
    
	public String makeS3ObjectPublic(String bucket, String s3File)
	{
		AmazonS3 s3 = new AmazonS3Client();
		s3.setObjectAcl(bucket, s3File, CannedAccessControlList.PublicRead);
		return s3.getUrl(bucket, s3File).toString();
	}
   
	protected CvUser loadCvUserFromTable(String fromAddress, String cameraId) {
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient();
		ddbClient.withRegion(Regions.US_WEST_2);
		DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
		Map<String,AttributeValue> attributeValues = new HashMap<String,AttributeValue>();
		attributeValues.put(":email",new AttributeValue(fromAddress));
		attributeValues.put(":cameraId",new AttributeValue(cameraId));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		scanExpression.setFilterExpression("client_email = :email AND camera_id = :cameraId");

		scanExpression.setExpressionAttributeValues(attributeValues);
		PaginatedScanList<CvUser> cvUserList = null;
		try {
		 cvUserList = mapper.scan(CvUser.class, scanExpression);
		} catch (Exception e) {
			System.out.println("Exception thrown " + e.getMessage());
			return null;
		}
		
		if (cvUserList == null || cvUserList.size() == 0) {
			return null;
		}
  		return cvUserList.get(0);
	}
}