package com.piccritic.compute;

import com.piccritic.compute.feedback.FeedbackService;
import com.piccritic.compute.feedback.FeedbackServiceInterface;
import com.piccritic.compute.license.LicenseService;
import com.piccritic.compute.license.LicenseServiceInterface;
import com.piccritic.compute.post.PostService;
import com.piccritic.compute.post.PostServiceInterface;
import com.piccritic.compute.user.UserService;
import com.piccritic.compute.user.UserServiceInterface;

public class MasterService {

	public static FeedbackServiceInterface feedbackService;
	public static UserServiceInterface userService;
	public static LicenseServiceInterface licenseService;
	public static PostServiceInterface postService;
	
	public static void init() {
		if (feedbackService == null) feedbackService = FeedbackService.createService();
		if (userService == null) userService = UserService.createService();
		if (licenseService == null) licenseService = new LicenseService();
		if (postService == null) postService = new PostService();
	}
}
