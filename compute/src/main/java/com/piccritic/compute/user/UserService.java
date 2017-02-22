/**
 * JPAUserConnector.java
 * Created Feb 16, 2017
 */
package com.piccritic.compute.user;

import com.piccritic.database.user.*;

/**
 * 
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class UserService {
  	private static UserService instance;
  	private static UserConnector connector;
  	// private static Hasher hasher;
    private static String createSuccess = "Profile successfully created!";
    private static String updateSuccess = "Profile successfully updated!";
    private static String createFailure = "Profile could not be created.";
    private static String updateFailure = "Profile could not be updated.";
    private static String passwordTooShort = "Password too short; must be at least 10 characters.";
    private static String handleInUse = "Handle already in use.";
  	
  	private UserService() {
      	connector = new JPAUserConnector();
      	// hasher = new Hasher();
    }
  
  	public static UserService createService() {
      	if (instance == null) {
			final UserService service = new UserService();
          	instance = service;
        }
      	
      	return instance;
    }

  	public String create(Critic critic, String password) {
      	Critic selected = connector.selectCritic(critic.getHandle());
      	if (selected != null) {
          	return handleInUse;
        }

      	//TODO implement hashing
      	String salt = "abcdef";
      	String hash = password+salt;
      	Critic inserted = connector.insertCritic(critic, hash);
      
      	if (inserted == null) {
          	return createFailure;
        }
      
      	return createSuccess;
    }
  
  	public String update(Critic critic, String password) {
      	Critic selected = connector.selectCritic(critic.getHandle());
      	if (selected == null) {
        	return updateFailure;
        }
      
      	Critic updated;
      	if (password.isEmpty()) {
          	updated = connector.updateCritic(critic);
          	if (updated == null) {
              	return updateFailure;
            }
          	return updateSuccess;
        }
      
      	String hash = connector.getUserHash(critic.getHandle());
      	if (hash == null) {
        	return updateFailure;
        }
      
      	return updateSuccess;
    }
}
