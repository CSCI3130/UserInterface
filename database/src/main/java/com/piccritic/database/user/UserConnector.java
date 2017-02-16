/**
 * UserConnector.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public interface UserConnector {
	
	/**
	 * 
	 * @param user the user to add to the database
	 * @param password the hashed password to store with the user data
	 * @param salt the salt to store with the hashed password
	 * @return true on success
	 */
	public User insertUser(User user, String hash, String salt);
	
	public boolean deleteUser(User user);
	
	public User selectUser(String handle);
	
	public User updateUser(User user, String hash, String salt);
}
