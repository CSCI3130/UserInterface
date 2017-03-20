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
import com.piccritic.database.feedback.JPARatingConnector;
import com.piccritic.database.feedback.JPAVoteConnector;
import com.piccritic.database.feedback.Rating;
import com.piccritic.database.feedback.RatingConnector;
import com.piccritic.database.feedback.RatingException;
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
public class FeedbackService implements FeedbackServiceInterface {
	private static FeedbackServiceInterface instance;
  	private static CommentConnector cc;
  	private static VoteConnector vc;
  	private static RatingConnector rc;
  	private static PostConnector pc;
  	
  	final private static int upperScoreLimit = 2;
  	final private static int lowerScoreLimit = -5;
  	final private static int ratingWeight = 2; 
  	final private static int repGain = 1;
  	final private static int repLoss = -1;
  	
  	private FeedbackService() {
      	cc = new JPACommentConnector();
      	vc = new JPAVoteConnector();
      	rc = new JPARatingConnector();
      	pc = new JPAPostConnector();
    }
  	
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getUpperScoreLimit()
	 */
  	@Override
	public int getUpperScoreLimit() {
  		return upperScoreLimit;
  	}
  	
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getLowerScoreLimit()
	 */
  	@Override
	public int  getLowerScoreLimit() {
  		return lowerScoreLimit;
  	}
  	
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getRepGain()
	 */
  	@Override
	public int getRepGain() {
  		return repGain;
  	}
  	
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getRepLoss()
	 */
  	@Override
	public int getRepLoss() {
  		return repLoss;
  	}
  	
  	/*
  	 * (non-Javadoc)
  	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getRatingWeight()
  	 */
	public int getRatingWeight(){
		return ratingWeight;
	}
  
  	public static FeedbackServiceInterface createService() {
      	if (instance == null) {
			final FeedbackServiceInterface service = new FeedbackService();
          	instance = service;
        }
      	
      	return instance;
    }
  	
  	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getComments(com.piccritic.database.post.Post)
	 */
	@Override
	public List<Comment> getComments(Post post) throws PostException {
		//return pc.selectPost(post.getPath()).getComments();
		return cc.getComments(post);
	}

	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#insertComment(com.piccritic.database.feedback.Comment)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#deleteComment(com.piccritic.database.feedback.Comment)
	 */
	@Override
	public boolean deleteComment(Comment comment) throws CommentException {
		return cc.deleteComment(comment);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getAvgRatings(com.piccritic.database.post.Post)
	 */
	@Override
	public Rating getAvgRatings(Post post) {
		Rating avg = new Rating();
		double focus = 0d;
		double color = 0d;
		double exposure = 0d;
		double lighting = 0d;
		double composition = 0d;
		
		for (Rating r : post.getRatings()) {
			focus += r.getFocus();
			color += r.getColor();
			exposure += r.getExposure();
			lighting += r.getLighting();
			composition += r.getComposition();
		}
		
		int size = post.getRatings().size();

		avg.setFocus(focus/size);
		avg.setColor(color/size);
		avg.setExposure(exposure/size);
		avg.setLighting(lighting/size);
		avg.setComposition(composition/size);
		
		return avg;
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getVote(com.piccritic.database.user.Critic, com.piccritic.database.feedback.Comment)
	 */
	@Override
	public Vote getVote(Critic critic, Comment comment) {
		Long id = vc.getVoteId(critic, comment);
		if (id == null) {
			return null;
		} else {
			return vc.selectVote(id);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#insertVote(com.piccritic.database.feedback.Vote)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#deleteVote(com.piccritic.database.feedback.Vote)
	 */
	@Override
	public boolean deleteVote(Vote vote) throws VoteException {
		if (vote == null || vote.getCritic() == null || vote.getComment() == null) {
			return false;
		}
		Vote v = vc.selectVote(vc.getVoteId(vote.getCritic(), vote.getComment()));
		return vc.deleteVote(v);
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getScore(com.piccritic.database.feedback.Comment)
	 */
	@Override
	public int getScore(Comment comment) {
		return vc.getScore(comment);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getCriticCommentReputation(com.piccritic.database.user.Critic)
	 */
	@Override
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
	
	/*
	 * (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#getCriticPostReputation(com.piccritic.database.user.Critic)
	 */
	public int getCriticPostReputation(Critic critic) {
		int total = 0;
		//TODO unsure if the following line has consequences, but it seemed necessary. maybe add a fs.refresh method?
		pc = new JPAPostConnector(); //refreshes DB connection
		List<Post> posts = pc.getPosts(critic);
		for(Post post : posts) {
			Rating avg = getAvgRatings(post);
			if(avg.getAverage() >= 2.5){
				total += repGain;
			}
			else if(avg.getAverage() < 2.5){
				total += repLoss;
			}
		}
		return total;
	}
	
	/* (non-Javadoc)
	 * @see com.piccritic.compute.feedback.FeedbackServiceInterface#calculateReputation(com.piccritic.database.user.Critic)
	 */
	public long calculateReputation(Critic critic) {
		int commentRep = getCriticCommentReputation(critic);
		int ratingRep = getCriticPostReputation(critic);
		int totalRep = commentRep + ratingRep*ratingWeight;
		
		return totalRep; 
	}

	@Override
	public Rating insertRating(Rating rating) throws RatingException {
		return rc.insertRating(rating);
	}

	@Override
	public boolean deleteRating(Rating rating) {
		if( rating == null | rating.getCritic() == null | rating.getPost() == null) {
			return false;
		}
		Rating tmp = rc.selectRating(rating.getId());
		return rc.deleteRating(tmp);
	}

	@Override
	public Rating updateRating(Rating rating) throws RatingException {
		/* rc.updateRating(rating);
		return selectRating(rating.getId());*/
		deleteRating(rating);
		rating.setId(null);
		return insertRating(rating);
	}

	@Override
	public Rating selectRating(Long id) {
		return rc.selectRating(id);
	}
}
