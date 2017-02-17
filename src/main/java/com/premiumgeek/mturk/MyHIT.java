package com.premiumgeek.mturk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.amazonaws.mturk.requester.EventType;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.HITLayoutParameter;
import com.amazonaws.mturk.requester.NotificationSpecification;
import com.amazonaws.mturk.requester.NotificationTransport;
import com.amazonaws.mturk.requester.PolicyParameter;
import com.amazonaws.mturk.requester.ReviewPolicy;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.service.axis.RequesterServiceRaw;
import com.amazonaws.mturk.util.PropertiesClientConfig;

public class MyHIT {
	private RequesterService service;
	
	public MyHIT() {
		service = new RequesterService(new PropertiesClientConfig("mturk.properties"));
	}
	
	/**
	 * Check to see if your account has sufficient funds
	 * @return true if there are sufficient funds. False if not.
	 */
	public boolean hasEnoughFund() {
	    double balance = service.getAccountBalance();
	    System.out.println("Got account balance: " + RequesterService.formatCurrency(balance));
	    return balance > 0;
	}
  
	public HIT createHitUsingLayout(String s3FileURL, CvUser cvUser) {
		HIT hit =  null;
		try {
			//make sure that object is public so HIT can disply it properly
			//Loading the HIT properties file.  HITProperties is a helper class that contains the 
			//properties of the HIT defined in the external file.  This feature allows you to define
			//the HIT attributes externally as a file and be able to modify it without recompiling your code.
			//In this sample, the qualification is defined in the properties file.
		//	HITProperties props = new HITProperties(propertiesFile);
			Map<String, String> layoutParams = buildLayoutParams(s3FileURL, cvUser);
			
			PolicyParameter[] phrParams = {
			        new PolicyParameter("QuestionIds", new String[] {"1"}, null),
			        new PolicyParameter("QuestionAgreementThreshold", new String[] {"50"}, null),
			    };
			    ReviewPolicy hitReviewPolicy = new ReviewPolicy("SimplePlurality/2011-09-01", phrParams);

			    long defaultAssignmentDurationInSeconds = (long) 60 * 60;
			    long defaultAutoApprovalDelayInSeconds = (long)1;
			    long defaultLifetimeInSeconds = (long) 60 * 60 * 24 * 3;
			    
			    Set<HITLayoutParameter> parameterObjects = new HashSet<HITLayoutParameter>();
			    for (String key : layoutParams.keySet()) {
			      parameterObjects.add(new HITLayoutParameter(key, layoutParams.get(key)));
			    }
			    
			    hit = service.createHIT(
			            null, // hitTypeId
			            cvUser.getTitle(),
			            cvUser.getDescription(),
			            null, // keywords
			            cvUser.getReward(),
			            defaultAssignmentDurationInSeconds,
			            defaultAutoApprovalDelayInSeconds,
			            defaultLifetimeInSeconds,
			            cvUser.getMaxAssignments(),
			            null, // requesterAnnotation
			            null, // qualificationRequirements
			            null, // responseGroup
			            null, // uniqueRequestToken
			            null,
			            hitReviewPolicy,
			            cvUser.getHitLayoutId(), 

			            (HITLayoutParameter[]) parameterObjects.toArray(new HITLayoutParameter[0]));
			System.out.println(service.getWebsiteURL() + "/mturk/preview?groupId=" + hit.getHITTypeId());
		}
		catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
		return hit;
	}

	private Map<String, String> buildLayoutParams(String s3FileURL, CvUser cvUser) {
		Map<String,String> layoutParams = new HashMap<String,String>();
		layoutParams.put("mturk_ans1", cvUser.getMturkAns1());
		layoutParams.put("mturk_ans2", cvUser.getMturkAns2());
		layoutParams.put("mturk_ans3", cvUser.getMturkAns3());
		layoutParams.put("mturk_ans4", cvUser.getMturkAns4());
		layoutParams.put("mturk_ans5", cvUser.getMturkAns5());
		layoutParams.put("mturk_question", cvUser.getMturkQuestion());
		layoutParams.put("image_url", s3FileURL);		
		return layoutParams;
	}
	
	public void setHITupdateNotification(String endp, String hitTypeId) {
		EventType[] et = { EventType.HITReviewable};
		NotificationSpecification ns = new NotificationSpecification(
			endp,//"https://sqs.us-west-2.amazonaws.com/755956456552/mturk-queue-notification",
			NotificationTransport.SQS,
			RequesterServiceRaw.NOTIFICATION_VERSION, et);
		// active the notify object with spec HIT type id
		service.setHITTypeNotification(hitTypeId, ns, true);
		return;
	}
}
