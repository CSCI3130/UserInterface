package com.piccritic.compute.feedback;

import java.util.List;

import com.piccritic.database.feedback.Comment;
import com.piccritic.database.feedback.CommentException;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.feedback.RatingException;
import com.piccritic.database.feedback.Vote;
import com.piccritic.database.feedback.VoteException;
import com.piccritic.database.post.Post;
import com.piccritic.database.post.PostException;
import com.piccritic.database.user.Critic;

public interface FeedbackServiceInterface {

	int getUpperScoreLimit();

	int getLowerScoreLimit();

	/**
	 * Returns reputation gain
	 * @return the current reputation gain for positive contributions.
	 */
	int getRepGain();

	/**
	 * Gets rep loss
	 * @return the current reputation loss for negative contributions.
	 */
	int getRepLoss();
	
	/**
	 * 
	 * @return multiplier for how much more rep users gain for post ratings.
	 */
	int getRatingWeight();

	/**
	 * Gets all comments associated with the post
	 * @param post
	 * @return An ArrayList of comments for the given post
	 * @throws PostException 
	 */
	List<Comment> getComments(Post post) throws PostException;
	
	/**
	 * Adds a comment to the database
	 * @param comment
	 * @return The comment inserted
	 */
	Comment insertComment(Comment comment) throws CommentException;

	/**
	 * Deletes a comment from the database
	 * @param comment - comment to delete from the database
	 * @return true or false whether or not the comment was deleted
	 */
	boolean deleteComment(Comment comment) throws CommentException;

	/**
	 * Gets the average ratings for the given post.
	 * @param post - post to average the ratings on
	 * @return An ArrayList of integers
	 */
	Rating getAvgRatings(Post post);

	/**
	 * Returns vote
	 * @param critic
	 * @param comment
	 * @return Vote associated with critic and comment, null if doesn't exist
	 */
	Vote getVote(Critic critic, Comment comment);

	/**
	 * Inserts a vote into the database. If this critic has already voted on the
	 * comment, then the vote is updated.
	 * @param vote
	 * @return Vote inserted
	 */
	Vote insertVote(Vote vote);

	/**
	 * Deletes the vote from the database
	 * @param vote
	 * @return true if successful
	 * @throws VoteException 
	 */
	boolean deleteVote(Vote vote) throws VoteException;

	/**
	 * Returns score
	 * @param comment
	 * @return score of given comment
	 */
	int getScore(Comment comment);

	/**
	 * Method to calculate total reputation given by comments for a user 
	 * (across all of their comments)
	 * @param critic - User to evaluate total comment score for
	 * @return total comment score
	 */
	int getCriticCommentReputation(Critic critic);
	
	/**
	 * Method to calculate total reputation given by ratings on posts for a user 
	 * (across all of their posts)
	 * @param critic - User to evaluate total post rating for
	 * @return total comment score
	 */
	public int getCriticPostReputation(Critic critic);
	
	/**
	 * 
	 * @param critic - critic to calculate the total reputation for
	 * @return the total calculated reputation for a user.
	 */
	public long calculateReputation(Critic critic);
	
	Rating insertRating(Rating rating) throws RatingException;

	boolean deleteRating(Rating rating);
	
	Rating updateRating(Rating rating) throws RatingException;

	Rating selectRating(Long id);


}