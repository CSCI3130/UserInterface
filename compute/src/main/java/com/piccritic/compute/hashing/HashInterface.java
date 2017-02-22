/**
 * LoginInterface.java
 * Created Feb 19, 2017
 */
package com.piccritic.compute.hashing;

/**
 * This interface is to be used to submit the user login information and check login status.
 * 
 * This interface should be used by the UI.
 * 
 * @author ian-dawson <br>
 * 		ajsteadly
 *
 */

public interface HashInterface {
	
	/**
	 * Generates the hash
	 * 
	 * @return a String containing the hash
	 */
	public String generateHash(String password);
	
	/**
	 * Checks user-input login info for validity
	 * 
	 * @param handle User-input handle
	 * @param password User-input password
	 * @return True if login info valid
	 */
	public boolean checkLogin(String handle, String password);
}
