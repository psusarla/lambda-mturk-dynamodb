package com.premiumgeek.mturk;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "cv-log")
public class CvLog {

	@DynamoDBHashKey(attributeName="uuid")
	private String uuid;
	
	@DynamoDBAttribute(attributeName = "answer")
	private String answer;
	
	@DynamoDBAttribute(attributeName = "client_id")
	private String clientId;
	
	@DynamoDBAttribute(attributeName = "hit_id")
	private String hitId;
	
	@DynamoDBAttribute(attributeName = "question")
	private String question;
	
	@DynamoDBAttribute(attributeName = "s3_image_url")
	private String imageUrl;
	
	@DynamoDBAttribute(attributeName = "timestamp_utc")
	private String timestamp;
	
	@DynamoDBAttribute(attributeName = "turk_worker_id")
	private String mturkWorkerId;

	@DynamoDBAttribute(attributeName = "from_address")
	private String fromAddress;

	@DynamoDBAttribute(attributeName = "camera_id")
	private String cameraId;
	
	@DynamoDBAttribute(attributeName = "subject")
	private String subject;
	
	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getCameraId() {
		return cameraId;
	}

	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getHitId() {
		return hitId;
	}

	public void setHitId(String hitId) {
		this.hitId = hitId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMturkWorkerId() {
		return mturkWorkerId;
	}

	public void setMturkWorkerId(String mturkWorkerId) {
		this.mturkWorkerId = mturkWorkerId;
	}
}
