package com.piccritic.compute;

import com.piccritic.database.feedback.CommentConnector;
import com.piccritic.database.feedback.JPACommentConnector;
import com.piccritic.database.feedback.JPARatingConnector;
import com.piccritic.database.feedback.JPAVoteConnector;
import com.piccritic.database.feedback.RatingConnector;
import com.piccritic.database.feedback.VoteConnector;
import com.piccritic.database.license.JPALicenseConnector;
import com.piccritic.database.license.LicenseConnector;
import com.piccritic.database.post.AlbumConnector;
import com.piccritic.database.post.JPAAlbumConnector;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.tag.JPATagConnector;
import com.piccritic.database.tag.TagConnector;
import com.piccritic.database.user.JPAUserConnector;
import com.piccritic.database.user.UserConnector;

public class MasterConnector {

	public static PostConnector postConnector;
	public static AlbumConnector albumConnector;
	public static UserConnector userConnector;
	public static LicenseConnector licenseConnector;
	public static CommentConnector commentConnector;
	public static VoteConnector voteConnector;
	public static RatingConnector ratingConnector;
	public static TagConnector tagConnector;
	
	public static void init() {
		if (postConnector == null) postConnector = new JPAPostConnector();
		if (albumConnector == null) albumConnector = new JPAAlbumConnector();
		if (userConnector == null) userConnector = new JPAUserConnector();
		if (licenseConnector == null) licenseConnector = new JPALicenseConnector();
		if (commentConnector == null) commentConnector = new JPACommentConnector();
		if (voteConnector == null) voteConnector = new JPAVoteConnector();
		if (ratingConnector == null) ratingConnector = new JPARatingConnector();
		if (tagConnector == null) tagConnector = new JPATagConnector();
	}
}
