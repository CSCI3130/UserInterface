/**
 * Post.java
 * Created Feb 22, 2017
 */
package com.piccritic.database.post;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * this class is a Java bean that defines the database table
 * for the PicCritic project.
 *
 * It holds all the necessary fields for a post data object.
 * The primary key is the post's id (Long) defined by the database.
 * It is assigned to an {@link Album} object.
 *
 * @author Ryan Lowe<br>Johnathan Ignacio
 */
@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	@NotNull
	private String path;
	@NotNull
	private Date uploadDate;
	private String title;
	private String description;
	private float rating;

	@ManyToOne(optional=false)
	private Album album;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * @return the title of this Post
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String toString() {
		return String.format("Post{id=%d, path=%s, uploadDate=%s, title=%s, description=%s, rating=%f, album.id=%d}", 
				(id == null) ? null:id.longValue(), path, (uploadDate == null) ? null :uploadDate.toString(),
				title, description, rating, (album == null) ? null : album.getId());
	}
	
	/**
	 * Compares this Post to another object and returns true if they are equal.
	 * 
	 * @param o the object to compare
	 * @return true if the object is an identical Post to this one
	 */
	public boolean equals(Object o) {
		if (o != null && o instanceof Post) {
			Post p = (Post) o;
			return this.toString().equals(p.toString());
		}
		return false;
	}
	
	
}
