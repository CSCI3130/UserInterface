/**
 * LoginInterface.java
 * Created Feb 19, 2017
 */
package com.piccritic.compute;

/**
 * This interface is to be used to submit the user login information and check login status.
 * 
 * This interface should be used by the UI.
 * 
 * @author ian-dawson <br>
 * 		ajsteadly
 *
 */

public interface LoginInterface {
	
	public enum LoginStatus {
		LOGGED_IN, LOGGED_OUT, INVALID_INFO, SESSION_TIMEOUT
	}
	
	/**
	 * Attempts to login user with submitted information, returns status
	 * of login attempt.
	 * 
	 * @param handle User submitted handle
	 * @param password User submitted password
	 * @return LoginStatus for the user's session
	 */
	public LoginStatus loginUser(String handle, String password);
	
	/**
	 * Gets the LoginStatus of the user
	 * 
	 * @return LoginStatus
	 */
	public LoginStatus getLoginStatus();
	
	/**
	 * Logs out the user. 
	 * 
	 * @return LoginStatus is LOGGED_OUT if the log out is successful.
	 */
	public LoginStatus logoutUser();
	
	/**
	 * Generates the hash and the salt
	 * @return an String[] containing the hash and the salt
	 * String[0] is the hash and String[1] is the salt
	 */
	public String[] generateHashSalt(String handle, String password);
}
