/**
 * HashInterface.java
 * Created Feb 19, 2017
 */
package com.piccritic.compute.hashing;

/**
 * This interface is for hashing user passwords.
 * 
 * @author ian-dawson <br>
 * 		ajsteadly
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
