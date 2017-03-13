/**
 * VoteConnector.java
 * Created Mar 12, 2017
 */

package com.piccritic.database.feedback;

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
