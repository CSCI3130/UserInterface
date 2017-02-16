/**
 * UserConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

/**
 * This interface is to be used for any user data queries.
 *
 * It provides an abstraction of the database model and the
 * query structure to keep the code modular and decoupled.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
public interface UserConnector {

	/**
	 * insertUser method inserts a new user in the database.
	 *
	 * @param user the user to add to the database
	 * @param hash the hashed password to store with the user data
	 * @param salt the salt to store with the hashed password
	 * @return User object that was added
	 */
	public User insertUser(User user, String hash, String salt);

	/**
	 * deleteUser method deletes a given user object from the database.
	 *
	 * @param user the user to be deleted
	 * @return true on success
	 */
	public boolean deleteUser(User user);

	/**
	 * selectUser method selects a user from the database with the
	 * user handle.
	 *
	 * @param handle primary key for the User table
	 * @return User object selected from the database
	 */
	public User selectUser(String handle);

	/**
	 * updateUser method updates a user from the database given the User
	 * object.
	 *
	 * @param user User to update
	 * @return User object that was updated
	 */
	public User updateUser(User user);

	/**
	 * updateUser method updates a user from the database given the User
	 * object.
	 *
	 * @param user User to update
	 * @param hash
	 * @return User object that was updated
	 */
	public User updateUser(User user, String hash);
}
