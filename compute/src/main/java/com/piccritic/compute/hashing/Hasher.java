/**
 * Hasher.java
 * Created Feb 21, 2017
 */
package com.piccritic.compute.hashing;

/**
 * This class is for hashing user passwords.
 * 
 * @author ian-dawson <br>
 * 		ajsteadly
 */

import java.sql.SQLException;

import com.piccritic.database.user.*;

public class Hasher implements HashInterface {
	
	private UserConnector con;
	
	/**
	 * Initializes a new Hasher
	 * 
	 * @param con Connection to user information from database
	 * @throws SQLException 
	 */
	public Hasher() throws SQLException {
		UserConnector con = new DBUserConnector();
		this.con = con;
	}
	
	/**
	 * Generates the hash of password
	 * 
	 * @param password
	 * @return hash of password
	 */
	public String generateHash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}
	
	/**
	 * Verifies login information
	 * 
	 * @param handle User-input handle
	 * @param password User-input password
	 * @return true if information correct, false otherwise
	 */
	public boolean checkLogin(String handle, String password) {
		UserLogin userLogin = con.getUserLogin(handle);
		return BCrypt.checkpw(password, userLogin.getHash());
	}
}
