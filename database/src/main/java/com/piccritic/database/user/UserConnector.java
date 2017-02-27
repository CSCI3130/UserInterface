/**
 * UserConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

/**
 * This interface is to be used for any critic data queries.
 *
 * It provides an abstraction of the database model and the query structure to
 * keep the code modular and decoupled.
 *
 * @author Ryan Lowe<br>
 * 		Damien Robichaud
 */
public interface UserConnector {
	
	/**
	 * Inserts a new critic in the database.
	 *
	 * @param critic the critic to add to the database
	 * @param hash the hashed and salted password to store with the critic data
	 * @return Critic object that was added
	 */
	public Critic insertCritic(Critic critic, String hash) throws UserException;
	
	/**
	 * Deletes a given critic object from the database.
	 *
	 * @param critic the critic to be deleted
	 * @return true on success
	 */
	public boolean deleteCritic(Critic critic);
	
	/**
	 * Selects a critic from the database with the critic handle.
	 *
	 * @param handle primary key for the Critic table
	 * @return Critic object selected from the database
	 */
	public Critic selectCritic(String handle);
	
	/**
	 * updateUser method updates a critic from the database given the Critic object.
	 *
	 * @param critic Critic to update
	 * @return Critic object that was updated
	 * @throws UserException 
	 */
	public Critic updateCritic(Critic critic) throws UserException;
	
	/**
	 * Updates a critic from the database given the Critic object.
	 *
	 * @param critic Critic to update
	 * @param hash the critic's new hash
	 * @return Critic object that was updated
	 */
	public Critic updateCritic(Critic critic, String hash) throws UserException;
	
	/**
	 * Gets the hash from the database
	 * to process the information at login.
	 *
	 * @param handle String containing primary key
	 * @return The hash corresponding to the critic handle
	 */
	public String getUserHash(String handle);
}
