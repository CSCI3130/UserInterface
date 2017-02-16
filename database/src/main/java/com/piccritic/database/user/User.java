/**
 * User.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import java.io.Serializable;
import java.sql.Date;

/**
 * User class is a User Java Bean for the piccritic project.
 *
 * The bean holds the information needed to store a user in the database
 * according to the User table. The handle field is the primary key.
 * The getter and setters do not directly update the database. The
 * UserConnector has to be used to handle queries.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
public class User implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private String handle;
	private String firstName;
	private String lastName;
	private Date joinDate;
	private int licenseID;
	private String bio;

	/**
	 * @return the handle of this User
	 */
	public String getHandle() {
		return handle;
	}

	/**
	 * @param handle the handle to set
	 */
	public void setHandle(String username) {
		this.handle = username;
	}

	/**
	 * @return the firstName of this User
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName of this User
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the joinDate of this User
	 */
	public Date getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the joinDate to set
	 */
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the licenseID of this User
	 */
	public int getLicenseID() {
		return licenseID;
	}

	/**
	 * @param licenseID the licenseID to set
	 */
	public void setLicenseID(int licenseID) {
		this.licenseID = licenseID;
	}

	/**
	 * @return the bio of this User
	 */
	public String getBio() {
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * Custom equals method, uses toString value
	 *
	 * @param o Object to compare
	 */
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof User) {
			User u = (User) o;
			return this.toString().equals(u.toString());
		}
		return false;
	}

	/**
	 * @return string representing bean value
	 */
	public String toString() {
		return "User{" + "handle=" + handle + ", firstName=" + firstName
			+ ", lastName=" + lastName + ", joinDate=" + joinDate.toString()
 			+ ", licenseID=" + licenseID + ", bio=" + bio + "}";
	}

}
