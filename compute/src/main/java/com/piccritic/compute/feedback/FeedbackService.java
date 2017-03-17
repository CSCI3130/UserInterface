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
  	
  	//TODO possibly change these constants to an enum
  	final private static int upperScoreLimit = 2;
  	final private static int lowerScoreLimit = -5;
  	final private static int repGain = 1;
  	final private static int repLoss = -1;
  	
  	private FeedbackService() {
      	cc = new JPACommentConnector();
      	pc = new JPAPostConnector();
      	vc = new JPAVoteConnector();
      	//rc = new JPARatingConnector();
    }
  	
  	public int getUpperScoreLimit() {
  		return upperScoreLimit;
  	}
  	
  	public int  getLowerScoreLimit() {
  		return lowerScoreLimit;
  	}
  	
  	/**
  	 * 
  	 * @return the current reputation gain for positive contributions.
  	 */
  	public int getRepGain() {
  		return repGain;
  	}
  	
  	/**
  	 * 
  	 * @return the current reputation loss for negative contributions.
  	 */
  	public int getRepLoss() {
  		return repLoss;
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
	 * Deletes a comment from the database
	 * @param comment - comment to delete from the database
	 * @return true or false whether or not the comment was deleted
	 */
	public boolean deleteComment(Comment comment) throws CommentException {
		return cc.deleteComment(comment);
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
	public int getScore(Comment comment) {
		return vc.getScore(comment);
	}
	
	/**
	 * Method to calculate total reputation given by comments for a user 
	 * (across all of their comments)
	 * @param critic - User to evaluate total comment score for
	 * @return total comment score
	 */
	public int getCriticCommentReputation(Critic critic){
		int total = 0;
		List<Comment> comments = cc.getComments(critic);
		for(Comment comment : comments){
			int score = getScore(comment);
			if(score >= upperScoreLimit){
				total += repGain;
			}
			else if(score <= lowerScoreLimit){
				total += repLoss;
			}
		}
		return total;
	}
	
//	/**
//	 * 
//	 * @param critic - critic to calculate the total reputation for
//	 * @return the total calculated reputation for a user.
//	 */
//	public long calculateReputation(Critic critic) {
//		int commentRep = getCriticCommentReputation(critic);
//		
//		
//		return 0; 
//	}
	
}
