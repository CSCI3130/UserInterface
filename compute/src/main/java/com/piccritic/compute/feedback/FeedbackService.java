package com.piccritic.compute.feedback;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentConnector;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.JPACommentConnector;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.post.PostException;

public class FeedbackService {
	private static FeedbackService instance;
  	private static CommentConnector cc;
  	private static PostConnector pc;
  	
  	private FeedbackService() {
      	cc = new JPACommentConnector();
      	pc = new JPAPostConnector();
    }
  
  	public static FeedbackService createService() {
      	if (instance == null) {
			final FeedbackService service = new FeedbackService();
          	instance = service;
        }
      	
      	return instance;
    }
  	
  	/**
	 * Gets all comments associated with the post
	 * @param post
	 * @return An ArrayList of comments for the given post
  	 * @throws PostException 
	 */
	public List<Comment> getComments(Post post) throws PostException {
		//return pc.selectPost(post.getPath()).getComments();
		return cc.getComments(post);
	}

	/**
	 * Adds a comment to the database
	 * @param comment
	 * @return The comment inserted
	 */
	public Comment insertComment(Comment comment) throws CommentException {
		if (comment.getCreationDate() == null) {
			comment.setCreationDate( new Date(Calendar.getInstance().getTime().getTime()));
		}	
		Comment inserted = cc.insertComment(comment);
		if (inserted == null) {
			throw new CommentException("Error inserting comment.");
		}
		return inserted;
	}
	
	/**
	 * Gets the average ratings for the given post.
	 * Indices:
	 * 0: LIGHTING
	 * 1: EXPOSURE
	 * 2: COMPOSURE
	 * 3: FOCUS
	 * 4: COLOR
	 * 
	 * @param post
	 * @return An ArrayList of integers
	 */
	public Double[] getAvgRatings(Post post) {
		return null;
	}
}
