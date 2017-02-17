package com.premiumgeek.mturk;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.mturk.requester.HIT;

public class TestMturkHitGenerator {
	
	@Test
	public void retriveCvEntry() {
		MturkHitGenerator mturk = new MturkHitGenerator();
		CvUser cvUser = mturk.loadCvUserFromTable("fadarnell@gmail.com","12345a");
		assertNotNull(cvUser);
		System.out.println(cvUser.toString());
	}
	
	@Test
	public void testCreateHit() {
 		MturkHitGenerator mturk = new MturkHitGenerator();
		MyHIT myHit = new MyHIT();
		CvUser cvUser = mturk.loadCvUserFromTable("fadarnell@gmail.com","12345a");

        HIT hit = myHit.createHitUsingLayout("www.s3.com/myobject",cvUser);
        assertNotNull(hit);
        assertNotNull(hit.getHITId());
	}
	
	@Test
	public void testCreateHit2() {
 		MturkHitGenerator mturk = new MturkHitGenerator();
		MyHIT myHit = new MyHIT();
		CvUser cvUser = mturk.loadCvUserFromTable("fadarnell@gmail.com","12345a");

        HIT hit = myHit.createHitUsingLayout("www.s3.com/myobject",cvUser);
        assertNotNull(hit);
        System.out.println("Hit id " + hit.getHITId());
        assertNotNull(hit.getHITId());
	}
	
	@Test
	public void makeS3objectPublic() {
 		MturkHitGenerator mturk = new MturkHitGenerator();
        String s3FileUrl= mturk.makeS3ObjectPublic("voicechecklist-temp", "023a12fa-56d0-4f1e-9e1a-968d114e3315.mp4_aa4d2253-5a5a-438f-a976-8f7c5bf9175e");
        System.out.println(s3FileUrl);
	}
	
/*  Fix the test and uncomment
	@Test
	public void insertToCvLog() {
 		MturkHitGenerator mturk = new MturkHitGenerator();
        mturk.insertToCvLog("www.s3url.com", "7272827");		
	}
*/
	@Test
	public void testEndToEnd() {
 		MturkHitGenerator mturk = new MturkHitGenerator();
 		
 		Map<String,String> envVars = new HashMap<String,String>();
 		envVars.put("NOTIFICATION_ENDPOINT","https://sqs.us-east-1.amazonaws.com/321327483606/crowdvision-mturk-notifications");
 		mturk.setEnvironmentVariables(envVars);
 		
        mturk.process("voicechecklist-temp", "023a12fa-56d0-4f1e-9e1a-968d114e3315.mp4_9f314e74-3785-4735-89f1-d2b761b4b07a");	
	}

	@Test
	public void testEndToEnd2() {
 		MturkHitGenerator mturk = new MturkHitGenerator();
 		
 		Map<String,String> envVars = new HashMap<String,String>();
 		envVars.put("NOTIFICATION_ENDPOINT","https://sqs.us-west-2.amazonaws.com/755956456552/mturk-queue-notification");
 		mturk.setEnvironmentVariables(envVars);
 		
        mturk.process("voicechecklist-temp", "023a12fa-56d0-4f1e-9e1a-968d114e3315.mp4_aa4d2253-5a5a-438f-a976-8f7c5bf9175e");	
	}
	
	@Test
	public void testExtractEmailAddress() {
		MturkHitGenerator mturk = new MturkHitGenerator();
 		String emailAddress = mturk.extractEmailAddress("Phani Susarla <phani.susarla@gmail.com>");
        assertEquals("phani.susarla@gmail.com", emailAddress);
	}

	@Test
	public void testExtractEmailAddress2() {
		MturkHitGenerator mturk = new MturkHitGenerator();
 		String emailAddress = mturk.extractEmailAddress("some junk");
        assertEquals("some junk", emailAddress);
	}

}
