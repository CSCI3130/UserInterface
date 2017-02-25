/**
 * Critic.java
 * Created Feb 15, 2017
 */
package com.piccritic.database.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.piccritic.database.post.Album;

import java.sql.Date;
import java.util.Set;

/**
 * This class is a bean that holds the information needed to store a critic in the database
 * according to the Critic table. The handle field is the primary key.
 *
 * @author Ryan Lowe<br>Damien Robichaud
 */
@Entity
public class Critic {

	@Id
	@Size(min=5,max=15)
	private String handle;
	@Size(min=1,max=15)
	private String firstName;
	@Size(min=1,max=15)
	private String lastName;
	@NotNull
	private Date joinDate;
	private int licenseID;
	@Size(max=200)
	private String bio;
	
	@OneToMany(mappedBy="critic") 
	private Set<Album> albums; 

	/**
	 * @return the handle of this Critic
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
	 * @return the firstName of this Critic
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
	 * @return the lastName of this Critic
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
	 * @return the joinDate of this Critic
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
	 * @return the licenseID of this Critic
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
	 * @return the bio of this Critic
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
	 * @return the albums of this critic
	 */
	public Set<Album> getAlbums() {
		return albums;
	}

	/**
	 * @param albums the album set to assign
	 */
	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}

	/**
	 * Compares this Critic to another object.
	 * Returns true if both string representations are equal.
	 *
	 * @param o Object to compare
	 * @return true if both Users have identical data
	 */
	public boolean equals(Object o) {
		if (o != null && o instanceof Critic) {
			Critic u = (Critic) o;
			return this.toString().equals(u.toString());
		}
		return false;
	}

	/**
	 * @return string representing bean value
	 */
	public String toString() {
		return "Critic{" + "handle=" + handle + ", firstName=" + firstName
			+ ", lastName=" + lastName + ", joinDate=" + joinDate.toString()
 			+ ", licenseID=" + licenseID + ", bio=" + bio + "}";
	}

}
