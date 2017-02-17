package com.premiumgeek.mturk;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "cv-user")
public class CvUser {

	@DynamoDBAttribute(attributeName = "title")
	private String title;
	
	@DynamoDBAttribute(attributeName = "alert_message")
	private String alertMessage;
	
	@DynamoDBAttribute(attributeName = "AssignmentDurationInSeconds")
	private Integer assignmentDurationInSeconds;
	
	@DynamoDBAttribute(attributeName = "client_address")
	private String clientAddress;
	
	@DynamoDBAttribute(attributeName = "client_email")
	private String clientEmail;
	
	@DynamoDBAttribute(attributeName = "contact_number")
	private String contactNumber;
	
	@DynamoDBAttribute(attributeName = "description")
	private String description;
	
	@DynamoDBAttribute(attributeName = "device_mac_address")
	private String deviceMacAddress;
	
	@DynamoDBAttribute(attributeName = "hit_rate")
	private Double hitRate;
	
	@DynamoDBAttribute(attributeName = "HITLayoutId")
	private String hitLayoutId;
	
	@DynamoDBAttribute(attributeName = "HITTypeID")
	private String hitTypeId;
	
	@DynamoDBAttribute(attributeName = "LifetimeInSeconds")
	private Double lifetimeInSeconds;
	
	@DynamoDBAttribute(attributeName = "MaxAssignments")
	private Integer maxAssignments;
	
	@DynamoDBAttribute(attributeName = "monitoring_interval_minutes")
	private Integer monitoringIntervalMinutes;
	
	@DynamoDBAttribute(attributeName = "mturk_ans1")
	private String mturkAns1;
	
	@DynamoDBAttribute(attributeName = "mturk_ans2")
	private String mturkAns2;
	
	@DynamoDBAttribute(attributeName = "mturk_ans3")
	private String mturkAns3;
	
	@DynamoDBAttribute(attributeName = "mturk_ans4")
	private String mturkAns4;
	
	@DynamoDBAttribute(attributeName = "mturk_ans5")
	private String mturkAns5;
	
	@DynamoDBAttribute(attributeName = "mturk_question")
	private String mturkQuestion;
	
	@DynamoDBAttribute(attributeName = "Reward")
	private Double reward;
	
	@DynamoDBHashKey(attributeName="user_id")
	private String userId;
	
	@DynamoDBAttribute(attributeName = "user_name")
	private String userName;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public Integer getAssignmentDurationInSeconds() {
		return assignmentDurationInSeconds;
	}

	public void setAssignmentDurationInSeconds(Integer assignmentDurationInSeconds) {
		this.assignmentDurationInSeconds = assignmentDurationInSeconds;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}

	public Double getHitRate() {
		return hitRate;
	}

	public void setHitRate(Double hitRate) {
		this.hitRate = hitRate;
	}

	public String getHitLayoutId() {
		return hitLayoutId;
	}

	public void setHitLayoutId(String hitLayoutId) {
		this.hitLayoutId = hitLayoutId;
	}

	public String getHitTypeId() {
		return hitTypeId;
	}

	public void setHitTypeId(String hitTypeId) {
		this.hitTypeId = hitTypeId;
	}

	public Double getLifetimeInSeconds() {
		return lifetimeInSeconds;
	}

	public void setLifetimeInSeconds(Double lifetimeInSeconds) {
		this.lifetimeInSeconds = lifetimeInSeconds;
	}

	public Integer getMaxAssignments() {
		return maxAssignments;
	}

	public void setMaxAssignments(Integer maxAssignments) {
		this.maxAssignments = maxAssignments;
	}

	public Integer getMonitoringIntervalMinutes() {
		return monitoringIntervalMinutes;
	}

	public void setMonitoringIntervalMinutes(Integer monitoringIntervalMinutes) {
		this.monitoringIntervalMinutes = monitoringIntervalMinutes;
	}

	public String getMturkAns1() {
		return mturkAns1;
	}

	public void setMturkAns1(String mturkAns1) {
		this.mturkAns1 = mturkAns1;
	}

	public String getMturkAns2() {
		return mturkAns2;
	}

	public void setMturkAns2(String mturkAns2) {
		this.mturkAns2 = mturkAns2;
	}

	public String getMturkAns3() {
		return mturkAns3;
	}

	public void setMturkAns3(String mturkAns3) {
		this.mturkAns3 = mturkAns3;
	}

	public String getMturkAns4() {
		return mturkAns4;
	}

	public void setMturkAns4(String mturkAns4) {
		this.mturkAns4 = mturkAns4;
	}

	public String getMturkAns5() {
		return mturkAns5;
	}

	public void setMturkAns5(String mturkAns5) {
		this.mturkAns5 = mturkAns5;
	}

	public String getMturkQuestion() {
		return mturkQuestion;
	}

	public void setMturkQuestion(String mturkQuestion) {
		this.mturkQuestion = mturkQuestion;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "CvUser [alertMessage=" + alertMessage + ", assignmentDurationInSeconds=" + assignmentDurationInSeconds
				+ ", clientAddress=" + clientAddress + ", clientEmail=" + clientEmail + ", contactNumber="
				+ contactNumber + ", description=" + description + ", deviceMacAddress=" + deviceMacAddress
				+ ", hitRate=" + hitRate + ", hitLayoutId=" + hitLayoutId + ", hitTypeId=" + hitTypeId
				+ ", lifetimeInSeconds=" + lifetimeInSeconds + ", maxAssignments=" + maxAssignments
				+ ", monitoringIntervalMinutes=" + monitoringIntervalMinutes + ", mturkAns1=" + mturkAns1
				+ ", mturkAns2=" + mturkAns2 + ", mturkAns3=" + mturkAns3 + ", mturkAns4=" + mturkAns4 + ", mturkAns5="
				+ mturkAns5 + ", mturkQuestion=" + mturkQuestion + ", reward=" + reward + ", userId=" + userId
				+ ", userName=" + userName + "]";
	}
	
	
}
