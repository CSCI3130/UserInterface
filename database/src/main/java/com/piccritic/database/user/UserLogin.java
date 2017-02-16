/**
 * UserLogin.java
 * Created Feb 16, 2017
 */
package com.piccritic.database.user;

import java.io.Serializable;

/**
 * This class is a Java Bean that holds login information.
 *
 * Used to store the result of a query from the database.
 * @see UserConnector
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class UserLogin implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 2L;
	
	private String handle;
	private String hash;
	private String salt;
	
	/**
     * @return the handle of this user
     */
  	public String getHandle() {
		return handle;
	}
	
  	/**
     * @param handle the handle to set
     */
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
  	/**
     * @return the hash of this user
     */
	public String getHash() {
		return hash;
	}
  
	/**
     * @param hash the hash to set
     */
	public void setHash(String hash) {
		this.hash = hash;
	}
	
  	/**
     * @return the salt of this user
     */
	public String getSalt() {
		return salt;
	}
	
  	/**
     * @param salt the salt to set
     */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
  	/**
     * Gets a formatted string of the data.
     *
     * @return formatted UserLogin String
     */
	public String toString() {
		return String.format("UserLogin{handle=%s, hash=%s, salt=%s}", handle, hash, salt);
	}
	
  	/**
     * Checks that both objects have identical field values.
     *
     * @param o object to compare to this
     * @return true if both UserLogin objects have identical data
     */
	public boolean equals(Object o) {
		if (o != null && o instanceof UserLogin) {
			UserLogin u = (UserLogin) o;
			return handle.equals(u.getHandle()) && hash.equals(u.getHash()) && salt.equals(u.getSalt());
		}
		return false;
	}
	
}
