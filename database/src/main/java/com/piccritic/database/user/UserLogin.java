/**
 * UserLogin.java
 * Created Feb 16, 2017
 */
package com.piccritic.database.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class is a Java Bean that keeps critic login data
 * separate from the rest of the critic data.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
@Entity
public class UserLogin {
	@Id
	@Size(min=5,max=15)
	private String handle;
	@NotNull
	private String hash;
	
	/**
     * @return the handle of this critic
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
     * @return the hash of this critic
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
     * Gets a formatted string of the data.
     *
     * @return formatted UserLogin String
     */
	public String toString() {
		return String.format("UserLogin{handle=%s, hash=%s}", handle, hash);
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
			return handle.equals(u.getHandle()) && hash.equals(u.getHash());
		}
		return false;
	}
	
}
