/**
 * FeedbackService.java
 * Created Feb 12, 2017
 */
package com.piccritic.compute.feedback;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentConnector;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.JPACommentConnector;
import com.piccritic.database.feedback.JPAVoteConnector;
import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteConnector;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.post.JPAPostConnector;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostConnector;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;

/**
 * Create User form
 * 
 * @author Amelia Stead<br>
 *         Jonathan Ignacio
 */
public class FeedbackService {
	private static FeedbackService instance;
  	private static CommentConnector cc;
  	private static PostConnector pc;
  	private static VoteConnector vc;
  	
  	private FeedbackService() {
      	cc = new JPACommentConnector();
      	pc = new JPAPostConnector();
      	vc = new JPAVoteConnector();
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
	 * @param post - post to average the ratings on
	 * @return An ArrayList of integers
	 */
	public Double[] getAvgRatings(Post post) {
		return null;
	}
	
//	/**
//	 * Method to calculate total comment score for a user (across all of their comments)
//	 * @param critic - User to evaluate total comment score for
//	 * @return total comment score
//	 */
//	public int getCommentScore(Critic critic){
//		int total;
//		List<Comment> comments = cc.getComments(critic);
//		for(Comment comment : comments){
//			total+= comment.get
//		}
//		return 0;
//	}
	
	/**
	 * Method to calculate total comment score for a user (across all of their comments)
	 * @param critic - User to evaluate total comment score for
	 * @return total comment score
	 */
	public int getTotalCommentScore(Critic critic){
		return 0;
	}
	
	/**
	 * 
	 * @param critic
	 * @param comment
	 * @return Vote associated with critic and comment, null if doesn't exist
	 */
	public Vote getVote(Critic critic, Comment comment) {
		Long id = vc.getVoteId(critic, comment);
		if (id == null) {
			return null;
		} else {
			return vc.selectVote(id);
		}
	}
	
	/**
	 * Inserts a vote into the database. If this critic has already voted on the
	 * comment, then the vote is updated.
	 * @param vote
	 * @return Vote inserted
	 */
	public Vote insertVote(Vote vote) {
		Vote inserted = null;
		try {
			Long id = vc.getVoteId(vote.getCritic(), vote.getComment());
			if (id == null) {
				inserted = vc.insertVote(vote);
			} else {
				Vote v = vc.selectVote(id);
				v.setRating(vote.getRating());
				inserted = vc.updateVote(v);
			}
		} catch (VoteException e) {
			e.getLocalizedMessage();
		}
		return inserted;
	}
	
	/**
	 * Deletes the vote from the database
	 * @param vote
	 * @return true if successful
	 * @throws VoteException 
	 */
	public boolean deleteVote(Vote vote) throws VoteException {
		if (vote == null || vote.getCritic() == null || vote.getComment() == null) {
			return false;
		}
		Vote v = vc.selectVote(vc.getVoteId(vote.getCritic(), vote.getComment()));
		return vc.deleteVote(v);
	}
	
	/**
	 * 
	 * @param comment
	 * @return score of given comment
	 */
	//TODO: test
	public int getScore(Comment comment) {
		return vc.getScore(comment);
	}
}
