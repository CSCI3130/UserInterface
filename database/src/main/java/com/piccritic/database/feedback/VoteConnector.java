/**
 * VoteConnector.java
 * Created Mar 12, 2017
 */

package com.piccritic.database.feedback;

import com.piccritic.database.user.Critic;

/**
 * Interface to provide methods that read to and write from the database
 * with regard to {@link Vote} objects.
 * 
 * @author Jonathan Ignacio <br> Frank Bosse
 */
public interface VoteConnector {
	/**
	 * 
	 * @param id the ID of the vote to get
	 * @return the vote with the matching ID
	 */
	public Vote selectVote(Long id);
	
	/**
	 * Gets the vote id for the vote on comment by critic
	 * @param critic
	 * @param comment
	 * @return Long voteId or null if not exists
	 */
	public Long getVoteId(Critic critic, Comment comment);
	
	/**
	 * 
	 * @param vote the vote to insert
	 * @return the same vote with an auto-generated ID
	 */
	public Vote insertVote(Vote vote) throws VoteException;
	
	/**
	 * 
	 * @param vote the vote to update
	 * @return the updated vote
	 */
	public Vote updateVote(Vote vote) throws VoteException;
	
	/**
	 * 
	 * @param vote the vote to delete
	 * @return true on successful deletion
	 */
	public boolean deleteVote(Vote vote);
}
