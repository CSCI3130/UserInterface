/**
 * Album.java
 * Created Feb 22, 2017
 */
package com.piccritic.database.post;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.piccritic.database.user.Critic;

/**
 * This class is a Java bean that holds data corresponding to an album entity
 * in the database. Each album is owned by a {@link Critic}
 * and can hold many {@link Post} objects.
 * 
 * @author Ryan Lowe<br>Jonathan Ignacio
 */
@Entity
public class Album {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Date creationDate;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	private Critic critic;
	
	@OneToMany(mappedBy="album", fetch=FetchType.EAGER)
	private Set<Post> posts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Critic getCritic() {
		return critic;
	}

	public void setCritic(Critic critic) {
		this.critic = critic;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
	
	public String toString() {
		return String.format("Album{ id=%d, name=%s, creationDate=%s, critic.handle=%s }", id, name, creationDate,
				(critic == null) ? null : critic.getHandle());
	}
	
	/**
	 * Compares this Album to another object and returns true if they are equal.
	 * 
	 * @param o the object to compare
	 * @return true if the object is an identical Album to this one
	 */
	public boolean equals(Object o) {
		if (o != null && o instanceof Album) {
			Album a = (Album) o;
			return this.toString().equals(a.toString());
		}
		return false;
	}
	
}
